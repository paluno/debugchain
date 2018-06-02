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
        data: function () {
    return {
      token: null
    };
  },
        created: function () {
    // lifecycle-hook. prüfen, ob uri den token beinhaltet, wir also aus Gitlab zurückkommen
            console.log(window.location.href);
            const that = this;
    if (window.location.href.includes("access_token")) {
        GitlabOAuth.token.getToken(window.location.href).then(function (result) {
            console.log(result);
            that.setToken(result);
      });
    }
  },
  methods: {
      loginViaGitlab: function (event) {
          console.log("Login-Button geklickt. Leite um auf GitLab");
      window.location.href = GitlabOAuth.token.getUri();
    },
      setToken: function (newToken) {
      this.token = newToken;
          console.log(this.token);
      UserSession.token = this.token;
      UserSession.loggedIn = true;
    }
  }
};
</script>