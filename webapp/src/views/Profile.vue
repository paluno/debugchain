<template>
  <div v-if="profile" id="profile">
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
      <div v-if="profile.address === null" class="col-md-9">
        <span>No address has been set.</span>
        <button class="btn btn-outline-secondary btn-sm" @click="showAddressModal = true">Set address</button>
      </div>
      <div v-else class="col-md-9">
        <span>{{profile.address}}</span>
      </div>
    </div>
    <div class="form-group row">
      <div class="col-md-3">Reviewer-State:</div>
      <div class="col-md-9">
        <div class="form-check" v-for="membership in projectMemberships" :key="membership.gitlabId">
          <input class="form-check-input" v-model="checkedMemberships" v-bind:id="membership.gitlabId" v-bind:value="membership.gitlabId" type="checkbox" />
          <label class="form-check-label" v-bind:for="membership.gitlabId">I want to be a reviewer for Project: {{membership.projectname}}</label>
        </div>
        <button class="btn btn-outline-secondary btn-sm" @click="saveMembership">Save Reviewer-State</button>
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
import Gitlab from "@/api/gitlab";
import Navigation from "@/components/Navigation";

export default {
  name: "profile",
  props: {
    projectId: String
  },
  data: function() {
    return {
      profile: null,
      projectMemberships: [],
      checkedMemberships: [],
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
    setProfile: function(profile) {
      this.profile = profile;
    },
    setProjectMemberships: function(projects, memberships) {
      if (memberships.length > 0) {
        this.projectMemberships = projects.map(project => {
          for (let membership in memberships) {
            if (membership.gitlabId == project.id) {
              return {
                gitlabId: project.id,
                projectname: project.name,
                isReviewer: membership.reviewer
              };
            }
          }
          return {
            gitlabId: project.id,
            projectname: project.name,
            isReviewer: false
          };
        });
      } else {
        this.projectMemberships = projects.map(project => {
          return {
            gitlabId: project.id,
            projectname: project.name,
            isReviewer: false
          };
        });
      }
    },
    saveMembership: function() {
      let preparedMemberships = this.projectMemberships.map(membership => {
        for (let i = 0; i < this.checkedMemberships.length; i++) {
          const checked = this.checkedMemberships[i];
          if (membership.gitlabId == checked) {
            return {
              gitlabId: membership.gitlabId,
              reviewer: true
            };
          }
        }
        return {
          gitlabId: membership.gitlabId,
          reviewer: false
        };
      });
    },
    updateData: function() {
      const gitlab = Gitlab.getClient();
      const backend = Backend.getClient();

      this.$emit("isLoading", true);
      // TODO handle / display errors in component
      Promise.all([
        gitlab.projects.list(),
        backend.get("/profile").then(response => {
          return response.data;
        }),
        backend.get("/profile/memberships").then(response => {
          return response.data;
        })
      ]).then(results => {
        const projects = results[0];
        const profile = results[1];
        const memberships = results[2];

        this.setProfile(profile);
        this.setProjectMemberships(projects, memberships);
        this.$emit("isLoading", false);
      });
    }
  }
};
</script>
