<!DOCTYPE html>
<%@ page contentType="text/html;charset=utf-8" %>
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
    <title>Добавление в друзья</title>
</head>

<body class="w3-light-grey">
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
                if (request.getAttribute("NoData") != null) {
                    out.println("<p>Не написано имя либо возраст</p>");
                }
            %>
    <div>


        <div>
        <%
                if (request.getAttribute("name") != null) {
                    out.println("<p>Кентишка '" + request.getAttribute("name") + "' добавлен!</p>");
                }
            %>
            <div>

        <form method="post">
            <label>Имя:
                <input type="text" name="name"><br/>
            </label>

            <label>Возраст:
                <input type="number" name="age"><br/>
            </label>
            <button class="w3-button w3-white w3-padding-large w3-large w3-opacity w3-hover-opacity-off btn-block" type="submit">Добавится в друганы</button>
        </form>
<br><button class="w3-button w3-white w3-padding-large w3-large w3-opacity w3-hover-opacity-off btn-block"  onclick="location.href='/list'" name="friends">Показать всех друзей</button>





</body>
</html>