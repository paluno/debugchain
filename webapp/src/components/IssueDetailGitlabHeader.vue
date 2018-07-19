<template>
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
</template>

<script>
export default {
  name: "IssueDetailGitlabHeader",
  props: {
    issue: {
      type: Object,
      required: true
    }
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
