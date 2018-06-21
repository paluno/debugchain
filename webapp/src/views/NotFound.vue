<template>
    <div class="row">
        <div class="col-auto mt-5">
            <h1>404 - Page not found</h1>
            <p>The page you requested could not be found.</p>
            <router-link to="/">Go back to the home page.</router-link>
        </div>
    </div>
</template>

<script>
export default {
  name: "notFound"
};
</script>
