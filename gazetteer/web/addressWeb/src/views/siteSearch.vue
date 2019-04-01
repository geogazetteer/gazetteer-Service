/*
*siteSearch 标准地名搜索页
*/

<template>
  <div class="wrapper">
   <base-map :markerCoord="markerCoord"></base-map>
    <!--搜索框-->
    <search-box @setMarkCoord="onGetMarkCoord"  class="leftView"></search-box>
  </div>
</template>

<script>
  import baseMap from '../components/baseMap.vue'
  import searchBox from '../components/searchBox.vue'
export default {
  name: 'home',
  components: {
    baseMap,searchBox
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

</style>
