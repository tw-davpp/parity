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

    private String userId;
    private String query;
    private String[] queryTerms;

    public UserQuery(String userId, String queryStr) throws IOException {
        setUserId(userId);
        setQuery(queryStr);

        ArrayList<String> queryTerms = new ArrayList<String>();
        StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_30);

        TokenStream stream = analyzer.tokenStream("query", new StringReader(queryStr));

        TermAttribute term = stream.addAttribute(TermAttribute.class);
        while (stream.incrementToken()) {
            queryTerms.add(new String(term.term()));
        }

        /*
        boolean hasTokens = true;
        while (hasTokens) {
            Token token = stream.next();

            if (token == null) {
                hasTokens = false;
            } else {
                queryTerms.add(new String(token.termBuffer(), 0, token.termLength()));
            }
        }
        */

        this.queryTerms = queryTerms.toArray(new String[queryTerms.size()]);
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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
        result = prime * result + ((userId == null) ? 0 : userId.hashCode());
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
        if (userId == null) {
            if (other.userId != null)
                return false;
        } else if (!userId.equals(other.userId))
            return false;
        return true;
    }

}

