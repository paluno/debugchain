<template>
  <div class="d-inline">
    <button class="btn btn-outline-warning btn-sm" v-on:click="showModal">Unlock Issue</button>

    <Modal v-model="show" title="Unlock Issue">
      <p>
        Do you really want to unlock Issue "{{issue.title}}"?
      </p>

      <template slot="footer">
        <button type="button" class="btn btn-primary" @click="unlockIssue">Yes</button>
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
  name: "UnlockAction",
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
    unlockIssue: function() {
      const issueId = this.issueId;
      const contract = new Contract(this.contractAddress);

      // TODO extract to global module
      this.closeModal();
      this.showChainSubmit = true;

      contract
        .unlock(issueId)
        .catch(error => {
          if (!error.canceled) ErrorContainer.add(error);
        })
        .then(() => (this.showChainSubmit = false))
        .then(() => this.$emit("unlocked"));
    }
  }
};
</script>

<style>
</style>
