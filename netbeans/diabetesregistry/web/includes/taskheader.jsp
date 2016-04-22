<%-- 
    Document   : taskheader
    Created on : Nov 1, 2015, 6:18:12 PM
    Author     : Bryan Daniel
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>St. Joseph Medical Clinic Diabetes Registry</title>
        <meta name="robots" content="noindex, nofollow">
        <link rel="stylesheet" href="<c:url value='/script/jquery-ui.css'/>">
        <script src="<c:url value='/script/jquery-1.10.2.js'/>"></script>
        <script src="<c:url value='/script/jquery-ui.js'/>"></script>
        <link rel="stylesheet" type="text/css" href="<c:url value='/style/main.css'/>">
        <script>
            $(function () {
                $(".datepicker").datepicker();
                $(".datepicker").datepicker("option", "dateFormat", "yy-mm-dd");
            });
        </script>
        <script type="text/javascript" src="<c:url value='/script/calculate.js'/>"></script>
    </head>
    <body>
        <header>
            <h1 id="title">St. Joseph Medical Clinic<br>Diabetes Registry</h1>            
                <c:if test="${user!=null}">
                <ul id="usersignout">
                    <li>Hello, <c:out value="${user.firstName}"></c:out></li>
                        <c:if test="${user.administrator == true}">
                        <li><a href="<c:url value='/admin?action=administrate'/>">Administration</a></li>
                        </c:if>
                    <li><a href="<c:url value='/signout?action=signout'/>">Sign Out</a></li>
                </ul>               
            </c:if>
            <nav id="nav-main"> 
                <ul>
                    <li><a href="<c:url value='/home'/>">Home</a></li>
                    <li><a href="<c:url value='/newpatient'/>">New Patient</a></li>
                    <li><a href="<c:url value='/updatepatient'/>">Update Patient</a></li>
                    <li><a href="<c:url value='/dataentry'/>">Data Entry</a></li>
                    <li><a href="<c:url value='/history'/>">Patient History</a></li>
                    <li><a href="<c:url value='/treatment'/>">Patient Treatment Entry</a></li>
                    <li><a href="<c:url value='/quality'/>">Quality Checklist</a></li>
                    <li><a href="<c:url value='/statistics'/>">Clinic Statistics</a></li>
                    <li><a href="<c:url value='/calllists'/>">Call Lists</a></li>
                    <li><a href="<c:url value='/progress'/>">Progress Note</a></li>
                </ul>
            </nav>
        </header>
