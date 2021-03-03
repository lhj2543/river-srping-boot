<template>

    <div class="content-panel" style="height:100%;">

        <div class="content-card" style="height:100%;">
        <div class="header" >
            <span class="title" >
            <Icon :type="$common.icon.detail" />
                job 管理
            </span>
            
            <span class="right-tool" style="float:right">
                <Icon title="刷新"  size="18" @click="refresh" class="cursor-pointer"  :type="$common.icon.refresh" />
            </span>
        </div>
        <div class="card-body" style="height:calc(100vh - 60px);max-height:999999999px">

            <Split v-model="split1" min="250px" max="900px" >
                <div slot="left" class="trees-split-pane" >
                    
                    <Tree :data="trees" :render="renderContent" class="trees-tree" @on-select-change="menuOnSelectChange" ref="treesTree"  ></Tree>

                </div>
                <div slot="right"   class="trees-split-pane" :style="{padding:'40px'}">
                    
                    <Form ref="treeForm" :model="treeForm" :rules="ruleValidate" :label-width="100" :disabled="isDisabled">

                        <Row>
                            <Col span="12">
                                <FormItem label="类型" prop="type">
                                     <Select :disabled="isDisabled" v-model="treeForm.type" placeholder="请选择类型..." >
                                        <Option  v-for="(value,key) in items['quartz_job_type']" :key="key+'-'+value" :value="key" >{{value}}</Option>
                                    </Select>
                                </FormItem>
                            </Col>
                            <Col span="12">
                                <FormItem label="排序" prop="sortKey">
                                    <InputNumber v-model="treeForm.sortKey"  placeholder="请输入排序..." :min="0" style="width:100%;"></InputNumber>
                                </FormItem>
                            </Col>
                        </Row>

                        <Row>
                            <Col span="12">
                                <FormItem label="显示名" prop="displayName">
                                    <Input v-model="treeForm.displayName" placeholder="请输入显示名..."></Input>
                                </FormItem>
                            </Col>

                            <Col span="12">
                                <FormItem :label="treeForm.type==1?'组别code':'job名'" prop="name">
                                    <Input v-model="treeForm.name" :disabled="treeForm.sid!=''" placeholder="请输入组别名..."></Input>
                                </FormItem>
                            </Col>
                            
                        </Row>

                        <Row v-if="treeForm.type!=1">
                            <Col span="12">
                                <FormItem label="类名" prop="className">
                                    <Input v-model="treeForm.className" placeholder="请输入类名..."></Input>
                                </FormItem>
                            </Col>
                            <Col span="12">
                                <FormItem label="job组名" prop="jobGroup">
                                    <Input v-model="treeForm.jobGroup" disabled placeholder="请输入job组名..."></Input>
                                </FormItem>
                            </Col>
                        </Row>

                        <FormItem v-if="treeForm.type!=1" label="job参数" prop="jobData">
                            <Input type="textarea" maxlength="500" :disabled="isDisabled" show-word-limit v-model="treeForm.jobData" placeholder="请输job参数(JSON格式)..." :rows="8" ></Input>
                        </FormItem>


                        <FormItem label="描述" prop="notes">
                            <Input type="textarea" maxlength="500" :disabled="isDisabled" show-word-limit v-model="treeForm.notes" placeholder="请输描述..." :rows="5" ></Input>
                        </FormItem>

                        

                        <FormItem v-if="isAdd" :style="{padding:'20px 0','text-align':'center'}">
                            <Button type="primary" @click="handleSubmit('treeForm')"  >保存</Button>
                            <Button @click="handleReset('treeForm')" style="margin-left: 8px"  >重置</Button>
                        </FormItem>

                    </Form>

                </div>
            </Split>
            

        </div>
            
        </div>
        
        <!-- loadding 防止多次点击 -->
        <Spin fix v-if="loading">
            <Icon type="ios-loading" size=18 class="spin-icon-load"></Icon>
            <div>数据加载中请稍后</div>
        </Spin>
        
    </div>
    
</template>

