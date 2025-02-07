<template>
  <div>
    <HeaderCom></HeaderCom>
      <div className="content-header-wrapper">

      <div className="content-header">
        <img width="23" height="22" src="../assets/cat.png" alt="">
        <span class="img-title">购物车</span>
        <span class="tip">        温馨提示：购物车含有限时购商品，不要错过优惠，尽快结算
</span>
        
      </div>
      </div>
      <div class="goods-wrapper">
      <div class="goods">
        <div class="goods-card">
            <div v-if="list.length" class="table-title-header">
                
                <div class="good-item-title">
                    <el-checkbox v-model="selectAll" @change="checkBoxAllClick"
                    >全选</el-checkbox >
                </div>
                    <div class="good-item-title title-img">图片</div>
                    <div class="good-item-title">商品名称</div>
                    <div class="good-item-title">单价</div>
                    <div class="good-item-title caculate">数量</div>
                    <div class="good-item-title">小计</div>
                    <div class="good-item-title">操作</div> 
            </div> 
                     <div
            v-for="(item, index) in list"
            :key="index" 
            class="table-title table-content"
            >
            <div class="good-item-title">
                <el-checkbox
                v-model="item.checked"
                @change="itemClick(value, item)"
                ></el-checkbox>
            </div>
                <div class="good-item-title">
                  <img class="good-img" :src="item.productImage" />
                </div>
                <div class="good-item-title">
                  {{ item.productName }}
                </div>
                <div class="good-item-title ">
                  ¥{{ item.price / 100 }}
                </div>
                <div class="good-item-title caculate">
                  <span
                    v-if="item.quantity != 1"
                    :style="{ marginRight: '8px', cursor: 'pointer' }"
                    @click="onGoodClick(item, index)"
                    >-</span>
                  <el-input
                    v-model="item.quantity"
                    @change="inputClick"
                    @blur="inputBlur(e, item)"
                    :style="{ border: 'none' }"
                    :disabled="true"
                    class="goods_count"
                    placeholder="数值"
                  ></el-input>
                  <span
                    @click="spanClick(item, index)"
                    :style="{ marginLeft: '8px', cursor: 'pointer' }"
                    >+</span
                  >
                </div>
                <div class="good-item-title money">
                   ¥{{ ((item.price / 100) * item.quantity).toFixed(2) }}
                </div>
                <div class="good-item-title delete">
                    <span @click="subContentClick(item)" class="delete-btn">移除</span>
                </div>

         </div>
        <div v-if="!list.length" class="empty">购物车是空的</div>
               <div v-if="list.length" class="content-footer">
                <div class="item">
                 <div class="item-name">配送费：</div>
                 <div class="item-value1">¥0</div>
                </div>
                 <div class="item item2">
                 <div class="item-name">商品金额：</div>
                 <div class="item-value2">¥{{(money / 100).toFixed(2)}}</div>
                </div>
                                <div class="item item3">
                 <div class="item-name">应付金额：</div>
                 <div class="item-value3">¥{{(money / 100).toFixed(2)}}</div>
                </div>
               </div>
               <div  v-if="total" class="buy-btn-wrapper">
                    <el-button class="login-btn" @click="locationFn('/ordering')" type="primary"
                    >去结算</el-button
                    >
               </div>
                     <!-- <div>已选择<span class="count">{{total}}</span>件商品</div> -->
                     <!-- <div class="footer-btn">
                      <div>合计<span class="count">{{ (money / 100).toFixed(2) }}</span>元</div>
                </div> -->
        </div>


      </div>
      </div>


    <FooterCom></FooterCom>
  </div>
</template>

