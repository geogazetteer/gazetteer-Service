webpackJsonp([1,4],{Ay0f:function(t,e){},Jnd1:function(t,e){},Mm1Y:function(t,e,s){"use strict";var a={name:"smallSpin",data:function(){return{}},components:{},computed:{},props:{msg:{type:[String]}},created:function(){},mounted:function(){},methods:{},beforedestroy:function(){},watch:{}},i={render:function(){var t=this.$createElement,e=this._self._c||t;return e("div",{staticClass:"spin-icon-load"},[e("Spin",{attrs:{fix:""}},[e("Icon",{staticClass:"demo-spin-icon-load",attrs:{type:"ios-loading",size:"18"}}),this._v(" "),this.msg?e("div",[this._v(this._s(this.msg))]):this._e()],1)],1)},staticRenderFns:[]};var n=s("VU/8")(a,i,!1,function(t){s("lPGJ")},"data-v-3c08583e",null);e.a=n.exports},NiFe:function(t,e,s){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var a={name:"batchHandle",components:{loading:s("KgXo").a},created:function(){},data:function(){return{showLoading:!1,excelUrl:encodeURI(BATCHSERVICE.modelUrl),columns:[{title:"",type:"index",width:70,align:"center"},{title:"地址",key:"address",className:"header"},{title:"经度",key:"longitude",className:"header"},{title:"纬度",key:"latitude",className:"header"}],tableData:[],upLoadUrl:BATCHSERVICE.uploadUrl,progressNum:0}},methods:{exportData:function(){this.$refs.table.exportCsv({filename:"批量转换结果"})},beforeUpload:function(t){var e=t.name.split(".");if("xls"!=e[e.length-1]&&"xlsx"!=e[e.length-1])return this.$Modal.warning({title:"上传文件后缀应为.xls或.xlsx!",width:320}),!1;this.showLoading=!0,this.$refs.upload.clearFiles()},onUpSuccess:function(t,e,s){this.$Message.success(s[0].name+"转换成功!"),this.tableData=t.rows,this.progressNum=100,this.showLoading=!1},onUpError:function(t,e,s){this.showLoading=!1,this.$Message.error(s.name+"转换失败，请重试！")}}},i={render:function(){var t=this,e=t.$createElement,s=t._self._c||e;return s("div",{staticClass:"Wrapper"},[s("h1",{staticClass:"title"},[t._v("批量地址匹配服务")]),t._v(" "),s("div",{staticClass:"topTool flex_row"},[s("div",{staticClass:"btnBox flex_row"},[s("a",{staticClass:"item",attrs:{href:t.excelUrl}},[s("Button",{attrs:{icon:"md-download",type:"primary"}},[t._v("下载模板")])],1),t._v(" "),s("Upload",{ref:"upload",staticClass:"flex_row",attrs:{action:t.upLoadUrl,name:"fileName",accept:"file",format:["xls","xlsx"],"before-upload":t.beforeUpload,"on-success":t.onUpSuccess,"on-error":t.onUpError}},[s("Button",{staticClass:"item",attrs:{icon:"md-arrow-round-up",type:"primary"}},[t._v("导入")])],1)],1),t._v(" "),t.progressNum?s("div",{staticClass:"right flex_row"},[s("div",{staticClass:"label"},[t._v("匹配进度：")]),t._v(" "),s("Progress",{attrs:{percent:t.progressNum,status:"active"}}),t._v(" "),s("div",{staticClass:"item"},[s("Button",{attrs:{icon:"ios-redo",type:"primary"},on:{click:t.exportData}},[t._v("导出")])],1)],1):t._e()]),t._v(" "),s("Table",{ref:"table",attrs:{columns:t.columns,data:t.tableData,stripe:"",border:"","max-height":"80%"},scopedSlots:t._u([{key:"fuzzy",fn:function(t){var e=t.row;return[s("Progress",{attrs:{percent:e.fuzzy,status:"active"}})]}}])}),t._v(" "),t.showLoading?s("Spin",{staticStyle:{background:"rgba(255,255,255,.5)"},attrs:{fix:""}},[s("loading")],1):t._e()],1)},staticRenderFns:[]};var n=s("VU/8")(a,i,!1,function(t){s("Jnd1")},"data-v-d6dab16e",null);e.default=n.exports},aBFZ:function(t,e){},fETb:function(t,e){},iyIL:function(t,e,s){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var a=s("Dd8w"),i=s.n(a),n=s("845D"),o=s("Mm1Y"),l=s("R4Sj"),r={name:"searchBox",data:function(){return{searchContent:"",showCard:!1,cardList:[],needSpin:!1,resultPage:1,resultCount:0,showResult:!1,resultList:[],showHis:!1,hisList:[],showDetail:!1,hideDetail:!1,detail:{standard:[],detailList:[]}}},components:{smallSpin:o.a},computed:i()({},Object(l.c)(["isOpenCoordinate","searchPointCoordArr"]),{maxHeight:function(){return Object(n.b)()}}),props:{},created:function(){},mounted:function(){this.hisList=Object(n.c)()},methods:i()({},Object(l.b)(["updateMarkLngLat"]),{clearInput:function(){this.searchContent="",this.showCard=!1,this.showHis=!1,this.needSpin=!1,this.showResult=!1,this.showDetail=!1},testChar:function(t,e,s){t=t||URLCFG.isSensitiveUrl,this.$api.getMsg(t,{chars:e}).then(function(t){s&&s(t)})},getCardList:function(){this.showResult=!1,this.showDetail=!1,this.showHis=!1;var t=Object(n.e)(this.searchContent),e=this;t&&e.$api.getSearchListByPage(1,t).then(function(t){t.rows.length>0&&(e.cardList=t.rows,e.showCard=!0)})},searchItems:function(t){var e=this,s="string"==typeof t?t:Object(n.e)(e.searchContent);s&&(e.showResult=!1,e.showHis=!1,e.showCard=!1,e.hideDetail=!0,e.showDetail=!1,e.testChar(null,s,function(t){t?e.$Message.warning("含有敏感词"):e.isOpenCoordinate?e.testChar(URLCFG.isCoordinateUrl,s,function(t){t?(e.resultCount=0,e.resultList=[],e.showResult=!0):e.$Modal.warning({title:"请输入合法的坐标值\n如:113.9754368,22.599587479",width:320})}):(e.searchContent=s,e.needSpin=!0,e.$api.getSearchCount(s).then(function(t){e.resultCount=t.total,e.$api.getSearchListByPage(1,s).then(function(t){e.resultList=t.rows,e.showResult=!0,e.needSpin=!1,t.total>0&&(e.hisList=Object(n.f)(s))})}))}))},pageChanged:function(t){var e=this;e.needSpin=!0,e.$api.getSearchListByPage(t,e.searchContent).then(function(t){e.resultList=t.rows,e.showResult=!0,e.needSpin=!1})},showDetailList:function(t){var e=this;t&&(this.showResult=!1,e.$api.getDetailBySearchId(t).then(function(t){var s=t.rows[0],a={standard:[{label:"省",value:s.province},{label:"市",value:s.city},{label:"区",value:s.district},{label:"街道",value:s.street},{label:"社区",value:s.community},{label:"基础网格",value:""},{label:"路",value:s.road},{label:"路号",value:s.road_num},{label:"小区",value:s.village},{label:"楼栋",value:s.building}],detailList:[{label:"楼栋编码",value:s.building_id},{label:"详细地址",value:s.address},{label:"门楼号地址",value:""},{label:"小学学区",value:""},{label:"初级中学学区",value:""},{label:"社区网格员ID",value:""}]};e.$api.getMsg(URLCFG.getHouseNumberByAddrUrl,{chars:s.address}).then(function(t){t.length>0&&(a.detailList[2].value=t[0]),e.detail=a,e.showDetail=!0,e.hideDetail=!1}).catch(function(){}),e.$api.getCoordinatesByBId(s.building_id).then(function(t){t&&t.x&&t.y?e.updateMarkLngLat([t.x,t.y]):(e.$Message.warning("没有查询到坐标！"),e.updateMarkLngLat(void 0))}).catch(function(t){e.$Message.warning("没有查询到坐标！"),e.updateMarkLngLat(void 0)})}))},backResult:function(){this.showResult=!0,this.showDetail=!1},searchFocus:function(){this.searchContent||(this.showHis=!0,this.showCard=!1)},searchBlur:function(){},hideHis:function(){this.showHis=!1},clearHis:function(){Object(n.a)(),this.hisList=[]}}),beforedestroy:function(){},watch:{searchPointCoordArr:{handler:function(t,e){if(t){var s=this,a=t.join(",");s.searchContent=a,s.needSpin=!0,s.showResult=!1,s.showHis=!1,s.showCard=!1,s.hideDetail=!0,s.showDetail=!1,s.$api.searchByPoint(t[0],t[1]).then(function(t){t.rows.length>0?(s.resultCount=t.total,s.resultList=t.rows,s.showResult=!0,s.needSpin=!1):(s.$Message.warning("没有匹配到建筑物！"),s.resultCount=0,s.resultList=[],s.showResult=!0,s.needSpin=!1)})}},immediate:!1},resultList:{handler:function(){}}}},c={render:function(){var t=this,e=t.$createElement,s=t._self._c||e;return s("div",{staticClass:"leftView"},[s("div",{staticClass:"searchbox flex_row"},[s("div",{staticClass:"searchbox-content flex_row"},[s("input",{directives:[{name:"model",rawName:"v-model",value:t.searchContent,expression:"searchContent"}],attrs:{id:"sole-input",type:"text",autocomplete:"off",maxlength:"256",placeholder:"输入地名进行搜索"},domProps:{value:t.searchContent},on:{input:[function(e){e.target.composing||(t.searchContent=e.target.value)},t.getCardList],keyup:function(e){return!e.type.indexOf("key")&&t._k(e.keyCode,"enter",13,e.key,"Enter")?null:t.searchItems(e)},blur:function(t){},focus:t.searchFocus}}),t._v(" "),s("div",{directives:[{name:"show",rawName:"v-show",value:t.searchContent,expression:"searchContent"}],staticClass:"input-clear",attrs:{title:"清空"},on:{click:t.clearInput}})]),t._v(" "),s("button",{attrs:{id:"search-button","data-title":"搜索"},on:{click:t.searchItems}})]),t._v(" "),t.showCard?s("ul",{staticClass:"cardlist"},t._l(t.cardList,function(e,a){return s("li",{staticClass:"ellipsis",staticStyle:{height:"45px"},attrs:{title:e.address},on:{click:function(s){return t.searchItems(e.address)}}},[s("Icon",{attrs:{type:"md-search",size:"20"}}),t._v(" "),s("span",[t._v(t._s(e.address))])],1)}),0):t._e(),t._v(" "),t.needSpin?s("small-spin",{staticStyle:{width:"320px"},attrs:{msg:"正在搜索……"}}):t._e(),t._v(" "),s("div",{directives:[{name:"show",rawName:"v-show",value:t.showResult,expression:"showResult"}],staticClass:"resultWrapper"},[s("ul",{staticClass:"cardlist resultList",style:{maxHeight:t.maxHeight-140+"px"}},t._l(t.resultList,function(e,a){return s("li",{staticClass:"flex_row",on:{click:function(s){return t.showDetailList(e.id)}}},[s("div",{staticClass:"imgItem",style:{backgroundPosition:-21*a+"px 0"}}),t._v(" "),s("div",{staticClass:"right flex_col"},[s("div",{staticClass:"small detail"},[t._v("详细地址："+t._s(e.address))])])])}),0),t._v(" "),0==t.resultList.length&&t.showResult?s("div",{staticClass:"bottomDiv noSuch"},[s("span",[t._v("--没有搜索结果--")])]):t._e(),t._v(" "),s("Page",{attrs:{total:t.resultCount,"show-total":"",size:"small","class-name":"pageClass"},on:{"on-change":t.pageChanged}})],1),t._v(" "),t.showDetail?s("div",{staticClass:"detailList"},[s("div",{staticClass:"back flex_row",on:{click:t.backResult}},[s("span",{staticClass:"img"}),t._v(" "),s("span",{staticClass:"ellipsis"},[t._v("返回“"+t._s(t.searchContent)+"”的搜索结果")])]),t._v(" "),s("div",{staticClass:"bottomDiv title flex_row"},[s("span",{staticClass:"hisBottom"},[t._v("编码信息")]),t._v(" "),s("span",{directives:[{name:"show",rawName:"v-show",value:!t.hideDetail,expression:"!hideDetail"}],staticClass:"hideHis",on:{click:function(e){t.hideDetail=!t.hideDetail}}},[t._v("▲")]),t._v(" "),s("span",{directives:[{name:"show",rawName:"v-show",value:t.hideDetail,expression:"hideDetail"}],staticClass:"hideHis",on:{click:function(e){t.hideDetail=!t.hideDetail}}},[t._v("▼")])]),t._v(" "),s("div",{directives:[{name:"show",rawName:"v-show",value:!t.hideDetail&&t.showDetail,expression:"!hideDetail&&showDetail"}],staticClass:"content",style:{maxHeight:t.maxHeight-160+"px"}},[s("div",{staticClass:"line"},[s("span",{staticClass:"bold"},[t._v(t._s(t.detail.detailList[0].label)+":")]),t._v(" "),s("span",[t._v(t._s(t.detail.detailList[0].value))])]),t._v(" "),s("div",{staticClass:"line standard"},[s("span",{staticClass:"bold"},[t._v("标准地址")]),t._v(" "),s("div",{staticClass:"standardCtx"},t._l(t.detail.standard,function(e){return s("div",{staticClass:"standardLine flex_row"},[s("div",{staticClass:"label flex_row"},[t._v(t._s(e.label))]),t._v(" "),s("div",{staticClass:"value"},[t._v(t._s(e.value))])])}),0)]),t._v(" "),t._l(t.detail.detailList.slice(1),function(e){return s("div",{staticClass:"line"},[s("span",{staticClass:"bold"},[t._v(t._s(e.label)+"：")]),t._v(" "),s("span",[t._v(t._s(e.value))])])})],2)]):t._e(),t._v(" "),t.hisList.length>0&&t.showHis?s("ul",{staticClass:"cardlist hislist"},[t._l(t.hisList,function(e,a){return s("li",{on:{click:function(s){return t.searchItems(e)}}},[s("Icon",{attrs:{type:"md-alarm",size:"20"}}),t._v(" "),s("span",[t._v(t._s(e))])],1)}),t._v(" "),s("div",{staticClass:"bottomDiv flex_row"},[s("div",{staticClass:"hisBottom",on:{click:t.clearHis}},[t._v("--清空历史记录--")]),t._v(" "),s("div",{staticClass:"hideHis",on:{click:t.hideHis}},[t._v("▲")])])],2):t._e()],1)},staticRenderFns:[]};var d=s("VU/8")(r,c,!1,function(t){s("aBFZ")},"data-v-216b3b26",null).exports,u=s("mvHQ"),h=s.n(u),v={name:"toolBox",data:function(){return{isOpen:!1,settings_1:SETLABELARR.settings_1,settings_2:SETLABELARR.settings_2,settings_3:SETLABELARR.settings_3,settings_4:SETLABELARR.settings_4,setModal_1:[],setModal_2:[],setModal_3:SETLABELARR.settings_3[0],setModal_4:SETLABELARR.settings_4[0],showLoading:!1,canSelPoint:!1}},components:{},computed:{setObj_all_false:function(){var t={};for(var e in SETDICT)t[SETDICT[e]]=!1;for(var e in SETDICT_DEFAULT)t[SETDICT_DEFAULT[e]]=!0;return t},setdict:function(){var t=SETDICT;for(var e in SETDICT_DEFAULT)t[e]=SETDICT_DEFAULT[e];return t}},props:{},created:function(){this.updateCanSelPoint(!1)},mounted:function(){this.$api.setSettings(this.setObj_all_false)},methods:i()({},Object(l.b)(["updateIsOpenCoordinate","updateCanSelPoint"]),{toggleTool:function(t){this.setdict&&this.setObj_all_false&&(this.isOpen=!!t)},getModal:function(){var t=this;t.showLoading=!0;var e=t.setModal_1.concat(t.setModal_2);t.setModal_3&&e.push(t.setModal_3),t.setModal_4&&e.push(t.setModal_4);for(var s=JSON.parse(h()(t.setObj_all_false)),a=0;a<e.length;a++)s[t.setdict[e[a]]]=!0;t.$api.setSettings(s).then(function(e){t.showLoading=!1}),-1!=e.indexOf("坐标识别")?t.updateIsOpenCoordinate(!0):t.updateIsOpenCoordinate(!1)},openBatch:function(){this.$emit("openBatch")},openSelPoint:function(t){this.canSelPoint=!!t,this.updateCanSelPoint(this.canSelPoint)}}),beforedestroy:function(){},watch:{}},p={render:function(){var t=this,e=t.$createElement,s=t._self._c||e;return s("div",{staticClass:"toolWrapper"},[s("div",{staticClass:"iconBox flex_row"},[s("div",{staticClass:"iconWrap flex_col"},[t.canSelPoint?s("Icon",{attrs:{type:"ios-pin",size:"24",color:"#3385ff",title:"搜索地图上点"},on:{click:function(e){return t.openSelPoint(0)}}}):t._e(),t._v(" "),t.canSelPoint?t._e():s("Icon",{attrs:{type:"ios-pin",size:"24",color:"#989898",title:"搜索地图上点"},on:{click:function(e){return t.openSelPoint(1)}}})],1),t._v(" "),s("div",{staticClass:"iconWrap flex_col hoverBlue"},[s("Icon",{attrs:{type:"ios-folder-open",color:"#989898",size:"24",title:"批量处理"},on:{click:t.openBatch}})],1),t._v(" "),s("div",{staticClass:"iconWrap flex_col"},[t.isOpen?s("Icon",{attrs:{type:"md-settings",size:"24",title:"设置搜索条件",color:"#3385ff"},on:{click:function(e){return t.toggleTool(0)}}}):t._e(),t._v(" "),t.isOpen?t._e():s("Icon",{attrs:{type:"md-settings",size:"24",title:"设置搜索条件",color:"#989898"},on:{click:function(e){return t.toggleTool(1)}}})],1)]),t._v(" "),t.isOpen?s("div",{staticClass:"tools"},[s("span",{staticClass:"arrow"}),t._v(" "),s("div",{staticClass:"line"},[s("CheckboxGroup",{on:{"on-change":t.getModal},model:{value:t.setModal_1,callback:function(e){t.setModal_1=e},expression:"setModal_1"}},t._l(t.settings_1,function(t,e){return s("Checkbox",{key:e,attrs:{label:t,title:"开启"+t}})}),1)],1),t._v(" "),s("div",{staticClass:"line"},[s("CheckboxGroup",{on:{"on-change":t.getModal},model:{value:t.setModal_2,callback:function(e){t.setModal_2=e},expression:"setModal_2"}},t._l(t.settings_2,function(t,e){return s("Checkbox",{key:e,attrs:{label:t,title:"开启"+t}})}),1)],1),t._v(" "),s("div",{staticClass:"line"},[s("RadioGroup",{on:{"on-change":t.getModal},model:{value:t.setModal_3,callback:function(e){t.setModal_3=e},expression:"setModal_3"}},t._l(t.settings_3,function(t,e){return s("Radio",{key:e,attrs:{label:t,title:"开启"+t}})}),1)],1),t._v(" "),s("div",{staticClass:"line"},[s("RadioGroup",{on:{"on-change":t.getModal},model:{value:t.setModal_4,callback:function(e){t.setModal_4=e},expression:"setModal_4"}},t._l(t.settings_4,function(t,e){return s("Radio",{key:e,attrs:{label:t,title:"开启"+t}})}),1)],1),t._v(" "),t.showLoading?s("Spin",{attrs:{fix:"",size:"large"}}):t._e()],1):t._e()])},staticRenderFns:[]};var _={name:"home",components:{searchBox:d,toolBox:s("VU/8")(v,p,!1,function(t){s("fETb")},"data-v-42dd1e9b",null).exports,batchHandle:s("NiFe").default},created:function(){},data:function(){return{openBatch:!1}},methods:{}},f={render:function(){var t=this,e=t.$createElement,s=t._self._c||e;return s("div",{staticClass:"wrapper"},[s("search-box",{staticClass:"leftView"}),t._v(" "),s("tool-box",{staticClass:"toolBox",on:{openBatch:function(e){t.openBatch=!0}}}),t._v(" "),t.openBatch?s("div",{staticClass:"batchWrapper"},[s("Icon",{staticClass:"close",attrs:{type:"md-close",size:"32",color:"#3385ff"},on:{click:function(e){t.openBatch=!1}}}),t._v(" "),s("batch-handle")],1):t._e()],1)},staticRenderFns:[]};var C=s("VU/8")(_,f,!1,function(t){s("Ay0f")},"data-v-6ed980ff",null);e.default=C.exports},lPGJ:function(t,e){}});
//# sourceMappingURL=1.ec07cc47948ed50fdc69.js.map