<style scoped>
  
  .htmlTopPdf{
    border:1px solid #1DB954;
    width: 1240px;
    height: 1754px;
    margin: 0 auto;
    padding: 50px 20px;
  }

  .exportPdf{
    padding: 10px 30px;
    border: none;
    background: orange;
    color: #fff;
    margin-top: 20px;
    font-weight: 600;
    cursor: pointer;
  }

  #downloadForm{
    border:1px solid #1DB954;
    width: 1240px;
    margin: 20px auto;
    padding: 20px 20px;
    font-size: 17px;
  }
  #downloadForm div{
    padding: 5px 0;
  }
  #downloadForm input{
    border:1px solid #999;
    height: 40px;
  }

  .report_content div{
    padding: 20px 0;
  }

</style>

<template>

  <div>

    <form  id="downloadForm" :action="downloadForm.url"  method="post"  >
        <input type="hidden" v-model="downloadForm.htmlConnect" name="htmlConnect" id="htmlConnect" />
        <div>
          wkhtmltopdf转换参数(官网：<a target="_blank" href="https://wkhtmltopdf.org/">https://wkhtmltopdf.org/</a>  參考博客：<a target="_blank" href="https://www.jianshu.com/p/4d65857ffe5e/">https://www.jianshu.com/p/4d65857ffe5e/</a>)
          <input type="text" v-model="downloadForm.params" name="params" id="params" style="width:100%;" />
        </div>

        <div>
          转换PDF的页面url地址：
        <input type="text" v-model="downloadForm.remoteUrl" name="remoteUrl" id="remoteUrl" style="width:100%;" />
        </div>

        <button class="exportPdf" @click="exportPdf">导出PDF</button> 
        &nbsp; &nbsp; 注：不输入页面url情况下是导出本页面
        
    </form>


    <div  class="htmlTopPdf"  >

      
      <div class="report_content"  id="report_content">

        <h1>
          HTML 转 PDF 之 wkhtmltopdf
        </h1>

        <br/>

        <h2>
          命令参数
        </h2>

        <div>
          命令参数包含五部分，分别是“全局参数”，“大纲参数选项”，“页面对象参数”，“页眉和页脚参数选项”和“目录对象参数”。
        </div>
        
        <h2>
          全局参数
        </h2>
        <div>
          <pre class="hljs xml"><code class="xml">    
                --collate             当输出多个副本时进行校验(这是默认设置)
                --no-collate          当输出多个副本时不进行校验
                --cookie-jar <span class="hljs-tag">&lt;<span class="hljs-name">path</span>&gt;</span>   从提供的JAR文件中读写cookie数据
                --copies <span class="hljs-tag">&lt;<span class="hljs-name">number</span>&gt;</span>     设置输出副本的数量(默认主1)，其实为1就够了
            -d, --dpi <span class="hljs-tag">&lt;<span class="hljs-name">dpi</span>&gt;</span>           指定一个要分辨率(这在 X11 系统中并没有什么卵用)
            -H, --extended-help       相对 -h 参数，显示更详细的说明文档
            -g, --grayscale           指定以灰度图生成PDF文档。占用的空间更小
            -h, --help                显示帮助信息  
                --htmldoc             输出程序的html帮助文档
                --image-dpi <span class="hljs-tag">&lt;<span class="hljs-name">integer</span>&gt;</span> 当页面中有内嵌的图片时，
                                      会下载此命令行参数指定尺寸的图片(默认值是 600)
                --image-quality <span class="hljs-tag">&lt;<span class="hljs-name">interger</span>&gt;</span> 当使用 jpeg 算法压缩图片时使用这个参数指定的质量(默认为 94)
                --license             输出授权信息并退出
            -l, --lowquality          生成低质量的 PDF/PS ,能够很好的节约最终生成文档所占存储空间
                --manpage             输出程序的手册页
            -B, --margin-bottom <span class="hljs-tag">&lt;<span class="hljs-name">unitreal</span>&gt;</span> 设置页面的 底边距
            -L, --margin-left <span class="hljs-tag">&lt;<span class="hljs-name">unitreal</span>&gt;</span>   设置页面的 左边距 (默认是 10mm)
            -R, --margin-right <span class="hljs-tag">&lt;<span class="hljs-name">unitreal</span>&gt;</span>  设置页面的 右边距 (默认是 10mm)
            -T, --margin-top <span class="hljs-tag">&lt;<span class="hljs-name">unitreal</span>&gt;</span>    设置页面的 上边距
            -O, --orientation <span class="hljs-tag">&lt;<span class="hljs-name">orientation</span>&gt;</span> 设置为“风景(Landscape)”或“肖像(Portrait)”模式,
                                            默认是肖像模块(Portrait)
                --page-height <span class="hljs-tag">&lt;<span class="hljs-name">unitreal</span>&gt;</span>   页面高度
            -s, --page-size <span class="hljs-tag">&lt;<span class="hljs-name">Size</span>&gt;</span>         设置页面的尺寸，如：A4,Letter等，默认是：A4
                --page-width <span class="hljs-tag">&lt;<span class="hljs-name">unitreal</span>&gt;</span>    页面宽度
                --no-pdf-compression       不对PDF对象使用丢失少量信息的压缩算法，不建议使用些参数，
                                          因为生成的PDF文件会非常大。
            -q, --quiet                    静态模式，不在标准输出中打印任何信息
                --read-args-from-stdin     从标准输入中读取命令行参数，后续会有针对此指令的详细介绍，
                                          请参见 **从标准输入获取参数**
                --readme                   输出程序的 readme 文档
                --title <span class="hljs-tag">&lt;<span class="hljs-name">text</span>&gt;</span>             生成的PDF文档的标题，如果不指定则使用第一个文档的标题
            -V, --version                  输出版本信息后退出
            </code></pre>
        </div>

        <h3>--copies N</h3>
        <div>
          N 是一个正整数。
          这个选项可以先不用关心了，因为你这辈子可能都用不到。他的作用是在生成的PDF文档中，把内容重复输出 N 份。也就是说，你将得到一个PDF文档，这个文档中的大小、内容量都将是不使用此参数时的 N 倍。然而重复的内容对你来说并没有什么卵用。
          如果不使用 --copies 参数，那么 --collate 和 --no-collate 参数就不用了解了，因为他们只在 --copies 参数中的 N 大于 1 时才有意义。
        </div>

        <h3>-g, --grayscale</h3>
        <div>
            这个参数非常有用，使用这个参数可以有效压缩生成的PDF所占用的存储空间。当然这个压缩是要付出一定代价的，那就是最终生成的PDF文档将是灰度的，没有任何色彩。如果你能接受灰度PDF文档，并不影响实际使用，那就请使用这个参数吧。生成的PDF文档越大，使用此参数获得的惊喜就越大。
        </div>

        <h3>-l, --lowquality</h3>
        <div>
            这个参数与 -g 参数有异曲同工之妙， -l 参数也会大大压缩PDF文档所占用的存储空间。只是它是通过降低PDF文档的质量来完成这一任务的。这个参数也值得推荐，你最好先尝试一下，看看使用此参数后生成的PDF文档与不使用此参数的区别再做决定。我可以告诉你的是，在纯文字的情况下他们的差别不大，此参数只是降低了PDF文档的质量，看上去是糙了一些，但不会影响阅读。如果你是一个追求感官享受，或是你生成的PDF文档中有大量图片，那就不要使用此参数了。
        </div>

        <h3>--no-pdf-compression</h3>
        <div>
            这个参数强烈建议不要使用，最好这辈子都不要去了解他的好，因为对于你来说肯定用不到。它的作用就是在输出PDF文档时，不使用任何的压缩。这将会导致输出的PDF文档特别的大，质量是无损的，但是对于人类来说从感观上根本察觉不到压缩前后的质量变化的。如果你的感观超乎于常人，压缩之后的体验对你来说无法接受，那我收回前面的话，你就尽情使用此参数吧
        </div>


        <h3>-q, --quiet</h3>
        <div>
          使用这个参数后，你将得到一个干净的命令行输出，就连程序处理的进度和状态都没有。这个参数会抑制所有命令行输出，在程序的工作过程中，你看不到任何输出。建议不会使用此参数，因为程序输出一些进度和状态信息还是非常有用的。万一程序工作到某处死了呢(嘿嘿)，在 -q 模式下你是无法分辨是否程序死掉了的。
        </div>

      </div>

    </div>

    <p class="pages-p" style="height:20px;"></p>

    <div  class="htmlTopPdf" >

      <div class="report_content" id="report_content">

        <h2>页面对象参数</h2>
        <div>
          <pre class="hljs xml"><code class="xml">    
            --allow <span class="hljs-tag">&lt;<span class="hljs-name">path</span>&gt;</span>                指定加载HTML中相对路径文件的目录(可重复使用此参数指定多个
                                  目录)，这个参数会在后面进行更详细的讲解
              --background                  输出页面背景到PDF文档(这是默认设置)
              --no-background               不输出页面背景到PDF文档
              --cache-dir <span class="hljs-tag">&lt;<span class="hljs-name">path</span>&gt;</span>            网页的缓存目录
              --checkbox-checked-svg <span class="hljs-tag">&lt;<span class="hljs-name">path</span>&gt;</span> 使用指定的SVG文件渲染选中的复选框
              --checkbox-svg <span class="hljs-tag">&lt;<span class="hljs-name">path</span>&gt;</span>         使用指定的SVG文件渲染未选中的筛选框
              --cookie <span class="hljs-tag">&lt;<span class="hljs-name">name</span>&gt;</span> <span class="hljs-tag">&lt;<span class="hljs-name">value</span>&gt;</span>       设置访问网页时的cookie,value 需要进行url编码
                                            (可重复使用此参数指定多个cookie)
              --custom-header <span class="hljs-tag">&lt;<span class="hljs-name">name</span>&gt;</span> <span class="hljs-tag">&lt;<span class="hljs-name">value</span>&gt;</span> 设置访问网页时的HTTP头(可重复使用此参数指定多个HTTP头)
              --custom-header-propagation   为每个要加载的资源添加由 --custom-header 指定的HTTP头
              --no-custom-header-propagation 不为每个要加载的资源添加由 --custom-header 指定的HTTP头
              --debug-javascript            显示javascript调试输出的信息
              --no-debug-javascript         不显示javascript调试输出的信息(这是默认设置)
              --default-header              添加一个默认的“头”，在页面的左头显示页面的名字，
                                            在页面的右头显示页码，这相对于进行了如下设置：
                                            --header-left='[webpage]'
                                            --header-right='[page]/[toPage]'
                                            --top 2cm
                                            --header-line
              --encoding <span class="hljs-tag">&lt;<span class="hljs-name">encoding</span>&gt;</span>         为输入的文本设置默认的编码方式
              --disable-external-links      禁止页面中的外链生成超链接
              --enable-external-links       允许页面中的外链生成超链接(这是默认设置)
              --disable-forms               不转换HTML表单为PDF表单(这是默认设置)
              --enable-forms                转换HTML表单为PDF表单
              --images                      加载图片并输出到PDF文档(这是默认设置)
              --no-images                   在生成的PDF文档中过滤掉图片
              --disable-internal-links      禁止页面中的内链生成超链接
              --enable-internal-links       允许页面中的内链生成超连接(这是默认设置)
          -n, --disable-javascript          禁止WEB页面执行 javascript
              --enable-javascript           允许WEB页面执行 javascript(这是默认设置)
              --javascript-delay <span class="hljs-tag">&lt;<span class="hljs-name">msec</span>&gt;</span>     延迟一定的毫秒等待javascript 执行完成(默认值是200)
              --load-error-handling <span class="hljs-tag">&lt;<span class="hljs-name">handler</span>&gt;</span> 指定当页面加载失败后的动作，可以指定为：abort(中止)、
                                              ignore(忽略)、skip(跳过)；(默认值是：abort)
              --load-media-error-handling <span class="hljs-tag">&lt;<span class="hljs-name">handler</span>&gt;</span> 指定当媒体文件加载失败后的动作，可以指定为：
                                                    abort(中止)、ignore(忽略)、skip(跳过)；
                                                    (默认值是：ignore)
              --disable-local-file-access   不允许一个本地文件加载其他的本地文件，使用命令行参数
                                            `--allow` 指定的目录除外。
              --enable-local-file-access    允许本地文件加载其他的本地文件(这是默认设置)
              --minimum-font-size <span class="hljs-tag">&lt;<span class="hljs-name">int</span>&gt;</span>     设置最小的字号，除非必要不推荐使用该参数
              --exclude-from-outline        拒绝加载当前页面到PDF文档的目录和大纲中
              --include-in-outline          加载当前页面到PDF文档的目录和大纲中(这是默认设置)
              --page-offset <span class="hljs-tag">&lt;<span class="hljs-name">offset</span>&gt;</span>        设置页码的起始值(默认值为0)
              --password <span class="hljs-tag">&lt;<span class="hljs-name">password</span>&gt;</span>         HTTP身份认证的密码
              --disable-plugins             禁止使用插件(这是默认设置)
              --enable-plugins              允许使用插件，但插件可能并不工作
              --post <span class="hljs-tag">&lt;<span class="hljs-name">name</span>&gt;</span> <span class="hljs-tag">&lt;<span class="hljs-name">value</span>&gt;</span>         添加一个POST字段，可以重复使用该参数添加多个POST字段。
              --post-file <span class="hljs-tag">&lt;<span class="hljs-name">name</span>&gt;</span> <span class="hljs-tag">&lt;<span class="hljs-name">value</span>&gt;</span>    添加一个POST文件，可以重复使用该参数添加多个文件。
              --print-media-type            用显示媒体类型代替屏幕
              --no-print-media-type         不用显示媒体类型代替屏幕
          -p, --proxy <span class="hljs-tag">&lt;<span class="hljs-name">proxy</span>&gt;</span>               使用代理
          --radiobutton-checked-svg <span class="hljs-tag">&lt;<span class="hljs-name">path</span>&gt;</span>  使用指定的SVG文件渲染选中的单选框
          --radiobutton-svg <span class="hljs-tag">&lt;<span class="hljs-name">path</span>&gt;</span>          使用指定的SVG文件渲染未选中的单选框
          --run-sript <span class="hljs-tag">&lt;<span class="hljs-name">js</span>&gt;</span>                  页面加载完成后执行一个附加的JS文件，可以重复使用此参数指定
                                            多个要在页面加载完成后要执行的JS文件。
          --disable-smart-shrinking         不使用智能收缩策略
          --enable-smart-shrinking          使用智能收缩策略(这是默认设置)
          --stop-slow-scripts               停止运行缓慢的javascript代码(这是默认设置)
          --no-stop-slow-scripts            不停止运行缓慢的javascript代码
          --disable-toc-back-links          禁止从标题链接到目录(这是默认设置)
          --enable-toc-back-links           允许从标题链接到目录
          --user-style-sheet <span class="hljs-tag">&lt;<span class="hljs-name">url</span>&gt;</span>          设置一个在每个页面都加载的用户自定义样式表
          --username <span class="hljs-tag">&lt;<span class="hljs-name">username</span>&gt;</span>             HTTP身谁的用户名
          --viewport-size <span class="hljs-tag">&lt;&gt;</span>                设置窗口大小,需要你自定义滚动条或css属性来自适应窗口大小。
          --window-status <span class="hljs-tag">&lt;<span class="hljs-name">windowStatus</span>&gt;</span>    Wait until window.status is equal to
                                            this string before rendering page
          --zoom <span class="hljs-tag">&lt;<span class="hljs-name">float</span>&gt;</span>                    设置转换成PDF时页面的缩放比例(默认为1)
          </code></pre>
        </div>

      </div>

    </div>

    <!-- <div  class="htmlTopPdf" >

      <div class="report_content" id="report_content">
      
        <h2>
          大纲参数选项
        </h2>
        <div>
          <pre class="hljs xml"><code class="xml">
          --dump-default-toc-xsl     输出默认的 TOC xsl 样式表到标准输出
          --dump-outline <span class="hljs-tag">&lt;<span class="hljs-name">file</span>&gt;</span>      输出“大纲”到指定的文件(文件内容为xml)
          --outline                  在生成的PDF文档中输出“大纲”(这是默认设置)
          --no-outline               不在pdf文档中输出大纲
          --outline-depth <span class="hljs-tag">&lt;<span class="hljs-name">level</span>&gt;</span>    设置生成大纲的深度(默认为 4)
          </code></pre>
        </div>
        <div style="color:orange;">
          大纲参数中唯一需要特别说一下的是 --outline-depth ，其他参数默认就好了。
          PDF目录或导航。大纲是根据你HTML中的标题(H1,H2...Hn标签)自动生成的。
        </div>

      </div>

    </div> -->


  </div>

</template>

<script>

import $ from 'jquery'

export default {
  name: 'htmlToPdf',
  data () {
          return {
            downloadForm:{
              url:this.$globSetting.apiURL + '/site/public/htmlToPdf/export',
              htmlConnect:'',
              params:'',
              remoteUrl:''
            }
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
    //导出pdf
    exportPdf(e){

      $('#downloadForm').hide();
      $('.pages-p').hide();
      var html = $('html').prop("outerHTML");
      $('#downloadForm').show();
      $('.pages-p').show();

      var htmlStr = window.encodeURIComponent(html);
      //var htmlStr = window.encodeURIComponent("<html><body>" + $("#report_content").html() + "</body></html>");
      var htmlStrBase64 = window.btoa(htmlStr);

      if(!this.downloadForm.remoteUrl){
        this.$set(this.downloadForm,'htmlConnect',htmlStrBase64);
        $('#htmlConnect').val(htmlStrBase64);
      }

      $("#downloadForm").submit();

    },
   

  },
  mounted(){


  },
  destroyed(){
   
  }

}
</script>
