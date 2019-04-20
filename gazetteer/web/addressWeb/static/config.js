/**
 * Created by lixiaochao on 2019/3/21.
 */

const serverUrl = 'http://119.3.72.23:8083/';//服务器地址
var ProxyServlet = 'http://localhost:9192/ProxyServlet/proxyHandler?url=';//代理服务器地址
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
};

const geoServerUrl='http://119.3.72.23:8085/';//geoserver服务地址
const MAPURL={
  //雪碧图
  sprite:'http://localhost:8087/static/mapjs/sptite/sprite',
  //底图
  mapTile:ProxyServlet+"http://localhost:9192/mapboxLayer/{z}/{x}/{y}.pbf",
  //字体
  glyphs:ProxyServlet+"http://localhost:9192/myfonts/{fontstack}/{range}.pbf",
  //房屋面
  building_vec_tile:geoServerUrl+"geoserver/gwc/service/tms/1.0.0/gazetteer%3ALH_building_4490@EPSG%3A900913@pbf/{z}/{x}/{y}.pbf"
};
//编辑模块，左侧街道社区selector配置
const EDITSELECTORCFG={
  street:['民治街道','观湖街道','福城街道','观澜街道','龙华街道','大浪街道']
};


//搜索设置字典
const SETDICT={
  '别名识别':"addressAlias",
  "数字转换": 'chineseNumber',
  "地址补全": 'completed',
  "繁体转换": 'complexChar',
  "坐标识别": 'coordinates',
  "全角转换": 'fullChar',
  "地名": 'geoName',
  "地名别名": 'geoNameAlias',
  "通假字识别": 'interchangeable',
  "同音字识别": 'ishomophone',
  "POI": 'poi',
  "POI别名": 'poialias',
  "地址范围识别": 'withtin'
};

