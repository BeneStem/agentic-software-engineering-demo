import { createApp } from 'vue';
import ProduktlisteApp from '@/main/frontend/app/ProduktlisteApp.vue';
import { _createStore, storeKey } from '@/main/frontend/app/store';
import { _createRouter } from '@/main/frontend/app/router';

const appName = 'produktliste';
const deriveRootElementId = (appName: string) => (process.env.NODE_ENV === 'development' ? 'app' : appName);

if (!window.SSI_STORE) {
  window.SSI_STORE = {};
}

if (!window.__INITIAL_STATE__) {
  window.__INITIAL_STATE__ = {};
}

const produktlistenStores: string[] = Object.keys(window.__INITIAL_STATE__).filter((key) => key.startsWith(storeKey));
const produktlistenIds = produktlistenStores.map((store) => store.split('__')[1]);

produktlistenIds.forEach((produktlisteId) => {
  const rootElementId = deriveRootElementId(`${appName}_${produktlisteId}`);
  const rootElement = document.getElementById(rootElementId);

  if (!rootElement) {
    console.error(`cannot find vue app root element by id: ${rootElementId}`);
  } else {
    [...rootElement.getElementsByClassName('b2k_ssi')].forEach((element) => {
      const ssiStoreKey = element.getAttribute('ssi-store-key');
      if (!ssiStoreKey) {
        console.error(`expected element ${element} to have required attribute "ssiStoreKey"`);
      } else {
        window.SSI_STORE[ssiStoreKey] = element.innerHTML;
      }
    });
  }

  console.log(`create App: produktliste ${produktlisteId}`);

  const app = createApp(ProduktlisteApp);
  const store = _createStore();
  const router = _createRouter();

  (async (r, a) => {
    const localStoreKey = `${storeKey}__${produktlisteId}`;
    if (window.__INITIAL_STATE__ && window.__INITIAL_STATE__[localStoreKey]) {
      store.replaceState(window.__INITIAL_STATE__[localStoreKey]);
    }

    a.use(router);
    a.use(store);

    await router.push(store.getters['AppState/getUrl']);
    await router.isReady();

    await r.isReady();
    a.mount(`#${rootElementId}`, true);
  })(router, app);
});
