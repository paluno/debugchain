import SETTINGS from "../settings"
import { UserSession } from "../auth.js";
const GitlabApis = require("gitlab-api-wrapper");

export default {
    getClient() {
        return GitlabApis({
            base_url: SETTINGS.gitlabConnection.url,
            private_token: UserSession.state.accessToken,
            timeout: 3000
        });
    }
}