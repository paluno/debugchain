<template>
  <div class="issue_detail">
    <h1>This is an issue detail page.</h1>
  </div>
</template>

<script>
export default {
  name: "IssueDetail",
  props: {
    projectId: Number,
    issueId: Number
  }
};
</script>