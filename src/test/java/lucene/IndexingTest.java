package lucene;

import junit.framework.TestCase;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.index.Term;

import java.io.IOException;

public class IndexingTest extends TestCase {
    protected String[] ids = {"1", "2"};
    protected String[] unindexed = {"Netherlands", "Italy"};
    protected String[] unstored = {"Amsterdam has lots of bridges",
            "Venice has lots of canals"};
    protected String[] text = {"Amsterdam", "Venice"};

    private Directory directory;

    protected void setUp() throws Exception {       //1 Run before every test
        directory = new RAMDirectory();

        IndexWriter writer = getWriter();           //2 Create IndexWriter

        for (int i = 0; i < ids.length; i++) {      //3 Add documents
            Document doc = new Document();
            doc.add(new Field("id", ids[i],
                    Field.Store.YES,
                    Field.Index.NOT_ANALYZED));
            doc.add(new Field("country", unindexed[i],
                    Field.Store.YES,
                    Field.Index.NO));
            doc.add(new Field("contents", unstored[i],
                    Field.Store.NO,
                    Field.Index.ANALYZED));
            doc.add(new Field("city", text[i],
                    Field.Store.YES,
                    Field.Index.ANALYZED));
            writer.addDocument(doc);
        }
        writer.close();
    }

    private IndexWriter getWriter() throws IOException {       // 2 Create IndexWriter
        return new IndexWriter(directory, new WhitespaceAnalyzer(),
                IndexWriter.MaxFieldLength.UNLIMITED);
    }

    protected int getHitCount(String fieldName, String searchString) throws IOException {
        IndexSearcher searcher = new IndexSearcher(directory); //4 Create new searcher
        Term t = new Term(fieldName, searchString);
        Query query = new TermQuery(t);                        //5 Build simple single-term query
        int hitCount = TestUtil.hitCount(searcher, query);     //6 Get number of hits
        searcher.close();
        return hitCount;
    }

    public void testIndexWriter() throws IOException {
        IndexWriter writer = getWriter();
        assertEquals(ids.length, writer.numDocs());            //7 Verify writer document count
        writer.close();
    }

    public void testIndexReader() throws IOException {
        IndexReader reader = IndexReader.open(directory);
        assertEquals(ids.length, reader.maxDoc());             //8 Verify reader document count
        assertEquals(ids.length, reader.numDocs());
        reader.close();
    }

    public void testDeleteBeforeOptimize() throws IOException {
        IndexWriter writer = getWriter();
        assertEquals(2, writer.numDocs());   //A 2 docs in the index
        writer.deleteDocuments(new Term("id", "1"));  //B Delete first document
        writer.commit();
        assertTrue(writer.hasDeletions());   //1 Index contains deletions
        assertEquals(2, writer.maxDoc());    //2 1 indexed document, 1 deleted document
        assertEquals(1, writer.numDocs());
        writer.close();
    }

    public void testDeleteAfterOptimize() throws IOException {
        IndexWriter writer = getWriter();
        assertEquals(2, writer.numDocs());
        writer.deleteDocuments(new Term("id", "1"));
        writer.optimize();                 //3 Optimize compacts deletes
        writer.commit();
        assertFalse(writer.hasDeletions());
        assertEquals(1, writer.maxDoc());  //C 1 indexed document, 0 deleted documents
        assertEquals(1, writer.numDocs()); //C 1 indexed document, 0 deleted documents
        writer.close();
    }

    public void testUpdate() throws IOException {

        assertEquals(1, getHitCount("city", "Amsterdam"));

        IndexWriter writer = getWriter();

        Document doc = new Document();         //A Create new document with "Haag" in city field
        doc.add(new Field("id", "1",
                Field.Store.YES,
                Field.Index.NOT_ANALYZED));
        doc.add(new Field("country", "Netherlands",
                Field.Store.YES,
                Field.Index.NO));
        doc.add(new Field("contents",
                "Den Haag has a lot of museums",
                Field.Store.NO,
                Field.Index.ANALYZED));
        doc.add(new Field("city", "Den Haag",
                Field.Store.YES,
                Field.Index.ANALYZED));

        writer.updateDocument(new Term("id", "1"), doc);  //B Replace original document with new version

        writer.close();

        assertEquals(0, getHitCount("city", "Amsterdam"));//C Verify old document is gone
        assertEquals(1, getHitCount("city", "Haag"));     //D Verify new document is indexed
    }

    public void testMaxFieldLength() throws IOException {

        assertEquals(1, getHitCount("contents", "bridges"));  //1 One initial document has bridges

        IndexWriter writer = new IndexWriter(directory,       //2 Create writer with maxFieldLength 1
                new WhitespaceAnalyzer(),
                new IndexWriter.MaxFieldLength(1));
        Document doc = new Document();                        //3 Index document with bridges
        doc.add(new Field("contents",
                "these bridges can't be found",
                Field.Store.NO, Field.Index.ANALYZED));
        writer.addDocument(doc);
        writer.close();

        assertEquals(1, getHitCount("contents", "bridges"));   //4 Document can't be found
    }

}
