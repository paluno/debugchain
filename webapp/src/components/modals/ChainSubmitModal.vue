<template>
  <div class="d-inline">
    <Modal v-model="value" title="Transaction Infos">
      <div>
        <p>
          Please confirm the action in your Ethereum client / Metamask.<br />
        </p>
        <p>
          This message will close automatically once your transaction is submitted.
          <i>This might take a few seconds.</i>
        </p>
        <div class="alert alert-warning">
          <p>
            Note, that the time to confirm the transaction in the Ethereum network depends on the gas price you set.
          </p>
          <p>
            Thus, you might not see your changes reflected on the page immediately. Please observe transactions in your client and reload the page at a later time if neccessary.
          </p>
        </div>
      </div>
      <template slot="footer">
        <button type="button" class="btn btn-secondary" @click="closeModal">Close</button>
      </template>
    </Modal>
  </div>
</template>

<script>
import Modal from "@/components/Modal";

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