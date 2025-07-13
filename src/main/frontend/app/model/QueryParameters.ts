export class QueryParameters {
  farben?: string[];
  blumensorten?: string[];
  klassifikation3?: string[];
  produktNummern?: string[];
  produktnummernVerwendung?: string;
  limit?: number;
  minPreis?: number;
  maxPreis?: number;

  static mapQueryToQueryParameters = (query: any): QueryParameters => {
    return {
      farben: query.farben ? convertStringToArray(query.farben) : [],
      blumensorten: query.blumensorten ? convertStringToArray(query.blumensorten) : [],
      klassifikation3: query.klassifikation3 ? convertStringToArray(query.klassifikation3) : [],
      produktNummern: query.produktNummern ? convertStringToArray(query.produktNummern) : [],
      produktnummernVerwendung: query.produktnummernVerwendung ? query.produktnummernVerwendung : null,
      limit: query.limit ? query.limit : null,
      minPreis: query.minPreis ? query.minPreis : null,
      maxPreis: query.maxPreis ? query.maxPreis : null,
    };
  };
}

const convertStringToArray = (value: string): string[] => {
  return value.toString().split(',');
};
