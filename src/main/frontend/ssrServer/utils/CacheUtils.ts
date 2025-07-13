const oneSecond = 1;
const oneMinute = 60 * oneSecond;
const oneHour = 60 * oneMinute;
const oneDay = 24 * oneHour;
const oneYear = 365 * oneDay;

const cacheTimeInSeconds = {
  staticFiles: oneYear * 30,
  services: oneSecond * 10,
};

export const buildCacheControlHeaderValue = (cacheTimeInSeconds: number): string => {
  if (cacheTimeInSeconds > 0) {
    return `public, max-age=${cacheTimeInSeconds}`;
  }
  return 'no-cache, no-store';
};

export const getCacheControlHeaderValueForStaticFiles = (): string => {
  return buildCacheControlHeaderValue(cacheTimeInSeconds.staticFiles);
};

export const getCacheControlHeaderValueForServices = (): string => {
  return buildCacheControlHeaderValue(cacheTimeInSeconds.services);
};
