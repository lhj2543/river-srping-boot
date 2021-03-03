
import Vue from 'vue'

import axios from '@/config/axiosConfig' //引入axios 全局配置
import VueAxios from 'vue-axios' //vue-axios是将axios集成到Vue.js的小包装器
Vue.use(VueAxios,axios);



var commomObj= {
    
    
    install(v, options) {
        Vue.prototype.getSysItems = (params,callback) => {//加载字典数据 
            
            let _this = Vue;
                
            _this.axios.post('/system/sysItem/getSysItemByCategoryName',params,{headers:{type:'json'}})
            .then((response)=>{
                let row = response.data;
                if(response.success){
                    callback(row);
                }else{
                    //Vue.prototype.$Message.info(response.message);
                    Vue.prototype.$Notice.error({title: '字典加载失败',desc: response.message});
                }
            })
            .catch((error)=>{
                //Vue.prototype.$Modal.error({title: '字典加载异常',content: error});
                Vue.prototype.$Notice.error({title: '字典加载异常',desc: error});
            });

        }
    },



    icon:{//全局图标定义
        logo:'iconfont icon-icon-logo',//logo
        add:'ios-add',//新增
        modify:'ios-create-outline',//修改
        detail:'ios-browsers',//详情
        delete:'ios-trash-outline',//删除
        remover:'',//移除
        search:'ios-search',//搜索
        list:'ios-list-box-outline',//列表
        refresh:'md-sync',//刷新
        back:'ios-undo-outline',//返回
        save:'ios-albums-outline',//保存
        cancel:'ios-undo-outline',//取消
        clean:'ios-refresh',//清除 重置
        close:'ios-close',//关闭
        full:'',//全屏
        
    },
    securityEncodeKey :'river_key354282p',//密码加密密钥
    tokenKey:'sysAccountInfo',//localStorage 存储用户信息key
    webSitRoules:[],//站点路由数据
    webSitMenus:[],//站点菜单数据
    manageRoules:[],//后台管理路由数据
    manageMenus:{},//后台管理菜单数据

    ueditConfig: {//百度编辑器配置
        // 编辑器不自动被内容撑高
        autoHeightEnabled: false,
        // 初始容器高度
        initialFrameHeight: '100%',
        // 初始容器宽度
        initialFrameWidth: '100%',
        // 上传文件接口
        serverUrl: '/rivert/site/public/ueditor/exec',
        // UEditor 资源文件的存放路径，如果你使用的是 vue-cli 生成的项目，通常不需要设置该选项，vue-ueditor-wrap 会自动处理常见的情况，如果需要特殊配置，参考下方的常见问题2
        UEDITOR_HOME_URL: '/static/UEditor/'
        /* headers:{
        'Authorization' : 'Bearer ' + this.$common.getToken()
        }, */
    },
    
    logout:(params)=>{//登出
        let _this = params._this;
        _this.lodding = true;
        _this.axios.delete('/rauth/logout')
        .then((response)=>{
            _this.lodding = false;
            let data = response;
            if(response.success){
                _this.$common.cleanToken();
                //_this.$router.push({path:'/login'});
                location.href='/river/login';
            }else{
                _this.$Notice.info({title:'注销失败',desc: data.message});
            }
        })
        .catch((error)=>{
            _this.lodding = false;
            _this.$Notice.error({title:'用户注销异常',desc: error});
        });
    },
    setToken:(value)=>{//设置token
        if(window.localStorage){
            window.localStorage.setItem(commomObj.tokenKey,JSON.stringify(value));
        }
    },
    getToken:(params)=>{//获取token
        let result = '';
        if(window.localStorage && window.localStorage.getItem(commomObj.tokenKey)!=undefined){
            let sysAccountInfo = JSON.parse(window.localStorage.getItem(commomObj.tokenKey));
            result =  sysAccountInfo.access_token;
        }
        return result;
    },
    cleanToken:()=>{//清除token
        if(window.localStorage){
            window.localStorage.removeItem(commomObj.tokenKey);
        }
    },
    getAccountInfo:(params)=>{//获取账户信息
        let result = {};
        if(window.localStorage && window.localStorage.getItem(commomObj.tokenKey)!=undefined){
            result = JSON.parse(window.localStorage.getItem(commomObj.tokenKey));
        }
        return result;
    },
    //返回顶部
    backTop(dom){

        let timer=setInterval(function(){
            var scrollTop = dom.scrollTop;
            var ispeed=Math.floor(-scrollTop/6);
            
            if(scrollTop<=0){
                clearInterval(timer);
            }

            dom.scrollTop = scrollTop+ispeed;

        },30);

    }

}

// 暴露出这些属性和方法
export default commomObj;
