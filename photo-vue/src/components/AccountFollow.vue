<template>
  <div class='box'>
    <div style="display: flex;width: 100%;">
      <div class="aside-menu">
        <div style="display: flex;flex-direction: column;
    align-items: flex-start;">
          <div style="font-size: 14px;color:#7f7f7f ;padding: 5px;font-size: 16px">我的关注</div>
          <div class="aside-item">
            全部关注
          </div>
          <div style="padding-bottom: 10px;border-bottom: 2px solid rgba(233, 233, 233, 1.0);width: 100%"></div>
          <div style="font-size: 14px;color:#7f7f7f ;padding: 5px;font-size: 16px">我的粉丝</div>
          <div class="aside-item" @click="goFans" style="background: none;color: #000000">
            全部粉丝
          </div>
        </div>
      </div>
      <div class="content">
        <div class="top-menu">
          <div style="font-size: 20px">全部关注</div>
          <div class="top-item">
            <div :class="item1" @click="RecentlyFollowed">最近关注</div>
            <div :class="item2" @click="MostFollowers">最多粉丝</div>
            <div>
              <el-input size="mini" v-model="select" style="width: 200px;" slot="prepend" placeholder="请选择">
                <el-button style="padding: 12px 10px" slot="append" icon="el-icon-search"></el-button>
              </el-input>

            </div>

          </div>
        </div>
        <div class="content-main">
          <div v-for="(item,index) in followsList" :key="index" class="main-item">
            <div class="main-item-left">
              <router-link :to="{ path: '/UserDetail/'+item.folId}">
              <div class="item-header">
                <el-image :src=item.folAvatar style="width: 40px;height: 40px;border-radius: 50%;"
                          fit="cover"></el-image>
              </div>
              </router-link>

              <div class="item-detail">
                <div style="font-size: 18px;font-weight: 600">
                  {{item.folName}}
                </div>
                <div style="font-size: 14px;color: #3a3a3a">
                  {{item.folSign}}
                </div>
              </div>
            </div>
            <div class="main-item-right">
              <div @click="unfollow(item,index)">取消关注</div>
            </div>
          </div>
        </div>
        <div class="footer">
          <el-pagination
            background
            layout="prev, pager, next"
            :total="200" :current-page="currentPage"prev-text=" 上一页 "	next-text=" 下一页 "
            @prev-click="handlePrevClick"
            @next-click="handleNextClick"
            @current-change="handleCurrentChange"
          >
          </el-pagination>
        </div>
      </div>

    </div>
    <el-dialog
      title="确认提示"
      :visible.sync="determine"
      width="400px"
      center>
      <div style="font-size: 18px;font-weight: 600;text-align: center;width: 100%;">确定取消关注吗？</div>
      <span slot="footer" class="dialog-footer">
    <el-button @click="determine = false">取 消</el-button>
    <el-button type="primary" @click="determineUnFollow">确 定</el-button>
  </span>
    </el-dialog>

  </div>
</template>

<script>
// 这里可以导入其他文件（比如：组件，工具js，第三方插件js，json文件，图片文件等等）
// 例如：import 《组件名称》 from ‘《组件路径》‘;
import api from '../api/http.js'
export default {
  name: 'AccountFollow',
  // import引入的组件需要注入到对象中才能使用

  components: {},
  data() {
    // 这里存放数据
    return {
      item1: 'checked',
      item2: 'noChecked',
      determine:false,
      select: "",
      currentPage:2,
      followsList:[
        {
          userId:1,
          folName:'1',
          folAvatar:'',
          folSign:'1111'
        }
      ],
      followsItem:{},
      followListIndex:0,
    }
  },
  // 监听属性 类似于data概念
  computed: {},
  // 监控data中的数据变化
  watch: {},
  // 方法集合
  methods: {
    determineUnFollow(){
      this.determine=false
      const vm =this
      api.post("/api/users/follow",{
        userId:vm.followsItem.folId,
        fansId:vm.followsItem.userId,
        userName:vm.followsItem.folName,
        toFollow:false,
      }).then(function (res){
        console.log(res)
        vm.followsList.splice(vm.followListIndex,1)
      }).catch(function (err){
        console.log(err)
      })

    },
    unfollow(item,index){
      this.determine=true
      this.followsItem=item
      this.followListIndex=index
    },
    RecentlyFollowed() {
      this.item2='noChecked'
      this.item1='checked'
    },
    MostFollowers() {
      this.item1='noChecked'
      this.item2='checked'
    },
    handlePrevClick(){

    },
    handleNextClick(){

    },
    handleCurrentChange(){

    },
    goFans(){
      this.$emit('follow-event','5')
    },
    getFollowsList(){
      const vm=this
      api.get("/api/users/follows").then(function (res){
        vm.followsList=res.data.data
      }).catch(function (err){
        console.log(err)
      })

    }

  },
  // 生命周期 - 创建完成（可以访问当前this实例）
  created() {

  },
  // 生命周期 - 挂载完成（可以访问DOM元素）
  mounted() {
    this.getFollowsList()
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
.footer{
  padding: 10px;
  margin-top: 40px;
}
.item-detail {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
}

.item-header {
  margin-right: 15px;
}

.main-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
  padding: 20px 0px;
  border-bottom: 1px solid #cdcdcd;
}

.content-main {

  height: 700px;
  display: flex;
  flex-direction: column;

}

.main-item-right {
  border-radius: 5px;
  padding: 5px 10px;
  background: #e1e1e1;
  color: #5e5e5e;
  cursor: pointer;
  transition: .1s;
}
.main-item-right:hover{
  /*color: #000000;*/
  background: #e8e8e8;
}

.main-item-left {
  display: flex;
}

.checked {
  color: #409EFF;
  border-bottom: 2px solid #409EFF;
  cursor: pointer;
  padding: 5px;
  margin: 0px 5px;
}

.noChecked {
  cursor: pointer;
  transition: .1s;
  padding: 5px;
  margin: 0px 5px;

}

.noChecked:hover {
  color: #00a1d6;
}

.top-menu {
  height: 32px;
  display: flex;
  width: 100%;
  justify-content: space-between;
  align-items: center;
  padding-bottom: 10px;
  border-bottom: 1px solid #e0e0e0;
}

.top-item {
  display: flex;
  align-items: center;
}

.content {
  width: 100%;
  padding: 0px 20px 20px 20px;
}

.aside-item {
  height: 50px;
  line-height: 50px;
  display: flex;
  align-items: center;
  margin-top: 10px;
  padding: 5px 5px 5px 25px;
  width: 200px;
  cursor: pointer;
  transition: all .1s;
  background-color: #4caf50;
  box-sizing: border-box;
  color: #ffffff;
}

.aside-menu {
  display: flex;
  padding-right: 5px;
  box-sizing: border-box;
  width: 240px;
  border-right: 2px solid rgba(233, 233, 233, 1.0);
}

.box {
  min-height: 800px;
  display: flex;
  background-color: #FFF;
  padding: 20px 20px 20px 0px;
  border-radius: 8px;

}

</style>

