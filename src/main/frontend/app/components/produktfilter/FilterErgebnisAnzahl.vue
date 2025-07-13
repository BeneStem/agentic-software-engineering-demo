<template>
  <div class="erk_finden__filter_ergebnis-anzahl">
    <B2kIcon name="Stern" />
    <p>
      Tadaaaa, wir haben
      <strong>{{ ergebnisAnzahlString }}</strong>
      für dich!
    </p>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue';
import { useProduktFilter } from '@/main/frontend/app/composables/ProduktFilter';
import B2kIcon from '@blume2000/design-system/dist/komponenten/atome/B2kIcon.vue';

const { anzahlProdukte } = useProduktFilter();

const ergebnisAnzahlString = computed(() => {
  const ergebnisString = anzahlProdukte.value === 1 ? 'Ergebnis' : 'Ergebnisse';
  return `${anzahlProdukte.value} ${ergebnisString}`;
});
</script>

<style lang="scss" scoped>
.erk_finden__filter_ergebnis-anzahl {
  display: flex;
  height: 48px;
  align-items: center;
  justify-content: flex-end;
  @include font-Rubik-light(12);

  p {
    padding-top: 12px;
  }

  strong {
    @include font-Rubik-medium(12);
  }

  svg {
    height: 100%;
    width: 56px;
  }

  @media (min-width: $breakpoint-small) {
    @include font-Rubik-light(16);

    strong {
      @include font-Rubik-medium(16);
    }
  }
}
</style>
