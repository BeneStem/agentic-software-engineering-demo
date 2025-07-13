import { computed, ComputedRef, inject, InjectionKey, provide, reactive, Ref } from 'vue';
import { FilterTyp } from '@/main/frontend/app/model/FilterTyp';
import {
  AnzahlSelectedFilterwert,
  VeraenderteFilterwerte,
  ZeigeFilter,
} from '@/main/frontend/app/store/ProduktfilterState';
import { useStore } from 'vuex';

interface UiStatus {
  anzuzeigenderFilter: FilterTyp | null;
  filterwertZurueckgesetzt: boolean;
  setFilterwertZurueckgesetzt(filterwertZurueckgesetzt: boolean): void;
  setzeAnzuzeigendenFilter(filtertyp: FilterTyp): void;
  schliesseFilter(): void;
}

interface FilterUiStatus {
  name: 'Sorten' | 'Farben' | 'Preis' | 'Produktarten' | 'Tag der Lieferung';
  sichtbar: boolean;
  filterTyp: FilterTyp;
}

function setzeAnzuzeigendenFilter(filtertyp: FilterTyp): void {
  uiStatus.anzuzeigenderFilter = filtertyp;
}

function setFilterwertZurueckgesetzt(filterwertZurueckgesetzt: boolean): void {
  uiStatus.filterwertZurueckgesetzt = filterwertZurueckgesetzt;
}

function schliesseFilter(): void {
  uiStatus.anzuzeigenderFilter = null;
}

export const uiStatus = reactive<UiStatus>({
  anzuzeigenderFilter: null,
  filterwertZurueckgesetzt: false,
  setFilterwertZurueckgesetzt,
  setzeAnzuzeigendenFilter,
  schliesseFilter,
});

const uiStatusInjectionKey: InjectionKey<UiStatus> = Symbol('uiStatusInjectionKey');

export interface ProduktFilterUiStatus {
  uiStatus: UiStatus;
  anzuzeigendeFilterListe: ComputedRef<FilterUiStatus[]>;
  veraenderteFilterwerte: Ref<VeraenderteFilterwerte>;
  istFilterwerteBereichAusgeklappt: ComputedRef<boolean>;
  istFilterwerteBereichEingeklappt: ComputedRef<boolean>;
  provideUiStatus(): UiStatus;
  injectUiStatus(): UiStatus;
  gebeAnzahlSelectedFilterwerte(filtertyp: FilterTyp): number | null;
}

export function useProduktFilterUiStatus(): ProduktFilterUiStatus {
  const store = useStore();
  const istFilterwerteBereichAusgeklappt = computed(() => !!uiStatus.anzuzeigenderFilter);
  const istFilterwerteBereichEingeklappt = computed(() => !istFilterwerteBereichAusgeklappt.value);
  const veraenderteFilterwerte = computed<VeraenderteFilterwerte>(
    () => store.getters['ProduktfilterState/getVeraenderteFilterwerte']
  );
  const zeigeFilter = computed<ZeigeFilter>(() => store.getters['ProduktfilterState/getZeigeFilter']);
  const anzahlSelectedFilterwert = computed<AnzahlSelectedFilterwert>(
    () => store.getters['ProduktfilterState/getAnzahlSelectedFilterwerte']
  );
  const filterListe = computed<FilterUiStatus[]>(() => [
    {
      name: 'Farben',
      sichtbar: zeigeFilter.value.FARBE,
      filterTyp: FilterTyp.FARBE,
    },
    {
      name: 'Tag der Lieferung',
      sichtbar: zeigeFilter.value.VERFUEGBARKEIT,
      filterTyp: FilterTyp.VERFUEGBARKEIT,
    },
    {
      name: 'Sorten',
      sichtbar: zeigeFilter.value.SORTE,
      filterTyp: FilterTyp.SORTE,
    },
    {
      name: 'Produktarten',
      sichtbar: zeigeFilter.value.PRODUKTART,
      filterTyp: FilterTyp.PRODUKTART,
    },
    {
      name: 'Preis',
      sichtbar: zeigeFilter.value.PREIS,
      filterTyp: FilterTyp.PREIS,
    },
  ]);
  const anzuzeigendeFilterListe = computed(() => filterListe.value.filter((filter) => filter.sichtbar));

  function provideUiStatus(): UiStatus {
    provide(uiStatusInjectionKey, uiStatus);
    return uiStatus;
  }

  function injectUiStatus(): UiStatus {
    return inject(uiStatusInjectionKey, uiStatus);
  }

  function gebeAnzahlSelectedFilterwerte(filtertyp: FilterTyp): number | null {
    if (filtertyp === FilterTyp.PREIS) {
      return null;
    }

    return anzahlSelectedFilterwert.value[filtertyp];
  }

  return {
    uiStatus,
    anzuzeigendeFilterListe,
    veraenderteFilterwerte,
    istFilterwerteBereichAusgeklappt,
    istFilterwerteBereichEingeklappt,
    gebeAnzahlSelectedFilterwerte,
    provideUiStatus,
    injectUiStatus,
  };
}
