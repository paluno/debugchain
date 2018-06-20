const SETTINGS = {
    oauthConfig: {
        clientId: "b173bf67743561c91b84952e575ed3229feb9b99fe7e7c98d492488e427d8327",
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