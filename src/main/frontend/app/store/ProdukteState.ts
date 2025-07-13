import { ActionTree, Commit, Dispatch, GetterTree, Module, MutationTree } from 'vuex';
import { Produkt, ProduktDTO } from '@/main/frontend/app/model/Produkt';
import { RootState } from '@/main/frontend/app/store/index';
import { ProdukteFilter } from '@/main/frontend/app/store/ProduktfilterState';
import { ProdukteMitVerfuegbarenFilterwertenDTO } from '@/main/frontend/app/model/VerfuegbareFilterwerte';
import {
  getAlleProdukteMitVerfuegbarenFilterwerten,
  getAlleProdukteMitVerfuegbarenFilterwertenSSR,
  getAlleProdukteSSR,
} from '@/main/frontend/app/api/produkte.api';
import { SingleActiveDebouncedAction } from '@/main/frontend/app/api/SingleActiveDebouncedAction';
import { AxiosError, AxiosResponse } from 'axios';

interface ProdukteStoreState {
  produkte: Produkt[];
}

const state = (): ProdukteStoreState => {
  return {
    produkte: [],
  };
};

const requestAPI = new SingleActiveDebouncedAction<AxiosResponse>(500, 'getAlleProdukteMitVerfuegbarenFilterwerten');

const getters: GetterTree<ProdukteStoreState, RootState> = {
  getProdukte: (state: ProdukteStoreState): Produkt[] => {
    return state.produkte;
  },
  getAnzahlProdukte: (state: ProdukteStoreState): number => {
    return state.produkte.length;
  },
};

const mutations: MutationTree<ProdukteStoreState> = {
  setProdukte(state, produkte: Produkt[]) {
    state.produkte = produkte;
  },
};

const actions: ActionTree<ProdukteStoreState, RootState> = {
  async fetchProdukteSSR({ commit, rootGetters }) {
    const produktFilter: ProdukteFilter = rootGetters['ProduktfilterState/getProdukteFilter'];
    await getAlleProdukteSSR(produktFilter.cmsProdukteFilter)
      .then((response) => setProdukte(commit, response.data))
      .catch((error: AxiosError) => {
        console.log(`Error fetching Produkte; Error: ${error.message}`);
      });
  },
  async fetchProdukteMitVerfuegbarenFilterwerten(
    { commit, dispatch, rootGetters },
    { istAktionAlleFilterZuruecksetzen, useDebouncedAPI }
  ) {
    const produktFilter: ProdukteFilter = rootGetters['ProduktfilterState/getProdukteFilter'];

    let fetchcallPromise: Promise<AxiosResponse>;
    if (useDebouncedAPI) {
      fetchcallPromise = requestAPI.enqueueAction(() => getAlleProdukteMitVerfuegbarenFilterwerten(produktFilter));
    } else {
      fetchcallPromise = getAlleProdukteMitVerfuegbarenFilterwerten(produktFilter);
    }

    await fetchcallPromise
      .then((response) =>
        setProdukteMitVerfuegbarenFilterwerten(commit, dispatch, response.data, istAktionAlleFilterZuruecksetzen)
      )
      .catch((error: AxiosError) => {
        console.log(`Error fetching ProdukteMitVerfuegbarenFilterwerten; Error: ${error.message}`);
      });
  },
  async fetchProdukteMitVerfuegbarenFilterwertenSSR(
    { commit, dispatch, rootGetters },
    { istAktionAlleFilterZuruecksetzen, useDebouncedAPI }
  ) {
    const produktFilter: ProdukteFilter = rootGetters['ProduktfilterState/getProdukteFilter'];

    let fetchcallPromise: Promise<AxiosResponse>;
    if (useDebouncedAPI) {
      fetchcallPromise = requestAPI.enqueueAction(() => getAlleProdukteMitVerfuegbarenFilterwertenSSR(produktFilter));
    } else {
      fetchcallPromise = getAlleProdukteMitVerfuegbarenFilterwertenSSR(produktFilter);
    }

    await fetchcallPromise
      .then((response) =>
        setProdukteMitVerfuegbarenFilterwerten(commit, dispatch, response.data, istAktionAlleFilterZuruecksetzen)
      )
      .catch((error: AxiosError) => {
        console.log(`Error fetching ProdukteMitVerfuegbarenFilterwerten in SSR; Error: ${error.message}`);
      });
  },
};

const setProdukte = (commit: Commit, produkte: ProduktDTO[]) => {
  commit(
    'setProdukte',
    produkte.map((produktDTO: ProduktDTO) => Produkt.vonProduktDTO(produktDTO))
  );
};

const setProdukteMitVerfuegbarenFilterwerten = (
  commit: Commit,
  dispatch: Dispatch,
  produkteMitVerfuegbarenFilterwerten: ProdukteMitVerfuegbarenFilterwertenDTO,
  istAktionFilterZuruecksetzen: boolean
) => {
  if (produkteMitVerfuegbarenFilterwerten.produkte) {
    setProdukte(commit, produkteMitVerfuegbarenFilterwerten.produkte);
  }
  if (produkteMitVerfuegbarenFilterwerten.verfuegbareFilterwerte) {
    dispatch(
      'ProduktfilterState/updateVerfuegbareFilterwerte',
      {
        verfuegbareFilterwerte: produkteMitVerfuegbarenFilterwerten.verfuegbareFilterwerte,
        istAktionFilterZuruecksetzen: istAktionFilterZuruecksetzen,
      },
      { root: true }
    );
  }
};

const produkteModule: Module<ProdukteStoreState, RootState> = { namespaced: true, state, getters, mutations, actions };

export default produkteModule;
