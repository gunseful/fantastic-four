<%@ page import="java.util.List" %>
<%@ page import="app.entities.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="ru">
<head> <link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">>
    <meta charset="UTF-8">
    <title>Список пацанов Илюхи</title>
</head>

<body class="w3-light-grey">
<div class="w3-container w3-blue-grey w3-opacity w3-center-align">
    <h1>Эт самые крутые челы в мире:</h1>
</div>

    <form name="input" method="post">
    <%
        List<User> pacani = (List<User>) request.getAttribute("pacani");
        if (pacani != null && !pacani.isEmpty()) {
            for (User user : pacani) {
                out.println("<input type=\"checkbox\" name = \"friend\" value=\""+user.getId()+"\">"+user.getName()+"<br>");
            }

            out.println("<input class=\"w3-button w3-white w3-padding-large w3-large w3-opacity w3-hover-opacity-off\" onclick=\"location.href='/'\" type=\"submit\" value=\"DELETE\">");
        } else out.println("<p>There are no users yet!</p>");
    %>
    </form></div>


</body>
</html>