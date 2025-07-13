import { Produkt, ProduktDTO } from '@/main/frontend/app/model/Produkt';
import { formatISO } from 'date-fns';

export const testProdukt: Produkt = {
  nummer: 'abc123',
  name: 'Produktname',
  preis: '10.00',
  streichpreis: '10.00',
  zeigeStreichpreis: false,
  klassifikationName: 'Strauss',
  bildUrl: 'https://assets.ecom.blume2000.de/images/produkt_platzhalter.png',
  produktUrl: '#',
  naechstmoeglicheVerfuegbarkeit: {
    bestellschluss: new Date(),
    liefertag: new Date(),
  },
  letztmoeglicheVerfuegbarkeit: {
    bestellschluss: new Date(),
    liefertag: new Date(),
  },
};

export const testProduktDto: ProduktDTO = {
  ...testProdukt,
  naechstmoeglicheVerfuegbarkeit: {
    liefertag: formatISO(new Date()),
    bestellschluss: formatISO(new Date()),
  },
  letztmoeglicheVerfuegbarkeit: {
    liefertag: formatISO(new Date()),
    bestellschluss: formatISO(new Date()),
  },
};

export const testProduktMitStreichpreis: Produkt = {
  nummer: 'abc123',
  name: 'Produktname',
  preis: '10.00',
  streichpreis: '20.00',
  zeigeStreichpreis: true,
  klassifikationName: 'Strauss',
  bildUrl: 'https://assets.ecom.blume2000.de/images/produkt_platzhalter.png',
  produktUrl: '#',
  naechstmoeglicheVerfuegbarkeit: {
    bestellschluss: new Date(),
    liefertag: new Date(),
  },
  letztmoeglicheVerfuegbarkeit: {
    bestellschluss: new Date(),
    liefertag: new Date(),
  },
};

export const testProduktListe = [testProdukt, testProduktMitStreichpreis];
