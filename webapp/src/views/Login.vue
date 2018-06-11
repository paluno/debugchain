<template>
  <div>
    <h1>{{ computedToken }}</h1>
    <input type="submit" value="Login" v-on:click="loginViaGitlab" />
  </div>
</template>


<script>
import { GitlabOAuth, UserSession } from "../auth";
import Storage from "../webStorage";

export default {
  // We use computed properties based on 'props' to avoid any complex logic in our template
  computed: {
    computedToken: function() {
      if (
        this.session.token != null &&
        this.session.token.accessToken != null
      ) {
        return "Token: " + this.session.token.accessToken;
      } else {
        return "No token";
      }
    }
  },
  data: function() {
    return {
      session: UserSession
    };
  },
  created: function() {
    if (UserSession.loggedIn) {
      // User is already authenticated. Perform redirect, if requested
      this.performRedirect();
    } else {
      // if #access_token exists, set token, then redirect, if requested
      const that = this;
      if (window.location.href.includes("access_token")) {
        GitlabOAuth.token.getToken(window.location.href).then(function(token) {
          that.login(token);
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
    login: function(newToken) {
      UserSession.token = newToken;
      UserSession.loggedIn = true;
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