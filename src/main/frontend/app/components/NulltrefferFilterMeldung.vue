<template>
  <div class="erk_finden__nulltreffermeldung">
    <div class="erk_finden__nulltreffermeldung__vogelkaefig">
      <B2kIcon name="VogelAnVase" class="erk_finden__nulltreffermeldung__vogel" />
      <TrennlinieDick class="erk_finden__nulltreffermeldung__trennlinie" />
    </div>
    <div class="erk_finden__nulltreffermeldung__ueberschrift">
      <UeberschriftDrei>Für diese Filterkombination haben wir leider keine Ergebnisse für dich. </UeberschriftDrei>
      <UeberschriftDrei>Versuchs noch mal!</UeberschriftDrei>
    </div>
    <Button
      typ="sekundaer"
      groesse="auto"
      class="erk_finden__nulltreffermeldung__filterruecksetzenbutton"
      @click="filterruecksetzen()"
    >
      Alle Filter zurücksetzen
    </Button>
  </div>
</template>

<script setup lang="ts">
import TrennlinieDick from '@blume2000/design-system/dist/komponenten/atome/TrennlinieDick.vue';
import B2kIcon from '@blume2000/design-system/dist/komponenten/atome/B2kIcon.vue';
import UeberschriftDrei from '@blume2000/design-system/dist/komponenten/content/UeberschriftDrei.vue';
import Button from '@blume2000/design-system/dist/komponenten/interaktion/Button.vue';
import { useStore } from 'vuex';

const store = useStore();

const filterruecksetzen = () => {
  store.commit('ProduktfilterState/setAlleFilterZuruecksetzen');
};
</script>

<style lang="scss" scoped>
.erk_finden__nulltreffermeldung {
  margin-top: 72px;

  &__ueberschrift {
    margin: 24px 0;
  }

  &__vogelkaefig {
    position: relative;
    margin-top: 72px;
    margin-bottom: 24px;
  }

  &__vogel {
    position: absolute;
    top: -67px;
    height: 92px;
    width: 128px;
    --hauptfarbe: #{$color-dark};
  }

  &__trennlinie {
    --hauptfarbe: #{$color-dark};
  }
}

@media (min-width: $breakpoint-medium) {
  .erk_finden__nulltreffermeldung {
    margin: 128px 0;

    &__filterruecksetzenbutton {
      padding-top: 24px;
    }
  }
}
</style>
