<template>
  <nav id="nav" class="navbar navbar-expand bg-light navbar-light">
    <span class="navbar-brand">Debug-Chain</span>
    <div class="navbar-nav mr-auto" v-if="session.loggedIn">
      <router-link class="nav-item nav-link" :to="{ name: 'projects' }">
        Projects
      </router-link>
      <router-link v-if="project" class="nav-item nav-link" :to="{ name: 'issueList', params: { projectId: project.id }}">
        {{project.name}}
      </router-link>
      <router-link v-if="project && issue" class="nav-item nav-link" :to="{ name: 'issue', params: { projectId: project.id, issueId: issue.id }}">
        {{issue.title}}
      </router-link>
    </div>
    <div class="navbar-nav">
      <span v-if="session.loggedIn" class="navbar-text">
        <small v-if="showAvailableEther">You have {{profile.pendingWithdrawals | weiToEther}} ETH available.</small>
        <small v-if="showSetAddressHint">Set your wallet address to access more actions.</small>
      </span>
      <router-link v-if="session.loggedIn" class="nav-item nav-link" :to="{ name: 'profile'}">
        Profile
      </router-link>
      <router-link class="nav-item nav-link" :to="{name: 'faq'}">
        FAQ
      </router-link>
      <a v-if="session.loggedIn" v-on:click="logout" href="/" class="nav-item nav-link">Logout</a>
    </div>
  </nav>
</template>

<script>
// TODO load and display project name and / or issuename in nav items

import UserSession from "@/auth.js";
import getWeb3 from "@/api/getWeb3";

export default {
  name: "Navigation",
  filters: {
    weiToEther: function(value) {
      return getWeb3().fromWei(value, "ether");
    }
  },
  props: {
    project: {
      type: Object
    },
    issue: {
      type: Object
    },
    profile: {
      type: Object
    }
  },
  data: function() {
    return {
      session: UserSession.state
    };
  },
  computed: {
    showSetAddressHint() {
      // profile is loaded but has no address set
      return this.profile && this.profile.address === null;
    },
    showAvailableEther() {
      // profile is loaded and has ether available to withdraw
      return (
        this.profile &&
        typeof this.profile.pendingWithdrawals === "number" &&
        this.profile.pendingWithdrawals > 0
      );
    }
  },
  methods: {
    logout: function() {
      UserSession.logout();
    }
  }
};
</script>

<style lang="scss" scoped>
#nav {
  margin-bottom: 1rem;
}
</style>
