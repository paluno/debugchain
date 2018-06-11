<template>
  <div class="home">
    <Navigation/>
    <IssueTable v-bind:projectId="projectId" />
  </div>
</template>

<script>
import IssueTable from "@/components/IssueTable";
import Navigation from "@/components/Navigation";

export default {
  name: "home",
  props: {
    projectId: String
  },
  components: {
    IssueTable,
    Navigation
  }
};
</script>
