import axios from "axios";

class DownloadService {
    downloadPDF(documentName, documentType) {
        axios
            .get(
                `${
                    import.meta.env.VITE_BASE_PATH
                }/download?fileName=${documentName}`,
                {
                    responseType: "blob",
                }
            )
            .then((response) => {
                this.initialteDownload(response, documentType);
            })
            .catch(console.error);
    }

    initialteDownload(response, documentType, extension) {
        const blob = new Blob([response.data], {
            type: "application/" + extension,
        });
        const link = document.createElement("a");
        link.href = URL.createObjectURL(blob);
        link.download = documentType + ".pdf";
        link.click();
        URL.revokeObjectURL(link.href);
    }
}

export const downloadService = new DownloadService();
