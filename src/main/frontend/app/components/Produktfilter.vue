<template>
  <aside
    aria-label="Produktefilter"
    class="erk_finden__produktfilter"
    :class="{ 'erk_finden__produktfilter-eingeklappt': istFilterwerteBereichEingeklappt }"
  >
    <FilterTagList />
    <div v-if="!istFilterwerteBereichEingeklappt">
      <div>
        <TrennlinieDuenn class="erk_finden__produktfilter-trennlinie" />
      </div>
      <Filterwerte
        v-if="uiStatus.anzuzeigenderFilter"
        :filtertyp="uiStatus.anzuzeigenderFilter"
        :filterwerte="verfuegbareFilterwerte[uiStatus.anzuzeigenderFilter]"
        @filterwert-geklickt="(geklickterFilterwert) => toggleSelection(geklickterFilterwert)"
      />
      <div
        v-if="uiStatus.anzuzeigenderFilter === FilterTyp.PREIS"
        :id="erstelleFilterTabPanelId(FilterTyp.PREIS)"
        :aria-labelledby="erstelleFilterTabId(FilterTyp.PREIS)"
      >
        {{ verfuegbareFilterwerte[FilterTyp.PREIS] }}
      </div>
      <div>
        <TrennlinieDuenn class="erk_finden__produktfilter-trennlinie" />
      </div>
    </div>
    <footer>
      <div class="erk_finden__produktfilter__footer__filterwerte-entfernen">
        <button
          class="erk_finden__produktfilter__footer__filterwerte-zuruecksetzen"
          v-if="selectedFilterwerte.length"
          @click="onAlleFilterZuruecksetzen()"
        >
          {{ selectedFilterwerte.length }} Filter zurücksetzen
        </button>
        <FilterwertEntfernen
          class="erk_finden__produktfilter__footer__filterwerte"
          v-for="filterwert in selectedFilterwerte"
          :key="filterwert.lesbarerName"
          :filterwert-name="filterwert.lesbarerName"
          @filterwertEntfernen="filterwertZuruecksetzen(filterwert)"
        />
      </div>
      <FilterErgebnisAnzahl v-if="anzahlProdukte && istFilterAktiv" />
    </footer>
  </aside>
</template>

<script setup lang="ts">
import { FilterTyp } from '@/main/frontend/app/model/FilterTyp';
import FilterTagList from '@/main/frontend/app/components/produktfilter/FilterTagList.vue';
import { useProduktFilterUiStatus } from '@/main/frontend/app/composables/ProduktFilterUiStatus';
import { erstelleFilterTabId, erstelleFilterTabPanelId } from '@/main/frontend/app/components/produktfilter/utils';
import Filterwerte from '@/main/frontend/app/components/produktfilter/Filterwerte.vue';
import FilterwertEntfernen from '@/main/frontend/app/components/produktfilter/FilterwertEntfernen.vue';
import TrennlinieDuenn from '@blume2000/design-system/dist/komponenten/atome/TrennlinieDuenn.vue';
import { useProduktFilter } from '@/main/frontend/app/composables/ProduktFilter';
import FilterErgebnisAnzahl from '@/main/frontend/app/components/produktfilter/FilterErgebnisAnzahl.vue';
import { useStore } from 'vuex';

const { provideUiStatus, istFilterwerteBereichEingeklappt } = useProduktFilterUiStatus();
const {
  verfuegbareFilterwerte,
  selectedFilterwerte,
  anzahlProdukte,
  toggleSelection,
  istFilterAktiv,
  filterwertZuruecksetzen,
  onAlleFilterZuruecksetzen,
} = useProduktFilter();
const uiStatus = provideUiStatus();

const store = useStore();

store.subscribe(async (mutation) => {
  if (
    mutation.type === 'ProduktfilterState/setFilterwert' ||
    mutation.type === 'ProduktfilterState/setAlleFilterZuruecksetzen'
  ) {
    await store.dispatch('ProdukteState/fetchProdukteMitVerfuegbarenFilterwerten', {
      istAktionAlleFilterZuruecksetzen: mutation.type === 'ProduktfilterState/setAlleFilterZuruecksetzen',
      useDebouncedAPI: true,
    });
  }
});
</script>

<style lang="scss" scoped>
.erk_finden__produktfilter {
  display: grid;
  grid-gap: 8px;
  margin-bottom: 16px;

  &-trennlinie {
    z-index: 1;
    --hauptfarbe: #{$color-iron-grey};
  }

  &__footer {
    &__filterwerte-zuruecksetzen {
      margin-left: 32px;
      margin-bottom: 4px;
    }

    &__filterwerte {
      display: none;
      margin-left: 32px;
      margin-bottom: 4px;

      @media (min-width: $breakpoint-small) {
        display: flex;
      }
    }

    &__filterwerte-entfernen {
      display: flex;
      flex-wrap: wrap;
      margin-left: -32px;
      margin-bottom: -4px;
      @include font-Rubik-medium(16);
    }
  }

  &__filterauswahl {
    display: grid;
    grid-template-columns: repeat(5, max-content);
    grid-gap: 16px;
  }
}
</style>
