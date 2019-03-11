package top.geomatics.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Paths;
import java.sql.ResultSet;


@Slf4j
@Component
public class IndexUtil {
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
        IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
        iwc.setRAMBufferSizeMB(16);
        IndexWriter writer = new IndexWriter(dir, iwc);
        return writer;
    }

    /**
     * 定时更新索引（每月更新一次索引）
     *
     * @throws Exception
     */

    @Scheduled(cron = "0 0 8 1 * ? ")
    private void updateIndex() throws Exception {
        dir = FSDirectory.open(Paths.get("D:\\lucene_index"));
        IndexWriter writer = getWriter();
        long deleteCount = writer.deleteAll();

        ResultSet resultSet = new SqlliteUtil().getResultSet();
        while (resultSet.next()) {
            Document doc = new Document();
            doc.add(new StringField("address" , (String) resultSet.getObject(1), Field.Store.YES));
            writer.addDocument(doc); // 添加文档
        }
        writer.close();
    }

    public static IndexSearcher init() throws IOException {
        IndexSearcher indexSearcher = null;
        if (indexSearcher == null) {
            // 1、创建Directory
            Directory directory = FSDirectory.open(Paths.get("D:\\lucene_index"));
            // 2、创建IndexReader
            DirectoryReader directoryReader = DirectoryReader.open(directory);
            // 3、根据IndexReader创建IndexSearch
            indexSearcher = new IndexSearcher(directoryReader);
        }
        return indexSearcher;
    }

    //    临时测试加索引
    public static void main(String[] args) throws Exception {
        String string = "民治街道";
        String eStr = URLEncoder.encode(string, "utf-8");
        System.out.println(eStr);
    }


}
