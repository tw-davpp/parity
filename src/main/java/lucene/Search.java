package lucene;

import database.Product;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Search {
    private final IndexSearcher searcher;

    public Search() throws IOException {
        Directory dir = FSDirectory.open(new File("./index"));
        searcher = new IndexSearcher(dir);
    }

    public List<Product> search(String queryStr) throws ParseException, IOException {
        List<Product> result = new ArrayList<Product>();

        QueryParser parser = new QueryParser(Version.LUCENE_30, "title", new StandardAnalyzer(Version.LUCENE_30));
        Query query = parser.parse(queryStr);
        SortField currentPriceField = new SortField("currentPrice", SortField.FLOAT, false);
        SortField scoreField = new SortField("score", SortField.FLOAT, true);
        Sort sort = new Sort(new SortField[]{currentPriceField, scoreField});

        TopDocs docs = searcher.search(query, null, 1000, sort);
        for (ScoreDoc scoreDoc : docs.scoreDocs) {
            int docId = scoreDoc.doc;
            float score = scoreDoc.score;
            Document doc = searcher.doc(docId);
            //System.out.println(doc.get("title") + ";money:" + doc.get("currentPrice") + ";score:" + doc.get("score"));
            result.add(
                    new Product(Integer.parseInt(doc.get("id")),
                            doc.get("title"),
                            doc.get("url"),
                            doc.get("originPrice"),
                            doc.get("currentPrice"),
                            doc.get("score")));
        }
        return result;
    }
}
