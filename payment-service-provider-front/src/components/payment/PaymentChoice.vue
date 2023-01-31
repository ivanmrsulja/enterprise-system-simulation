<template>
  <v-container>
    <v-row justify="center">
      <v-col cols="6" md="6" class="header-col">
        <h1>Choose one paying method from the list below</h1>
      </v-col>
    </v-row>

    <v-card
      class="mx-auto"
      max-width="344"
      outlined
      v-for="paymentMethod in merchantPaymentMethods"
      style="margin: 20px"
      min-width="40%"
      elevation="5"
    >
      <v-list-item three-line>
        <div class="text-overline mb-4">{{ paymentMethod }}</div>
        <v-avatar tile size="100" color="grey" style="float: right">
          <img
            width="100"
            height="100"
            style="object-fit: cover"
            :src="
              '/src/assets/images/' + paymentMethod.replace(' ', '') + '.png'
            "
            :alt="paymentMethod"
        /></v-avatar>
        <v-list-item-title class="text-h5 mb-1">
          {{ paymentMethod }}
        </v-list-item-title>

        <v-list-item-subtitle
          >Click 'Pay' to make payment using this payment
          method.</v-list-item-subtitle
        >
      </v-list-item>

      <v-card-actions>
        <v-btn
          outlined
          rounded
          text
          color="#04aa6d"
          @click="makePayment(paymentMethod)"
          >Pay</v-btn
        >
      </v-card-actions>
    </v-card>
  </v-container>
</template>

<script>
import { ref, onMounted } from "vue";
import { useRoute } from "vue-router";
import { paymentMethodService } from "../../service/paymentMethodService";
import { paymentService } from "../../service/paymentService";

export default {
  name: "PaymentChoice",
  setup() {
    const merchantPaymentMethods = ref([]);
    const route = useRoute();

    onMounted(() => {
      paymentMethodService
        .getAllPaymentMethodsForMerchant(route.params.merchantId)
        .then((response) => {
          merchantPaymentMethods.value = response.data;
        });
    });

    const makePayment = (method) => {
      let methodUrlLabel = method.replace(" ", "-").toLowerCase();
      if (methodUrlLabel === "credit-card" || methodUrlLabel === "qr-code") {
        paymentService
          .getRedirectToBank(
            route.params.merchantOrderId,
            route.params.transactionId,
            methodUrlLabel
          )
          .then((response) => {
            window.open(response.data.paymentUrl, "_blank");
          });
      } else {
        // TODO: Ovdje dodati da se genericki odradi placanje 3rd party servisom
        let payment = {
          paymentMethod: methodUrlLabel,
          currency: "USD",
          description: "hallo",
          merchantOrderId: route.params.merchantOrderId,
          transactionId: route.params.transactionId,
        };
        paymentService.getPayment(payment).then((response) => {
          console.log(response.data);
          if (methodUrlLabel === "paypal") {
            window.open(response.data);
          } else if (methodUrlLabel === "bitcoin") {
            var intervalId = null;
            var varName = function () {
              paymentService.getBitcoinHash().then((response) => {
                let result = response.data;
                console.log("checkout transaction hash result: " + result);
                if (result) {
                  clearInterval(intervalId);
                  alert("Transaction hash is : " + result);
                  window.open(
                    "http://www.bonita-sajt.com:8080/bonita/apps/userAppBonita/transaction-successful/"
                  );
                }
              });
            };

            intervalId = setInterval(varName, 60000);
          }
        });
      }
    };

    return { merchantPaymentMethods, makePayment };
  },
};
</script>

<style scoped>
.header-col {
  text-align: center;
  margin-top: 2%;
  margin-bottom: 2%;
}
</style>
