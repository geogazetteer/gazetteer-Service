//初始化变量
var RMapType={};



//基础地图信息--开始
//设置坐标
var projectionBase = new ol.proj.Projection({
	code: 'EPSG:4490',
	units: 'degrees',
	axisOrientation: 'neu'
});
//设置每一级别比例尺
var resolutionsBase = [0.0013732910156250004, 6.866455078125002E-4, 3.433227539062501E-4,  1.7166137695312505E-4,8.583068847656253E-5, 4.2915344238281264E-5, 2.1457672119140632E-5, 1.0728836059570316E-5, 5.364418029785158E-6, 2.682209014892578E-6, 1.341104507446289E-6];
// var resolutionsBase = [0.001373291015625, 6.866455078125E-4, 3.4332275390625E-4, 1.71661376953125E-4, 8.58306884765625E-5, 4.291534423828125E-5, 2.1457672119140625E-5, 1.0728836059570312E-5, 5.364418029785156E-6, 2.682209014892578E-6, 1.341104507446289E-6, 6.705522537231445E-7, 3.3527612686157227E-7];
// var resolutionsBase = [0.703125, 0.3515625, 0.17578125, 0.087890625, 0.0439453125, 0.02197265625, 0.010986328125, 0.0054931640625, 0.00274658203125, 0.001373291015625, 6.866455078125E-4, 3.4332275390625E-4, 1.71661376953125E-4, 8.58306884765625E-5, 4.291534423828125E-5, 2.1457672119140625E-5, 1.0728836059570312E-5, 5.364418029785156E-6, 2.682209014892578E-6, 1.341104507446289E-6, 6.705522537231445E-7, 3.3527612686157227E-7];
//设置origin
var originBase = [-400.0,399.9999999999998];
var mCenterBase=[114.05457,22.54635];//地图中心点-经纬度坐标
// 地图范围

var fullExtentBase = [113.75134199857723
	,22.39593669772114
	,114.62449893355381
	,22.864739894866602
];//原始范围

//无图的时候，显示的无图提示
RMapType.NoDateMap=new ol.layer.Tile({
	zIndex:0,
	source :new ol.source.TileArcGISRest({
//	  url: mapPath+'/resources/js/fullquery/ritu/NoData.png',
	  projection : projectionBase
	})
});

//创建地图图层

RMapType.BaseLayer=new ol.layer.Tile({
  source: new ol.source.XYZ({
	  tileGrid: new ol.tilegrid.TileGrid({
		  tileSize: 256,
		  origin: originBase,
		  maxExtent: fullExtentBase,
		  resolutions: resolutionsBase
	   }),
	  projection: projectionBase,
	  maxZoom:11,
	  minZoom:1,
	  tileUrlFunction : function (xyz, obj1, obj2) {
		  var url = MAPURL['ol_map_tile']+"/"  + xyz[0] +  "/" +  -xyz[2]  + "/" +  xyz[1];
		  return url;
	  }
  })
});

var RedLineConfig = {
		Title: "深圳网格办信息服务平台",
		City: "深圳",
		Area: "宝安区",
		MapInit: {
			mapLng: 114.029277545,
			mapLat: 22.609630505,
			boundMinLng:113.590203125,
			boundMaxLng:114.73186849999998,
			boundMinLat:22.20,
			boundMaxLat:22.93,
			mapLevel: 6,
			mapBaseLayers: [RMapType.NoDateMap,RMapType.BaseLayer],
			mapExtraLayers: [],
      markLevel:9
		}
	};



