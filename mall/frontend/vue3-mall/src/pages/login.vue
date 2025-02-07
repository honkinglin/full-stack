<template>

<div class="login">
  <div class="content">
    <div class="left">
      <!-- <img width="337" height="380" src="../assets/loginbg.png" alt=""> -->
    </div>
    <div class="right">
      <div class="title">账户登录</div>
        <el-input
          class="name"
          v-model="loginForm.userName"
          placeholder="请输入你的用户名"
        ></el-input>
         <el-input
         class="password"
                type="password"
                show-password
                v-model="loginForm.password"
                placeholder="请输入登录密码"
          ></el-input>
          <el-button class="login-btn" @click="login" type="primary"
              >立即登录</el-button
            >
            <p class="footer-desc" @click="locationFn('/register')">立即注册</p>
    </div>
  </div>
</div>
</template>

<script>
/* eslint-disable */
	import http from './http';
import api from './api';
import { useRouter } from 'vue-router';

const router = useRouter();

	export default {
	    data(){
			return {
				loginForm: {
					username: '',
					password: '',
				}
			}
		},
		mounted(){
      console.log('this::',this);
		},
		computed: {
		},
		methods: {
			locationFn(url){
					this.$router.push(url)
			},

			login(){
        if(!this.loginForm.userName){
						this.$message({
								type: 'error',
								message: '请输入用户名'
						});
            return;
        }
        if(!this.loginForm.password){
						this.$message({
								type: 'error',
								message: '请输入密码'
						});
            return;
        }
        http.get(api.login,{userName:this.loginForm.userName,password:this.loginForm.password},1).then((res)=>{
            if(res.status === 10000){
							      localStorage.setItem('username',res.data.username);
                    localStorage.setItem('personalizedSignature',res.data.personalizedSignature);
                    localStorage.setItem('role',res.data.role);
                    	http.get(api.getToken + `?userName=${this.loginForm.userName}&password=${this.loginForm.password}`,null,1).then((_res)=>{
											if(_res.status === 10000){
												sessionStorage.setItem("token", _res.data);
										    this.$router.push('/index')
											}
										})
            }else{
							this.$message({
									type: 'error',
									message: res.msg
							});
            }
        })
			},
		}
	}
</script>

<style lang="less" scoped>
.login{
  background-image: url('../assets/homebg.png');
  background-size: cover;
  background-repeat: no-repeat;
  background-position: center;
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
}
.logo{
  display: flex;
  margin-top: 20px;
}
.content{
  display: flex;
  width: 720px;
  height: 380px;
  background: #FFFFFF;
  box-shadow: 0px 12px 24px 0px rgba(28,31,33,0.1);
  border-radius: 12px;
  .left{
    width: 337px;
    flex:none;
    background-image: url('../assets/loginbg.png');
    background-size: cover;
  }
  .right{
    flex: 1;
    .title{
      width: 73px;
      height: 24px;
      font-family: PingFangSC-Semibold;
      font-size: 18px;
      color: #F34D00;
      line-height: 24px;
      font-weight: 600;
      margin: 0 auto;
      margin-top: 33px;
    }
    .name{
      width: 320px;
      height: 48px;
      // background: rgba(28,31,33,0.06);
      border-radius: 8px;
      margin-top: 32px;
    }
    .password{
      width: 320px;
      height: 48px;
      //background: rgba(28,31,33,0.06);
      border-radius: 8px;
      margin-top: 20px;
    }
  }
  .login-wrapper{
    padding: 16px;
    width: 500px;
    height: 300px;
    margin-right: 20px;
    margin-top: 20px;
    border-radius: 6px;
    background-color: rgba(1, 1, 1, 0.6);
    .item{
      display: flex;
      margin-top: 20px;
      align-items: center;
      color: #fff;
      span{
        display: inline-block;
        width: 80px;
      }
    }
    .el-button-primary{
            background: #F34D00;
      border-radius: 24px;
    }
  }
}
    .login-btn{
      width: 320px;
      height: 48px;
      background: #F34D00;
      border-radius: 24px;
      margin-top: 32px
    }
.register-info{
  color: #fff;
  text-align: right;
}
.footer-desc{
  width: 68px;
height: 24px;
font-family: PingFangSC-Semibold;
font-size: 16px;
color: #F34D00;
text-align: left;
line-height: 24px;
font-weight: 600;
margin-top:22px;
margin: 22px auto
}
.tip{
  margin-top: 20px;
  color: #F34D00;
  font-size: 16px;
}

</style>
