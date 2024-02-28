<template>
  <div class="box">
    <el-header style="padding: 0;	">
      <transition name="fade">
        <Header :fixed="true"></Header>
      </transition>
    </el-header>
    <div class="option">
      <div class="color">
        <el-popover
          placement="bottom"
          width="300"
          trigger="click"
        >
          <div style="display: flex;flex-wrap: wrap;
    justify-content: space-around;">
            <div class="item" v-for="(item,index) in colorList" :key="index">
              <el-tooltip effect="dark" :content=item.name placement="top">

                <div :style="colorBox[index]" class="colorBox" @click="clickColorBox(index)">

                </div>

              </el-tooltip>
            </div>
          </div>
          <div style="width: 30px;height: 20px;padding: 5px 8px;
          line-height: 20px;margin-top: 10px;margin-left: 2px; display: flex;
              justify-content: center;background: #52aa3e;color: #f7f7f7;
              cursor: pointer;border-radius: 7px"
               @click="isColor=true,clickColorBox(-1)"
          >全部
          </div>
          <div slot="reference" class="color-option" :style="isColor?'width:50px':'background:#efefef;width:50px'">
            <span v-if="isColor">颜色</span>
            <div v-else :style=colorOption></div>
          </div>
        </el-popover>
      </div>
      <div class="date">
        <el-popover
          placement="bottom"
          width="400"
          trigger="click"
        >
          <el-date-picker
            v-model="datetimes"
            type="daterange"
            format="yyyy-MM-dd"
            align="right"
            unlink-panels
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            :picker-options="pickerOptions"
            size="mini"
            @change="clickDateTime"
          >
          </el-date-picker>
          <div class="color-option" slot="reference">选择日期</div>
        </el-popover>
      </div>

      <div class="size">
        <el-popover
          placement="bottom"
          width="283"
          trigger="click"
        >
          <span style="font-weight: 600;margin-left: 2px">大于</span> <br>
          <input style="margin-top: 10px" placeholder="宽度（像素）" type="number" min="0" class="size-textInput"
                 tabindex="1" v-model="picWidth">
          ✖
          <input placeholder="高度（像素）" type="number" min="0" class="size-textInput" tabindex="1" v-model="picHeight">

          <div style="display: flex;justify-content: space-between;margin-top: 20px">
            <div>
              <el-button type="danger" icon="el-icon-delete" size="small"
                         @click="picWidth='',picHeight='',showSizeInput=false,getPicList()">清除
              </el-button>
            </div>
            <div>
              <el-button type="success" size="small" @click="clickSize">应用</el-button>
            </div>
          </div>

          <div class="color-option"
               :style="showSizeInput?'background: #efefef;color:#000':'color:#656f79;font-weight:600'" slot="reference">
            <span v-if="!showSizeInput">尺寸</span> <span v-else>> {{ picWidth }} x {{ picHeight }} px</span></div>
        </el-popover>
      </div>

    </div>

    <div>
      <div style="display: flex;font-weight: 800;font-size: 22px;padding:20px">
        共{{ count }}张{{ keywords }}图片
      </div>

      <el-container>
        <el-main class="main">
          <div class="mainTop">
            <div class="tag">
              <div v-for="(item,index) in tags" :key="index">
                <router-link style="color: black" :to="{ path: '/Search/'+item,}">{{ item }}</router-link>
              </div>
            </div>
          </div>
          <div style="display: flex">
            <div v-for="(item,index) in advertisements" :key="index">
              <div class="advertising-box" v-if="$store.state.isDisplayAd">
                <a :href="item.targetUrl" target="_blank">
                  <Advertising :advertisements="item">

                  </Advertising>
                </a>
              </div>
            </div>
          </div>
          <div class="picContent">


            <div class="contentColumn" v-for="(items,index) in pictureInfos" :key="index">
              <div v-for="(item,index2) in items" :key="item.picId">
                <transition name="fade-transform" mode="out-in">
                  <ImageBox :itemprop="item" @clickMark="handleClickMark"></ImageBox>
                </transition>
              </div>
            </div>
          </div>
        </el-main>
      </el-container>
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

  </div>
</template>

<script>
import api from '../api/http.js'
import Header from '../components/Header'
import ImageBox from "../components/ImageBox";
import Vue from "vue";
import {Notification} from "element-ui";
import pubsub from "pubsub-js";
import moment from 'moment'
import Advertising from "../components/Advertising.vue";

