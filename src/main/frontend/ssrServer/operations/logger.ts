// TODO use pino on client side as well https://github.com/pinojs/pino/blob/master/docs/browser.md
import pino from 'pino';
import type { LoggerOptions } from 'pino';

const PinoLevelToSeverityLookup: Record<string, string> = {
  default: 'DEFAULT',
  trace: 'DEBUG',
  debug: 'DEBUG',
  info: 'INFO',
  warn: 'WARNING',
  error: 'ERROR',
  fatal: 'CRITICAL',
};

const defaultPinoConf: LoggerOptions = {
  messageKey: 'message',
  mixin() {
    return {
      labels: {
        environment: process.env.ENVIRONMENT || 'local',
        team: process.env.B2K_TEAM_KEY || 'local',
        application: process.env.VUE_APPLICATION_NAME || 'local',
      },
    };
  },
  formatters: {
    level(label) {
      const severity = PinoLevelToSeverityLookup[label] || PinoLevelToSeverityLookup['default'];
      const level: Record<string, string> = {
        severity: severity,
      };
      if (severity === 'ERROR') {
        level['@type'] = 'type.googleapis.com/google.devtools.clouderrorreporting.v1beta1.ReportedErrorEvent';
      }
      return level;
    },
  },
};

export function createLogger(options?: LoggerOptions) {
  return pino(Object.assign({}, options, defaultPinoConf));
}
