<template>
	<el-container>
		<el-header style="margin-bottom: 20px;padding: 0;" class="header">
			<transition name="fade">
				<Header></Header>
			</transition>
		</el-header>
		<el-main style="margin-left: 100px;margin-right: 100px;">
			<el-header>
				  <el-tabs  v-model="activeName"  @tab-click="handleClick">
				    <el-tab-pane label="我发布的图片" name="1">
					</el-tab-pane>
				    <el-tab-pane label="上传" name="2"></el-tab-pane>
				    <el-tab-pane label="收藏夹" name="3"></el-tab-pane>
				    <el-tab-pane label="关注列表" name="4"></el-tab-pane>
				    <el-tab-pane label="粉丝列表" name="5"></el-tab-pane>
				    <el-tab-pane label="设置" name="6"></el-tab-pane>
				  </el-tabs>
			</el-header>
		</el-main>
		
		
	</el-container>
</template>

<script>
	import Header from '../components/Header.vue'
	export default {
		name: 'AccountsHeader',
		// import引入的组件需要注入到对象中才能使用
		components: {
			Header
		},
		props:['indexs'],
		data() {
			// 这里存放数据
			return {
				activeName:'',
			}
		},
		// 监听属性 类似于data概念
		computed: {},
		// 监控data中的数据变化
		watch: {},
		// 方法集合
		methods: {
			handleClick(tab,event){
				if(this.activeName=='2'){
					this.$router.push('/Account/upload')
				}else if(this.activeName=='3'){
					this.$router.push('/Account/favorites')
				}else if(this.activeName=='4'){
					this.$router.push('/Account/follow')
				}else if(this.activeName=='5'){
					this.$router.push('/Account/fans')
				}else if(this.activeName=='6'){
					this.$router.push('/Account/setting')
				}else if(this.activeName=='1'){
					this.$router.push('/Account/myimg')
				}
			}
			
		},
		// 生命周期 - 创建完成（可以访问当前this实例）、
		mounted(){
			this.activeName=this.indexs
		}
	}
</script>
<style scoped>
	.header {
		background-color:rgba(73, 73, 73, 1);
	}
</style>