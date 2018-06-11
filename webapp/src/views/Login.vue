<template>
  <div>
    <Navigation v-if="session.loggedIn"/>
    <h1>{{ computedToken }}</h1>
    <input type="submit" value="Login" v-on:click="loginViaGitlab" />
  </div>
</template>


<script>
import { GitlabOAuth, UserSession } from "../auth";
import Storage from "../webStorage";
import Navigation from "@/components/Navigation";

export default {
  components: {
    Navigation
  },
  computed: {
    computedToken: function() {
      if (this.session.accessToken != null) {
        return "Token: " + this.session.accessToken;
      } else {
        return "No token";
      }
    }
  },
  data: function() {
    return {
      session: UserSession.state
    };
  },
  created: function() {
    if (this.session.loggedIn) {
      // User is already authenticated. Perform redirect, if requested
      this.performRedirect();
    } else {
      // if #access_token exists, set token, then redirect, if requested
      const that = this;
      if (window.location.href.includes("access_token")) {
        GitlabOAuth.token.getToken(window.location.href).then(function(token) {
          UserSession.login(token.accessToken);
          that.performRedirect();
        });
      }
    }
  },
  methods: {
    loginViaGitlab: function(event) {
      Storage.setLoginRedirect(this.$route.query.redirect);
      window.location.href = GitlabOAuth.token.getUri();
    },
    performRedirect: function() {
      if (this.$route.query.hasOwnProperty("redirect")) {
        this.$router.push(this.$router.query.redirect);
        return;
      }
      const redirect = Storage.getLoginRedirect();
      if (redirect != null) {
        this.$router.push(redirect);
        
        Storage.setLoginRedirect(null);

      }
    }
  }
};
</script>