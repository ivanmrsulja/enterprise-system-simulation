import axios from "axios";

class PaymentService {
  getRedirectToBank(merchantOrderId, transactionId, method) {
    return axios.get(
      `${
        import.meta.env.VITE_BASE_PATH
      }/bank-payment/request-redirect-to-bank/${merchantOrderId}/${transactionId}/${method}`
    );
  }
  getPayment(customPayment) {
    return axios.post(
      `${import.meta.env.VITE_BASE_PATH}/payments/pay`,
      customPayment
    );
  }
}

export const paymentService = new PaymentService();
