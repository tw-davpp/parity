<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
<div class="search">
    <h1 class="title" style="text-align: center">Parity Login</h1>

    <form class="register" action="registerToDatabase.jsp" method="post" style="text-align: center">
        <label>User Name:</label>
        <input name="name" class="input" type="text" size="20"/>
        <br/>
        <label>Password:</label>
        <input name="password" class="input" type="password" size="20"/>

        <div class="submit">
            <button id="submit">Submit</button>
        </div>

        <div class="reset">
            <button id="reset">Reset</button>
        </div>
    </form>
</div>
</body>
</html>