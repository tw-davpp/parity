<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.sql.*" %>
<%@ page import="database.JDBCConnect" %>
<html>
<head>
    <title>parity</title>
    <link href="views/css/jquery-ui-1.10.3.custom.min.css" rel="stylesheet">
    <script src="views/js/jquery-1.9.1.min.js"></script>
    <script src="views/js/jquery-ui-1.10.3.custom.min.js"></script>

    <script type="text/javascript" src="views/js/register.js"></script>
    <link rel="stylesheet" href="views/css/main.css">
    <link rel="stylesheet" href="views/css/register.css">

</head>
<body>
<%
    JDBCConnect jdbcConnect = JDBCConnect.getInstance();
    Connection connection = jdbcConnect.getConnection();

    String name = (String) request.getParameter("name");
    String password = (String) request.getParameter("password");
    String sql = "select * from user where name='" + name + "'";

    Statement statement = connection.createStatement();
    ResultSet resultSet = statement.executeQuery(sql);

    try {
        if (resultSet.next()) {
            if (password.equals(resultSet.getString("password"))) {
                session.setAttribute("user", name);
%>
<jsp:forward page="index.jsp">
    <jsp:param name="user" value="<%=name%>"/>
</jsp:forward>
<%
            } else
                session.setAttribute("user", null);
        }
    } catch (StringIndexOutOfBoundsException e) {
        System.out.println();
    }
%>

<a href="index.jsp" style="text-align: center">back</a>

</body>
</html>