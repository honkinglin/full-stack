import { createRouter, createWebHashHistory } from "vue-router";
import Login from "../pages/login.vue";
import index from "../pages/index.vue";
import pay from "../pages/pay.vue";
import cart from "../pages/cart.vue";
import detail from "../pages/detail.vue";
import orders from "../pages/orders.vue";
import order_detail from "../pages/order_detail.vue";
import ordering from "../pages/ordering.vue";
import personal from "../pages/personal.vue";
import register from "../pages/register.vue";
import search from "../pages/search.vue";

const routes = [
  {
    path: "/",
    redirect: "/index",
  },
  {
    path: "/search",
    name: "search",
    component: search,
  },
  {
    path: "/personal",
    name: "personal",
    component: personal,
  },
  {
    path: "/register",
    name: "register",
    component: register,
  },
  {
    path: "/Login",
    name: "Login",
    component: Login,
  },
  {
    path: "/index",
    name: "index",
    component: index,
  },
  {
    path: "/pay",
    name: "pay",
    component: pay,
  },
  {
    path: "/cart",
    name: "cart",
    component: cart,
  },
  {
    path: "/detail",
    name: "detail",
    component: detail,
  },
  {
    path: "/orders",
    name: "orders",
    component: orders,
  },
  {
    path: "/order_detail",
    name: "order_detail",
    component: order_detail,
  },
  {
    path: "/ordering",
    name: "ordering",
    component: ordering,
  },
];

const router = createRouter({
  history: createWebHashHistory(process.env.BASE_URL),
  routes,
});

export default router;
