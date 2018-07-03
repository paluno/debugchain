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
          <button v-if="lockable" class="btn btn-outline-warning btn-sm" v-on:click="lockIssue">Lock Issue</button>
          <button v-if="inDevelopment" class="btn btn-outline-primary btn-sm" v-on:click="finishDevelopment">Ready for Review</button>
          <button v-if="approvable" class="btn btn-outline-success btn-sm" v-on:click="approveIssue">Approve</button>
          <button v-if="reviewable" class="btn btn-outline-primary btn-sm" v-on:click="finishReview">Finish Review</button>
          <button v-if="withdrawable" class="btn btn-outline-success btn-sm" v-on:click="withdraw">Withdraw</button>
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
import Gitlab from "@/api/gitlab";
import Backend from "@/api/backend";

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
      approvable: false,
      lockable: false,
      inDevelopment: false,
      reviewable: false,
      withdrawable: false,
    };
  },
  created: function() {
    this.updateData();
  },
  methods: {
    donateEther: function() {
      alert("Hier muss der Metamask-Aufruf für das Donaten rein");
      // Donate Ether -> Show Approve button
      this.setApprovable(); //TODO Nur einmalig
    },
    approveIssue: function() {
      alert("Hier muss der Metamask-Aufruf für das Approven des Issues rein");
      // Approve Issue -> Show Lock button
      this.setLockable();
    },
    lockIssue: function() {
      alert("Hier muss der Metamask-Aufruf für das Locken des Issues rein");
      // Lock Issue -> Show Ready for Review button
      this.setInDevelopment();
    },
    finishDevelopment: function() {
      alert("Hier muss der Metamask-Aufruf für das Markieren des Issues als fertig bearbeitet und ready for review rein");
      // Finish Development -> Show Review button
      this.setReviewable();
    },
    finishReview: function() {
      alert("Hier muss der Metamask-Aufruf für das Finishen des Reviews rein");
      // Finish Review -> Show Withdraw Button
      this.setWithdrawable(); //TODO Überprüfen, ob alle Reviewer bestätigt haben
    },
    withdraw: function() {
      alert("Hier muss der Metamask-Aufruf für das Withdrawen des Geldes rein");
      // Withdraw Money -> Disable all buttons
      this.reviewable = false;
    },
    setIssue: function(issue) {
      this.issue = issue;
    },
    setApprovable: function() {
      // Show Approve button
      this.approvable = true;
    },
    setLockable: function() {
      // Disable Approve button and show Lock button
      this.approvable = false;
      this.lockable = true;
    },
    setInDevelopment: function() {
      // Disable Lock button and show Ready for Review button
      this.lockable = false;
      this.inDevelopment = true;
    },
    setReviewable: function() {
      // Disable Ready for Review button and show Review button
      this.inDevelopment = false;
      this.reviewable = true;
    },
    setWithdrawable: function() {
      //this.donatable = false TODO Könnte das Sinn machen?
      // Disable Review button and show Withdraw button
      this.reviewable = false;
      this.withdrawable = true;
    },
    updateData: function() {
      const gitlab = Gitlab.getClient();
      const backend = Backend.getClient();
      this.$emit("isLoading", true);
      Promise.all([
        gitlab.projects.issues.one(this.projectId, this.issueId),
        gitlab.projects.owned(),
        backend
          .get("/profile/memberships")
          .then(r => r.data)
          .then(data => data.find(m => m.projectGitlabId == this.projectId)),
        backend
          .get("/projects/" + this.projectId + "/issues/" + this.issueId)
          .then(r => r.data)
          .catch(error => {
            // TODO fix response code in backend to 404
            if (error.response.status != 500) throw error;
          }),
        backend.get("/profile").then(r => r.data)
      ]).then(results => {
        const issue = results[0];
        const ownedProjects = results[1];
        const membership = results[2];
        const contractIssue = results[3];
        const profile = results[4];
        //const reviewer = null; 
        const reviewer = membership.reviewer;
        const user = profile.address;

        this.setIssue(issue);

        if (ownedProjects.find(project => project.id == this.projectId)) {
          // TODO: set appovable if user == maintainer
          //this.setApprovable();
        }

        // show contract actions if issue is available in contract/backend (i.e. has been donated to)
        if (contractIssue) {
          const locked = contractIssue.locked;
          const developer = contractIssue.developer;

          //only show "lock issue" - button if dev is not a reviewer for it himself and not locked already
          if (!reviewer && !locked) {
            this.setLockable();
          }

          //only show "mark ready for review" - button if current user is the developer that has locked the issue
          if (user === developer) {
            this.setInDevelopment();
          }
        }

        this.$emit("isLoading", false);
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