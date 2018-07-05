<template>
  <div class="issue_detail">
    <Navigation v-bind:projectId="projectId" v-bind:issueId="issueId" />
    <div v-if="issue">
      <div class="form-group row">
        <div class="col">
          <h1>{{issue.title}}</h1>
        </div>
        <div class="col-auto">
          <button v-if="donatable" class="btn btn-outline-secondary btn-sm" v-on:click="showDonateEtherModal">Donate Ether</button>

          <Modal v-model="donateEtherModal.show" title="Donate Ether">
            <p>
              How much Ether do you want to donate on this Issue?
            </p>
            <div class="row">
              <label class="col-sm-3">Donation:</label>
              <input class="col" type="number" placeholder="Enter your donation" v-model="donateEtherModal.donation" />
              <label class="col-sm-3"> Ether </label>
              <!--TODO check the validity of the input-->
            </div>

            <template slot="footer">
              <button type="button" class="btn btn-primary" @click="donateEther">Save</button>
              <button type="button" class="btn btn-secondary" @click="closeDonateEtherModal">Close</button>
            </template>
          </Modal>

          <button v-if="approvable" class="btn btn-outline-success btn-sm" v-on:click="showApproveIssueModal">Approve</button>

          <Modal v-model="approveIssueModal.show" title="Approve Issue">
            <p>
              Do you really want to approve Issue "{{issue.title}}"?
            </p>

            <template slot="footer">
              <button type="button" class="btn btn-primary" @click="approveIssue">Yes</button>
              <button type="button" class="btn btn-secondary" @click="closeApproveIssueModal">No</button>
            </template>
          </Modal>

          <button v-if="lockable" class="btn btn-outline-warning btn-sm" v-on:click="showLockIssueModal">Lock Issue</button>

          <Modal v-model="lockIssueModal.show" title="Lock Issue">
            <p>
              Do you really want to lock Issue "{{issue.title}}"?
            </p>

            <template slot="footer">
              <button type="button" class="btn btn-primary" @click="lockIssue">Yes</button>
              <button type="button" class="btn btn-secondary" @click="closeLockIssueModal">No</button>
            </template>
          </Modal>

          <button v-if="inDevelopment" class="btn btn-outline-primary btn-sm" v-on:click="showFinishDevelopmentModal">Finish Development</button>

          <Modal v-model="finishDevelopmentModal.show" title="Finish Development">
            <p>
              Do you really want to finish the development of Issue "{{issue.title}}"?
            </p>

            <template slot="footer">
              <button type="button" class="btn btn-primary" @click="finishDevelopment">Yes</button>
              <button type="button" class="btn btn-secondary" @click="closeFinishDevelopmentModal">No</button>
            </template>
          </Modal>

          <button v-if="reviewable" class="btn btn-outline-primary btn-sm" v-on:click="showFinishReviewModal">Finish Review</button>

          <Modal v-model="finishReviewModal.show" title="Finish Review">
            <p>
              Give your review feedback for isssue "{{issue.title}}":
            </p>

            <template slot="footer">
              <!--TODO: handle finishReviewModal.accepted = true (onclick Accept) /false (onclick Reject) -->
              <button type="button" class="btn btn-primary" @click="finishReview">Accept</button>
              <button type="button" class="btn btn-primary" @click="finishReview">Reject</button>
              <button type="button" class="btn btn-secondary" @click="closeFinishReviewModal">Cancel</button>
            </template>
          </Modal>

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
      issue: null,
      contractAddress: null,
      // TODO implement as reactive computed properties
      donatable: true,
      approvable: false,
      lockable: false,
      inDevelopment: false,
      reviewable: false,
      withdrawable: false,
      donateEtherModal: {
        donation: 0,
        show: false
      },
      approveIssueModal: {
        show: false
      },
      lockIssueModal: {
        show: false
      },
      finishDevelopmentModal: {
        show: false
      },
      finishReviewModal: {
        show: false,
        accepted: false
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
      // TODO: get selected reviewers
      //const reviewers = this.approveIssueModal.reviewers;
      const reviewers = [
        "0x2341998aeb343",
        "0x2341998aeb340",
        "0x2341998aeb345"
      ]; // Dummy reviewers addresses
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
    finishReview: function() {
      const accepted = this.finishReviewModal.accepted; // TODO get user input from finishReviewModal
      const client = Backend.getClient();
      const issueId = this.issueId;
      const contract = new Contract(this.contractAddress);
      contract
        .review(issueId, accepted)
        .then(() => this.closeFinishReviewModal())
        .then(() => this.updateData());
    },
    withdraw: function() {
      contract.withdraw().then(() => {
        // TODO show alert message
      });
      // Withdraw Money -> Disable all buttons
      this.donatable = false;
      this.withdrawable = false;
    },
    setIssue: function(issue) {
      this.issue = issue;
    },
    setApprovable: function() {
      // Show Approve button
      // TODO set appovable if user == maintainer and donations on this issue not empty
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
      //this.donatable = false; //TODO Macht das Sinn?
      // Disable Review button and show Withdraw button
      this.reviewable = false;
      this.withdrawable = true;
    },
    setContractAddress: function(address) {
      this.setContractAddress = address;
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
        backend.get("/profile").then(r => r.data),
        bakcend.get("/projects/" + this.projectId).then(r => r.data)
      ]).then(results => {
        const issue = results[0];
        const ownedProjects = results[1];
        const membership = results[2];
        const contractIssue = results[3];
        const profile = results[4];
        const project = results[5];
        const reviewer = null;
        //const reviewer = membership.reviewer; // TODO
        const user = profile.address;

        // TODO set loaded data directly as properties and use reactive properties

        this.setIssue(issue);
        this.setContractAddress(project.address);

        if (ownedProjects.find(project => project.id == this.projectId)) {
          this.setApprovable();
        }

        // show contract actions if issue is available in contract/backend (i.e. has been donated to)
        if (contractIssue) {
          const locked = contractIssue.locked;
          const developer = contractIssue.developer;

          //only show "lock issue" - button if dev is not a reviewer for it himself and not locked already
          if (!reviewer && !locked) {
            this.setLockable();
          }

          //only show "finish Development" - button if current user is the developer that has locked the issue
          if (user === developer) {
            this.setInDevelopment();
          }
        }

        this.$emit("isLoading", false);
      });
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