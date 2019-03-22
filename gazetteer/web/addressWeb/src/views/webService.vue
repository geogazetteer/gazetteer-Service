<template>
  <div class="wrapper">
    <h1 class="title">深圳市龙华区地名地址服务</h1>
    <Table
      :columns="columns" :data="data"
      stripe
    ></Table>
  </div>
</template>

<script>

export default {
  name: 'webService',
  components: {
  },
  created(){

  },
  data () {
    return {
      columns: [
        {
          title:' ',
          key:'id',
          width:30,
          fixed:true
        },
        {
          title: '服务名称',
          key: 'name',
		  className:'header'
        },
        {
          title: '服务描述',
          key: 'describe',
		  className:'header'
        },
        {
          title: '服务地址URL',
          key: 'url',
		  className:'header'
        }
      ],
      data: [
        {
          id:1,
          name: '地名地址服务',
          describe: '深圳市龙华区地名地址服务',
          url: '/address/services',
        },
        {
          id:2,
          name: '综合搜索',
          describe: '综合查询数据',
          url: '/address/select',
        },
        {
          id:3,
          name: '根据建筑物Code查询信息',
          describe: '实现根据建筑物Code查询相关建筑物信息的服务',
          url: '/address/selectByCodes/{code}',
        },
        {
          id:4,
          name: '根据建筑物Address查询信息',
          describe: '实现根据建筑物Address查询相关建筑物信息的服务',
          url: '/address/selectByAddress/{address}',
        }
      ]
    }
  },
  methods:{

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
</style>
