<template>
  <div class="projectsetup">
    <Navigation/>

    <vue-good-table :columns="columns" :rows="gitlabProjects" :pagination-options="{ enabled: true, perPage: 10}" :search-options="{ enabled: true}" styleClass="vgt-table striped bordered" @on-row-click="onRowClick">
    </vue-good-table>

    <Modal v-model="createProjectModal.show" title="Create Project">
      <p>
        Do you want to create a DebugChain project for this GitLab project?<br />
      </p>
      <div class="alert alert-primary">
        {{createProjectModal.url}}
      </div>

      <template slot="footer">
        <button type="button" class="btn btn-primary" @click="createProject">Save</button>
        <button type="button" class="btn btn-secondary" @click="closeCreateProjectModal">Close</button>
      </template>
    </Modal>
  </div>

</template>


<script>
import Gitlab from "@/api/gitlab";
import Modal from "@/components/Modal.vue";
import Navigation from "@/components/Navigation";
import Backend from "@/api/backend";
import Contract from "../api/contract";

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
    createProject: function() {
      const client = Backend.getClient();
      const contract = new Contract();
      const projectId = this.createProjectModal.id;
      contract
        .deploy(projectId)
        .then(address => {
          client.post("/projects/", {
            address: address,
            gitlabId: projectId
          });
        })
        .then(() => {
          this.$router.push({
            name: "issueList",
            params: { projectId: projectId.toString() }
          });
        });
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
      client.projects.list().then(projects => {
        this.setProjects(projects);
      });
    },
    showCreateProjectModal: function(id, url) {
      this.createProjectModal.show = true;
      this.createProjectModal.id = id;
      this.createProjectModal.url = url;
    },
    closeCreateProjectModal: function() {
      this.createProjectModal.show = false;
      this.createProjectModal.id = 0;
      this.createProjectModal.url = "";
    },
    openProject: function(id, url) {
      const client = Backend.getClient();

      client
        .get("/projects")
        .then(response => {
          this.projects = response.data;
          const project = this.projects.find(e => e.gitlabId === id);

          if (project !== undefined) {
            this.$router.push({
              name: "issueList",
              params: { projectId: id.toString() }
            });
          } else {
            this.showCreateProjectModal(id, url);
          }
        })
        .catch(function(error) {
          // TODO error handling
          alert("Could not load projects from backend: " + error.message);
        });
    },
    onRowClick: function(params) {
      this.openProject(params.row.id, params.row.url);
    }
  }
};
</script>