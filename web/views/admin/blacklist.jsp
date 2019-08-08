<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="java.util.List" %>
<%@ page import="app.entities.user.User" %>
<%@ page import="app.entities.products.Product" %>
<%@ page import="java.util.Base64" %>
<%@ page import="app.entities.products.Basket" %>
<%@ page import="app.model.Model" %>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" language="java" %>
<html lang="ru">
<head>
    <style>

        div.ex1 {
            width: 30%;
            padding-top: 5px;
        }
        div.ex2 {
            padding-left: 30%
        }
        #clickme {
            background-color: #4CAF50; /* Green */
            border: none;
            color: white;
            padding: 15px 32px;
            text-align: center;
            text-decoration: none;
            display: inline-block;
            font-size: 16px;
        }
        #hider {
            position: absolute;
            top: -9999px;
            left: -9999px;
        }
        #hider:checked + .content {
            display: block;
        }
        i.normal {
            font-style: normal;
            font-family: "Lucida Console";
            font-size: 20px;
        }
        i.normals {
            font-style: normal;
            font-family: "Lucida Console";
            font-size: 30px;
        }
        .content {
            margin-top: 10px;
            display: none;
        }
        p.ex1 {
            padding: 0px;
        }
    </style>
    <link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.7.0/css/all.css"
          integrity="sha384-lZN37f5QGtY3VHgisS14W3ExzMWZxybE1SJSEsQp9S+oqd12jhcu+A56Ebc1zFSJ" crossorigin="anonymous">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta charset="UTF-8">
    <title>Черный список</title>
</head>
<%--прост шапка сайта, везде одинковая--%>
<div>
    <div class="ex1 w3-container w3-dark-gray w3-opacity w3-center-align">
        <i class="fas fa-dragon w3-jumbo" onclick="location.href='../..'"
           style="font-size:60px;color:white;text-shadow:2px 2px 4px #000000;"></i><i class="normals"> W</i><i
            class="normal">hite </i><i class="normals"> D</i><i class="normal">ragon</i>
    </div>
</div>
<%--Здесь мы берем из сессии юзера и выводим строчку - вы пошли как nickname--%>
<p class="ex1" style="font-size:15px;">Вы вошли как ${user.getNickname()} </p>
<p class="ex1" style="font-size:15px;">Вы Администратор</p>
<p class="ex1" style="font-size:12px;">Вы можете разблокировать кого-нибудь из пользователей</p>
<%--просто кнопка "к магазину", которая переносит на стандартную страницу клиентлиста--%>
<div>
    <button class="w3-button w3-cyan w3-padding-large w3-large w3-hover-opacity-off btn-block"
            onclick="location.href='/listAdmin'">К магазину
    </button>
</div>
<%--просто кнопка "Заказы", которая переносит на стандартную страницу заказов--%>
<button class="w3-button w3-light-green w3-padding-large w3-large w3-hover-opacity-off btn-block"
        onclick="location.href='/orders'" name="Orders" type="submit" value="Orders">Заказы
</button>
<%--кнопка вызывает сервлет выхода и системы, обнуляет сессию и переносим на хоумпейдж--%>
<div>
    <form action="LogoutServlet" method="post">
        <button class="w3-button w3-white w3-padding-large w3-large w3-opacity w3-hover-opacity-off btn-block"
                type="submit" value="logout">Выйти
        </button>
    </form>
</div>
<%--если админ ошибся, ему подсказка--%>
<div class="ex2">
    <div>
        <c:if test="${nullData != null}">
            <p style="font-size:15px;">Вы ничего не выбрали</p>
        </c:if>
    </div>
    <%--    большой блок где от сервлета приходит черный список, он и выводится--%>
    <div>
        <form name="input" method="post">
            <c:if test="${!blacklist.isEmpty()}">
                <p>Черный список:</p>
                <c:forEach var="user" items="${blacklist}">
                    <input type="checkbox" name="userForDelete" value=" ${user.getId()} ">${user.getName()}<br>
                </c:forEach>
                <input class="w3-button w3-red " onclick="location.href='../..'" type="submit"
                       value="Удалить из черного списка">
            </c:if>
            <c:if test="${blacklist.isEmpty()}">
                <p>Черный список пуст.</p>
            </c:if>
        </form>
    </div>
</div>
<div>
</div>
</body>
</html>