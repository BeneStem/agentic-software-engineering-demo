import serializeJavascript from 'serialize-javascript';

export function renderAppState(appId: string, storeKey: string, state: unknown): string {
  const localStoreKey = `${storeKey}__${appId}`;
  const autoRemove =
    ';(function(){var s;(s=document.currentScript||document.scripts[document.scripts.length-1]).parentNode.removeChild(s);}());';
  const initializeStateScript = `
    <script type='text/javascript'>
      if(!window.__INITIAL_STATE__) {
          window.__INITIAL_STATE__ = {}
      }
      window.__INITIAL_STATE__["${localStoreKey}"] = ${serializeJavascript(state)}
      ${autoRemove}
    </script>
  `;
  return state ? initializeStateScript : '';
}
