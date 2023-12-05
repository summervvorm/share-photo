<template>
  <div class="box">
    <div class="nav">
      <div class="title">
        标签反馈列表
      </div>
      <div class="menu">
        <div class="op">
          <el-button type="primary" size="small" @click="allPass">批量通过</el-button>
          <el-button type="danger" size="small" @click="allIgnore">批量忽略</el-button>
          自动审核 <el-switch
            v-model="autoReport"
            active-color="#13ce66"
            inactive-color="#ff4949">
          </el-switch>

        </div>
        <div class="search">
          <el-input
            placeholder="请输入内容"
            prefix-icon="el-icon-search"
            v-model="searchVal"
            size="small"
          >
          </el-input>
          <el-button type="primary" icon="el-icon-search"
                     style="height: 32px;line-height: 32px;padding: 0px 10px;margin-left: 10px"
          @click="search"
          >搜索
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
        @selection-change="handleSelectionChange">
        <el-table-column
          type="selection"
          width="55">
        </el-table-column>
        <el-table-column
          label="日期">
          <template slot-scope="scope">
            <i class="fa-regular fa-clock"></i>
            {{ scope.row.createTime | removeT}}

          </template>
        </el-table-column>
        <el-table-column
          prop="userName"
          label="用户名">
        </el-table-column>
<!--        <el-table-column-->
<!--          prop="picName"-->
<!--          label="图片名"-->
<!--        >-->
<!--        </el-table-column>-->
        <el-table-column
          label="图片"
        >
          <template slot-scope="scope">
            <div @click="clickImg(scope.row.picName)">
              <el-image
                style="width: 45px; height: 45px;"
                :src="scope.row.picName"
                fit="cover"
              >
              </el-image>
            </div>
          </template>
        </el-table-column>
        <el-table-column
          prop="tags"
          label="标签名"
        >
          <template slot-scope="scope">
<!--            <div @click="clickImg(scope.row.tags)">-->
<!--           -->
<!--            </div>-->
            <span class="el-tag el-tag--light">{{scope.row.tags}}</span>

          </template>

        </el-table-column>
        <el-table-column
          label="操作"
        >
          <template slot-scope="scope">
            <el-button
              @click.native.prevent="pass(scope.$index, tableData)"
              type="primary"
              size="small">
              通过
            </el-button>
            <el-button
              @click.native.prevent="ignore(scope.$index, tableData)"
              type="danger"
              size="small">
              忽略
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


  </div>
</template>
<!--<link href="src/assets/style/global.css" rel="stylesheet">-->
<script>

import NavBar from "../components/NavBar.vue";
import SideBar from "../components/SideBar.vue";
import api from "../api/http"
import Vue from "vue";
export default {
  name: 'TagReport',
  filters:{
    removeT(value){
      return value.replace(/T/,' ')
    }
  },
  // import引入的组件需要注入到对象中才能使用
  data() {
    return {
      autoReport:false,
      current:1,
      count:1,
      currentImg:'',
      tableData: [
        {
          name: '夏虫',
          picName: require('../img/02.jpg'),
          createTime: '2023-07-03',
          tags: '夜晚',
        }
      ],
      searchVal: '',
      multipleSelection: [],
      ImgDialogVisible:false,
      timer: null,
    }
  },
  watch:{
    autoReport(newVal,oldVal){
      if (newVal) {
          this.sendRequest();
          this.$message({
            message:"开始自动审核",
            type:'success'
          })
      } else {

      }
    }
  },
  methods: {

    sendRequest() {
      // 发送请求的逻辑
      // ...
      const data1=this.tableData[0]
      const vm = this
      api.post("/api/auto/reportTags",data1).then((res)=>{
        vm.tableData.splice(0,1)
        if(!res.data.data){
          vm.$message({
            message:"标签"+data1.tags+"不符合,已忽略",
            type:'info'
          })
        }else{
          vm.$message({
            message:"已通过标签"+data1.tags,
            type:'success'
          })
        }
        console.log(res)

        if(vm.autoReport&&vm.tableData.length>0){
          vm.sendRequest()
        }

      }).catch((err)=>{
        console.log(err)
      })

    },
    search(){
      this.getReportTags()
    },


    allIgnore(){
      const data1=this.multipleSelection
       this.tableData = this.tableData.filter(item => {
        return !this.multipleSelection.includes(item)
      });
      api.delete("/api/reportTags",{
        data:data1
      }).then(function (res){
        console.log(res)
      }).catch(function (err){
        console.log(err)
      })


    },
    allPass(){
      const data=this.multipleSelection
      this.tableData = this.tableData.filter(item => {
        return !this.multipleSelection.includes(item)
      });
      api.post("/api/tags",data).then(function (res){
        console.log(res)
      }).catch(function (err){
        console.log(err)
      })
    },
    handlePrevClick() {
      this.current--
      this.getReportTags()
    },
    handleNextClick() {
      this.current++;
      this.getReportTags()
    },
    handleCurrentChange(currentPage) {
      this.current=currentPage
      this.getReportTags()

    },
    clickImg(url){
      this.currentImg=url
      this.ImgDialogVisible=true
    },
    pass(index, rows) {
      // 通过需要向用户发送通知
      const data=[]
      data.push(this.tableData[index])
      api.post("/api/tags",data).then(function (res){
        console.log(res)
      }).catch(function (err){
        console.log(err)
      })
      rows.splice(index, 1);

    },
    ignore(index, rows) {
      const data2=[]
      data2.push(this.tableData[index])
      api.delete("/api/reportTags",{
        data:data2
      }).then(function (res){
        console.log(res)
      }).catch(function (err){
        console.log(err)
      })
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
      // console.log(val)
    },
    getReportTags(){
      const vm = this
      const vue = Vue
      const params = {
        current: this.current,
        size: 10,
        keywords:this.searchVal,
      };

      api.get("/api/reportTags",{
        params
      }).then(function (res){
        vm.tableData.splice(0,vm.tableData.length)
        console.log(res)
        vm.tableData=res.data.data.data
        vm.count=res.data.data.count
      })
    },
  },

  mounted(){
    this.getReportTags()
  },
  components: {
    NavBar,
    SideBar
  },
  computed: {

  },

}
</script>


<style scoped>
.page{
  margin-top: 10px;
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
</style>
