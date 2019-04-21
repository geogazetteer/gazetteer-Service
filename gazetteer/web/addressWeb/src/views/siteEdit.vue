/*
*siteSearch 标准地名搜索页
*/

<template>
  <div class="editWrapper">
    <!--编辑框-->
    <edit-box  class="leftView"></edit-box>

  </div>
</template>

<script>
  import editBox from '../components/editBox.vue'
export default {
  name: 'home',
  components: {
    editBox
  },
  created(){
  },
  data () {
    return {
    }
  },
  methods:{
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.editWrapper{
  width: 100%;
  height: 100%;
  overflow-y: hidden;
}
.leftView {
  position: absolute;
  left: 20px;
  z-index: 100;
}


</style>
