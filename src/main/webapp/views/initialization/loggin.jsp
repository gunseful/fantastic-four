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
    <title>${bundle.getObject("loggin.title")}</title>
</head>

<body class="w3-light-grey">
<%--стандартная шапка--%>
<div>
    <div class="ex1 w3-container w3-dark-gray w3-opacity w3-center-align">
        <i class="fas fa-dragon w3-jumbo" onclick="location.href='/'"
           style="font-size:60px;color:white;text-shadow:2px 2px 4px #000000;"></i><i class="normals"> W</i><i
            class="normal">hite </i><i class="normals"> D</i><i class="normal">ragon</i>
    </div>
</div>
<%--если в черном списке юзер введенный, то вылазит вот это--%>
<div>
    <c:if test="${inBlackList != null}">
        <p style="font-size:15px;">
        <p style="font-size:20px;">${bundle.getObject("user")} - ${inBlackList} ${bundle.getObject("inblacklist")}</p>
    </c:if>
</div>
<%--если ввели неправильно, то выводит это--%>
<div>
    <c:if test="${NoData != null}">
        <p style="font-size:15px;">${bundle.getObject("nodata")}</p>
    </c:if>
</div>
<%--форма ввода ника и пароля, и кнопка сабмит--%>
<div>
    <form method="post" accept-charset="ISO-8859-1">
        <label>
            <p style="font-size:15px;">${bundle.getObject("loggin")}</p> <input type="text" name="nickname"><br/>
        </label>
        <label>
            <p style="font-size:15px;">${bundle.getObject("password")}</p> <input type="password" name="password"><br/>
        </label>
        <button class="w3-button w3-white w3-padding-large w3-large w3-opacity w3-hover-opacity-off  btn-block"
                type="submit">${bundle.getObject("log.in")}
        </button>
    </form>
</div>
</body>
</html>