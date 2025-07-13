import { FastifyInstance, FastifyReply, FastifyRequest } from 'fastify';
import { isServerSideIncludeUrl } from './UrlUtils';
import { getCacheControlHeaderValueForServices } from './CacheUtils';
import { assertNever, CacheControlKey, ContentType, ContentTypeKey } from '../types';

export function addCachingHeaderOnResponseHookIfRequired(request: FastifyRequest, reply: FastifyReply) {
  if (isServerSideIncludeUrl(request.url)) {
    reply.header(...createContentTypeHeader('html'));
    reply.header(...createCacheControlHeader(getCacheControlHeaderValueForServices()));
  }
}

export function addWarningWhenResponseNotHealthy(
  fastifyInstance: FastifyInstance,
  request: FastifyRequest,
  reply: FastifyReply
) {
  if (reply.statusCode && reply.statusCode !== 200) {
    fastifyInstance.log.warn(`Warning: Route="${request.url}" StatusCode="${reply.statusCode}"`);
  }
}

export function createCacheControlHeader(cacheControl: string): [CacheControlKey, string] {
  return ['Cache-Control', cacheControl];
}

export function createContentTypeHeader(type: ContentType): [ContentTypeKey, string] {
  switch (type) {
    case 'html':
      return ['Content-Type', 'text/html; charset=utf-8'];
  }
  assertNever(type);
}
