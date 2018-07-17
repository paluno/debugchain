<template>
  <div id="app">
    <div class="error-toast-container">
      <error-toast v-for="error in errors" :key="error.id" :error="error.raw" class="error-toast"></error-toast>
    </div>
    <LoadingOverlay v-if="loading" />
    <router-view @isLoading="onIsLoadingChanged" />
  </div>
</template>
<style>
.content {
  padding: 0;
  margin: 25px 25px 25px 25px;
}
</style>

<script>
import "bootstrap/dist/css/bootstrap.css";
import ErrorContainer from "@/api/errorContainer";
import LoadingOverlay from "@/components/LoadingOverlay";
import ErrorToast from "@/components/ErrorToast";

export default {
  components: {
    LoadingOverlay,
    ErrorToast
  },
  data: function() {
    return {
      loading: false,
      errors: ErrorContainer.errors
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

.error-toast-container {
  position: fixed;
  z-index: 9998;
  top: 1rem;
  right: 1rem;

  .error-toast {
    position: static;
  }
}
</style>
