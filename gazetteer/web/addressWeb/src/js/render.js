/**
 * Created by clover on 2019/3/12.
 */

/**
 * 用正则表达式处理数据
  * @param str
  * @param type Number 1:去除空格
 * @constructor
 */
var RegularStr = function(str,type){
  type=type||1;
  switch (type){
    //第1类，去除空格
    case 1:
      return str.replace(/\s+/g,"");
      break
  }

};



/**
 * 插入搜索历史记录
 * @param item 新输入的内容
 * @returns {searchRecord，历史记录数组}
 */
function SetRecord(item) {
  var searchRecord = localStorage.getItem("searchRecord");
  if(searchRecord){
    searchRecord = JSON.parse(searchRecord)['records'];
    //历史记录中有重复项目时，删除重复项目
    for (var i = 0; i < searchRecord.length; i++) {
      if (item == searchRecord[i]) {
        //删除重复项目
        searchRecord.splice(i, 1);
        break;
      }
    }
    //历史记录已达到最大值10个时，删除最老的
    if (searchRecord.length == 10) {
      searchRecord.splice(0, 1);
    }
    //插入新输入的记录
    searchRecord.push(item);
  }else{
    //没有历史记录时直接插入记录
    searchRecord=[item];
  }
  //存储到内存
  localStorage.setItem("searchRecord",JSON.stringify({records:searchRecord}))

  return searchRecord.reverse();//reverse按从新到老排序
}
/**
 * 获取所有的历史记录
 * @returns {历史记录array}
 */
function GetRecord() {
  var searchRecord = localStorage.getItem("searchRecord");
  if(searchRecord) {
    //有历史记录时，返回记录数组,reverse使数组从新到老排序
    return JSON.parse(localStorage.getItem("searchRecord"))['records'].reverse();
  }else{
    //没有记录时，返回空
    return []
  }
}
/**
 * 清空搜索历史
 */
function ClearRecord() {
  localStorage.removeItem("searchRecord")
}

export{
  RegularStr,SetRecord,GetRecord,ClearRecord
}
