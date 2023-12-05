<template>
  <div class="box">
    <el-header style="margin-bottom: 20px;padding: 0;	">
      <transition name="fade">
        <Header :fixed="true"></Header>
      </transition>
    </el-header>
    <div class="main">
      <div class="main-top">
        <div class="main-top-left">
          <router-link :to="{ path: '/UserDetail/'+authorDetail.userId}">
          <div class="header">
            <el-image :src="authorDetail.header" fit="cover"
                      style="width: 48px; height: 48px;border-radius: 50%;"></el-image>
          </div>
          </router-link>
          <div class="user-detail">
            <div style="font-weight: 600;margin-bottom:8px">{{ authorDetail.nickname }}</div>
            <div style="font-size: 14px">{{ authorDetail.sign }}</div>
          </div>
          <div class="follow" @click="clickFollow">
            <div>
              <i v-if="!isFollow" class="fa-solid fa-plus" style="margin-right: 5px;"></i><span
              v-if="!isFollow" style="margin-left: 10px">关注 {{ authorDetail.fansCount }}</span>
              <span v-if="isFollow">已关注</span>
            </div>
          </div>
        </div>
        <div class="main-top-right">
          <div :class="checkLike?'main-top-right-button2':'main-top-right-button'" @click="clickLike">
            <i :class="checkLike?'fa-solid fa-heart':'fa-regular fa-heart'"></i>{{ image.likes }}
          </div>
          <div slot="reference" style="width: 43px;" :class="checkFav?'main-top-right-button2':'main-top-right-button'"
               @click="clickFav">
            <i :class="checkFav?'fa-solid fa-bookmark':'fa-regular fa-bookmark'"></i>
          </div>
          <div class="download" @click="clickDownload">
            <span>
               <i class="fa-solid fa-download"></i> 下载
           </span>
          </div>
          <a :href="image.src" ref="link" style="display: none">
          </a>
        </div>
      </div>
      <div class="main-content">
        <div class="image" style="position: relative" @mouseenter="enterContent" @mouseleave="leaveContent">
          <el-image :src="image.src" :fit="'cover'">
          </el-image>
          <div class="imageDetail" :style="touming">
            <el-tooltip class="item" effect="dark" content="举报此内容" placement="top">
              <div class="DetailIcon" @click="reportImg"><i class="fa-solid fa-flag"></i></div>
            </el-tooltip>

          </div>
        </div>


      </div>
      <div class="description">
        <div class="tags">
          <span><i class="fa-solid fa-tag"></i></span>
          <div style="padding: 0px 5px;color: #7f7f7f" v-for="(item,index) in tags" :key="index">
            {{ item.tagName }}
          </div>
        </div>
        <div class="detail">
          <div class="detail-but">
            <span><i class="fa-solid fa-share-nodes"></i></span>
          </div>
          <div class="detail-but" @click="clickDetail">
            <span><i class="fa-solid fa-circle-info"></i> 细节</span>
          </div>
        </div>
      </div>
      <div class="descriptionBox">
        <el-drawer
          title="图片细节"
          :visible.sync="drawer"
          :with-header="true"
          size="450px">
          <div class="desBox-count">
            <div style="display: flex;justify-content: center;">
              <div style="width: 100px">
                <i class="fa-solid fa-heart"></i>
                {{ image.likes }}
                <br>
                喜欢
              </div>
              <div style="width: 100px">
                <i class="fa-solid fa-bookmark"></i>
                {{ image.fav }}
                <br>
                收藏
              </div>
            </div>
            <div style="font-size: 14px;margin-top: 10px">
              分辨率:{{image.width}}x{{image.height}}px
            </div>
            <div style="font-size: 14px;margin-top: 10px">
              大小:{{image.size}}
            </div>
            <div style="margin-top: 10px;font-size: 14px">
              发布日期:{{ image.creatTime }}
            </div>
          </div>
          <div class="line"></div>
          <div class="desBox-tag">
            <div style="font-weight: 600;margin-bottom: 20px">标签</div>
            <div class="des-tag">
              <div class="des-but" v-for="(tag,index) in tags" :key="index">
                {{ tag.tagName }}
              </div>
            </div>
            <div class="des-report" @click="clickReportTag">
              <i style="margin-right: 5px" class="fa-solid fa-flag"></i>提交标签
            </div>
          </div>
          <div class="line"></div>
          <div class="des-des">
            <div style="font-weight: 600;margin-bottom: 20px">描述</div>
            <div>
              {{ image.description }}
            </div>
          </div>
          <div class="line"></div>
          <div class="report" @click="reportImg">报告图片问题</div>
        </el-drawer>
      </div>
      <div class="advertising">

      </div>
      <div class="main-comment">
        <div class="comment-left">
          <div style="font-weight: 600;font-size: 16px;width: 100px;display: flex;align-items: center;">
            <span style="margin-right: 5px">{{ commentList.length }}</span>条评论
          </div>
          <div class="comment-inputs">
            <div class="comment-inputs-avatar">
              <img :src="userDetail.header"
                        style="width: 48px; height: 48px;border-radius: 50%;object-fit: cover">
              </img>
            </div>
            <div class="comment-inputs-content">
              <el-input
                type="textarea"
                placeholder="请输入您的评论"
                v-model="commentContent"
                maxlength="120"
                show-word-limit
                resize="none"
              >
              </el-input>
              <div class="comment-content-but" @click="commitComment">
                评论
              </div>
            </div>

          </div>
          <div class="line"></div>
          <div class="comment-list">
            <div v-for="(item,index) in commentList" :key="index" class="comment-list-item">

              <div class="comment-avatar">
                <el-image :src="item.userAvatar" fit="cover"
                          style="width: 42px; height: 42px;border-radius: 50%;"></el-image>
              </div>
              <div class="comment-item-right">
                <div style="font-size: 16px;font-weight: 600;margin-bottom: 10px;display: flex;align-items: center;">
                  <div>{{ item.userName }}</div>
                  <div style="font-weight: 400;font-size: 12px;margin-left: 10px">
                    {{ item.createTime.replace("T", " ") }}
                  </div>
                </div>
                <div class="comment-text">
                  {{ item.content }}
                </div>
              </div>

            </div>

          </div>
        </div>
        <div class="comment-right">
          <div style="font-weight: 600;font-size: 16px;width: 100px;display: flex;align-items: center;">
            <span style="margin-right: 5px">关于创作者</span>
          </div>
          <div style="display: flex;margin-top: 20px;margin-bottom: 20px">
            <div>
              <el-image :src="authorDetail.header" fit="cover"
                        style="width: 48px; height: 48px;border-radius: 50%;"></el-image>
            </div>
            <div style="display: flex;flex-direction: column;
    align-items: flex-start;margin-left: 15px">
              <div style="font-weight: 600;margin-bottom: 10px">{{ authorDetail.nickname }}</div>
              <div style="color: #7f7f7f;font-size: 14px">粉丝:{{ authorDetail.fansCount }}</div>
            </div>
          </div>
          <div class="line"></div>
          <div style="display: flex;margin-top: 20px;font-size: 10px">
            {{ authorDetail.sign }}
          </div>

          <div style="display: flex;margin-top: 20px;color: #7f7f7f">
            <div style="margin-right: 36px">点赞总数: <i class="fa-solid fa-heart"></i> {{ authorDetail.likesCount }}</div>
            <div>收藏总数: <i class="fa-solid fa-bookmark"></i> {{ authorDetail.favCount }}</div>
          </div>
          <div
            style="display: flex;cursor:pointer;width: 60px;margin-top: 30px;border: 1px solid black;padding: 5px;border-radius: 7px;justify-content: center;">
            捐 赠
          </div>
        </div>
      </div>
      <div class="main-other">

      </div>
      <div class="footer">

      </div>
      <el-dialog
        title="提交标签"
        :visible.sync="centerDialogVisible"
        width="450px"
        center>
        <div>
          <div style="margin-bottom: 40px;margin-left: 10px;font-size: 18px;font-weight: 600">我们缺失了哪些标签?</div>
          <el-tag
            :key="tag"
            v-for="tag in dynamicTags"
            closable
            :disable-transitions="false"
            @close="handleClose(tag)">
            {{ tag }}
          </el-tag>
          <el-input
            class="input-new-tag"
            v-if="inputVisible"
            v-model="inputValue"
            ref="saveTagInput"
            size="small"
            @keyup.enter.native="handleInputConfirm"
            @blur="handleInputConfirm"
          >
          </el-input>
          <el-button v-else class="button-new-tag" size="small" @click="showInput">+ New Tag</el-button>

        </div>
        <span slot="footer" class="dialog-footer">
    <el-button @click="centerDialogVisible = false,dynamicTags.splice(0,dynamicTags.length)">取 消</el-button>
    <el-button type="primary" @click="reportTags">提 交</el-button>
  </span>
      </el-dialog>
      <el-dialog
        title="举报该图片"
        :visible.sync="reportImgDialogVisible"
        width="450px"
        center>
        <div style="font-size: 14px;font-weight: 600;margin-top: 20px;margin-bottom: 10px">
          问题是什么？
        </div>
        <div>
          <el-select v-model="feedback.category" placeholder="请选择">
            <el-option
              v-for="item in options"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
        </div>
        <div style="font-size: 14px;font-weight: 600;margin-top: 30px;margin-bottom: 10px">描述问题</div>
        <div>
          <el-input
            type="textarea"
            placeholder="请输入内容"
            v-model="feedback.content"
            maxlength="30"
            show-word-limit
          >
          </el-input>
        </div>

        <span slot="footer" class="dialog-footer">
    <el-button @click="reportImgDialogVisible = false">取 消</el-button>
    <el-button type="primary" @click="reportFeedback">提 交</el-button>
  </span>
      </el-dialog>
      <el-dialog
        title="收藏图片"
        :visible.sync="saveImageDialog"
        width="30%"
        center>
        <div class="saveImage">
          <div>
            <div style="margin-bottom: 10px">选择收藏夹</div>
            <el-checkbox-group v-model="saveFavList" style="border: 1px solid #ececec;padding: 10px; border-radius: 10px">
              <el-checkbox style="width: 80%;margin: 5px 10px;box-sizing: border-box;" v-for="(item,index) in favLists" :label="index" :key="index" border >{{item.favName}}</el-checkbox>


            </el-checkbox-group>

          </div>
          <el-input style="width: 80%;margin-top: 10px" v-if="isShowNew" v-model="newFavName" placeholder="请输入内容">
            <el-button @click="creatNewFav" slot="append" >
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

  </div>
