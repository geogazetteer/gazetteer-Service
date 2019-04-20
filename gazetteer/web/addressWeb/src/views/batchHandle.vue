
<!--批量处理页-->
<template>
  <div class="wrapper">
    <h1 class="title">批量处理</h1>
    <div class="btnBox">
      <Button  icon="md-add" type="primary">导入</Button>
      <Button  icon="md-download" type="primary" @click="exportData">导出</Button>
      <Progress :percent="25" />
      <Icon type="md-refresh-circle" color="#3385ff" size="32"/><!--刷新-->
    </div>
    <Table ref="table"
      :columns="columns" :data="data"
      stripe border max-height="80%"
    ></Table>
  </div>
</template>

<script>

export default {
  name: 'batchHandle',
  components: {
  },
  created(){

  },
  data () {
    return {
      columns: [
        {
          title:' ',
          width:50,
          type: 'selection'
        },
        {
          title: '序号',
          key: 'uid',
		  className:'header'
        },
        {
          title: '编码方式',
          key: 'encode',
		  className:'header'
        },
        {
          title: '匹配结果',
          key: 'name',
		  className:'header'
        },
        {
          title: '创建时间',
          key: 'time',
          className:'header'
        },
        {
          title: '操作',
          key: '',
          className:'handle'
        }
      ],
      data: [
        {
          uid:1,
          encode:'utf-8',
          name: '深圳市龙华区地名地址服务',
          describe: '深圳市龙华区观澜街道a栋1806',
          time: '2019-04-20 23:34',
        },
      ]
    }
  },
  methods:{
    exportData(){
      this.$refs.table.exportCsv({
        filename: ''
      });
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.wrapper{
  width: 90%;
  height: 100%;
  margin: 0 auto;
}
  .title{
    margin-bottom: 20px;
  }
.wrapper .header{
	font-weight: bold;
    font-size: 16px;
  }
  .btnBox{
    margin-bottom: 20px;
  }
</style>
