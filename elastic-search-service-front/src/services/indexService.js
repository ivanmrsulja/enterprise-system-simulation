import axios from "axios";

class IndexService {
    indexApplication(formData) {
        return axios.post("http://localhost:8001/api/index", formData, {
            headers: {
                "Content-Type": "multipart/form-data",
            },
        });
    }
}

export const indexService = new IndexService();
