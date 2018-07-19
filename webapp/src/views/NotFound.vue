<template>
  <div class="content">
    <div class="text-center">
      <h1>404 - Page not found</h1>
      <p>The page you requested could not be found.</p>
      <ul class="list-unstyled">
        <li v-if="from">
          <router-link :to="from">Go back to the previous page.</router-link>
        </li>
        <li>
          <router-link to="/">Go back to the home page.</router-link>
        </li>
      </ul>
    </div>
  </div>
</template>

<script>
export default {
  name: "not-found",
  data() {
    return {
      from: null
    };
  },
  beforeRouteEnter(to, from, next) {
    if (from.fullPath == "/") {
      next();
    } else {
      next(vm => (vm.from = from.fullPath));
    }
  }
};
</script>
