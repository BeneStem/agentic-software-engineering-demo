<template>
  <div class="erk_finden__keine-produkte-meldung__strasse">
    <div class="erk_finden__keine-produkte-meldung__strasse__sicherheitsabstand"></div>
    <transition name="erk_finden__keine-produkte-meldung__strasse__laster-animation" @after-leave="toggleLastwagen">
      <B2kIcon
        v-show="zeigeLastwagen"
        class="erk_finden__keine-produkte-meldung__strasse__laster"
        name="LastwagenMitBlumen"
        @click="toggleLastwagen"
      />
    </transition>
  </div>
  <TrennlinieDuenn />
  <UeberschriftDrei class="erk_finden__keine-produkte-meldung__schrift"
    >Wie es aussieht, befinden sich derzeit keine passenden Produkte in unserem Sortiment .. Stöber doch in den
    aktuellen Highlights der Saison!
  </UeberschriftDrei>
  <a href="/blumen">
    <Button typ="primaer" groesse="auto" theme="hell">Jetzt entdecken</Button>
  </a>
</template>

<script setup lang="ts">
import B2kIcon from '@blume2000/design-system/dist/komponenten/atome/B2kIcon.vue';
import UeberschriftDrei from '@blume2000/design-system/dist/komponenten/content/UeberschriftDrei.vue';
import Button from '@blume2000/design-system/dist/komponenten/interaktion/Button.vue';
import TrennlinieDuenn from '@blume2000/design-system/dist/komponenten/atome/TrennlinieDuenn.vue';
import { ref } from 'vue';

const zeigeLastwagen = ref(true);

function toggleLastwagen() {
  zeigeLastwagen.value = !zeigeLastwagen.value;
}
</script>

<style scoped lang="scss">
.erk_finden__keine-produkte-meldung {
  &__strasse {
    display: flex;
    overflow: hidden;
    margin-bottom: -12px;

    &__sicherheitsabstand {
      flex: 0.9;
    }

    &__laster {
      &-animation-leave-active {
        animation: lastwagen 8s ease-in-out 0s 1;
      }

      display: inline-block;
      position: relative;
      height: 90px;
      width: 80px;
    }
  }

  &__schrift {
    margin-top: 16px;
    margin-bottom: 16px;
  }
}

@keyframes lastwagen {
  0% {
    left: 0;
  }
  5% {
    rotate: 2deg;
  }
  10% {
    rotate: 1deg;
  }
  50% {
    left: -140%;
    opacity: 1;
  }
  51% {
    left: -200%;
    opacity: 0;
  }
  52% {
    left: 200%;
    opacity: 0;
  }
  53% {
    left: 200%;
    opacity: 1;
  }
  60% {
    left: 200%;
    opacity: 1;
  }
  92% {
    rotate: 0deg;
  }
  97% {
    rotate: -2deg;
    left: 0;
  }
  100% {
    rotate: 0deg;
    left: 0;
  }
}
</style>
