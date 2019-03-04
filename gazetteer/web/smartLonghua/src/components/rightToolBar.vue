<template xmlns:v-bind="http://www.w3.org/1999/xhtml">
  <div class="viewRight flex_col">

    <div class="flex_row">

      <div class="toolscontainer flex_row">
        <!--编辑-->
        <div class="itemBlock flex_row" @click="toggleEditModal">
          <span class="imgItem">
            <img src="../../static/images/map/edit.png">
          </span>
          <span class="item">编辑</span>
        </div>

        <!--编辑对话框-->
        <Modal
          v-model="showEditModal"
          title="采集数据"
          ok-text="确定"
          cancel-text="取消">
          <p>信息一...</p>
          <p>信息二...</p>
          <p>信息三...</p>
        </Modal>

        <!--导入-->
        <div class="itemBlock flex_row" >
          <input type="file" class="fileInput" @change="setFilePath" multiple>
          <span class="imgItem">
            <img src="../../static/images/map/input.png">
          </span>
          <span class="item">导入</span>
        </div>

        <!--导出-->
        <div class="itemBlock flex_row" @click="outputFile">
          <span class="imgItem">
            <img src="../../static/images/map/output.png">
          </span>
          <span class="item">导出</span>
        </div>

      </div>
      <!--头像-->
      <div class="user-center">
        <div class="avatar" @click="toggleUser"></div>
      </div>
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
    name: 'rightToolBar',
    data() {
      return {
        needUser:false,
        showEditModal:false,//编辑浮云
      }
    },
    components: {},
    computed: {},
    props: {},
    created(){
    },
    mounted(){

    },
    methods: {
      //个人中心开关
      toggleUser(){
        this.needUser = !this.needUser
      },
      //编辑开关
      toggleEditModal(){
        this.showEditModal = !this.showEditModal
      },
      //存储获取的文件路径
      setFilePath(e) {
        var paths = e.currentTarget.files
      },
      //导出文件
      outputFile(){
        alert('url为空！')
      }
    },
    beforedestroy(){

    },
    watch: {
      /*    currentBtn:{
       handler:function (btn, oldVal) {
       },
       //immediate:true
       }*/
    }
  }
</script>

<style scoped>
  .viewRight {
    z-index: 100;
    position: absolute;
    top: 20px;
    right: 10px;
    width: 300px;
  }
  /*用户中心*/
  .user-center {
    width: 48px;
    height: 48px;
    border-radius: 50%;
    box-shadow: 1px 2px 1px rgba(0,0,0,.15);
  }
  .user-center .avatar {
    background-color: #fff;
    background-image: url('../../static/images/map/avatar-default.png');
    border-radius: 50%;
    width: 100%;
    height: 100%;
    overflow: hidden;
    cursor: pointer;
    background-size: cover;
    background-repeat: no-repeat;
    background-position: center center;
    -webkit-transition: opacity .4s linear;
    transition: opacity .4s linear;
  }

  /*工具栏*/
  .toolscontainer{
    margin-right: 12px;
    background: #fff;
    box-sizing: border-box;
    border-radius: 2px;
    box-shadow: 1px 2px 1px rgba(0,0,0,.15);
    padding: 8px 0;
  }
  .toolscontainer .itemBlock{
    padding: 0 12px;
    border-right: 1px solid #f2f2f2;
    font-size: 12px;
    cursor: pointer;
    position: relative;
  }
  /*隐藏选择文件按钮*/
  .toolscontainer .itemBlock .fileInput{
    position: absolute;
    left: 0;
    top: 0;
    width: 100%;
    height: 100%;
    opacity: 0;
    cursor: pointer;
    filter:alpha(opacity=0)
  }
  .toolscontainer .itemBlock:last-child{
    border: none;
  }
  .toolscontainer  .imgItem{
    margin-right: 6px;
  }
  .toolscontainer  .imgItem img{
    width: 80%;
  }

  /*个人中心*/
  .info-box {
    width: 284px;
    position: relative;
    margin-top: 24px;
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
    border-radius: 2px;
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
  .info-box .detail-info-box .loginBtn{
    border: 1px solid #fff;
    border-radius: 8px;
    padding: 5px 10px;
    margin-left: 10px;

  }
</style>
