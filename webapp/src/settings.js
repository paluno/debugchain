const SETTINGS = {
    oauthConfig: {
        clientId: process.env.VUE_APP_OAUTH_CLIENT_ID,
        accessTokenUri: "http://localhost:80/oauth/token",
        authorizationUri: "http://localhost:80/oauth/authorize",
        redirectUri: "http://localhost:9000/login",
        scopes: ["api"]
    },
    gitlabConnection: {
        url: "http://localhost",
    }

};

export default SETTINGS;