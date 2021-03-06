<%--@elvariable id="user" type="app.model.user.User"--%>
<%--@elvariable id="bundle" type="java.util.ResourceBundle"--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<html lang="ru">
<head>
    <link rel="stylesheet" href="<c:url value="/views/css/style.css"/>" type="text/css">
    <link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.7.0/css/all.css"
          integrity="sha384-lZN37f5QGtY3VHgisS14W3ExzMWZxybE1SJSEsQp9S+oqd12jhcu+A56Ebc1zFSJ" crossorigin="anonymous">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta charset="UTF-8">
    <title>${bundle.getObject("blacklist.title")}</title>
</head>
<%--top if the site--%>
<div>

    <div class="ex1 w3-container w3-dark-gray w3-opacity w3-center-align">
            <form action="LangServlet" method="post">
                Ru <input type="radio" name="lang" value="ru">
                En <input type="radio" name="lang" value="en">
                Fr <input type="radio" name="lang" value="fr">
                <input type="hidden" name="jspname" value="/blacklist" />
                <input class="w3-button w3-dark-gray w3-padding-small" type="submit" value="${bundle.getObject("change.language")}">
            </form>
        <i class="fas fa-dragon w3-jumbo" onclick="location.href='/'"
           style="font-size:60px;color:white;text-shadow:2px 2px 4px #000000;"></i><i class="normals"> W</i><i
            class="normal">hite </i><i class="normals"> D</i><i class="normal">ragon</i>
    </div>
</div>

<p class="ex1" style="font-size:15px;">${bundle.getObject("entry")} ${user.getNickname()} </p>
<p class="ex1" style="font-size:15px;">${bundle.getObject("admin")} </p>
<p class="ex1" style="font-size:12px;">${bundle.getObject("blacklist.hint")}</p>

<div>
    <button class="w3-button w3-cyan w3-padding-large w3-large w3-hover-opacity-off btn-block"
            onclick="location.href='/listAdmin'">${bundle.getObject("tothestore")}
    </button>
</div>

<button class="w3-button w3-light-green w3-padding-large w3-large w3-hover-opacity-off btn-block"
        onclick="location.href='/orders'" name="Orders" type="submit" value="Orders">${bundle.getObject("orders")}
</button>

<div>
    <form action="LogoutServlet" method="post">
        <button class="w3-button w3-white w3-padding-large w3-large w3-opacity w3-hover-opacity-off btn-block"
                type="submit" value="logout">${bundle.getObject("out")}
        </button>
    </form>
</div>

<div class="ex2">
    <div>
        <%--@elvariable id="nullData" type="String"--%>
        <c:if test="${nullData != null}">
            <p style="font-size:15px;">${bundle.getObject("nulldata")}</p>
        </c:if>
    </div>
    <%--    --%>
    <div>
        <form name="input" method="post">
            <%--@elvariable id="blacklist" type="List<User>"--%>
            <c:if test="${!blacklist.isEmpty()}">
                <p>${bundle.getObject("blacklist.title")}</p>
                <c:forEach var="user" items="${blacklist}">
                    <label>
                        <input type="checkbox" name="userForDelete" value=" ${user.getId()} ">
                    </label>${user.getNickname()}<br>
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
</html>