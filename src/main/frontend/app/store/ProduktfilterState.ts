import { ActionContext, ActionTree, GetterTree, Module, MutationTree } from 'vuex';
import { RootState } from '@/main/frontend/app/store/index';
import { Filterwert, FilterwertMitId } from '@/main/frontend/app/model/Filterwert';
import {
  FarbeDTO,
  KlassifikationDTO,
  PreisbereichDTO,
  SorteDTO,
  VerfuegbareFilterwerteDTO,
} from '@/main/frontend/app/model/VerfuegbareFilterwerte';
import { CmsProdukteFilterDTO, UserProdukteFilterDTO } from '@/main/frontend/app/model/ProdukteFilterParameter';
import { QueryParameters } from '@/main/frontend/app/model/QueryParameters';
import { FilterTyp } from '@/main/frontend/app/model/FilterTyp';
import { uiStatus } from '@/main/frontend/app/composables/ProduktFilterUiStatus';
import { addDays, format, isSameDay, isTomorrow, parse } from 'date-fns';

export type CmsPreisFilter = {
  minPreis: number;
  maxPreis: number;
};

export type ProdukteFilter = {
  cmsProdukteFilter: CmsProdukteFilterDTO;
  userProdukteFilter: UserProdukteFilterDTO;
};

export interface VerfuegbareFilterwerte {
  [FilterTyp.SORTE]: Filterwert[];
  [FilterTyp.FARBE]: Filterwert[];
  [FilterTyp.PRODUKTART]: Filterwert[];
  [FilterTyp.PREIS]: PreisbereichDTO | null;
  [FilterTyp.VERFUEGBARKEIT]: Filterwert[];
}

export interface AnzahlSelectedFilterwert {
  [FilterTyp.SORTE]: number;
  [FilterTyp.FARBE]: number;
  [FilterTyp.PRODUKTART]: number;
  [FilterTyp.VERFUEGBARKEIT]: number;
}

export interface NeueAnzahlVerfuegbareFilterwerte {
  [FilterTyp.SORTE]: number;
  [FilterTyp.FARBE]: number;
  [FilterTyp.PRODUKTART]: number;
  [FilterTyp.VERFUEGBARKEIT]: number;
}

export type VeraenderteFilterwerte = Record<FilterTyp, boolean>;

export type FilterstatusUndVerfuegbareFilterwerte = {
  verfuegbareFilterwerte: VerfuegbareFilterwerteDTO;
  istAktionFilterZuruecksetzen: boolean;
};

export interface AnzahlVerfuegbareFilterwerte {
  [FilterTyp.SORTE]: number;
  [FilterTyp.FARBE]: number;
  [FilterTyp.PRODUKTART]: number;
  [FilterTyp.VERFUEGBARKEIT]: number;
}

export type ZeigeFilter = Record<FilterTyp, boolean>;

interface ProduktfilterState {
  cmsProdukteFilter: QueryParameters | null;
  sorten: Map<string, Filterwert>;
  farben: Map<string, Filterwert>;
  preisBereich: PreisbereichDTO | null;
  klassifikationen: Map<string, FilterwertMitId>;
  liefertage: Map<string, Filterwert>;
}

const state = (): ProduktfilterState => {
  return {
    cmsProdukteFilter: null,
    sorten: new Map<string, Filterwert>(),
    farben: new Map<string, Filterwert>(),
    preisBereich: null,
    klassifikationen: new Map<string, FilterwertMitId>(),
    liefertage: new Map<string, Filterwert>(),
  };
};

