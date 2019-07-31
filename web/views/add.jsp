<!DOCTYPE html>
<%@ page contentType="text/html;charset=utf-8" %>
<html lang="ru">
<head> <link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">>
    <meta charset="UTF-8">
    <title>Добавление в друзья</title>
</head>

<body class="w3-light-grey">
<!-- header -->
<div class="w3-container w3-blue-grey w3-opacity w3-center-align">
    <h1>Так ну чо, расскажи о себе!</h1>
</div>

        <div>
        <%
                if (request.getAttribute("name") != null) {
                    out.println("<p>Кентишка '" + request.getAttribute("name") + "' добавлен!</p>");
                }
            %>
            <div>

        <form method="post">
            <label>Имя:
                <input type="text" name="name"><br/>
            </label>

            <label>Размер хуя:
                <input type="number" name="dick"><br/>
            </label>
            <button class="w3-button w3-white w3-padding-large w3-large w3-opacity w3-hover-opacity-off" type="submit">Добавится в друганы</button>
        </form>

                <div>
                    <button class="w3-button w3-white w3-padding-large w3-large w3-opacity w3-hover-opacity-off" onclick="location.href='/'">вернуца</button>
                </div>


</body>
</html>