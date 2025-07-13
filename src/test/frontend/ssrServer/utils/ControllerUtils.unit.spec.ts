import { createCssLinkTag, createScriptTag } from '@/main/frontend/ssrServer/utils/ControllerUtils';

describe('ControllerUtils', () => {
  describe('createCssLinkTag', () => {
    it('should return correct css link tag', () => {
      expect(
        createCssLinkTag({
          appName: 'appName',
          filePath: '/filePath',
        })
      ).toEqual("<link rel='stylesheet' href='/finden/static/appName/client/filePath' />");
    });

    it.each<[string | undefined]>([[undefined], ['']])('should return empty string if file path is: %s', (filePath) => {
      expect(
        createCssLinkTag({
          appName: 'appName',
          filePath,
        })
      ).toEqual('');
    });
  });

  describe('createScriptTag', () => {
    it('should return correct css link tag', () => {
      expect(
        createScriptTag({
          appName: 'appName',
          filePath: '/filePath',
        })
      ).toEqual("<script type='text/javascript' src='/finden/static/appName/client/filePath'></script>");
    });

    it.each<[string | undefined]>([[undefined], ['']])('should return empty string if file path is: %s', (filePath) => {
      expect(
        createScriptTag({
          appName: 'appName',
          filePath,
        })
      ).toEqual('');
    });
  });
});
