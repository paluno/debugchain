<template>
  <div id="profile">
    <set-address-modal v-model="showAddressModal" v-on:save="addressModalSaveEvent" />

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
        <button class="btn btn-outline-secondary btn-sm" @click="showAddressModal = true">Set address</button>
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
import SetAddressModal from "@/components/modals/SetAddressModal";
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
      showAddressModal: false
    };
  },
  components: {
    Modal,
    SetAddressModal,
    Navigation
  },
  created: function() {
    this.updateData();
  },
  methods: {
    addressModalSaveEvent: function(newAddress) {
      const client = Backend.getClient();

      this.$emit("isLoading", true);
      client
        .post("/profile", {
          address: newAddress
        })
        .then(response => {
          this.updateData();
          this.$emit("isLoading", false);
          this.showAddressModal = false;
        })
        .catch(error => {
          // TODO handle / display errors in component
          this.$emit("isLoading", false);
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
      const backend = Backend.getClient();

      this.$emit("isLoading", true);
      // TODO handle / display errors in component
      backend.get("/profile").then(response => {
        this.$emit("isLoading", false);
        this.address = response.data.address;
      });
    }
  }
};
</script>
