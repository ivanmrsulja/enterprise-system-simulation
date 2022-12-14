import axios from "axios";

class PaymentService {
  getTransactionDetails(paymentId) {
    return axios.get(
      `${import.meta.env.VITE_BASE_PATH}/transaction-details/${paymentId}`
    );
  }

  getTransactionDetailsForQrCode(paymentId) {
    return axios.get(
      `${import.meta.env.VITE_BASE_PATH}/transaction-details-qr/${paymentId}`
    );
  }

  payWithQrCode(paymentDto) {
    return axios.patch(
      `${import.meta.env.VITE_BASE_PATH}/credit-card-payment`,
      paymentDto
    );
  }

}

export const paymentService = new PaymentService();
