<template>
  <div :style="{ backgroundColor: '#ffffff' }">
    <HeaderCom></HeaderCom>
      <div className="content-header-wrapper">

      <div className="content-header">
                商品详情
      </div>
      </div>
      <div class="detail-wrapper-wrapper">

      <div class="detail-wrapper">
    <div class="good-detail">
      <!-- <div class="title">
        {{ name }}
      </div> -->
      <div class="bottom">
          <img
          class="detail-img"
            :style="{ width: '540px', height: '304px' }"
            :src="img"
          />
          <div class="detail-left">
            <div class="desc">
              <div class="subname">
              商品名称：{{ name }}
              </div>
              <div class="subname">
              其他：{{ subName }}
              </div>
            </div>
            <div class="left-bottom">
                          <div class="price">售价：{{ money / 100 }}元</div>
              <el-button class="buy" type="primary" @click="addFn"
                >加入购物车</el-button
              >
              </div>
          </div>
      </div>
    </div>
    </div>
      </div>
    <FooterCom></FooterCom>
    <el-dialog class="buy-dialog" title="" v-model="visible" width="30%">
      <img class="good-img" src="../assets/already.png" alt="">
      <div
        class="already-buy"
      >
        已将商品加入购物车
      </div>
      <div :style="{ display: 'flex', justifyContent: 'center' }">
        <el-button class="cancel-btn" :style="{ marginTop: '20px' }" @click="visible = false"
          >继续购物</el-button
        >
        <el-button
        class="yes-btn"
          :style="{ marginTop: '20px' }"
          type="primary"
          @click="handleOk"
          >去结算</el-button
        >
      </div>
    </el-dialog>
  </div>
</template>

<script>
/* eslint-disable */

import HeaderCom from '../components/header-com.vue';
import FooterCom from '../components/footer-com.vue';
import http from './http';
import api from './api';

    export default {
        data(){
            return {
                visible: false,
                name: '默认商品',
                subName: '默认描述',
                money:'100.00',
                img: ''
            }
        },
    	components: {
    		HeaderCom,
        FooterCom,
    	},
        created(){
            this.initData();
        },
        methods: {
            addFn(){
                http.post(api.cartAdd,{productId: window.location.hash.split('=')[1],count: 1}).then((res)=>{
                    if(res.status === 10000){
                        this.visible = true;
                    }else{
                        		this.$message({
								type: 'error',
								message: res.msg
						});
                    }
                })
            },
initData(){
       let hash = window.location.hash.split('=')[1]
        http.get(api.productDetail,{
            id:hash
        },1).then((res)=>{
            if(res.status === 10000){
                this.name = res.data.name;
                this.img = res.data.image;
                this.subName = res.data.detail;
                this.money = res.data.price;
                this.stock = res.data.stock;
            }
        })
},
            handleOk(){
                this.$router.push('/cart')
                this.visible = false;
            },

        },
    }
</script>

<style lang="less" scoped>
.content-header-wrapper{
  width: 100%;
}
.content-header{
    text-align: left;
  height:64px;
  padding:0 160px;
  line-height: 64px;
  width: 1152px;
  margin-left: -24px;
  font-family: PingFangSC-Regular;
font-size: 18px;
color: #343434;
font-weight: 400;
}
.good-detail{
  .title{
    font-size: 26px;
    font-weight: 700;
    text-align: left;
  }
}
.detail-img{
  width: 540px;
height: 304px;
background: #FFFFFF;
border: 1px solid #EBEBEB;
border-radius: 8px;
}
.detail-wrapper-wrapper{
  width: 100%;
    height: calc(100vh - 220px);
    background: #F8FAFC;
}
.detail-wrapper{
  padding-top: 35px;
  width: 1152px;
  margin: 0 auto;
}
.bottom{
  display: flex;
  margin-top: 20px;
  img{
    flex: none;
  }
  .detail-left{
    margin-left: 24px;
  }
  .desc{
    width: 588px;
height: 250px;
background: #F3F5F6;
border: 1px solid #D5D5D5;
border-radius: 8px;
padding:15px 24px;

    position: relative;
    .price{
      color:#e4393c;
      margin-top: 20px;
      font-size: 22px;
      text-align: right;
    }
  }
  .subname{
    text-align: left;
    font-family: PingFangSC-Regular;
font-size: 18px;
color: #343434;
font-weight: 400;
margin-top:12px;
  }
}
.buy-btn{
  position: absolute;
  bottom: 0;
  right: 0;
}
.left-bottom{
  display:flex;
  justify-content: flex-end;
  align-items: center;
  margin-top: 36px;
}
.price{
  font-family: PingFangSC-Semibold;
font-size: 24px;
color: #F04314;
line-height: 18px;
font-weight: 600;
}
.buy{
  margin-left: 24px;
  width: 120px;
height: 36px;
background: #F34D00;
border-radius: 18px;
}
.cancel-btn{
  width: 120px;
height: 36px;
border: 1px solid #F34D00;
border-radius: 18px;

font-family: PingFangSC-Regular;
font-size: 14px;
color: #F34D00;
text-align: center;
font-weight: 400;
}
.yes-btn{
  width: 120px;
height: 36px;
background: #F34D00;
border-radius: 18px;
}
.already-buy{
font-family: PingFangSC-Medium;
font-size: 21px;
color: #9199A1;
text-align: center;
font-weight: 500;
margin-top: 23px;
text-align: center;
margin-bottom: 46px;
}
.good-img{
  width:64px;
  height:64px;
}
.buy-dialog{
  width: 582px;
  height:310px;
}
</style>
