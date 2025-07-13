import { createStore } from 'vuex';
import { mount } from '@vue/test-utils';

import NulltrefferFilterMeldung from '@/main/frontend/app/components/NulltrefferFilterMeldung.vue';

describe('NulltrefferFilterMeldung', () => {
  it('renders correctly', () => {
    // Given
    const mockStateMutation = jest.fn();
    const store = createStore({
      mutations: {
        'ProduktfilterState/setAlleFilterZuruecksetzen': mockStateMutation
      }
    });
    const wrapper = mount(NulltrefferFilterMeldung, {
      global: {
        plugins: [store]
      }
    });

    // Then
    expect(wrapper.element).toMatchSnapshot();
  });

  it('triggers a store event on filter rücksetzten click', () => {
    // Given
    const mockStateMutation = jest.fn();
    const store = createStore({
      mutations: {
        'ProduktfilterState/setAlleFilterZuruecksetzen': mockStateMutation
      }
    });

    const wrapper = mount(NulltrefferFilterMeldung, {
      global: {
        plugins: [store]
      }
    });

    // Then
    wrapper.find('.erk_finden__nulltreffermeldung__filterruecksetzenbutton').trigger('click');

    expect(mockStateMutation.mock.calls.length).toBe(1);
  });
});
