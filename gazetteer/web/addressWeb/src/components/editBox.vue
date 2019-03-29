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
          <span v-show="showList" @click="showList=!showList">▲</span>
          <span v-show="!showList" @click="showList=!showList">▼</span>
        </div>
      </div>


      <!--列表-->
      <ul v-if="showList" class="siteList">
        <li class="site flex_row" @click="toggleEditModal(l.fid,index)" v-for="(l,index) in listArr">
          <span>{{l.address}}</span>
          <img src="../../static/images/map/edit.png" class="imgItem">
        </li>
        <Page :total="listCount" show-total size="small" class-name="pageClass" @on-change="pageChanged"/>
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


        <input type="text" v-model="editValue[val.val]" :title="editValue[val.val]"
               :class="val.disabled?'text':'text canEdit'"
               :disabled="val.disabled" v-if="val.type=='text'"/>
        <div class="autoHeight"
             v-if="val.type=='textarea'">
          {{editValue[val.val]}}
        </div>

      </div>


      <div class="editLine flex_row selectContainer">
        <div class="label flex_row">
          <span>相似标准地址</span>
        </div>


        <div class="selectWrapper">
          <select v-model="curSelAddress" class="select" @change="onEditSelect" size="5">
            <option v-for="s in matchResult">
              {{s.address}}
            </option>
          </select>
        </div>

      </div>

      <!--按钮-->
      <div slot="footer" class="">
        <Button size="small" @click="backRecord">
          <Icon type="ios-arrow-back"></Icon>
        </Button>
        <Button>取消</Button>
        <Button type="primary" @click="submitEdit">保存</Button>
        <Button size="small" @click="nextRecord">
          <Icon type="ios-arrow-forward"></Icon>
        </Button>
      </div>

    </Modal>

  </div>

</template>


<script>
  import {EDITSELECTORCFG, URLCFG} from '../js/config.js'

  export default {
    name: 'editBox',
    data() {
      return {
        needUser: false,

        page: 1,//当前页
        curStreet: '',//当前选择的街道
        allStreets: EDITSELECTORCFG['street'],
        curCommunity: '',//当前选择的社区
        allCommunity: [],
        showList: false,//打开非标准地址列表
        //非标准地址列表
        listArr: [],
        listCount: 0,//非标准地址数目，用于分页

        showEditModal: false,//是否打开编辑浮云
        //编辑对象
        editVisible: false,
        editObj: [
          {'label': '统一社会信用代码', val: 'code', 'disabled': true, 'necessary': false, type: 'text'},
          {'label': '企业名称', val: 'name', 'disabled': true, 'necessary': false, type: 'text'},
          {'label': '街道', val: 'street', 'disabled': true, 'necessary': false, type: 'text'},
          {'label': '社区', val: 'community', 'disabled': true, 'necessary': false, type: 'text'},
          {'label': '原地址', val: 'originAddress', 'disabled': true, 'necessary': false, type: 'textarea'},//JYCS字段

        ],
        editValue: {
          code: '00xx',
          name: 'xxx',
          street: 'xx街道',
          community: 'xx社区',
          originAddress: '原地址'
        },
        curSelAddress: '匹配结果1',//当前选择的匹配结果
        matchResult: []

      }
    },
    components: {},
    computed: {},
    props: {},
    created() {
    },
    mounted() {

    },
    methods: {
      //监听到页码变化
      pageChanged(page) {
        var $this = this;
        $this.page = page;
        //调用接口获取当前街道，社区下的所有非标准地址
        $this.getAddressByCommunity()
      },

      //选择街道时
      onSelStreet() {
        var curStreet = this.curStreet;
        var $this = this;
        if (curStreet) {
          //调用接口获取当前街道含有的所有社区
          var url = URLCFG['getCommunityByStreetUrl'];
          $this.$api.getMsg(url, {tablename: curStreet}).then(function (res) {
            $this.allCommunity = res.rows;
            $this.curCommunity = res['rows'][0]['community'];
            //调用接口获取当前街道，社区下的所有非标准地址
            $this.getAddressByCommunity()
          })

        }

      },
      //当选择社区时
      onSelCommunity() {
        var curCommunity = this.curCommunity;
        var $this = this;
        if (curCommunity) {
          $this.getAddressByCommunity()
        }

      },
      //调用接口获取当前街道，社区下的所有非标准地址
      getAddressByCommunity() {
        var $this = this;
        var url = URLCFG['getAddressByCommunityUrl'];
        $this.$api.getAddressByCommunity(url, $this.page, $this.curCommunity).then(function (res) {
          $this.listCount = res.total;
          $this.listArr = res.rows;
          $this.showList = true;
        });
      },
      //编辑开关
      toggleEditModal(fid,index) {
        var $this = this;
        //调用接口查询编辑信息
        var url = URLCFG['getEditInfoUrl'];
        $this.$api.getEditMsg(url, fid, $this.curCommunity).then(function (res) {
          $this.editValue = {
            code: res.rows[0]['code'],
            name: res.rows[0]['name'],
            street: res.rows[0]['street'],
            community: res.rows[0]['community'],
            originAddress: res.rows[0]['address']
          };
          $this.showEditModal = true;
          $this.curEditIndex = index;//记录当前索引，方便下一条

          //调用接口获取匹配地址
          var keyword = res.rows[0]['address'];
          //var keyword = '龙华区';
          var match_url = URLCFG['getMatchListUrl'];
          $this.$api.getMatchList(match_url, keyword).then(function (res) {
            $this.matchResult = res.rows;
            $this.curSelAddress = res.rows[0]['address'];//默认选择第一个

          });

        });

      },

      //编辑框中select控件改变时
      onEditSelect(label, value) {
        var editObj = this.$data.editObj;
        editObj[editObj.length - 2]['value'] = value;//将所选择的相似标准地址写入校正……
        this.$set(editObj, editObj)
      },

      //上一条
      backRecord(){
        if(this.curEditIndex>0){
          var index = this.curEditIndex-1;
          var fid = this.listArr[index]['fid'];
          this.toggleEditModal(fid, index);
          if(index==0){
            //执行向前翻页
            this.curEditIndex=9
          }
        }else{
        }
      },
      nextRecord(){
        if(this.curEditIndex<this.listCount-1) {
          var index = this.curEditIndex + 1;
          var fid = this.listArr[index]['fid'];
          this.toggleEditModal(fid, index);
          if (index == 9) {
            //执行翻页
            this.curEditIndex = 0
          }
        }
      },
      //提交地址编辑
      submitEdit() {
        var $this = this;
        //console.log(this.editObj)
        var editObj = this.editObj;
        for (var i = 0; i < editObj.length; i++) {

        };
        setTimeout(function () {
          $this.editVisible = false
        }, 1000)
      },
    },
    beforedestroy() {

    },
    watch: {
    }
  }
