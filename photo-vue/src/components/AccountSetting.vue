<template>
  <div class='box'>
    <div class="aside-menu">
      <div :class="index===0?'aside-item active':'aside-item'" @click="index=0">
        个人信息
      </div>
      <div :class="index===1?'aside-item active':'aside-item'" @click="index=1">
        账号安全
      </div>
    </div>
    <div class="content">
      <div style="display: flex" v-if="index===0">
        <div class="aside">
          <el-upload
            class="avatar-uploader"
            action=""
            :show-file-list="false"
            :on-change="handleAvatarSuccess"
            :before-upload="beforeAvatarUpload"
            :auto-upload="false"
          >
            <img v-if="userInfo.header" :src="userInfo.header" class="avatar" style="object-fit: cover">
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
        </div>
        <div class="main">
          <div class="main-top">
            我的信息
          </div>

          <div class="main-input">
            <div style="margin-right: 30px">昵称：</div>
            <el-input size="small" style="width: 400px" v-model="userInfo.nickname"></el-input>
          </div>
          <div class="main-input">
            <div style="margin-right: 30px">账号：</div>
            <span>{{ userInfo.userEmail }}</span></div>
          <div class="main-input">
            <div style="margin-right: 40px">经验:</div>
            <div>
              <i class="fa-solid fa-bolt"></i> {{userInfo.experience}}
            </div>
          </div>

          <div class="main-input" style="display: flex;align-items: flex-start;">
            <span>我的签名：</span>
            <el-input
              type="textarea"
              placeholder="请输入内容"
              v-model="userInfo.sign"
              maxlength="30"
              show-word-limit
              resize="none"
              style="width: 400px;"
            ></el-input>
          </div>
          <div class="main-button">
            <button @click="saveUserInfo">保存</button>

          </div>

        </div>
      </div>
      <div v-if="index===1" class="password1">
        <div style="font-size: 24px;font-weight: 600;margin-top: 40px">
          修改密码
        </div>
        <div >
          <div style="display: flex;margin-bottom: 30px;margin-top: 20px">
            <div style="width: 100px;line-height: 50px">原密码: </div><el-input
            style="width: 400px;"
            placeholder="请输入内容"
            v-model="oldPassword"
            show-password

          >
          </el-input>

          </div>
          <div style="display: flex">
            <div style="width: 100px;line-height: 50px">新密码: </div>
            <el-input
              style="width: 400px;"

              placeholder="请输入内容"
              v-model="newPassword"
              show-password
            >
            </el-input>
          </div>
          <div style="display: flex;margin-top: 30px">
            <div style="width: 100px;line-height: 50px;">确定密码: </div>
            <el-input
              style="width: 400px;"

              placeholder="请输入内容"
              v-model="certainPassword"
              show-password
            >
            </el-input>
          </div>
          <div class="tip" v-show="tip">
            *{{msg}}
          </div>

          <div class="main-button">
          <button @click="savePassword">保存</button>
            <button style="background: #ec6767;margin-left: 20px" @click="forgetPasswordDialog=true">忘记密码</button>

          </div>
        </div>

      </div>
    </div>
    <el-dialog
      title="忘记密码"
      :visible.sync="forgetPasswordDialog"
      width="450px"
      center>
<!--      <div style="align-items: center">-->
<!--        <div style="font-weight: 600;font-size: 16px;margin-bottom: 10px">主题:</div><el-input placeholder="添加一个主题"  v-model="theme"></el-input>-->
<!--      </div>-->

<!--      <div>-->
<!--        <div style="font-weight: 600;font-size: 16px;margin-bottom: 10px;margin-top: 20px">消息:</div>-->
<!--        <el-input  type="textarea" maxlength="1000" show-word-limit  :autosize="{ minRows: 4, maxRows: 100}" placeholder="输入内容"  v-model="content"></el-input>-->
<!--      </div>-->
      <el-form v-model="form"  label-width="80px">



        <el-form-item label="新密码">
          <el-input v-model="form.newPassword"></el-input>
        </el-form-item>
        <el-form-item label="确定密码">
          <el-input v-model="form.password"></el-input>
        </el-form-item>

        <el-form-item label="验证码:" prop="code">
          <el-input v-model="form.emailCode" style="width: 60%;" oninput="value=value.replace(/[^\d]/g,'')"
                    maxlength="6">

          </el-input>
          <el-button v-show="!showcode" style="width: 35%;margin-left: 10px;" disabled>
            <span style="width: 50px;"> {{count}} s</span>
          </el-button>
          <el-button @click="getCode()" style="width:  35%;" type="info" v-show="showcode">
            <span style="width: 50px;"> 获取验证码</span>
          </el-button>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
    <el-button @click="forgetPasswordDialog = false">取 消</el-button>
    <el-button type="primary" @click="resetPassword">提 交</el-button>
  </span>
    </el-dialog>


  </div>
</template>

