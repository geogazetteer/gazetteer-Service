/**
 * Created by lixiaochao on 2019/3/21.
 */
const localIP = 'http://localhost';
const huaweiIP = 'http://119.3.72.23';	//http://119.3.72.23
const serverUrl = localIP+':8083/';//服务器地址
const URLCFG={
  searchCtxUrl:serverUrl+'address/hint',//搜索联想，param:keywords=上塘农贸建材市场 库坑凹背村&limit=1000
  searchAddressUrl:serverUrl+'address/hint',//搜索，param:keywords=上塘农贸建材市场 库坑凹背村&limit=1000
  //根据id获取详情
  getDetailBySearchIdUrl:serverUrl+'address/id',// + 794848


  //查询当前街道包含的所有社区
  getCommunityByStreetUrl:serverUrl+'address/all',//(param:tablename=民治街道）
  //查询当前社区包含的地址
  getCountByCommunityUrl:serverUrl+'editor/count',//(?tablename=tablename)
  getAddressByCommunityUrl:serverUrl+'editor/page',//(+pageNum?fields=fid,code,name,address&tablename=tablename&limit=10)
  //查询当前地址包含的编辑信息
  getEditInfoUrl:serverUrl+'editor/fid',
  //查询当前地址匹配的标准地址
  getMatchListUrl:serverUrl+'matcher/address',//(param:tablename=油松社区&keywords=%25万亨达大厦%25&min_sim=0.1&pagesize=10)

  //搜索设置（设置开启繁体、全角数字转换等）
  setSetUrl:serverUrl+'address/searcher/settings',//繁体转简体（settings ：）
  //判断输入是否为敏感词
  isSensitiveUrl:serverUrl+'transform/sensitive',//(param:chars=chars)
  //判断是否为正确的坐标
  isCoordinateUrl:serverUrl+'transform/coordinate',//(param:chars=chars)
  //根据坐标查code
  getCodeByPointUrl:serverUrl+'building/codes',//(param:x=114.017776720804&y=22.6390350934369)
  //根据code搜索标准地址
  searchByCodeUrl:serverUrl+'address/code',//(param:code)
  //根据坐标查标准地址
  searchByPointUrl:serverUrl+'building/address',//(param:x=114.019777&y=22.672456)
  //根据地址获取门楼号
  getHouseNumberByAddrUrl:serverUrl+'transform/houseNumber',//(params,chars=chars)

  //根据楼栋编号查询坐标
  getCoordinatesByCodeUrl:serverUrl+'building/point',//(param:code='')
  //获取搜索结果总数
  searchCtxTotalUrl:serverUrl+'address/sum',//param：keywords=''
  //根据页码获取搜索结果
  searchListUrl:serverUrl+'address/page',// "/1?keywords=a&limit=10"
};

const geoServerUrl=huaweiIP + ':8085/';//geoserver服务地址
const webUrl=huaweiIP + ':8087/';
const MAPURL={
  //雪碧图
  sprite:webUrl+'sprite/sprite',
  //底图
  mapTile:webUrl+"mapboxLayer/{z}/{x}/{y}.pbf",
  //字体
  glyphs:webUrl+"myfonts/{fontstack}/{range}.pbf",
  //政务内网底图  http://10.148.26.70:50001/proxy/layer/7C1327683470412586F72F48CBE425BA/25A0E34254A04FCFB2E3276924A03615/tile/0/1072/1461
  ol_map_tile:'http://10.148.26.70:50001/proxy/layer/7C1327683470412586F72F48CBE425BA/25A0E34254A04FCFB2E3276924A03615/tile',
  //房屋面
  building_vec_tile:geoServerUrl+"geoserver/gwc/service/tms/1.0.0/gazetteer%3ALH_building_4490@EPSG%3A900913@png/{z}/{x}/{y}.png"
};
//编辑模块，左侧街道社区selector配置
const EDITSELECTORCFG={
  street:['民治街道','观湖街道','福城街道','观澜街道','龙华街道','大浪街道']
};


//搜索设置字典
const SETDICT={//默认为false的配置项
  "繁体转换": 'complexChar',
  "全角转换": 'fullChar',
  "中文数字转换": 'chineseNumber',

  '别名转换':"alias",
  "同音字转换": 'homophone',
  "同义词转换":"synonym",
  "通假字转换": 'interchangeable',



  "地名": 'geoName',
  "POI": 'poi',
  "坐标": 'coordinates',
  "建筑物编码":"buildingCode",

  "普通搜索": 'databaseSearch',
  "快速搜索": 'luceneSearch',
};
const SETDICT_DEFAULT={//默认为true的配置项
  "地址": 'address',
  "普通搜索": 'databaseSearch',
};

const SETLABELARR={
  settings_1:['繁体转换','全角转换','中文数字转换'],
  settings_2:['别名转换','同音字转换','同义词转换','通假字转换'],
  settings_3:['地址','地名','POI','坐标','建筑物编码'],
  settings_4:['普通搜索','快速搜索'],
};

const BATCHSERVICE={
  modelUrl:webUrl+'files/downloads/批量处理模版.xls',//模板地址
  uploadUrl:serverUrl+'data/upload/matcher',//文件上传
};
