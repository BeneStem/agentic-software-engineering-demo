<template>
  <div class="erk_finden__produktliste">
    <Produktkachel
      v-for="(produkt, index) in produkte"
      :key="produkt.nummer"
      :produkt="produkt"
      :position="index"
      :listenname="trackingname"
      :listennummer="trackingnummer"
    />
  </div>
</template>

<script setup lang="ts">
import { Produkt } from '../model/Produkt';
import Produktkachel from '@/main/frontend/app/components/Produktkachel.vue';

interface ProduktlisteProps {
  produkte: Produkt[];
  trackingname: string;
  trackingnummer: number;
}

defineProps<ProduktlisteProps>();
</script>

<style scoped lang="scss">
.erk_finden__produktliste {
  display: grid;
  grid-template-columns: 1fr;
  grid-column-gap: 40px;
  grid-row-gap: 20px;
}

@media (min-width: $breakpoint-small) {
  .erk_finden__produktliste {
    grid-template-columns: 1fr 1fr;
  }
}

@media (min-width: $breakpoint-large) {
  .erk_finden__produktliste {
    grid-template-columns: 1fr 1fr 1fr;
  }
}
</style>
