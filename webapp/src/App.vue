<template>
  <div id="app" class="container">
    <LoadingOverlay v-if="loading"/>
    <router-view @isLoading="onIsLoadingChanged"/>
  </div>
</template>

<script>
import "bootstrap/dist/css/bootstrap.css";
import LoadingOverlay from "@/components/LoadingOverlay";

export default {
  components: {
    LoadingOverlay
  },
  data: function() {
    return {
        loading: false
    };
  },
  methods: {
    onIsLoadingChanged(isLoading) {
      this.loading = isLoading;
    }
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
