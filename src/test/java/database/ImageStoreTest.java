package database;

import org.junit.Test;

public class ImageStoreTest {
    String url = "http://ec4.images-amazon.com/images/I/31lvV7ujZiL._AA160_.jpg";

    @Test
    public void test_store_image() {
        ImageStore imageStore = new ImageStore(url);
        String sql = "select * from image where name = 'name2'";
        int imageSite = 2;
        imageStore.storeImage(sql, imageSite);
    }
}
