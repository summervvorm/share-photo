<template>
  <div class="box">
    <div class="nav">
      <div class="title">
        广告列表
      </div>
      <div class="menu">
        <div class="op">
          <el-button type="primary" size="small" @click="addAdDialogVisible=true">新增广告</el-button>
          <el-button type="danger" size="small" @click="allIgnore">批量删除</el-button>


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
          label="开始日期">
          <template slot-scope="scope">
            <i class="fa-regular fa-clock"></i>
            {{ scope.row.startDate | removeT}}
          </template>
        </el-table-column>
        <el-table-column
          label="截止日期">
          <template slot-scope="scope">
            <i class="fa-regular fa-clock"></i>
            {{scope.row.endDate | removeT}}
          </template>
        </el-table-column>
        <el-table-column
          prop="title"
          label="标题">
        </el-table-column>
        <el-table-column
          prop="description"
          label="描述">
        </el-table-column>
        <!--        <el-table-column-->
        <!--          prop="picName"-->
        <!--          label="图片名"-->
        <!--        >-->
        <!--        </el-table-column>-->
        <el-table-column
          label="广告图片"
        >
          <template slot-scope="scope">
            <div @click="clickImg(scope.row.imageUrl)">
              <el-image
                style="width: 45px; height: 45px;"
                :src="scope.row.imageUrl"
                fit="cover"
              >
              </el-image>
            </div>
            <div>

            </div>


          </template>
        </el-table-column>
        <el-table-column
          prop="targetUrl"
          label="广告地址"
        >
          <template slot-scope="scope">
            <!--            <div @click="clickImg(scope.row.tags)">-->
            <!--           -->
            <!--            </div>-->
            <a class="el-tag el-tag--light" :href="scope.row.targetUrl">{{scope.row.targetUrl}}</a>

          </template>

        </el-table-column>
        <el-table-column
          label="操作"
        >
          <template slot-scope="scope">
            <el-button
              @click.native.prevent="edit(scope.$index, tableData)"
              type="primary"
              size="small">
              编辑
            </el-button>
            <el-button
              @click.native.prevent="ignore(scope.$index, tableData)"
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
      title="编辑广告信息"
      center
      :visible.sync="editDialogVisible"
      width="400px"
    >
      <div>
        <el-form label-position="left" label-width="100px" v-model="dialogAdInfo">
          <el-form-item label="标题">
            <el-input v-model="dialogAdInfo.title" :placeholder="dialogAdInfo.title"></el-input>
          </el-form-item>
          <el-form-item label="描述">
            <el-input v-model="dialogAdInfo.description" :placeholder="dialogAdInfo.description"></el-input>
          </el-form-item>
          <el-form-item label="上传图片">
            <el-upload
              class="avatar-uploader"
              action="http://localhost:8082/advertisementImages"
              :show-file-list="false"
              :on-change="handleChange"
              ref="uploadAd"

              :auto-upload="false"
              :on-success="handleAvatarSuccess">
              <img v-if="dialogAdInfo.imageUrl" :src="dialogAdInfo.imageUrl" class="avatar">
              <i v-else class="el-icon-plus avatar-uploader-icon"></i>
            </el-upload>
          </el-form-item>
          <el-form-item label="广告地址">
            <el-input v-model="dialogAdInfo.targetUrl" :placeholder="dialogAdInfo.targetUrl"></el-input>
          </el-form-item>
          <el-form-item label="时间范围">
            <el-date-picker
              v-model="value3"
              type="daterange"
              align="right"
              size="small"
              style="width: 250px"
              unlink-panels
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              :picker-options="pickerOptions">
            </el-date-picker>
          </el-form-item>

        </el-form>
      </div>
      <span slot="footer" class="dialog-footer">
    <el-button @click="editDialogVisible = false">取 消</el-button>
    <el-button type="primary" @click="editAdInfo">提 交</el-button>
  </span>
    </el-dialog>



    <el-dialog
      title="添加广告"
      center
      :visible.sync="addAdDialogVisible"
      width="400px"
    >
      <div>
        <el-form label-position="left" label-width="100px" v-model="dialogAddAdInfo">
          <el-form-item label="标题">
            <el-input v-model="dialogAddAdInfo.title" :placeholder="dialogAddAdInfo.title"></el-input>
          </el-form-item>
          <el-form-item label="描述">
            <el-input v-model="dialogAddAdInfo.description" :placeholder="dialogAddAdInfo.description"></el-input>
          </el-form-item>
          <el-form-item label="上传广告图片">
