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
import {computed, ref, onMounted, inject} from 'vue'
import { authService } from '../service/authService'

export default {
    name: "navbar",
    setup(){
        const loggedIn = ref(false)
        const roles = ref("")

        const route = useRoute()
        const path = computed(() => route.path)

        const router = inject('router');
        const emitter = inject('emitter');

        onMounted(() => {
          if (authService.userLoggedIn()) {
            let decodedToken = authService.getDecodedToken();
            loggedIn.value = true;
            roles.value = decodedToken.roles;
          }
          emitter.on("loginSuccess", (role) => {
              loggedIn.value = authService.userLoggedIn();
              roles.value = role;
          });
        });

        const logout = () => {
            sessionStorage.removeItem("jwt");
            loggedIn.value = false;
            router.push({ name: "login" });
        }

        return {path, loggedIn, logout}
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