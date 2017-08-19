<%-- 
    Document   : updateclinic
    Created on : Feb 6, 2016, 11:51:33 AM
    Author     : Bryan Daniel
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<jsp:include page="/includes/taskheader.jsp" />
<h2>Administration Functions</h2>
<c:if test="${errorMessage != null}">
    <p class="error"><c:out value="${errorMessage}"/></p>
</c:if>
<c:if test="${message != null}">
    <p class="success"><c:out value="${message}"/></p>
</c:if>
<jsp:include page="/includes/adminnav.jsp" />
<h3>Update Clinic</h3>
<table class="adminText">
    <tr>
        <td class="datalabels">
            <label for="clinicname">Clinic:</label>
        </td>
        <td>
            <output name="clinicname"><c:out value="${applicationScope.references.clinic.clinicName}"/></output>
        </td>
    </tr>
</table>
<form class="contentText" action="admin" method="post">
    <input type="hidden" name="action" value="updateClinicInformation">
    <table class="adminText">        
        <tr>
            <td class="datalabels">
                <label for="clinicName" class="labels">Clinic Name:</label>
            </td>
            <td>
                <input id="clinicName" type="text" name="clinicName" value="<c:out value="${applicationScope.references.clinic.clinicName}"/>" required>
            </td><td></td>
        </tr>
        <tr>
            <td class="datalabels">
                <label for="address" class="labels">Clinic Address:</label>
            </td>
            <td>
                <input id="address" type="text" name="address" value="<c:out value="${applicationScope.references.clinic.address}"/>" required>
            </td><td></td>
        </tr>
        <tr>
            <td class="datalabels">
                <label for="phoneNumber" class="labels">Phone Number:</label>
            </td>
            <td>
                <input id="phoneNumber" type="text" name="phoneNumber" value="<c:out value="${applicationScope.references.clinic.phoneNumber}"/>" required>
            </td><td></td>
        </tr>        
        <tr>
            <td class="datalabels">
                <label for="email">Email:</label>
            </td>
            <td>
                <input id="email" type="email" name="email" value="<c:out value="${applicationScope.references.clinic.emailAddress}"/>">
            </td>
        </tr>
        <tr>
            <td></td>
            <td>
                <input class="button" type="submit" value="Update Clinic">
            </td>
        </tr>
    </table>            
</form>
</section>
<jsp:include page="/includes/footer.jsp" />
