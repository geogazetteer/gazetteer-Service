<template xmlns:v-bind="http://www.w3.org/1999/xhtml">
  <div class="viewRight flex_col">

    <div class="flex_row">

      <div class="toolscontainer flex_row">
        <!--编辑-->
    <!--    <div class="itemBlock flex_row" @click="toggleEditModal">
          <img src="../../static/images/map/edit.png" class="imgItem">
          <span class="item">编辑</span>
        </div>-->

        <!--编辑对话框-->
        <Modal
          v-model="showEditModal"
          title="地址编辑"
          ok-text="确定"
          cancel-text="取消"
          @on-ok="submitEdit"
          :mask="false"
          :styles="{
          width: '320px',
          position: 'absolute',
          top: '75px',
          right: '20px',
          maxHeight:'80%'}"
          loading scrollable :visible="editVisible"
        >

          <div v-for="(e,index) in editObj" class="editLine flex_row">
            <div class="label flex_row">
              <span style="color:red;vertical-align: middle;padding-right: 4px;" v-if="e.necessary">*</span>
              <span >{{e.label}}：</span>
            </div>


            <input type="text" v-model="e.value" v-if="e.type=='text'" :class="e.disabled?'text':'text canEdit'" :disabled="e.disabled"/>
            <select v-if="e.type=='select'" v-model="e.value" class="select">
              <option  v-for="s in e.selectors">
                {{s}}
              </option>
            </select>

            <textarea v-if="e.type=='textarea'" class="textarea"
                      v-model="e.value" :placeholder="e.placeholder"></textarea>
          </div>

        </Modal>

        <!--导入-->
   <!--     <div class="itemBlock flex_row">
          <input type="file" class="fileInput" @change="setFilePath" multiple>
          <img src="../../static/images/map/input.png" class="imgItem">
          <span class="item">导入</span>
        </div>

        &lt;!&ndash;导出&ndash;&gt;
        <div class="itemBlock flex_row" @click="outputFile">
          <img src="../../static/images/map/output.png" class="imgItem">
          <span class="item">导出</span>
        </div>-->

        <!--我要反馈-->
        <div class="itemBlock flex_row" @click="toggleAssumeModal">
          <img src="../../static/images/map/msg.png" class="imgItem">
          <span class="item">我要反馈</span>
        </div>

        <!--反馈对话框-->
        <Modal
          v-model="showAssumeModal"
          title="地址反馈"
          ok-text="确定"
          cancel-text="取消"
          @on-ok="submitAssume"
          :mask="false"
          :styles="{
          width: '320px',
          position: 'absolute',
          top: '75px',
          right: '20px',
           'overflow-y':'scroll',
    'max-height':'85%'}"
          loading scrollable
        >

          <div v-for="(e,index) in assumeObj" class="editLine flex_row">
            <div class="label flex_row">
              <span style="color:red;vertical-align: middle;padding-right: 4px;" v-if="e.necessary">*</span>
              <span >{{e.label}}：</span>
            </div>


            <input type="text" v-model="e.value" v-if="e.type=='text'" :class="e.disabled?'text':'text canEdit'" :disabled="e.disabled"/>
            <select v-if="e.type=='select'" v-model="e.value" class="select">
              <option  v-for="s in e.selectors">
                {{s}}
              </option>
            </select>

            <textarea v-if="e.type=='textarea'" class="textarea"
                      v-model="e.value" :placeholder="e.placeholder"></textarea>
          </div>

        </Modal>
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
        showEditModal:false,//是否打开编辑浮云

        //编辑对象
        editVisible:false,
        editObj:[
          {'label':'省份','disabled':true,value:'广东省','necessary':true,type:'text'},
          {'label':'城市','disabled':true,value:'深圳市','necessary':true,type:'text'},
          {'label':'区县','disabled':true,value:'宝安区','necessary':true,type:'text'},
          {'label':'街道','disabled':true,value:'xxx街道','necessary':true,type:'text'},
          {'label':'社区','selectors':['A社区','B社区'],value:'A社区','necessary':true,type:'select'},
          {'label':'姓名',value:'','necessary':true,type:'text'},
          {'label':'手机',value:'','necessary':true,type:'text'},
          {'label':'标准地址',value:'','necessary':true,type:'text'},//选择确认的标准地址
          {'label':'历史地址',value:'','necessary':true,type:'text'},//JYCS字段
          {'label':'相似标准地址','value':'',placeholder:'(可选)列出相似标准地址，帮助匹配地址',type:'textarea',},//列出相似的标准地址,可选择
        ],

        //是否打开反馈浮云
        showAssumeModal:false,
        assumeObj:[
          {'label':'省份','disabled':true,value:'广东省','necessary':true,type:'text'},
          {'label':'城市','disabled':true,value:'深圳市','necessary':true,type:'text'},
          {'label':'区县','disabled':true,value:'宝安区','necessary':true,type:'text'},
          {'label':'街道','disabled':true,value:'xxx街道','necessary':true,type:'text'},
          {'label':'社区','selectors':['A社区','B社区'],value:'A社区','necessary':true,type:'select'},
          {'label':'姓名',value:'','necessary':true,type:'text'},
          {'label':'手机',value:'','necessary':true,type:'text'},
          {'label':'问题编码',value:'','necessary':true,type:'text'},//选择确认的标准地址
          {'label':'问题地址',value:'','necessary':true,type:'text'},//JYCS字段
          {'label':'问题描述','value':'','necessary':true,type:'textarea',},//列出相似的标准地址,可选择

        ]
      }
    },
    components: {},
    computed: {},
    props: {
      showEdit:{
        type:Boolean
      }
    },
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
      //提交地址编辑
      submitEdit(){
        var $this = this;
        //console.log(this.editObj)
        var editObj = this.editObj;
        for (var i=0;i<editObj.length;i++){

        };
        setTimeout(function(){
          $this.editVisible = false
        },1000)

      },
      //存储获取的文件路径
      setFilePath(e) {
        var paths = e.currentTarget.files
      },
      //导出文件
      outputFile(){
        alert('url为空！')
      },

      //打开我要反馈窗口
      toggleAssumeModal(){
        this.showAssumeModal = !this.showAssumeModal
      },
      //提交反馈内容
      submitAssume(){
      }
    },
    beforedestroy(){

    },
    watch: {
      showEdit: {
        handler: function (val, oldVal) {
          debugger
          if(val){
            this.showEditModal = val;
            this.$emit('onEditHasOpen');
          }
        },
        immediate:false
      }
    }
  }
