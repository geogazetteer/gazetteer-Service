<template xmlns:v-bind="http://www.w3.org/1999/xhtml">
  <ul>
    <li v-if="iconIndex==0">
      <Icon type="md-search"  size="40" color="#3385ff"/><!--标准地名搜索选中-->
    </li>
    <li v-if="iconIndex==1">
      <Icon type="ios-search-outline"  @click="toggleRouter(0)" size="40" color="#3385ff"/>
    </li>
    <li v-if="iconIndex==1">
      <Icon type="ios-create"  size="40" color="#3385ff"/><!--编辑选中-->
    </li>
    <li v-if="iconIndex==0">
      <Icon type="ios-create-outline" @click="toggleRouter(1)"  size="40" color="#3385ff"/>
    </li>
  </ul>

</template>


<script>
  export default{
    name: 'toggleBox',
    data() {
      return {
        iconIndex:0
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
      toggleRouter(index){
        this.iconIndex = index;
        if(index==0){
          this.$router.push("siteSearch");//跳转地名搜索
        }else{
          this.$router.push("siteEdit");//跳转
        }
      }
    },
    beforedestroy(){

    },
    watch: {
    }
  }
</script>

<style scoped>
</style>
