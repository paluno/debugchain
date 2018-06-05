<template>
    <div id="nav" v-if="session.loggedIn">
      <router-link to="/">Issue Overview</router-link> |
      <router-link to="/profile">Profile</router-link> |
      <router-link to="/debug">Debug</router-link>
    </div>
</template>

<script>
import { UserSession } from "../auth.js";

export default {
  data: function() {
    return {
      session: UserSession
    };
  }
};
</script>

<style lang="scss" scoped>
#nav {
  padding: 30px;
  a {
    font-weight: bold;
    color: #2c3e50;
    &.router-link-exact-active {
      color: #42b983;
    }
  }
}
</style>
