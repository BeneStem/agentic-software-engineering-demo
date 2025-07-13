import { FilterTyp } from '@/main/frontend/app/model/FilterTyp';

export type FilterwertMitId = {
  id: string;
  name: string;
  lesbarerName: string;
  selected: boolean;
  filtertyp: FilterTyp;
  sortIndex?: number;
};

export type Filterwert = Omit<FilterwertMitId, 'id'>;
