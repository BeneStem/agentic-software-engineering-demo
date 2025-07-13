<template>
  <a
    :href="produkt.produktUrl"
    class="erk_finden__mini-produktkachel"
    @click="sendProduktClickTrackingEvent"
    :title="produkt.name"
  >
    <div class="erk_finden__mini-produktkachel-border" ref="htmlElement">
      <div class="erk_finden__mini-produktkachel-bild" ref="target">
        <Produktbild :url="produkt.bildUrl" :altText="produkt.name" />
      </div>
    </div>
    <div class="erk_finden__mini-produktkachel-name">{{ produkt.name }}</div>
    <div class="erk_finden__mini-produktkachel-lieferaussage">
      <Fliesstext typ="klein">{{ lieferaussage }}</Fliesstext>
    </div>
    <div class="erk_finden__mini-produktkachel-preis">
      {{ produkt.preis }} €
      <div class="erk_finden__mini-produktkachel-streichpreis" v-if="produkt.zeigeStreichpreis">
        {{ produkt.streichpreis }} €
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

interface MiniProduktkachelProps {
  produkt: Produkt;
  position: number;
  listenname: string;
  listennummer: number;
}

const props = defineProps<MiniProduktkachelProps>();

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
.erk_finden__mini-produktkachel {
  color: $color-dark;
  text-decoration: none;
  outline: none;
  @media (max-width: 567px) {
    width: 145px;
  }

  &-border {
    border-radius: 6px;
    border: 2px solid $color-light;
    margin-bottom: 16px;
    padding: 8px;
  }

  &-bild {
    aspect-ratio: 1 / 1;
    border-radius: 6px;
    overflow: hidden;
    width: 100%;

    @media (max-width: 567px) {
      width: 125px;
    }

    img {
      width: 100%;
      outline: none;
    }
  }

  &-name {
    @include font-BLUME2000(22, 20);
    @media (max-width: 567px) {
      //Max 3 Zeilen wegen absolutem Layout auf mobile
      max-height: 62px;
      overflow: hidden;
    }
    margin-top: 8px;
    overflow-wrap: anywhere;
    padding-left: 12px;
    padding-right: 12px;
  }

  &-lieferaussage {
    margin-top: 8px;
    padding-left: 12px;
    padding-right: 12px;
  }

  &-preis {
    @include font-Rubik-light;
    margin-top: 8px;
    padding-left: 12px;
    padding-right: 12px;
  }

  &-streichpreis {
    @include font-Rubik-light;
    color: $color-alert;
    margin-top: 4px;
    text-decoration: line-through;
  }

  @include hasHover {
    &:hover {
      .erk_finden__mini-produktkachel-border {
        border-color: $color-dark;
      }
    }
  }
}

@supports not (aspect-ratio: 1 / 1) {
  .erk_finden__mini-produktkachel-bild::before {
    float: left;
    padding-top: 100%;
    content: '';
  }

  .erk_finden__mini-produktkachel-bild::after {
    display: block;
    content: '';
    clear: both;
  }
}
</style>
