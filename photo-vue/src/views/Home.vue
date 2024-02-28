<template>
  <!-- <Login/> -->
  <div>
    <el-container>
      <div class="topImg" style="width: 100%;">
        <div class="overlay--ppwJy">
        </div>
        <div class="topMain" style="width: 100%;">
          <h1 style="font-size: 34px;">精彩的免版税图片和免版税库存</h1>
          <h2 style="font-size: 14px;">拥有优质图片素材，让你轻松应对各种设计场景</h2>
          <div style="width: 60%;height: 70px;margin-top: 40px;">
            <el-input style="width: 100%;" placeholder="请输入内容" v-model="search">
              <el-button slot="append" icon="el-icon-search" @click="onSearch"></el-button>
            </el-input>
          </div>
        </div>

      </div>

      <el-header style="margin-bottom: 20px;padding: 0;">
        <transition name="fade">
          <Header></Header>
        </transition>
      </el-header>
      <el-container>
        <el-main class="main">
          <div class="mainTop">
            <div class="tag">
              <div v-for="(item,index) in tags" :key="index">
                <router-link style="color: black" :to="{ path: '/Search/'+item,}">{{ item }}</router-link>
              </div>
            </div>
            <div class="option">
              <el-radio-group v-model="radio1">
                <el-radio-button label="最新"></el-radio-button>
                <el-radio-button label="最热"></el-radio-button>
              </el-radio-group>
            </div>
          </div>
          <!-- <el-col :span="6" v-for="item in 110" :key="item" v-bind:style="{ height:200+(item*20)%100+ 'px' }"> -->
          <!-- 瀑布流项的内容 -->
          <div class="picContent">
            <!-- <waterfall :col="4" column-width="400px" :data="data1">
              <template>
                <div v-for="item in 110" :key="item" v-bind:style="{ height:200+(item*20)%100+ 'px',width:'20%'}">
                  {{item}}
                </div>
              </template>
            </waterfall> -->
            <div class="contentColumn" v-for="(items,index) in pictureInfos" :key="index">
              <div v-for="(item,index2) in items" :key="item.picId">
                <!-- {{item}} -->
                <ImageBox :itemprop="item" @clickMark="handleClickMark"></ImageBox>
                <!--                {{item}}-->
              </div>
            </div>

          </div>
          <div class="contentButtom">
            <div>
              <a href="">
                <span>发现更多</span>
              </a>
            </div>
          </div>


        </el-main>
      </el-container>
      <el-footer>
        <div class="footer">
          <!--          我是页脚-->
        </div>

      </el-footer>


    </el-container>
    <el-dialog
      title="收藏图片"
      :visible.sync="saveImageDialog"
      width="30%"
      center>
      <div class="saveImage">
        <div>
          <div style="margin-bottom: 10px">选择收藏夹</div>
          <el-checkbox-group v-model="saveFavList" style="border: 1px solid #ececec;padding: 10px; border-radius: 10px">
            <el-checkbox style="width: 80%;margin: 5px 10px;box-sizing: border-box;" v-for="(item,index) in favLists"
                         :label="index" :key="index" border>{{ item.favName }}
            </el-checkbox>
          </el-checkbox-group>

        </div>
        <el-input style="width: 80%;margin-top: 10px" v-if="isShowNew" v-model="newFavName" placeholder="请输入内容">
          <el-button @click="creatNewFav" slot="append">
            创建
          </el-button>

        </el-input>
        <div v-if="!isShowNew" style="width: 80%;height: 40px;
          border-radius: 10px;border: 1px solid #ffffff;
          cursor: pointer;display: flex;color: white;
          align-items: center;
          margin: 10px auto;
          background: rgba(93,190,70,0.74);
          justify-content: center;" @click="isShowNew=!isShowNew">
          + 创建新收藏夹
        </div>
      </div>
      <span slot="footer" class="dialog-footer">
    <el-button @click="saveImageDialog = false">取 消</el-button>
    <el-button type="primary" @click="saveImage">提 交</el-button>
  </span>

    </el-dialog>


  </div>
</template>

