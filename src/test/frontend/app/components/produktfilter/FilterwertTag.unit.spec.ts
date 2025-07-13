import { mount } from '@vue/test-utils';
import FilterwertTag from '@/main/frontend/app/components/produktfilter/FilterwertTag.vue';

describe('FilterwertTag.vue', () => {
  it('Rendert FilterwertTag', () => {
    // Given
    const wrapper = mount(FilterwertTag, {
      props: {
        name: 'Filtername',
        ausgewaehlt: false,
      },
    });

    // Then
    expect(wrapper.element).toMatchSnapshot();
  });

  it('Rendert FilterwertTag im ausgewählten Zustand', () => {
    // Given
    const wrapper = mount(FilterwertTag, {
      props: {
        name: 'Filtername',
        ausgewaehlt: true,
      },
    });

    // Then
    expect(wrapper.element).toMatchSnapshot();
  });

  //TODO: test zum laufen kriegen, wrapper.emitted() ist immer undefined
  // it('Wirft ein filterwertAktualisiert Event, wenn ein Filterwert angeklickt wird', async () => {
  //   // Given
  //   const wrapper = mount(FilterwertTag, {
  //     props: {
  //       filterwert: { name: 'Filtername', selected: false },
  //     },
  //   });
  //
  //   const input = wrapper.find('input');
  //
  //   console.log(input);
  //
  //   //await input.setValue('checked');
  //   //console.log(input.element.value);
  //   input.trigger('click');
  //   await nextTick();
  //
  //   console.log(wrapper.emitted());
  //
  //   expect(wrapper.emitted()).toHaveProperty('filterwertAktualisiert');
  // });
});
