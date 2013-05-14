<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>parity</title>
    <link href="views/css/jquery-ui-1.10.3.custom.min.css" rel="stylesheet">
    <script src="views/js/jquery-1.9.1.min.js"></script>
    <script src="views/js/jquery-ui-1.10.3.custom.min.js"></script>

    <script type="text/javascript" src="views/js/main.js"></script>
    <link rel="stylesheet" href="views/css/main.css">
</head>
<body>
<div class="search">
    <h1 class="title" style="text-align: center">Parity</h1>

    <form action="result.jsp" method="post" style="text-align: center">
        <div class="input-table">
            <input name="input" class="input" type="text" size="50" />
        </div>
        <div class="submit">
            <button id="button">search</button>
        </div>
    </form>
</div>
</body>
</html>