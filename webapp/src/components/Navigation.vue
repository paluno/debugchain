<template>
  <div id="nav">
    <template v-if="session.loggedIn">
      <router-link :to="{ name: 'issueList', params: { projectId: projectId }}">
        Issue Overview
      </router-link> |
      <router-link :to="{ name: 'profile', params: { projectId: projectId }}">
        Profile
      </router-link> |
    </template>
    <router-link to="/debug">
      Debug
    </router-link>
  </div>
</template>

<script>
import UserSession from "@/auth.js";

export default {
  name: "navigation",
  props: {
    projectId: {
      type: String,
      // TODO remove debug default
      default: "1"
    }
  },
  data: function() {
    return {
      session: UserSession.state
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
