package database;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ProductStore {
    public void store(String title, String url, String fileName, String price, String score) {
        JDBCConnect jdbcConnect = JDBCConnect.getInstance();
        Connection con = jdbcConnect.getConnection();
        String insertSql = "insert into product values(null,?,?,?,?,?)";
        PreparedStatement stmt = null;
        try {
            File file = new File(fileName);
            FileInputStream fin = new FileInputStream(file);

            stmt = con.prepareStatement(insertSql);
            stmt.setString(1, title);
            stmt.setString(2, url);
            stmt.setString(3, price);
            stmt.setString(4, score);
            stmt.setBinaryStream(5, fin);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
