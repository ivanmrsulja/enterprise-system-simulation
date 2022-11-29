<template>
    <ul>
        <li><a :class="path == '/' ? 'active' : ''" href="/">Home</a></li>
        <li><a :class="path == '/login' ? 'active' : ''" href="/login">Login</a></li>
        <li><a :class="path == '/register' ? 'active' : ''" href="/register">Register</a></li>
        <li><a href="#" @click="RouteChanged()">Payment options</a></li>
    </ul>
</template>

<script>
import {useRoute} from 'vue-router'
import {computed} from 'vue'

export default {
    name: "navbar",
    setup(){
        const route=useRoute();
        const path = computed(() =>route.path)

        return {path}
    }
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