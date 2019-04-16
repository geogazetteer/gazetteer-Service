/**
 * Created by lixiaochao on 2019/3/21.
 */

const domain='http://119.3.72.23';
const serverUrl = 'http://localhost:8083/';
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
};

const MAPURL={
  //腾讯底图
  mapTile_tx:domain+":8080/crs/tecentMapCrsConvert?tilecache=true&x={x}&y={y}&z={z}",
  // building_vec_tile:domain+":8085/geoserver/gwc/service/tms/1.0.0/gazetteer%3Abuilding@EPSG%3A4326%3A512@pbf/{z}/{x}/{y}.pbf"
  building_vec_tile:"http://119.3.72.23:8085/geoserver/gwc/service/wmts?layer=gazetteer:longhua_building&style=&tilematrixset=EPSG_4326_512&Service=WMTS&Request=GetTile&Version=1.0.0&Format=image/png&TileMatrix=EPSG_4326_512:{z}&TileCol={x}&TileRow={y}"
};
//编辑模块，左侧街道社区selector配置
const EDITSELECTORCFG={
  street:['民治街道','观湖街道','福城街道','观澜街道','龙华街道','大浪街道']
};

export{
  URLCFG,EDITSELECTORCFG,MAPURL
}
