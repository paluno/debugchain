<template>
  <div id="debug">
    <Navigation/>
    <h1>Debug</h1>
    <h2>Create Test / Demo data</h2>
    <div>
      Gitlab Project id: <input v-model="contract.projectId"><br>
      <button @click="createContract">Deploy &amp; Create in Backend</button>
      <button @click="createDemoContract">Create demo contract with sample data</button><br> Contract Address: <input v-model="contract.address">
    </div>
    <div>
      Issue id: <input v-model="contract.issueId"><br>
      <button @click="donate">Donate (0.005 ETH)</button><br>
      <button @click="approve">Approve</button><br>
      <button @click="lock">Lock</button><br>
      <button @click="unlock">Unlock</button><br>
      <button @click="develop">Give to review</button><br>
      <button @click="reviewAccept">Review accept</button><br>
      <button @click="reviewDecline">Review decline</button><br>
      <button @click="resetIssue">Reset Issue</button><br>
      <button @click="deleteIssue">Delete Issue</button><br>
      <button @click="withdraw">Payday</button><br>
    </div>
    <hr>
    <h2>Router</h2>
    <router-link to="/invalid-url">Test invalid router-link url</router-link>
    <h2>Error component</h2>
    <button @click="addError">Add Error</button>
  </div>
</template>

<script>
import Gitlab from "@/api/gitlab";
import Backend from "@/api/backend";
import Contract from "@/api/contract";
import ErrorContainer from "@/api/errorContainer";
import Navigation from "@/components/Navigation";

export default {
  name: "Debug",
  components: {
    Navigation
  },
  data: function() {
    return {
      contract: {
        address: "",
        projectId: null,
        issueId: null
      }
    };
  },
  methods: {
    createDemoContract() {
      if (!this.contract.projectId) {
        alert("Please enter a project id");
        return;
      }

      this.createContract()
        .then(() => (this.contract.issueId = 1))
        .then(() => this.donate())
        .then(() => this.approve())
        .then(() => this.lock())
        .then(() => this.contract.issueId = 2)
        .then(() => this.donate())
        .then(() => this.donate())
        .catch(error => {
          alert("Could not complete demo contract creation.");
          console.log("Demo creation failed:", error);
        });
    },
    addError() {
      ErrorContainer.add("Some Error");
    },
    createContract: function() {
      const contract = new Contract(null, "http://localhost:9545");
      const backend = Backend.getClient();

      return contract
        .deploy(this.contract.projectId)
        .then(address => (this.contract.address = address))
        .then(() => console.log("Deployed contract for project"))
        .then(() => {
          return backend.post("/projects/", {
            address: this.contract.address,
            gitlabId: this.contract.projectId
          });
        })
        .then(() => console.log("Created project in backend"));
    },
    getContract: function() {
      if (!this.contract.address || this.contract.address.length === 0) {
        alert("Please enter a contract address");
        return;
      }
      return new Contract(this.contract.address);
    },
    donate: function() {
      if (!this.contract.issueId) {
        alert("Please enter an issue id");
        return;
      }
      const contract = this.getContract();
      return contract.donate(this.contract.issueId, 0.005).then(() => {
        console.log("0.005 ETH donated to issue " + this.contract.issueId);
      });
    },
    approve: function() {
      if (!this.contract.issueId) {
        alert("Please enter an issue id");
        return;
      }
      const contract = this.getContract();
      // address is the first one from test rpc
      // TODO add input for address
      const reviewers = [contract.web3.eth.accounts[0]];
      return contract.approve(this.contract.issueId, reviewers).then(() => {
        console.log(
          "issue " +
            this.contract.issueId +
            " approved with reviewers: " +
            JSON.stringify(reviewers)
        );
      });
    },
    lock: function() {
      if (!this.contract.issueId) {
        alert("Please enter an issue id");
        return;
      }
      const contract = this.getContract();
      return contract.lock(this.contract.issueId).then(() => {
        console.log("issue " + this.contract.issueId + " locked.");
      });
    },
    unlock: function() {
      if (!this.contract.issueId) {
        alert("Please enter an issue id");
        return;
      }
      const contract = this.getContract();
      return contract.unlock(this.contract.issueId).then(() => {
        console.log("issue " + this.contract.issueId + "unlocked.");
      });
    },
    develop: function() {
      if (!this.contract.issueId) {
        alert("Please enter an issue id");
        return;
      }
      const contract = this.getContract();
      return contract.develop(this.contract.issueId).then(() => {
        console.log(
          "issue " + this.contract.issueId + " development completed"
        );
      });
    },
    reviewAccept: function() {
      this.review(true);
    },
    reviewDecline: function() {
      this.review(false);
    },
    review: function(val) {
      if (!this.contract.issueId) {
        alert("Please enter an issue id");
        return;
      }
      const contract = this.getContract();
      return contract.review(this.contract.issueId, val).then(() => {
        console.log("issue " + this.contract.issueId + " reviewed: " + val);
      });
    },
    withdraw: function() {
      if (!this.contract.issueId) {
        alert("Please enter an issue id");
        return;
      }
      const contract = this.getContract();
      return contract.withdraw().then(() => {
        console.log("withdrawn ether");
      });
    },
    resetIssue: function() {
      if (!this.contract.issueId) {
        alert("Please enter an issue id");
        return;
      }
      const contract = this.getContract();
      return contract.reset(this.contract.issueId).then(() => {
        console.log("Issue reseted");
      });
    },
    delete: function() {
      if (!this.contract.issueId) {
        alert("Please enter an issue id");
        return;
      }
      const contract = this.getContract();
      return contract.delete(this.contract.issueId).then(() => {
        console.log("Issue deleted");
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
