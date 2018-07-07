<template>
  <div v-if="profile" id="profile">
    <set-address-modal v-model="showAddressModal" v-on:save="addressModalSaveEvent" />

    <Navigation :address="profile.address" />
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
        <div class="form-check" v-for="membership in projectMemberships" :key="membership.projectGitlabId">
          <input class="form-check-input" v-model="membership.isReviewer" v-bind:id="membership.projectGitlabId" type="checkbox" />
          <label class="form-check-label" v-bind:for="membership.projectGitlabId">I want to be a reviewer for Project: {{membership.projectGitlabId}}</label>
        </div>
        <button class="btn btn-outline-secondary btn-sm" @click="saveMembership">Save Reviewer-State</button>
      </div>
    </div>
    <div v-else class="form-group row">
      <div class="col-md-3">Reviewer-State:</div>
      <div class="col-md-9">
        <p>You need to add a ethereum-address in order to manage your reviewer-settings!</p>
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
  data: function() {
    return {
      profile: null,
      gitlabUsername: null,
      projectMemberships: [],
      assignedIssuesAsDev: [],
      assignedIssuesAsReviewer: [],
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
      //TODO: get project names from gitlab call to display behind checkboxes instead of just ID
      if (memberships.length > 0) {
        this.projectMemberships = projects.map(project => {
          for (let i = 0; i < memberships.length; i++) {
            const membership = memberships[i];
            if (membership.projectGitlabId == project.gitlabId) {
              return {
                projectGitlabId: project.gitlabId,
                userGitlabId: membership.userGitlabId,
                projectaddress: project.address,
                isReviewer: membership.reviewer
              };
            }
          }
          return {
            projectGitlabId: project.gitlabId,
            userGitlabId: this.profile.gitlabId,
            projectaddress: project.address,
            isReviewer: false
          };
        });
      } else {
        this.projectMemberships = projects.map(project => {
          return {
            projectGitlabId: project.gitlabId,
            userGitlabId: this.profile.gitlabId,
            projectaddress: project.address,
            isReviewer: false
          };
        });
      }
    },
    saveMembership: function() {
      let preparedMemberships = this.projectMemberships.map(membership => {
        return {
          projectGitlabId: membership.projectGitlabId,
          userGitlabId: membership.userGitlabId,
          reviewer: membership.isReviewer
        };
      });

      const backend = Backend.getClient();
      this.$emit("isLoading", true);
      backend.post("/profile/memberships", preparedMemberships).then(result => {
        this.$emit("isLoading", false);
        this.updateData();
      });
    },
    getGitlabUsername: function(id) {
      const gitlab = Gitlab.getClient();
      return new Promise((resolve, reject) => {
        gitlab.get("/users/" + id)
          .then(results => {
            resolve(results.username);
          })
          .catch(err => {
            reject(err);
          })
      })
    },
    getAllIssuesOfProjects: function(projects) {
      const backend = Backend.getClient();
      const results = [];
      return new Promise((resolve, reject) => {
        for (let i = 0; i < projects.length; i++) {
          backend.get("/projects/" + projects[i].gitlabId + "/issues")
            .then(issues => {results.push(issues)})
            .catch(err => {console.log("/issues call to project failed")});
        }
        if(results.length > 0){
          resolve(results);
        }
        else{
          reject("No issues existent!");
        }
      })
    },
    filterAssignedIssues: function(issues) {
      issues.array.forEach(element => {
        if (element.developer == this.profile.address) {
          assignedIssuesAsDev.push(element);
        }
        else if (element.reviewers.includes(this.profile.address)){
          assignedIssuesAsReviewer.push(element);
        }
      });
    },
    updateData: function() {
      const backend = Backend.getClient();

      this.$emit("isLoading", true);
      // TODO handle / display errors in component
      Promise.all([
        backend.get("/projects").then(response => {
          return response.data;
        }),
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

        const gitlabUsername = this.getGitlabUsername(profile.gitlabId)
          .then(username => {this.gitlabUsername = username})
          .catch(err => {console.log(err)});
        if (this.profile.address !== null) { //can only filter for assigned issues if address is set
          const allIssues = this.getAllIssuesOfProjects(projects)
          .then(issues => {this.filterAssignedIssues(issues)})
          .catch(err => {console.log(err)});
        }
        else {
          //TODO handle?
        }

        this.$emit("isLoading", false);
      });
    }
  }
};
</script>
