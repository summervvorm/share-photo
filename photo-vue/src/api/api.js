import api from "./http.js"
import {ele_error, ele_success, ele_warning} from "../notify/element-notify";

export const reqLogin = (parmas) => api.post('/login', parmas)


export const apiCheckEmail = (email) => api.get(`/api/users/emails/check/${email}`).then(response => {
  const flag = response.data.data;
  if (!flag) {
    ele_warning("邮箱已存在,请重新输入")
  }
  return flag;
}).catch(error => {
  ele_error('Error checking email:', error)
  console.error();
});


export const apiRegisterUser = (userData) => api({
  method: 'post',
  url: '/api/users/register',
  data: userData
}).then(response => {
  ele_success('注册成功')
  return response;
}).catch(error => {
  ele_error('Registration failed:', error)
});

export const apiFetchUserCode = (userEmail) => api({
  method: 'get',
  url: '/api/users/code/',
  params: {userEmail}
}).then(response => {
  ele_success(response.data.message)
  return response;
}).catch(error => {
  console.log(error)
  ele_error(error.data.message)
  return error
});


export const apiLoginUser = (data1) => api.post("/api/users/login", data1, {
  headers: {'Content-Type': 'multipart/form-data'}
}).then(res => {
  return res;
}).catch(err => {
  ele_error(err.response.data.message)
  return err
});


