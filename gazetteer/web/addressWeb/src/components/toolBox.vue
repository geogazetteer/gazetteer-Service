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
    </div>

    <!--按钮-->
    <div class="iconWrap flex_col">
      <Icon type="md-settings" size="24" title='设置搜索条件' color="#3385ff" v-if="isOpen" @click="toggleTool(0)"/>
      <Icon type="md-settings" size="24" title='设置搜索条件' color="#989898" v-if="!isOpen" @click="toggleTool(1)"/>
    </div>
  </div>
</template>


<script>
  export default{
    name: 'toolBox',
    data() {
      return {
        isOpen:false,
        settings_1:['繁体转换','全角转换','数字转换'],
        settings_2:['别名识别','同音字识别','通假字识别','同义词识别'],

        setModal_1:[],//选中状态1
        setModal_2:[],//选中状态2
      }
    },
    components: {},
    computed: {},
    props: {

    },
    created(){
      this.setModal_1=this.settings_1;
      this.setModal_2=this.settings_2;
    },
    mounted(){

    },
    methods: {
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
        console.log(this.setModal_1.concat(this.setModal_2))
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
    margin-right: 10px;
    background: #fff;
    padding: 2px 10px;
    -webkit-box-shadow: 1px 2px 1px rgba(0,0,0,.15);
    box-shadow: 1px 2px 1px rgba(0,0,0,.15);
    border-radius: 2px;
  }
</style>
