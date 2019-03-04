<template xmlns:v-bind="http://www.w3.org/1999/xhtml">

  <div class='baseMapWrapper'>
    <div id="baseMap"></div><!--地图-->

  </div>


</template>


<script>

  import * as maptalks from 'maptalks';
  //marker图标
  import marker from './red_cursor.png'
  export default{
    name: 'baseMap',
    data() {
      return {}
    },
    components: {},
    computed: {},
    props: {
      markerCoord:{
        type:Array
      }
    },
    created(){
    },
    mounted(){
      this.initMap();
    },
    methods: {
      //地图初始化
      initMap(){
        var $this = this;

        //地点marker
        var siteMarker = new maptalks.Marker([0,0],{
          cursor:'pointer',
          "symbol":{
            "markerFile"  : marker,
            "markerWidth" : 20
          },
        });
        var map = new maptalks.Map('baseMap', {
          center: [114.30, 30.52],
          zoom: 10,
          minZoom: 1,
          maxZoom: 19,
          spatialReference: {
            projection: 'baidu'
          },
          baseLayer: new maptalks.TileLayer('base', {
            'urlTemplate': 'http://online{s}.map.bdimg.com/onlinelabel/?qt=tile&x={x}&y={y}&z={z}&styles=pl&scaler=1&p=1',
            'subdomains': [0, 1, 2, 3, 4, 5, 6, 7, 8, 9],
          }),
          attribution:false,
          zoomControl : true, // add zoom control
          scaleControl : true, // add scale control
          layers : [
            new maptalks.VectorLayer('markerLayer', [siteMarker])
          ]
        });
        //setCoordinates
        $this.baseMap = map;
        $this.siteMarker = siteMarker;


      },

    },
    beforedestroy(){
      if (this.baseMap) {
        this.baseMap.remove();
      }
    },
    watch: {
      //监听mark坐标变化
      markerCoord:{
       handler:function (val, oldVal) {
         this.baseMap.setCenter(val);//设置地图中心
         this.siteMarker.setCoordinates(val);//设置点坐标
       },
       immediate:false
       }
    }
  }
</script>

<style scoped>

  .baseMapWrapper {
    text-align: left;
    width: 100%;
    height: 100%;
    top: 0;
    left: 0;
    position: absolute;
    z-index: 100;
  }

  #baseMap {
    width: 100%;
    height: 100%;
  }
</style>
