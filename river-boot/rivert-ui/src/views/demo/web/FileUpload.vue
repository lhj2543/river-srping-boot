<style>
    .river-fileUpload{
        width: 700px;
        border: 1px solid #1DB954;
        min-height: 120px;
        position: absolute;
        top: 30%;
        left: 50%;
        transform: translateX(-50%)  translateY(-50%);
        -webkit-transform: translate(-50%,-50%);
        -ms-transform: translate(-50%,-50%);
        border-radius: 3px;
    }
    .river-fileUpload .title-panel{
        padding: 20px;
        text-align: right;
        border-bottom: 1px solid #1DB954;
    }
    .river-fileUpload .title-panel .title{
        float: left;
        font-weight: 550;
        font-size: 16px;
    }
    .river-fileUpload .title-panel .button{
        cursor: pointer;
        /* border: 1px solid #ccc; */
        padding: 5px 15px;
        background: #EB5E5E;
        color: #fff;
        border-radius: 2px;
    }

    .river-fileUpload .file-item{
        height: 70px;
        border-bottom: 1px solid #1DB954;
        padding:20px;
        padding-right: 0;
    }
    .river-fileUpload .file-item div{
        float: left;
    }
    .river-fileUpload .file-item .file-title{
        width: 75%;
        padding-right: 15px;
    }
    .river-fileUpload .file-item .item-button{
        width: 25%;
        text-align: right;
        padding-right:20px; 
    }
    .river-fileUpload .file-item .file-button{
        padding: 5px 10px;
        color: #1DB954;
        cursor: pointer;
        font-weight: 500;
    }
    .river-fileUpload .ivu-progress-inner{
        background-color: #ccc ;
    }
    .river-fileUpload .ivu-progress-show-info .ivu-progress-outer{
        margin-top:8px; 
    }
    .river-fileUpload .ivu-progress-show-info .ivu-progress-outer{
        padding-right: 25px;
    }
    .river-fileUpload .ivu-progress-text{
        margin-left: 31px;
    }
</style>
<template>
  <div class="river-fileUpload">

      <input type="file" style="display:none;" name="file" multiple ref="changeFile" @change="fileChange" /> 
      <span class="file-button" v-show="false" @click="download()">下载</span>
      <span class="file-button" v-show="false" @click="download2()">下载2</span>
      <div class="title-panel">
          <span class="title">文件断点续传下载</span>
          <span class="button" style="background:#0099CC;" @click="addFile">添加文件</span>
          <span class="button" v-if="fileItems.length>0"  @click="uploadAllFile">一键上传</span>
      </div>

      <div class="file-item-panel">

          <div class="file-item" v-for="(item, index) in fileItems" :key="index">
              <div class="file-title">
                
                <span >
                    <span class="file-name">{{item.file.name}}</span>
                    <span class="file-size" style="float:right;">{{parseFloat(item.file.size/(1024*1024)).toFixed(2)}}MB</span>
                </span>

                <Progress v-if="item.status!=0" :percent="item.process" :stroke-width="5" :stroke-color="['#108ee9', '#87d068']" />
                
              </div>

              <div class="item-button">
                <span class="file-button" v-if="item.status==0" @click="upload(index)">上传</span>
                <span class="file-button" v-if="item.status==1" @click="pause(index)">暂停</span>
                <span class="file-button" v-if="item.status==-1" @click="upload(index)">继续上传</span>
                <span class="file-button" v-if="item.status==2" >等待中</span>
                <span class="file-button" v-if="item.status==3" @click="download(item)">下载</span>
                <span class="file-button" v-if="item.status!=1" @click="removeFile(index)">删除</span>
              </div>

          </div>

      </div>

      <form  ref="downloadForm" :action="downloadUrl"  method="get"  >
          <input type="hidden" name="fileName" ref="fileName" />
      </form>
      <form  ref="downloadForm2" :action="downloadUrl2"  method="get"  >
          <input type="hidden"  name="fileName" ref="fileName2" />
      </form>

  </div>
</template>
<script>


