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

</style>
