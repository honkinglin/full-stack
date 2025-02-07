<template>
    <div class="fillcontain">
        <div class="btn-wrapper">
            <div class="page-title">订单管理</div>
            <el-button type="primary" @click="staticFn">订单统计</el-button>
        </div>
        <div class="table-container">
		<el-table
            :data="tableData"
            :row-key="row => row.orderNo"
            style="width: 100%">
            <el-table-column type="expand">
                <template #default="props">
                    <el-table
                            :data="props.row.orderItemVOList"
                            style="width: 100%">
                            <el-table-column
								label="商品名称"
								prop="productName">
							</el-table-column>
                              <el-table-column
								label="商品图片"
								prop="productName">
                                <template #default="scope">
									<img :src="scope.row.productImg" width="50" alt="">
                                </template>
							</el-table-column>
                            <el-table-column
								label="商品单价(元)"
								prop="unitPrice">
                                <template #default="scope">
									<div>{{scope.row.unitPrice ? scope.row.unitPrice/100:null }}</div>

                                </template>
							</el-table-column>
                            <el-table-column
								label="商品数量"
								prop="quantity">
							</el-table-column>
                            <el-table-column
								label="商品总价(元)"
								prop="totalPrice">
                                <template #default="scope">
									<div>{{scope.row.totalPrice ? scope.row.totalPrice/100:null }}</div>

                                </template>
							</el-table-column>
                     </el-table>
                </template>
            </el-table-column>
							<el-table-column
								label="订单号"
								prop="orderNo">
							</el-table-column>
							<el-table-column
								label="订单总价(元)"
								prop="totalPrice">
                                 <template #default="scope">
									<div>{{scope.row.totalPrice ? scope.row.totalPrice/100:null }}</div>

                                </template>
							</el-table-column>
							<el-table-column
                                label="订单状态"
                                prop="orderStatus">
                                <template #default="scope">
                            <div v-if="scope.row.orderStatus === 0">用户已取消</div>
                                        <div v-if="scope.row.orderStatus === 10">未付款</div>
                                        <div v-if="scope.row.orderStatus === 20">已付款</div>
                                        <div v-if="scope.row.orderStatus === 30">已发货</div>
                                        <div v-if="scope.row.orderStatus === 40">交易完成</div>
                                </template>                             
							</el-table-column>
							<el-table-column
								label="创建时间"
								prop="createTime">
                                <template #default="scope">
									<div>{{timeFormat(scope.row.createTime)}}</div>

                                </template>
							</el-table-column>

							<el-table-column
								label="操作"
								prop="status">
                                <template #default="scope">
                                    <div class="table-btns">
									<el-button @click="handleEdit(scope.row,false)"  type="danger">查看收件人信息</el-button>
                                    <el-popconfirm
                                    v-if="scope.row.orderStatus !== 30 && scope.row.orderStatus !== 40"
                                        title="请确认是否已发货？"
                                         confirmButtonText='是'
                                         cancelButtonText='否'
                                         @confirm="startFn(scope.row)"
                                        >
                                        <template #reference>
			                        <el-button type="success">发货</el-button>
                                        </template>
                                    </el-popconfirm>
                                      <el-popconfirm
                                      v-if="scope.row.orderStatus !== 40"
                                       confirmButtonText='是'
                                         cancelButtonText='否'
                                         @confirm="finishFn(scope.row)"
                                        title="请确认订单是否已完结？"
                                        >
                                        <template #reference>
			                        <el-button type="success">订单完结</el-button>
                                        </template>
                                    </el-popconfirm>
                                    </div>
                                </template>
							</el-table-column>
				</el-table>
				<div class="pagination-wrapper" style="text-align: left;">
						<el-pagination
							@size-change="handleSizeChange"
							@current-change="handleCurrentChange"
							:current-page="currentPage"
							:page-size="10"
							layout="total, prev, pager, next"
							:total="count">
						</el-pagination>
				</div>
        </div>
        <el-dialog
            title="详细信息"
            v-model="dialogVisible"
            width="30%"
            >

            <el-form ref="form" :model="form" label-width="80px">
                <el-form-item label="收件人">
                    <el-input disabled v-model="form.receiverName"></el-input>
                </el-form-item>
                <el-form-item label="电话">
                    <el-input disabled v-model="form.receiverMobile"></el-input>
                </el-form-item>
                <el-form-item label="收件地址">
                    <el-input disabled v-model="form.receiverAddress"></el-input>
                </el-form-item>
            </el-form>
                 <template #footer>
   <div class="dialog-footer">
                <!-- <el-button @click="dialogVisible = false">取 消</el-button> -->
                <!-- <el-button type="primary" @click="dialogVisible = false">确 定</el-button> -->
            </div>
                 </template>

         
        </el-dialog>
        <el-dialog
        title="统计"
        v-model="staticTrue"
        width="50%">
        <div id="chart-dom"></div>
        <template #footer>
            <span class="dialog-footer">
            <!-- <el-button @click="staticTrue = false">取 消</el-button> -->
            </span>
        </template>
        </el-dialog>
    </div>
</template>

