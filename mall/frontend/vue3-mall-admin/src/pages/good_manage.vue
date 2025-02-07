<template>
    <div class="fillcontain">
        <div class="btn-wrapper">
            <div class="page-title">商品管理</div>
        <div class="btn-top-wrapper">

                <el-upload
                    class="avatar-uploader"
                    action="http://127.0.0.1:8083/admin/upload/product"
                    :show-file-list="false"
                    :on-success="uploadImg"
                    :before-upload="beforeImgUpload">
                    <el-button type="success" >批量上传商品</el-button>
                </el-upload>

            <el-popconfirm
            title="请确定是否上架所选商品"
             v-if="selectedIds && selectedIds.length"
            @confirm="upOffGood(1)"
            >
                <template #reference>
                <el-button type="success" >上架商品</el-button>

                </template>
            </el-popconfirm>
            <el-popconfirm
                title="请确定是否下架所选商品"
                 v-if="selectedIds && selectedIds.length"
            @confirm="upOffGood(2)"
                >
            <template #reference>
            <el-button  type="danger">下架商品</el-button>

            </template>

            </el-popconfirm>
            <el-button @click="handleAdd" type="primary">新增商品</el-button>
        </div>
        </div>

        <div class="table-container">
            <el-table
                :fit="true"
                ref="multipleTable"
			    :data="tableData"
                :row-key="row => row.index"
                @selection-change="handleSelectionChange"
			    style="width: 100%">
                <el-table-column
                    type="selection"
                    width="55">
                </el-table-column>
			    <el-table-column
			      label="ID"
                  width="50"
			      prop="id">
			    </el-table-column>
                                <el-table-column
			      label="图片"
                  width="170"
			      prop="image">
                    <template #default="scope">
                       <img :style="{maxWidth:'150px',maxHeight:'150px', borderRadius:'4px'}" :src="scope.row.image" alt="">
                    </template>
			    </el-table-column>
			    <el-table-column
			      label="名称"
                  width="150"
			      prop="name">
			    </el-table-column>
			    <el-table-column
			      label="详情"
                  width="170"
			      prop="detail">
			    </el-table-column>
			    <el-table-column
			      label="单价(元)"
                  sortable
                  :default-sort = "{prop: 'price', order: 'descending'}"
			      prop="price">
                  <template #default="scope">
                       <div>{{scope.row.price ? scope.row.price/100 : null}}</div>
                    </template>
			    </el-table-column>
                <el-table-column
			      label="是否上架"
			      prop="status">
                    <template #default="scope">
                        <div>{{scope.row.status ===1 ? '已上架' : '已下架'}}</div>
                    </template>
			    </el-table-column>
                <el-table-column
			      label="库存"
			      prop="stock">
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
   <el-button @click="handleEdit(scope.row)" type="success">修改</el-button>
                        <el-popconfirm
                            title="请确定是否删除"
                            @confirm="handleDelete(scope.row)"
                            >
                            <template #reference>
                            <el-button class="btn-delete"   type="danger" >删除</el-button>

                            </template>
                        </el-popconfirm>
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
            :title="isEdit ? '编辑商品' : '新增商品'"
            v-model="dialogVisible"
            width="30%"
            >
            <el-form ref="form" :model="form" label-width="80px">
                <el-form-item label="商品分类">
                      <el-cascader
                        v-model="form.value"
                        :options="tree"
                        :show-all-levels="false"
                        :props="{ expandTrigger: 'hover' }"
                        @change="handleChange"></el-cascader>
                </el-form-item>
                <el-form-item label="商品名称">
                    <el-input v-model="form.name"></el-input>
                </el-form-item>
                                <el-form-item label="商品简介">
                    <el-input v-model="form.detail"></el-input>
                </el-form-item>
                                                <el-form-item label="商品库存">
                    <el-input v-model="form.stock"></el-input>
                </el-form-item>
                <el-form-item label="商品售价">
                    <el-input placeholder="请填写整数，单位为分" v-model="form.price"></el-input>
                </el-form-item>

                <el-form-item label="商品图片">
                    <el-upload
                    :headers="getHeader"
                    class="avatar-uploader avatar-uploader-dialog"
                    action="http://127.0.0.1:8083/admin/upload/image"
                    :show-file-list="false"
                    :on-success="handleAvatarSuccess"
                    :before-upload="beforeAvatarUpload">
                    <img v-if="form.imageUrl" :src="form.imageUrl" class="avatar-upload">
                    <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                    </el-upload>
                </el-form-item>
                </el-form>
                 <template #footer>
            <div  class="dialog-footer">
                <el-button @click="dialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="update">确 定</el-button>
            </div>
                 </template>

        </el-dialog>
    </div>
