// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import ElementUI from 'element-ui';
import {
	Message
} from 'element-ui'
import store from './stores/stores.js'
import 'element-ui/lib/theme-chalk/index.css';
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate'
import {
	createPinia
} from 'pinia'
//main.js
import axios from 'axios'
import * as echarts from 'echarts';
import api from '../src/api/http'
import waterfall from 'vue-waterfall2'
/* import the fontawesome core */
import { library } from '@fortawesome/fontawesome-svg-core'
import ECharts from 'vue-echarts'
/* import font awesome icon component */
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'

/* import specific icons */
import { faUserSecret } from '@fortawesome/free-solid-svg-icons'

/* add icons to the library */
library.add(faUserSecret)
// import { library } from '@fortawesome/fontawesome-svg-core'
import "./assets/style/global.css";
/* add font awesome icon component */
Vue.component('font-awesome-icon', FontAwesomeIcon)
Vue.component('v-chart', ECharts)

Vue.prototype.$axios = axios
Vue.prototype.$message = Message
Vue.config.productionTip = false
Vue.use(ElementUI);
/* eslint-disable no-new */
const pinia = createPinia()
// Vue.prototype.$message = Message
pinia.use(piniaPluginPersistedstate)
/* 路由*/
router.beforeEach((to, from, next) => {
	const requiresAuth = to.matched.some(record => record.meta.requiresAuth)
	const currentUser = store.state.currentUser
	console.log(currentUser)
	if (requiresAuth && !currentUser) {

		console.log("取消跳转")
		// 显示登录对话框
		// store.commit('SET_SHOW_LOGIN_DIALOG', true) / // 取消路由跳转
			next('/Login')


		// 监听登录对话框的关闭事件

	} else {

		next()
		console.log("跳转")
	}

})
Vue.use(waterfall)
store.dispatch('fetchData').then(() => {
	new Vue({
		el: '#app',
		router,
		axios,
		ElementUI,
		pinia,
    echarts,
    api,
		waterfall,
		components: {
			App
		},
		store,
		template: '<App/>',
		render: h => h(App)
	})

})
