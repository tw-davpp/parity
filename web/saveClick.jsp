<%@ page import="database.JDBCConnect" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="java.sql.PreparedStatement" %>
<%@ page import="java.sql.SQLException" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    request.setCharacterEncoding("utf-8");

    String user = (String) session.getAttribute("user");
    String input = (String) request.getParameter("input");
    String url = (String) request.getParameter("url");
    System.out.println(url);

    JDBCConnect jdbcConnect = JDBCConnect.getInstance();
    Connection connection = jdbcConnect.getConnection();

    String sql  = "insert into click values(null,?,?,?)";

    try {
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1,user);
        preparedStatement.setString(2,input);
        preparedStatement.setString(3,url);

        preparedStatement.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
    }
%>
<html>
<head>
    <title>Result</title>
</head>
<body>
</body>
</html>