module.exports = {
  lintOnSave: false,
  devServer: {
    port: 9000,
    proxy: {
      "/api" : {
        target: "http://localhost:8080"
      }
    }
  },
  configureWebpack: config => {
      // common config mutations
      config.module.rules.push(
          {
              test: /\.abi$/,
              use: 'json-loader'
          },
          {
              test: /\.bin$/,
              use: 'raw-loader'
          }
      );
      if (process.env.NODE_ENV === 'production') {
          // mutate config for production...
      } else {
          // mutate for development...
          config.devtool = 'source-map';
      }
  }
}

