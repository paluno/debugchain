<template>
  <div class="home">
    <Navigation/>
    <IssueTable v-bind:projectId="projectId" />
  </div>
</template>

<script>
// @ is an alias to /src
import IssueTable from "@/components/IssueTable.vue";
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
