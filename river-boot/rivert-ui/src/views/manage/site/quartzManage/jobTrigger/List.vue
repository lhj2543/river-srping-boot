
<style scoped>
.addDepartUser-depart .ivu-form-item-content{
    position: relative;
}

.addDepartUser-depart .ivu-dropdown{
    position: absolute;
    right: 10px;
    top: 0px;
}

.addDepartUser-depart .ivu-select-dropdown{
    top: 30px !important;
    left: -190px !important;
    padding: 5px;
}

.addDepartUser-depart .ivu-dropdown-rel .ivu-icon{
    font-size: 14px;
    color: #808695;
    transition: all .2s ease-in-out;
}

.departVisibleSelectIcon{
    transform: rotateZ(-180deg);
}

.addDepartUser-depart .ivu-dropdown .ivu-tree{
    width: 212px;
}

.addDepartUser-depart .ivu-tree ul li{
    margin: 2px 0;
}
</style>


</style>
<template>
  <div class="content-panel" >

    <div class="content-card" v-if="showPage=='list'">
      <div class="header"  >
        <span class="title">
          <Icon :type="$common.icon.list" />
          job触发器列表
        </span>
        
        <span class="right-tool" style="float:right">
          <Icon title="刷新"  size="18" @click="refresh" class="cursor-pointer"  :type="$common.icon.refresh" />
        </span>
      </div>
      <div class="card-body" style="padding:0 25px;">
        
        <Form ref="searchForm" :model="searchForm" :label-width="120" style="padding:25px 0px 10px 0px;">
          <Row>
            <Col span="6">
              <FormItem label="job名称" prop="jobDisplayName">
                <Input type="text" v-model="searchForm.jobDisplayName" placeholder="请输入job名称..."></Input>
              </FormItem>
            </Col>
            <Col span="6">
              <FormItem label="job组"  prop="targetId">
                  <Input type="text" v-model="searchForm.jobGroup" placeholder="请输入job组..."></Input>
              </FormItem>

              <!-- <Form-Item label="所属部门" class="addDepartUser-depart">
                  <i-input v-model="searchForm.targetId" placeholder="请选择" readonly></i-input>
                  <Dropdown trigger="custom" :visible="departVisible" placement="bottom-end">
                      <a href="javascript:void(0)" @click="departVisible=!departVisible">
                          <Icon type="ios-arrow-down" v-bind:class="{departVisibleSelectIcon:departVisible}"></Icon>
                      </a>
                      <DropdownMenu slot="list">
                          <Tree v-bind:data="departmentTreeData2" @on-select-change="selectDownitem"> </Tree>
                      </DropdownMenu>
                  </Dropdown>
              </Form-Item> -->

            </Col>
           
            
            <Col span="12" style="text-align:right">
                    <Button @click="search('searchForm')" type="info" ghost :icon="$common.icon.search" >检索</Button>
                    <Button @click="formReset('searchForm')" type="info" ghost :icon="$common.icon.clean" >清空</Button>
                    <span >
                      <Button type="info" ghost :icon="$common.icon.add" @click="toAdd">新增</Button>
                      <Button type="error" ghost :icon="$common.icon.delete" @click="deletes(checkedRows)">批量删除</Button>
                    </span>
            </Col>
            
          </Row>
        </Form>

        <!-- 列表 -->
        <Table :loading="loading" class="content-table" height="519" border :columns="columns" :data="row.rows" 
        @on-selection-change = "listSelectChange" @on-sort-change="listSort" ></Table>

        <!-- 分页 -->
        <Page class="content-page" :total="row.total" :current="row.page" :page-size="row.pageSize" :page-size-opts="pageSizeOpts"
        @on-change="pageChange" @on-page-size-change="pageSizeChange" 
        show-total show-sizer show-elevator :transfer="true" />
        
      </div>
        
    </div>

    <!-- 新增/修改/详情 共同页面 -->
    <modify v-if="showPage == 'modify'" v-bind:thisRow="thisRow" v-bind:items="items" v-on:chindrenChangeData="chindrenChangeData"   />

    <!-- 添加用户角色窗口 -->
    <span v-if="isShowLogsModel2">
      <Modal   v-model="isShowLogsModel" @on-visible-change="onVisibleChange" title="运行日志" :footer-hide="true" :scrollable="true" width="70%" >
        <jobLogs v-bind:modalParams="modalParams"  /> 
      </Modal>
    </span>


  </div>

</template>

