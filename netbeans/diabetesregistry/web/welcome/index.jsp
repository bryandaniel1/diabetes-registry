<%-- 
    Document   : index
    Created on : Oct 31, 2015, 12:45:14 PM
    Author     : Bryan Daniel
--%>
<%@page import="java.util.Date"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/includes/header.jsp" />
<section id="signinpage">
    <h1>Hello</h1>        
    <c:choose>
        <c:when test="${user == null}">
            <p class="forminstructions">You are not signed in.</p><br>
            <section id="signoutbutton">
                <a href="<c:url value='/'/>">Return to Sign In Page</a>
            </section>
        </c:when>
        <c:otherwise>
            <section id="signinpart">
                <%
                    Date date = new Date();
                    String loginTime = date.toString();
                %>
                <p class="forminstructions">Welcome, <c:out value="${user.firstName}"></c:out><br>
                    Current date and time: <c:out value="<%=loginTime%>"></c:out><br></p>
                    <form action="home" method="post">
                        <input class="button" id="homebutton" type="submit" value="Proceed to Registry">        
                    </form>
                    <p class="error">
                    <c:if test="${message!=null}">
                        <c:out value="${message}"></c:out>
                    </c:if>
                </p>
            </section>
            <section id="forgotpasswordpart">
                <form action="signout" method="post">
                    <input type="hidden" name="action" value="signout">
                    <input class="button" id="signoutbuttonbutton" type="submit" value="Sign Out">
                </form>
            </section>
        </c:otherwise>
    </c:choose>        
</section>
<jsp:include page="/includes/footer.jsp" />
