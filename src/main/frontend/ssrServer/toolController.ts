import type { FastifyReply, FastifyRequest } from 'fastify';
import { loadClientManifest } from './utils/ManifestUtils';
import { createContentTypeHeader } from './utils/ServerUtils';

const appName = 'tool';
const clientManifest = loadClientManifest(appName);

export const toolController = async (request: FastifyRequest, reply: FastifyReply) => {
  const html = `
    <link rel='stylesheet' href='/finden/static/${appName}/client${clientManifest['app.css']}' />
    <div id='tool-produktliste'></div>
    <script type='text/javascript' src='/finden/static/${appName}/client${clientManifest['chunk-vendors.js']}'></script>
    <script type='text/javascript' src='/finden/static/${appName}/client${clientManifest['app.js']}'></script>
`;

  reply.header(...createContentTypeHeader('html'));
  reply.send(html);
};
