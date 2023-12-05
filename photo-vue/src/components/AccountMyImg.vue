<template>
  <div class='box'>
    <div style="padding: 20px 30px;">
      <div class="mypic">
        <div>
          <div class="mypic-head">
            <el-select v-model="category" style="width: 150px;" placeholder="请选择" @change="getUploadImg">
              <el-option v-for="item in options" :key="item.value" :label="item.label"
                         :value="item.value">
              </el-option>
            </el-select>
            <el-select v-model="status" style="width: 150px;" placeholder="请选择" @change="getUploadImg">
              <el-option v-for="item in options2" :key="item.value" :label="item.label"
                         :value="item.value">
              </el-option>
            </el-select>
            <div style="margin-left: 40px;"></div>
            <el-input style="width: 300px;" placeholder="请输入内容" v-model="keywords">
              <el-button slot="append" icon="el-icon-search"  @click="getUploadImg"></el-button>
<!--              <el-button type="primary" icon="el-icon-search" @click="getUploadImg"-->
<!--                         style="height: 32px;line-height: 32px;padding: 0px 10px;margin-left: 10px">搜索-->
<!--              </el-button>-->
            </el-input>

          </div>
          <div class="mypic-main">
            <div style="margin-top: 40px;"></div>
            <div class="mypic-pic">
              <div class="content-main-item" v-for="(item,index) in uploadPicList" :key="index">
                <!--              <img style="z-index: 6" :src="item.picName" alt="">-->
                <el-image :src="item.picName" fit="cover"  lazy style="z-index: 6; width: 100%;
  height: 100%;">
                  <div slot="placeholder" class="image-slot">
                    <el-skeleton-item
                      variant="image"
                      style="width: 300px; height: 200px;"
                    />
                  </div>
                </el-image>


                <div class="content-main-detail">
                  <div><span>{{ item.picSign }}</span></div>
                  <div>
                    <span> <i class="far fa-heart"></i>{{ item.picLikes }}</span>
                    <span><i class="far fa-bookmark"></i>{{ item.picCollections }}</span>
                  </div>
                  <div style="width: 100%;height: 100%;position: absolute;top: 0px;left: 0px;z-index: 5"
                       @click="clickImgDetail(item)"
                  >

                  </div>




                  <div class="deleteImg" slot="reference" @click="clickDeleteImg(index)">
                    <span>✖</span>
                  </div>
                  <div slot="reference" style="left: 2px;position: absolute;top: 2px">
                    <span>{{ status===0?'已发布':'审核中' }}</span>
                  </div>
                </div>
              </div>
            </div>
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

    </div>
    <el-dialog
      title="提示"
      :visible.sync="dialogVisible"
      width="400px"
      :before-close="handleClose"
      center>

      <div style="font-size: 18px;font-weight: 600;text-align: center;width: 100%;">确定删除吗？</div>
      <span slot="footer" class="dialog-footer">
    <el-button @click="dialogVisible = false">取 消</el-button>
    <el-button type="primary" @click="dialogVisible = false,deleteImg(index)">确 定</el-button>
  </span>
    </el-dialog>


  </div>
</template>

<script>
// 这里可以导入其他文件（比如：组件，工具js，第三方插件js，json文件，图片文件等等）
// 例如：import 《组件名称》 from ‘《组件路径》‘;
import api from '../api/http.js'

export default {
  name: 'AccountMyImg',
  // import引入的组件需要注入到对象中才能使用
  components: {},
  data() {
    // 这里存放数据
    return {
      x: '1',
      value: '',
      dialogVisible: false,
      index: -1,
      count: 0,
      current: 1,
      size: 40,
      keywords: '',
      category:null,
      status: 0,
      options: [
        {
          value:null,
          label:'最新',
        },
        {
          value: 1,
          label: '最热',
        },


      ],
      options2:[
        {
          value: 2,
          label: '审核中'
        },
        {
          value:0,
          label: '已发布'
        }
      ],
      input1: '',
      src: require('../img/header.jpg'),
      uploadPicList: [
        {
          picName: require('../img/header.jpg'),
          picLikes: 2,
          picCollections: 3,
          picSign: '1111',
        }

      ],
    }
  },
  // 监听属性 类似于data概念
  computed: {},
  // 监控data中的数据变化
  watch: {},
  // 方法集合
  methods: {
    handlePrevClick() {
      this.current--
      this.getUploadImg()
    },
    handleNextClick() {
      this.current++;
      this.getUploadImg()

    },
    handleCurrentChange(currentPage) {
      this.current=currentPage
      this.getUploadImg()

    },


    // 删除图片
    deleteImg(index) {
      const vm = this
      api.delete("/api/picture/images/" + this.uploadPicList[index].picId).then(function (res) {
        vm.uploadPicList.splice(index, 1)
        console.log(res)
      }).catch(function (err) {
      })
    },
    clickDeleteImg(index) {
      console.log(index)
      this.index = index
      this.dialogVisible = true
    },
    clickImgDetail(item) {
      this.$router.push("/picture/" + item.picId)
    },
    handleClose() {

    },
    getUploadImg() {
      const userId = this.$store.state.currentUser.userId
      const vm = this
      const params={
        current:this.current,
        size:this.size,
        keywords:this.keywords,
        category:this.category,
        status:this.status
      }
      api.get("/api/picture/images/upload/" + userId,{
        params
      }).then(function (res) {
        vm.uploadPicList.splice(0, vm.uploadPicList.length, ...res.data.data.data)
        console.log(res)
        vm.count=res.data.data.count
      }).catch(function (err) {
        console.log(err)
      })
    },

  },
  // 生命周期 - 创建完成（可以访问当前this实例）
  created() {

  },
  // 生命周期 - 挂载完成（可以访问DOM元素）
  mounted() {
    this.getUploadImg()
  },
}
</script>
<style scoped>
.deleteImg:hover {
  background: #606060;

}

.deleteImg {
  width: 20px;
  height: 20px;
  line-height: 20px;
  border-radius: 6px;
  padding: 4px;
  position: absolute;
  top: -4px;
  color: #fff;
  background: #3b3b3b;
  z-index: 7;
  right: -4px;
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
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
  opacity: 0;
  transition: .2s;
  z-index: 7;
}

.content-main-detail:hover {
  opacity: 1;
}


.content-main-item {
  flex: 0.5 1 auto;
  margin: 10px;
  position: relative;
  height: 200px;
  z-index: 5;
//width: 200px; cursor: pointer;
}

.content-main-item img {

  /* position: absolute; */
}

.mypic-pic {
  display: flex;
  flex-wrap: wrap;
  width: 100%;
}

.mypic {
}

.mypic-head {
  display: flex;
}

.box {
  min-height: 800px;
  display: flex;
  background-color: #FFF;
  padding: 20px 20px 20px 0px;
  border-radius: 8px;
  width: 100%;
}
</style>
