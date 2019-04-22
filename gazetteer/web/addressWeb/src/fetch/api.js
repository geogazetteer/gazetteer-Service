
import axios from 'axios';
import qs from 'qs';
import Promise from 'pinkie-promise';


//axios.defaults.baseURL = `${config.BASE_URL}/njData/data`;

export function fetch(method, url, params,option) {
    return new Promise((resolve, reject) => {
        if (method == 'get') {
            axios.get(url, { params: params })
                .then(response => {
                    // alert(JSON.stringify(response.data))
                    resolve(JSON.parse(JSON.stringify(response.data)));
                }, err => {
                    reject(err);
                })
                .catch((error) => {
                    reject(error)
                })
        } else if (method == 'post') {
          //axios.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoed;charset=UTF-8';
          option=option||{};
          axios.post(url ,params,option)
                .then(response => {
                    resolve(JSON.parse(JSON.stringify(response.data)));
                }, err => {
                    reject(err);
                })
                .catch((error) => {
                    reject(error)
                })
        }else if(method=='put'){
          axios.put(url ,params)
            .then(response => {
              resolve(JSON.parse(JSON.stringify(response.data)));
            }, err => {
              reject(err);
            })
            .catch((error) => {
              reject(error)
            })
        }
    })
}

export default {
    getMsg(url,params) {
        return fetch('get', url,params)
    },

  //搜索联想 http://localhost:8083/address/hint?keywords=上塘农贸建材市场 库坑凹背村&limit=1000
  getSearchCtx(url,keyword){
      return fetch('get',url,{
        keywords:keyword,
        limit:1000
      })
  },
  //获取搜索详情
  getDetailBySearchId(url,id) {
    return fetch('get', url+'/'+id)
  },


  //获取当前社区所包含的所有非标准地址
  getAddressByCommunity(url,page,tablename){
    return fetch('get', url+'/'+page,{
      fields:'fid,code,name,address',
      tablename:tablename,
      limit:10
    })
  },
  //获取编辑信息
  getEditMsg(url,fid,tablename) {
    return fetch('get', url+'/'+fid,{
      tablename:tablename
    })
  },

  //获取匹配标准地址
  getMatchList(url,keywords) {
      //http://localhost:8083/matcher/address?tablename=油松社区&keywords=万亨达大厦&min_sim=0.1&pagesize=10
    return fetch('get', url,{
      keywords:keywords,
      min_sim:0.1,
      limit:10
    })
  },

  //搜索设置
  //http://119.3.72.23:8083/address/searcher/settings
  setSettings(setObj){
    return fetch('put', URLCFG['setSetUrl'] ,setObj)
  },

  //坐标反查服务,先根据坐标找code，再根据code搜索
  getCodeByPoint(x,y){
      return fetch('get',URLCFG['getCodeByPointUrl']+'?x='+x+'&y='+y)
  },
  //根据code搜索
  searchByCode(code){
    return fetch('get',URLCFG['searchByCodeUrl']+'/'+code)
  },
  //根据点找标准地址
  searchByPoint(x,y){
      return fetch('get',URLCFG['searchByPointUrl']+'?x='+x+'&y='+y)
  },

};
