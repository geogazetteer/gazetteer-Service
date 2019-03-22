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
export{
  RegularStr
}
