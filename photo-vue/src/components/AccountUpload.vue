<template>
	<div class="box">
		<div style="width: 100%;">
			<div class="upload">
				<div class="aside-tip">
					<h3>支持的文件类型</h3>
					<div>
						jpg、jpeg、png
					</div>
          <div style="width: 100%;border-top: 1px solid #5e5e5e;margin-top: 10px">

          </div>
          <div style="margin-top: 10px;font-size: 14px">
            上传一张图片可获得1经验，
            每日无上限。
          </div>

				</div>
				<div class="upload-main">
					<div v-if="isupload" @click="handleUploadClick" class="upload-box">
						<input type="file" @change="handleFileChange" ref="fileInput" style="display: none;">
						<i class="el-icon-upload"
							style="font-size: 87px;color: #c0c4cc; margin: 60px 0 16px;line-height: 50px;"></i>
						<div style="width: 100px;height: 40px;line-height: 40px;
						margin-top: 40px;background: #52aa3e;color:#fff;border-radius: 12px">点击上传</div>
					</div>
					<div v-if="!isupload" >
						<div class="detail">
							<div class="imgshow">
								<el-image :src="imgUrl"  alt="" fit="cover" style="border-radius: 10px;width: 250px;height: 250px">
								</el-image>
							</div>
							<div style="margin-left: 40px;width: 300px;">
								<div style="white-space: nowrap;text-overflow: ellipsis;overflow: hidden;">图片名：<span style="color: #7f7f7f;font-size: 14px;"> {{imgName}}</span></div>
								<div style="text-align: left;margin-top: 20px">分辨率：<span style="color: #7f7f7f">{{width}}✖{{height}}</span></div>
                <div style="text-align: left;margin-top: 20px">大小：<span  style="color:#7f7f7f;">{{imgSize}}</span></div>
							</div>
						</div>
						<div class="tagList">
							<el-tag :key="tag" v-for="tag in dynamicTags" closable :disable-transitions="false"
								@close="handleClose(tag)">
								{{tag}}
							</el-tag>
							<el-input class="input-new-tag" v-if="inputVisible" v-model="inputValue" ref="saveTagInput"
								size="small" @keyup.enter.native="handleInputConfirm" @blur="handleInputConfirm">
							</el-input>
							<el-button v-else class="button-new-tag" size="small" @click="showInput">➕ 添 加 标 签</el-button>

						</div>
						<div style="margin: 40px;"></div>
						<div>
							<el-input type="textarea" :autosize="{ minRows: 2, maxRows: 4}" placeholder="请输入内容描述图片"
								v-model="textarea2">
							</el-input>
						</div>
						<div style="margin: 40px;"></div>
						<div>
							<el-button   @click="centerDialogVisible =true" style="margin-right: 20px;">
								删除
							</el-button>
							<el-button  type="success" @click="submitUpload" style="margin-left: 20px;">提交
							</el-button>
						</div>
					</div>
          <div style="margin-top: 100px;color: #7f7f7f">
            上传图片，即表示您已同意共享图库图片版权协议，请勿上传色情，反动等违法图片。
          </div>
				</div>

			</div>
		</div>
		<el-dialog title="确定删除？" :visible.sync="centerDialogVisible" width="30%" center>
			<span slot="footer" class="dialog-footer">
				<el-button @click="centerDialogVisible = false">取 消</el-button>
				<el-button type="primary" @click="backUpload">确 定</el-button>
			</span>
		</el-dialog>
	</div>
</template>

