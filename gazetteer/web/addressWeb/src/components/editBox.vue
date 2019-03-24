/*
*编辑框
*/

<template xmlns:v-bind="http://www.w3.org/1999/xhtml">
  <div>
    <!--非标准地址列表-->
    <div class="listView">
      <div class="selector flex_row">
        <div class="block flex_row">
          <!--选择街道-->
          <span class="label">街道：</span>
          <select v-model="curStreet" class="select" @change="onSelStreet">
            <option v-for="s in allStreets">
              {{s}}
            </option>
          </select>
        </div>

        <div class="block flex_row">
          <span class="label">社区：</span>
          <!--选择社区-->
          <select v-model="curCommunity" class="select" @change="onSelCommunity">
            <option v-for="c in allCommunity">
              {{c.community}}
            </option>
          </select>
        </div>
        <div class="flex_row toggle">
          <span v-show="showList"  @click="showList=!showList">▲</span>
          <span v-show="!showList"  @click="showList=!showList">▼</span>
        </div>
      </div>


      <!--列表-->
     <ul v-if="showList" class="siteList">
       <li class="site flex_row" @click="toggleEditModal(l.address)" v-for="(l,index) in listArr.slice(10*(page-1),10*page)">
         <span>{{l.address}}</span>
         <img src="../../static/images/map/edit.png" class="imgItem">
       </li>
       <Page :total="count" show-total size="small" class-name="pageClass" @on-change="pageChanged"/>
     </ul>

    </div>

    <!--编辑对话框-->
    <Modal
      v-model="showEditModal"
      title="地址编辑"

      @on-ok="submitEdit"
      :mask="false"
      :styles="{
          width: '320px',
          position: 'absolute',
          top: '75px',
          right: '20px',
          maxHeight:'80%'}"
       scrollable :visible="editVisible"
    >

      <div v-for="(val,index) in editObj" class="editLine flex_row">
        <div class="label flex_row">
          <span v-html="val.label"></span>
        </div>


        <input type="text" v-model="editValue[val.val]"  :class="val.disabled?'text':'text canEdit'"
               :disabled="val.disabled" />

      </div>


      <div  class="editLine flex_row selectContainer">
        <div class="label flex_row">
          <span>相似标准地址</span>
        </div>


        <div  class="selectWrapper">
          <select  v-model="curSelAddress" class="select" @change="onEditSelect" size="5">
            <option v-for="s in matchResult">
              {{s.address}}
            </option>
          </select>
        </div>

      </div>

      <!--按钮-->
      <div slot="footer" class="">
        <Button size="small">
          <Icon type="ios-arrow-back"></Icon>
        </Button>
        <Button>取消</Button>
        <Button type="primary"  @click="submitEdit">保存</Button>
        <Button  size="small">
          <Icon type="ios-arrow-forward"></Icon>
        </Button>
      </div>

    </Modal>

  </div>

</template>


