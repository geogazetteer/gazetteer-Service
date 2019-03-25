/*
*
*/

<template>
  <div class="wrapper">
    <router-view></router-view>
    <toggle class="toggle"></toggle>
    <!--个人中心-->
    <user-center class="rightView"></user-center>
  </div>
</template>

<script>
import toggle from '../components/toggleBox';
import userCenter from '../components/userCenter'

export default {
  name: 'home',
  components: {
    toggle,userCenter
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
    width: 100%;
    height: 100%;
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
