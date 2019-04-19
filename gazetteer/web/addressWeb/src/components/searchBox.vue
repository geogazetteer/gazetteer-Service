<template xmlns:v-bind="http://www.w3.org/1999/xhtml">
  <div class="leftView">
    <!--搜索框-->
    <div class="searchbox flex_row">

      <div class="searchbox-content flex_row">
        <input id="sole-input" type="text" autocomplete="off"
               maxlength="256" placeholder="输入地名进行搜索" v-model="searchContent"
               v-on:input="getCardList" v-on:keyup.enter="searchItems" @blur="" @focus="searchFocus"
        />

        <div class="input-clear" title="清空" v-show="searchContent" @click="clearInput"></div>

      </div>

      <button id="search-button" data-title="搜索" @click="searchItems"></button>
    </div>



    <!--搜索联想-->
    <!--<ul class="cardlist" v-if="showCard">
    <li v-for="(ca,index) in cardList" @click="searchItems(ca.address)" class="flex_row">
      <span class="imgItem"></span>
      <span>{{ca.address}}</span>
    </li>
    </ul>-->

    <!--loading spin-->
    <small-spin v-if="needSpin" msg="正在搜索……"></small-spin>



    <!--搜索结果-->
   <div v-show="showResult" class="resultWrapper">
     <ul class="cardlist resultList"  :style="{maxHeight:maxHeight}">
       <li v-for="(ca,index) in resultList.slice(10*(resultPage-1),10*resultPage)" @click="showDetailList(ca.id)" class="flex_row">
         <div class="imgItem" :style="{backgroundPosition:(-21*index)+'px 0'}"></div>
         <div class="right flex_col">
           <div class="address" v-html="ca.village"></div>
           <div class="small detail">详细地址：{{ca.address}}</div>
           <!--<div class="small">楼栋编码：{{ca.code}}</div>-->
         </div>
       </li>
     </ul>
     <!--没有搜索结果-->
     <div v-if="resultList.length==0&&showResult" class="bottomDiv noSuch">
       <!--<span class="imgItem">-->
       <!--<img src="/static/images/map/search.png">-->
       <!--</span>-->
       <span>--没有搜索结果--</span>
     </div>
     <!--分页-->
     <Page :total="resultCount" show-total size="small" class-name="pageClass" @on-change="pageChanged"/>
   </div>


    <!--详情列表-->
    <div v-if="showDetail" class="detailList" >
      <div class="back flex_row" @click="backResult">
        <span class="img"></span>
        <span>返回“{{searchContent}}”的搜索结果</span>
      </div>

      <div class="bottomDiv title flex_row">
        <!--<span class="img"></span>-->
        <span class="hisBottom">编码信息</span>
        <span v-show="!hideDetail" class="hideHis" @click="hideDetail=!hideDetail">▲</span>
        <span v-show="hideDetail" class="hideHis" @click="hideDetail=!hideDetail">▼</span>
      </div>

      <div class="content" v-show="!hideDetail&&showDetail">
        <div class="line">
          <span class="bold">{{detail.detailList[0].label}}:</span>
          <span>{{detail.detailList[0].value}}</span>
        </div>

        <div class="line standard">
          <span class="bold">标准地址</span>
          <div class="standardCtx">
            <div v-for="s in detail.standard" class="standardLine flex_row">
              <div class="label flex_row">{{s.label}}</div>
              <div class="value">{{s.value}}</div>
            </div>
          </div>
        </div>

        <div class="line" v-for="d in detail.detailList.slice(1)">
          <span class="bold">{{d.label}}：</span>
          <span>{{d.value}}</span>
        </div>

      </div>
    </div>


    <!--历史记录-->
    <ul class="cardlist hislist" v-if="hisList.length>0&&showHis">
      <li v-for="(ca,index) in hisList" @click="searchItems(ca)" class="flex_row">
        <span class="imgItem"></span>
        <span>{{ca}}</span>
      </li>
      <div  class="bottomDiv flex_row">
        <!--<span class="imgItem">-->
        <!--<img src="/static/images/map/search.png">-->
        <!--</span>-->
        <div class="hisBottom" @click="clearHis">--清空历史记录--</div>
        <div @click="hideHis" class="hideHis">▲</div>
      </div>
    </ul>

  </div>

