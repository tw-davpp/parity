package lucene;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.Version;
import org.junit.Test;

import java.io.IOException;

public class SearchTest {
    @Test
    public void test_search() throws IOException, ParseException {
        Directory dir = TestUtil.getIndexDirectory();
        IndexSearcher searcher = new IndexSearcher(dir);
        QueryParser parser = new QueryParser(Version.LUCENE_30, "title", new StandardAnalyzer(Version.LUCENE_30));
        Query query = parser.parse("中兴");
        SortField currentPriceField = new SortField("currentPrice", SortField.FLOAT, false);
        SortField scoreField = new SortField("score", SortField.FLOAT, true);
        Sort sort = new Sort(new SortField[]{currentPriceField, scoreField});

        TopDocs docs = searcher.search(query, null, 1000, sort);
        for (ScoreDoc scoreDoc : docs.scoreDocs) {
            int docId = scoreDoc.doc;
            float score = scoreDoc.score;
            Document doc = searcher.doc(docId);
            System.out.println(doc.get("title") + ";money:" + doc.get("currentPrice") + ";score:" + doc.get("score"));
        }
    }

}
