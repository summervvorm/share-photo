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
import waterfall from 'vue-waterfall2'
/* import the fontawesome core */
import { library } from '@fortawesome/fontawesome-svg-core'
import "./assets/style/global.css";
/* import font awesome icon component */
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'

/* import specific icons */
import { faUserSecret } from '@fortawesome/free-solid-svg-icons'
// import './assets/style/element-variables.scss'
/* add icons to the library */
library.add(faUserSecret)

/* add font awesome icon component */
Vue.component('font-awesome-icon', FontAwesomeIcon)
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
  console.log("用户详情")
  console.log(currentUser)
	if (requiresAuth && !currentUser) {

		console.log("取消跳转")
		// 显示登录对话框
		store.commit('SET_SHOW_LOGIN_DIALOG', true) / // 取消路由跳转
			next('/')


		// 监听登录对话框的关闭事件
		store.watch(
			state => state.showLoginDialog,
			(newVal, oldVal) => {
				if (!newVal) {
					// 用户关闭了登录对话框，尝试再次路由跳转
					const currentUser = store.state.currentUser
					if (currentUser) {
						next()
						console.log("跳转成功")
					} else {
						// store.commit('SET_SHOW_LOGIN_DIALOG', true)
					}
				}
			}
		)
	} else {

		next()
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
		waterfall,
		components: {
			App
		},
		store,
		template: '<App/>',
		render: h => h(App)
	})

})
