<template>
  <div
    class="erk_finden__filtertaglist"
    :style="{ '--display-left-gradient': displayLeftGradient, '--display-right-gradient': displayRightGradient }"
  >
    <ul
      ref="tablistElement"
      role="tablist"
      aria-label="Produktefilter Kategorien"
      class="erk_finden__filtertaglist-liste sha_hide-scrollbar"
    >
      <li
        v-for="{ name, filterTyp } in anzuzeigendeFilterListe"
        :key="filterTyp"
        class="erk_finden__filtertaglist-liste-element"
      >
        <FilterTag
          :name="name"
          :filter-typ="filterTyp"
          :ist-ausgewaehlt="uiStatus.anzuzeigenderFilter === filterTyp"
          :hatFilterung="veraenderteFilterwerte[filterTyp]"
          :anzahl="gebeAnzahlSelectedFilterwerte(filterTyp)"
          @click="onFilterKlick(filterTyp)"
        />
      </li>
    </ul>
  </div>
</template>

<script setup lang="ts">
import { FilterTyp } from '@/main/frontend/app/model/FilterTyp';
import FilterTag from '@/main/frontend/app/components/produktfilter/FilterTag.vue';
import { useProduktFilterUiStatus } from '@/main/frontend/app/composables/ProduktFilterUiStatus';
import { useScroll } from '@vueuse/core';
import { computed, ref } from 'vue';

type Display = 'none' | 'block';

const { injectUiStatus, anzuzeigendeFilterListe, veraenderteFilterwerte, gebeAnzahlSelectedFilterwerte } =
  useProduktFilterUiStatus();
const tablistElement = ref<HTMLElement | null>(null);
const { arrivedState } = useScroll(tablistElement);
const uiStatus = injectUiStatus();
const displayLeftGradient = computed<Display>(() => (arrivedState.left ? 'none' : 'block'));
const displayRightGradient = computed<Display>(() => (arrivedState.right ? 'none' : 'block'));

function onFilterKlick(filter: FilterTyp): void {
  if (uiStatus.anzuzeigenderFilter === filter) {
    uiStatus.schliesseFilter();
  } else {
    uiStatus.setzeAnzuzeigendenFilter(filter);
  }
}
</script>

<style lang="scss" scoped>
@mixin backgroundLinearGradient($direction) {
  background: linear-gradient(
    to $direction,
    rgba(255, 255, 255, 1) 0%,
    rgba(255, 255, 255, 0.9) 15%,
    rgba(255, 255, 255, 0.75) 25%,
    rgba(255, 255, 255, 0.15) 75%,
    rgba(255, 255, 255, 0) 100%
  );
}

.erk_finden__filtertaglist {
  position: relative;
  padding: 2px 0;

  @media screen and (max-width: $breakpoint-medium) {
    &:before,
    &:after {
      pointer-events: none;
      position: absolute;
      width: 48px;
      top: 0;
      bottom: 0;
      height: 100%;
      content: '';
      z-index: 1;
    }

    &:before {
      @include backgroundLinearGradient(right);
      display: var(--display-left-gradient);
      left: 0;
    }

    &:after {
      @include backgroundLinearGradient(left);
      display: var(--display-right-gradient);
      right: 0;
    }
  }

  &-liste {
    display: flex;

    &-element {
      margin-right: 16px;

      &:last-child {
        margin-right: 0;
      }
    }

    @media screen and (max-width: $breakpoint-medium) {
      max-width: calc(100vw - 2rem);
      overflow-x: scroll;
    }
  }
}
</style>
