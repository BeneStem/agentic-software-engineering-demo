import { mount } from '@vue/test-utils';
import Produktkachel from '@/main/frontend/app/components/Produktkachel.vue';
import { testProdukt } from '@/test/frontend/utils/TestProduktHelper';

describe('ProduktTracking.ts', () => {
  beforeEach(() => {
    window.dataLayer = [];
  });

  it('Schreibt Produktdaten in den DataLayer, wenn das Produkt im sichtbaren Bereich ist', async () => {
    const preis = 15.99;
    const position = 10;
    const produkt = testProdukt;
    produkt.name = 'Test Produkt';
    produkt.nummer = '1234';
    produkt.preis = preis.toString();

    const listenname = 'Test_Produktliste';
    const listennummer = 20211223101;

    const wrapper = mount(Produktkachel, {
      props: {
        produkt: produkt,
        position: position,
        listenname: listenname,
        listennummer: listennummer,
      },
    });

    await wrapper.find('.erk_finden__produktkachel').trigger('scroll');

    const trackingData = window.dataLayer[0];
    const trackingDataItem = trackingData.view_item_list.items[0];

    expect(trackingData.event).toEqual('Ecommerce - Item List Views');
    expect(trackingData.event_name).toEqual('view_item_list');
    expect(trackingDataItem.item_name).toEqual(produkt.name);
    expect(trackingDataItem.item_id).toEqual(produkt.nummer);
    expect(trackingDataItem.price).toEqual(preis);
    expect(trackingDataItem.item_brand).toBeUndefined();
    expect(trackingDataItem.item_category).toEqual(produkt.klassifikationName);
    expect(trackingDataItem.item_category2).toBeUndefined();
    expect(trackingDataItem.item_category3).toBeUndefined();
    expect(trackingDataItem.item_variant).toBeUndefined();
    expect(trackingDataItem.item_list_name).toEqual(listenname);
    expect(trackingDataItem.item_list_id).toEqual(listennummer);
    expect(trackingDataItem.index).toEqual(position);
    expect(trackingDataItem.quantity).toEqual(1);
  });

  it('Schreibt Produktdaten in den DataLayer, wenn das Produkt geklickt wird', async () => {
    const preis = 15.99;
    const position = 10;
    const produkt = testProdukt;
    produkt.name = 'Test Produkt';
    produkt.nummer = '1234';
    produkt.preis = preis.toString();

    const listenname = 'Test_Produktliste';
    const listennummer = 20211223101;

    const wrapper = mount(Produktkachel, {
      props: {
        produkt: produkt,
        position: position,
        listenname: listenname,
        listennummer: listennummer,
      },
    });

    await wrapper.find('.erk_finden__produktkachel').trigger('click');

    const trackingData = window.dataLayer[0];
    const trackingDataItem = trackingData.select_item.items[0];

    expect(trackingData.event).toEqual('Ecommerce - Select Item');
    expect(trackingData.event_name).toEqual('select_item');
    expect(trackingDataItem.item_name).toEqual(produkt.name);
    expect(trackingDataItem.item_id).toEqual(produkt.nummer);
    expect(trackingDataItem.price).toEqual(preis);
    expect(trackingDataItem.item_brand).toBeUndefined();
    expect(trackingDataItem.item_category).toEqual(produkt.klassifikationName);
    expect(trackingDataItem.item_category2).toBeUndefined();
    expect(trackingDataItem.item_category3).toBeUndefined();
    expect(trackingDataItem.item_variant).toBeUndefined();
    expect(trackingDataItem.item_list_name).toEqual(listenname);
    expect(trackingDataItem.item_list_id).toEqual(listennummer);
    expect(trackingDataItem.index).toEqual(position);
    expect(trackingDataItem.quantity).toEqual(1);
  });
});
