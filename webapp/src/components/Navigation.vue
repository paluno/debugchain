<template>
  <nav id="nav" class="navbar navbar-expand bg-light navbar-light">
    <span class="navbar-brand">Debug-Chain</span>
    <div class="navbar-nav mr-auto" v-if="session.loggedIn">
      <router-link class="nav-item nav-link" :to="{ name: 'projects' }">
        Projects
      </router-link>
      <router-link v-if="projectId" class="nav-item nav-link" :to="{ name: 'issueList', params: { projectId: projectId }}">
        Project #{{projectId}}
      </router-link>
      <router-link v-if="projectId && issueId" class="nav-item nav-link" :to="{ name: 'issue', params: { projectId: projectId, issueId: issueId }}">
        Issue #{{issueId}}
      </router-link>
    </div>
    <div class="navbar-nav">
      <router-link v-if="session.loggedIn" class="nav-item nav-link" :to="{ name: 'profile'}">
        Profile
      </router-link>
      <router-link class="nav-item nav-link" :to="{name: 'debug'}">
        Debug
      </router-link>
    </div>
  </nav>
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
  margin-bottom: 1rem;
}
</style>