</script>

<style scoped>
  .listView {
    width: 380px;
  }

  .listView .selector {
    margin-top: 20px;
    width: 100%;
    background: #3385ff;
    color: #fff;
    border-radius: 2px;
    box-shadow: 1px 2px 1px rgba(0, 0, 0, .15);
    height: 40px;
  }

  .selector .block {
    width: 50%;
    padding: 0 2%;
  }

  .selector .block:first-child {
    border-right: 1px solid #f2f2f2;
  }

  .selector .select {
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
  .siteList {
    background: #fff;
    margin-top: 10px;
    border-radius: 4px;
    width: 100%;
    padding: 0 0 10px;
  }

  .site {
    height: 45px;
    justify-content: flex-start;
    cursor: pointer;
    padding: 0 10px;
  }

  .site:hover {
    color: #fff;
    background: rgba(0, 0, 0, 0.2);
  }

  .site span {
    width: 100%;
    padding: 2px 0;
  }

  .site .imgItem {
    flex-shrink: 0;
    width: 20px;
    margin-left: 10px;
  }

  /*分页*/
  .pageClass {
    padding: 5px 10px 0;
    border-top: 1px solid #f2f2f2;
  }


  /*编辑对话框*/
  .editModal .ivu-modal {
    position: absolute;
    top: 120px;
    right: 0;
  }

  .editLine {
    justify-content: flex-start;
    width: 100%;
    height: 35px;
    border-bottom: 2px solid #f2f2f2;
  }

  .editLine .label {
    background: #3385ff;
    color: #fff;
    height: 100%;
    width: 100px;
    justify-content: center;
    flex-shrink: 0;
  }

  .editLine .canEdit {
    border: 1px solid #7f7f7f;
  }

  .editLine .text {
    background: none;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }

  .editLine:nth-last-child(2).label {
    /*height: auto;*/
    /*min-height: 35px;*/
  }
  .editLine:nth-last-child(2){
    height: auto;
    background:#3385ff ;
  }
  .editLine .autoHeight {
    height: auto;
    padding-left: 10px;
    width: 100%;
    background: #fff;

  }

  .editLine .text, .editLine .textarea, .editLine .selectWrapper {
    margin-left: 10px;
    height: 80%;
    width: 100%;
  }

  .selectContainer {
    height: 120px;
    border: none;
  }

  .editLine .selectWrapper {
    position: relative;
    max-height: 120px;
  }

  .editLine .select {
    position: absolute;
    top: 0;
    width: 100%;
  }

  .editLine .select option {
    height: 20px;
  }
</style>
