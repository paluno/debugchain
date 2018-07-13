<template>
  <div class="issue_detail">
    <Navigation :address="profile.address" :pendingWithdrawals="profile.pendingWithdrawals" v-bind:projectId="projectId" v-bind:issueId="issueId" />

    <div v-if="issue">
      <div class="form-group row">
        <div class="col">
          <h1>{{issue.title}}</h1>
        </div>
        <div class="col-auto">
          <a class="btn btn-link btn-sm" :href="issue.web_url" target="_blank">Open in Gitlab  <i class="fas fa-external-link-alt"></i></a>

          <donate-action v-if="canDonate" @donated="updateData" :contractAddress="contractAddress" :issueId="issueId" @isLoading="onIsLoadingChanged"></donate-action>

          <approve-action v-if="canApprove" @approved="updateData" :contractAddress="contractAddress" :issueId="issueId" :possibleReviewers="possibleReviewers" @isLoading="onIsLoadingChanged"></approve-action>

          <lock-action v-if="canLock" @locked="updateData" :contractAddress="contractAddress" :issueId="issueId" :issue="issue" @isLoading="onIsLoadingChanged"></lock-action>

          <unlock-action v-if="canUnlock" @unlocked="updateData" :contractAddress="contractAddress" :issueId="issueId" :issue="issue" @isLoading="onIsLoadingChanged"></unlock-action>

          <finish-development-action v-if="canFinishDevelopment" @finishedDevelopment="updateData" :contractAddress="contractAddress" :issueId="issueId" :issue="issue" @isLoading="onIsLoadingChanged"></finish-development-action>

          <review-action v-if="canReview" @reviewed="updateData" :contractAddress="contractAddress" :issueId="issueId" :issue="issue" @isLoading="onIsLoadingChanged"></review-action>

          <reset-action v-if="canReset" @reset="updateData" :contractAddress="contractAddress" :issueId="issueId" :issue="issue" @isLoading="onIsLoadingChanged"></reset-action>

          <delete-action v-if="canDelete" @deleted="updateData" :contractAddress="contractAddress" :issueId="issueId" :issue="issue" @isLoading="onIsLoadingChanged"></delete-action>
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
        <div class="col" v-html="markdownDescription">
        </div>
      </div>
    </div>
    <hr>
    <div v-if="contractIssue">
      <div class="row">
        <div class="col">
          <h2>Issue-Chain-Detail</h2>
        </div>
      </div>
      <div class="row">
        <div class="col">
          <span :class="chainBadgeState">{{readableLifecycle}}</span>
          <span v-if="contractIssue.developer">
            <b>{{contractIssue.developer}}</b>
            is listed as developer
          </span>
          <span v-else>
            There is no developer assigned to this issue
          </span>
        </div>
      </div>
      <hr>
      <div class="row">
        <div class="col">
          <h4>{{contractIssue.donationSum}} Ether is the current bounty</h4>
        </div>
      </div>
      <div v-for="donation in contractIssue.donationValues" :key="donation.donator" class="row">
        <div class="col">
          <span>{{donation.donator}} has donated {{donation.value}} Ether </span>
        </div>
      </div>
      <hr>
      <div v-if="contractIssue.reviewStatus.length > 0">
        <div class="row">
          <div class="col">
            <h4>Review-Overview</h4>
          </div>
        </div>
        <div v-for="review in contractIssue.reviewStatus" :key="review.reviewer" class="row">
          <div class="col">
            <span v-if="review.value">{{review.reviewer}} has reviewed this issue</span>
            <span v-else>{{review.reviewer}} has not yet reviewed this issue</span>
          </div>
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
import ErrorContainer from "@/api/errorContainer";
import Navigation from "@/components/Navigation";
import Modal from "@/components/Modal.vue";
import DonateAction from "@/components/actions/DonateAction";
import ApproveAction from "@/components/actions/ApproveAction";
import LockAction from "@/components/actions/LockAction";
import UnlockAction from "@/components/actions/UnlockAction";
import FinishDevelopmentAction from "@/components/actions/FinishDevelopmentAction";
import ReviewAction from "@/components/actions/ReviewAction";
import ResetAction from "@/components/actions/ResetAction";
import DeleteAction from "@/components/actions/DeleteAction";
import Gitlab from "@/api/gitlab";
import Backend from "@/api/backend";
import Contract from "@/api/contract";
import getWeb3 from "@/api/getWeb3";
import marked from "marked";

