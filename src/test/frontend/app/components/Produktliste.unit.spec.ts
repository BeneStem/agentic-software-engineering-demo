import { shallowMount } from '@vue/test-utils';
import Produktliste from '@/main/frontend/app/components/Produktliste.vue';
import { testProduktListe } from '@/test/frontend/utils/TestProduktHelper';

describe('Produktliste.vue', () => {
  it('Rendert eine Produktliste', () => {
    // Given
    const wrapper = shallowMount(Produktliste, {
      props: {
        produkte: testProduktListe,
        trackingname: 'trackingname',
        trackingnummer: 1
      }
    });

    // Then
    expect(wrapper.element).toMatchSnapshot();
  });
});
