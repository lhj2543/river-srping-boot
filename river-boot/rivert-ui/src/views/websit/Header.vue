<template>
    <header id="index-header" :class="contentActiveTheme" >

      <Row class="header-row" >
        <Col span="4">
          <router-link target="" :to="{path:'/'}" >
            <span class="cursor-pointer" title="www.rivert.cn">
              <Icon id="logo" class="logo"  color="" custom="iconfont icon-icon-logo" />
              <span class="log-name">RIVER</span>
            </span>
          </router-link>
        </Col>
        <Col span="20">
          <div class="mnue-more-r">
            
            <span class="top-mnues cursor-pointer hove-color" v-for="(row,index) in headerMenus" :key="row.id">
              <a v-if="index===headerMenus.length-1" :href="row.url">{{row.displayName}}</a>
              <router-link :class="[{'top-mnues-active':row.active}]" :id="row.id" v-else target="_self" @click="topMenuClick({path:row.url,id:row.id})" :to="{path:row.url}">{{row.displayName}}</router-link>
            </span>

            <span class="top-mnues ">|</span>

            <!-- 用户信息 -->
            <span v-if="sysAccountInfo.access_token!='' && sysAccountInfo.access_token!=undefined" >
                <span class="top-mnues " style="padding:0;">
                  <Icon type="ios-person-outline" />
                  {{sysAccountInfo.user_name}}
                </span>
                <span class="top-mnues cursor-pointer hove-color logout" @click="logout">注销</span>
            </span>
            <span v-else class="top-mnues cursor-pointer hove-color" >
              <router-link target="_self" :to="{path:'/river/login'}" >登录</router-link>
            </span>

            <span v-if="isIndex" style="font-weight: bold;" class="cursor-pointer" @click="moreMnueOpen">网站简介<!-- <Icon title="简介" class="mnue-more " type="md-list" color=""  /> --></span>

          </div>
        </Col>
      </Row>

      <!-- loadding  -->
      <Spin fix v-if="lodding">
          <Icon type="ios-loading" size=18 class="spin-icon-load"></Icon>
          <div>登录中请稍后...</div>
      </Spin>
      
    </header>
</template>


<script>

import $ from 'jquery'

export default {
  name: 'index-header',
  data(){
    return {
      sysAccountInfo:this.$common.getAccountInfo(),
      headerMenus:this.$common.webSitMenus,
      topActiveMenuId:'',
      isIndex:false,
      lodding:false
    }
  },
  props:{
      contentActiveTheme:String,
  },
  beforeCreate(){//拿不到任何信息，无法篡改数据，一般做loding，这个时候的vue实例还什么都没有，但是$route对象是存在的，可以根据路由信息进行重定向之类的操作
    
  },
  created() {//el 没有初始化，数据已加载完成，阔以篡改数据，并更新，不会触发，，在这结束，还做一些初始化，实现函数自执行，ref属性内容为空数组
   
  },
  beforeMount(){//$el已被初始化,，数据已加载完成，阔以篡改数据，并更新，不会触发beforeUpdate，updated，在挂载开始之前被调用，beforeMount之前，会找到对应的template，并编译成render函数
 
  },
  mounted () {//钩子
    
    //顶部菜单定位
    let path = this.$route.path;
    if(path != '/'){
      //不是首页重新设置大小
      document.documentElement.style.fontSize =  "15px";
    }else{
      this.isIndex = true;
    }
    for(var i in this.headerMenus){
      let row = this.headerMenus[i];
      if(row.url == path){
        row.active = true;
        this.topActiveMenuId = row.id;
      }else{
        row.active = false;
      }
    }

  },
  methods: {
    moreMnueOpen(ev){
      this.$emit('openMnue',ev);//调用父级openMnue
    },
    logout(){//用户注销
        this.$common.logout({_this:this});//公共注销方法
    },
  },
  destroyed () {//销毁

  },

}
</script>


<style scoped>

  .top-mnues-active{
    color: #1DB954 !important;
  }

  .top-fied{
    background:#fff;

    /* border-bottom: 0.1rem solid #f0f0f0;
    -moz-box-shadow: 0 0 1rem #CBCBCB;
    -ms-box-shadow: 0 0 1rem #CBCBCB;
    -o-box-shadow: 0 0 1rem #CBCBCB;
    box-shadow: 0 0 1rem #CBCBCB; */

    border-bottom: 0.05rem solid #E01222;
    -moz-box-shadow: 0 0  0.6rem #E01222;
    -ms-box-shadow: 0 0  0.6rem #E01222;
    -o-box-shadow: 0 0  0.6rem #E01222;
    box-shadow: 0 0 0.6rem #E01222;


    -webkit-transition: background 1s;
    -moz-transition: background 1s;
    -ms-transition: background 1s;
    -o-transition: background 1s;
    transition:  background 1s;
  }
  .theme-1.top-fied *{
    color: #000;
  }
  .theme-0.color-white *{
    color: #fff;
  }

  #index-header{
    padding: 0rem 2rem;
    position: fixed;
    width: 100%;
    top: 0;
    z-index: 100;
  }
  .header-row{
    line-height: 4rem;height:4rem;
  }
  .logo{
    font-size: 2.5rem;
    vertical-align: middle;
  }
  .log-name{
    font-size: 1.5rem;
    font-family: Bradley Hand ITC,High Tower Text;
    font-weight: bold;
  }
  .mnue-more-r{
    float: right;
  }
  .mnue-more{
    font-size: 1.5rem;
    vertical-align:middle;
    padding-left:0.5rem ;
  }
  .top-mnues{
    font-size: 1.1rem;
    font-weight: bold;
    padding: 0 1.5rem;
  }
  .top-mnues i{
    font-size: 1.5rem;
    vertical-align: middle;
  }
  
</style>
