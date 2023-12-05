<script>
import {defineComponent} from 'vue'

export default defineComponent({
  name: "Advertisement"
})
</script>

<template>
  <div>

    <transition name="fade-transform" mode="out-in">
      <router-view :key="$route.fullPath" />
    </transition>

  </div>
</template>

<style scoped>

</style>
