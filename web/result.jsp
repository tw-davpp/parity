<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="lucene.*" %>
<%@ page import="java.util.List" %>
<%@ page import="database.*" %>

<% int clicks = 0; %>
<html>
<head>
    <title>Result</title>
    <link href="views/css/jquery-ui-1.10.3.custom.min.css" rel="stylesheet">
    <script src="views/js/jquery-1.9.1.min.js"></script>
    <script src="views/js/jquery-ui-1.10.3.custom.min.js"></script>

    <script type="text/javascript" src="views/js/result.js"></script>
    <link rel="stylesheet" href="views/css/main.css">
    <link rel="stylesheet" href="views/css/result.css">

    <script type="text/javascript">
        $(function () {
            var clicks = <%=clicks%>;
            $(".link").bind("click", function () {
                clicks++;
                alert(clicks);
            });
        });
    </script>
</head>
<body>
<div class="header">
    <label class="user">
        <%
            String user = (String) session.getAttribute("user");
            if (user != null)
        %>
        User:<%=user%>
    </label>
</div>

<form class="search-box">
    <label><h1 class="title">Parity</h1></label>
    <label>
        <div class="input-table">
            <input class="input" type="text" size="50">
        </div>
    </label>
    <label>
        <div class="submit">
            <button id="button">search</button>
        </div>
    </label>
</form>

<%
    request.setCharacterEncoding("utf-8");
    String input = (String)request.getParameter("input");
    Search search = new Search();
    List<Product> products = search.search(input);
    for (Product product : products) {
%>

<div class="product">
    <a class="link" href=<%=product.url%> >
        <img class="image" src="img/<%=product.id%>.jpg"
             title="<%=product.title%>" style style="text-align: center"/>
    </a>

    <p class="name"><%=product.title%></p>
    <p class="price">origin:￥<%=product.originPrice%></p>
    <p class="price">new:￥<%=product.currentPrice%></p>
</div>
<%
    }
%>

</body>
</html>