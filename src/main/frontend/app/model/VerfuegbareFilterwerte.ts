import { ProduktDTO } from '@/main/frontend/app/model/Produkt';

export type KlassifikationDTO = {
  name: string;
  id: string;
};

export type LiefertageDTO = {
  verfuegbareLiefertage: string[];
};

export type PreisbereichDTO = {
  minPreis?: PreisDTO | null;
  maxPreis?: PreisDTO | null;
  selectedMinPreis?: number;
  selectedMaxPreis?: number;
};

export class SorteDTO {
  name: string;
  constructor(name: string) {
    this.name = name;
  }
}

export type FarbeDTO = {
  name: string;
};

export type PreisDTO = {
  bruttoBetrag: number | null;
  waehrung: string;
};

export type ProduktnummerDto = {
  nummer: string;
};

export class VerfuegbareFilterwerteDTO {
  sorten: SorteDTO[];
  farben: FarbeDTO[];
  preisbereich: PreisbereichDTO;
  klassifikationen: KlassifikationDTO[];
  liefertage: LiefertageDTO;

  constructor(
    sorten: SorteDTO[],
    farben: FarbeDTO[],
    preisbereich: PreisbereichDTO,
    klassifikationen: KlassifikationDTO[],
    liefertage: LiefertageDTO
  ) {
    this.sorten = sorten;
    this.farben = farben;
    this.preisbereich = preisbereich;
    this.klassifikationen = klassifikationen;
    this.liefertage = liefertage;
  }
}

export type ProdukteMitVerfuegbarenFilterwertenDTO = {
  produkte: ProduktDTO[];
  verfuegbareFilterwerte: VerfuegbareFilterwerteDTO;
};

export interface Liefertag {
  name: string;
}
