import axios from "axios"

class QrCodeService {
  validateQr(qrRequest) {
    return axios.post(
      `${import.meta.env.VITE_BASE_PATH_QR}/validate`
    );
  }

  generateQr(qrRequest) {
    return axios.post(
      `${import.meta.env.VITE_BASE_PATH_QR}/generate`
    );
  }
}

export const qrCodeService = new QrCodeService();