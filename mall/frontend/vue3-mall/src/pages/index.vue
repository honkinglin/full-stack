<template>
  <div class="index-wrapper" :style="{ backgroundColor: '#ffffff' }">
    <HeaderCom></HeaderCom>
    <div class="nav-wrapper">
      <div class="middle-wrapper">
              <div class="pop-wrapper">
            <el-popover
              placement="right"
              :key="index"
              v-for="(item, index) in types"
              trigger="hover"
            >
              <div :key="_index" v-for="(_item, _index) in item.childCategory">                
                <a
                  @click="locationFn('/search?keyword=&categoryId==' + _item.id)"
                  class="good-item"
                  >{{ _item.name }}
                </a>
                <div>
                  <a
                    @click="locationFn('/search?keyword=&categoryId==' + item2.id)"
                    :key="index2"
                    class="good-item"
                    v-for="(item2, index2) in _item.childCategory"
                    :style="{ fontSize: '10px' }"
                    >·{{ item2.name }}</a
                  >
                </div>
              </div>
            <template #reference>
            <div class="pop-item-wrapper">
                <img v-if="index===0" width="22" height="22" class="item-logo" src="../assets/fruit.png" alt="">
                <img v-if="index===1" width="22" height="22" class="item-logo" src="../assets/seafood.png" alt="">
                <img v-if="index===2" width="22" height="22" class="item-logo" src="../assets/meat.png" alt="">
                <img v-if="index===3" width="22" height="22" class="item-logo" src="../assets/coldwater.png" alt="">
                <img v-if="index===4" width="22" height="22" class="item-logo" src="../assets/vegetable.png" alt="">
                <img v-if="index===5" width="22" height="22" class="item-logo" src="../assets/drink.png" alt="">
                <span>{{item.name}}&nbsp;&nbsp;&nbsp;></span>
            </div>

            </template>

            </el-popover>
      </div>
      <div class="banner">
            <el-carousel height="447">
              <el-carousel-item>
                <img :style="{height:'447px',width:'100%',display:'block'}" src="../assets/banner-1.png" />
              </el-carousel-item>
                            <el-carousel-item>
              <img :style="{height:'447px',width:'100%',display:'block'}" src="../assets/banner-2.png" />

              </el-carousel-item>
                            <el-carousel-item>
              <img :style="{height:'447px',width:'100%',display:'block'}" src="../assets/banner-3.png" />

              </el-carousel-item>
                                       <el-carousel-item>
              <img :style="{height:'447px',width:'100%',display:'block'}" src="../assets/banner-4.png" />

              </el-carousel-item>
            </el-carousel>
      </div>
      </div>
    </div>
          <div class="items-wrapper-wrapper">
          <div class="items-wrapper">
      <div class="tip">
        <img height="27" class="item-logo" src="../assets/chang.png" alt="">
        <a class="more" @click="locationFn('/search')">查看全部>></a>
      </div>
      <div class="goods">
        <div
        class="bottom-good-item"
            v-for="(item, index) in productList"
            :key="index"
            @click="locationFn('/detail?id=' + item.id)"
        >
          <!-- <img v-if="index%2===0" width="58" height="24" class="tip-img" src="item.image" alt="" />
          <img v-if="index%3===0" width="58" height="24" class="tip-img" src="item.image" alt="" />
         -->
        <div class="img-wrapper">
          <img class="good-img" :src="item.image" alt="" />
        </div>
          <div class="item-bottom">
            <div class="name">{{ item.name }}</div>
            <div class="price"> <span :style="{color:'#e4393c',fontSize:'20px',fontWeight:'700'}">￥{{ item.price / 100 }}</span></div>
          
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
import Footer from '../components/footer-com.vue';
import VueSlickCarousel from 'vue-slick-carousel';
import 'vue-slick-carousel/dist/vue-slick-carousel.css'
import 'vue-slick-carousel/dist/vue-slick-carousel-theme.css'
import http from './http';
import api from './api';

