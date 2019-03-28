/**
 * Created by lixiaochao on 2019/3/21.
 */
const URLCFG={
  searchCtx:'http://localhost:8083/address/hint',//搜索联想，param:keywords=上塘农贸建材市场 库坑凹背村&limit=1000
  searchAddressUrl:'http://localhost:8083/address/hint',//搜索，param:keywords=上塘农贸建材市场 库坑凹背村&limit=1000
  //根据id获取详情
  getDetailBySearchId:'http://localhost:8083/address/id/',// + 794848

  //查询当前街道包含的所有社区
  getCommunityByStreet:'http://localhost:8083/address/all',//(param:tablename=民治街道）
  //查询当前社区包含的地址
  getAddressByCommunity:'http://localhost:8083/editor/all',//(param:fields=address&tablename=民治社区)
  //查询当前地址包含的编辑信息
  getEditInfo:'http://localhost:8083/editor/address',
  //查询当前地址匹配的标准地址
  getMatchList:'http://localhost:8083/matcher/address',//(param:tablename=油松社区&keywords=%25万亨达大厦%25&min_sim=0.1&pagesize=10)
};


//编辑模块，左侧街道社区selector配置
const EDITSELECTORCFG={
  street:['民治街道','观湖街道','福城街道','观澜街道','龙华街道','大浪街道']
};

export{
  URLCFG,EDITSELECTORCFG
}
