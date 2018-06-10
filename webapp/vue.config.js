module.exports = {
  lintOnSave: false,
  devServer: {
    proxy: {
      "/api" : {
        target: "http://localhost:8081",
        pathRewrite: {"^/api" : ""}
      }
    }
  },
  configureWebpack: config => {
    if (process.env.NODE_ENV === 'production') {
      // mutate config for production...
    } else {
      // mutate for development...
      config.devtool = 'source-map';
    }
  }
}