<script>
/* eslint-disable */
    import header from '../components/header.vue'
    import http from './http'
    import api from './_api'
    // import * as echarts from 'echarts';
    import moment from 'moment'
    import * as echarts from 'echarts/core';
    import {
        TitleComponent,
        ToolboxComponent,
        TooltipComponent,
        GridComponent,
        LegendComponent
    } from 'echarts/components';
    import {
        BarChart,
        LineChart
    } from 'echarts/charts';
    import {
        CanvasRenderer
    } from 'echarts/renderers';

    echarts.use(
        [ToolboxComponent, TitleComponent, TooltipComponent, GridComponent, LegendComponent, BarChart, LineChart, CanvasRenderer]
    );

    export default {
        data(){
            return {
                dialogVisible:false,
                tableData: [],
                staticTrue: false,
                orderData:[],
                count: 0,
                currentPage: 1,
                isOrder:false,
                form: {
                    orderNo:null,
                    productName:null,
                    unitPrice:null,
                    quantity:null,
                    totalPrice:null,
                    receiverName:null,
                    receiverMobile:null,
                    receiverAddress:null,
                }
            }
        },
    	components: {
    		header,
    	},
        created(){
        },
        mounted(){
            this.initData();
        },
        methods: {
            handleEdit(row, flag){
                console.log('row:',row);
                this.isOrder = flag;
                this.orderData = row.orderItemVOList;
                this.dialogVisible = true
                  this.form.orderNo=row.orderNo
                    this.form.productName=row.productName
                   this.form.unitPrice=(row.unitPrice/100).toFixed(2)
                    this.form.quantity=row.quantity
                    this.form.totalPrice=(row.totalPrice/100).toFixed(2)
                    this.form.receiverName=row.receiverName
                    this.form.receiverMobile=row.receiverMobile
                    this.form.receiverAddress=row.receiverAddress;
            },
            timeFormat(t){
                return moment(t).format('YYYY-MM-DD HH:mm:ss')
            },

            async initData(){
                http.get(api.orderList,{
                    pageNum: this.currentPage,
                    pageSize: 10,
                }).then((res)=>{
                    console.log('res:',res);
                      this.tableData = res.data.list;
                    this.count = res.data.total;               
                })
                },

            handleSizeChange(val) {
                console.log(`每页 ${val} 条`);
            },
            handleCurrentChange(val) {
                         this.currentPage =val;
                this.initData();
            },
            startFn(data){
                        http.post(api.orderDerive,{orderNo:data.orderNo}).then((res)=>{
                                if(res.status === 10000){
                                  this.$message({
                                type: 'success',
                                message: '发货成功!'
                            });
                                this.initData();
                                }else{
                                     this.$message({
                                type: 'error',
                                message: res.msg
                            });
                                }
                            })
            },
            finishFn(data){
                        http.post(api.orderFinish,{orderNo:data.orderNo}).then((res)=>{
                                if(res.status === 10000){
                                  this.$message({
                                type: 'success',
                                message: '订单成功完结!'
                            });
                                this.initData();
                                }else{
                                     this.$message({
                                type: 'error',
                                message: res.msg
                            });
                                }
                            })
            },
            onSubmit(){

            },
            getstcData(){
                http.get(api.adminStatics,{
                    startDate: '2019-02-01',
                    endDate: '2030-10-01',
                }).then((res)=>{
                    let x_arr = [],y_arr=[];
                    res.data && res.data.map((item)=>{
                        x_arr.push(item.days.substring(0,11)),
                        y_arr.push(item.amount)
                    })
                    var myChart = echarts.init(document.getElementById('chart-dom'));
                    // 绘制图表
                    myChart.setOption({
                        title: {
                            text: '订单趋势图'
                        },
                        tooltip: {},
                        xAxis: {
                            axisLabel: {
                                inside: true,
                                textStyle: {
                                    color: '#fff'
                                }
                            },
                            axisTick: {
                                show: false
                            },
                            axisLine: {
                                show: false
                            },
                            data: x_arr
                        },
                        yAxis: {
                            axisLine: {
                                show: false
                            },
                            axisTick: {
                                show: false
                            },
                            axisLabel: {
                                textStyle: {
                                    color: '#999'
                                }
                            }
                        },
                        series: [{
                            name: '日期-订单量',
                            type: 'bar',
                            itemStyle: {
                                color: new echarts.graphic.LinearGradient(
                                    0,0,0,1,
                                    [
                                        //渐变色
                                        {offset: 0, color: '#83bff6'},
                                        {offset: 0.5, color: '#188df0'},
                                        {offset: 1, color: '#7bbfea'}
                                    ]
                                )
                            },
                            label: {
                                show: true,
                                position: 'inside'
                            },
                            data: y_arr
                        },
                        {
                            name: '日期-订单量',
                            type: 'line',
                            data: y_arr
                        }
                        ]
                    });                
                })
            },
            staticFn(){
                this.staticTrue=true
                this.getstcData();
                },
        },
    }
</script>

<style lang="less" scoped>
    #chart-dom{
        height: 400px;
    }
    .table_container{
        // padding: 20px;
    }
        .el-pagination{
        text-align: right;
    }
      .btn-wrapper{
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 21px 28px;
height: 64px;
background: #FFFFFF;
      .page-name{
          margin-left: 20px;
          margin-top: 20px;
      }
  }
  .cell{
      button{
          margin: 0!important;
      }
  }
  .table-btns{
      display: flex;
      flex-direction: column;
      button{
          margin-top: 10px!important;
      }
  }
      .pagination-wrapper{
      background: #fff;
      height:60px;
      display:flex;
      align-items:center;
      justify-content: center;
  }
        .page-title{
          margin-left: 20px;
          font-family: PingFangSC-Semibold;
font-size: 16px;
color: #545C63;
font-weight: 600;
      }
      .table-container{
          margin-top:10px;
      }
</style>
