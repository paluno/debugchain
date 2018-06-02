<template>
    <div>
        <h1>{{computedToken}}</h1>
        <input type="submit" value="Login" v-on:click="loginViaGitlab"/>
    </div>
</template>


<script>
    import {GitlabOAuth, UserSession} from "../auth.js";

    export default {
  // We use computed properties based on 'props' to avoid any complex logic in our template
  computed: {
    computedToken() {
      if (this.token != null && this.token.accessToken != null) {
        return "Token: " + this.token.accessToken;
      } else {
        return "No token";
      }
    }
  },
        data: () => {
    return {
      token: null
    };
  },
        created: () => {
    // lifecycle-hook. prüfen, ob uri den token beinhaltet, wir also aus Gitlab zurückkommen
    if (window.location.href.includes("access_token")) {
        GitlabOAuth.token.getToken(window.location.href).then((result) => {
            this.setToken(result);
      });
    }
  },
  methods: {
      loginViaGitlab: (event) => {
      window.location.href = GitlabOAuth.token.getUri();
    },
      setToken: (newToken) => {
      this.token = newToken;
      UserSession.token = this.token;
      UserSession.loggedIn = true;
    }
  }
};
</script>