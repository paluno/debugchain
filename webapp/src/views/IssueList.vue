<template>
  <div>
    <Navigation :profile="profile" :project="project" />
    <div class="content">
      <div class="container-fluid">
        <div v-if="project" class="row">
          <div class="col">
            <h1>{{project.name}}</h1>
          </div>
          <div class="col-auto">
            <a class="btn btn-link btn-sm" :href="project.web_url" target="_blank">
              Open in Gitlab
              <i class="fas fa-external-link-alt"></i>
            </a>
            <button v-if="canWithdraw" class="btn btn-outline-secondary btn-sm" @click="showWithdrawModal">
              Withdraw Ether
            </button>
          </div>
        </div>
      </div>
      <div v-if="canWithdraw" class="row">
        <div class="col">
          <p>You have {{this.profile.pendingWithdrawals | weiToEther}} Ether available to withdraw in this project.</p>
        </div>
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
      <div class="table">
        <vue-good-table :columns="columns" :rows="rows" :pagination-options="{ enabled: true, perPage: 5}" :search-options="{ enabled: true}" styleClass="vgt-table striped bordered" @on-row-click="navigate">
        </vue-good-table>
      </div>
    </div>
  </div>
</template>

<script>
import ErrorContainer from "@/api/errorContainer";
import { Gitlab } from "@/api/gitlab";
import { Backend } from "@/api/backend";
import Modal from "@/components/Modal.vue";
import Navigation from "@/components/Navigation";
import Contract from "@/api/contract";
import getWeb3 from "@/api/getWeb3";

export default {
  name: "IssueList",
  components: {
    Navigation,
    Modal
  },
  filters: {
    weiToEther: function(value) {
      return getWeb3().fromWei(value, "ether");
    }
  },
  props: {
    projectId: [String, Number]
  },
  computed: {
    canWithdraw: function() {
      return (
        this.profile &&
        typeof this.profile.pendingWithdrawals === "number" &&
        this.profile.pendingWithdrawals > 0
      );
    }
  },
  data: function() {
    return {
      project: null,
      profile: null,
      columns: [
        {
          label: "Id",
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
          label: "Donation (ETH)",
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
      const gitlab = new Gitlab();
      const backend = new Backend();

      this.$emit("isLoading", true);
      Promise.all([
        gitlab.getProjectIssues(this.projectId),
        backend.getProjectIssues(this.projectId),
        backend.getProfile(this.projectId),
        gitlab.getProject(this.projectId),
        backend.getProject(this.projectId)
      ])
        .then(results => {
          const issues = results[0];
          const contractIssues = results[1];
          const profile = results[2];
          const gitlabProject = results[3];
          const project = results[4];

          this.setIssues(issues, contractIssues);
          this.setProfile(profile);
          this.setProject(gitlabProject);
          this.setContractAddress(project.address);
        })
        .catch(error => ErrorContainer.add(error))
        .then(() => this.$emit("isLoading", false));
    },
    setProject: function(project) {
      this.project = project;
    },
    setProfile: function(newProfile) {
      this.profile = newProfile;
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
