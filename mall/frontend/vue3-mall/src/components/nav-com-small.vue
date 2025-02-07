<template>
  <div :style="{ backgroundColor: '#ffffff',marginBottom:'20px',marginTop:'20px',justifyContent:'space-between', display:'flex' }">
    <img @click="locationFn2('/index')" class="logo" src="../imgs/img2.png" alt="">
  </div>
</template>

<script>

export default {
  data() {
    return {
      name: "",
    };
  },
  created() {},
  mounted() {},
  methods: {
    locationFn2(url) {
      this.$router.push(url);
    },
    locationFn() {
      this.$router.push("/search?keyword=" + this.name);
    },
  },
};
</script>

<style lang="less" scoped>
.search-wrapper{
  display: flex;
  align-items: center;
  justify-content: center;
  .el-icon-search{
    font-size: 20px;
    cursor: pointer;
    margin-left: 4px;
  }
}
.input-search{
  width: 300px;
}
.logo{
  max-width: 200px;
  cursor: pointer;
}
</style>
