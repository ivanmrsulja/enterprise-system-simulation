import { createRouter, createWebHistory } from "vue-router";
import HomeView from "../views/HomeView.vue";

const roles = { worker: "WORKER", boss: "BOSS", indexer: "INDEX_WORKER" };

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
            path: "/login/:fetchToken",
            name: "login",
            component: () => import("../views/LoginView.vue"),
            meta: {
                authenticated: false,
                authorities: [],
            },
        },
        {
            path: "/search",
            name: "search",
            component: () => import("../views/SearchView.vue"),
            meta: {
                authenticated: true,
                authorities: [roles.worker, roles.boss, roles.indexer],
            },
        },
        {
            path: "/report",
            name: "report",
            component: () => import("../views/ReportView.vue"),
            meta: {
                authenticated: true,
                authorities: [roles.boss],
            },
        },
        {
            path: "/index-application",
            name: "index",
            component: () => import("../views/IndexView.vue"),
            meta: {
                authenticated: true,
                authorities: [roles.indexer],
            },
        },
    ],
});

export default router;
