<template>
  <div class="erk_finden__mini-produktliste">
    <div class="erk_finden__mini-produktliste-wrapper">
      <div class="erk_finden__mini-produktliste-grid">
        <MiniProduktkachel
          v-for="(produkt, index) in produkte"
          :key="produkt.nummer"
          :produkt="produkt"
          :position="index"
          :listenname="trackingname"
          :listennummer="trackingnummer"
        />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { Produkt } from '../model/Produkt';
import MiniProduktkachel from '@/main/frontend/app/components/MiniProduktkachel.vue';

interface MiniProduktlisteProps {
  produkte: Produkt[];
  trackingname: string;
  trackingnummer: number;
}

defineProps<MiniProduktlisteProps>();
</script>

<style scoped lang="scss">
.erk_finden__mini-produktliste {
  @media (max-width: 567px) {
    -webkit-overflow-scrolling: touch;
    height: 320px;
    &-wrapper {
      position: absolute;
      width: 100%;
      left: 0;
    }

    &-grid {
      padding-left: 8px;
      display: flex;
      overflow-x: auto;
      height: 320px;
    }
  }

  &-grid {
    @media (min-width: 568px) {
      display: grid;
      grid-column-gap: 20px;
      grid-row-gap: 22px;
      grid-template-columns: repeat(3, calc((100% - 2 * 20px) / 3));
    }

    @media (min-width: 992px) {
      grid-template-columns: repeat(4, calc((100% - 3 * 20px) / 4));
    }

    @media (min-width: 1366px) {
      grid-template-columns: repeat(6, calc((100% - 5 * 20px) / 6));
    }
  }
}
</style>
