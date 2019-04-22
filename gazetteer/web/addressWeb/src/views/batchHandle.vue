
<!--批量处理页-->
<template>
  <div class="Wrapper">
    <h1 class="title">批量地址匹配服务</h1>
    <div class="topTool flex_row">
      <div class="btnBox flex_row">
        <a :href="excelUrl" class="item">
          <Button icon="md-download" type="primary" >下载模板</Button>
        </a>


        <Upload :action="upLoadUrl" class="flex_row" ref="upload"
                name="fileName"
                accept="file"
                :format="['xls','xlsx']"
                :before-upload="beforeUpload"
                :on-success="onUpSuccess"
                :on-error="onUpError"
        >
          <Button  class="item" icon="md-arrow-round-up" type="primary">导入</Button>

        </Upload>

      </div>

      <div class="right flex_row">
        <div class="label">匹配进度：</div>
        <Progress :percent="progressNum" status="active"/><!--进度条-->
        <div class="item">
          <Button icon="ios-redo" type="primary" @click="exportData">导出</Button>
        </div>
        <!--<Icon type="md-refresh-circle" color="#3385ff" size="32" class="refresh" title="刷新"/>&lt;!&ndash;刷新&ndash;&gt;-->
      </div>

    </div>



    <Table ref="table"
      :columns="columns" :data="data"
      stripe border max-height="80%"
    >

      <template slot-scope="{ row }" slot="fuzzy">
        <Progress :percent="row.fuzzy" status="active"/><!--相似度-->
      </template>
    </Table>


    <Spin fix v-if="showLoading">
      <Icon type="ios-loading" size=18 class="demo-spin-icon-load"></Icon>
      <div>上传中……</div>
    </Spin>

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
      showLoading:false,
      excelUrl:encodeURI(BATCHSERVICE['modelUrl']),//批量下载excel模板
      columns: [
        {
          title:' ',
          width:50,
          type: 'selection'
        },
        {
          title: '序号',
          key: 'uid',
		      width:70,
          align:'center'
        },
        {
          title: '地址',
          key: 'address',
		  className:'header'
        },
        {
          title: '经度',
          key: 'lng',
          className:'header'
        },
        {
          title: '纬度',
          key: 'lat',
		  className:'header'
        }
      ],
      data: [
        {
          uid:1,
          address: '深圳市龙华区地名地址服务',
          lng:'114.30',
          lat:'30.52',
        },
      ],

      upLoadUrl:BATCHSERVICE['uploadUrl'],//上传url
      progressNum:100,//进度条
    }
  },
  methods:{
    exportData(){
      this.$refs.table.exportCsv({
        filename: ''
      });
    },

    //导入前
    beforeUpload(file){
      var $this = this;

      var nameArr=file.name.split('.');
      if(nameArr[nameArr.length-1]=='xls'||nameArr[nameArr.length-1]=='xlsx'){
        //判断上传文件的类型是excel后缀
        $this.showLoading=true
        //清空上传列表
        $this.$refs.upload.clearFiles();
      }else{
        $this.$Modal.warning({
          title: '上传文件后缀应为.xls或.xlsx!',
          width:320
        });
        return false
      }

    },
    //导入成功
    onUpSuccess(response, file, fileList) {
      this.showLoading=false;
      this.$Message.success(fileList[0].name+'上传成功!');

    },
    //导入失败
    onUpError(error, file, fileList){
      this.showLoading=false;
      this.$Message.error( fileList[0].name+'上传失败，请重试！');
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.Wrapper{
  width: 100%;
  height: 100%;
  position: relative;
  border: 2px solid #3385ff;
  background: #F6EBDD;
  padding: 0 4%;

}
  .title{
    margin-bottom: 20px;
    text-align: center;
    margin-top: 50px;
  }

.topTool{
  margin-bottom: 20px;
}
  .topTool .btnBox{
    flex-grow: 3;
    justify-content: flex-start;
  }
.topTool .btnBox .item{
  margin-right: 5px;
}
  .topTool .right{
    flex-grow: 1;
  }
.topTool .right .label
{
  flex-shrink: 0;
}
  .refresh{
    cursor: pointer;
  }

.demo-spin-icon-load{
  animation: ani-demo-spin 1s linear infinite;
}
@keyframes ani-demo-spin {
  from { transform: rotate(0deg);}
  50%  { transform: rotate(180deg);}
  to   { transform: rotate(360deg);}
}
</style>
