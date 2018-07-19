<template>
  <div class="issue_detail">
    <Navigation :profile="profile" :project="project" :issue="issue" />

    <div class="container-fluid">
      <div v-if="issue">
        <div class="form-group row">
          <div class="col">
            <h1>{{issue.title}}</h1>
          </div>
          <div class="col-auto">
            <a class="btn btn-link btn-sm" :href="issue.web_url" target="_blank">Open in Gitlab
              <i class="fas fa-external-link-alt"></i>
            </a>

            <donate-action v-if="canDonate" @donated="updateData" :contractAddress="contractAddress" :issueId="issueId"></donate-action>

            <approve-action v-if="canApprove" @approved="updateData" :contractAddress="contractAddress" :issueId="issueId" :possibleReviewers="possibleReviewers"></approve-action>

            <lock-action v-if="canLock" @locked="updateData" :contractAddress="contractAddress" :issueId="issueId" :issue="issue"></lock-action>

            <unlock-action v-if="canUnlock" @unlocked="updateData" :contractAddress="contractAddress" :issueId="issueId" :issue="issue"></unlock-action>

            <finish-development-action v-if="canFinishDevelopment" @finishedDevelopment="updateData" :contractAddress="contractAddress" :issueId="issueId" :issue="issue"></finish-development-action>

            <review-action v-if="canReview" @reviewed="updateData" :contractAddress="contractAddress" :issueId="issueId" :issue="issue"></review-action>

            <reset-action v-if="canReset" @reset="updateData" :contractAddress="contractAddress" :issueId="issueId" :issue="issue"></reset-action>

            <delete-action v-if="canDelete" @deleted="updateData" :contractAddress="contractAddress" :issueId="issueId" :issue="issue"></delete-action>
          </div>
        </div>
        <issue-detail-gitlab-header :issue="issue"></issue-detail-gitlab-header>
        <hr>
        <div class="row">
          <div class="col" v-html="markdownDescription">
          </div>
        </div>
      </div>
      <hr>
      <issue-detail-contract :contractIssue="contractIssue"></issue-detail-contract>
    </div>
  </div>
</template>

<script>
import ErrorContainer from "@/api/errorContainer";
import Navigation from "@/components/Navigation";
import Modal from "@/components/Modal";
import IssueDetailContract from "@/components/IssueDetailContract";
import IssueDetailGitlabHeader from "@/components/IssueDetailGitlabHeader";
import DonateAction from "@/components/actions/DonateAction";
import ApproveAction from "@/components/actions/ApproveAction";
import LockAction from "@/components/actions/LockAction";
import UnlockAction from "@/components/actions/UnlockAction";
import FinishDevelopmentAction from "@/components/actions/FinishDevelopmentAction";
import ReviewAction from "@/components/actions/ReviewAction";
import ResetAction from "@/components/actions/ResetAction";
import DeleteAction from "@/components/actions/DeleteAction";
import { Gitlab } from "@/api/gitlab";
import { Backend } from "@/api/backend";
import marked from "marked";

