import { shallowMount } from '@vue/test-utils';
import MiniProduktlisteView from '@/main/frontend/app/views/MiniProduktlisteView.vue';
import { createStore } from 'vuex';

describe('MiniProduktlisteView', () => {
  it('keine überschrift wenn undefined', () => {
    // Given
    const store = createMockStore(false, false, false, true);
    const wrapper = shallowMount(MiniProduktlisteView, {
      global: {
        plugins: [store],
      },
    });

    // Then
    expect(wrapper.element).toMatchSnapshot();
  });

  it('zeigt ueberschrift wenn defined', () => {
    // Given
    const store = createMockStore(true, false, false, true);
    const wrapper = shallowMount(MiniProduktlisteView, {
      global: {
        plugins: [store],
      },
    });

    // Then
    expect(wrapper.element).toMatchSnapshot();
  });

  it('zeigt alle anzeigen link wenn text und ziel defined', () => {
    // Given
    const store = createMockStore(true, true, true, true);
    const wrapper = shallowMount(MiniProduktlisteView, {
      global: {
        plugins: [store],
      },
    });

    // Then
    expect(wrapper.element).toMatchSnapshot();
  });

  it('kein alle anzeigen link wenn ziel fehlt', () => {
    // Given
    const store = createMockStore(true, true, false, true);
    const wrapper = shallowMount(MiniProduktlisteView, {
      global: {
        plugins: [store],
      },
    });

    // Then
    expect(wrapper.element).toMatchSnapshot();
  });

  it('kein alle anzeigen link wenn text fehlt', () => {
    // Given
    const store = createMockStore(true, false, true, true);
    const wrapper = shallowMount(MiniProduktlisteView, {
      global: {
        plugins: [store],
      },
    });

    // Then
    expect(wrapper.element).toMatchSnapshot();
  });

  it('Sendet beim Klick auf den alle anzeigen Link ein Event an GA', () => {
    // Given
    const store = createMockStore(true, true, true, true);
    const wrapper = shallowMount(MiniProduktlisteView, {
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
});

function createMockStore(
  hasUeberschrift: boolean,
  hasLinkText: boolean,
  hasLinkZiel: boolean,
  isFeatureToggleActive: boolean,
  trackingname = 'trackingname',
  trackingnummer = 1234567890
) {
  const store = createStore({
    getters: {
      'EinstellungenState/getUeberschrift': (state: any): string | undefined => {
        return hasUeberschrift ? 'überschrift' : undefined;
      },
      'ProdukteState/getProdukte': (state: any) => {
        return [{}];
      },
      'EinstellungenState/getAlleAnzeigenLinkText': (state: any): string | undefined => {
        return hasLinkText ? 'linktext' : undefined;
      },
      'EinstellungenState/getAlleAnzeigenLinkZiel': (state: any): string | undefined => {
        return hasLinkZiel ? 'linkziel' : undefined;
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
