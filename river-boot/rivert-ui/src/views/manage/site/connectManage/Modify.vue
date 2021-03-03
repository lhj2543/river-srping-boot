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
          修改内容
        </span>
        <span class="title" v-else-if="thisRow.pageType == 'add'">
          <Icon :type="$common.icon.add" />
          新增内容
        </span>
        <span class="title" v-else >
          <Icon :type="$common.icon.detail" />
          内容详情
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
            <Col span="6">
              <FormItem label="标题" prop="title">
                <Input type="text" :disabled="isDisabled" v-model="row.title" placeholder="请输入标题...">
                </Input>
              </FormItem>
            </Col>
            <Col span="6">
              <FormItem label="所属菜单"  prop="targetId">
                  <Select :disabled="isDisabled" v-model="row.targetId" placeholder="请选择所属菜单..." >
                      <Option  v-for="(value,key) in items['targetMenus']" :key="key+'-'+value" :value="key" >{{value}}</Option>
                  </Select>
              </FormItem>
            </Col>
            <Col span="6">
              <FormItem label="内容分类"  prop="category">
                  <Select :disabled="isDisabled" v-model="row.category" placeholder="请选择内容分类..." @on-change="getMaxNumber" >
                      <Option  v-for="(value,key) in items['site_connect_category']" :key="key+'-'+value" :value="key" >{{value}}</Option>
                  </Select>
              </FormItem>
            </Col>
             <Col span="6" >
              <FormItem label="状态"  prop="status">
                <RadioGroup v-model="row.status" >
                  <Radio v-for="(value,key) in items['isActive']" :key="key+'-'+value" :label="key">{{value}}</Radio>
                </RadioGroup>
              </FormItem>
            </Col>
            
          </Row>

          <Row>
            <Col span="6">
              <FormItem label="置顶" prop="toping">
                <InputNumber v-model="row.toping" :disabled="isDisabled" placeholder="请输入置顶序号..." :min="0" style="width:100%;"></InputNumber>
              </FormItem>
            </Col>

            <Col span="6" >
              <FormItem label="排序"  prop="sortKey">
                <InputNumber v-model="row.sortKey" :disabled="isDisabled" placeholder="请输入排序号..." :min="0" style="width:100%;"></InputNumber>
              </FormItem>
            </Col>

            <Col span="6">
              <FormItem label="类型"  prop="type">
                  <Select :disabled="isDisabled" v-model="row.type" placeholder="请选择类型..." >
                      <Option  v-for="(value,key) in items['site_connect_type']" :key="key+'-'+value" :value="key" >{{value}}</Option>
                  </Select>
              </FormItem>
            </Col>

            <Col span="6" v-if="row.type!=3">
              <FormItem label="url"  prop="url">
                  <Input type="text" :disabled="isDisabled" v-model="row.url" placeholder="请输入url...">
                  </Input>
              </FormItem>
            </Col>
          </Row>

          <Row style="">
            <Col span="12">
              <FormItem label="封面" prop="img">
                

                <div class="img-upload-list" v-for="(item, index) in uploadList" :key="index">
                    <template v-if="item.status === 'finished'">
                        <img :src="item.url">
                        <div class="img-upload-list-cover" v-if="!isDisabled">
                            <div>
                              <!-- <Icon type="ios-eye-outline" @click.native="handleView(item.name)"></Icon> -->
                              <Icon type="ios-trash-outline" @click.native="handleRemove(item)"></Icon>
                            </div>
                            <div v-text="item.name" style="color: #fff;position: absolute; bottom: 0px; text-align: center;width:100%; ">

                            </div>
                        </div>
                    </template>
                    <template v-else>
                        <Progress v-if="item.showProgress" :percent="item.percentage" hide-info></Progress>
                    </template>
                </div>

                <Upload v-show="!isDisabled && uploadList<=0"
                    ref="imgUpload"
                    :show-upload-list="false"
                    :default-file-list="defaultList"
                    :on-success="handleSuccess"
                    :on-error="handeError"
                    :format="['jpg','jpeg','png']"
                    :max-size="2048"
                    :on-format-error="handleFormatError"
                    :on-exceeded-size="handleMaxSize"
                    :before-upload="handleBeforeUpload"
                    :multiple="false"
                    :action="attachAction"
                    :headers="attacHeaders"
                    :data="attachData"
                    type="drag"
                    style="display: inline-block;width:58px;">
                    <div style="width: 58px;height:58px;line-height: 58px;">
                        <Icon type="ios-camera" size="20"></Icon>
                    </div>
                </Upload>

              </FormItem>
            </Col>

          </Row>

          <Row v-if="thisRow.pageType=='detail'">
            <Col span="6" >
              <FormItem label="创建人"  prop="createdBy">
                <Input type="text" :disabled="isDisabled" v-model="row.createdBy" placeholder="">
                </Input>
              </FormItem>
            </Col>
            <Col span="6">
              <FormItem label="创建时间"  prop="createdDate">
                  <Input type="text" :disabled="isDisabled" v-model="row.createdDate" placeholder="">
                  </Input>
              </FormItem>
            </Col>
            <Col span="6">
              <FormItem label="修改人" prop="updateBy">
                <Input type="text" :disabled="isDisabled" v-model="row.updateBy" placeholder="">
                </Input>
              </FormItem>
            </Col>
            <Col span="6">
              <FormItem label="修改时间"  prop="updateDate">
                  <Input type="text" :disabled="isDisabled" v-model="row.updateDate" placeholder="">
                  </Input>
              </FormItem>
            </Col>
          </Row>

          <Row>
            <Col span="24">
              <FormItem label="备注" prop="notes">
                <Input type="textarea" maxlength="500" :disabled="isDisabled" show-word-limit v-model="row.notes" placeholder="请输备注..." :rows="5" >
                </Input>
              </FormItem>
            </Col>
            
          </Row>

          <Row>
            <Col span="24">
             
             <div v-if="!isDisabled && row.type!=2" style="height:800px;" >
                <!-- 百度编辑器 -->
                <VueUeditorWrap v-model="row.bodys" :config="ueditConfig"/>
             </div>

             <FormItem  v-if="isDisabled && row.type!=2" label="内容" prop="bodys">
                <div v-html="row.bodys" style="border:1px solid #ccc;padding:20px;"></div>
             </FormItem>

            </Col>
          </Row>

        </Form>

      </div>
        
    </div>
    
    <!-- loadding 防止多次点击 -->
    <Spin fix v-if="loading">
        <Icon type="ios-loading" size=18 class="spin-icon-load"></Icon>
        <div>数据保存中请稍后</div>
    </Spin>
    
  </div>

