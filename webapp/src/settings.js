const SETTINGS = {
    oauthConfig: {
        clientId: "acb6f1458c7cc4b8fda433f836186c9af1115581389021e9e3aa7536bf8d4f0a",
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