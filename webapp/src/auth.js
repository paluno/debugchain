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
        this.setCookie(newToken, 1);
    },
    logout: function () {
        this.state.accessToken = null;
        this.state.loggedIn = false;
        this.deleteCookie();
    },
    setCookie: function (value, expireIn) {
        var d = new Date();
        d.setTime(d.getTime() + (expireIn * 24 * 60 * 60 * 1000)); // Wann cookie abläuft
        var expires = "expires=" + d.toUTCString();
        document.cookie = "debugchain=" + value + ";" + expires + ";"; // cookie setzen
    },
    deleteCookie: function () {
        this.setCookie("", -1); // Cookie wird gelöscht, wenn das expire-datum abgelaufen ist
    },
    getCookie: function () {
        var name = "debugchain=";
        var ca = document.cookie.split(';');
        for (var i = 0; i < ca.length; i++) {
            var c = ca[i];
            while (c.charAt(0) == ' ') {
                c = c.substring(1);
            }
            if (c.indexOf(name) == 0) {
                return c.substring(name.length, c.length);
            }
        }
        return "";
    },
    checkCookie: function () {
        var token = this.getCookie();
        if (token != "") {
            this.state.accessToken = token;
            this.state.loggedIn = true;
        }
    }
};

export { UserSession as default, UserSession, GitlabOAuth };