export default {
  name: "Search",
  components: {
    Advertising,
    Header,
    ImageBox,
  },
  data() {
    return {
      showSizeInput: false,
      picWidth: '',
      picHeight: '',

      colorBox: [{
        background: '#ffffff',
      }],
      colorList: [
        {
          rgb: 'red',
          name: '红色',
        },
        {
          rgb: 'rgba(255, 140, 0, 1)',
          name: '橙色',
        },
        {
          rgb: 'rgba(255, 215, 0, 1)',
          name: '黄色',
        },
        {
          rgb: 'rgba(43, 202, 43, 1)',
          name: '绿色',
        },
        {
          rgb: '#00d7e8',
          name: '青色',
        },
        {
          rgb: '#00f',
          name: '蓝色',
        },
        {
          rgb: '#c93ef7',
          name: '紫色',
        },
        {
          rgb: '#f8c1ff',
          name: '粉色',
        },
        {
          rgb: '#bbb',
          name: '灰色',
        },
        {
          rgb: '#ffffff',
          name: '白色',
        },
        {
          rgb: '#000000',
          name: '黑色',
        },
        {
          rgb: '#ae5700',
          name: '棕色'
        }

      ],
      isColor: true,
      colorOption: {
        background: '#000000',
        width: '20px',
        height: '20px',
        lineHeight: '20px',
        borderRadius: '50%',
        border: '1px solid #e5e5e5'
      },
      newFavName: '',
      isShowNew: false,
      favLists: [],
      saveFavList: [],
      saveImageDialog: false,
      count: 0,
      current: 1,
      size: 40,
      keywords: '',
      category: '',
      color: '',
      pictureInfos: [[], [], [], []],
      tags: new Set([]),
      pickerOptions: {
        shortcuts: [{
          text: '最近一周',
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
            picker.$emit('pick', [start, end]);
          }
        }, {
          text: '最近一个月',
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
            picker.$emit('pick', [start, end]);
          }
        }, {
          text: '最近三个月',
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
            picker.$emit('pick', [start, end]);
          }
        }]
      },
      datetimes: '',
      advertisements: [],
    }
  },
  computed: {},
  watch: {
    '$route.params.search': function (newVal, oldVal) {
      // 监听 $route.params.search 的变化，并在发生变化时触发该回调函数
      this.keywords = newVal
      this.getPicList()
    },
  },
  methods: {
    clickSize() {
      this.showSizeInput = true
      this.getPicList()
    },
    clickDateTime() {
      // const localDateTime=moment(this.datetimes).format('YYYY-MM-DDTHH:mm:ss');
      console.log();

      this.getPicList()
    },
    handlePrevClick() {
      this.current--
      this.getPicList()
    },
    handleNextClick() {
      this.current++;
      this.getPicList()

    },
    handleCurrentChange(currentPage) {
      this.current = currentPage
      this.getPicList()

    },

    clickColorBox(index) {

      if (index !== -1) {
        this.isColor = false
        this.colorOption.background = this.colorList[index].rgb
        this.color = this.colorList[index].name
        this.getPicList()
      } else {
        this.color = ''
        this.getPicList()
      }
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
    demo(msgname, data) {
      this.isLogin = data
      if (data) {
        this.dialogVisible = false
      }
    },


    getAdList() {


      api.get("/api/advertisements").then((res) => {

        this.advertisements = res.data.data
        console.log(res)
      }).catch((err) => {
        console.log(err)
      })

    },

    getPicList() {
      let sTime = ''
      let eTime = ''
      console.log(this.datetimes)
      if (this.datetimes !== '' && this.datetimes !== null) {
        sTime = (new Date(this.datetimes[0]).toISOString())
        eTime = (new Date(this.datetimes[1]).toISOString())
      }
      const params = {
        current: this.current,
        size: this.size,
        keywords: this.keywords,
        category: '',
        color: this.color,
        startTime: sTime,
        endTime: eTime,
        picWidth: this.picWidth,
        picHeight: this.picHeight,
      };
      const vm = this
      const x = Vue
      api.get("/api/picture/imagespage", {

        params
      })
        .then(function (res) {
          vm.count = res.data.data.count
          // vm.size = res.data.data.data.length
          console.log(res)
          api.get("/api/picture/likeAndFav/images").then(function (res2) {
            console.log(res)
            vm.pictureInfos[0].splice(0, vm.pictureInfos[0].length)
            vm.pictureInfos[1].splice(0, vm.pictureInfos[1].length)
            vm.pictureInfos[2].splice(0, vm.pictureInfos[2].length)
            vm.pictureInfos[3].splice(0, vm.pictureInfos[3].length)
            vm.tags.clear()
            for (let i1 = 0; i1 < res.data.data.data.length; i1++) {
              let j = Math.floor(i1 / 4);
              let i = i1 % 4;
              // vm.pictureInfos[i].splice(0,vm.pictureInfos[i].length)
              // vm.pictureInfos[i][j] = res.data.data.data[i1];
              if (res2.data.data!== undefined) {
                res.data.data.data[i1].isLike = !!(
                  res2.data.data.likePicIdList &&
                  res2.data.data.likePicIdList.includes(res.data.data.data[i1].picId)
                );

              res.data.data.data[i1].isFav = !!(res2.data.data.favPicIdList &&
                res2.data.data.favPicIdList.includes(res.data.data.data[i1].picId));
              }
              res.data.data.data[i].indexs = i1
              // x.set(vm.pictureInfos[i], j, res.data.data.data[i1])
              vm.pictureInfos[i].push(res.data.data.data[i1])

              if (vm.tags.size <= 10) {
                if (res.data.data.data[i1].tags !== null)
                  for (let q of res.data.data.data[i1].tags) {
                    vm.tags.add(q.tagName)
                  }
              }
            }
            console.log(vm.pictureInfos)
            console.log(vm.tags)

            // console.log(res.data)
          }).catch(function (err) {
            console.log(err, vm.pictureInfos)
          })
        }).catch(function (err) {
        console.log(err)
      })
    },
  },
  mounted() {
    this.keywords = this.$route.params.search === undefined ? '' : this.$route.params.search
    this.getPicList()
    this.getAdList()
    for (let i = 0; i < this.colorList.length; i++) {
      Vue.set(this.colorBox, i, {
        background: this.colorList[i].rgb
      })
    }
    this.pubId = pubsub.subscribe('demo', this.demo)
    this.getFavList()
  },
}
</script>

