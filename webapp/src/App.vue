<template>
  <div id="app" class="container">
      <Navigation/>
    <router-view/>
  </div>
</template>

<script>

import 'bootstrap/dist/css/bootstrap.css'

import Navigation from "@/components/Navigation.vue";

export default {
  components: {
    Navigation
  }
};
</script>

<style lang="scss">
#app {
  font-family: "Avenir", Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2c3e50;
}
</style>
