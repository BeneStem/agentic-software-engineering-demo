import { Filterwert, FilterwertMitId } from '@/main/frontend/app/model/Filterwert';
import {
  testAction,
  testFilterwerteFarben,
  testFilterwerteKlassifikationen,
  testFilterwerteLiefertage,
  testFilterwertePreisbereich,
  testFilterwerteSorten,
} from '@/test/frontend/utils/TestProduktfilterHelper';
import {
  actions,
  AnzahlSelectedFilterwert,
  FilterstatusUndVerfuegbareFilterwerte,
  getters,
  mutations,
  ProdukteFilter,
  VeraenderteFilterwerte,
  ZeigeFilter,
} from '@/main/frontend/app/store/ProduktfilterState';
import {
  FarbeDTO,
  KlassifikationDTO,
  PreisbereichDTO,
  SorteDTO,
} from '@/main/frontend/app/model/VerfuegbareFilterwerte';
import { QueryParameters } from '@/main/frontend/app/model/QueryParameters';
import { FilterTyp } from '@/main/frontend/app/model/FilterTyp';
import { addDays, format } from 'date-fns';

const rootState = { version: '100' };
let defaultState = {
  cmsProdukteFilter: null,
  sorten: testFilterwerteSorten(),
  farben: testFilterwerteFarben(),
  preisBereich: testFilterwertePreisbereich(),
  klassifikationen: testFilterwerteKlassifikationen(),
  liefertage: testFilterwerteLiefertage(),
};