</template>

<script>
/* eslint-disable */
    import header from '../components/header.vue'
    import http from './http';
import http2 from './http2';
    import api from './_api'
    import moment from 'moment'
    export default {
        data(){
            return {
                dialogVisible:false,
                tableData: [],
                count: 0,
                currentPage: 1,
                selectedIds:[],
                isEdit: false,
                tree:[],
                form: {
                    name: '',
                    value: [],
                    imageUrl:null,
                    detail:null,
                    stock:null,
                    id:null,
                    price:null
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
            this.getCate();
        },
        methods: {
            getHeader(){
                return {
                    'jwt_token': sessionStorage.getItem('token') || null
                }
            },
            handleDelete(row){

                             http2.post(api.goodDelete +'?id=' + row.id,{
                                                            id:row.id,
                                                          }).then((res)=>{
                                                            if(res.status ===10000){
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
            handleChange(v){
                console.log('vvv:',v);
            },
            getCate(){
              http.get(api.categoryList2).then((res)=>{
        if(res.status === 10000){
            this.tree = res.data
            this.tree.map((item)=>{
                item.value = item.id;
                item.label = item.name;
                if(item.childCategory.length){
                    item.children = item.childCategory;
                    item.childCategory.map((_item)=>{
                        _item.value = _item.id;
                        _item.label = _item.name;
                        if(_item.childCategory.length){
                    _item.children = _item.childCategory;
                            _item.childCategory.map((sub_item)=>{
                                sub_item.value = sub_item.id;
                        sub_item.label = sub_item.name;
                            })
                        }
                    })
                }
            })
        }else{
          this.$message({
            type:  'error',
            message: res.msg
        });
        }
      })
            },
        handleAvatarSuccess(res, file) {
            if(res.status ===10000){
                this.form.imageUrl = res.data;
                this.$message({
                    type: 'success',
                    message: '上传成功'
                })
                }else{
                    this.$message({
                        type:  'error',
                        message: res.msg
                    });
                }
        },
        beforeAvatarUpload(file) {
            const isJPG = file.type === 'image/jpeg';
            const isLt2M = file.size / 1024 / 1024 < 2;

            if (!isJPG) {
                this.$message.error('上传图片只能是 JPG 格式!');
                return false
            }
            if (!isLt2M) {
                this.$message.error('上传图片大小不能超过 2MB!');
                return false;
            }
            return isJPG && isLt2M;
        },
         /**
         * Created by kun on 2020/7/17 https://www.jianshu.com/p/ddf8db73385f
         * @param {object} cascader - element ui cascader options
         * @param {number} id - last child node value
         * @returns {Array} - full value for cascader show
         * 
         */
        getCascaderFullValue(cascader, id) {
            let cascaderFullValue = [id];
            _getParentId(cascaderFullValue, cascader,cascader, id);

            return cascaderFullValue;
            /**
             * 
             * @param {Array} cascaderFullValue value for return
             * @param {Array} cascader value for loop
             * @param {Array} _cascader value of original
             * @param {number} _id  value of bottom item
             */
            function _getParentId(cascaderFullValue, cascader, _cascader, _id) {
                if (!Array.isArray(cascader)) return;
                
                for (let cascaderItem of cascader) {
                if (!Array.isArray(cascaderItem.children)) continue;

                let index = cascaderItem.children.findIndex(item => item.value === _id);
                if (index >= 0) {
                    cascaderFullValue.unshift(cascaderItem.value);

                    if(_cascader.findIndex(item => item.value === cascaderItem.value) < 0) //is original item
                    _getParentId(cascaderFullValue, _cascader, _cascader,cascaderItem.value);
                } else {
                    _getParentId(cascaderFullValue, cascaderItem.children, _cascader,_id);
                }
                }
            }
            },

            handleAdd(){
                this.dialogVisible = true;
                this.isEdit = false
                this.form.name = null;
                this.form.detail = null;
                this.form.stock = null;
                this.form.price = null;
                this.form.imageUrl = null;
                this.form.id = null;
                this.form.value = null;
            },
            uploadImg(res){
                if(res.status ===10000){
                    this.$message({
                        type: 'success',
                        message: '上传成功'
                    })
                    }else{
                        this.$message({
                            type:  'error',
                            message: res.msg
                        });
                    }
            },
            update(){
                console.log('form:',this.form);
                http2.post(`${this.isEdit ? api.goodUpdate : api.goodAdd}`,{
                    categoryId:this.form.value[this.form.value.length-1],
                    name:this.form.name,
                    detail:this.form.detail,
                    stock:this.form.stock,
                    price:this.form.price,
                    id: this.form.id,
                    image: this.form.imageUrl  ? this.form.imageUrl : null
                    }).then((res)=>{
                    if(res.status ===10000){
                        this.dialogVisible = false;
                        this.initData();
                        this.$message({
                                            type: 'success',
                                            message: `${this.isEdit ? '编辑成功' : '新增成功'}`
                    })
                    }else{
       this.$message({
                                            type:  'error',
                                            message: res.msg
                                        });
                    }
                    })
            },
            handleEdit(row){
                console.log('row:',row)
                this.isEdit=true;
                this.form.name = row.name;
                this.form.id = row.id;
                this.form.value = this.getCascaderFullValue(this.tree,row.categoryId)
                console.log('value:',this.form.value)
                this.form.detail = row.detail;
                this.form.stock = row.stock;
                this.form.price = row.price;
                this.form.imageUrl = row.image;
                this.dialogVisible = true;
            },
            beforeImgUpload(info){
                return true
            },
            handleDelete(row){
                                         http2.post(api.goodDelete +'?id=' + row.id,{
                                                            id:row.id,
                                                          }).then((res)=>{
                                                            if(res.status ===10000){
                                                              this.initData()
                                            this.$message({
                                                type: 'success',
                                                message: '删除成功!'
                                            });
                                        }else{
                                            this.$message({
                                                type: 'error',
                                                message: res.msg
                                            });
                                            }
                                        })
            },
            timeFormat(t){
                return moment(t).format('YYYY-MM-DD HH:mm:ss')
            },
            handleSelectionChange(val) {
                console.log('value:::',val)
                this.selectedIds = val;
            },
            async initData(){
                http.get(api.goodList,{
                    pageNum: this.currentPage,
                    pageSize: 10,
                }).then((res)=>{
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
            onSubmit(){

            },
            upOffGood(type){
                        let ids = [];
                        this.selectedIds.map((item)=>{
                            ids.push(item.id)
                        })
                        if(type === 1){
                            http.post(api.upDownGood,{ids:ids.join(','),sellStatus: 1})
                            .then((res)=>{
                                if(res.status === 10000){
                                                        this.$message({
                                            type: 'success',
                                            message: '上架成功!'
                                        });
                                    this.initData();
                                }else{
 this.$message({
                                            type:  'error',
                                            message: res.msg
                                        });
                                }
                            })
                        }else{
                             http.post(api.onOffLine,{ids:ids.join(','),sellStatus: 0}).then((res)=>{
              if(res.status === 10000){
                                  this.$message({
                        type: 'success',
                        message: '下架成功!'
                    });
              this.initData();
              }else{
                                                                                            this.$message({
                                            type:  'error',
                                            message: res.msg
                                        });
              }
            })
                        }
            },
        },
    }
</script>

<style lang="less" scoped>
    .btn-top-wrapper{
        margin-left: 20px;
        display: flex;
        align-items: center;
    }
    .avatar-uploader{
        height: 40px;
        margin-right: 20px;
    }
    .el-pagination{
        text-align: right;
    }
    .avatar-uploader-dialog{
        height: auto;
    }
    .avatar-uploader .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
  }
  .avatar-uploader .el-upload:hover {
    border-color: #409EFF;
  }
  .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 178px;
    height: 178px;
    line-height: 178px;
    text-align: center;
    position: relative;
    &::before{
        top: 50%;
        left: 50%;
        position: absolute;
        transform: translate(-50%,-50%);
    }
  }
  .avatar-upload {
    width: 100px;
    height: 100px;
    display: block;
  }
  .btn-wrapper{
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 21px 28px;
height: 64px;
background: #FFFFFF;
      .page-title{
          margin-left: 20px;
          font-family: PingFangSC-Semibold;
font-size: 16px;
color: #545C63;
font-weight: 600;
      }
  }
  .el-cascader{
      width: 100%;
  }
  .table-container{
      margin-top: 10px;
  }
  .pagination-wrapper{
      background: #fff;
      height:60px;
      display:flex;
      align-items:center;
      justify-content: center;
  }
  .btn-delete{
      margin-left: 0px!important;
  }
</style>