export default {
  name: "IssueDetail",
  components: {
    Modal,
    Navigation,
    IssueDetailContract,
    IssueDetailGitlabHeader,
    DonateAction,
    ApproveAction,
    LockAction,
    UnlockAction,
    FinishDevelopmentAction,
    ReviewAction,
    ResetAction,
    DeleteAction
  },
  props: {
    projectId: [String, Number],
    issueId: [String, Number]
  },
  data: function() {
    return {
      profile: null,
      project: null,
      issue: null,
      contractIssue: null,
      contractAddress: null,
      isMaintainer: false,
      possibleReviewers: null
    };
  },
  computed: {
    userAddress: function() {
      // TODO consider using web3 address instead of profile
      return this.profile ? this.profile.address : null;
    },
    markdownDescription: function() {
      return marked(this.issue.description);
    },
    canDonate: function() {
      if (this.contractIssue != null) {
        return this.contractIssue.lifecycleStatus != "COMPLETED";
      }
      return true;
    },
    canApprove: function() {
      return (
        this.isMaintainer &&
        this.contractIssue &&
        this.contractIssue.lifecycleStatus == "DEFAULT"
      );
    },
    canLock: function() {
      return (
        this.contractIssue && this.contractIssue.lifecycleStatus == "APPROVED"
      );
    },
    canUnlock: function() {
      return (
        this.contractIssue &&
        this.contractIssue.lifecycleStatus == "LOCKED" &&
        (this.isMaintainer || this.userAddress == this.contractIssue.developer)
      );
    },
    canFinishDevelopment: function() {
      return (
        this.contractIssue &&
        this.contractIssue.lifecycleStatus == "LOCKED" &&
        this.userAddress == this.contractIssue.developer
      );
    },
    canReview: function() {
      return (
        this.contractIssue &&
        this.contractIssue.lifecycleStatus == "DEVELOPED" &&
        this.contractIssue.reviewers.includes(this.userAddress)
        // TODO check contractIssue.reviewStatus? Should reviewer be able to change status anytime?
      );
    },
    canReset: function() {
      return (
        this.isMaintainer &&
        this.contractIssue &&
        this.contractIssue.lifecycleStatus != "COMPLETED"
      );
    },
    canDelete: function() {
      return (
        this.isMaintainer &&
        this.contractIssue &&
        this.contractIssue.lifecycleStatus != "COMPLETED"
      );
    }
  },
  created: function() {
    this.updateData();
  },
  methods: {
    setIssue: function(issue) {
      this.issue = issue;
    },
    setPossibleReviewers: function(possibleReviewers, projectMembers) {
      // TODO replace this with computed property
      this.possibleReviewers = possibleReviewers
        .map(reviewer => {
          const member = projectMembers.find(m => m.id == reviewer.gitlabId);
          if (member) {
            return {
              address: reviewer.address,
              gitlabId: reviewer.gitlabId,
              username: member.username
            };
          }
        })
        // filter elements where member was not found > nothing returned.
        .filter(x => x !== undefined);
    },
    setContractAddress: function(address) {
      this.contractAddress = address;
    },
    setContractIssue: function(issue) {
      this.contractIssue = issue;
    },
    setIsMaintainer: function(val) {
      this.isMaintainer = val;
    },
    setProject: function(project) {
      this.project = project;
    },
    updateData: function() {
      const gitlab = new Gitlab();
      const backend = new Backend();

      this.$emit("isLoading", true);

      Promise.all([
        gitlab.getProjectIssue(this.projectId, this.issueId),
        gitlab.getProjectsOwned(),
        gitlab.getProjectMembers(this.projectId),
        backend.getProjectReviewers(this.projectId),
        backend.getProjectIssue(this.projectId, this.issueId).catch(error => {
          console.log(
            "Could not get issue-details from backend/chain. Maybe this issue is not yet tracked"
          );
          // ignore 404 (issue not in contract)
          if (error.response.status != 404) throw error;
        }),
        backend.getProfile(this.projectId),
        backend.getProject(this.projectId),
        gitlab.getProject(this.projectId)
      ])
        .then(results => {
          const issue = results[0];
          const ownedProjects = results[1];
          const projectMembers = results[2];
          const possibleReviewers = results[3];
          const contractIssue = results[4];
          const profile = results[5];
          const backendProject = results[6];
          const gitlabProject = results[7];

          this.setIssue(issue);
          if (ownedProjects.find(project => project.id == this.projectId)) {
            this.setIsMaintainer(true);
          }
          this.setPossibleReviewers(possibleReviewers, projectMembers);
          this.setContractIssue(contractIssue);
          this.setProfile(profile);
          this.setContractAddress(backendProject.address);
          this.setProject(gitlabProject);
        })
        .catch(error => ErrorContainer.add(error))
        .then(() => this.$emit("isLoading", false));
    },
    setProfile: function(newProfile) {
      this.profile = newProfile;
    },
    onIsLoadingChanged: function(isLoading) {
      this.$emit("isLoading", isLoading);
    }
  }
};
</script>

<style>
</style>