<template>
<div id="profile">
  <Navigation :address="profile.address" />
  <div class="content" v-if="profile">
    <set-address-modal v-model="showAddressModal" v-on:save="addressModalSaveEvent" />

    <h1>Profile</h1>
    <div class="form-group row">
      <label class="col-md-3" for="username">Username:</label>
      <div class="col-md-9">
        <span class="">{{gitlabUsername}}</span>
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
    <div v-if="profile.address" class="form-group row">
      <div class="col-md-3">Reviewer-State:</div>
      <div class="col-md-9">
        <div class="form-check" v-for="membership in changedMemberships" :key="membership.projectGitlabId">
          <input class="form-check-input" v-model="membership.isReviewer" v-bind:id="membership.projectGitlabId" type="checkbox" />
          <label class="form-check-label" v-bind:for="membership.projectGitlabId">I want to be a reviewer for Project: {{membership.projectGitlabId}}</label>
        </div>
        <button v-if="isMembershipsChanged" class="btn btn-outline-secondary btn-sm" @click="saveMembership">Save Reviewer-State</button>
      </div>
    </div>
    <div v-else class="form-group row">
      <div class="col-md-3">Reviewer-State:</div>
      <div class="col-md-9">
        <p>You need to add an ethereum-address in order to manage your reviewer-settings!</p>
      </div>
    </div>
    <hr class="my-4">
    <h2>Assigned issues:</h2>
    <vue-good-table :columns="columns" :rows="assignedIssuesRows" styleClass="vgt-table striped bordered">
    </vue-good-table>
  </div>
  </div>
</template>

<script>
import ErrorContainer from "@/api/errorContainer";
import Modal from "@/components/Modal";
import SetAddressModal from "@/components/modals/SetAddressModal";
import { Backend } from "@/api/backend";
import { Gitlab } from "@/api/gitlab";
import Navigation from "@/components/Navigation";
import getWeb3 from "@/api/getWeb3";

export default {
  name: "Profile",
  components: {
    Modal,
    SetAddressModal,
    Navigation
  },
  data: function() {
    return {
      profile: null,
      gitlabUsername: null,
      projectMemberships: [],
      showAddressModal: false,
      assignedIssues: [],
      changedMemberships: [],
      projects: [],
      memberships: [],
      columns: [
        {
          label: "Id",
          field: "id",
          type: "number"
        },
        {
          label: "Status",
          field: "status"
        },
        {
          label: "Donation (ETH)",
          field: "eth",
          type: "number"
        },
        {
          label: "Assigned as",
          field: "assignedAs"
        }
      ]
    };
  },
  computed: {
    assignedIssuesRows() {
      if (!this.profile.address) {
        return [];
      }
      return this.assignedIssues.map(issue => {
        const asDeveloper = issue.developer === this.profile.address;
        return {
          id: issue.id,
          status: issue.lifecycleStatus,
          eth: getWeb3().fromWei(issue.donationSum, "ether"),
          assignedAs: asDeveloper ? "Developer" : "Reviewer"
        };
      });
    },
    isMembershipsChanged() {
      return this.combinedMemberships.some(original =>
        this.changedMemberships.some(
          changed =>
            original.projectGitlabId === changed.projectGitlabId &&
            original.isReviewer !== changed.isReviewer
        )
      );
    },
    combinedMemberships() {
      return this.projects.map(project => {
        const member = this.memberships.find(
          m => m.projectGitlabId == project.gitlabId
        );

        return {
          projectGitlabId: project.gitlabId,
          userGitlabId: this.profile.gitlabId,
          projectaddress: project.address,
          isReviewer: member ? member.reviewer : false
        };
      });
    }
  },
  watch: {
    combinedMemberships() {
      // if memberships is reloaded: update modifiable data
      this.changedMemberships = JSON.parse(
        JSON.stringify(this.combinedMemberships)
      );
    }
  },
  created: function() {
    this.updateData();
  },
  methods: {
    addressModalSaveEvent: function(newAddress) {
      const backend = new Backend();

      this.$emit("isLoading", true);
      backend
        .setProfile({
          address: newAddress
        })
        .then(() => this.updateData())
        .catch(error => ErrorContainer.add(error))
        .then(() => this.$emit("isLoading", false))
        .then(() => (this.showAddressModal = false));
    },
    saveMembership: function() {
      const backend = new Backend();
      let preparedMemberships = this.changedMemberships.map(membership => {
        return {
          projectGitlabId: membership.projectGitlabId,
          userGitlabId: membership.userGitlabId,
          reviewer: membership.isReviewer
        };
      });

      this.$emit("isLoading", true);
      backend
        .setProfileMemberships(preparedMemberships)
        .then(() => this.updateData())
        .catch(error => ErrorContainer.add(error))
        .then(() => this.$emit("isLoading", false));
    },
    setProjectMemberships: function(projects, memberships) {
      this.projects = projects;
      this.memberships = memberships;
    },
    setProfile: function(profile) {
      this.profile = profile;
    },
    setAssignedIssues(issues) {
      this.assignedIssues = issues;
    },
    updateData: function() {
      const backend = new Backend();
      const gitlab = new Gitlab();

      this.$emit("isLoading", true);
      // TODO handle / display errors in component
      Promise.all([
        backend.getProjects(),
        backend.getProfile(),
        backend.getProfileMemberships(),
        backend.getProfileAssignedIssues(),
        gitlab.getCurrentUser()
      ])
        .then(results => {
          const projects = results[0];
          const profile = results[1];
          const memberships = results[2];
          const assignedIssues = results[3];
          const user = results[4];

          this.setProfile(profile);
          this.setProjectMemberships(projects, memberships);
          this.setAssignedIssues(assignedIssues);

          this.gitlabUsername = user.username;
        })
        .catch(error => ErrorContainer.add(error))
        .then(() => this.$emit("isLoading", false));
    }
  }
};
</script>
