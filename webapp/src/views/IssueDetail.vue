<template>
  <div class="issue_detail">
    <Navigation v-bind:projectId="projectId"/>
    <h1>This is an issue detail page.</h1>
  </div>
</template>

<script>
import Navigation from "@/components/Navigation";

export default {
  name: "IssueDetail",
  components: {
    Navigation
  },
  props: {
    projectId: String,
    issueId: String
  }
};
</script>