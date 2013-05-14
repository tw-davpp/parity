package database;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ImageSave {
    public void saveFromDatabase() {
        String sql = "select * from product";
        try {
            JDBCConnect jdbcConnect = JDBCConnect.getInstance();
            Connection connect = jdbcConnect.getConnection();

            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                int id = rs.getInt("id");
                saveImage(id, rs);
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveImage(int id, ResultSet rs) throws IOException, SQLException {
        File file = new File("./img/" + id + ".jpg");
        if(!file.exists())
            file.createNewFile();
        InputStream img = rs.getBlob("img").getBinaryStream();
        BufferedImage bufferedImage = ImageIO.read(img);
        ImageIO.write(bufferedImage, "jpg", file);
    }

    public static void main(String[] args) {
        ImageSave imageSave = new ImageSave();
        imageSave.saveFromDatabase();

    }
}
