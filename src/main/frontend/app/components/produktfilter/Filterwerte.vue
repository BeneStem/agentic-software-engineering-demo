<template>
  <div
    class="erk_finden__filterwerte"
    :style="{
      '--display-top-gradient': displayTopGradient,
      '--display-bottom-gradient': displayBottomGradient,
      '--filterwerteListeMaxHeight': filterwerteListeMaxHeight + 'px',
    }"
  >
    <div
      ref="filterwerteListe"
      :id="erstelleFilterTabPanelId(filtertyp)"
      :aria-labelledby="erstelleFilterTabId(filtertyp)"
      role="tabpanel"
      class="erk_finden__filterwerte_tag-container"
      :class="['erk_finden__filterwerte_tag-container--' + filtertyp]"
    >
      <template v-if="filtertyp === FilterTyp.FARBE">
        <FilterwertKachel
          v-for="filterwert in sortedFilterwerteFarbe"
          :key="filterwert.name"
          :name="filterwert.name"
          :lesbarerName="filterwert.lesbarerName"
          :ausgewaehlt="filterwert.selected"
          @change="$emit('filterwertGeklickt', filterwert)"
        />
      </template>
      <FilterwertTag
        v-else
        v-for="filterwert in filterwerte"
        :key="filterwert.name"
        :name="filterwert.lesbarerName"
        :ausgewaehlt="filterwert.selected"
        @change="$emit('filterwertGeklickt', filterwert)"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import { Filterwert } from '@/main/frontend/app/model/Filterwert';
import FilterwertTag from '@/main/frontend/app/components/produktfilter/FilterwertTag.vue';
import { FilterTyp } from '@/main/frontend/app/model/FilterTyp';
import { erstelleFilterTabId, erstelleFilterTabPanelId } from '@/main/frontend/app/components/produktfilter/utils';
import FilterwertKachel from '@/main/frontend/app/components/produktfilter/FilterwertKachel.vue';
import { computed, ref } from 'vue';
import { useScroll, useResizeObserver } from '@vueuse/core';

const props = defineProps<{
  filtertyp: FilterTyp;
  filterwerte: Filterwert[];
}>();

defineEmits<{
  (e: 'filterwertGeklickt', filterwert: Filterwert): void;
}>();

type Display = 'none' | 'block';

const filterwerteListe = ref<HTMLElement | null>(null);
const filterwerteListeMaxHeight = 224;
const filterwerteListeHeight = ref(0);
const { arrivedState } = useScroll(filterwerteListe);

useResizeObserver(filterwerteListe, (entries) => {
  const { height } = entries[0].contentRect;
  filterwerteListeHeight.value = height;
});

const hideGradients = computed(() => {
  return filterwerteListeHeight.value < filterwerteListeMaxHeight;
});

const displayTopGradient = computed<Display>(() => (hideGradients.value || arrivedState.top ? 'none' : 'block'));
const displayBottomGradient = computed<Display>(() => (hideGradients.value || arrivedState.bottom ? 'none' : 'block'));

const sortedFilterwerteFarbe = computed<Filterwert[]>(() =>
  [...props.filterwerte].sort((a, b) => ((a.sortIndex ? a.sortIndex : 0) > (b.sortIndex ? b.sortIndex : 0) ? 1 : -1))
);
</script>

<style scoped lang="scss">
@mixin backgroundLinearGradient($direction) {
  background: linear-gradient(
    to $direction,
    rgba(255, 255, 255, 0.95) 0%,
    rgba(255, 255, 255, 0.15) 75%,
    rgba(255, 255, 255, 0) 100%
  );
}

.erk_finden__filterwerte {
  position: relative;

  &_tag-container {
    display: flex;
    flex-flow: wrap;
    margin-top: 16px;

    &--FARBE {
      display: grid;
      grid-template-columns: repeat(auto-fill, 50px);
      grid-gap: 8px 32px;
      justify-content: center;
      justify-items: center;
      margin-top: 8px;
      margin-bottom: 8px;
    }

    @media screen and (max-width: $breakpoint-medium) {
      &--SORTE,
      &--PRODUKTART {
        max-height: var(--filterwerteListeMaxHeight);
        overflow: scroll;
        margin-bottom: 0;

        &:before,
        &:after {
          pointer-events: none;
          position: absolute;
          left: 0;
          right: 0;
          width: 100%;
          height: 40px;
          content: '';
          z-index: 1;
        }

        &:before {
          @include backgroundLinearGradient(bottom);
          display: var(--display-top-gradient);
          top: 0;
        }

        &:after {
          @include backgroundLinearGradient(top);
          display: var(--display-bottom-gradient);
          bottom: 0;
        }
      }
    }
  }
}
</style>
