<template>
  <div>
    <HeaderCom></HeaderCom>
    <div class="content">
      <div class="header-wrapper">
      <div class="header">
        <div class="left">
          <span  class="desc">订单详情</span>
          <el-progress v-if="status === 0" :text-inside="true" :stroke-width="20" :percentage="30" status="exception">
            <span>已取消</span>
          </el-progress>
          <el-progress v-if="status === 10" :text-inside="true" :stroke-width="20" :percentage="35" status="exception">
            <span>待付款</span>
          </el-progress>
          <el-progress v-if="status === 20" :text-inside="true" :stroke-width="20" :percentage="70" status="warning">
            <span>付款成功</span>
          </el-progress>
                  <el-progress v-if="status === 30"  :text-inside="true" :stroke-width="20" :percentage="90" status="warning">
            <span>出库成功</span>
          </el-progress>
                  <el-progress v-if="status === 40"  :text-inside="true" :stroke-width="20" :percentage="100" status="success">
            <span>交易成功</span>
          </el-progress>
        </div>
        <el-button  size="small" @click="locationFn('/orders')" type="primary">返回订单列表</el-button>
      </div>
      </div>
      <div class="info-card-wrapper">

      <div class="info-card">
       <div class="info-card-content">
        <div class="info-title">
          <span class="title">核对订单信息</span>
          <span class="tip">共{{goods.length}}件商品</span>
        </div>
                        
        <div class="items" :key="index" v-for="(item,index) in goods" >
            <img
              :src="item.productImg"
              width="160"
              height="90"
              alt=""
            />
            <span class="name">{{ item.productName }}</span>
            <span class="num">数量：{{ item.quantity }}</span>
            <span class="money">¥{{ item.unitPrice / 100 }} × {{ item.quantity }}</span>
        </div>
        <div class="adrress-wrapper">
          <div class="adrress">
          
            <span class="adrress-name">收货信息：</span>
            <span class="adrress-info">{{address}}</span>
            <span class="user">{{name}}</span>
            <span class="phone">{{phone}}</span>
          </div>
          <div class="pay">
          <span>支付方式：</span>
          <span>支付宝在线支付</span>
          </div>
        </div>
        <div class="content-footer">
          <div class="item">
            <div class="item-name">配送费：</div>
            <div class="item-value1">¥0</div>
          </div>
            <div class="item item2">
            <div class="item-name">商品金额：</div>
            <div class="item-value2">¥{{ totalPrice / 100 }}</div>
          </div>
                          <div class="item item3">
            <div class="item-name">应付金额：</div>
            <div class="item-value3">¥{{ totalPrice / 100 }}</div>
          </div>
        </div>
        <div class="btns-wrapper">
                         <el-button  v-if="status === 10" class="login-btn" @click="cancelOrder" type="danger"
              >取消订单</el-button
            >
                   <el-button v-if="status === 10" class="login-btn" @click="toPay" type="primary"
              >去支付</el-button
            >
       <el-button v-if="status === 0" class="login-btn" type="success"
              >已取消</el-button
            >
            <el-button v-else-if="status !== 40" class="login-btn" @click="getGood" type="success"
              >确认收货</el-button>
        </div>
       </div>
      </div>
      </div>
    </div>
    <Footer></Footer>
  </div>
</template>

<script>
/* eslint-disable */

