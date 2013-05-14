package database;

import java.sql.*;

public class ProductInfoStore {
    private final String selectSql = "select * from product";
    private final String insertSql = "insert into productInfo value(?,?,?,?,?,?)";

    public ProductInfoStore() throws SQLException {
        JDBCConnect jdbcConnect = JDBCConnect.getInstance();
        Connection connection = jdbcConnect.getConnection();

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(selectSql);

        try {
            while (resultSet.next()) {
                PreparedStatement preparedStatement = connection.prepareStatement(insertSql);
                preparedStatement.setInt(1, resultSet.getInt("id"));
                preparedStatement.setString(2, resultSet.getString("title"));
                preparedStatement.setString(3, resultSet.getString("url"));

                String[] prices = resultSet.getString("price").trim().split(" ");
                preparedStatement.setString(4, prices[0].substring(1));

                if (prices.length < 2 || prices[1].indexOf("￥") == -1)
                    preparedStatement.setString(5, prices[0].substring(1));
                else
                    preparedStatement.setString(5, prices[1].substring(1));

                String score = resultSet.getString("score");
                if (score.equals(""))
                    preparedStatement.setString(6, "0");
                else
                    preparedStatement.setString(6, score.substring(2, score.length() - 2));

                preparedStatement.executeUpdate();
            }
        } catch (StringIndexOutOfBoundsException e) {
            System.out.println();
        }
        resultSet.close();
    }

    public static void main(String[] args) throws SQLException {
        ProductInfoStore productInfoStore = new ProductInfoStore();
    }
}
