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

    if (window.location.href.includes("access_token")) {
      // if #access_token exists, set token, then redirect, if requested
      GitlabOAuth.token.getToken(window.location.href).then(token => {
        UserSession.login(token.accessToken);
        if (redirect) {
          next(redirect);
        } else {
          next();
        }
      });
    } else if (UserSession.state.loggedIn) {
      // if already logged in, redirect, if requested
      if (redirect) {
        next(redirect);
      } else {
        next();
      }
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