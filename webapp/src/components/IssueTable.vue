<template>
  <div class="table">
    <input type="submit" value="GET CURRENT USER" v-on:click="getUser"/>
    <p>
      {{json}}
    </p>

    <vue-good-table
      :columns="columns"
      :rows="rows"
      :pagination-options="{ enabled: true, perPage: 5}"
      :search-options="{ enabled: true}"
      styleClass="vgt-table striped bordered"
      @on-row-click="navigate"> 
    </vue-good-table>
    <button v-on:click="updateData">load from gitlab</button>
  </div>
</template>

<script>
import router from "../router";
import gitlab from "../api/gitlab";

export default {
  name: "IssueTable",
  props: {},
  data: function() {
    return {
      json: "",

      //issue table dummy data
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
      rows: [
        { id: 1, issue: "Fix Bug #1", eth: 2.3, status: "Open" },
        { id: 2, issue: "Fix Bug #2", eth: 1.7, status: "In Review" },
        { id: 3, issue: "Fix Bug #3", eth: 3, status: "Locked" },
        { id: 4, issue: "Fix Bug #4", eth: 12.2, status: "In Review" },
        { id: 5, issue: "Fix Bug #5", eth: 5.3, status: "Open" },
        { id: 6, issue: "Fix Bug #6", eth: 0.7, status: "Locked" },
        { id: 7, issue: "Fix Bug #7", eth: 8.5, status: "Open" },
        { id: 8, issue: "Fix Bug #8", eth: 7, status: "Locked" }
      ]
    };
  },
  methods: {
    getUser: function(event) {
      const client = gitlab.getClient();
      const that = this;
      client.users.current().then(function(result) {
        that.setJson(JSON.stringify(result));
      });
    },
    setJson: function(newJson) {
      this.json = newJson;
    },
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
    updateData: function(event) {
      const client = gitlab.getClient();
      const that = this;
      client.projects.issues.list(1).then(issues => {
        that.setIssues(issues);
      });
    },
    navigate: function(params) {
      router.push({ path: "issue", query: { id: params.row.id } });
    }
  }
};
</script>

<style scoped lang="scss">
#table {
  padding: 20px;
}
</style>