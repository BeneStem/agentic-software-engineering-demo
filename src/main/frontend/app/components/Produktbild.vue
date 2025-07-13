<template>
  <div class="erk_finden__produktbild">
    <picture v-if="defaultImage">
      <source
        v-for="pictureSource in pictureSources"
        :key="pictureSource.media"
        :media="pictureSource.media"
        :type="pictureSource.type"
        :srcset="pictureSource.srcset"
      />
      <img :src="defaultImage" :srcset="defaultImageSrcSet" :alt="altText" loading="lazy" @error="onError" />
    </picture>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { PictureSource } from '@blume2000/design-system/dist/models/PictureSource';

interface Props {
  url: string;
  altText: string;
}

const props = defineProps<Props>();

const defaultDpr = 2;
const allDprs = [1, 2, 3];
const defaultImage = ref('');
const defaultImageSrcSet = ref('');

const finalUrl = (width: number, height: number, dpr: number) =>
  props.url.replace('%w%', width.toString()).replace('%h%', height.toString()).replace('%d%', dpr.toString());

const finalSrcSet = (width: number, height: number): string => {
  if (!width || !height) {
    return '';
  }
  return allDprs.map((dpr: number) => `${finalUrl(width, height, dpr)} ${dpr}x`).join(',');
};

defaultImage.value = finalUrl(328, 328, defaultDpr);
defaultImageSrcSet.value = finalSrcSet(328, 328);

const pictureSources: PictureSource[] = [
  {
    srcset: finalSrcSet(401, 401),
    media: '(min-width: 361px)',
    type: 'image/jpeg',
  },
];

const onError = () => {
  defaultImage.value = 'https://assets.ecom.blume2000.de/images/produkt_platzhalter.png';
  defaultImageSrcSet.value = '';
};
</script>
