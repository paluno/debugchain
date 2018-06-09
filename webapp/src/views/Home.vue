<template>
  <div class="home">
    <IssueTable v-bind:projectId="projectId" />
  </div>
</template>

<script>
// @ is an alias to /src
import IssueTable from "@/components/IssueTable.vue";

export default {
  name: "home",
  props: {
    projectId: Number
  },
  components: {
    IssueTable
  }
};
</script>
