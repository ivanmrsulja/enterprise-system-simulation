<template>
  <v-container>
    <v-row align="center" justify="center">
      <v-col cols="6" md="6" class="header-col">
        <h1>Hello {{ companyName }}! Here you can configure payment methods</h1>
      </v-col>
    </v-row>

    <v-card
      class="mx-auto"
      max-width="344"
      outlined
      v-for="paymentMethod in allPaymentMethods"
      style="margin: 20px"
      min-width="40%"
      elevation="5"
    >
      <v-list-item three-line>
        <div class="text-overline mb-4">{{ paymentMethod }}</div>
        <v-avatar
          tile
          size="80"
          :color="
            merchantPaymentMethods.includes(paymentMethod) ? '#04aa6d' : 'grey'
          "
          style="float: right"
        ></v-avatar>
        <v-list-item-title class="text-h5 mb-1">
          {{ paymentMethod }}
        </v-list-item-title>

        <v-list-item-subtitle>{{
          merchantPaymentMethods.includes(paymentMethod)
            ? "This payment method is successfully integrated in your application."
            : "Click 'Add' to integrate this payment method in your application"
        }}</v-list-item-subtitle>
      </v-list-item>

      <v-card-actions>
        <v-btn
          outlined
          rounded
          text
          :color="
            merchantPaymentMethods.includes(paymentMethod) ? 'error' : '#04aa6d'
          "
          @click="
            updatePaymentMethods(
              paymentMethod,
              merchantPaymentMethods.includes(paymentMethod) ? true : false
            )
          "
        >
          {{
            merchantPaymentMethods.includes(paymentMethod)
              ? "Remove from application"
              : "Add"
          }}
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-container>
</template>

<script>
import { computed, ref, onMounted, inject } from "vue";
import { authService } from "../service/authService";
import { paymentMethodService } from "../service/paymentMethodService";

export default {
  name: "PaymentMethodsView",
  setup() {
    const merchantPaymentMethods = ref([]);
    const allPaymentMethods = ref([]);
    const companyName = ref("");

    let decodedToken = authService.getDecodedToken();

    companyName.value = decodedToken.companyName;

    paymentMethodService.getAllPaymentMethods().then((response) => {
      allPaymentMethods.value = response.data;
    });

    paymentMethodService
      .getAllPaymentMethodsForMerchant(decodedToken.sub)
      .then((response) => {
        merchantPaymentMethods.value = response.data;
      });

    const updatePaymentMethods = (paymentMethod, remove) => {
      if (remove) {
        merchantPaymentMethods.value = merchantPaymentMethods.value.filter(
          (el) => {
            return el !== paymentMethod;
          }
        );
      } else {
        merchantPaymentMethods.value.push(paymentMethod);
      }
      let payload = {
        id: decodedToken.userId,
        paymentMethods: merchantPaymentMethods.value,
      };
      paymentMethodService.setPaymentMethodsForMerchant(payload);
    };

    return {
      merchantPaymentMethods,
      allPaymentMethods,
      companyName,
      updatePaymentMethods,
    };
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
