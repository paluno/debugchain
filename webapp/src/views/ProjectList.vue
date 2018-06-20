<template>
  <div class="projectsetup">
    <Navigation/>

    <vue-good-table :columns="columns" :rows="gitlabProjects" :pagination-options="{ enabled: true, perPage: 10}" :search-options="{ enabled: true}" styleClass="vgt-table striped bordered" @on-row-click="onRowClick">
    </vue-good-table>
    <button @click="showCreateProjectModal()">Create new</button>

    <Modal v-model="createProjectModal.show" title="Create Project">
      <p>
        Do you want to create a DebugChain project for this GitLab project?<br />
      </p>
      <div class="alert alert-primary">
        <label>ID: <input type="text" class="form-control" v-model="createProjectModal.id" /></label>
        <label>URL <input type="text" class="form-control" v-model="createProjectModal.url"/></label>
      </div>

      <template slot="footer">
        <button type="button" class="btn btn-primary" @click="createProjectModalSave()">Save</button>
        <button type="button" class="btn btn-secondary" @click="closeCreateProjectModal()">Close</button>
      </template>
    </Modal>
  </div>

</template>


<script>
import Gitlab from "@/api/gitlab";
import Modal from "@/components/Modal.vue";
import Navigation from "@/components/Navigation";
import appContract from "@/api/contract"

export default {
  name: "projectList",
  components: {
    Modal,
    Navigation
  },
  data: function() {
    return {
      createProjectModal: {
        show: false,
        id: 0,
        url: ""
      },
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
    };
  },
  created: function() {
    this.updateData();
  },
  
  methods: {
    createProjectModalSave: function(){
      this.createProject(this.createProjectModal.id, this.createProjectModal.url);
    },

    createProject: function(id, url) {
      console.log("create project called with ID / url:");
      console.log(this.createProjectModal.id);
      console.log(this.createProjectModal.url);
      //TODO meta mask + backend calls
      //deploy contract
      var debugchainContractDeployed = appContract.newContract(id);
      console.log(debugchainContractDeployed);

    },
    setProjects: function(newProjects) {
      this.gitlabProjects = newProjects.map(project => {
        return {
          id: project.id,
          url: project.web_url,
          owner: project.owner.username
        };
      });
    },
    updateData: function() {
      const client = Gitlab.getClient();
      const that = this;
      client.projects.list().then(projects => {
        that.setProjects(projects);
      });
    },
    showCreateProjectModal: function(params) {
      this.createProjectModal.show = true;
    },
    closeCreateProjectModal: function() {
      this.createProjectModal.show = false;
      this.createProjectModal.address = 0;
      this.createProjectModal.id = "";
    },
    openProject: function(id) {
      // TODO see if project already exists in our system,
      //   open its issue table if yes,
      //   else
      //   open Modal to create new project
      this.$router.push({
        name: "issueList",
        params: { projectId: id.toString() }
      });
    },
    onRowClick: function(params) {
      this.openProject(params.row.id);
    }
  }
};
</script>