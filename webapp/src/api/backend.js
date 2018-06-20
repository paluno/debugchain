import UserSession from "@/auth.js";
import Axios from "axios";

export default {
    getClient() {
        return Axios.create({
            baseURL: 'http://localhost:9000/api/',
            timeout: 3000,
            headers: {
                Authorization: "Bearer " + UserSession.state.accessToken
            }
        });
    }
}