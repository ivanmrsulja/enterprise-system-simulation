import axios from "axios"

class QrCodeService {
  validateQr(qrRequest) {
    return axios.post(
      `${import.meta.env.VITE_BASE_PATH_QR}/qr-code-validate`,
      qrRequest
    );
  }

  generateQr(qrRequest) {
    return axios.post(
      `${import.meta.env.VITE_BASE_PATH_QR}/qr-code-generate`,
      qrRequest
    );
  }
}

export const qrCodeService = new QrCodeService();