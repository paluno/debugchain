<template>
  <div id="profile">
    <Modal v-model="setAddressModal.show" title="Set Ethereum address">
      <p>
        To execute some actions in this application, we need the public address of your Ethereum wallet. Please enter it below.<br />
      </p>
      <div class="alert alert-warning">
        This address will be used for your interactions with the Ethereum Smart Contract and can not be changed afterwards.
      </div>
      <!-- TODO validate Ethereum address -->
      <input class="form-control" type="text" placeholder="Enter your Ethereum address..." v-model="setAddressModal.address" />
      <template slot="footer">
        <button type="button" class="btn btn-primary" @click="setAddressModalSave(setAddressModal.address)">Save</button>
        <button type="button" class="btn btn-secondary" @click="closeSetAddressModal()">Close</button>
      </template>
    </Modal>

    <Navigation v-bind:projectId="projectId" />
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
import Modal from "@/components/Modal";
import Backend from "@/api/backend";
import Navigation from "@/components/Navigation";

export default {
  name: "profile",
  props: {
    projectId: String
  },
  data: function() {
    return {
      address: undefined,
      setAddressModal: {
        show: false,
        address: null
      }
    };
  },
  components: {
    Modal,
    Navigation
  },
  created: function() {
    this.updateData();
  },
  methods: {
    setAddressModalSave: function(newAddress) {
      const client = Backend.getClient();
      
      client
        .post("/profile", {
          address: newAddress
        })
        .then(response => {
          this.updateData();
          this.closeSetAddressModal();
        })
        .catch(error => {
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
      const client = Backend.getClient();

      // TODO handle / display errors in component
      client.get("/profile").then(response => {
        this.address = response.data.address;
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
