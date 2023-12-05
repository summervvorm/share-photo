<template>
	<el-container class="box">
		<el-header style="margin-bottom: 20px;padding: 0;" class="header">
			<transition name="fade">
				<Header></Header>
			</transition>
		</el-header>
		<div >
			<el-main style="  width: 1400px;margin: 0 auto;">
					<el-tabs v-model="activeName" @tab-click="handleClick">
						<el-tab-pane label="我发布的图片" name="1">
							<AccountMyImg></AccountMyImg>
						</el-tab-pane>
						<el-tab-pane label="上传" name="2">
							<AccountUpload></AccountUpload>
						</el-tab-pane>
						<el-tab-pane label="收藏夹" name="3">
							<AccountFavorites></AccountFavorites>
						</el-tab-pane>
						<el-tab-pane label="关注列表" name="4">
              <AccountFollow @follow-event="handleFollowEvent"></AccountFollow>

            </el-tab-pane>
						<el-tab-pane label="粉丝列表" name="5">
              <AccountFans @fans-event="handleFollowEvent"></AccountFans>
            </el-tab-pane>
						<el-tab-pane label="设置" name="6">
              <AccountSetting></AccountSetting>
            </el-tab-pane>
					</el-tabs>
			</el-main>
		</div>
	</el-container>
</template>

<script>
	import Header from '../components/Header.vue'
	import AccountUpload from '../components/AccountUpload.vue'
	import AccountMyImg from '../components/AccountMyImg.vue'
	import AccountFavorites from '../components/AccountFavorites.vue'
  import AccountFollow from '../components/AccountFollow.vue'
  import AccountFans from '../components/AccountFans.vue'
  import AccountSetting from '../components/AccountSetting.vue'
  // import '@fortawesome/fontawesome-free/css/all.css'


  import api from '../api/http.js'
	export default {
		name: 'Accounts',
		// import引入的组件需要注入到对象中才能使用
		components: {
			Header,
			AccountUpload,
			AccountMyImg,
			AccountFavorites,
      AccountFollow,
      AccountFans,
      AccountSetting,
		},
		data() {
			// 这里存放数据
			return {
				activeName: '1',
				statusColor: '',
				uploadPercentage: 0,
				dialogImageUrl: '',
				isupload: true,
				file: '',
				imgUrl: '',
				dynamicTags: [],
				inputVisible: false,
				inputValue: '',
				textarea2: '',
				centerDialogVisible: false,




				value: '',
				options: [{
					value: '选项1',
					label: '黄金糕'
				}, {
					value: '选项2',
					label: '双皮奶'
				}, {
					value: '选项3',
					label: '蚵仔煎'
				}, {
					value: '选项4',
					label: '龙须面'
				}, {
					value: '选项5',
					label: '北京烤鸭'
				}],
				input1: '',
				src: require('../img/header.jpg')
			}
		},
		// 监听属性 类似于data概念
		computed: {},
		// 监控data中的数据变化
		watch: {
      '$route.query.itemId':function (newVal,oldVal){
        this.activeName=newVal
      }

    },
		// 方法集合
		methods: {
			handleClose(tag) {
				this.dynamicTags.splice(this.dynamicTags.indexOf(tag), 1);
			},
			showInput() {
				this.inputVisible = true;
				this.$nextTick(_ => {
					this.$refs.saveTagInput.$refs.input.focus();
				});
			},
			handleInputConfirm() {
				let inputValue = this.inputValue;
				if (inputValue) {
					this.dynamicTags.push(inputValue);
				}
				this.inputVisible = false;
				this.inputValue = '';
			},
			handleUploadClick() {
				this.$refs.fileInput.click();
			},
			handleClick() {

			},
			handleFileChange() {
				this.file = event.target.files[0];
				this.imgUrl = (window.URL.createObjectURL(event.target.files[0]))

				this.isupload = false

			},
			// 上传图片
			submitUpload() {
				api.post()
			},
			backUpload() {
				this.centerDialogVisible = false;
				this.isupload = true
				this.file = ''
				this.imgUrl = ''
				this.dynamicTags = []
				this.textarea2 = ''
			},
      handleFollowEvent(data){
        this.activeName=data
      },



			// 删除图片
			mypicDelete() {

			}

		},

		// 生命周期 - 创建完成（可以访问当前this实例）、
		mounted() {
			this.activeName = this.$route.query.itemId
			console.log(this.activeName)
		},
	}
</script>
<style scoped>
	.box {
		background-color: rgba(245, 245, 245, 1.0);

	}

	.upload {
		display: flex;
		justify-content: center;
		flex-direction: row;
	}

	.header {
		background-color: rgba(73, 73, 73, 1);
	}

	.main {
		display: flex;
	}

	.tabs {
		width: 100%;
	}
</style>
