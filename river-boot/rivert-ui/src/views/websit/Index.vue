<template>

  <div id="index-body" class="index-body"  :class="{'mnue-more-open':mnueMoreOpenFlag}" >

    <div class="mnue-more-panel">
      <div class="mnue-more-top">
        <!-- <span><Icon class="" type="md-list" style="vertical-align:middle;" /> 菜单</span> -->
        <Icon type="ios-close" title="关闭" class="mnue-close cursor-pointer" @click="closeMnue" />
      </div>

      <div class="site-introduce">
        
        <div>
          <span class="cursor-pointer">
            <Icon id="logo" class="logo"   color="" custom="iconfont icon-icon-logo" />
            <span class="log-name">RIVER</span>
          </span>
        </div>

        <div class="site-introduce-panel">
          <div class="title" v-text="indexData.attrs.siteIntroduce?indexData.attrs.siteIntroduce.title:''"></div>
          <div class="notes" v-html="indexData.attrs.siteIntroduce?indexData.attrs.siteIntroduce.notes:''">
            通过这个个人网站，我用它记录学习和生活的点点滴滴，我勉励自己，一定不能偷懒，学无止境。这个主站完全个人从零开始搭建，途中好几个版本，也走了好多弯路，不过这也是一种历程，你变秃了你也变强了不是没有道理的。网站还有许多优化完善的地方，欢迎大家多多指点共勉！
            <br/>
            <br/>
            网站程序： <br/>
            前端：<br/>
            Vue + iview + JS + jQuery + Html +  Css <br/>
            后端：<br/>
            Java + SpringBoot + SpringCloud <br/>
             + SpringMvc + Mybatis-Plush <br/>
            安全权限：SpringSecurity + oAuth2 <br/>
            注册和配置中心：Alibaba Nacos <br/>
            服务网关：Gateway <br/>
            负载均衡：Ribbon <br/>
            远程调用：Feign + Restemplate <br/>
            服务熔断限流：Sentinel <br/>
            监控中心：SpringBoot Admin <br/>
            消息队列：RabbitMQ <br/>
            数据存储：MySql + Redis <br/>
            部署：nginx + docker <br/>
          </div>
        </div>

        <div  v-text="indexData.attrs.siteIntroduce?indexData.attrs.siteIntroduce.contactInfoTitle:''" style="padding:1rem 0 0.4rem 0;font-weight: 500;font-size: 1.2rem;">
          
        </div>
        <div class="site-introduce-contact-info" v-html="indexData.attrs.siteIntroduce?indexData.attrs.siteIntroduce.contactInfo:''">
          <!-- <div class="contact-info-d" >
            hhh
          </div> -->
        </div>

        <!-- <div class='contact-info-d' >
          <i class='octagonWrap-icon ivu-icon ivu-icon-ios-mail-outline' ></i>
          <span style='padding:1rem 0 0 1.8rem;'>river015@163.com</span>
        </div> -->

      </div>


    </div>

    <!-- 遮罩层 -->
    <div class="shade" :class="[mnueMoreOpenFlag?'display-block':'display-none']"  @click="closeMnue" ></div>

    <div id="index" class="index" >
      <!-- 头部 -->
      <!--或者这种写法也行<AppHeader/> -->
      <app-header ref="refHeader" v-on:openMnue="openMnue" :contentActiveTheme="contentActiveTheme" />

      <!-- ======================右侧切换条=============== -->
      <div class="right-tab-panel plan-right-centre theme-0" >
        <div :key="index" v-for="(row,index) in contentActives" :class="[row.className=='active'?'active':'']" @click="rightTabClick(index)" class="right-tab"></div>
      </div>
      <!-- ======================右侧切换条=============== -->

      <!-- ===============首页内容-1============== -->
      <img id="indexContent2Img" :src="indexData.attrs.content1?'url('+indexData.attrs.content1.background+')':'/static/index/conent2-bg1.jpg'" style="display:none;" />
      <div class="index-content index-content2 theme-1" :style="{'backgroundImage':indexData.attrs.content1&&indexData.attrs.content1.background?'url('+indexData.attrs.content1.background+')':''}" :class="contentActives[0].className" >
        
        <div class="content2-main-panel plan-centre">
          <div style="font-size:3rem;padding:2rem 0;">{{indexData.attrs.content1?indexData.attrs.content1.title:'——欢迎进入个人主页'}}</div>
          <div style="font-size:1.8rem;">{{indexData.attrs.content1?indexData.attrs.content1.nodes:'所谓天才，只不过是把别人喝咖啡的功夫都用在工作上罢了。'}}</div>
          <div class="resume-deail cursor-pointer" style="margin-top: 9.5rem;color: #F4CD29;" >{{indexData.attrs.content1?indexData.attrs.content1.btn_personal:'个人详情'}}</div>
        </div>

      </div>
      <!-- ===============首页内容-1============== -->

      <div v-if="isShowAllContent" >
        
        <!-- ===============首页内容-2============== -->
        <div class="index-content index-content3 theme-1"  :class="contentActives[1].className"  >
          
          <div class="content3-topbg" :style="{'backgroundImage':indexData.attrs.content2&&indexData.attrs.content2.background?'url('+indexData.attrs.content2.background+')':''}">
            
          </div>

          <div class="plan-centre content3-main-panel">

            <div v-for="(item, index) in indexData.projects" :key="index" class="card-item cursor-pointer"  >

              <div v-if="index == 0" class="content3-title ">{{indexData.attrs.content2?indexData.attrs.content2.title:'——项目作品展览'}}</div>

              <router-link :to="{name:'project'}" ><div v-if="index == 2" class="content3-more hove-color">查看更多</div></router-link>

              <div class="card-item-panel" style="height:100%;">
                  <div class="card-img"><img width="100%" height="100%" :src="item.img?$globSetting.attachUrl+item.img:''" onerror="this.src='/static/default/loadError.jpg'" /></div>
                    
                    <div @click="toProject(item.url)" class="card-detail">
                      <div class="card-detail-name" v-text="item.title"></div>
                      <div class="card-detail-desc">
                        <span v-text="item.createdBy"></span>
                        &nbsp;
                        <span  v-text="item.createdDate"></span>
                      </div>
                    </div>

                    <div class="card-info" v-html="item.notes"></div>
              </div>

            </div>

          </div>

        </div>
        <!-- ===============首页内容-2============== -->

        <!-- ===============首页内容-4============== -->
        <div class="index-content index-content4"  :class="contentActives[2].className"  >
          
          <div class="content4-title plan-centre" >
            <router-link :to="{path:indexData.attrs.content3?indexData.attrs.content3.title_link:'/river/skill'}">{{indexData.attrs.content3?indexData.attrs.content3.title:'个人随笔'}}</router-link>
          </div>

          <div class="plan-centre content4-main-panel">

            <div class="card-item" v-for="(row) in indexData.notebookCategorys" :key="row.sid" >
                <div class="cnt4-icon-d"><Icon :type="row.icon?row.icon:'ios-cafe-outline'" class="cnt4-icon" /></div>
                <div class="cnt4-title">
                  <div class="cnt4-title-name">{{row.displayName}}</div>
                  <div class="cnt4-title-desc">{{row.notes}}</div>
                </div>
                <div class="cnt4-button-d">
                  <router-link :to="{name:'skills',params:{sid:row.sid}}" >
                    <span class="content4-button cursor-pointer">查看详细</span>
                  </router-link>
                </div>
            </div>
            
          </div>

        </div>
        <!-- ===============首页内容-4============== -->

        <!-- ===============首页内容-5============== -->
        <div class="index-content index-content5"  :class="contentActives[3].className"  >
          <div class="content5-title plan-centre">
             <router-link :to="{name:'tool'}">
                {{indexData.attrs.content4?indexData.attrs.content4.title:'在线工具'}}
             </router-link>
          </div>

          <div class="plan-centre content5-main-panel">

            <div class="content5-card-panel">
              
              <div v-for="(item, index) in indexData.tools" :key="index" class="content5-card-item">
                <div class="content5-card-item-left" >
                  <img :src="item.img?$globSetting.attachUrl+item.img:''" onerror="this.src='/static/default/loadError1.jpg'" width="100%" height="100%"> 
                </div>
                <div class="content5-card-item-right">
                  <div class="info title">
                      {{item.title}}
                      <div class="title-icon" style="float:right">
                        <Icon type="md-heart-outline" />
                      </div>
                  </div>
                  <div class="info descs" v-html="item.notes"></div>
                  <div class="info " style="padding-top:1.8rem;">

                    <router-link :to="{path:item.url || '/river/tool'}">
                      <span class="button cursor-pointer">
                        {{indexData.attrs.content4 && indexData.attrs.content4.list_btn?indexData.attrs.content4.list_btn:'开 始 使 用'}}
                      </span>
                    </router-link>
                    
                    <span style="float:right;margin-top:0.3rem">
                      <span style="color:#7f8a99;">{{item.createdDate}}</span>
                    </span>
                  </div>
                </div>
              </div>

            </div>

          </div>

          <div class="content5-bottom-panel">
            <div class="content5-bottom-card">

              <div class="content5-bottom-card-item" v-for="(item, index) in indexData.attrs.links" :key="index">

                <div class="icon-box">
                  <diV class="octagonWrap">
                      <div class="octagon">
                      </div>
                      <Icon :type="item.icon || 'ios-cafe-outline'" slot="prepend" class="octagonWrap-icon"></Icon>
                  </diV>
                </div>
                
                <div class="content">
                  <div class="content-title" v-text="item.title"></div>
                  <div class="content-desc" v-text="item.notes">Perfectly simple & easy to in our hour, when our power.</div>
                  <div class="content-more">
                    <a :href="item.href">
                      <Icon type="ios-arrow-round-forward" />{{item.linkName}}
                    </a>
                  </div>
                </div>

              </div>
              
            </div>
          </div>

        </div>
        <!-- ===============首页内容-5============== -->

        <!-- ===============首页内容-6============== -->
        <div class="index-content index-content6 theme-1"  :class="contentActives[4].className"  >
          
          <div class="content6-topbg" :style="{'backgroundImage':indexData.attrs.content5&&indexData.attrs.content5.background?'url('+indexData.attrs.content5.background+')':''}" >
            <div class="" style="width:80%;height:10rem;border:1px solid red1;margin:0 auto;position: relative;top:35%;">
              <div class="" style="font-size:3rem;">{{indexData.attrs.content5?indexData.attrs.content5.title:'欢迎留言'}}</div>
              <div class="" style="font-size:1.5rem;">{{indexData.attrs.content5?indexData.attrs.content5.nodes:'所谓天才，只不过是把别人喝咖啡的功夫都用在工作上罢了。'}}</div>
            </div>
          </div>

          <div class="content6-main-panel">
            <div class="content6-left">
              <table width="100%" height="100%" >
                <tr>
                  <td height='100%' width="60%" >
                    <div class="content6-left-right"></div>
                  </td>
                  <td width="3%" ></td>
                  <td width="40%" >
                    <div class="content6-left-title">
                      <div>{{indexData.attrs.content5?indexData.attrs.content5.liuyan:'留言'}}</div>
                      <span style="font-size:1.4rem;">{{indexData.attrs.content5?indexData.attrs.content5.liuyanNodes:'欢迎留言意见，吐槽！'}}</span>
                    </div>
                  </td>
                </tr>
              </table>
            </div>
            <div class="content6-right">
                <div class="leaving-panel">
                  <Form  ref="formInline" >
                      <div class="leavin-div">
                        <Icon type="ios-person-outline" slot="prepend" class="leavingName-icon"></Icon>
                        <input class="leavingName" name="leavingName " placeholder="留言人" value="游客" />
                      </div>
                      <div class="leavin-div">
                        <textarea class="leavingContent" name="leavingContent " placeholder="请输入留言内容.." />
                      </div>
                      <div class="leavin-div leavingBut">
                        <div style="float:right">
                        <span class="content4-button cursor-pointer" style="background:#D5AE82;">{{indexData.attrs.content5?indexData.attrs.content5.btn_personal:'留 言'}}</span>
                        </div>
                      </div>
                  </Form>
                </div>

            </div>
          </div>
            

        </div>
        <!-- ===============首页内容-6============== -->

      </div>



      <!-- 尾部 -->
      <app-footer :contentActiveTheme="contentActiveTheme" />
    </div>

    <!-- <div class="river-lodding plan-centre">
        <span class="notes">努力加载中...</span>
		    <ul class="river-lodding-ul">
          <li></li><li></li><li></li><li></li><li></li>
        </ul>
    </div> -->

  </div>