console.log('pppp')
    export default {
			components:{
				HeaderCom,
				Footer,
				VueSlickCarousel,
			},
			data(){
    		return {
					types:[],
					productList:[],
					settings:{
						dots: true,
						infinite: true,
						centerMode: true,
						slidesToScroll: 1,
						centerPadding: '150px',
						variableWidth: true,
						initialSlide: 1,
						autoplay: true,
					}
    		}
    	},
			mounted(){
					this.getData();
					this.getProduct();
          console.log('carousel__container:::',document.getElementsByClassName('el-carousel__container'))
          document.getElementsByClassName('el-carousel__container')[0].style.height = '100%'
			},
			methods: {

        locationFn(url){
            this.$router.push(url)
        },
        getProduct(){
          http.get(api.productList,{pageSize: 20},1).then((res)=>{
            this.productList = res.data.list;
          })
        },

        getData(){
            http.get(api.categoryList,null,1).then((res)=>{
              this.types = res.data
              console.log('types::',this.types);
            })
        }
			},
    }
</script>


<style lang="less" scoped>
  .el-carousel{
    height: 100%;
    border-top-right-radius: 10px;
    border-bottom-right-radius: 10px;
  }
  .el-carousel__container{
    height: 100%!important;
    height: 440px;
  }
  .good-item{
    cursor: pointer;
    padding: 4px;
    margin-top: 10px;
    &:not(:first-child){
      margin-top: 10px;
    }
  }
  .good{
    cursor: pointer;
  }
  .more-items{
    padding: 0;
  }
  .middle-wrapper{
    display: flex;
    height: 440px;
    .banner{
      flex:1;
    }
  }
  .pop-wrapper{
    display: flex;
    flex:none;
    // width: 100px;
    width:192px;
    background:#4A4D4F;
    color:white;
    flex-direction: column;
    justify-content: space-between;
    border-top-left-radius: 10px;
    border-bottom-left-radius: 10px;
    // padding:12px;
    span{
      display: inline-block;
      width: 100px;
      line-height: 50px;
      cursor: pointer;
    }
  }
  .tip{
    display: flex;
    justify-content: space-between;
    margin-top: 20px;
    align-items: flex-end;
    margin-bottom: 20px;
  }
  .goods{
    display: flex;
    flex-wrap: wrap;
    margin-bottom: 60px;
    justify-content: space-between;
    .bottom-good-item{
      flex: 0 0 22%;
      cursor: pointer;
      display:flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      background: #fff;
      margin: 10px;
      box-shadow: 0 6px 10px 0 rgb(95 101 105 / 15%);
      // width: 270px;
      // height: 220px;
      // background: #F9F9F9;
      box-shadow: 0px 6px 12px 0px rgba(55,55,55,0.2);
      border-radius: 8px;
      // padding: 30px 0;
      .img-wrapper{
        height: 152px;
        padding-top: 10px;
        flex: none;
          img{
            max-width: 231px;
            max-height: 130px;
          }
      }
      .item-bottom{
        background: #F8FAFC;
        width:100%;
        flex: 1;
        .name{
          font-family: PingFangSC-Regular;
font-size: 14px;
color: #545C63;
line-height: 20px;
font-weight: 400;
margin-top: 10px;
padding-left: 10px;
        }
        .price{
          font-family: PingFangSC-Semibold;
font-size: 14px;
margin-top: 10px;
color: #F04314;
line-height: 18px;
font-weight: 600;
padding-left: 10px;
padding-bottom: 8px;
        }
        div{
          text-align: left;
        }
      }
    }
  }
  .nav-wrapper{
    // padding:  0 160px 24px 160px;
    padding:  0 0 24px 0;
      width: 1152px;
    margin: 0 auto;
  }
  .good-img{
    border-radius: 4px;
  }
  .pop-item-wrapper{
    display: flex;
    justify-content: center;
    align-items: center;
    height:100%;
    &:hover{
background: #2D353A;
    }
  }
  .items-wrapper{
    // padding: 24px 160px 84px 160px;
    padding: 24px 0px 84px 0px;
    width: 1152px;
    margin: 0 auto;
  }
  .items-wrapper-wrapper{
    width: 100%;
    background: #F8FAFC;
  }
</style>