<script>
/* eslint-disable */
import FooterCom from '../components/footer-com.vue';
import http from './http';
import HeaderCom from '../components/header-com.vue';
import api from './api';
    export default {
        data(){
            return {
                list:[],
                selected: [],
                selectAll: false,
                total:0,
                money:null
            }
        },
    	components: {
    		FooterCom,
            HeaderCom
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
            subContentClick(item){
                    http.post(api.cartDelete,{productId: item.productId}).then((res)=>{
                    if(res.status === 10000){
                        this.getData();
                    }else{
                        this.$message({
                            type: 'error',
                            message: res.msg
                        });
                    }
                })
            },

            spanClick(item,index){
                let num ='';
                this.list.map((_item,_index)=>{
                    if(_index === index){
                            _item.quantity = _item.quantity + 1;
                            num = _item.quantity;
                    }
                    return _item;
                })
                http.post(api.cartUpdate,{productId: item.productId,count: num}).then((res)=>{
                    if(res.status === 10000){
                        this.getData();
                    }else{
                        this.$message({
                            type: 'error',
                            message: res.msg
                        });
                    }
                })
            },

            inputBlur(e,item){
                http.post(api.cartUpdate,{productId: item.productId,count: e.target.value}).then((res)=>{
                    if(res.status === 10000){
                        this.getData();
                    }else{
                        this.$message({
                            type: 'error',
                            message: res.msg
                        });
                    }
                })
            },
            inputClick(e){
                let flag = 0;
                e.target.value.split('').map((item)=>{
                    if(item === '-'){
                        flag = 1;
                    }
                })
                if(!flag){
                    this.list.map((_item,_index)=>{
                        if(_index === index){
                            if(_item.quantity !== 0){
                                _item.quantity = e.target.value.replace(/[^\-?\d.]/g,'') ;
                            }
                        }
                        return _item;
                    })
                }
            },
            onGoodClick(item,index){
                let num=''
                this.list.map((_item,_index)=>{
                    if(_index === index){
                        if(_item.quantity !== 0){
                            _item.quantity = _item.quantity - 1;
                            num=_item.quantity;
                        }
                    }
                    return _item;
                })
                http.post(api.cartUpdate,{productId: item.productId,count: num}).then((res)=>{
                    if(res.status === 10000){
                        this.getData();
                    }else{
                        this.$message({
                                type: 'error',
                                message: res.msg
                        });
                    }
                })
            },

            itemClick(value,item){
                console.log('value:',value);
                console.log('item:',item);
                let arr = this.selected;
                if(item.checked){
                    arr.push(item.id);
                }else{
                    if(arr.indexOf(item.id)!== -1){
                        arr.splice(arr.indexOf(item.id),1);
                    }
                }
                    let arr2 = this.list;
                    arr2.map((_item)=>{
                        if(_item.id === item.id){
                            _item.checked = item.checked;
                        }
                    })
                    this.list = arr2;
                    this.selected = arr;
                    if(this.selected.length === this.list.length){
                        this.selectAll =true;
                    }else{
                        this.selectAll =false;
                    }
                    http.post(api.cartSelect,{selected: item.checked ? 1 : 0,productId: item.productId}).then((res)=>{
                        if(res.status === 10000){
                            this.getData();
                        }else{
                            this.$message({
                                    type: 'error',
                                    message: res.msg
                            });
                        }
                    })
            },

            checkBoxAllClick(value){
                console.log('this.selectAll:',this.selectAll)
                console.log('value:',value);
                this.selectAll = value;
                if(this.selectAll){
                    this.list.map((item)=>{
                        item.checked = true;
                    })
                }else{
                    this.list.map((item)=>{
                        item.checked = false;
                    })
                }
                http.post(api.cartSelectAll,{selected: this.selectAll ? 1 : 0}).then((res)=>{
                    if(res.status === 10000){
                        this.getData();
                    }else{
                        													this.$message({
								type: 'error',
								message: res.msg
						});
                    }
                })
            },

            getData(){
                http.get(api.cartList).then((res)=>{
                    if(res.status === 10000){
                        let flag = 1;
                        res.data.map((item)=>{
                            if(item.selected === 0){
                                item.checked = false;
                                flag = 0;
                            }else{
                                item.checked = true;
                            }
                            // item.checked = false;
                        })
                        if(flag === 1){
                            this.selectAll = true;
                        }
                        this.list = res.data;
                        console.log('list:',this.list);
                        let total = 0,money=0;
                        this.list.map((item)=>{
                            if(item.selected === 1){
                                                            total = total + item.quantity;
                            money = money + item.quantity * item.price;
                            }
                        })
                        this.total = total;
                        this.money = money;
                    }else{
                        													this.$message({
								type: 'error',
								message: res.msg
						});
                    }
                })
            },


        },
    }
