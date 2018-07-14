<template>
  <div class="issueList">
    <Navigation :address="profile.address" :pendingWithdrawals="profile.pendingWithdrawals" :projectId="projectId" />
    
    <div class="row">
      <div v-if="project" class="col">
        <h1>{{project.name}}</h1>
      </div>
      <div class="col-auto">
        <a class="btn btn-outline-primary btn-sm" :href="project.web_url">Open in Gitlab  <i class="fas fa-external-link-alt"></i></a>
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
import Navigation from "@/components/Navigation";
import getWeb3 from "@/api/getWeb3";

export default {
  name: "IssueList",
  props: {
    projectId: String
  },
  components: {
    Navigation
  },
  data: function() {
    return {
      project: null,
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
      rows: []
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
    updateData: function() {
      const gitlab = Gitlab.getClient();
      const backend = Backend.getClient();

      this.$emit("isLoading", true);
      Promise.all([
        gitlab.projects.issues.list(this.projectId),
        backend
          .get("projects/" + this.projectId + "/issues/")
          .then(result => result.data),
        backend.get("/profile/withdrawals/" + this.projectId).then(r => r.data),
        gitlab.projects.one(this.projectId)
      ]).then(results => {
        const issues = results[0];
        const contractIssues = results[1];
        const profile = results[2];
        const project = results[3];

        this.setIssues(issues, contractIssues);
        this.setProfile(profile);
        this.setProject(project);
        this.$emit("isLoading", false);
      });
    },
    setProject: function(project) {
      this.project = project;
    },
    setProfile: function(newProfile) {
      if (newProfile) {
        this.profile = {
          address: newProfile.address,
          pendingWithdrawals: newProfile.pendingWithdrawals
        };
      }
    },
    navigate: function(params) {
      this.$router.push({
        name: "issue",
        params: {
          projectId: this.projectId,
          issueId: params.row.id.toString()
        }
      });
    }
  }
};
</script>

<style scoped lang="scss">
#table {
  padding: 20px;
}
</style>
