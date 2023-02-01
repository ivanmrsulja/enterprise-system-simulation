import axios from "axios";

class PaymentService {
  getRedirectToBank(merchantOrderId, transactionId, method) {
    return axios.get(
      `${import.meta.env.VITE_BASE_PATH
      }/bank-payment/request-redirect-to-bank/${merchantOrderId}/${transactionId}/${method}`
    );
  }
  getPayment(customPayment) {
    return axios.post(
      `${import.meta.env.VITE_BASE_PATH}/payments/pay`,
      customPayment
    );
  }
  getBitcoinHash() {
    return axios.get(
      `${import.meta.env.VITE_BASE_PATH}/payments/getBitcoinHash`
    );
  }
  getSubscription(customPayment) {
    return axios.post(
      `${import.meta.env.VITE_BASE_PATH}/payments/subscription`,
      customPayment
    );
  }
}

export const paymentService = new PaymentService();
