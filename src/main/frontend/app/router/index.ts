import { createMemoryHistory, createRouter, Router, RouteRecordRaw } from 'vue-router';
import ProduktlisteView from '@/main/frontend/app/views/ProduktlisteView.vue';
import MiniProduktlisteView from '@/main/frontend/app/views/MiniProduktlisteView.vue';

const routes: Array<RouteRecordRaw> = [
  {
    path: '/ssi/finden/produktliste',
    name: 'Produktliste',
    component: ProduktlisteView,
  },
  {
    path: '/ssi/finden/mini-produktliste',
    name: 'MiniProduktliste',
    component: MiniProduktlisteView,
  },
  {
    path: '/:pathMatch(.*)*',
    name: 'NotFound',
    component: {},
  },
];

export function _createRouter(): Router {
  return createRouter({ routes, history: createMemoryHistory() });
}
