import Vue from 'vue'
import Router from 'vue-router'
import HelloWorld from '@/components/HelloWorld'

import Login from '../components/Login.vue'
import Header from '../components/Header.vue'
import AccountsHeader from '../components/AccountsHeader.vue'
import Accounts from '../views/Accounts.vue'
import Home from '../views/Home.vue'
import Search from '../views/Search.vue'
import Picture from '../views/Picture.vue'
import NotFoundImg from '../views/NotFoundImg.vue'
import Test from  '../views/Test.vue'
import UserDetail from "../views/UserDetail.vue";
import Advertising from "../components/Advertising.vue";
Vue.use(Router)
export default new Router({
	routes: [{
			path: '/',
			name: 'Home',
			component: Home
		},
    {
      path: '/Test',
      name: 'Test',
      component: Test
    },
		{
			path: '/Header',
			name: 'Header',
			component: Header
		},
		{
			path: '/Accounts',
			name: 'Accounts',
			component: Accounts,
			meta: {
			    requiresAuth:true
			}
		},
		{
			path: '/AccountsHeader',
			name: 'AccountsHeader',
			component: AccountsHeader,
			meta: {
			    requiresAuth:true
			}
		},
    {
      path: '/Search/:search?',
      name: 'Search',
      component: Search,
    },
    {
      path:'/Picture/:picId',
      name:'Picture',
      component: Picture,
      meta: {
        requiresAuth:true
      }
    },
    {
      path:'/Error/NotFoundImg',
      name:'NotFoundImg',
      component: NotFoundImg,
    },
    {
      path:'/UserDetail/:authorId',
      name:'UserDetail',
      component:UserDetail,
    },
    {
      path:'/Advertising',
      name:'Advertising',
      component:Advertising
    }

	]
})