describe('ProduktfilterState.ts - Getters', () => {
  beforeEach(() => {
    defaultState = {
      cmsProdukteFilter: null,
      sorten: testFilterwerteSorten(),
      farben: testFilterwerteFarben(),
      preisBereich: testFilterwertePreisbereich(),
      klassifikationen: testFilterwerteKlassifikationen(),
      liefertage: testFilterwerteLiefertage(),
    };
  });
  it('should return store state sorten via getter', () => {
    const result: Filterwert[] = getters.getSorten(defaultState, {}, rootState, {});

    const expected: Filterwert[] = [
      { name: 'Lilien', lesbarerName: 'Lilien', selected: false, filtertyp: FilterTyp.SORTE },
      { name: 'Nelken', lesbarerName: 'Nelken', selected: true, filtertyp: FilterTyp.SORTE },
    ];
    expect(result).toEqual(expected);
  });

  it('should return store state farben via getter', () => {
    const result: Filterwert[] = getters.getFarben(defaultState, {}, rootState, {});

    const expected: Filterwert[] = [
      { name: 'rot', lesbarerName: 'Rot', selected: true, filtertyp: FilterTyp.FARBE, sortIndex: 5 },
      { name: 'gelb', lesbarerName: 'Gelb', selected: false, filtertyp: FilterTyp.FARBE, sortIndex: 3 },
    ];
    expect(result).toEqual(expected);
  });

  it('should return store state klassifikationen via getter', () => {
    const result: FilterwertMitId[] = getters.getKlassifikationen(defaultState, {}, rootState, {});

    const expected: FilterwertMitId[] = [
      { id: 'ID1', name: 'Strauß', lesbarerName: 'Strauß', selected: true, filtertyp: FilterTyp.PRODUKTART },
      { id: 'ID2', name: 'Marktware', lesbarerName: 'Marktware', selected: false, filtertyp: FilterTyp.PRODUKTART },
    ];
    expect(result).toEqual(expected);
  });

  it('should return store state preisbereich via getter', () => {
    const result: PreisbereichDTO = getters.getPreisbereich(defaultState, {}, rootState, {});

    const expected: PreisbereichDTO = {
      maxPreis: {
        bruttoBetrag: 55,
        waehrung: 'EUR',
      },
      minPreis: {
        bruttoBetrag: 2,
        waehrung: 'EUR',
      },
      selectedMaxPreis: 20,
      selectedMinPreis: 2,
    };
    expect(result).toEqual(expected);
  });

  it('should return store state liefertage via getter', () => {
    const result: Filterwert[] = getters.getLiefertage(defaultState, {}, rootState, {});

    const expected: Filterwert[] = [
      { name: '2022-04-08', lesbarerName: '08.04.2022', selected: true, filtertyp: FilterTyp.VERFUEGBARKEIT },
      { name: '2022-04-09', lesbarerName: '09.04.2022', selected: false, filtertyp: FilterTyp.VERFUEGBARKEIT },
    ];
    expect(result).toEqual(expected);
  });

  it('should return store state selected Sorten via getter', () => {
    const result: SorteDTO[] = getters.getSelectedSorten(
      defaultState,
      {
        getSorten: [
          { name: 'Lilien', selected: false, filtertyp: FilterTyp.SORTE },
          { name: 'Nelken', selected: true, filtertyp: FilterTyp.SORTE },
        ],
      },
      rootState,
      getters
    );
    expect(result).toEqual([{ name: 'Nelken', selected: true, filtertyp: FilterTyp.SORTE }]);
  });

  it('should return store state selected Farben via getter', () => {
    const result: FarbeDTO[] = getters.getSelectedFarben(
      defaultState,
      {
        getFarben: [
          { name: 'rot', selected: true, filtertyp: FilterTyp.FARBE },
          { name: 'gelb', selected: false, filtertyp: FilterTyp.FARBE },
        ],
      },
      rootState,
      {}
    );
    expect(result).toEqual([{ name: 'rot', selected: true, filtertyp: FilterTyp.FARBE }]);
  });

  it('should return store state selected Klassifikationen via getter', () => {
    const result: KlassifikationDTO[] = getters.getSelectedKlassifikationen(
      defaultState,
      {
        getKlassifikationen: [
          { id: 'ID1', name: 'Strauß', selected: true, filtertyp: FilterTyp.PRODUKTART },
          { id: 'ID2', name: 'Marktware', selected: false, filtertyp: FilterTyp.PRODUKTART },
        ],
      },
      rootState,
      {}
    );
    expect(result).toEqual([{ id: 'ID1', name: 'Strauß', selected: true, filtertyp: FilterTyp.PRODUKTART }]);
  });

  it('should return store state selected Liefertage via getter', () => {
    const result: FarbeDTO[] = getters.getSelectedLiefertage(
      defaultState,
      {
        getLiefertage: [
          { name: '2022-04-08', selected: true, filtertyp: FilterTyp.VERFUEGBARKEIT },
          { name: '2022-04-09', selected: false, filtertyp: FilterTyp.VERFUEGBARKEIT },
        ],
      },
      rootState,
      {}
    );
    expect(result).toEqual([{ name: '2022-04-08', selected: true, filtertyp: FilterTyp.VERFUEGBARKEIT }]);
  });

  it('should return store state produkteFilter via getter', () => {
    const result: ProdukteFilter = getters.getProdukteFilter(
      defaultState,
      {
        getSelectedSorten: [{ name: 'Nelken' }],
        getSelectedFarben: [{ name: 'rot' }],
        getSelectedKlassifikationen: [
          {
            id: 'ID1',
            name: 'Strauß',
          },
        ],
        getSelectedLiefertage: [{ name: '2022-04-08' }],
      },
      rootState,
      {}
    );

    const expected: ProdukteFilter = {
      cmsProdukteFilter: {
        blumensorten: [],
        farben: [],
        klassifikationen: [],
        preisbereich: { minPreis: null, maxPreis: null },
        limit: null,
        produktNummern: [],
        produktnummernVerwendung: null,
      },
      userProdukteFilter: {
        sorten: [{ name: 'Nelken' }],
        farben: [{ name: 'rot' }],
        klassifikationen: [
          {
            id: 'ID1',
            name: 'Strauß',
          },
        ],
        liefertage: { verfuegbareLiefertage: ['2022-04-08'] },
        preisbereich: null,
      },
    };
    expect(result).toEqual(expected);
  });

  it('should return store state produkteFilter with cmsProdukteFilter and userProdukteFilter via getter', () => {
    const cmsProdukteFilter: QueryParameters = {
      farben: ['rot', 'blau'],
      blumensorten: ['Rosen', 'Nelken'],
      klassifikation3: ['12'],
      produktNummern: ['039120391', '7489579483'],
      produktnummernVerwendung: 'selektionsbasis',
      limit: 6,
      minPreis: 14.99,
      maxPreis: 44.99,
    };
    const state = { ...defaultState, cmsProdukteFilter: cmsProdukteFilter };

    const result: ProdukteFilter = getters.getProdukteFilter(
      state,
      {
        getSelectedSorten: [{ name: 'Nelken' }],
        getSelectedFarben: [{ name: 'rot' }],
        getSelectedKlassifikationen: [
          {
            id: 'ID1',
            name: 'Strauß',
          },
        ],
        getSelectedLiefertage: [{ name: '2022-04-08' }],
      },
      rootState,
      {}
    );

    const expected: ProdukteFilter = {
      cmsProdukteFilter: {
        blumensorten: [{ name: 'Rosen' }, { name: 'Nelken' }],
        farben: [{ name: 'rot' }, { name: 'blau' }],
        klassifikationen: [{ id: '12', name: '12' }],
        preisbereich: {
          maxPreis: {
            bruttoBetrag: 44.99,
            waehrung: 'EUR',
          },
          minPreis: {
            bruttoBetrag: 14.99,
            waehrung: 'EUR',
          },
        },
        limit: 6,
        produktNummern: [{ nummer: '039120391' }, { nummer: '7489579483' }],
        produktnummernVerwendung: 'selektionsbasis',
      },
      userProdukteFilter: {
        sorten: [{ name: 'Nelken' }],
        farben: [{ name: 'rot' }],
        klassifikationen: [
          {
            id: 'ID1',
            name: 'Strauß',
          },
        ],
        liefertage: { verfuegbareLiefertage: ['2022-04-08'] },
        preisbereich: null,
      },
    };
    expect(result).toEqual(expected);
  });

  it('getAnzahlSelectedFilterwerte should return correct anzahl of selected filterwerte per filter', () => {
    const result: ProdukteFilter = getters.getAnzahlSelectedFilterwerte(
      defaultState,
      {
        getSelectedSorten: [{ name: 'Nelken' }],
        getSelectedFarben: [{ name: 'rot' }, { name: 'blau' }],
        getSelectedKlassifikationen: [],
        getSelectedLiefertage: [{ name: '2022-04-09' }],
      },
      rootState,
      {}
    );

    const expected: AnzahlSelectedFilterwert = {
      [FilterTyp.SORTE]: 1,
      [FilterTyp.FARBE]: 2,
      [FilterTyp.PRODUKTART]: 0,
      [FilterTyp.VERFUEGBARKEIT]: 1,
    };
    expect(result).toEqual(expected);
  });

  it('getVeraenderteFilterwerte should return correct states for filter with changed filterwerte', () => {
    const result: ProdukteFilter = getters.getVeraenderteFilterwerte(
      defaultState,
      {
        getAnzahlSelectedFilterwerte: {
          [FilterTyp.SORTE]: 1,
          [FilterTyp.FARBE]: 0,
          [FilterTyp.PRODUKTART]: 12,
          [FilterTyp.VERFUEGBARKEIT]: 0,
        },
      },
      rootState,
      {}
    );

    const expected: VeraenderteFilterwerte = {
      [FilterTyp.SORTE]: true,
      [FilterTyp.FARBE]: false,
      [FilterTyp.PRODUKTART]: true,
      [FilterTyp.VERFUEGBARKEIT]: false,
      [FilterTyp.PREIS]: false,
    };
    expect(result).toEqual(expected);
  });

  it('getAnzahlVerfuegbareFilterwerte should return correct anzahl of verfuegbare filterwerte per filter', () => {
    const result: ProdukteFilter = getters.getAnzahlVerfuegbareFilterwerte(
      defaultState,
      {
        getSorten: [{ name: 'Nelken', selected: false, lesbarerName: 'Nelken', filtertyp: FilterTyp.SORTE }],
        getFarben: [
          { name: 'rot', selected: false, lesbarerName: 'rot', filtertyp: FilterTyp.FARBE },
          { name: 'blau', selected: false, lesbarerName: 'blau', filtertyp: FilterTyp.FARBE },
        ],
        getKlassifikationen: [],
        getLiefertage: [
          { name: '2022-04-09', selected: false, lesbarerName: '09.04.2022', filtertyp: FilterTyp.VERFUEGBARKEIT },
        ],
      },
      rootState,
      {}
    );

    const expected: AnzahlSelectedFilterwert = {
      [FilterTyp.SORTE]: 1,
      [FilterTyp.FARBE]: 2,
      [FilterTyp.PRODUKTART]: 0,
      [FilterTyp.VERFUEGBARKEIT]: 1,
    };
    expect(result).toEqual(expected);
  });

  it('getZeigeFilter should return correct state of zeige filter per filter', () => {
    const result: ProdukteFilter = getters.getZeigeFilter(
      defaultState,
      {
        getAnzahlVerfuegbareFilterwerte: {
          [FilterTyp.SORTE]: 1,
          [FilterTyp.FARBE]: 2,
          [FilterTyp.PRODUKTART]: 12,
          [FilterTyp.VERFUEGBARKEIT]: 0,
        },
        getSelectedSorten: [],
        getSelectedFarben: [],
        getSelectedKlassifikationen: [],
        getSelectedLiefertage: [],
      },
      rootState,
      {}
    );

    const expected: ZeigeFilter = {
      [FilterTyp.SORTE]: true,
      [FilterTyp.FARBE]: true,
      [FilterTyp.PRODUKTART]: true,
      [FilterTyp.VERFUEGBARKEIT]: false,
      // Should be true, if FilterTyp Preis is not disabled
      [FilterTyp.PREIS]: false,
    };
    expect(result).toEqual(expected);
  });

  it('getZeigeFilter should return correct state of zeige filter per filter if werte selected', () => {
    const result: ProdukteFilter = getters.getZeigeFilter(
      defaultState,
      {
        getAnzahlVerfuegbareFilterwerte: {
          [FilterTyp.SORTE]: 1,
          [FilterTyp.FARBE]: 1,
          [FilterTyp.PRODUKTART]: 12,
          [FilterTyp.VERFUEGBARKEIT]: 0,
        },
        getSelectedSorten: [{ name: 'Sorte A' }],
        getSelectedFarben: [{ name: 'Farbe A' }],
        getSelectedKlassifikationen: [{ name: 'Klassifikation A' }, { name: 'Klassifikation B' }],
        getSelectedLiefertage: [{ name: 'Liefertag A' }],
      },
      rootState,
      {}
    );

    const expected: ZeigeFilter = {
      [FilterTyp.SORTE]: true,
      [FilterTyp.FARBE]: true,
      [FilterTyp.PRODUKTART]: true,
      [FilterTyp.VERFUEGBARKEIT]: true,
      // Should be true, if FilterTyp Preis is not disabled
      [FilterTyp.PREIS]: false,
    };
    expect(result).toEqual(expected);
  });

  it('getSelectedFilterwerte should return correct selected filterwerte', () => {
    const result: ProdukteFilter = getters.getSelectedFilterwerte(
      defaultState,
      {
        getSelectedSorten: [{ name: 'Nelken', lesbarerName: 'Nelken', selected: true, filtertyp: FilterTyp.SORTE }],
        getSelectedFarben: [
          { name: 'rot', lesbarerName: 'Rot', selected: true, filtertyp: FilterTyp.FARBE },
          { name: 'blau', lesbarerName: 'Blau', selected: true, filtertyp: FilterTyp.FARBE },
        ],
        getSelectedKlassifikationen: [
          {
            id: '3a',
            name: 'Trockenblumen',
            lesbarerName: 'Trockenblumen',
            selected: true,
            filtertyp: FilterTyp.PRODUKTART,
          },
          {
            id: '4b',
            name: 'Marktware',
            lesbarerName: 'Marktware',
            selected: true,
            filtertyp: FilterTyp.PRODUKTART,
          },
        ],
        getSelectedLiefertage: [
          {
            name: '2022-04-08',
            lesbarerName: 'Morgen',
            selected: true,
            filtertyp: FilterTyp.VERFUEGBARKEIT,
          },
        ],
      },
      rootState,
      {}
    );

    const expected: (Filterwert | FilterwertMitId)[] = [
      { name: 'Nelken', lesbarerName: 'Nelken', selected: true, filtertyp: FilterTyp.SORTE },
      { name: 'rot', lesbarerName: 'Rot', selected: true, filtertyp: FilterTyp.FARBE },
      { name: 'blau', lesbarerName: 'Blau', selected: true, filtertyp: FilterTyp.FARBE },
      {
        id: '3a',
        name: 'Trockenblumen',
        lesbarerName: 'Trockenblumen',
        selected: true,
        filtertyp: FilterTyp.PRODUKTART,
      },
      {
        id: '4b',
        name: 'Marktware',
        lesbarerName: 'Marktware',
        selected: true,
        filtertyp: FilterTyp.PRODUKTART,
      },
      {
        name: '2022-04-08',
        lesbarerName: 'Morgen',
        selected: true,
        filtertyp: FilterTyp.VERFUEGBARKEIT,
      },
    ];
    expect(result).toEqual(expected);
  });
});

