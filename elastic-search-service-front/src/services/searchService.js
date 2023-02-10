import axios from "axios";

class SearchService {
    search(payload) {
        return axios.post(
            `${import.meta.env.VITE_BASE_PATH}/search/boolean`,
            payload
        );
    }
}

export const searchService = new SearchService();
