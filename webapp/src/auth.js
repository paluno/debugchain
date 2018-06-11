import SETTINGS from "./settings"
import ClientOAuth2 from "client-oauth2";
import Storage from "./webStorage"

const GitlabOAuth = new ClientOAuth2(SETTINGS.oauthConfig);

const UserSession = {
    state: {
        accessToken: null,
        loggedIn: false
    },
    login: function (newToken) {
        if (newToken == null) {
            this.logout();
        }
        this.state.accessToken = newToken;
        this.state.loggedIn = true;
    },
    logout: function () {
        this.state.accessToken = null;
        this.state.loggedIn = false;
    }
};

export { UserSession, GitlabOAuth };