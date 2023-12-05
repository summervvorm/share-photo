<template>
<div>

  <transition name="fade-transform" mode="out-in">
    <router-view :key="$route.fullPath" />
  </transition>

</div>
</template>
<script>
export default {
  name: 'Report',
  // import引入的组件需要注入到对象中才能使用
  components: {
  },
}
</script>
<style scoped>

</style>
