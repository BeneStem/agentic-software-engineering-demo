import { isServerSideIncludeUrl } from '@/main/frontend/ssrServer/utils/UrlUtils';

describe('UrlUtils', () => {
  describe('isServerSideIncludeUrl', () => {
    describe('with "finden/static/header/client/js/app.d1f740f6.js"', () => {
      it('should false', () => {
        expect(isServerSideIncludeUrl('finden/static/header/client/js/app.d1f740f6.js')).toEqual(false);
      });
    });

    describe('with "finden/static/"', () => {
      it('should false', () => {
        expect(isServerSideIncludeUrl('finden/static/')).toEqual(false);
      });
    });

    describe('with "ssi/finden/header/content"', () => {
      it('should true', () => {
        expect(isServerSideIncludeUrl('ssi/finden/header/content')).toEqual(true);
      });
    });

    describe('with "ssi/"', () => {
      it('should true', () => {
        expect(isServerSideIncludeUrl('ssi/')).toEqual(true);
      });
    });
  });
});
