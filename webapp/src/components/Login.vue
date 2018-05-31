<template>
    <div>
        <h1>{{computedToken}}</h1>
        <input type="submit" value="Login" v-on:click="loginViaGitlab"/>
    </div>
</template>


<script>
import router from "../router.js";
import UserSession from "../auth.js";
const ClientOAuth2 = require("client-oauth2");

const GitlabOAuth = new ClientOAuth2({
  clientId: "e17e062d1dbaf7b95df11ac2ea2a2efe7715b1b11efea02793209e9014177d1f",
  clientSecret:
    "79557d718c85353a04e8e78fefc7ba8fedb6040a1cf0627833e768f6eaaa6321",
  accessTokenUri: "http://localhost:80/oauth/token",
  authorizationUri: "http://localhost:80/oauth/authorize",
  redirectUri: "http://localhost:8080/login",
  scopes: ["api"]
});

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
  data: function() {
    return {
      token: null
    };
  },
  created: function() {
    // lifecycle-hook. prüfen, ob uri den token beinhaltet, wir also aus Gitlab zurückkommen
    console.log(window.location.href);
    const that = this;
    if (window.location.href.includes("access_token")) {
      GitlabOAuth.token.getToken(window.location.href).then(function(result) {
        console.log(result);
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
      this.token = newToken;
      console.log(this.token);
      UserSession.token = this.token;
      UserSession.loggedIn = true;
    }
  }
};
</script>