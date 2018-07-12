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
    <vue-good-table :columns="columns"
                    :rows="filterAssignedIssues"
                    styleClass="vgt-table striped bordered">
    </vue-good-table>
  </div>
</template>

<script>
import Modal from "@/components/Modal";
import SetAddressModal from "@/components/modals/SetAddressModal";
import Backend from "@/api/backend";
import Gitlab from "@/api/gitlab";
import Navigation from "@/components/Navigation";
import getWeb3 from "@/api/getWeb3";

export default {
  name: "profile",
  data: function() {
    return {
      profile: null,
      gitlabUsername: null,
      projectMemberships: [],
      assignedIssuesAsDev: [],
      assignedIssuesAsReviewer: [],
      showAddressModal: false,
      allIssues: [],
      columns: [
        {
          label: "ID",
          field: "id",
          type: "number"
        },
        {
          label: "Status",
          field: "status"
        },
        {
          label: "ETH",
          field: "eth",
          type: "number"
        },
        {
          label: "Assigned as",
          field: "assignedAs"
        }
      ],
      rows: []
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
  computed: {
    filterAssignedIssues: function() {
      if(this.profile.address === null) return [];
      const profileAddress = this.profile.address.toLowerCase();
      const rows = [];

      this.allIssues.forEach(issue => {
        if (issue.developer.toLowerCase() === profileAddress) {
          rows.push({
            id: issue.id,
            status: issue.lifecycleStatus,
            eth: getWeb3().fromWei(issue.donationSum, "ether"),
            assignedAs: "Developer"
          });
        }
        else {
          issue.reviewers.forEach(r => {
            if (r.toLowerCase() === profileAddress){
              rows.push({
                id: issue.id,
                status: issue.lifecycleStatus,
                eth: getWeb3().fromWei(issue.donationSum, "ether"),
                assignedAs: "Reviewer" 
              });
            }
          });
        }
      });
      return rows;
    }
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
    
    getAllIssues: function(projects) {
      const backend = Backend.getClient();
      projects.map(p => {backend.get("/projects/" + p.gitlabId + "/issues/")
                          .then(issues => { issues.data.forEach(element => {this.allIssues.push(element)}) })
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
        //get all issues of all projects, to then filter for assigned issues afterwards
        this.getAllIssues(projects);
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
