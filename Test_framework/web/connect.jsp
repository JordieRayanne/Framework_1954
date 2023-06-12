
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<% String user = String.valueOf(request.getAttribute("user")); %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>connecter:<%= user %> <h1>
        <br>

    </body>
</html>
