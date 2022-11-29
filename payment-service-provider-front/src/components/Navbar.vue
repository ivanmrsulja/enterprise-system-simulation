<template>
    <ul>
        <li><router-link :class="path == '/' ? 'active' : ''" to="/">Home</router-link></li>
        <li><router-link v-if="!loggedIn" :class="path == '/login' ? 'active' : ''" to="/login">Login</router-link></li>
        <li><router-link v-if="!loggedIn" :class="path == '/register' ? 'active' : ''" to="/register">Register</router-link></li>
        <li><router-link v-if="loggedIn" :class="path == '/payment-methods' ? 'active' : ''" to="/payment-methods">Payment methods</router-link></li>
        <li><router-link v-if="loggedIn" @click="logout()" to="/login">Logout</router-link></li>
    </ul>
</template>

<script>
import {useRoute} from 'vue-router'
import {computed} from 'vue'
import { authService } from '../service/authService'

export default {
    name: "navbar",
    setup(){
        const route = useRoute()
        const path = computed(() => route.path)
        return {path}
    },
    data() {
    return {
      loggedIn: false,
      roles: "",
      snackbarMessage: "Error logging out.",
      snackbar: false,
      timeout: 5000,
    };
    },
    mounted() {
        if (authService.userLoggedIn()) {
            let decodedToken = authService.getDecodedToken();
            this.loggedIn = true;
            this.roles = decodedToken.roles;
        }
        this.emitter.on("loginSuccess", (roles) => {
            this.loggedIn = authService.userLoggedIn();
            this.roles = roles;
        });
    },
    methods: {
        redirect(routeName) {
            this.$router.push({ name: routeName }).catch((error) => {});
        },
        logout() {
            sessionStorage.removeItem("jwt");
            this.loggedIn = false;
            this.$router.push({ name: "login" });
        },
    },
}
</script>

<style scoped>
ul {
  list-style-type: none;
  margin: 0;
  padding: 0;
  overflow: hidden;
  background-color: #333;
}

li {
  float: left;
}

li a {
  display: block;
  color: white;
  text-align: center;
  padding: 14px 16px;
  text-decoration: none;
}

li a:hover:not(.active) {
  background-color: #111;
}

.active {
  background-color: #04AA6D;
}
</style>