import axios from 'axios';

const httpClientSSR = axios.create({
  baseURL: __FINDEN_API_HOST__,
});

export default httpClientSSR;
