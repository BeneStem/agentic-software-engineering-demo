import type { FastifyReply, FastifyRequest } from 'fastify';
import { diag, DiagLogLevel } from '@opentelemetry/api';
import { MeterProvider } from '@opentelemetry/sdk-metrics-base';
import { MetricExporter } from '@google-cloud/opentelemetry-cloud-monitoring-exporter';
import { removeQueryParams } from '../utils/MonitoringUtils';
import { createLogger } from './logger';

// eslint-disable-next-line @typescript-eslint/ban-ts-comment
// @ts-ignore
diag.setLogger(createLogger(), DiagLogLevel.INFO);

const metricLabels: Record<string, string> = {
  application: process.env.APPLICATION || '',
  projectId: process.env.PROJECT_ID || '',
};

const responseTimes: Record<string, number[]> = {};

// 1.0 is not a valid percentile
const percentilesToMonitor = [0.5, 0.95, 0.99];

const calculatePercentiles = () => {
  return Object.keys(responseTimes).map((path) => {
    const responseTimesForMethod = responseTimes[path];
    responseTimesForMethod.push(0); // always have 0 as default in
    const sortedResponseTimes = responseTimesForMethod.sort((a, b) => a - b);

    return percentilesToMonitor.map((percentile) => {
      const cutoff = sortedResponseTimes.length * percentile;
      return {
        path,
        percentile,
        value: sortedResponseTimes[Math.floor(cutoff)],
      };
    });
  });
};

const responses: Record<string, Record<string, number>> = {};

export const initializeMonitoring = () => {
  const exporter = new MetricExporter({
    projectId: process.env.PROJECT_ID,
  });

  const meter = new MeterProvider({
    exporter,
    interval: 60000,
  }).getMeter('ssr-meter');

  meter.createObservableGauge('finden.ssr.responseTime', {}, (observerResult) => {
    calculatePercentiles().forEach((percentileInfos) =>
      percentileInfos.forEach((percentileInfo) => {
        observerResult.observe(percentileInfo.value, {
          ...metricLabels,
          path: percentileInfo.path,
          percentile: (percentileInfo.percentile * 100).toString(), // nicer to display on dashboard
        });
      })
    );

    Object.keys(responseTimes).forEach((path) => (responseTimes[path] = []));
  });

  meter.createObservableGauge('finden.ssr.responseCode', {}, (observerResult) => {
    Object.keys(responses).forEach((httpCode) =>
      Object.keys(responses[httpCode]).forEach((methodurl) => {
        observerResult.observe(responses[httpCode][methodurl], {
          ...metricLabels,
          status: httpCode,
          methodurl: removeQueryParams(methodurl),
        });
      })
    );

    Object.keys(responses).forEach((httpCode) =>
      Object.keys(responses[httpCode]).forEach((methodurl) => (responses[httpCode][methodurl] = 0))
    );
  });
};

export const monitoringOnResponseHook = (request: FastifyRequest, reply: FastifyReply) => {
  const methodUrl = request.method + ' ' + request.url;
  const statusCodeString = reply.statusCode.toString();
  if (!responses[statusCodeString]) {
    responses[statusCodeString] = {};
  }
  if (!responses[statusCodeString][methodUrl]) {
    responses[statusCodeString][methodUrl] = 0;
  }
  responses[statusCodeString][methodUrl]++;

  if (reply.statusCode < 300) {
    if (!responseTimes[`${request.routerMethod} ${request.routerPath}`]) {
      responseTimes[`${request.routerMethod} ${request.routerPath}`] = [];
    }
    responseTimes[`${request.routerMethod} ${request.routerPath}`].push(reply.getResponseTime());
  }
};
