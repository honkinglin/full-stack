<template>
  <div>
    <div class="header-wrapper">

      <div class="header">
        <div class="left">
          <span>企业服务</span>
          <span>关于我们</span>
          <span>联系我们</span>
          <span>合作招商</span>
          <span>帮助中心</span>
          <span>意见反馈</span>
          <span>友情链接</span>
        </div>
        <div class="right">
          <div class="item">
            <img width="20" height="20" class="item-logo" src="../assets/wx.png" alt="">
            <span>官方公众号</span>
          </div>
          <div class="item">
            <img width="20" height="20" class="item-logo" src="../assets/weibo.png" alt="">
            <span>官方微博</span>
          </div>
          <div class="item">
            <img width="20" height="20" class="item-logo" src="../assets/kongjian.png" alt="">
            <span>官方空间</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  created() {},
};
</script>

<style lang="less" scoped>
.header-wrapper{
  width: 100%;
  height:90px;
  position: fixed;
  bottom: 0;
  background: #fff;
}
.header{
  color: #999;
  height: 90px;
  font-size: 12px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 1152px!important;
  margin: 0 auto;
  overflow-x: hidden;
  .left{
    flex: 0 0 50%;
    display: flex;
    justify-content: space-between;
    align-items: center;
    width: 56px;
    height: 28px;
    font-family: PingFangSC-Light;
    font-size: 14px;
    color: #545C63;
    text-align: center;
    line-height: 28px;
    font-weight: 200;
  }
  .right{
    // flex: 0 0 50%;
    display: flex;
    .item{
      display: flex;
      flex-direction: column;
      justify-content: center;
      align-items: center;
      margin-left: 20px;
      span{
        margin-top: 16px;
      }
    }
  // justify-content: space-between;
  // align-items: center;
  }
}
</style>