import HeaderCom from '../components/header-com.vue';
import NavCom from '../components/nav-com.vue';
import Footer from '../components/footer-com.vue';
import http from './http'
import api from './api'
    export default {
        data(){
            return {
                id:'',
                current: 0,
                address:'',
                address_money: 10,
                all:20,
                name:'',
                phone: '',
                status: '',
                totalPrice:'',
                goods:[
                ]
            }
        },
    	components: {
    		HeaderCom,
            NavCom,
            Footer,
    	},
        created(){
        },
        mounted(){

                this.getData();
        },
        methods: {
            toPay(){
                 this.$router.push('/pay?orderNo='+window.location.hash.split('=')[1])
            },
            getGood(){
                http.post(api.orderFinish,{orderNo: window.location.hash.split('=')[1]}).then((res)=>{
                                                    if(res.status === 10000){
                                                        this.$message({
                                                            type: 'success',
                                                            message: '收货成功'
                                                        }); 
                                                        this.$router.push('/orders');
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
            cancelOrder(){
      http.post(api.orderCancel,{orderNo: window.location.hash.split('=')[1]}).then((res)=>{
                                                                        if(res.status === 10000){
                                                                             this.$message({
                    type:'success',
                    message:'取消成功'
                })
                        this.$router.push('/orders')
                                                                        }else{
                                                                              this.$message({
                    type:'error',
                    message:res.msg
                })
                                                                        }
                                                                    })
            },
    getData(){
        http.get(api.orderDetail,{orderNo:window.location.hash.split('=')[1]}).then((res)=>{
            if(res.status === 10000){
                this.goods = res.data.orderItemVOList;
                this.name = res.data.receiverName;
                this.phone = res.data.receiverMobile;
                this.address = res.data.receiverAddress;
                this.totalPrice = res.data.totalPrice;
                this.status = res.data.orderStatus;
            }else{
                this.$message({
                    type:'error',
                    message:res.msg
                })
            }
        })
    }
        },
    }
</script>
<style lang="less" scoped>
.content{
  margin-top: 30px;
  .header-wrapper{
    width: 100%
  }
  .header{
    width: 1152px;
    margin: 0 auto;
    display: flex;
    align-items: center;
    justify-content: space-between;
        margin-top: -24px;
  height:64px;
  padding:0 160px;
  line-height: 64px;
    .left{
      display: flex;
    }
    .desc{
font-family: PingFangSC-Semibold;
font-size: 12px;
color: #07111B;
line-height: 24px;
font-weight: 600;
    }
    .el-progress{
      width: 300px;
      margin-left: 100px;
    }
  }
}
.info-wrapper{
  display: flex;
  justify-content: center;
  margin-top: 20px;
  border-top: 1px solid #eeeeee;
  padding-top: 20px;
}
.info{
  width: 50%;
}
.footer{
  padding-right: 100px;
  padding-bottom: 20px;
  margin-bottom: 40px;

  p{
    text-align: right;
    i{
      font-size: 20px;
      color: #e4393c;
    }
  }
}
.info-card-wrapper{
  width: 100%;
        background-color: #f3f3f3;
        margin-bottom: 91px;
}
.info-card{
          height: calc(100vh - 220px);
          width: 1152px;
          margin: 0 auto;
        display:flex;
        align-items: center;
}
.info-card-content{
  width: 992px;
  // height: 770px;
  background: #FFFFFF;
  box-shadow: 0px 4px 8px 0px rgba(0,0,0,0.1);
  border-radius: 8px;
  padding:24px 33px;
}
.info-title{
  border-bottom: 1px solid #F1F1F1;
  padding-bottom: 16px;
  text-align:left;
  .title{
    font-family: PingFangSC-Semibold;
    font-size: 12px;
    color: #07111B;
    line-height: 24px;
    font-weight: 600;
  }
  .tip{
    font-family: PingFangSC-Regular;
font-size: 12px;
color: #93999F;
line-height: 24px;
font-weight: 400;
margin-left: 15px;
  }
}
.items{
  display:flex;
  align-items: center;
  justify-content: space-between;
  margin-top: 20px;
  .name{
    font-family: PingFangSC-Regular;
font-size: 14px;
color: #07111B;
line-height: 22px;
font-weight: 400;
  }
  .num{
    font-family: PingFangSC-Regular;
font-size: 12px;
color: #93999F;
line-height: 22px;
font-weight: 400;
  }
  .money{
    font-family: PingFangSC-Regular;
font-size: 12px;
color: #F04314;
line-height: 24px;
font-weight: 400;
  }
}
.adrress-wrapper{
  border-top: 1px solid #F1F1F1;
  border-bottom: 1px solid #F1F1F1;
  padding: 14px 0;
  margin-top:20px;
  .adrress{
    text-align: left;
    .adrress-name{
      font-family: PingFangSC-Semibold;
      font-size: 12px;
      color: #07111B;
      line-height: 24px;
      font-weight: 600;
    }
    .adrress-info{
      font-family: PingFangSC-Regular;
font-size: 12px;
color: #07111B;
line-height: 24px;
font-weight: 400;
    }
    .user{
      font-family: PingFangSC-Regular;
font-size: 12px;
color: #93999F;
line-height: 24px;
font-weight: 400;
margin-left: 27px;
    }
    .phone{
      font-family: PingFangSC-Regular;
font-size: 12px;
color: #93999F;
line-height: 24px;
font-weight: 400;
margin-left: 10px;
    }
  }
}
.pay{
  text-align: left;
  span{
    &:first-child{
      font-family: PingFangSC-Semibold;
font-size: 12px;
color: #07111B;
line-height: 24px;
font-weight: 600;
    }
    &:last-child{
      font-family: PingFangSC-Regular;
font-size: 12px;
color: #0079F1;
line-height: 24px;
font-weight: 400;
    }
  }
}

.content-footer{
    padding: 21px;
    border-bottom: 1px solid #F1F1F1;
    text-align: right;
    .item{
        display: flex;
        justify-content: flex-end;
        .item-name{
            font-family: PingFangSC-Semibold;
            font-size: 12px;
            color: #07111B;
            line-height: 24px;
            font-weight: 600;
        }
    }
    .item2{
        margin-top: 7px;
    }
    .item3{
        margin-top: 15px;
    }
    .item-value1{
        font-family: PingFangSC-Regular;
font-size: 12px;
color: #F04314;
line-height: 24px;
font-weight: 400;
    }
        .item-value2{
font-family: PingFangSC-Regular;
font-size: 12px;
color: #F04314;
line-height: 24px;
font-weight: 400;
    }
        .item-value3{
font-family: PingFangSC-Regular;
font-size: 18px;
color: #F04314;
line-height: 24px;
font-weight: 400;
    }

}
.btns-wrapper{
  display:flex;
  align-items: center;
  justify-content: flex-end;
  margin-top: 30px;
}
.login-btn{
  width: 120px;
height: 36px;
// background: #F34D00;
border-radius: 18px;
}

</style>
