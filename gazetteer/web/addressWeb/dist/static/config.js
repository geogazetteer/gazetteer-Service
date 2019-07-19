/**
 * Created by lixiaochao on 2019/3/21.
 */

// const IP = 'http://localhost';//本地ip
const IP = 'http://119.3.72.23';	//华为云ip
// const IP = 'http://10.148.26.63'; //政务内网ip
const serverUrl = IP+':8083/';//服务器地址
const geoServerUrl=IP + ':8085/';//geoserver服务地址
const webUrl=IP + ':8087/';

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
  //判断建筑物编码输入是否合法
  isCodeUrl:serverUrl+'transform/buildingcode',//(param:chars=chars)
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
  //根据一组id查询坐标信息
  getLLByIdUrl:serverUrl+'address/point',//?id=1,2,3&tablename=dmdz
  //根据一组id查询坐标信息
  getLLByIdsUrl:serverUrl+'address/points',//?id=1,2,3&tablename=dmdz
  //获取搜索结果总数
  searchCtxTotalUrl:serverUrl+'address/sum',//param：keywords=''
  //根据页码获取搜索结果
  searchListUrl:serverUrl+'address/page',// "/1?keywords=a&limit=10"
  //获取龙华区所有街道
  getAllStreetUrl:serverUrl+'address/龙华区',
  //根据街道获取社区
  getComByStrUrl:serverUrl+'address/龙华区',
  //根据社区获取道路
  getRoadByComUrl:serverUrl+'address/roads',//{community}
  //根据道路获取道路编号
  getRoadCodeByRoadUrl:serverUrl+'address/road_nums',//{community}/{road}
  //根据社区获取小区
  getVillByComUrl:serverUrl+'address/villages',//{community}
  //查询某个小区的所有楼栋编号
  getCodeByVillageUrl:serverUrl+'address/codes',//http://119.3.72.23:8083/address/codes/community/village
  //相似查找
  getAddressLikeUrl:serverUrl+'address/like',



  //用户注册
  registerUserUrl:serverUrl+'user/register',
  //用户登录
  loginUrl:serverUrl+'user/login',

};

const MAPURL={
  //雪碧图
  sprite:webUrl+'sprite/sprite',
  //底图
  mapTile:webUrl+"mapboxLayer/{z}/{x}/{y}.pbf",
  //字体
  glyphs:webUrl+"myfonts/{fontstack}/{range}.pbf",
  //政务网底图  http://10.148.26.70:50001/proxy/layer/7C1327683470412586F72F48CBE425BA/25A0E34254A04FCFB2E3276924A03615/tile/0/1072/1461
  //http://10.148.26.61:6080/arcgis/rest/services/basemap/szmap_4490/MapServer
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

//批量匹配
const BATCHSERVICE={
  modelUrl:webUrl+'files/downloads/批量处理模板.xlsx',//模板地址
  uploadUrl:serverUrl+'data/upload/matcher',//文件上传
};


const EDITSERVICE={
  // 地址编辑，数据导入
  uploadUrl:serverUrl+'data/import',
  //获取设置列表
  getTargetListUrl:serverUrl+'data/target',
  setTargetUrl:serverUrl+'data/settings',//?fileName=
  exportUrl:serverUrl+'data/export',//?fileName=
  getCountUrl:serverUrl+'revision/count',//?tablename=
  initialUrl:serverUrl+'revision/initial',//?username=clover
  getListUrl:serverUrl+'revision/page',//1?fields=fid%2Corigin%2Caddress&limit=10&status=0&tablename=dmdz_edit
  getInfoByFidUrl:serverUrl+'revision/fid',///1?fields=*&tablename=dmdz_edit

  setEditUrl:serverUrl+'revision/update',///fid?tablename=dmdz_edit&username=user_admin
  editDict : {
    "community": "community_",//社区
    "username": "modifier",  //修改的用户名
    "originAddress": "origin_address",//原地址
    "curSelAddress": "similar_address",//相似标准地址
    // "standard_address": "standard_address",
    // "status": "status",
    "street": "street_",
    // "update_address": "update_address",//标准地址
    "code": "update_building_code",//楼栋编号
    // "update_date": "2019-06-10T15:05:06.229Z"
  },
  //构建一个地址
  addSiteUrl:serverUrl+'revision/build',//?street=民治街道&community=民治社区&village=梅花山庄&suffix=欣梅园D5栋&code=4403060090042100005
  //道路输入联想
  roadLikeUrl:serverUrl+'address/road_like',
  //道路编号联想
  roadCodeLikeUrl:serverUrl+'address/road_num_like',
  //小区输入联想
  addressLikeUrl:serverUrl+'address/village_like',
  codeLikeUrl:serverUrl+'address/code_like',

  //根据坐标获取标准地址
  getStandardByLLUrl:serverUrl+'building/address',//?x=114.019777&y=22.672456
  //根据fid获取标准地址
  getStandardByFidUrl:serverUrl+'revision/address/guess',// /1?username=username&tablename=dmdz_edit

  //根据fid获取一组坐标
  getLLByFIdsUrl:serverUrl+'revision/points',//?id=1,2,3&tablename=dmdz
};
