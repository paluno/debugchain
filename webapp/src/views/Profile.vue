<template>
  <div id="profile">
    <Modal v-model="setAddressModal.show" title="Set Ethereum address">
      <p>
        To execute some actions in this application, we need the public address of your Ethereum wallet. Please enter it below.<br />
      </p>
      <div class="alert alert-warning">
        This address will be used for your interactions with the Ethereum Smart Contract and can not be changed afterwards.
      </div>
      <input class="form-control" type="text" placeholder="Enter your Ethereum address..." v-model="setAddressModal.address" />
      <template slot="footer">
        <button type="button" class="btn btn-primary" @click="setAddressModalSave(setAddressModal.address)">Save</button>
        <button type="button" class="btn btn-secondary" @click="closeSetAddressModal()">Close</button>
      </template>
    </Modal>

    <h1>Profile</h1>
    <div class="form-group row">
      <label class="col-md-3" for="username">Username:</label>
      <div class="col-md-9">
        <span class="">[Your gitlab username]</span>
      </div>
    </div>
    <div class="form-group row">
      <label class="col-md-3" for="address">Ethereum Address:</label>
      <div v-if="address === null" class="col-md-9">
        <span>No address has been set.</span>
        <button class="btn btn-outline-secondary btn-sm" @click="showSetAddressModal()">Set address</button>
      </div>
      <div v-else class="col-md-9">
        <span>{{address}}</span>
      </div>
    </div>
    <div class="form-group row">
      <div class="col-md-3">Reviewer:</div>
      <div class="col-md-9">
        <div class="form-check">
          <input class="form-check-input" id="isReviewer" type="checkbox" />
          <label class="form-check-label" for="isReviewer">You are not available as reviewer.</label>
        </div>
      </div>
    </div>
    <hr class="my-4">
    <h2>Assigned issues:</h2>
    <table class="table">
      <thead>
        <tr>
          <th>ID</th>
          <th>Issue</th>
          <th>Role</th>
        </tr>
      </thead>
      <tbody>
        <tr>
          <td>1</td>
          <td>Fix pagination</td>
          <td>As Developer</td>
        </tr>
        <tr>
          <td>2</td>
          <td>Dummy data</td>
          <td>As Reviewer</td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
import Modal from "@/components/Modal.vue";
import backend from "../api/backend";

export default {
  props: {
    projectId: Number
  },
  data: function() {
    return {
      address: null,
      setAddressModal: {
        show: false,
        address: null
      }
    };
  },
  components: {
    Modal
  },
  created: function() {
    this.updateData();
  },
  methods: {
    setAddressModalSave: function(newAddress) {
      const client = backend.getClient();
      const self = this;
      // TODO get project id from context
      client
        .post("/projects/1/members", {
          address: newAddress,
          reviewer: false
        })
        .then(function(response) {
          self.updateData();
        })
        .catch(function(error) {
          // TODO handle / display errors in component
          const msg = "Could not save address.\n";
          if (error.response) {
            alert(
              "Could not save address.\nServer returned error: " +
                error.response.status +
                error.response.statusText +
                "\n" +
                JSON.stringify(error.response.data, null, 2)
            );
          } else {
            console.log(
              "Could not save address.\nUnknown error: " + error.message
            );
          }
          console.log(error);
        });
    },
    updateData: function() {
      const client = backend.getClient();
      const self = this;
      // TODO handle / display errors in component
      // TODO get project id from context
      client.get("/projects/1/members").then(function(response) {
        self.address = response.data.find(element => {
          // TODO get user id from context
          return element.gitlabId == 1;
        }).address.value;
      });
    },
    showSetAddressModal: function() {
      this.setAddressModal.show = true;
      this.setAddressModal.address = null;
    },
    closeSetAddressModal: function() {
      this.setAddressModal.show = false;
      this.setAddressModal.address = null;
    }
  }
};
</script>