export const getters: GetterTree<ProduktfilterState, RootState> = {
  getSorten: (state: ProduktfilterState): Filterwert[] => {
    return [...state.sorten.values()];
  },
  getSelectedSorten: (state: ProduktfilterState, getters): Filterwert[] => {
    return getters.getSorten.filter((value: Filterwert) => value.selected);
  },
  getFarben: (state: ProduktfilterState): Filterwert[] => {
    return [...state.farben.values()];
  },
  getSelectedFarben: (state: ProduktfilterState, getters): Filterwert[] => {
    return getters.getFarben.filter((value: Filterwert) => value.selected);
  },
  getKlassifikationen: (state: ProduktfilterState): Filterwert[] => {
    return [...state.klassifikationen.values()];
  },
  getSelectedKlassifikationen: (state: ProduktfilterState, getters): Filterwert[] => {
    return getters.getKlassifikationen.filter((value: FilterwertMitId) => value.selected);
  },
  getPreisbereich: (state: ProduktfilterState): PreisbereichDTO | null => {
    return state.preisBereich;
  },
  getLiefertage: (state: ProduktfilterState): Filterwert[] => {
    return [...state.liefertage.values()];
  },
  getSelectedLiefertage: (state: ProduktfilterState, getters): Filterwert[] => {
    return getters.getLiefertage.filter((value: Filterwert) => value.selected);
  },
  getVerfuegbareFilterwerte: (state: ProduktfilterState, getters): VerfuegbareFilterwerte => {
    return {
      [FilterTyp.SORTE]: getters.getSorten,
      [FilterTyp.FARBE]: getters.getFarben,
      [FilterTyp.PRODUKTART]: getters.getKlassifikationen,
      [FilterTyp.PREIS]: getters.getPreisbereich,
      [FilterTyp.VERFUEGBARKEIT]: getters.getLiefertage,
    };
  },
  getProdukteFilter: (state: ProduktfilterState, getters): ProdukteFilter => {
    const userProdukteFilter = {
      sorten: getters.getSelectedSorten.map((value: Filterwert) => ({ name: value.name })),
      farben: getters.getSelectedFarben.map((value: Filterwert) => ({ name: value.name })),
      klassifikationen: getters.getSelectedKlassifikationen.map((value: FilterwertMitId) => ({
        name: value.name,
        id: value.id,
      })),
      liefertage: { verfuegbareLiefertage: getters.getSelectedLiefertage.map((value: Filterwert) => value.name) },
      preisbereich: null,
    };

    return { cmsProdukteFilter: mapToCmsProdukteFilter(state), userProdukteFilter: userProdukteFilter };
  },
  getAnzahlSelectedFilterwerte: (state: ProduktfilterState, getters): AnzahlSelectedFilterwert => ({
    [FilterTyp.SORTE]: getters.getSelectedSorten.length,
    [FilterTyp.FARBE]: getters.getSelectedFarben.length,
    [FilterTyp.PRODUKTART]: getters.getSelectedKlassifikationen.length,
    [FilterTyp.VERFUEGBARKEIT]: getters.getSelectedLiefertage.length,
  }),
  getSindFilterwerteAusgewaehlt: (state: ProduktfilterState, getters): boolean => {
    const anzahlSelectedFilterwerte = getters.getAnzahlSelectedFilterwerte;
    const anzahlSelectedFilterwerteSum =
      anzahlSelectedFilterwerte.SORTE +
      anzahlSelectedFilterwerte.FARBE +
      anzahlSelectedFilterwerte.PRODUKTART +
      anzahlSelectedFilterwerte.VERFUEGBARKEIT;
    return anzahlSelectedFilterwerteSum > 0;
  },
  getVeraenderteFilterwerte: (state: ProduktfilterState, getters): VeraenderteFilterwerte => {
    const anzahlSelectedFilterwerte = getters.getAnzahlSelectedFilterwerte;
    return {
      [FilterTyp.SORTE]: !!anzahlSelectedFilterwerte.SORTE,
      [FilterTyp.FARBE]: !!anzahlSelectedFilterwerte.FARBE,
      [FilterTyp.PRODUKTART]: !!anzahlSelectedFilterwerte.PRODUKTART,
      [FilterTyp.VERFUEGBARKEIT]: !!anzahlSelectedFilterwerte.VERFUEGBARKEIT,
      [FilterTyp.PREIS]: false, // TODO: Save initial preis to check for veraenderung
    };
  },
  getAnzahlVerfuegbareFilterwerte: (state: ProduktfilterState, getters): AnzahlVerfuegbareFilterwerte => ({
    [FilterTyp.SORTE]: getters.getSorten.length,
    [FilterTyp.FARBE]: getters.getFarben.length,
    [FilterTyp.PRODUKTART]: getters.getKlassifikationen.length,
    [FilterTyp.VERFUEGBARKEIT]: getters.getLiefertage.length,
  }),
  getZeigeFilter: (state: ProduktfilterState, getters): ZeigeFilter => {
    const anzahlVerfuegbareFilterwerte: AnzahlVerfuegbareFilterwerte = getters.getAnzahlVerfuegbareFilterwerte;
    const verfuegbareSorten = anzahlVerfuegbareFilterwerte.SORTE;
    const verfuegbareFarben = anzahlVerfuegbareFilterwerte.FARBE;
    const verfuegbareKlassifikationen = anzahlVerfuegbareFilterwerte.PRODUKTART;
    const verfuegbareLiefertage = anzahlVerfuegbareFilterwerte.VERFUEGBARKEIT;
    const selectedSorten = getters.getSelectedSorten.length;
    const selectedFarben = getters.getSelectedFarben.length;
    const selectedKlassifikationen = getters.getSelectedKlassifikationen.length;
    const selectedLiefertage = getters.getSelectedLiefertage.length;

    return {
      [FilterTyp.SORTE]: verfuegbareSorten > 0 || selectedSorten > 0,
      [FilterTyp.FARBE]: verfuegbareFarben > 0 || selectedFarben > 0,
      [FilterTyp.PRODUKTART]: verfuegbareKlassifikationen > 0 || selectedKlassifikationen > 0,
      [FilterTyp.VERFUEGBARKEIT]: verfuegbareLiefertage > 0 || selectedLiefertage > 0,
      [FilterTyp.PREIS]: false, // TODO: Check initial preis if filter should be shown
    };
  },
  getSelectedFilterwerte: (state: ProduktfilterState, getters): (Filterwert | FilterwertMitId)[] => {
    return [
      ...getters.getSelectedSorten,
      ...getters.getSelectedFarben,
      ...getters.getSelectedKlassifikationen,
      ...getters.getSelectedLiefertage,
    ];
  },
};

