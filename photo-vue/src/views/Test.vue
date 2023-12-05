<template>
<div>

</div>
</template>

<script>
import store from '../stores/stores'
import io from 'socket.io-client';
import { socket } from '../util/websocket'
export default {
  name:'Test',
  data() {
    return {
      path:'ws://localhost:8082/websocket/1',
      socket:''
    }
  },
  created() {
    // this.socket = io('http://localhost:8082');
  },
  mounted() {
    this.initWebsocket();

  },
  destroyed: function () { // 离开页面生命周期函数
    // this.websocketclose();
  },
  methods: {
    initWebsocket: function () {
      if(typeof(WebSocket) === "undefined"){
        alert("您的浏览器不支持socket")
      }else{
        // 实例化socket
        this.socket = new WebSocket(this.path)
        // 监听socket连接
        this.socket.onopen = this.open
        // 监听socket错误信息
        this.socket.onerror = this.error
        // 监听socket消息
        this.socket.onmessage = this.getMessage
      }
    },
    open: function () {
      console.log("socket连接成功")
    },
    error: function () {
      console.log("连接错误")
    },
    getMessage: function (msg) {
      console.log(msg.data)
    },
    send: function (params) {
      this.socket.send(params)
    },
    close: function () {
      console.log("socket已经关闭")
    },
  }
}
</script>


<style scoped>

</style>
