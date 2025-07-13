import { mount } from '@vue/test-utils';
import Filterwerte from '@/main/frontend/app/components/produktfilter/Filterwerte.vue';
import { FilterTyp } from '@/main/frontend/app/model/FilterTyp';

describe('Filterwerte.vue', () => {
  it('Rendert Filterwerte', () => {
    // Given
    const wrapper = mount(Filterwerte, {
      props: {
        filtertyp: FilterTyp.SORTE,
        filterwerte: [
          { name: 'Filter 1', selected: false, lesbarerName: 'Filter 1', filtertyp: FilterTyp.SORTE },
          { name: 'Filter 2', selected: true, lesbarerName: 'Filter 2', filtertyp: FilterTyp.SORTE },
          { name: 'Filter 3', selected: false, lesbarerName: 'Filter 3', filtertyp: FilterTyp.SORTE },
        ],
      },
    });

    // Then
    expect(wrapper.element).toMatchSnapshot();
  });

  it('Rendert Filterwerte Farbe als Farbkachel', () => {
    // Given
    const wrapper = mount(Filterwerte, {
      props: {
        filtertyp: FilterTyp.FARBE,
        filterwerte: [
          { name: 'rot', selected: false, lesbarerName: 'Rot', filtertyp: FilterTyp.FARBE },
          { name: 'grün', selected: true, lesbarerName: 'Grün', filtertyp: FilterTyp.FARBE },
          { name: 'weiss', selected: false, lesbarerName: 'Weiß', filtertyp: FilterTyp.FARBE },
        ],
      },
    });

    // Then
    expect(wrapper.element).toMatchSnapshot();
  });

  it('Rendert Filterwerte wenn keine Filter vorhanden sind', () => {
    // Given
    const wrapper = mount(Filterwerte, {
      props: {
        filtertyp: FilterTyp.FARBE,
        filterwerte: [],
      },
    });

    // Then
    expect(wrapper.element).toMatchSnapshot();
  });
});
