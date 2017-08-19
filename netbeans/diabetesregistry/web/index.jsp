<%-- 
    Document   : index
    Created on : Oct 27, 2015, 9:04:04 PM
    Author     : Bryan Daniel
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/includes/header.jsp" />
<section id="signinpage">
    <c:choose>
        <c:when test="${reset == 1}">
            <h1>Password Reset Request</h1>
            <p class="error">
                <c:if test="${errorMessage!=null}">
                    <c:out value="${errorMessage}"></c:out>
                </c:if>
            </p>
            <section id="resetrequestpart">
                <p class="forminstructions">To request a password reset, enter your username and email address.</p>
                <form class="contentText" action="signin" method="post">
                    <input type="hidden" name="action" value="sendPasswordResetRequest">
                    <label id="idlabel" for="id" class="resetrequestlabels">Username:</label>
                    <input id="id" type="text" name="userName" value="" required autocomplete="off">
                    <label id="emaillabel" for="requestEmail" class="resetrequestlabels">Email Address:</label>
                    <input id="requestEmail" type="text" name="email" value="" required autocomplete="off">
                    <input class="button" id="signinbutton" type="submit" value="Send Request">        
                </form>
                <form action="signin" method="get">
                    <input class="button" id="signinbutton" type="submit" value="Cancel">
                </form>
            </section>
        </c:when>
        <c:otherwise>
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
            <section id="forgotpasswordpart">
                <a href="<c:url value='/signin?action=requestreset'/>">
                        Forgot Password?</a>
            </section>
        </c:otherwise>
    </c:choose>    
</section>
<jsp:include page="/includes/footer.jsp" />
