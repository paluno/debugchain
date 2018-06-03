<template>
  <div class="table">
    <h1>{{ msg }}</h1>
    <input type="submit" value="GET CURRENT USER" v-on:click="getUser"/>
    <p>
      {{json}}
    </p>

    <vue-good-table
      :columns="columns"
      :rows="rows"
      :pagination-options="{ enabled: true, perPage: 5}"
      :search-options="{ enabled: true}"
      styleClass="vgt-table striped bordered"> 
    </vue-good-table>
  </div>
</template>

<script>
import { UserSession } from "../auth.js";
const GitlabApis = require("gitlab-api-wrapper");

export default {
  name: "IssueTable",
  props: {
    
  },
  data: function() {
    return {
      json: "",
    
      //issue table dummy data
      columns: [
        {
          label: 'ID',
          field: 'id',
          filterOptions: {
            enabled: true,
          },
        },
        {
          label: 'Issue',
          field: 'issue',
          filterOptions: {
            enabled: true,
          },
        },
        {
          label: 'ETH',
          field: 'eth',
          type: 'number',
        },
        {
          label: 'Status',
          field: 'status',
          filterOptions: {
            enabled: true,
          },
        }
      ],
      rows: [
        { id:1, issue:"Fix Bug #1", eth: 2.3, status: "Open" },
        { id:2, issue:"Fix Bug #2", eth: 1.7, status: "In Review" },
        { id:3, issue:"Fix Bug #3", eth: 3, status: "Locked" },
        { id:4, issue:"Fix Bug #4", eth: 12.2, status: "In Review" },
        { id:5, issue:"Fix Bug #5", eth: 5.3, status: "Open" },
        { id:6, issue:"Fix Bug #6", eth: 0.7, status: "Locked" },
        { id:7, issue:"Fix Bug #7", eth: 8.5, status: "Open" },
        { id:8, issue:"Fix Bug #8", eth: 7, status: "Locked" },
      ],
    };
  },
  methods: {
    getUser: function(event) {
      const client = GitlabApis({
        // the GitLab url
        base_url: "http://localhost",
        private_token: UserSession.token.accessToken,
        timeout: 3000
      });
      const that = this;
      client.users.current().then(function(result) {
        that.setJson(JSON.stringify(result));
      });
    },
    setJson: function(newJson) {
      this.json = newJson;
    }
  }
};
</script>

<style scoped lang="scss">
  #table{
  padding: 20px;
  }
</style>