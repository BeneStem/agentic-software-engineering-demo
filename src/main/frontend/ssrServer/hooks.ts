import { addCachingHeaderOnResponseHookIfRequired, addWarningWhenResponseNotHealthy } from './utils/ServerUtils';
import { initializeMonitoring, monitoringOnResponseHook } from './operations/monitoring';
import { FastifyInstance } from 'fastify';

export function addHooks(fastifyInstance: FastifyInstance): void {
  fastifyInstance.addHook('onSend', (request, reply, payload, done) => {
    addCachingHeaderOnResponseHookIfRequired(request, reply);
    done(null, payload);
  });

  if (fastifyInstance.config.IS_LOCAL_DEPLOYMENT !== 'true') {
    initializeMonitoring();
    fastifyInstance.addHook('onResponse', (request, reply) => {
      monitoringOnResponseHook(request, reply);

      addWarningWhenResponseNotHealthy(fastifyInstance, request, reply);
    });
  }

  if (fastifyInstance.config.DEBUG_MEMORY_USAGE === 'true') {
    debugMemory(fastifyInstance);
  }
}

function debugMemory(fastifyInstance: FastifyInstance): void {
  fastifyInstance.addHook('onSend', (request, reply, payload, done) => {
    const used = process.memoryUsage().heapUsed / 1024 / 1024;
    request.log.info(`The service uses approximately ${Math.round(used * 100) / 100} MB memory`);
    done();
  });
}
