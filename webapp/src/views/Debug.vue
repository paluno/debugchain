<template>
  <div id="debug">
    <Navigation/>
    <h1>Debug</h1>
    <h2>Create Test / Demo data</h2>
    <div>
      Gitlab Project id: <input v-model="contract.projectId">
      <button @click="createDemoContract">Create demo contract</button><br> Created Contract Address: {{contract.address}}
    </div>
    <hr>
    <h2>UserSession</h2>
    <input type="submit" value="Get current User" v-on:click="getUser" />
    <pre>{{userJson}}</pre>
    <hr>
    <h2>Router</h2>
    <router-link to="/invalid-url">Test invalid router-link url</router-link>
  </div>
</template>

<script>
import Gitlab from "@/api/gitlab";
import Backend from "@/api/backend";
import Contract from "@/api/contract";
import Navigation from "@/components/Navigation";

export default {
  name: "Debug",
  components: {
    Navigation
  },
  data: function() {
    return {
      userJson: "",
      contract: {
        address: "",
        projectId: 0
      }
    };
  },
  methods: {
    createDemoContract() {
      const contract = new Contract(null, "http://localhost:9545");
      const backend = Backend.getClient();

      if (!this.contract.projectId) {
        alert("Please enter a project id");
        return;
      }

      contract
        .deploy(this.contract.projectId)
        .then(address => (this.contract.address = address))
        .then(() => console.log("Deployed contract for project"))
        .then(() =>
          backend.post("/projects/", {
            address: this.contract.address,
            gitlabId: this.contract.projectId
          })
        )
        .then(() => console.log("Created project in backend"))
        // TODO create more test data: donate issues, etc
        .catch(error => {
          alert("Could not complete demo contract creation.");
          console.log("Demo creation failed:", error);
        });
    },
    getUser: function(event) {
      const client = Gitlab.getClient();
      client.users.current().then(result => {
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
