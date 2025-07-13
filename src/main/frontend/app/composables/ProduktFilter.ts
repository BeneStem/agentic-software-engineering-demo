import { useStore } from 'vuex';
import { computed, ComputedRef, ref, Ref, watch } from 'vue';
import { VerfuegbareFilterwerte } from '@/main/frontend/app/store/ProduktfilterState';
import { Filterwert, FilterwertMitId } from '@/main/frontend/app/model/Filterwert';
import { useProduktFilterUiStatus } from '@/main/frontend/app/composables/ProduktFilterUiStatus';
import { useProduktFilterTracking } from '@/main/frontend/app/composables/ProduktFilterTracking';

export interface ProduktFilter {
  verfuegbareFilterwerte: ComputedRef<VerfuegbareFilterwerte>;
  selectedFilterwerte: ComputedRef<Filterwert | FilterwertMitId[]>;
  istFilterAktiv: Ref<boolean>;
  anzahlProdukte: ComputedRef<number>;

  toggleSelection(filterwert: FilterwertMitId): void;

  filterwertZuruecksetzen(filterwert: FilterwertMitId): void;

  onAlleFilterZuruecksetzen(): void;
}

export function useProduktFilter(): ProduktFilter {
  const { injectUiStatus } = useProduktFilterUiStatus();
  const uiStatus = injectUiStatus();
  const store = useStore();
  const { sendProduktFilterChangeTrackingEvent, sendAlleProduktFilterZuruecksetzenTrackingEvent } =
    useProduktFilterTracking();

  const verfuegbareFilterwerte: ComputedRef<VerfuegbareFilterwerte> = computed(
    () => store.getters['ProduktfilterState/getVerfuegbareFilterwerte']
  );
  const selectedFilterwerte: ComputedRef<Filterwert | FilterwertMitId[]> = computed(
    () => store.getters['ProduktfilterState/getSelectedFilterwerte']
  );

  const istFilterAktiv: Ref<boolean> = ref(false);

  watch(() => store.getters['ProdukteState/getProdukte'], aktualisiereIstFilterAktiv);

  const anzahlProdukte: ComputedRef<number> = computed(() => store.getters['ProdukteState/getAnzahlProdukte']);

  function aktualisiereIstFilterAktiv() {
    istFilterAktiv.value = store.getters['ProduktfilterState/getSindFilterwerteAusgewaehlt'];
  }

  function toggleSelection(filterwert: FilterwertMitId): void {
    store.commit('ProduktfilterState/setFilterwert', { ...filterwert, selected: !filterwert.selected });
    sendProduktFilterChangeTrackingEvent();
  }

  function filterwertZuruecksetzen(filterwert: FilterwertMitId): void {
    uiStatus.setFilterwertZurueckgesetzt(true);
    toggleSelection(filterwert);
  }

  function onAlleFilterZuruecksetzen(): void {
    store.commit('ProduktfilterState/setAlleFilterZuruecksetzen');
    sendAlleProduktFilterZuruecksetzenTrackingEvent();
  }

  return {
    verfuegbareFilterwerte,
    selectedFilterwerte,
    istFilterAktiv,
    anzahlProdukte,
    toggleSelection,
    filterwertZuruecksetzen,
    onAlleFilterZuruecksetzen,
  };
}
