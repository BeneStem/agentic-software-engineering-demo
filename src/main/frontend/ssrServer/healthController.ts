import type { FastifyReply, FastifyRequest } from 'fastify';

export const healthController = async (request: FastifyRequest, reply: FastifyReply) => {
  reply.send();
};
