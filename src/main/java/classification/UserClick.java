package classification;

import database.JDBCConnect;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class UserClick extends BaseInstance {

    UserQuery userQuery;
    String url;

    public UserClick() {
        super();
    }

    public UserClick(UserQuery userQuery, String url) {
        super();

        this.userQuery = userQuery;
        this.setConcept(new BaseConcept(url));

        attributes = new StringAttribute[this.userQuery.getQueryTerms().length + 1];
        attributes[0] = new StringAttribute("UserName", this.userQuery.getUserId());

        int j = 1;
        for (String query : userQuery.getQueryTerms()) {
            attributes[j] = new StringAttribute("QueryTerm_" + j, query);
            j++;
        }
    }

    @Override
    public UserClick[] load(BufferedReader bR) throws IOException {
        ArrayList<UserClick> userClicks = new ArrayList<UserClick>();

        String line;
        boolean hasMoreLines = true;

        while (hasMoreLines) {
            line = bR.readLine();
            if (line == null) {
                hasMoreLines = false;
            } else {
                String[] data = line.split(",");
                UserQuery uQ = new UserQuery(data[0], data[1]);
                UserClick userClick = new UserClick(uQ, data[2].substring(1, data[2].length() - 1));
                userClick.print();
                userClicks.add(userClick);
            }
        }

        return userClicks.toArray(new UserClick[userClicks.size()]);
    }

    public UserClick[] load() {
        ArrayList<UserClick> userClicks = new ArrayList<UserClick>();

        JDBCConnect jdbcConnect = JDBCConnect.getInstance();
        Connection connection = jdbcConnect.getConnection();

        String sql = "select * from click";

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                String user = resultSet.getString("user");
                String queryStr = resultSet.getString("query");
                String url = resultSet.getString("url");
                UserQuery uQ = new UserQuery(user, queryStr);
                UserClick userClick = new UserClick(uQ, url.substring(1, url.length() - 1));
                userClick.print();
                userClicks.add(userClick);
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return userClicks.toArray(new UserClick[userClicks.size()]);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getUrl() == null) ? 0 : getUrl().hashCode());
        result = prime * result
                + ((userQuery == null) ? 0 : userQuery.hashCode());
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
        final UserClick other = (UserClick) obj;
        if (getUrl() == null) {
            if (other.getUrl() != null)
                return false;
        } else if (!getUrl().equals(other.getUrl()))
            return false;
        if (userQuery == null) {
            if (other.userQuery != null)
                return false;
        } else if (!userQuery.equals(other.userQuery))
            return false;
        return true;
    }

    public String getUrl() {
        return getConcept().getName();
    }

    public UserQuery getUserQuery() {
        return userQuery;
    }


}

