<template>

<div class="login">
  <div class="content">
    <div class="left">
      <!-- <img width="337" height="380" src="../assets/loginbg.png" alt=""> -->
    </div>
    <div class="right">
      <div class="title">账户注册</div>
 <el-input
          class="name"

                v-model="userName"
                placeholder="请输入用户名"
              ></el-input>
<el-input
          class="password"
                show-password
                v-model="password"
                placeholder="请输入密码"
              ></el-input>
              <div class="item">
              <el-input
              class="email"
                v-model="email"
                placeholder="请输入邮箱"
              ></el-input>
               <el-button class="email-btn" size="small" @click="sendEmail" type="primary"
                  >发送验证码到邮箱{{start ? `(${time})` : ''}}</el-button
                >
      </div>
            <div class="item">
 <el-input
                v-model="code"
                placeholder="请输入验证码"
              ></el-input>
      </div>
                  <el-button v-if="!!code" class="login-btn" @click="login" type="primary"
              >注册</el-button
            >
      <div class="register-info" @click="locationFn('/login')">已有账号，去登录></div>
    </div>
  </div>
</div>
</template>

<script>
/* eslint-disable */

import http from './http';
import api from './_api';

	export default {
	    data(){
			return {
        userName:'',
				password: '',
        code:null,
        email: '',
        time:60,
        start: false,
        show: true,
			}
		},
		mounted(){
		},
		computed: {
		},
		methods: {
      sendEmail(){
        http.post(api.emailSend,{emailAddress:this.email},1).then((res)=>{
          if(res.status === 10000){
            this.start = true;
                    setInterval(() => {
             if(this.time !==0){
          this.time = this.time -1
          }
        }, 1000)
          }else{
            		this.$message({
										type: 'error',
										message: res.msg
								});
          }
        })
      },
			locationFn(url){
					this.$router.push(url)
			},
			login(){
        if(!this.userName){
						this.$message({
								type: 'error',
								message: '请输入用户名'
						});
            return;
        }
        if(!this.password){
						this.$message({
								type: 'error',
								message: '请输入密码'
						});
            return;
        }
        http.post(api.register,{userName:this.userName,password:this.password,emailAddress:this.email,verificationCode:this.code},1).then((res)=>{
            if(res.status === 10000){
                // localStorage.setItem('username',this.userName);
                // localStorage.setItem('personalizedSignature',res.data.personalizedSignature);
                // localStorage.setItem('role',res.data.role);
                this.$message({
                  type: 'success',
                  message: '注册成功'
								});
                this.$router.push('/login')
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
  width: 100%!important;
}
.logo{
  display: flex;
  margin-top: 20px;
}
.content{
  display: flex;
  width: 720px;
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
      margin-top: 20px
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
  color: #fff;
  font-size: 12px;
}
.item{
  display:flex;
  width:320px;
  margin: 20px auto;
  // .el-input__inner{
  //   flex: 1;
  // }
  .email{
    flex: 1;
  }
  .email-btn{
    flex: none;
  }
}
 .register-info{
    color:#F34D00;
    margin-right: 12px;
    margin-top: 20px;
    margin-bottom: 12px;
  }

</style>
