import {
  getCacheControlHeaderValueForStaticFiles,
  getCacheControlHeaderValueForServices,
  buildCacheControlHeaderValue,
} from '@/main/frontend/ssrServer/utils/CacheUtils';

describe('CacheUtils', () => {
  describe('getCacheControlHeaderValueForStaticFiles', () => {
    it('should return 30 years cache header', () => {
      expect(getCacheControlHeaderValueForStaticFiles()).toBe('public, max-age=946080000');
    });
  });

  describe('getCacheControlHeaderValueForServices', () => {
    it('should return 10 seconds cache header', () => {
      expect(getCacheControlHeaderValueForServices()).toBe('public, max-age=10');
    });
  });

  describe('buildCacheControlHeaderValue', () => {
    describe('with 0 cache time in seconds', () => {
      it('should return no-cache', () => {
        expect(buildCacheControlHeaderValue(0)).toBe('no-cache, no-store');
      });
    });

    describe('with -1 cache time in seconds', () => {
      it('should return no-cache', () => {
        expect(buildCacheControlHeaderValue(-1)).toBe('no-cache, no-store');
      });
    });

    describe('with 30 cache time in seconds', () => {
      it('should return 30 seconds cache time', () => {
        expect(buildCacheControlHeaderValue(30)).toBe('public, max-age=30');
      });
    });
  });
});
