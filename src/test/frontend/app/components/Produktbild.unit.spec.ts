import { mount } from '@vue/test-utils';
import Produktbild from '@/main/frontend/app/components/Produktbild.vue';

describe('Produktbild.vue', () => {
  it('Rendert Produktbild', () => {
    // Given
    const wrapper = mount(Produktbild, {
      props: {
        url: 'https://res.cloudinary.com/blume2000/image/upload/c_fill,dpr_%d%,f_auto,h_%h%,q_auto,w_%w%/v1/prod/produkte/PRIMAER/20003385',
        altText: 'Alt text',
      },
    });

    // Then
    expect(wrapper.element).toMatchSnapshot();
  });

  it('Zeigt Platzhalter, wenn das Produktbild nicht geladen werden kann', async () => {
    // Given
    const wrapper = mount(Produktbild, {
      props: {
        url: 'gibts nicht',
        altText: 'Alt text',
      },
    });

    await wrapper.find('img').trigger('error');

    // Then
    expect(wrapper.find('img').attributes()['src']).toEqual(
      'https://assets.ecom.blume2000.de/images/produkt_platzhalter.png'
    );
  });
});
