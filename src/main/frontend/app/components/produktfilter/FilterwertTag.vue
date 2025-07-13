<template>
  <div :class="['erk_finden__filterwerttag', { 'erk_finden__filterwerttag--selected': ausgewaehlt }]">
    <div class="erk_finden__filterwerttag-background" />
    <div class="erk_finden__filterwerttag-sprenkel">
      <B2kIcon name="Sprenkel" />
    </div>
    <div class="erk_finden__filterwerttag-top-left">
      <svg viewBox="0 0 70 81" fill="none" preserveAspectRatio="none" xmlns="http://www.w3.org/2000/svg">
        <path
          class="erk_finden__filterwerttag--fill-color"
          d="M13.1029 46.8024C15.225 36.7229 17.3471 27.7042 24.7747 20.8077C31.1411 14.9722 40.1602 13.3807 50.2405 11.2587C56.6069 11.2587 62.9733 10.4485 69.3397 10.979V0.326451C55.0152 -0.734553 40.1603 1.17911 26.3663 4.89262C18.9388 8.07563 13.1028 13.3807 9.38904 20.8077C2.49205 32.4787 -0.691152 45.7414 0.369923 59.5344C0.369923 64.3089 0.169922 73.3274 0.169922 80.224C3.88368 80.224 7.09767 80.224 10.8114 80.224C9.21982 65.3699 11.5112 53.6989 13.1029 46.8024Z"
        />
      </svg>
    </div>
    <div class="erk_finden__filterwerttag-top erk_finden__filterwerttag--border-color"></div>
    <div class="erk_finden__filterwerttag-top-right">
      <svg viewBox="0 0 70 81" fill="none" preserveAspectRatio="none" xmlns="http://www.w3.org/2000/svg">
        <path
          class="erk_finden__filterwerttag--fill-color"
          d="M57.1431 46.802C55.021 36.7224 52.8989 27.7038 45.4714 20.8073C39.1049 14.9717 30.0857 13.3803 20.0055 11.2583C13.639 11.2583 7.27271 10.4486 0.90625 10.9791V0.326028C15.2308 -0.734975 30.0857 1.17869 43.8797 4.8922C51.3072 8.07521 57.1432 13.3802 60.857 20.8073C67.754 32.4783 70.9372 45.7409 69.8762 59.534C69.8762 64.3085 70.0762 73.3279 70.0762 80.2244C66.3624 80.2244 63.1484 80.2235 59.4346 80.2235C61.0263 65.3695 58.7348 53.6985 57.1431 46.802Z"
        />
      </svg>
    </div>
    <div class="erk_finden__filterwerttag-left erk_finden__filterwerttag--border-color"></div>
    <div class="erk_finden__filterwerttag-right erk_finden__filterwerttag--border-color"></div>
    <div class="erk_finden__filterwerttag-bottom-left">
      <svg viewBox="0 0 70 80" fill="none" preserveAspectRatio="none" xmlns="http://www.w3.org/2000/svg">
        <path
          class="erk_finden__filterwerttag--fill-color"
          d="M13.1029 33.5428C15.225 43.6223 17.3471 52.6408 24.7747 59.5374C31.1411 65.3729 40.1602 66.9643 50.2405 69.0863C56.6069 69.0863 63.6336 69.6168 70 69.0863V80C55.6755 81.061 40.1603 79.1659 26.3663 75.4524C18.9388 72.2694 13.1028 66.9644 9.38904 59.5374C2.49205 47.8663 -0.691152 34.6037 0.369923 20.8106C0.369923 16.0361 0.169922 7.01762 0.169922 0.121094C3.88368 0.121094 7.09767 0.121094 10.8114 0.121094C9.21982 14.9751 11.5112 26.6463 13.1029 33.5428Z"
        />
      </svg>
    </div>
    <div class="erk_finden__filterwerttag-bottom erk_finden__filterwerttag--border-color"></div>
    <div class="erk_finden__filterwerttag-bottom-right">
      <svg viewBox="0 0 71 80" fill="none" preserveAspectRatio="none" xmlns="http://www.w3.org/2000/svg">
        <path
          class="erk_finden__filterwerttag--fill-color"
          d="M71 0H60.2711C60.2711 1.81208 60.0924 1.81209 60.0924 3.62417C54.9416 6.04028 61.8093 16.9128 60.0924 23.5571C42.9231 38.6577 61.3801 62.8188 30.0462 63.4228C26.1831 63.4228 22.2923 63.896 18 64.5C16.7123 63.896 13.2877 66.104 12 65.5C8.13692 65.5 3.86308 68 0 68V79C3.00462 79 6.00927 79.7315 9.01389 79.7315C11.5893 79.7315 14.1646 80.3356 17.1693 79.7315C22.7493 79.7315 28.7585 79.7315 33.4801 79.7315C48.5032 79.1275 54.9416 75.5034 63.5263 59.1946C66.5309 48.9262 68.2478 38.0537 68.677 27.1812C68.677 25.3691 68.677 23.557 68.677 21.1409C69.1063 17.5168 70.4292 14.1242 70 10.5C70 9.29195 71 3.02013 71 0Z"
        />
      </svg>
    </div>
    <div class="erk_finden__filterwerttag-center">
      <label>
        <input type="checkbox" :checked="ausgewaehlt" @change="$emit('change', $event.target.checked)" />
        {{ name }}
      </label>
    </div>
  </div>
