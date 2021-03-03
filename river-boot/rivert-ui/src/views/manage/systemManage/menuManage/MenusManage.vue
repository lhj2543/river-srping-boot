<template>

    <div class="content-panel" style="height:100%;">

        <div class="content-card" style="height:100%;">
        <div class="header" >
            <span class="title" >
            <Icon :type="$common.icon.detail" />
                菜单管理
            </span>
            
            <span class="right-tool" style="float:right">
                <Icon title="刷新"  size="18" @click="refresh" class="cursor-pointer"  :type="$common.icon.refresh" />
            </span>
        </div>
        <div class="card-body" style="height:calc(100vh - 60px);max-height:999999999px">

            <Split v-model="split1" min="250px" max="900px" >
                <div slot="left" class="menus-split-pane" >
                    
                    <Tree :data="menus" :render="renderContent" class="menus-tree" @on-select-change="menuOnSelectChange" ref="menusTree"  ></Tree>

                </div>
                <div slot="right"   class="menus-split-pane" :style="{padding:'40px'}">
                    
                    <Form ref="menuForm" :model="menuForm" :rules="ruleValidate" :label-width="100" :disabled="isDisabled">
                        <input type="hidden" name="id" v-model="menuForm.id"/>
                        <input type="hidden" name="parentId" v-model="menuForm.parentId" />

                        <Row>
                            <Col span="12">
                                <FormItem label="菜单名称" prop="displayName">
                                    <Input v-model="menuForm.displayName" placeholder="请输入菜单名称..."></Input>
                                </FormItem>
                            </Col>
                            <Col span="12">
                                <FormItem label="菜单权限" prop="permission">
                                    <Input v-model="menuForm.permission" placeholder="请输菜单权限..."></Input>
                                </FormItem>
                            </Col>
                        </Row>
                        <Row>
                            <Col span="12">
                                <FormItem label="菜单URL" prop="url">
                                    <Input v-model="menuForm.url" placeholder="请输入菜单URL..."></Input>
                                </FormItem>
                            </Col>
                            <Col span="12">
                                <FormItem label="页面路径" prop="pagePath">
                                    <Input v-model="menuForm.pagePath" placeholder="请输入页面路径..."></Input>
                                </FormItem>
                            </Col>
                        </Row>

                        <Row>
                            <Col span="12">
                                <FormItem label="URL名称" prop="menuName">
                                    <Input v-model="menuForm.menuName" placeholder="请输入URL名称..."></Input>
                                </FormItem>
                            </Col>
                             <Col span="12">
                                <FormItem label="分类" prop="category">
                                    <Input v-model="menuForm.category" placeholder="请输入分类..."></Input>
                                </FormItem>
                            </Col>
                        </Row>

                        <Row>
                            <Col span="12">
                                <FormItem label="菜单类型" prop="type">
                                    <Select  v-model="menuForm.type" placeholder="请选择菜单类型..." >
                                        <Option  v-for="(value,key,index) in items['menuType']" :key="key+'-'+value" :value="key" >{{value}}</Option>
                                    </Select>
                                </FormItem>
                            </Col>
                            <Col span="12">
                                <FormItem label="菜单图标" prop="icon">
                                    <Input v-model="menuForm.icon" placeholder="请输入菜单图标..."></Input>
                                </FormItem>
                            </Col>
                        </Row>

                        
                        <Row>
                             <Col span="12">
                                <FormItem label="状态" prop="status">
                                    <Select  v-model="menuForm.status" placeholder="请选择菜单状态..." >
                                        <Option  v-for="(value,key,index) in items['isActive']" :key="key+'-'+value" :value="key" >{{value}}</Option>
                                    </Select>
                                </FormItem>
                            </Col>
                             <Col span="12">
                                <FormItem label="排序" prop="sortKey">
                                    <InputNumber v-model="menuForm.sortKey" placeholder="请输入排序号..." :min="0" style="width:100%;"></InputNumber>
                                </FormItem>
                            </Col>
                        </Row>
                        
                        <FormItem label="菜单描述" prop="notes">
                            <Input v-model="menuForm.notes"   type="textarea"  :autosize="{minRows: 2,maxRows: 10}" placeholder="请输入菜单描述..."></Input>
                        </FormItem>

                        <FormItem v-if="isAdd" :style="{padding:'20px 0','text-align':'center'}">
                            <Button type="primary" @click="handleSubmit('menuForm')"  >保存</Button>
                            <Button type="info" @click="handleCopy('menuForm')" style="margin-left: 8px"  >复制</Button>
                            <Button type="warning" @click="handleReset('menuForm')" style="margin-left: 8px"  >重置</Button>
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
                displayName: '',
                url: '',
                pagePath:'',
                menuName: '',
                status: '',
                icon: '',
                sortKey: 0,
                notes: '',
                category:'',
                type:'',
            };


