<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html class="errorh">
<head>
    <meta charset="utf-8">
    <link href="../../static/css/app.css" rel="stylesheet">
</head>
<body class="errorbody">
<div class="noise"></div>
<div class="overlay"></div>
<div class="terminal">
    <h1>Error <span class="errorcode">${statusCode }</span></h1>
    <c:choose>
        <c:when test="${statusCode == 403}"><p class="output">You don't have permissions to view this resource</p></c:when>
        <c:when test="${statusCode == 404}"><p class="output">Requested resource not found</p></c:when>
        <c:otherwise><p class="output">Can't process this request! Try again later...</p></c:otherwise>
    </c:choose>
    <p class="output">Please try to <a class="errora" href="/products">go back</a> or <a class="errora" href="/products">return to the homepage</a>.</p>
    <p class="output">Good luck.</p>

</div>
</body>
</html>