</template>


<script>

import VueUeditorWrap from 'vue-ueditor-wrap'//vue 百度富文本编辑器

  export default {
    name: 'connectModify',
    data () {
      return {
        detailUrl:'/site/siteConnentMain/detail',
        saveUrl:'/site/siteConnentMain/modify',
        isDisabled:this.thisRow.pageType=='detail'?true:false,
        titleIsDisabled:this.thisRow.pageType=='modify'?true:false,
        loading:false,//laoding 标识
        row:{},
        form1Validate:{//表单验证
          title: [
              { required: true,message:'请输入标题',validator: '', trigger: 'blur' }
          ],
          targetId: [
              { required: true,message:'请选择所属菜单',validator: '', trigger: 'blur' }
          ],
          category: [
              { required: true,message:'请选择内容分类',validator: '', trigger: 'blur' }
          ],
          type: [
              { required: true,message:'请选择类型',validator: '', trigger: 'blur' }
          ],
        },

        //封面上传
        attachAction:this.$globSetting.apiURL+'/system/sysAttachs/save',
        attachDelUrl:'/system/sysAttachs/deletes',//附件预删除
        attacHeaders:{Authorization:'Bearer ' + this.$common.getToken()},
        attachData:{},
        attachIds:[],
        defaultList: [
            /* {
              'name': 'a42bdcc1178e62b4694c830f028db5c0',
              'url': 'http://localhost:8080/static/error/404.jpg'
            } */
        ],
        uploadList: [
           /* {
              status: 'finished',
              percentage: '100',
              uid: '1600570618171',
              url:'http://localhost:8080/static/error/404.jpg'
            } */
        ],
        //封面上传

        ueditConfig:Object.assign({},this.$common.ueditConfig,{initialFrameHeight: '800'}) ,//百度编辑器配置
      }
    },
    props:{
      thisRow:Object,//父页面传过来的参数
      items:Object,//字典数据
    },
    components:{//注册组件
      VueUeditorWrap
    },
    beforeCreate(){//拿不到任何信息，无法篡改数据，一般做loding，这个时候的vue实例还什么都没有，但是$route对象是存在的，可以根据路由信息进行重定向之类的操作
      
    },
    created() {//el 没有初始化，数据已加载完成，阔以篡改数据，并更新，不会触发，，在这结束，还做一些初始化，实现函数自执行，ref属性内容为空数组
       
    },
    beforeMount(){//$el已被初始化,，数据已加载完成，阔以篡改数据，并更新，不会触发beforeUpdate，updated，在挂载开始之前被调用，beforeMount之前，会找到对应的template，并编译成render函数
    },
    methods:{//el 已被初始化，数据已加载完成，阔以篡改数据，并更新，并且触发，，在这发起后端请求，拿回数据，配合路由钩子做一些事情，ref属性可以访问
     async initImg(){//初始化封面图片

        if(this.thisRow.sid){
          //附件初始化
          await this.axios.get('/system/sysAttachs/getListByTargetId',{
            params:{'targetId':this.thisRow.sid}
          })
          .then((response)=>{
            
            if(response.success){
              let data = response.data;
              
              data.forEach((row,i) => {
                
                this.attachIds.splice(-1,0,row.sid);
                this.defaultList.splice(-1,0,{
                  name: row.name,
                  url: this.$globSetting.attachUrl + row.path
                });

              });

              
            }else{
              this.$Notice.warning({
                title: '获取封面失败',
                desc: response.message
              });
            }
            
          })
          .catch((error)=>{
            
            this.$Notice.warning({
              title: '获取封面异常',
              desc: error
            });
  
          });
        }

    },
    init(){//表单初始化
      this.loading=true;
      let _this = this;
      this.axios.get(this.detailUrl,{params:{
        sid:_this.thisRow.sid
      }})
      .then((response)=>{
        _this.loading=false;
        let row = response.data;
        if(response.success){
          _this.row = row;
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

          _this.loading=true;
          _this.axios.post(_this.saveUrl,_this.row,{headers:{type:'json'}})
          .then((response)=>{
            let row = response.data;
            _this.loading=false;
            this.$Notice.info({title:'提示',desc: response.message});
            if(response.success){

              if(!this.thisRow.sid && this.uploadList && this.uploadList.length>0){
                this.updateTargetId(row.sid,'new_connect_attach',callback);
              }else{
                callback();
              }

              function callback(){
                let isReFresh = _this.thisRow.pageType=='add'?true:false;
                let updateData = _this.thisRow.pageType=='modify'?{row:row}:false;
                _this.$emit('chindrenChangeData',{showPage:"list",updateData:{row:row},isReFresh:isReFresh});//调用父级方法
              }
              
            }
          })
          .catch((error)=>{
            _this.loading=false;
            this.$Notice.error({title: '异常',desc: error});
          });

        }

      });
      
    },
    updateTargetId(targetId,oldTargetId,callback){//更新附件targetId

      this.axios.post('/system/sysAttachs/updateTargetId',this.$qs.stringify({'targetId':targetId,'oldTargetId':oldTargetId}))
      .then((response)=>{
        
        if(response.success){
          callback();
        }else{
            this.$Notice.warning({title: '更新附件targetId失败',desc: response.message});
        }
        
      })
      .catch((error)=>{
        this.$Notice.error({title: '更新附件targetId异常',desc: error});
      });

    },
    cancel(){//取消
      this.$emit('chindrenChangeData',{showPage:"list"});//调用父级方法
    },
    getMaxNumber(value){

       let sid = this.thisRow.sid

       if(!sid){

         let data = {
           tableName:'site_connent_main',
           numberColumn:'sort_key',
           wheres:[
             {column:'category',opt:'=',value:value},
           ]
         };
        
         this.axios.post('/site/siteCommon/getMaxNumber',data,{headers:{type:'json'}})
        .then((response)=>{
          
          if(response.success){
            
            this.$set(this.row, "sortKey", parseInt(response.data+1));

          }else{
            this.$Notice.warning({title: '提示',desc: response.message});
          }
          
        })
        .catch((error)=>{
          this.$Notice.warning({title: '异常',desc: error});
        });

       }

        
    },

    //=========封面上传============
    handleRemove (file) {
        
        this.axios.delete(this.attachDelUrl,{
          params:{'ids':this.attachIds},
          paramsSerializer: params => {
            return this.$qs.stringify(params, { indices: false })
          }
        })
        .then((response)=>{
          
          if(response.success){
            this.row.img = '';
            this.attachIds = [];
            const fileList = this.$refs.imgUpload.fileList;
            this.$refs.imgUpload.fileList.splice(fileList.indexOf(file), 1);
          }else{
            this.$Notice.warning({title: '删除失败',desc: response.message});
          }
          
        })
        .catch((error)=>{
          this.$Notice.warning({title: '删除异常',desc: error});
        });

    },
    handleSuccess (res, file) {
        console.log(file);
        console.log(this.uploadList);
        console.log(this.$refs.imgUpload.fileList);
        if(res.success){
          let data = res.data;
          if(data){
            for(let i in data){
              let row = data[i];
              file.url = this.$globSetting.attachUrl + row.path;
              file.name = row.name;
              this.row.img = row.path;
              this.attachIds = [row.sid];
            }
          }
          
        }else{
            const fileList = this.$refs.imgUpload.fileList;
            this.$refs.imgUpload.fileList.splice(fileList.indexOf(file), 1);

            this.$Notice.warning({title: '文件上传失败',desc: res.message});

            return false;
        }
        
    },
    handeError (error, file, fileList) {
        this.$Notice.error({title: '文件上传异常', desc: error});
    },
    handleFormatError (file) {
        this.$Notice.warning({title: '文件格式错误！',desc: '请上传 jpg/jpeg/png 类型图片'});
    },
    handleMaxSize (file) {
        this.$Notice.warning({title: '文件大小错误',desc: '文件最大为 2M'});
    },
    handleBeforeUpload () {
      const check = this.uploadList.length < 1;
      if (!check) {
          this.$Notice.warning({title: '最多上传一张封面'});
      }else{
        let targetId = this.row.sid || 'new_connect_attach';
        this.$set(this.attachData,'targetId',targetId);
      }

      return check;
    }
      //=========封面上传============

  },
    async mounted(){
      await this.initImg();//等待封面图片加载完
      
      this.init();
      
      this.uploadList = this.$refs.imgUpload.fileList;
     
    },
    destroyed(){
    
    }

  }

</script>

<style >
/* 图片上传 */
.img-upload-list{
    display: inline-block;
    width: 280px;
    height: 160px;
    line-height: 60px;
    border: 1px solid transparent;
    border-radius: 4px;
    overflow: hidden;
    background: #fff;
    position: relative;
    box-shadow: 0 1px 1px rgba(0,0,0,.2);
    margin-right: 4px;
    text-align: right;
}
.img-upload-list img{
    width: 100%;
    height: 100%;
}
.img-upload-list-cover{
    display: none;
    position: absolute;
    top: 0;
    bottom: 0;
    left: 0;
    right: 0;
    background: rgba(0,0,0,.6);
}
.img-upload-list:hover .img-upload-list-cover{
    display: block;
}
.img-upload-list-cover i{
    color: #fff;
    font-size: 20px;
    cursor: pointer;
    margin: 0 2px;
    position: relative;
    top:-10px;
}
/* 图片上传 */
</style>