

<style scoped>
  .error-404{
    background-size: cover !important;
    background-position: center !important;
    background-image: url(/static/error/404.jpg);
    width: 100%;
    height: 100%;
  }
</style>

<template>
    <div class="error-404 plan-centre">

    <!-- 头部 -->
    <!--或者这种写法也行<AppHeader/> -->
    <app-header :contentActiveTheme="contentActiveTheme" />
      
    </div>
</template>


<script>
import AppHeader from '@/views/websit/Header.vue'//引入头部

export default {
  name: 'error404',
  data(){
    return {
      contentActiveTheme:'theme-0  top-fied',//主题
    }
  },
  components:{
    AppHeader
  },
  created() {
    
  },
  mounted () {//钩子
    
  },
  methods: {
  },
  destroyed () {
  },

}
</script>