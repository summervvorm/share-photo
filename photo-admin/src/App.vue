<template>
  <div id="app">
<!--    <transition :name="SkipSwitchName">-->
    <router-view/>
<!--    </transition>-->
  </div>
</template>

<script>
export default {
  name: 'App',




}
</script>

<style>
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  /* margin-top: 60px; */
}
body {
		padding: 0;
		margin: 0;
	}



</style>
