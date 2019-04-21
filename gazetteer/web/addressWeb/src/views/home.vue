/*
*
*/

<template>
  <div class="wrapper">
    <!--地图-->
    <base-map ></base-map>
    <router-view></router-view>
    <toggle class="toggle"></toggle>
    <!--个人中心-->
    <user-center class="rightView"></user-center>
  </div>
</template>

<script>
import toggle from '../components/toggleBox';
import userCenter from '../components/userCenter'
import baseMap from '../components/baseMap.vue'

export default {
  name: 'home',
  components: {
    baseMap,toggle,userCenter
  },
  created(){
    this.$router.push("siteSearch");//跳转地名搜索
  },
  data () {
    return {
    }
  },
  methods:{

  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
  .wrapper{
  }
  .toggle{
    position: absolute;
    bottom: 20px;
    right: 20px;
    z-index: 100;
    cursor: pointer;
  }
  .rightView {
    z-index: 100;
    position: absolute;
    top: 20px;
    right: 20px;
  }
</style>
