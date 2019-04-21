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

      <!--loading spin-->
      <small-spin v-if="needSpin" msg="正在加载……"></small-spin>

      <!--列表-->
      <ul v-show="showList" class="siteList">
        <li class="flex_row" @click="toggleEditModal(l.fid,index)" v-for="(l,index) in listArr"
         :class="{sel:curEditIndex==index}"
        >
          <span>{{l.address}}</span>
          <img src="../../static/images/map/edit.png" class="imgItem">
        </li>
        <Page :current='page' :total="listCount" show-total size="small" class-name="pageClass" @on-change="pageChanged"/>
      </ul>

    </div>

    <!--编辑对话框-->
    <Modal
      v-model="showEditModal"
      title="地址编辑"
      :mask="false"
      :styles="{
          width: '320px',
          position: 'absolute',
          top: '75px',
          right: '20px',
          maxHeight:'80%'}"
      scrollable :visible="editVisible"
    >

      <div v-for="(val,index) in editObj" class="editLine flex_row"
      :class="{autoHeightWrap:val.type=='textarea'}">
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


      <!--相似标准地址-->
      <div class="editLine flex_row  autoHeightWrap">
        <div class="label flex_row">
          <span>相似标准地址</span>
        </div>
        <div class="selectWrapper autoHeight">
          <Select v-model="curSelAddress.address" class="select"
                  placeholder="无匹配数据"
          >
            <Option v-for="(s,index) in matchResult" :value="s.address" :key="index"
                    :label="s.address">
                <span>{{ s.address }}</span>
                <Progress
                          :stroke-width="7" title="相似度"
                          :percent="s.similarity*100" class="similar_row"/><!--相似度 similarity-->
            </Option>

          </Select>

        </div>
      </div>

      <!--选择结果-->
      <div class="editLine flex_row autoHeightWrap">
        <div class="label flex_row">
          <span>标准地址</span>
        </div>

        <div class="autoHeight flex_row" v-if="curSelAddress.address">
          <span>{{curSelAddress.address}}</span>
          <Progress vertical :stroke-width="7" title="相似度"
                    :percent="curSelAddress.similarity*100" class="similar"/><!--相似度 similarity-->
        </div>
        <div v-if="!curSelAddress.address" class="autoHeight flex_row" >无匹配数据</div>
      </div>

      <!--按钮-->
      <div slot="footer" class="">
        <Button size="small" @click="backRecord"
                :disabled="page==1&&curEditIndex==0"
        >
          <Icon type="ios-arrow-back"></Icon>
        </Button>
        <Button>取消</Button>
        <Button type="primary" @click="submitEdit">保存</Button>
        <Button size="small" @click="nextRecord"
                :disabled="(curEditIndex==listCount%10-1||curEditIndex==9)&&(page==Math.ceil(listCount/10))"

        >
          <Icon type="ios-arrow-forward"></Icon>
        </Button>
      </div>

    </Modal>

  </div>

</template>


