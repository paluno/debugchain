<template>
  <div class="hello">
    <h1>{{ msg }}</h1>
    <input type="submit" value="GET CURRENT USER" v-on:click="getUser"/>
    <p>
      {{json}}
    </p>
  </div>
</template>

<script>
import { UserSession } from "../auth.js";
const GitlabApis = require("gitlab-api-wrapper");

export default {
  name: "HelloWorld",
  props: {
    msg: String
  },
  data: function() {
    return {
      json: ""
    };
  },
  methods: {
    getUser: function(event) {
      const client = GitlabApis({
        // the GitLab url
        base_url: "http://localhost",
        private_token: UserSession.token.accessToken,
        timeout: 3000
      });
      const that = this;
      client.users.current().then(function(result) {
        that.setJson(JSON.stringify(result));
      });
    },
    setJson: function(newJson) {
      this.json = newJson;
    }
  }
};
</script>