<template>
  <div class="box">
    <div class="nav">
      <div class="title">
        用户列表
      </div>
      <div class="menu">
        <div class="op">
<!--          <el-button type="primary" size="small">批量通过</el-button>-->
<!--          <el-button type="danger" size="small">批量忽略</el-button>-->
        </div>
        <div class="search">
          <el-input
            placeholder="请输入内容"
            prefix-icon="el-icon-search"
            v-model="searchVal"
            size="small"
          >
          </el-input>
          <el-button @click="search" type="primary" icon="el-icon-search"
                     style="height: 32px;line-height: 32px;padding: 0px 10px;margin-left: 10px">搜索

          </el-button>
        </div>
      </div>



    </div>
    <div class="table">
      <el-table
        :data="tableData"
        tooltip-effect="dark"
        style="width: 100%"
        border
        @selection-change="handleSelectionChange">
<!--        <el-table-column-->
<!--          type="selection"-->
<!--          width="55">-->
<!--        </el-table-column>-->
        <el-table-column
          width="250"
          label="注册日期"
          >
          <template slot-scope="scope">
          <span>
            <i class="fa-regular fa-clock"></i>
          </span>
            {{ scope.row.createTime|removeT }}

          </template>
        </el-table-column>
        <el-table-column
          label="头像"
          width="80"
        >
          <template slot-scope="scope">
            <div @click="clickImg(scope.row.userHeaderImage)">
              <el-image
                style="width: 50px; height: 50px;"
                :src="scope.row.userHeaderImage"
                fit="cover"
              >
              </el-image>
            </div>
          </template>
        </el-table-column>
        <el-table-column
          prop="userNickname"
          label="用户昵称"
          width="120">
        </el-table-column>
        <el-table-column
          prop="userEmail"
          label="用户账号"
        >
        </el-table-column>
        <el-table-column
          prop="userSign"
          label="用户签名"
        >
        </el-table-column>
        <el-table-column
          width="100"
          prop="experience"
          label="用户经验"
        >
          <template slot-scope="scope">
            <i class="fa-solid fa-bolt"></i> {{ scope.row.experience}}
          </template>

        </el-table-column>
        <el-table-column
          label="操作"
          width="120"
        >
          <template slot-scope="scope">
            <el-button
              @click.native.prevent="edit(scope.$index, tableData)"
              type="primary"
              size="small"

            >
              编辑
            </el-button>
<!--            <el-button-->
<!--              @click.native.prevent="ignore(scope.$index, tableData)"-->
<!--              type="danger"-->
<!--              size="small">-->
<!--              忽略-->
<!--            </el-button>-->
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
      title="编辑用户信息"
      center
      :visible.sync="editDialogVisible"
      width="400px"
    >
      <div>
      <el-form label-position="left" label-width="80px" v-model="dialogUserInfo">
        <el-form-item label="用户昵称">
          <el-input v-model="dialogUserInfo.userNickname" disabled :placeholder="dialogUserInfo.nickname"></el-input>
        </el-form-item>
        <el-form-item label="用户签名">
          <el-input v-model="dialogUserInfo.userSign" disabled></el-input>
        </el-form-item>
        <el-form-item label="用户账号">
          <el-input v-model="dialogUserInfo.userEmail" disabled></el-input>
        </el-form-item>
        <el-form-item label="用户经验">
          <el-input  type="number" min="0" v-model="dialogUserInfo.experience"></el-input>
        </el-form-item>
      </el-form>
      </div>
      <span slot="footer" class="dialog-footer">
    <el-button @click="editDialogVisible = false">取 消</el-button>
    <el-button type="primary" @click="editUserInfo">提 交</el-button>
  </span>
    </el-dialog>


  </div>
</template>
<!--<link href="src/assets/style/global.css" rel="stylesheet">-->
<script>

import NavBar from "../components/NavBar.vue";
import SideBar from "../components/SideBar.vue";
import api from "../api/http"
import '@fortawesome/fontawesome-free/css/all.css'
export default {
  name: 'UserList',
  // import引入的组件需要注入到对象中才能使用
  data() {
    return {
      current:1,
      count:1,
      currentImg:'',
      tableData: [
        {
          nickname: '夏虫',
          userHeaderImage: require('../img/02.jpg'),
          createTime: '2023-07-03',
          userSign: '夜晚',
          userEmail: "2030155342@qq.com",
          experience:10,
        }

      ],
      dialogUserInfo:{
          userNickname:'',
          userEmail: '',
          experience:'',
          userSign:'',
      },

      searchVal: '',
      multipleSelection: [],
      editDialogVisible:false,
      ImgDialogVisible:false,
    }
  },
  methods: {
    search(){
      this.getUserList()
    },
    editUserInfo(){
      // this.dialogUserInfo
      const vm = this
      api.put("/api/users/experiences/"+this.dialogUserInfo.userId+"/"+this.dialogUserInfo.experience).then(function (res){
        vm.editDialogVisible=false
        vm.$message({
          message:'修改成功',
          type:'success'
        })
        console.log(res)
      }).catch(function (err){
        console.log(res)

      })


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
      this.current=currentPage
      this.getPicList()

    },
    clickImg(url){
      this.currentImg=url
      this.ImgDialogVisible=true
    },
    edit(index, rows) {
      // 通过需要向用户发送通知
      this.editDialogVisible=true

      // console.log(index,rows)
      this.dialogUserInfo=rows[index]
      // console.log(this.dialogUserInfo)
    },
    ignore(index, rows) {
      rows.splice(index, 1);

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
    init(){

    this.getUserList()

    },
    getUserList(){
      const params={
        current: this.current,
        size: 10,
        keywords:this.searchVal,
      };
      const vm = this
      api.get("/api/users/users", {
        params
      }).then(function (res){
        console.log(res)
        vm.tableData.splice(0,vm.tableData.length,...res.data.data.data)

      }).catch(function (err){
        console.log(err)
      })
    }

  },


  mounted() {

    this.init()

  },
  filters:{
    removeT(value){
      if (value && typeof value === 'string') {
        return value.replace(/T/, ' ');
      } else {
        return '';
      }
    },
    removePrefix(value){
      return  value.substr(value.lastIndexOf('/') + 1);
    }
  },
  components: {
    NavBar,
    SideBar
  },
  computed: {}
}
</script>


<style scoped>
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
.el-pagination , /deep/.is-background{
  background-color: white;
}
/deep/
.el-pagination .btn-prev,/deep/ .el-pagination .btn-next{
  height: 34px;
  width: 65px;
  background-color: white !important;
  border: 1px solid #cccccc;
  border-radius: 8px !important;
}
/deep/
.el-pagination .btn-prev:not([disabled="disabled"]):hover,/deep/ .el-pagination .btn-next:not([disabled="disabled"]):hover{
  background-color: #eaeaea !important;
}
/deep/
.el-pagination .el-pager .number{
  height: 34px;
  width: 34px;
  line-height: 34px;
  background-color: white !important;
  border: 1px solid #cccccc;
  border-radius: 8px !important;
  transition: .1s;
}
/deep/
.el-pagination .el-pager .number ~ :not(.active):hover{
  color: black !important;
  background-color: #eaeaea !important;
}
/deep/
.el-pagination .el-pager .active{
  background: #79c966 !important;
  border: none !important;
}
/deep/
.el-pagination.is-background .el-pager .el-icon{
  height: 34px;
  width: 34px;
  line-height: 34px;
  border: 1px solid #cccccc;
  background: white;
  border-radius: 8px !important;
}
</style>
