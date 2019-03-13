package top.geomatics.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.util.Version;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;

import org.wltea.analyzer.lucene.IKAnalyzer;
import top.geomatics.utils.IndexUtil;
import top.geomatics.utils.SqlliteUtil;

@RestController
@RequestMapping("/AddressWebService")
public class SelectController {

//    @Autowired
//    private SqlliteUtil sqlliteUtil;

    // 查询全部数据（测试前10条）
    @RequestMapping("/select")
    public String select2() {
        SqlliteUtil sqlliteUtil=new SqlliteUtil();
        // 查询总记录数
        // int count = sqlliteUtil.count("dmdz");
        // 测试前10条数据
        String sql = "select * from dmdz limit 10";
        List list = sqlliteUtil.selectAll(sql);
        // 使用阿里巴巴的fastjson
        String json = JSON.toJSONString(list);
        System.out.println(json);
        return json;

    }

    // 根据详细地址address查询
    @RequestMapping("/selectByAddress")
    public String selectByAddress() {
        SqlliteUtil sqlliteUtil=new SqlliteUtil();
        Long start = System.currentTimeMillis();
        List list = sqlliteUtil.selectByAddress("dmdz" , "广东省深圳市龙华区民治街道龙塘社区上塘农贸建材市场L25号铁皮房");
        // 使用阿里巴巴的fastjson
        Long end = System.currentTimeMillis();
        String json = JSON.toJSONString(list);
        System.out.println(json);
        System.out.println("本次搜索共经历的时间：毫秒  " + (end - start));
        return json;
    }

//    模糊查询（测试上芬社区龙屋新村三巷8号508）1270毫秒
    @RequestMapping("/selectByAddressLike")
    public String selectByAddressLike(@RequestParam(value = "keyWord")String keyWord){
        SqlliteUtil sqlliteUtil=new SqlliteUtil();
        long start=System.currentTimeMillis();
        String s = JSON.toJSONString(sqlliteUtil.fuzzyQuery(keyWord));
        long end=System.currentTimeMillis();
        System.out.println("直接模糊搜索共经历的时间：毫秒  " + (end - start));
        return s;
    }

    //	根据lucene索引进行查询
    @RequestMapping("/selectAddressBylucene")
    public String selectAddressBylucene(@RequestParam(value = "keyWord") String keyWord) {
        String json=null;
        try { List<String> list = new ArrayList<String>();
            IndexSearcher indexSearcher = IndexUtil.init();
            QueryParser queryParser = new QueryParser(Version.LUCENE_47,"address" , new IKAnalyzer(true));
//            String q="select ADDRESS from dmdz where ADDRESS like '%"+keyWord+"%' limit 0,10";
            Query query = queryParser.parse(keyWord);
            Long start = System.currentTimeMillis();
            TopDocs topDocs = indexSearcher.search(query, 10);
            Long end = System.currentTimeMillis();
            System.out.println("lucene搜索共经历的时间：毫秒  " + (end - start));
            for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
                Document doc = indexSearcher.doc(scoreDoc.doc);
                list.add(doc.get("address"));
            }
            json = JSON.toJSONString(list);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return json;
        }

    }


    // 根据地理编码code查询
    @RequestMapping("/selectByCode")
    public String selectByCode() {
        SqlliteUtil sqlliteUtil = new SqlliteUtil();
        List list = sqlliteUtil.selectByCode("dmdz" , "44030600960102T0117");
        // 使用阿里巴巴的fastjson
        String json = JSON.toJSONString(list);
        System.out.println(json);
        return json;
    }

    // 综合地址查询
    @RequestMapping("/selectByMul")
    public String selectByMul() {

        return null;

    }

}
