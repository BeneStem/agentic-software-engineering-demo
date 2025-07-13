module.exports = {
  projects: ['jest.client.config.js', 'jest.server.config.js'],
  collectCoverageFrom: [
    '**/src/**/*.{js,ts,vue}',
    '!**/*.d.ts',
    '!**/src/test/**/*.{js,ts,vue}',
    '!.eslintrc.js',
    '!*.config.js',
    '!**/build/**',
    '!**/coverage/**',
    '!**/node_modules/**',
    '!**/dist/**',
  ],
};
