<template>
  <div class='box'>

    <div style="display: flex;width: 100%;">
      <div class="aside-menu">
        <div style="display: flex;flex-direction: column;
    align-items: flex-start;">
          <div style="font-size: 14px;color:#7f7f7f ;padding-left: 20px">我的收藏夹</div>

          <div class="addfav">
            <span><i class="fa-solid fa-plus" style="margin-right: 10px;"></i>新建收藏夹</span>
          </div>
          <!--					<div :class="onclass" @click="clickone">-->
          <!--						<i class="fa-solid fa-bookmark" style="margin-right: 10px;"></i> 默认收藏夹-->
          <!--					</div>-->
          <div v-for="(item, index) in favLists" :key="index" @click="activeIndex = index,clickAside(index)
					onclass='menufav'" :class="{ 'active': activeIndex === index ,'menufav':activeIndex !== index
					 }">
            <i class="fa-solid fa-bookmark" style="margin-right: 10px;"></i>{{ item.favName }}
            <el-popover
              placement="bottom-end"
              width="80"
              trigger="hover">
              <div class="editBut" @click="editInfo(index)">编辑信息</div>
              <div class="delFav" @click="delFavList(index)">删除收藏夹</div>
              <div slot="reference" style="width: 30px;height: 30px;line-height: 30px;
             position: absolute;
              right: 10px;top: 8px">
                    <i class="el-icon-more"></i>
              </div>
            </el-popover>
          </div>
        </div>
      </div>
      <div v-if="favLists.length!==0" class="content">
        <div class="content-top">
          <div class="fengmian">
            <img v-if="favPicList.length!==0" :src="favPicList[0].picName" alt="">
            <div v-else style="width: 100%;height: 100%;background: #e8e8e8"></div>
          </div>
          <div class="content-detail">
            <div>{{ favLists[activeIndex].favName }}</div>
            <div style="font-size: 14px;color: #7f7f7f;">创建人: {{ userinfo.nickname }}</div>
            <div style="font-size: 14px;color: #7f7f7f;">
              {{ favPicList.length }}个内容 <span style="color: black;font-weight: 600">·</span>
              {{ favLists[activeIndex].favType === 0 ? '公开' : '仅自己可见' }}
            </div>
          </div>
        </div>
        <div class="content-menu">
          <div v-if="isop" class="content-menu-box">
            <div class="content-menu-1" @click="onclickop">
              <span>批量操作</span>
            </div>
            <div class="content-menu-2">
              <el-select v-model="value1" placeholder="全部标签">
                <el-option v-for="item in tags" :key="item.value" :label="item.label"
                           :value="item.value">
                </el-option>
              </el-select>
            </div>
            <div class="content-menu-2">
              <el-select v-model="value2" placeholder="最近收藏">
                <el-option v-for="(item,index) in options" :key="index" :label="item.label"
                           :value="index">
                </el-option>
              </el-select>
            </div>
            <div class="content-menu-3">
              <el-input placeholder="请输入内容" v-model="input3" class="input-with-select">
                <el-button slot="append" icon="el-icon-search"></el-button>
              </el-input>
            </div>
          </div>
          <div v-if="!isop" class="opmenu">
            <div class="back" @click="onback">
              <i class="fa-solid fa-angle-left"></i>返回
            </div>
            <div class="opmenu-bot">
              <div style="display: flex;">
                <div @click="clickAll" :style="clickallsty" style="cursor: pointer;">
									<span><i class="fas fa-check-square" v-if="checkedAll"></i>
										<i class="far fa-check-square" v-if="!checkedAll"></i> 全选</span>
                </div>
                <div @click="deleteFav">
                  取消收藏
                </div>
              </div>
              <div>
                已选中:{{ checkSize }}
              </div>

            </div>
          </div>
          <div class="content-main">
            <div class="content-main-item" v-for="(item,index) in favPicList" :key="index">
              <img :src="item.picName" alt="">
              <div class="content-main-detail" @click="clickImgDetail(item)">
                <div><span>{{ item.picSign }}</span></div>
                <div>
                  <span> <i class="far fa-heart"></i>{{ item.picLikes }}</span>
                  <span><i class="far fa-bookmark"></i>{{ item.picCollections }}</span>
                </div>
              </div>
              <div v-show="!isop" class="content-main-check" @click="onclickimg(index)">
								<span v-if="selectedItems[index]" style="color: #1cb4d6;font-size: 24px;">
									<i class="fas fa-check-square"></i>
								</span>
                <span v-if="!selectedItems[index]" style="color: #fff;font-size: 24px;">
									<i class="far fa-check-square"></i>
								</span>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div v-if="favLists.length===0" class="none-content">
        这里什么都没有, 快去收藏图片吧
      </div>


    </div>
    <el-dialog
      title="收藏夹信息"
      :visible.sync="editDialog"
      width="500px"

      center>
      <div>
        <el-form ref="form" :model="form" :rules="rules2" label-width="90px"
                 style="width: 400px;margin: 0 auto;">
          <el-form-item label="收藏夹名:" prop="nikename">
            <el-input v-model="form.favName"></el-input>
          </el-form-item>
          <el-form-item label="公开收藏夹">
            <el-switch v-model="form.ispublic"></el-switch>
          </el-form-item>
        </el-form>
      </div>




      <span slot="footer" class="dialog-footer">
    <el-button @click="editDialog = false">取 消</el-button>
    <el-button type="primary" @click="determineEdit">确 定</el-button>
  </span>
    </el-dialog>

    <el-dialog
      title="确认提示"
      :visible.sync="determine"
      width="400px"
    center>
      <div style="font-size: 18px;font-weight: 600;text-align: center;width: 100%;">确定删除这个收藏夹吗？</div>
      <span slot="footer" class="dialog-footer">
    <el-button @click="determine = false">取 消</el-button>
    <el-button type="primary" @click="determineDel">确 定</el-button>
  </span>
    </el-dialog>

  </div>
