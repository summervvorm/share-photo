import Vue from 'vue'
import Vuex from 'vuex'
import api from '../api/http.js'
Vue.use(Vuex)

export default new Vuex.Store({
	state: {
		currentUser: null,
		showLoginDialog: false,
		pictureList:null,
    isDisplayAd:true,
    downloadCount:0,
    downloadAbles:10,
	},
	mutations: {
		SET_CURRENT_USER(state, user) {
			state.currentUser = user
		},
		SET_SHOW_LOGIN_DIALOG(state, value) {
			state.showLoginDialog = value
		},
		SET_PICTURE_LIST_(state,pic){
			state.pictureList=pic
		},
	},
	actions: {
		login({commit}, credentials) {
			// 发送登录请求，获取用户信息
			const user = credentials
			console.log(credentials)
			commit('SET_CURRENT_USER', user)
			commit('SET_SHOW_LOGIN_DIALOG', false)
		},
		logout({commit}) {
			commit('SET_CURRENT_USER', null)
		},
		async fetchData({commit}) {
			try {
				// 发送后台请求获取数据
				const response =  await  api.get('/api/users/info/').then(function(res){
					// 将获取到的数据保存到 store 中
					commit('SET_CURRENT_USER', res.data.data)
				})
			} catch (error) {
				console.error(error)
			}

		}
	},
	getters: {
		isLoggedIn: state => !!state.currentUser
	}
})
