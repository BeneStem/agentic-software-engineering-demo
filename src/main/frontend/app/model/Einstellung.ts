export class Einstellung {
  id: string | undefined;
  ueberschrift: string | undefined;
  istHauptueberschrift: boolean;
  alleAnzeigenLinkText: string | undefined;
  alleAnzeigenLinkZiel: string | undefined;
  trackingname: string | undefined;
  trackingnummer: string | undefined;
  istFilterAusgeblendet: boolean;

  constructor(
    id: string | undefined,
    name: string | undefined,
    istHauptueberschrift: boolean,
    alleAnzeigenLinkText: string | undefined,
    alleAnzeigenLinkZiel: string | undefined,
    trackingname: string | undefined,
    trackingnummer: string | undefined,
    istFilterAusgeblendet: boolean
  ) {
    this.id = id;
    this.ueberschrift = name;
    this.istHauptueberschrift = istHauptueberschrift;
    this.alleAnzeigenLinkText = alleAnzeigenLinkText;
    this.alleAnzeigenLinkZiel = alleAnzeigenLinkZiel;
    this.trackingname = trackingname;
    this.trackingnummer = trackingnummer;
    this.istFilterAusgeblendet = istFilterAusgeblendet;
  }
}