</script>

<style scoped>
  .viewRight {
    z-index: 100;
    position: absolute;
    top: 20px;
    right: 10px;
    align-items: flex-end;
    width:260px
    /*width: 340px;*/
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
    /*padding: 0 30px;*/
    border-right: 1px solid #f2f2f2;
    font-size: 12px;
    cursor: pointer;
    position: relative;
    width: 100px;
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
    width: 16px;
  }
  .toolscontainer  .imgItem img{
    width: 80%;
  }

  /*个人中心*/
  .info-box {
    width: 100%;
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

  /*编辑对话框*/
  .editModal .ivu-modal{
    position: absolute;
    top:120px;
    right: 0;
  }
  .editLine{
    justify-content: flex-start;
    width: 100%;
    height: 35px;
    border-bottom: 2px solid #f2f2f2;
  }
  .editLine .label{
    background: #3385ff;
    color: #fff;
    height: 100%;
    width: 85px;
    justify-content: center;
    flex-shrink: 0;
  }
  .editLine .canEdit{
    border: 1px solid #7f7f7f;
  }
  .editLine .text{
    background: none;
  }
  .editLine .text,.editLine .select,.editLine .textarea{
    margin-left: 10px;
    height: 80%;
    width: 100%;
  }
  .editLine .textarea{
    padding: 2px;
  }
  .editLine:last-child{
    height: 100px;
  }
</style>
