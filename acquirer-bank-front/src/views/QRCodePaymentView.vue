<template>
  <div>
    <center>
      <img :src="'data:image/png;base64,' + qrCode" />
      <br />
      <h1 class="typewriter">
        Scan this qr code in order to pay for services you want to buy
      </h1>
      <br />
      <button class="pay-btn" @click="simulateScanning()">
        Simulate qr code scanning
      </button>
    </center>
  </div>
</template>

<script>
import { ref, onMounted } from "vue";
import { useRoute } from "vue-router";
import { paymentService } from "../services/paymentService";
import { qrCodeService } from "../services/qrCodeService";
import QrcodeVue from "qrcode.vue";

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
        const response = await paymentService.getTransactionDetailsForQrCode(
          paymentId
        );
        merchantId.value = response.data.merchantId;
        merchantName.value = response.data.merchantName;
        paymentAmount.value = response.data.amount;
      } catch (err) {
        console.log(
          `Error occurred while loading transaction details for payment id: ${paymentId}`
        );
        console.log(err);
        return;
      }

      const qrRequest = createQrRequestBody();
      try {
        await qrCodeService.validateQr(qrRequest);
      } catch (err) {
        console.log(err);
        return;
      }

      try {
        const response = await qrCodeService.generateQr(qrRequest);
        qrCode.value = response.data.i;
      } catch (err) {
        console.log(
          `Error occurred while generating qr code for data: ${qrRequest}`
        );
        console.log(err);
        return;
      }
    });

    const simulateScanning = async () => {
      /**
       * When paying make sure that:
       * @pan
       * @securityCode
       * @cardHolderName
       * @expiryDate
       * are all valid at table @bank_accounts
       */
      console.log(
        "Simulating scanning and and sending request to acquirer bank.."
      );

      const paymentDto = {
        pan: "1111111231",
        securityCode: "456",
        cardHolderName: "Jovan Jovanovic",
        expiryDate: "05/23",
        merchantOrderId: +route.params.merchantOrderId,
        paymentId: +route.params.paymentId,
      };

      paymentService
        .payWithQrCode(paymentDto)
        .then((res) => {
          console.log(res);
          window.location.href = res.data.redirectUrl;
        })
        .catch((err) => {
          console.log(err);
        });
    };

    const createQrRequestBody = () => {
      return {
        K: "PR",
        V: "01",
        C: "1",
        R: merchantId.value,
        N: merchantName.value,
        I: `RSD${formatAmout()}`,
        SF: "129",
        S: `Payment for ${merchantName.value} on payment id ${route.params.paymentId}`,
      };
    };

    const formatAmout = () => {
      return paymentAmount.value.toFixed(2).toString().replace(".", ",");
    };

    return { qrCode, simulateScanning };
  },
};
</script>

<style scoped>
.pay-btn {
  background-image: linear-gradient(
    92.88deg,
    #455eb5 9.16%,
    #5643cc 43.89%,
    #673fd7 64.72%
  );
  border-radius: 8px;
  border-style: none;
  box-sizing: border-box;
  color: #ffffff;
  cursor: pointer;
  flex-shrink: 0;
  font-family: "Inter UI", "SF Pro Display", -apple-system, BlinkMacSystemFont,
    "Segoe UI", Roboto, Oxygen, Ubuntu, Cantarell, "Open Sans", "Helvetica Neue",
    sans-serif;
  font-size: 24px;
  font-weight: 700;
  padding: 1.3em 4.6rem;
  text-align: center;
  text-shadow: rgba(0, 0, 0, 0.25) 0 3px 8px;
  transition: all 0.5s;
  user-select: none;
  -webkit-user-select: none;
  touch-action: manipulation;
}

.pay-btn:hover {
  box-shadow: rgba(80, 63, 205, 0.5) 0 1px 30px;
  transition-duration: 0.1s;
}
</style>
