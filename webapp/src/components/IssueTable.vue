<template>
  <div class="table">
    <vue-good-table
      :columns="columns"
      :rows="rows"
      :pagination-options="{ enabled: true, perPage: 5}"
      :search-options="{ enabled: true}"
      styleClass="vgt-table striped bordered"
      @on-row-click="navigate"> 
    </vue-good-table>
  </div>
</template>

<script>
import router from "@/router";
import gitlab from "@/api/gitlab";

export default {
  name: "IssueTable",
  props: {
    projectId: String
  },
  data: function() {
    return {
      columns: [
        {
          label: "ID",
          field: "id",
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
    setIssues: function(newIssues) {
      this.rows = newIssues.map(issue => {
        return {
          id: issue.id,
          issue: issue.title,
          eth: 0,
          status: "Unknown"
        };
      });
    },
    updateData: function() {
      const client = gitlab.getClient();
      const that = this;
      client.projects.issues.list(this.projectId).then(issues => {
        that.setIssues(issues);
      });
    },
    navigate: function(params) {
      router.push({
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