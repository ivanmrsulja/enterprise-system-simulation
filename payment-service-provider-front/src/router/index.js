import { createRouter, createWebHistory } from "vue-router";
import HomeView from "../views/HomeView.vue";

const roles = { merchant: "ROLE_MERCHANT" };

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/",
      name: "home",
      component: HomeView,
      meta: {
        authenticated: false,
        authorities: [],
      },
    },
    {
      path: "/login",
      name: "login",
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import("../views/LoginView.vue"),
      meta: {
        authenticated: false,
        authorities: [],
      },
    },
    {
      path: "/register",
      name: "register",
      component: () => import("../views/RegisterView.vue"),
      meta: {
        authenticated: false,
        authorities: [],
      },
    },
    {
      path: "/make-payment/:merchantOrderId/:transactionId/:merchantId",
      name: "make-payment",
      component: () => import("../views/PaymentChoiceView.vue"),
      meta: {
        authenticated: false,
        authorities: [],
      },
    },
    {
      path: "/payment-methods",
      name: "paymentMethods",
      component: () => import("../views/PaymentMethodsView.vue"),
      meta: {
        authenticated: true,
        authorities: [roles.merchant],
      },
    },
  ],
});

export default router;
