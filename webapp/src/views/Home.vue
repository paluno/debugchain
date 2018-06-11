<template>
  <div class="home">
    <IssueTable />
  </div>
</template>

<script>
// @ is an alias to /src
import IssueTable from "@/components/IssueTable.vue";

export default {
  name: "home",
  components: {
    IssueTable
  }
};
</script>
