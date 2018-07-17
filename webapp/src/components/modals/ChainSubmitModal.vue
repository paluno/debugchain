<template>
  <div class="d-inline">
      <Modal v-model="show" title="Transaction Infos">
        <p >
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
  props: {},
  data() {
    return {
      show: false
    };
  },
  methods: {
    showModal() {
      this.show = true;
    },
    closeModal() {
      this.show = false;
    }
  }
};
</script>

<style>
</style>