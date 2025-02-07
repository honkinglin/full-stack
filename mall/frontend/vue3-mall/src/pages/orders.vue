<template>
  <div>
    <div>
      <HeaderCom></HeaderCom>
      <div class="content">

<el-card
v-for="(item, index) in orders"
  :key="index"
 class="box-card">
  <template #header>
    <div class="card-header">
      <div class="desc-wrapper">
        <span class="item-name">订单号：<span class="item-value">{{item.orderNo}}</span></span>
        <span class="item-name">下单时间：<span class="item-value">{{getTime(item.createTime)}}</span> </span>
        <span class="item-name">订单状态：<span class="item-value-red">{{item.orderStatusName}}</span> </span>
      </div>
      <el-button class="button" type="text">
        <a
                                      @click="
                                        locationFn(
                                          '/order_detail?orderId=' +
                                            item.orderNo
                                        )
                                      "
                                      >订单详情</a
                                    ></el-button>
    </div>
  </template>
    <div>
                      <div
v-for="(_item, _index) in item.orderItemVOList"
                        :key="_index"
                        :style="{ height: 'auto',marginTop:'24px', alignItems:'center', display:'flex',justifyContent:'center' }"
                      >
                                        <div class="good-item">
                                            <a>
                                              <img
                                                :src="_item.productImg"
                                                width="160"
                                                height="90"
                                                alt=""
                                              />
                                            </a>
                                          </div>
                                          <p class="good-item name">
                                            <a
                                              @click="locationFn('/detail?id=' + _item.productId)"
                                              >{{ _item.productName }}
                                              </a>
                                          </p>
                                          <p class="good-item num">数量：{{ _item.quantity }}</p>
                                          <p class="good-item money"
                                          >
                                            ¥{{
                                              (_item.unitPrice / 100).toFixed(
                                                2
                                              )
                                            }}
                                          </p>
                      </div>
  </div>

</el-card>
      </div>
      
      <div class="pagination-wrapper">
        <el-pagination
          :style="{ marginBottom:'160px',marginTop: '20px' }"
          v-if="total > 0"
          layout="prev, pager, next"
          class="pagiantion-cus"
          :page-size="3"
          :total="total"
          :current-page="current"
          @current-change="pageChange"
        >
        </el-pagination>
        </div>

        <div v-if="!orders || orders.length === 0" class="empty">
  暂无订单数据，请下单
</div>
    </div>
    <Footer></Footer>
  </div>
</template>

<script>
/* eslint-disable */

import HeaderCom from '../components/header-com.vue';
import Footer from '../components/footer-com.vue';
import moment from 'moment-mini'

    import http from './http'
    import api from './api'
    export default {
        data(){
            return {
                orders:[
                    
                ],
                current: 1,
                total:0,
            }
        },
    	components: {
    		HeaderCom,
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
            pageChange(cur){
                this.current =cur;
                this.getData();
            },
            getTime(t){
              return moment(t).format('YYYY.MM.DD HH:mm:ss')
            },
            getData(){
                http.get(api.orderList,{
                    pageSize: 3,
                    pageNum: this.current,
                }).then((res)=>{
                    if(res.status === 10000){
                        this.orders = res.data.list;
                        this.total = res.data.total;
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
.pagination-wrapper{
  width: 100%;
}
.pagiantion-cus{
  margin: 0 auto;
  width: auto;
}
  .header{
    display: flex;
    align-items: center;
    padding: 10px;
    .desc{
      font-size: 24px;
    }
    .el-progress{
      width: 300px;
      margin-left: 100px;
    }
  }
  .good-item{
    flex:1
  }
  .card-header{
    text-align: left;
    display: flex;
    justify-content: space-between;
  }
  .box-card{
    margin-top: 20px;
    width: 800px!important;
    padding: 24px 33px;
    width: 992px;
background: #FFFFFF;
box-shadow: 0px 4px 8px 0px rgba(0,0,0,0.1);
border-radius: 8px;
  }
  .empty{
    font-size: 20px;
  }
  .desc-wrapper{
    display: flex;
    flex-direction: column;
    .item-name{
      font-family: PingFangSC-Semibold;
      font-size: 12px;
      color: #07111B;
      line-height: 24px;
      font-weight: 600;
    }
    .item-value{
      font-family: PingFangSC-Regular;
font-size: 12px;
color: #07111B;
line-height: 24px;
font-weight: 400;
    }
    .item-value-red{
      font-family: PingFangSC-Semibold;
font-size: 12px;
color: #F34D00;
line-height: 24px;
font-weight: 600;
    }
  }
  .button{
    font-family: PingFangSC-Semibold;
font-size: 12px;
color: #1771FF;
line-height: 24px;
font-weight: 600;
  }
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
color: #07111B;
line-height: 24px;
font-weight: 400;
  }
</style>