export const mutations: MutationTree<ProduktfilterState> = {
  setSorten(state: ProduktfilterState, sorten: Map<string, Filterwert>) {
    state.sorten = sorten;
  },
  setFarben(state: ProduktfilterState, farben: Map<string, Filterwert>) {
    state.farben = farben;
  },
  setKlassifikation(state: ProduktfilterState, klassifikationen: Map<string, FilterwertMitId>) {
    state.klassifikationen = klassifikationen;
  },
  setLiefertage(state: ProduktfilterState, liefertage: Map<string, Filterwert>) {
    state.liefertage = liefertage;
  },
  setPreisBereich(state: ProduktfilterState, preisBereich: PreisbereichDTO) {
    state.preisBereich = preisBereich;
  },
  setCmsFilter(state: ProduktfilterState, cmsProdukteFilter: QueryParameters) {
    state.cmsProdukteFilter = cmsProdukteFilter;
  },
  setFilterwert(state: ProduktfilterState, filterWert: FilterwertMitId) {
    switch (filterWert.filtertyp) {
      case FilterTyp.SORTE: {
        state.sorten = state.sorten.set(filterWert.name, filterWert);
        break;
      }
      case FilterTyp.FARBE: {
        state.farben = state.farben.set(filterWert.name, filterWert);
        break;
      }
      case FilterTyp.PRODUKTART: {
        state.klassifikationen = state.klassifikationen.set(filterWert.id, filterWert);
        break;
      }
      case FilterTyp.VERFUEGBARKEIT: {
        state.liefertage.set(filterWert.name, filterWert);
        break;
      }
    }
  },
  setAlleFilterZuruecksetzen(state: ProduktfilterState) {
    state.sorten.forEach((value) => {
      value.selected = false;
    });
    state.farben.forEach((value) => {
      value.selected = false;
    });
    state.klassifikationen.forEach((value) => {
      value.selected = false;
    });
    state.liefertage.forEach((value) => {
      value.selected = false;
    });
  },
};

