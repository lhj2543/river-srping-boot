<style scoped>

  .river-player{
    width: 80%;
    position: absolute;
    left: 50%;
    top: 50%;
    transform: translateX(-50%)  translateY(-50%);
    -webkit-transform: translate(-50%,-50%);
    -ms-transform: translate(-50%,-50%);
  }

</style>

<template>

  <div class="river-player">

    <div id="video" style="width: 100%; height: 800px;"></div>
      
  </div>

</template>


<script>


import $ from 'jquery'

/* var oHead = document.getElementsByTagName('head')[0];
var oScript = document.createElement("script");
oScript.type = "text/javascript";
oScript.src = "/static/plugs/ckplayer/ckplayer/ckplayer.js";
oHead.appendChild(oScript); */

export default {
  name: 'ckplayer',
  data () {
          return {
           
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

    playVideo() {

      var videoObject = {
        playerID:'ckplayer01',//播放器ID，第一个字符不能是数字，用来在使用多个播放器时监听到的函数将在所有参数最后添加一个参数用来获取播放器的内容
				container: '#video', //容器的ID或className
				variable: 'player', //播放函数名称
				//loaded: 'loadedHandler', //当播放器加载后执行的函数
				loop: false, //播放结束是否循环播放
				autoplay: false, //是否自动播放
				//duration: 500, //设置视频总时间
				//forceduration:120000,
        //cktrack: '../material/en.srt', //字幕文件,
        /* cktrack:[
					['../material/en.srt','英文',0],
					['../material/zh.vtt','中文',1]
        ], */
        //crossorigin:'anonymous',//设置可以截图上
				cktrackdelay:0.2,//字幕延迟0.2秒显示
				//poster: '../material/poster.jpg', //封面图片
				/* preview: { //预览图片
					file: ['../material/mydream_en1800_1010_01.png', '../material/mydream_en1800_1010_02.png'],
					scale: 2
        }, */
        drag: 'start', //拖动的属性
				seek: 3, //默认跳转的时间
				playbackrate:1,//默认速度的编号，只对html5有效,设置成-1则不显示倍速
				//advertisements:'website:ad.json',
				promptSpot: [ //提示点
					{
						words: '提示点文字01',
						time: 30
					},
					{
						words: '提示点文字02',
						time: 150
					}
        ],
        //advertisements:'website:ad.json',//广告部分也可以用一个json文件来进行配置，可以动态文件
				//广告部分结束
				//config: '', //指定配置函数
				debug: true, //是否开启调试模式
				//flashplayer: true, //强制使用flashplayer
				mobileCkControls:true,//是否在移动端（包括ios）环境中显示控制栏
				mobileAutoFull:false,//在移动端播放后是否按系统设置的全屏播放
				//live:true,//是否是直播视频，true=直播，false=点播
				//h5container:'#videocontainer',//h5环境中使用自定义播放器ID
				//h5videoid:'videoplayer',//h5环境中使用自定义的播放器ID
				//html5m3u8:true,//是否在pc端环境使用hls播放m3u8
        video: [//视频地址列表形式
          ['http://localhost/attach/video/d.mp4', 'video/mp4', '标清', 0],
          ['http://localhost/attach/video/d2.mp4', 'video/mp4', '高清', 10],
          ['http://localhost/attach/video/d3.mp4', 'video/mp4', '超清', 0],
        ]
      };

      //window.player = new ckplayer(this.videoObject); // eslint-disable-line no-undef
      window.player = new ckplayer(videoObject); // eslint-disable-line no-undef


      function loadedHandler() {
				player.addListener('error', errorHandler); //监听视频加载出错
				player.addListener('loadedmetadata', loadedMetaDataHandler); //监听元数据
				player.addListener('duration', durationHandler); //监听播放时间
				player.addListener('time', timeHandler); //监听播放时间
				player.addListener('play', playHandler); //监听暂停播放
				player.addListener('pause', pauseHandler); //监听暂停播放
				player.addListener('buffer', bufferHandler); //监听缓冲状态
				player.addListener('seek', seekHandler); //监听跳转播放完成
				player.addListener('seekTime', seekTimeHandler); //监听跳转播放完成
				player.addListener('volume', volumeChangeHandler); //监听音量改变
				player.addListener('full', fullHandler); //监听全屏/非全屏切换
				player.addListener('ended', endedHandler); //监听播放结束
				player.addListener('screenshot', screenshotHandler); //监听截图功能
				player.addListener('mouse', mouseHandler); //监听鼠标坐标
				player.addListener('frontAd', frontAdHandler); //监听前置广告的动作
				player.addListener('wheel', wheelHandler); //监听视频放大缩小
				player.addListener('controlBar', controlBarHandler); //监听控制栏显示隐藏事件
				player.addListener('clickEvent', clickEventHandler); //监听点击事件
				player.addListener('videoClick', videoClickHandler); //监听播放器单击事件
				player.addListener('definitionChange', definitionChangeHandler); //监听清晰度切换事件
				player.addListener('loadTime', loadTimeHandler); //监听加载速度
			}
			function videoClickHandler(obj,name){
				console.log(obj,name);
			}
			function errorHandler() {
				//console.log('出错')
				changeText('.playerstate', '状态：视频加载错误，停止执行其它动作，等待其它操作');
			}

			function loadedMetaDataHandler() {
				var metaData = player.getMetaDate();
				var html = ''
				if(parseInt(metaData['videoWidth']) > 0) {
					changeText('.playerstate', '状态：获取到元数据信息，如果数据错误，可以使用延迟获取');
					html += '总时间：' + metaData['duration'] + '秒，';
					html += '音量：' + metaData['volume'] + '（范围0-1），';
					html += '播放器的宽度：' + metaData['width'] + 'px，';
					html += '播放器的高度：' + metaData['height'] + 'px，';
					html += '视频宽度：' + metaData['videoWidth'] + 'px，';
					html += '视频高度：' + metaData['videoHeight'] + 'px，';
					html += '视频原始宽度：' + metaData['streamWidth'] + 'px，';
					html += '视频原始高度：' + metaData['streamHeight'] + 'px，';
					html += '是否暂停状态：' + metaData['paused']+ '，';
					html += '已加载时间：' + metaData['loadTime']+'秒';
				} else {
					changeText('.playerstate', '状态：未正确获取到元数据信息');
					html = '没有获取到元数据';
				}
				changeText('.metadata', html);
			}
			function playHandler() {
				//player.animateResume();//继续播放所有弹幕
				changeText('.playstate', getHtml('.playstate') + ' 播放');
				window.setTimeout(function() {
					loadedMetaDataHandler();
				}, 1000);
				loadedMetaDataHandler();
			}

			function pauseHandler() {
				//player.animatePause();//暂停所有弹幕
				changeText('.playstate', getHtml('.playstate') + ' 暂停');
				loadedMetaDataHandler();
			}

			function timeHandler(time) {
				changeText('.currenttimestate', '当前播放时间（秒）：' + time);
			}
			
			function durationHandler(duration) {
				changeText('.duration', '总时间（秒）：' + duration);
			}
			function seekHandler(state) {
				changeText('.seekstate', getHtml('.seekstate') + ' ' + state);
			}

			function seekTimeHandler(time) {
				changeText('.seekstate', getHtml('.seekstate') + ' seekTime:' + time);
			}

			function bufferHandler(buffer) {
				//console.log(buffer);
				changeText('.bufferstate', '缓冲：' + buffer);
			}

			function volumeChangeHandler(vol) {
				changeText('.volumechangestate', '当前音量：' + vol);
			}
			function loadTimeHandler(t) {
				changeText('.loadtime', '当前加载的时间：' + t);
			}
			function screenshotHandler(obj) {
				changeText('.screenshot', '图片名称：' + obj['name'] + ',截图对象：' + obj['object'] + ',是否用户保存：' + obj['save'] + ',图片：<img src="' + obj['base64'] + '">');
			}

			function fullHandler(b) {
				if(b) {
					html = ' 全屏';
					player.changeSubtitlesSize(60,80);
				} else {
					html = ' 否';
					player.changeSubtitlesSize(18,30);
				}
				changeText('.fullstate', getHtml('.fullstate') + html);
				
			}

			function endedHandler() {
				changeText('.endedstate', '播放结束');
			}

			function mouseHandler(obj) {
				changeText('.mouse', '鼠标位置，x：' + obj['x'] + '，y：' + obj['y']);
			}

			function frontAdHandler(status) {
				changeText('.frontad', getHtml('.frontad') + ' ' + status);
			}
			var zoomNow = 1;

			function wheelHandler(n) {
				if(n > 0) {
					if(zoomNow < 5) {
						zoomNow += 0.1;
					}
				} else {
					if(zoomNow > 0) {
						zoomNow -= 0.1;
					}
				}
				player.videoZoom(zoomNow);//支持鼠标滚轮控制放大缩小
			}
			function controlBarHandler(show){
				if(show) {
					html = ' 显示';
				} else {
					html = ' 隐藏';
				}
				changeText('.controlBar', getHtml('.controlBar') + html);
			}
			function clickEventHandler(eve){
				changeText('.clickEvent', getHtml('.clickEvent') + ' '+eve);
			}
			function definitionChangeHandler(num){
				changeText('.definitionChange', getHtml('.definitionChange') + ',切换清晰度编号'+num);
			}
			var videoChangeNum = 0;

			function seekTime() {
				var time = parseInt(player.getByElement('.seektime').value);
				var metaData = player.getMetaDate();
				var duration = metaData['duration'];
				if(time < 0) {
					alert('请填写大于0的数字');
					return;
				}
				if(time > duration) {
					alert('请填写小于' + duration + '的数字');
					return;
				}
				player.videoSeek(time);
			}

			function changeVolume() {
				var volume = player.getByElement('.changevolume').value;
				volume = Math.floor(volume * 100) / 100
				if(volume < 0) {
					alert('请填写大于0的数字');
					return;
				}
				if(volume > 1) {
					alert('请填写小于1的数字');
					return;
				}
				player.changeVolume(volume);
			}

			function changeSize() {
				player.changeSize(w, h)
			}

			function frontFun() {
				alert('点击了前一集');
			}

			function nextFun() {
				alert('点击了下一集');
			}

			function adjump() {
				alert('点击了跳过广告按钮');
				//player.videoPlay();
			}

			function newVideo() {
				var videoUrl = player.getByElement('.videourl').value;
				changeVideo(videoUrl);
			}

			function newVideo2() {
				var videoUrl = player.getByElement('.videourl2').value;
				changeVideo(videoUrl);
			}

			function changeVideo(videoUrl) {
				if(player == null) {
					return;
				}

				var newVideoObject = {
					container: '#video', //容器的ID
					variable: 'player',
					autoplay: true, //是否自动播放
					loaded: 'loadedHandler', //当播放器加载后执行的函数
					video: videoUrl
				}
				//判断是需要重新加载播放器还是直接换新地址

				if(player.playerType == 'html5video') {
					if(player.getFileExt(videoUrl) == '.flv' || player.getFileExt(videoUrl) == '.m3u8' || player.getFileExt(videoUrl) == '.f4v' || videoUrl.substr(0, 4) == 'rtmp') {
						player.removeChild();

						player = null;
						player = new ckplayer();
						player.embed(newVideoObject);
					} else {
						player.newVideo(newVideoObject);
					}
				} else {
					if(player.getFileExt(videoUrl) == '.mp4' || player.getFileExt(videoUrl) == '.webm' || player.getFileExt(videoUrl) == '.ogg') {
						player = null;
						player = new ckplayer();
						player.embed(newVideoObject);
					} else {
						player.newVideo(newVideoObject);
					}
				}
			}
			var elementTemp = null; //保存元件
			
      

   },
    

  },
  mounted(){

    let _this = this;

    /* $.holdReady(true);    //hold住，等待a.js加载，后续代码不能执行
    $.getScript('/static/plugs/ckplayer/ckplayer.js',function(){
    　　$.holdReady(false);     //释放，a.js加载完成，继续执行后续代码

        _this.playVideo();
    }); */

   _this.playVideo();

   
  },
  watch: {

  },
  beforeDestroy () {
      
  },
  destroyed(){
   
  }

}
</script>
