import { mount, shallowMount } from '@vue/test-utils';
import MiniProduktkachel from '@/main/frontend/app/components/MiniProduktkachel.vue';
import { testProdukt, testProduktMitStreichpreis } from '@/test/frontend/utils/TestProduktHelper';
import { ProduktVerfuegbarkeit } from '@/main/frontend/app/model/ProduktVerfuegbarkeit';
import { addDays, addHours, format, subHours } from 'date-fns';
import deLocale from 'date-fns/locale/de';

describe('MiniProduktkachel', () => {
  require('vue-router').useRoute = jest.fn();
  require('vue-router').useRoute.mockImplementation(() => ({ path: '/test' }));

  const position = 1;
  const listenname = 'listenname';
  const listennummer = 1337;

  it('Rendert eine MiniProduktkachel', () => {
    // Given
    const wrapper = shallowMount(MiniProduktkachel, {
      props: {
        produkt: testProdukt,
        position,
        listenname,
        listennummer,
      },
    });

    // Then
    expect(wrapper.element).toMatchSnapshot();
  });

  it('Rendert eine MiniProduktkachel mit Streichpreis', () => {
    // Given
    const wrapper = shallowMount(MiniProduktkachel, {
      props: {
        produkt: testProduktMitStreichpreis,
        position,
        listenname,
        listennummer,
      },
    });

    // Then
    expect(wrapper.element).toMatchSnapshot();
  });

  it('Rendert eine MiniProduktkachel mit Verfügbarkeit heute bestellt, morgen da', () => {
    // Given
    const naechstmoeglicheVerfuegbarkeit: ProduktVerfuegbarkeit = {
      bestellschluss: addHours(new Date(), 2),
      liefertag: addDays(new Date(), 1),
    };
    const wrapper = mount(MiniProduktkachel, {
      props: {
        produkt: {
          ...testProdukt,
          naechstmoeglicheVerfuegbarkeit,
        },
        position,
        listenname,
        listennummer,
      },
    });

    // Then
    expect(wrapper.find('.erk_finden__mini-produktkachel-lieferaussage *').text()).toEqual(
      'Heute bestellt - morgen geliefert'
    );
  });

  it('Rendert eine MiniProduktkachel mit Verfügbarkeit heute bestellt, übermorgen da', () => {
    // Given
    const naechstmoeglicheVerfuegbarkeit: ProduktVerfuegbarkeit = {
      bestellschluss: addHours(new Date(), 2),
      liefertag: addDays(new Date(), 2),
    };
    const wrapper = mount(MiniProduktkachel, {
      props: {
        produkt: {
          ...testProdukt,
          naechstmoeglicheVerfuegbarkeit,
        },
        position,
        listenname,
        listennummer,
      },
    });

    // Then
    expect(wrapper.find('.erk_finden__mini-produktkachel-lieferaussage *').text()).toEqual(
      'Heute bestellt - übermorgen geliefert'
    );
  });

  it('Rendert eine MiniProduktkachel mit Verfügbarkeit heute bestellt, ab XX.XX da', () => {
    // Given
    const naechstmoeglicheVerfuegbarkeit: ProduktVerfuegbarkeit = {
      bestellschluss: addHours(new Date(), 2),
      liefertag: addDays(new Date(), 4),
    };
    const wrapper = mount(MiniProduktkachel, {
      props: {
        produkt: {
          ...testProdukt,
          naechstmoeglicheVerfuegbarkeit,
        },
        position,
        listenname,
        listennummer,
      },
    });

    // Then
    expect(wrapper.find('.erk_finden__mini-produktkachel-lieferaussage *').text()).toEqual(
      `Heute bestellt - ab ${format(naechstmoeglicheVerfuegbarkeit.liefertag, 'eeeeee, dd.MM.', {
        locale: deLocale,
      })} geliefert`
    );
  });

  it('Rendert eine MiniProduktkachel ohne Verfügbarkeit falls Verfügbarkeit nicht mehr gegeben', () => {
    // Given
    const naechstmoeglicheVerfuegbarkeit: ProduktVerfuegbarkeit = {
      bestellschluss: subHours(new Date(), 2),
      liefertag: addDays(new Date(), 2),
    };
    const wrapper = mount(MiniProduktkachel, {
      props: {
        produkt: {
          ...testProdukt,
          naechstmoeglicheVerfuegbarkeit,
        },
        position,
        listenname,
        listennummer,
      },
    });

    // Then
    expect(wrapper.find('.erk_finden__mini-produktkachel-lieferaussage *').text()).toEqual('');
  });

  it('Sendet beim Klick auf die Kachel ein Event an GA', () => {
    // Given
    const wrapper = shallowMount(MiniProduktkachel, {
      props: {
        produkt: testProduktMitStreichpreis,
        position,
        listenname,
        listennummer,
      },
    });

    wrapper.vm.sendProduktClickTrackingEvent = jest.fn();

    // When
    wrapper.find('a').trigger('click');

    // Then
    expect(wrapper.vm.sendProduktClickTrackingEvent).toHaveBeenCalled();
  });
});