<script>
// 这里可以导入其他文件（比如：组件，工具js，第三方插件js，json文件，图片文件等等）
// 例如：import 《组件名称》 from ‘《组件路径》‘;
import api from '../api/http.js'
import {
  Notification
} from 'element-ui'
import store from "../stores/stores";
import router from "../router";
const TIME_COUNT = 10 // 倒计时的时间
export default {
  name: 'AccountSetting',
  // import引入的组件需要注入到对象中才能使用
  components: {},
  data() {
    // 这里存放数据
    return {
      showcode:true,
      tip:false,
      count:'',
      form:{},
      forgetPasswordDialog:false,
      code:'',
      userInfo: {},
      index: 0,
      timer: null,
      file: null,
      oldPassword:'',
      newPassword:'',
      certainPassword:'',
      msg:'',
    }
  },
  // 监听属性 类似于data概念
  computed: {},
  // 监控data中的数据变化
  watch: {
    certainPassword(newVal){
      if(newVal!==this.newPassword){
        this.tip=true
        this.msg="两次输入的密码不相同！"
      }else{
        this.tip=false
      }
    }

  },
  // 方法集合
  methods: {


    getCode(){
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
      const params={
        userEmail:this.$store.state.currentUser.userEmail
      }
      api.get("/api/users/code/",{
        params
      }).then((res)=>{

      }).catch((err)=>{

      })
    },

    resetPassword(){

      if(this.form.newPassword!==this.form.password){
        this.$message({
          message:'两次输入的密码不相同',
          type:'warning'
        })
      }else{
        api.post("/api/users/authors",{
          password:this.form.password,
          code:this.form.emailCode
        }).then((res)=>{
          console.log(res)
          this.$router.go(0)
        }).catch((err)=>{
          console.log(err)
        })
      }

    },

    savePassword(){

      if(!this.tip){

        api.post("/api/users/password",{
          oldPassword:this.oldPassword,
          newPassword:this.newPassword
        }).then(function (res){
          if(res.data.code===20000){
            Notification({
              message:'修改成功',
              type:'success'
            })
            api.post("/api/users/logout").then(function (res) {

              store.dispatch('login')
              router.go(0)
            }).catch(function (err) {
              Notification({
                message: err.data.message,
                type: 'error'
              })
            })
          }else{
            Notification({
              message:res.data.message,
              type:'warning'
            })
          }
        }).catch(function (err){

        })
      }

    },
    saveUserInfo() {
      const data1 = new FormData();
      data1.append('file', this.file)
      const vm = this
      api.put("/api/users/update", {
        userId: this.userInfo.userId,
        nickname: this.userInfo.nickname,
        sign: this.userInfo.sign
      }).then(function (res) {
        console.log(res)
        Notification({
          message: '保存成功',
          type: 'success'
        })
        if (vm.file !== null)
          api.post("/api/users/header", data1).then(function (res2) {
            console.log(res2)
            vm.userInfo.header = res2.data.data.data

            vm.$store.state.currentUser = vm.userInfo
          }).catch(function (err) {

          })
      }).catch(function (err) {

      })
    },
    handleAvatarSuccess(file) {
      this.userInfo.header = window.URL.createObjectURL(file.raw);
      this.file = file.raw
    },
    beforeAvatarUpload(file) {
      return true
    }

  },
  // 生命周期 - 创建完成（可以访问当前this实例）
  created() {

  },
  // 生命周期 - 挂载完成（可以访问DOM元素）
  mounted() {
    this.userInfo = this.$store.state.currentUser

  },
  beforeCreate() {
  }, // 生命周期 - 创建之前
  beforeMount() {
  }, // 生命周期 - 挂载之前
  beforeUpdate() {
  }, // 生命周期 - 更新之前
  updated() {
  }, // 生命周期 - 更新之后
  beforeDestroy() {
  }, // 生命周期 - 销毁之前
  destroyed() {
  }, // 生命周期 - 销毁完成
  activated() {
  } // 如果页面有keep-alive缓存功能，这个函数会触发
}
</script>
<style scoped>
.tip{
  color: #c72525;
  font-size: 12px;
  display: flex;
  margin-left: 100px;
  margin-top: 10px;
}
.active {
  background: #4caf50 !important;
  color: white;
}

.aside-item {
  width: 100%;
  height: 50px;
  line-height: 50px;
  font-size: 18px;
  cursor: pointer;
}

.aside-item:hover {
  background: #e8e8e8;
}

.content {
  display: flex;
  justify-content: center;
  width: 100%;
}
/deep/
.password1 .el-input__inner{
  line-height: 50px !important;
  height: 50px;
}
.aside-menu {
  display: flex;
  flex-direction: column;
  padding-right: 5px;
  box-sizing: border-box;
  width: 250px;
  border-right: 2px solid rgba(233, 233, 233, 1.0);
}

.main-button{
  margin-top: 20px;
}

.main-button button {
  //width: 80px;
  height: 40px;
  padding: 5px 20px;
  background: #52aa3e;
  border: none;
  cursor: pointer;
  color: white;
  font-size: 16px;
  border-radius: 5px;
}

.main-top {
  margin: 20px 0px;
  font-size: 24px;
  font-weight: 600;
}

.main-input {
  display: flex;
  align-items: center;
  margin-bottom: 40px;
}

.aside {
  background: #FFFFFF;
  padding: 30px;
  border-radius: 20px;
  height: 178px;
}

.main {
  background: #ffffff;
  border-radius: 20px;
  margin-left: 40px;
  width: 100%;
  padding: 10px 50px;
}

.box {
  min-height: 800px;
  display: flex;
}

.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}

.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}

.avatar {
  width: 178px;
  height: 178px;
  display: block;
}

.box {
  min-height: 700px;
  display: flex;
  background-color: #FFF;
  padding: 20px 20px 20px 0px;
  border-radius: 8px;
}
</style>
