<template>
  <div class="pay-wrapper">
      <p>请用支付宝/微信扫一扫</p>
            <img
              width="300"
              height="300"
              :src="img"
              :style="{ display: 'block',marginBottom:'20px' }"
            />
             <el-button @click="paySuccess" type="primary"
              >支付成功</el-button
            >
    <div class="foot"></div>
  </div>
</template>

<script>
/* eslint-disable */

import http from './http'
import api from './api'

    export default {
        data(){
            return {
                img:"",
            }
        },
        created(){

        },
        mounted(){
            this.initData()
        },
        methods: {
            paySuccess(){
                this.$router.push('/order_detail?orderNo='+ window.location.hash.split('=')[1])
            },
            async initData(){
                http.get(api.orderCode,{orderNo:window.location.hash.split('=')[1]}).then((res)=>{
                    if(res.status === 10000){
                        this.img = res.data;
                    }else{
                        this.$message({
                            type:'error',
                            message:res.msg
                        })
                    }
                })
            },
        },
    }
</script>

<style lang="less" scoped>
.pay-wrapper{
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}
</style>
