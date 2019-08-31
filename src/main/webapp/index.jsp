<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<html lang="ru">
<head>
    <link rel="stylesheet" href="<c:url value="/views/css/style.css"/>" type="text/css">
    <link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.7.0/css/all.css"
          integrity="sha384-lZN37f5QGtY3VHgisS14W3ExzMWZxybE1SJSEsQp9S+oqd12jhcu+A56Ebc1zFSJ" crossorigin="anonymous">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>White Dragon</title>
</head>

<body class="w3-light-gray">




<div class="ex1 w3-container w3-dark-gray w3-opacity w3-center-align">

    <div>
        <form action="LangServlet" method="post">
            Ru <input type="radio" name="lang" value="ru">
            En <input type="radio" name="lang" value="en">
            Fr <input type="radio" name="lang" value="fr">
            <input type="hidden" name="jspname" value="/" />
            <input class="w3-button w3-dark-gray" type="submit" value="${bundle.getObject("change.language")}">
        </form>
    </div>

    <i class="fas fa-dragon w3-jumbo" onclick="location.href='/'"
       style="font-size:60px;color:white;text-shadow:2px 2px 4px #000000;"></i><i class="normals"> W</i><i
        class="normal">hite </i><i class="normals"> D</i><i class="normal">ragon</i>

</div>



    <div>
        <div>
            <button class="w3-button w3-white w3-padding-large w3-large w3-opacity w3-hover-opacity-off btn-block"
                    onclick="location.href='/loggin'">${bundle.getObject("log.in")}
            </button>
            <button class="w3-button w3-white w3-padding-large w3-large w3-opacity w3-hover-opacity-off btn-block"
                    onclick="location.href='/registration'">${bundle.getObject("registration")}
            </button>
        </div>
    </div>
    <div class="ex2">
            ${bundle.getObject("welcome")}
    </div>







</body>
</html>