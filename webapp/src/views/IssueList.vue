<template>
  <div class="issueList">
    <Navigation :address="profile.address" :pendingWithdrawals="profile.pendingWithdrawals" :projectId="projectId" />
    <div v-if="canWithdraw" class="form-group row">
      <div class="col">
        <p>You have {{this.profile.pendingWithdrawals}} Ether in your pending withdrawals for this project.</p>
      </div>
      <div class="col-auto">
        <button class="btn btn-outline-primary btn-sm" v-on:click="showWithdrawModal">Withdraw</button>
        <Modal v-model="withdrawModal.show" title="Withdraw">
          <p>
            Do you really want to get pending withdrawals for the project: "{{projectId}}"?
          </p>

          <template slot="footer">
            <button type="button" class="btn btn-primary" @click="withdraw">Yes</button>
            <button type="button" class="btn btn-secondary" @click="closeWithdrawModal">No</button>
          </template>
        </Modal>
      </div>
    </div>
    <div class="table">
      <vue-good-table :columns="columns" :rows="rows" :pagination-options="{ enabled: true, perPage: 5}" :search-options="{ enabled: true}" styleClass="vgt-table striped bordered" @on-row-click="navigate">
      </vue-good-table>
    </div>
  </div>
</template>

<script>
import Gitlab from "@/api/gitlab";
import Backend from "@/api/backend";
import Modal from "@/components/Modal.vue";
import Navigation from "@/components/Navigation";
import Contract from "@/api/contract";
import getWeb3 from "@/api/getWeb3";

export default {
  name: "IssueList",
  props: {
    projectId: String
  },
  components: {
    Navigation,
    Modal
  },
  computed: {
    canWithdraw: function() {
      return (
        typeof this.profile.pendingWithdrawals === "number" &&
        this.profile.pendingWithdrawals > 0
      );
    }
  },
  data: function() {
    return {
      profile: {
        address: null,
        pendingWithdrawals: null
      },
      columns: [
        {
          label: "ID",
          field: "id",
          type: "number",
          filterOptions: {
            enabled: true
          }
        },
        {
          label: "Issue",
          field: "issue",
          filterOptions: {
            enabled: true
          }
        },
        {
          label: "ETH",
          field: "eth",
          type: "number"
        },
        {
          label: "Status",
          field: "status",
          filterOptions: {
            enabled: true
          }
        }
      ],
      rows: [],
      contractAddress: null,
      withdrawModal: {
        show: false
      }
    };
  },
  created: function() {
    this.updateData();
  },
  methods: {
    setIssues: function(gitlabIssues, contractIssues) {
      this.rows = gitlabIssues.map(gIssue => {
        const cIssue = contractIssues.find(c => c.id == gIssue.id);
        return {
          id: gIssue.id,
          issue: gIssue.title,
          eth: cIssue ? getWeb3().fromWei(cIssue.donationSum, "ether") : 0,
          status: cIssue ? this.getIssueStateFromContractIssue(cIssue) : "New"
        };
      });
    },
    getIssueStateFromContractIssue: function(issue) {
      switch (issue.lifecycleStatus) {
        case "APPROVED":
          return "Approved";
        case "LOCKED":
          return "In Development";
        case "DEVELOPED":
          return "In Review";
        case "COMPLETED":
          return "Completed";
        case "DEFAULT":
          return "New";
      }
    },
    withdraw: function() {
      const contract = new Contract(this.contractAddress);
      contract
        .withdraw()
        .then(() => this.closeWithdrawModal())
        .then(() => this.updateData());
    },
    updateData: function() {
      const gitlab = Gitlab.getClient();
      const backend = Backend.getClient();

      this.$emit("isLoading", true);
      Promise.all([
        gitlab.projects.issues.list(this.projectId),
        backend
          .get("projects/" + this.projectId + "/issues/")
          .then(result => result.data),
        backend
          .get("/profile/withdrawals/" + this.projectId)
          .then(r => r.data)
          .catch(error => {
            // see deb-159
            console.log(
              '"/profile/withdrawals/:id" failed: ignoring response as workaround.'
            );
            return null;
          }),
        backend.get("/projects/" + this.projectId).then(r => r.data)
      ]).then(results => {
        const issues = results[0];
        const contractIssues = results[1];
        const profile = results[2];
        const project = results[3];

        this.setIssues(issues, contractIssues);
        this.setProfile(profile);
        this.setContractAddress(project.address);
        this.$emit("isLoading", false);
      });
    },
    setProfile: function(newProfile) {
      if (newProfile) {
        this.profile = {
          address: newProfile.address,
          pendingWithdrawals: newProfile.pendingWithdrawals
        };
      }
    },
    setContractAddress: function(address) {
      this.contractAddress = address;
    },
    navigate: function(params) {
      this.$router.push({
        name: "issue",
        params: {
          projectId: this.projectId,
          issueId: params.row.id.toString()
        }
      });
    },

    showWithdrawModal: function() {
      this.withdrawModal.show = true;
    },
    closeWithdrawModal: function() {
      this.withdrawModal.show = false;
    }
  }
};
</script>

<style scoped lang="scss">
#table {
  padding: 20px;
}
</style>
