<template>

  <div class="box">
    <div style="display: flex;justify-content: space-between;height: 100%;">
    <div class="left">
      <div style="font-size: 40px;color: whitesmoke;margin-top: 20%;font-weight: 800">
        共享图库后台管理系统
      </div>
    </div>


<div class="right">


    <el-form ref="form" :rules="rules1" :model="form" label-width="80px"
             style="width: 400px;margin: 200px auto;height: 400px;padding: 20px;background: #ffffff;">
      <div style="display: flex;margin-left: 34px;margin-bottom: 20px;font-weight: 600">
        管理员登录
      </div>
      <el-form-item label="账号:" prop="name">
        <el-input v-model="form.name"></el-input>
      </el-form-item>

      <el-form-item label="密码:" prop="password">
        <el-input v-model="form.password" show-password></el-input>
      </el-form-item>

      <div style="margin-top: 45px">
        <el-button type="primary" @click="onLogin">立即登录</el-button>
      </div>
    </el-form>
</div>
    </div>
  </div>

</template>
<script>
import useUserStore from "../stores/user";
import form_data from 'form-data'
import api from '../api/http.js'
import {
  Notification
} from 'element-ui'
export default {
  name: "Login",

  data(){

    return{
      form:{
        name:'',
        password:'',
      },

      rules1: {
        name: [{
          required: true,
          message: '请输入账号',
          trigger: 'blur'
        },
          {
            min: 4,
            max: 30,
            message: '长度为4~30个字符 ',
            trigger: 'blur'
          }
        ],
        password: [{
          required: true,
          message: '请输入密码',
          trigger: 'blur'
        },
          {
            min: 3,
            max: 15,
            message: '长度为3~15个字符 ',
            trigger: 'blur'
          }
        ]
      }
    }

  },

  methods: {
    handleOpen(key, keyPath) {
      console.log(key, keyPath);
    },
    handleClose(key, keyPath) {
      console.log(key, keyPath);
    },
    onLogin(){
      var FormData = form_data
      var data1 = new FormData();
      data1.append('username', this.form.name);
      data1.append('password', this.form.password);
      const store=this.$store
      console.log(data1)
      const router=this.$router
      const vm=this
      api.post("/api/users/login", data1, {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      }).then((res)=>{
        useUserStore.userInfo = res.data

        localStorage.setItem('adminToken', res.data.data)
        console.log(res)
        useUserStore.token = res.data.data

        const credentials =res.data.data
        store.dispatch('login', credentials)
        router.push({ path: "/Index" })
      }).catch((err)=>{
        console.log(err)
      })
    }
  }
}
</script>



<style scoped>

.left{
  width: 100%;
  background: url("../img/Admin-background.png");
  background-size: cover;
}
.right{
  //background: rgba(131, 194, 95, 0.71);
}
.box{
  width: 100%;
  height: 100vh;

}
</style>
