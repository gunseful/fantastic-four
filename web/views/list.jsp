<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="ru">
<head> <link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">>
    <meta charset="UTF-8">
    <title>Список пацанов Илюхи</title>
</head>

<body class="w3-light-grey">
<!-- header -->
<div class="w3-container w3-blue-grey w3-opacity w3-center-align">
    <h1>Эт самые крутые челы в мире:</h1>
</div>

<ul>
    <%
        List<String> pacani = (List<String>) request.getAttribute("pacani");
        if (pacani != null && !pacani.isEmpty()) {
            out.println("<ui>");
            for (String s : pacani) {
                out.println("<li>" + s + "</li>");
            }
            out.println("</ui>");
        } else out.println("<p>There are no users yet!</p>");
    %>
</ul>


</body>
</html>