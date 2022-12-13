<template>
  <div>
    <center>
      <img :src="'data:image/png;base64,' + qrCode" />
      <h1 class="typewriter">
        Scan this qr code in order to pay for services you want to buy
      </h1>
      <button @click="simulateScanning()">Simulate qr code scanning</button>
    </center>
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
      const paymentId = route.params.paymentId;
      try {
        const response = await paymentService.getTransactionDetailsForQrCode(paymentId);
        merchantId.value = response.data.merchantId;
        merchantName.value = response.data.merchantName;
        paymentAmount.value = response.data.amount;
      } catch(err) {
        console.log(`Error occurred while loading transaction details for payment id: ${paymentId}`);
        console.log(err);
        return;
      }

      const qrRequest = createQrRequestBody();
      try {
        await qrCodeService.validateQr(qrRequest);
      } catch(err) {
        console.log(err)
        return;
      }

      try {
        const response = await qrCodeService.generateQr(qrRequest);
        qrCode.value = response.data.i;
      } catch(err) {
        console.log(`Error occurred while generating qr code for data: ${qrRequest}`);
        console.log(err)
        return;
      }
    });

    const simulateScanning = () => {
      // TODO Create object and call appropriate api
    }

    const createQrRequestBody = () => {
      return {
        K: "PR",
        V: "01",
        C: "1",
        R: merchantId.value,
        N: `Payment for ${merchantName.value} on payment id ${route.params.paymentId}`,
        I: `RSD${formatAmout()}`,
        SF: "129"
      };
    }

    const formatAmout = () => {
      return paymentAmount.value.toFixed(2).toString().replace(".", ",");
    }

    return { qrCode };
  }
};
</script>