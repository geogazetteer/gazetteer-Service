webpackJsonp([3],{AY5B:function(e,t){},Mm1Y:function(e,t,s){"use strict";var a={name:"smallSpin",data:function(){return{}},components:{},computed:{},props:{msg:{type:[String]}},created:function(){},mounted:function(){},methods:{},beforedestroy:function(){},watch:{}},i={render:function(){var e=this.$createElement,t=this._self._c||e;return t("div",{staticClass:"spin-icon-load"},[t("Spin",{attrs:{fix:""}},[t("Icon",{staticClass:"demo-spin-icon-load",attrs:{type:"ios-loading",size:"18"}}),this._v(" "),this.msg?t("div",[this._v(this._s(this.msg))]):this._e()],1)],1)},staticRenderFns:[]};var n=s("VU/8")(a,i,!1,function(e){s("lPGJ")},"data-v-3c08583e",null);t.a=n.exports},WPs3:function(e,t,s){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var a=s("Dd8w"),i=s.n(a),n=s("mvHQ"),o=s.n(n),r=s("845D"),l=s("Mm1Y"),d=s("R4Sj"),c={name:"editBox",data:function(){return{needUser:!1,needSpin:!1,page:1,curStreet:"",allStreets:EDITSELECTORCFG.street,curCommunity:"",allCommunity:[],showList:!1,listArr:[],listCount:0,curEditIndex:-1,showEditModal:!1,showELoad:!1,editVisible:!1,editObj:[{label:"名称",val:"name_",disabled:!0,necessary:!1,type:"text"},{label:"代码",val:"code_",disabled:!0,necessary:!1,type:"text"},{label:"街道",val:"street_",disabled:!0,necessary:!1,type:"text"},{label:"社区",val:"community_",disabled:!0,necessary:!1,type:"text"},{label:"历史地址",val:"originAddress",disabled:!0,necessary:!1,type:"textarea"}],editValue:{fid:null,code_:"",name_:"",street_:"",community_:"",originAddress:"",lng:"",lat:""},matchResult:[],curSelAddress:"",curSelAddObj:{},showAddLoad:!1,curTarget:"",targetList:[],showAddModal:!1,addEditObj:{street:{label:"街道",necessary:!1,canInput:!1,changeHandler:function(){}},community:{label:"社区",necessary:!1,canInput:!1,changeHandler:function(){}},road:{label:"道路",necessary:!1,canInput:!0,changeHandler:this.getRoadLike},road_num:{label:"道路编码",necessary:!1,canInput:!0,changeHandler:this.getRoadCodeLike},village:{label:"小区",necessary:!1,canInput:!0,changeHandler:this.getVillageLike},code:{label:"楼栋编码",necessary:!1,canInput:!0,changeHandler:this.getCodeLike}},radioArr:{street:EDITSELECTORCFG.street,community:null,road:null,road_num:null,village:null,code:null},addEditValue:{code:null,street:null,community:null,road:null,road_num:null,village:null,suffix:""},showOption:-1,showAddBtn:!0,suffixArr:[]}},components:{SmallSpin:l.a},computed:i()({},Object(d.c)(["username","needRefreshTarget"])),props:{},created:function(){},mounted:function(){},methods:i()({},Object(d.b)(["updateTablename","updateMarkLngLat"]),{pageChanged:function(e,t){t=t||{needSelFirst:!0};this.page=e,this.getAddByTarget(e,t)},onSelTarget:function(){var e=this.curTarget,t=this;e&&(t.updateTablename(e),t.updateMarkLngLat(),t.curEditIndex=-1,t.showList=!1,t.needSpin=!0,t.$api.setTarget(e,t.username).then(function(e){t.$Message.success("数据源切换成功！"),t.$api.getCount_edit(t.username).then(function(e){t.listCount=e.total,t.page=1,t.getAddByTarget(t.page)}).catch(function(){t.needSpin=!1,t.listArr=[],t.showList=!1})}).catch(function(e){e.response.data&&t.$Message.warning(e.response.data+",切换失败!"),t.listArr=[],t.showList=!1,t.needSpin=!1}))},getAddByTarget:function(e,t){t=t||{};var s=this;s.$api.getAddressByTarget(e,s.username).then(function(e){if(s.needSpin=!1,s.listArr=e.rows,s.showList=!0,e.rows.length>0){var a="",i=[];for(var n in e.rows)e.rows[n].fid&&(a+=e.rows[n].fid+",",i.push(e.rows[n].fid));s.$api.getLLByIds(a,s.username?s.username:"user_admin").then(function(e){var t=[];for(var a in e)t.push(e[a]?[e[a].x,e[a].y,i[a]]:null);s.updateMarkLngLat(t)}).catch(function(e){s.$Message.warning("定位失败，请稍后重试！")})}t.needSelFirst?s.toggleEditModal(e.rows[0].fid,0):t.needSelLast&&s.toggleEditModal(e.rows[9].fid,9)}).catch(function(e){s.needSpin=!1,s.listArr=[],s.showList=!0})},onSelStreet:function(){var e=this.curStreet,t=this;if(e){var s=URLCFG.getCommunityByStreetUrl;t.$api.getMsg(s,{tablename:e}).then(function(e){t.allCommunity=e.rows,t.curCommunity=e.rows[0].community,t.getAddressByCommunity(!0,{needSelFirst:!0})})}},onSelCommunity:function(){this.curCommunity&&this.getAddressByCommunity(!0,{needSelFirst:!0})},getAddressByCommunity:function(e,t){var s=this;if(t=t||{},s.curEditIndex=-1,s.showList=!1,s.needSpin=!0,e=void 0==e||e){var a=URLCFG.getCountByCommunityUrl;s.$api.getMsg(a,{tablename:s.curCommunity}).then(function(e){s.listCount=e.total}),s.page=1}s.$api.getAddressByCommunity(s.page,s.curCommunity).then(function(e){s.needSpin=!1,s.listArr=e.rows,s.showList=!0,t.needSelFirst?s.toggleEditModal(e.rows[0].fid,0):t.needSelLast&&s.toggleEditModal(e.rows[9].fid,9)})},toggleEditModal:function(e,t){var s=this;s.showELoad=!0,s.curSelAddress="",s.$api.getInfoByFid_edit(e,s.username).then(function(a){if(a.rows.length>0){var i=a.rows[0];s.updateMarkLngLat(["high",e]),s.editValue={fid:e,code_:i.code_?i.code_:"",name_:i.name_?i.name_:"",street_:i.street_?i.street_:"",community_:i.community_?i.community_:"",originAddress:i.origin_address,lng:i.longitude_?i.longitude_:"",lat:i.latitude_?i.latitude_:""},s.showEditModal=!0,s.curEditIndex=t;var n=i.name_;n&&s.$api.getMatchList(n).then(function(e){e.rows&&e.rows.length>0?(s.matchResult=e.rows,s.curSelAddress=e.rows[0].address+":"+e.rows[0].similarity):(s.matchResult=[],s.curSelAddress=""),s.showELoad=!1}).catch(function(e){s.showELoad=!1,s.matchResult=[],s.curSelAddress=""})}else s.showELoad=!1,s.$Message.warning("获取地址信息失败，请稍后重试！")}).catch(function(e){s.showELoad=!1,s.$Message.warning("获取地址信息失败，请稍后重试！")})},backRecord:function(){if(this.curEditIndex>0){var e=this.curEditIndex-1,t=this.listArr[e].fid;this.toggleEditModal(t,e)}else this.page>1&&(this.pageChanged(this.page-1,{needSelLast:!0}),this.curEditIndex=9)},nextRecord:function(){var e=Math.ceil(this.listCount/10);if(this.page<e)if(this.curEditIndex<9){var t=this.curEditIndex+1,s=this.listArr[t].fid;this.toggleEditModal(s,t)}else this.pageChanged(this.page+1,{needSelFirst:!0}),this.curEditIndex=0;else if(this.listCount%10==0&&this.curEditIndex<9||this.curEditIndex<this.listCount%10-1){t=this.curEditIndex+1,s=this.listArr[t].fid;this.toggleEditModal(s,t)}},toggleAddModal:function(){if(this.username){var e=function(){s.$api.getStandardByLL(s.editValue.lng,s.editValue.lat).then(function(e){if(e.rows.length>0){var a=e.rows[0];s.addEditValue={code:a.code,street:a.street,community:a.community,road:a.road,road_num:a.road_num,village:a.village,suffix:""},s.getAllComByStr(a.street,!1),s.showAddLoad=!1}else t();s.showAddLoad=!1}).catch(function(e){t()})},t=function(){s.addEditValue={code:"",street:s.editValue.street_,community:s.editValue.community_,road:"",road_num:"",village:"",suffix:""},s.editValue.street_&&s.getAllComByStr(s.editValue.street_,!1),s.showAddLoad=!1};this.showAddModal=!0,this.showAddLoad=!0;var s=this;s.$api.getStandardByFid(s.editValue.fid,s.username).then(function(t){if(t.rows.length>0){var a=t.rows[0];s.addEditValue={code:a.code,street:a.street,community:a.community,road:a.road,road_num:a.road_num,village:a.village,suffix:""},s.getAllComByStr(a.street,!1),s.showAddLoad=!1}else e()}).catch(function(t){e()})}else this.$Message.warning("请先登录！")},getAllComByStr:function(e,t){var s=this,a=JSON.parse(o()(s.addEditValue));s.$api.getComByStr(e).then(function(e){for(var i=[],n=e.rows,o=0;o<n.length;o++)i.push(n[o].community);s.radioArr.community=i,t&&(a.community=i[0],s.addEditValue=a),s.getRoadByCommunity(i[0],t)}).catch(function(t){s.radioArr.community=[],s.radioArr.road=[],s.radioArr.road_num=[],s.radioArr.village=[],s.radioArr.code=[],a.community=null,a.road=null,a.road_num=null,a.village=null,a.code=null,s.addEditValue=a,s.$Message.warning("获取"+e+"社区列表失败，请稍后重试")})},getRoadByCommunity:function(e,t){var s=this,a=JSON.parse(o()(s.addEditValue));s.$api.getRoadByCommunity(e).then(function(e){for(var i=[],n=e.rows,o=0;o<n.length;o++)i.push(n[o].road);s.radioArr.road=i,t&&(a.road=i[0],s.addEditValue=a),s.getRoadCodeByRoad(i[0],t)}).catch(function(t){s.radioArr.road=[],s.radioArr.road_num=[],a.road=null,a.road_num=null,s.addEditValue=a,s.$Message.warning("获取"+e+"道路列表失败，请稍后重试")})},getRoadCodeByRoad:function(e,t){var s=this,a=JSON.parse(o()(s.addEditValue)),i=a.community;s.$api.getRoadCodeByRoad(i,e).then(function(e){for(var i=[],n=e.rows,o=0;o<n.length;o++)i.push(n[o].road_num);s.radioArr.road_num=i,t&&(a.road_num=i[0],s.addEditValue=a)}).catch(function(t){s.radioArr.road_num=[],a.road_num=null,s.addEditValue=a,s.$Message.warning("获取"+e+"道路编号列表失败，请稍后重试")})},getVillageByCom:function(e){var t=this,s=JSON.parse(o()(t.addEditValue));t.$api.getVillByCom(e).then(function(e){for(var a=[],i=e.rows,n=0;n<i.length;n++)a.push(i[n].village);t.radioArr.village=a,s.village=a[0],t.addEditValue=s}).catch(function(a){t.radioArr.village=[],t.radioArr.code=[],s.village=null,s.code=null,t.addEditValue=s,t.$Message.warning("获取"+e+"小区列表失败，请稍后重试")})},getCodeByVillage:function(e){var t=this,s=JSON.parse(o()(t.addEditValue)),a=s.community;t.$api.getCodeByVillage(a,e).then(function(e){for(var s=[],a=e.rows,i=0;i<a.length;i++)s.push(a[i].code);t.radioArr.code=s}).catch(function(i){t.radioArr.code=[],s.code=null,t.addEditValue=s,t.$Message.warning("获取"+a+","+e+"楼栋编号列表失败，请稍后重试")})},radioChange:function(e){var t=this.showOption;switch(this.showOption=-1,t){case"street":this.getAllComByStr(e);break;case"community":this.getRoadByCommunity(e);break;case"road":this.getRoadCodeByRoad(e)}},getRoadLike:function(e){if(e=Object(r.e)(e)){var t=this,s=t.addEditValue;if(s.community&&s.street){var a={community:s.community,fields:"road",keywords:e,street:s.street};t.$api.getRoadLike(a).then(function(e){if(e.rows.length>0){for(var s=[],a=0;a<e.rows.length;a++)s.push(e.rows[a].road);t.radioArr.road=s,t.showOption="road"}}).catch(function(e){})}}},getRoadCodeLike:function(e){if(e=Object(r.e)(e)){var t=this,s=t.addEditValue;if(s.street&&s.community){var a={community:s.community,fields:"road_num",keywords:e,street:s.street};t.$api.getRoadCodeLike(a).then(function(e){if(e.rows.length>0){for(var s=[],a=0;a<e.rows.length;a++)s.push(e.rows[a].road_num);t.radioArr.road_num=s,t.showOption="road_num"}}).catch(function(e){})}}},getVillageLike:function(e){if(e=Object(r.e)(e)){var t=this,s=t.addEditValue;if(!s.community||!s.street)return;var a={community:s.community,fields:"village",keywords:e,street:s.street};t.$api.getAddressLike(a).then(function(e){if(e.rows.length>0){for(var s=[],a=0;a<e.rows.length;a++)s.push(e.rows[a].village);t.radioArr.village=s,t.showOption="village"}}).catch(function(e){})}},getCodeLike:function(e){if(e=Object(r.e)(e)){var t=this,s=t.addEditValue;if(!s.community||!s.street)return;var a={community:s.community,fields:"code",keywords:e,street:s.street};t.$api.getCodeLike(a).then(function(e){if(e.rows.length>0){for(var s=[],a=0;a<e.rows.length;a++)s.push(e.rows[a].code);t.radioArr.code=s,t.showOption="code"}}).catch(function(e){})}},handlerAddSite:function(){var e=this;if(e.username){var t=e.addEditValue;t.code&&t.village&&t.community&&t.street&&t.road&&t.road_num?(t.username=e.username,t.originAddress=e.editValue.originAddress,e.showAddLoad=!0,e.$api.addSite(t).then(function(t){e.showAddLoad=!1,e.$Message.success("新增地址成功！");var s=e.matchResult,a=t.split(":");s.unshift({address:a[0],similarity:a[1]}),e.matchResult=s,e.curSelAddress=t,e.showAddModal=!1}).catch(function(t){e.showAddLoad=!1,e.$Message.warning("新增地址失败，请稍后重试！")})):e.$Message.warning("请填写完整！")}else e.$Message.warning("请先登录！")},submitEdit:function(){var e=this;if(e.username){var t=e.curSelAddObj.address;if(t){e.showELoad=!0;var s=e.editValue.fid;e.$api.setEdit(s,e.username,t).then(function(t){"ok"==t.update?(e.showELoad=!1,e.$Message.success("保存成功！"),e.showEditModal=!1,e.listCount-=1,e.getAddByTarget(e.page)):(e.showELoad=!1,e.$Message.warning("保存失败，请重试！"))}).catch(function(t){e.showELoad=!1,e.$Message.warning("保存失败，请重试！")})}else e.$Message.warning("请选择标准地址或新增一个地址！")}else e.$Message.warning("请先登录！")}}),beforedestroy:function(){},watch:{username:{handler:function(e){e&&(this.showAddModal=!1,this.showEditModal=!1)},immediate:!1},curSelAddress:{handler:function(e){if(e){var t=e.split(":");this.curSelAddObj={address:t[0],similarity:t[1]}}else this.curSelAddObj={}}},needRefreshTarget:{handler:function(e){var t=this;e&&(t.curTarget="",t.$api.getTargetList(t.username).then(function(e){e?(t.targetList=e,t.curEditIndex=-1,t.showList=!1):(t.$Message.error("获取可下载的数据文件失败"),t.targetList=[])}).catch(function(e){t.$Message.error("获取可下载的数据文件失败"),t.targetList=[]}))},immediate:!0}}},u={render:function(){var e=this,t=e.$createElement,s=e._self._c||t;return s("div",[s("div",{staticClass:"listView"},[s("div",{staticClass:"selector flex_row"},[s("div",{staticClass:"block flex_row editBlock"},[s("span",{staticClass:"label"},[e._v("数据源：")]),e._v(" "),s("Select",{staticClass:"select",attrs:{title:e.curTarget},on:{"on-change":e.onSelTarget},model:{value:e.curTarget,callback:function(t){e.curTarget=t},expression:"curTarget"}},e._l(e.targetList,function(t,a){return s("Option",{key:a,staticClass:"ellipsis",attrs:{title:t,value:t}},[e._v("\n            "+e._s(t)+"\n          ")])}),1)],1)]),e._v(" "),e.needSpin?s("small-spin",{attrs:{msg:"正在加载……"}}):e._e(),e._v(" "),s("ul",{directives:[{name:"show",rawName:"v-show",value:e.showList,expression:"showList"}],staticClass:"siteList"},[e._l(e.listArr,function(t,a){return s("li",{staticClass:"flex_row",class:{sel:e.curEditIndex==a},on:{click:function(s){return e.toggleEditModal(t.fid,a)}}},[s("div",{staticClass:"imgItem",style:{backgroundPosition:-21*a+"px 0"}}),e._v(" "),s("span",[e._v(e._s(t.origin_address))])])}),e._v(" "),s("Page",{attrs:{current:e.page,total:e.listCount,"show-total":"",size:"small","class-name":"pageClass"},on:{"on-change":e.pageChanged}})],2)],1),e._v(" "),s("Modal",{attrs:{title:"地址编辑",mask:!1,styles:{width:"380px",position:"absolute",top:"75px",right:"20px",maxHeight:"80%"},scrollable:"",visible:e.editVisible},model:{value:e.showEditModal,callback:function(t){e.showEditModal=t},expression:"showEditModal"}},[e._l(e.editObj,function(t,a){return s("div",{staticClass:"editLine flex_row",class:{autoHeightWrap:"textarea"==t.type}},[s("div",{staticClass:"label flex_row"},[s("span",{domProps:{innerHTML:e._s(t.label)}})]),e._v(" "),"text"==t.type?s("input",{directives:[{name:"model",rawName:"v-model",value:e.editValue[t.val],expression:"editValue[val.val]"}],class:t.disabled?"text":"text canEdit",attrs:{type:"text",title:e.editValue[t.val],disabled:t.disabled},domProps:{value:e.editValue[t.val]},on:{input:function(s){s.target.composing||e.$set(e.editValue,t.val,s.target.value)}}}):e._e(),e._v(" "),"textarea"==t.type?s("div",{staticClass:"autoHeight"},[e._v("\n        "+e._s(e.editValue[t.val])+"\n      ")]):e._e()])}),e._v(" "),s("div",{staticClass:"editLine flex_row  autoHeightWrap"},[s("div",{staticClass:"label flex_row"},[s("span",[e._v("可选标准地址")])]),e._v(" "),s("div",{staticClass:"selectWrapper autoHeight flex_row"},[s("Select",{staticClass:"select",attrs:{placeholder:"无匹配数据"},model:{value:e.curSelAddress,callback:function(t){e.curSelAddress=t},expression:"curSelAddress"}},e._l(e.matchResult,function(t,a){return s("Option",{key:a,attrs:{value:t.address+":"+t.similarity,label:t.address}},[s("span",[e._v(e._s(t.address))]),e._v(" "),t.similarity?s("Progress",{staticClass:"similar_row",attrs:{"stroke-width":7,title:"相似度",percent:100*t.similarity}}):e._e()],1)}),1),e._v(" "),s("div",{staticClass:"editBtnWrap"},[s("Button",{attrs:{shape:"circle",icon:"md-create",title:"新增",size:"small"},on:{click:e.toggleAddModal}})],1)],1)]),e._v(" "),s("div",{staticClass:"editLine flex_row autoHeightWrap"},[s("div",{staticClass:"label flex_row"},[s("span",[e._v("已选标准地址")])]),e._v(" "),e.curSelAddress?s("div",{staticClass:"autoHeight flex_row"},[s("span",[e._v(e._s(e.curSelAddObj.address))]),e._v(" "),e.curSelAddObj.similarity?s("Progress",{staticClass:"similar",attrs:{vertical:"","stroke-width":7,title:"相似度",percent:100*e.curSelAddObj.similarity}}):e._e()],1):e._e(),e._v(" "),e.curSelAddress?e._e():s("div",{staticClass:"autoHeight flex_row"},[e._v("无匹配数据")])]),e._v(" "),s("div",{attrs:{slot:"footer"},slot:"footer"},[s("Button",{attrs:{size:"small",disabled:1==e.page&&0==e.curEditIndex},on:{click:e.backRecord}},[s("Icon",{attrs:{type:"ios-arrow-back"}})],1),e._v(" "),s("Button",{on:{click:function(t){e.showEditModal=!1}}},[e._v("取消")]),e._v(" "),s("Button",{attrs:{type:"primary"},on:{click:e.submitEdit}},[e._v("保存")]),e._v(" "),s("Button",{attrs:{size:"small",disabled:(e.curEditIndex==e.listCount%10-1||9==e.curEditIndex)&&e.page==Math.ceil(e.listCount/10)},on:{click:e.nextRecord}},[s("Icon",{attrs:{type:"ios-arrow-forward"}})],1)],1),e._v(" "),e.showELoad?s("Spin",{attrs:{fix:"",size:"large"}}):e._e()],2),e._v(" "),s("Modal",{attrs:{title:"新增地址  +",styles:{width:"380px",position:"absolute",top:"75px",right:"20px",maxHeight:"80%"},scrollable:""},model:{value:e.showAddModal,callback:function(t){e.showAddModal=t},expression:"showAddModal"}},[s("div",{staticClass:"editLine flex_row"},[s("div",{staticClass:"label flex_row"},[e._v("\n        历史地址\n      ")]),e._v(" "),s("div",{staticClass:"input"},[e._v("\n        "+e._s(e.editValue.originAddress)+"\n      ")])]),e._v(" "),e._l(e.addEditObj,function(t,a,i){return s("div",{staticClass:"editLine flex_row"},[s("div",{staticClass:"label flex_row"},[s("span",{domProps:{innerHTML:e._s(t.label)}})]),e._v(" "),s("Input",{class:t.canInput?"input":"cannot input",attrs:{title:e.addEditValue[a],readonly:!t.canInput,icon:e.showOption!=a?"ios-arrow-down":"ios-arrow-up"},on:{"on-click":function(t){e.showOption!=a?e.showOption=a:e.showOption=-1},"on-change":function(s){return t.changeHandler(e.addEditValue[a])}},model:{value:e.addEditValue[a],callback:function(t){e.$set(e.addEditValue,a,t)},expression:"addEditValue[addName]"}}),e._v(" "),e.showOption==a?s("div",{staticClass:"options"},[s("RadioGroup",{attrs:{vertical:""},on:{"on-change":function(t){return e.radioChange(e.addEditValue[a])}},model:{value:e.addEditValue[a],callback:function(t){e.$set(e.addEditValue,a,t)},expression:"addEditValue[addName]"}},e._l(e.radioArr[a],function(t,a){return s("Radio",{key:a,attrs:{label:t}},[e._v("\n            "+e._s(t)+"\n          ")])}),1)],1):e._e()],1)}),e._v(" "),e.showAddBtn?s("div",{staticClass:"editLine flex_row addLine",attrs:{title:"新增其他"},on:{click:function(t){e.showAddBtn=!1}}},[s("Icon",{attrs:{type:"md-add-circle",size:"24",color:"#25A4F4"}})],1):e._e(),e._v(" "),e.showAddBtn?e._e():s("div",{staticClass:"editLine flex_row"},[s("div",{staticClass:"label flex_row"},[s("span",[e._v("其他")])]),e._v(" "),s("Input",{staticClass:"input",attrs:{title:e.addEditValue.suffix,icon:"suffix"!=e.showOption?"ios-arrow-down":"ios-arrow-up"},on:{"on-click":function(t){"suffix"!=e.showOption?e.showOption="suffix":e.showOption=-1}},model:{value:e.addEditValue.suffix,callback:function(t){e.$set(e.addEditValue,"suffix",t)},expression:"addEditValue.suffix"}}),e._v(" "),s("Icon",{attrs:{type:"ios-remove-circle-outline",size:"24",color:"#25A4F4",title:"删除其他"},on:{click:function(t){e.showAddBtn=!0}}}),e._v(" "),"suffix"==e.showOption?s("div",{staticClass:"options otherOpt"},[s("RadioGroup",{attrs:{vertical:""},on:{"on-change":e.radioChange},model:{value:e.addEditValue.suffix,callback:function(t){e.$set(e.addEditValue,"suffix",t)},expression:"addEditValue['suffix']"}},e._l(e.suffixArr,function(t,a){return s("Radio",{key:a,attrs:{label:t}},[e._v("\n            "+e._s(t)+"\n          ")])}),1)],1):e._e()],1),e._v(" "),s("div",{attrs:{slot:"footer"},slot:"footer"},[s("Button",{on:{click:function(t){e.showAddModal=!1}}},[e._v("取消")]),e._v(" "),s("Button",{attrs:{type:"primary"},on:{click:e.handlerAddSite}},[e._v("确定")])],1),e._v(" "),e.showAddLoad?s("Spin",{attrs:{fix:"",size:"large"}}):e._e()],2)],1)},staticRenderFns:[]};var g=s("VU/8")(c,u,!1,function(e){s("AY5B")},"data-v-58cb3e50",null).exports,h=s("KgXo"),f={name:"userCenter",data:function(){return{hasLogin:!1,needUser:!1,disabledRegister:!1,showRegister:!1,registerBox:[{label:"用户名称",val:"username",lType:"text"},{label:"手机号码",val:"phone",lType:"text"},{label:"邮箱地址",val:"email",lType:"text"},{label:"真实姓名",val:"truename",lType:"text"},{label:"密码",val:"password_",lType:"password"},{label:"确认密码",val:"_password",lType:"password"}],blankRe:[null,null,null,null,null,null],registerVal:[null,null,null,null,null,null],r_right:[0,0,0,0,0,0],showReTips:!1,showReLoad:!1,showLogin:!1,disabledLogin:!1,username:null,password:null,l_success:0,showLoLoad:!1}},components:{},computed:{},props:{},created:function(){},mounted:function(){},methods:i()({},Object(d.b)(["updateUsername","updateNeedRefreshTarget"]),{toggleUser:function(){this.needUser=!this.needUser},toggleRegister:function(){this.showRegister=!0,this.r_right=[0,0,0,0,0,0]},reInputFocus:function(e){var t=this.r_right;t[e]=0,this.r_right=t},registerUser:function(){var e=this.registerVal,t=this.registerBox,s={},a=this;a.disabledRegister=!0,a.showReLoad=!0;for(var i=0;i<e.length;i++){var n=t[i].val,o=e[i],r=a.r_right;switch(n){case"username":o?(r[i]=1,a.r_right=r):(r[i]=2,a.r_right=r);break;case"phone":/^1[3456789]\d{9}$/.test(o)?(r[i]=1,a.r_right=r):(r[i]=2,a.r_right=r);break;case"email":/^([A-Za-z0-9_\-\.])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,4})$/.test(o)?(r[i]=1,a.r_right=r):(r[i]=2,a.r_right=r);break;case"truename":/^[\u4e00-\u9fa5]+$/.test(o)?(r[i]=1,a.r_right=r):(r[i]=2,a.r_right=r);break;case"password_":o?(r[i]=1,a.r_right=r):(r[i]=2,a.r_right=r);break;case"_password":var l=s.password_;l&&l==o?(r[i]=1,a.r_right=r):(r[i]=2,a.r_right=r)}s[n]=o}"111111"==a.r_right.join("")?(a.showReTips=!1,delete s._password,a.$api.registerUser(s).then(function(e){e&&(a.$Message.success(s.username+"，注册成功，请登录"),a.showRegister=!1)}).catch(function(e){a.$Message.warning("注册失败请重试"),a.showReLoad=!1,a.disabledRegister=!1})):(a.showReTips=!0,a.showReLoad=!1,a.disabledRegister=!1)},reVC:function(e){e?(this.disabledRegister=!1,this.showReTips=!1,this.showReLoad=!1):this.registerVal=this.blankRe},toggleLogin:function(){this.hasLogin||(this.showLogin=!0,this.disabledLogin=!1,this.showLoLoad=!1,this.username=null,this.password=null,this.l_success=0)},loginIn:function(){var e=this,t=e.username,s=e.password;if(t&&s){e.showLoLoad=!0,e.disabledLogin=!0;var a={username:t,password_:s};e.$api.loginIn(a).then(function(s){e.showLoLoad=!1,"登录成功"==s?(e.$Message.success("登录成功！"),e.l_success=1,e.hasLogin=!0,e.showLogin=!1,e.updateUsername(t),e.updateNeedRefreshTarget((new Date).getTime().toString())):(e.disabledLogin=!1,e.l_success=2,e.$Message.warning("用户名或密码错误"))}).catch(function(t){e.showLoLoad=!1,e.disabledLogin=!1,"登录失败"==t.response.data?(e.l_success=2,e.$Message.warning("用户名或密码错误")):(e.l_success=0,e.$Message.warning("登录失败，请重试"))})}else e.$Message.warning("请输入完整！")},exitLogin:function(){this.hasLogin=!1,this.toggleLogin(),this.updateUsername("user_admin"),this.updateNeedRefreshTarget((new Date).getTime().toString())}}),beforedestroy:function(){},watch:{}},p={render:function(){var e=this,t=e.$createElement,s=e._self._c||t;return s("div",[s("div",{staticClass:"user-center flex_row",on:{click:function(t){e.needUser=!e.needUser,e.showLogin=!1}}},[e.hasLogin?e._e():s("Icon",{attrs:{type:"md-person",color:"#979797",size:"38"}}),e._v(" "),e.hasLogin?s("Icon",{attrs:{type:"md-person",color:"#0083FF",size:"38"}}):e._e()],1),e._v(" "),e.needUser?s("div",{staticClass:"info-box"},[s("div",{staticClass:"arrow"}),e._v(" "),e.hasLogin||e.showLogin?e._e():s("div",{staticClass:"detail-info-box flex_row"},[s("div",{staticClass:"tips",on:{click:e.toggleLogin}},[e._v("登录")]),e._v(" "),s("div",{staticClass:"tips loginBtn",on:{click:e.toggleRegister}},[e._v("注册")])]),e._v(" "),e.hasLogin?s("div",{staticClass:"detail-info-box flex_row"},[e._v("\n      您好，"),s("div",{staticClass:"tips"},[e._v(e._s(e.username))]),e._v(" "),s("div",{staticClass:"tips loginBtn",on:{click:e.exitLogin}},[e._v("退出登录")])]):e._e(),e._v(" "),e.showLogin?s("div",{staticClass:"detail-info-box flex_row"},[s("div",{staticClass:"loginbox flex_col"},[s("div",{staticClass:"line flex_row"},[s("Input",{staticClass:"input",attrs:{placeholder:"用户名",disabled:e.disabledLogin,prefix:"ios-contact",clearable:""},model:{value:e.username,callback:function(t){e.username=t},expression:"username"}}),e._v(" "),1==e.l_success?s("Icon",{attrs:{type:"ios-checkmark-circle-outline",color:"#fff",size:"16"}}):e._e(),e._v(" "),2==e.l_success?s("Icon",{attrs:{type:"ios-close-circle-outline",color:"#fff",size:"16"}}):e._e()],1),e._v(" "),s("div",{staticClass:"line flex_row"},[s("Input",{staticClass:"input",attrs:{placeholder:"密码",type:"password",disabled:e.disabledLogin,prefix:"ios-lock",clearable:""},model:{value:e.password,callback:function(t){e.password=t},expression:"password"}}),e._v(" "),1==e.l_success?s("Icon",{attrs:{type:"ios-checkmark-circle-outline",color:"#fff",size:"16"}}):e._e(),e._v(" "),2==e.l_success?s("Icon",{attrs:{type:"ios-close-circle-outline",color:"#fff",size:"16"}}):e._e()],1),e._v(" "),s("div",{staticClass:"btnBox flex_row"},[s("Button",{staticClass:"btn1 flex_row",attrs:{long:"",shape:"circle"},on:{click:e.loginIn}},[e._v("登录")]),e._v(" "),s("div",{staticClass:"btn2",on:{click:e.toggleRegister}},[e._v("注册")])],1)]),e._v(" "),e.showLoLoad?s("Spin",{attrs:{fix:"",size:"large"}}):e._e()],1):e._e()]):e._e(),e._v(" "),s("Drawer",{staticClass:"registerBox",attrs:{title:"用户注册"},on:{"on-visible-change":e.reVC},model:{value:e.showRegister,callback:function(t){e.showRegister=t},expression:"showRegister"}},[s("div",e._l(e.registerBox,function(t,a){return s("div",{staticClass:"flex_row line"},[s("div",{staticClass:"label"},[e._v(e._s(t.label)+"：")]),e._v(" "),s("Input",{staticClass:"input",attrs:{placeholder:"请输入"+t.label+"…",type:t.lType,disabled:e.disabledRegister,clearable:""},on:{"on-focus":function(t){return e.reInputFocus(a)}},model:{value:e.registerVal[a],callback:function(t){e.$set(e.registerVal,a,t)},expression:"registerVal[bIndex]"}}),e._v(" "),1==e.r_right[a]?s("Icon",{attrs:{type:"ios-checkmark-circle-outline",color:"green",size:"16"}}):e._e(),e._v(" "),2==e.r_right[a]?s("Icon",{attrs:{type:"ios-close-circle-outline",color:"red",size:"16"}}):e._e()],1)}),0),e._v(" "),s("div",{staticClass:"btnBox flex_row"},[s("Button",{staticClass:"btn",on:{click:function(t){e.showRegister=!1}}},[e._v("取消")]),e._v(" "),s("Button",{staticClass:"btn",attrs:{type:"primary"},on:{click:e.registerUser}},[e._v("注册")])],1),e._v(" "),e.showReLoad?s("Spin",{attrs:{fix:"",size:"large"}}):e._e(),e._v(" "),e.showReTips?s("div",{staticClass:"tip"},[e._v("\n      请确认输入值合法后，再重试\n    ")]):e._e()],1)],1)},staticRenderFns:[]};var m=s("VU/8")(f,p,!1,function(e){s("jL9I")},"data-v-19288520",null).exports,v={name:"home",components:{editBox:g,loading:h.a,userCenter:m},created:function(){},data:function(){return{showLoading:!1,openTarget:!1,targetData:null,targetListIndex:0}},computed:i()({},Object(d.c)(["username","tablename"]),{upLoadUrl:function(){return EDITSERVICE.uploadUrl+"?username="+this.username}}),methods:i()({},Object(d.b)(["updateNeedRefreshTarget"]),{importFile:function(){if(!this.username)return this.$Message.warning("请先登录"),!1},beforeUpload:function(e){},onUpSuccess:function(e,t,s){this.$Message.success("数据上传成功!"),this.showLoading=!1,this.updateNeedRefreshTarget((new Date).getTime().toString())},onUpError:function(e,t,s){this.showLoading=!1,this.$Message.error("导入失败，请重试！")},downloadFile:function(){if(this.username)if(this.tablename){var e=this;e.$Message.loading({content:"正在创建下载链接，请稍后……",duration:0}),e.$api.exportFile(e.tablename,e.username).then(function(t){if(t){e.$Message.destroy();var s=e.tablename.substring(0,e.tablename.indexOf(".gpkg"))+".zip";download(t,s,"application/x-download;charset=UTF-8")}else e.$Message.warning("下载链接创建失败，请重试！")}).catch(function(t){t.message&&e.$Message.warning(t.message)})}else this.$Message.warning("请先选择数据源！");else this.$Message.warning("请先登录！")}})},_={render:function(){var e=this,t=e.$createElement,s=e._self._c||t;return s("div",{staticClass:"editWrapper"},[s("edit-box",{staticClass:"leftView"}),e._v(" "),s("div",{staticClass:"rightBox flex_row"},[e.username?s("Upload",{ref:"upload",staticClass:"flex_row",attrs:{action:e.upLoadUrl,name:"fileName",accept:"file","before-upload":e.beforeUpload,"on-success":e.onUpSuccess,"on-error":e.onUpError,"show-upload-list":!1,multiple:""}},[s("Button",{staticClass:"item",attrs:{icon:"md-arrow-round-up",title:"多个文件推荐使用zip"}},[e._v("导入")])],1):e._e(),e._v(" "),e.username?e._e():s("Button",{staticClass:"item",attrs:{icon:"md-arrow-round-up",title:"多个文件推荐使用zip"},on:{click:e.importFile}},[e._v("\n      导入\n    ")]),e._v(" "),s("div",{staticClass:"item flex_col"},[s("Button",{attrs:{icon:"md-download"},on:{click:e.downloadFile}},[e._v("导出")])],1)],1),e._v(" "),e.showLoading?s("Spin",{staticStyle:{background:"rgba(255,255,255,.5)"},attrs:{fix:""}},[s("loading",{attrs:{msg:"上传中……"}})],1):e._e(),e._v(" "),s("user-center",{staticClass:"userView"})],1)},staticRenderFns:[]};var w=s("VU/8")(v,_,!1,function(e){s("hH7r")},"data-v-39a53651",null);t.default=w.exports},hH7r:function(e,t){},jL9I:function(e,t){},lPGJ:function(e,t){}});
//# sourceMappingURL=3.0e85d6d5c1462a01c0dc.js.map