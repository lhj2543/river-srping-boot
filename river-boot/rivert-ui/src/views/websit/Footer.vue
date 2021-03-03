<template>
    <footer id="index-footer" :class="contentActiveTheme" >
      <div class="left-contact">

        <div class="contact-d" v-for="(item, index) in footerData.contacts" :key="index" >
         
          <Poptip v-if="item.type=='img' || item.type=='text'" placement="right" trigger="hover">
            <Icon :type="item.icon"/>
            <div class="poptip-content" slot="content">

              <img v-if="item.type=='img'" width="150" height="150" :src="item.notes" />
              <div v-else-if="item.type=='text'" v-html="item.notes" style="color: #222 !important;"></div>

            </div>
          </Poptip>

          <a v-else-if="item.type=='link'" :title="item.notes" :href="item.notes" target="_blank" ><Icon :type="item.icon"/></a>

          <Icon v-else :type="item.icon" :title="item.title"  ></Icon>

        </div>

      </div>
      <span class="copyright" v-html="footerData.copyright"></span>
    </footer>
</template>


<script>
import {dateFormat} from '@/utils/date.js'

export default {
  name: 'index-footer',
  data(){
    return {
      footerData:{
        contacts:[
            {
              "title": "微信",
              "icon": "ios-chatbubbles",
              "type": "img",
              "notes": "/static/index/river-wecat.jpg"
            },
            {
              "title": "邮箱",
              "icon": "logo-twitch",
              "type": "text",
              "notes": "river_015@163.com"
            },
            {
              "title": "GitHub",
              "icon": "logo-github",
              "type": "link",
              "notes": "https://github.com/lhj2543/lhj-blog.git"
            },
            {
              "title": "点赞",
              "icon": "ios-thumbs-up-outline",
              "type": "event",
              "notes": ""
            }
        ],
        copyright:'Copyright © '+dateFormat(new Date(),'yyyy')+' RIVER'
      }
    }
  },
  props:{
      contentActiveTheme:String,
  },
  created(){

    let footerData = window.localStorage.getItem('footer');
    if(footerData && footerData!=null){
      let data = JSON.parse(footerData);

      let nowYear = dateFormat(new Date(),'yyyy');
      this.footerData.copyright = data.copyright?data.copyright.replace('{}',nowYear):this.footerData.copyright;
      this.footerData.contacts = data.contacts || [];
    }
    
  },
  mounted () {
  },
  methods: {
  },
  destroyed () {
  },

}
</script>


<style scoped>
  
  .copyright{
    position: fixed;
    bottom: 0;
    right: 0;
    padding: 2rem;
    padding-bottom: 1rem;
  }
  .left-contact{
    position: fixed;
    left: 0;
    padding: 2rem;
    bottom: 2rem;
  }
  .left-contact i{
    font-size: 1.6rem;
  }
  .contact-d{
    padding-top: 2rem;
    cursor: pointer;
  }
</style>
