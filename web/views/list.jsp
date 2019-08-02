<%@ page import="java.util.List" %>
<%@ page import="app.entities.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="ru">
<head>
    <style>
        div.ex1 {
            padding-top:5px;
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
    </style>
    <link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.7.0/css/all.css" integrity="sha384-lZN37f5QGtY3VHgisS14W3ExzMWZxybE1SJSEsQp9S+oqd12jhcu+A56Ebc1zFSJ" crossorigin="anonymous">
    <meta charset="UTF-8">
    <title>Список пацанов Илюхи</title>
</head>

<body class="w3-light-grey" >
<div>
    <i class="fa fa-spinner fa-spin w3-jumbo"></i>
    <i class="fa fa-home w3-jumbo" onclick="location.href='/'"></i>
</div>
<div>
    <div class="ex1 w3-container w3-dark-gray w3-opacity w3-center-align">

        <i class="fas fa-dragon w3-jumbo" style="font-size:60px;color:white;text-shadow:2px 2px 4px #000000;"></i><i class="normals"> W</i><i class="normal">hite </i><i class="normals"> D</i><i class="normal">ragon</i>

    </div>

    <div>

<div>
        <%
                if (request.getAttribute("NullPoint") != null) {
                    out.println("<p>" + request.getAttribute("NullPoint") + "!</p>");
                }
            %>
    <div>

<div>
        <%
                if (request.getAttribute("deleted") != null) {
                    String[] str = (String[]) request.getAttribute("deleted");
                    for(String s : str){
                        out.println("<p>Кентишка '" + s + "' удален:(</p>");
                    }

                }
            %>
    <div>

    <form name="input" method="post">
    <%
        List<User> pacani = (List<User>) request.getAttribute("pacani");
        if (pacani != null && !pacani.isEmpty()) {
            for (User user : pacani) {
                String age = "";
                int years = 0;
                if(user.getAge()<10){
                    years=user.getAge();
                }else{
                    years=user.getAge()%10;
                }
                if(years==1){
                    age="год";
                }else{
                    if(years>1 && years<5){
                        age="года";
                    }else{
                        age="лет";
                    }
                }
                out.println("<input type=\"checkbox\" name = \"friend\" value=\""+user.getId()+" "+user.getName()+"\">"+user.getName()+" - "+user.getAge()+" "+age+

                        "<br>");
            }

            out.println("<input class=\"w3-button w3-white w3-padding-large w3-large w3-opacity w3-hover-opacity-off btn-block\" onclick=\"location.href='/'\" type=\"submit\" value=\"Удалить\">");

        } else out.println("<p>У тебя совсем нет друзей:(</p>");
    %>

    </form>
    </div>




</body>
</html>