/**
 * Created by clover on 2019/3/12.
 */

/**
 * 用正则表达式处理数据
  * @param str
  * @param type Number 1:去除空格
 * @constructor
 */
var RegularStr = function(str,type){
  type=type||1;
  switch (type){
    //第1类，去除空格
    case 1:
      return str.replace(/\s+/g,"");
      break
  }

};



/**
 * 插入搜索历史记录
 * @param item 新输入的内容
 * @returns {searchRecord，历史记录数组}
 */
function SetRecord(item) {
  var searchRecord = localStorage.getItem("searchRecord");
  if(searchRecord){
    searchRecord = JSON.parse(searchRecord)['records'];
    //历史记录中有重复项目时，删除重复项目
    for (var i = 0; i < searchRecord.length; i++) {
      if (item == searchRecord[i]) {
        //删除重复项目
        searchRecord.splice(i, 1);
        break;
      }
    }
    //历史记录已达到最大值10个时，删除最老的
    if (searchRecord.length == 10) {
      searchRecord.splice(0, 1);
    }
    //插入新输入的记录
    searchRecord.push(item);
  }else{
    //没有历史记录时直接插入记录
    searchRecord=[item];
  }
  //存储到内存
  localStorage.setItem("searchRecord",JSON.stringify({records:searchRecord}))

  return searchRecord.reverse();//reverse按从新到老排序
}
/**
 * 获取所有的历史记录
 * @returns {历史记录array}
 */
function GetRecord() {
  var searchRecord = localStorage.getItem("searchRecord");
  if(searchRecord) {
    //有历史记录时，返回记录数组,reverse使数组从新到老排序
    return JSON.parse(localStorage.getItem("searchRecord"))['records'].reverse();
  }else{
    //没有记录时，返回空
    return []
  }
}
/**
 * 清空搜索历史
 */
function ClearRecord() {
  localStorage.removeItem("searchRecord")
}