</template>


<script>
  import {RegularStr,SetRecord,GetRecord,ClearRecord} from '../js/render.js'
  import {URLCFG} from '../../static/config.js'
  import smallSpin from "./smallSpin";
  export default{
    name: 'searchBox',
    data() {
      return {
        searchContent: '',
        //搜索联想
        showCard: false,//显示搜索联想
        cardList: [//搜索联想结果
        ],

        //搜索结果
        needSpin: false,//显示spin
        resultPage:1,
        resultCount:0,//总数
        showResult:false,//显示搜索结果
        resultList:[
          /*{
            "address": "广东省深圳市龙华区观湖街道观城社区横坑河东村329号",
            "id":0001
          }*/
        ],

        //历史记录
        showHis: false,
        hisList: [],

        //详情列表
        showDetail:false,
        hideDetail:false,
        detail:{
            standard:[
              /*{label:'省',value:'广东省'},
              {label:'市',value:'深圳市'},
              {label:'区',value:'南山区'},
              {label:'街道',value:'粤海街道'},
              {label:'社区',value:'深大社区'},
              {label:'基础网格',value:'440305007015004'},
              {label:'路',value:'白石路'},
              {label:'路号',value:'3883号'},
              {label:'小区',value:'深圳大学南校区'},
              {label:'楼栋',value:'深圳大学医学院'},*/
            ],
          detailList: [
           /* {label:'楼栋编码',value:'44030500700419000013'},
            {label:'详细地址',value:'广东省深圳市南山区粤海街道深大社区白石路3833号深圳大学南校区深圳大学医学院'},
            {label:'门楼号地址',value:'广东省深圳市南山区粤海街道深大社区白石路3833号深圳大学南校区深圳大学医学院'},
            {label:'小学学区',value:''},
            {label:'初级中学学区',value:''},
            {label:'社区网格员ID',value:117847},*/
          ]
        }
      }
    },
    components: {smallSpin},
    computed: {
      maxHeight(){
        return (document.body.clientHeight*0.8||window.innerHeight*0.8)+'px'
      }
    },
    props: {},
    created(){
    },
    mounted(){
      //获取搜索历史记录
      this.hisList = GetRecord();
    },
    methods: {
      //清空搜索框
      clearInput(){
        this.searchContent = '';
        this.showCard = false;
        this.showHis = false;
        this.needSpin = false;
        this.showResult = false;
        this.showDetail=false;
      },
      //获取搜索联想
      getCardList(){
        /*this.showResult = false;//隐藏搜索结果
        this.showDetail=false;//隐藏详细信息
        var str = RegularStr(this.searchContent);//输入的搜索内容(去除空格)
        var $this = this;
        if(str){
          //调用接口获取搜索联想
          var url = URLCFG['searchCtx'];
          $this.$api.getSearchCtx(url,str).then(function (res) {
            debugger
            this.cardList = res.rows;//将接口获取的值传递到vue实例的data中
            this.showCard = true;//显示搜索联想
            this.showHis = false;//隐藏历史记录
          });
        }else{
          //当输入为空时，显示历史记录
          this.showCard = false;//隐藏搜索联想
          this.showHis = true;//显示历史记录
        }*/
      },
     //获取搜索结果
      searchItems(curStr){
        var $this = this;
        var str = typeof curStr=='string'?curStr:RegularStr($this.searchContent);//输入的搜索内容(去除空格)
        if (str) {
          $this.searchContent = str;//更新搜索框的内容
          $this.showResult = false;//隐藏结果
          $this.showHis = false;//隐藏历史记录
          $this.showCard = false;//隐藏联想
          $this.needSpin = true;//显示spin

          var url = URLCFG['searchCtxUrl'];
          $this.$api.getSearchCtx(url, str).then(function (res) {
            $this.resultCount = res.total;
            $this.resultList = res.rows;
            $this.showResult = true;//显示搜索结果
            $this.needSpin = false;//隐藏spin

            //记录历史记录到localStorage
            if(res.total>0){
              $this.hisList = SetRecord(str)
            }
          })
        } else {

        }
      },
      //监听到页码变化
      pageChanged(page){
        this.resultPage = page
      },
      //显示搜索详情
      showDetailList(id){
        var $this = this;
        if(id){
          this.showResult = false;//隐藏搜索结果

          //调用接口获取搜索详情
          var url = URLCFG['getDetailBySearchIdUrl'];
          $this.$api.getDetailBySearchId(url,id).then(function (res) {
            var listData = res.rows[0];
            var detail = {
              standard: [
                {label: '省', value: listData['province']},
                {label: '市', value: listData['city']},
                {label: '区', value: listData['district']},
                {label: '街道', value: listData['street']},
                {label: '社区', value: listData['community']},
                {label: '基础网格', value: ''},
                {label: '路', value: listData['road']},
                {label: '路号', value: listData['road_num']},
                {label: '小区', value: listData['village']},
                {label: '楼栋', value: listData['building']},
              ],
              detailList: [
                {label: '楼栋编码', value: listData['building_id']},
                {label: '详细地址', value: listData['address']},
                {label: '门楼号地址', value: ''},
                {label: '小学学区', value: ''},
                {label: '初级中学学区', value: ''},
                {label: '社区网格员ID', value: ''},
              ]
            };
            $this.detail = detail;

            $this.showDetail = true;//显示详情
            $this.hideDetail  = false;
            //this.$emit('setMarkCoord', [114.30,30.52]);//地图定位
          })

        }

      },
      //返回搜索结果
      backResult(){
        this.showResult = true;//显示搜索结果
        this.showDetail = false;//隐藏详情
      },



      //搜索框获取焦点
      searchFocus(){
        if (!this.searchContent) {
          this.showHis = true;
          this.showCard = false;
        }
      },
      //失去焦点时清空结果
      searchBlur(){
        this.searchContent = '';
        this.showCard = false;
        this.showHis = false;
        this.needSpin = false;
      },
      //收起历史记录
      hideHis(){
        this.showHis = false;
      },
      //清空历史记录
      clearHis(){
        ClearRecord();
        this.hisList=[];
      }
    },
    beforedestroy(){

    },
    watch: {
    }
  }
