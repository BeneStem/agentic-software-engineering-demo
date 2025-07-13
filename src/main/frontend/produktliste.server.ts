import { App as VueApp, createSSRApp } from 'vue';
import ProduktlisteApp from '@/main/frontend/app/ProduktlisteApp.vue';
import { _createStore, RootState } from '@/main/frontend/app/store';
import { _createRouter } from '@/main/frontend/app/router';

export default async (ssrContext: { url: string }): Promise<{ app: VueApp; state: RootState }> => {
  const app = createSSRApp(ProduktlisteApp);

  const store = _createStore();
  const router = _createRouter();

  app.use(store);
  app.use(router);

  const { url } = ssrContext;
  store.commit('AppState/setUrl', url);

  await router.push(url);
  await router.isReady();

  return { app, state: store.state };
};
