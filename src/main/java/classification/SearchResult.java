package classification;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SearchResult {

    private String id;
    private String originPrice;
    private String currentPrice;
    private String title;
    private String url;
    private String productScore;

    private double score;

    public SearchResult(String id, String title, String url, String originPrice,String currentPrice, String productScore, double score) {
        this.id = id;
        this.title = title;
        this.url = url;
        this.originPrice = originPrice;
        this.currentPrice = currentPrice;
        this.productScore = productScore;
        this.score = score;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String print() {
        StringBuilder strB = new StringBuilder();
        strB.append("Document ID    : ").append(id).append("\n");
        strB.append("Document Title : ").append(title).append("\n");
        strB.append("Document URL: ").append(url).append("  -->  ");
        strB.append("Relevance Score: ").append(score).append("\n");
        return strB.toString();
    }

    /**
     * Sorts list in descending order of score value.
     */
    public static void sortByScore(List<SearchResult> values) {
        Collections.sort(values, new Comparator<SearchResult>() {
            public int compare(SearchResult r1, SearchResult r2) {
                int result = 0;
                // sort based on score value
                if (r1.getScore() < r2.getScore()) {
                    result = 1; // sorting in descending order
                } else if (r1.getScore() > r2.getScore()) {
                    result = -1;
                } else {
                    result = 0;
                }
                return result;
            }
        });
    }

    /**
     * Sorts array in descending order of score value.
     */
    public static void sortByScore(SearchResult[] values) {
        Arrays.sort(values, new Comparator<SearchResult>() {
            public int compare(SearchResult r1, SearchResult r2) {
                int result = 0;
                // sort based on score value
                if (r1.getScore() < r2.getScore()) {
                    result = 1; // sorting in descending order
                } else if (r1.getScore() > r2.getScore()) {
                    result = -1;
                } else {
                    result = 0;
                }
                return result;
            }
        });
    }

    public String getOriginPrice() {
        return originPrice;
    }

    public void setOriginPrice(String originPrice) {
        this.originPrice = originPrice;
    }

    public String getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(String currentPrice) {
        this.currentPrice = currentPrice;
    }

    public String getProductScore() {
        return productScore;
    }

    public void setProductScore(String productScore) {
        this.productScore = productScore;
    }
}
