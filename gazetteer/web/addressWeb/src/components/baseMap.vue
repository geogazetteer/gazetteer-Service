/*
*地图组件
*/
<template xmlns:v-bind="http://www.w3.org/1999/xhtml">

  <div class='baseMapWrapper'>
    <div id="map">
     <!-- <div class="ol-viewport" data-view="3"
           style="position: relative; overflow: hidden; width: 100%; height: 100%; touch-action: none;">
        &lt;!&ndash;<canvas class="ol-unselectable" width="1161" height="517"&ndash;&gt;
                &lt;!&ndash;style="width: 100%; height: 100%; display: block;"></canvas>&ndash;&gt;
        &lt;!&ndash;<div class="ol-overlaycontainer"></div>&ndash;&gt;
        <div class="ol-overlaycontainer-stopevent">

          <div class="ol-mouse-position">114.02729272842407,22.650740146636963</div>
          &lt;!&ndash;<div id="scale" class="ol-scale-value">Scale = 1 : 17K</div>&ndash;&gt;
        </div>
      </div>-->
    </div>

    <!--<div id="baseMap"></div>&lt;!&ndash;地图&ndash;&gt;-->
    <div style="position: absolute;bottom: 10px;left:10px;color:blue;font-size: 20px" id="coords"></div>
  </div>


</template>


<script>

  import {InitOlVecMap,InitMapboxMap} from '../js/render.js'

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

        //初始化房屋面
        //InitOlVecMap();
        InitMapboxMap()
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

  #map {
    width: 100%;
    height: 100%;
  }

  .ol-scale-value {top: 24px; right: 8px; position: absolute; }
</style>
