<%-- 
    Document   : index
    Created on : Oct 27, 2015, 9:04:04 PM
    Author     : Bryan Daniel
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/includes/header.jsp" />
<section id="signinpage">
    <h1>Sign In Page</h1>
    <p class="success">
        <c:if test="${message!=null}">
            <c:out value="${message}"></c:out>
        </c:if>
    </p>
    <section id="signinpart">
        <p class="forminstructions">Enter your credentials to sign in.</p>
        <form class="contentText" action="signin" method="post">
            <input type="hidden" name="action" value="signin_user">
            <label id="idlabel" for="id" class="signinlabels">Username:</label>
            <input id="id" type="text" name="userName" value="" required autocomplete="off">
            <label id="passwordlabel" for="password" class="signinlabels">Password:</label>
            <input id="password" type="password" name="password" value="" required autocomplete="off">
            <input class="button" id="signinbutton" type="submit" value="Sign In">        
        </form>        
    </section>
    <section id="registerpart">
        <p class="forminstructions">Not registered?</p>
        <form action="register">
            <input class="button" id="registerbutton" type="submit" value="Register Here">
        </form>
    </section>
</section>
<jsp:include page="/includes/footer.jsp" />
