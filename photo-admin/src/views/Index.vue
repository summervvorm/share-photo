<template>
  <div class="box">
    <div class="top">
      <el-card style="width: 25%;margin: 0 20px;box-sizing: border-box">
        <div class="card-top">
          <div style="font-size: 40px;color:rgb(52, 191, 163);"><i class="fa-solid fa-users"></i></div>
          <div style="display: flex;
    align-items: flex-start;
    flex-direction: column;margin-left: 30px">
            <div style="font-weight: 600;font-size: 20px;color:#777777;">用户量</div>
            <div style="font-size: 20px;color:#8d8d8d;font-weight: 600">{{ dataCount.userCount }}</div>
          </div>
        </div>
      </el-card>
      <el-card style="width: 25%;margin: 0 20px;box-sizing: border-box">
        <div class="card-top">
          <div style="font-size: 40px;color:rgb(213,184,112);"><i class="fa-solid fa-bookmark"></i></div>
          <div style="display: flex;
    align-items: flex-start;
    flex-direction: column;margin-left: 30px">
            <div style="font-weight: 600;font-size: 20px;color:#777777;">标签量</div>
            <div style="font-size: 20px;color:#8d8d8d;font-weight: 600">{{ dataCount.tagCount }}</div>
          </div>
        </div>
      </el-card>
      <el-card style="width: 25%;margin: 0 20px;box-sizing: border-box">
        <div class="card-top">
          <div style="font-size: 40px;color:rgb(70,180,62);"><i class="fa-solid fa-image"></i></div>
          <div style="display: flex;
    align-items: flex-start;
    flex-direction: column;margin-left: 30px">
            <div style="font-weight: 600;font-size: 20px;color:#777777;">图片量</div>
            <div style="font-size: 20px;color:#8d8d8d;font-weight: 600">{{ dataCount.picCount }}</div>
          </div>
        </div>
      </el-card>
      <el-card style="width: 25%;margin: 0 20px;box-sizing: border-box">
        <div class="card-top">
          <div style="font-size: 40px;color:rgb(59,141,178);"><i class="fa-solid fa-comment"></i></div>
          <div style="display: flex;
    align-items: flex-start;
    flex-direction: column;margin-left: 30px">
            <div style="font-weight: 600;font-size: 20px;color:#777777;">留言量</div>
            <div style="font-size: 20px;color:#8d8d8d;font-weight: 600">{{ dataCount.commentCount }}</div>
          </div>
        </div>
      </el-card>

    </div>
    <div class="main">
    <el-card>
<div style="width: 100%;height: 300px">

  <v-chart :option="picStatsOption" />

</div>
    </el-card>
      <div style="margin-top: 20px">
        <el-card style="width: 40%;">
          <div style="height: 300px">
            <div class="word-cloud-chart" ref="chart"></div>
          </div>
        </el-card>


      </div>


    </div>
  </div>
</template>
<script>
// import EchartsCalendar from '../components/EchartsCalendar.vue'
import '@fortawesome/fontawesome-free/css/all.css'
import * as echarts from "echarts";
import 'echarts-wordcloud';
import api from '../api/http'
export default {

  name: 'Index',
  data() {
    // let calendarCreate;
    return {
      calendarCreate:['2022','2023'],
      dataCount:{
        userCount:10,
        tagCount:20,
        picCount:10,
        commentCount:10
      },
      picStatsOption :{
        title: {
          top: 30,
          left: 'center',
          text: '图片上传统计'
        },
        tooltip: {},
        visualMap: {
          // min: 0,
          // max: 50,
          type: 'piecewise',
          orient: 'horizontal',
          left: 'center',
          top: 65,
          inRange: {
            color: ['#C7DBFF','#5291FF']
          },
        },
        calendar: {
          top: 120,
          left: 30,
          right: 30,
          cellSize: ['auto', 20],
          range: '2023',
          itemStyle: {
            color:'rgb(255,255,255)',
            borderWidth: 0.5
          },
          yearLabel: { show: true },
          splitLine: {
            show: true,
            lineStyle: {
              color: '#7e7e7e',
              width: 0.5,
              type: 'solid'
            }
          }
        },
        series: {
          type: 'heatmap',
          coordinateSystem: 'calendar',
          data: this.getVirtualData()
        },

      },
      chart: null,

      chartOption: {}


    }

  },
  methods:{
    getVirtualData() {
      const res=[]
      let data=[]
      const vm = this
      const x=[]
      api.get("/api/picture/pictureStats").then((resp)=>{
        console.log(resp)
        data=resp.data.data

        for (let i=0;i<data.length;i++) {
          res.push([
            // 2023-06-02T00:00:00
            echarts.time.format( data[i].picCreateTime, '{yyyy}-{MM}-{dd}', false),
            data[i].count
          ])

        }
        const now = new Date();

// 获取当前时间前一年的时间
        const lastYear = new Date(now.getFullYear() - 1, now.getMonth(), now.getDate());

// 格式化时间
        const formatDate = (date) => {
          const year = date.getFullYear();
          const month = date.getMonth() + 1;
          const day = date.getDate();
          return `${year}-${month >= 10 ? month : '0' + month}-${day >= 10 ? day : '0' + day}`;
        };

// 格式化当前时间和当前时间前一年的时间
        const nowFormatted = formatDate(now);
        const lastYearFormatted = formatDate(lastYear);



        vm.picStatsOption.calendar.range=[lastYearFormatted,nowFormatted]
        console.log(res)
        // return res;
      }).catch((err)=> {
        console.log(err)
      })
      return res;
    },

     getTagWordCloudOption(){
      let data = [];
      const vm  =this
      api.get("/api/tagsWordClouds").then(function (res){
        data=res.data.data
        vm.chart.setOption({
          title: {
            top: 0,
            left: 'left',
            text: '图片标签统计'
          },
          series:
            {
              type: 'wordCloud',
              shape: 'circle',
              gridSize: 10,
              keepAspect: false,
              sizeRange: [10, 60],
              rotationRange: [-90, 90],
              rotationStep:90,
              textStyle: {
                fontFamily: 'sans-serif',
                fontWeight: 'bold',
                color: function () {
                  return 'rgb(' + Math.round(Math.random() * 150) + ', ' + Math.round(Math.random() * 250) + ', ' + Math.round(Math.random() * 255) + ')';
                }
              },
              data:data
            }
        })
      }).catch((err)=>{
        console.log(err)
      })



     },
    initChart() {
      const chartDom = this.$refs.chart;

      this.chart = echarts.init(chartDom);
    },
    initDataCount(){
      const vm=this
      api.get("/api/picture/dataCounts").then((res)=>{
        vm.dataCount.userCount=res.data.data[0]
        vm.dataCount.tagCount=res.data.data[1]
        vm.dataCount.picCount=res.data.data[2]
        vm.dataCount.commentCount=res.data.data[3]
        console.log(res)
      }).catch((err)=>{
        console.log(err)
      })
    }

  },

  mounted() {
    // this.renderECharts()
    this.initChart()
    this.getTagWordCloudOption()
    this.initDataCount()
  },

  components: {
    // EchartsCalendar
  }

}
</script>
<style scoped>
.box {

}

.top {
  display: flex;justify-content: space-around;
}

.card-top {
  box-sizing: border-box;
  display: flex;
  height: 70px;
  align-items: center;
  justify-content: space-between;
}
.main{
  margin-top: 20px;
  width: 100%;
  position: relative;
  min-height: 200px;
}
.word-cloud-chart {
  width: 100%;
  height: 100%;
}
</style>
