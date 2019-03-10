
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
    //demo
    getSearchList(url) {
        return fetch('get', url)
    }
};
