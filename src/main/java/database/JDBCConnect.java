package database;

import util.PropertiesUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class JDBCConnect {
    private static final String DATABASE_PROPERTIES = "database.properties";
    private static final String DRIVER = "com.mysql.jdbc.Driver";
    private static Connection connection;
    private static JDBCConnect jdbcConnect = null;

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
        Properties config = PropertiesUtils.getProperties(DATABASE_PROPERTIES);
        String url = config.getProperty("mysql.url");
        String user = config.getProperty("mysql.user");
        String password = config.getProperty("mysql.password");

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
