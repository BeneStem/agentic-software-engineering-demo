import { FastifyInstance } from 'fastify';

export function setErrorHandler(fastifyInstance: FastifyInstance) {
  fastifyInstance.setErrorHandler((error, request, reply) => {
    if (reply.raw.statusCode === 500) {
      fastifyInstance.log.error(error);
      reply.send(new Error('Oh no! Something went wrong!'));
    } else {
      fastifyInstance.log.error(error);
      reply.send(error);
    }
  });
  fastifyInstance.log.info('fastify error handler set');
}