export const actions: ActionTree<ProduktfilterState, RootState> = {
  updateVerfuegbareFilterwerte(context, payload: FilterstatusUndVerfuegbareFilterwerte) {
    const { commit, state } = context;
    const neueFilterwerte = {
      sorten: payload.verfuegbareFilterwerte.sorten,
      farben: payload.verfuegbareFilterwerte.farben,
      klassifikationen: payload.verfuegbareFilterwerte.klassifikationen,
      liefertage: payload.verfuegbareFilterwerte.liefertage.verfuegbareLiefertage,
    };
    const anzahlNeueFilterwerte = {
      sorten: neueFilterwerte.sorten.length,
      farben: neueFilterwerte.farben.length,
      klassifikationen: neueFilterwerte.klassifikationen.length,
      liefertage: neueFilterwerte.liefertage.length,
    };
    const istAktionFilterZuruecksetzen = payload.istAktionFilterZuruecksetzen;

    const aktualisiereSorten = sollAktualisiertWerden(
      FilterTyp.SORTE,
      context,
      anzahlNeueFilterwerte.sorten,
      istAktionFilterZuruecksetzen
    );
    const aktualisiereFarben = sollAktualisiertWerden(
      FilterTyp.FARBE,
      context,
      anzahlNeueFilterwerte.farben,
      istAktionFilterZuruecksetzen
    );
    const aktualisiereProduktarten = sollAktualisiertWerden(
      FilterTyp.PRODUKTART,
      context,
      anzahlNeueFilterwerte.klassifikationen,
      istAktionFilterZuruecksetzen
    );
    const aktualisiereLiefertage = sollAktualisiertWerden(
      FilterTyp.VERFUEGBARKEIT,
      context,
      anzahlNeueFilterwerte.liefertage,
      istAktionFilterZuruecksetzen
    );

    if (aktualisiereSorten) {
      const updatedSorten = updateSortenOrFarben(state.sorten, neueFilterwerte.sorten, FilterTyp.SORTE);
      commit('setSorten', updatedSorten);
    }
    if (aktualisiereFarben) {
      const updatedFarben = updateSortenOrFarben(state.farben, neueFilterwerte.farben, FilterTyp.FARBE);
      commit('setFarben', updatedFarben);
    }
    if (aktualisiereProduktarten) {
      const updatedKlassifikationen = updateKlassifikationen(state.klassifikationen, neueFilterwerte.klassifikationen);
      commit('setKlassifikation', updatedKlassifikationen);
    }
    if (aktualisiereLiefertage) {
      const updatedLiefertage = updateLiefertage(state.liefertage, neueFilterwerte.liefertage);
      commit('setLiefertage', updatedLiefertage);
    }
    if (
      uiStatus.anzuzeigenderFilter != FilterTyp.PREIS ||
      payload.istAktionFilterZuruecksetzen ||
      uiStatus.filterwertZurueckgesetzt
    ) {
      commit('setPreisBereich', payload.verfuegbareFilterwerte.preisbereich);
    }
    uiStatus.setFilterwertZurueckgesetzt(false);
  },
};

function sollAktualisiertWerden(
  filtertyp: FilterTyp.SORTE | FilterTyp.FARBE | FilterTyp.PRODUKTART | FilterTyp.VERFUEGBARKEIT,
  { getters }: ActionContext<ProduktfilterState, any>,
  verfuegbareFilterwerte: number,
  istAktionFilterZuruecksetzen: boolean
): boolean {
  const filterIstNichtOffen = uiStatus.anzuzeigenderFilter != filtertyp;
  const nurEinVerfuegbarerFilterwert =
    getters.getAnzahlSelectedFilterwerte[filtertyp] === 0 && verfuegbareFilterwerte > 0;
  const filterIstOffen = uiStatus.anzuzeigenderFilter;
  const filterwertWurdeDurchZuruecksetzenButtonEntfernt = uiStatus.filterwertZurueckgesetzt;

  return (
    filterIstNichtOffen ||
    (filterIstOffen && nurEinVerfuegbarerFilterwert) ||
    (filterwertWurdeDurchZuruecksetzenButtonEntfernt && nurEinVerfuegbarerFilterwert) ||
    istAktionFilterZuruecksetzen
  );
}

type FarbInformation = {
  lesbarerName: string;
  sortIndex: number;
};

const colorMap = new Map<string, FarbInformation>()
  .set('blau', { lesbarerName: 'Blau', sortIndex: 1 })
  .set('grün', { lesbarerName: 'Grün', sortIndex: 2 })
  .set('gelb', { lesbarerName: 'Gelb', sortIndex: 3 })
  .set('orange', { lesbarerName: 'Orange', sortIndex: 4 })
  .set('rot', { lesbarerName: 'Rot', sortIndex: 5 })
  .set('rosa', { lesbarerName: 'Rosa', sortIndex: 6 })
  .set('pink', { lesbarerName: 'Pink', sortIndex: 7 })
  .set('lila', { lesbarerName: 'Lila', sortIndex: 8 })
  .set('weiss', { lesbarerName: 'Weiß', sortIndex: 9 })
  .set('bunt', { lesbarerName: 'Bunt', sortIndex: 10 });

