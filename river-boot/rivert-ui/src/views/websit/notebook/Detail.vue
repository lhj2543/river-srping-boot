<template>
  
  <div class="content-detail-panel" >

    
    <div class="title">
      <span v-text="row.title"></span>

      <span class="close" title="关闭" @click="close()">
        <Icon :type="$common.icon.close" color="" />
      </span>
    </div>

    <div class="content-detail-body" v-html="row.bodys">

    </div>


  </div>

</template>


<script>

  export default {
    name: 'notebookDetail',
    data () {
      return {
        
        detailUrl:'/site/public/notebook/detail',
        loading:false,//laoding 标识
        row:{
          bodys:''
        },

      }
    },
    props:{
      detailParams:Object,//父页面传过来的参数
    },
    components:{//注册组件
      
    },
    beforeCreate(){//拿不到任何信息，无法篡改数据，一般做loding，这个时候的vue实例还什么都没有，但是$route对象是存在的，可以根据路由信息进行重定向之类的操作
      
    },
    created() {//el 没有初始化，数据已加载完成，阔以篡改数据，并更新，不会触发，，在这结束，还做一些初始化，实现函数自执行，ref属性内容为空数组
      
      this.initData();

    },
    beforeMount(){//$el已被初始化,，数据已加载完成，阔以篡改数据，并更新，不会触发beforeUpdate，updated，在挂载开始之前被调用，beforeMount之前，会找到对应的template，并编译成render函数
    },
    methods:{//el 已被初始化，数据已加载完成，阔以篡改数据，并更新，并且触发，，在这发起后端请求，拿回数据，配合路由钩子做一些事情，ref属性可以访问

      initData(){

        let _this = this;
        _this.loading=true;
        this.axios.get(this.detailUrl,{params:{
          sid:_this.detailParams.sid,
        }})
        .then((response)=>{
          _this.loading=false;
          
          if(response.success){

            this.row = response.data;

          }else{
            this.$Notice.info({title:'提示',desc: response.message});
          }
        })
        .catch((error)=>{
          _this.loading=false;
          this.$Notice.error({title: '异常',desc: error});
        });

      },
      close(){
        this.$emit('chindrenChangeData',{showPage:"list"});//调用父级方法
      }

    },
    mounted(){
      

    },
    destroyed(){
    
    },
    watch:{
      

    }

  }

</script>