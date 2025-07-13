<template>
  <div class="erk_finden__produktliste">
    <UeberschriftZwei
      class="erk_finden__produktliste-headline"
      :ist-haupt-ueberschrift="istHauptueberschrift"
      v-if="ueberschrift"
      >{{ ueberschrift }}
    </UeberschriftZwei>
    <div class="erk_finden__produktliste-produkte">
      <div
        class="erk_finden__produktliste-produkte-vogel"
        v-if="produkte && produkte.length > 2 && ueberschrift && !produktfilterAnzeigen"
      >
        <VogelMitHerzAnimiert />
      </div>
      <Produktfilter v-if="produktfilterAnzeigen" />
      <Produktliste :produkte="produkte" :trackingname="trackingname" :trackingnummer="trackingnummer" />
      <NulltrefferFilterMeldung v-if="zeigeNulltrefferseite" />
      <KeineProdukteMeldung v-if="!zeigeNulltrefferseite && produkte.length === 0" />
    </div>
    <div
      class="erk_finden__produktliste-alle-anzeigen-link-container"
      v-if="alleAnzeigenLinkText && alleAnzeigenLinkZiel"
    >
      <a
        class="erk_finden__produktliste-alle-anzeigen-link"
        :href="alleAnzeigenLinkZiel"
        @click="sendTrackAlleAnzeigenKlick"
        >{{ alleAnzeigenLinkText }}</a
      >
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, onServerPrefetch, watch } from 'vue';
import { useStore } from 'vuex';
import Produktliste from '@/main/frontend/app/components/Produktliste.vue';
import Produktfilter from '@/main/frontend/app/components/Produktfilter.vue';
import UeberschriftZwei from '@blume2000/design-system/dist/komponenten/content/UeberschriftZwei.vue';
import VogelMitHerzAnimiert from '@blume2000/design-system/dist/komponenten/atome/VogelMitHerzAnimiert.vue';
import NulltrefferFilterMeldung from '@/main/frontend/app/components/NulltrefferFilterMeldung.vue';
import KeineProdukteMeldung from '@/main/frontend/app/components/KeineProdukteMeldung.vue';
import { trackAlleAnzeigenKlick } from '../util/tracking';
import { FeatureToggles } from '@/main/frontend/app/util/featureToggles';
import { useProduktFilterTracking } from '@/main/frontend/app/composables/ProduktFilterTracking';
import { QueryParameters } from '@/main/frontend/app/model/QueryParameters';
import { useProduktFilter } from '@/main/frontend/app/composables/ProduktFilter';
import { useRoute } from 'vue-router';

const store = useStore();
const { sendNulltrefferTrackingEvent } = useProduktFilterTracking();
const { istFilterAktiv } = useProduktFilter();
const ueberschrift = computed(() => store.getters['EinstellungenState/getUeberschrift']);
const istHauptueberschrift = computed(() => store.getters['EinstellungenState/getIstHauptueberschrift']);
const trackingname = computed(() => store.getters['EinstellungenState/getTrackingname']);
const trackingnummer = computed(() => store.getters['EinstellungenState/getTrackingnummer']);
const produkte = computed(() => store.getters['ProdukteState/getProdukte']);
const alleAnzeigenLinkText = computed(() => store.getters['EinstellungenState/getAlleAnzeigenLinkText']);
const alleAnzeigenLinkZiel = computed(() => store.getters['EinstellungenState/getAlleAnzeigenLinkZiel']);
const produktfilterAnzeigen = computed<boolean>(
  () =>
    !store.getters['EinstellungenState/getIstFilterAusgeblendet'] &&
    store.getters['FeatureState/getFeatureStatus'](FeatureToggles.PRODUKTFILTER_ANZEIGEN)
);
const zeigeNulltrefferseite = computed(() => {
  return produktfilterAnzeigen.value && istFilterAktiv.value && produkte.value.length === 0;
});

watch(
  zeigeNulltrefferseite,
  (newValue, oldValue) => {
    if (newValue && !oldValue) {
      sendNulltrefferTrackingEvent();
    }
  },
  { immediate: true }
);

const sendTrackAlleAnzeigenKlick = trackAlleAnzeigenKlick;

onServerPrefetch(async () => {
  const route = useRoute();
  const queryParameters = QueryParameters.mapQueryToQueryParameters(route.query);

  // Diese Storeänderungen sind unabhängig voneinander und können daher gemeinsam awaited werden
  await Promise.all([
    store.commit('EinstellungenState/setEinstellungenFromQueryparameters', route.query),
    store.dispatch('FeatureState/fetchFeatureTogglesSSR'),
    store.commit('ProduktfilterState/setCmsFilter', queryParameters, { root: true }),
  ]);

  // Im folgenden sind wir abhängig von den vorangegangenen Änderungen und müssen daher sequentiel awaiten
  const produktfilterAnzeigen =
    !store.getters['EinstellungenState/getIstFilterAusgeblendet'] &&
    store.getters['FeatureState/getFeatureStatus'](FeatureToggles.PRODUKTFILTER_ANZEIGEN);

  if (produktfilterAnzeigen) {
    await store.dispatch('ProdukteState/fetchProdukteMitVerfuegbarenFilterwertenSSR', {
      istAktionAlleFilterZuruecksetzen: false,
      useDebouncedAPI: false,
    });
  } else {
    await store.dispatch('ProdukteState/fetchProdukteSSR');
  }
});
</script>

<style lang="scss">
.erk_finden__produktliste {
  margin: 0 auto;
  max-width: 100%;

  &-headline {
    margin-bottom: 24px;
  }

  &-produkte {
    position: relative;
    margin-top: 8px;
  }

  &-produkte-vogel {
    position: absolute;
    right: 12px;
    top: -66px;
    height: 66px;

    svg {
      transform: scale(-1, 1);
      --hauptfarbe: #{$color-dark};
    }
  }

  &-alle-anzeigen-link {
    @include font-Rubik-light(18);
    color: $color-dark;
    padding-left: 8px;
    padding-right: 8px;
    text-align: center;

    &:visited {
      color: $color-dark;
    }
  }

  &-alle-anzeigen-link-container {
    width: 100%;
    display: flex;
    justify-content: center;
    padding-top: 32px;
  }
}
</style>