const updateSortenOrFarben = (
  oldValues: Map<string, Filterwert>,
  newValues: SorteDTO[] | FarbeDTO[],
  filtertyp: FilterTyp
): Map<string, Filterwert> => {
  const newMap = new Map<string, Filterwert>();
  newValues.map((v) => {
    let lesbarerName: string | undefined = v.name;
    let sortIndex = 0;
    if (filtertyp === FilterTyp.FARBE) {
      const color = colorMap.get(v.name);
      if (color) {
        lesbarerName = color.lesbarerName;
        sortIndex = color.sortIndex;

        newMap.set(v.name, {
          name: v.name,
          lesbarerName: lesbarerName,
          selected: false,
          filtertyp: filtertyp,
          sortIndex: sortIndex,
        });
      }
    } else {
      newMap.set(v.name, {
        name: v.name,
        lesbarerName: v.name,
        selected: false,
        filtertyp: filtertyp,
      });
    }
  });
  const filterwerte = [...oldValues.values()].filter((value) => value.selected);
  filterwerte.forEach((filterwert) => {
    newMap.set(filterwert.name, filterwert);
  });
  return newMap;
};

const updateLiefertage = (oldValues: Map<string, Filterwert>, newValues: string[]): Map<string, Filterwert> => {
  const newMap = new Map<string, Filterwert>();
  newValues.map((v) => {
    const datum = parse(v, 'yyyy-MM-dd', new Date());
    const lesbaresDatum = isTomorrow(datum)
      ? 'Morgen'
      : isSameDay(datum, addDays(Date.now(), 2))
      ? 'Übermorgen'
      : format(datum, 'dd.MM.yyyy');
    newMap.set(v, {
      name: v,
      selected: false,
      lesbarerName: lesbaresDatum,
      filtertyp: FilterTyp.VERFUEGBARKEIT,
    });
  });
  const filterwerte = [...oldValues.values()].filter((value) => value.selected);
  filterwerte.forEach((filterwert) => {
    newMap.set(filterwert.name, filterwert);
  });
  return newMap;
};

const updateKlassifikationen = (
  oldValues: Map<string, FilterwertMitId>,
  newValues: KlassifikationDTO[]
): Map<string, FilterwertMitId> => {
  const newMap = new Map<string, FilterwertMitId>();
  newValues.map((v) => {
    const filterwert = oldValues.get(v.id);
    newMap.set(v.id, {
      id: v.id,
      name: v.name,
      lesbarerName: v.name,
      selected: filterwert ? filterwert.selected : false,
      filtertyp: FilterTyp.PRODUKTART,
    });
  });
  const filterwerte = [...oldValues.values()].filter((value) => value.selected);
  filterwerte.forEach((filterwert) => {
    newMap.set(filterwert.id, filterwert);
  });
  return newMap;
};

const mapToCmsProdukteFilter = (state: ProduktfilterState): CmsProdukteFilterDTO => {
  return {
    blumensorten: state.cmsProdukteFilter?.blumensorten?.map((value) => ({ name: value })) || [],
    farben: state.cmsProdukteFilter?.farben?.map((value) => ({ name: value })) || [],
    klassifikationen: state.cmsProdukteFilter?.klassifikation3?.map((value) => ({ name: value, id: value })) || [],
    preisbereich: {
      minPreis: state.cmsProdukteFilter?.minPreis
        ? {
            bruttoBetrag: state.cmsProdukteFilter?.minPreis,
            waehrung: 'EUR', // TODO: Gibt es auch eine Währung aus dem CMS?
          }
        : null,
      maxPreis: state.cmsProdukteFilter?.maxPreis
        ? {
            bruttoBetrag: state.cmsProdukteFilter?.maxPreis,
            waehrung: 'EUR', // TODO: Gibt es auch eine Währung aus dem CMS?
          }
        : null,
    },
    limit: state.cmsProdukteFilter?.limit || null,
    produktNummern: state.cmsProdukteFilter?.produktNummern?.map((value) => ({ nummer: value })) || [],
    produktnummernVerwendung: state.cmsProdukteFilter?.produktnummernVerwendung || null,
  };
};

const produktfilterModule: Module<ProduktfilterState, RootState> = {
  namespaced: true,
  state,
  getters,
  mutations,
  actions,
};

export default produktfilterModule;
