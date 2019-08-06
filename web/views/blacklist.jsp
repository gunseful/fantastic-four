<%@ page import="java.util.List" %>
<%@ page import="app.entities.User" %>
<%@ page import="app.entities.Product" %>
<%@ page import="java.util.Base64" %>
<%@ page import="app.entities.Basket" %>
<%@ page import="app.model.Model" %>
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
    <title>Черный список</title>
</head>


<div>
    <div class="ex1 w3-container w3-dark-gray w3-opacity w3-center-align">

        <i class="fas fa-dragon w3-jumbo" onclick="location.href='/'" style="font-size:60px;color:white;text-shadow:2px 2px 4px #000000;"></i><i class="normals"> W</i><i class="normal">hite </i><i class="normals"> D</i><i class="normal">ragon</i>


    </div>
</div>



<div>
    <%
        if (request.getAttribute("loggin") != null) {
            out.println("<p class=\"ex1\" style=\"font-size:15px;\">Вы вошли как "+request.getAttribute("loggin")+"</p>");
        }
    %>
</div>

<div>
    <button class="w3-button w3-cyan w3-padding-large w3-large w3-hover-opacity-off btn-block" onclick="location.href='/listAdmin'">К магазину</button>
</div>

<div>
    <form method="post" accept-charset="ISO-8859-1">
        <button class="w3-button w3-white w3-padding-large w3-large w3-opacity w3-hover-opacity-off btn-block" name="exit" type="submit" value="exit">Выйти</button>
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
                List<User> users = (List<User>) request.getAttribute("blacklist");
                System.out.println(users);
                if (users != null && !users.isEmpty()) {
                    out.println("<p>Ваша корзина:</p>");
                    for (User user : users) {
                        out.println("<input type=\"checkbox\" name = \"userForDelete\" value=\""+user.getId()+"\">"+user.getName()+"<br>");
                    }

                    out.println("<input class=\"w3-button w3-green \" onclick=\"location.href='/'\" type=\"submit\" value=\"Удалить из черного списка\">");





                } else out.println("<p>Черный список пуст</p>");
            %>

        </form>



</div>


</div>

<div>

</div>




</body>
</html>