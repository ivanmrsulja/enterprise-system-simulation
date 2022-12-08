<template>
  <main>
    <h2>Payment to {{ merchantName }}</h2>
    <ul>
      <li>Amount: {{ paymentAmount }} EUR</li>
      <li>Date: {{ dateTime }}</li>
    </ul>
    <CreditCardInput />
  </main>
</template>

<script>
import { ref, onMounted } from "vue";
import { useRoute } from "vue-router";
import { paymentService } from "../services/paymentService";
import CreditCardInput from "../components/creditcard/CreditCardInput.vue";

export default {
  name: "CreditCardPaymentView",
  components: { CreditCardInput },
  setup() {
    const paymentAmount = ref(0);
    const merchantName = ref("");
    const today = new Date();
    const date =
      today.getDate() +
      "." +
      (today.getMonth() + 1) +
      "." +
      today.getFullYear() +
      ".";
    const time =
      today.getHours() + ":" + today.getMinutes() + ":" + today.getSeconds();
    const dateTime = date + " " + time;

    const route = useRoute();

    onMounted(() => {
      paymentService
        .getTransactionDetails(route.params.paymentId)
        .then((response) => {
          paymentAmount.value = response.data.amount;
          merchantName.value = response.data.merchantName;
        });
    });

    return { merchantName, paymentAmount, dateTime };
  },
};
</script>