function InitOlVecMap(){
  /*function ScaleControl(opt_options) {
    var options = opt_options || {};

    var element = document.createElement('div');
    element.setAttribute('id', 'scale');
    element.className = 'ol-scale-value';

    ol.control.Control.call(this, {
      element: element,
      target: options.target
    });

  };*/
  //ol.inherits(ScaleControl, ol.control.Control);
/*  ScaleControl.prototype.setMap = function(map) {
    map.on('postrender', function() {
      var view = map.getView();
      var resolution = view.getResolution();
      var dpi = 90.71428571428572;
      var mpu = map.getView().getProjection().getMetersPerUnit();
      var scale = resolution * mpu * 39.37 * dpi;

      if (scale >= 9500 && scale <= 950000) {
        scale = Math.round(scale / 1000) + 'K';
      } else if (scale >= 950000) {
        scale = Math.round(scale / 1000000) + 'M';
      } else {
        scale = Math.round(scale);
      }
      document.getElementById('scale').innerHTML =  'Scale = 1 : ' + scale;
    }, this);
    ol.control.Control.prototype.setMap.call(this, map);
  }*/

  var gridsetName = 'CGCS2000_512';
  var gridNames = ['CGCS2000_512:0', 'CGCS2000_512:1', 'CGCS2000_512:2', 'CGCS2000_512:3', 'CGCS2000_512:4', 'CGCS2000_512:5', 'CGCS2000_512:6', 'CGCS2000_512:7', 'CGCS2000_512:8', 'CGCS2000_512:9', 'CGCS2000_512:10', 'CGCS2000_512:11', 'CGCS2000_512:12', 'CGCS2000_512:13', 'CGCS2000_512:14', 'CGCS2000_512:15', 'CGCS2000_512:16', 'CGCS2000_512:17'];
  var baseUrl = 'http://119.3.72.23:8085/geoserver/gwc/service/wmts';
  var style = '';
  var format = 'application/vnd.mapbox-vector-tile';
  var infoFormat = 'text/html';
  var layerName = 'gazetteer:LH_building_4490';
  var projection = new ol.proj.Projection({
    code: 'EPSG:4490',
    units: 'degrees',
    axisOrientation: 'neu'
  });
  var resolutions = [0.3515625, 0.17578125, 0.087890625, 0.0439453125, 0.02197265625, 0.010986328125, 0.0054931640625, 0.00274658203125, 0.001373291015625, 6.866455078125E-4, 3.433227539062E-4, 1.716613769531E-4, 8.58306884766E-5, 4.29153442383E-5, 2.14576721191E-5, 1.07288360596E-5, 5.3644180298E-6, 2.6822090149E-6];
  var params = {
    'REQUEST': 'GetTile',
    'SERVICE': 'WMTS',
    'VERSION': '1.0.0',
    'LAYER': layerName,
    'STYLE': style,
    'TILEMATRIX': gridsetName + ':{z}',
    'TILEMATRIXSET': gridsetName,
    'FORMAT': format,
    'TILECOL': '{x}',
    'TILEROW': '{y}'
  };

  function constructSource() {
    var url = baseUrl+'?'
    for (var param in params) {
      url = url + param + '=' + params[param] + '&';
    }
    url = url.slice(0, -1);
    var source = new ol.source.VectorTile({
      url: url,
      format: new ol.format.MVT({}),

      tileGrid: new ol.tilegrid.WMTS({
        tileSize: [512,512],
        origin: [-180.0, 90.0],
        resolutions: resolutions,
        matrixIds: gridNames
      }),
      wrapX: true
    });
    return source;
  }


  //添加天地图
  //http://t0.tianditu.gov.cn/vec_w/wmts
  var tian_di_tu_road_layer = new ol.layer.VectorTile({
    //title: "天地图路网",
    source:new ol.source.VectorTile({
      url: 'http://t0.tianditu.gov.cn/vec_w/wmts?T=vec&x={x}&y={y}&l={z}&tk=b0ac8e412f12c8e709241af20d5c93ec',
      format: new ol.format.MVT({}),

      tileGrid: new ol.tilegrid.WMTS({
        tileSize: [512,512],
        origin: [-180.0, 90.0],
        resolutions: resolutions,
        matrixIds: gridNames
      }),
      wrapX: true
    })
    /*source: new ol.source.XYZ({
      url: "http://t0.tianditu.gov.cn/vec_w/wmts?T=vec&x={x}&y={y}&l={z}&tk=b0ac8e412f12c8e709241af20d5c93ec",
      projection:projection
    })*/
  });
  var tian_di_tu_annotation = new ol.layer.VectorTile({
    //title: "天地图文字标注",
    source:new ol.source.VectorTile({
      url: 'http://t0.tianditu.gov.cn/cva_c/wmts?T=cva&x={x}&y={y}&l={z}&tk=b0ac8e412f12c8e709241af20d5c93ec',
      format: new ol.format.MVT({}),

      tileGrid: new ol.tilegrid.WMTS({
        tileSize: [512,512],
        origin: [-180.0, 90.0],
        resolutions: resolutions,
        matrixIds: gridNames
      }),
      wrapX: true
    })
//
    /*source: new ol.source.XYZ({
      url: 'http://t0.tianditu.gov.cn/cva_c/wmts?T=cva&x={x}&y={y}&l={z}&tk=b0ac8e412f12c8e709241af20d5c93ec',
      projection: projection,
    })*/
  });

  var layer = new ol.layer.VectorTile({
    source: constructSource()
  });

/*  new ol.layer.VectorTile({
    style:simpleStyle,
    source: new ol.source.VectorTile({
      tilePixelRatio: 1, // oversampling when > 1
      tileGrid: ol.tilegrid.createXYZ({maxZoom: 19}),
      format: new ol.format.MVT(),
      url: '/geoserver/gwc/service/tms/1.0.0/' + layer +
        '@EPSG%3A'+projection_epsg_no+'@pbf/{z}/{x}/{-y}.pbf'
    })
  })*/

  var view = new ol.View({
    center: [0, 0],
    zoom: 2,
    projection: projection,
    extent: [-180.0,-90.0,180.0,90.0]
  });

  var map = new ol.Map({
    controls: ol.control.defaults({attribution: false}).extend([
      new ol.control.MousePosition(),
      //new ScaleControl()
    ]),
    layers: [tian_di_tu_road_layer,tian_di_tu_annotation,layer],
    target: 'map',
    view: view
  });





  map.getView().fit([113.96541405669348,22.589560879997443,114.10613043167055,22.767843427008312], map.getSize());

  window.setParam = function(name, value) {
    if (name == "STYLES") {
      name = "STYLE"
    }
    params[name] = value;
    layer.setSource(constructSource());
    map.updateSize();
  };

  map.on('singleclick', function(evt) {

    /*document.getElementById('info').innerHTML = '';

    var source = layer.getSource();
    var resolution = view.getResolution();
    var tilegrid = source.getTileGrid();
    var tileResolutions = tilegrid.getResolutions();
    var zoomIdx, diff = Infinity;

    for (var i = 0; i < tileResolutions.length; i++) {
      var tileResolution = tileResolutions[i];
      var diffP = Math.abs(resolution-tileResolution);
      if (diffP < diff) {
        diff = diffP;
        zoomIdx = i;
      }
      if (tileResolution < resolution) {
        break;
      }
    }
    var tileSize = tilegrid.getTileSize(zoomIdx);
    var tileOrigin = tilegrid.getOrigin(zoomIdx);

    var fx = (evt.coordinate[0] - tileOrigin[0]) / (resolution * tileSize[0]);
    var fy = (tileOrigin[1] - evt.coordinate[1]) / (resolution * tileSize[1]);
    var tileCol = Math.floor(fx);
    var tileRow = Math.floor(fy);
    var tileI = Math.floor((fx - tileCol) * tileSize[0]);
    var tileJ = Math.floor((fy - tileRow) * tileSize[1]);
    var matrixIds = tilegrid.getMatrixIds()[zoomIdx];
    var matrixSet = source.getMatrixSet();

    var url = baseUrl+'?'
    for (var param in params) {
      if (param.toUpperCase() == 'TILEMATRIX') {
        url = url + 'TILEMATRIX='+matrixIds+'&';
      } else {
        url = url + param + '=' + params[param] + '&';
      }
    }

    url = url
      + 'SERVICE=WMTS&REQUEST=GetFeatureInfo'
      + '&INFOFORMAT=' +  infoFormat
      + '&TileCol=' +  tileCol
      + '&TileRow=' +  tileRow
      + '&I=' +  tileI
      + '&J=' +  tileJ;

    if (url) {
      document.getElementById('info').innerHTML = 'Loading... please wait...';
      var xmlhttp = new XMLHttpRequest();    xmlhttp.onreadystatechange = function() {
        if (xmlhttp.readyState == XMLHttpRequest.DONE ) {
          if (xmlhttp.status == 200) {
            document.getElementById('info').innerHTML = xmlhttp.responseText;
          }
          else {
            document.getElementById('info').innerHTML = '';
          }
        }
      }
      xmlhttp.open('GET', url, true);
      xmlhttp.send();
    }*/
  });
}
import mapboxgl from 'mapbox-gl'
import {MAPURL} from '../../static/config'
/**
 * Created by lixiaochao on 2019/3/7.
 */



//用mapbox记载天地图和房屋面
import {MAPSTYLE} from '../../static/mapgis/styleCfg.js'
function InitMapboxMap(div,option) {
  option=option||{};
  if(div){
    var map = new mapboxgl.Map({
      container: div,
      style:MAPSTYLE,
      center: option.center?option.center:[114.051259,22.702632],
      minZoom: 0,
      maxZoom: 18,
      zoom: option.zoom?option.zoom:10,
      renderWorldCopies: false,
      isAttributionControl: false
    });
    map.on('load',function(){
      if(option.onLoad){
        option.onLoad(map)
      }
    });

    if(option.onSuccess){
      option.onSuccess(map)
    }
  }else{
    throw 'InitMapboxMap缺少div'
  }

}


export{
  RegularStr,SetRecord,GetRecord,ClearRecord,InitMapboxMap
}
