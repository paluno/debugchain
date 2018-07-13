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

  </div>
</template>

<script>
import Modal from "@/components/Modal.vue";
import Contract from "@/api/contract";

export default {
  name: "LockAction",
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
    lockIssue: function() {
      const issueId = this.issueId;
      const contract = new Contract(this.contractAddress);

      // TODO extract to global module
      this.$emit("isLoading", true);
      contract
        .lock(issueId)
        .catch(error => ErrorContainer.add(error))
        .then(() => this.$emit("isLoading", false))
        .then(() => this.closeModal())
        .then(() => this.$emit("locked"));
    }
  }
};
</script>

<style>
</style>