export default {
  name: "IssueDetail",
  components: {
    Modal,
    Navigation,
    DonateAction,
    ApproveAction,
    LockAction,
    UnlockAction,
    FinishDevelopmentAction,
    ReviewAction,
    ResetAction,
    DeleteAction
  },
  props: {
    projectId: String,
    issueId: String
  },
  computed: {
    markdownDescription: function() {
      return marked(this.issue.description);
    },
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
    canDonate: function() {
      if (this.contractIssue != null) {
        return this.contractIssue.lifecycleStatus != "COMPLETED";
      }
      return true;
    },
    canApprove: function() {
      return (
        this.isMaintainer &&
        this.contractIssue &&
        this.contractIssue.lifecycleStatus == "DEFAULT"
      );
    },
    canLock: function() {
      return (
        this.contractIssue && this.contractIssue.lifecycleStatus == "APPROVED"
      );
    },
    canUnlock: function() {
      return (
        this.contractIssue &&
        this.contractIssue.lifecycleStatus == "LOCKED" &&
        (this.isMaintainer || this.userAddress == this.contractIssue.developer)
      );
    },
    canFinishDevelopment: function() {
      return (
        this.contractIssue &&
        this.contractIssue.lifecycleStatus == "LOCKED" &&
        this.userAddress == this.contractIssue.developer
      );
    },
    canReview: function() {
      return (
        this.contractIssue &&
        this.contractIssue.lifecycleStatus == "DEVELOPED" &&
        this.contractIssue.reviewers.includes(this.userAddress)
        // TODO check contractIssue.reviewStatus? Should reviewer be able to change status anytime?
      );
    },
    canReset: function() {
      return (
        this.isMaintainer &&
        this.contractIssue &&
        this.contractIssue.lifecycleStatus != "COMPLETED"
      );
    },
    canDelete: function() {
      return (
        this.isMaintainer &&
        this.contractIssue &&
        this.contractIssue.lifecycleStatus != "COMPLETED"
      );
    },
    chainBadgeState: function() {
      if (this.contractIssue != null) {
        switch (this.contractIssue.lifecycleStatus) {
          case "DEFAULT": // Default = New
            return "badge badge-success";
          case "APPROVED":
            return "badge badge-info";
          case "LOCKED":
            return "badge badge-primary";
          case "DEVELOPED":
            return "badge badge-warning";
          case "COMPLETED":
            return "badge badge-light";
          default:
            return "badge badge-secondary";
        }
      }
      return "badge badge-secondary";
    },
    readableLifecycle: function() {
      if (this.contractIssue != null) {
        switch (this.contractIssue.lifecycleStatus) {
          case "APPROVED":
            return "Approved";
          case "LOCKED":
            return "In Development";
          case "DEVELOPED":
            return "In Review";
          case "COMPLETED":
            return "Completed";
          case "DEFAULT":
            return "New";
          default:
            return "New";
        }
      }
    }
  },
  data: function() {
    return {
      profile: {
        address: null,
        pendingWithdrawals: null
      },
      issue: null,
      contractIssue: null,
      contractAddress: null,
      isMaintainer: false,
      userAddress: null,
      possibleReviewers: null
    };
  },
  created: function() {
    this.updateData();
  },
  methods: {
    setIssue: function(issue, contractIssue) {
      this.issue = issue;
      if (contractIssue !== undefined) {
        this.contractIssue = contractIssue;
        this.combineDonations(this.contractIssue);
        this.combineReviews(this.contractIssue);
      }
    },
    combineDonations(cIssue) {
      // TODO replace this with computed property
      cIssue.donationSum = getWeb3().fromWei(cIssue.donationSum, "ether");
      this.combined = [];
      if (cIssue.donationValues.length == cIssue.donators.length) {
        for (let i = 0; i < cIssue.donationValues.length; i++) {
          this.combined[i] = {
            donator: cIssue.donators[i],
            value: getWeb3().fromWei(cIssue.donationValues[i], "ether")
          };
        }
      }
      cIssue.donationValues = this.combined;
      cIssue.donators = undefined; // Remove donators since donators are now merged in donationValues
    },
    combineReviews(cIssue) {
      // TODO replace this with computed property
      let combined = [];
      if (cIssue.reviewers.length == cIssue.reviewStatus.length) {
        combined = cIssue.reviewers.map((reviewer, index) => {
          return {
            reviewer: reviewer,
            value: cIssue.reviewStatus[index]
          };
        });
      }
      cIssue.reviewStatus = combined;
      // TODO ignore for merge conflict > replace with computed property
      // cIssue.reviewers = undefined; // Remove reviewers since reviewers are now merged in reviewStatus
    },
    setPossibleReviewers: function(possibleReviewers, projectMembers) {
      // TODO replace this with computed property
      this.possibleReviewers = possibleReviewers.map(reviewer => {
        for (let i = 0; i < projectMembers.length; i++) {
          const member = projectMembers[i];
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
    setContractAddress: function(address) {
      this.contractAddress = address;
    },
    setContractIssue: function(issue) {
      this.contractIssue = issue;
    },
    setIsMaintainer: function(val) {
      this.isMaintainer = val;
    },
    setUserAddress: function(address) {
      this.userAddress = address;
    },
    updateData: function() {
      const gitlab = Gitlab.getClient();
      const backend = Backend.getClient();

      this.$emit("isLoading", true);

      Promise.all([
        gitlab.projects.issues.one(this.projectId, this.issueId),
        gitlab.projects.owned(),
        gitlab.projects.members.list(this.projectId),
        backend
          .get("/projects/" + this.projectId + "/reviewers")
          .then(r => r.data),
        backend
          .get("/projects/" + this.projectId + "/issues/" + this.issueId)
          .then(r => r.data)
          .catch(error => {
            console.log(
              "Could not get issue-details from backend/chain. Maybe this issue is not yet tracked"
            );
            // ignore 404 (issue not in contract)
            if (error.response.status != 404) throw error;
          }),
        backend.get("/profile/withdrawals/" + this.projectId).then(r => r.data),
        backend.get("/projects/" + this.projectId).then(r => r.data)
      ])
        .then(results => {
          const issue = results[0];
          const ownedProjects = results[1];
          const projectMembers = results[2];
          const possibleReviewers = results[3];
          const contractIssue = results[4];
          const profile = results[5];
          const project = results[6];

          this.setIssue(issue, contractIssue);
          if (ownedProjects.find(project => project.id == this.projectId)) {
            this.setIsMaintainer(true);
          }
          // TODO consider using web3 address instead of profile
          this.setUserAddress(profile.address);
          this.setPossibleReviewers(possibleReviewers, projectMembers);
          this.setContractIssue(contractIssue);
          this.setProfile(profile);
          this.setContractAddress(project.address);
        })
        .catch(error => ErrorContainer.add(error))
        .then(() => this.$emit("isLoading", false));
    },
    setProfile: function(newProfile) {
      if (newProfile) {
        this.profile = {
          address: newProfile.address,
          pendingWithdrawals: newProfile.pendingWithdrawals
        };
      }
    },
    onIsLoadingChanged: function(isLoading) {
      this.$emit("isLoading", isLoading);
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