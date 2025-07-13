import { Filterwert, FilterwertMitId } from '@/main/frontend/app/model/Filterwert';
import { FilterTyp } from '@/main/frontend/app/model/FilterTyp';
import { PreisbereichDTO } from '@/main/frontend/app/model/VerfuegbareFilterwerte';

export const testFilterwerteSorten = (): Map<string, Filterwert> => {
  const sorten = new Map<string, Filterwert>();
  sorten.set('Lilien', { name: 'Lilien', lesbarerName: 'Lilien', selected: false, filtertyp: FilterTyp.SORTE });
  sorten.set('Nelken', { name: 'Nelken', lesbarerName: 'Nelken', selected: true, filtertyp: FilterTyp.SORTE });
  return sorten;
};

export const testFilterwerteFarben = (): Map<string, Filterwert> => {
  const farben = new Map<string, Filterwert>();
  farben.set('rot', { name: 'rot', lesbarerName: 'Rot', selected: true, filtertyp: FilterTyp.FARBE, sortIndex: 5 });
  farben.set('gelb', { name: 'gelb', lesbarerName: 'Gelb', selected: false, filtertyp: FilterTyp.FARBE, sortIndex: 3 });
  return farben;
};

export const testFilterwertePreisbereich = (): PreisbereichDTO => {
  return {
    minPreis: {
      bruttoBetrag: 2,
      waehrung: 'EUR',
    },
    maxPreis: {
      bruttoBetrag: 55,
      waehrung: 'EUR',
    },
    selectedMinPreis: 2,
    selectedMaxPreis: 20,
  };
};

export const testFilterwerteKlassifikationen = (): Map<string, FilterwertMitId> => {
  const klassifikationen = new Map<string, FilterwertMitId>();
  klassifikationen.set('ID1', {
    id: 'ID1',
    name: 'Strauß',
    lesbarerName: 'Strauß',
    selected: true,
    filtertyp: FilterTyp.PRODUKTART,
  });
  klassifikationen.set('ID2', {
    id: 'ID2',
    name: 'Marktware',
    lesbarerName: 'Marktware',
    selected: false,
    filtertyp: FilterTyp.PRODUKTART,
  });
  return klassifikationen;
};

export const testFilterwerteLiefertage = (): Map<string, Filterwert> => {
  const liefertage = new Map<string, Filterwert>();
  liefertage.set('2022-04-08', {
    name: '2022-04-08',
    lesbarerName: '08.04.2022',
    selected: true,
    filtertyp: FilterTyp.VERFUEGBARKEIT,
  });
  liefertage.set('2022-04-09', {
    name: '2022-04-09',
    lesbarerName: '09.04.2022',
    selected: false,
    filtertyp: FilterTyp.VERFUEGBARKEIT,
  });
  return liefertage;
};

// helper for testing action with expected mutations
export interface TestActionProps {
  action: any;
  state: any;
  getters?: any;
  payload: any;
  expectedMutations: any;
  done: any;
}
export const testAction = ({ action, state, getters, payload, expectedMutations, done }: TestActionProps) => {
  let count = 0;

  // mock commit
  const commit = (type: any, payload: any) => {
    const mutation = expectedMutations[count];

    try {
      expect(type).toEqual(mutation.type);
      expect(payload).toEqual(mutation.payload);
    } catch (error) {
      done(error);
    }

    count++;
    if (count >= expectedMutations.length) {
      done();
    }
  };

  // call the action with mocked store and arguments
  action({ commit, state, getters }, payload);

  // check if no mutations should have been dispatched
  if (expectedMutations.length === 0) {
    expect(count).toEqual(0);
    done();
  }
};
