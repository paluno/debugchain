import SETTINGS from "@/settings"
import UserSession from "@/auth.js";
import GitlabApis from "gitlab-api-wrapper";
import Axios from "axios";
import Localization from "@/api/errorLocalization";

export default {
    getClient() {
        return GitlabApis({
            base_url: SETTINGS.gitlabConnection.url,
            private_token: UserSession.state.accessToken,
            timeout: 3000
        });
    }
}

// TODO remove getClient() export, make class export default
export class Gitlab {

    constructor() {
        this._client = Axios.create({
            baseURL: SETTINGS.gitlabConnection.url + '/api/v3/',
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
                error.userMessage = Localization.getForAxios(error);
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

    getProjectIssues(projectId) {
        return this._client
            .get(`/projects/${projectId}/issues`);
    }

    getProjectIssue(projectId, issueId) {
        return this._client
            .get(`/projects/${projectId}/issues/${issueId}`);
    }

    getProjectsOwned() {
        return this._client
            .get(`/projects/owned`);
    }

    getProjectMembers(projectId) {
        return this._client
            .get(`/projects/${projectId}/members`);
    }

    getCurrentUser() {
        return this._client
            .get(`/user`);
    }
}
