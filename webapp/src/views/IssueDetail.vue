<template>
  <div class="issue_detail">
    <Navigation v-bind:projectId="projectId" />
    <div v-if="issue">
      <h1>
        <span :class="badgeState">{{state}}</span> {{issue.title}}</h1>
      <div class="row">
        <div class="col">
          <label>Created</label>
          <span>at {{prettyTime}}</span>
          <label>by</label>
          <span>{{issue.author.username}} </span><img class="avatar" :src="issue.author.avatar_url" />
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
      <hr>
      <div class="form-group row">
        <div class="col">
          <button class="btn btn-outline-secondary btn-sm">Donate Ether</button>
        </div>
      </div>
    </div>
    <div v-else>
      <div class="row">
        <div class="col-md-3">
          <span>Das Issue wird geladen...</span>
          <div class="progress">
            <div class="progress-bar progress-bar-striped progress-bar-animated" role="progressbar" aria-valuenow="100" aria-valuemin="0" aria-valuemax="100" style="width: 100%"></div>
          </div>
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
      issue: null
    };
  },
  created: function() {
    this.updateData();
  },
  methods: {
    setIssue: function(issue) {
      this.issue = issue;
    },
    updateData: function() {
      const client = gitlab.getClient();
      client.projects.issues.one(this.projectId, this.issueId).then(issue => {
        this.setIssue(issue);
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