<!--            <el-input v-model="dialogAddAdInfo.imageUrl" :placeholder="dialogAddAdInfo.imageUrl"></el-input>-->
            <el-upload
              class="upload-demo"
              action="http://localhost:8082/advertisementImages"
              :file-list="fileList"
              :on-success="handleSuccess"
              :limit="1"
              ref="upload"
              :auto-upload="false"
              list-type="picture">
              <el-button size="small" type="primary">点击上传</el-button>
              <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过500kb</div>
            </el-upload>

          </el-form-item>
          <el-form-item label="广告地址">
            <el-input v-model="dialogAddAdInfo.targetUrl" :placeholder="dialogAddAdInfo.targetUrl"></el-input>
          </el-form-item>
          <el-form-item label="时间范围">
            <el-date-picker
              v-model="value2"
              type="daterange"
              align="right"
              size="small"
              style="width: 250px"
              unlink-panels
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              :picker-options="pickerOptions">
            </el-date-picker>
          </el-form-item>

        </el-form>
      </div>
      <span slot="footer" class="dialog-footer">
    <el-button @click="addAdDialogVisible = false">取 消</el-button>
    <el-button type="primary" @click="addAd">提 交</el-button>
  </span>
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
  name: 'Advertisements',
  filters:{
    removeT(value){
      return value.replace(/T/,' ')
    }
  },
  // import引入的组件需要注入到对象中才能使用
  data() {
    return {
      value3:[],
      editDialogVisible:false,
      addAdDialogVisible:false,
      fileList:[],
      value2:'',
      dialogAddAdInfo:{

      },
      pickerOptions: {
        shortcuts: [{
          text: '最近一周',
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
            picker.$emit('pick', [start, end]);
          }
        }, {
          text: '最近一个月',
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
            picker.$emit('pick', [start, end]);
          }
        }, {
          text: '最近三个月',
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
            picker.$emit('pick', [start, end]);
          }
        }]
      },


      dialogAdInfo:{

      },

      current:1,
      count:1,
      currentImg:'',
      tableData: [

      ],
      searchVal: '',
      multipleSelection: [],
      ImgDialogVisible:false,
      timer: null,
    }
  },
  watch:{

  },
  methods: {
    handleAvatarSuccess(response,file){
      console.log(response)
      const data={
        id:this.dialogAdInfo.id,
        title:this.dialogAdInfo.title,
        description:this.dialogAdInfo.description,
        imageUrl:response.data,
        targetUrl:this.dialogAdInfo.targetUrl,
        startDate:this.value3[0],
        endDate:this.value3[1]
      }
      api.put("/api/advertisements",data).then((res)=>{
        this.editDialogVisible=false
        this.$message({
          type:'success',
          message:'操作成功'
        })
        this.dialogAdInfo={}
      }).catch((err)=>{
        console.log(err)
      })


    },
    handleSuccess(response, file){
      console.log(response,file)
      const data={
        title:this.dialogAddAdInfo.title,
        description:this.dialogAddAdInfo.description,
        imageUrl:response.data,
        targetUrl:this.dialogAddAdInfo.targetUrl,
        startDate:this.value2[0],
        endDate:this.value2[1]
      }
      api.post("/api/advertisements",data).then((res)=>{
        this.addAdDialogVisible=false
        this.$message({
          type:'success',
          message:'操作成功'
        })
        this.getReportTags()
        this.dialogAddAdInfo={}
      }).catch((err)=>{
      })


    },

    addAd(){
      this.$refs.upload.submit();
    },
    editAdInfo(){
    this.$refs.uploadAd.submit();
    const data=this.dialogAdInfo
      data.startDate=this.value3[0]
      data.endDate=this.value3[1]
    api.put("/api/advertisements",data).then((res)=>{
      this.editDialogVisible=false
      this.$message({
        message:"操作成功",
        type:'success'
      })
      this.getReportTags()
    }).catch((err)=>{

    })
    },
    handleChange(file){
      console.log(file)
      this.dialogAdInfo.imageUrl = URL.createObjectURL(file.raw);

    },

    search(){
      this.getReportTags()
    },
    allIgnore(){
      const data1=this.multipleSelection
      this.tableData = this.tableData.filter(item => {
        return !this.multipleSelection.includes(item)
      });
      api.delete("/api/advertisements",{
        data:data1
      }).then(function (res){
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
    edit(index, rows) {
      // 通过需要向用户发送通知

      this.dialogAdInfo=JSON.parse(JSON.stringify(rows[index]))
      this.value3[0]=JSON.parse(JSON.stringify(rows[index].startDate))
      this.value3[1]=JSON.parse(JSON.stringify(rows[index].endDate))
      this.editDialogVisible=true

    },
    ignore(index, rows) {
      const data2=[]
      data2.push(this.tableData[index])
      api.delete("/api/advertisements",{
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

      api.get("/api/advertisements/all",{
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
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}
.avatar {
  width: 178px;
  height: 178px;
  display: block;
}

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
