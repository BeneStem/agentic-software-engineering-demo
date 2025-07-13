import { pushInDataLayer } from '@/main/frontend/app/util/tracking';
import { useStore } from 'vuex';
import { Filterwert } from '@/main/frontend/app/model/Filterwert';

export interface ProduktFilterTracking {
  sendProduktFilterChangeTrackingEvent(): void;

  sendAlleProduktFilterZuruecksetzenTrackingEvent(): void;

  sendNulltrefferTrackingEvent(): void;
}

interface ProduktFilterTrackingData {
  filter_sorts: string | undefined;
  filter_colour: string | undefined;
  filter_category: string | undefined;
  filter_availability: string | undefined;
  filter_price: string | undefined;
}

export function useProduktFilterTracking(): ProduktFilterTracking {
  const store = useStore();

  function filterwerteToTrackingString(filterwerte: Filterwert[] | undefined): string | undefined {
    return filterwerte !== undefined && filterwerte.length > 0
      ? filterwerte.map((filterwert) => filterwert.lesbarerName).join(',')
      : undefined;
  }

  function getSelectedFilterTrackingData(): ProduktFilterTrackingData {
    return {
      filter_sorts: filterwerteToTrackingString(store.getters['ProduktfilterState/getSelectedSorten']),
      filter_colour: filterwerteToTrackingString(store.getters['ProduktfilterState/getSelectedFarben']),
      filter_category: filterwerteToTrackingString(store.getters['ProduktfilterState/getSelectedKlassifikationen']),
      filter_availability: filterwerteToTrackingString(store.getters['ProduktfilterState/getSelectedLiefertage']),
      filter_price: undefined, // TODO: Preisspanne hinzufügen, wenn Preisfilter umgesetzt ist (z.b. 10-30)
    };
  }

  function sendProduktFilterChangeTrackingEvent() {
    pushInDataLayer({
      event: 'gaEvent',
      event_name: 'select_filter',
      select_filter: getSelectedFilterTrackingData(),
    });
  }

  function sendAlleProduktFilterZuruecksetzenTrackingEvent() {
    pushInDataLayer({
      event: 'gaEvent',
      event_name: 'remove_all_filter',
    });
  }

  function sendNulltrefferTrackingEvent() {
    pushInDataLayer({
      event: 'gaEvent',
      event_name: 'no_results_filter_config',
      no_results_filter_config: getSelectedFilterTrackingData(),
    });
  }

  return {
    sendProduktFilterChangeTrackingEvent,
    sendAlleProduktFilterZuruecksetzenTrackingEvent,
    sendNulltrefferTrackingEvent,
  };
}
