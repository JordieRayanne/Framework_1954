<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Hello World!</h1>
        <form action="login.action" method="post" enctype="multipart/form-data">
            <input type="text" name="username">
            <input type="text" name="mdp">
            <input type="submit" value="Valider">
        </form>
        <br>

    </body>
</html>