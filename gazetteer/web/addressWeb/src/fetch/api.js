
import axios from 'axios';
import qs from 'qs';
import Promise from 'pinkie-promise';
//import config from './config';

axios.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoed;charset=UTF-8';
//axios.defaults.baseURL = `${config.BASE_URL}/njData/data`;

export function fetch(method, url, params) {
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
            axios.post(url + "?" + qs.stringify(params))
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
  //获取编辑信息
  getEditMsg(url,address,tablename) {
    return fetch('get', url+'/'+address,{
      tablename:tablename
    })
  },
  //获取匹配标准地址
  getMatchAddress(url,tablename,keywords) {
      //http://localhost:8083/matcher/address?tablename=油松社区&keywords=万亨达大厦&min_sim=0.1&pagesize=10
    return fetch('get', url,{
      tablename:tablename,
      keywords:keywords,
      min_sim:0.1,
      pagesize:10
    })
  }
};
