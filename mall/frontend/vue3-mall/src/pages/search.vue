<template>
  <div :style="{ backgroundColor: '#ffffff' }">
    <HeaderCom></HeaderCom>
      <div className="search">
                {{ deUrl() ? "搜索‘" + decodeURI(deUrl()) + "’" : "" }}
      </div>
      <div class="content">
        <div class="btn-wrapper">
          <span @click="getData2" class="fbtn" :class="{'active':active1}">综合</span>
          <span @click="getDescData" class="btn" :class="{'active':active2}">价格</span>
        </div>

      <!-- <div class="goods">
        <div
        class="good-item"
            v-for="(item, index) in goods"
            :key="index"
            @click="locationFn('/detail?id=' + item.id)"
        >
          <img :src="item.image" alt="" />
          <p class="name">{{ item.name }}</p>
          <p class="price">{{ item.price / 100 }}元</p>
        </div>
      </div> -->

            <div class="goods">
        <div
        class="bottom-good-item"
            v-for="(item, index) in goods"
            :key="index"
            @click="locationFn('/detail?id=' + item.id)"
        >
        <div class="img-wrapper">
          <img class="good-img" :src="item.image" alt="" />
        </div>
          <div class="item-bottom">
            <div class="name">{{ item.name }}</div>
            <div class="price"> <span :style="{color:'#e4393c',fontSize:'20px',fontWeight:'700'}">￥{{ item.price / 100 }}</span></div>
          
          </div>
</div>
</div>

            <div
        :style="{
          display: 'flex',
          justifyContent: 'center',
          marginBottom: '60px',
        }"
      >
        <el-pagination
          @current-change="handleCurrentChange"
          :current-page="current"
          layout=" prev, pager, next"
          :total="total"
        >
        </el-pagination>
      </div>
      </div>
    <Footer></Footer>
  </div>
</template>

<script>
/* eslint-disable */

import HeaderCom from '../components/header-com.vue';
import Footer from '../components/footer-com.vue';
import http from './http'
import api from './api'

	export default {
	    data(){
			return {
        desc: false,
        current: 1,
        total: 0,
        active1: true,
        active2: false,
        goods:[]
			}
		},
    create(){
      this.active1 = true;
      this.active2 = false;
    },
    components: {
      HeaderCom,
          Footer,
    },
		mounted(){
			this.current =1;
			this.getData();
		},
		watch: {
      $route: {
            handler() {
              console.log('=====')
              this.getData();
                //深度监听，同时也可监听到param参数变化
          },
          // deep: true,
      }
  },
		computed: {
		},
		methods: {
			locationFn(url){
					this.$router.push(url)
			},
			deUrl(){
				return this.getUrlkey(window.location.href).keyword
			},
			handleCurrentChange(cur){
				this.current = cur;
				this.getData();
			},
			getUrlkey(url) {
					var params = {};
					var urls = url.split("?");
					if(urls[1]){
							var arr = urls[1].split("&");   
							for (var i = 0, l = arr.length; i < l; i++) {
									var a = arr[i].split("=");               
									params[a[0]] = a[1];                     
								}               
					}                                                    
					return params;
			},
      getDescData(){
        this.desc = !this.desc;
                    this.active1 = false;
        this.active2 = true;
        this.getData()
      },
			getData(){

							const hash = window.location.hash.split('=')[1] ? window.location.hash.split('=')[1] : '';
							const hash2 = window.location.hash.split('==')[1] ? window.location.hash.split('==')[1] : '';
              let that = this;
							http.get(api.productList,{
									keyword:hash2 ? '' : decodeURI(hash),
									orderBy: that.desc ? 'price desc' : 'price asc',
									categoryId: decodeURI(hash2),
									pageSize: 20,
									pageNum: this.current,
							},1).then((res)=>{
								this.goods = res.data.list;
								this.total = res.data.total;
							})
			},
			getData2(){
        this.active1 = true;
        this.active2 = false;
        const hash = window.location.hash.split('=')[1] ? window.location.hash.split('=')[1] : '';
        const hash2 = window.location.hash.split('==')[1] ? window.location.hash.split('==')[1] : '';
        http.get(api.productList,{
            keyword:hash2 ? '' : decodeURI(hash),
            // orderBy: this.state.desc ? 'price desc' : 'price asc',
            categoryId: decodeURI(hash2),
            pageSize: 10,
            pageNum: this.current,
        },1).then((res)=>{
            if(res.status === 10000){
							this.goods = res.data.list;
								this.total = res.data.total;
            }
        })
    }
		}
	}
</script>

<style lang="less" scoped>
.content{
  background: #F8FAFC;
  padding:0 160px;
  height: calc(100vh - 220px);
  padding-top: 24px;
}
.search{
  text-align: left;
  margin-top: -24px;
  height:64px;
  padding:0 160px;
  line-height: 64px;
  font-family: PingFangSC-Regular;
font-size: 18px;
color: #343434;
font-weight: 400;
}
.btn-wrapper{
  text-align: left;
  .active{
    font-family: PingFangSC-Semibold!important;
font-size: 14px!important;
color: #343434!important;
font-weight: 600!important;
  }
  .fbtn{
          font-family: PingFangSC-Regular;
font-size: 14px;
color: #545C63;
font-weight: 400;
cursor:pointer;
  }
  .btn{
      margin-left:32px;
      font-family: PingFangSC-Regular;
font-size: 14px;
color: #545C63;
font-weight: 400;
cursor:pointer;

  }
}

  // .goods{
  //   display: flex;
  //   flex-wrap: wrap;
  //   margin-bottom: 60px;
  //   justify-content: space-around;
  //   .good-item{
  //     flex: 0 0 20%;
  //     margin: 10px;
  //     img{
  //       width: 150px;
  //       height: 150px;
  //     }
  //   }
  // }

    .goods{
    display: flex;
    flex-wrap: wrap;
    margin-bottom: 60px;
    justify-content: space-around;
    .bottom-good-item{
      flex: 0 0 23%;
      display:flex;
      cursor: pointer;
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
        padding-top: 20px;
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

</style>
