<template>
    <div class="about">
        <h1>Be patient, you will be logged in shortly...</h1>
    </div>
</template>

<script>
import { authService } from "../services/authService";
import { useRoute, useRouter } from "vue-router";
import jwtDecode from "jwt-decode";
import { onMounted } from "@vue/runtime-core";
import { inject } from "vue";

export default {
    setup() {
        const route = useRoute();
        const router = useRouter();
        const emitter = inject("emitter");

        onMounted(() => {
            setTimeout(() => {
                authService.login(route.params.fetchToken).then((response) => {
                    let decodedToken = jwtDecode(response.data.token);
                    authService.setToken(response.data.token);
                    emitter.emit("loginSuccess", decodedToken.roles);
                    if (decodedToken.roles === "WORKER") {
                        router.push("/search");
                    } else if (decodedToken.roles === "BOSS") {
                        router.push("/report");
                    }
                });
            }, 1000);
        });
    },
};
</script>

<style>
@media (min-width: 1024px) {
    .about {
        min-height: 100vh;
        display: flex;
        align-items: center;
    }
}
</style>
