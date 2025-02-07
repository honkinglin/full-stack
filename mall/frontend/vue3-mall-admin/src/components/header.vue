<template>
    <div class="header">
			<div class="header-left">
				<img width="50" src="../assets/micon.png" alt="">
				<span class="page-name">慕慕生鲜后台管理系统</span>
			</div>
<div class="right">
				<a class="page-name-yes" target='_blank' href="http://127.0.0.1:8080/#/index">慕慕生鲜</a>

		<el-dropdown @command="handleCommand" menu-align='start'>
			<img width="32" height="32" class="avator"  src="../assets/person.png" alt="">
			  <template #dropdown>
				<el-dropdown-menu>
				<el-dropdown-item command="home">
					<a target='_blank' href="http://127.0.0.1:8080/#/index">慕慕生鲜首页</a>
				</el-dropdown-item>
				<el-dropdown-item command="signout">退出登陆</el-dropdown-item>
			</el-dropdown-menu>
				</template>
		</el-dropdown>
</div>

    </div>
</template>

<script>
    import http from '../pages/http';
	import api from '../pages/_api';

    export default {
    	data(){
    		return {
    		}
    	},
			props:['text'],
    	created(){
    	},
    	computed: {
    	},
			methods: {
				async handleCommand(command) {
					if(command == 'signout'){
							http.post(api.logout).then((res)=>{
								if(res.status === 10000){
									sessionStorage.setItem("token", null);
									window.location.href = 'http://127.0.0.1/admin/#/';
								}else{
									this.$message({
				            type: 'error',
				            message: res.msg
				        });
								}
							})
					}
				},
			}
    }
</script>

<style lang="less">
	.header{
		display: flex;
		justify-content: space-between;
		align-items: center;
		padding-left: 20px;
height: 64px;
background: #0079DD;
padding: 0 60px;
	}
	.avator{
		cursor: pointer;
		border-radius: 50%;
		margin-left: 37px;
	}
	.el-dropdown-menu__item{
        text-align: center;
    }
		.header-left{
			display: flex;
			align-items: center;
							a{
					color: #409EFF;
					text-decoration: none;
					
				}
				
			img{
				width: 25px;
				height: 25px;
				margin-right: 20px;

			}
		}
		.page-name, .page-name-yes{
			font-family: PingFangSC-Regular;
font-size: 16px;
color: #FFFFFF!important;
text-align: left;
font-weight: 400;
text-decoration:none;
		}
		.right{
			display:flex;
			align-items: center;
		}
</style>
