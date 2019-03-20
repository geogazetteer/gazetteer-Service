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
          <select v-model="curStreet" class="select">
            <option v-for="s in allStreets">
              {{s}}
            </option>
          </select>
        </div>

        <div class="block flex_row">
          <span class="label">社区：</span>
          <!--选择社区-->
          <select v-model="curCommunity" class="select">
            <option v-for="c in allCommunity">
              {{c}}
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
       <li class="site flex_row" @click="toggleEditModal" v-for="(l,index) in listArr.slice(10*(page-1),10*page)">
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
      ok-text="保存"
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
          <span>{{e.label}}：</span>
        </div>


        <input type="text" v-model="e.value" v-if="e.type=='text'" :class="e.disabled?'text':'text canEdit'"
               :disabled="e.disabled"/>
        <select v-if="e.type=='select'" v-model="e.value" class="select" @change="onEditSelect(e.label,e.value)">
          <option v-for="s in e.selectors">
            {{s}}
          </option>
        </select>

            <textarea v-if="e.type=='textarea'" class="textarea"
                      v-model="e.value" :placeholder="e.placeholder"></textarea>
      </div>

    </Modal>

  </div>

</template>


<script>
  export default{
    name: 'editBox',
    data() {
      return {
        needUser:false,

        page:1,//当前页
        curStreet:'龙华街道',//当前选择的街道
        allStreets:['龙华街道','街道2','街道3','街道4'],
        curCommunity:'社区1',//当前选择的社区
        allCommunity:['社区1','社区2','社区3','社区4'],
        showList:false,//打开非标准地址列表
        //非标准地址列表
        listArr:[
          {id: 0, address: '非标准地址武汉市洪山区广八路'},
          {id: 1, address: '非标准地址武汉市洪山区广埠屯路'},
          {id: 2, address: '非标准地址武汉市洪山区八一路路'},
          {id: 3, address: '非标准地址武汉市洪山区广八路'},
          {id: 0, address: '非标准地址武汉市洪山区广八路'},
          {id: 1, address: '非标准地址武汉市洪山区广埠屯路'},
          {id: 2, address: '非标准地址武汉市洪山区八一路路'},
          {id: 3, address: '非标准地址武汉市洪山区广八路'},
          {id: 0, address: '非标准地址武汉市洪山区广八路'},
          {id: 1, address: '非标准地址武汉市洪山区广埠屯路'},
          {id: 2, address: '非标准地址武汉市洪山区八一路路'},
          {id: 3, address: '非标准地址武汉市洪山区广八路'}, {id: 0, address: '非标准地址武汉市洪山区广八路'},
          {id: 1, address: '非标准地址武汉市洪山区广埠屯路'},
          {id: 2, address: '非标准地址武汉市洪山区八一路路'},
          {id: 3, address: '非标准地址武汉市洪山区广八路'}
          , {id: 0, address: '非标准地址武汉市洪山区广八路'},
          {id: 1, address: '非标准地址武汉市洪山区广埠屯路'},
          {id: 2, address: '非标准地址武汉市洪山区八一路路'},
          {id: 3, address: '非标准地址武汉市洪山区广八路'},
          {id: 2, address: '非标准地址武汉市洪山区八一路路'},
          {id: 3, address: '非标准地址武汉市洪山区广八路'}
        ],

        showEditModal:false,//是否打开编辑浮云
        //编辑对象
        editVisible:false,
        editObj:[
          {'label':'省份','disabled':true,value:'广东省','necessary':false,type:'text'},
          {'label':'城市','disabled':true,value:'深圳市','necessary':false,type:'text'},
          {'label':'区县','disabled':true,value:'宝安区','necessary':false,type:'text'},
          {'label':'街道','disabled':true,value:'xxx街道','necessary':false,type:'text'},
          {'label':'社区',value:'A社区','disabled':true,'necessary':false,type:'text'},
//          {'label':'姓名',value:'','necessary':true,type:'text'},
//          {'label':'手机',value:'','necessary':true,type:'text'},
          {'label':'原始地址',value:'观澜街道A1栋3307','disabled':true,'necessary':false,type:'text'},//JYCS字段
          {'label':'相似标准地址','value':'匹配结果1','selectors':['匹配结果1','匹配结果2','匹配结果3','匹配结果4'],type:'select',},//列出相似的标准地址,可选择
          {'label':'校正标准地址',value:'','disabled':true,'necessary':true,type:'text'},//选择确认的标准地址

          {'label':'特殊说明',value:'','necessary':false,type:'textarea'},//选择确认的标准地址

        ],


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


      //编辑开关
      toggleEditModal(){
        this.showEditModal = true;

        //默认选择第一个相似标准地址
        var editObj = this.$data.editObj;
        editObj[editObj.length-2]['value'] = '匹配结果1';//将所选择的相似标准地址写入校正……
        this.$set(editObj,editObj)
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
  width: 320px;
}
.listView .selector{
  margin-top: 10px;
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
    width: 95px;
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
