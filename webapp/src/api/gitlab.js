import SETTINGS from "@/settings"
import UserSession from "@/auth.js";
import GitlabApis from "gitlab-api-wrapper";
import Axios from "axios";

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
            baseURL: SETTINGS.gitlabConnection.url +'/api/v3/',
            timeout: 3000,
            headers: {
                Authorization: "Bearer " + UserSession.state.accessToken
            }
        });

        // TODO consider handling common errors: 404, 500
        this._client.interceptors.response.use(
            // return data directly: we usually dont need detailed response information on success
            response => response.data,
            error => Promise.reject(this._setLocalizedErrorMessage(error)));
    }

    _setLocalizedErrorMessage(error) {
        let msg;
        if (error.response) {
            // The request was made and the server responded with an error status code
            switch (error.response.status) {
                case 404:
                    msg = `The requested resource does not exist. Try going back to the last page.`;
                    break;
                case 500:
                    msg = `An internal error occured. We're sorry, please try again later.`;
                    break;
                default:
                    msg = `The server returned an error: ${error.response.status} ${error.response.statusText}`;
            }
        } else if (error.request) {
            // The request was made but no response was received
            msg = `The server did not respond. Try checking your internet connection.`;
        }
        error.userMessage = msg;
        return error;
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
