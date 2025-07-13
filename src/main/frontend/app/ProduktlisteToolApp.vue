<template>
  <main class="erk_finden__checker">
    <h1>Produktnummern prüfen</h1>
    <div class="erk_finden__checker__container">
      <p>
        Bitte geben Sie die gewünschten Produktnummern mit Kommas getrennt und ohne Leer- oder andere Sonderzeichen ein.
        Klicken Sie anschließend auf "Prüfen".
        <br />
        Nach erfolgreicher Prüfung können die gültigen Produktnummern mit "Kopieren" in die Zwischenablage übertragen
        werden.
      </p>
      <textarea v-model="produktnummern" placeholder="Produktnummern (mit Kommas getrennt)" />
      <div class="erk_finden__checker__container_controls">
        <button :disabled="isLoading" @click="onClick">Prüfen</button>
        <div v-if="errorMessage !== ''" class="erk_finden__checker__container_controls_error">{{ errorMessage }}</div>
      </div>
    </div>
    <div class="erk_finden__checker__container">
      <textarea v-model="verfuegbareProduktnummern"></textarea>
      <div class="erk_finden__checker__container_controls">
        <button :disabled="!verfuegbareProduktnummern" @click="onCopy">Kopieren</button>
        <div class="erk_finden__checker__container_controls_success" v-if="copyHintVisible">
          Produktnummern in die Zwischenablage kopiert!
        </div>
      </div>
    </div>
  </main>
</template>

<script lang="ts">
import axios from 'axios';
import { defineComponent, ref } from 'vue';

export default defineComponent({
  name: 'ProduktlisteToolApp',
  components: {},
  setup() {
    const produktnummern = ref('');
    const verfuegbareProduktnummern = ref('');
    const copyHintVisible = ref(false);
    const isLoading = ref(false);
    const errorMessage = ref('');

    const onClick = async () => {
      if (produktnummern.value === '') {
        errorMessage.value = 'Bitte geben Sie mindestens eine Produktnummer ein!';
        return;
      }

      isLoading.value = true;
      errorMessage.value = '';

      axios
        .post('/api/finden/tool/produktliste', produktnummern.value.split(','), {
          headers: {
            Accept: 'application/json',
            'Content-Type': 'application/json',
          },
        })
        .then((result) => {
          verfuegbareProduktnummern.value = result.data;
        })
        .catch((error) => {
          console.log(error.response.status);
          if (error.response.status >= 500) {
            errorMessage.value = 'Server nicht erreichbar. Bitte versuchen Sie es später erneut.';
          } else {
            errorMessage.value = 'Unbekannter Fehler.';
          }
        })
        .finally(() => {
          isLoading.value = false;
        });
    };

    const onCopy = () => {
      navigator.clipboard.writeText(verfuegbareProduktnummern.value);
      copyHintVisible.value = true;
      setTimeout(() => {
        copyHintVisible.value = false;
      }, 5000);
    };

    return {
      produktnummern,
      verfuegbareProduktnummern,
      onClick,
      onCopy,
      isLoading,
      copyHintVisible,
      errorMessage,
    };
  },
});
</script>

<style lang="scss" scoped>
.erk_finden {
  &__checker {
    margin: auto;
    width: 40%;
    padding: 8px;
    font-family: Helvetica, sans-serif;

    button {
      margin-top: 12px;
    }

    textarea {
      width: 100%;
      height: 300px;
    }

    &__container {
      margin-top: 24px;

      &_controls {
        display: grid;
        grid-template-columns: min-content auto;
        grid-gap: 12px;
        font-size: 14px;

        &_error {
          color: $color-alert;
          align-self: end;
        }

        &_success {
          color: $color-highlight;
          align-self: end;
        }
      }
    }
  }
}
</style>