describe('ProduktfilterState.ts - Mutations', () => {
  beforeEach(() => {
    defaultState = {
      cmsProdukteFilter: null,
      sorten: testFilterwerteSorten(),
      farben: testFilterwerteFarben(),
      preisBereich: testFilterwertePreisbereich(),
      klassifikationen: testFilterwerteKlassifikationen(),
      liefertage: testFilterwerteLiefertage(),
    };
  });

  it('should set Sorten via setter', () => {
    const sorten = new Map<string, Filterwert>()
      .set('Eukalyptus', {
        name: 'Eukalyptus',
        lesbarerName: 'Eukalyptus',
        selected: true,
        filtertyp: FilterTyp.SORTE,
      })
      .set('Tulpen', {
        name: 'Tulpen',
        lesbarerName: 'Tulpen',
        selected: true,
        filtertyp: FilterTyp.SORTE,
      });

    expect(defaultState.sorten.get('Eukalyptus')).toBeUndefined();
    expect(defaultState.sorten.get('Tulpen')).toBeUndefined();

    mutations.setSorten(defaultState, sorten);

    expect(defaultState.sorten.get('Eukalyptus')).toEqual({
      filtertyp: 'SORTE',
      lesbarerName: 'Eukalyptus',
      name: 'Eukalyptus',
      selected: true,
    });
    expect(defaultState.sorten.get('Tulpen')).toEqual({
      filtertyp: 'SORTE',
      lesbarerName: 'Tulpen',
      name: 'Tulpen',
      selected: true,
    });
  });

  it('should set Farben via setter', () => {
    const farben = new Map<string, Filterwert>()
      .set('Lila', {
        name: 'Lila',
        lesbarerName: 'Lila',
        selected: true,
        filtertyp: FilterTyp.FARBE,
      })
      .set('Bunt', {
        name: 'Bunt',
        lesbarerName: 'Bunt',
        selected: true,
        filtertyp: FilterTyp.FARBE,
      });

    expect(defaultState.farben.get('Lila')).toBeUndefined();
    expect(defaultState.farben.get('Bunt')).toBeUndefined();

    mutations.setFarben(defaultState, farben);

    expect(defaultState.farben.get('Lila')).toEqual({
      filtertyp: 'FARBE',
      lesbarerName: 'Lila',
      name: 'Lila',
      selected: true,
    });
    expect(defaultState.farben.get('Bunt')).toEqual({
      filtertyp: 'FARBE',
      lesbarerName: 'Bunt',
      name: 'Bunt',
      selected: true,
    });
  });

  it('should set Klassifikation via setter', () => {
    const klassifikation = new Map<string, FilterwertMitId>().set('3a', {
      id: '3a',
      name: 'Trockenblume',
      lesbarerName: 'Trockenblume',
      selected: true,
      filtertyp: FilterTyp.PRODUKTART,
    });

    expect(defaultState.klassifikationen.get('3a')).toBeUndefined();

    mutations.setKlassifikation(defaultState, klassifikation);

    expect(defaultState.klassifikationen.get('3a')).toEqual({
      id: '3a',
      filtertyp: 'PRODUKTART',
      lesbarerName: 'Trockenblume',
      name: 'Trockenblume',
      selected: true,
    });
  });

  it('should set Liefertage via setter', () => {
    const liefertage = new Map<string, Filterwert>().set('2022-04-19', {
      name: '2022-04-19',
      lesbarerName: 'Morgen',
      selected: true,
      filtertyp: FilterTyp.VERFUEGBARKEIT,
    });

    expect(defaultState.liefertage.get('2022-04-19')).toBeUndefined();

    mutations.setLiefertage(defaultState, liefertage);

    expect(defaultState.liefertage.get('2022-04-19')).toEqual({
      name: '2022-04-19',
      lesbarerName: 'Morgen',
      selected: true,
      filtertyp: FilterTyp.VERFUEGBARKEIT,
    });
  });

  it('should set Preisbereich via setter', () => {
    const preisbereich: PreisbereichDTO = {
      maxPreis: {
        bruttoBetrag: 80,
        waehrung: 'EUR',
      },
      minPreis: {
        bruttoBetrag: 20,
        waehrung: 'EUR',
      },
      selectedMaxPreis: 40,
      selectedMinPreis: 30,
    };

    expect(defaultState.preisBereich).toEqual({
      maxPreis: {
        bruttoBetrag: 55,
        waehrung: 'EUR',
      },
      minPreis: {
        bruttoBetrag: 2,
        waehrung: 'EUR',
      },
      selectedMaxPreis: 20,
      selectedMinPreis: 2,
    });
    mutations.setPreisBereich(defaultState, preisbereich);
    expect(defaultState.preisBereich).toEqual({
      maxPreis: {
        bruttoBetrag: 80,
        waehrung: 'EUR',
      },
      minPreis: {
        bruttoBetrag: 20,
        waehrung: 'EUR',
      },
      selectedMaxPreis: 40,
      selectedMinPreis: 30,
    });
  });

  it('should set Filterwert via setter', () => {
    const filterwerte: (Filterwert | FilterwertMitId)[] = [
      {
        name: 'Lilien',
        lesbarerName: 'Lilien',
        selected: true,
        filtertyp: FilterTyp.SORTE,
      },
      {
        name: 'rot',
        lesbarerName: 'rot',
        selected: false,
        filtertyp: FilterTyp.FARBE,
      },
      {
        id: 'ID1',
        name: 'Strauß',
        lesbarerName: 'Strauß',
        selected: false,
        filtertyp: FilterTyp.PRODUKTART,
      },
      {
        name: '2022-04-09',
        lesbarerName: '09.04.2022',
        selected: true,
        filtertyp: FilterTyp.VERFUEGBARKEIT,
      },
    ];

    expect(defaultState.sorten.get('Lilien')?.selected).toBeFalsy();
    expect(defaultState.farben.get('rot')?.selected).toBeTruthy();
    expect(defaultState.klassifikationen.get('ID1')?.selected).toBeTruthy();
    expect(defaultState.liefertage.get('2022-04-09')?.selected).toBeFalsy();

    filterwerte.forEach((filterwert) => {
      mutations.setFilterwert(defaultState, filterwert);
    });
    expect(defaultState.sorten.get('Lilien')?.selected).toBeTruthy();
    expect(defaultState.farben.get('rot')?.selected).toBeFalsy();
    expect(defaultState.klassifikationen.get('ID1')?.selected).toBeFalsy();
    expect(defaultState.liefertage.get('2022-04-09')?.selected).toBeTruthy();
  });

  it('should setAlleFilterZuruecksetzen via setter', () => {
    expect(defaultState.sorten.get('Nelken')?.selected).toBeTruthy();
    expect(defaultState.farben.get('rot')?.selected).toBeTruthy();
    expect(defaultState.klassifikationen.get('ID1')?.selected).toBeTruthy();
    expect(defaultState.liefertage.get('2022-04-08')?.selected).toBeTruthy();

    mutations.setAlleFilterZuruecksetzen(defaultState);

    expect(defaultState.sorten.get('Nelken')?.selected).toBeFalsy();
    expect(defaultState.farben.get('rot')?.selected).toBeFalsy();
    expect(defaultState.klassifikationen.get('ID1')?.selected).toBeFalsy();
    expect(defaultState.liefertage.get('2022-04-08')?.selected).toBeFalsy();
  });

  it('should set CmsFilter via setter', () => {
    const queryParameters: QueryParameters = {
      farben: ['blau'],
      limit: 6,
    };

    expect(defaultState.cmsProdukteFilter).toEqual(null);
    mutations.setCmsFilter(defaultState, queryParameters);
    expect(defaultState.cmsProdukteFilter).toEqual({ farben: ['blau'], limit: 6 });
  });
});

