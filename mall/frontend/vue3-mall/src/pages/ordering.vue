<template>
  <div>
    <HeaderCom></HeaderCom>
    <div class="content">
      <div class="header-wrapper">
      <div class="header">
        <span  class="desc">请填写收件信息，并提交订单</span>
        <el-progress :text-inside="true" :stroke-width="20" :percentage="20" status="exception">
           <span>下单中</span>
        </el-progress>
      </div>
      </div>
      <div class="info-card-wrapper">

      <div class="info-card">
        <div  class="info-content">
            <div class="info-wrapper">
                <div class="info">
                        <div
                    className="items"
                    :style="{ display: 'flex', alignItems: 'center', width: '100%' }"
                >
                    <span :style="{ width: '100px', fontSize: '16px', whiteSpace:'nowrap' }">收件人姓名：</span>
                    <el-input v-model="name" placeholder="请输入收件人"></el-input>
                </div>
                <div
                    className="items"
                    :style="{
                    display: 'flex',
                    alignItems: 'center',
                    width: '100%',
                    marginTop: '20px',
                    }"
                >
                    <span :style="{ width: '100px', fontSize: '16px' }">电话：</span>
                    <el-input v-model="phone" placeholder="请输入电话"></el-input>
                </div>
                <div
                    className="items"
                    :style="{
                    display: 'flex',
                    alignItems: 'center',
                    width: '100%',
                    marginTop: '20px',
                    }"
                >
                    <span :style="{ width: '100px', fontSize: '16px' }">地址：</span>
                    <el-input v-model="address" placeholder="请输入地址"></el-input>
                </div>
            </div>
                <div class="footer">
      <p>应付总价 <i>{{(money / 100).toFixed(2)}}</i>元</p>
       <el-button class="login-btn" @click="orderSubmit" type="primary">提交订单</el-button>
    </div>
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
import http from './http2'
import api from './api'
    export default {
        data(){
            return {
                list:[],
                address:'',
                name: null,
                phone:null,
                money:null,
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
          							locationFn(url){
					this.$router.push(url)
			},

            getData(){
                http.get(api.cartList).then((res)=>{
                    if(res.status === 10000){
                        let arr = [];
                        res.data.map((item)=>{
                            if(item.selected === 1){
                                arr.push(item);
                            }
                        })
                        this.list = arr;
                        let money = 0;
                           this.list.map((item)=>{
            money = item.totalPrice + money
        });
        this.money = money
                    }else{
                                         this.$message({
                        type: 'error',
                        message: res.msg
                    }); 
                    }
                })
            },
            orderSubmit(){
                if(!this.name){
                    this.$message({
                        type: 'error',
                        message: '请填写收件人'
                    }); 
                    return;
                }
                if(!this.phone){
                    this.$message({
                        type: 'error',
                        message: '请填写电话'
                    }); 
                    return;
                }
                if(!this.address){
                    this.$message({
                        type: 'error',
                        message: '请填写收件人地址'
                    }); 
                    return;
                }
                http.post(api.orderCreate,{receiverName:this.name,receiverMobile:this.phone,receiverAddress:this.address}).then((res)=>{
                    if(res.status === 10000){
                        this.$router.push('/order_detail?orderNo=' + res.data)
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
</script>

<style lang="less" scoped>
.content{
    .header-wrapper{
        width: 100%;
  margin-top: -24px;
  padding:0 160px;
    }
  .header{
    display: flex;
    align-items: center;
    width: 1152px;
  height:64px;
    margin: 0 auto;
        text-align: left;
  line-height: 64px;
    .desc{
        font-family: PingFangSC-Semibold;
        font-size: 12px;
        color: #07111B;
        line-height: 24px;
        font-weight: 600;
        padding: 0 160px;
    }
    .el-progress{
      width: 300px;
      margin-left: 100px;
    }
  }
}
.info-wrapper{
//   display: flex;
//   justify-content: center;
//   align-items: center;
//   border-top: 1px solid #eeeeee;
  padding-top: 20px;
}
.info{
  width: 500px;
}
.info-card-wrapper{
        width: 100%;
        background-color: #f3f3f3;
}
.info-card{
        height: calc(100vh - 220px);
                display: flex;
        justify-content: center;
        align-items: center;
        flex-direction: column;
        width: 1152px;
        margin: 0 auto;
}
.footer{
//   padding-right: 100px;
//   padding-bottom: 20px;
  p{
    text-align: right;
    i{
      font-size: 20px;
      color: #e4393c;
    }
  }
}
.info-content{
    display:flex;
    align-items: center;
    justify-content: center;
    width: 992px;
height: 770px;
background: #FFFFFF;
box-shadow: 0px 4px 8px 0px rgba(0,0,0,0.1);
border-radius: 8px;
}
.login-btn{
    width: 120px;
height: 36px;
background: #F34D00;
border-radius: 18px;
}
</style>
