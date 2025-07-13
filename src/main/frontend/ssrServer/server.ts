import { createServer } from './serverApp';

async function start(): Promise<void> {
  const server = await createServer();
  await server.listen({ host: '0.0.0.0', port: server.config.FASTIFY_SERVER_PORT }, (error) => {
    if (error) {
      server.log.error(error);
      process.exit(1);
    }
  });
}

(async () => await start())();
