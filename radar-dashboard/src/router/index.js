import { createRouter, createWebHistory } from "vue-router";
import Home from "../views/Home.vue";
import Segmentation from "../views/Segmentation.vue";
import Sessions from "../components/SessionTable.vue";
import userSession from "../views/userSession.vue";
import userSessionGraphs from "../views/userSessionGraphs.vue";

const routes = [
    { path: "/", name: "Home", component: Home },
    { path: "/sessions", name: "sessions", component: Sessions},
    { path: "/sessions/:id", component: userSession, props: true },
    { path: "/sessions/analytics", component: userSessionGraphs },
    { path: "/k-mean", name: "K-mean", component: Segmentation}
];


const router = createRouter({
    history: createWebHistory(),
    routes,
});

export default router;