</template>

<script>
import '@/assets/css/index.css'//引入首页样式
import AppHeader from './Header.vue'//引入头部
import AppFooter from './Footer.vue'//引入头部

var scrollFunc = null;

export default {
  name: 'Index',
  data () {
          return {
             mnueMoreOpenFlag:false,
             isShowAllContent:false,//默认加载首页第一个内容
             contentActiveIndex:0,//当前选中内容标识
             contentActiveTheme:'theme-1',//主题
             contentActives:this.initContentActives(),
             indexData:{
               attrs:{
                 links:[//友情链接
                    {title:'程序员工具',icon:'ios-cafe-outline',linkName:'点击链接',href:'https://tool.lu/',notes:'程序员工具有各种在线工具：json、html、xml、js、sql、base64等等工具集合！'},
                    {title:'妙味课堂学习',icon:'ios-bulb-outline',linkName:'点击链接',href:'https://www.miaov.com',notes:'妙味课堂里面有许多学习视频，个人觉得还是挺好的！'},
                    {title:'哔哩哔哩',icon:'ios-mail-outline',linkName:'点击链接',href:'https://www.bilibili.com/',notes:'bilibili各种搞笑鬼畜、学生视频也挺过，个人比较喜欢。'},
                  ]
               },
               notebookCategorys:[],//随笔记录
               projects:[],
               tools:[],
             }
          }
        },
  components:{//注册组件
    AppHeader,
    AppFooter
  },
  beforeCreate(){//拿不到任何信息，无法篡改数据，一般做loding，这个时候的vue实例还什么都没有，但是$route对象是存在的，可以根据路由信息进行重定向之类的操作
    
  },
  created() {//el 没有初始化，数据已加载完成，阔以篡改数据，并更新，不会触发，，在这结束，还做一些初始化，实现函数自执行，ref属性内容为空数组
    
  },
  beforeMount(){//$el已被初始化,，数据已加载完成，阔以篡改数据，并更新，不会触发beforeUpdate，updated，在挂载开始之前被调用，beforeMount之前，会找到对应的template，并编译成render函数
  },
  methods:{//el 已被初始化，数据已加载完成，阔以篡改数据，并更新，并且触发，，在这发起后端请求，拿回数据，配合路由钩子做一些事情，ref属性可以访问
    initData(){

      let topActiveMenuId = this.$refs.refHeader.topActiveMenuId;

      //初始化数据
      let _this = this;
      this.axios.get('/site/public/index/initData', {params:{targetId:topActiveMenuId}})
      .then(function (result) {

        let data = result.data;

        if(result.success){
          let indexAttrs = data.siteAttrs || [];
          
          for(var i in indexAttrs){
              let row =  indexAttrs[i];
              let key = row.attrKey;
              let value = row.attrValue;

              try {

                let jsonValue = JSON.parse(value);
                if(key == 'footer'){
                  window.localStorage.setItem('footer',value);
                }else{
                  _this.indexData.attrs[key] = jsonValue;
                }

              } catch (error) {
                this.$Notice.error({title: '异常',desc: error});
              }
            }
          
          _this.indexData.notebookCategorys = data.reserve.notebookCategory || [];
          _this.indexData.projects = data.reserve.projects || [];
          _this.indexData.tools = data.reserve.tools || [];

        }else{
          this.$Notice.info({title: '提示',desc: result.message});
        }
      })
      .catch(function (error) {
        this.$Notice.error({title: '异常',desc: error});
      });

    },
    initContentActives(){//数据初始化
      var result = new Array(5);
      for(var i=0;i<result.length;i++){
        var className = i==0?'active':'activeUp';
        var theme = 'theme-1';
        if(i==1){
          theme='theme-0  color-white';
        }
        if(i==2){
          theme='theme-0  top-fied';
        }else if(i==4 || i==3){
          theme='theme-1  top-fied';
        }

        result[i] = {className:className,theme:theme};
      }
      return result;
    },
    pageResize(event){ //计算窗口大小，自适应布局

      var e = {
                current: window.innerHeight,
                /* limit: window.innerWidth < 768 ? window.innerWidth < window.innerHeight ? 1280 : 602 : 1600, */
                limit: window.innerWidth < 768 ? window.innerWidth < window.innerHeight ? 1600 : 1600 : 1600,
                /* limit: 1280, */
                scale: 0
              },
          t = {
          current: window.innerWidth,
          limit: window.innerWidth < 768 ? window.innerWidth < window.innerHeight ? 2560 : 2560 : 2560,
          /* limit: 2560, */
          scale: 0
          };
          /* alert(e.limit);
          alert(t.limit); */
          e.scale = e.current / e.limit;
          t.scale = t.current / t.limit;
          var n = 22.5 * Math.min(e.scale, t.scale);
          /* var n = 32 * Math.min(e.scale, t.scale); */
          document.documentElement.style.fontSize = n + "px";
    },
    closeMnue(ev){//关闭右侧菜单
      ev.stopPropagation();
			ev.preventDefault();
      this.mnueMoreOpenFlag = false;
    },
    openMnue(ev){//打开右侧菜单
      ev.stopPropagation();
			ev.preventDefault();
      this.mnueMoreOpenFlag = true;
    },
    rightTabClick(index,type){

      this.isShowAllContent = true;

      if(index>this.contentActiveIndex || type=='down'){
        //改变数组数据立即dom立即生效 两种方式
        /* this.contentActives.splice(index, 1, {className:'activeUp'}); */
        this.$set(this.contentActives, index, {className:'activeUp',theme:this.contentActives[index].theme});

        setTimeout(()=>{
          this.contentActives[this.contentActiveIndex].className='activeDown delay';
          this.contentActives[index].className='active';
          this.contentActiveIndex = index;
          this.contentActiveTheme=this.contentActives[index].theme;
        },100);

      }else if(index<this.contentActiveIndex){
        //改变数组数据立即dom立即生效 两种方式
        /* this.contentActives.splice(index, 1, {className:'activeUp'}); */
        this.$set(this.contentActives, index, {className:'activeDown',theme:this.contentActives[index].theme});
        setTimeout(()=>{
          this.contentActives[this.contentActiveIndex].className='activeUp delay';
          this.contentActives[index].className='active';
          this.contentActiveIndex = index;
          this.contentActiveTheme=this.contentActives[index].theme;
        },100);
      }
    },
    scrollUp(e,this_){
     //向上滚动
     if(this.contentActiveIndex-1 < 0){
       return;
     }
     var nexIndex = this.contentActiveIndex-1 < 0 ? this.contentActives.length-1 : this.contentActiveIndex-1;
     this.rightTabClick(nexIndex);
    },
    scrollDown(e,this_){
     //向下滚动
     var nexIndex = this.contentActiveIndex+1 >= this.contentActives.length? 0 : this.contentActiveIndex+1;
     this.rightTabClick(nexIndex,'down');
    },
    scrollListener(){
      //滚轮滚动事件监听
      var this_ = this;
      scrollFunc=function(e){

        e = e || window.event;
        if (e.wheelDelta) {  //判断浏览器IE，谷歌滑轮事件
            if (e.wheelDelta > 0) { 
              this_.scrollUp(e,this_);
            }
            if (e.wheelDelta < 0) { 
              this_.scrollDown(e,this_);
            }
        } else if (e.detail) {  //Firefox滑轮事件
            if (e.detail> 0) { 
                this_.scrollDown(e,this_);
            }
            if (e.detail< 0) { 
                this_.scrollUp(e,this_);
            }
        }
        
        /*解决滚动时候多次调用问题 */
        document.removeEventListener('DOMMouseScroll',scrollFunc, false);//FireFox
        window.onmousewheel = document.onmousewheel = null;//IE/Opera/Chrome 
        setTimeout(()=>{
          this_.scrollListener();
        },800);

      };
      //给页面绑定滑轮滚动事件
      if (document.addEventListener) {
          document.addEventListener('DOMMouseScroll',scrollFunc, false);
      }
      //滚动滑轮触发scrollFunc方法
      window.onmousewheel = document.onmousewheel = scrollFunc;
      
    },
    toProject(url){
      window.open(url,'_blank');
    }
  },
  mounted(){

    this.pageResize();
    let _this = this;//赋值vue的this
    window.onresize = (e)=>{
　　　　//调用methods中的事件
      _this.pageResize(e);
    };

    this.scrollListener();//滚轮滚动监听

    /* 判断首页第一张图片是否加载完毕 */
    indexContent2Img.onload=function(){
      _this.isShowAllContent = true;
    }
    var timer = setInterval(function() {
        if (indexContent2Img.complete) {
            _this.isShowAllContent = true;
            clearInterval(timer)
        }
    }, 50)
    setTimeout(() => {
      _this.isShowAllContent = true;
    }, 1000*2);



    //后台加载首页数据
    this.initData();

  },
  destroyed(){
    window.onresize = null;

    document.removeEventListener('DOMMouseScroll',scrollFunc, false);
    window.onmousewheel = document.onmousewheel = null;
  }

}
</script>


