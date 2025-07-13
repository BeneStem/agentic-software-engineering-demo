import { ref, watch, Ref } from 'vue';
import { useElementVisibility } from '@vueuse/core';
import { pushInDataLayer } from '@/main/frontend/app/util/tracking';

interface ProduktTracking {
  sendProduktClickTrackingEvent(): void;
}

export function useProduktTracking(
  element: Ref<Element | null | undefined>,
  nummer: string,
  name: string,
  preis: string,
  listenname: string,
  listennummer: number,
  position: number,
  klassifikationName: string
): ProduktTracking {
  const viewportEventFired = ref(false);
  const elementIsInViewport = useElementVisibility(element);

  function sendProduktClickTrackingEvent() {
    trackProduktClick();
  }

  watch(
    () => elementIsInViewport.value,
    () => {
      if (!viewportEventFired.value) {
        trackProduktView();
        viewportEventFired.value = true;
      }
    }
  );

  function konvertierePreisZuZahl(preis: string): number {
    return parseFloat(preis.replace('€', '').replace(',', '.'));
  }

  function createProduktTrackingData() {
    return {
      item_name: name,
      item_id: nummer,
      price: konvertierePreisZuZahl(preis),
      item_brand: undefined,
      item_category: klassifikationName,
      item_category2: undefined,
      item_category3: undefined,
      item_variant: undefined,
      item_list_name: listenname,
      item_list_id: listennummer,
      index: position,
      quantity: 1,
    };
  }

  function trackProduktView() {
    pushInDataLayer({
      event: 'Ecommerce - Item List Views',
      event_name: 'view_item_list',
      view_item_list: {
        items: [createProduktTrackingData()],
      },
    });
  }

  function trackProduktClick() {
    pushInDataLayer({
      event: 'Ecommerce - Select Item',
      event_name: 'select_item',
      select_item: {
        items: [createProduktTrackingData()],
      },
    });
  }

  return {
    sendProduktClickTrackingEvent,
  };
}
