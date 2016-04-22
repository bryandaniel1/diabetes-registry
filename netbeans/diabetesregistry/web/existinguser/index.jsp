<%-- 
    Document   : index
    Created on : Oct 31, 2015, 7:56:56 PM
    Author     : Bryan Daniel
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/includes/registrationheader.jsp" />
<section id="registerpage">
    <h1>Existing User Registration</h1>
    <p id="registerinstructions" class="forminstructions">All text fields require input for registration.</p><br>

    <form class="contentText" action="existinguser" method="post">
        <input type="hidden" name="action" value="registerExistingUser">
        <table id="registertable">
            <tr>
                <td class="dataregisterlabels">
                    <label id="keylabel" for="key" class="registerlabels">Registration Key:</label>
                </td>
                <td>
                    <input id="key" type="password" name="registrationKey" value="" required>
                </td>
            </tr>
            <tr>
                <td class="dataregisterlabels">
                    <label id="clinicidlabel" for="clinicid" class="registerlabels">Clinic ID:</label>
                </td>
                <td>
                    <select name="clinicId" required>
                        <option value="" selected disabled>Select a clinic</option>
                        <c:forEach var="clinic" items="${references.clinics}">
                            <option value="${clinic.clinicId}"><c:out value="${clinic.clinicName}"/></option>
                        </c:forEach>
                    </select>
                </td>
            </tr>        
            <tr>
            <tr>
                <td class="dataregisterlabels">
                    <label id="idlabel" for="registerid" class="registerlabels">User Name:</label>
                </td>
                <td>
                    <input id="registerid" type="text" name="userName" value="" required>
                </td>
            </tr>
            <tr>
                <td class="dataregisterlabels">
                    <label id="passwordlabel" for="registerpassword" class="registerlabels">
                        Password:</label>
                </td>
                <td>
                    <input id="registerpassword" type="password" name="password" value="" required>
                </td>
            </tr>
        </table>
        <input class="button" type="submit" value="Register">        
    </form>
</section>
<jsp:include page="/includes/footer.jsp" />
