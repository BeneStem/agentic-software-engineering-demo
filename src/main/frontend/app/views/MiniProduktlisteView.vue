<template>
  <div v-if="produkte.length > 0" class="erk_finden__mini-produktliste-view">
    <div class="erk_finden__mini-produktliste-view-header">
      <UeberschriftDrei
        class="erk_finden__mini-produktliste-view-headline"
        :ist-haupt-ueberschrift="istHauptueberschrift"
        v-if="ueberschrift"
      >
        {{ ueberschrift }}
      </UeberschriftDrei>
      <a
        class="erk_finden__mini-produktliste-view-alle-anzeigen-link"
        :href="alleAnzeigenLinkZiel"
        v-if="alleAnzeigenLinkText && alleAnzeigenLinkZiel"
        @click="sendTrackAlleAnzeigenKlick"
      >
        <span class="erk_finden__mini-produktliste-view-alle-anzeigen-link-text">{{ alleAnzeigenLinkText }}</span>
        <PfeilspitzeImKreis class="erk_finden__mini-produktliste-view-alle-anzeigen-link-pfeil" richtung="rechts" />
      </a>
    </div>
    <div class="erk_finden__mini-produktliste-view-produkte">
      <MiniProduktliste :produkte="produkte" :trackingname="trackingname" :trackingnummer="trackingnummer" />
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, onServerPrefetch } from 'vue';
import { useStore } from 'vuex';
import MiniProduktliste from '@/main/frontend/app/components/MiniProduktliste.vue';
import UeberschriftDrei from '@blume2000/design-system/dist/komponenten/content/UeberschriftDrei.vue';
import { useRoute } from 'vue-router';
import { trackAlleAnzeigenKlick } from '../util/tracking';
import PfeilspitzeImKreis from '@blume2000/design-system/dist/komponenten/visuelles/icons/allgemein/PfeilspitzeImKreis.vue';
import { QueryParameters } from '@/main/frontend/app/model/QueryParameters';

const store = useStore();
const ueberschrift = computed(() => store.getters['EinstellungenState/getUeberschrift']);
const istHauptueberschrift = computed(() => store.getters['EinstellungenState/getIstHauptueberschrift']);
const trackingname = computed(() => store.getters['EinstellungenState/getTrackingname']);
const trackingnummer = computed(() => store.getters['EinstellungenState/getTrackingnummer']);
const produkte = computed(() => store.getters['ProdukteState/getProdukte']);
const alleAnzeigenLinkText = computed(() => store.getters['EinstellungenState/getAlleAnzeigenLinkText']);
const alleAnzeigenLinkZiel = computed(() => store.getters['EinstellungenState/getAlleAnzeigenLinkZiel']);

const sendTrackAlleAnzeigenKlick = trackAlleAnzeigenKlick;

onServerPrefetch(async () => {
  const route = useRoute();

  const queryParameters = QueryParameters.mapQueryToQueryParameters(route.query);
  //Das setzten der CMS filter im State muss awaited werden, da fetchProdukte im folgenden auf den CMS filter State zugreift
  await store.commit('ProduktfilterState/setCmsFilter', queryParameters, { root: true });

  return Promise.all([
    store.commit('EinstellungenState/setEinstellungenFromQueryparameters', route.query),
    store.dispatch('FeatureState/fetchFeatureTogglesSSR'),
    store.dispatch('ProdukteState/fetchProdukteSSR'),
  ]);
});
</script>

<style lang="scss" scoped>
.erk_finden__mini-produktliste-view {
  margin: 0 auto;
  max-width: 100%;

  &-header {
    display: flex;
    margin-bottom: 24px;
  }

  &-alle-anzeigen-link {
    @include font-Rubik-medium(18, 25);
    color: $color-dark;

    @media (min-width: 568px) {
      margin-left: auto;
      text-align: right;
      text-decoration: none;
    }

    &-text {
      display: none;

      @media (min-width: 568px) {
        display: inline-block;
      }
    }

    .erk_finden__mini-produktliste-view &-pfeil {
      display: inline-block;
      height: 22px;
      margin-left: 8px;
      width: 22px;

      @media (min-width: 568px) {
        position: relative;
        top: 4px;
      }

      svg {
        height: 22px;
        width: 22px;
      }
    }

    &:visited {
      color: $color-dark;
    }
  }
}
</style>
