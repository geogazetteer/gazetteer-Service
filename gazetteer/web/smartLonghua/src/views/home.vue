<template>
  <div class="wrapper">
   <base-map :markerCoord="markerCoord"></base-map>
    <!--搜索框-->
    <search-box @setMarkCoord="onGetMarkCoord" @onSendEdit="onGetEdit"></search-box>

    <!--右侧工具栏-->
    <right-tool  :showEdit=showEdit @onEditHasOpen="showEdit=false"></right-tool>
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
   /* var url='';
    this.$api.getSearchList(url).then(function(res){
    })*/
  },
  data () {
    return {
      markerCoord:[],
      showEdit:false
    }
  },
  methods:{
    onGetMarkCoord(coordArr){
      this.markerCoord = coordArr
    },
    //打开编辑框
    onGetEdit(){
      this.showEdit = true;
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
