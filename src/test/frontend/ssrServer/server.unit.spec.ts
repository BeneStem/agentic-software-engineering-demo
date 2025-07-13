import { createServer } from '@/main/frontend/ssrServer/serverApp';
import axios from 'axios';
import { FastifyInstance } from 'fastify';
import { AppAsset, AppManifest } from '@/main/frontend/ssrServer/types';
import path from 'path';
import { testProduktDto } from '@/test/frontend/utils/TestProduktHelper';

const produktlisteDistPath = path.join(__dirname, `./testDist/produktliste`);
const produktlisteClientManifest: AppManifest = require(path.join(
  produktlisteDistPath,
  '/client/client-manifest.json'
));

jest.mock('axios');
const mockedAxios = axios as jest.Mocked<typeof axios>;

describe('fastify server tests', () => {
  let fastify: FastifyInstance;
  beforeAll(async () => {
    fastify = await createServer();
  });

  it('health endpoint returns 200', async () => {
    const res = await fastify.inject({
      method: 'GET',
      url: '/health',
    });
    expect(res.statusCode).toEqual(200);
  });

  it.each<[string, AppAsset, AppManifest]>([
    ['produktliste', 'app.js', produktlisteClientManifest],
    ['produktliste', 'app.css', produktlisteClientManifest],
    ['produktliste', 'chunk-vendors.js', produktlisteClientManifest],
    ['produktliste', 'chunk-vendors.css', produktlisteClientManifest],
  ])('static endpoint returns 200 for %s/%s', async (app, asset, manifest) => {
    const res = await fastify.inject({
      method: 'GET',
      url: `/finden/static/${app}/client${manifest[asset]}`,
    });
    expect(res.statusCode).toEqual(200);
  });

  it.each([
    ['styles', '/styles'],
    ['content', ''],
    ['scripts', '/scripts'],
  ])("produktliste ssi endpoint '%s' returns 200", async (_, path) => {
    const res = await fastify.inject({
      method: 'GET',
      url: `/ssi/finden/produktliste${path}?produktlistenname=produktlistenname&produktlisten_id=1`,
    });
    mockedAxios.get.mockResolvedValueOnce({ status: 200, data: [testProduktDto] });
    expect(res.statusCode).toEqual(200);
  });

  it('health endpoint has no cache-control', async () => {
    const res = await fastify.inject({
      method: 'GET',
      url: '/health',
    });
    expect(res.headers['cache-control']).toBeUndefined();
  });

  it.each<[string, AppAsset]>([
    ['produktliste', 'app.js'],
    ['produktliste', 'app.css'],
    ['produktliste', 'chunk-vendors.js'],
    ['produktliste', 'chunk-vendors.css'],
  ])('static endpoint %s/%s has correct cache-control', async (app, asset) => {
    const res = await fastify.inject({
      method: 'GET',
      url: `/finden/static/${app}/client${produktlisteClientManifest[asset]}`,
    });
    expect(res.headers['cache-control']).toEqual('public, max-age=946080000');
  });

  it.each([
    ['styles', '/styles'],
    ['content', ''],
    ['scripts', '/scripts'],
  ])('produktliste ssi endpoint %s has correct cache-control', async (_, path) => {
    const res = await fastify.inject({
      method: 'GET',
      url: `/ssi/finden/produktliste${path}?produktlistenname=produktlistenname&produktlisten_id=1`,
    });
    mockedAxios.get.mockResolvedValueOnce({ status: 200, data: [testProduktDto] });
    expect(res.headers['cache-control']).toEqual('public, max-age=10');
  });

  it('404 response does not have a cache header set', async () => {
    const res = await fastify.inject({
      method: 'GET',
      url: `finden/static/unknown/endpoint`,
    });
    expect(res.statusCode).toEqual(404);
    expect(res.headers['cache-control']).toBeUndefined();
  });
});
