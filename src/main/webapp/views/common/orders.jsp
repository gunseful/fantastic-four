<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" language="java" %>
<html lang="ru">
<head>
    <link rel="stylesheet" href="<c:url value="/views/css/style.css"/>" type="text/css">
    <link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.7.0/css/all.css"
          integrity="sha384-lZN37f5QGtY3VHgisS14W3ExzMWZxybE1SJSEsQp9S+oqd12jhcu+A56Ebc1zFSJ" crossorigin="anonymous">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta charset="UTF-8">
    <title>Заказы</title>
</head>
<%--top if the site--%>
    <div class="ex1 w3-container w3-dark-gray w3-opacity w3-center-align">
            <form action="LangServlet" method="post">
                Ru <input type="radio" name="lang" value="ru">
                En <input type="radio" name="lang" value="en">
                Fr <input type="radio" name="lang" value="fr">
                <input type="hidden" name="jspname" value="/orders" />
                <input class="w3-button w3-dark-gray w3-padding-small" type="submit" value="${bundle.getObject("change.language")}">
            </form>
        <i class="fas fa-dragon w3-jumbo" onclick="location.href='/'"
           style="font-size:60px;color:white;text-shadow:2px 2px 4px #000000;"></i><i class="normals"> W</i><i
            class="normal">hite </i><i class="normals"> D</i><i class="normal">ragon</i>
    </div>

<div>
    <p class="ex1" style="font-size:15px;">${bundle.getObject("entry")} ${user.getNickname()} </p>
    <form name="input" method="post">
        <c:if test="${isAdmin}">
            <p class="ex1" style="font-size:15px;">${bundle.getObject("admin")} </p>
            <p class="ex1" style="font-size:15px;">${bundle.getObject("orders.hint")} </p>
        </c:if>
    </form>
</div>
<%--button - to the store--%>
<div>
    <div>
        <c:if test="${isAdmin}">
        <button class="w3-button w3-cyan w3-padding-large w3-large w3-hover-opacity-off btn-block"
                onclick="location.href='/listAdmin'">${bundle.getObject("tothestore")}
        </button>
        </c:if>
        <c:if test="${!isAdmin}">
            <button class="w3-button w3-cyan w3-padding-large w3-large w3-hover-opacity-off btn-block"
                    onclick="location.href='/listClient'">${bundle.getObject("tothestore")}
            </button>
        </c:if>
        <div>
            <c:if test="${isAdmin}">
                <button class="w3-button w3-black w3-padding-large w3-large w3-hover-opacity-off btn-block"
                        onclick="location.href='/blacklist'" name="blacklist" type="submit"
                        value="blacklist">${bundle.getObject("blacklist.title")}
                </button>
            </c:if>
        </div>
    </div>
</div>

<div>
    <form action="LogoutServlet" method="post">
        <button class="w3-button w3-white w3-padding-large w3-large w3-opacity w3-hover-opacity-off btn-block"
                type="submit" value="logout">${bundle.getObject("out")}
        </button>
    </form>
</div>

<div class="ex2">
    <div>
        <c:if test="${nullData != null}">
            <p style="font-size:15px;">${bundle.getObject("nulldata")}</p>
        </c:if>
    </div>

    <div>
        <form name="input" method="post">
            <c:if test="${!orders.isEmpty()}">
                <c:if test="${isAdmin}">
                    <p>${bundle.getObject("orders.admin")}</p>
                </c:if>
                <c:if test="${!isAdmin}">
                    <p>${bundle.getObject("orders.client")}</p>
                </c:if>
                <c:forEach var="order" items="${orders}">

                    <input type="checkbox" name="orders" value="${order.getId()}">



                    ${bundle.getObject("orders.order")} ${order.getId()}, ${bundle.getObject("orders.creation")} ${order.getCreationDate()}
                    <br>
                    <c:if test="${isAdmin}">
                        <c:if test="${!order.getUser().isInBlackList()}">
                            <p>
                                ${bundle.getObject("orders.client.title")}${order.getUser().getNickname()}  <button class="w3-button w3-padding-small w3-red" style="vertical-align:middle" name="block" value="${order.getUser().getId()}"><span>${bundle.getObject("orders.client.block")}</span></button>
                        </c:if>
                        <c:if test="${order.getUser().isInBlackList()}">
                            ${bundle.getObject("orders.client.title")}${order.getUser().getNickname()}  <span style="background-color:red;">${bundle.getObject("orders.client.blocked")}</span>
                        </c:if>
                    </c:if>
                    <table>
                        <tr>
                            <td>${bundle.getObject("name")}</td>
                            <td>${bundle.getObject("price")}</td>
                            <td>${bundle.getObject("count")}</td>
                        </tr>
                        <c:forEach var="product" items="${order.getProducts()}">
                            <tr>
                                <td>${product.getName()}</td>
                                <td>${String.format("%.2f", (product.getPrice()*bundle.getObject("exchange.rates")))} ${bundle.getObject("currency")}</td>
                                <td align="right">${product.getCount()}</td>
                            </tr>
                        </c:forEach>
                    </table>
                    <br>
                    ${bundle.getObject("orders.total.price")} ${String.format("%.2f", (order.totalPrice()*bundle.getObject("exchange.rates")))} ${bundle.getObject("currency")}
                    <br>
                    <c:if test="${order.isPaid()}">
                        ${bundle.getObject("orders.paid")}
                    </c:if>
                    <c:if test="${!order.isPaid()}">
                        ${bundle.getObject("orders.notpaid")}
                    </c:if>
                    <br>_____________________________________________<br>
                </c:forEach>
                <input class="w3-button w3-red " type="submit" value=${bundle.getObject("delete")}>
                <c:if test="${!isAdmin}">
                    <input class="w3-button w3-black " name="Pay" type="submit" value=${bundle.getObject("orders.pay")}>
                </c:if>
            </c:if>
            <c:if test="${orders.isEmpty()}">
                <p>${bundle.getObject("orders.empty")}</p>
            </c:if>
        </form>
    </div>
</div>
</body>
</html>