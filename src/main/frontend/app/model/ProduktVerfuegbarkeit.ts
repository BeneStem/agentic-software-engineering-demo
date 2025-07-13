export class ProduktVerfuegbarkeit {
  bestellschluss: Date;
  liefertag: Date;

  constructor(bestellschluss: Date, liefertag: Date) {
    this.bestellschluss = bestellschluss;
    this.liefertag = liefertag;
  }
}

export type ProduktVerfuegbarkeitDTO = {
  bestellschluss: string;
  liefertag: string;
};
