<style>

</style>

<template>

  <div class="site-main-content" style="height:auto;">

    <!-- 头部 -->
    <!--或者这种写法也行<AppHeader/> -->
    <app-header :contentActiveTheme="contentActiveTheme" />

    <!-- ============================ -->
    <div class="content-list" >

      <div class="center-panel" v-if="showPage!='detail'">

        <div class="list-search">

          <div class="search-head">
            <span class="category" v-for="(item, key) in searchData.category" :key="key" v-text="item" :class="searchData.checkData.category[key]" @click="searchCheck('category',key,false)"> </span>

            <a class="search-input">
              <input name="searchValue" v-model="searchForm.searchValue" placeholder="请输入关键字..." @keydown="enterKey" />
              <i @click="reloadList()" class="ivu-icon ivu-icon-ios-search" />
            </a>
          </div>
          <div class="search-bodys">
            
            <div class="search-bodys-list">
              <span style="padding:0 0 0 20px;">标签：</span>

              <span class="search-bodys-list-span">
                <span class="taging" v-for="(item, key) in searchData.tag" :key="key" v-text="item" :class="searchData.checkData.tag[key]" @click="searchCheck('tag',key,true)"></span>
              </span>
            </div>

            <div class="search-bodys-list">
              <span style="padding:0 0 0 20px;">排序：</span>

              <span class="search-bodys-list-span">
                <span class="orders" v-for="(item, key) in searchData.orders" :key="key" v-text="item" :class="searchData.checkData.orders[key]" @click="searchCheck('orders',key,false)"></span>
              </span>
            </div>

          </div>

        </div>

          <div class="connect-list-body" ref="connectListBody">

              <div  @click="( (item.category=='project') && toDetail({sid:item.sid},$event) ) || ( (item.category!='project') && toHref(item.url,$event) )" class="list-panel" v-for="(item, index) in row.records" :key="index" :style="{marginRight:((index+1)%3==0?'0px':'20px')}">

                <div class="list-panel-img" >
                  <img width="100%" height="100%" :src="item.img?$globSetting.attachUrl+item.img:''" onerror="this.src='/static/default/loadError1.jpg'" />
                </div>

                <div class="list-panel-info">

                  <div @click="(item.category=='project' && item.url) && toHref(item.url,$event)" :title="(item.category=='project' && item.url) && item.url" 
                    class="title" :class="{'link':(item.category=='project')}" v-text="item.title">
                  </div>

                  <div class="notes" v-html="item.notes">
                  </div>
                </div>

              </div>


              <!-- 滚动条下拉加载 -->
              <div class="scrollLoadding" ref="scrollLoadding">
                <i class="spin-icon-load ivu-icon ivu-icon-ios-loading" style="font-size: 18px;"></i>
                 <div>数据加载中...</div>
              </div>

              <div v-if="!hasListData" style="color: #0093ff;text-align:center;width:100%;padding:20px;background:#fff;">
                没有更多数据了...
              </div>

          </div>

      </div>

      <div class="connect-detail" v-if="showPage =='detail'">
        <Detail ref="detailPage" v-bind:detailParams="detailParams" v-on:chindrenChangeData="chindrenChangeData"  />

         <!-- 关闭详情 -->
        <div class="closeDetail" style="opacity:0;" title="返回" @click="chindrenChangeData({showPage:'list'})" ref="refBackList">
          
          <Icon :type="$common.icon.close" />

        </div>
      </div>





      <!-- 返回顶部 -->
      <div class="back-top"  title="返回顶部" @click="backTop()" ref="refBackTop">
        <i class="ivu-icon ivu-icon-ios-arrow-up"></i>
      </div>

      <!-- loadding  -->
      <Spin fix v-if="initLoading">
          <Icon type="ios-loading" size=18 class="spin-icon-load"></Icon>
          <div>数据加载中...</div>
      </Spin>


       <!-- 尾部 -->
      <app-footer :contentActiveTheme="contentActiveTheme" />

    </div>


   

  </div>

</template>

<script>
import '@/assets/css/site.css'
import AppHeader from '@/views/websit/Header.vue'//引入头部
import AppFooter from '@/views/websit/Footer.vue'//引入头部
import Detail from '@/views/websit/connect/Detail.vue'


