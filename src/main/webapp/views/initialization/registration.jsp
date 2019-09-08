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
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>${bundle.getObject("registration")}</title>
</head>
<body class="w3-light-grey">

<%--top if the site--%>
<div class="ex1 w3-container w3-dark-gray w3-opacity w3-center-align">
    <form action="LangServlet" method="post">
        Ru <input type="radio" name="lang" value="ru">
        En <input type="radio" name="lang" value="en">
        Fr <input type="radio" name="lang" value="fr">
        <input type="hidden" name="jspname" value="/registration"/>
        <input class="w3-button w3-dark-gray w3-padding-small" type="submit" value="select">
    </form>
    <i class="fas fa-dragon w3-jumbo" onclick="location.href='/'"
       style="font-size:60px;color:white;text-shadow:2px 2px 4px #000000;"></i><i class="normals"> W</i><i
        class="normal">hite </i><i class="normals"> D</i><i class="normal">ragon</i>
</div>

<div>
    <div>
        <c:if test="${fail != null}">
        <p style="font-size:15px;">${bundle.getObject("registration.fail")}
            </c:if>
    </div>
</div>

<div>
    <div>
        <c:if test="${alreadyExist != null}">
        <p style="font-size:15px;">${alreadyExist}${bundle.getObject("registration.already.exist")}
            </c:if>
    </div>
</div>

<div>
    <form method="post" accept-charset="ISO-8859-1">
        <label>
            <p style="font-size:15px;">${bundle.getObject("name")}</p> <input type="text" name="name" value=""><br/>
        </label>
        <label>
            <p style="font-size:15px;">${bundle.getObject("nickname")}</p> <input type="text" name="nickname"
                                                                                  value=""><br/>
        </label>
        <label>
            <p style="font-size:15px;">${bundle.getObject("password")}</p> <input type="password" name="password"
                                                                                  value=""><br/>
        </label>
        <button class="w3-button w3-white w3-padding-large w3-large w3-opacity w3-hover-opacity-off btn-block"
                type="submit">${bundle.getObject("registration")}</button>
    </form>
</div>
</body>
</html>