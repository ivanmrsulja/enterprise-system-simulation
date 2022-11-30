import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import vuetify from './plugins/vuetify'
import { loadFonts } from './plugins/webfontloader'
import axios from 'axios'
import jwt_decode from 'jwt-decode'
import mitt from 'mitt';

loadFonts()

// Configure axios to always include JWT when sending a request
axios.interceptors.request.use(
  (config) => {
    let jwt = sessionStorage.getItem("jwt");
    if (jwt) {
      if (config.headers) {
        config.headers.Authorization = `Bearer ${jwt}`;
      }
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Authority guard
router.beforeEach((to, from, next) => {
  const { authenticated, authorities } = to.meta;

  if (to.name === "Home" && sessionStorage.getItem("jwt")) {
    next({ name: "CertificateRequests" });
  }

  if (authenticated) {
    let jwt = sessionStorage.getItem("jwt");
    if (jwt) {
      let decodedToken = jwt_decode(jwt);
      if (authorities.some((element) => decodedToken.roles.includes(element))) {
        next();
      } else {
        next({ name: "login" });
      }
    } else {
      next({ name: "login" });
    }
  } else {
    next();
  }
});

const emitter = mitt();

const app = createApp(App)
app.config.globalProperties.emitter = emitter;
app.use(router)
app.use(vuetify)

app.provide('router', router)
app.provide('emitter', emitter)

app.mount('#app')
