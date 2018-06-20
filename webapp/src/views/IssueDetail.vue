<template>
  <div class="issue_detail">
    <Navigation v-bind:projectId="projectId"/>
    <h1><a href="gitlabLink" title="Click to watch issue in Gitlab" class="badge badge-success">Open</a> IssueTitle</h1>
    <div class="row">
      <div class="col">
        <label>Created</label>
        <span>10 Minutes ago</span>
        <label>by</label>
        <span>User </span><img class="avatar" src="https://www.gravatar.com/avatar/e64c7d89f26bd1972efa854d13d7dd61?s=48&d=identicon"/>
      </div>
    </div>
    <hr>
    <div class="row">
      <div class="col">
        <p>
          Hier soll die Beschreibung des Issues rein. Damit man auch lesen kann wofür das Issue steht
        </p>
      </div>
    </div>
    <hr>
    <div class="form-group row">
      <div class="col">
        <button class="btn btn-outline-secondary btn-sm">Donate Ether</button>
      </div>
    </div>
  </div>
</template>
<style>
.avatar {
  border-radius: 50%;
  height: 25px;
  width: 25px;
}
</style>
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