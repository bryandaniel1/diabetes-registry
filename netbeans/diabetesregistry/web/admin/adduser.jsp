<%-- 
    Document   : adduser
    Created on : March 06, 2017
    Author     : Bryan Daniel
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/includes/taskheader.jsp" />
<h2>Administration Functions</h2>
<c:if test="${errorMessage != null}">
    <p class="error"><c:out value="${errorMessage}"/></p>
</c:if>
<c:if test="${message != null}">
    <p class="success"><c:out value="${message}"/></p>
</c:if>
<jsp:include page="/includes/adminnav.jsp" />
    <h3>Add New User</h3>
    <p id="registerinstructions" class="forminstructions">First name, last name, 
        job title, and the administrator email password are required fields.</p><br>

    <form class="contentText" action="admin" method="post">
        <input type="hidden" name="action" value="saveNewUser">
        <table id="registertable">
            <tr>
                <td class="datalabels">
                    <label id="firstnamelabel" for="firstname" class="labels">First Name:</label>
                </td>
                <td>
                    <input id="firstname" type="text" name="firstName" value="" required>
                </td>
            </tr>
            <tr>
                <td class="datalabels">
                    <label id="lastnamelabel" for="lastname" class="labels">Last Name:</label>
                </td>
                <td>
                    <input id="lastname" type="text" name="lastName" value="" required>
                </td>
            </tr>
            <tr>
                <td class="datalabels">
                    <label id="idlabel" for="jobtitle" class="labels">Job Title:</label>
                </td>
                <td>
                    <input id="jobtitle" type="text" name="jobTitle" value="" required>
                </td>            
            </tr>
            <tr>
            <tr>
                <td class="datalabels">
                    <label id="emaillabel" for="useremail" class="labels">New User Email Address:</label>
                </td>
                <td>
                    <input id="useremail" type="text" name="userEmail" value="">
                </td>
            </tr>
        </table>
        <h4>Enter the administrator email password to add a new user.</h4>
        <label id="adminemailpasswordlabel" class="adminemailpassword" for="emailPassword">Administrator Email Password:</label>
        <input class="adminemailpassword" id="emailPassword" type="password" name="emailPassword" value="">
        <input class="button" type="submit" value="Save User">        
    </form>
</section>
<jsp:include page="/includes/footer.jsp" />
