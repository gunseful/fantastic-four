<%@ page import="java.util.List" %>
<%@ page import="app.entities.User" %>
<%@ page import="app.entities.Product" %>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" language="java"%>
<html lang="ru">
<head>
    <style>

        div.ex1 {
            width: 30%;
            padding-top:5px;
        }

        div.ex2 {
            padding-left:30%
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
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.7.0/css/all.css" integrity="sha384-lZN37f5QGtY3VHgisS14W3ExzMWZxybE1SJSEsQp9S+oqd12jhcu+A56Ebc1zFSJ" crossorigin="anonymous">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta charset="UTF-8">
    <title>Список товаров магазина</title>
</head>


<div>
    <div class="ex1 w3-container w3-dark-gray w3-opacity w3-center-align">

        <i class="fas fa-dragon w3-jumbo" onclick="location.href='/'" style="font-size:60px;color:white;text-shadow:2px 2px 4px #000000;"></i><i class="normals"> W</i><i class="normal">hite </i><i class="normals"> D</i><i class="normal">ragon</i>


    </div>
</div>



<div>
    <%
            User user = (User)session.getAttribute("user");
            out.println("<p class=\"ex1\" style=\"font-size:15px;\">Вы вошли как "+user.getNickname()+"</p>");
            out.println("<p class=\"ex1\" style=\"font-size:15px;\">Вы Администратор</p>");
            out.println("<p class=\"ex1\" style=\"font-size:12px;\">Вы можете добавлять или удалять товар из магазина</p>");
    %>
</div>

<button class="w3-button w3-light-green w3-padding-large w3-large w3-hover-opacity-off btn-block" onclick="location.href='/orders'" name="Orders" type="submit" value="Orders">Заказы</button>
<button class="w3-button w3-black w3-padding-large w3-large w3-hover-opacity-off btn-block" onclick="location.href='/blacklist'" name="blacklist" type="submit" value="blacklist">Black List</button>


<div>
    <form action="LogoutServlet" method="post">
        <button class="w3-button w3-white w3-padding-large w3-large w3-opacity w3-hover-opacity-off btn-block" type="submit" value="logout">Выйти</button>
    </form>
</div>

<div class="ex2">
<div>
    <%
        if (request.getAttribute("nullData") != null) {
            out.println("<p style=\"font-size:15px;\">Вы ничего не выбрали</p>");
        }
    %>
</div>

<div>

    <form name="input" method="post">
        <%
            List<Product> products = (List<Product>) request.getAttribute("products");
            if (products != null && !products.isEmpty()) {
                out.println("<p>Список товаров нашего магазина:</p>");
                for (Product product : products) {
                    out.println("<input type=\"checkbox\" name = \"productForDelete\" value=\""+product.getId()+" "+product.getName()+"\">"+product.getName()+" - "+product.getPrice()+" тенге"+"<br>");
                }

                out.println("<input class=\"w3-button w3-red \" onclick=\"location.href='/'\" type=\"submit\" value=\"Удалить\">");

            } else out.println("<p>Товаров пока нет.</p>");
        %>

    </form>
</div>


<label class="link" for="hider" id="clickme">Добавить новый товар</label>
<input type="checkbox" id="hider">

<div class="content">
        <form method="post" accept-charset="ISO-8859-1">
            <label>
                <p style="font-size:13px;">Наименование:</p> <input type="text" name="name">
                <p style="font-size:13px;">Цена (в тенге):</p> <input type="number" name="price"><br/>
            </label>
            <br/>
            <button class="w3-button w3-green w3-padding-large" type="submit">Добавить</button>
        </form>
    </div>
</div>
</div>







<%--<%--%>
<%--    //allow access only if session exists--%>
<%--    String user = (String) session.getAttribute("user");--%>
<%--    String userName = null;--%>
<%--    String sessionID = null;--%>
<%--    Cookie[] cookies = request.getCookies();--%>
<%--    if(cookies !=null){--%>
<%--        for(Cookie cookie : cookies){--%>
<%--            if(cookie.getName().equals("user")) userName = cookie.getValue();--%>
<%--            if(cookie.getName().equals("JSESSIONID")) sessionID = cookie.getValue();--%>
<%--        }--%>
<%--    }--%>
<%--%>--%>
<%--<h3>Hi <%=userName %>, Login successful. Your Session ID=<%=sessionID %></h3>--%>
<%--<br>--%>
<%--User=<%=user %>--%>
<%--<br>--%>







</body>
</html>