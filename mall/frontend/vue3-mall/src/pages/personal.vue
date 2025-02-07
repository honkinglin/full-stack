<template>
  <div>
    <HeaderCom></HeaderCom>
<el-card class="box-card">
  <template #header>
    <div class="card-header">
      <span>个人中心</span>
      <el-button class="button" type="text" @click="editFn">更改个性签名</el-button>
    </div>
  </template>
  <div class="item">
    <div class="item-name">昵称：</div>
    <div class="item-value">{{ name }}</div>
  </div>
    <div class="item">
    <div class="item-name">个性签名：</div>
    <div class="item-value">{{ sign }}</div>
  </div>
    <div class="item">
    <div class="item-name">角色：</div>
    <div class="item-value">{{
              role === "1" ? "普通用户" : "管理员"
            }}</div>
  </div>
</el-card>

    <Footer></Footer>
                  <el-dialog
                title="更改个人信息"
                v-model="visible"
                width="30%"
              >
                <div
                  className="items"
                  :style="{ display: 'flex', width: '100%',alignItems:'center' }"
                >
                  <span :style="{ width: '100px', fontSize: '16px',whiteSpace:'nowrap' }"
                    >个人签名：</span
                  >
                  <el-input
                    v-model="sign"
                    placeholder="请输入个性签名"
                  ></el-input>
                </div>
                <div class="dialog-footer">
                  <el-button @click="visible = false">取 消</el-button>
                  <el-button
                    type="primary"
                    @click="handleOk
                    "
                    >确 定</el-button
                  >
                </div>
              </el-dialog>
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
                name: localStorage.getItem('username'),
                role: localStorage.getItem('role'),
                sign:'' || localStorage.getItem('personalizedSignature'),
                visible: false,
            }
        },
    	components: {
    		HeaderCom,
            Footer,
    	},
        created(){
        },
        mounted(){
                // this.getData();
        },
        methods: {
            handleOk(){
                                    http
                        .post(api.editPersonal, {
                          signature: this.sign,
                        })
                        .then((e) => {
                          if (e.status === 10000) {
                            // this.sign = this.signature;
                            localStorage.setItem(
                              'personalizedSignature',
                              this.sign
                            );
                            this.$message({
                              type: 'success',
                              message: '修改成功',
                            });
                          }
                        })
                        .finally(() => {
                          this.visible = false;
                        })
    },
    editFn(){
      this.visible = true;
    }
        },
    }
</script>

<style lang="less">
.edit-class{
  cursor: pointer;
}
.dialog-footer{
  margin-top: 20px;
}
.card-header{
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.box-card{
  width: 992px;
background: #FFFFFF;
box-shadow: 0px 4px 8px 0px rgba(0,0,0,0.1);
border-radius: 8px;
  margin:auto;
  .item{
    display: flex;
    margin-top: 46px;
    .item-name{
      flex: none;
      width: 100px;
      text-align: right;
    }
  }
}
</style>
