<template>
    <div id="debug">
        <Navigation/>
        <h1>Debug</h1>
        <input type="submit" value="Get current User" v-on:click="getUser"/>
        <pre>{{userJson}}</pre>
    </div>
</template>

<script>
import gitlab from "@/api/gitlab";
import Navigation from "@/components/Navigation";

export default {
  name: "debug",
  components: {
    Navigation
  },
  data: function() {
    return {
      userJson: ""
    };
  },
  methods: {
    getUser: function(event) {
      const client = gitlab.getClient();
      const that = this;
      client.users.current().then(function(result) {
        that.userJson = JSON.stringify(result, null, 2);
      });
    }
  }
};
</script>

<style lang="scss" scoped>
#debug {
  pre {
    text-align: start;
  }
}
</style>
