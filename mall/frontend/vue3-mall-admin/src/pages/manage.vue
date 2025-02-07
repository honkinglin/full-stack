<template>
	<div class="manage_page fillcontain">
               <Header></Header>
		<div class="page" style="height: 100%;">
				<el-menu :default-active="defaultActive" class="menu" style="min-height: 100%;" theme="dark" router>
					<el-menu-item index="good"><i class="el-icon-goods"></i>商品管理</el-menu-item>
					<el-menu-item index="type"><i class="el-icon-menu"></i>分类管理</el-menu-item>
					<el-menu-item index="order"><i class="el-icon-document-copy"></i>订单管理</el-menu-item>
					<el-menu-item index="person"><i class="el-icon-user"></i>个人信息</el-menu-item>
				</el-menu>
		<router-view></router-view>
		</div>
  	</div>
</template>

<script>
    import Header from '../components/header.vue'
/* eslint-disable */
    export default {
		computed: {
			defaultActive: function(){
				return this.$route.path.replace('/', '');
			}
		},
		components: {
    		Header,
    	},
    }
</script>
<style lang="scss" scoped>

.page{
	padding: 0 60px;
	margin-top: 20px;
	display: flex;
}

.menu{
	width: 120px;
}
.fillcontain{
	flex: 1;
}


</style>
