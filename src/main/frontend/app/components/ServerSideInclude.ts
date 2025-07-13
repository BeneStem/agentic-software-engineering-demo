import { defineComponent, h } from 'vue';

export default defineComponent({
  render() {
    const innerHTML =
      typeof window === 'undefined'
        ? `<!--#include virtual="${this.to}" -->`
        : window.SSI_STORE && window.SSI_STORE[this.ssiStoreKey]
        ? window.SSI_STORE[this.ssiStoreKey]
        : '';

    return h(this.elementName, {
      innerHTML,
      class: 'b2k_ssi',
      ['ssi-store-key']: this.ssiStoreKey,
      ...this.attributes,
    });
  },
  props: {
    to: { type: String, required: true },
    elementName: { type: String, required: true },
    attributes: { type: Object, default: () => ({}) },
    ssiStoreKey: { type: String, required: true },
  },
});
