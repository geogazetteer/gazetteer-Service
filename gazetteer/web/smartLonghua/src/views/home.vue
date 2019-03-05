<template>
  <div class="wrapper">
   <base-map :markerCoord="markerCoord"></base-map>
    <!--搜索框-->
    <search-box @setMarkCoord="onGetMarkCoord"></search-box>

    <!--右侧工具栏-->
    <right-tool></right-tool>
  </div>
</template>

<script>
  import baseMap from '../components/baseMap.vue'
  import searchBox from '../components/searchBox.vue'
  import rightTool from '../components/rightToolBar.vue'
export default {
  name: 'home',
  components: {
    baseMap,searchBox,rightTool
  },
  created(){
    //获取接口数据get方法的demo
    var url='https://www.seniorland.cn/v1/pb/vusers/10177'
    this.$api.getSrarchList(url).then(function(res){
      debugger
    })
  },
  data () {
    return {
      markerCoord:[]
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
}
</style>
