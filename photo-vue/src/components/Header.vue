<template>
  <div>
    <div :class='headmain'>
      <div class="logo" @click="onlog">
				<span style="font-weight: 900;font-size: 26px;line-height: 60px;font-family: MiSans">
					 共享图库
				</span>
      </div>
      <div style="width: 60%;">
        <el-input style="width: 100%;" placeholder="请输入内容" v-model="search" v-show="showSearch">
          <el-button slot="append" icon="el-icon-search" @click="onSearch"></el-button>
        </el-input>
      </div>
      <div class="menu">
        <!-- <el-menu :default-active="activeIndex" class="el-menu-demo" mode="horizontal" @select="handleSelect">
          <el-menu-item index="1">首页</el-menu-item>
          <el-menu-item index="2">图片索引</el-menu-item>
          <el-menu-item index="3">共享图片</el-menu-item>
        </el-menu> -->
        <button :style="{color: textColor}" style="margin-right: 10px;width: 70px" class="loginButton">探索</button>
        <button @click="onLogin" v-if="!isLogin" type="button" :style="{color: textColor}" class="loginButton">登录
        </button>
        <div v-if="isLogin" style="cursor: pointer;" class="isLogin">
          <el-popover placement="bottom" width="200" trigger="click">
            <div class="userDetail">
              <div style="font-size: 18px;font-weight: 600;">{{ nikename }}</div>
              <el-popover
                placement="left"
                width="200"
                trigger="hover"
                >
                <div>
                  作为lv:{{level}}你可以:
                  <br>
                  每日下载{{$store.state.downloadAbles}}张图片
                  <br>
                  <span v-if="!$store.state.isDisplayAd">
                    享受免广告特权
                  </span>
                  <br>
                  <br>
                  <span style="cursor: pointer" @click="dialogLevelTableVisible = true">点我查看等级说明</span>
                </div>

              <div style="width: 200px;display: flex;justify-content: space-evenly;"  slot="reference">
                <div>
                  <span style="font-weight: 600">lv:</span>{{level}}
                </div>
                <div style="width: 140px;">
                <el-progress :percentage="percentage" :format="format" color="#5dbe46"></el-progress>
                </div>
                <div v-if="experience<requireCount">
                  <span style="font-weight: 600">lv:</span>{{level+1}}
                </div>
              </div>
              </el-popover>
              <div style="font-size:12px;color:#b4b4b4;margin-bottom: 10px">
                当前经验:{{experience}}<span v-if="experience<requireCount">,离lv{{level+1}}还差{{requireCount-experience}}</span>
              </div>

              <div class="detailNumber">
                <!-- <a href=""> -->
                <div @click="tofollow">
                  <div>{{ follow }}</div>
                  <div>关注</div>
                </div>
                <!-- </a> -->
                <!-- <a href=""> -->
                <div @click="tofans">
                  <div>{{ fans }}</div>
                  <div>粉丝</div>
                </div>
                <!-- </a> -->
              </div>
              <div class="detailMenu">
                <el-menu @select="handleDetailSelect">
                  <el-menu-item index="1">
                    <i class="el-icon-user"></i>
                    <span>个人中心</span>
                  </el-menu-item>
                  <el-menu-item index="2">
                    <i class="el-icon-upload2"></i>
                    <span>上传图片</span>
                  </el-menu-item>
                  <!-- <el-divider></el-divider> -->
                  <hr>
                  <el-menu-item index="3">
                    <i class="el-icon-switch-button"></i>
                    <span>退出登录</span>
                  </el-menu-item>
                </el-menu>
              </div>
            </div>
            <el-image @mouseenter="getUserInfo" slot="reference" style="width: 40px; height: 40px;border-radius: 50%;"
                      :src="headerUrl" :fit="fit">
            </el-image>
          </el-popover>
          <div class="info">
            <el-badge :value="Number.parseInt(unreadTotal.toString())" class="item" :hidden="unreadTotal===0">
              <el-popover title="消息中心" width="560" trigger="click" @show="clickNotifyBox">
                <div class="notification_box" v-if="notifyNameList.length!==0">
                  <div class="notification_aside">
                    <div v-for="(item,index) in notifyNameList" :key="index" :class="index===notifyActiveIndex?'notification_active':''" @click="clickNotifyMenu(index)">
                    <el-badge :value="item.unreadNumber" class="item" :hidden="item.unreadNumber===0">
                    <div class="notification_menu">
                      {{item.name}}
                    </div>
                    </el-badge>
                    </div>
                  </div>
                  <div class="notification_main">
                    <div v-for="(item,index) in notificationDetail" class="notification_content">
                      <div style="text-align: center;font-size: 12px;margin: 0 auto 10px;">
                        {{item.createTime | filterTime}}
                      </div>
                      <div style=" background: white;
                        border-radius: 10px;
                        padding: 10px;">
                        <h3 v-if="item.title!==null||item.title!==''">{{item.title}}</h3>
                        <p>
                          {{item.content}}
                        </p>
                      </div>
                    </div>
                  </div>
                </div>
                <div v-else>
                  <h2>当前没有信息哦</h2>
                </div>

                <button slot="reference" :style="{color: textColor}"><i class="el-icon-bell"></i></button>
              </el-popover>
            </el-badge>

          </div>
        </div>

        <button @click="handleSelect"
                style="margin-left: 20px;padding: 12px 0px; background-color:#52aa3e;width: 80px;box-sizing: border-box"
                class="loginButton">
          <i class="el-icon-upload2"></i> <span>上传</span>
        </button>

      </div>
    </div>
    <Login/>
    <el-dialog title="等级特权说明" :visible.sync="dialogLevelTableVisible">
      <el-table :data="gridData">
        <el-table-column property="levelName" label="等级" width="150"></el-table-column>
        <el-table-column property="downloadCount" label="每日可下载图片数" width="200"></el-table-column>
        <el-table-column property="requiresExperience" label="需要经验"></el-table-column>
        <el-table-column property="isDisplayAd" label="是否显示广告" :formatter="formatIsDisplayAd"></el-table-column>

      </el-table>
    </el-dialog>

  </div>
