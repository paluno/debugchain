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
    <hr>
    <div v-if="chainIssue">
      <div class="row">
        <div class="col">
          <h2>Issue-Chain-Detail</h2>
        </div>
      </div>
      <div class="row">
        <div class="col">
          <span :class="chainBadgeState">{{chainIssue.lifecycleStatus}}</span>
          <span>
            <b>{{chainIssue.developer}}</b>
            is listed as developer
          </span>
        </div>
      </div>
      <hr>
      <div class="row">
        <div class="col">
          <h4>{{chainIssue.donationSum}} Ether is the current bounty</h4>
        </div>
      </div>
      <div v-for="donation in chainIssue.donationValues" :key="donation.donator" class="row">
        <div class="col">
          <span>{{donation.donator}} has donated {{donation.value}} Ether </span>
        </div>
      </div>
      <hr>
      <div class="row">
        <div class="col">
          <h4>Review-Overview</h4>
        </div>
      </div>
      <div v-for="review in chainIssue.reviewStatus" :key="review.reviewer" class="row">
        <div class="col">
          <span v-if="review.value">{{review.reviewer}} has reviewed this issue</span>
          <span v-else>{{review.reviewer}} has not yet reviewed this issue</span>
        </div>
      </div>
    </div>
    <div v-else>
      <div class="row">
        <div class="col">
          <h2>This issue is not yet tracked in the DebugChain</h2>
          <p>It will automatically be part of the DebugChain after someone has donated some ether so it. You can do this by clicking the "Donate"-button above!</p>
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
    },
    chainBadgeState: function() {
      if (this.chainIssue != null) {
        switch (this.chainIssue.lifecycleStatus) {
          case "DEFAULT": // Default = New
            return "badge badge-success";
            break;
          case "APPROVED":
            return "badge badge-info";
            break;
          case "LOCKED":
            return "badge badge-primary";
            break;
          case "DEVELOPED":
            return "badge badge-warning";
            break;
          case "COMPLETED":
            return "badge badge-light";
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
      chainIssue: null,
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
    setIssue: function(issue, chainIssue) {
      this.issue = issue;
      if (chainIssue !== undefined) {
        this.chainIssue = chainIssue;
        this.chainIssue.donationSum =
          this.chainIssue.donationSum / 1000000000000000000;
        this.combinedDonations = [];
        if (
          this.chainIssue.donationValues.length ==
          this.chainIssue.donators.length
        ) {
          for (let i = 0; i < this.chainIssue.donationValues.length; i++) {
            this.combinedDonations[i] = {
              donator: this.chainIssue.donators[i],
              value: this.chainIssue.donationValues[i] / 1000000000000000000
            };
          }
        }
        this.combinedReviews = [];
        if (
          this.chainIssue.reviewers.length ==
          this.chainIssue.reviewStatus.length
        ) {
          for (let i = 0; i < this.chainIssue.reviewers.length; i++) {
            this.combinedReviews[i] = {
              reviewer: this.chainIssue.reviewers[i],
              value: this.chainIssue.reviewStatus[i]
            };
          }
        }
        console.log(this.chainIssue);
        this.chainIssue.donationValues = this.combinedDonations;
        this.chainIssue.reviewStatus = this.combinedReviews;
      }
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
        new Promise((resolve, reject) => {
          backend
            .get("projects/" + this.projectId + "/issues/" + this.issueId)
            .then(result => {
              resolve(result.data);
            })
            .catch(error => {
              console.log(
                "Could not get issue-details from backend/chain. Maybe this issue is not yet tracked"
              );
              resolve(); // Resolve auch im Fehlerfall, damit das Promise.all() nicht auch aufs Maul fliegt
            });
        })
      ]).then(results => {
        const issue = results[0];
        const projects = results[1];
        const chainIssue = results[2];

        this.setIssue(issue, chainIssue);
        if (projects.find(project => project.id == this.projectId)) {
          this.setApprovable();
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