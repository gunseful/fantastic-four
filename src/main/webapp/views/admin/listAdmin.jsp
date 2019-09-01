<%--@elvariable id="bundle" type="java.util.ResourceBundle"--%>
<%--@elvariable id="user" type="app.model.user.User"--%>
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
    <title>${bundle.getObject("admin.list.title")}</title>
</head>
<%--top if the site--%>
<div class="ex1 w3-container w3-dark-gray w3-opacity w3-center-align">
        <form action="LangServlet" method="post">
            Ru <input type="radio" name="lang" value="ru">
            En <input type="radio" name="lang" value="en">
            Fr <input type="radio" name="lang" value="fr">
            <input type="hidden" name="jspname" value="/listAdmin" />
            <input class="w3-button w3-dark-gray w3-padding-small" type="submit" value="${bundle.getObject("change.language")}">
        </form>
    <i class="fas fa-dragon w3-jumbo" onclick="location.href='/'"
       style="font-size:60px;color:white;text-shadow:2px 2px 4px #000000;"></i><i class="normals"> W</i><i
        class="normal">hite </i><i class="normals"> D</i><i class="normal">ragon</i>
</div>
<%--getting user from session and displaing string - you entryed as ${nickname}--%>
<p class="ex1" style="font-size:15px;">${bundle.getObject("entry")} ${user.getNickname()} </p>
<p class="ex1" style="font-size:15px;">${bundle.getObject("admin")} </p>
<p class="ex1" style="font-size:12px;">${bundle.getObject("admin.list.hint")}</p>

<%--button "Orders", which redirecting to order's page--%>
<button class="w3-button w3-light-green w3-padding-large w3-large w3-hover-opacity-off btn-block"
        onclick="location.href='/orders'" name="Orders" type="submit" value="Orders">${bundle.getObject("orders")}
</button>

<%--button "Black List", which redirecting to black list's page--%>
<button class="w3-button w3-black w3-padding-large w3-large w3-hover-opacity-off btn-block"
        onclick="location.href='/blacklist'" name="blacklist" type="submit"
        value="blacklist">${bundle.getObject("blacklist.title")}
</button>

<%--button, which calls logout servlet and nullifies session -> redirect to index.jsp page--%>
<div>
    <form action="LogoutServlet" method="post">
        <button class="w3-button w3-white w3-padding-large w3-large w3-opacity w3-hover-opacity-off btn-block"
                type="submit" value="logout">${bundle.getObject("out")}
        </button>
    </form>
</div>

<%--if nothing is selected, displays string - You chose nothing--%>
<div class="ex2">
    <div>
        <%--@elvariable id="nullData" type="String"--%>
        <c:if test="${nullData != null}">
            <p style="font-size:15px;">${bundle.getObject("nulldata")}</p>
        </c:if>
    </div>
    <%--getting list of products from database and displaying it--%>
    <form name="input" method="post">
        <form name="input" method="post">
            <%--@elvariable id="products" type="List<Product>"--%>
            <c:if test="${!products.isEmpty()}">
                <p>${bundle.getObject("admin.list.title")}</p>
                <table>
                    <tr>
                        <td></td>
                        <td>${bundle.getObject("name")}</td>
                        <td>${bundle.getObject("price")}</td>
                    </tr>
                    <c:forEach var="product" items="${products}"><%--@elvariable id="String" type="java.lang.String"--%>
                        <tr>
                            <td><input type="checkbox" name="productForDelete" value=" ${product.getId()} "></td>
                            <td>${product.getName()}</td>
                            <td>${String.format("%.2f", (product.getPrice()*bundle.getObject("exchange.rates")))}</td>
                            <td>${bundle.getObject("currency")}</td>
                        </tr>
                    </c:forEach>
                </table>
                <input class="w3-button w3-red " onclick="location.href='../../../../../web'" type="submit"
                       value=${bundle.getObject("delete")}>
            </c:if>
            <c:if test="${products.isEmpty()}">
                <p>${bundle.getObject("admin.list.no.products")}</p>
            </c:if>
        </form>
    </form>
<%--block of adding new product--%>
    <label class="link" for="hider" id="clickme">${bundle.getObject("admin.list.add.product")}</label>
    <input type="checkbox" id="hider">
    <div class="content">
        <form method="post" accept-charset="ISO-8859-1">
            <label
            <%--entering of name and price--%>
            <p style="font-size:13px;">${bundle.getObject("nomination")}</p> <input type="text" name="name">
            <p style="font-size:13px;">${bundle.getObject("price")}  ${bundle.getObject("currency")}</p> <input type="number" name="price"><br/>
            </label>
            <br/>
            <%--submit button--%>
            <button class="w3-button w3-green w3-padding-large" type="submit">${bundle.getObject("add")}</button>
        </form>
    </div>
</div>
</html>