</template>

<script>
// 这里可以导入其他文件（比如：组件，工具js，第三方插件js，json文件，图片文件等等）
// 例如：import 《组件名称》 from ‘《组件路径》‘;
import element from 'element-ui'
import Login from '../components/Login.vue'
import pubsub from 'pubsub-js'
import api from '../api/http.js'
import store from '../stores/stores.js'
import {
  Notification
} from 'element-ui'
import {socket} from "../util/websocket";

export default {
  name: 'Header',
  // import引入的组件需要注入到对象中才能使用
  components: {
    Login
  },
  props: {
    main: String,
    fixed: false,
  },
  data() {
    // 这里存放数据
    return {
      gridData: [{
        levelName: '等级一',
        downloadCount: '5',
        requiresExperience: '10',
        isDisplayAd:'显示',
      }, {
        levelName: '等级二',
        downloadCount: '15',
        requiresExperience: '20',
        isDisplayAd:'显示',
      }, {
        levelName: '等级三',
        downloadCount: '50',
        requiresExperience: '30',
        isDisplayAd:'显示',
      }, {
        levelName: '等级四',
        downloadCount: '100',
        requiresExperience: '50',
        isDisplayAd:'免广告',
      }],
      dialogLevelTableVisible:false,
      level:1,
      requireCount:40,
      experience:28,
      downloadCount:1,
      notificationDetail:[
        {
          title:'反馈结果通知',
          content:'您反馈的标签已通过，感谢您为本网站的贡献',
          createTime:'2023-07-05 08:00:00'
        }
      ],
      notifyNameList:[
        {
          name:'系统消息',
        }
      ],
      notifyActiveIndex:0,
      // websocket
      loading: true,
      websocketCount: -1,
      //查询条件
      queryCondition: {
        type: "message",
      },

      search: '',
      activeIndex: '1',
      isLogin: false,
      fit: 'cover',
      headerUrl: require('../img/header.jpg'),
      follow: '55',
      fans: '50',
      nikename: '夏虫',
      dialogTableVisible: true,
      headmain: 'main1',
      textColor: '',
      showSearch: false,
      // unreadTotal:0,

    }
  },
  filters:{
    filterTime(value){
      if (!value) return '';
      const date = new Date(value);
      const year = date.getFullYear();
      const month = ('0' + (date.getMonth() + 1)).slice(-2);
      const day = ('0' + date.getDate()).slice(-2);
      const hour = ('0' + date.getHours()).slice(-2);
      const minute = ('0' + date.getMinutes()).slice(-2);
      const second = ('0' + date.getSeconds()).slice(-2);
      return `${year}年${month}月${day}日 ${hour}:${minute}:${second}`;
    }
  },
  // 监听属性 类似于data概念
  computed: {
    percentage(){

      return this.experience/this.requireCount>1?100:this.experience/this.requireCount*100

    },


    unreadTotal(){
      let count=0
      for(let i=0;i<this.notifyNameList.length;i++){
        count=count+this.notifyNameList[i].unreadNumber
      }
      return count
    }

  },
  // 监控data中的数据变化
  watch: {


  },
  // 方法集合
  methods: {
    formatIsDisplayAd(row) {
      return row.isDisplayAd === 1 ? '是' : '否';
    },
    format(percentage){
      return "";
    },

    clickNotifyBox(){
      this.clickNotifyMenu(0)
    },
    onHead(event) {

      const scrollTop = event.target.scrollingElement.scrollTop
      // console.log(event.target.scrollingElement.scrollTop)
      if (!this.fixed) {
        if (scrollTop > 30) {
          this.headmain = "main2"
          this.textColor = "#656f79"
          this.showSearch = true
        } else {
          this.headmain = "main1"
          this.textColor = '#f0f5ff'
          this.showSearch = false

        }
      }
    },
    clickNotifyMenu(index){
      if(this.notifyNameList.length>0){
      this.notifyActiveIndex=index
      this.notifyNameList[index].unreadNumber=0


      const vm = this
    //   向后端发送请求让该分类的消息改为已读,并且获取信息列表
      api.put("/api/notifications/"+this.$store.state.currentUser.userId+"/"+this.notifyNameList[index].name).then(function (res){

        console.log(res)
        vm.notificationDetail.splice(0, vm.notificationDetail.length, ...res.data.data);

      })
      }

    },
    getUserInfo() {
      const vm = this
      api.get("/api/users/info/").then(function (res) {
        vm.headerUrl = res.data.data.header
        vm.nikename = res.data.data.nickname
        vm.fans = res.data.data.fansListId.length
        vm.follow = res.data.data.followListId.length
        vm.experience=res.data.data.experience
      }).catch(function (err) {
        console.log(err)
      })
    },
    fixeds() {
      this.headmain = "main2"
      this.textColor = "#656f79"
      this.showSearch = true
    },
    tofollow() {
      this.$router.push({path: '/Accounts', query: {itemId: 4}})
    },
    tofans() {
      this.$router.push({path: '/Accounts', query: {itemId: 5}})
    },
    onSearch() {

      this.$router.push({path: `/Search/${this.search}`})

    },
    onLogin() {
      pubsub.publish('getLoginSign', true)
    },
    handleSelect() {
      console.log(this.$router)
      this.$router.push({path: '/Accounts', query: {itemId: 2}})

      pubsub.publish('getLoginSign', this.$store.state.showLoginDialog)
    },
    handleDetailSelect(tab) {
      const router = this.$router
      if (tab == '3') {
        api.post("/api/users/logout").then(function (res) {

          store.dispatch('login')
          router.go(0)
        }).catch(function (err) {
          Notification({
            message: err.data.message,
            type: 'error'
          })
        })
      } else if (tab == '1') {
        this.$router.push({path: '/Accounts', query: {itemId: 1}})
      } else if (tab == '2') {
        this.$router.push('/Accounts')
      }
    },
    onlog() {
      this.$router.push('/')
    },

    //   websocket
    init() {
      this.queryCondition.type = "message";
      socket.sendMsg(JSON.stringify(this.queryCondition));
    },
    websocketOnMessage(event) {
      //初始化界面时，主动向后台发送一次消息，获取数据
      this.websocketCount += 1;
      if (this.websocketCount === 0) {
        this.init();
      }
      let info = JSON.parse(event.data);
      switch (info.type) {
        case "heartbeat":
          socket.websocketState = true;
          break;
        case "message":
          this.loading = true;
          this.$nextTick(() => {
            this.consumeMessage(info);
          })
          break;
        case "error":
          this.loading = false;
          break;
      }
    },
    consumeMessage(info) {
      //拿到最新数据重新渲染界面
      Notification({
        message:info.data,
        type:'info'
      })
      this.initInfo()
      console.log(info)

    },
    initGridData(){
      const vm = this
      api.get("/api/levels").then(function (res){
        vm.gridData=res.data.data.level
        vm.$store.state.downloadCount=res.data.data.count
        for(let i=0;i<vm.gridData.length;i++){
          if(vm.$store.state.currentUser.experience<vm.gridData[i].requiresExperience){
            vm.$store.state.downloadAbles=vm.gridData[i-1].downloadCount
            vm.requireCount=vm.gridData[i].requiresExperience
            vm.level=i
            vm.$store.state.isDisplayAd=vm.gridData[i].isDisplayAd
            break
          }
        }
        if(vm.$store.state.currentUser.experience>=vm.gridData[vm.gridData.length-1].requiresExperience){
          vm.$store.state.downloadAbles=vm.gridData[vm.gridData.length-1].downloadCount
          vm.requireCount=vm.gridData[vm.gridData.length-1].requiresExperience
          vm.$store.state.isDisplayAd=vm.gridData[vm.gridData.length-1].isDisplayAd

          vm.level=vm.gridData.length
        }

        console.log(res)
      }).catch(function (err){

      })

    },

    initInfo(){
      const vm =this
      // if (!this.$store.state.currentUser)
      setTimeout(() => {
        api.get("/api/notifications/senderList/"+this.$store.state.currentUser.userId).then(function (res){
          vm.notifyNameList=res.data.data
          console.log(res)
        }).catch(function (err){

        })
        // 在这里执行您想要执行的操作
      }, 1000);

    },

  },
  // 生命周期 - 创建完成（可以访问当前this实例）
  created() {
//初始化websocket对象
    //window.location.host获取ip和端口，
    //process.env.VUE_APP_WEBSOCKET_BASE_API获取请求前缀
    if (this.$store.state.currentUser){
    socket.initWebSocket(
      `ws:localhost:8082/websocket/` +
      this.$store.state.currentUser.userId
    );
    //绑定接收消息方法
    socket.websocket.onmessage = this.websocketOnMessage;
    }
  },
  // 生命周期 - 挂载完成（可以访问DOM元素）
  mounted() {
    this.initGridData()
    if (this.fixed)
      this.fixeds()

    window.addEventListener('scroll', this.onHead)
    if (!this.$store.state.currentUser) {
      // console.log(this.$store.state.currentUser)
      this.isLogin = false
    } else {
      this.initInfo()
      this.isLogin = true
      const userDetail = this.$store.state.currentUser
      this.headerUrl = userDetail.header
      this.nikename = userDetail.nickname
      this.fans = userDetail.fansListId.length
      this.follow = userDetail.followListId.length
      this.experience=userDetail.experience
    }
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
    window.removeEventListener('scroll', this.onHead)
  }, // 生命周期 - 销毁之前
  destroyed() {
    // socket.websocketOnClose()

  }, // 生命周期 - 销毁完成
  activated() {
  } // 如果页面有keep-alive缓存功能，这个函数会触发
}
</script>
<style scoped>

