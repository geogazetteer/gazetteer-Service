/*
*地图组件
*/
<template xmlns:v-bind="http://www.w3.org/1999/xhtml">

  <div class='baseMapWrapper'>
    <div id="map"></div>
    <div style="position: absolute;bottom: 10px;left:55px;color:blue;font-size: 16px" id="coords"></div>

    <!--选中地图点的浮云-->
    <div id="pointPopup" class="pointPopup">
      <div class="inner flex_col">
        <div class="close" @click.stop="closePop">
          <Icon type="md-close" size="20"/>
        </div>

        <div class="coords">
          <p class="label">当前位置：</p>
          <p>{{pointCoords[0]}}</p>
          <p>{{pointCoords[1]}}</p>
        </div>
        <div class="search">
          <Button type="primary" shape="circle" icon="ios-search" size="small" @click.stop='search'>搜索</Button>
        </div>
        <div class="arrow"></div>
      </div>
    </div>

    <!--加载中-->
    <Spin fix size="large" v-if="showLoading" style="background-color: rgba(255,255,255,.35);"></Spin>
  </div>


</template>


<script>

  import {InitOlVecMap, InitMapboxMap} from '../js/render.js'
  import { mapGetters,mapActions } from 'vuex'
  import mapboxgl from 'mapbox-gl'
  export default {
    name: 'baseMap',
    data() {
      return {
        showLoading:false,
        pointCoords:[],//选中地图点的浮云坐标
      }
    },
    components: {},
    computed: {
      ...mapGetters(['canSelPoint']) ,// 动态计算属性
    },
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
      ...mapActions( //mapActions需要写在methods里面
        ['updateSearchPointCoordArr']),// 取得这些方法
      //地图初始化
      initMap() {
        var $this = this;

        $this.showLoading=true;
        //初始化房屋面
        InitMapboxMap('map', {
          zoom:13,
          onLoad(map) {
            //添加房屋面资源
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
              document.getElementById('coords').innerHTML=e.lngLat.lng+';'+e.lngLat.lat;
            });

            map.on('click',function (e) {
             if($this.canSelPoint){
               $this.marker.setLngLat([e.lngLat.lng,e.lngLat.lat]);
               $this.pointCoords=[e.lngLat.lng,e.lngLat.lat]
             }
            });
            $this.showLoading=false

          },
          onSuccess(map){
            map.addControl(new mapboxgl.NavigationControl({showCompass:false}),'bottom-left');
            var marker = new mapboxgl.Marker({
              element:document.getElementById('pointPopup')
            })
              .setLngLat([0,0])
              .addTo(map);
            $this.marker=marker
          }
        })
      },
      //关闭浮云
      closePop(){
        this.marker.setLngLat([0,0]);
        this.pointCoords=[]
      },
      //搜索坐标对应的位置
      search(){
        this.updateSearchPointCoordArr(this.pointCoords);
      }

    },
    beforedestroy() {
      if (this.baseMap) {
        this.baseMap.remove();
      }
    },
    watch: {
      //监听mark坐标变化
      /*markerCoord: {
        handler: function (val, oldVal) {
          this.baseMap.setCenter(val);//设置地图中心
          this.siteMarker.setCoordinates(val);//设置点坐标
        },
        immediate: false
      }*/
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
  .pointPopup{
    background: #fff;
    box-shadow: 1px 2px 1px rgba(0,0,0,.15);
    border-radius: 3px;
    width: 200px;
    height: 110px;
    padding: 10px;
  }
  .pointPopup .inner{
    width: 100%;
    height: 100%;
  }
  .pointPopup .close{
    align-self: flex-end;
    flex-shrink: 0;
  }
  .pointPopup .coords{
    height: 100%;
    align-self: flex-start;
    width: 100%;
  }
  .pointPopup .coords .label{
       font-weight: bold;
     }
  .pointPopup .search{
    align-self: flex-end;
    flex-shrink: 0;
  }
  .pointPopup .arrow{
    width: 0;
    height: 0;
    border-style: solid;
    border-width: 8px 6px 0;
    border-color: #fff transparent transparent;
    position: absolute;
    bottom: -8px;
    left: 100px;
  }
</style>
