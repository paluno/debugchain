<template>
  <div v-if="!session.loggedIn">
    <div class="content">
      <div class="text-center">
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

export default {
  name: "Login",
  data: function() {
    return {
      session: UserSession.state
    };
  },
  beforeRouteEnter: function(to, from, next) {
    var redirect = null;
    if (to.query.hasOwnProperty("redirect")) {
      // get redirect from query
      redirect = to.query.redirect;
    }
    if (!redirect) {
      // get redirect from storage (redirect coming from gitlab)
      redirect = Storage.getLoginRedirect();
      Storage.setLoginRedirect(null);
    }
    if (!redirect) {
      // default: redirect to home
      redirect = "/";
    }

    if (window.location.href.includes("access_token")) {
      // if #access_token exists, set token, then redirect
      GitlabOAuth.token.getToken(window.location.href).then(token => {
        UserSession.login(token.accessToken);
        next(redirect);
      });
    } else if (UserSession.state.loggedIn) {
      // if already logged in, redirect
      next(redirect);
    } else {
      // not logged in and no access_token > no action / show login
      next();
    }
  },
  methods: {
    loginViaGitlab: function(event) {
      Storage.setLoginRedirect(this.$route.query.redirect);
      window.location.href = GitlabOAuth.token.getUri();
    }
  }
};
</script>