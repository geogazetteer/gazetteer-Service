package top.geomatics.gazetteer.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.alibaba.fastjson.JSON;

import top.geomatics.gazetteer.service.utils.SqlliteUtil;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.management.Query;



//@Component
public class LuceneUtil {
    @Value("${index.path}")
    public static String INDEX_PATH;
    private Directory dir;

    /**
     * 获取IndexWriter实例
     *
     * @return
     * @throws Exception
     */
    private IndexWriter getWriter() throws Exception {
        Analyzer analyzer = new IKAnalyzer(true); // 第三方分词器  按最大词长进行划分
        IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_47,analyzer);
        iwc.setRAMBufferSizeMB(16);
        IndexWriter writer = new IndexWriter(dir, iwc);
        return writer;
    }

    /**
     * 定时更新索引（每月1号8点更新一次索引）
     *
     * @throws Exception
     */

    @Scheduled(cron = "0 0 8 1 * ? ")
    public void updateIndex() throws Exception {
        dir = FSDirectory.open(new File("D:\\lucene_index"));
        IndexWriter writer = getWriter();
        writer.deleteAll();

        ResultSet resultSet = new SqlliteUtil().getResultSet();
        while (resultSet.next()) {
            Document doc = new Document();
            doc.add(new StringField("code",resultSet.getString("CODE"),Field.Store.YES));
            doc.add(new TextField("address" , resultSet.getString("ADDRESS"), Field.Store.YES));
            writer.addDocument(doc); // 添加文档
        }
        writer.close();
    }

    private IndexSearcher init() throws IOException {
        IndexSearcher indexSearcher = null;
        if (indexSearcher == null) {
            // 1、创建Directory
            Directory directory = FSDirectory.open(new File("E:\\lucene_index"));
            // 2、创建IndexReader
            DirectoryReader directoryReader = DirectoryReader.open(directory);
            // 3、根据IndexReader创建IndexSearch
            indexSearcher = new IndexSearcher(directoryReader);
        }
        return indexSearcher;
    }

    public String selectAddressBylucene( String keyWord) {
		String json = null;
		try {
			List<String> list = new ArrayList<String>();
			IndexSearcher indexSearcher = init();
			QueryParser queryParser = new QueryParser(Version.LUCENE_47, "address", new IKAnalyzer(true));
//          String q="select ADDRESS from dmdz where ADDRESS like '%"+keyWord+"%' limit 0,10";
			org.apache.lucene.search.Query query = queryParser.parse(keyWord);
			Long start = System.currentTimeMillis();
			TopDocs topDocs = indexSearcher.search(query, 10);
			Long end = System.currentTimeMillis();
			System.out.println("lucene耗时" + (end - start));
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
    
  
}
