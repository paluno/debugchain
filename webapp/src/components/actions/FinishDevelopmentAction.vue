<template>
  <div class="d-inline">
    <button class="btn btn-outline-primary btn-sm" @click="showModal">Finish Development</button>

    <Modal v-model="show" title="Finish Development">
      <p>
        Do you really want to finish the development of Issue "{{issue.title}}"?
      </p>

      <template slot="footer">
        <button type="button" class="btn btn-primary" @click="finishDevelopment">Yes</button>
        <button type="button" class="btn btn-secondary" @click="closeModal">No</button>
      </template>
    </Modal>
  </div>
</template>

<script>
import ErrorContainer from "@/api/errorContainer";
import Modal from "@/components/Modal.vue";
import Contract from "@/api/contract";

export default {
  name: "FinishDevelopmentAction",
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
    issue: {
      type: Object,
      required: true
    }
  },
  data: function() {
    return {
      show: false
    };
  },
  methods: {
    showModal: function() {
      this.show = true;
    },
    closeModal: function() {
      this.show = false;
    },
    finishDevelopment: function() {
      const issueId = this.issueId;
      const contract = new Contract(this.contractAddress);

      // TODO extract to global module
      this.$emit("isLoading", true);
      contract
        .develop(issueId)
        .catch(error => ErrorContainer.add(error))
        .then(() => this.$emit("isLoading", false))
        .then(() => this.closeModal())
        .then(() => this.$emit("finishedDevelopment"));
    }
  }
};
</script>

<style>
</style>
