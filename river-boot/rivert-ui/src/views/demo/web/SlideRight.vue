<style  scope>

  .demoIndex{
    height: 100%;
  }
  .SlideRight .index{
    height: 100%;
    -webkit-transition: -webkit-transform 0.5s;
	  transition: transform 0.5s;
  }
  .SlideRight {
    height: 100% ;
    overflow: hidden;
    background: rgba(0,0,0,0.01);
    overflow-x: hidden;
  }

  .SlideRight .shade{ /* 遮罩层 */
    width:100%;
    height:100%;
    position: absolute;
    z-index: 10;
  }

  .SlideRight .index{
    -webkit-transition: -webkit-transform 0.5s;
	  transition: transform 0.5s;
  }

  .SlideRight .index::after {
    position: absolute;
    top: 0;
    right: 0;
    width: 0;
    height: 0;
    background: rgba(0,0,0,0.2);
    content: '';
    opacity: 0;
    -webkit-transition: opacity 0.5s, width 0.1s 0.5s, height 0.1s 0.5s;
    transition: opacity 0.5s, width 0.1s 0.5s, height 0.1s 0.5s;
  }
  
  .SlideRight.mnue-more-open .index::after {
    width: 100%;
    height: 100%;
    opacity: 0;
    -webkit-transition: opacity 0.5s;
    transition: opacity 0.5s;
  }

  .SlideRight,.SlideRight.mnue-more-open{
    -webkit-perspective: 120em;
		perspective: 120em;
  }
  
  .SlideRight.mnue-more-open .index {
		-webkit-transform: translate3d(-18rem, 0rem, -60rem) rotateY(25deg);
		transform: translate3d(-18rem, 0rem, -60rem) rotateY(25deg);
  }

  .SlideRight .mnue-more-panel{
    position: absolute;
    right: 0;
    height: 100%;
    width: 25rem;
    z-index: 100;
    background: #1ABC9C;

    -webkit-transition: all 0.5s;
    transition: all 0.5s;

    -webkit-transform: translate3d(100%, 0, 0);
    transform: translate3d(100%, 0, 0);

    visibility: hidden;/* 隐藏也会占据空间 */
    /* backface-visibility:hidden;
    -webkit-backface-visibility:hidden;
    -moz-backface-visibility:hidden;
    -ms-backface-visibility:hidden; */
  }

  .SlideRight.mnue-more-open .mnue-more-panel{
    visibility: visible;
    -webkit-transition: -webkit-transform 0.5s;
    transition: transform 0.5s;
    -webkit-transform: translate3d(0, 0, 0);
    transform: translate3d(0, 0, 0);
  }

  .SlideRight .mnue-more-panel *{
    font-size: 1.2rem;
    color: #fff;
  }

  .SlideRight .mnue-close{
    font-size: 2.5rem ;
    float:right;
    vertical-align:middle;
    margin-top: -0.2rem;
  }
  .SlideRight .mnue-more-top{
    /* border-bottom:0.01rem solid #6D6D6D; */
    padding: 1rem;
  }

  .SlideRight .main-center{
    cursor: pointer;
    width:80%;
    height:80%;
    background:#00CFDD;
    font-size: 30px;
    text-align: center;
    padding-top:20%; 
  }

  .SlideRight .plan-centre{
    position: absolute;
    left: 50%;
    top: 50%;
    transform: translateX(-50%)  translateY(-50%);
    -webkit-transform: translate(-50%,-50%);
    -ms-transform: translate(-50%,-50%);
  }

  .SlideRight .menus-panel{
    padding:20px;
  }
  .SlideRight .menus-panel div{
    padding: 10px;
    font-size: 17px;
    font-weight: 600;
    cursor: pointer;
  }

</style>

<template>

  <div id="index-body" class="index-body SlideRight"  :class="{'mnue-more-open':mnueMoreOpenFlag}" >


    <div class="mnue-more-panel">
      
      <div class="mnue-more-top">
        <Icon type="ios-close" title="关闭" class="mnue-close cursor-pointer" @click="closeMnue" />
      </div>

      <div>

        <div class="menus-panel">
          <div>右侧菜单一</div>
          <div>右侧菜单一</div>
          <div>右侧菜单一</div>
          <div>右侧菜单一</div>
        </div>

      </div>

    </div>

    <!-- 遮罩层 -->
    <div class="shade" :class="[mnueMoreOpenFlag?'display-block':'display-none']"  @click="closeMnue" ></div>

    <div id="index" class="index" >

      <div class="main-center plan-centre" @click="openMnue">
          点击打开右侧菜单
      </div>

    </div>


  </div>

</template>

<script>

export default {
  name: 'SlideRight',
  data () {
          return {
            mnueMoreOpenFlag:false,
          }
        },
  components:{//注册组件
    
  },
  props:{

  },
  beforeCreate(){
    
  },
  created() {
    
  },
  beforeMount(){
 
  },
  methods:{
    openMnue(ev){//打开右侧菜单
      ev.stopPropagation();
			ev.preventDefault();
      this.mnueMoreOpenFlag = true;
    },
    closeMnue(ev){//关闭右侧菜单
      ev.stopPropagation();
			ev.preventDefault();
      this.mnueMoreOpenFlag = false;
    },

  },
  mounted(){

    
  },
  destroyed(){
   
  }

}
</script>
