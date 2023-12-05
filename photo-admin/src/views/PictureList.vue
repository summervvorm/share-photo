<template>
  <div class="box">
    <div class="nav">
      <div class="title">
        图片列表
      </div>
      <div class="menu">
        <div class="op">
          <!--          <el-button type="primary" size="small">批量通过</el-button>-->
          <el-button type="danger" size="small">批量删除</el-button>
        </div>
        <div class="search">
          <el-input
            placeholder="请输入内容"
            prefix-icon="el-icon-search"
            v-model="searchVal"
            size="small"
          >
          </el-input>
          <el-button type="primary" icon="el-icon-search" @click="search"
                     style="height: 32px;line-height: 32px;padding: 0px 10px;margin-left: 10px">搜索
          </el-button>
        </div>
      </div>


    </div>
    <div class="table">
      <el-table
        ref="multipleTable"
        :data="tableData"
        tooltip-effect="dark"
        style="width: 100%"
        border
        :default-sort="{prop:'picLikes',order:'descending'}"

        @selection-change="handleSelectionChange">
        <el-table-column
          type="selection"
          width="55"
        >
        </el-table-column>
        <el-table-column
          label="创建日期"
          width="170"
          sortable
          prop="picCreatTime"

        >
          <template slot-scope="scope"><i class="fa-regular fa-clock"></i>
            {{scope.row.picCreatTime.replace("T", " ") }}
          </template>
        </el-table-column>
        <el-table-column
          label="图片名"
        >
          <template slot-scope="scope">
            {{ scope.row.picName.replace("http://arrows.oss-cn-beijing.aliyuncs.com/photos/photos/", "") }}
          </template>
        </el-table-column>
        <el-table-column
          label="图片"
          width="70"
        >
          <template slot-scope="scope">
            <div @click="clickImg(scope.row.picName)">
              <el-image
                style="width: 50px; height: 50px;"
                :src="scope.row.picName"
                fit="cover"
              >
              </el-image>
            </div>
          </template>
        </el-table-column>
        <el-table-column
          prop="userId"
          label="发布者id"
          width="80">
        </el-table-column>
        <el-table-column
          prop="tags"
          label="标签名"
        >
          <template slot-scope="scope">
           <span class="el-tag el-tag--light" style="margin: 2px" v-for="(item,index) in scope.row.tags">
             {{ item.tagName }}
           </span>
          </template>

        </el-table-column>
        <el-table-column
          prop="picSign"
          label="描述"
        >
        </el-table-column>
        <el-table-column
          label="颜色"
        >
          <template slot-scope="scope">
           <span class="el-tag el-tag--light" style="margin: 2px" v-for="(item,index) in scope.row.color">
             {{ item }}
           </span>
          </template>

        </el-table-column>
        <el-table-column
          prop="picLikes"
          label="点赞数"
          sortable
          width="90"
        >
        </el-table-column>
        <el-table-column
          prop="picCollections"
          label="收藏数"
          width="90"
          sortable
        >
        </el-table-column>
        <el-table-column
          label="操作"
          width="150"
        >
          <template slot-scope="scope">
            <el-button
              @click.native.prevent="edit(scope.$index,JSON.parse(JSON.stringify(scope.row)))"
              type="primary"
              size="small">
              编辑
            </el-button>
            <el-button
              @click.native.prevent="deleteImg(scope.$index, tableData)"
              type="danger"
              size="small">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <div class="page">
      <el-pagination
        background
        layout="prev, pager, next"
        :total=count
        :page-size="10"
        :current-page="current" prev-text=" 上一页 " next-text=" 下一页 "
        @prev-click="handlePrevClick"
        @next-click="handleNextClick"
        @current-change="handleCurrentChange"
      >
      </el-pagination>
    </div>

    <el-dialog
      :visible.sync="ImgDialogVisible"
      width="900px"
    >
      <el-image
        style="width: 800px;"
        :src="currentImg"
        fit="scale-down"
      >
      </el-image>
    </el-dialog>
    <el-dialog
      :visible.sync="tipDialogVisible"
      width="400px"
      title="提示"
    >
  <span slot="footer" class="dialog-footer">
    <el-button @click="tipDialogVisible = false">取 消</el-button>
    <el-button type="primary" @click="deleteImg">提 交</el-button>
  </span>
    </el-dialog>

    <el-dialog
      :visible.sync="editImgDialogVisible"
      width="400px"
      center
      title="编辑图片信息"
    >
      <div>
        <el-form label-position="left" label-width="80px" v-model="dialogImgInfo">
          <el-form-item label="创建日期">
            <el-input v-model="dialogImgInfo.picCreatTime" disabled :placeholder="dialogImgInfo.nickname"></el-input>
          </el-form-item>
          <el-form-item label="标签名">
            <div>
              <el-tag
                :key="index"
                v-for="(tag,index) in dialogImgInfo.tags"
                closable
                :disable-transitions="false"
                @close="handleClose(tag)">
                {{ tag.tagName }}
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
          </el-form-item>
          <el-form-item label="描述">
            <el-input v-model="dialogImgInfo.picSign"></el-input>
          </el-form-item>
          <el-form-item label="颜色">
            <el-select v-model="dialogImgInfo.color" multiple placeholder="请选择">
              <el-option
                v-for="(item,index1) in options"
                :key="item.value"
                :label="item.label"
                :value="item.value">
                <span>
                  {{ item.label }}
                </span>
                <div :style=getColor(index1) class="opBack">

                </div>
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="点赞数">
            <el-input type="number" min="0" v-model="dialogImgInfo.picLikes"></el-input>
          </el-form-item>
          <el-form-item label="收藏数">
            <el-input type="number" min="0" v-model="dialogImgInfo.picCollections"></el-input>
          </el-form-item>

        </el-form>
      </div>

      <span slot="footer" class="dialog-footer">
    <el-button @click="editImgDialogVisible = false">取 消</el-button>
    <el-button type="primary" @click="editImgInfo">提 交</el-button>
  </span>
    </el-dialog>


  </div>