export default {
    name:'MenusManage',
    data(){
        return{
            split1:0.2,
            loading:false,
            isAdd:false,
            items:{},//字典数据
            menus:[],
            menusRoot:[],
            buttonProps: {
                type: 'text',
                /* size: 'small', */
            },
            menuForm:Object.assign({},menuObj,{}),
            isDisabled:false,//表单按钮是否不可点击
            ruleValidate: {
                displayName: [
                    { required: true, message: '菜单名称必填!', trigger: 'blur' }
                ],
                notes: [
                    { type: 'string', max: 200, message: '最大不能超过200个字！', trigger: 'blur' }
                ]
            }

        }
    },
    created() {

        /* 加载页面上所有用到的字典 */
      let params = {categoryNames:['menuType','isActive']};
      this.getSysItems(params,(data)=>{
        this.items = data.itemMap?data.itemMap:{};
      });

        this.loadMenus();//加载后台菜单
    },
    methods: {
        refresh(){//刷新
            this.loadMenus();
            this.menuForm=Object.assign({},menuObj,{});
        },
        openAll(){//展开所有菜单
            
        },
        closeAll(){//收缩所有菜单

        },
        loadMenus(){//加载后台菜单
            let _this = this;
            this.loading = true;
            this.axios.get('/system/sysMenu/query',{params:{expand:true}})
            .then((response)=>{
                _this.loading=false;
               
                if(response.success){
                    this.menus = response.data;
                }else{
                    this.$Notice.info({title:'提示',desc: response.message});
                }

            })
            .catch((error)=>{
                _this.loading=false;
                this.$Notice.error({title:'异常',desc: error});
            });

        },
        renderContent (h, { root, node, data }) {//自定义树样式
            /* console.log(root);
            console.log(node);
            console.log(data); */
            this.menusRoot = root;
            
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
                        h('span', data.displayName)
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
        append (root, node, data) {//添加菜单
            //判断父节点是否为空
            if(!data.sid || data.sid==''){
                this.$Message.info('请先保存该节点数据！');
                return;
            }
            /* console.log("============添加菜单=========");
            console.log(data);
            console.log(node);
            console.log(root); */
            var selectedNodes=this.$refs.menusTree.getSelectedNodes();
            //console.log(selectedNodes);
            selectedNodes.forEach(node => {
                node.selected=false;
            });

            const children = data.children || [];
            var newMenu=Object.assign({},menuObj,{
                sid:'',
                displayName:'新建菜单',
                parentId:data.sid,
                status:'1',
                selected:true,
                expand:true
            });
            //this.menuForm=Object.assign({},newMenu,{});
            this.menuForm=newMenu;
            children.push(newMenu);
            this.isAdd=true;
            this.$set(data, 'children', children);

            this.getMaxSortKey(data.sid,newMenu);
        },
        getMaxSortKey(sid,menu){

            if(sid){

                this.axios.get('/system/sysMenu/getMaxSortKey',{
                    params:{sid:sid},
                })
                .then((response)=>{
                    
                    if(response.success){
                        
                        this.$set(menu, "sortKey", parseInt(response.data+1));

                    }else{
                        this.$Notice.warning({title: '提示',desc: response.message});
                    }
                
                })
                .catch((error)=>{
                this.$Notice.warning({title: '异常',desc: error});
                });

            }

        },
        remove (root, node, data) {//移除菜单
            /* console.log(root);
            console.log(node);
            console.log(data); */
            
            let sid = data.sid;
            let _this = this;
            //alert(sid);

            const parentKey = root.find(el => el === node).parent;
            const parent = root.find(el => el.nodeKey === parentKey).node;
            const index = parent.children.indexOf(data);

            if(sid){

                this.$Modal.confirm({
                    title: '您确定要删除菜单？',
                    content: '<p>删除该菜单同时会删除该菜单下所有子菜单！</p>',
                    onOk: () => {
                        
                        //后台删除数据
                        this.axios.post('/system/sysMenu/deletes',data,{headers:{type:'json'}})
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
                        //this.$Message.info('取消删除菜单');
                    }
                });

            }else{
                refreshFormData(index);
            }

            function refreshFormData(index){
                parent.children.splice(index, 1);
                _this.isAdd=false;
                _this.menuForm = menuObj;
            }

        },
        menuOnSelectChange(selectNodes,node){//菜单选中事件
            /*console.log("============菜单选中=========");
             console.log(selectNodes);
            console.log(node); */

            //表单赋值
            //var k=Object.assign({},node,{});
            this.menuForm=node;
            this.isAdd=true;
            this.isDisabled=this.menuForm.parentId=='-1'?true:false;//根节点不能修改
            
        },
        handleSubmit (name) {//表单提交
            let _this = this;
            this.$refs[name].validate((valid) => {

                if (valid) {
                    
                    _this.loading=true;
                    _this.axios.post('/system/sysMenu/save',_this.menuForm,{headers:{type:'json'}})
                    .then((response)=>{
                        let row = response.data;
                        _this.loading=false;
                        this.$Notice.info({title:'提示',desc: response.message});
                        if(response.success){
                            _this.menuForm.sid = row.sid;
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
            this.$refs[name].resetFields();
        },
        handleCopy(name){//复制

            let node = this.menuForm;

            const this_node = this.menusRoot.find((el)=>{
                return el.node === node;
            });

            if(this_node){

                const parentKey = this_node.parent;
                const parent = this.menusRoot.find(el => el.nodeKey === parentKey).node;

                let newNode = Object.assign({},node,{
                    sid:'',checked:false,selected:true,
                    displayName:'复制'+node.displayName,children:[]
                });
                parent.children.push(newNode);

                this_node.node.selected = false;
                this.menuForm = newNode;

                this.getMaxSortKey(node.parentId,newNode);

            }
           
        }
    },
    mounted(){
        
    }
}
</script>

<style scoped>
    #menusManage{
        height: 100%;
    }

    .menus-split-pane{
        height:calc(100vh - 180px);
        padding: 10px;
        overflow: auto;
    }

</style>