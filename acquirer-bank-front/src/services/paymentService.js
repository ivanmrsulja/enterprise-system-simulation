import axios from "axios";

class PaymentService {
  getTransactionDetails(paymentId) {
    return axios.get(
      `${import.meta.env.VITE_BASE_PATH}/transaction-details/${paymentId}`
    );
  }
}

export const paymentService = new PaymentService();
