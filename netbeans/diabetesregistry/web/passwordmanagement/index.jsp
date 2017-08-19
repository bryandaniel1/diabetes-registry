<%-- 
    Document   : index
    Created on : Oct 31, 2015, 7:56:56 PM
    Author     : Bryan Daniel
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/includes/header.jsp" />
<section id="changepasswordpage">
    <h1>Password Reset</h1>
    <c:if test="${errorMessage != null}">
        <p class="error"><c:out value="${errorMessage}"/></p>
    </c:if>
    <c:if test="${message != null}">
        <p class="success"><c:out value="${message}"/></p>
    </c:if>
    <p id="registerinstructions" class="forminstructions">Enter your new password below.</p><br>
    <form class="contentText" action="changepassword" method="post">
        <input type="hidden" name="action" value="saveNewPassword">
        <table id="registertable">
            <tr>
                <td class="datalabels">
                    <label id="keylabel" for="key" class="labels">User:</label>
                </td>
                <td>
                    <c:out value="${sessionScope.sessionUserName}"/>
                </td>
            </tr>
            <tr>
                <td colspan="2">Passwords require at least one lower-case letter, at least one 
                    upper-case letter, at least one number, at least one special character, 
                    must contain no spaces, and must have between 10 and 20 characters inclusive.
                </td>
            </tr>
            <tr>
                <td class="datalabels">
                    <label id="passwordlabel" for="newpassword" class="labels">
                        Password:</label>
                </td>
                <td>
                    <input id="newpassword" type="password" name="newpassword" value="" required>
                </td>
            </tr>
            <tr>
                <td class="datalabels">
                    <label id="repeatpasswordlabel" for="repeatpassword" class="labels">
                        Re-enter Password:</label>
                </td>
                <td>
                    <input id="repeatpassword" type="password" name="repeatpassword" value="" required>
                </td>
            </tr>
        </table>
        <input class="button" type="submit" value="Save Password">        
    </form>
</section>
<jsp:include page="/includes/footer.jsp" />
