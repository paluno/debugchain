<template>
  <div class="projectsetup">
    <Navigation/>

    <vue-good-table 
      :columns="columns"
      :rows="gitlabProjects"
      :pagination-options="{ enabled: true, perPage: 10}"
      :search-options="{ enabled: true}"
      styleClass="vgt-table striped bordered"
      @on-row-click="onRowClick">
    </vue-good-table>

    <Modal v-model="createProjectModal.show" title="Create Project">
      <p>
        Do you want to create a DebugChain project for this GitLab project?<br />
      </p>
      <div class="alert alert-primary">
        ID: {{ createProjectModal.id }}, URL: {{ createProjectModal.url }}
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

export default {
  name: "projectList",
  components: {
    Modal,
    Navigation
  },
  data: function() {
    return {
      projects: [],
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
      //TODO meta mask
      console.log("Create project called for project: ID = " + this.createProjectModal.id + ", URL = " + this.createProjectModal.url);

      //dummy POST to create project without actual contract address
      //-----just creates project with dummy contract address for dev purposes----
      const client = Backend.getClient();
      const self = this;
      client.post("/projects/", {
          address: "0x123456789",
          gitlabId: self.createProjectModal.id
        })
        .then(function(response) {
          console.log("Project created");
          self.$router.push({
              name: "issueList",
              params: { projectId: self.createProjectModal.id.toString() }
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
      const that = this;
      client.projects.list().then(projects => {
        that.setProjects(projects);
      });
    },
    showCreateProjectModal: function(id, url) {
      this.createProjectModal.show = true;
      this.createProjectModal.id = id;
      this.createProjectModal.url = url;
    },
    closeCreateProjectModal: function() {
      this.createProjectModal.show = false;
      this.createProjectModal.address = 0;
      this.createProjectModal.id = "";
    },
    openProject: function(id, url) {
      const client = Backend.getClient();
      const self = this;

      client
        .get("/projects")
        .then(function(response) {
          self.projects = response.data;
          const project = self.projects.find(e => e.gitlabId === id);

          if (project !== undefined) {
            self.$router.push({
              name: "issueList",
              params: { projectId: id.toString() }
            });
          }
          else {
            self.showCreateProjectModal(id, url);
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