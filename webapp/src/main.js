import Vue from 'vue'
import App from './App.vue'
import router from './router'
import VueGoodTable from 'vue-good-table'
import 'vue-good-table/dist/vue-good-table.css'

Vue.use(VueGoodTable)

Vue.config.productionTip = false

new Vue({
  router,
  render: h => h(App)
}).$mount('#app')
