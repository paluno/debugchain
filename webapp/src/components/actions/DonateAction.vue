<template>
  <div class="d-inline">
    <button class="btn btn-outline-secondary btn-sm" @click="showDonateEtherModal">Donate Ether</button>

    <Modal v-model="show" title="Donate Ether">
      <p>
        How much Ether do you want to donate on this Issue?
      </p>
      <div class="row">
        <label class="col-sm-3">Donation:</label>
        <input class="col" type="number" step="0.1" placeholder="Enter your donation" v-model="donation" />
        <label class="col-sm-3"> Ether </label>
        <!--TODO check the validity of the input-->
      </div>
      <template slot="footer">
        <button type="button" class="btn btn-primary" @click="donateEther">Donate</button>
        <button type="button" class="btn btn-secondary" @click="closeDonateEtherModal">Close</button>
      </template>
    </Modal>
  </div>
</template>

<script>
import ErrorContainer from "@/api/errorContainer";
import Modal from "@/components/Modal.vue";
import Contract from "@/api/contract";

export default {
  name: "DonateAction",
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
    }
  },
  data() {
    return {
      show: false,
      donation: 0
    };
  },
  methods: {
    showDonateEtherModal() {
      this.show = true;
    },
    closeDonateEtherModal() {
      this.show = false;
      this.donation = 0;
    },
    donateEther() {
      const donation = this.donation;
      const issueId = this.issueId;
      const contract = new Contract(this.contractAddress);

      // TODO extract to global module
      this.$emit("isLoading", true);
      contract
        .donate(issueId, donation)
        .catch(error => ErrorContainer.add(error))
        .then(() => this.$emit("isLoading", false))
        .then(() => this.closeDonateEtherModal())
        .then(() => this.$emit("donated"));
    }
  }
};
</script>

<style>
</style>
