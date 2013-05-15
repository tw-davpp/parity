package database;

import util.PropertiesUtils;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class JDBCConnect {
    private static final String DATABASE_PROPERTIES = "database.properties";
    private static final String DRIVER = "com.mysql.jdbc.Driver";
    private static Connection connection;
    private static JDBCConnect jdbcConnect = null;
    private static String url = "jdbc:mysql://127.0.0.1:3306/crawler?useUnicode=true&characterEncoding=utf8";
    private static String user = "root";
    private static String password = "cn123456";

    public static JDBCConnect getInstance() {
        if (jdbcConnect == null) {
            jdbcConnect = new JDBCConnect();
        }
        return jdbcConnect;
    }

    private JDBCConnect() {
        init();
    }

    public Connection getConnection() {
        return connection;
    }

    private static void init() {
        File file = new File(DATABASE_PROPERTIES);
        if (file.exists()) {
            Properties config = PropertiesUtils.getProperties(DATABASE_PROPERTIES);
            url = config.getProperty("mysql.url");
            user = config.getProperty("mysql.user");
            password = config.getProperty("mysql.password");
        }

        try {
            Class.forName(DRIVER);
            connection = DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        JDBCConnect jdbcConnect = JDBCConnect.getInstance();
    }

}
