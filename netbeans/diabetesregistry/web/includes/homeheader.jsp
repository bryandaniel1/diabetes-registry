<%-- 
    Document   : header
    Created on : Oct 30, 2015, 8:26:12 AM
    Author     : Bryan Daniel
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Diabetes Registry</title>
        <meta name="robots" content="noindex, nofollow">
        <link rel="stylesheet" href="<c:url value='/script/jquery-ui.css'/>">
        <script src="<c:url value='/script/jquery-1.12.1.js'/>"></script>
        <script src="<c:url value='/script/jquery-ui.js'/>"></script>
        <link rel="stylesheet" type="text/css" href="<c:url value='/style/main.css'/>">
        <script type="text/javascript" src="<c:url value='/script/main.js'/>"></script>
    </head>
    <body>
        <header>
            <h1 id="title">Diabetes Registry</h1>            
                <c:if test="${user!=null}">
                <ul id="usersignout">
                    <li>Hello, <c:out value="${user.firstName}"></c:out></li>
                        <c:if test="${user.administrator == true}">
                        <li><a href="<c:url value='/admin?action=administrate'/>">Administration</a></li>
                        </c:if>
                    <li><a href="<c:url value='/signout?action=signout'/>">Sign Out</a></li>
                </ul>               
            </c:if>
            <nav>        
            </nav>
        </header>