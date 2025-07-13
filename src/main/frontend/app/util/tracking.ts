export function trackAlleAnzeigenKlick() {
  window.dataLayer = window.dataLayer || [];
  window.dataLayer.push({
    event: 'gaEvent',
    event_name: 'navigation',
    navigation: {
      navigation_bar: 'Links',
      navigation_item: 'show_all',
    },
  });
}

export function pushInDataLayer(data: unknown) {
  window.dataLayer = window.dataLayer || [];
  window.dataLayer.push(data);
}
