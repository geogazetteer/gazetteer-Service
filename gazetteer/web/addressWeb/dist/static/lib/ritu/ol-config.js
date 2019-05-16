//初始化变量
var RMapEngineRoot=null;
var RMapType={};
var RMapOptions={
	MapRoots:[]
}
//基础地图信息--开始
//设置坐标
var projectionBase = new ol.proj.Projection({
	code: 'EPSG:4326',
	units: 'degrees',
	axisOrientation: 'neu'
});
//设置每一级别比例尺
var resolutionsBase = [0.703125, 0.3515625, 0.17578125, 0.087890625, 0.0439453125, 0.02197265625, 0.010986328125, 0.0054931640625, 0.00274658203125, 0.001373291015625, 6.866455078125E-4, 3.4332275390625E-4, 1.71661376953125E-4, 8.58306884765625E-5, 4.291534423828125E-5, 2.1457672119140625E-5, 1.0728836059570312E-5, 5.364418029785156E-6, 2.682209014892578E-6, 1.341104507446289E-6, 6.705522537231445E-7, 3.3527612686157227E-7];
//设置origin
var originBase = [-180.0, -90.0];
var mCenterBase=[114.05457,22.54635];//地图中心点-经纬度坐标
// 地图范围

var fullExtentBase = [113.3619204711914,22.15763153076172,115.14614852905273,23.1];//原始范围

//无图的时候，显示的无图提示
RMapType.NoDateMap=new ol.layer.Tile({
	zIndex:0,
	source :new ol.source.TileArcGISRest({
//	  url: mapPath+'/resources/js/fullquery/ritu/NoData.png',
	  projection : projectionBase
	})
})

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
	  tileUrlFunction : function (xyz, obj1, obj2) {
		  var url = "http://61.144.226.122:10000/geoserver/gwc/service/tms/1.0.0/shenzhen%3Ashenzhenwgb@EPSG%3A4326@png/"  + xyz[0] +  "/" +  xyz[1]  + "/" +  xyz[2] +  ".png";
		  return url;
	  }
  })
})
//基础地图信息--结束

RMapType.SZGongZuoWangGe = new ol.layer.Tile({
	  source: new ol.source.XYZ({
		  tileGrid: new ol.tilegrid.TileGrid({
			  tileSize: 256,
			  origin: originBase,
			  maxExtent: [113.75260925293,22.3920040130615,114.604064941406,22.8639850616455],
			  resolutions: resolutionsBase
		  }),
		  projection: projectionBase,
		  tileUrlFunction : function (xyz, obj1, obj2) {
			  var url = "http://61.144.226.122:10000/geoserver/gwc/service/tms/1.0.0/shenzhen%3AWorkGridLayer@EPSG%3A4326@png/"  + xyz[0] +  "/" +  xyz[1]  + "/" +  xyz[2] +  ".png";
			  return url;
		  }
	  })
})

RMapType.SZLouDongMian = new ol.layer.Tile({
	  source: new ol.source.XYZ({
		  tileGrid: new ol.tilegrid.TileGrid({
			  tileSize: 256,
			  origin: originBase,
			  maxExtent: [113.759452819824,22.451904296875,114.612815856934,22.8432445526123],
			  resolutions: resolutionsBase
		  }),
		  projection: projectionBase,
		  tileUrlFunction : function (xyz, obj1, obj2) {
			  var url = "http://61.144.226.122:10000/geoserver/gwc/service/tms/1.0.0/shenzhen%3AWgbLoudong@EPSG%3A4326@png/"  + xyz[0] +  "/" +  xyz[1]  + "/" +  xyz[2] +  ".png";
			  return url;
		  }
	  })
})

RMapType.SZWeiXingTu = new ol.layer.Tile({
	  source: new ol.source.XYZ({
		  tileGrid: new ol.tilegrid.TileGrid({
			  tileSize: 256,
			  origin: originBase,
			  maxExtent: [113.68103027343751,22.23825716511717,114.65881069317844,22.859725586446075],
			  resolutions: resolutionsBase
		  }),
		  projection: projectionBase,
		  tileUrlFunction : function (xyz, obj1, obj2) {
			  var url = "http://61.144.226.122:10000/geoserver/gwc/service/tms/1.0.0/shenzhen%3Ashenzhen_satellite@EPSG%3A4326@png/"  + xyz[0] +  "/" +  xyz[1]  + "/" +  xyz[2] +  ".png";
			  return url;
		  }
	  })
})

RMapType.SubwayStationSurface=new ol.layer.Tile({
	  source: new ol.source.XYZ({
		  tileGrid: new ol.tilegrid.TileGrid({
			  tileSize: 256,
			  origin: originBase,
			  maxExtent: [113.795280456543,22.4746742248535,114.280693054199,22.7547588348389],
			  resolutions: resolutionsBase
		  }),
		  projection: projectionBase,
		  tileUrlFunction : function (xyz, obj1, obj2) {
			  var url = "http://61.144.226.122:10000/geoserver/gwc/service/tms/1.0.0/shenzhensubway@EPSG%3A4326@png/"  + xyz[0] +  "/" +  xyz[1]  + "/" +  xyz[2] +  ".png";
			  return url;
		  }
	  })
})