<script>
  import {EDITSELECTORCFG,URLCFG} from '../js/config.js'
  export default{
    name: 'editBox',
    data() {
      return {
        needUser:false,

        page:1,//当前页
        curStreet:'',//当前选择的街道
        allStreets:EDITSELECTORCFG['street'],
        curCommunity:'',//当前选择的社区
        allCommunity:[],
        showList:false,//打开非标准地址列表
        //非标准地址列表
        listArr:[],

        showEditModal:false,//是否打开编辑浮云
        //编辑对象
        editVisible:false,
        editObj:[
          {'label':'统一社会信用代码',val:'code','disabled':true,'necessary':false,type:'text'},
          {'label':'企业名称',val:'name','disabled':true,'necessary':false,type:'text'},
          {'label':'街道',val:'street','disabled':true,'necessary':false,type:'text'},
          {'label':'社区',val:'community','disabled':true,'necessary':false,type:'text'},
          {'label':'原地址',val:'originAddress','disabled':true,'necessary':false,type:'text'},//JYCS字段

        ],
        editValue:{
          code:'00xx',
          name:'xxx',
          street:'xx街道',
          community:'xx社区',
          originAddress:'原地址'
        },
        curSelAddress:'匹配结果1',//当前选择的匹配结果
        matchResult:[]

      }
    },
    components: {},
    computed: {
      //非标准地址条数
      count(){
        return this.listArr.length
      },
    },
    props: {
    },
    created(){
    },
    mounted(){

    },
    methods: {
      //监听到页码变化
      pageChanged(page){
        this.page = page
      },

      //选择街道时
      onSelStreet(){
        var curStreet = this.curStreet;
        var $this = this;
        if(curStreet){
          //调用接口获取当前街道含有的所有社区
          var url = URLCFG['getCommunityByStreet'];
          $this.$api.getMsg(url,{tablename:curStreet}).then(function (res) {
            $this.allCommunity = res;
            $this.curCommunity = res[0]['community'];

             //调用接口获取当前街道，社区下的所有非标准地址
           var url = URLCFG['getAddressByCommunity'];
           $this.$api.getMsg(url, {fields:'address',tablename: $this.curCommunity}).then(function (res) {
            $this.listArr=res;
            $this.showList = true;
           });
          })



         //测试数据
          /*var res = [{"community":"龙塘社区"},{"community":"新牛社区"},{"community":"白石龙社区"},{"community":"民乐社区"},{"community":"樟坑社区"},{"community":"民治社区"},{"community":"民新社区"},{"community":"上芬社区"},{"community":"民强社区"},{"community":"大岭社区"},{"community":"北站社区"},{"community":"民泰社区"}];
          $this.allCommunity = res;
          $this.curCommunity = res[0]['community'];
          $this.showList = true;
          var res = [{"address":"深圳市龙华区民治街道民治社区民治大道398号汇宝江大厦B503"},{"address":"深圳市龙华区民治街道民治社区潜龙＊鑫茂花园A区3栋2单元16D"},{"address":"深圳市龙华区民治街道民治社区沙吓新二村8栋902号"},{"address":"深圳市龙华区民治街道民治社区沙元埔大厦380栋1408"},{"address":"深圳市龙华区民治街道民治社区梅花山庄欣梅园C33栋整套"},];
          $this.listArr = res;
          $this.showList = true;*/
        }

      },
      //当选择社区时
      onSelCommunity(){
        var curCommunity  = this.curCommunity;
        var $this = this;
        if(curCommunity){
          //调用接口获取当前街道，社区下的所有非标准地址
          var url = URLCFG['getAddressByCommunity'];
          $this.$api.getMsg(url, {fields:'address',tablename: curCommunity}).then(function (res) {
           $this.listArr=res;
           $this.showList = true;
          });

        /* //测试数据
          var res = [{"address":"深圳市龙华区民治街道民治社区民治大道398号汇宝江大厦B503"},{"address":"深圳市龙华区民治街道民治社区潜龙＊鑫茂花园A区3栋2单元16D"},{"address":"深圳市龙华区民治街道民治社区沙吓新二村8栋902号"},{"address":"深圳市龙华区民治街道民治社区沙元埔大厦380栋1408"},{"address":"深圳市龙华区民治街道民治社区梅花山庄欣梅园C33栋整套"},];
          $this.listArr = res;
          $this.showList = true;*/
        }

      },
      //编辑开关
      toggleEditModal(address){
        var $this = this;
        //调用接口查询编辑信息
         var url = URLCFG['getEditInfo'];
         $this.$api.getEditMsg(url,address,$this.curStreet).then(function (res) {
           $this.editValue = {
             code:res[0]['code'],
               name:res[0]['name'],
               street:res[0]['street'],
               community:res[0]['community'],
               originAddress:res[0]['address']
           };
           $this.showEditModal = true;

           //调用接口获取匹配地址
           var keyword = res[0]['name'];
           var match_url = URLCFG['getMatchList'];
           $this.$api.getEditMsg(match_url,$this.curCommunity,keyword).then(function (res) {
             $this.matchResult =res;
             $this.curSelAddress=res[0]['address'];//默认选择第一个

           });

         });


         //测试数据
        /* var res = [{"address":"深圳市龙华区龙华办事处和平路28号福轩大厦第九层908号","code":"9144030007177920XH","name":"深圳福吉田科技有限公司","owner":"陈佳杰","street":"龙华"}]
        $this.editValue = {
          code:res[0]['code'],
          name:res[0]['name'],
          street:res[0]['street'],
          community:res[0]['community'],
          originAddress:res[0]['address']
        };
        $this.showEditModal = true;
        var res=[{"address":"广东省深圳市龙华区龙华街道油松社区东环二路万亨达大厦","similarity":0.22},{"address":"广东省深圳市龙华区龙华街道油松社区东环二路水斗新围万亨达大厦104","similarity":0.17},{"address":"广东省深圳市龙华区龙华街道油松社区东环二路水斗新围万亨达大厦B808","similarity":0.17},{"address":"广东省深圳市龙华区龙华街道油松社区东环二路水斗新围万亨达大厦A1001","similarity":0.17},{"address":"广东省深圳市龙华区龙华街道油松社区东环二路水斗新围万亨达大厦A2002","similarity":0.17},{"address":"广东省深圳市龙华区龙华街道油松社区东环二路水斗新围万亨达大厦102","similarity":0.17},{"address":"广东省深圳市龙华区龙华街道油松社区东环二路水斗新围万亨达大厦201","similarity":0.17},{"address":"广东省深圳市龙华区龙华街道油松社区东环二路水斗新围万亨达大厦A1101","similarity":0.17},{"address":"广东省深圳市龙华区龙华街道油松社区东环二路水斗新围万亨达大厦301","similarity":0.17}]
        $this.matchResult =res;
        $this.curSelAddress=res[0]['address'];//默认选择第一个*/
      },

      //编辑框中select控件改变时
      onEditSelect(label,value){
        var editObj = this.$data.editObj;
        editObj[editObj.length-2]['value'] = value;//将所选择的相似标准地址写入校正……
        this.$set(editObj,editObj)
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
    },
    beforedestroy(){

    },
    watch: {
/*      showEdit: {
        handler: function (val, oldVal) {
          debugger
          if(val){
            this.showEditModal = val;
            this.$emit('onEditHasOpen');
          }
        },
        immediate:false
      }*/
    }
  }
</script>

<style scoped>
.listView{
  width: 380px;
}
.listView .selector{
  margin-top: 20px;
  width: 100%;
  background: #3385ff;
  color: #fff;
  border-radius: 2px;
  box-shadow: 1px 2px 1px rgba(0, 0, 0, .15);
  height: 40px;
}
.selector .block{
  width: 50%;
  padding: 0 2%;
}
.selector .block:first-child{
  border-right: 1px solid #f2f2f2;
}
.selector .select{
  border-color: #fff;
  padding: 2px;
  width: 60%;
  border-radius: 3px;
}
  .selector .toggle {
    flex-shrink: 0;
    padding: 0 10px;
    border-left: 1px solid #fff;
    cursor: pointer;
  }
  /*非标准地址列表*/
  .siteList{
    background: #fff;
    margin-top: 10px;
    border-radius: 4px;
    width: 100%;
    padding: 0 0 10px;
  }
.site{
  height: 45px;
  justify-content: flex-start;
  cursor: pointer;
  padding: 0 10px;
}
.site:hover{
  color: #fff;
  background: rgba(0,0,0,0.2);
}
.site span{
  width: 100%;
  padding: 2px 0;
}
.site .imgItem{
  flex-shrink: 0;
  width: 20px;
  margin-left:10px;
}
/*分页*/
.pageClass{
  padding: 5px 10px 0;
  border-top: 1px solid #f2f2f2;
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
    width: 100px;
    justify-content: center;
    flex-shrink: 0;
  }
  .editLine .canEdit{
    border: 1px solid #7f7f7f;
  }
  .editLine .text{
    background: none;
  }
  .editLine .text,.editLine .textarea,.editLine .selectWrapper{
    margin-left: 10px;
    height: 80%;
    width: 100%;
  }
  .editLine .textarea{
    padding: 2px;
  }
.selectContainer{
  height: 120px;
  border: none;
}
  .editLine .selectWrapper{
    position: relative;
    max-height: 120px;
  }
  .editLine .select{
    position: absolute;
    top: 0;
    width: 100%;
  }
.editLine .select option{
height: 20px;
}
</style>
