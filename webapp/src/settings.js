const SETTINGS = {
    oauthConfig: {
        clientId: "f30a23841e0b180caeb04cb51eb8e442d7ce64727fd1e0929dbdbd9e33d6d25b",
		clientSecret:
            "2a753e9f6fa7458801b0dc30cb74031c2251dee0368f9902f022695b500a2ac5",
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