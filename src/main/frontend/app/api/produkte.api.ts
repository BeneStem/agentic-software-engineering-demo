import httpClient from '@/main/frontend/app/api/httpClient';
import httpClientSSR from '@/main/frontend/app/api/httpClientSSR';
import { ProdukteFilter } from '@/main/frontend/app/store/ProduktfilterState';
import { CmsProdukteFilterDTO } from '@/main/frontend/app/model/ProdukteFilterParameter';

const endPointProdukteMitCMSFilter = '/api/finden/produkte';
const endPointProdukteMitCMSUndUserfilter = '/api/finden/produkte/mituserfilterung';

const getAlleProdukteSSR = (filter: CmsProdukteFilterDTO | null) =>
  httpClientSSR.post(endPointProdukteMitCMSFilter, filter);

const getAlleProdukteMitVerfuegbarenFilterwerten = (filter: ProdukteFilter) =>
  httpClient.post(endPointProdukteMitCMSUndUserfilter, filter);

const getAlleProdukteMitVerfuegbarenFilterwertenSSR = (filter: ProdukteFilter) =>
  httpClientSSR.post(endPointProdukteMitCMSUndUserfilter, filter);

export {
  getAlleProdukteSSR,
  getAlleProdukteMitVerfuegbarenFilterwerten,
  getAlleProdukteMitVerfuegbarenFilterwertenSSR,
};
