import { createRouter, createWebHistory } from "vue-router";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/",
      name: "home",
      component: () => import("../views/HomeView.vue"),
    },
    {
      path: "/payment/:merchantOrderId/:paymentId/credit-card",
      name: "credit-card-payment",
      component: () => import("../views/CreditCardPaymentView.vue"),
    },
    {
      path: "/payment/:merchantOrderId/:paymentId/qr-code",
      name: "qr-code-payment",
      component: () => import("../views/QRCodePaymentView.vue"),
    },
  ],
});

export default router;
