import { mount } from '@vue/test-utils';
import KeineProdukteMeldung from '@/main/frontend/app/components/KeineProdukteMeldung.vue';

describe('KeineProdukteMeldung', () => {
  it('renders correctly', () => {
    // Given
    const wrapper = mount(KeineProdukteMeldung);

    // Then
    expect(wrapper.element).toMatchSnapshot();
  });
});
