<template>
  	<div class="login_page fillcontain">
	  		<section class="card" v-show="showLogin">
		  		<div class="left">
      				<img width="148" height="36" src="../assets/logo.png" alt="">
      				<img width="544" height="306" src="../assets/pic.png" alt="">
				  </div>
		    	<el-form :model="loginForm" :rules="rules" ref="loginForm">
						<p class="welcom">慕慕生鲜管理系统登录</p>
						<p class="welcom-tip">欢迎来到慕慕生鲜后台管理系统</p>
					
					<el-form-item prop="username">
						<div class="login-item" >
						<el-input class="input-wrapper" v-model="loginForm.username" placeholder="请输入用户名"></el-input>
						</div>
					</el-form-item>
					<el-form-item   prop="password">
						<div class="login-item" >
						<el-input class="input-wrapper" type="password" placeholder="请输入密码" v-model="loginForm.password"></el-input>
						</div>
					</el-form-item>
					<el-form-item>
				    	<el-button type="primary" @click="submitForm('loginForm')" class="submit_btn">登陆</el-button>
				  	</el-form-item>
				</el-form>
	  		</section>
  	</div>
</template>

<script>
/* eslint-disable */
	import http from './http';
import api from './_api';

	export default {
	    data(){
			return {
				loginForm: {
					username: '',
					password: '',
				},
				rules: {
					username: [
			            { required: true, message: '请输入用户名', trigger: 'blur' },
			        ],
					password: [
						{ required: true, message: '请输入密码', trigger: 'blur' }
					],
				},
				showLogin: false,
			}
		},
		mounted(){
			this.showLogin = true;
		},
		computed: {
		},
		methods: {
			async submitForm(formName) {
				this.$refs[formName].validate(async (valid) => {
					if (valid) {
						// const res = await login({user_name: this.loginForm.username, password: this.loginForm.password})
						
						http.post(api.login,{userName:this.loginForm.username,password:this.loginForm.password}).then((res)=>{
								if(res.status === 10000){
										console.log('user',res.data.username);
										localStorage.setItem('username',res.data.username);
										localStorage.setItem('personalizedSignature',res.data.personalizedSignature);
										localStorage.setItem('role',res.data.role);
										http.get(api.getToken + `?userName=${this.loginForm.username}&password=${this.loginForm.password}`).then((_res)=>{
											if(_res.status === 10000){
												sessionStorage.setItem("token", _res.data);
												this.$router.push('good')
											}
										})
								}else{
									this.$message({
											type: 'error',
											message: res.msg
									});
								}
        })
					} else {
						this.$notify.error({
							title: '错误',
							message: '请输入正确的用户名密码',
							offset: 100
						});
						return false;
					}
				});
			},
		}
	}
</script>

<style lang="less" scoped>
	.login_page{
  background-image: url('../assets/bg.png');
  background-size: contain;
  background-repeat: no-repeat;
  background-position: top;
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
	}
	.welcom{
		font-family: PingFangSC-Semibold;
		font-size: 21px;
		color: #008BFF;
		line-height: 24px;
		font-weight: 600;
		margin-top: 40px;
		text-align: left;
	}
	.manage_tip{
		width: 100%;
		top: -100px;
		left: 0;
		p{
			font-size: 20px;
			font-weight: 700;
			color:#e4393c
		}
	}
	.card{
		width: 1000px;
		background: #FFFFFF;
		box-shadow: 0px 8px 16px 0px rgba(0,0,0,0.18);
		border-radius: 8px;
		display: flex;
		padding: 30px;
		.left{
			display:flex;
			flex-direction: column;
		}
	}
	.form_contianer{
		// .wh(320px, 210px);
		// .ctp(320px, 210px);
		top: auto!important;
		border: 1px solid #eee;
		padding: 25px;
		left: auto;
		height: auto;
		border-radius: 5px;
		text-align: center;
		background-color: #fff;

	}
	.tip{
		font-size: 12px;
		color: red;
	}

	.welcom-tip{
font-family: PingFangSC-Regular;
font-size: 16px;
color: #9199A1;
line-height: 24px;
font-weight: 400;
text-align: left;
	}
	.login-item,.el-form-item__content{
		display: flex;
		align-items: center;
		justify-content: space-between;
		span{
			flex: none;
			width: 70px;
		}
	}
	.input-wrapper{
		width: 320px;
height: 48px;
border-radius: 8px;
	}
			.submit_btn{
width: 320px;
height: 48px;
// background: rgba(0,139,255,0.30);
border-radius: 8px;
		}
</style>
