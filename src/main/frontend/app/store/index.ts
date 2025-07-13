import { createStore, Store } from 'vuex';
import ProdukteState from './ProdukteState';
import EinstellungenState from './EinstellungenState';
import FeatureState from '@/main/frontend/app/store/FeatureState';
import ProduktfilterState from '@/main/frontend/app/store/ProduktfilterState';
import AppState from '@/main/frontend/app/store/AppState';

export interface RootState {
  version: string;
}

export const storeKey = 'PRODUKTLISTE'; // needs to be kept in sync manually with the controller

export function _createStore(): Store<RootState> {
  return createStore<RootState>({
    state: {
      version: '1.0',
    },
    modules: {
      AppState,
      ProdukteState,
      ProduktfilterState,
      EinstellungenState,
      FeatureState,
    },
  });
}
