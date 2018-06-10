<template>
  <div class="projectsetup">

    <vue-good-table
      :columns="columns"
      :rows="gitlabProjects"
      :pagination-options="{ enabled: true, perPage: 10}"
      :search-options="{ enabled: true}"
      styleClass="vgt-table striped bordered"> 
    </vue-good-table>

    <form @submit.prevent="createProject">
        <input type="text" placeholder="Enter the GitLab-ID..." v-model="id">
        <button @click="createProject" type="button">Create Project</button>
      </form>
  </div>
</template>

<script>
import gitlab from "../api/gitlab";

export default {
  name: "ProjectSetup",
  data() {
    return {
        id: "",
        columns: [
            {
          label: "ID",
          field: "id",
          type: "number"
        },
        {
          label: "Maintainer",
          field: "owner"
        },
        {
          label: "URL",
          field: "url"
        }
        ],
        gitlabProjects: []
    }
  },
  created: function() {
    this.updateData();
  },
  methods: {
      createProject() {
          //TODO meta mask + backend calls
          console.log(this.id);
      },
      setProjects(newProjects) {
        this.gitlabProjects = newProjects.map(project => {
        return {
        id: project.id,
        url: project.web_url,
        owner: project.owner.username
        };
      });
      },
      updateData() {
        const client = gitlab.getClient();
        const that = this;
        client.projects.list().then(projects => {
        that.setProjects(projects);
      });
      }
  }
};
</script>