import { createApp } from 'vue';
import ProduktlisteToolApp from './app/ProduktlisteToolApp.vue';

console.log('create App: ProduktlisteToolApp');

const app = createApp(ProduktlisteToolApp, {});

(async (a) => {
  a.mount('#tool-produktliste');
})(app);
