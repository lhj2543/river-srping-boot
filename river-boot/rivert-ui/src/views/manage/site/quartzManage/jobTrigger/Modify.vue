<style>
  .user-modify-panel .ivu-tabs-tabpane{
    max-height:590px;
    overflow-X: auto;
  }
  .user-modify-panel .ivu-tabs-nav{
    height:60px;
    line-height:40px;
  }
</style>
<template>
  
  <div class="content-panel" >

    <div class="content-card">
      <div class="header" >
        <span class="title" v-if="thisRow.pageType == 'modify'">
          <Icon :type="$common.icon.modify" />
          修改job触发器
        </span>
        <span class="title" v-else-if="thisRow.pageType == 'add'">
          <Icon :type="$common.icon.add" />
          新增job触发器
        </span>
        <span class="title" v-else >
          <Icon :type="$common.icon.detail" />
          job触发器详情
        </span>
        
        <span class="right-tool" style="float:right">
          <Button v-if="!isDisabled"  type="info" ghost @click="save"  :icon="$common.icon.save" size="small" >保存</Button>
          <Button  type="info" ghost @click="cancel"  :icon="$common.icon.cancel"  size="small" ><span v-if="isDisabled" >返回</span><span v-else >取消</span></Button>
          <!-- <Button  type="info" ghost @click="formReset('dataForm1')"  :icon="$common.icon.clean" size="small" >重置</Button> -->
          <Button  type="info" ghost @click="init"  :icon="$common.icon.refresh" size="small" >刷新</Button>
        </span>
      </div>
      <div class="card-body" style="padding:25px 25px;">

        <Form ref="dataForm1" :model="row" :rules="form1Validate" :disabled="isDisabled" :label-width="120" style="padding:20px;">
          <Row>
            <Col span="8">
              <FormItem label="所属job"  prop="targetId">
                  <Select :disabled="isDisabled" v-model="row.targetId" placeholder="请选择所属job..." >
                      <Option  v-for="(item,index) in jobDetails" :key="index" :value="item.sid" >{{item.displayName}}</Option>
                  </Select>
                  <!-- <Input type="text" :disabled="isDisabled" v-model="row.targetId" placeholder="请输入所属job...">
                  </Input> -->
              </FormItem>
            </Col>
            <Col span="8">
              <FormItem label="job触发器名" prop="triggerName">
                <Input type="text" :disabled="isDisabled" v-model="row.triggerName" placeholder="请输入job触发器名...">
                </Input>
              </FormItem>
            </Col>
            <Col span="8">
              <FormItem label="cron表达式" prop="cronExpression">
                <Input type="text" :disabled="isDisabled" v-model="row.cronExpression" placeholder="请输入cron表达式...">
                </Input>
              </FormItem>
            </Col>
          </Row>


          <Row>
            <Col span="24">
              <FormItem label="触发器参数" prop="triggerData">
                  <Input type="textarea" maxlength="500" :disabled="isDisabled" show-word-limit v-model="row.triggerData" placeholder="请输触发器参数(JSON格式)..." :rows="8" ></Input>
              </FormItem>
            </Col>
          </Row>
          
          <Row>
            <Col span="24">
              <FormItem label="备注" prop="notes">
                <Input type="textarea" maxlength="500" :disabled="isDisabled" show-word-limit v-model="row.notes" placeholder="请输备注..." :rows="5" ></Input>
              </FormItem>
            </Col>
          </Row>


        </Form>

      </div>
        
    </div>
    
    <!-- loadding 防止多次点击 -->
    <Spin fix v-if="loading">
        <Icon type="ios-loading" size=18 class="spin-icon-load"></Icon>
        <div>数据加载中请稍后...</div>
    </Spin>
    
  </div>

</template>


<script>


  export default {
    name: 'jobTriggerModify',
    data () {
      return {
        detailUrl:'/site/quartzJobTrigger/detail',
        saveUrl:'/site/quartzJobTrigger/add',
        isDisabled:this.thisRow.pageType=='detail'?true:false,
        titleIsDisabled:this.thisRow.pageType=='modify'?true:false,
        loading:false,//laoding 标识
        row:{},
        jobDetails:[],
        form1Validate:{//表单验证
          targetId: [
              { required: true,message:'请选择所属job',validator: '', trigger: 'blur' }
          ],
          triggerName: [
              { required: true,message:'请输入触发器名',validator: '', trigger: 'blur' }
          ],
          cronExpression: [
              { required: true,message:'请输入cron表达式',validator: '', trigger: 'blur' }
          ],
        },

      }
    },
    props:{
      thisRow:Object,//父页面传过来的参数
      items:Object,//字典数据
    },
    components:{//注册组件
      
    },
    beforeCreate(){//拿不到任何信息，无法篡改数据，一般做loding，这个时候的vue实例还什么都没有，但是$route对象是存在的，可以根据路由信息进行重定向之类的操作
      
    },
    created() {//el 没有初始化，数据已加载完成，阔以篡改数据，并更新，不会触发，，在这结束，还做一些初始化，实现函数自执行，ref属性job触发器为空数组
       
    },
    beforeMount(){//$el已被初始化,，数据已加载完成，阔以篡改数据，并更新，不会触发beforeUpdate，updated，在挂载开始之前被调用，beforeMount之前，会找到对应的template，并编译成render函数
    },
    methods:{//el 已被初始化，数据已加载完成，阔以篡改数据，并更新，并且触发，，在这发起后端请求，拿回数据，配合路由钩子做一些事情，ref属性可以访问
      init(){
        //表单初始化
        this.loading=true;
        let _this = this;
        this.axios.get(this.detailUrl,{params:{
          sid:_this.thisRow.triggerSid
        }})
        .then((response)=>{
          _this.loading=false;
          
          if(response.success){
            let row = response.data;
            _this.row = row;
            this.jobDetails = response.unknown;
          }else{
            this.$Notice.warning({title: '数据初始化失败',desc: response.message});
          }
        })
        .catch((error)=>{
          _this.loading=false;
          this.$Notice.error({title: '异常',desc: error});
        });

      },
      save(params){//保存
        let _this=this;

        this.$refs.dataForm1.validate((vali1)=>{//表单验证

          if(vali1){

            let url = this.saveUrl;
            if(this.row.sid){
              url= '/site/quartzJobTrigger/modify';
            }

            _this.loading=true;
            _this.axios.post(url,_this.row,{headers:{type:'json'}})
            .then((response)=>{
              let row = response.data;
              _this.loading=false;
              this.$Notice.info({title:'提示',desc: response.message});
              if(response.success){
                let isReFresh = _this.thisRow.pageType=='add' || _this.thisRow.pageType=='modify'?true:false;
                let updateData = _this.thisRow.pageType=='modify'?{row:row}:false;
                _this.$emit('chindrenChangeData',{showPage:"list",updateData:{row:row},isReFresh:isReFresh});//调用父级方法
                
              }
            })
            .catch((error)=>{
              _this.loading=false;
              this.$Notice.error({title: '异常',desc: error});
            });

          }

        });
      
      },
      cancel(){//取消
        this.$emit('chindrenChangeData',{showPage:"list"});//调用父级方法
      },

    },
    mounted(){
      
      this.init();
      
    },
    destroyed(){
    
    }

  }

</script>