import { ActionTree, Commit, GetterTree, Module, MutationTree } from 'vuex';
import { RootState } from '@/main/frontend/app/store/index';
import axios, { AxiosRequestConfig } from 'axios';
import { FeatureToggles } from '@/main/frontend/app/util/featureToggles';

export interface FeatureToggle {
  feature: string;
  active: boolean;
}

interface FeatureStoreState {
  features: FeatureToggle[] | undefined;
}

const state = (): FeatureStoreState => {
  return {
    features: undefined,
  };
};

const getters: GetterTree<FeatureStoreState, RootState> = {
  getFeatureStatus:
    (state: FeatureStoreState) =>
    (featureToggleName: FeatureToggles): boolean => {
      const toggle: FeatureToggle | undefined = state.features?.find((toggle) => toggle.feature === featureToggleName);
      return toggle ? toggle.active : false;
    },
};

const mutations: MutationTree<FeatureStoreState> = {
  setFeatures(state, features) {
    state.features = features;
  },
};

const handleError = (error: any, commit: Commit) => {
  console.error(error.message);
  commit('setFeatures', undefined);
};

const request = async (path: string, commit: Commit) => {
  try {
    const axiosResponse = await axios.get(path);
    if (!axiosResponse.status.toString().startsWith('2')) {
      new Error(`Bad Response: ${axiosResponse.status} body: ${axiosResponse.data}`);
    }
    return axiosResponse.data;
  } catch (error: any) {
    handleError(error, commit);
    return;
  }
};

const actions: ActionTree<FeatureStoreState, RootState> = {
  async fetchFeatureTogglesSSR({ commit }) {
    const toggles = await request(__FINDEN_API_HOST__ + `/api/finden/feature-toggles`, commit);
    if (toggles) {
      commit('setFeatures', toggles);
    }
  },
};
const featureModule: Module<FeatureStoreState, RootState> = { namespaced: true, state, getters, mutations, actions };

export default featureModule;
