import { createApp } from 'vue';
import { ProduktFilterUiStatus, useProduktFilterUiStatus } from '@/main/frontend/app/composables/ProduktFilterUiStatus';
import { App } from '@vue/runtime-core';
import { createStore, Store } from 'vuex';
import { FilterTyp } from '@/main/frontend/app/model/FilterTyp';
import {
  AnzahlSelectedFilterwert,
  VeraenderteFilterwerte,
  ZeigeFilter,
} from '@/main/frontend/app/store/ProduktfilterState';

export function withSetup<ComposableReturnType, State>(
  composable: () => ComposableReturnType,
  store?: Store<State>
): {
  result: ComposableReturnType;
  app: App;
} {
  let result: ComposableReturnType;
  const app = createApp({
    setup() {
      result = composable();
      // suppress missing template warning
      // eslint-disable-next-line @typescript-eslint/no-empty-function
      return () => {};
    },
  });
  if (store) app.use(store);
  app.mount(document.createElement('div'));
  // return the result and the app instance
  // for testing provide / unmount
  // eslint-disable-next-line @typescript-eslint/ban-ts-comment
  // @ts-ignore
  return { result, app };
}

describe('ProduktFilterUiStatus.ts', () => {
  it('should return null for anzuzeigenderFilter, when initialized', () => {
    const { result } = withSetup<ProduktFilterUiStatus, unknown>(useProduktFilterUiStatus, createMockStore());
    expect(result.uiStatus.anzuzeigenderFilter).toEqual(null);
  });

  it('should return correct list in correct order of anzuzeigenderFilter', () => {
    const zeigeFilter: ZeigeFilter = {
      [FilterTyp.SORTE]: true,
      [FilterTyp.FARBE]: false,
      [FilterTyp.PRODUKTART]: true,
      [FilterTyp.VERFUEGBARKEIT]: true,
      [FilterTyp.PREIS]: false,
    };
    const { result } = withSetup<ProduktFilterUiStatus, null>(
      useProduktFilterUiStatus,
      createMockStore({ zeigeFilter })
    );
    expect(result.anzuzeigendeFilterListe.value).toEqual([
      {
        name: 'Tag der Lieferung',
        sichtbar: true,
        filterTyp: FilterTyp.VERFUEGBARKEIT,
      },
      {
        name: 'Sorten',
        sichtbar: true,
        filterTyp: FilterTyp.SORTE,
      },
      {
        name: 'Produktarten',
        sichtbar: true,
        filterTyp: FilterTyp.PRODUKTART,
      },
    ]);
  });

  it('should return correct veraenderteFilterWerte', () => {
    const veraenderteFilterwerte: VeraenderteFilterwerte = {
      [FilterTyp.SORTE]: false,
      [FilterTyp.FARBE]: true,
      [FilterTyp.PRODUKTART]: true,
      [FilterTyp.VERFUEGBARKEIT]: false,
      [FilterTyp.PREIS]: false,
    };
    const { result } = withSetup<ProduktFilterUiStatus, null>(
      useProduktFilterUiStatus,
      createMockStore({ veraenderteFilterwerte })
    );
    expect(result.veraenderteFilterwerte.value).toEqual({
      [FilterTyp.SORTE]: false,
      [FilterTyp.FARBE]: true,
      [FilterTyp.PRODUKTART]: true,
      [FilterTyp.VERFUEGBARKEIT]: false,
      [FilterTyp.PREIS]: false,
    });
  });

  it('should return null for selected filterwerte of preis', () => {
    const { result } = withSetup<ProduktFilterUiStatus, null>(useProduktFilterUiStatus, createMockStore());
    expect(result.gebeAnzahlSelectedFilterwerte(FilterTyp.PREIS)).toEqual(null);
  });

  it.each<[FilterTyp, number]>([
    [FilterTyp.SORTE, 0],
    [FilterTyp.FARBE, 1],
    [FilterTyp.PRODUKTART, 5],
    [FilterTyp.VERFUEGBARKEIT, 20],
  ])('should return correct anzahl for selected filterwerte of %s', (filtertyp, expectedAnzahl) => {
    const anzahlSelectedFilterwerte: AnzahlSelectedFilterwert = {
      [FilterTyp.SORTE]: filtertyp === FilterTyp.SORTE ? expectedAnzahl : 0,
      [FilterTyp.FARBE]: filtertyp === FilterTyp.FARBE ? expectedAnzahl : 0,
      [FilterTyp.PRODUKTART]: filtertyp === FilterTyp.PRODUKTART ? expectedAnzahl : 0,
      [FilterTyp.VERFUEGBARKEIT]: filtertyp === FilterTyp.VERFUEGBARKEIT ? expectedAnzahl : 0,
    };
    const { result } = withSetup<ProduktFilterUiStatus, null>(
      useProduktFilterUiStatus,
      createMockStore({ anzahlSelectedFilterwerte })
    );
    expect(result.gebeAnzahlSelectedFilterwerte(filtertyp)).toEqual(expectedAnzahl);
  });

  interface MockStoreProps {
    zeigeFilter?: ZeigeFilter;
    veraenderteFilterwerte?: VeraenderteFilterwerte;
    anzahlSelectedFilterwerte?: AnzahlSelectedFilterwert;
  }

  const initialMockStoreProps: MockStoreProps = {
    zeigeFilter: undefined,
    veraenderteFilterwerte: undefined,
    anzahlSelectedFilterwerte: undefined,
  };

  function createMockStore(props: MockStoreProps = initialMockStoreProps): Store<null> {
    const { zeigeFilter, veraenderteFilterwerte, anzahlSelectedFilterwerte } = props;
    return createStore({
      getters: {
        'ProduktfilterState/getVeraenderteFilterwerte': (): VeraenderteFilterwerte => ({
          [FilterTyp.SORTE]: false,
          [FilterTyp.FARBE]: false,
          [FilterTyp.PRODUKTART]: false,
          [FilterTyp.VERFUEGBARKEIT]: false,
          [FilterTyp.PREIS]: false,
          ...veraenderteFilterwerte,
        }),
        'ProduktfilterState/getZeigeFilter': (): ZeigeFilter => ({
          [FilterTyp.SORTE]: true,
          [FilterTyp.FARBE]: true,
          [FilterTyp.PRODUKTART]: true,
          [FilterTyp.VERFUEGBARKEIT]: true,
          [FilterTyp.PREIS]: true,
          ...zeigeFilter,
        }),
        'ProduktfilterState/getAnzahlSelectedFilterwerte': (): AnzahlSelectedFilterwert => ({
          [FilterTyp.SORTE]: 0,
          [FilterTyp.FARBE]: 0,
          [FilterTyp.PRODUKTART]: 0,
          [FilterTyp.VERFUEGBARKEIT]: 0,
          ...anzahlSelectedFilterwerte,
        }),
      },
    });
  }
});
