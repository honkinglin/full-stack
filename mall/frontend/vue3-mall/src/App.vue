<template>
  <div id="app">
    <router-view class="router-view" v-slot="{ Component }">
        <component :is="Component" />
    </router-view>
  </div>
</template>


<style lang="scss">
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
}
  .el-button–primary {
            background: #F34D00!important;
      border-radius: 24px;
}
.router-view{
  background: #F8FAFC;
  overflow-x: hidden;
  min-height: calc(100vh - 91px);
}

</style>
