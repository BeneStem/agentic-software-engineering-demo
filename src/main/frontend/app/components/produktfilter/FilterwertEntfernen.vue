<template>
  <button type="button" role="tab" @click="filterwertEntfernen" class="erk_finden__filterwert-entfernen">
    {{ filterwertName }}
    <B2kIcon name="Schliessen" class="erk_finden__filterwert-entfernen-schliessen" />
  </button>
</template>

<script setup lang="ts">
import B2kIcon from '@blume2000/design-system/dist/komponenten/atome/B2kIcon.vue';

defineProps<{
  filterwertName: string;
}>();

const emit = defineEmits(['filterwertEntfernen']);

function filterwertEntfernen() {
  emit('filterwertEntfernen');
}
</script>

<style lang="scss" scoped>
button {
  display: flex;
  align-items: center;
  @include font-Rubik-light(16);
}
.erk_finden__filterwert-entfernen {
  display: flex;

  &-schliessen {
    margin-left: 4px;
    width: 16px;
    height: 16px;
  }
}
</style>
