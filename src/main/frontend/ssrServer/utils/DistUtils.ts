import path from 'path';

const DEFAULT_RELATIVE_DIST_PATH = '../../../dist';
const TEST_RELATIVE_DIST_PATH = '../../../../test/frontend/ssrServer/testDist';

const RELATIVE_DIST_PATH = process.env.NODE_ENV === 'test' ? TEST_RELATIVE_DIST_PATH : DEFAULT_RELATIVE_DIST_PATH;

export const DIST_PATH = path.join(__dirname, RELATIVE_DIST_PATH);