</script>

<style scoped>


  .searchbox .searchbox-content {
    pointer-events: auto;
    width: 320px;
    height: 38px;
    box-sizing: border-box;
    box-shadow: 1px 2px 1px rgba(0, 0, 0, .15);
    border-radius: 2px 0 0 2px;
    background: #fff;
    justify-content: flex-start;
    margin-top: 20px;
  }

  .searchbox-content #sole-input {
    margin-left: 10px;
    line-height: 20px;
    font-size: 16px;
    height: 100%;
    color: #333;
    border-radius: 2px 0 0 2px;
    justify-content: flex-start;
    outline: none;
    width: 100%;
  }

  /*清空*/
  .searchbox-content .input-clear {
    cursor: pointer;
    width: 27px;
    height: 38px;
    background: url(../../static/images/map/searchbox.png) no-repeat 0 -114px #fff;
    margin-right: 6px;
  }

  /*搜索按钮*/
  .searchbox #search-button {
    margin-top: 20px;
    pointer-events: auto;
    background: url('../../static/images/map/searchbox.png') no-repeat 0 -76px #3385ff;
    width: 57px;
    height: 38px;
    border: 0;
    padding: 0;
    cursor: pointer;
    border-radius: 0 2px 2px 0;
    box-shadow: 1px 2px 1px rgba(0, 0, 0, .15);
  }

  /*搜索结果*/
  .resultWrapper{
    position: absolute;
    top: 65px;
    left: 0;
  }
  .cardlist {
    border-top: 1px solid #f2f2f2;
    height: auto;
    max-height: 396px;
    /*overflow-y: scroll;*/
    overflow-x: hidden;
    width: 320px;
    background: #fff;
    padding-bottom: 12px;
    max-height: 80%;
  }

  .cardlist li {
    cursor: pointer;
    justify-content: flex-start;
    padding: 10px;
    font-size: 14px;
  }

  .cardlist li:hover {
    background: #3385ff;
    color: #fff;
  }

  .cardlist li .imgItem {
    margin-right: 10px;
    background-image: url('../../static/images/map/search.png');
    width: 14px;
    height: 14px;
    background-size: cover;
  }

  .cardlist li:hover .imgItem {
    background-image: url('../../static/images/map/search_w.png');
  }

  .bottomDiv {
    font-size: 14px;
    padding-top: 10px;
    border-top: 1px solid #f2f2f2;
    text-align: center;
  }
  .noSuch{
    padding-bottom: 10px;
    border: none;
    padding-top: 0;
    background: #fff;
  }

  .bottomDiv .hisBottom {
    width: 100%;
    cursor: pointer;
  }

  .bottomDiv .hideHis {
    border-left: 1px solid #f2f2f2;
    width: 40px;
    flex-shrink: 0;
    color: #3385ff;
    cursor: pointer;
  }


  /*搜索结果*/
  .resultList{
    /*top: 65px;*/
    /*position: absolute;*/
  }
  .resultList li{
    justify-content: flex-start;
    align-items: flex-start;
    position: relative;
  }
  .resultList li .imgItem{
    background-image: url('../../static/images/map/result_markers.png');
    width: 21px;
    height: 31px;
    flex-shrink: 0;
    background-size: auto;
    margin-top:4px;
  }
  .resultList li .right{
    align-items: flex-start;
  }
  .resultList li:hover{
    background: rgba(0,0,0,0.1);
    color: #000;
  }
  .resultList li:hover .imgItem{
    background-image: url('../../static/images/map/result_markers_sel.png');
  }
  .resultList li .address{
    color: #3385ff;
  }
  .resultList li .small{
    font-size: 13px;
    text-align: left;
    line-height: 25px;
  }
  /*分页*/
  .pageClass{
    padding: 5px 10px 0;
    border-top: 1px solid #f2f2f2;
    background: #fff;
    width: 320px;
  }



  /*详细信息列表*/
  /*返回搜索结果*/
  .detailList{
    width: 320px;
    height: 100%;
  }
  .back{
    background: #fff;
    box-shadow: 1px 2px 1px rgba(0, 0, 0, .15);
    border-radius: 2px 0 0 2px;
    margin: 10px 0;
    width: 100%;
    height: 38px;
    padding: 12px;
    box-sizing: border-box;
    cursor: pointer;
    color: #3385ff;
    white-space: nowrap;
    text-overflow: ellipsis;
    overflow: hidden;
   justify-content: flex-start;
  }
  .back .img{
    background: url('../../static/images/map/searchbox.png') no-repeat 0 -263px;
    padding-left: 28px;
    height: 38px;
    width: 10px;
  }
  .detailList .content{
    background: #fff;
    box-shadow: 1px 2px 1px rgba(0, 0, 0, .15);
    border-radius: 2px 0 0 2px;
    width: 100%;
    overflow-y: scroll;
    max-height: 485px;
  }
  .detailList .title{
    background: #3385ff;
    color: #fff;
    font-weight: bold;
    padding: 10px 0;
    cursor: pointer;
  }
  .detailList .title .hideHis{
    color: #fff;
  }
  .detailList .content .line{
    border-bottom: 2px solid #f2f2f2;
    line-height: 25px;
    padding: 5px 0;
    margin: 0 20px;
    text-align: left;
  }
  .detailList .content .line .bold{
    font-weight: bold;
  }
  /*标准地址*/
  .detailList .content .standard{
    border:none;
  }
  .standardCtx{
    width: 90%;
    border: 1px solid #f2f2f2;
    margin: 0 auto;
  }
  .standardLine{
    height: 30px;
    border-bottom: 2px solid #f2f2f2;
  }
  .standardLine .label{
    background:#3385ff;
    width: 100px;
    height: 100%;
    color: #fff;
    flex-shrink: 0;
  }
  .standardLine .value{
    padding-left: 10px;
    width: 100%;
  }

  /*历史记录*/
  .hislist li .imgItem{
    background-image: url('../../static/images/map/his.png');
  }
  .hislist li:hover .imgItem{
    background-image: url('../../static/images/map/his_sel.png');
  }
</style>
