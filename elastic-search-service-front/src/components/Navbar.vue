<template>
    <ul>
        <li :hidden="loggedIn === true ? false : true">
            <router-link :class="path == '/search' ? 'active' : ''" to="/search"
                >Search</router-link
            >
        </li>
        <li :hidden="roles === 'BOSS' ? false : true">
            <router-link :class="path == '/report' ? 'active' : ''" to="/report"
                >Browse reports</router-link
            >
        </li>
    </ul>
</template>

<script>
import { useRoute } from "vue-router";
import { computed, ref, onMounted, inject } from "vue";
import { authService } from "../services/authService";

export default {
    setup() {
        const loggedIn = ref(false);
        const roles = ref("");

        const route = useRoute();
        const path = computed(() => route.path);

        const router = inject("router");
        const emitter = inject("emitter");

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

        return { path, roles, loggedIn };
    },
};
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
    background-color: #04aa6d;
}
</style>
