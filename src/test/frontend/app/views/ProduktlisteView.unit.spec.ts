import { shallowMount } from '@vue/test-utils';
import ProduktlisteView from '@/main/frontend/app/views/ProduktlisteView.vue';
import { createStore } from 'vuex';

describe('ProduktlisteView.vue', () => {
  it('keine überschrift wenn undefined', () => {
    // Given
    const store = createMockStore(false, false, false, true, false);
    const wrapper = shallowMount(ProduktlisteView, {
      global: {
        plugins: [store],
      },
    });

    // Then
    expect(wrapper.element).toMatchSnapshot();
  });

  it('zeigt ueberschrift wenn defined', () => {
    // Given
    const store = createMockStore(true, false, false, true, false);
    const wrapper = shallowMount(ProduktlisteView, {
      global: {
        plugins: [store],
      },
    });

    // Then
    expect(wrapper.element).toMatchSnapshot();
  });

  it('zeigt alle anzeigen link wenn text und ziel defined', () => {
    // Given
    const store = createMockStore(true, true, true, true, false);
    const wrapper = shallowMount(ProduktlisteView, {
      global: {
        plugins: [store],
      },
    });

    // Then
    expect(wrapper.element).toMatchSnapshot();
  });

  it('kein alle anzeigen link wenn ziel fehlt', () => {
    // Given
    const store = createMockStore(true, true, false, true, false);
    const wrapper = shallowMount(ProduktlisteView, {
      global: {
        plugins: [store],
      },
    });

    // Then
    expect(wrapper.element).toMatchSnapshot();
  });

  it('kein alle anzeigen link wenn text fehlt', () => {
    // Given
    const store = createMockStore(true, false, true, true, false);
    const wrapper = shallowMount(ProduktlisteView, {
      global: {
        plugins: [store],
      },
    });

    // Then
    expect(wrapper.element).toMatchSnapshot();
  });

  it('Sendet beim Klick auf den alle anzeigen Link ein Event an GA', () => {
    // Given
    const store = createMockStore(true, true, true, true, false);
    const wrapper = shallowMount(ProduktlisteView, {
      global: {
        plugins: [store],
      },
    });

    wrapper.vm.sendTrackAlleAnzeigenKlick = jest.fn();

    // When
    wrapper.find('a').trigger('click');

    // Then
    expect(wrapper.vm.sendTrackAlleAnzeigenKlick).toHaveBeenCalled();
  });

  it('Zeigt keine Filter an, wenn Produktfilter ausgeblendet ist und Feature Toggle profuktfilter_anzeigen aus', () => {
    // Given
    const store = createMockStore(true, false, false, true, false);
    const wrapper = shallowMount(ProduktlisteView, {
      global: {
        plugins: [store],
      },
    });

    // Then
    expect(wrapper.element).toMatchSnapshot();
  });

  it('Zeigt Filter an, wenn Produktfilter nicht ausgeblendet ist und Feature Toggle profuktfilter_anzeigen an', () => {
    // Given
    const store = createMockStore(true, false, false, false, true);
    const wrapper = shallowMount(ProduktlisteView, {
      global: {
        plugins: [store],
      },
    });

    // Then
    expect(wrapper.element).toMatchSnapshot();
  });

  it('Zeigt nulltreffer meldung wenn filter aktiv und dadurch keine Produkte sichtbar', () => {
    // Given
    const store = createMockStore(true, false, false, false, true, true);
    const wrapper = shallowMount(ProduktlisteView, {
      global: {
        plugins: [store],
      },
    });
    // Then
    expect(wrapper.element).toMatchSnapshot();
  });

  it('Zeigt keineprodukte meldung wenn keine filter aktiv und trotzdem keine Produkte sichtbar', () => {
    // Given
    const store = createMockStore(true, false, false, false, true, true);
    const wrapper = shallowMount(ProduktlisteView, {
      global: {
        plugins: [store],
      },
    });

    // Then
    expect(wrapper.element).toMatchSnapshot();
  });
});

function createMockStore(
  hasUeberschrift: boolean,
  hasLinkText: boolean,
  hasLinkZiel: boolean,
  istFilterAusgeblendet: boolean,
  isFeatureToggleActive: boolean,
  hatKeineProdukte = false,
  trackingname = 'trackingname',
  trackingnummer = 1234567890
) {
  const store = createStore({
    getters: {
      'EinstellungenState/getUeberschrift': (state: any): string | undefined => {
        return hasUeberschrift ? 'überschrift' : undefined;
      },
      'ProdukteState/getProdukte': (state: any) => {
        if (hatKeineProdukte) return [];
        else {
          return [{}];
        }
      },
      'EinstellungenState/getAlleAnzeigenLinkText': (state: any): string | undefined => {
        return hasLinkText ? 'linktext' : undefined;
      },
      'EinstellungenState/getAlleAnzeigenLinkZiel': (state: any): string | undefined => {
        return hasLinkZiel ? 'linkziel' : undefined;
      },
      'EinstellungenState/getIstFilterAusgeblendet': (state: any): boolean => {
        return istFilterAusgeblendet ? true : false;
      },
      'FeatureState/getFeatureStatus':
        (state: any) =>
        (featureToggleName: string): boolean => {
          return isFeatureToggleActive;
        },
      'EinstellungenState/getTrackingname': (state: any): string => {
        return trackingname;
      },
      'EinstellungenState/getTrackingnummer': (state: any): number => {
        return trackingnummer;
      }
    },
  });
  store.dispatch = jest.fn();
  return store;
}
