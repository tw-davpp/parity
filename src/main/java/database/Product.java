package database;

public class Product {
    public int id;
    public String title;
    public String url;
    public float originPrice;
    public float currentPrice;
    public float score;

    public Product(int id, String title, String url, String originPrice, String currentPrice, String score) {
        this.id = id;
        this.title = title;
        this.url = url;
        this.originPrice = Float.parseFloat(originPrice);
        this.currentPrice = Float.parseFloat(currentPrice);
        this.score = Float.parseFloat(score);
    }
}
