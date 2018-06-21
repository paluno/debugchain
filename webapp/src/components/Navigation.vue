<template>
  <div id="nav">
    <template v-if="session.loggedIn">
      <router-link :to="{ name: 'projects' }">
        Projects
      </router-link> |
      <template v-if="projectId">
        <router-link :to="{ name: 'issueList', params: { projectId: projectId }}">
          Project #{{projectId}}
        </router-link> |
      </template>
      <template v-if="projectId && issueId">
        <router-link :to="{ name: 'issue', params: { projectId: projectId, issueId: issueId }}">
          Issue #{{issueId}}
        </router-link> |
      </template>
      <router-link :to="{ name: 'profile'}">
        Profile
      </router-link> |
    </template>
    <router-link to="/debug">
      Debug
    </router-link>
  </div>
</template>

<script>
// TODO load and display project name and / or issuename in nav items

import UserSession from "@/auth.js";

export default {
  name: "navigation",
  props: {
    projectId: {
      type: String
    },
    issueId: {
      type: String
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
