<template>
    <div>
        <h1>{{ computedToken }}</h1>
        <input type="submit" value="Login" v-on:click="loginViaGitlab"/>
    </div>
</template>


<script>
import { GitlabOAuth, UserSession } from "../auth.js";

export default {
  // We use computed properties based on 'props' to avoid any complex logic in our template
  computed: {
    computedToken: function() {
      if (this.session.token != null && this.session.token.accessToken != null) {
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
    // lifecycle-hook. prüfen, ob uri den token beinhaltet, wir also aus Gitlab zurückkommen
    const that = this;
    if (window.location.href.includes("access_token")) {
      GitlabOAuth.token.getToken(window.location.href).then(function(result) {
        that.setToken(result);
      });
    }
  },
  methods: {
    loginViaGitlab: function(event) {
      console.log("Login-Button geklickt. Leite um auf GitLab");
      window.location.href = GitlabOAuth.token.getUri();
    },
    setToken: function(newToken) {
      UserSession.token = newToken;
      UserSession.loggedIn = true;
    }
  }
};
</script>