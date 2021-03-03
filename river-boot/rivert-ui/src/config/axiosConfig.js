//import Vue from 'vue'
import Vue from 'vue'

import setting from '@/setting.js'

import axios from 'axios' //引入axios 

import common from '@/components/common.js' // 导入共用组件

const apiURL = setting.apiURL;
//const apiURL = '/api/river';

// 创建axios实例
/* const service = axios.create({
    baseURL: process.env.BASE_API, // node环境的不同，对应不同的baseURL
    timeout: 5000, // 请求的超时时间
    //设置默认请求头，使post请求发送的是formdata格式数据// axios的header默认的Content-Type好像是'application/json;charset=UTF-8',我的项目都是用json格式传输，如果需要更改的话，可以用这种方式修改
    // headers: {  
      // "Content-Type": "application/x-www-form-urlencoded"
    // },
    withCredentials: true // 允许携带cookie
  }) */

// axios默认配置
axios.defaults.timeout = 1000 * 60 * 5;   // 超时时间
axios.defaults.baseURL = apiURL;  // 默认地址
axios.defaults.withCredentials = true ;// 允许携带cookie


/* axios.defaults.transformRequest = (data,config)=>{
  alert(data);
  console.log(config);
} */

// 添加请求拦截器
axios.interceptors.request.use(
    function (config) {
        //console.log(config);
        
        // 在发送请求之前做些什么
        if(config.method == 'post'){
          //config.headers['Content-Type'] = 'application/json;charset=UTF-8';
        }
        
        // headers中配置serialize为true开启序列化
        if (config.method === 'post' && (config.headers || {}).type=='json') {
          config.headers['Content-Type'] = 'application/json;charset=UTF-8';
          config.data = JSON.stringify(config.data);
          delete config.headers.type;
        }

        //获取token 
        const isToken = (config.headers || {}).isToken === false
        let token =  common.getToken();
        if (token && !isToken) {
          config.headers['Authorization'] = 'Bearer ' + token// //向服务器发送请求时需添加Authorization请求头
        }else if(config.url.indexOf("/public/")==-1 && config.url.indexOf("/rauth/")==-1 && config.url.indexOf("/oauth/")==-1 && config.url.slice(0,5) != '/site' && config.url.slice(0,6) != '/login' && config.url.slice(0,6) != '/logut'){//后台管理都拦截
          location.href='/river/login';
        }

        return config;
     }, function (error) {
        Vue.prototype.$Notice.error({title: '请求异常',desc: data.message});
        // 对请求错误做些什么
        return Promise.reject(error);
     }
);

// 添加响应拦截器
axios.interceptors.response.use(
    function (response) {
         // 对响应数据做点什么
        /*  console.log('================axios response '+response.config.url+'==========');
         console.log(response); */

         let data = response.data;

         if(data.code == '401'){//401 没有权限
            common.cleanToken();
            location.href='/river/login';
         }

        return data;
     }, 
    function (error) {
        // 对响应错误做点什么
        return Promise.reject(error);
    }

);


export default axios