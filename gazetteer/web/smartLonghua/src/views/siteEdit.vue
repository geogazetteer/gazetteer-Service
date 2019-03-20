/*
*siteSearch 标准地名搜索页
*/

<template>
  <div class="wrapper">
   <base-map :markerCoord="markerCoord"></base-map>
    <!--编辑框-->
    <edit-box @setMarkCoord="onGetMarkCoord"  class="leftView"></edit-box>

    <!--个人中心-->
    <right-view   class="rightView"></right-view>

    <!--返回搜索按钮-->
    <div class="toSearch" @click="toSearch">
      <img src="../../static/images/search_1.png">
    </div>
  </div>
</template>

<script>
  import baseMap from '../components/baseMap.vue'
  import editBox from '../components/editBox.vue'
  import rightView from '../components/userCenter.vue'
export default {
  name: 'home',
  components: {
    baseMap,editBox,rightView
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
    toSearch(){
      this.$router.push("/siteSearch");//跳转地名搜索
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
  z-index: 100;
  height: 100%;
}
.rightView {
  z-index: 100;
  position: absolute;
  top: 20px;
  right: 20px;
}
  .toSearch{
    position: absolute;
    bottom: 20px;
    right: 20px;
    width: 50px;
    height: 50px;
    z-index: 100;
    cursor: pointer;
    background: #fff;
    border-radius: 50%;
    border:3px solid #d8d8d8;
  }
  .toSearch img{
    width: 100%;
    border-radius: 50%;
  }
</style>
