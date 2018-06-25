<template>
  <div>
    <Navigation v-if="session.loggedIn"/>
      <div class="row">
        <div class="col text-center">
          <h1>Login</h1>
          <p>
          You are not authenticated. Please login using your Gitlab-Account.
          </p>
          <button class="btn btn-outline-secondary btn-sm" v-on:click="loginViaGitlab">Gitlab Login</button>
        </div>
      </div>
  </div>
</template>


<script>
import { GitlabOAuth, UserSession } from "@/auth";
import Storage from "@/webStorage";
import Navigation from "@/components/Navigation";

export default {
  name: "login",
  components: {
    Navigation
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