import SETTINGS from "./settings.js"
const ClientOAuth2 = require("client-oauth2");

console.log(SETTINGS);
const GitlabOAuth = new ClientOAuth2(SETTINGS.oauthConfig);

var UserSession = {
    token: null,
    loggedIn: false
};

export { UserSession, GitlabOAuth };