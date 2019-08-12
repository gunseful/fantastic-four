<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" language="java" %>
<html lang="ru">
<head>
    <link rel="stylesheet" href="views/css/style.css" type="text/css">
    <link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.7.0/css/all.css"
          integrity="sha384-lZN37f5QGtY3VHgisS14W3ExzMWZxybE1SJSEsQp9S+oqd12jhcu+A56Ebc1zFSJ" crossorigin="anonymous">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta charset="UTF-8">
    <title>${bundle.getObject("blacklist.title")}</title>
</head>
<%--прост шапка сайта, везде одинковая--%>
<div>
    <div class="ex1 w3-container w3-dark-gray w3-opacity w3-center-align">
        <i class="fas fa-dragon w3-jumbo" onclick="location.href='/'"
           style="font-size:60px;color:white;text-shadow:2px 2px 4px #000000;"></i><i class="normals"> W</i><i
            class="normal">hite </i><i class="normals"> D</i><i class="normal">ragon</i>
    </div>
</div>
<%--Здесь мы берем из сессии юзера и выводим строчку - вы пошли как nickname--%>
<p class="ex1" style="font-size:15px;">${bundle.getObject("entry")} ${user.getNickname()} </p>
<p class="ex1" style="font-size:15px;">${bundle.getObject("admin")} </p>
<p class="ex1" style="font-size:12px;">${bundle.getObject("blacklist.hint")}</p>
<%--просто кнопка "к магазину", которая переносит на стандартную страницу клиентлиста--%>
<div>
    <button class="w3-button w3-cyan w3-padding-large w3-large w3-hover-opacity-off btn-block"
            onclick="location.href='/listAdmin'">${bundle.getObject("tothestore")}
    </button>
</div>
<%--просто кнопка "Заказы", которая переносит на стандартную страницу заказов--%>
<button class="w3-button w3-light-green w3-padding-large w3-large w3-hover-opacity-off btn-block"
        onclick="location.href='/orders'" name="Orders" type="submit" value="Orders">${bundle.getObject("orders")}
</button>
<%--кнопка вызывает сервлет выхода и системы, обнуляет сессию и переносим на хоумпейдж--%>
<div>
    <form action="LogoutServlet" method="post">
        <button class="w3-button w3-white w3-padding-large w3-large w3-opacity w3-hover-opacity-off btn-block"
                type="submit" value="logout">${bundle.getObject("out")}
        </button>
    </form>
</div>
<%--если админ ошибся, ему подсказка--%>
<div class="ex2">
    <div>
        <c:if test="${nullData != null}">
            <p style="font-size:15px;">${bundle.getObject("nulldata")}</p>
        </c:if>
    </div>
    <%--    большой блок где от сервлета приходит черный список, он и выводится--%>
    <div>
        <form name="input" method="post">
            <c:if test="${!blacklist.isEmpty()}">
                <p>${bundle.getObject("blacklist.title")}</p>
                <c:forEach var="user" items="${blacklist}">
                    <input type="checkbox" name="userForDelete" value=" ${user.getId()} ">${user.getNickname()}<br>
                </c:forEach>
                <input class="w3-button w3-red " onclick="location.href='../../../../../web'" type="submit"
                       value="${bundle.getObject("blacklist.delete")}">
            </c:if>
            <c:if test="${blacklist.isEmpty()}">
                <p>${bundle.getObject("blacklist.empty")}</p>
            </c:if>
        </form>
    </div>
</div>
<div>
</div>
</body>
</html>