<script>
import pubsub from 'pubsub-js'
import Header from '../components/Header.vue'
import ImageBox from '../components/ImageBox.vue'
import api from '../api/http.js'
import Vue from "vue";
import '@fortawesome/fontawesome-free/css/all.css'
import {Notification} from "element-ui";

export default {
  name: 'Home',
  data() {
    return {
      newFavName: '',
      isShowNew: false,
      favLists: [],
      saveFavList: [],
      saveImageDialog: false,

      dialogVisible: false,
      isLogin: false,
      loggedIn: false,
      asideSrc: require('../img/header.jpg'),
      data1: [1, 2, 3, 4],
      search: '',
      radio1: '最热',
      tags: new Set([]),
      pictureInfos: [[], [], [], []],
      pictureNewInfos: [[], [], [], []],
      emitPicInfo: {},
    }
  },
  methods: {
    clickHotPic() {

    },
    clickNewPic() {

    },
    //接收ImageBox的图片信息，处理收藏操作
    handleClickMark(data) {
      this.emitPicInfo = data
      this.saveImageDialog = true
    },
    saveImage() {
      const res = this.favLists.filter((item, index) => {
        return this.saveFavList.indexOf(index) !== -1;
      })
      const favPicId = this.emitPicInfo.picId
      const favUserId = this.$store.state.currentUser.userId

      const favAddTimes = new Date()
      const year = favAddTimes.getFullYear()
      const month = ('0' + (favAddTimes.getMonth() + 1)).slice(-2)
      const day = ('0' + favAddTimes.getDate()).slice(-2)
      const hour = ('0' + favAddTimes.getHours()).slice(-2)
      const minute = ('0' + favAddTimes.getMinutes()).slice(-2)
      const second = ('0' + favAddTimes.getSeconds()).slice(-2)
      const favAddTime = `${year}-${month}-${day} ${hour}:${minute}:${second}`
      const fav = []
      for (let i = 0; i < res.length; i++) {
        fav.push({
          "favPicId": favPicId,
          "favCollectName": res[i].favName,
          "createTime": favAddTime,
          "favUserId": favUserId,
          "favListId": res[i].id,
        })
      }
      const vm = this
      const vue = Vue
      if (fav.length != 0) {
        api.post("/api/favorites/imageList", fav).then(function (res) {
          vm.getFavList()
          let i = Math.floor(vm.emitPicInfo.indexs / 10)
          let j = vm.emitPicInfo.indexs % 10
          vm.emitPicInfo.isFav = true
          console.log(i + " " + j + " ")
          console.log(vm.emitPicInfo)
          vue.set(vm.pictureInfos[i], j, vm.emitPicInfo)
        }).catch(function (err) {

        })
        // this.checkFav = true
        this.saveImageDialog = false
      }


    },
    creatNewFav() {
      if (this.newFavName.length >= 1) {
        this.isShowNew = !this.isShowNew
        const item = {
          favCollectName: this.newFavName
        }


        const data = {}
        data.userId = this.$store.state.currentUser.userId
        data.favName = this.newFavName
        data.favType = 0
        console.log(data)
        const vm = this
        api.post("/api/favorites/favoritesList", data).then(function (res) {
          console.log(res)
          vm.favLists.push(res.data.data)
        }).catch(function (err) {

        })
      } else {
        Notification({
          message: "收藏夹名不能为空！",
          type: "warning"
        })
      }
    },
    getFavList() {
      const vm = this
      api.get("/api/favorites/imagesList").then(function (res) {
        console.log(res)
        vm.favLists = res.data.data
        vm.favLength = res.data.data.length
      }).catch(function (err) {
      })
    },
    demo(msgname, data) {
      this.isLogin = data
      if (data) {
        this.dialogVisible = false
      }
    },
    onSearch() {
      this.$router.push({path: `/Search/${this.search}`})
    },
    getPicInfos(type) {
      const x = this
      const vm = Vue
      api.get("/api/picture/typeImages" + type).then(function (res) {
        // this.pictureInfos=res.data.data
        // Vue.set(this.pictureInfos,res.data.data)
        console.log(res)
        api.get("/api/picture/likeAndFav/images").then(function (res2) {
          console.log(res2)

          for (let j = 0; j < 10; j++) {
            for (let i = 0; i < 4; i++) {

              // x.pictureInfos[i][j]=res.data.data[i*10+j]
              if (res2.data.data !== undefined) {
                res.data.data[i * 10 + j].isLike = !!res2.data.data.likePicIdList.includes(res.data.data[i * 10 + j].picId);

                res.data.data[i * 10 + j].isFav = !!res2.data.data.favPicIdList.includes(res.data.data[i * 10 + j].picId);
              }
              res.data.data[i * 10 + j].indexs = i * 10 + j
              vm.set(x.pictureInfos[i], j, res.data.data[i * 10 + j])
              if (x.tags.size <= 10) {
                for (let q of res.data.data[i * 10 + j].tags) {
                  x.tags.add(q.tagName)
                }
              }

            }
          }
          console.log(x.pictureInfos)
          console.log(x.tags)
          // console.log(res.data)
        }).catch(function (err) {
        })


      }).catch(function (err) {
        console.log(err)
      })
    }
  },
  mounted() {
    this.getPicInfos('hot')
    this.pubId = pubsub.subscribe('demo', this.demo)
    this.getFavList()
    // this.$on('clickMark', this.handleClickMark)
  },
  watch: {
    radio1(newVal, oldVal) {
      if (newVal === '最新') {
        this.getPicInfos('new')
      } else {
        this.getPicInfos('hot')
      }

    }
  },
  beforeDestroy() {
    pubsub.unsubscribe(this.pubId) //取消订阅

  },
  beforeMount() {
    // this.getPicInfos()
  },

  components: {
    Header,
    ImageBox,
    // Waterfall
  }
}
</script>