</template>
<!--<link href="src/assets/style/global.css" rel="stylesheet">-->
<script>

import NavBar from "../components/NavBar.vue";
import SideBar from "../components/SideBar.vue";
import api from "../api/http"

export default {
  name: 'TagReport',
  // import引入的组件需要注入到对象中才能使用
  data() {
    return {
      inputVisible: false,
      inputValue: '',
      nowIndex: -1,
      optionsStyle: {
        background: '',
        width: '20px',
        height: '20px',
        borderRadius: '50%',
        border: '1px solid #e5e5e5'

      },
      options: [{
        rgb: 'red',

        value: '红色',
        label: '红色'
      }, {
        rgb: 'rgba(255, 140, 0, 1)',

        value: '橙色',
        label: '橙色'
      }, {
        rgb: 'rgba(255, 215, 0, 1)',

        value: '黄色',
        label: '黄色'
      }, {
        rgb: 'rgba(43, 202, 43, 1)',
        value: '绿色',
        label: '绿色'
      }, {
        rgb: '#00d7e8',
        value: '青色',
        label: '青色'
      }, {
        rgb: '#00f',
        value: '蓝色',
        label: '蓝色'
      }, {
        rgb: '#c93ef7',
        value: '紫色',
        label: '紫色'
      },
        {
          rgb: '#f8c1ff',
          value: '粉色',
          label: '粉色'
        }, {
          rgb: '#bbb',
          value: '灰色',
          label: '灰色'
        }, {
          rgb: '#ffffff',
          value: '白色',
          label: '白色'
        },
        {
          rgb: '#000000',
          value: '黑色',
          label: '黑色'
        },
        {
          rgb: '#ae5700',
          value: '棕色',
          label: '棕色'
        },
      ],
      current: 1,
      count: 1,
      currentImg: '',
      dialogImgInfo: {
        picSign: '',
        color: '',
      },
      tableData: [
        {
          picCreatTime: '2023-07-03',
          picSign: 'test',
          color: [],
          picLikes: 1,
          picCollections: 2,
          picId: '1',
          userEmail: '2030155342@qq.com',
          tagName: '夜晚',
          name: '夏虫',
          picName: require('../img/02.jpg'),
        }

      ],
      searchVal: '',
      multipleSelection: [],
      tipDialogVisible: false,
      editImgDialogVisible: false,
      ImgDialogVisible: false,
    }
  },
  methods: {

    handleClose(tag) {
      this.dialogImgInfo.tags.splice(this.dialogImgInfo.tags.indexOf(tag), 1);
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
        const item = {
          tagName: inputValue
        }

        this.dialogImgInfo.tags.push(item);
      }
      this.inputVisible = false;
      this.inputValue = '';
    },
    getColor(index) {

      return 'background:' + this.options[index].rgb

    },

    getPicList() {

      const params = {
        current: this.current,
        size: 10,
        keywords: this.searchVal,
        color: '',
        category: '',
      }
      const vm = this
      api.get("/api/picture/imagespage", {
        params
      }).then(function (res) {

        vm.tableData.splice(0, vm.tableData.length, ...res.data.data.data)
        vm.tableData = res.data.data.data.map(str => {
          str.color = str.color.split('_')
          return str
        })

        vm.count = res.data.data.count
        console.log(vm.tableData)
      }).catch(function (err) {
        console.log(err)
      })


    },

    editImgInfo() {
      this.tableData.splice(this.nowIndex, 1, this.dialogImgInfo)
      const request = JSON.parse(JSON.stringify(this.dialogImgInfo))
      request.color = request.color.join('_');
      // console.log(request)
      api.post("/api/picture/pictures", request).then((res) => {
        console.log(res)
      }).catch((err) => {
        console.log(err)
      })


      this.editImgDialogVisible = false
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
    clickImg(url) {
      this.currentImg = url
      this.ImgDialogVisible = true
    },
    search() {
      this.getPicList()
    },
    edit(index, row) {
      // 通过需要向用户发送通知
      this.nowIndex = index
      this.dialogImgInfo = row
      this.editImgDialogVisible = true

    },
    deleteImg(index, rows) {
      // rows.splice(index, 1);

    },
    toggleSelection(rows) {
      if (rows) {
        rows.forEach(row => {
          this.$refs.multipleTable.toggleRowSelection(row);
        });
      } else {
        this.$refs.multipleTable.clearSelection();
      }
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
  },
  mounted() {

    this.getPicList()

  },
  components: {
    NavBar,
    SideBar
  },
  computed: {}
}
</script>


<style scoped>
.opBack {
  width: 20px;
  height: 20px;
  line-height: 30px;
  margin: 6px auto;
  border-radius: 50%;
  border: 1px solid #e5e5e5;
  float: right;
}

.table {
  width: 100%;
  margin-top: 20px;
}

.search {
  display: flex;
}

.menu {
  width: 100%;
  margin-top: 30px;
  display: flex;
  justify-content: space-between;
}

.title {
  border-left: 3px solid #1b8bff;
  padding-left: 15px;
  display: flex;
}

.nav {
  font-weight: 600;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
}

.box {
  background: white;
  padding: 10px;
  width: 100%;
  height: 100%;
  box-sizing: border-box;
  border-radius: 10px;
}

/deep/
.el-pagination, /deep/ .is-background {
  background-color: white;
}

/deep/
.el-pagination .btn-prev, /deep/ .el-pagination .btn-next {
  height: 34px;
  width: 65px;
  background-color: white !important;
  border: 1px solid #cccccc;
  border-radius: 8px !important;
}

/deep/
.el-pagination .btn-prev:not([disabled="disabled"]):hover, /deep/ .el-pagination .btn-next:not([disabled="disabled"]):hover {
  background-color: #eaeaea !important;
}

/deep/
.el-pagination .el-pager .number {
  height: 34px;
  width: 34px;
  line-height: 34px;
  background-color: white !important;
  border: 1px solid #cccccc;
  border-radius: 8px !important;
  transition: .1s;
}

/deep/
.el-pagination .el-pager .number ~ :not(.active):hover {
  color: black !important;
  background-color: #eaeaea !important;
}

/deep/
.el-pagination .el-pager .active {
  background: #79c966 !important;
  border: none !important;
}

/deep/
.el-pagination.is-background .el-pager .el-icon {
  height: 34px;
  width: 34px;
  line-height: 34px;
  border: 1px solid #cccccc;
  background: white;
  border-radius: 8px !important;
}
</style>
