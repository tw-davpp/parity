package lucene;

import database.JDBCConnect;
import database.Product;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.NumericField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Index {
    private final Directory directory;
    private final JDBCConnect jdbcConnect;
    private final String sql = "select * from productInfo";

    public Index(File file) throws IOException, SQLException {
        directory = FSDirectory.open(file);
        IndexWriter writer = new IndexWriter(directory,
                new StandardAnalyzer(Version.LUCENE_30),
                IndexWriter.MaxFieldLength.UNLIMITED);
        jdbcConnect = JDBCConnect.getInstance();

        Connection connect = jdbcConnect.getConnection();
        Statement stmt = connect.createStatement();
        ResultSet resultSet = stmt.executeQuery(sql);

        while (resultSet.next()) {
            Product product = getProduct(resultSet);
            writer.addDocument(addProductToDocument(product));
        }
        writer.close();
    }

    private Document addProductToDocument(Product product) {
        Document document = new Document();
        document.add(new Field("id", product.id + "", Field.Store.YES, Field.Index.NOT_ANALYZED));
        document.add(new Field("url", product.url, Field.Store.YES, Field.Index.NOT_ANALYZED));

        Field title = new Field("title", product.title, Field.Store.YES, Field.Index.ANALYZED);
        title.setBoost(0.6f);
        document.add(title);
        document.add(new NumericField("originPrice", Field.Store.YES,true).setFloatValue(product.originPrice));

        NumericField currentPrice = new NumericField("currentPrice", Field.Store.YES, true).setFloatValue(product.currentPrice);
        currentPrice.setBoost(0.3f);
        document.add(currentPrice);

        NumericField score = new NumericField("score", Field.Store.YES, true).setFloatValue(product.score);
        score.setBoost(0.1f);
        document.add(score);

        return document;
    }

    private Product getProduct(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String title = resultSet.getString("title");
        String url = resultSet.getString("url");
        String originPrice = resultSet.getString("originPrice");
        String currentPrice = resultSet.getString("currentPrice");
        String score = resultSet.getString("score");

        return new Product(id, title, url, originPrice, currentPrice, score);
    }

    public static void main(String[] args) throws IOException, SQLException {
        Index index = new Index(new File("./index"));
    }
}
