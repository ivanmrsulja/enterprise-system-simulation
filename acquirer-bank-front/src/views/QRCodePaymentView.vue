<template>
  <div>
    <qrcode-vue :value="value" :size=150 />
    <button @click="simulateScanning()">Simulate qr code scanning</button>
  </div>
</template>

<script>
import { ref, onMounted } from "vue";
import { useRoute } from "vue-router";
import { paymentService } from "../services/paymentService";
import { qrCodeService } from "../services/qrCodeService";
import QrcodeVue from 'qrcode.vue'

export default {
  components: { QrcodeVue },
  setup() {
    const merchantId = ref("");
    const merchantName = ref("");
    const paymentAmount = ref(0);
    const route = useRoute();
    const qrCode = ref("");

    onMounted(async () => {
      try {
        const response = await paymentService.getTransactionDetailsForQrCode(route.params.paymentId);
        merchantId.value = response.data.merchantId;
        merchantName.value = response.data.merchantName;
        paymentAmount.value = response.data.amount;
      } catch(err) {
        console.log(err);
        return;
      }

      const qrRequest = createQrRequestBody();
      try {
        const response = await qrCodeService.generateQr(qrRequest);
        qrCode.value = response.data.i;
      } catch(err) {
        console.log(err);
      }
    });

    const simulateScanning = () => {
      // Create object and call appropriate api
    }

    const createQrRequestBody = () => {
      return {
        K: "PR",
        V: "01",
        C: "1",
        R: merchantId.value,
        N: `Payment for ${merchantName.value} on payment id: ${route.params.paymentId}`,
        I: `RSD${paymentAmount.value}`,
        SF: "129"
      };
    }

    return { qrCode };
  }
};
</script>