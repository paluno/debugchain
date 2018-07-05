const SETTINGS = {
    oauthConfig: {
        clientId: process.env.VUE_APP_OAUTH_CLIENT_ID,
        accessTokenUri: process.env.VUE_APP_OAUTH_TOKEN_URI,
        authorizationUri: process.env.VUE_APP_OAUTH_AUTH_URI,
        redirectUri: process.env.VUE_APP_OAUTH_REDIRECT_URI,
        scopes: ["api"]
    },
    rpc: {
        useHttpProviderUrl: process.env.VUE_APP_WEB3_USE_HTTPPROVIDER_URL
    },
    gitlabConnection: {
        url: process.env.VUE_APP_GITLAB_URI,
    }
};

export default SETTINGS;