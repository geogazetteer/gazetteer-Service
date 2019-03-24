/**
 * Created by lixiaochao on 2019/3/21.
 */
const URLCFG={
  searchAddressUrl:'http://localhost:8080/address/selectAddressBylucene',//搜索

  //查询当前街道包含的所有社区
  getCommunityByStreet:'http://localhost:8080/address/all',//(param:tablename=民治街道）
  //查询当前社区包含的地址
  getAddressByCommunity:'http://localhost:8080/editor/all',//(param:fields=address&tablename=民治社区)
  //查询当前地址包含的编辑信息
  getEditInfo:'http://localhost:8080/editor/address',
  //查询当前地址匹配的标准地址
  getMatchList:'http://localhost:8080/matcher/address',//(param:tablename=油松社区&keywords=%25万亨达大厦%25&min_sim=0.1&pagesize=10)
};


//编辑模块，左侧街道社区selector配置
const EDITSELECTORCFG={
  street:['民治街道','观湖街道','福城街道','观澜街道','龙华街道','大浪街道']
};

export{
  URLCFG,EDITSELECTORCFG
}
