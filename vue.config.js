/* eslint-disable prettier/prettier */

require('dotenv').config({ path: './src/main/frontend/.env' });

const { WebpackManifestPlugin } = require('webpack-manifest-plugin');
const nodeExternals = require('webpack-node-externals');

const isSSR = process.env.SSR;
const findenApiHost = process.env.VUE_APP_API_HOST || undefined;

const isDevelopmentMode = process.env.NODE_ENV === 'development';

exports.outputDir = isSSR ? `./dist/${process.env.APP}/ssr` : `./dist/${process.env.APP}/client`;

exports.pluginOptions = {
  webpackBundleAnalyzer: {
    analyzerMode: 'static',
    openAnalyzer: process.env.OPEN_ANALYZER === 'true',
  },
};

exports.chainWebpack = (webpackConfig) => {
  webpackConfig.plugin('html').tap((args) => {
    args[0].template = isDevelopmentMode ? './src/main/frontend/devPublic/index.html' : args[0].template;
    return args;
  });
  webpackConfig.module.rule('vue').uses.delete('cache-loader');
  webpackConfig.module.rule('js').uses.delete('cache-loader');
  webpackConfig.module.rule('ts').uses.delete('cache-loader');
  webpackConfig.module.rule('tsx').uses.delete('cache-loader');

  webpackConfig.plugin('define').tap((args) => {
    const devFindenApiHost = isDevelopmentMode ? process.env.VUE_APP_API_HOST : '';
    args[0] = {
      ...args[0],
      __FINDEN_API_HOST__: isSSR ? JSON.stringify(findenApiHost) : JSON.stringify(devFindenApiHost),
    };

    return args;
  });

  webpackConfig.module
    .rule('vue')
    .use('vue-loader')
    .tap((options) => {
      options.compilerOptions = {
        ...(options.compilerOptions || {}),
        comments: true,
        isCustomElement: (tag) => tag.startsWith('b2k-'),
      };
      return options;
    });

  if (!process.env.SSR) {
    webpackConfig.entry('app').clear().add(`./src/main/frontend/${process.env.APP}.client.ts`);

    webpackConfig.plugin('manifest').use(new WebpackManifestPlugin({ fileName: 'client-manifest.json' }));

    webpackConfig.resolve.mainFields.clear().add('main').add('module');
  } else {
    webpackConfig.entry('app').clear().add(`./src/main/frontend/${process.env.APP}.server.ts`);

    webpackConfig.target('node');
    webpackConfig.output.libraryTarget('commonjs2');

    webpackConfig.plugin('manifest').use(new WebpackManifestPlugin({ fileName: 'ssr-manifest.json' }));

    webpackConfig.externals(nodeExternals({ allowlist: /\.(css|vue)$/ }));

    webpackConfig.optimization.splitChunks(false).minimize(false);

    webpackConfig.plugins.delete('hmr');
    webpackConfig.plugins.delete('preload');
    webpackConfig.plugins.delete('prefetch');
    webpackConfig.plugins.delete('progress');
  }
};

// Enable the scss config from the design system
exports.css = {
  extract: true,
  loaderOptions: {
    scss: {
      additionalData: `
          @import '~@blume2000/design-system/dist/scss/globals.scss';
        `,
    },
  },
};
