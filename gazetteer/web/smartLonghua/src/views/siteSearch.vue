/*
*siteSearch 标准地名搜索页
*/

<template>
  <div class="wrapper">
   <base-map :markerCoord="markerCoord"></base-map>
    <!--搜索框-->
    <search-box @setMarkCoord="onGetMarkCoord"  class="leftView"></search-box>

    <!--右侧工具栏-->
    <right-view   class="rightView"></right-view>

    <!--编辑跳转按钮-->
    <div class="toEdit" @click="toEdit">
      <img src="../../static/images/edit_1.png">
    </div>
  </div>
</template>

<script>
  import baseMap from '../components/baseMap.vue'
  import searchBox from '../components/searchBox.vue'
  import rightView from '../components/userCenter.vue'
export default {
  name: 'home',
  components: {
    baseMap,searchBox,rightView
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
    },
    //跳转到编辑页
    toEdit(){
      this.$router.push("/siteEdit");
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
  top: 20px;
  z-index: 100;
  /*height: 100%;*/
}
.rightView {
  z-index: 100;
  position: absolute;
  top: 20px;
  right: 20px;
}
  .toEdit{
    position: absolute;
    bottom: 20px;
    right: 20px;
    width: 50px;
    height: 50px;
    cursor: pointer;
    z-index: 100;
    background: #fff;
    border-radius: 50%;
    border:3px solid #d8d8d8;
  }
  .toEdit img{
    width: 100%;
    border-radius: 50%;
  }
</style>
