<template>
  <div class="issue_detail">
    <Navigation v-bind:projectId="projectId" v-bind:issueId="issueId" />
    <div v-if="issue">
      <div class="form-group row">
        <div class="col">
          <h1>{{issue.title}}</h1>
        </div>
        <div class="col-auto">
          <button class="btn btn-outline-secondary btn-sm" v-on:click="donateEther">Donate Ether</button>
          <button v-if="approvable" class="btn btn-outline-success btn-sm" v-on:click="approveIssue">Approve</button>
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
import gitlab from "@/api/gitlab";

export default {
  name: "IssueDetail",
  components: {
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
      issue: null,
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
      alert("Hier muss der Metamask-Aufruf für das Approven des Issues rein");
    },
    setIssue: function(issue) {
      this.issue = issue;
    },
    setApprovable: function() {
      this.approvable = true;
    },
    updateData: function() {
      const client = gitlab.getClient();

      this.$parent.showOverlay();
      client.projects.issues.one(this.projectId, this.issueId).then(issue => {
        this.$parent.hideOverlay();
        this.setIssue(issue);
      });
      this.$parent.showOverlay();
      client.projects.owned().then(projects => {
        this.$parent.hideOverlay();
        projects.forEach(project => {
          if (project.id == this.projectId) {
            this.setApprovable();
          }
        });
      });
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