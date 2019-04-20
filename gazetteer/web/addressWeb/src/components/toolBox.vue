<template xmlns:v-bind="http://www.w3.org/1999/xhtml">
  <div class="toolWrapper">

    <div class="tools" v-if="isOpen">
      <!--上层，繁体，半角，数字化-->
      <div class="upper">
        <CheckboxGroup v-model="setModal_1" @on-change="getModal">
          <Checkbox v-for="(s,index) in settings_1" :label="s" :title="'开启'+s" :key="index"></Checkbox>
        </CheckboxGroup>
      </div>
      <!--下层，别名，同音字，通假词，同义词-->
      <div class="down">
        <CheckboxGroup v-model="setModal_2" @on-change="getModal">
          <Checkbox v-for="(s,index) in settings_2" :label="s" :title="'开启'+s" :key="index"></Checkbox>
        </CheckboxGroup>
      </div>
      <div class="down">
        <CheckboxGroup v-model="setModal_3" @on-change="getModal">
          <Checkbox v-for="(s,index) in settings_3" :label="s" :title="'开启'+s" :key="index"></Checkbox>
        </CheckboxGroup>
      </div>

      <div class="down">
        <CheckboxGroup v-model="setModal_4" @on-change="getModal">
          <Checkbox v-for="(s,index) in settings_4" :label="s" :title="'开启'+s" :key="index"></Checkbox>
        </CheckboxGroup>
      </div>

      <!--加载中-->
      <Spin fix size="large" v-if="showLoading"></Spin>
    </div>

    <!--按钮-->
    <div class="iconWrap flex_col">
      <Icon type="md-settings" size="24" title='设置搜索条件' color="#3385ff" v-if="isOpen" @click="toggleTool(0)"/>
      <Icon type="md-settings" size="24" title='设置搜索条件' color="#989898" v-if="!isOpen" @click="toggleTool(1)"/>
    </div>



  </div>
</template>


<script>
  import { mapActions } from 'vuex'
  export default{
    name: 'toolBox',
    data() {
      return {
        isOpen:false,
        settings_1:['繁体转换','全角转换','数字转换'],
        settings_2:['别名识别','同音字识别','通假字识别'],
        settings_3:['地址补全','地名','地名别名','POI','POI别名'],
        settings_4:['地址范围识别','坐标识别'],

        setModal_1:[],//选中状态1
        setModal_2:[],//选中状态2
        setModal_3:[],//选中状态2
        setModal_4:[],//选中状态2,

        showLoading:false
      }
    },
    components: {},
    computed: {
      //...mapGetters(['isOpenCoordinate']) // 动态计算属性，相当于this.$store.getters.isOpenCoordinate
    },
    props: {

    },
    created(){
      //将所有搜索设置为false
      var setObj={
        "addressAlias": false,
        "chineseNumber": false,
        "completed": false,
        "complexChar": false,
        "coordinates": false,
        "fullChar": false,
        "geoName": false,
        "geoNameAlias": false,
        "interchangeable": false,
        "ishomophone": false,
        "poi": false,
        "poialias": false,
        "withtin": false
      };
      this.$api.setSettings(setObj)
    },
    mounted(){

    },
    methods: {
      ...mapActions( //mapActions需要写在methods里面
        ['updateIsOpenCoordinate'] // 取得这些方法
      ),
      //打开工具箱
      toggleTool(needOpen){
        if(needOpen){
          this.isOpen=true
        }else{
          this.isOpen=false
        }
      },
      //改变工具箱选项
      getModal(){

        var $this = this;
        $this.showLoading=true;//loading
        var setArr=$this.setModal_1.concat($this.setModal_2).concat($this.setModal_3).concat($this.setModal_4)
        var setObj={
          "addressAlias": false,
          "chineseNumber": false,
          "completed": false,
          "complexChar": false,
          "coordinates": false,
          "fullChar": false,
          "geoName": false,
          "geoNameAlias": false,
          "interchangeable": false,
          "ishomophone": false,
          "poi": false,
          "poialias": false,
          "withtin": false
        };
        for (var i=0;i<setArr.length;i++){
          setObj[SETDICT[setArr[i]]]=true
        }
        $this.$api.setSettings(setObj).then(function (res) {
          $this.showLoading=false;//loading
        })

        //vuex传值，存储是否开启了坐标识别
        if(setArr.indexOf('坐标识别')!=-1){
          $this.updateIsOpenCoordinate(true)
        }else{
          $this.updateIsOpenCoordinate(false)
        }

      }
    },
    beforedestroy(){

    },
    watch: {
      /*showEdit: {
        handler: function (val, oldVal) {
          debugger
          if(val){
            this.showEditModal = val;
            this.$emit('onEditHasOpen');
          }
        },
        immediate:false*/
      }

  }
</script>

<style scoped>
.iconWrap{
  width: 34px;
  height: 34px;
  box-shadow: 1px 2px 1px rgba(0,0,0,.15);
  cursor: pointer;
  border-radius: 2px;
  background: #fff;
}
  .tools{
    margin-top: 20px;
    margin-right: 10px;
    background: #fff;
    padding: 2px 10px;
    -webkit-box-shadow: 1px 2px 1px rgba(0,0,0,.15);
    box-shadow: 1px 2px 1px rgba(0,0,0,.15);
    border-radius: 2px;
    position: relative;
  }
</style>
