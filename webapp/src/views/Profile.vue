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
        <button type="button" class="btn btn-secondary" @click="setAddressModal.show = false">Close</button>
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
        <button class="btn btn-outline-secondary btn-sm" @click="setAddressModal.show = true">Set address</button>
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
const client = backend.getClient();

export default {
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
      const self = this;
      // TODO get project id from context
      client
        .post("/projects/1/members", {
          address: newAddress,
          reviewer: false
        })
        .then(function(response) {
          // TODO
          console.log(response);
          self.updateData();
          self.setAddressModal.show = false;
          self.setAddressModal.address = null;
        })
        .catch(function(error) {
          // TODO
          alert(
            "Could not save address: Server returned: " +
              JSON.stringify(error, null, 2)
          );
          console.log(error);
        });
    },
    updateData: function() {
      const self = this;
      client.get("/projects/1/members").then(function(response) {
        self.address = response.data[0].address.value;
      });
    }
  }
};
</script>
