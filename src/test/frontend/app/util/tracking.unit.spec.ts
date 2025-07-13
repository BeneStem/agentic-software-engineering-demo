import { pushInDataLayer } from '@/main/frontend/app/util/tracking';

describe('tracking.ts', () => {
  beforeEach(() => {
    window.dataLayer = [];
  });

  it('Schreibt Daten in den DataLayer', () => {
    pushInDataLayer({
      event: 'gaEvent',
      event_name: 'testEvent',
      testData: {
        irgendwas: 'Test',
      },
    });

    expect(window.dataLayer[0].event).toEqual('gaEvent');
    expect(window.dataLayer[0].testData.irgendwas).toEqual('Test');
  });
});
