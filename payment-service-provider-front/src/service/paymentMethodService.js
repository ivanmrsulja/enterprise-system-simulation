import axios from "axios";

class PaymentMethodService {
  getAllPaymentMethods() {
    return axios.get(`${import.meta.env.VITE_BASE_PATH}/payment-methods/all`);
  }

  getAllPaymentMethodsForMerchant(merchantId) {
    return axios.get(
      `${import.meta.env.VITE_BASE_PATH}/payment-methods/${merchantId}/all`
    );
  }

  setPaymentMethodsForMerchant(payload) {
    return axios.put(
      `${import.meta.env.VITE_BASE_PATH}/payment-methods`,
      payload
    );
  }
}

export const paymentMethodService = new PaymentMethodService();
