
<template>
  <div class="content-panel" >

    <div class="content-card" v-if="showPage=='list'">
      <div class="card-body" style="padding:0 25px;">
        
        <Form ref="searchForm" :model="searchForm" :label-width="80" label-position="left" style="padding:25px 0px 10px 10px;">
          <Row>
            <Col span="6">
              <FormItem label="运行状态" prop="status">
                <Select  v-model="searchForm.status" placeholder="请选择运行状态..."  >
                    <Option  v-for="(value,key) in items['state']" :key="key+'-'+value" :value="key" >{{value}}</Option>
                </Select>
              </FormItem>
            </Col>
            
            <Col span="18" style="text-align:right">
                    <Button @click="search('searchForm')" type="info" ghost :icon="$common.icon.search" >检索</Button>
                    <Button @click="formReset('searchForm')" type="info" ghost :icon="$common.icon.clean" >清空</Button>
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

  </div>

</template>

<script>

  import table from '@/components/Table.js'
  
  let methods = Object.assign({},table.methods,{
    getSearchForm(){//检索表单
        
        let result ={
         status:'',
         targetId:this.modalParams.triggerSid
        }
        return result;
      },
  });

  //let tableHandle = Object.assign({},table.handle,{});

  export default {
    name: 'jobLogsList',
    data () {

      let data_ = {
        queryUrl:'/site/quartzJobTrigger/jobLogs',//列表后台url
        searchForm:this.getSearchForm(),//检索表单
        items:{},//字典数据
        row:{//列表数据
          total:0,
          page:1,
          pageSize:10,
          rows: []
        },
        columns: [
              {
                  title: 'job名', 
                  key: 'jobName', 
                  dbKey:'job_name',
                  sortable: true, /* 是否排序 */
                  resizable: true,/* 是否可拖拽宽度 */
              },
              {
                  title: '运行时间',
                  key: 'runTime', 
                  dbKey:'run_time',
                  sortable: true, /* 是否排序 */
                  resizable: true,/* 是否可拖拽宽度 */
                  render:(h,params)=>{
                    return h('span',{
                    },params.row.runTime+' ms');
                  },
              },
              {
                  title: '开始运行时间',
                  key: 'startTime',
                  dbKey:'start_time',
                  sortable: true, /* 是否排序 */
                  resizable: true,/* 是否可拖拽宽度 */
              },
              {
                  title: '运行结束时间',
                  key: 'endTime',
                  dbKey:'end_time',
                  sortable: true, /* 是否排序 */
                  resizable: true,/* 是否可拖拽宽度 */
              },
              {
                  title: '运行状态',
                  key: 'status',
                  dbKey:'status',
                  resizable: true,/* 是否可拖拽宽度 */
                  render:(h,params)=>{
                    let color = params.row.status=='1'?'#57c5f7':'red';
                    return h('span',{
                      attrs:{
                        style:'color:'+color+';'
                      }
                    },this.items['state']?this.items['state'][params.row.status] || params.row.status : params.row.status);
                  },
              },
              {
                  title: '异常信息',
                  key: 'exception',
                  dbKey:'exception',
                  sortable: true, /* 是否排序 */
                  resizable: true,/* 是否可拖拽宽度 */
              },
              {
                  title: '创建时间',
                  key: 'createdDate',
                  dbKey:'created_date',
                  sortable: true, /* 是否排序 */
                  resizable: true,/* 是否可拖拽宽度 */
              },
              
          ],
        
      
      }
      let data = Object.assign({},table.data,data_);//继承 自定义table组件属性 

      return data;
    },
    props:{
      modalParams:Object
    },
    components:{//注册组件
      
    },
    beforeCreate(){//拿不到任何信息，无法篡改数据，一般做loding，这个时候的vue实例还什么都没有，但是$route对象是存在的，可以根据路由信息进行重定向之类的操作
      
    },
    created() {//el 没有初始化，数据已加载完成，阔以篡改数据，并更新，不会触发，，在这结束，还做一些初始化，实现函数自执行，ref属性内容为空数组
      
       /* 加载页面上所有用到的字典 */
      let params = {categoryNames:['state']};
      this.getSysItems(params,(data)=>{
        this.items = data.itemMap?data.itemMap:{};
      });

      //加载列表数据
      this.loadListData();

    },
    beforeMount(){//$el已被初始化,，数据已加载完成，阔以篡改数据，并更新，不会触发beforeUpdate，updated，在挂载开始之前被调用，beforeMount之前，会找到对应的template，并编译成render函数
    },
    //el 已被初始化，数据已加载完成，阔以篡改数据，并更新，并且触发，，在这发起后端请求，拿回数据，配合路由钩子做一些事情，ref属性可以访问
    methods:methods,

    mounted(){
      
    },
    destroyed(){
    
    }

  }

</script>