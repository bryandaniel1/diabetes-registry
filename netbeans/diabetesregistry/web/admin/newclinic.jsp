<%-- 
    Document   : newclinic
    Created on : Feb 6, 2016, 11:50:46 AM
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
<h3>Add Clinic</h3>
<form class="contentText" action="admin" method="post">
    <input type="hidden" name="action" value="addclinicsubmit">
    <table class="adminText">
        <tr><td colspan="3">The administrator clinic must have a valid email address to process a new clinic.</td></tr>
        <tr>
            <td class="dataregisterlabels">
                <label for="clinic">Clinic of Administrator:</label>
            </td>
            <td colspan="2">
                <select id="clinic" name="clinicselect">
                    <c:forEach var="clinic" items="${user.clinics}">
                        <c:choose>
                            <c:when test="${clinic.clinicId == sessionScope.clinicId}">
                                <option value="${clinic.clinicId}" selected><c:out value="${clinic.clinicName}"/></option>
                            </c:when>
                            <c:otherwise>
                                <option value="${clinic.clinicId}"><c:out value="${clinic.clinicName}"/></option>
                            </c:otherwise>                        
                        </c:choose>                        
                    </c:forEach>
                </select>
            </td>
        </tr>
        <tr>
            <td class="dataregisterlabels">
                <label for="emailPassword">Administrator Email Password:</label>
            </td>
            <td colspan="2">
                <input id="emailPassword" type="password" name="emailPassword" value="">
            </td>
        </tr>
        <tr>
            <th colspan="3">New Clinic Details</th>
        </tr>
        <tr>
            <td class="dataregisterlabels">
                <label for="clinicName">Clinic Name:</label>
            </td>
            <td colspan="2">
                <input id="clinicName" type="text" name="clinicName" value="" required>
            </td>
        </tr>
        <tr>
            <td class="dataregisterlabels">
                <label for="address">Clinic Address:</label>
            </td>
            <td colspan="2">
                <input id="address" type="text" name="address" value="" required>
            </td>
        </tr>
        <tr>
            <td class="dataregisterlabels">
                <label for="phoneNumber">Phone Number:</label>
            </td>
            <td colspan="2">
                <input id="phoneNumber" type="text" name="phoneNumber" value="" required>
            </td>
        </tr>
        <tr>
            <td class="dataregisterlabels">
                <label for="email">Email Address:</label>
            </td>
            <td colspan="2">
                <input id="email" type="email" name="email" value="">
            </td>
        </tr>
        <tr>
            <td colspan="3">
                <input class="button" type="submit" value="Add Clinic">
            </td>
        </tr>
    </table>            
</form>
</section>
<jsp:include page="/includes/footer.jsp" />
