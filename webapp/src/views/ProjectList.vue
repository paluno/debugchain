<template>
  <div id="projectlist">
    <Navigation :profile="profile" />
    <div class="content">
      <vue-good-table :columns="columns" :rows="gitlabProjects" :pagination-options="{ enabled: true, perPage: 10}" :search-options="{ enabled: true}" styleClass="vgt-table striped bordered" @on-row-click="onRowClick">
        <template slot="table-row" slot-scope="props">
          <span v-if="props.column.field == 'created' && props.formattedRow[props.column.field] == 'Yes'">
            <span style="font-weight: bold; color: green;">{{props.row.created}}</span>
          </span>
          <span v-else-if="props.column.field == 'created' && props.formattedRow[props.column.field] == 'No'">
            <span style="font-weight: bold; color: red;">{{props.row.created}}</span>
          </span>
          <span v-else>
            {{props.formattedRow[props.column.field]}}
          </span>
        </template>
      </vue-good-table>

      <Modal v-model="createProjectModal.show" title="Create Project">
        <p>
          Do you want to create a DebugChain project for this GitLab project?
        </p>
        <div class="row">
          <label class="col-sm-3">Name:</label>
          <div class="col">{{createProjectModal.name}}</div>
        </div>
        <div class="row">
          <label class="col-sm-3">Gitlab-Link:</label>
          <div class="col">
            <a :href="createProjectModal.url">{{createProjectModal.url}}</a>
          </div>
        </div>

        <template slot="footer">
          <button type="button" class="btn btn-primary" @click="createProject">Create</button>
          <button type="button" class="btn btn-secondary" @click="closeCreateProjectModal">Close</button>
        </template>
      </Modal>

      <chain-submit-modal v-model="showChainSubmit"></chain-submit-modal>
    </div>
  </div>

</template>


<script>
import ErrorContainer from "@/api/errorContainer";
import { Gitlab } from "@/api/gitlab";
import Modal from "@/components/Modal.vue";
import ChainSubmitModal from "@/components/modals/ChainSubmitModal";
import Navigation from "@/components/Navigation";
import { Backend } from "@/api/backend";
import Contract from "@/api/contract";

export default {
  name: "ProjectList",
  components: {
    Modal,
    ChainSubmitModal,
    Navigation
  },
  data: function() {
    return {
      profile: null,
      createProjectModal: {
        show: false,
        id: 0,
        name: "",
        url: ""
      },
      columns: [
        {
          label: "Id",
          field: "id",
          type: "number"
        },
        {
          label: "Maintainer",
          field: "owner"
        },
        {
          label: "Gitlab-Link",
          field: "url"
        },
        {
          label: "tracked in chain",
          field: "created"
        }
      ],
      gitlabProjects: [],
      showChainSubmit: false
    };
  },
  created: function() {
    this.updateData();
  },
  methods: {
    createProject: function() {
      const backend = new Backend();
      const contract = new Contract();
      const projectId = this.createProjectModal.id;

      this.closeCreateProjectModal();
      this.showChainSubmit = true;

      contract
        .deploy(projectId)
        .then(address => backend.createProject(projectId, address))
        .then(() => {
          this.$router.push({
            name: "issueList",
            params: { projectId: projectId.toString() }
          });
        })
        .catch(error => ErrorContainer.add(error))
        .then(() => (this.showChainSubmit = false));
    },
    setProjects: function(gitlabProjects, debugChainProjects) {
      this.gitlabProjects = gitlabProjects.map(gProject => {
        const dcProject = debugChainProjects.find(
          p => p.gitlabId == gProject.id
        );
        const projectExistent = dcProject === undefined ? "No" : "Yes";
        return {
          id: gProject.id,
          url: gProject.web_url,
          name: gProject.name,
          owner: gProject.owner.username,
          created: projectExistent
        };
      });
    },
    setProfile: function(newProfile) {
      this.profile = newProfile;
    },
    updateData: function() {
      const gitlab = new Gitlab();
      const backend = new Backend();

      this.$emit("isLoading", true);
      Promise.all([
        gitlab.getProjects(),
        backend.getProfile(),
        backend.getProjects()
      ])
        .then(results => {
          this.setProjects(results[0], results[2]);
          this.setProfile(results[1]);
        })
        .catch(error => ErrorContainer.add(error))
        .then(() => this.$emit("isLoading", false));
    },
    showCreateProjectModal: function(id, name, url) {
      this.createProjectModal.show = true;
      this.createProjectModal.id = id;
      this.createProjectModal.name = name;
      this.createProjectModal.url = url;
    },
    closeCreateProjectModal: function() {
      this.createProjectModal.show = false;
      this.createProjectModal.id = 0;
      this.createProjectModal.name = "";
      this.createProjectModal.url = "";
    },
    openProject: function(id, name, url) {
      const backend = new Backend();

      this.$emit("isLoading", true);
      backend
        // TODO replace with getProject(id) and catch/ignore 404
        .getProjects()
        .then(projects => projects.find(e => e.gitlabId === id))
        .then(project => {
          if (project !== undefined) {
            this.$router.push({
              name: "issueList",
              params: { projectId: id.toString() }
            });
          } else {
            this.showCreateProjectModal(id, name, url);
          }
        })
        .catch(error => ErrorContainer.add(error))
        .then(() => this.$emit("isLoading", false));
    },
    onRowClick: function(params) {
      this.openProject(params.row.id, params.row.name, params.row.url);
    }
  }
};
</script>