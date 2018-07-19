<template>
  <div class="d-inline">
    <button class="btn btn-outline-primary btn-sm" v-on:click="showModal">Finish Review</button>

    <Modal v-model="show" title="Finish Review">
      <p>
        Give your review feedback for issue "{{issue.title}}":
      </p>

      <template slot="footer">
        <button type="button" class="btn btn-primary" @click="finishReview(true)">Accept</button>
        <button type="button" class="btn btn-danger" @click="finishReview(false)">Reject</button>
        <button type="button" class="btn btn-secondary" @click="closeModal">Cancel</button>
      </template>
    </Modal>

    <chain-submit-modal v-model="showChainSubmit"></chain-submit-modal>
  </div>
</template>

<script>
import ErrorContainer from "@/api/errorContainer";
import Modal from "@/components/Modal.vue";
import Contract from "@/api/contract";
import ChainSubmitModal from "@/components/modals/ChainSubmitModal";

export default {
  name: "ReviewAction",
  components: {
    Modal,
    ChainSubmitModal
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
    issue: {
      type: Object,
      required: true
    }
  },
  data: function() {
    return {
      show: false,
      showChainSubmit: false
    };
  },
  methods: {
    showModal: function() {
      this.show = true;
    },
    closeModal: function() {
      this.show = false;
    },
    finishReview: function(isAccepted) {
      const issueId = this.issueId;
      const contract = new Contract(this.contractAddress);

      // TODO extract to global module
      this.closeModal();
      this.showChainSubmit = true;

      contract
        .review(issueId, isAccepted)
        .catch(error => ErrorContainer.add(error))
        .then(() => (this.showChainSubmit = false))
        .then(() => this.$emit("reviewed"));
    }
  }
};
</script>

<style>
</style>
