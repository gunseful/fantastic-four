<%@ page import="java.util.List" %>

<%@ page import="app.entities.Order" %>
<%@ page import="app.model.Model" %>
<%@ page import="app.entities.User" %>
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
        <i class="fas fa-dragon w3-jumbo" onclick="location.href='/'"
           style="font-size:60px;color:white;text-shadow:2px 2px 4px #000000;"></i><i class="normals"> W</i><i
            class="normal">hite </i><i class="normals"> D</i><i class="normal">ragon</i>
    </div>
</div>
<%--Выводит текущего пользователя, если админ еще добавляет кое что--%>
<div>
    <%
        User currentUser = (User) session.getAttribute("user");
        out.println("<p class=\"ex1\" style=\"font-size:15px;\">Вы вошли как " + currentUser.getNickname() + "</p>");
        if (currentUser.isAdministrator()) {
            out.println("<p class=\"ex1\" style=\"font-size:15px;\">Вы Администратор</p>");
            out.println("<p class=\"ex1\" style=\"font-size:12px;\">Вы можете удалять заказы и блокировать неплатильщиков</p>");
        }
    %>
</div>
<%--кнопка к магазину--%>
<div>
    <div>
        <button class="w3-button w3-cyan w3-padding-large w3-large w3-hover-opacity-off btn-block"
                onclick="location.href='/listClient'">К магазину
        </button>
        <%
            //если юзер админ, то еще и кнопка черного листа
            if (currentUser.isAdministrator()) {
                out.println("<button class=\"w3-button w3-black w3-padding-large w3-large w3-hover-opacity-off btn-block\" onclick=\"location.href='/blacklist'\" name=\"blacklist\" type=\"submit\" value=\"blacklist\">Black List</button>\n");
            }
        %>
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
        <%
            if (request.getAttribute("nullData") != null) {
                out.println("<p style=\"font-size:15px;\">Вы ничего не выбрали</p>");
            }
        %>
    </div>
<%--    здесь выводятся заказы, если админ то все заказы, если юзер - только его--%>
    <div>
        <form name="input" method="post">
            <%
                List<Order> orders = (List<Order>) request.getAttribute("orders");
                if (orders != null && !orders.isEmpty()) {
                    if (!currentUser.isAdministrator()) {
                        out.println("<p>Ваши заказы:</p>");
                    }
                    {
                        out.println("<p>Заказы:</p>");
                    }
                    for (Order order : orders) {
                        if (currentUser.isAdministrator()) {
                            User user = Model.getInstance().getUser(order.getCustomerID());
                            //проверяет есть ли юзер (чей заказ) в блек листе, если да то красным выводит что уже заблочен
                            if (!Model.getInstance().checkBlackList(user)) {
                                out.println("<p><div style=\" border: 6px solid green;  width:max-content;\"><h3>Клиент - " + user.getNickname() + "</h3></div>" + "<p><button class=\"w3-button w3-red btn-block\" name=\"block\" value=" + user.getId() + ">Block this Client</button>\n<br>");
                            } else {
                                //если нет, то появляется возможность его добавить в черный список
                                out.println("<p><div style=\" border: 8px solid green;  width:max-content;\"><h3>Клиент - " + user.getNickname() + "</h3></div>" + "<p><div style=\"background-color:red; width: max-content;\"><h3>THIS CLIENT WAS BLOCKED</h3></div>");
                            }
                        }
                        //прост разделитель, чтобы заказы удобнее было смотреть
                        out.println("<input type=\"checkbox\" name = \"orders\" value=\"" + order.getId() + "\">" + order.toString() + "_____________________________________________<br>");
                    }
                    //кнопка для Админа и для Клиента - удалить заказ
                    out.println("<input class=\"w3-button w3-red \" onclick=\"location.href='/'\" type=\"submit\" value=\"Удалить заказ\">");
                    if (!currentUser.isAdministrator()) {
                        //кнопка для Клиента - оплатить заказ
                        out.println("<input class=\"w3-button w3-black \" onclick=\"location.href='/'\" name=\"Pay\" type=\"submit\" value=\"Pay\" value=\"Оплатить\">");
                    }
                        //если нет заказов, выводит что заказов нет
                } else out.println("<p>У вас пока нет ни одного заказа</p>");
            %>
        </form>
    </div>
</div>
</body>
</html>