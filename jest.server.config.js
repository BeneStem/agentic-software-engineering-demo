module.exports = {
  preset: 'ts-jest',
  testMatch: ['**/ssrServer/**/*.unit.spec.ts'],
  testEnvironment: 'node',
  transform: {
    '^.+\\js$': 'babel-jest',
    '^.+\\.ts?$': 'ts-jest',
  },
  moduleNameMapper: {
    '^@/(.*)': '<rootDir>/src/$1',
  },
  moduleFileExtensions: ['js', 'json', 'ts'],
};
