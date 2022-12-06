import axios from "axios";

class PaymentService {
  getRedirectToBank(merchantOrderId, transactionId, method) {
    return axios.get(
      `${
        import.meta.env.VITE_BASE_PATH
      }/bank-payment/request-redirect-to-bank/${merchantOrderId}/${transactionId}/${method}`
    );
  }
}

export const paymentService = new PaymentService();
