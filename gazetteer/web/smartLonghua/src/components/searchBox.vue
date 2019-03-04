<template xmlns:v-bind="http://www.w3.org/1999/xhtml">
  <div  class="leftView">
    <!--搜索框-->
    <div class="searchbox flex_row">

      <div  class="searchbox-content flex_row">
        <input id="sole-input"  type="text" autocomplete="off"
               maxlength="256" placeholder="输入地名进行搜索" v-model="searchContent"
               v-on:input="searchItems" v-on:keyup.enter="searchItems" @blur="" @focus="searchFocus"
        />

        <div class="input-clear" title="清空" v-show="searchContent" @click="clearInput"></div>

      </div>

      <button id="search-button" data-title="搜索" @click="searchItems"></button>
    </div>

    <!--搜索结果-->
    <!--loading spin-->
    <div v-if="needSpin" class="spin-icon-load">
      <Spin fix>
        <Icon type="ios-loading" size=18 class="demo-spin-icon-load"></Icon>
        <div>搜索中……</div>
      </Spin>
    </div>

    <ul class="cardlist" v-if="showCard">
      <li v-for="(ca,index) in cardlist"  @click="posSite(ca.value,ca.coord)" class="flex_row">
        <span class="imgItem"></span>
        <span>{{ca.value}}</span>
      </li>
      <!--没有搜索结果-->
      <div v-if="cardlist.length==0" class="bottomDiv">
        <!--<span class="imgItem">-->
            <!--<img src="/static/images/map/search.png">-->
        <!--</span>-->
        <span>--没有搜索结果--</span>
      </div>
    </ul>
    <!--历史记录-->
    <ul class="cardlist" v-if="showHis">
      <li v-for="(ca,index) in hisList"  @click="posSite(ca.value,ca.coord)" class="flex_row">
        <span class="imgItem"></span>
        <span>{{ca.value}}</span>
      </li>
      <div v-if="hisList.length>0" @click="clearHis" class="bottomDiv flex_row">
        <!--<span class="imgItem">-->
            <!--<img src="/static/images/map/search.png">-->
        <!--</span>-->
        <div class="hisBottom">--清空历史记录--</div>
        <div @click="hideHis" class="hideHis">▲</div>
      </div>
    </ul>
  </div>

</template>


<script>
  export default{
    name: 'searchBox',
    data() {
      return {
        searchContent:'',
        //搜索结果
        needSpin:false,//显示spin
        showCard:false,
        cardlist:[
          {id:0,value:'武汉市洪山区广八路',coord:[113.30,30.52]},
          {id:1,value:'武汉市洪山区广埠屯路',coord:[112.90,20,19]},
          {id:2,value:'武汉市洪山区八一路路',coord:[114.30,30.52]},
          {id:3,value:'武汉市洪山区广八路',coord:[114.90,34.66]},
          {id:4,value:'武汉市洪山区广埠屯路',coord:[112.90,90.21]},
          {id:5,value:'武汉市洪山区八一路路',coord:[114.4234,32.45]},
          {id:6,value:'武汉市洪山区广八路',coord:[111.23,40.32]},
          {id:7,value:'武汉市洪山区广埠屯路',coord:[114.78,45.43]},
          {id:8,value:'武汉市洪山区八一路路',coord:[113.56,36.67]},
          {id:9,value:'武汉市洪山区广八路',coord:[113.76,45.32]},
          {id:10,value:'武汉市洪山区广埠屯路',coord:[111.234,34.233]},
          {id:11,value:'武汉市洪山区八一路路',coord:[110.233,23.223]},
          {id:12,value:'武汉市洪山区广八路',coord:[110.315,30.15]},
          {id:13,value:'武汉市洪山区广埠屯路',coord:[113.22,52]},
          {id:14,value:'武汉市洪山区八一路路',coord:[113.62,50.9]},
          {id:15,value:'武汉市洪山区广八路',coord:[114.12,34.82]},
          {id:16,value:'武汉市洪山区广埠屯路',coord:[114.12,43.12]},
          {id:17,value:'武汉市洪山区八一路路',coord:[110.24,124]},
        ],
        //历史记录
        showHis:false,
        hisList:[
          {id:0,value:'历史记录1',coord:[110.315,30.15]},
          {id:1,value:'历史记录2',coord:[114.78,45.43]},
          {id:2,value:'历史记录3',coord:[114.30,30.52]},
          {id:3,value:'历史记录4',coord:[110.24,124]},
          {id:4,value:'历史记录5',coord:[114.77,30.7]}
        ],
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
      //清空搜索框
      clearInput(){
        this.searchContent='';
        this.showCard=false;
        this.showHis = false;
        this.needSpin=false;
      },
      //搜索结果
      searchItems(){
        if(this.searchContent){
//          this.needSpin = true;
          this.showHis = false;
          this.showCard = true;
        }else{
          this.showCard=false;
          this.showHis = true;
        }
      },
      //搜索框获取焦点
      searchFocus(){
        if(!this.searchContent){
          this.showHis = true;
          this.showCard=false;
        }
      },
      //失去焦点时清空结果
      searchBlur(){
        this.searchContent='';
        this.showCard=false;
        this.showHis = false;
        this.needSpin=false;
      },
      //收起历史记录
      hideHis(){
        this.showHis = false;
      },
      //清空历史记录
      clearHis(){},
      //定位搜索结果位置
      posSite(val,coord){
        this.searchContent=val;
        this.$emit('setMarkCoord',coord)
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
  .leftView {
    position: absolute;
    left: 20px;
    top: 20px;
    z-index: 100;
  }

  .searchbox .searchbox-content {
    pointer-events: auto;
    width: 320px;
    height: 38px;
    box-sizing: border-box;
    box-shadow: 1px 2px 1px rgba(0, 0, 0, .15);
    border-radius: 2px 0 0 2px;
    background: #fff;
    justify-content: flex-start;
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
  .spin-icon-load{
    background: #fff;
    width: 377px;
    position: relative;
    height: 50px;
    border-top: 1px solid #f2f2f2;
  }
  .cardlist {
    border-top: 1px solid #f2f2f2;
    height: auto;
    max-height: 396px;
    /*overflow-y: scroll;*/
    overflow-x: hidden;
    width: 377px;
    background: #fff;
    padding-bottom: 12px;
  }

  .cardlist li {
    cursor: pointer;
    justify-content: flex-start;
    padding: 10px;
    font-size: 14px;
  }
  .cardlist li:hover{
    background: #3385ff;
    color: #fff;
  }
  .cardlist li .imgItem {
    margin-right: 10px;
    background-image:url('../../static/images/map/search.png') ;
    width: 14px;
    height: 14px;
    background-size: cover;
  }

  .cardlist li:hover .imgItem {
    background-image:url('../../static/images/map/search_w.png') ;
  }
  .bottomDiv {
    font-size: 14px;
    padding-top: 10px;
    border-top: 1px solid #f2f2f2;
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
</style>