<script>
	// 这里可以导入其他文件（比如：组件，工具js，第三方插件js，json文件，图片文件等等）
	// 例如：import 《组件名称》 from ‘《组件路径》‘;
	import AccountsHeader from '../components/AccountsHeader.vue'
	import api from '../api/http.js'
	import form_data from 'form-data'
	import {
		Notification
	} from 'element-ui'
	export default {
		name: 'AccountUpload',
		// import引入的组件需要注入到对象中才能使用
		components: {
			AccountsHeader
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
        imgName:'',
        imgSize:'',
        width:'',
        height:'',
				dynamicTags: [],
				inputVisible: false,
				inputValue: '',
				textarea2: '',
				centerDialogVisible: false,
				x: '2',
			}
		},
		// 监听属性 类似于data概念
		computed: {},
		// 监控data中的数据变化
		watch: {},
		// 方法集合
		methods: {
      formatFileSize(size) {
        if (size < 1024) {
          return `${size} B`;
        } else if (size < 1024 * 1024) {
          return `${(size / 1024).toFixed(2)} KB`;
        } else if (size < 1024 * 1024 * 1024) {
          return `${(size / (1024 * 1024)).toFixed(2)} MB`;
        } else {
          return `${(size / (1024 * 1024 * 1024)).toFixed(2)} GB`;
        }
      },
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
        this.imgName=this.file.name
        this.imgSize=this.formatFileSize(this.file.size)
        const reader = new FileReader();
        const img = new Image();

        reader.onload = () => {
          img.onload = () => {
            this.width = img.width;
            this.height = img.height;
          };

          img.src = reader.result;
        };
        reader.readAsDataURL(this.file);
			},
			// 上传图片
			submitUpload() {
				if(this.dynamicTags.length===0){
					Notification({
						message: '标签不能为空',
						type: 'error'
					})
				}else if(this.textarea2.length===0){
					Notification({
						message: '描述不能为空',
						type: 'error'
					})
				}else{
          const FormData = form_data;
          const data1 = new FormData();
          data1.append('file',this.file)
				const vm=this
				data1.append('picTags',this.dynamicTags)
				data1.append('picSign',this.textarea2)
				api.post("/api/picture/upload",data1).then(function(res){
					Notification({
						message: '上传成功',
						type: 'success'
					})
					vm.backUpload()
				}).catch(function(err){
					console.log(err)
				})
				}
			},
			backUpload() {
				this.centerDialogVisible = false;
				this.isupload = true
				this.file = ''
				this.imgUrl = ''
				this.dynamicTags = []
				this.textarea2 = ''

			},



			// 删除图片
			mypicDelete() {

			}
		},
		// 生命周期 - 创建完成（可以访问当前this实例）
		created() {

		},
		// 生命周期 - 挂载完成（可以访问DOM元素）
		mounted() {

		},
		beforeDestroy() {}, // 生命周期 - 销毁之前
	}
</script>
<style scoped>
	.mypic-delete {
		position: absolute;
		width: 40px;
		right: 5px;
		top: 5px;
		color: #ffffff;
		cursor: pointer;
		background-color: rgba(77, 77, 77, 0.2);
	}

	.mypic-pic {
		display: flex;
		background-color: rgba(215, 215, 215, 0.5);
		padding: 20px;
		width: 200px;
		height: 150px;
		position: relative;
	}

	.mypic {}

	.mypic-head {
		display: flex;
	}

	.tagList {
		display: flex;
		flex-direction: row;
		align-items: center;
    margin-top: 20px;
	}

	.detail {
		display: flex;
	}

	.imgshow {
		width: 250px;
		height: 250px;
	}

	.upload-box {
		border: 1px dashed #1cb4d6;
		width: 800px;
		height: 300px;
		cursor: pointer;
		display: flex;
		align-items: center;
		flex-direction: column;
	}

	.aside-tip {
		padding: 0px 20px;
    border-right: 2px solid rgba(233, 233, 233, 1.0);
    width: 230px;
    box-sizing: border-box;
    height: 100%;
	}

	.upload-demo {
		width: 500px;
	}

	.upload-main {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: flex-start;
		width: 100%;
		margin-right: 20px;
		padding: 5px;
	}

	.upload {
		display: flex;
		justify-content: space-between;
		flex-direction: row;
		background-color: white;
		border-radius: 8px;
		padding: 10px;
    height: 100%;
    width: 100%;
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



	.el-tag+.el-tag {
		margin-right: 10px;
	}

	.button-new-tag {
		margin-right: 10px;
		height: 32px;
		line-height: 30px;
		padding-top: 0;
		padding-bottom: 0;
	}

	.input-new-tag {
		width: 90px;
		margin-right: 10px;
		vertical-align: bottom;
	}
  .box{
    min-height: 800px;
    display: flex;
    background-color: #FFF;
    padding: 20px 20px 20px 0px;
    border-radius: 8px;
  }
</style>