/deep/
.el-progress-bar{
  padding: 0px;
}

.notification_content {
//border: 1px solid #000; margin-bottom: 30px;
}

.notification_active {
  background: #eaeaea;
}

.notification_menu:hover {
  background: #eaeaea;
}

.notification_menu {
  padding: 10px;
  color: #000;
  transition: .1s;
  width: 100px;
  //box-sizing: border-box;
  cursor: pointer;
}

.notification_main {
  padding: 0px 20px 10px 20px;
  height: 400px;
  background: #f5f5f5;
  display: flex;
  flex-direction: column-reverse;
  width: 100%;
  overflow-y: auto;
}

.notification_aside {
  border-right: 1px solid #8c939d;
  width: 130px;
  box-sizing: border-box;

}

.notification_box {
  background: #fafafa;
  display: flex;
}

.info {
  margin-left: 10px;
  display: flex;
  width: 40px;
  height: 40px;
}

.info button {
  width: 40px;
  height: 40px;
  background-color: rgba(191, 191, 191, 0);
  border: none;
  cursor: pointer;
  color: #ffffff;
}

.info:hover {
  background-color: rgba(156, 156, 156, 0.5);
  border-radius: 50%;
}

.isLogin {
  display: flex;
}

.loginButton {
  display: inline-block;
  line-height: 1;
  cursor: pointer;
  background: rgba(191, 191, 191, 0);
  color: #f0f5ff;
  -webkit-appearance: none;
  text-align: center;
  margin: 0;
  transition: .1s;
  font-weight: 600;
  padding: 12px 20px;
  font-size: 14px;
  border-radius: 14px;
  border: none;
}