describe('ProduktfilterState.ts - Actions', () => {
  beforeEach(() => {
    defaultState = {
      cmsProdukteFilter: null,
      sorten: new Map<string, Filterwert>(),
      farben: new Map<string, Filterwert>(),
      preisBereich: testFilterwertePreisbereich(),
      klassifikationen: new Map<string, FilterwertMitId>(),
      liefertage: new Map<string, Filterwert>(),
    };
  });

  it('should updateVerfuegbareFilterwerte via action', (done) => {
    const filterstatusUndVerfuegbareFilterwerte: FilterstatusUndVerfuegbareFilterwerte = {
      verfuegbareFilterwerte: {
        sorten: [{ name: 'Bromelie' }, { name: 'Chrysanthemen' }, { name: 'Eukalyptus' }],
        farben: [{ name: 'rot' }, { name: 'weiss' }],
        preisbereich: {
          minPreis: { bruttoBetrag: 39.99, waehrung: 'EUR' },
          maxPreis: { bruttoBetrag: 39.99, waehrung: 'EUR' },
        },
        klassifikationen: [
          { name: 'Strauß', id: '11' },
          { name: 'Topf', id: '12' },
        ],
        liefertage: {
          verfuegbareLiefertage: [
            format(addDays(new Date(), 1), 'yyyy-MM-dd'),
            format(addDays(new Date(), 2), 'yyyy-MM-dd'),
            format(addDays(new Date(), 3), 'yyyy-MM-dd'),
          ],
        },
      },
      istAktionFilterZuruecksetzen: false,
    };

    const expectedSorten = new Map<string, Filterwert>()
      .set('Bromelie', {
        filtertyp: FilterTyp.SORTE,
        lesbarerName: 'Bromelie',
        name: 'Bromelie',
        selected: false,
      })
      .set('Chrysanthemen', {
        filtertyp: FilterTyp.SORTE,
        lesbarerName: 'Chrysanthemen',
        name: 'Chrysanthemen',
        selected: false,
      })
      .set('Eukalyptus', {
        filtertyp: FilterTyp.SORTE,
        lesbarerName: 'Eukalyptus',
        name: 'Eukalyptus',
        selected: false,
      });

    const expectedFarben = new Map<string, Filterwert>()
      .set('rot', {
        filtertyp: FilterTyp.FARBE,
        lesbarerName: 'Rot',
        name: 'rot',
        selected: false,
        sortIndex: 5,
      })
      .set('weiss', {
        filtertyp: FilterTyp.FARBE,
        lesbarerName: 'Weiß',
        name: 'weiss',
        selected: false,
        sortIndex: 9,
      });

    const expectedKlassifikationen = new Map<string, FilterwertMitId>()
      .set('11', {
        id: '11',
        filtertyp: FilterTyp.PRODUKTART,
        lesbarerName: 'Strauß',
        name: 'Strauß',
        selected: false,
      })
      .set('12', {
        id: '12',
        filtertyp: FilterTyp.PRODUKTART,
        lesbarerName: 'Topf',
        name: 'Topf',
        selected: false,
      });

    const expectedLiefertage = new Map<string, Filterwert>()
      .set(format(addDays(new Date(), 1), 'yyyy-MM-dd'), {
        filtertyp: FilterTyp.VERFUEGBARKEIT,
        lesbarerName: 'Morgen',
        name: format(addDays(new Date(), 1), 'yyyy-MM-dd'),
        selected: false,
      })
      .set(format(addDays(new Date(), 2), 'yyyy-MM-dd'), {
        filtertyp: FilterTyp.VERFUEGBARKEIT,
        lesbarerName: 'Übermorgen',
        name: format(addDays(new Date(), 2), 'yyyy-MM-dd'),
        selected: false,
      })
      .set(format(addDays(new Date(), 3), 'yyyy-MM-dd'), {
        filtertyp: FilterTyp.VERFUEGBARKEIT,
        lesbarerName: format(addDays(new Date(), 3), 'dd.MM.yyyy'),
        name: format(addDays(new Date(), 3), 'yyyy-MM-dd'),
        selected: false,
      });

    const expectedPreisbereich = {
      maxPreis: { bruttoBetrag: 39.99, waehrung: 'EUR' },
      minPreis: { bruttoBetrag: 39.99, waehrung: 'EUR' },
    };

    testAction({
      action: actions.updateVerfuegbareFilterwerte,
      state: defaultState,
      getters: {
        getAnzahlSelectedFilterwerte: {
          [FilterTyp.SORTE]: 0,
          [FilterTyp.FARBE]: 0,
          [FilterTyp.PRODUKTART]: 0,
          [FilterTyp.VERFUEGBARKEIT]: 0,
        },
      },
      payload: filterstatusUndVerfuegbareFilterwerte,
      expectedMutations: [
        { type: 'setSorten', payload: expectedSorten },
        { type: 'setFarben', payload: expectedFarben },
        { type: 'setKlassifikation', payload: expectedKlassifikationen },
        { type: 'setLiefertage', payload: expectedLiefertage },
        { type: 'setPreisBereich', payload: expectedPreisbereich },
      ],
      done,
    });
  });

  it('should updateVerfuegbareFilterwerte when filterwerte already selected via action', (done) => {
    const filterstatusUndVerfuegbareFilterwerte: FilterstatusUndVerfuegbareFilterwerte = {
      verfuegbareFilterwerte: {
        sorten: [{ name: 'Eukalyptus' }, { name: 'Lilien' }, { name: 'Nelken' }],
        farben: [{ name: 'rot' }, { name: 'weiss' }],
        preisbereich: {
          minPreis: { bruttoBetrag: 39.99, waehrung: 'EUR' },
          maxPreis: { bruttoBetrag: 39.99, waehrung: 'EUR' },
        },
        klassifikationen: [
          { name: 'Strauß', id: '11' },
          { name: 'Topf', id: '12' },
        ],
        liefertage: {
          verfuegbareLiefertage: [
            format(addDays(new Date(), 1), 'yyyy-MM-dd'),
            format(addDays(new Date(), 2), 'yyyy-MM-dd'),
            format(addDays(new Date(), 3), 'yyyy-MM-dd'),
          ],
        },
      },
      istAktionFilterZuruecksetzen: false,
    };

    const expectedSorten = new Map<string, Filterwert>()
      .set('Eukalyptus', {
        filtertyp: FilterTyp.SORTE,
        lesbarerName: 'Eukalyptus',
        name: 'Eukalyptus',
        selected: false,
      })
      .set('Lilien', {
        filtertyp: FilterTyp.SORTE,
        lesbarerName: 'Lilien',
        name: 'Lilien',
        selected: false,
      })
      .set('Nelken', {
        filtertyp: FilterTyp.SORTE,
        lesbarerName: 'Nelken',
        name: 'Nelken',
        selected: true,
      });

    const expectedFarben = new Map<string, Filterwert>()
      .set('rot', {
        filtertyp: FilterTyp.FARBE,
        lesbarerName: 'Rot',
        name: 'rot',
        selected: true,
        sortIndex: 5,
      })
      .set('weiss', {
        filtertyp: FilterTyp.FARBE,
        lesbarerName: 'Weiß',
        name: 'weiss',
        selected: false,
        sortIndex: 9,
      });

    const expectedKlassifikationen = new Map<string, FilterwertMitId>()
      .set('11', {
        id: '11',
        filtertyp: FilterTyp.PRODUKTART,
        lesbarerName: 'Strauß',
        name: 'Strauß',
        selected: false,
      })
      .set('12', {
        id: '12',
        filtertyp: FilterTyp.PRODUKTART,
        lesbarerName: 'Topf',
        name: 'Topf',
        selected: false,
      })
      .set('ID1', {
        id: 'ID1',
        name: 'Strauß',
        lesbarerName: 'Strauß',
        selected: true,
        filtertyp: FilterTyp.PRODUKTART,
      });

    const expectedLiefertage = new Map<string, Filterwert>()
      .set(format(addDays(new Date(), 1), 'yyyy-MM-dd'), {
        filtertyp: FilterTyp.VERFUEGBARKEIT,
        lesbarerName: 'Morgen',
        name: format(addDays(new Date(), 1), 'yyyy-MM-dd'),
        selected: false,
      })
      .set(format(addDays(new Date(), 2), 'yyyy-MM-dd'), {
        filtertyp: FilterTyp.VERFUEGBARKEIT,
        lesbarerName: 'Übermorgen',
        name: format(addDays(new Date(), 2), 'yyyy-MM-dd'),
        selected: false,
      })
      .set(format(addDays(new Date(), 3), 'yyyy-MM-dd'), {
        filtertyp: FilterTyp.VERFUEGBARKEIT,
        lesbarerName: format(addDays(new Date(), 3), 'dd.MM.yyyy'),
        name: format(addDays(new Date(), 3), 'yyyy-MM-dd'),
        selected: false,
      })
      .set('2022-04-08', {
        filtertyp: FilterTyp.VERFUEGBARKEIT,
        lesbarerName: '08.04.2022',
        name: '2022-04-08',
        selected: true,
      });

    const expectedPreisbereich = {
      maxPreis: { bruttoBetrag: 39.99, waehrung: 'EUR' },
      minPreis: { bruttoBetrag: 39.99, waehrung: 'EUR' },
    };

    testAction({
      action: actions.updateVerfuegbareFilterwerte,
      state: {
        ...defaultState,
        sorten: testFilterwerteSorten(),
        farben: testFilterwerteFarben(),
        klassifikationen: testFilterwerteKlassifikationen(),
        liefertage: testFilterwerteLiefertage(),
      },
      getters: {
        getAnzahlSelectedFilterwerte: {
          [FilterTyp.SORTE]: 0,
          [FilterTyp.FARBE]: 0,
          [FilterTyp.PRODUKTART]: 0,
          [FilterTyp.VERFUEGBARKEIT]: 0,
        },
      },
      payload: filterstatusUndVerfuegbareFilterwerte,
      expectedMutations: [
        { type: 'setSorten', payload: expectedSorten },
        { type: 'setFarben', payload: expectedFarben },
        { type: 'setKlassifikation', payload: expectedKlassifikationen },
        { type: 'setLiefertage', payload: expectedLiefertage },
        { type: 'setPreisBereich', payload: expectedPreisbereich },
      ],
      done,
    });
  });
});
