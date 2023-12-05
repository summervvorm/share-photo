<template>
<div class="ad-box"  @mouseenter="mouseenter" @mouseleave="mouseleave">
  <div class="imgTop" :style="imgTopStyle">
    <span style="font-weight: 800;font-size: 20px;color:#ffffff;">{{advertisements.title}}</span>
  </div>

  <el-image style="width: 100%;" :src="advertisements.imageUrl" fit="cover" lazy @click="imgDetail">
    <div slot="placeholder" class="image-slot">
      <el-skeleton-item
        variant="image"
        style="width: 340px; height: 220px;"
      />
    </div>
  </el-image>
  <div class="imgBot" :style="imgBotStyle">
    <span style="font-weight: 600;font-size: 14px;color:#ffffff;">{{advertisements.description}}</span>

  </div>
  <div style="position: absolute;font-size: 14px; right: 10px;top: 10px;color: #9499a0;font-weight: 400;border: 1px solid #9499a0;padding: 2px 4px;border-radius: 5px">
    广 告
  </div>
</div>
</template>
<script>
import api from '../api/http.js'
export default {
  name:'Advertising',
  props: {
    advertisements:{},
  },
  data(){
    return{
      // advertisements:{
      //   title:'test',
      //   image_url:'http://arrows.oss-cn-beijing.aliyuncs.com/photos/photos/2854458b1b6187ee0a7db2e17d6ab53f.jpg',
      //   description:'test',
      //
      // },
      imgTopStyle: {
        opacity: 0,
      },
      imgBotStyle: {
        opacity: 0,
      },
      hover: 'false',
    }
  },

  methods:{
    imgDetail(){

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
  mounted() {
  }
}
</script>
<style scoped>

.ad-box {
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
  box-sizing: border-box;
  padding: 16px 16px 16px 16px;
  background: linear-gradient(180deg, rgba(25, 27, 38, .56), rgba(25, 27, 38, 0));
  display: flex;
  align-items: center;
  justify-content: flex-start;
  z-index: 4;
}

.imgBot {
  box-sizing: border-box;

  opacity: 0;
  transition: all .25s;
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  padding: 16px 16px;
  background: linear-gradient(180deg, rgba(25, 27, 38, 0), rgba(25, 27, 38, .56));
  display: flex;
  align-items: center;
  justify-content: flex-start;
  z-index: 4;
  flex-direction: row;
  flex-wrap: wrap;
}

</style>