<style scoped>
.size-textInput:focus-visible {
  border: 1px solid #4caf50;
}

.size-textInput {
  outline: none;
  height: 35px;
  width: 115px;
  border: 1px solid #c7c7c7;
  border-radius: 7px;
  padding: 0px 5px 0px 10px;
}

.size {
  margin: 0px 40px;
}

.color-option:hover {
  background: #efefef;
}

.color-option {
  padding: 0px 20px;
  height: 40px;
//font-weight: 600; display: flex; align-items: center; justify-content: space-around;
  border-radius: 10px;
  cursor: pointer;
  transition: .1s;

}

.colorBox:hover {
  transform: scale(1.2);
}

.colorBox {

  cursor: pointer;
  transition: .2s;
  width: 35px;
  height: 35px;
  border-radius: 50%;
  border: 1px solid #ececec;
}

.date {
  display: flex;
  height: 40px;
  line-height: 40px;
  font-weight: 600;
  color: #656f79;
  margin: 0px 40px;
}

.item {
  display: flex;
  padding: 5px 6px;
}

.color {
  align-items: center;
  display: flex;
  height: 40px;
  line-height: 40px;
  font-weight: 600;
  color: #656f79;
  margin: 0px 40px;
}

.option {
  display: flex;
  height: 70px;
  align-items: center;
  flex-direction: row;
  border-bottom: 1px solid #e0e0e0;
}

.main {
  position: relative;
  text-decoration: inherit;
  vertical-align: inherit;
  overflow: hidden;
  /*height: 2800px;*/
  margin-bottom: 50px;
}

.mainTop {


  display: flex;
  justify-content: space-between
}

.tag {
  display: flex;
  align-items: center;

}

.tag div {
  transition: .3s;
  cursor: pointer;
  height: 30px;
  padding: 1px 15px;
  margin-right: 20px;
  border: 1px solid #c5c5c5;
  border-radius: 8px;
  display: flex;
  align-items: center;
}

.tag div:hover {
  border-color: black;
}

.tag a {
  text-decoration: none;
}

.advertising-box {
  display: flex;
//width: 24%; margin: 10px 5px; //max-height: 300px; height: 300px;
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


.imgbox {
  display: flex;
}

.img {
  margin: 5px;
  height: 200px;
}

.box {
  background: #ffffff;

}

</style>