<script>
  import SmallSpin from "./smallSpin";

  export default {
    name: 'editBox',
    data() {
      return {
        needUser: false,

        needSpin: false,//显示spin
        page: 1,//当前页
        curStreet: '',//当前选择的街道
        allStreets: EDITSELECTORCFG['street'],
        curCommunity: '',//当前选择的社区
        allCommunity: [],


        showList: false,//打开非标准地址列表
        //非标准地址列表
        listArr: [],
        listCount: 0,//非标准地址总数，用于分页
        curEditIndex:-1,//当前选择结果索引


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
          code: '',
          name: '',//企业名称
          street: '',//街道
          community: '',//社区
          originAddress: ''//原地址
        },

        matchResult: [],//相似标准地址
        curSelAddress: {},//当前选择的匹配结果

      }
    },
    components: {SmallSpin},
    computed: {},
    props: {},
    created() {
    },
    mounted() {

    },
    methods: {
      //监听到页码变化
      pageChanged(page,option) {
        option=option||{needSelFirst: true};
        var $this = this;
        $this.page = page;
        //调用接口获取当前街道当前页，社区下的所有非标准地址
        $this.getAddressByCommunity(false,option)
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
            $this.getAddressByCommunity(true,{needSelFirst:true})
          })

        }

      },
      //当选择社区时
      onSelCommunity() {
        var curCommunity = this.curCommunity;
        var $this = this;
        if (curCommunity) {
          $this.getAddressByCommunity(true,{needSelFirst:true})
        }

      },
      //调用接口获取当前街道，社区下的所有非标准地址
      getAddressByCommunity(needTotal,option) {
        var $this = this;
        option=option||{};
        $this.curEditIndex=-1;
        $this.showList=false;
        $this.needSpin = true;//显示正在加载

        needTotal = needTotal == undefined ? true : needTotal;//翻页的时候可以不必查询总数
        if (needTotal) {
          //查询总数
          var url = URLCFG['getCountByCommunityUrl'];
          $this.$api.getMsg(url, {tablename: $this.curCommunity}).then(function (res) {
            $this.listCount = res.total;//总数
          });
          //将页面恢复到第1页
          $this.page=1;
        }
        //查询当前页列表
        var url = URLCFG['getAddressByCommunityUrl'];
        $this.$api.getAddressByCommunity(url, $this.page, $this.curCommunity).then(function (res) {
          $this.needSpin = false;//显示正在加载
          $this.listArr = res.rows;
          $this.showList = true;
          if(option.needSelFirst){
            //需要默认选择第一项
            $this.toggleEditModal(res.rows[0]['fid'],0)
          }else if(option.needSelLast){
            //需要默认选择最后一项
            $this.toggleEditModal(res.rows[9]['fid'],9)
          }
        });
      },
      //编辑开关
      toggleEditModal(fid,index) {
        var $this = this;
        $this.curSelAddress={};
        //调用接口查询编辑信息
        var url = URLCFG['getEditInfoUrl'];
        $this.$api.getEditMsg(url, fid, $this.curCommunity).then(function (res) {
          $this.editValue = {
            code: res.rows[0]['code'],
            name: res.rows[0]['name'],
            street: res.rows[0]['street'],
            // community: res.rows[0]['community'],
            community: $this.curCommunity,
            originAddress: res.rows[0]['address']
          };
          $this.showEditModal = true;
          $this.curEditIndex = index;//记录当前索引，方便下一条

          //调用接口获取匹配地址
          var keyword = res.rows[0]['address'];
          var match_url = URLCFG['getMatchListUrl'];
          $this.$api.getMatchList(match_url, keyword).then(function (res) {
            if(res.rows&&res.rows.length>0){
              $this.matchResult = res.rows;
              $this.curSelAddress = res.rows[0];//默认选择第一个
            }else{
              //没有匹配结果
              $this.matchResult=[];
              $this.curSelAddress={}
            }
          });

        });

      },

      //上一条
      backRecord() {
        if (this.curEditIndex > 0) {
          var index = this.curEditIndex - 1;
          var fid = this.listArr[index]['fid'];
          this.toggleEditModal(fid, index);
        } else if (this.page > 1) {
          //执行向前翻页
          this.pageChanged(this.page - 1, {needSelLast:true});
          this.curEditIndex = 9
        }
      },
      //下一条
      nextRecord(){
        var pageCount = Math.ceil(this.listCount/10);//总页数
        if(this.page<pageCount){
          //非最后一页
          if(this.curEditIndex<9) {
            var index = this.curEditIndex + 1;
            var fid = this.listArr[index]['fid'];
            this.toggleEditModal(fid, index);
          }else{
            //往后翻一页
            this.pageChanged(this.page+1,{needSelFirst:true});
            this.curEditIndex = 0
          }
        }else if((this.listCount%10==0&&this.curEditIndex<9)||(this.curEditIndex<this.listCount%10-1)){
          //是最后一页
          //非最后一条时可以点击下一条
          var index = this.curEditIndex + 1;
          var fid = this.listArr[index]['fid'];
          this.toggleEditModal(fid, index);
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

  .siteList li {
    height: 45px;
    justify-content: flex-start;
    cursor: pointer;
    padding: 0 10px;
  }

  .siteList .sel,.siteList li:hover {
    color: #fff;
    background: rgba(0, 0, 0, 0.2);
  }

  .siteList li span {
    width: 100%;
    padding: 2px 0;
  }

  .siteList li .imgItem {
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

  .autoHeightWrap .label {
    /*height: auto;*/
    /*min-height: 35px;*/
  }
  .autoHeightWrap{
    height: auto;
    background:#3385ff ;
  }
  .editLine .autoHeight {
    height: auto;
    min-height: 35px;
    padding-left: 10px;
    width: 100%;
    background: #fff;
    justify-content: flex-start;
  }

  .editLine .text, .editLine .textarea{
    margin-left: 10px;
    height: 80%;
    width: 100%;
  }

  .selectContainer {
    height: 45px;
  }

  .editLine .selectWrapper {
    position: relative;
    padding: 5px 0 10px 5px;
  }
  /*相似度*/
  .similar{
    border-left: 1px solid #f3f3f3;
    color: #3385ff;
    text-align: center;
  }
  .similar_row{
    border-top: 1px solid #f3f3f3;
    color: #3385ff;
    text-align: center;
  }
  .editLine .select {
    width: 100%;
    height: auto;
  }

  .editLine:last-child{
    border: none;
  }

</style>
