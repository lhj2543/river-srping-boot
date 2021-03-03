<template>

  <div class="site-main-content" >

    <!-- 头部 -->
    <!--或者这种写法也行<AppHeader/> -->
    <app-header :contentActiveTheme="contentActiveTheme" />

    <!-- ============================ -->
    <div class="content-list" >
      
      <div class="left-layout">

          <br/>

      </div>
      <div class="right-layout" ref="rightLayout">
          
          <div class="right-layout-centent">

              <div v-if="showPage!='detail'" style="padding-bottom:60px;opacity: 1; ">


                  <div class="list-search">

                    <div class="search-head" style="padding-bottom:50px;">
                     
                      <a class="search-input">
                        <input name="searchValue" v-model="searchForm.searchValue" placeholder="请输入关键字..." @keydown="enterKey" />
                        <i @click="pageChange(1)" class="ivu-icon ivu-icon-ios-search" />
                      </a>

                    </div>

                  </div>
                  
                  <div style="width:100%;text-align:center;background:#fff;padding:100px 0;margin:20px auto;" v-if="row.rows.length <=0 ">
                      <h3>暂无数据</h3>
                  </div>


                  <!-- ============列表================== -->
                  <div  class="custom-list" style="background:#fff;border:none;" v-for="(item,index) in row.rows" :index="index" :key="item.sid" >

                      <div>
                          <span class="title">{{item.title}}</span>
                          <span class="right-tool">
                          </span>
                      </div>
                      <div class="content" @click="toDetail(item.sid,index)"  v-html="item.bodys"></div>
                      <div class="buttom">
                          <span> 
                              <Icon type="ios-star-outline" color="#f60" /> 123   &nbsp;   &nbsp; 
                              <Icon type="ios-thumbs-up-outline" color="#f60" /> 234  &nbsp;   &nbsp; 
                              <Icon type="ios-chatbubbles-outline" color="#f60" /> 345  &nbsp;   &nbsp; 
                          </span>
                          <span class="right-tool"> 
                              <Icon type="ios-contact-outline" size="17" /> {{item.createdBy}}  &nbsp; 
                              <Icon type="ios-time-outline" size="17" /> {{item.createdDate}}
                          </span>
                      </div>
                  </div>
                  <!-- ============列表================== -->

                  <!-- 分页 -->
                  <Page v-if="row.rows.length >0 " class="content-page-bottonm" :total="row.total" :current="row.page" :page-size="row.pageSize" :page-size-opts="pageSizeOpts"
                  @on-change="pageChange" @on-page-size-change="pageSizeChange" 
                  show-total show-sizer show-elevator :transfer="true" />

              </div>

              <div v-if="showPage=='detail'">

                <detail   ref="detailPage" v-bind:detailParams="detailParams" v-on:chindrenChangeData="chindrenChangeData" />

                <!-- 关闭详情 -->
                <div class="closeDetail" title="返回" @click="chindrenChangeData({showPage:'list'})" ref="refBackList">
                  
                  <Icon :type="$common.icon.close" />

                </div>

              </div>
              

            
              <!-- 返回顶部 -->
              <div class="back-top" title="返回顶部" @click="backTop()" ref="refBackTop">
                <i class="ivu-icon ivu-icon-ios-arrow-up"></i>
              </div>

              <!-- loadding  -->
              <Spin fix v-if="loading">
                  <Icon type="ios-loading" size=18 class="spin-icon-load"></Icon>
                  <div>数据加载中...</div>
              </Spin>
              

          </div>

      </div>
      

    </div>
    <!-- ============================ -->


    <!-- 尾部 -->
    <!-- <app-footer :contentActiveTheme="contentActiveTheme" /> -->

  </div>

</template>

<script>
import $ from 'jquery'
import '@/assets/css/site.css'
import AppHeader from '@/views/websit/Header.vue'//引入头部
import AppFooter from '@/views/websit/Footer.vue'//引入头部
import Detail from '@/views/websit/notebook/Detail.vue'


