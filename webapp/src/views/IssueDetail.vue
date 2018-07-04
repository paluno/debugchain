<template>
  <div class="issue_detail">
    <Navigation v-bind:projectId="projectId" v-bind:issueId="issueId" />

    <Modal v-model="approveModal.show" title="Create Project">
      <p>
        Please assign at least one reviewer in order to approve this issue.
        The reviewers will be responsible for reviewing the proposed solution for this issue.
      </p>
      <div class="row">
        <label class="col">Pick reviewers (CTRL+Click to choose multiple)</label>
      </div>
      <div class="row">
        <select class="col custom-select" multiple>
          <option value="1">One</option>
          <option value="2">Two</option>
          <option value="3">Three</option>
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
        show: false
      },
      issue: null,
      approvable: false
    };
  },
  created: function() {
    this.updateData();
    const contract = new Contract(null, "http://localhost:9545");
    const backend = Backend.getClient();
    backend.get("/projects/" + this.projectId).then(result => {
      this.contractAdress = result.address;
    });
  },
  methods: {
    donateEther: function() {
      alert("Hier muss der Metamask-Aufruf für das Donaten rein");
    },
    approveIssue: function() {
      const contract = new Contract(this.contractAdress);
      contract.approve(this.issueId, reviewers).then(() => {
        alert("Hurray, you approved");
      });
    },
    setIssue: function(issue) {
      this.issue = issue;
    },
    setApprovable: function() {
      this.approvable = true;
    },
    updateData: function() {
      const gitlab = Gitlab.getClient();

      this.$emit("isLoading", true);
      Promise.all([
        gitlab.projects.issues.one(this.projectId, this.issueId),
        gitlab.projects.owned()
      ]).then(results => {
        const issue = results[0];
        const projects = results[1];

        this.setIssue(issue);
        if (projects.find(project => project.id == this.projectId)) {
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