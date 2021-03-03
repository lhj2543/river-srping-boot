<style>
  .ali-player{
    width: 100%;
  }
  .ali-player .main {
    box-sizing: border-box;
    color: #FFFFFF;
  }
  .ali-player .video-center {
    position: relative;
  }
  .ali-player .tips{
    position: absolute;
    top: 50%;
    left: 50%;
    -webkit-transform: translate(-50%, -50%);
    transform: translate(-50%, -50%);
    z-index: 9999;
    opacity: 0.79;
  }
  .ali-player .name{
    position: absolute;
    left: 50%;
    top: -20px;
    font-size: 18px;
    -webkit-transform: translateX(-50%);
    transform: translateX(-50%);
  }
  .ali-player #video {
    width: 100%;
    height: 500px !important;
    opacity: 0.79;
  }
</style>

<template>

  <div class="ali-player">
      <div class="main">
          <div class="video-center">
              <div v-if="!sourceUrls && !loadingVideo" class="tips">请选择视频源</div>
              <div v-if="waiting" class="tips">获取视频中，请稍等...</div>
              <div id="video" v-loading="loadingVideo" style="background-color: #0c0b0b"></div>
          </div>
      </div>
  </div>

</template>


<script>

var oHead = document.getElementsByTagName('head')[0];
var oScript = document.createElement("script");
oScript.type = "text/javascript";
oScript.src = "/static/plugs/ckplayer/ckplayer.js";
oHead.appendChild(oScript);

export default {
  name: 'ckplayer',
  data () {
          return {
            sourceUrls:this.sourceUrl,//如果不赋值，在加载组件时会报错
            player: null,
            loadingVideo:false,
            waiting:false,
            reloadPlayTime:null //当视频流获取超时定时器
          }
        },
  components:{//注册组件
    
  },
  props:{

    sourceUrl:{
        type:String,
        default:''
    },
    height:{
        type:String,
        default:'550px'
    }

  },
  beforeCreate(){
    
  },
  created() {
    
  },
  beforeMount(){
 
  },
  methods:{

    playVideo() {

      debugger

      window.player = null
      document.querySelector("#video").innerHTML = ''
      let _this = this;
      this.$nextTick(()=>[
          this.loadingVideo = false
      ])
      /* 选择播放视频时,同时缓存该视频的相关点位信息*/
      this.videoObject = {
          width: '100%', // 宽度，也可以支持百分比(不过父元素宽度要有)
          height: this.height, // 高度，也可以支持百分比
          container: "#video", // “#”代表容器的ID，“.”或“”代表容器的class
          variable: "player", // 该属性必需设置，值等于下面的new chplayer()的对象
          autoplay: true, // 自动播放
          loaded:'loadedHandler', // 监听播放器加载成功
          live: true,
          video:this.sourceUrls

      }
      window.player = new ckplayer(this.videoObject); // eslint-disable-line no-undef

      //超时播放，重新点击播放
      this.reloadPlayTime = setTimeout( ()=> {
          this.$Message.warning('获取视频超时，请重新播放...')
          this.sourceUrls = ''
          this.loadingVideo = false
          this.waiting = false
          this.disabledShot = true
          this.$emit('playStatus', false)
          window.player.videoClear()
          document.querySelector("#video").innerHTML = ''
      },30*1000)

      window.fullHandler = function (){//flashplayer状态下会调用该函数监听全屏切换
          console.log('已监听到全屏切换状态');
      };
      window.timeHandler=function (time){//flashplayer状态下为调用该函数监听当前播放时间
  //                    console.log('当前播放时间：'+time);
      };
      window.pauseHandler=function (){
          var metaData = window.player.getMetaDate();
          var html = ''
          html += '总时间：' + metaData['duration'] + '秒，';
          html += '音量：' + metaData['volume'] + '（范围0-1），';
          html += '播放器的宽度：' + metaData['width'] + 'px，';
          html += '播放器的高度：' + metaData['height'] + 'px，';
          html += '视频宽度：' + metaData['videoWidth'] + 'px，';
          html += '视频高度：' + metaData['videoHeight'] + 'px，';
          html += '视频原始宽度：' + metaData['streamWidth'] + 'px，';
          html += '视频原始高度：' + metaData['streamHeight'] + 'px，';
          html += '是否暂停状态：' + metaData['paused'];
      };
      window.playHandler=function (){//flashplayer状态下为调用该函数监听当前播放时间
  //                    console.log('当前播放中...：');
          var metaData = window.player.getMetaDate();
          if( !metaData['paused']) {
              console.log('playing...')
              _this.waiting = false
              _this.$emit('playStatus', true) // 当点位存在播放地址时，可以截图
              //当正在播放时，取消定时器
              clearTimeout(_this.reloadPlayTime);
          }
      };
      window.loadedHandler=function (){
  //                    console.log('已正确监听到播放器加载,loadedHandler');
          window.player.addListener('full',fullHandler);
          window.player.addListener('time',timeHandler);
          window.player.addListener('play', playHandler);
          window.player.addListener('pause', pauseHandler);

          var metaData = window.player.getMetaDate();
          var html = ''
          html += '总时间：' + metaData['duration'] + '秒，';
          html += '音量：' + metaData['volume'] + '（范围0-1），';
          html += '播放器的宽度：' + metaData['width'] + 'px，';
          html += '播放器的高度：' + metaData['height'] + 'px，';
          html += '视频宽度：' + metaData['videoWidth'] + 'px，';
          html += '视频高度：' + metaData['videoHeight'] + 'px，';
          html += '视频原始宽度：' + metaData['streamWidth'] + 'px，';
          html += '视频原始高度：' + metaData['streamHeight'] + 'px，';
          html += '是否暂停状态：' + metaData['paused'];
          console.log(html)
          function playHandler () {
              //正在播放
              console.log('正在播放')
          }
          function pauseHandler () {
              //正在播放
              console.log('暂停播放')
          }
          function fullHandler () {
              //正在播放
              console.log('全屏中')
          }

          function timeHandler () {
              //正在播放
              console.log('播放时间')
          }
      }
  },
    

  },
  mounted(){

    let _this = this;

    setTimeout(() => {
      
      //this.sourceUrls = 'https://ckplayer-video.oss-cn-shanghai.aliyuncs.com/sample-mp4/d0897b4e9ddd9a5.mp4';
      _this.sourceUrl = 'D:\test-file\b.mp4';

    }, 1500);

   
  },
  watch: {

    sourceUrl: {
        handler(newVal, oldVal) {
            if(this.reloadPlayTime) { //重新播放或者播放新视频时，清空定时器
                clearTimeout(this.reloadPlayTime);
            }
            if( newVal && newVal !== oldVal ) {
                this.waiting = true
                this.sourceUrls = newVal
                this.playVideo()
            }
        },
        // 代表在wacth里声明了firstName这个方法之后立即先去执行handler方法
        immediate: false
    }

  },
  beforeDestroy () {
      clearTimeout(this.reloadPlayTime);
      window.player  = null //销毁播放器
  },
  destroyed(){
   
  }

}
</script>
