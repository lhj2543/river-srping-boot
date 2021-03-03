<style scoped>

    .river-captcha{
        width: 10%;
        min-width: 700px;
        max-width: 1500px;
        min-height: 350px;
        border:1px solid #57a3f3;
        position: absolute;
        top: 30%;
        left: 50%;
        transform: translateX(-50%)  translateY(-50%);
        -webkit-transform: translate(-50%,-50%);
        -ms-transform: translate(-50%,-50%);
        border-radius: 3px;
        padding-top: 10px;
    }

    .river-captcha .captcha-img-panel{
        width: 80%;
        height: 300px;
        margin: 0 auto;
        padding-top:100px;
        text-align: center;
    }
    .river-captcha .captcha-img-sapn{
       border:1px solid #1DB954 ;
       padding: 10.5px 0 13px 10px;
    }
    .river-captcha .captcha-img{
        cursor: pointer;
        vertical-align: middle;
    }
    .river-captcha .captchaCode{
        padding:5px 0;
        border: none;
        background: none;
        text-indent: 5px;
        font-size: 14px;
    }
    .captcha-btn{
        padding: 9px 25px;
        background: #1DB954;
        border: none;
        color: #fff;
        cursor: pointer;
        border-radius: 2px;
    }


</style>

<template>
  <!-- 验证码 -->
  <div class="river-captcha">

      

    <Tabs >

        
        <TabPane label="图片滑动验证" icon="logo-tux">
            
             <div class="" style="">
                 <captchaSpell /> 
            </div>

        </TabPane>

        <TabPane label="输入验证码验证" icon="logo-apple">

            <div class="captcha-img-panel">
                 <span class="captcha-img-sapn">
                    验证码：
                    <input name="captchaCode" v-model="captcha.code" class="captchaCode" placeholder="请输入验证码..."/> 
                    <img title="刷新" class="captcha-img" :src="captcha.url+'?randomStr='+captcha.randomStr" @click="refreshCaptcha"/>
                 </span>

                <button @click="checkCaptcha" class="captcha-btn">验 证</button>

            </div>
             
           
        </TabPane>
        
        <TabPane label="拖动滑动验证" icon="logo-windows">

            <div class="captcha-img-panel">
                <captchaSlide />
            </div>

        </TabPane>

    </Tabs>

      


  </div>
</template>
<script>


import {randomLenNum} from '@/utils/util.js'
import captchaSlide from '@/views/demo/web/CaptchaSlide.vue'
import captchaSpell from '@/views/demo/web/CaptchaSpell.vue'

export default {
  name: 'captcha',
  data(){
    return {
      captcha:{
          url:this.$globSetting.apiURL + '/site/public/captcha/create',
          randomStr:randomLenNum(4,true),
          code:''
      }
    }
  },
  components:{//注册组件
    captchaSlide,
    captchaSpell
    
  },
  created(){

  },
  mounted(){

      setInterval(() => {
          this.refreshCaptcha();
      }, 1000 * 60);
      
  },
  methods: {

      //刷新验证码
      refreshCaptcha(){
          this.$set(this.captcha,'randomStr',randomLenNum(4,true));
      },
      //后台验证验证码
      checkCaptcha(){

        if(!this.captcha.code){
            this.$Notice.warning({title:'提示',desc: "验证码不能为空"});
            return ;
        }
          
        this.axios.get('/site/public/captcha/check',{
            params: {code:this.captcha.code,randomStr:this.captcha.randomStr},
        })
        .then((res)=>{

            this.$Notice.info({title:'提示',desc: res.message});
            if(!res.success){
                this.$set(this.captcha,'code','');
            }
            this.refreshCaptcha();

        })
        .catch((error)=>{
            this.$Notice.error({title:'异常',desc: error});
            this.refreshCaptcha();
            this.$set(this.captcha,'code','');
        });

      }


  },
  watch: {//数据监听
  
  },
  beforeDestroy(){
        
  }

}
</script>