export default {
  name: 'FileUpload',
  data(){
    return {
      downloadUrl:this.$globSetting.apiURL + '/site/public/fileBreakPoint/download',
      downloadUrl2:this.$globSetting.apiURL + '/site/public/fileBreakPoint/download2',
      chunkSize: 1024 * 102.4 * 1,  // 单个分块大小
      fileObj:{
        file:{},
        process: 0,//上传进度
        status: 0,//上传状态 0:未上传，1:上传中，2：等待中，3：上传完了,-1:已暂停
        count: 0, // 切成的总片数
        index:0,//当前切片的下标
        indexs:[],//已上传完了的切片下标
      },
      fileItems:[//上传文件集合
      ],
      maxfileMQ:3,//最多只能同时上传个数
      fileMQ:{}//最多同步上传3个，超过3个等待中

    }
  },
  mounted(){

  },
  methods: {
    //添加文件  
    addFile(){
        this.$refs.changeFile.dispatchEvent(new MouseEvent('click'));
    },
    fileChange(){

        let files = this.$refs.changeFile.files;
        
        for(let i=0;i<files.length;i++){
            let file = files[i];

            let count = Math.ceil(file.size / this.chunkSize);
            if(count>10*5){
                this.$Notice.info({title:'提示',desc: file.name + ",单个文件不能大于5MB"});
                continue;
            }
           
            if(this.fileItems.length>=5){
                this.$Notice.info({title:'提示',desc: "最多只能上传5个文件"});
                break;
            }
            let f = Object.assign({},this.fileObj,{
                file:file,
                count:count,
                index:0,
                indexs:[]
            });
            this.fileItems.push(f);
        }

    },
    //删除文件
    removeFile(index){
        
        this.$delete(this.fileMQ,this.fileItems[index].file.name);
        this.fileItems.splice(index,1);
        return;

        let file = this.fileItems[index].file;

        if(this.fileItems[index].status!=3){
            this.fileItems.splice(index,1);
            return;
        }

        this.axios({
            url: '/site/public/fileBreakPoint/delete',
            method: 'delete',
            params: {fileName:file.fileName},
        })
        .then((res)=>{
            this.$Notice.info({title:'提示',desc: res.message});

            if(res.success){
                this.fileItems.splice(index,1);
            }

        })
        .catch((error)=>{        // 发生断网等意外
            this.$Notice.error({title:'异常',desc: error});
        });

    },
    //上传所有文件
    uploadAllFile(){
       for(let i=0;i< this.fileItems.length ;i++){
           this.upload(i);
       }
    },
    //上传
    upload(i){
        this.$set(this.fileItems[i],'status',1);
        this.uploadFile(i);
    },
    // 暂停/继续 上传
    pause(i){

      if(this.fileItems[i].status !=-1 ){
        this.$set(this.fileItems[i],'status',-1);
        this.$set(this.fileItems[i],'index',0);
        this.$delete(this.fileMQ,this.fileItems[i].file.name);
        //this.$forceUpdate();
      }

    },
    //上传文件
    uploadFile(i){


        let fileItem = this.fileItems[i];

        let file = fileItem.file;
        let fileName = file.name;

        let index = fileItem.index;
        let indexs = fileItem.indexs;
        let count = fileItem.count;

        var fileMQKeys = Object.keys(this.fileMQ); 

        if(fileMQKeys.indexOf(fileName) === -1 && fileMQKeys.length<this.maxfileMQ){
            this.$set(this.fileMQ,fileName,i);
        }

       
        if(fileMQKeys.length>=this.maxfileMQ && fileMQKeys.indexOf(fileName) === -1){
            //最多同步上传3个文件
            this.$set(this.fileItems[i],'status',2);
        }

        if(index>count){
            //上传完了
            this.$set(fileItem,'status',3);
            
            //上传完了队列中移除
            //delete this.fileMQ[fileName];
            this.$delete(this.fileMQ,fileName);
        }

        if(fileItem.status==3 || fileItem.status==-1 || fileItem.status==2){
            return;
        }
        
        let end = (index + 1) > count? count : index + 1;

        let sliceFile = file.slice(index * this.chunkSize,end * this.chunkSize);

        let form = new FormData();

        form.append("file",sliceFile);
        form.append("fileName",file.name);
        form.append("fileSize",file.size);

        //上传状态 0:未上传，1:上传中，2：等待中，3：上传完了,-1:已暂停
        if(indexs.indexOf(index) === -1) {   // 未上传过该分片

            this.axios({
                url: '/site/public/fileBreakPoint/upload',
                method: 'post',
                data: form
            })
            .then((res)=>{
                //文件已上传过直接完成
                if(res.code == 4000){
                    index = count;
                }

                fileItem.indexs.splice(-1,0,index);
                fileItem.process = Math.floor(index/count *100);

                if(fileItem.status == 1) {
                    setTimeout(() => {
                        this.$set(fileItem,'index',index+1);
                        this.uploadFile(i);
                    }, 100);
                }
               

            })
            .catch((err)=>{        // 发生断网等意外
                this.$set(fileItem,'status',-1);
                alert(err);
            });

        } else {
            this.$set(fileItem,'index',fileItem.index+1)
            this.uploadFile(i);
        }


    },
    download(fileItem){
        this.$refs.fileName.value = fileItem.file.name
        this.$refs.downloadForm.submit();
    },
    download2(fileItem){

        this.$refs.downloadForm2.submit();

    },

  },
    watch: {//数据监听
       fileMQ: {
            handler(newVal,oldVal) {
                
                let fileMQKeys = Object.keys(newVal);
                if(fileMQKeys.length<this.maxfileMQ){

                    for(let i in this.fileItems){
                        let fileItem = this.fileItems[i];
                        if(fileItem.status == 2){
                            this.upload(i);
                        }
                    }
                }
            },
            deep: true,
            immediate: true
        }
    },
    beforeDestroy(){
        
    }

}
</script>
