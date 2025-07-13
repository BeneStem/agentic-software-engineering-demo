import type { FastifyReply, FastifyRequest } from 'fastify';
import { App } from 'vue';

export type RenderSsrApp = (ssrContext: { url: string }) => Promise<{ app: App; state: unknown }>;

export interface SsiHandlers {
  styles(request: FastifyRequest, reply: FastifyReply): Promise<void>;

  content(request: FastifyRequest, reply: FastifyReply): Promise<void>;

  scripts(request: FastifyRequest, reply: FastifyReply): Promise<void>;
}

export type AppType = 'client' | 'ssr';

export type AppAsset = 'app.js' | 'app.css' | 'chunk-vendors.js' | 'chunk-vendors.css';

export type AppManifest = Record<AppAsset, string>;

declare module 'fastify' {
  export interface FastifyInstance {
    config: {
      FASTIFY_SERVER_PORT: number;
      IS_LOCAL_DEPLOYMENT: string;
      DEBUG_MEMORY_USAGE: string;
    };
  }
}

export type ContentTypeKey = 'Content-Type';
export type ContentType = 'html';
export type CacheControlKey = 'Cache-Control';

export function assertNever(value: never): never {
  throw new Error(`Unhandled discriminated union member: ${JSON.stringify(value)}`);
}
