import { removeQueryParams } from '@/main/frontend/ssrServer/utils/MonitoringUtils';

describe('MonitoringUtil', () => {
  it('Should not manipulate URLs without query params', () => {
    expect(removeQueryParams('/ssi/finden/produktliste')).toEqual('/ssi/finden/produktliste');
    expect(removeQueryParams('/ssi/finden/')).toEqual('/ssi/finden/');
    expect(removeQueryParams('/')).toEqual('/');
  });

  it('Should remove query params', () => {
    expect(
      removeQueryParams('/ssi/finden/produktliste?produktIds=1,2,3,4711,1337&farben=dunkelschwarz,neongrau')
    ).toEqual('/ssi/finden/produktliste');
    expect(removeQueryParams('/ssi/finden/produktliste?')).toEqual('/ssi/finden/produktliste');
  });
});
