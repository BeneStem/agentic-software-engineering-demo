import type { FastifyInstance } from 'fastify';
import path from 'path';
import { DIST_PATH } from './DistUtils';
import fastifyEnv from '@fastify/env';

export const FASTIFY_SERVER_DEFAULT_PORT = 8080;

export async function registerEnvironment(fastifyInstance: FastifyInstance): Promise<void> {
  const DOT_ENV_PATH = path.join(DIST_PATH, '../.env.fastify');
  const schema = {
    type: 'object',
    required: ['FASTIFY_SERVER_PORT'],
    properties: {
      FASTIFY_SERVER_PORT: {
        type: 'number',
        default: FASTIFY_SERVER_DEFAULT_PORT,
      },
      IS_LOCAL_DEPLOYMENT: {
        type: 'string',
        default: 'false',
      },
      DEBUG_MEMORY_USAGE: {
        type: 'string',
        default: 'false',
      },
    },
  };
  await fastifyInstance.register(fastifyEnv, {
    schema,
    dotenv: {
      path: DOT_ENV_PATH,
    },
  });
}
