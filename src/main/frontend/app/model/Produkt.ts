import { ProduktVerfuegbarkeit, ProduktVerfuegbarkeitDTO } from '@/main/frontend/app/model/ProduktVerfuegbarkeit';
import { parseISO } from 'date-fns';

export class Produkt {
  nummer: string;
  name: string;
  preis: string;
  streichpreis: string;
  zeigeStreichpreis: boolean;
  klassifikationName: string;
  bildUrl: string;
  produktUrl: string;
  naechstmoeglicheVerfuegbarkeit: ProduktVerfuegbarkeit;
  letztmoeglicheVerfuegbarkeit: ProduktVerfuegbarkeit;

  constructor(
    nummer: string,
    name: string,
    preis: string,
    streichpreis: string,
    zeigeStreichpreis: boolean,
    klassifikationName: string,
    bildUrl: string,
    produktUrl: string,
    naechstmoeglicheVerfuegbarkeit: ProduktVerfuegbarkeit,
    letztmoeglicheVerfuegbarkeit: ProduktVerfuegbarkeit
  ) {
    this.nummer = nummer;
    this.name = name;
    this.preis = preis;
    this.streichpreis = streichpreis;
    this.zeigeStreichpreis = zeigeStreichpreis;
    this.klassifikationName = klassifikationName;
    this.bildUrl = bildUrl;
    this.produktUrl = produktUrl;
    this.naechstmoeglicheVerfuegbarkeit = naechstmoeglicheVerfuegbarkeit;
    this.letztmoeglicheVerfuegbarkeit = letztmoeglicheVerfuegbarkeit;
  }

  static vonProduktDTO(produktDTO: ProduktDTO): Produkt {
    return {
      ...produktDTO,
      naechstmoeglicheVerfuegbarkeit: {
        bestellschluss: parseISO(produktDTO.naechstmoeglicheVerfuegbarkeit.bestellschluss),
        liefertag: parseISO(produktDTO.naechstmoeglicheVerfuegbarkeit.liefertag),
      },
      letztmoeglicheVerfuegbarkeit: {
        bestellschluss: parseISO(produktDTO.letztmoeglicheVerfuegbarkeit.bestellschluss),
        liefertag: parseISO(produktDTO.letztmoeglicheVerfuegbarkeit.liefertag),
      },
    };
  }
}

export type ProduktDTO = Omit<Produkt, 'naechstmoeglicheVerfuegbarkeit' | 'letztmoeglicheVerfuegbarkeit'> & {
  naechstmoeglicheVerfuegbarkeit: ProduktVerfuegbarkeitDTO;
  letztmoeglicheVerfuegbarkeit: ProduktVerfuegbarkeitDTO;
};
