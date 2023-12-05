import Vue from 'vue'
import Router from 'vue-router'
import Home from '../views/Home.vue'
import Login from "../views/Login.vue";
import Report from "../views/Report.vue";

Vue.use(Router)
export default new Router({
  routes: [{
    path: '/',
    name: 'Home',
    component: Home,
    meta: {
      requiresAuth: true
    },
    children: [
      {
        path: 'Index',
        name: '首页',
        component: () => import('../views/Index.vue'),
        meta: {
          requiresAuth: true
        },
      },
      {
        path: 'Report',
        name: '反馈管理',
        component: () => import('../views/Report.vue'),
        meta: {
          requiresAuth: true
        },
        children: [
          {
            path: 'TagReport',
            name: '标签反馈管理',
            component: () => import('../views/TagReport.vue'),
            meta: {
              requiresAuth: true
            },
          },
          {
            path: 'PicReport',
            name: '图片反馈管理',
            component: () => import('../views/PicReport.vue'),
            meta: {
              requiresAuth: true
            },
          },
        ]
      },
      {
        path: 'User',
        name: '用户管理',
        component: () => import('../views/User.vue'),
        meta: {
          requiresAuth: true
        },
        children: [
          {
            path: 'UserList',
            name: '用户列表',
            meta: {
              requiresAuth: true
            },
            component: () => import('../views/UserList.vue')
          },
          {
            path: 'UserLevel',
            name: '用户等级列表',
            meta: {
              requiresAuth: true
            },
            component:()=> import('../views/UserLevel.vue')
          }

        ]
      },
      {
        path: 'Picture',
        name: '图片管理',
        component: () => import('../views/Picture.vue'),
        meta: {
          requiresAuth: true
        },
        children: [
          {
            path: 'PictureList',
            name: '图片列表',
            component: () => import ('../views/PictureList.vue'),
            meta: {
              requiresAuth: true
            },
          }
        ]
      },
      {
        path: 'Audit',
        name: '审核管理',
        component: () => import('../views/Audit.vue'),
        meta: {
          requiresAuth: true
        },
        children: [
          {
            path: 'AuditList',
            name: '图片审核列表',
            component: () => import ('../views/AuditList.vue'),
            meta: {
              requiresAuth: true
            },
          }
        ]
      },
      {
        path: 'Advertisement',
        name: '广告管理',
        component: () => import('../views/Advertisement.vue'),
        meta: {
          requiresAuth: true
        },
        children: [
          {
            path: 'Advertisements',
            name: '广告列表',
            component: () => import ('../views/Advertisements.vue'),
            meta: {
              requiresAuth: true
            },
          }
        ]
      }
    ]
  },
    {
      path: '/login',
      name: 'Login',
      component: Login
    },

  ]
})
