import { createStore, Store } from 'vuex';
import { Filterwert } from '@/main/frontend/app/model/Filterwert';
import { FilterTyp } from '@/main/frontend/app/model/FilterTyp';
import { App } from '@vue/runtime-core';
import { createApp } from 'vue';
import { ProduktFilterTracking, useProduktFilterTracking } from '@/main/frontend/app/composables/ProduktFilterTracking';

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

describe('ProduktTracking.ts', () => {
  beforeEach(() => {
    window.dataLayer = [];
  });

  it('Schreibt alle ausgewählten Userfilter in den DataLayer, wenn sendProduktFilterChangeTrackingEvent ausgeführt wird', () => {
    const { result } = withSetup<ProduktFilterTracking, unknown>(
      useProduktFilterTracking,
      createMockStore({
        selectedFarben: testSelectedFarben(),
        selectedSorten: testSelectedSorten(),
        selectedKlassifikationen: testSelectedKlassifikationen(),
        selectedLiefertage: testSelectedLiefertage(),
      })
    );

    result.sendProduktFilterChangeTrackingEvent();

    const trackingData = window.dataLayer[0];
    const trackingDataItem = trackingData.select_filter;

    expect(trackingData.event).toEqual('gaEvent');
    expect(trackingData.event_name).toEqual('select_filter');
    expect(trackingDataItem.filter_sorts).toEqual('Lilie,Nelken');
    expect(trackingDataItem.filter_colour).toEqual('Rot,Gelb');
    expect(trackingDataItem.filter_category).toEqual('Strauß,Marktware');
    expect(trackingDataItem.filter_availability).toEqual('Morgen,Übermorgen');
    expect(trackingDataItem.filter_price).toBeUndefined();
  });

  it('Schreibt für alle Userfilter undefined in den DataLayer, wenn sendProduktFilterChangeTrackingEvent ausgeführt wird und kein Filter ausgewählt wurde', () => {
    const { result } = withSetup<ProduktFilterTracking, unknown>(useProduktFilterTracking, createMockStore());

    result.sendProduktFilterChangeTrackingEvent();

    const trackingData = window.dataLayer[0];
    const trackingDataItem = trackingData.select_filter;

    expect(trackingData.event).toEqual('gaEvent');
    expect(trackingData.event_name).toEqual('select_filter');
    expect(trackingDataItem.filter_sorts).toBeUndefined();
    expect(trackingDataItem.filter_colour).toBeUndefined();
    expect(trackingDataItem.filter_category).toBeUndefined();
    expect(trackingDataItem.filter_availability).toBeUndefined();
    expect(trackingDataItem.filter_price).toBeUndefined();
  });

  it('Schreibt alle ausgewählten Userfilter in den DataLayer, die zu einer Nulltrefferseite geführt haben', async () => {
    const { result } = withSetup<ProduktFilterTracking, unknown>(
      useProduktFilterTracking,
      createMockStore({
        selectedFarben: testSelectedFarben(),
      })
    );

    result.sendNulltrefferTrackingEvent();

    const trackingData = window.dataLayer[0];
    const trackingDataItem = trackingData.no_results_filter_config;

    expect(trackingData.event).toEqual('gaEvent');
    expect(trackingData.event_name).toEqual('no_results_filter_config');
    expect(trackingDataItem.filter_sorts).toBeUndefined();
    expect(trackingDataItem.filter_colour).toEqual('Rot,Gelb');
    expect(trackingDataItem.filter_category).toBeUndefined();
    expect(trackingDataItem.filter_availability).toBeUndefined();
    expect(trackingDataItem.filter_price).toBeUndefined();
  });

  interface MockStoreProps {
    selectedSorten?: Filterwert[];
    selectedFarben?: Filterwert[];
    selectedKlassifikationen?: Filterwert[];
    selectedLiefertage?: Filterwert[];
  }

  const initialMockStoreProps: MockStoreProps = {
    selectedSorten: [],
    selectedFarben: [],
    selectedKlassifikationen: [],
    selectedLiefertage: [],
  };

  function createMockStore(props: MockStoreProps = initialMockStoreProps): Store<null> {
    const { selectedSorten, selectedFarben, selectedKlassifikationen, selectedLiefertage } = props;
    return createStore({
      getters: {
        'ProduktfilterState/getSelectedSorten': (): Filterwert[] | undefined => selectedSorten || [],
        'ProduktfilterState/getSelectedFarben': (): Filterwert[] | undefined => selectedFarben || [],
        'ProduktfilterState/getSelectedKlassifikationen': (): Filterwert[] | undefined =>
          selectedKlassifikationen || [],
        'ProduktfilterState/getSelectedLiefertage': (): Filterwert[] | undefined => selectedLiefertage || [],
      },
    });
  }

  function testSelectedFarben() {
    return [
      {
        name: 'rot',
        lesbarerName: 'Rot',
        selected: true,
        filtertyp: FilterTyp.FARBE,
      },
      {
        name: 'gelb',
        lesbarerName: 'Gelb',
        selected: true,
        filtertyp: FilterTyp.FARBE,
      },
    ];
  }

  function testSelectedSorten() {
    return [
      {
        name: 'Lilie',
        lesbarerName: 'Lilie',
        selected: true,
        filtertyp: FilterTyp.SORTE,
      },
      {
        name: 'Nelken',
        lesbarerName: 'Nelken',
        selected: true,
        filtertyp: FilterTyp.SORTE,
      },
    ];
  }

  function testSelectedKlassifikationen() {
    return [
      {
        name: 'Strauß',
        lesbarerName: 'Strauß',
        selected: true,
        filtertyp: FilterTyp.PRODUKTART,
      },
      {
        name: 'Marktware',
        lesbarerName: 'Marktware',
        selected: true,
        filtertyp: FilterTyp.PRODUKTART,
      },
    ];
  }

  function testSelectedLiefertage() {
    return [
      {
        name: '2022-04-08',
        lesbarerName: 'Morgen',
        selected: true,
        filtertyp: FilterTyp.VERFUEGBARKEIT,
      },
      {
        name: '2022-04-09',
        lesbarerName: 'Übermorgen',
        selected: true,
        filtertyp: FilterTyp.VERFUEGBARKEIT,
      },
    ];
  }
});
