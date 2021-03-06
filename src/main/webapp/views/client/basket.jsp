<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<html lang="ru">
<%--в хеде ничего интересного--%>
<head>
    <link rel="stylesheet" href="<c:url value="/views/css/style.css"/>" type="text/css">
    <link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.7.0/css/all.css"
          integrity="sha384-lZN37f5QGtY3VHgisS14W3ExzMWZxybE1SJSEsQp9S+oqd12jhcu+A56Ebc1zFSJ" crossorigin="anonymous">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta charset="UTF-8">
    <title>${bundle.getObject("basket.title")}</title>
</head>
<%--top if the site--%>

    <div class="ex1 w3-container w3-dark-gray w3-opacity w3-center-align">
            <form action="LangServlet" method="post">
                Ru <input type="radio" name="lang" value="ru">
                En <input type="radio" name="lang" value="en">
                Fr <input type="radio" name="lang" value="fr">
                <input type="hidden" name="jspname" value="/basket" />
                <input class="w3-button w3-dark-gray w3-padding-small" type="submit" value="${bundle.getObject("change.language")}">
            </form>
        <i class="fas fa-dragon w3-jumbo" onclick="location.href='/'"
           style="font-size:60px;color:white;text-shadow:2px 2px 4px #000000;"></i><i class="normals"> W</i><i
            class="normal">hite </i><i class="normals"> D</i><i class="normal">ragon</i>
    </div>

<p class="ex1" style="font-size:15px;">${bundle.getObject("entry")} ${user.getNickname()} </p>

<div>
    <div>
        <button class="w3-button w3-cyan w3-padding-large w3-large w3-hover-opacity-off btn-block"
                onclick="location.href='/listClient'">${bundle.getObject("tothestore")}
        </button>
    </div>
</div>

<div>
    <div>
        <button class="w3-button w3-light-green w3-padding-large w3-large w3-hover-opacity-off btn-block"
                onclick="location.href='/orders'" name="Orders" type="submit"
                value="Orders">${bundle.getObject("orders")}
        </button>
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
            <c:if test="${!basket.isEmpty()}">
                <p>${bundle.getObject("basket.title")}</p>
                <table>
                    <tr>
                        <td></td>
                        <td>${bundle.getObject("name")}</td>
                        <td>${bundle.getObject("price")}</td>
                        <td></td>
                        <td></td>
                        <td>${bundle.getObject("count")}</td>
                    </tr>
                    <c:forEach var="product" items="${basket}">
                        <tr>
                            <td><input type="checkbox" name="productForDelete" value=${product.getId()}></td>
                            <td>${product.getName()}</td>
                            <td>${String.format("%.2f", (product.getPrice()*bundle.getObject("exchange.rates")))}</td>
                            <td>${bundle.getObject("currency")}</td>
                            <td>
                                <button class="w3-button w3-circle w3-deep-orange w3-hover-opacity-off btn-block"
                                        name="minus" type="submit" value=${product.getId()}>-
                                </button>
                            </td>
                            <td align="center">${product.getCount()}</td>
                            <td>
                                <button class="w3-button w3-circle w3-light-green w3-hover-opacity-off btn-block"
                                        name="plus" type="submit" value=${product.getId()}>+
                                </button>
                            </td>
                        </tr>
                    </c:forEach>
                </table>
                <input class="w3-button w3-red " onclick="location.href='../../../../../web'" type="submit"
                       value=${bundle.getObject("delete")}>
            </c:if>
            <c:if test="${basket.isEmpty()}">
                <p>${bundle.getObject("basket.empty")}</p>
            </c:if>
        </form>

        <form name="input" method="post">
            <c:if test="${!basket.isEmpty()}">
                <button class="w3-button w3-yellow w3-padding-large w3-large w3-hover-opacity-off btn-block"
                        name="getOrder" type="submit" value="getOrder">${bundle.getObject("basket.get.order")}
                </button>
            </c:if>
        </form>
    </div>
</div>
</html>