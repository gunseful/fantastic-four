<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="java.util.List" %>
<%@ page import="app.entities.products.Order" %>
<%@ page import="app.model.Model" %>
<%@ page import="app.entities.user.User" %>
<%@ page import="org.h2.engine.Mode" %>
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
            background-color: #000000; /* Green */
            border: none;
            color: white;
            padding: 15px 32px;
            text-align: center;
            text-decoration: none;
            display: inline-block;
            font-size: 16px;
        }

        #clickmeUser {
            background-color: #4CAF50; /* Green */
            border: none;
            color: white;
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
    <title>Заказы</title>
</head>
<%--станадртная шапка--%>
<div>
    <div class="ex1 w3-container w3-dark-gray w3-opacity w3-center-align">
        <i class="fas fa-dragon w3-jumbo" onclick="location.href='../..'"
           style="font-size:60px;color:white;text-shadow:2px 2px 4px #000000;"></i><i class="normals"> W</i><i
            class="normal">hite </i><i class="normals"> D</i><i class="normal">ragon</i>
    </div>
</div>
<%--Выводит текущего пользователя, если админ еще добавляет кое что--%>
<div>
    <p class="ex1" style="font-size:15px;">Вы вошли как ${user.getNickname()} </p>
    <form name="input" method="post">
        <c:if test="${user.isAdministrator()}">
            <p class="ex1" style="font-size:15px;">Вы Администратор</p>
            <p class="ex1" style="font-size:12px;">Вы можете удалять заказы и блокировать неплатильщиков</p>
        </c:if>
    </form>
</div>
<%--кнопка к магазину--%>
<div>
    <div>
        <button class="w3-button w3-cyan w3-padding-large w3-large w3-hover-opacity-off btn-block"
                onclick="location.href='/listClient'">К магазину
        </button>
            <div>
                <c:if test="${user.isAdministrator()}">
                <button class="w3-button w3-black w3-padding-large w3-large w3-hover-opacity-off btn-block"
                        onclick="location.href='/blacklist'">Black List
                </button>
                </c:if>
            </div>
    </div>
</div>
<%--ну по классике выход--%>
<div>
    <form action="LogoutServlet" method="post">
        <button class="w3-button w3-white w3-padding-large w3-large w3-opacity w3-hover-opacity-off btn-block"
                type="submit" value="logout">Выйти
        </button>
    </form>
</div>
<%--подсказка для пользователя если ничего не выбрано--%>
<div class="ex2">
    <div>
        <c:if test="${nullData != null}">
            <p style="font-size:15px;">Вы ничего не выбрали</p>
        </c:if>
    </div>
    <%--    здесь выводятся заказы, если админ то все заказы, если юзер - только его--%>
    <div>
        <form name="input" method="post">
            <c:if test="${!orders.isEmpty()}">
                <c:if test="${!user.isAdministrator()}">
                    <p>Ваши заказы:</p>
                </c:if>
                <c:if test="${user.isAdministrator()}">
                    <p>Заказы:</p>
                </c:if>
                <c:forEach var="order" items="${orders}">
                <c:if test="${user.isAdministrator()}">
                <c:if test="${!order.getUser().isInBlackList()}">
                <p><div style=" border: 6px solid green;  width:max-content;"><h3>Клиент - ${order.getUser().getNickname()} </h3></div><button class="w3-button w3-red btn-block" name="block" value="${order.getUser().getId()}">Block this Client</button><br>
                </c:if>
                <c:if test="${order.getUser().isInBlackList()}">
                <p><div style=" border: 8px solid green;  width:max-content;"><h3>Клиент - ${order.getUser().getNickname()}</h3></div><p><div style="background-color:red; width: max-content;"><h3>THIS CLIENT WAS BLOCKED</h3></div>
                </c:if>
                </c:if>
                    <input type="checkbox" name = "orders" value="${order.getId()}"> ${order.toString()} _____________________________________________<br>
                </c:forEach>
                <input class="w3-button w3-red " type="submit" value="Удалить заказ">
                <c:if test="${!user.isAdministrator()}">
                    <input class="w3-button w3-black " name="Pay" type="submit" value="Pay" >
                </c:if>
            </c:if>
            <c:if test="${orders.isEmpty()}">
                <p>Заказов нет</p>
            </c:if>
        </form>
    </div>
</div>
</body>
</html>