export default {
  name: 'notebook',
  data () {
          return {
            contentActiveTheme:'theme-0  top-fied',//主题
            clickNode:{},
            nodes:[],

            loading:false,
            checkSid:'',//选择的分类
            isAdd:false,
            row:{//列表数据
                total:0,
                page:1,
                pageSize:20,
                rows: []
            },
            pageSizeOpts:[5,10, 20, 50, 100],//列表每页显示多少条数据下拉框
            searchForm:{searchValue:''},//检索表单
            showPage:'',
            detailParams:{},
            rightLayout:null,
            toDetailScrollTop:0,//进入详情页面的高度
          }
        },
  components:{//注册组件
    AppHeader,
    AppFooter,
    Detail
  },
  beforeCreate(){//拿不到任何信息，无法篡改数据，一般做loding，这个时候的vue实例还什么都没有，但是$route对象是存在的，可以根据路由信息进行重定向之类的操作
    
  },
  created() {//el 没有初始化，数据已加载完成，阔以篡改数据，并更新，不会触发，，在这结束，还做一些初始化，实现函数自执行，ref属性内容为空数组

    let params = this.$route.params || {};
    this.checkSid = params.sid || '';

    this.search();//加载所有笔记列表

  },
  beforeMount(){//$el已被初始化,，数据已加载完成，阔以篡改数据，并更新，不会触发beforeUpdate，updated，在挂载开始之前被调用，beforeMount之前，会找到对应的template，并编译成render函数
 
  },
  methods:{//el 已被初始化，数据已加载完成，阔以篡改数据，并更新，并且触发，，在这发起后端请求，拿回数据，配合路由钩子做一些事情，ref属性可以访问
    //后台数据
    initData(params){
      
      let _this = this;
      
      this.axios.get('/site/public/notebook/getNotebookCategoryTree', {params:{}})
      .then(function (response) {
        
        if(response.success){
          
          let data = response.data;
          _this.nodes = data;
          _this.initTree();

        }else{
           this.$Notice.info({title:'提示',desc: response.message});
        }

      })
      .catch(function (error) {
        this.$Notice.error({title:'异常',desc: error});
      });



    },
    //笔记数据加载
    pageChange(page){
        this.row.page = page;
        this.search();
    },
    pageSizeChange(pageSize){
        this.row.pageSize = pageSize;
        this.search();
    },
    search(params){//检索
        this.loadNoteBooks({categoryId:this.checkSid,searchValue:this.searchForm.searchValue});
    },
    loadNoteBooks(params){
      let _this = this;

      params.page={
        size:this.row.pageSize?this.row.pageSize:20,
        current:_this.row.page,
      };

      this.loading = true;

      this.axios.post('/site/public/notebook/getNotebook',params,{headers:{type:'json'}})
      .then((response)=>{

          _this.loading = false;
          
          if(response.success){

              let row = response.data;
              let rows = row.records;
              _this.row.total = row.total;
              _this.row.rows = rows;

              //_this.row = row;
          }else{
             this.$Notice.info({title:'提示',desc: response.message});
          }

      })
      .catch((error)=>{
          _this.loading=false;
          this.$Notice.error({title:'异常',desc: error});
      });
    },
    toDetail(sid,index){
      
      this.toDetailScrollTop = this.rightLayout.scrollTop;
      this.showPage='detail';
      this.detailParams ={sid:sid,index:index};
        
    },

    chindrenChangeData(params){/* 子类修改父级数据调用方法 */
        this.showPage=params.showPage;

        let _this = this;
        setTimeout(() => {
          /* if(_this.$refs.connectListBody){
            _this.$refs.connectListBody.style.opacity = 1;
          } */
          this.rightLayout.scrollTop = _this.toDetailScrollTop;
        }, 100);
    },

    //目录树初始化
    initTree(){

      const nodeHtml= $('<div class="river-node"></div>');

      let nodes = this.nodes;

      for(let i in nodes){
        let row = nodes[i];
        let id = row.sid;
        let title = row.displayName;
        let isExpand = row.expand;
        let children = row.children;

        let openClass = '';
        if(isExpand){
            openClass = ' ivu-tree-arrow-open river-arrow-open ';
        }

        nodeHtml.append('<div class="left-layout-menu" id="'+id+'_node"><div class="left-layout-menu-b '+openClass+'" id="'+id+'"><div class="root-title"><i class="arrow-open-icon ivu-icon ivu-icon-ios-arrow-forward"></i> <span class="title">'+title+'</span></div></div></div>');

        $('#'+id,nodeHtml).data(id,row);

        //节点展开收缩事件
        $('#'+id+' .arrow-open-icon',nodeHtml).on('click',(event)=>{
          let _this = $(event.target);
          let node = _this.parents('.left-layout-menu-b');
          
          let data = node.data(node.attr('id'));
          this.clickNode = data;

          let isOpen = node.hasClass('ivu-tree-arrow-open');
          
          if(isOpen){
            node.removeClass('ivu-tree-arrow-open');
            $('.left-layout-menu',node).slideUp(250);
          }else{
            node.addClass('ivu-tree-arrow-open');
            $('.left-layout-menu',node).slideDown(250);
          }


          //阻止冒泡
          event.preventDefault();
					event.stopPropagation();
        });

        $('#'+id+' .root-title .title',nodeHtml).on('click',(event)=>{
          let _this = $(event.target);
          let node = _this.parents('.left-layout-menu-b');

          this.nodeClick(_this,node,event);
        });

        if(children && children.length>0 ){
            this.recursion(children,row,nodeHtml);
        }

      }

      $('.left-layout').append(nodeHtml);
      if(this.checkSid){
        $('.title','#'+this.checkSid).eq(0).addClass('active');
      }

    },
    //迭代树
    recursion(nodes,node,nodeHtml,level){
      
      let parentId = node.sid;
      let nodeParentId = node.parentId;
      

      for(let i in nodes){
        let row = nodes[i];
        let id = row.sid;
        let title = row.displayName;
        let isExpand = row.expand;
        let children = row.children;

        let openClass = '';
        if(isExpand){
            openClass = 'ivu-tree-arrow-open';
        }
        
        let linkClass = '';
        if(level != 3 && (!children || !children.length || children.length<=0) ){
          linkClass = ' link ';
        }
        
        let lastLeveClass = '';
        if(level == 3){
          lastLeveClass = ' last-leve ';
        }

        $('#'+parentId,nodeHtml).append('<div class="left-layout-menu '+lastLeveClass+linkClass+'" id="'+id+'_node" style="text-indent:15px"><div class="left-layout-menu-b" id="'+id+'"><div class="title">'+title+'</div></div></div>');

        $('#'+id,nodeHtml).data(id,row);

        $('#'+id,nodeHtml).on('click',(event)=>{
          let _this = $(event.target);
          let node = _this.parent('.left-layout-menu-b');
          this.nodeClick(_this,node,event);
          
        });

        
        if(children && children.length>0 ){
            this.recursion(children,row,nodeHtml,3);
        }

      }

    },
    nodeClick(_this,node,event){//节点点击
      let data = node.data(node.attr('id'));
      this.clickNode = data;


      this.showPage = 'list';
      if(!_this.hasClass('active')){
        this.checkSid = data.sid;
        this.row.page = 1;
        this.loadNoteBooks({categoryId:data.sid});//加载分类下所有笔记
      }

      $('.left-layout-menu .title').removeClass('active');
      _this.addClass('active');

      //阻止冒泡
      event.preventDefault();
      event.stopPropagation();
    },
    //返回顶部
    backTop(){
      this.$common.backTop(this.rightLayout);
    },
    //enter 键盘事件
    enterKey(e){
      if (e.keyCode == 13) {
        this.pageChange(1);
      }
    }

  },
  mounted(){

    //数据初始化
    this.initData();

    // 通过$refs获取dom元素
    this.rightLayout = this.$refs.rightLayout;
    // 监听这个dom的scroll事件
    this.rightLayout.addEventListener('scroll', () => {

      var scrollTop = this.$refs.rightLayout.scrollTop;

      if(scrollTop >200){

        this.$refs.refBackTop.style.opacity='1';

        if(this.showPage == 'detail'){
          this.$refs.refBackList.style.opacity='1';
        }

      }else{
        this.$refs.refBackTop.style.opacity='0';

         if(this.showPage == 'detail'){
           this.$refs.refBackList.style.opacity='0';
         }

      }
    });
    
  },
  destroyed(){
   
   /* this.rightLayout.removeEventListener('scroll'); */
   
  }

}
</script>