.loginButton:hover {
  background-color: rgba(191, 191, 191, 0.5);
}

.main2 {
  height: 64px;
  width: 100%;
  display: flex;
  position: fixed;
  top: 0;
  right: 0;
  flex-direction: row;
  align-items: center;
  justify-content: space-around;
  background-color: white;
  opacity: 1;
  z-index: 99;
  color: #656f79;
  border-bottom: 1px solid #e0e0e0;
}

.main1 {
  height: 64px;
  width: 100%;
  display: flex;
  position: fixed;
  top: 0;
  right: 0;
  padding: 0;
  flex-direction: row;
  align-items: center;
  justify-content: space-around;
  opacity: 1;
  /* background-color: white; */
  color: white;
}

.menu li {
  width: 100px;
  /* height: 72px; */
}

div {
  /* border: 1px solid; */
}

.logo {
  width: 200px;
  height: 70px;
  cursor: pointer;
}

.detailMenu {
  margin-top: 10px;
}

.detailMenu ul {
  border: none;
}

.detailMenu li {
  width: 200px;
  height: 50px;
  line-height: 50px;
}

.detailNumber {
  width: 200px;
  display: flex;
  flex-direction: row;
  justify-content: space-around;
  align-items: center;
  cursor: pointer;
}

.userDetail {
  flex-direction: column;
  display: flex;
  align-items: center;
}
.menu {
  /* margin-left: 200px; */
  display: flex;
  align-items: center;
}
</style>