export default {
  name: 'Login',
  data () {
          return {
            contentActiveTheme:'theme-0  top-fied',//主题
            loading:false,
            initLoading:false,//初始化loadding
            hasListData:true,//是否还存在数据
            toDetailScrollTop:0,//进入详情页面的高度
            showPage:'list',
            detailParams:{},
            searchData:{
              category:{
                all:'全部',
                project:'项目',
                demo:'Demo',
                tool:'工具'
              },
              tag:{
                all:'全部',
                web:'web特效',
                bigData:'大数据',
                workFlow:'工作流'
              },
              orders:{
                all:'默认',
                toping:'置顶',
                visits:'访问量最多',
                created_date:'最新发布'
              },
              checkData:{//选中的数据
                'category':{},
                'tag':{},
                'orders':{}
              }
            },

            //搜索表单
            searchForm:{
              page:{
                size:9,
                current:1,
                orders:[],
              },
              category:'',
              tags:[],
              searchValue:''
            },
            row:{//列表数据
              records:[],
              total:0
            },

          }
        },
  components:{//注册组件
    AppHeader,
    AppFooter,
    Detail
  },
  props:{
    propParams:Object
  },
  beforeCreate(){//拿不到任何信息，无法篡改数据，一般做loding，这个时候的vue实例还什么都没有，但是$route对象是存在的，可以根据路由信息进行重定向之类的操作
    
  },
  created() {//el 没有初始化，数据已加载完成，阔以篡改数据，并更新，不会触发，，在这结束，还做一些初始化，实现函数自执行，ref属性内容为空数组
    if(this.propParams && this.propParams.category){
     if(this.propParams.category!='all'){
       this.searchForm.category = this.propParams.category;
     }
     this.searchData.checkData.category[this.propParams.category]='active';
    }
    
    this.initLoading = true;
    this.loadList();
  },
  beforeMount(){//$el已被初始化,，数据已加载完成，阔以篡改数据，并更新，不会触发beforeUpdate，updated，在挂载开始之前被调用，beforeMount之前，会找到对应的template，并编译成render函数
 
  },
  methods:{//el 已被初始化，数据已加载完成，阔以篡改数据，并更新，并且触发，，在这发起后端请求，拿回数据，配合路由钩子做一些事情，ref属性可以访问

     loadList(params){
       let p = params||{};
       let isReload = p.isReload;
      
      let _this = this;
      this.loading = true;

      this.axios.post('/site/public/siteConnent/list',this.searchForm,{headers:{type:'json'}})
      .then((response)=>{

          _this.loading = false;
          _this.initLoading = false;
          if(_this.$refs.scrollLoadding){
            _this.$refs.scrollLoadding.style.display='none';
          }
          document.documentElement.scrollTop = document.documentElement.scrollTop-20;
          
          if(response.success){
              let records = response.data.records;
              if(isReload){
                _this.row.records = records;
              }else{
                _this.row.records.push(...records);
              }
              _this.row.total = response.data.total;

              if(!records || records.length<=0){
                _this.hasListData = false;
              }

          }else{
              this.$Notice.info({title:'提示',desc: response.message});
          }

      })
      .catch((error)=>{
          _this.loading=false;
          _this.initLoading = false;
          this.$Notice.error({title:'异常',desc: error});
      });

     },
     //检索选中触发
     searchCheck(type,key,isCheckbox){

       let checkData = this.searchData.checkData[type];

       let checkd = checkData[key];

       if((key=='all') && checkd && checkd=='active'){
         return;
       }

       if(type=='category' && checkd && checkd=='active' ){
         return
       }

      if(type=='category' && (!checkd || checkd!='active') ){
        let obj = {};
        obj[key]='active';
        this.$set(this.searchData.checkData,type,obj);
        this.$set(this.searchData.checkData,'tag',{});
        this.$set(this.searchData.checkData,'orders',{});
      }else if((key=='all') && (!checkd || checkd!='active')){
        let obj = {};
        obj[key]='active';
        this.$set(this.searchData.checkData,type,obj);
        //this.searchData.checkData[type] = {key:'active'};
      }else if(checkd){
        this.$set(this.searchData.checkData[type],key,false);
      }else if(!checkd){

        if(!isCheckbox){
          let obj = {};
          obj[key] = 'active'
          this.$set(this.searchData.checkData,type,obj);
        }else{
          this.$set(this.searchData.checkData[type],key,'active');
        }
        this.$set(this.searchData.checkData[type],'all',false);

      }

      //检索列表
      for(var key in this.searchData.checkData){
        let row = this.searchData.checkData[key];

        if(key!='tag'){

          this.searchForm.page.orders = [];

          for(var i in row){
            if(row[i]=='active'){
              if(i=='all'){
                if(key=='category'){
                  this.searchForm.category = '';
                }
                if(key=='orders'){
                   this.searchForm.page.orders = [];
                }
                
              }else{
                if(key=='category'){
                  this.searchForm.category = i;
                }
                if(key=='orders'){
                   this.searchForm.page.orders = [
                     {column:i,asc:false}
                   ];
                }
              }
              break;
            }
          }

        }

      }

      this.reloadList();

     },
     //返回顶部
    backTop(){
      this.$common.backTop(document.documentElement||document.body);
    },
    //详情页
    toDetail(params,event){

      //阻止冒泡
      event.preventDefault();
      event.stopPropagation();

      this.toDetailScrollTop = document.documentElement.scrollTop || document.body.scrollTop;

      let sid = params.sid;
      this.showPage='detail';
      this.detailParams ={sid:sid};

    },
    chindrenChangeData(params){/* 子类修改父级数据调用方法 */
        this.showPage=params.showPage;
        
        let _this = this;
        setTimeout(() => {

          if(_this.$refs.connectListBody){
            _this.$refs.connectListBody.style.opacity = 1;
          }

          document.documentElement.scrollTop = _this.toDetailScrollTop;
          document.body.scrollTop = _this.toDetailScrollTop;
        }, 100);
        
    },
    //新窗口
    toHref(url){
      //阻止冒泡
      event.preventDefault();
      event.stopPropagation();

      //window.location.href = url ; 
      window.open(url,"_blank");
    },
    reloadList(){
      this.searchForm.page.current = 1;
      
      this.hasListData = true;
      this.loadList({isReload:true});
    },
    //enter 键盘事件
    enterKey(e){
      if (e.keyCode == 13) {
        this.reloadList();
      }
    }



  },
  mounted(){

    
    // 监听这个dom的scroll事件
    let _this = this;
    document.addEventListener('scroll', () => {
      
      let scrollTop = document.documentElement.scrollTop || document.body.scrollTop;
      let clientHeight = document.documentElement.clientHeight || document.body.clientHeight;
      let scrollHeight = document.documentElement.scrollHeight || document.body.scrollHeight;

      /* 返回顶部 */
      if(scrollTop >200){
        if(this.$refs.refBackTop){
          this.$refs.refBackTop.style.opacity='1';
          if(this.showPage == 'detail'){
            this.$refs.refBackList.style.opacity='1';
          }
        }
      }else{
        if(this.$refs.refBackTop){
          this.$refs.refBackTop.style.opacity='0';
          if(this.showPage == 'detail'){
            this.$refs.refBackList.style.opacity='0';
          }
        }
      }

      //避免没有数据的时候 重复执行 scrollHeight > clientHeight 
      if(this.loading==false && this.hasListData && scrollHeight > clientHeight && scrollTop + clientHeight >= scrollHeight-10) {
        
        let countPage = this.row.total%this.searchForm.page.size==0?this.row.total/this.searchForm.page.size:parseInt(this.row.total/this.searchForm.page.size)+1;
        if(this.searchForm.page.current+1>countPage){
          this.hasListData = false;
          return ;
        }else{
          this.searchForm.page.current ++;
        }

        if(_this.$refs.scrollLoadding){
          _this.$refs.scrollLoadding.style.display='block';
        }
        this.loading = true;

        
        this.loadList();

      }

    });
    
  },
  destroyed(){
    
    //document.removeEventListener('scroll');

  }

}
</script>
