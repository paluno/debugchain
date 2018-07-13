<template>

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
</template>

<script>
export default {
  name: "IssueDetailContract",
  props: {
    contractIssue: {
      type: Object,
      required: false
    }
  },
  computed: {
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
  }
};
</script>

<style>
</style>
