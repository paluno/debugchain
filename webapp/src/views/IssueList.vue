<template>
  <div class="issueList">
    <Navigation v-bind:projectId="projectId"/>
    <IssueTable v-bind:projectId="projectId" />
  </div>
</template>

<script>
import IssueTable from "@/components/IssueTable";
import Navigation from "@/components/Navigation";

export default {
  name: "issueList",
  props: {
    projectId: String
  },
  components: {
    IssueTable,
    Navigation
  }
};
</script>
