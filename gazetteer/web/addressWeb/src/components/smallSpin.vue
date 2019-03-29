/*
*个人中心组建
*/
<template xmlns:v-bind="http://www.w3.org/1999/xhtml">

  <!--loading spin-->
  <div  class="spin-icon-load">
    <Spin fix>
      <Icon type="ios-loading" size=18 class="demo-spin-icon-load"></Icon>
      <div v-if="msg">{{msg}}</div>
    </Spin>
  </div>
</template>


<script>
  export default{
    name: 'smallSpin',
    data() {
      return {

      }
    },
    components: {},
    computed: {},
    props: {
      msg:{
        type:[String]
      }
    },
    created(){
    },
    mounted(){

    },
    methods: {
    },
    beforedestroy(){

    },
    watch: {

    }
  }
</script>

<style scoped>

  .spin-icon-load {
    background: #fff;
    width: 377px;
    position: relative;
    height: 50px;
    border-top: 1px solid #f2f2f2;
  }
</style>
