<template>
    <div class="fillcontain">
        <div class="btn-wrapper">
            <div class="page-title">分类管理</div>
                    <div class="btn-top">
            <el-button @click="handleAdd" type="primary">新增分类</el-button>
        </div>
        </div>
        <div class="table-container">
				<el-table
                border
                        :data="tableData"
                        :row-key="row => row.index"
							style="width: 100%">
							<el-table-column
								label="ID"
								prop="id">
							</el-table-column>
							<el-table-column
								label="名称"
								prop="name">
							</el-table-column>
							<el-table-column
								label="排序"
								prop="orderNum">
							</el-table-column>
									<el-table-column
								label="创建时间"
								prop="orderNum">
								<template v-slot="scope">
									<div>{{timeFormat(scope.row.createTime)}}</div>
									</template>
							</el-table-column>
							<el-table-column
								label="操作"
								prop="status">
							<template v-slot="scope">
                                            <el-popconfirm
            title="请确定是否删除"
            @confirm="handleDelete(scope.row)"
            >
            <template #reference>
									<el-button  type="danger">删除</el-button>

            </template>
                </el-popconfirm>
									<el-button   @click="handleEdit(scope.row)" type="success">修改</el-button>

							</template>
							</el-table-column>
				</el-table>
            <div class="pagination-wrapper" style="text-align: left;">
                <el-pagination
                  @current-change="handleCurrentChange"
                  :current-page="currentPage"
                  :page-size="10"
                  layout="total, prev, pager, next"
                  :total="count">
                </el-pagination>
            </div>
        </div>
        <el-dialog
            :title="isEdit ? '编辑分类' : '新增分类'"
            v-model="dialogVisible"
            width="30%"
            >
            <el-form ref="form" :model="form" label-width="90px">
                <el-form-item label="新增分类">
                    <el-input v-model="form.name"></el-input>
                </el-form-item>
                <el-form-item label="排序值">
                    <el-input v-model="form.orderNum"></el-input>
                </el-form-item>
                <el-form-item label="目录级别">
                    <el-input v-model="form.type"></el-input>
                </el-form-item>
                <el-form-item label="上级目录ID">
                    <el-input v-model="form.parentId"></el-input>
                </el-form-item>
                </el-form>
                 <template #footer>
            <div class="dialog-footer">
                <el-button @click="dialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="update">确 定</el-button>
            </div>
                 </template>
        </el-dialog>
    </div>
</template>

<script>
/* eslint-disable */
    import Header from '../components/header.vue'
    import http from './http';
    import http2 from './http2';
    import api from './_api'
    import moment from 'moment'
    export default {
        data(){
            return {
                dialogVisible:false,
                tableData: [],
                isEdit: false,
                count: 0,
                currentPage: 1,
                form:{
                                    name:null,
                                    id:null,
                orderNum:null,
                type:null,
                parentId:null
                }
            }
        },
    	components: {
    		Header,
    	},
        created(){
            this.initData();
        },
        mounted(){

        },
        methods: {
            handleDelete(row){
                           http.post(api.categoryDelete,{id: row.id}).then((res)=>{
              if(res.status === 10000){
                                            this.$message({
                                            type: 'success',
                                            message: '删除成功'
                    })
               this.initData()
              }else{
                              this.$message({
                                            type:  'error',
                                            message: res.msg
                                        });
              }
            })
            },
            update(){
                let url='';
                if(this.isEdit){
                    url = api.categoryUpdate
                }else{
                    url = api.categoryAdd
                }
                http2.post(url,{
                    id:this.form.id,
                    name: this.form.name,
                    orderNum: this.form.orderNum,
                    type:this.form.type, 
                    parentId: this.form.parentId})
                .then((res)=>{
                if(res.status === 10000){
                    this.dialogVisible = false;
                                            this.$message({
                                            type: 'success',
                                            message: `${this.isEdit ? '编辑成功' : '新增成功'}`
                    })
                    this.initData();
                }else{
                         this.$message({
                                            type:  'error',
                                            message: res.msg
                                        });
                }
                })
            },
            handleAdd(){
                this.dialogVisible =true;
                this.isEdit = false;
                this.form.name = null;
                this.form.orderNum = null;
                this.form.type = null;
                this.form.id = null;
                this.form.parentId = null;
            },
            handleEdit(row){
                console.log('row:',row);
                this.dialogVisible = true;
                this.isEdit = true;
                this.form.name = row.name;
                this.form.orderNum = row.orderNum;
                this.form.type = row.type;
                this.form.id = row.id;
                this.form.parentId = row.parentId;
            },
            timeFormat(t){
                return moment(t).format('YYYY-MM-DD HH:mm:ss')
            },

            async initData(){
                      http.get(api.categoryList,{
                          pageNum: this.currentPage,
                          pageSize: 10,
                      }).then((res)=>{
                    this.tableData = res.data.list;
                    this.count = res.data.total;
      })

            },
            handleCurrentChange(val) {
                this.currentPage =val;
                this.initData();
            },
            onSubmit(){

            },
    
        },
    }
</script>

<style lang="less" scoped>
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
