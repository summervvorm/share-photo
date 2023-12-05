<template>
  <div>
    <el-dialog title="请先登录或注册" :visible.sync="dialogVisible" width="500px">
      <el-container class="main" v-if="dialogVisible">
        <el-main style="height: 330px">
          <el-form ref="form2" :rules="rules1" :model="form2" label-width="80px"
                   style="width: 400px;margin: 0 auto;" v-show="!show">
            <el-form-item label="账号:" prop="name">
              <el-input v-model="form2.name"></el-input>
            </el-form-item>

            <el-form-item label="密码:" prop="password">
              <el-input v-model="form2.password" show-password></el-input>
            </el-form-item>

            <div style="margin-top: 145px">
              <el-button type="primary" @click="onLogin">立即登录</el-button>
              <el-button @click="goenroll">去注册</el-button>
            </div>
          </el-form>

          <el-form ref="form" :model="form" :rules="rules2" label-width="80px"
                   style="width: 400px;margin: 0 auto;" v-show="show">

            <el-form-item label="邮箱:" prop="email">
              <el-input v-model="form.email" show-email @blur="checkEmail"></el-input>
            </el-form-item>

            <el-form-item label="昵称:" prop="nikename">
              <el-input v-model="form.nikename"></el-input>
            </el-form-item>

            <el-form-item label="密码:" prop="password">
              <el-input v-model="form.password" show-password></el-input>
            </el-form-item>

            <el-form-item label="验证码:" prop="code">
              <el-input v-model="form.emailCode" style="width: 60%;" oninput="value=value.replace(/[^\d]/g,'')"
                        maxlength="6">

              </el-input>
              <el-button v-show="!showcode" style="width: 35%;margin-left: 10px;" disabled>
                <span style="width: 50px;"> {{ count }} s</span>
              </el-button>
              <el-button @click="getCode()" style="width:  35%;" type="info" v-show="showcode">
                <span style="width: 50px;"> 获取验证码</span>
              </el-button>
            </el-form-item>

            <div>
              <el-button type="primary" @click="onre">立即注册</el-button>
              <el-button @click="gologin">去登录</el-button>
            </div>
          </el-form>
        </el-main>

      </el-container>
    </el-dialog>
  </div>
</template>
<script>
import api from '../api/http.js'
import form_data from 'form-data'
import Vue from 'vue'
import useUserStore from '../stores/user.js'
import {
  Notification
} from 'element-ui'
import pubsub from 'pubsub-js'
import {apiCheckEmail, apiFetchUserCode, apiLoginUser, apiRegisterUser, checkEmail, registerUser} from "../api/api";
import {ele_success} from "../notify/element-notify";

const TIME_COUNT = 10 // 倒计时的时间
export default {
  name: 'Login',
  data() {
    return {
      flag: true,
      showcode: true,
      count: '',
      timer: null,
      dialogVisible: false,
      form: {
        email: '',
        nikename: '',
        password: '',
        emailCode: '',
      },
      show: false,
      form2: {
        name: '',
        password: ''
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
      },
      rules2: {
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
        ],
        nikename: [{
          required: true,
          message: '请输入昵称',
          trigger: 'blur'
        }],
        email: [{
          required: true,
          message: '请输入邮箱',
          trigger: 'blur'
        },
          {
            type: 'email',
            message: '请输入正确的邮箱地址',
            trigger: ['blur', 'change']
          }
        ]
      }
    }
  },
  computed: {},
  mounted() {
    this.pubId = pubsub.subscribe('getLoginSign', this.getLoginSign)
  },
  methods: {
    // 登录
    onLogin() {
      var FormData = form_data
      var data1 = new FormData();
      data1.append('username', this.form2.name);
      data1.append('password', this.form2.password);
      const store = this.$store
      console.log(data1)
      const router = this.$router
      const vm = this
      apiLoginUser(data1).then(res => {
        useUserStore.userInfo = res.data
        localStorage.setItem('token', res.data.data)
        console.log(res)
        useUserStore.token = res.data.data
        pubsub.publish('demo', true)
        ele_success("登陆成功")
        const credentials = res.data.data
        store.dispatch('login', credentials)
        vm.dialogVisible = false
        router.go(0)
      })
    },
    getLoginSign(msgName, data) {
      this.dialogVisible = data;
    },
    goenroll() {
      this.show = !this.show
    },
    gologin() {
      this.show = !this.show
    },
    getCode() {
      // 验证码倒计时
      if (!this.timer) {
        this.count = TIME_COUNT
        this.showcode = false
        this.timer = setInterval(() => {
          if (this.count > 0 && this.count <= TIME_COUNT) {
            this.count--
          } else {
            this.showcode = true
            clearInterval(this.timer)
            this.timer = null
          }
        }, 1000)
      }
      apiFetchUserCode(this.form.email)
    },
    onre() {
      const redata = {
        username: this.form.email,
        nickname: this.form.nikename,
        password: this.form.password,
        emailCode: this.form.emailCode
      }
      const vm = this
      apiRegisterUser(redata).then(() => vm.gologin())
    },
    checkEmail() {
      if (this.form.email.length >= 5) {
        const flag = apiCheckEmail(this.form.email);
        this.showcode = !!flag;
      }
    }
  },
  watch: {
    dialogVisible(newVal, oldVal) {
      console.log(newVal)
      if (!newVal)
        this.$store.state.showLoginDialog = false;
    }
  },

  beforeDestroy() {
    pubsub.unsubscribe(this.pubId) //取消订阅
  }

}
</script>

<style scoped>
body {
  padding: 0;
  margin: 0;
}

.pr {
  position: relative;
}

.main {
  /* width: 800px; */
}

.code-btn {
  width: 100px;
  height: 34px;
  position: absolute;
  top: 3px;
  right: 5px;
  z-index: 222;
  color: #F5A623;
  font-size: 14px;
  border: none;
  border-left: 1px solid #bababa;
  padding-left: 16px;
  background-color: #fff;
}

.count {
  color: #000;
}
</style>
