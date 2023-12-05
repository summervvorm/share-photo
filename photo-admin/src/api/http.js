import axios from 'axios'; // 引入axios
import QS from 'qs'; // 引入qs模块，用来序列化post类型的数据，后面会提到
// vant的toast提示框组件，大家可根据自己的ui组件更改。
import router from '@/router'
import {Message} from "element-ui";
import Router from "vue-router";
// import { Loading } from 'element-ui';
const instance = axios.create({
  // baseURL: 'http://localhost:8082/', // 基准地址  可直接配置路径,也可代理路径(以实际项目为准)
  timeout: 10000 // 超时时间
})
let loadingInstance
instance.interceptors.request.use((config) => {
  // 设置请求头
  let token = localStorage.getItem('adminToken')
  config.headers.Authorization = token
  // config.headers['Content-Type']="multipart/form-data"
  // console.log(config)
  //设置logding配置 赋值
  // loadingInstance = Loading.service({====
  //     lock: true,
  //     text: '拼命加载中...',
  //     spinner: 'el-icon-loading',
  //     background: 'rgba(0, 0, 0, 0.7)'
  //
  // }
  //
  //
  // );

  // loadingInstance.close();
  // 返回请求头
  return config
}, err => {
  // 请求失败返回失败信息
  return Promise.reject(err)
})

// 响应拦截
instance.interceptors.response.use((res) => {
  // 响应成功则关闭loading加载
  // loadingInstance.close();
  //返回响应数据
  // console.log(JSON.stringify(res))
  if(res.data.code==51000||res.data.code==52000){
    // res.data.message="用户名或密码错误"
    // console.log(Promise.reject(res))
    Message.error(res.data.message)
    localStorage.removeItem("adminToken")
    router.push("/Login")

    return Promise.reject(res)
  }
  return res
}, err => {
  // 响应失败则关闭loading加载
  // setTimeout(() => {
  //     loadingInstance.close();
  // }, 1000);
  // 响应失败返回失败信息
  return Promise.reject(err)
})
// 导出实例
export default instance
