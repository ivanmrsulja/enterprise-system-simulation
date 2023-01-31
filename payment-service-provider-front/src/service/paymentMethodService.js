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

  addAccountDetails(payload) {
    return axios.post(
      `${import.meta.env.VITE_BASE_PATH}/payment-methods/business-account`,
      payload
    );
  }

  getAccountDetails(merchantId, paymentMethod) {
    return axios.get(
      `${
        import.meta.env.VITE_BASE_PATH
      }/payment-methods/business-account/${merchantId}/${paymentMethod}`
    );
  }
}

export const paymentMethodService = new PaymentMethodService();
