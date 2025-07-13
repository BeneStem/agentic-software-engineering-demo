import type { FastifyReply, FastifyRequest } from 'fastify';
import { renderToString } from '@vue/server-renderer';
import type { SsiHandlers } from './types';
import { loadClientManifest, loadRenderSsrApp, loadSsrManifest } from './utils/ManifestUtils';
import { renderAppState } from './utils/AppUtils';
import { createCssLinkTag, createScriptTag } from './utils/ControllerUtils';

const APP_NAME = 'produktliste';
const STORE_KEY = 'PRODUKTLISTE'; // needs to be kept in sync manually with vuex store

const ssrManifest = loadSsrManifest(APP_NAME);
const clientManifest = loadClientManifest(APP_NAME);
const renderSsrApp = loadRenderSsrApp(APP_NAME, ssrManifest);

export const produktlistenController: SsiHandlers = {
  styles: async (request: FastifyRequest, reply: FastifyReply): Promise<void> => {
    const html = [
      createCssLinkTag({ appName: APP_NAME, filePath: clientManifest['chunk-vendors.css'] }),
      createCssLinkTag({ appName: APP_NAME, filePath: clientManifest['app.css'] }),
    ].join('');
    reply.send(html);
  },
  content: async (request: FastifyRequest, reply: FastifyReply): Promise<void> => {
    const { app, state } = await renderSsrApp({ url: request.url });
    const renderedApp = await renderToString(app);
    // eslint-disable-next-line @typescript-eslint/ban-ts-comment
    // @ts-ignore
    const produktlisteId: string = state.EinstellungenState.einstellungen.id;

    const html = `
      <div id='${APP_NAME}_${produktlisteId}'>
        ${renderedApp}
      </div>
      ${renderAppState(produktlisteId, STORE_KEY, state)}
    `;

    reply.send(html);
  },
  scripts: async (request: FastifyRequest, reply: FastifyReply): Promise<void> => {
    const html = [
      createScriptTag({ appName: APP_NAME, filePath: clientManifest['chunk-vendors.js'] }),
      createScriptTag({ appName: APP_NAME, filePath: clientManifest['app.js'] }),
    ].join('');

    reply.send(html);
  },
};
