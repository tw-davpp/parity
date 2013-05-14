package classification;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class MySearcher {

    private static final String PRETTY_LINE =
            "_______________________________________________________________________";

    private String indexDir;
    private NaiveBayes learner = null;
    private boolean verbose = true;

    public MySearcher(String indexDir) {
        this.indexDir = indexDir;
    }

    public void setUserLearner(NaiveBayes naiveBayes) {
        learner = naiveBayes;
    }

    public SearchResult[] search(String query, int numberOfMatches) {
        SearchResult[] docResults = new SearchResult[0];

        IndexSearcher searcher = null;
        try {
            searcher = new IndexSearcher(FSDirectory.open(new File(indexDir)));
        } catch (IOException ioX) {
            System.out.println("ERROR: " + ioX.getMessage());
        }

        QueryParser qp = new QueryParser(Version.LUCENE_30, "content", new StandardAnalyzer(Version.LUCENE_30));

        Query q = null;
        try {
            q = qp.parse(query);
        } catch (ParseException pX) {
            System.out.println("ERROR: " + pX.getMessage());
        }

        TopDocs docs = null;
        try {
            docs = searcher.search(q, numberOfMatches);

            int hits = docs.totalHits;
            docResults = new SearchResult[hits];

            ScoreDoc[] scoreDoc = docs.scoreDocs;
            for (int i = 0; i < hits; i++) {
                Document document = searcher.doc(scoreDoc[i].doc);
                docResults[i] = new SearchResult(document.get("docid"),
                        document.get("doctype"),
                        document.get("title"),
                        document.get("url"),
                        scoreDoc[i].score);
            }

            searcher.close();
        } catch (IOException ioX) {
            System.out.println("ERROR: " + ioX.getMessage());
        }

        String header = "Search results using Lucene index scores:";
        boolean showTitle = true;
        printResults(header, "Query: " + query, docResults, showTitle);

        return docResults;
    }


    public SearchResult[] search(UserQuery uQuery, int numberOfMatches) {
        SearchResult[] docResults = search(uQuery.getQuery(), numberOfMatches);

        String url;
        int docN = docResults.length;

        if (docN > 0) {
            int loop = (docN < numberOfMatches) ? docN : numberOfMatches;

            for (int i = 0; i < loop; i++) {
                url = docResults[i].getUrl();

                UserClick uClick = new UserClick(uQuery, url);
                double indexScore = docResults[i].getScore();

                double userClickScore = 0.0;
                for (Concept bC : learner.getTset().getConceptSet()) {
                    if (bC.getName().equalsIgnoreCase(url)) {
                        userClickScore = learner.getProbability(bC, uClick);
                    }
                }

                // Create the final score
                double hScore;
                if (userClickScore == 0) {
                    hScore = indexScore;
                } else {
                    hScore = indexScore * userClickScore;
                }

                // Update the score of the results
                docResults[i].setScore(hScore);
            }
        }

        // Sort array of results
        SearchResult.sortByScore(docResults);

        String header = "Search results using combined Lucene scores, " +
                "user clicks:";
        String query = "Query: user=" + uQuery.getUid() + ", query text=" + uQuery.getQuery();
        boolean showTitle = false;
        printResults(header, query, docResults, showTitle);

        return docResults;
    }

    public boolean isVerbose() {
        return verbose;
    }

    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    private void printResults(String header, String query, SearchResult[] values, boolean showDocTitle) {
        if (verbose) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);

            boolean printEntrySeparator = false;
            if (showDocTitle) { // multiple lines per entry
                printEntrySeparator = true;
            }

            pw.print("\n");
            pw.println(header);
            if (query != null) {
                pw.println(query);
            }
            pw.print("\n");
            for (int i = 0, n = values.length; i < n; i++) {
                if (showDocTitle) {
                    pw.printf("Document Title: %s\n", values[i].getTitle());
                }
                pw.printf("Document URL: %-46s  -->  Relevance Score: %.15f\n",
                        values[i].getUrl(), values[i].getScore());
                if (printEntrySeparator) {
                    pw.printf(PRETTY_LINE);
                    pw.printf("\n");
                }
            }
            if (!printEntrySeparator) {
                pw.print(PRETTY_LINE);
            }

            System.out.println(sw.toString());
        }
    }

}

