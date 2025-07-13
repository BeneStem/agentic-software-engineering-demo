import { GetterTree, Module, MutationTree } from 'vuex';
import { RootState } from '@/main/frontend/app/store/index';
import { Einstellung } from '@/main/frontend/app/model/Einstellung';

interface EinstellungenStoreState {
  einstellungen: Einstellung;
}

const state = (): EinstellungenStoreState => {
  return {
    einstellungen: new Einstellung(undefined, undefined, false, undefined, undefined, undefined, undefined, false),
  };
};

const getters: GetterTree<EinstellungenStoreState, RootState> = {
  getId: (state: EinstellungenStoreState): string | undefined => {
    return state.einstellungen.id;
  },
  getUeberschrift: (state: EinstellungenStoreState): string | undefined => {
    return state.einstellungen.ueberschrift;
  },
  getIstHauptueberschrift: (state: EinstellungenStoreState): boolean => {
    return state.einstellungen.istHauptueberschrift;
  },
  getAlleAnzeigenLinkText: (state: EinstellungenStoreState): string | undefined => {
    return state.einstellungen.alleAnzeigenLinkText;
  },
  getAlleAnzeigenLinkZiel: (state: EinstellungenStoreState): string | undefined => {
    return state.einstellungen.alleAnzeigenLinkZiel;
  },
  getTrackingname: (state: EinstellungenStoreState): string | undefined => {
    return state.einstellungen.trackingname;
  },
  getTrackingnummer: (state: EinstellungenStoreState): string | undefined => {
    return state.einstellungen.trackingnummer;
  },
  getIstFilterAusgeblendet: (state: EinstellungenStoreState): boolean => {
    return state.einstellungen.istFilterAusgeblendet;
  },
};

const mutations: MutationTree<EinstellungenStoreState> = {
  setEinstellungenFromQueryparameters(state, queryparameters) {
    const alleAnzeigenLinkZiel =
      queryparameters.alleAnzeigenLinkZielTyp === 'story' && queryparameters.alleAnzeigenLinkZielUrl
        ? '/' + queryparameters.alleAnzeigenLinkZielUrl
        : undefined;

    state.einstellungen.istFilterAusgeblendet = queryparameters.istFilterAusgeblendet === 'true';
    state.einstellungen.id = queryparameters._uid;
    state.einstellungen.ueberschrift = queryparameters.ueberschrift;
    state.einstellungen.istHauptueberschrift = queryparameters.ist_hauptueberschrift === 'true';
    state.einstellungen.alleAnzeigenLinkText = queryparameters.alleAnzeigenLinkText;
    state.einstellungen.alleAnzeigenLinkZiel = alleAnzeigenLinkZiel;
    state.einstellungen.trackingname = queryparameters.produktlistenname;
    state.einstellungen.trackingnummer = queryparameters.produktlisten_id;
  },
};

const einstellungenModule: Module<EinstellungenStoreState, RootState> = {
  namespaced: true,
  state,
  getters,
  mutations,
};

export default einstellungenModule;
