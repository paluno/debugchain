const SETTINGS = {
    oauthConfig: {
        clientId: "2642be6246ec17f3a9fcebb0571f5db004fa2b4be128e49633964f85924a941e",
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