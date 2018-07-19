<template>
  <div class="d-inline">
    <button class="btn btn-outline-warning btn-sm" v-on:click="showModal">Lock Issue</button>

    <Modal v-model="show" title="Lock Issue">
      <p>
        Do you really want to lock Issue "{{issue.title}}"?
      </p>

      <template slot="footer">
        <button type="button" class="btn btn-primary" @click="lockIssue">Yes</button>
        <button type="button" class="btn btn-secondary" @click="closeModal">No</button>
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
  name: "LockAction",
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
    lockIssue: function() {
      const issueId = this.issueId;
      const contract = new Contract(this.contractAddress);

      // TODO extract to global module
      this.closeModal();
      this.showChainSubmit = true;

      contract
        .lock(issueId)
        .catch(error => ErrorContainer.add(error))
        .then(() => (this.showChainSubmit = false))
        .then(() => this.$emit("locked"));
    }
  }
};
</script>

<style>
</style>
