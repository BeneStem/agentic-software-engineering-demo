<template>
  <a
    :href="produkt.produktUrl"
    class="erk_finden__produktkachel"
    @click="sendProduktClickTrackingEvent"
    :title="produkt.name"
  >
    <div class="erk_finden__produktkachel-bild" ref="htmlElement">
      <Produktbild :url="produkt.bildUrl" :altText="produkt.name" />
    </div>
    <div class="erk_finden__produktkachel-infos">
      <div class="erk_finden__produktkachel-name">{{ produkt.name }}</div>
      <div class="erk_finden__produktkachel-lieferaussage">
        <Fliesstext typ="klein">{{ lieferaussage }}</Fliesstext>
      </div>
      <div class="erk_finden__produktkachel-preis">
        {{ produkt.preis }} €
        <div class="erk_finden__produktkachel-streichpreis" v-if="produkt.zeigeStreichpreis">
          {{ produkt.streichpreis }} €
        </div>
      </div>
    </div>
  </a>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import Fliesstext from '@blume2000/design-system/dist/komponenten/content/Fliesstext.vue';
import { Produkt } from '../model/Produkt';
import Produktbild from './Produktbild.vue';
import { useProduktTracking } from '@/main/frontend/app/composables/ProduktTracking';
import { useProduktLieferaussage } from '@/main/frontend/app/composables/ProduktLieferaussage';

interface ProduktkachelProps {
  produkt: Produkt;
  position: number;
  listenname: string;
  listennummer: number;
}

const props = defineProps<ProduktkachelProps>();

const { lieferaussage } = useProduktLieferaussage(props.produkt.naechstmoeglicheVerfuegbarkeit);
const htmlElement = ref<HTMLElement | null>(null);
const { sendProduktClickTrackingEvent } = useProduktTracking(
  htmlElement,
  props.produkt.nummer,
  props.produkt.name,
  props.produkt.preis,
  props.listenname,
  props.listennummer,
  props.position,
  props.produkt.klassifikationName
);
</script>

<style lang="scss">
.erk_finden__produktkachel {
  display: block;
  color: $color-dark;
  text-decoration: none;
  outline: none;
  overflow: hidden;

  &-bild {
    border-radius: 6px;
    overflow: hidden;
    width: 100%;
    aspect-ratio: 1 / 1;

    img {
      width: 100%;
      outline: none;
    }
  }

  &-infos {
    margin-top: 16px;
    display: grid;
    grid-template-columns: 1fr max-content;
    grid-template-rows: 1fr max-content;
    grid-template-areas:
      'top-left top-right'
      'bottom-left bottom-right';
  }

  &-name {
    grid-area: top-left;
    word-break: break-word;
    @include font-BLUME2000(22, 20);
  }

  &-lieferaussage {
    grid-area: bottom-left;
  }

  &-preis {
    grid-column: top-right;
    grid-row-start: top-right;
    grid-row-end: bottom-right;
    justify-self: end;
    @include font-Rubik-medium;
    text-align: right;
    padding-left: 12px;
    padding-top: 2px;
  }

  &-streichpreis {
    margin-top: 4px;
    color: $color-alert;
    text-decoration: line-through;
    @include font-Rubik-light;
  }
}

@supports not (aspect-ratio: 1 / 1) {
  .erk_finden__produktkachel-bild::before {
    float: left;
    padding-top: 100%;
    content: '';
  }

  .erk_finden__produktkachel-bild::after {
    display: block;
    content: '';
    clear: both;
  }
}
</style>
