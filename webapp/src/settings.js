const SETTINGS = {
    oauthConfig: {
        clientId: "a3d193576e3d22d993e3e92dcc19485ad9ba32e44e9677cf837a87472ed7e8b6",
        clientSecret:
            "d4a59cadd718fe22f72133ed2dcf3290fd9c7ca8551649abd6f5d075e5921de",
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