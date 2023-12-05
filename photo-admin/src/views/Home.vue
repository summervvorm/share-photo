<template>
  <el-container style="background: #efefef">
    <el-aside width="200px">
      <SideBar />
    </el-aside>
    <el-container>
      <el-header height="84px" style="padding: 0">
        <NavBar :key="$route.fullPath" />
      </el-header>
      <el-main style="background: #f7f9fb">
        <div class="fade-transform-box">
          <transition name="fade-transform" mode="out-in">
            <router-view :key="$route.fullPath" />
          </transition>
        </div>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
// 这里可以导入其他文件（比如：组件，工具js，第三方插件js，json文件，图片文件等等）
// 例如：import 《组件名称》 from ‘《组件路径》‘;
import NavBar from "../components/NavBar.vue";
import SideBar from "../components/SideBar.vue";
export default {
  name: 'Home',
  // import引入的组件需要注入到对象中才能使用
  components: {
    NavBar,
    SideBar
  },
  data(){
    return{
      SkipSwitchName:""
    }
  },
  computed: {
  },

}
</script>
<style scoped>

.fade-transform-box {
  position: relative;
  top: 0px;
  bottom: 0px;
  width: 100%;
  overflow: hidden;
}
.fade-transform-enter-active,
.fade-transform-leave-active {
  transition: all 0.5s ease 0s;
}
.fade-transform-enter {
  opacity: 0;
  transform: translateX(-30px);
}
.fade-transform-leave-to {
  opacity: 0;
  transform: translateX(30px);
}
.fade-transform-box {
  position: relative;
  top: 0px;
  bottom: 0px;
  width: 100%;
  overflow: hidden;
}
.Skright-enter-active,
.Skright-leave-active,
.Skleft-enter-active,
.Sklef-leave-active {
  transition: all 600ms;
}
//先给 所要用的上下层级跳转写一个过渡时间
//然后你就可以发挥自己的想象力来写自己动画了 下面是一个例子
.Skright-enter {
  transform: translate3d(-100%, 0, 0);
}
.Skright-leave-to {
  transform: translate3d(100%, 0, 0);
}
.Skleft-enter {
  transform: translate3d(100%, 0, 0);
}
.Skleft-leave-to {
  transform: translate3d(-100%, 0, 0);
}
</style>
