import { GetterTree, Module, MutationTree } from 'vuex';
import { RootState } from '@/main/frontend/app/store/index';

interface AppStoreState {
  url: string | undefined;
}

const state = (): AppStoreState => {
  return {
    url: undefined,
  };
};

const getters: GetterTree<AppStoreState, RootState> = {
  getUrl: (state: AppStoreState): string | undefined => {
    return state.url;
  },
};

const mutations: MutationTree<AppStoreState> = {
  setUrl(state, url) {
    state.url = url;
  },
};

const appModule: Module<AppStoreState, RootState> = { namespaced: true, state, getters, mutations };

export default appModule;
