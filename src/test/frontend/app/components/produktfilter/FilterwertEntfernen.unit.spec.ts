import { mount } from '@vue/test-utils';
import FilterwertEntfernen from '@/main/frontend/app/components/produktfilter/FilterwertEntfernen.vue';

describe('FilterwertEntfernen.vue', () => {
  it('Rendert FilterwertEntfernen', () => {
    // Given
    const wrapper = mount(FilterwertEntfernen, {
      props: {
        filterwertName: 'FilterwertName',
      },
    });

    // Then
    expect(wrapper.element).toMatchSnapshot();
  });
});
