/*
*地图组件
*/
<template xmlns:v-bind="http://www.w3.org/1999/xhtml">

  <div class='baseMapWrapper'>
    <div id="map"></div>
    <!--<div style="position: absolute;bottom: 10px;left:10px;color:blue;font-size: 20px" id="coords"></div>-->

    <!--加载中-->
    <Spin fix size="large" v-if="showLoading" style="background-color: rgba(255,255,255,.35);"></Spin>
  </div>


</template>


<script>

  import {InitOlVecMap, InitMapboxMap} from '../js/render.js'
  import {MAPURL} from "../../static/config.js";

  export default {
    name: 'baseMap',
    data() {
      return {
        showLoading:false
      }
    },
    components: {},
    computed: {},
    props: {
      markerCoord: {
        type: Array
      }
    },
    created() {
    },

    mounted() {
      this.initMap();
    },
    methods: {
      //地图初始化
      initMap() {
        var $this = this;

        $this.showLoading=true;
        //初始化房屋面
        InitMapboxMap('map', {
          onLoad(map) {
            //添加资源
            map.addSource('longhua', {
              //龙华房屋面
              "type": "vector",
              "scheme": "tms",
              "tiles": [MAPURL['building_vec_tile']],
            });
            //添加房屋面图层
            map.addLayer(
              //房屋面
              {
                "id": "border",
                "type": "line",
                "source-layer": "LH_building_4490",
                "source": "longhua",
                "layout": {
                  "line-join": "round",
                  "line-cap": "round"
                },
                "paint": {
                  "line-color": '#5A92D9',
                  "line-width": 2
                },
              });

            map.addLayer({
              "id": "region",
              "type": "fill",
              "source-layer": "LH_building_4490",
              "source": "longhua",
              "layout": {},
              "paint": {
                'fill-opacity': 0
              },
            });

            map.on('click', 'region', function (e) {
              var p = e.features[0].properties;

              console.log(JSON.stringify(p))
            });
            map.on('mousemove', function (e) {
              //document.getElementById('coords').innerHTML='缩放层级：'+map.getZoom()+'</br>'+e.lngLat.lng+';'+e.lngLat.lat
            });

            $this.showLoading=false

          }
        })
      },

    },
    beforedestroy() {
      if (this.baseMap) {
        this.baseMap.remove();
      }
    },
    watch: {
      //监听mark坐标变化
      markerCoord: {
        handler: function (val, oldVal) {
          this.baseMap.setCenter(val);//设置地图中心
          this.siteMarker.setCoordinates(val);//设置点坐标
        },
        immediate: false
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

  .ol-scale-value {
    top: 24px;
    right: 8px;
    position: absolute;
  }
</style>
