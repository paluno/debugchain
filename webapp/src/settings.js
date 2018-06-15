const SETTINGS = {
    oauthConfig: {
        clientId: "d1b13811b78e39f0849960524f7046d8466eeed677ce65ffc6d1a6695f146396",
        clientSecret:
            "a5ce5d651827437ca5f25caeec919bf735e2733ac3152578aba95b5bc191ce57",
        accessTokenUri: "http://localhost:80/oauth/token",
        authorizationUri: "http://localhost:80/oauth/authorize",
        redirectUri: "http://localhost:8080/login",
        scopes: ["api"]
    },
    gitlabConnection: {
        url: "http://localhost",
    }
};

export default SETTINGS;