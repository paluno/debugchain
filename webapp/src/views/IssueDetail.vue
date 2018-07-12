<template>
  <div class="issue_detail">
    <Navigation :address="profile.address" :pendingWithdrawals="profile.pendingWithdrawals" v-bind:projectId="projectId" v-bind:issueId="issueId" />

    <div v-if="issue">
      <div class="form-group row">
        <div class="col">
          <h1><a :href="issue.web_url">{{issue.title}}</a></h1>
        </div>
        <div class="col-auto">
          <button v-if="canDonate" class="btn btn-outline-secondary btn-sm" v-on:click="showDonateEtherModal">Donate Ether</button>

          <Modal v-model="donateEtherModal.show" title="Donate Ether">
            <p>
              How much Ether do you want to donate on this Issue?
            </p>
            <div class="row">
              <label class="col-sm-3">Donation:</label>
              <input class="col" type="number" step="0.1" placeholder="Enter your donation" v-model="donateEtherModal.donation" />
              <label class="col-sm-3"> Ether </label>
              <!--TODO check the validity of the input-->
            </div>

            <template slot="footer">
              <button type="button" class="btn btn-primary" @click="donateEther">Save</button>
              <button type="button" class="btn btn-secondary" @click="closeDonateEtherModal">Close</button>
            </template>
          </Modal>

          <button v-if="canApprove" class="btn btn-outline-success btn-sm" v-on:click="showApproveIssueModal">Approve</button>

          <Modal v-model="approveIssueModal.show" title="Assign Reviewers">
            <p>
              Please assign at least one reviewer in order to approve this issue. The reviewers will be responsible for reviewing the proposed solution for this issue.
            </p>
            <div class="row">
              <label class="col">Pick reviewers (CTRL+Click to choose multiple)</label>
            </div>
            <div class="row">
              <select class="col custom-select" v-model="approveIssueModal.selectedReviewers" multiple>
                <option v-for="reviewer in possibleReviewers" :key="reviewer.address" v-bind:value="reviewer.address">{{reviewer.username}} - {{reviewer.address}}</option>
              </select>
            </div>
            <template slot="footer">
              <button type="button" class="btn btn-primary" @click="approveIssue">Approve</button>
              <button type="button" class="btn btn-secondary" @click="closeApproveIssueModal">Close</button>
            </template>
          </Modal>

          <button v-if="canLock" class="btn btn-outline-warning btn-sm" v-on:click="showLockIssueModal">Lock Issue</button>

          <Modal v-model="lockIssueModal.show" title="Lock Issue">
            <p>
              Do you really want to lock Issue "{{issue.title}}"?
            </p>

            <template slot="footer">
              <button type="button" class="btn btn-primary" @click="lockIssue">Yes</button>
              <button type="button" class="btn btn-secondary" @click="closeLockIssueModal">No</button>
            </template>
          </Modal>

          <button v-if="canFinishDevelopment" class="btn btn-outline-primary btn-sm" v-on:click="showFinishDevelopmentModal">Finish Development</button>

          <Modal v-model="finishDevelopmentModal.show" title="Finish Development">
            <p>
              Do you really want to finish the development of Issue "{{issue.title}}"?
            </p>

            <template slot="footer">
              <button type="button" class="btn btn-primary" @click="finishDevelopment">Yes</button>
              <button type="button" class="btn btn-secondary" @click="closeFinishDevelopmentModal">No</button>
            </template>
          </Modal>

          <button v-if="canReview" class="btn btn-outline-primary btn-sm" v-on:click="showFinishReviewModal">Finish Review</button>

          <Modal v-model="finishReviewModal.show" title="Finish Review">
            <p>
              Give your review feedback for issue "{{issue.title}}":
            </p>

            <template slot="footer">
              <button type="button" class="btn btn-primary" @click="finishReview(true)">Accept</button>
              <button type="button" class="btn btn-danger" @click="finishReview(false)">Reject</button>
              <button type="button" class="btn btn-secondary" @click="closeFinishReviewModal">Cancel</button>
            </template>
          </Modal>
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
import Navigation from "@/components/Navigation";
import Modal from "@/components/Modal.vue";
import Gitlab from "@/api/gitlab";
import Backend from "@/api/backend";
import Contract from "@/api/contract";
import getWeb3 from "@/api/getWeb3";
import marked from "marked";

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
        this.contractIssue &&
        this.contractIssue.lifecycleStatus == "APPROVED" &&
        this.contractIssue.reviewers.includes(this.userAddress)
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
      possibleReviewers: null,

      donateEtherModal: {
        donation: 0,
        show: false
      },
      approveIssueModal: {
        show: false,
        selectedReviewers: []
      },
      lockIssueModal: {
        show: false
      },
      finishDevelopmentModal: {
        show: false
      },
      finishReviewModal: {
        show: false
      }
    };
  },
  created: function() {
    this.updateData();
  },
  methods: {
    donateEther: function() {
      const donation = this.donateEtherModal.donation;
      const client = Backend.getClient();
      const issueId = this.issueId;
      const contract = new Contract(this.contractAddress);
      contract
        .donate(issueId, donation)
        .then(() => this.closeDonateEtherModal())
        .then(() => this.updateData());
    },
    approveIssue: function() {
      const client = Backend.getClient();
      const issueId = this.issueId;
      const contract = new Contract(this.contractAddress);
      const reviewers = this.approveIssueModal.selectedReviewers;
      contract
        .approve(issueId, reviewers)
        .then(() => this.closeApproveIssueModal())
        .then(() => this.updateData());
    },
    lockIssue: function() {
      const client = Backend.getClient();
      const issueId = this.issueId;
      const contract = new Contract(this.contractAddress);
      contract
        .lock(issueId)
        .then(() => this.closeLockIssueModal())
        .then(() => this.updateData());
    },
    finishDevelopment: function() {
      const client = Backend.getClient();
      const issueId = this.issueId;
      const contract = new Contract(this.contractAddress);
      contract
        .develop(issueId)
        .then(() => this.closeFinishDevelopmentModal())
        .then(() => this.updateData());
    },
    finishReview: function(isAccepted) {
      const client = Backend.getClient();
      const issueId = this.issueId;
      const contract = new Contract(this.contractAddress);
      contract
        .review(issueId, isAccepted)
        .then(() => this.closeFinishReviewModal())
        .then(() => this.updateData());
    },
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
      this.combined = [];
      if (cIssue.reviewers.length == cIssue.reviewStatus.length) {
        for (let i = 0; i < cIssue.reviewers.length; i++) {
          if (!this.combinedReviews === undefined) {
            this.combinedReviews[i] = {
              reviewer: cIssue.reviewers[i],
              value: cIssue.reviewStatus[i]
            };
          }
        }
      }
      cIssue.reviewStatus = this.combined;
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
      ]).then(results => {
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
        this.$emit("isLoading", false);
      });
    },
    setProfile: function(newProfile) {
      if (newProfile) {
        this.profile = {
          address: newProfile.address,
          pendingWithdrawals: newProfile.pendingWithdrawals
        };
      }
    },
    showDonateEtherModal: function() {
      this.donateEtherModal.show = true;
    },
    closeDonateEtherModal: function() {
      this.donateEtherModal.show = false;
      this.donateEtherModal.donation = 0;
    },
    showApproveIssueModal: function() {
      this.approveIssueModal.show = true;
      this.approveIssueModal.reviewers = [];
    },
    closeApproveIssueModal: function() {
      this.approveIssueModal.show = false;
    },
    showLockIssueModal: function() {
      this.lockIssueModal.show = true;
    },
    closeLockIssueModal: function() {
      this.lockIssueModal.show = false;
    },
    showFinishDevelopmentModal: function() {
      this.finishDevelopmentModal.show = true;
    },
    closeFinishDevelopmentModal: function() {
      this.finishDevelopmentModal.show = false;
    },
    showFinishReviewModal: function() {
      this.finishReviewModal.show = true;
    },
    closeFinishReviewModal: function() {
      this.finishReviewModal.show = false;
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