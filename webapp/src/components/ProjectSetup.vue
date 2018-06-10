<template>
  <div class="projectsetup">
    <form @submit.prevent="createProject">
        <input type="text" placeholder="Enter the GitLab-ID..." v-model="id">
        <button @click="createProject" type="button">Create Project</button>
      </form>
  </div>
</template>

<script>
export default {
  name: "ProjectSetup",
  data() {
    return {
        id: "",
    }
  },
  methods: {
      createProject() {
          //TODO meta mask + backend calls
          console.log(this.id);
      }
  }
};
</script>