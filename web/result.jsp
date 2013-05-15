<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="lucene.*" %>
<%@ page import="java.util.List" %>
<%@ page import="database.*" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="java.sql.PreparedStatement" %>
<%@ page import="java.sql.SQLException" %>

<%
    request.setCharacterEncoding("utf-8");

    String user = (String) session.getAttribute("user");
    String input = (String) request.getParameter("input");
%>
<html>
<head>
    <title>Result</title>
    <link href="views/css/jquery-ui-1.10.3.custom.min.css" rel="stylesheet">
    <script src="views/js/jquery-1.9.1.min.js"></script>
    <script src="views/js/jquery-ui-1.10.3.custom.min.js"></script>

    <link rel="stylesheet" href="views/css/main.css">
    <link rel="stylesheet" href="views/css/result.css">

    <script type="text/javascript">
        $(function () {
            $("#button").button();
            $(document).tooltip();

            $(".image").bind("click", function () {
                document.getElementById("input").value = "<%=input%>";
                document.getElementById("url").value = $(this).attr("link");
                var formId = document.getElementById("click");
                formId.submit();
            });
        });
    </script>
</head>
<body>
<div class="header">
    <label class="user">
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
    Search search = new Search();
    List<Product> products = search.searchWithNaiveBayes(input);
    for (Product product : products) {
%>
<div class="product">
    <img class="image" src="img/<%=product.id%>.jpg" link="<%=product.url%>"
         title="<%=product.title%>" style style="text-align: center"/>


    <p class="name"><%=product.title%>
    </p>

    <p class="price">origin:￥<%=product.originPrice%>
    </p>

    <p class="price">new:￥<%=product.currentPrice%>
    </p>
</div>
<%
    }
%>

<form id="click" method="post" action="saveClick.jsp">
    <input id="input" type="hidden" name="input">
    <input id="url" type="hidden" name="url">
</form>
</body>
</html>