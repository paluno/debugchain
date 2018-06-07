module.exports = {
  lintOnSave: false,
  configureWebpack: config => {
    if (process.env.NODE_ENV === 'production') {
      // mutate config for production...
    } else {
      // mutate for development...
      config.devtool = 'source-map';
    }
  }
}
