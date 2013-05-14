package database;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class ImageDownload {
    public static String download(String imageUrl) throws IOException {
        InputStream inputStream = connectImageUrl(imageUrl);

        String imageFileName = getImageFileName(imageUrl);
        File file = new File(imageFileName);

        FileOutputStream outputStream = new FileOutputStream(file);
        int index = 0;
        while ((index = inputStream.read()) != -1) {
            outputStream.write(index);
        }
        inputStream.close();

        return imageFileName;
    }

    private static InputStream connectImageUrl(String imageUrl) throws IOException {
        URL url = new URL(imageUrl);
        URLConnection urlConnection = url.openConnection();
        return urlConnection.getInputStream();
    }

    private static String getImageFileName(String imageUrl) {
        String imageFileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
        return imageFileName;
    }

    public static void main(String[] args) throws IOException {
        ImageDownload.download("http://ec4.images-amazon.com/images/I/31lvV7ujZiL._AA160_.jpg");
    }
}
