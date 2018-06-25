<template>
  <modal :value="value" @input="inputEventHandler" title="Set Ethereum address">
    <p>
      To execute some actions in this application, we need the public address of your Ethereum wallet. Please enter it below.<br />
    </p>
    <div class="alert alert-warning">
      This address will be used for your interactions with the Ethereum Smart Contract and can not be changed afterwards.
    </div>
    <!-- TODO validate Ethereum address -->
    <input class="form-control" type="text" placeholder="Enter your Ethereum address..." v-model="address" />
    <template slot="footer">
      <button type="button" class="btn btn-primary" @click="save()">Save</button>
      <button type="button" class="btn btn-secondary" @click="close()">Close</button>
    </template>
  </modal>
</template>

<script>
import Modal from "@/components/Modal";

export default {
  props: {
    value: Boolean
  },
  components: {
    Modal
  },
  data: function() {
    return {
      address: null
    };
  },
  methods: {
    inputEventHandler: function(val) {
      if (!val) {
        this.close();
      }
    },
    save: function() {
      this.$emit("save", this.address);
    },
    reset: function() {
      this.address = null;
    },
    close: function() {
      this.reset();
      this.$emit("input", false);
    }
  }
};
</script>

<style>
</style>
