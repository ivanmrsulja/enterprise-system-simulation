import jwtDecode from "jwt-decode";
import axios from "axios";

class AuthService {
    login(fetchToken) {
        return axios.get(
            `${import.meta.env.VITE_BASE_PATH}/authenticate/${fetchToken}`
        );
    }

    setToken(token) {
        sessionStorage.setItem("jwt", token);
    }

    roles() {
        let jwt = sessionStorage.getItem("jwt");
        if (jwt) {
            let token = jwtDecode(jwt);
            return token.roles;
        } else return "";
    }

    getDecodedToken() {
        let jwt = sessionStorage.getItem("jwt");
        if (jwt) {
            return jwtDecode(jwt);
        } else return undefined;
    }

    userLoggedIn() {
        return sessionStorage.getItem("jwt") !== null;
    }

    userId() {
        let jwt = sessionStorage.getItem("jwt");
        if (jwt) {
            let token = jwtDecode(jwt);
            return token.userId;
        } else return -1;
    }
}

export const authService = new AuthService();
