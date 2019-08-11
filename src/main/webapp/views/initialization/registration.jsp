<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<html lang="ru">
<head>
    <link rel="stylesheet" href="views/css/style.css" type="text/css">
    <link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.7.0/css/all.css" integrity="sha384-lZN37f5QGtY3VHgisS14W3ExzMWZxybE1SJSEsQp9S+oqd12jhcu+A56Ebc1zFSJ" crossorigin="anonymous">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>Регистрация</title>
</head>
<%--Классическая шапка--%>
<body class="w3-light-grey">
<div>
    <div class="ex1 w3-container w3-dark-gray w3-opacity w3-center-align">
        <i class="fas fa-dragon w3-jumbo" onclick="location.href='/'"
           style="font-size:60px;color:white;text-shadow:2px 2px 4px #000000;"></i><i class="normals"> W</i><i
            class="normal">hite </i><i class="normals"> D</i><i class="normal">ragon</i>
    </div>
</div>
<%--ну либо неверные данные, либо юзер уже есть, классическая надпись если что-то пошло не так--%>
<div>
    <div>
        <c:if test="${fail != null}">
            <p style="font-size:15px;">Введенные данные не верны, либо пользователь с таким ником уже существует <br> Правильно введеные данные - пароль от 6 до 15 символов<br> Имя и Никнейм от 3 до 15 символов</p>
        </c:if>
    </div>
</div>
<%--имя, ник, пароль, поля для ввода и кнопка добавляющая юзера в базу данных--%>
    <div>
        <form method="post" accept-charset="ISO-8859-1">
            <label>
                <p style="font-size:15px;">Имя:</p> <input type="text" name="name" value=""><br/>
            </label>
            <label>
                <p style="font-size:15px;">Никнейм:</p> <input type="text" name="nickname" value=""><br/>
            </label>
            <label>
                <p style="font-size:15px;">Пароль:</p> <input type="password" name="password" value=""><br/>
            </label>
            <button class="w3-button w3-white w3-padding-large w3-large w3-opacity w3-hover-opacity-off btn-block" type="submit">Зарегестрироваться</button>
        </form>
            </div>
</body>
</html>