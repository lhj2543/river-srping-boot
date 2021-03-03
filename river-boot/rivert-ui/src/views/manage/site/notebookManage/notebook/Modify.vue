
<template>
  
  <div class="" >

    <div class="notebook-title" style="height:50px;">
        
         <Form ref="formRef" :model="row" :rules="formValidate" :label-width="0" :readonly="isDisabled" class="notebook-form">

            <FormItem label="" prop="title">
                <Input v-model="row.title" :readonly="isDisabled" placeholder="请输入标题..." >
                </Input>
                <span class="isPublic" v-if="!isDisabled || isPublic=='active'"><Icon type="ios-lock" title="是否私有" :class="['ios-lock',isPublic]" size="25" @click="isPublicChange" /></span>
            </FormItem>
        </Form>

    </div>

    <div v-if="!isDisabled" >
        <!-- 百度编辑器 -->
        <VueUeditorWrap v-model="row.bodys" :config="ueditConfig"/>
    </div>

    <div class="notebook-detail" v-if="isDisabled" v-html="row.bodys"> </div>
    
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
    name: 'noteBookModify',
    data () {
      return {
        detailUrl:'/site/noteBook/detail',
        saveUrl:'/site/noteBook/modify',
        isPublic:'',
        isDisabled:this.thisRow.pageType=='detail'?true:false,
        loading:false,//laoding 标识
        items:{},//字典数据
        row:{
          bodys:''
        },
        formValidate:{//表单验证
          title: [
              { required: true,message:'请输入标题!',validator: '', trigger: 'blur' }
          ],
          
        },
        ueditConfig: this.$common.ueditConfig,//百度编辑器配置
      }
    },
    props:{
      thisRow:Object,//父页面传过来的参数
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
      init(){//表单初始化
        let _this = this;
        _this.loading=true;
        this.axios.get(this.detailUrl,{params:{
          sid:_this.thisRow.sid,
          categoryId:_this.thisRow.categoryId,
        }})
        .then((response)=>{
          _this.loading=false;
          
          if(response.success){
            
            let row = response.data;
            _this.row = row;
            if(row.status=='1'){
              _this.isPublic = 'active';
            }

          }else{
            this.$Notice.info({title:'提示',desc: response.message});
          }
        })
        .catch((error)=>{
          _this.loading=false;
          this.$Notice.error({title:'异常',desc: error});
        });
      },
      save(params){//保存
        let _this=this;
        
        this.$refs.formRef.validate((vali1)=>{//表单验证

          if(vali1){

            _this.loading=true;
            _this.axios.post(_this.saveUrl,_this.row,{headers:{type:'json'}})
            .then((response)=>{
              
              _this.loading=false;
              this.$Notice.info({title:'提示',desc: response.message});

              if(response.success){

                let row = response.data;
                _this.row = row;
                let isReFresh = _this.thisRow.pageType=='add'?true:false;
                let updateData = _this.thisRow.pageType=='modify'?{row:row}:false;
                //_this.$emit('chindrenChangeData',{showPage:"list",updateData:{row:row},isReFresh:isReFresh});//调用父级方法

              }
            })
            .catch((error)=>{
              _this.loading=false;
              this.$Notice.error({title:'异常',desc: error});
            });

          }else{
            _this.$Message.info('标题不能为空！');
          }

        });
        
      },
      isPublicChange(){//是否公开
        if(this.isDisabled){
          return;
        }
        
        if(!this.isPublic){
          this.isPublic = 'active';
          this.row.status = '1';
        }else{
          this.isPublic = '';
          this.row.status = '2';
        }
      }
    
    },
    mounted(){
      this.init();

    },
    destroyed(){
    
    }

  }

</script>
<style>
/* =====================重写uedit 样式================= */
.edui-default .edui-editor{
    border-radius:0px !important;
    width: 100% !important;
    height:calc(100vh - 350px) !important;
}
.edui-editor-iframeholder .edui-default,#edui1_iframeholder{
    width: 100% !important;
    height: 100% !important;
}
.ios-lock{
  position: relative;
  top:-40px;
  right:-97%;
  cursor: pointer;
  color: #ccc;
}
.ios-lock.active{
  color:#f60;
}
/* =====================重写iview 样式================= */
</style>