</script>

<style lang="less" scoped>
    .title{
        display: flex;
        justify-content: flex-start;
        align-items: center;
        color: #e2231a;
        font-weight: 600;
        font-size: 22px;
        margin-bottom:20px;
        margin-top: 60px;
    }
    .table-title{
        width: 100%;
        display: flex;
        align-items: center;
        font-size: 16px;
    }
    .table-title-header{
        width: 100%;
        height:70px;
        display: flex;
        align-items: center;
        font-size: 16px;
        border-bottom: 1px solid #F1F1F1;
        .title-img{
            width: 160px;
            flex: none!important;
        }
        .good-item-title{
            flex: 1;
            font-size:14px;
            flex: 0 0 10%;
            font-family: PingFangSC-Regular;
            font-size: 14px;
            color: #07111B;
            line-height: 22px;
            font-weight: 400;
        }
    }
    .goods-wrapper{
        height: calc(100vh - 220px);
        width: 100%;
        padding-top:20px;
    }
    .goods{
        width: 1152px;
        margin: 0 auto;
        display: flex;
        justify-content: center;
        align-items: center;
        flex-direction: column;
        background-color: #f3f3f3;
        .goods-card{
            width: 1152px;
            overflow-y: scroll;
            height: 680px;
            background: #FFFFFF;
            box-shadow: 0px 4px 8px 0px rgba(0,0,0,0.1);
            border-radius: 8px;
            padding: 0 44px;
        }
    }
    .empty{
        font-size: 36px;
        font-weight: 700;
        color: #e2231a;
        text-align: center;
        margin-top: 200px;
    }
    .count{
             font-size: 36px;
        font-weight: 700;
        color: #e2231a;   
    }
    .table-content{
        .good-item-title{
            margin-top:20px;
            flex: 0 0 10%;
            display: flex;
            justify-content: center;
            align-items: center;
        }
    }

            .caculate{
            flex: 0 0 40%!important;
            display: flex;
            align-items: center;
        }

.delete{
    cursor: pointer;
}
.footer{
    background-color: #f3f3f3;
    padding:16px;
    display: flex;
    margin-top:20px;
    justify-content: space-between;
}
.footer-btn{
    display: flex;
}
.login-btn{
    width: 120px;
    height: 36px;
    background: #F34D00;
    border-radius: 18px;
    margin-top:24px;
}
.logo{
    display: flex;
    justify-content: flex-start;
    margin-bottom: 20px;
}
.img-title{
    font-family: PingFangSC-Regular;
font-size: 18px;
color: #343434;
font-weight: 400;
margin-left: 6px;
}
.tip{
    font-family: PingFangSC-Regular;
font-size: 12px;
color: #666666;
font-weight: 400;
margin-left: 16px;

}
.content-header-wrapper{
    width: 100%;
      margin-top: -24px;
      background-color: #fff;
  height:64px;
}
.content-header{
        text-align: left;
  line-height: 64px;
  width: 1152px;
  margin: 0 auto;
}
.good-img{
    width: 160px;
    height: 90px;
}
.delete-btn{
    font-family: PingFangSC-Regular;
font-size: 12px;
color: #93999F;
line-height: 24px;
font-weight: 400;
}
.money{
    font-family: PingFangSC-Regular;
font-size: 12px;
color: #F04314;
line-height: 24px;
font-weight: 400;
}
.content-footer{
    padding: 21px;
    border-top: 1px solid #F1F1F1;
    border-bottom: 1px solid #F1F1F1;
    margin-top: 20px;
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
.buy-btn-wrapper{
    display: flex;
    justify-content: flex-end;
}
</style>
