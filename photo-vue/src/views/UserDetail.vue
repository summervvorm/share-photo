<template>
  <div class="box">
    <el-container>

      <div class="topImg"
           :style="{backgroundImage: pictureInfos.length!==0?`url(`+pictureInfos[0].picName+`)`:`url('http://arrows.oss-cn-beijing.aliyuncs.com/photos/photos/23334877c71fecedbf04a18a1055956a2.jpg')`}">
        <div class="overlay--ppwJy">
        </div>
      </div>
      <el-header style="margin-bottom: 20px;padding: 0;">
        <transition name="fade">
          <Header></Header>
        </transition>
      </el-header>
      <div style="position: relative">
        <div class="avatar">
          <el-avatar :size="130" :src="authorInfo.header" fit="cover" lazy>
            <div slot="placeholder" class="image-slot">
              <el-skeleton-item
                variant="image"
                style="width: 130px; height: 130px;border-radius: 50%"
              />
            </div>
          </el-avatar>
        </div>
      </div>
      <div class="authorInfo">
        <div style="display: flex;justify-content: space-between;align-items: center;">
          <div style="display: flex;align-items: center;">
            <div style="font-weight: 800;font-size: 32px;">{{ authorInfo.nickname }}</div>
            <div class="follow" @click="clickFollow" v-if="authorInfo.userId!==this.$store.state.currentUser.userId">
              <div>
                <i v-if="!isFollow" class="fa-solid fa-plus" style="margin-right: 5px;"></i><span
                v-if="!isFollow" style="margin-left: 10px">关注 {{ authorInfo.fansCount }}</span>
                <span v-if="isFollow">已关注</span>
              </div>
            </div>
          </div>
          <el-tooltip v-if="authorInfo.userId!==this.$store.state.currentUser.userId" placement="top-start" content="发送消息" effect="dark">

            <div class="messageBox" @click="sendMsgDialog=true">
              <i class="fa-solid fa-comments"></i>
            </div>
          </el-tooltip>

        </div>
        <div style="display: flex;margin-top: 10px">
          <!--      <div>-->
          <!--        {{}} <span style="font-size: 14px;color: #757575">个关注者</span>-->
          <!--      </div>-->
          <div>
            {{ authorInfo.fansCount }} <span style="font-size: 14px;color: #757575">个粉丝</span>
          </div>
        </div>
        <div style="display: flex;margin-top: 10px;font-size: 14px;color: #757575">
          注册时间:{{ authorInfo.createTime.replace("T", " ") }}
        </div>
        <div style="display: flex;margin-top: 10px;font-size: 14px;">
          <div style="margin-right: 20px">
            喜欢:{{ authorInfo.likesCount }}
          </div>
          <div>
            收藏:{{ authorInfo.favCount }}
          </div>
        </div>
        <div>
          <div class="picNav">
            图片集
          </div>
          <div>
            <el-main class="main">
              <div class="picContent">
                <div class="contentColumn" v-for="(items,index) in pictureInfos" :key="index">
                  <div style="width: 100%;">
                    <ImageBox :itemprop="items" @clickMark="handleClickMark(items)"></ImageBox>
                  </div>
                </div>
              </div>
            </el-main>
          </div>
          <div class="footer">
            <el-pagination
              background
              layout="prev, pager, next"
              :total=count
              :page-size="40"
              :current-page="current" prev-text=" 上一页 " next-text=" 下一页 "
              @prev-click="handlePrevClick"
              @next-click="handleNextClick"
              @current-change="handleCurrentChange"
            >
            </el-pagination>
          </div>
        </div>
      </div>
    </el-container>
    <el-dialog
      title="收藏图片"
      :visible.sync="saveImageDialog"
      width="450px"
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


    <el-dialog
      title="发送消息"
      :visible.sync="sendMsgDialog"
      width="450px"
      center>
      <div style="align-items: center">
      <div style="font-weight: 600;font-size: 16px;margin-bottom: 10px">主题:</div><el-input placeholder="添加一个主题"  v-model="theme"></el-input>
      </div>

      <div>
        <div style="font-weight: 600;font-size: 16px;margin-bottom: 10px;margin-top: 20px">消息:</div>
        <el-input  type="textarea" maxlength="1000" show-word-limit  :autosize="{ minRows: 4, maxRows: 100}" placeholder="输入内容"  v-model="content"></el-input>
      </div>

      <span slot="footer" class="dialog-footer">
    <el-button @click="sendMsgDialog = false">取 消</el-button>
    <el-button type="primary" @click="sendMsg">提 交</el-button>
  </span>
    </el-dialog>
  </div>
</template>

<script>

import Vue, {defineComponent} from "vue";
import Header from "../components/Header.vue";
import ImageBox from "../components/ImageBox.vue";
import api from "../api/http";
import {Notification} from "element-ui";
import {url} from "socket.io-client/build/esm-debug/url";