</template>

<script setup lang="ts">
import B2kIcon from '@blume2000/design-system/dist/komponenten/atome/B2kIcon.vue';

defineProps<{
  name: string;
  ausgewaehlt: boolean;
}>();

defineEmits<{
  (e: 'change', value: boolean): void;
}>();
</script>

<style lang="scss" scoped>
%svg-container {
  position: relative;

  svg {
    position: absolute;
    width: 100%;
    height: 100%;
  }
}

.erk_finden__filterwerttag {
  display: inline-grid;
  grid-template-columns: 14px max-content 14px;
  grid-template-rows: 15px max-content 15px;
  grid-template-areas:
    'top-left top top-right'
    'left center right'
    'bottom-left bottom bottom-right';
  text-align: left;
  font-family: initial;
  font-size: 16px;
  pointer-events: none;
  margin-left: 8px;
  margin-right: 8px;
  margin-bottom: 16px;
  -webkit-tap-highlight-color: transparent;

  &-top-left {
    @extend %svg-container;
    grid-area: top-left;
  }

  &-top-right {
    @extend %svg-container;
    grid-area: top-right;
  }

  &-bottom-left {
    @extend %svg-container;
    grid-area: bottom-left;
  }

  &-bottom-right {
    @extend %svg-container;
    grid-area: bottom-right;
  }

  &-top {
    grid-area: top;
    border-top: 2px solid $color-dark;
    margin-left: -1px;
    margin-right: -1px;
  }

  &-left {
    grid-area: left;
    border-left: 2px solid $color-dark;
    margin-top: -1px;
    margin-bottom: -1px;
  }

  &-right {
    grid-area: right;
    border-right: 2px solid $color-dark;
    margin-top: -1px;
    margin-bottom: -1px;
  }

  &-bottom {
    grid-area: bottom;
    border-bottom: 2px solid $color-dark;
    margin-left: -1px;
    margin-right: -1px;
  }

  &-background {
    grid-column: 1 / -1;
    grid-row: 1 / -1;
    z-index: -2;
    margin: 1px 1px 2px;
    border-radius: 8px;
  }

  &-sprenkel {
    grid-column: 1 / -1;
    grid-row: 1 / -1;
    z-index: -1;
    overflow: hidden;
    border-radius: 8px;
    position: relative;

    svg {
      position: absolute;
      height: 100%;
      width: 30px;
      right: 0;
    }
  }

  &-center {
    grid-column: 1 / -1;
    grid-row: 1 / -1;
    overflow: hidden;
    border-radius: 8px;

    label {
      display: flex;
      align-items: center;
      height: 36px;
      padding: 0 14px;
      @include font-Rubik-light(16);
      cursor: pointer;
      user-select: none;
      pointer-events: auto;
      position: relative;

      input {
        position: absolute;
        opacity: 0;
        width: 1px;
        height: 1px;
        left: -10px;
      }
    }
  }

  &--fill-color {
    fill: $color-dark;
  }

  &--border-color {
    border-color: $color-dark;
  }

  &--selected {
    .erk_finden__filterwerttag-background {
      background-color: $color-highlight;
    }
  }

  @include hasHover {
    &:hover {
      .erk_finden__filterwerttag-background {
        background-color: $color-inactive;
      }
    }
  }
}
</style>
