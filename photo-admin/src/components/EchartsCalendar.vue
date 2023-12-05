<template>
  <div>

  <div ref="echarts" style="height: 400px"></div>

  </div>
</template>

<script>
import * as echarts from 'echarts';
import axios from 'axios'
export default {
  name:'EchartsCalendar',

  mounted() {
    this.renderECharts()
  },
  methods: {

      getVirtualData(year) {
        const date = +echarts.time.parse(year + '-01-01');
        const end = +echarts.time.parse(+year + 1 + '-01-01');
        const dayTime = 3600 * 24 * 1000;
        const data = [];
        for (let time = date; time < end; time += dayTime) {
          data.push([
            echarts.time.format(time, '{yyyy}-{MM}-{dd}', false),
            Math.floor(Math.random() * 10000)
          ]);
        }
        return data;
      },
    renderECharts() {
      const chart = echarts.init(this.$refs.echarts)
      const option = {
        title: {
          top: 30,
          left: 'center',
          text: 'Daily Step Count'
        },
        tooltip: {},
        visualMap: {
          min: 0,
          max: 10000,
          type: 'piecewise',
          orient: 'horizontal',
          left: 'center',
          top: 65
        },
        calendar: {
          top: 120,
          left: 30,
          right: 30,
          cellSize: ['auto', 13],
          range: '2023',
          itemStyle: {
            borderWidth: 0.5
          },
          yearLabel: { show: false }
        },
        series: {
          type: 'heatmap',
          coordinateSystem: 'calendar',
          data: this.getVirtualData('2023')
        },

      }
      chart.setOption(option)
    }
  }
}
</script>

<style scoped>

</style>