</template>

<script>
import Header from "../components/Header";
import ImageBox from "../components/ImageBox";
import api from "../api/http.js"
import '@fortawesome/fontawesome-free/css/all.css'
import {
  Notification
} from 'element-ui'

export default {
  name: "Picture",
  props: {},
  data() {
    return {
      isShowNew:false,
      newFavName:'',
      saveImageDialog: false,
      favLists: [

      ],
      saveFavList: [],
      commentList: [],
      commentContent: '',
      commentCount: 5,
      feedback: {
        category: '',
        content: '',
      },
      value: '',
      options: [{
        value: '隐私或版权问题',
        label: '隐私或版权问题'
      }, {
        value: '暴力、性或其他不适宜内容',
        label: '暴力、性或其他不适宜内容'
      }, {
        value: '垃圾内容或带水印内容',
        label: '垃圾内容或带水印内容'
      },
        {
          value: '其他',
          label: '其他'
        }],
      image: {
        // imageId: 1,
        // src: require("../img/03.jpg"),
        // likes: 10,
        // fav: 10,
        // description: '美女在小河旁',
        // creatTime: '2023年5月01日',
        // size:100,
        // height:10,
        // width:10
      },

      avatar: require('../img/03.jpg'),
      fansCount: 10,
      authorDetail: {
        // userId: 1,
        // nickname: "夏虫",
        // header: require('../img/03.jpg'),
        // sign: "一个创作者而已",
        // fansCount: 13,
        // likesCount: 20,
        // favCount: 21,
      },
      userDetail: {

      },

      checkLike: false,
      checkFav: false,
      drawer: false,
      dynamicTags: [],
      inputVisible: false,
      inputValue: '',
      touming: {
        opacity: 0
      },
      tags: [

      ],
      description: "我是描述",
      centerDialogVisible: false,
      reportImgDialogVisible: false,
      isFollow: false,
      favLength:0,
    }
  },
  methods: {
    formatFileSize(size) {
      if (size < 1024) {
        return `${size} B`;
      } else if (size < 1024 * 1024) {
        return `${(size / 1024).toFixed(2)} KB`;
      } else if (size < 1024 * 1024 * 1024) {
        return `${(size / (1024 * 1024)).toFixed(2)} MB`;
      } else {
        return `${(size / (1024 * 1024 * 1024)).toFixed(2)} GB`;
      }
    },
    creatNewFav(){
      if(this.newFavName.length>=1){
      this.isShowNew=!this.isShowNew
      const item={
        favCollectName: this.newFavName
      }
      const data ={}
      data.userId=this.$store.state.currentUser.userId
      data.favName=this.newFavName
      data.favType=0
      console.log(data)
      const vm = this

      api.post("/api/favorites/favoritesList",data).then(function (res){
        console.log(res)
        vm.favLists.push(res.data.data)
      }).catch(function (err){

      })
      }else{
        Notification({
          message:"收藏夹名不能为空!",
          type:"warning"
        })
      }
    },
    saveImage() {
      const res=this.favLists.filter((item, index) => {
        return this.saveFavList.indexOf(index) !== -1;
      })
      const favPicId=this.image.imageId
      const favUserId=this.userDetail.userId
      const favAddTimes=new Date()
      const year = favAddTimes.getFullYear()
      const month = ('0' + (favAddTimes.getMonth() + 1)).slice(-2)
      const day = ('0' + favAddTimes.getDate()).slice(-2)
      const hour = ('0' + favAddTimes.getHours()).slice(-2)
      const minute = ('0' + favAddTimes.getMinutes()).slice(-2)
      const second = ('0' + favAddTimes.getSeconds()).slice(-2)
      const favAddTime = `${year}-${month}-${day} ${hour}:${minute}:${second}`
      const fav=[]
      for(let i=0;i<res.length;i++){
        fav.push({
          "favPicId": favPicId,
          "favCollectName": res[i].favName,
          "createTime": favAddTime,
          "favUserId": favUserId,
          "favListId": res[i].id,
        })
      }
      const vm = this
      if(fav.length!==0) {
        api.post("/api/favorites/imageList", fav).then(function (res) {
          vm.getFavList()
        }).catch(function (err) {

        })
        this.checkFav = true
        this.saveImageDialog = false
      }

    },
    clickFollow() {
      if (!this.isFollow) {
        this.isFollow = true
      } else
        this.isFollow = false
      const FollowVO = {}
      FollowVO.toFollow = this.isFollow
      FollowVO.userId = this.authorDetail.userId
      FollowVO.userName = this.authorDetail.nickname
      FollowVO.fansId = this.userDetail.userId
      FollowVO.fansName = this.userDetail.userName

      const authors = this.authorDetail
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
    commitComment() {
      const data = {}
      data.picId = this.image.imageId
      data.userId = this.userDetail.userId
      data.userName = this.userDetail.userName
      data.userAvatar = this.userDetail.header
      data.content = this.commentContent
      if(data.content.length===0){
        Notification(
          {
            type:"warning",
            message:'评论内容不能为空！'
          }
        )
        return
      }
      const vm = this
      api.post("/api/comments", data).then(function (res) {
        console.log(res)
        vm.getComments()
      }).catch(function (err) {

      })
    },
    reportFeedback() {


      const data={
        userId:this.$store.state.currentUser.userId,
        picId:this.image.imageId,
        category:this.feedback.category,
        content:this.feedback.content
      }
      const vm= this
      api.post("/api/picture/reportPictures",data).then(function (res){
        console.log(res)
        vm.reportImgDialogVisible = false
        vm.$message({
          message:"提交成功",
          type:'success'
        })
      }).catch(function (err){
        console.log(err)
      })

    },
    reportImg() {
      this.reportImgDialogVisible = true
    },
    enterContent() {
      this.touming.opacity = 1
    },
    leaveContent() {
      this.touming.opacity = 0

    },
    clickLike() {
      let userId = this.userDetail.userId
      let picId = this.image.imageId
      const vm = this
      if (this.checkLike) {

        api.delete("/api/picture/images/like/" + userId + "/" + picId).then(function (res) {
          vm.checkLike = false
          vm.image.likes--
        }).catch(function (err) {
          console.log(err)
          Notification({
            type: "error",
            message: "操作失败"
          })
        })
      } else {

        api.post("/api/picture/images/like/" + userId + "/" + picId).then(function (res) {
          vm.checkLike = true
          vm.image.likes++
        }).catch(function (err) {
          Notification({
            type: "error",
            message: "操作失败"
          })
        })
      }
    },
    clickFav() {
      this.saveImageDialog = true
    },
    clickDownload() {
      // 判断用户当前可下载量，如果不为0则下载并且发送请求让redis中下载量+1，如果为0则提醒。
      if(this.$store.state.downloadAbles-this.$store.state.downloadCount<=0){
        this.$message({
          message:'已到达每日下载上限',
          type:'warning'
        })
      }else{

        this.$refs.link.click();
        const vm = this
        api.post("/api/picture/download/"+1).then(function (res){
          vm.$store.state.downloadCount=res.data.data
          console.log(res)
          const count="今日还可下载:"+(vm.$store.state.downloadAbles-vm.$store.state.downloadCount)

          vm.$message({
            message:count,
            type:'info'
          })
        }).catch(function (err){
          console.log(err)
        })


      }
    },
    clickDetail() {
      this.drawer = true
    },
    clickReportTag() {
      this.centerDialogVisible = true
    },
    handleClose(tag) {
      this.dynamicTags.splice(this.dynamicTags.indexOf(tag), 1);
    },

    showInput() {
      this.inputVisible = true;
      this.$nextTick(_ => {
        this.$refs.saveTagInput.$refs.input.focus();
      });
    },

    handleInputConfirm() {
      let inputValue = this.inputValue;
      if (inputValue) {
        this.dynamicTags.push(inputValue);
      }
      this.inputVisible = false;
      this.inputValue = '';
    },
    reportTags() {
      const data={
        userId:this.$store.state.currentUser.userId,
        picId:this.image.imageId,
        tags:this.dynamicTags
      }
      // console.log(this.dynamicTags)
      if(this.dynamicTags.length===0){
        Notification(
          {
            message:'请输入标签',
            type:'warning'
          }
        )
        return
      }
      this.centerDialogVisible = false
      const vm=this
      api.post("/api/reportTags",data).then(function (res){
        vm.$message({
          message: '提交成功',
          type: 'success'
        });
      }).catch(function (err){

      })

      this.dynamicTags.splice(0, this.dynamicTags.length)

    },
    getComments() {
      const picId = this.$route.params.picId
      const vm = this
      api.get("/api/comments/" + picId).then(function (res) {
        vm.commentList = res.data.data

      }).catch(function (err) {
        console.log(err)
      })
    },
    checkFollow() {
      const userId = this.authorDetail.userId
      const fansId = this.userDetail.userId
      const vm = this
      api.get("/api/users/follow/" + userId + "/" + fansId).then(function (res) {

        vm.isFollow = res.data.data
      }).catch(function (err) {
        console.log(err)
      })
    },
    checkLikesAndFav() {
      const userId = this.userDetail.userId
      const picId = this.$route.params.picId
      const vm = this
      api.get("/api/picture/images/check/" + userId + "/" + picId).then(function (res) {

        vm.checkLike = res.data.data.checkLikes
        vm.checkFav = res.data.data.checkFav
      }).catch(function (err) {

      })
    },
    getFavList(){
      const vm = this
      api.get("/api/favorites/imagesList").then(function (res){
        console.log(res)
        vm.favLists=res.data.data
        vm.favLength=res.data.data.length
      }).catch(function (err){
      })
    },
    //数据初始化
    initialize() {
      const picId = this.$route.params.picId
      const vm = this
      let AuthorId = 1

      this.userDetail.header = this.$store.state.currentUser.header
      this.userDetail.userId = this.$store.state.currentUser.userId
      this.userDetail.userName = this.$store.state.currentUser.nickname
      console.log(this.userDetail)
      this.getComments()
      this.checkLikesAndFav()
      this.getFavList()
      api.get("/api/picture/images/" + picId).then(function (res) {

        if(res.data.data===null){
          vm.$router.push({path: `/Error/NotFoundImg`})
        }
        console.log(res)
        res = res.data.data
        vm.tags = res.tags
        vm.image.imageId = res.picId
        vm.image.likes = res.picLikes
        vm.image.fav = res.picCollections
        vm.image.src = res.picName
        vm.image.description = res.picSign
        vm.image.creatTime = new Date(res.picCreatTime).toLocaleDateString()
        vm.image.size=vm.formatFileSize(res.picSize)
        vm.image.width=res.picWidth
        vm.image.height=res.picHeight
        AuthorId = res.userId
        api.get("/api/users/author/" + AuthorId).then(function (ans) {
          console.log(ans)
          vm.authorDetail = ans.data.data
          vm.checkFollow()
        }).catch(function (err) {

        })
      }).catch(function (err) {

      })




    }
  },
  mounted() {

    this.initialize()
  },
  watch: {

    saveImageDialog(newVal,oldVal){
      if(!newVal){
        this.favLists.splice(this.favLength,this.favLists.length)
      }
    }

  },

  components: {
    Header,
    ImageBox,
    // Waterfall
  }
}
</script>

<style scoped>
.saveImage {
  display: flex;
  flex-direction: column;
}

.comment-text {
  color: #656f79;
  padding-right: 24px;
  text-align: left;
  word-break: break-all;
}

.comment-item-right {
  display: flex;
  flex-direction: column;
  margin-left: 10px;
  /*width: 600px;*/
  height: auto;
}

.comment-list-item {
  display: flex;
  align-items: flex-start;
  margin-bottom: 10px;
  padding-bottom: 10px;
  /*border-bottom: 1px solid #e0e0e0;*/
}

.comment-list {
  margin-top: 20px;
  max-height: 400px;
  overflow-y: auto;
}

/deep/ .comment-inputs-content textarea {
  height: 120px;
  background: #f7f7f7;
  padding: 20px 10px 10px 20px;
  border: none;
}

.comment-content-but:hover {
  border-color: black;
}

.comment-content-but {
  background: white;
  position: absolute;
  bottom: 10px;
  left: 10px;
  border: 1px solid #c7c7c7;
  padding: 5px;
  border-radius: 7px;
  font-weight: 600;
  cursor: pointer;
  transition: .1s;
}

.comment-inputs-content {
  position: relative;
  width: 100%;
}

.comment-inputs-avatar {
  margin-right: 20px;
}

.comment-inputs {
  display: flex;
  margin-top: 20px;
  width: 100%;
  margin-bottom: 20px;
}

.comment-right {
  flex: 4 4 0;
  min-width: min(100%, 400px);
  width: 400px;
  height: 300px;
  /*box-shadow: 2px 2px 4px rgba(0, 0, 0, 0.2);*/
  /*border: 1px solid #e0e0e0;*/
  background: white;
  padding: 40px;
  box-sizing: border-box;
  border-radius: 10px;
}

.comment-left {
  flex: 6 6 0;
  min-width: min(100%, 600px);
  position: relative;
  box-sizing: border-box;
  /*border: 1px solid #e0e0e0;*/
  background: white;
  padding: 40px;
  border-radius: 10px;
}

.main-comment {
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
  justify-content: space-between;
  width: 100%;
  gap: 80px;
  margin-top: 20px;
}

.el-tag + .el-tag {
  margin-left: 10px;
}

.button-new-tag {
  margin-left: 10px;
  height: 32px;
  line-height: 30px;
  padding-top: 0;
  padding-bottom: 0;
}

.input-new-tag {
  width: 90px;
  margin-left: 10px;
  vertical-align: bottom;
}

.report:hover {
  color: #aa3e3e;
  border-color: #aa3e3e;
}

.report {
  font-weight: 600;
  font-size: 16px;
  margin: 30px auto;
  width: 400px;
  padding: 10px 20px;
  border: 1px solid #000000;
  border-radius: 10px;
  cursor: pointer;
  transition: .1s;
}

.des-des {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  margin: 30px auto;
  width: 400px;
}

.des-report {
  margin-top: 20px;
  font-size: 14px;
  color: #4d4d4d;
  cursor: pointer;
}

.des-tag {
  display: flex;
  width: auto;
  flex-wrap: wrap;
}

.desBox-tag {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  margin: 30px auto;
  width: 400px;
  border-radius: 5px;
}

.des-but:hover {
  color: #52aa3e;
  border-color: #52aa3e;
}

.des-but {
  margin-right: 10px;
  margin-bottom: 10px;
  /*width: 80px;*/
  box-sizing: border-box;
  padding: 8px 12px;
  border: 1px solid #7f7f7f;
  cursor: pointer;
  border-radius: 6px;
  transition: .1s;
}

.line {
  border-bottom: 1px solid #e0e0e0;
  width: 400px;
  margin: 0 auto;
}

.desBox-count {
  margin: 30px auto;
  width: 400px;
  padding: 30px;
  background: #f7f7f7;
  border-radius: 5px;
}

.descriptionBox {
  display: flex;
  border-bottom: 1px solid #7f7f7f;
  padding-bottom: 20px;
}

.detail {
  display: flex;
  align-items: center;
}

.detail-but:hover {
  color: #52aa3e;
  border-color: #52aa3e;
}

.detail-but {
  margin-left: 10px;
  padding: 8px 12px;
  border: 1px solid #7f7f7f;
  cursor: pointer;
  border-radius: 6px;
  transition: .1s;
}

.tags {
  display: flex;

}

.description {
  display: flex;
  margin-top: 20px;
  justify-content: space-between;
  align-items: center;
}

.DetailIcon:hover {
  background: rgba(180, 178, 178, 0.5);
  border: 1px solid white;
}

.DetailIcon {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background: rgba(192, 192, 192, 0.53);
  line-height: 40px;
  color: #FFFFFF;
  cursor: pointer;
  box-sizing: border-box;
  border: 1px solid rgba(1, 1, 1, 0);
  transition: .1s;
}

.imageDetail {
  opacity: 0;
  position: absolute;
  display: flex;
  flex-direction: row-reverse;
  background: linear-gradient(180deg, rgba(25, 27, 38, .4), rgba(25, 27, 38, 0));
  width: 100%;
  height: 120px;
  top: 0px;
  left: 0px;
  padding-top: 20px;
  padding-right: 20px;
  box-sizing: border-box;
  transition: .25s;
}

/deep/ .el-image__inner {
  max-height: 660px;
}

.main-content {
  margin-top: 30px;
  display: flex;
  justify-content: center;
}

.download:hover {
  background: #5dbe46;
}

.download {
  padding: 8px 16px;
  border-radius: 15px;
  cursor: pointer;
  transition: .1s;
  background: #52aa3e;
  color: white;
  transition: .1s;

}

.main-top-right {
  display: flex;
  align-items: center;
  height: 100%;
}

.main-top-right-button2 {
  box-sizing: border-box;
  height: 36px;
  width: 66px;
  line-height: 23px;
  padding: 5px 14px;
  border: 2px solid #52aa3e;
  border-radius: 5px;
  margin-right: 10px;
  cursor: pointer;
  color: #52aa3e;

}

.main-top-right-button:hover {
  border: 1px solid black;
}

.main-top-right-button {
  box-sizing: border-box;
  height: 36px;
  width: 66px;

  line-height: 25px;
  padding: 5px 15px;
  border: 1px solid #c9c9c9;
  border-radius: 5px;
  margin-right: 10px;
  cursor: pointer;
  transition: .1s;
}

.follow:hover {
  background: #5dbe46;
}

.follow {
  display: flex;
  align-items: center;
  padding: 2px 10px;
  background: rgb(82, 170, 62);
  height: 30px;
  color: white;
  cursor: pointer;
  transition: .1s;
  border-radius: 7px;
}

.user-detail {
  margin-left: 15px;
  margin-right: 15px;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  justify-content: space-around;
}

.header {
  width: 48px;
  height: 48px;
  border-radius: 50%;
}

.main-top-left {
  display: flex;
  align-items: center;
}

.main-top {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.box {
  background: #ffffff;
}

.main {
  padding: 64px 32px;
}
</style>
