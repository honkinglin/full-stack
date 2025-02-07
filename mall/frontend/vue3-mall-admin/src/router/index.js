import { createRouter, createWebHashHistory } from "vue-router";
import login from "../pages/login.vue";
import good_manage from "../pages/good_manage.vue";
import manage from "../pages/manage.vue";
import order from "../pages/order.vue";
import person from "../pages/person.vue";
import type from "../pages/type.vue";

const routes = [
  {
    path: "/",
    name: "login",
    component: login,
  },
  {
		path: '/manage',
		component: manage,
		name: '',
		children: [{
			path: '/type',
			component: type,
		},{
			path: '/good',
			component: good_manage,
		},{
			path: '/person',
			component: person,
		},{
			path: '/order',
			component: order,
		}]
	}
];

const router = createRouter({
  history: createWebHashHistory(process.env.BASE_URL),
  routes,
});

export default router;