RMapType.IndoorMapF1=new ol.layer.Tile({
	  source: new ol.source.XYZ({
		  tileGrid: new ol.tilegrid.TileGrid({
			  tileSize: 256,
			  origin: originBase,
			  maxExtent: [114.02670096,22.60696392,114.032077032454,22.6120680581422],
			  resolutions: resolutionsBase
		  }),
		  projection: projectionBase,
		  tileUrlFunction : function (xyz, obj1, obj2) {
			  var url = "http://61.144.226.122:10000/geoserver/gwc/service/tms/1.0.0/shenzhen%3AshenzhenIndoorF1@EPSG%3A4326@png/"  + xyz[0] +  "/" +  xyz[1]  + "/" +  xyz[2] +  ".png";
			  return url;
		  }
	  })
})

RMapType.IndoorMapF2=new ol.layer.Tile({
	  source: new ol.source.XYZ({
		  tileGrid: new ol.tilegrid.TileGrid({
			  tileSize: 256,
			  origin: originBase,
			  maxExtent: [114.027311686413,22.6076536211627,114.03099036,22.6110388803356],
			  resolutions: resolutionsBase
		  }),
		  projection: projectionBase,
		  tileUrlFunction : function (xyz, obj1, obj2) {
			  var url = "http://61.144.226.122:10000/geoserver/gwc/service/tms/1.0.0/shenzhen%3AshenzhenIndoorF2@EPSG%3A4326@png/"  + xyz[0] +  "/" +  xyz[1]  + "/" +  xyz[2] +  ".png";
			  return url;
		  }
	  })
})

RMapType.IndoorMapF3=new ol.layer.Tile({
	  source: new ol.source.XYZ({
		  tileGrid: new ol.tilegrid.TileGrid({
			  tileSize: 256,
			  origin: originBase,
			  maxExtent: [114.027277545,22.607630505,114.03099405,22.6110107250001],
			  resolutions: resolutionsBase
		  }),
		  projection: projectionBase,
		  tileUrlFunction : function (xyz, obj1, obj2) {
			  var url = "http://61.144.226.122:10000/geoserver/gwc/service/tms/1.0.0/shenzhen%3AshenzhenIndoorF3@EPSG%3A4326@png/"  + xyz[0] +  "/" +  xyz[1]  + "/" +  xyz[2] +  ".png";
			  return url;
		  }
	  })
})

RMapType.loadPoiMap=new ol.layer.Tile({
	  source: new ol.source.XYZ({
		  tileGrid: new ol.tilegrid.TileGrid({
			  tileSize: 256,
			  origin: originBase,
			  maxExtent: [113.759269714355,22.4505748748779,114.594764709473,22.840726852417],
			  resolutions: resolutionsBase
		  }),
		  projection: projectionBase,
		  tileUrlFunction : function (xyz, obj1, obj2) {
			  var url = "http://61.144.226.122:10000/geoserver/gwc/service/tms/1.0.0/shenzhen%3AshenzhenRoadPOI@EPSG%3A4326@png/"  + xyz[0] +  "/" +  xyz[1]  + "/" +  xyz[2] +  ".png";
			  return url;
		  }
	  })
})

var RedLineConfig = {
		Title: "深圳网格办信息服务平台",
		City: "深圳",
		Area: "宝安区",
		MapInit: {
			maxLevel: 17,
			minLevel: 9,
			mapLng: 114.029277545,
			mapLat: 22.609630505,
			boundMinLng:113.590203125,
			boundMaxLng:114.73186849999998,
			boundMinLat:22.20,
			boundMaxLat:22.93,
			mapLevel: 10,
			mapBaseLayers: [RMapType.NoDateMap,RMapType.BaseLayer],
			mapExtraLayers: [],
			mapShiNeiTuLayers:[RMapType.IndoorMapF1,RMapType.IndoorMapF2,RMapType.IndoorMapF3],
			mapZuLinLouDongLayers:[RMapType.SZLouDongMian],
			mapGongZuoWangLuo:[RMapType.SZGongZuoWangGe],
			mapSHENZHENBASE:[RMapType.SZ3DBASE],
			mapSubwayStationSurface:[RMapType.SubwayStationSurface]
		},

		//底图菜单
		BaseLayerMenu: [{
			name: "地图",
			mapMaxLevel:17,
			mapMinLevel:9,
//			mapLng: 114.029181,
//			mapLat: 22.609325,
			mapLevel: 12,
			mapBaseLayers: [RMapType.NoDateMap,RMapType.BaseLayer]
		}, {
			name: "卫星",
			mapMaxLevel:17,
			mapMinLevel:9,
			mapLevel: 12,
//			mapLng: 114.029181,
//			mapLat: 22.609325,
			mapBaseLayers: [RMapType.NoDateMap,RMapType.SZWeiXingTu,RMapType.loadPoiMap]
		}],

		//右下角菜单
		RightBottomMenu: [/*{
				name: "全景",
				icon: "../../../data/img/customized/mapmenu/1.png",
				panoramaListId: "all",
				mapLng: 119.944937,
				mapLat: 31.65478175,
				mapLevel: 10,
				mapExtraLayers: []
			},*/ {
				name: "三维",
				icon: "../../../data/img/customized/mapmenu/2.png",
				threeDimenListId: "all",
				mapMaxLevel:17,
				mapMinLevel:9,
				mapLevel: 12,
				mapLng: 114.037892,
				mapLat: 22.627365,
			}

		]
	};