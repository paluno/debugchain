<template>
  <div class="d-inline">
    <button class="btn btn-outline-success btn-sm" v-on:click="showApproveIssueModal">Approve</button>

    <Modal v-model="show" title="Assign Reviewers">
      <p>
        Please assign at least one reviewer in order to approve this issue. The reviewers will be responsible for reviewing the proposed solution for this issue.
      </p>
      <div class="row">
        <label class="col">Pick reviewers (CTRL+Click to choose multiple)</label>
      </div>
      <div class="row">
        <select class="col custom-select" v-model="selectedReviewers" multiple>
          <option v-for="reviewer in possibleReviewers" :key="reviewer.address" v-bind:value="reviewer.address">{{reviewer.username}} - {{reviewer.address}}</option>
        </select>
      </div>
      <template slot="footer">
        <!-- TODO validate selection: At least one reviewer; disable button; show error -->
        <button type="button" class="btn btn-primary" @click="approveIssue" v-bind:disabled="selectedReviewers.length == 0">Approve</button>
        <button type="button" class="btn btn-secondary" @click="closeApproveIssueModal">Close</button>
      </template>
    </Modal>
  </div>
</template>

<script>
import ErrorContainer from "@/api/errorContainer";
import Modal from "@/components/Modal.vue";
import Contract from "@/api/contract";

export default {
  name: "ApproveAction",
  components: {
    Modal
  },
  props: {
    contractAddress: {
      type: String,
      required: true
    },
    issueId: {
      type: [String, Number],
      required: true
    },
    possibleReviewers: {
      type: Array,
      required: true
    }
  },
  data: function() {
    return {
      show: false,
      selectedReviewers: []
    };
  },
  methods: {
    showApproveIssueModal: function() {
      this.show = true;
      this.reviewers = [];
    },
    closeApproveIssueModal: function() {
      this.show = false;
    },
    approveIssue: function() {
      const issueId = this.issueId;
      const contract = new Contract(this.contractAddress);
      const reviewers = this.selectedReviewers;

      // TODO extract to global module
      this.$emit("isLoading", true);
      contract
        .approve(issueId, reviewers)
        .catch(error => ErrorContainer.add(error))
        .then(() => this.$emit("isLoading", false))
        .then(() => this.closeApproveIssueModal())
        .then(() => this.$emit("approved"));
    }
  }
};
</script>

<style>
</style>
