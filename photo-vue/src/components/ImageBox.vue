<template>
  <div class="main1" @mouseenter="mouseenter" @mouseleave="mouseleave">
    <div class="imgTop" :style="imgTopStyle">
      <div class="iconbox" :style="checkFav?{background:'#00AB6B'}:{background:'rgba(180,180,180,0.34)'}" @click="mark">
        <i class="fa-solid fa-bookmark" style="color: white;"></i>
      </div>
      <div class="iconbox" :style="checkLike?{background:'#00AB6B'}:{background:'rgba(180,180,180,0.34)'}"
           @click="heart">
        <i class="fa-solid fa-heart" style="color: white;"></i>
      </div>
      <!-- <i class="fa-solid fa-heart"></i> -->
    </div>

    <el-image style="width: 100%;" :src="itemprop.picName" fit="cover" lazy @click="imgDetail">
      <div slot="placeholder" class="image-slot">
        <el-skeleton-item
          variant="image"
          style="width: 340px; height: 220px;"
        />
      </div>
    </el-image>
    <div class="imgBot" :style="imgBotStyle">
      <div class="tag" v-for="item in itemprop.tags" :key="item.tagId">
        <router-link  :to="{ path: '/Search/'+item.tagName}" custom v-slot="{ navigate }" >
          <span @click="navigate" @keypress.enter="navigate" role="link">{{ item.tagName }}</span>
        </router-link>
      </div>
    </div>
  </div>


</template>
<script>
// 这里可以导入其他文件（比如：组件，工具js，第三方插件js，json文件，图片文件等等）
// 例如：import 《组件名称》 from ‘《组件路径》‘;
import api from '../api/http.js'
import pubsub from "pubsub-js";

export default {
  name: 'ImageBox',
  // import引入的组件需要注入到对象中才能使用
  components: {},
  props: {
    pic: {},
    itemprop: {},
  },
  data() {
    // 这里存放数据
    return {

      pictureInfo: {},
      src: require('../img/header.jpg'),
      imgTopStyle: {
        opacity: 0,
      },
      imgBotStyle: {
        opacity: 0,
      },
      hover: 'false',
      iconColor1: {
        color: ''
      },
      iconColor2: {
        color: ''
      },
      checkFav: false,
      checkLike: false,
    }

  },
  // 监听属性 类似于data概念
  computed: {},
  // 监控data中的数据变化
  watch: {
    itemprop: {
      handler(newVal) {
        // 更新data属性
        this.checkFav = newVal.isFav;
      },
      deep: true
    }


  },
  // 方法集合
  methods: {
    imgDetail() {
      console.log(this.itemprop)
      console.log(1)
      const id = 2
      this.$router.push({path: `/Picture/${this.itemprop.picId}`})
    },
    mark() {
      // if (this.checkFav) {
      // 	this.checkFav = false
      // } else {
      // 	this.checkFav = true
      //
      // }
      if (this.$store.state.currentUser === null) {
        pubsub.publish('getLoginSign', true)
      } else {
        this.$emit('clickMark', this.itemprop)
      }
    },
    heart() {
      const vm = this

      // console.log(this.$store.state.showLoginDialog)
      if (this.$store.state.currentUser === null) {
        pubsub.publish('getLoginSign', true)
      } else {
        let userId = this.$store.state.currentUser.userId


        let picId = this.itemprop.picId
        if (this.checkLike) {
          this.checkLike = false
          api.delete("/api/picture/images/like/" + userId + "/" + picId)
        } else {
          this.checkLike = true

          api.post("/api/picture/images/like/" + userId + "/" + picId).then(function (res) {

          }).catch(function (err) {

          })
        }
      }
    },
    mouseenter() {
      this.imgTopStyle = {
        opacity: 1,
      }
      this.imgBotStyle = {
        opacity: 1,
      }
    },
    mouseleave() {
      this.imgTopStyle = {
        opacity: 0,
      }
      this.imgBotStyle = {
        opacity: 0,
      }
    },


  },
  // 生命周期 - 创建完成（可以访问当前this实例）
  created() {

  },
  // 生命周期 - 挂载完成（可以访问DOM元素）
  mounted() {
    this.pictureInfo = this.pic
    this.checkFav = this.itemprop.isFav
    this.checkLike = this.itemprop.isLike
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
.iconbox {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 30px;
  height: 30px;
  border: 1px solid #ababab;
  border-radius: 25%;
  margin-left: 10px;
}

.iconbox:hover {
  border: 1px solid #ffffff;
}

.main1 {
  display: flex;
  position: relative;
  cursor: pointer;
  height: 100%;
}

.imgTop {
  opacity: 0;
  transition: all .25s;
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  padding: 16px 0px 16px 0px;
  background: linear-gradient(180deg, rgba(25, 27, 38, .56), rgba(25, 27, 38, 0));
  display: flex;
  align-items: center;
  justify-content: flex-start;
  z-index: 4;
}

.imgBot {
  opacity: 0;
  transition: all .25s;
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  padding: 16px 0px 16px 0px;
  background: linear-gradient(180deg, rgba(25, 27, 38, 0), rgba(25, 27, 38, .56));
  display: flex;
  align-items: center;
  justify-content: flex-start;
  z-index: 4;
  flex-direction: row;
  flex-wrap: wrap;
}

.tag {
  color: #e1e1e1;
  margin-left: 10px;
}

.tag:hover {
  color: #ffffff;
}
</style>
