import path from 'path';
import { AppManifest, AppType, RenderSsrApp } from '../types';
import { DIST_PATH } from './DistUtils';

export function loadClientManifest(appName: string): AppManifest {
  return loadManifest(appName, 'client');
}

export function loadSsrManifest(appName: string): AppManifest {
  return loadManifest(appName, 'ssr');
}

export function loadRenderSsrApp(appName: string, ssrManifest: AppManifest): RenderSsrApp {
  return require(getSsrAppPath(appName, ssrManifest)).default;
}

function loadManifest(appName: string, appType: AppType): AppManifest {
  return require(path.join(DIST_PATH, `/${appName}/${appType}/${appType}-manifest.json`));
}

function getSsrAppPath(appName: string, manifest: AppManifest): string {
  const APP_DIST_PATH = path.join(DIST_PATH, appName);
  return path.join(APP_DIST_PATH, '/ssr', manifest['app.js']);
}
