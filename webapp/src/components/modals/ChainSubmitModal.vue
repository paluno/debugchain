<template>
  <div class="d-inline">
    <Modal v-model="value" title="Transaction Infos">
      <p>
        Please confirm the action in your Ethereum client / Metamask.<br />
      </p>
      <template slot="footer">
        <button type="button" class="btn btn-secondary" @click="closeModal">Close</button>
      </template>
    </Modal>
  </div>
</template>

<script>
import Modal from "@/components/Modal.vue";

export default {
  name: "ChainSubmitModal",
  components: {
    Modal
  },
  props: {
    value: Boolean
  },
  methods: {
    closeModal() {
      this.$emit("input", false);
    }
  }
};
</script>

<style>
</style>