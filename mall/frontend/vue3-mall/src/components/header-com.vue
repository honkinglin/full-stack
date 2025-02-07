<template>
  <div>
    <div class="header-wrapper">
    <header>
    <img @click="locationFn('/index')" width="170" class="logo" src="../assets/logo.png" alt="">
    <div class="right-wrapper">
        <div class="search-wrapper">
          <el-input class="input-search" v-model="searchName" placeholder="请输入商品名称"></el-input>
          <i class="el-icon-search" @click="locationFn2"></i>
        </div>
      <div v-if="login" class="desc-wrapper">
      
              <a class="cat-wrapper" @click="locationFn('/cart')" :style="{ color: '#e4393c',cursor:'pointer',marginLeft: '20px' }">
    <img width="24" height="24" src="../assets/cat.png" alt="">
                <span class="cat-text">购物车</span>
              </a>
          <div class="name">欢迎您，{{name}}</div>
          <el-dropdown>
            <span size="small" type="primary">
              个人中心<i class="el-icon-arrow-down el-icon--right"></i>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="locationFn('/personal')">个人中心</el-dropdown-item>
                <el-dropdown-item @click="locationFn('/orders')">我的订单</el-dropdown-item>
                <el-dropdown-item @click="logout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
      </div>
        <div class="btn-wrapper" v-else :style="{ display: 'flex', alignItems: 'center' }">
          <a @click="locationFn('/login')">请登录</a>
          <!-- <a @click="locationFn('/register')">免费注册</a> -->
          <a @click="locationFn('/login')" :style="{ color: '#e4393c',cursor:'pointer',marginLeft: '20px' }">
            我的购物车
          </a>
          </div>
    </div>
    </header>
    </div>
  </div>
</template>

<script>
import http from "../pages/http";
import api from "../pages/_api";

export default {
  data() {
    return {
      login: false,
      searchName: "",
      name: "",
      num: 0,
    };
  },
  created() {},
  mounted() {
    if (!localStorage.getItem("username")) {
      this.login = false;
    } else {
      this.login = true;
      this.name = localStorage.getItem("username");
    }
  },
  methods: {
    reload(){
      window.reload();
    },
    locationFn(url) {
      this.$router.push(url);
    },
    locationFn2(){
      this.$router.push("/search?keyword=" + this.searchName);
    },
    logout() {
      http.post(api.logout,{},1).then((res) => {
        if (res.status === 10000) {
          localStorage.removeItem("username");
          localStorage.removeItem("personalizedSignature");
          localStorage.removeItem("role");
          sessionStorage.removeItem("token");
          this.$router.push("/login");
        }
      });
    },
  },
};
</script>

<style lang="less" scoped>
.header-wrapper{
  border-bottom: 1px solid #F3F5F6;
  background-color: #fff;
  margin-bottom: 24px;
}
header{

  // background-color: #e3e4e5;
  color: #999;
  display: flex;
  padding: 6px;
  align-items: center;
  justify-content: space-between;
  // margin-bottom: 24px;
  padding: 12px 0px;
  margin: 0 auto 0 auto;
  width: 1152px;
  overflow-x: hidden;
}
.shopcart{
  cursor: pointer;
}
.btn-wrapper{
  width: 150px;
height: 20px;
font-family: PingFangSC-Regular;
font-size: 14px;
color: #999999;
font-weight: 400;
  a{
    margin-left: 10px;
  }
}
.desc-wrapper{
  display: flex;
  justify-content: flex-end;
  flex-direction: row;
  align-items: center;
}
.name{
  margin-right: 20px;
}
.search-wrapper{
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 30px;
  .el-icon-search{
    font-size: 20px;
    cursor: pointer;
    margin-left: -28px;
    z-index: 10;
  }
}

.right-wrapper{
  display: flex;
}
.input-search{
  width: 300px;
}
.cat-text{
  width: 42px;
height: 20px;
font-family: PingFangSC-Semibold;
font-size: 14px;
color: #F34D00;
font-weight: 600;
margin-left: 6px;
}
.cat-wrapper{
  margin-right: 24px;
  display: flex;
  justify-content: center;
  align-items: center;
}
</style>

