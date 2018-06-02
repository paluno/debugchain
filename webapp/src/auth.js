import SETTINGS from "./settings"
import ClientOAuth2 from "client-oauth2";

const GitlabOAuth = new ClientOAuth2(SETTINGS.oauthConfig);

const UserSession = {
    token: null,
    loggedIn: false
};

export { UserSession, GitlabOAuth };