<style scoped>
.footer {
  border-top: 1px solid #d2d2d2;
}

.saveImage {
  display: flex;
  flex-direction: column;
}

.main {
  position: relative;
  text-decoration: inherit;
  vertical-align: inherit;
  overflow: hidden;
  height: 2800px;
  margin-bottom: 50px;
}

.contentButtom a {
  text-decoration: none;
  color: inherit;
}

.contentButtom div {
  padding: 10px 26px;
  border-radius: 18px;
  cursor: pointer;
  margin-bottom: 10px;
  background: #ffffff;
  border: 1px solid #b0b0b0;
}

.contentButtom div:hover {
  background: #efefef;
}

.contentButtom {
  position: absolute;
  z-index: 5;
  left: 0;
  bottom: -1px;
  width: 100%;
  display: flex;
  align-items: flex-end;
  justify-content: center;
  height: 192px;
  background: linear-gradient(180deg, hsla(0, 0%, 100%, 0), #fff);

}

.picContent {
  margin-top: 40px;
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  text-decoration: inherit;
  vertical-align: inherit;
}

.contentColumn {
  display: flex;
  flex-direction: column;
  width: 24%;
}

.contentColumn div {
  width: 100%;
  /*border: 1px solid black;*/
  margin-bottom: 20px;
}


.tag {
  display: flex;
  align-items: center;
}

.tag div {
  transition: .3s;
  cursor: pointer;
  height: 30px;
  line-height: 30px;
  padding: 1px 10px;
//word-wrap: inherit; //min-width: 30px; margin-right: 20px; border: 1px solid #c5c5c5;
  border-radius: 8px;
//box-sizing: border-box; //display: flex; align-items: center;
}

.tag div:hover {
  border-color: black;
}

.tag a {
  text-decoration: none;
}

.mainTop {
  display: flex;
  justify-content: space-between;
//min-width: 1400px;
}

.fade {
  transition: opacity 0.5s;
}

.topImg {
  background-image: url("../img/headImage.jpg");
  background-size: cover;
  z-index: 0;
  width: 100%;
  height: 440px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.topMain {
  z-index: 9;
  color: white;
  display: flex;
  align-items: center;
  flex-direction: column;
}

/* .el-input{
  height: 70px;
} */
.overlay--ppwJy {
  position: absolute;
  left: 0;
  top: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(0deg, rgba(25, 27, 38, .32), rgba(25, 27, 38, .72));
  height: 440px;
}
</style>
