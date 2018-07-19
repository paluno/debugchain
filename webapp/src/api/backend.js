import UserSession from "@/auth.js";
import Axios from "axios";
import Localization from "@/api/errorLocalization";

export default {
    getClient() {
        return Axios.create({
            baseURL: '/api/',
            timeout: 3000,
            headers: {
                Authorization: "Bearer " + UserSession.state.accessToken
            }
        });
    }
}

// TODO remove getClient() export, make class export default
export class Backend {

    constructor() {
        this._client = Axios.create({
            baseURL: '/api/',
            timeout: 3000,
            headers: {
                Authorization: "Bearer " + UserSession.state.accessToken
            }
        });

        // TODO consider handling common errors: 404, 500
        this._client.interceptors.response.use(
            // return data directly: we usually dont need detailed response information on success
            response => response.data,
            error => {
                error.userMessage = Localization.getForAxios(error, "Debug-Chain");
                return Promise.reject(error);
            });
    }

    getProjects() {
        return this._client
            .get(`/projects`);
    }

    getProject(projectId) {
        return this._client
            .get(`/projects/${projectId}`);
    }

    createProject(projectId, address) {
        return this._client
            .post(`/projects/`, {
                address: address,
                gitlabId: projectId
            });
    }

    getProjectIssues(projectId) {
        return this._client
            .get(`/projects/${projectId}/issues`);
    }

    getProjectIssue(projectId, issueId) {
        return this._client
            .get(`/projects/${projectId}/issues/${issueId}`);
    }

    getProjectReviewers(projectId) {
        return this._client
            .get(`/projects/${projectId}/reviewers`);
    }

    getProfile(projectId) {
        return this._client
            .get(projectId === undefined
                ? `/profile`
                : `/profile/withdrawals/${projectId}`);
    }

    setProfile(profile) {
        return this._client
            .post("/profile", profile);
    }

    getProfileMemberships() {
        return this._client
            .get("/profile/memberships");
    }

    getProfileAssignedIssues() {
        return this._client
            .get("/profile/assignedIssues");
    }

    setProfileMemberships(memberships) {
        return this._client
            .post("/profile/memberships", memberships);
    }
}
