import fastifyStatic from '@fastify/static';
import { getCacheControlHeaderValueForStaticFiles } from './utils/CacheUtils';
import { healthController } from './healthController';
import { produktlistenController } from './produktlisteController';
import { toolController } from './toolController';
import { FastifyInstance } from 'fastify';
import { SsiHandlers } from './types';
import { DIST_PATH } from './utils/DistUtils';
import { createCacheControlHeader } from './utils/ServerUtils';
import fastifyMetrics from 'fastify-metrics';

export async function registerEndpoints(fastifyInstance: FastifyInstance): Promise<void> {
  await registerStaticEndpoints(fastifyInstance);
  registerMetricsEndpoint(fastifyInstance);
  registerSsiEndpoint(fastifyInstance, '/ssi/finden/produktliste', produktlistenController);
  fastifyInstance.get('/ssi/finden/mini-produktliste', produktlistenController.content);
  fastifyInstance.get('/health', healthController);
  fastifyInstance.get('/finden/tool', toolController);
  fastifyInstance.log.info('fastify routes registered');
}

async function registerStaticEndpoints(fastifyInstance: FastifyInstance): Promise<void> {
  await fastifyInstance.register(fastifyStatic, {
    root: DIST_PATH,
    prefix: '/finden/static',
    setHeaders: (response) =>
      response.setHeader(...createCacheControlHeader(getCacheControlHeaderValueForStaticFiles())),
  });
}

function registerSsiEndpoint(fastifyInstance: FastifyInstance, baseUrl: string, handlers: SsiHandlers): void {
  fastifyInstance.get(`${baseUrl}/scripts`, handlers.scripts);
  fastifyInstance.get(`${baseUrl}/styles`, handlers.styles);
  fastifyInstance.get(baseUrl, handlers.content);
}

function registerMetricsEndpoint(fastifyInstance: FastifyInstance): void {
  fastifyInstance.register(fastifyMetrics, {
    endpoint: '/metrics',
    routeMetrics: { registeredRoutesOnly: false },
  });
}
