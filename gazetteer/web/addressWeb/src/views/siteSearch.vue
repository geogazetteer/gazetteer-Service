/*
*siteSearch 标准地名搜索页
*/

<template>
  <div class="wrapper">
   <base-map :markerCoord="markerCoord"></base-map>
    <!--搜索框-->
    <search-box @setMarkCoord="onGetMarkCoord"  class="leftView"></search-box>

    <!--工具箱-->
    <tool-box class="toolBox"></tool-box>
  </div>
</template>

<script>
  import baseMap from '../components/baseMap.vue'
  import searchBox from '../components/searchBox.vue'
  import toolBox from '../components/toolBox.vue'
export default {
  name: 'home',
  components: {
    baseMap,searchBox,toolBox
  },
  created(){
  },
  data () {
    return {
      markerCoord:[],
    }
  },
  methods:{
    onGetMarkCoord(coordArr){
      this.markerCoord = coordArr
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.wrapper{
  width: 100%;
  height: 100%;
  overflow-y: hidden;
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
  right:100px;
  top: 20px;
  z-index: 10001;
  height: 48px;
  display: flex;
  align-items: center;
}

</style>