<script>

    let menuObj= {
                sid:'',
                parentId:'',
                type: '',
                calssName: '',
                name: '',
                displayName: '',
                jobGroup: '',
                jobData: '',
                sortKey: 0,
                notes: '',
            };


export default {
    name:'jobDetail',
    data(){
        return{
            split1:0.2,
            loading:false,
            isAdd:false,
            items:{},//字典数据
            trees:[],
            buttonProps: {
                type: 'text',
                /* size: 'small', */
            },
            treeForm:Object.assign({},menuObj,{}),
            isDisabled:false,//表单按钮是否不可点击
            ruleValidate: {
                type: [
                    { required: true, message: '类型必填!', trigger: 'blur' }
                ],
                name: [
                    { required: true, message: '名称必填!', trigger: 'blur' }
                ],
                displayName: [
                    { required: true, message: '显示名必填!', trigger: 'blur' }
                ],
                notes: [
                    { type: 'string', max: 500, message: '最大不能超过500个字！', trigger: 'blur' }
                ]
            }
        }
    },
    created() {

        /* 加载页面上所有用到的字典 */
        let params = {categoryNames:['quartz_job_type']};
        this.getSysItems(params,(data)=>{
            this.items = data.itemMap?data.itemMap:{};
        });

        this.loadtrees();//加载树
    },
    methods: {
        refresh(){//刷新
            this.loadtrees();
            this.treeForm=Object.assign({},menuObj,{});
        },
        openAll(){//展开所有树
            
        },
        closeAll(){//收缩所有树

        },
        loadtrees(){//加载树
            let _this = this;
            this.loading = true;
            this.axios.get('/site/quartzJobDetail/query',{params:{expand:true}})
            .then((response)=>{
                _this.loading=false;
                if(response.success){
                    this.trees = response.data;
                }else{
                    this.$Notice.info({title:'提示',desc: response.message});
                }
            })
            .catch((error)=>{
                _this.loading=false;
                this.$Notice.error({title:'异常',desc: response.message});
            });

        },
        renderContent (h, { root, node, data }) {//自定义树样式
            /* console.log(root);
            console.log(node);
            console.log(data); */
            var menuIcon='ios-paper-outline';
            if(data.children!=undefined && data.children.length>0){
                menuIcon='ios-folder-outline';
            }

            var removeButton=h('Button', 
                {
                props: Object.assign({}, this.buttonProps, {
                    icon: 'ios-remove'
                }),
                on: {
                    click: (e) => { 
                        e.stopPropagation();//阻止事件冒泡
                        this.remove(root, node, data) 
                    }
                }
            });

            if(data.parentId=='-1'){
                removeButton=h();
            }


            return h('span', 
                {
                    style: {
                        display: 'inline-block',
                        width: '100%'
                    }
                }, 
                [
                    h('span', [
                        h('Icon', {
                            props: {
                                type: menuIcon
                            },
                            style: {
                                marginRight: '8px'
                            }
                        }),
                        h('span', data.refData.displayName)
                    ]),
                    h('span', {
                        style: {
                            display: 'inline-block',
                            float: 'right',
                            marginRight: '2px',
                            marginLeft:'5px'
                        }
                    }, 
                    [
                        h('Button', {
                            props: Object.assign({}, this.buttonProps, {
                                icon: 'ios-add'
                                /* ,ghost:'' */
                            }),
                            style: {
                                marginRight: '2x'
                            },
                            on: {
                                click: (e) => { 
                                    e.stopPropagation();//阻止事件冒泡
                                    this.append(root, node, data); 
                                }
                            }
                        }),
                        removeButton
                    ])
                ]
            );
        },
        append (root, node, data) {//添加树
            //判断父节点是否为空
            if(!data.refData.sid || data.refData.sid==''){
                this.$Message.info('请先保存该节点数据！');
                return;
            }
            /* console.log("============添加树=========");
            console.log(data);
            console.log(node); */
            var selectedNodes=this.$refs.treesTree.getSelectedNodes();
            //console.log(selectedNodes);
            selectedNodes.forEach(node => {
                node.selected=false;
            });

            const children = data.children || [];
            var newMenu=Object.assign({},menuObj,{
                sid:'',
                displayName:'新建节点',
                parentId:data.sid,
                type:'1',
                jobGroup:data.refData.name
            });
            this.getMaxNumber(newMenu);


            let treeNode = {
                sid:newMenu.sid,
                displayName:newMenu.displayName,
                parentId:newMenu.parentId,
                selected:true,
                expand:true,
                refData:newMenu
            }

            //this.treeForm=Object.assign({},newMenu,{});
            this.treeForm=treeNode.refData;
            children.push(treeNode);

            this.isAdd=true;
            this.$set(data, 'children', children);
        },
        remove (root, node, data) {//移除树
            /* console.log(root);
            console.log(node);
            console.log(data); */
            
            let sid = data.refData.sid;
            let _this = this;
            //alert(sid);

            const parentKey = root.find(el => el === node).parent;
            const parent = root.find(el => el.nodeKey === parentKey).node;
            const index = parent.children.indexOf(data);

            if(sid){

                this.$Modal.confirm({
                    title: '您确定要删除分类？',
                    content: '<p>删除该树同时会删除该分类下所有子分类！</p>',
                    onOk: () => {
                        
                        //后台删除数据
                        this.axios.post('/site/quartzJobDetail/deletes',[data.refData],{headers:{type:'json'}})
                        .then((response)=>{

                            this.$Notice.info({title:'提示',desc: response.message});
                            if(response.success){
                                refreshFormData(index);
                            }
                            
                        })
                        .catch((error)=>{
                             this.$Notice.error({title:'异常',desc: error});
                        });
                    },
                    onCancel: () => {
                        //this.$Message.info('取消删除树');
                    }
                });

            }else{
                refreshFormData(index);
            }

            function refreshFormData(index){
                parent.children.splice(index, 1);
                _this.isAdd=false;
                _this.treeForm = menuObj;
            }

        },
        menuOnSelectChange(selectNodes,node){//树选中事件
            /* console.log("============树选中=========");
            console.log(node); */
            //表单赋值
            //var k=Object.assign({},node,{});
            this.treeForm=node.refData;
            
            this.isAdd=true;
            //this.isDisabled=this.treeForm.parentId=='-1'?true:false;//根节点不能修改
            
        },
        handleSubmit (name) {//表单提交
            let _this = this;
            this.$refs[name].validate((valid) => {

                if (valid) {
                    
                    _this.loading=true;
                    _this.axios.post('/site/quartzJobDetail/save',_this.treeForm,{headers:{type:'json'}})
                    .then((response)=>{
                        
                        _this.loading=false;
                        this.$Notice.info({title:'提示',desc: response.message});
                        if(response.success){
                            let sid = response.data;
                            _this.treeForm.sid = sid;
                        }
                    })
                    .catch((error)=>{
                        _this.loading=false;
                        this.$Notice.error({title:'异常',desc: error});
                    });
                    
                }

            })
        },
        handleReset (name) {//表单重置
            this.treeForm.name = '';
            this.treeForm.displayName = '';
            this.treeForm.className = '';
            this.treeForm.jobData = '';
            this.treeForm.notes = '';
        },
        getMaxNumber(params){

            let data = {
                tableName:'quartz_job_detail',
                numberColumn:'sort_key',
                wheres:[
                    {column:'parent_id',opt:'=',value:params.parentId},
                ]
            };
            
            this.axios.post('/site/siteCommon/getMaxNumber',data,{headers:{type:'json'}})
            .then((response)=>{
            
            if(response.success){
                
                this.$set(params, "sortKey", parseInt(response.data+1));

            }else{
                this.$Notice.warning({title: '提示',desc: response.message});
            }
            
            })
            .catch((error)=>{
                this.$Notice.warning({title: '异常',desc: error});
            });

                
        },

    },
    mounted(){
        
    }
}
</script>

<style scoped>
    #treesManage{
        height: 100%;
    }

    .trees-split-pane{
        height:calc(100vh - 180px);
        padding: 10px;
        overflow: auto;
    }

</style>