export default {
  name: 'UserDetail',

  data() {
    return {
      isFollow: true,
      isShowNew: false,
      sendMsgDialog: false,
      saveImageDialog: false,
      theme:"",
      content:'',
      newFavName: '',
      saveFavList: [],
      favLists: [],
      tags: [],
      count: 1,
      current: 1,
      size: 20,
      status: 0,
      keywords: '',
      category: '',
      pictureInfos: [],
      authorId: 1,
      authorInfo: {

        avatar: 'http://arrows.oss-cn-beijing.aliyuncs.com/photos/header/7bce73511036739c62033fcd4f20ec3d1.jpeg',
        nickname: 'user',
        fansCount: 2,
        createTime: 'T',
      },
      AuthorId: 2,
    }
  },
  methods: {
    clickImgDetail() {

    },
    sendMsg() {
      const data={
        title:this.theme,
        senderId:this.$store.state.currentUser.userId,
        sender:this.$store.state.currentUser.nickname,
        receiverId:this.authorId,
        content:this.content
      }

    api.post("/api/notifications",data).then((res)=>{
      console.log(res)
      this.$message({
        message:'操作成功',
        type:'success'
      })
      this.sendMsgDialog=false
    }).catch((err)=>{

    })

    },
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
      console.log(fav)
      if (fav.length !== 0) {
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
    clickFollow() {
      if (!this.isFollow) {
        this.isFollow = true
      } else
        this.isFollow = false
      const FollowVO = {}
      FollowVO.toFollow = this.isFollow
      FollowVO.userId = this.authorId
      FollowVO.userName = this.authorInfo.nickname
      FollowVO.fansId = this.$store.state.currentUser.userId
      FollowVO.fansName = this.$store.state.currentUser.userName

      const authors = this.authorInfo
      api.post("/api/users/follow", FollowVO).then(function (res) {
        if (FollowVO.toFollow) {
          Notification(
            {
              message: "关注成功",
              type: 'success'
            }
          )
          authors.fansCount++
        } else {
          Notification(
            {
              message: "取消关注成功",
              type: 'success'
            }
          )
          authors.fansCount--
        }
      }).catch(function (err) {
        console.log(err)
      })
    },

    handlePrevClick() {
      this.current--
      this.getPicInfos()
    },
    handleNextClick() {
      this.current++;
      this.getPicInfos()

    },
    handleCurrentChange(currentPage) {
      this.current = currentPage
      this.getPicInfos()

    },
    getAuthor() {
      const vm = this

      api.get("/api/users/author/" + this.authorId).then(function (ans) {
        console.log(ans)
        vm.authorInfo = ans.data.data
        vm.checkFollow()
      }).catch(function (err) {

      })
    },
    checkFollow() {
      const userId = this.authorId
      const fansId = this.$store.state.currentUser.userId
      const vm = this
      api.get("/api/users/follow/" + userId + "/" + fansId).then(function (res) {

        vm.isFollow = res.data.data
      }).catch(function (err) {
        console.log(err)
      })
    },
    getPicInfos() {
      const userId = this.$store.state.currentUser.userId
      const vm = this
      const params = {
        current: this.current,
        size: this.size,
        keywords: this.keywords,
        category: this.category,
        status: this.status
      }
      api.get("/api/picture/images/upload/" + this.authorId, {
        params
      }).then(function (res) {

        api.get("/api/picture/likeAndFav/images").then(function (res2) {
          for (let i1 = 0; i1 < res.data.data.data.length; i1++) {
            res.data.data.data[i1].isLike = !!res2.data.data.likePicIdList.includes(res.data.data.data[i1].picId);
            res.data.data.data[i1].isFav = !!res2.data.data.favPicIdList.includes(res.data.data.data[i1].picId);
          }
          vm.pictureInfos.splice(0, vm.pictureInfos.length, ...res.data.data.data)

        })

        console.log(res)
        vm.count = res.data.data.count
      }).catch(function (err) {
        console.log(err)
      })
    }

  },
  components: {ImageBox, Header},
  mounted() {
    this.authorId = this.$route.params.authorId

    this.getAuthor()
    this.getPicInfos()
    this.getFavList()

  }
}
</script>


<style scoped>
.messageBox:hover {
  background: rgba(199, 199, 199, 0.96);
  border-radius: 10px;
}

.messageBox {
  margin-right: 20px;
  cursor: pointer;
  padding: 7px 14px;
  transition: .1s;
  border-radius: 10px;
  border: 1px solid #bdbcbc;
}

.picNav {
  display: flex;
  margin-top: 20px;
  width: 80px;
  background: rgba(234, 229, 229, 0.76);
  border-radius: 10px;
  font-size: 18px;
  font-weight: 600;
  padding: 10px 20px;
  justify-content: center;
}

.main {
  position: relative;
  text-decoration: inherit;
  vertical-align: inherit;
  overflow: hidden;
  /*height: 2800px;*/
  margin-bottom: 50px;
}


.picContent {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  text-decoration: inherit;
  vertical-align: inherit;
  flex-wrap: wrap;
}

.contentColumn {
  display: flex;
  flex: 0.5 0.5 auto;
  margin: 10px;
  height: 300px;
}

.authorInfo {
  padding: 20px;
}

.follow:hover {
  background: #5dbe46;
}

.follow {
  margin-top: 6px;
  margin-left: 20px;
  display: flex;
  align-items: center;
  padding: 2px 10px;
  background: rgb(82, 170, 62);
  height: 24px;
  color: white;
  cursor: pointer;
  transition: .1s;
  border-radius: 7px;
}

.avatar {
  position: absolute;
  width: 130px;
  height: 130px;
  left: 40px;
  top: -159px;
  border-radius: 50%;
  border: 3px solid white;
}

.box {
//height: 121vh;
}

.topImg {
  position: relative;
//background-image: url("../img/headImage.jpg"); background-size: cover;
  z-index: 0;
  width: 100%;
  height: 240px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.overlay--ppwJy {
  position: absolute;
  left: 0;
  top: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(0deg, rgba(25, 27, 38, .32), rgba(25, 27, 38, .72));
  height: 240px;
}

</style>
