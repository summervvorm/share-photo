<template>
  <div class="box">
    <div class="top">
      共享图库后台管理
    </div>
    <div>
      <el-menu
        default-active="1"
        class="side-nav-bar"
        @open="handleOpen"
        @close="handleClose"
        @select="handleSelect"
        router
        background-color="#304156"
        text-color="#BFCBD9"
        active-text-color="#409EFF">
        <el-menu-item index="/Index">
          <i class="el-icon-menu"></i>
          <span slot="title">首页</span>
        </el-menu-item>
        <el-submenu index="2">
          <template slot="title">
            <i class="el-icon-location"></i>
            <span>反馈管理</span>
          </template>
          <el-menu-item-group>
            <el-menu-item index="/Report/TagReport">标签反馈列表</el-menu-item>
            <el-menu-item index="/Report/PicReport">图片反馈列表</el-menu-item>
          </el-menu-item-group>
        </el-submenu>
        <el-submenu index="3">
          <template slot="title">
            <i class="el-icon-location"></i>
            <span>用户管理</span>
          </template>
          <el-menu-item-group>
            <el-menu-item index="/User/UserList">
              <i class="el-icon-document"></i>
              <span>用户列表</span>
            </el-menu-item>
            <el-menu-item index="/User/UserLevel">
              <i class="el-icon-document"></i>
              <span>等级列表</span>
            </el-menu-item>

          </el-menu-item-group>
        </el-submenu>
        <el-submenu index="4">
          <template slot="title">
            <i class="el-icon-location"></i>
            <span>图片管理</span>
          </template>
          <el-menu-item-group>
            <el-menu-item index="/Picture/PictureList">
              <i class="el-icon-document"></i>
              <span>图片列表</span>
            </el-menu-item>
          </el-menu-item-group>
        </el-submenu>
        <el-submenu index="5">
          <template slot="title">
            <i class="el-icon-location"></i>
            <span>审核管理</span>
          </template>
          <el-menu-item-group>
            <el-menu-item index="/Audit/AuditList">
              <i class="el-icon-document"></i>
              <span>图片审核列表</span>
            </el-menu-item>
          </el-menu-item-group>
        </el-submenu>
        <el-submenu index="6">
          <template slot="title">
            <i class="el-icon-location"></i>
            <span>广告管理</span>
          </template>
          <el-menu-item-group>
            <el-menu-item index="/Advertisement/Advertisements">
              <i class="el-icon-document"></i>
              <span>广告列表</span>
            </el-menu-item>
          </el-menu-item-group>
        </el-submenu>
<!--        <el-menu-item index="/Audit">-->
<!--          <i class="el-icon-setting"></i>-->
<!--          <span slot="title">审核管理</span>-->
<!--        </el-menu-item>-->
      </el-menu>

    </div>

  </div>
</template>
<script>

export default {
  name: 'SideBar',
  // import引入的组件需要注入到对象中才能使用
  methods: {
    handleOpen(key, keyPath) {
      console.log(key, keyPath);
    },
    handleClose(key, keyPath) {
      console.log(key, keyPath);
    },
    handleSelect(key, keyPath) {
      let index = key - 1
      console.log(this.$router.options.routes[0].children);
      // const path =this.$router.options.routes[0].children[index].path
      // this.$router.push(path)
    }
  }
}
</script>


<style scoped>
.box {
  width: 200px;
  height: 100vh;
  position: fixed;
  background: #324057;
  padding: 20px 0px;
  text-align: left;
}

.top {
  color: #FFFFFF;
  font-size: 20px;
  font-weight: 600;
  border-bottom: 1px solid #8c939d;
  margin-bottom: 20px;
  box-sizing: border-box;
  padding: 0px 20px 20px 20px;
}

.side-nav-bar:not(.el-menu--collapse) {
  width: 200px;
}

.side-nav-bar {
  box-sizing: border-box;
//position: fixed; top: 0;
  left: 0;
  bottom: 0;
//overflow-y: auto;
}

.side-nav-bar i {
  margin-right: 1rem;
}

*::-webkit-scrollbar {
  width: 0.5rem;
  height: 1px;
}

*::-webkit-scrollbar-thumb {
  border-radius: 0.5rem;
  background-color: rgba(144, 147, 153, 0.3);
}
</style>