<script>

  import modify from './Modify.vue'
  import jobLogs from './JobLogs.vue'

  import table from '@/components/Table.js'
  
  let methods_ = {
    getSearchForm(){//检索表单
      let result ={
        jobDisplayName:'',
        jobGroup:'',
        triggerStatus:''
      }
      return result;
    },
    onVisibleChange(flag){
      setTimeout(() => {
        this.isShowLogsModel2 = flag;
      }, 200);
    },
    // 所属部门
    selectDownitem(value) {
        if (Array.isArray(value) && value.length > 0) {
            this.addDepartUserForm.departmentName = value[0].title;
            this.departVisible = false;
        }
    },
    //启动或停止job
    startOrStopJob(params,isStop){

      let row = params.row;
      let url = '/site/quartzJobTrigger/start';
      if(isStop){
        url = '/site/quartzJobTrigger/stop';
      }

      this.loading=true;
      this.axios.post(url,row,{headers:{type:'json'}})
      .then((response)=>{
        this.loading=false;
        this.$Notice.info({title: '提示',desc: response.message});
        if(response.success){
          //加载列表数据
          this.loadListData();
        }
      })
      .catch((error)=>{
        this.loading=false;
        this.$Notice.error({title: '异常',desc: error});
      });
      
    },
    //运行一次
    runOne(params){
      let row = params.row;
      this.loading=true;
      this.axios.post('/site/quartzJobTrigger/runOne',row,{headers:{type:'json'}})
      .then((response)=>{
        this.loading=false;
        this.$Notice.info({title: '提示',desc: response.message});
      })
      .catch((error)=>{
        this.loading=false;
        this.$Notice.error({title: '异常',desc: error});
      });

    },
    //运行日志
    toJobLogs(params){

      this.modalParams = params.row;
      this.isShowLogsModel2 = true;
      this.isShowLogsModel = true;

    }
    
  };
  let methods = Object.assign({},table.methods,methods_);

  //let tableHandle = Object.assign({},table.handle,{});

  

  export default {
    name: 'jobTriggerList',
    data () {
      
      //列表按钮
      let listButton = {title: '',width:'1'};

      listButton={
            title: '操作',
            key: 'action',
            width: 380,
            align: 'center',
            render: (h, params) => {

                let row = params.row;
                let triggerStatus = row.triggerStatus;

                let btn,edit,runOne;
                if(triggerStatus==='3' || triggerStatus==='1'){
                  btn = h('Button', {
                        props: {
                            type: 'primary',
                            size: 'small'
                        },
                        style: {
                            marginRight: '5px'
                        },
                        on: {
                            click: () => {
                              this.startOrStopJob(params,false);
                            }
                        }
                    }, '启动');

                  runOne =h('Button', {
                        props: {
                            type: 'primary',
                            size: 'small'
                        },
                        style: {
                            marginRight: '5px'
                        },
                        on: {
                            click: () => {
                              this.runOne(params);
                            }
                        }
                    }, '运行一次');

                  edit = h('Button', {
                        props: {
                            type: 'primary',
                            size: 'small',
                            //icon: this.$common.icon.delete
                        },
                        style: {
                            marginRight: '5px'
                        },
                        on: {
                            click: () => {
                                this.toModify(params)
                            }
                        }
                    }, '修改');

                }else{
                  btn = h('Button', {
                        props: {
                            type: 'primary',
                            size: 'small'
                        },
                        style: {
                            marginRight: '5px'
                        },
                        on: {
                            click: () => {
                              this.startOrStopJob(params,true);
                            }
                        }
                    }, '停止');
                }

                return h('div', [
                    runOne,
                    btn,
                    h('Button', {
                        props: {
                            type: 'primary',
                            size: 'small'
                        },
                        style: {
                            marginRight: '5px'
                        },
                        on: {
                            click: () => {
                              this.toJobLogs(params);
                            }
                        }
                    }, '日志'),
                    h('Button', {
                        props: {
                            type: 'primary',
                            size: 'small'
                        },
                        style: {
                            marginRight: '5px'
                        },
                        on: {
                            click: () => {
                                this.toDetail(params)
                            }
                        }
                    }, '详情'),
                    edit,
                    h('Button', {
                        props: {
                            type: 'error',
                            size: 'small'
                        },
                        on: {
                            click: () => {
                                this.deletes([params.row])
                            }
                        }
                    }, '删除')
                ]);
            }
        };
      
      let data_ = {
        items:{},//字典数据
        statusFilters:[],//状态过滤数组
        queryUrl:'/site/quartzJobTrigger/query',//列表后台url
        delUrl:'/site/quartzJobTrigger/deletes',//删除后台url
        searchForm:this.getSearchForm(),//检索表单
        isShowLogsModel:false,//运行日志窗口
        isShowLogsModel2:false,//运行日志窗口
        modalParams:{},//日志窗口参数
        
        departVisible:false,
        departmentTreeData2:[],

        row:{//列表数据
          total:0,
          page:1,
          pageSize:10,
          rows: []
        },
        columns: [
              {
                  type: 'selection',/* 复选框 */
                  width: 60,
                  align: 'center'
              },
              {
                  title: 'job名称', 
                  key: 'jobDisplayName', 
                  dbKey:'',
                  width:'300',
                  sortable: false, /* 是否排序 */
                  resizable: true,/* 是否可拖拽宽度 */
              },
              {
                  title: 'job组',
                  key: 'jobGroup', 
                  dbKey:'',
                  width:'200',
                  sortable: false, /* 是否排序 */
                  resizable: true,// 是否可拖拽宽度 
              },
              {
                  title: 'job类型',
                  key: 'jobType', 
                  dbKey:'',
                  width:'200',
                  sortable: false, /* 是否排序 */
                  resizable: true,// 是否可拖拽宽度 
                  render:(h,params)=>{
                    return h('span',{

                    },this.items['quartz_job_type']?this.items['quartz_job_type'][params.row.jobType] || params.row.jobType : params.row.jobType);
                  },
              },
              {
                  title: 'cron表达式',
                  key: 'cronExpression', 
                  dbKey:'',
                  width:'200',
                  sortable: false, /* 是否排序 */
                  resizable: true,// 是否可拖拽宽度 
              },
              {
                  title: '状态',
                  key: 'triggerStatus',
                  width:'120',
                  resizable: true,// 是否可拖拽宽度 
                  render:(h,params)=>{
                    let row = params.row;
                    let color = row.triggerStatus==='3'?'#f60':'#2d8cf0';
                    return h('span',{
                        style:{
                          color:color
                        }
                    },this.items['quartz_trigger_status']? this.items['quartz_trigger_status'][params.row.triggerStatus] || params.row.triggerStatus : params.row.triggerStatus);
                  },
                  filters: [// 过滤器 
                            {
                                label: '就绪',
                                value: '1'
                            },
                            {
                                label: '运行中',
                                value: '2'
                            },
                            {
                                label: '已停止',
                                value: '3'
                            },
                            {
                                label: '运行异常',
                                value: '4'
                            },
                        ],
                  filterMultiple: false,//过滤器是否可多选
                  /* filterMethod (value, row) {//过滤器触发函数
                      console.log(row);
                      console.log(value);
                      return true;
                  }, */
                  filteredValue:[],//在初始化时使用过滤，数组，值为需要过滤的 value 集合
                  filterRemote (value, row,c) {//后台过滤
                      /* console.log(row);
                      console.log(value); */
                      if(value.length>0){
                        this.searchForm.triggerStatus = value[0];
                      }else{
                        this.searchForm.triggerStatus = '';
                      }
                      //this.statusFilteredValue=value;
                      this.loadListData();
                  }
              },
              {
                  title: 'job描述',
                  key: 'jobDesc', 
                  dbKey:'',
                  sortable: false, /* 是否排序 */
                  resizable: true,// 是否可拖拽宽度 
              },
              {
                  title: '更新时间',
                  key: 'updateDate',
                  dbKey:'update_date',
                  width:'120',
                  sortable: false, /* 是否排序 */
                  resizable: true,/* 是否可拖拽宽度 */
              },
              listButton,//列表按钮
          ],
        
      
      }
      
      let data = Object.assign({},table.data,data_);//继承 自定义table组件属性 
      return data;
    },
    components:{//注册组件
      modify,
      jobLogs
    },
    props:{
      
    },
    watch:{
      
    },
    beforeCreate(){//拿不到任何信息，无法篡改数据，一般做loding，这个时候的vue实例还什么都没有，但是$route对象是存在的，可以根据路由信息进行重定向之类的操作
      
    },
    created() {//el 没有初始化，数据已加载完成，阔以篡改数据，并更新，不会触发，，在这结束，还做一些初始化，实现函数自执行，ref属性内容为空数组
      

    },
    beforeMount(){//$el已被初始化,，数据已加载完成，阔以篡改数据，并更新，不会触发beforeUpdate，updated，在挂载开始之前被调用，beforeMount之前，会找到对应的template，并编译成render函数
    },
    //el 已被初始化，数据已加载完成，阔以篡改数据，并更新，并且触发，，在这发起后端请求，拿回数据，配合路由钩子做一些事情，ref属性可以访问
    methods:methods,

    mounted(){

      /* 加载页面上所有用到的字典 */
      let params = {categoryNames:['quartz_job_type','quartz_trigger_status']};
      this.getSysItems(params,(data)=>{
        this.items = data.itemMap?data.itemMap:{};
      });

      //加载列表数据
      this.loadListData();
      
    },
    destroyed(){
    
    }

  }

</script>