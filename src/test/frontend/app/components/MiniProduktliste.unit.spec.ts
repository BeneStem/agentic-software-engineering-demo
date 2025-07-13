import { shallowMount } from '@vue/test-utils';
import MiniProduktliste from '@/main/frontend/app/components/MiniProduktliste.vue';
import { testProduktListe } from '@/test/frontend/utils/TestProduktHelper';

describe('MiniProduktliste', () => {
  it('Rendert eine MiniProduktliste', () => {
    // Given
    const wrapper = shallowMount(MiniProduktliste, {
      props: {
        produkte: testProduktListe,
        trackingname: 'trackingname',
        trackingnummer: 1,
      },
    });

    // Then
    expect(wrapper.element).toMatchSnapshot();
  });
});