</template>

<script>
// 这里可以导入其他文件（比如：组件，工具js，第三方插件js，json文件，图片文件等等）
// 例如：import 《组件名称》 from ‘《组件路径》‘;
import api from '../api/http.js'
import Vue from 'vue';

export default {
  name: 'AccountFavorites',
  // import引入的组件需要注入到对象中才能使用
  components: {},
  data() {
    // 这里存放数据
    return {
      value1: '',
      value2: '',
      form:{
        favListsIndex:0,
        favListId:0,
        ispublic:true,
        favName:'',
      },
      rules2: {

      },
      checkedAll: false,
      editDialog: false,
      determine:false,
      activeIndex: 0,
      onclass: 'active',
      item: '',
      clickallsty: '',
      showdetail: false,
      favLists: [],
      favPicList: [],
      ispublics: '公开',
      favindex: 0,
      userinfo: '',
      options: [
        {
          label: '最近收藏'
        },
        {
          label: '最多点赞'
        }

      ],
      tags: [],
      input3: '',
      selectedItems: [],
      isop: true,
      checkSize: 0,
      delIndex:'',
    }
  },
  // 监听属性 类似于data概念
  computed: {},
  // 监控data中的数据变化
  // 方法集合
  methods: {
    determineEdit(){
      this.editDialog=false
      const vm = this
      const favoritesListVO ={
        id:this.form.favListId,
        favName:this.form.favName,
        favType:this.form.ispublic?0:1
      }
      const ids=this.form.favListId
      api.put("/api/favorites/favoritesList",favoritesListVO ).then(function (res){
        const temp=vm.favLists[vm.form.favListsIndex]
        temp.favName=favoritesListVO.favName
        temp.favType=favoritesListVO.favType
        vm.favLists.splice(vm.form.favListsIndex,1, temp)
      }).catch(function (err){
        console.log(err)
      })
    },
    determineDel(){
      this.determine=false
      const vm = this
      api.delete("/api/favorites/favoritesList/"+this.favLists[this.delIndex].id).then(function (res){
        vm.favLists.splice(vm.delIndex,1)
        vm.activeIndex=0
        vm.clickAside(0)
      }).catch(function (err){
        console.log(err)
      })

    },
    delFavList(index){
      this.determine=true
      this.delIndex=index
    },
    editInfo(index) {
      this.editDialog = true
      this.form.favListsIndex=index
      this.form.favName=this.favLists[index].favName
      this.form.favListId=this.favLists[index].id
      this.form.ispublic=this.favLists[index].favType===0
      // console.log(this.favLists[index].id)
    },

    clickImgDetail(item) {
      console.log(item)
      this.$router.push({path: `/Picture/${item.picId}`})
    },
    clickAside(index) {

      this.checkSize = 0
      let favListIds = this.favLists[index].id
      const vm = this
      api.get("http://localhost:8082/favorites/images", {
        params: {
          current: 0,
          size: 10,
          favListId: parseInt(favListIds + ''),
        }
      }).then(function (res) {
        if (res.data.data == null) {
          vm.favPicList = []
          vm.favPicList.splice(0, vm.favPicList.length)
        } else {
          vm.favPicList = res.data.data.data
          console.log(res.data.data)


        }
        vm.selectedItems.splice(0, vm.selectedItems.length)
        for (let i = 0; i < vm.favPicList.length; i++) {
          vm.selectedItems.push(false)
        }

      }).catch(function (err) {
      })

    },
    onback() {
      this.isop = true
    },
    onclickop() {
      this.isop = false

    },
    handleOpen(key, keyPath) {
      console.log(key, keyPath);
    },


    onmenu(tab, event) {
      console.log(tab)
      console.log(event)
    },
    clickAll() {
      if (this.checkedAll) {
        this.checkedAll = false
        this.selectedItems.fill(false)
        this.checkSize = 0
      } else {
        this.checkedAll = true
        this.checkSize = this.selectedItems.length
      }
    },
    onclickimg(index) {
      if (this.selectedItems[index]) {
        Vue.set(this.selectedItems, index, false)
        this.checkSize = this.checkSize - 1
      } else {
        Vue.set(this.selectedItems, index, true)
        this.checkSize = this.checkSize + 1
      }
    },
    deleteFav() {
      if (this.checkSize !== 0) {
        // 删除图片元素
        const filteredArr = []
        for (let i = 0; i < this.favPicList.length; i++) {
          if (this.selectedItems[i] === true) {
            filteredArr.push({
              favPicId: this.favPicList[i].picId,
              favListId: this.favLists[this.activeIndex].id
            })
          }
        }
        const vm = this
        api.delete("/api/favorites/images", {data: filteredArr}).then(function (res) {
          // 筛选出未选中的元素
          const tempArr = []
          for (let i = 0; i < vm.favPicList.length; i++) {
            if (vm.selectedItems[i] !== true) {
              tempArr.push(vm.favPicList[i])
            }
          }

          // 更新数组

          vm.favPicList.splice(0, vm.favPicList.length, ...tempArr)
          vm.selectedItems.splice(0, vm.selectedItems.length)
          vm.clickAside(vm.activeIndex)

        }).catch(function (err) {
          console.log(err)
        })

      }
    },
    //初始化
    initialize() {
      const vm = this
      //获取收藏夹列表
      api.get("/api/favorites/imagesList").then(function (res) {
        if (res.data.data != null)
          vm.favLists = Object.values(res.data.data)
        vm.clickAside(0)
        console.log(vm.favLists)

      }).catch(function (err) {

      })


    },

  },
  //初始化请求


  // 生命周期 - 创建完成（可以访问当前this实例）
  created() {

  },
  // 生命周期 - 挂载完成（可以访问DOM元素）
  mounted() {

    this.userinfo = this.$store.state.currentUser

    this.initialize()

  },
  watch: {
    activeIndex(newValue, oldValue) {
      this.favindex = parseInt(newValue) + 1
      // if (this.favlists[this.favindex].ispublic) {
      // 	this.ispublics = '公开'
      // } else {
      // 	this.ispublics = '私有'
      // }
    },
    checkedAll(newValue, oldValue) {
      if (newValue) {
        this.clickallsty = {
          color: '#00a1d6'
        }
        this.selectedItems.fill(true)

      } else {
        this.clickallsty = {
          color: '#000000'
        }

      }
    },
    selectedItems(newval, oldval) {
      const filteredArr = newval.filter((item) => {
        return item === false;
      });
      if (filteredArr.length === 0) {
        this.checkedAll = true
      } else {
        this.checkedAll = false
      }
    }
  },

  beforeCreate() {
  }, // 生命周期 -创建之前
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
.delFav:hover{
  color: black;
  font-weight: 600;

}
.delFav{

  margin-top: 15px;
  font-size: 16px;
  transition: .1s;
  text-align: center;
  cursor: pointer;
}
.editBut:hover{
  color: black;
  font-weight: 600;
}
.editBut {
  border-bottom: 1px solid #bdbdbd;
  font-size: 16px;
  transition: .1s;
  text-align: center;
  padding-bottom: 15px;
  cursor: pointer;
}

.none-content {
  font-size: 24px;
  width: 100%;
  height: 500px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.content-main-check {
  width: 100%;
  height: 100%;
  position: absolute;
  top: 0px;
  display: flex;
  flex-direction: row-reverse;
  padding: 5px;
  box-sizing: border-box;
  z-index: 6;
  /* background: rgba(255, 255, 255, 0.2); */
}

.content-main-detail {
  width: 100%;
  height: 100%;
  padding: 5px;
  box-sizing: border-box;
  position: absolute;
  bottom: 0px;
  /* left: 5px; */
  color: #fff;
  z-index: 5;
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
  opacity: 0;
  transition: .2s;
}

.content-main-detail:hover {
  opacity: 1;
}

.content-menu-box {
  margin-top: 20px;
  display: flex;
  align-items: center;
  justify-content: flex-end;
}

.content-main-item {
  flex: 0.7 0.2 auto;
  margin: 10px;
  position: relative;
  height: 240px;
  cursor: pointer;
}

.content-main-item img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  /* position: absolute; */
}

.content-main {
  display: flex;
  flex-wrap: wrap;
  width: 100%;
}

.back {
  width: 50px;
  cursor: pointer;
  border: 1px solid #7f7f7f;
  height: 30px;
  line-height: 30px;
  border-radius: 5px;
  transition: .1s;
}

.back:hover {
  color: #00a1d6;
  border: 1px solid #00a1d6;
}

.opmenu-bot {
  display: flex;
  margin-top: 20px;
}

.opmenu-bot div {
  cursor: pointer;
  margin-right: 10px;

}

.opmenu-bot div:nth-child(2):hover {
  color: #949494;
  transition: .1s ease-in-out;
}

.opmenu {
  display: flex;
  flex-direction: column;
}

.content-menu-1 {
  cursor: pointer;

}

.content-menu-1:hover {
  color: #00a1d6;
}

.content-menu-2 {
  width: 120px;
  margin-left: 10px;
}

.content-menu-3 {
  width: 200px;
  margin-left: 10px;
}

.content-menu {
  margin-top: 20px;
  /*
  display: flex;
  align-items: center;
  justify-content: flex-end; */
}

.content-detail {
  margin-left: 20px;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
}

.content-top {
  width: 100%;
  display: flex;
  border-bottom: 1.5px solid rgba(233, 233, 233, 1.0);
  padding-bottom: 20px;
}

.fengmian {
  width: 200px;
  height: 130px;
  display: flex;
  border-radius: 5px;
}

.fengmian img {
  width: 200px;
  height: 130px;
  object-fit: cover;
  border-radius: 5px;
}

.content {
  width: 100%;
  margin-left: 20px;
}

.addfav {
  height: 50px;
  cursor: pointer;
  display: flex;
  align-items: center;
  padding: 5px 20px;
}

.box {
  display: flex;
  background-color: #FFF;
  padding: 20px 20px 20px 0px;
  border-radius: 8px;
  min-height: 800px;
}

.active {
  position: relative;
  height: 50px;
  display: flex;
  align-items: center;
  padding: 5px 20px;
  width: 200px;
  cursor: pointer;
  transition: all .1s;
  background-color: #4caf50;
  box-sizing: border-box;
  color: #ffffff;
  margin-top: 10px;

}

.active:hover {
  /*background-color: #64ce4b;*/
}

.menufav {
  position: relative;
  height: 50px;
  display: flex;
  align-items: center;
  padding: 5px 20px;
  width: 200px;
  cursor: pointer;
  box-sizing: border-box;
  transition: all .3s;
  margin-top: 10px;
}

.menufav:hover {
  background-color: rgba(233, 233, 233, 1.0);
  /*margin-left: 10px;*/
  width: 200px;
}

.aside-menu {

  display: flex;
  padding-right: 5px;
  box-sizing: border-box;
  width: 240px;
  border-right: 2px solid rgba(233, 233, 233, 1.0);

}

.content {
}
</style>
