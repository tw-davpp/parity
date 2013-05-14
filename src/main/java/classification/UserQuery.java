package classification;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.apache.lucene.util.Version;

/**
 * This is a class that encapsulates a personalized query
 *
 * @author babis
 */
public class UserQuery {

    private String uid;
    private String query;
    private String[] queryTerms;

    public UserQuery(String uid, String q) throws IOException {

        setUid(uid);
        setQuery(q);

        ArrayList<String> qTerms = new ArrayList<String>();
        StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_30);

        TokenStream stream = analyzer.tokenStream("query", new StringReader(q));

        TermAttribute term = stream.addAttribute(TermAttribute.class);
        while (stream.incrementToken()) {
            qTerms.add(new String(term.term()));
        }

        /*
        boolean hasTokens = true;
        while (hasTokens) {
            Token token = stream.next();

            if (token == null) {
                hasTokens = false;
            } else {
                qTerms.add(new String(token.termBuffer(), 0, token.termLength()));
            }
        }
        */

        queryTerms = qTerms.toArray(new String[qTerms.size()]);
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getName() {
        return UserQuery.class.getCanonicalName();
    }

    public UserQuery getValue() {

        return this;
    }

    public String[] getQueryTerms() {
        return queryTerms;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((query == null) ? 0 : query.hashCode());
        result = prime * result + Arrays.hashCode(queryTerms);
        result = prime * result + ((uid == null) ? 0 : uid.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final UserQuery other = (UserQuery) obj;
        if (query == null) {
            if (other.query != null)
                return false;
        } else if (!query.equals(other.query))
            return false;
        if (!Arrays.equals(queryTerms, other.queryTerms))
            return false;
        if (uid == null) {
            if (other.uid != null)
                return false;
        } else if (!uid.equals(other.uid))
            return false;
        return true;
    }

}

