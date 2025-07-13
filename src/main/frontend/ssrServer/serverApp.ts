import Fastify, { FastifyInstance } from 'fastify';
import { createLogger } from './operations/logger';
import { addHooks } from './hooks';
import { setErrorHandler } from './errorHandler';
import { registerEndpoints } from './registerEndpoints';
import { registerEnvironment } from './utils/EnvUtils';

export async function createServer(): Promise<FastifyInstance> {
  const fastify = Fastify({ logger: createLogger(), disableRequestLogging: true });
  await registerEnvironment(fastify);
  await registerEndpoints(fastify);
  addHooks(fastify);
  setErrorHandler(fastify);
  return fastify;
}
