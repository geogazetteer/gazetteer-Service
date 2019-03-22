/*
*个人中心组建
*/
<template xmlns:v-bind="http://www.w3.org/1999/xhtml">

<div>
  <!--头像-->
  <div class="user-center">
    <div class="avatar" @click="toggleUser"></div>
  </div>
  <!--个人中心-->
  <div class="info-box" v-if="needUser">
    <div class="arrow"></div>
    <div class="detail-info-box flex_row">
      <div class="tips">获取个人信息请登陆</div>
      <!--<div class="loginBtn">登陆</div>-->
    </div>
  </div>
</div>
</template>


<script>
  export default{
    name: 'userCenter',
    data() {
      return {
        needUser:false,

      }
    },
    components: {},
    computed: {},
    props: {

    },
    created(){
    },
    mounted(){

    },
    methods: {
      //个人中心开关
      toggleUser(){
        this.needUser = !this.needUser
      }
    },
    beforedestroy(){

    },
    watch: {

    }
  }
</script>

<style scoped>

  /*用户中心*/
  .user-center {
    width: 48px;
    height: 48px;
    border-radius: 50%;
    box-shadow: 1px 2px 1px rgba(0,0,0,.15);
    cursor: pointer;
    position: absolute;
    right: 0;
    background-color: #fff;
  }
  .user-center .avatar {
    background-image: url('../../static/images/map/avatar-default.png');
    border-radius: 50%;
    width: 100%;
    height: 100%;
    background-size: cover;
    background-repeat: no-repeat;
    background-position: center center;
    -webkit-transition: opacity .4s linear;
  }

  /*个人中心*/
  .info-box {
    width: 260px;
    position: absolute;
    right: 0;
    top: 65px;
  }
  .info-box .arrow {
    width: 0;
    height: 0;
    border-style: solid;
    border-width: 0 7px 8px;
    border-color: transparent transparent #3385ff;
    position: absolute;
    top: -8px;
    right: 16px;
  }
  .info-box .detail-info-box {
    border-radius: 3px;
    background: #3385ff;
    width: 100%;
    height: 80px;
    box-shadow: 1px 2px 1px rgba(0,0,0,.15);
    color: #fff;
  }
  .info-box .detail-info-box .tips{
    font-size: 14px;
    border-bottom: 1px solid #fff;
    cursor: pointer;
  }
</style>
