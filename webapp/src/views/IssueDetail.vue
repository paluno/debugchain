<template>
  <div class="issue_detail">
    <Navigation v-bind:projectId="projectId" v-bind:issueId="issueId" />

    <Modal v-model="approveModal.show" title="Assign Reviewers">
      <p>
        Please assign at least one reviewer in order to approve this issue.
        The reviewers will be responsible for reviewing the proposed solution for this issue.
      </p>
      <p> AKTUELL NOCH DUMMY DATEN!!</p>
      <div class="row">
        <label class="col">Pick reviewers (CTRL+Click to choose multiple)</label>
      </div>
      <div class="row">
        <select class="col custom-select" v-model="approveModal.selectedReviewers" multiple>
          <!--<option v-for="reviewer in possibleReviewers" :key="reviewer.address" value="reviewer.address">{{reviewer.username}}</option>-->
          <option value="0x627306090abab3a6e1400e9345bc60c78a8bef57">0x627306090abab3a6e1400e9345bc60c78a8bef57</option>
          <option value="0xf17f52151ebef6c7334fad080c5704d77216b732">0xf17f52151ebef6c7334fad080c5704d77216b732</option>
          <option value="0xc5fdf4076b8f3a5357c5e395ab970b5b54098fef">0xc5fdf4076b8f3a5357c5e395ab970b5b54098fef</option>
          <option value="0x821aea9a577a9b44299b9c15c88cf3087f3b5544">0x821aea9a577a9b44299b9c15c88cf3087f3b5544</option>
        </select>
      </div>
      <template slot="footer">
        <button type="button" class="btn btn-primary" @click="approveIssue">Approve</button>
        <button type="button" class="btn btn-secondary" @click="closeApproveModal">Close</button>
      </template>
    </Modal>

    <div v-if="issue">
      <div class="form-group row">
        <div class="col">
          <h1>{{issue.title}}</h1>
        </div>
        <div class="col-auto">
          <button class="btn btn-outline-secondary btn-sm" v-on:click="donateEther">Donate Ether</button>
          <button v-if="approvable" class="btn btn-outline-success btn-sm" v-on:click="openApproveModal">Approve</button>
        </div>
      </div>
      <div class="row">
        <div class="col">
          <span :class="badgeState">{{state}}</span>
          <label>Created</label>
          <span>at {{prettyTime}}</span>
          <label>by</label>
          <img class="avatar" :src="issue.author.avatar_url" />
          <span>
            <b>{{issue.author.username}}</b>
          </span>
        </div>
      </div>
      <hr>
      <div class="row">
        <div class="col">
          <p>
            {{issue.description}}
          </p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import Navigation from "@/components/Navigation";
import Modal from "@/components/Modal.vue";
import Gitlab from "@/api/gitlab";
import Backend from "@/api/backend";
import Contract from "@/api/contract";

export default {
  name: "IssueDetail",
  components: {
    Modal,
    Navigation
  },
  props: {
    projectId: String,
    issueId: String
  },
  computed: {
    prettyTime: function() {
      if (this.issue != null) {
        const options = {
          weekday: "long",
          year: "numeric",
          month: "long",
          day: "numeric"
        };
        const date = new Date(this.issue.created_at);
        return date.toLocaleDateString("en-EN", options);
      }
    },
    state: function() {
      if (this.issue != null) {
        switch (this.issue.state) {
          case "opened":
            return "Open";
            break;
          case "closed":
            return "Closed";
            break;
          default:
            return "None";
        }
      }
      return "None";
    },
    badgeState: function() {
      if (this.issue != null) {
        switch (this.issue.state) {
          case "opened":
            return "badge badge-success";
            break;
          case "closed":
            return "badge badge-primary";
            break;
          default:
            return "badge badge-secondary";
        }
      }
      return "badge badge-secondary";
    }
  },
  data: function() {
    return {
      approveModal: {
        show: false,
        selectedReviewers: []
      },
      issue: null,
      contractAddress: null,
      possibleReviewers: null,
      approvable: false
    };
  },
  created: function() {
    this.updateData();
  },
  methods: {
    donateEther: function() {
      alert("Hier muss der Metamask-Aufruf für das Donaten rein");
    },
    approveIssue: function() {
      const contract = new Contract(this.contractAddress);
      contract
        .approve(this.issueId, this.approveModal.selectedReviewers)
        .then(() => {
          this.closeApproveModal();
          this.updateData();
        });
    },
    setIssue: function(issue) {
      this.issue = issue;
    },
    setContractAddress: function(contractAddress) {
      this.contractAddress = contractAddress;
    },
    setPossibleReviewers: function(possibleReviewers, projectMembers) {
      this.possibleReviewers = possibleReviewers.map(reviewer => {
        for (let i = 0; i < this.projectMembers.length; i++) {
          const member = this.projectMembers[i];
          if (reviewer.gitlabId == member.id) {
            return {
              address: reviewer.address,
              gitlabId: reviewer.gitlabId,
              username: member.username
            };
          }
        }
      });
    },
    setApprovable: function() {
      this.approvable = true;
    },
    updateData: function() {
      const gitlab = Gitlab.getClient();
      const backend = Backend.getClient();

      this.$emit("isLoading", true);
      Promise.all([
        gitlab.projects.issues.one(this.projectId, this.issueId),
        gitlab.projects.owned(),
        gitlab.projects.members.list(this.projectId),
        backend.get("/projects/" + this.projectId).then(result => {
          return result.data;
        }),
        backend
          .get("/projects/" + this.projectId + "/reviewers")
          .then(result => {
            return result.data;
          })
      ]).then(results => {
        const issue = results[0];
        const ownedProjects = results[1];
        const projectMembers = results[2];
        const currentProject = results[3];
        const possibleReviewers = results[4];

        this.setIssue(issue);
        this.setContractAddress(currentProject.address);
        this.setPossibleReviewers(possibleReviewers, projectMembers);
        if (ownedProjects.find(project => project.id == this.projectId)) {
          this.setApprovable();
        }
        this.$emit("isLoading", false);
      });
    },
    openApproveModal: function() {
      this.approveModal.show = true;
    },
    closeApproveModal: function() {
      this.approveModal.show = false;
    }
  }
};
</script>

<style>
.avatar {
  border-radius: 50%;
  height: 25px;
  width: 25px;
}
</style>