webpackJsonp([3],{C6FL:function(e,t){},Mm1Y:function(e,t,s){"use strict";var i={name:"smallSpin",data:function(){return{}},components:{},computed:{},props:{msg:{type:[String]}},created:function(){},mounted:function(){},methods:{},beforedestroy:function(){},watch:{}},a={render:function(){var e=this.$createElement,t=this._self._c||e;return t("div",{staticClass:"spin-icon-load"},[t("Spin",{attrs:{fix:""}},[t("Icon",{staticClass:"demo-spin-icon-load",attrs:{type:"ios-loading",size:"18"}}),this._v(" "),this.msg?t("div",[this._v(this._s(this.msg))]):this._e()],1)],1)},staticRenderFns:[]};var n=s("VU/8")(i,a,!1,function(e){s("lPGJ")},"data-v-3c08583e",null);t.a=n.exports},WPs3:function(e,t,s){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var i=s("Dd8w"),a=s.n(i),n=s("mvHQ"),o=s.n(n),r=s("845D"),l=s("Mm1Y"),d=s("R4Sj"),c={name:"editBox",data:function(){return{needUser:!1,needSpin:!1,page:1,curStreet:"",allStreets:EDITSELECTORCFG.street,curCommunity:"",allCommunity:[],showList:!1,listArr:[],listCount:0,curEditIndex:-1,showEditModal:!1,showELoad:!1,editVisible:!1,editObj:[{label:"名称",val:"name_",disabled:!0,necessary:!1,type:"text"},{label:"代码",val:"code_",disabled:!0,necessary:!1,type:"text"},{label:"街道",val:"street_",disabled:!0,necessary:!1,type:"text"},{label:"社区",val:"community_",disabled:!0,necessary:!1,type:"text"},{label:"历史地址",val:"originAddress",disabled:!0,necessary:!1,type:"textarea"}],editValue:{fid:null,code_:"",name_:"",street_:"",community_:"",originAddress:"",lng:"",lat:""},matchResult:[],curSelAddress:"",curSelAddObj:{},showAddLoad:!1,curTarget:"",targetList:[],showAddModal:!1,addEditObj:{street:{label:"街道",necessary:!1,canInput:!1,changeHandler:function(){}},community:{label:"社区",necessary:!1,canInput:!1,changeHandler:function(){}},road:{label:"道路",necessary:!1,canInput:!0,changeHandler:this.getRoadLike},road_num:{label:"道路编码",necessary:!1,canInput:!0,changeHandler:this.getRoadCodeLike},village:{label:"小区",necessary:!1,canInput:!0,changeHandler:this.getVillageLike},code:{label:"楼栋编码",necessary:!1,canInput:!0,changeHandler:this.getCodeLike}},radioArr:{street:EDITSELECTORCFG.street,community:null,road:null,road_num:null,village:null,code:null},addEditValue:{code:null,street:null,community:null,road:null,road_num:null,village:null,suffix:""},showOption:-1,showAddBtn:!0,suffixArr:[]}},components:{SmallSpin:l.a},computed:a()({},Object(d.c)(["username","needRefreshTarget"])),props:{},created:function(){},mounted:function(){},methods:a()({},Object(d.b)(["updateTablename","updateMarkLngLat"]),{pageChanged:function(e,t){t=t||{needSelFirst:!0};this.page=e,this.getAddByTarget(e,t)},onSelTarget:function(){var e=this.curTarget,t=this;e&&(t.updateTablename(e),t.curEditIndex=-1,t.showList=!1,t.needSpin=!0,t.$api.setTarget(e,t.username).then(function(e){t.$Message.success("数据源切换成功！"),t.$api.getCount_edit(t.username).then(function(e){t.listCount=e.total,t.page=1,t.getAddByTarget(t.page)}).catch(function(){t.needSpin=!1,t.listArr=[],t.showList=!1})}).catch(function(e){e.response.data&&t.$Message.warning(e.response.data+",切换失败!"),t.listArr=[],t.showList=!1,t.needSpin=!1}))},getAddByTarget:function(e,t){t=t||{needSelFirst:!0};var s=this;s.$api.getAddressByTarget(e,s.username).then(function(e){s.needSpin=!1,s.listArr=e.rows,s.showList=!0,t.needSelFirst?s.toggleEditModal(e.rows[0].fid,0):t.needSelLast&&s.toggleEditModal(e.rows[9].fid,9)}).catch(function(e){s.needSpin=!1,s.listArr=[],s.showList=!0})},onSelStreet:function(){var e=this.curStreet,t=this;if(e){var s=URLCFG.getCommunityByStreetUrl;t.$api.getMsg(s,{tablename:e}).then(function(e){t.allCommunity=e.rows,t.curCommunity=e.rows[0].community,t.getAddressByCommunity(!0,{needSelFirst:!0})})}},onSelCommunity:function(){this.curCommunity&&this.getAddressByCommunity(!0,{needSelFirst:!0})},getAddressByCommunity:function(e,t){var s=this;if(t=t||{},s.curEditIndex=-1,s.showList=!1,s.needSpin=!0,e=void 0==e||e){var i=URLCFG.getCountByCommunityUrl;s.$api.getMsg(i,{tablename:s.curCommunity}).then(function(e){s.listCount=e.total}),s.page=1}s.$api.getAddressByCommunity(s.page,s.curCommunity).then(function(e){s.needSpin=!1,s.listArr=e.rows,s.showList=!0,t.needSelFirst?s.toggleEditModal(e.rows[0].fid,0):t.needSelLast&&s.toggleEditModal(e.rows[9].fid,9)})},toggleEditModal:function(e,t){var s=this;s.showELoad=!0,s.curSelAddress="",s.$api.getInfoByFid_edit(e,s.username).then(function(i){if(i.rows.length>0){var a=i.rows[0];a.longitude_&&a.latitude_?s.updateMarkLngLat([a.longitude_,a.latitude_]):(s.updateMarkLngLat(void 0),s.$Message.warning("没有查询到坐标！")),s.editValue={fid:e,code_:a.code_?a.code_:"",name_:a.name_?a.name_:"",street_:a.street_?a.street_:"",community_:a.community_?a.community_:"",originAddress:a.origin_address,lng:a.longitude_?a.longitude_:"",lat:a.latitude_?a.latitude_:""},s.showEditModal=!0,s.curEditIndex=t;var n=a.name_;n&&s.$api.getMatchList(n).then(function(e){e.rows&&e.rows.length>0?(s.matchResult=e.rows,s.curSelAddress=e.rows[0].address+":"+e.rows[0].similarity):(s.matchResult=[],s.curSelAddress=""),s.showELoad=!1}).catch(function(e){s.showELoad=!1,s.matchResult=[],s.curSelAddress=""})}else s.showELoad=!1,s.$Message.warning("获取地址信息失败，请稍后重试！")}).catch(function(e){s.showELoad=!1,s.$Message.warning("获取地址信息失败，请稍后重试！")})},backRecord:function(){if(this.curEditIndex>0){var e=this.curEditIndex-1,t=this.listArr[e].fid;this.toggleEditModal(t,e)}else this.page>1&&(this.pageChanged(this.page-1,{needSelLast:!0}),this.curEditIndex=9)},nextRecord:function(){var e=Math.ceil(this.listCount/10);if(this.page<e)if(this.curEditIndex<9){var t=this.curEditIndex+1,s=this.listArr[t].fid;this.toggleEditModal(s,t)}else this.pageChanged(this.page+1,{needSelFirst:!0}),this.curEditIndex=0;else if(this.listCount%10==0&&this.curEditIndex<9||this.curEditIndex<this.listCount%10-1){t=this.curEditIndex+1,s=this.listArr[t].fid;this.toggleEditModal(s,t)}},toggleAddModal:function(){if(this.username){var e=function(){s.$api.getStandardByLL(s.editValue.lng,s.editValue.lat).then(function(e){if(e.rows.length>0){var i=e.rows[0];s.addEditValue={code:i.code,street:i.street,community:i.community,road:i.road,road_num:i.road_num,village:i.village,suffix:""},s.getAllComByStr(i.street,!1),s.showAddLoad=!1}else t();s.showAddLoad=!1}).catch(function(e){t()})},t=function(){s.addEditValue={code:"",street:s.editValue.street_,community:s.editValue.community_,road:"",road_num:"",village:"",suffix:""},s.editValue.street_&&s.getAllComByStr(s.editValue.street_,!1),s.showAddLoad=!1};this.showAddModal=!0,this.showAddLoad=!0;var s=this;s.$api.getStandardByFid(s.editValue.fid,s.username).then(function(t){if(t.rows.length>0){var i=t.rows[0];s.addEditValue={code:i.code,street:i.street,community:i.community,road:i.road,road_num:i.road_num,village:i.village,suffix:""},s.getAllComByStr(i.street,!1),s.showAddLoad=!1}else e()}).catch(function(t){e()})}else this.$Message.warning("请先登录！")},getAllComByStr:function(e,t){var s=this,i=JSON.parse(o()(s.addEditValue));s.$api.getComByStr(e).then(function(e){for(var a=[],n=e.rows,o=0;o<n.length;o++)a.push(n[o].community);s.radioArr.community=a,t&&(i.community=a[0],s.addEditValue=i),s.getRoadByCommunity(a[0],t)}).catch(function(t){s.radioArr.community=[],s.radioArr.road=[],s.radioArr.road_num=[],s.radioArr.village=[],s.radioArr.code=[],i.community=null,i.road=null,i.road_num=null,i.village=null,i.code=null,s.addEditValue=i,s.$Message.warning("获取"+e+"社区列表失败，请稍后重试")})},getRoadByCommunity:function(e,t){var s=this,i=JSON.parse(o()(s.addEditValue));s.$api.getRoadByCommunity(e).then(function(e){for(var a=[],n=e.rows,o=0;o<n.length;o++)a.push(n[o].road);s.radioArr.road=a,t&&(i.road=a[0],s.addEditValue=i),s.getRoadCodeByRoad(a[0],t)}).catch(function(t){s.radioArr.road=[],s.radioArr.road_num=[],i.road=null,i.road_num=null,s.addEditValue=i,s.$Message.warning("获取"+e+"道路列表失败，请稍后重试")})},getRoadCodeByRoad:function(e,t){var s=this,i=JSON.parse(o()(s.addEditValue)),a=i.community;s.$api.getRoadCodeByRoad(a,e).then(function(e){for(var a=[],n=e.rows,o=0;o<n.length;o++)a.push(n[o].road_num);s.radioArr.road_num=a,t&&(i.road_num=a[0],s.addEditValue=i)}).catch(function(t){s.radioArr.road_num=[],i.road_num=null,s.addEditValue=i,s.$Message.warning("获取"+e+"道路编号列表失败，请稍后重试")})},getVillageByCom:function(e){var t=this,s=JSON.parse(o()(t.addEditValue));t.$api.getVillByCom(e).then(function(e){for(var i=[],a=e.rows,n=0;n<a.length;n++)i.push(a[n].village);t.radioArr.village=i,s.village=i[0],t.addEditValue=s}).catch(function(i){t.radioArr.village=[],t.radioArr.code=[],s.village=null,s.code=null,t.addEditValue=s,t.$Message.warning("获取"+e+"小区列表失败，请稍后重试")})},getCodeByVillage:function(e){var t=this,s=JSON.parse(o()(t.addEditValue)),i=s.community;t.$api.getCodeByVillage(i,e).then(function(e){for(var s=[],i=e.rows,a=0;a<i.length;a++)s.push(i[a].code);t.radioArr.code=s}).catch(function(a){t.radioArr.code=[],s.code=null,t.addEditValue=s,t.$Message.warning("获取"+i+","+e+"楼栋编号列表失败，请稍后重试")})},radioChange:function(e){var t=this.showOption;switch(this.showOption=-1,t){case"street":this.getAllComByStr(e);break;case"community":this.getRoadByCommunity(e);break;case"road":this.getRoadCodeByRoad(e)}},getRoadLike:function(e){if(e=Object(r.e)(e)){var t=this,s=t.addEditValue;if(s.community&&s.street){var i={community:s.community,fields:"road",keywords:e,street:s.street};t.$api.getRoadLike(i).then(function(e){if(e.rows.length>0){for(var s=[],i=0;i<e.rows.length;i++)s.push(e.rows[i].road);t.radioArr.road=s,t.showOption="road"}}).catch(function(e){})}}},getRoadCodeLike:function(e){if(e=Object(r.e)(e)){var t=this,s=t.addEditValue;if(s.street&&s.community){var i={community:s.community,fields:"road_num",keywords:e,street:s.street};t.$api.getRoadCodeLike(i).then(function(e){if(e.rows.length>0){for(var s=[],i=0;i<e.rows.length;i++)s.push(e.rows[i].road_num);t.radioArr.road_num=s,t.showOption="road_num"}}).catch(function(e){})}}},getVillageLike:function(e){if(e=Object(r.e)(e)){var t=this,s=t.addEditValue;if(!s.community||!s.street)return;var i={community:s.community,fields:"village",keywords:e,street:s.street};t.$api.getAddressLike(i).then(function(e){if(e.rows.length>0){for(var s=[],i=0;i<e.rows.length;i++)s.push(e.rows[i].village);t.radioArr.village=s,t.showOption="village"}}).catch(function(e){})}},getCodeLike:function(e){if(e=Object(r.e)(e)){var t=this,s=t.addEditValue;if(!s.community||!s.street)return;var i={community:s.community,fields:"code",keywords:e,street:s.street};t.$api.getCodeLike(i).then(function(e){if(e.rows.length>0){for(var s=[],i=0;i<e.rows.length;i++)s.push(e.rows[i].code);t.radioArr.code=s,t.showOption="code"}}).catch(function(e){})}},handlerAddSite:function(){var e=this;if(e.username){var t=e.addEditValue;t.code&&t.village&&t.community&&t.street&&t.road&&t.road_num?(t.username=e.username,t.originAddress=e.editValue.originAddress,e.showAddLoad=!0,e.$api.addSite(t).then(function(t){e.showAddLoad=!1,e.$Message.success("新增地址成功！");var s=e.matchResult,i=t.split(":");s.unshift({address:i[0],similarity:i[1]}),e.matchResult=s,e.curSelAddress=t,e.showAddModal=!1}).catch(function(t){e.showAddLoad=!1,e.$Message.warning("新增地址失败，请稍后重试！")})):e.$Message.warning("请填写完整！")}else e.$Message.warning("请先登录！")},submitEdit:function(){var e=this;if(e.username){var t=e.curSelAddObj.address;if(t){e.showELoad=!0;var s=e.editValue.fid;e.$api.setEdit(s,e.username,t).then(function(t){"ok"==t.update?(e.showELoad=!1,e.$Message.success("保存成功！"),e.showEditModal=!1,e.listCount-=1,e.getAddByTarget(e.page)):(e.showELoad=!1,e.$Message.warning("保存失败，请重试！"))}).catch(function(t){e.showELoad=!1,e.$Message.warning("保存失败，请重试！")})}else e.$Message.warning("请选择标准地址或新增一个地址！")}else e.$Message.warning("请先登录！")}}),beforedestroy:function(){},watch:{username:{handler:function(e){e&&(this.showAddModal=!1,this.showEditModal=!1)},immediate:!1},curSelAddress:{handler:function(e){if(e){var t=e.split(":");this.curSelAddObj={address:t[0],similarity:t[1]}}else this.curSelAddObj={}}},needRefreshTarget:{handler:function(e){var t=this;e&&(t.curTarget="",t.$api.getTargetList(t.username).then(function(e){e?(t.targetList=e,t.curEditIndex=-1,t.showList=!1):(t.$Message.error("获取可下载的数据文件失败"),t.targetList=[])}).catch(function(e){t.$Message.error("获取可下载的数据文件失败"),t.targetList=[]}))},immediate:!0}}},u={render:function(){var e=this,t=e.$createElement,i=e._self._c||t;return i("div",[i("div",{staticClass:"listView"},[i("div",{staticClass:"selector flex_row"},[i("div",{staticClass:"block flex_row editBlock"},[i("span",{staticClass:"label"},[e._v("数据源：")]),e._v(" "),i("Select",{staticClass:"select",attrs:{title:e.curTarget},on:{"on-change":e.onSelTarget},model:{value:e.curTarget,callback:function(t){e.curTarget=t},expression:"curTarget"}},e._l(e.targetList,function(t,s){return i("Option",{key:s,staticClass:"ellipsis",attrs:{title:t,value:t}},[e._v("\n            "+e._s(t)+"\n          ")])}),1)],1)]),e._v(" "),e.needSpin?i("small-spin",{attrs:{msg:"正在加载……"}}):e._e(),e._v(" "),i("ul",{directives:[{name:"show",rawName:"v-show",value:e.showList,expression:"showList"}],staticClass:"siteList"},[e._l(e.listArr,function(t,a){return i("li",{staticClass:"flex_row",class:{sel:e.curEditIndex==a},on:{click:function(s){return e.toggleEditModal(t.fid,a)}}},[i("span",[e._v(e._s(t.origin_address))]),e._v(" "),i("img",{staticClass:"imgItem",attrs:{src:s("wlOp")}})])}),e._v(" "),i("Page",{attrs:{current:e.page,total:e.listCount,"show-total":"",size:"small","class-name":"pageClass"},on:{"on-change":e.pageChanged}})],2)],1),e._v(" "),i("Modal",{attrs:{title:"地址编辑",mask:!1,styles:{width:"380px",position:"absolute",top:"75px",right:"20px",maxHeight:"80%"},scrollable:"",visible:e.editVisible},model:{value:e.showEditModal,callback:function(t){e.showEditModal=t},expression:"showEditModal"}},[e._l(e.editObj,function(t,s){return i("div",{staticClass:"editLine flex_row",class:{autoHeightWrap:"textarea"==t.type}},[i("div",{staticClass:"label flex_row"},[i("span",{domProps:{innerHTML:e._s(t.label)}})]),e._v(" "),"text"==t.type?i("input",{directives:[{name:"model",rawName:"v-model",value:e.editValue[t.val],expression:"editValue[val.val]"}],class:t.disabled?"text":"text canEdit",attrs:{type:"text",title:e.editValue[t.val],disabled:t.disabled},domProps:{value:e.editValue[t.val]},on:{input:function(s){s.target.composing||e.$set(e.editValue,t.val,s.target.value)}}}):e._e(),e._v(" "),"textarea"==t.type?i("div",{staticClass:"autoHeight"},[e._v("\n        "+e._s(e.editValue[t.val])+"\n      ")]):e._e()])}),e._v(" "),i("div",{staticClass:"editLine flex_row  autoHeightWrap"},[i("div",{staticClass:"label flex_row"},[i("span",[e._v("可选标准地址")])]),e._v(" "),i("div",{staticClass:"selectWrapper autoHeight flex_row"},[i("Select",{staticClass:"select",attrs:{placeholder:"无匹配数据"},model:{value:e.curSelAddress,callback:function(t){e.curSelAddress=t},expression:"curSelAddress"}},e._l(e.matchResult,function(t,s){return i("Option",{key:s,attrs:{value:t.address+":"+t.similarity,label:t.address}},[i("span",[e._v(e._s(t.address))]),e._v(" "),t.similarity?i("Progress",{staticClass:"similar_row",attrs:{"stroke-width":7,title:"相似度",percent:100*t.similarity}}):e._e()],1)}),1),e._v(" "),i("div",{staticClass:"editBtnWrap"},[i("Button",{attrs:{shape:"circle",icon:"md-create",title:"新增",size:"small"},on:{click:e.toggleAddModal}})],1)],1)]),e._v(" "),i("div",{staticClass:"editLine flex_row autoHeightWrap"},[i("div",{staticClass:"label flex_row"},[i("span",[e._v("已选标准地址")])]),e._v(" "),e.curSelAddress?i("div",{staticClass:"autoHeight flex_row"},[i("span",[e._v(e._s(e.curSelAddObj.address))]),e._v(" "),e.curSelAddObj.similarity?i("Progress",{staticClass:"similar",attrs:{vertical:"","stroke-width":7,title:"相似度",percent:100*e.curSelAddObj.similarity}}):e._e()],1):e._e(),e._v(" "),e.curSelAddress?e._e():i("div",{staticClass:"autoHeight flex_row"},[e._v("无匹配数据")])]),e._v(" "),i("div",{attrs:{slot:"footer"},slot:"footer"},[i("Button",{attrs:{size:"small",disabled:1==e.page&&0==e.curEditIndex},on:{click:e.backRecord}},[i("Icon",{attrs:{type:"ios-arrow-back"}})],1),e._v(" "),i("Button",{on:{click:function(t){e.showEditModal=!1}}},[e._v("取消")]),e._v(" "),i("Button",{attrs:{type:"primary"},on:{click:e.submitEdit}},[e._v("保存")]),e._v(" "),i("Button",{attrs:{size:"small",disabled:(e.curEditIndex==e.listCount%10-1||9==e.curEditIndex)&&e.page==Math.ceil(e.listCount/10)},on:{click:e.nextRecord}},[i("Icon",{attrs:{type:"ios-arrow-forward"}})],1)],1),e._v(" "),e.showELoad?i("Spin",{attrs:{fix:"",size:"large"}}):e._e()],2),e._v(" "),i("Modal",{attrs:{title:"新增地址  +",styles:{width:"380px",position:"absolute",top:"75px",right:"20px",maxHeight:"80%"},scrollable:""},model:{value:e.showAddModal,callback:function(t){e.showAddModal=t},expression:"showAddModal"}},[i("div",{staticClass:"editLine flex_row"},[i("div",{staticClass:"label flex_row"},[e._v("\n        历史地址\n      ")]),e._v(" "),i("div",{staticClass:"input"},[e._v("\n        "+e._s(e.editValue.originAddress)+"\n      ")])]),e._v(" "),e._l(e.addEditObj,function(t,s,a){return i("div",{staticClass:"editLine flex_row"},[i("div",{staticClass:"label flex_row"},[i("span",{domProps:{innerHTML:e._s(t.label)}})]),e._v(" "),i("Input",{class:t.canInput?"input":"cannot input",attrs:{title:e.addEditValue[s],readonly:!t.canInput,icon:e.showOption!=s?"ios-arrow-down":"ios-arrow-up"},on:{"on-click":function(t){e.showOption!=s?e.showOption=s:e.showOption=-1},"on-change":function(i){return t.changeHandler(e.addEditValue[s])}},model:{value:e.addEditValue[s],callback:function(t){e.$set(e.addEditValue,s,t)},expression:"addEditValue[addName]"}}),e._v(" "),e.showOption==s?i("div",{staticClass:"options"},[i("RadioGroup",{attrs:{vertical:""},on:{"on-change":function(t){return e.radioChange(e.addEditValue[s])}},model:{value:e.addEditValue[s],callback:function(t){e.$set(e.addEditValue,s,t)},expression:"addEditValue[addName]"}},e._l(e.radioArr[s],function(t,s){return i("Radio",{key:s,attrs:{label:t}},[e._v("\n            "+e._s(t)+"\n          ")])}),1)],1):e._e()],1)}),e._v(" "),e.showAddBtn?i("div",{staticClass:"editLine flex_row addLine",attrs:{title:"新增其他"},on:{click:function(t){e.showAddBtn=!1}}},[i("Icon",{attrs:{type:"md-add-circle",size:"24",color:"#25A4F4"}})],1):e._e(),e._v(" "),e.showAddBtn?e._e():i("div",{staticClass:"editLine flex_row"},[i("div",{staticClass:"label flex_row"},[i("span",[e._v("其他")])]),e._v(" "),i("Input",{staticClass:"input",attrs:{title:e.addEditValue.suffix,icon:"suffix"!=e.showOption?"ios-arrow-down":"ios-arrow-up"},on:{"on-click":function(t){"suffix"!=e.showOption?e.showOption="suffix":e.showOption=-1}},model:{value:e.addEditValue.suffix,callback:function(t){e.$set(e.addEditValue,"suffix",t)},expression:"addEditValue.suffix"}}),e._v(" "),i("Icon",{attrs:{type:"ios-remove-circle-outline",size:"24",color:"#25A4F4",title:"删除其他"},on:{click:function(t){e.showAddBtn=!0}}}),e._v(" "),"suffix"==e.showOption?i("div",{staticClass:"options otherOpt"},[i("RadioGroup",{attrs:{vertical:""},on:{"on-change":e.radioChange},model:{value:e.addEditValue.suffix,callback:function(t){e.$set(e.addEditValue,"suffix",t)},expression:"addEditValue['suffix']"}},e._l(e.suffixArr,function(t,s){return i("Radio",{key:s,attrs:{label:t}},[e._v("\n            "+e._s(t)+"\n          ")])}),1)],1):e._e()],1),e._v(" "),i("div",{attrs:{slot:"footer"},slot:"footer"},[i("Button",{on:{click:function(t){e.showAddModal=!1}}},[e._v("取消")]),e._v(" "),i("Button",{attrs:{type:"primary"},on:{click:e.handlerAddSite}},[e._v("确定")])],1),e._v(" "),e.showAddLoad?i("Spin",{attrs:{fix:"",size:"large"}}):e._e()],2)],1)},staticRenderFns:[]};var g=s("VU/8")(c,u,!1,function(e){s("C6FL")},"data-v-db10c3a8",null).exports,h=s("KgXo"),f={name:"userCenter",data:function(){return{hasLogin:!1,needUser:!1,disabledRegister:!1,showRegister:!1,registerBox:[{label:"用户名称",val:"username",lType:"text"},{label:"手机号码",val:"phone",lType:"text"},{label:"邮箱地址",val:"email",lType:"text"},{label:"真实姓名",val:"truename",lType:"text"},{label:"密码",val:"password_",lType:"password"},{label:"确认密码",val:"_password",lType:"password"}],blankRe:[null,null,null,null,null,null],registerVal:[null,null,null,null,null,null],r_right:[0,0,0,0,0,0],showReTips:!1,showReLoad:!1,showLogin:!1,disabledLogin:!1,username:null,password:null,l_success:0,showLoLoad:!1}},components:{},computed:{},props:{},created:function(){},mounted:function(){},methods:a()({},Object(d.b)(["updateUsername","updateNeedRefreshTarget"]),{toggleUser:function(){this.needUser=!this.needUser},toggleRegister:function(){this.showRegister=!0,this.r_right=[0,0,0,0,0,0]},reInputFocus:function(e){var t=this.r_right;t[e]=0,this.r_right=t},registerUser:function(){var e=this.registerVal,t=this.registerBox,s={},i=this;i.disabledRegister=!0,i.showReLoad=!0;for(var a=0;a<e.length;a++){var n=t[a].val,o=e[a],r=i.r_right;switch(n){case"username":o?(r[a]=1,i.r_right=r):(r[a]=2,i.r_right=r);break;case"phone":/^1[3456789]\d{9}$/.test(o)?(r[a]=1,i.r_right=r):(r[a]=2,i.r_right=r);break;case"email":/^([A-Za-z0-9_\-\.])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,4})$/.test(o)?(r[a]=1,i.r_right=r):(r[a]=2,i.r_right=r);break;case"truename":/^[\u4e00-\u9fa5]+$/.test(o)?(r[a]=1,i.r_right=r):(r[a]=2,i.r_right=r);break;case"password_":o?(r[a]=1,i.r_right=r):(r[a]=2,i.r_right=r);break;case"_password":var l=s.password_;l&&l==o?(r[a]=1,i.r_right=r):(r[a]=2,i.r_right=r)}s[n]=o}"111111"==i.r_right.join("")?(i.showReTips=!1,delete s._password,i.$api.registerUser(s).then(function(e){e&&(i.$Message.success(s.username+"，注册成功，请登录"),i.showRegister=!1)}).catch(function(e){i.$Message.warning("注册失败请重试"),i.showReLoad=!1,i.disabledRegister=!1})):(i.showReTips=!0,i.showReLoad=!1,i.disabledRegister=!1)},reVC:function(e){e?(this.disabledRegister=!1,this.showReTips=!1,this.showReLoad=!1):this.registerVal=this.blankRe},toggleLogin:function(){this.hasLogin||(this.showLogin=!0,this.disabledLogin=!1,this.showLoLoad=!1,this.username=null,this.password=null,this.l_success=0)},loginIn:function(){var e=this,t=e.username,s=e.password;if(t&&s){e.showLoLoad=!0,e.disabledLogin=!0;var i={username:t,password_:s};e.$api.loginIn(i).then(function(s){e.showLoLoad=!1,"登录成功"==s?(e.$Message.success("登录成功！"),e.l_success=1,e.hasLogin=!0,e.showLogin=!1,e.updateUsername(t),e.updateNeedRefreshTarget((new Date).getTime().toString())):(e.disabledLogin=!1,e.l_success=2,e.$Message.warning("用户名或密码错误"))}).catch(function(t){e.showLoLoad=!1,e.disabledLogin=!1,"登录失败"==t.response.data?(e.l_success=2,e.$Message.warning("用户名或密码错误")):(e.l_success=0,e.$Message.warning("登录失败，请重试"))})}else e.$Message.warning("请输入完整！")},exitLogin:function(){this.hasLogin=!1,this.toggleLogin(),this.updateUsername("user_admin"),this.updateNeedRefreshTarget((new Date).getTime().toString())}}),beforedestroy:function(){},watch:{}},p={render:function(){var e=this,t=e.$createElement,s=e._self._c||t;return s("div",[s("div",{staticClass:"user-center flex_row",on:{click:function(t){e.needUser=!e.needUser,e.showLogin=!1}}},[e.hasLogin?e._e():s("Icon",{attrs:{type:"md-person",color:"#979797",size:"38"}}),e._v(" "),e.hasLogin?s("Icon",{attrs:{type:"md-person",color:"#0083FF",size:"38"}}):e._e()],1),e._v(" "),e.needUser?s("div",{staticClass:"info-box"},[s("div",{staticClass:"arrow"}),e._v(" "),e.hasLogin||e.showLogin?e._e():s("div",{staticClass:"detail-info-box flex_row"},[s("div",{staticClass:"tips",on:{click:e.toggleLogin}},[e._v("登录")]),e._v(" "),s("div",{staticClass:"tips loginBtn",on:{click:e.toggleRegister}},[e._v("注册")])]),e._v(" "),e.hasLogin?s("div",{staticClass:"detail-info-box flex_row"},[e._v("\n      您好，"),s("div",{staticClass:"tips"},[e._v(e._s(e.username))]),e._v(" "),s("div",{staticClass:"tips loginBtn",on:{click:e.exitLogin}},[e._v("退出登录")])]):e._e(),e._v(" "),e.showLogin?s("div",{staticClass:"detail-info-box flex_row"},[s("div",{staticClass:"loginbox flex_col"},[s("div",{staticClass:"line flex_row"},[s("Input",{staticClass:"input",attrs:{placeholder:"用户名",disabled:e.disabledLogin,prefix:"ios-contact",clearable:""},model:{value:e.username,callback:function(t){e.username=t},expression:"username"}}),e._v(" "),1==e.l_success?s("Icon",{attrs:{type:"ios-checkmark-circle-outline",color:"#fff",size:"16"}}):e._e(),e._v(" "),2==e.l_success?s("Icon",{attrs:{type:"ios-close-circle-outline",color:"#fff",size:"16"}}):e._e()],1),e._v(" "),s("div",{staticClass:"line flex_row"},[s("Input",{staticClass:"input",attrs:{placeholder:"密码",type:"password",disabled:e.disabledLogin,prefix:"ios-lock",clearable:""},model:{value:e.password,callback:function(t){e.password=t},expression:"password"}}),e._v(" "),1==e.l_success?s("Icon",{attrs:{type:"ios-checkmark-circle-outline",color:"#fff",size:"16"}}):e._e(),e._v(" "),2==e.l_success?s("Icon",{attrs:{type:"ios-close-circle-outline",color:"#fff",size:"16"}}):e._e()],1),e._v(" "),s("div",{staticClass:"btnBox flex_row"},[s("Button",{staticClass:"btn1 flex_row",attrs:{long:"",shape:"circle"},on:{click:e.loginIn}},[e._v("登录")]),e._v(" "),s("div",{staticClass:"btn2",on:{click:e.toggleRegister}},[e._v("注册")])],1)]),e._v(" "),e.showLoLoad?s("Spin",{attrs:{fix:"",size:"large"}}):e._e()],1):e._e()]):e._e(),e._v(" "),s("Drawer",{staticClass:"registerBox",attrs:{title:"用户注册"},on:{"on-visible-change":e.reVC},model:{value:e.showRegister,callback:function(t){e.showRegister=t},expression:"showRegister"}},[s("div",e._l(e.registerBox,function(t,i){return s("div",{staticClass:"flex_row line"},[s("div",{staticClass:"label"},[e._v(e._s(t.label)+"：")]),e._v(" "),s("Input",{staticClass:"input",attrs:{placeholder:"请输入"+t.label+"…",type:t.lType,disabled:e.disabledRegister,clearable:""},on:{"on-focus":function(t){return e.reInputFocus(i)}},model:{value:e.registerVal[i],callback:function(t){e.$set(e.registerVal,i,t)},expression:"registerVal[bIndex]"}}),e._v(" "),1==e.r_right[i]?s("Icon",{attrs:{type:"ios-checkmark-circle-outline",color:"green",size:"16"}}):e._e(),e._v(" "),2==e.r_right[i]?s("Icon",{attrs:{type:"ios-close-circle-outline",color:"red",size:"16"}}):e._e()],1)}),0),e._v(" "),s("div",{staticClass:"btnBox flex_row"},[s("Button",{staticClass:"btn",on:{click:function(t){e.showRegister=!1}}},[e._v("取消")]),e._v(" "),s("Button",{staticClass:"btn",attrs:{type:"primary"},on:{click:e.registerUser}},[e._v("注册")])],1),e._v(" "),e.showReLoad?s("Spin",{attrs:{fix:"",size:"large"}}):e._e(),e._v(" "),e.showReTips?s("div",{staticClass:"tip"},[e._v("\n      请确认输入值合法后，再重试\n    ")]):e._e()],1)],1)},staticRenderFns:[]};var m=s("VU/8")(f,p,!1,function(e){s("jL9I")},"data-v-19288520",null).exports,v={name:"home",components:{editBox:g,loading:h.a,userCenter:m},created:function(){},data:function(){return{showLoading:!1,openTarget:!1,targetData:null,targetListIndex:0}},computed:a()({},Object(d.c)(["username","tablename"]),{upLoadUrl:function(){return EDITSERVICE.uploadUrl+"?username="+this.username}}),methods:a()({},Object(d.b)(["updateNeedRefreshTarget"]),{importFile:function(){if(!this.username)return this.$Message.warning("请先登录"),!1},beforeUpload:function(e){},onUpSuccess:function(e,t,s){this.$Message.success("数据上传成功!"),this.showLoading=!1,this.updateNeedRefreshTarget((new Date).getTime().toString())},onUpError:function(e,t,s){this.showLoading=!1,this.$Message.error("导入失败，请重试！")},downloadFile:function(){if(this.username)if(this.tablename){var e=this;e.$Message.loading({content:"正在创建下载链接，请稍后……",duration:0}),e.$api.exportFile(e.tablename,e.username).then(function(t){t?(e.$Message.destroy(),download(t,e.tablename)):e.$Message.warning("下载链接创建失败，请重试！")}).catch(function(t){t.response.data&&e.$Message.warning(t.response.data)})}else this.$Message.warning("请先选择数据源！");else this.$Message.warning("请先登录！")}})},w={render:function(){var e=this,t=e.$createElement,s=e._self._c||t;return s("div",{staticClass:"editWrapper"},[s("edit-box",{staticClass:"leftView"}),e._v(" "),s("div",{staticClass:"rightBox flex_row"},[e.username?s("Upload",{ref:"upload",staticClass:"flex_row",attrs:{action:e.upLoadUrl,name:"fileName",accept:"file","before-upload":e.beforeUpload,"on-success":e.onUpSuccess,"on-error":e.onUpError,"show-upload-list":!1,multiple:""}},[s("Button",{staticClass:"item",attrs:{icon:"md-arrow-round-up",title:"多个文件推荐使用zip"}},[e._v("导入")])],1):e._e(),e._v(" "),e.username?e._e():s("Button",{staticClass:"item",attrs:{icon:"md-arrow-round-up",title:"多个文件推荐使用zip"},on:{click:e.importFile}},[e._v("\n      导入\n    ")]),e._v(" "),s("div",{staticClass:"item flex_col"},[s("Button",{attrs:{icon:"md-download"},on:{click:e.downloadFile}},[e._v("导出")])],1)],1),e._v(" "),e.showLoading?s("Spin",{staticStyle:{background:"rgba(255,255,255,.5)"},attrs:{fix:""}},[s("loading",{attrs:{msg:"上传中……"}})],1):e._e(),e._v(" "),s("user-center",{staticClass:"userView"})],1)},staticRenderFns:[]};var _=s("VU/8")(v,w,!1,function(e){s("WlMt")},"data-v-1bed810d",null);t.default=_.exports},WlMt:function(e,t){},jL9I:function(e,t){},lPGJ:function(e,t){},wlOp:function(e,t){e.exports="data:image/png;base64,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"}});
//# sourceMappingURL=3.9678e5f32de297e0e814.js.map