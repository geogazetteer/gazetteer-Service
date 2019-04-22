/*
*siteSearch 标准地名搜索页
*/

<template>
  <div class="wrapper">
    <!--搜索框-->
    <search-box   class="leftView"></search-box>

    <!--工具箱-->
    <tool-box class="toolBox" @openBatch="openBatch=true"></tool-box>

    <!--批量处理-->
    <div class="batchWrapper" v-if="openBatch">
      <!--关闭按钮-->
      <Icon type="md-close" class="close" @click="openBatch=false" size="32" color="#3385ff"/>
      <batch-handle></batch-handle>
    </div>
  </div>
</template>

<script>
  import searchBox from '../components/searchBox.vue'
  import toolBox from '../components/toolBox.vue'
  import batchHandle from './batchHandle.vue'
export default {
  name: 'home',
  components: {
    searchBox,toolBox,batchHandle
  },
  created(){

  },
  data () {
    return {
      openBatch:false
    }
  },
  methods:{

  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.wrapper{
  /*width: 100%;*/
  /*height: 100%;*/
  /*overflow-y: hidden;*/
}
.leftView {
  position: absolute;
  left: 20px;
  top: 0;
  z-index: 100;
  /*height: 100%;*/
}
.toolBox{
  position: absolute;
  right:80px;
  top: 20px;
  z-index: 100;
  height: 48px;
  display: flex;
  align-items: center;
}

  .batchWrapper{
    position: absolute;
    top: 5%;
    left: 5%;
    width: 90%;
    height: 90%;
    z-index: 100;
  }
  .batchWrapper .close{
    position: absolute;
    right: 0;
    top: 0;
    z-index: 1;
    cursor: pointer;
  }
</style>
