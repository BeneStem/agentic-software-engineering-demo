import {
  FarbeDTO,
  KlassifikationDTO,
  LiefertageDTO,
  PreisbereichDTO,
  ProduktnummerDto,
  SorteDTO,
} from '@/main/frontend/app/model/VerfuegbareFilterwerte';

export type ProdukteFilterParameter = {
  farben: FarbeDTO[];
  blumensorten: SorteDTO[];
  sorten: SorteDTO[];
  klassifikationen: KlassifikationDTO[];
  produktNummern: ProduktnummerDto[];
  produktnummernVerwendung: string | null;
  limit: number | null;
  liefertage: LiefertageDTO | null;
  preisbereich: PreisbereichDTO | null;
};

export type CmsProdukteFilterDTO = Omit<ProdukteFilterParameter, 'liefertage' | 'sorten'>;
export type UserProdukteFilterDTO = Omit<
  ProdukteFilterParameter,
  'blumensorten' | 'produktNummern' | 'produktnummernVerwendung' | 'limit'
>;
