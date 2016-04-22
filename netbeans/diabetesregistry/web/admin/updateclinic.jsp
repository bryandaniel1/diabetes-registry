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
<form class="contentText" action="admin" method="post">
    <input type="hidden" name="action" value="getClinic">
    <table class="adminText">
        <tr>
            <td class="dataregisterlabels">
                <label for="clinicselect">Clinic:</label>
            </td>
            <td>
                <select name="clinicselect" onchange="this.form.submit()">
                    <option value="" selected disabled>Select a clinic</option>
                    <c:forEach var="clinic" items="${references.clinics}">
                        <option value="${clinic.clinicId}"><c:out value="${clinic.clinicName}"/></option>
                    </c:forEach>
                </select>
            </td>
        </tr>
    </table>
</form>
<c:if test="${clinic != null}">
    <form class="contentText" action="admin" method="post">
        <input type="hidden" name="action" value="updateClinicInformation">
        <input type="hidden" name="clinicId" value="${clinic.clinicId}">
        <table class="adminText">        
            <tr>
                <td class="dataregisterlabels">
                    <label for="clinic">Clinic of Administrator:</label>
                </td>
                <td colspan="2">
                    <select id="clinic" name="adminclinicselect">
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
                    <label for="clinicName" class="registerlabels">Clinic Name:</label>
                </td>
                <td>
                    <input id="clinicName" type="text" name="clinicName" value="<c:out value="${clinic.clinicName}"/>" required>
                </td><td></td>
            </tr>
            <tr>
                <td class="dataregisterlabels">
                    <label for="address" class="registerlabels">Clinic Address:</label>
                </td>
                <td>
                    <input id="address" type="text" name="address" value="<c:out value="${clinic.address}"/>" required>
                </td><td></td>
            </tr>
            <tr>
                <td class="dataregisterlabels">
                    <label for="phoneNumber" class="registerlabels">Phone Number:</label>
                </td>
                <td>
                    <input id="phoneNumber" type="text" name="phoneNumber" value="<c:out value="${clinic.phoneNumber}"/>" required>
                </td><td></td>
            </tr>        
            <tr>
                <td class="dataregisterlabels">
                    <label for="email">Email:</label>
                </td>
                <td>
                    <input id="email" type="email" name="email" value="<c:out value="${clinic.emailAddress}"/>">
                </td>
            </tr>
            <tr>
                <td colspan="2">
                    <label for="newkey" class="dataregisterlabels">create new registration key:</label>
                    <input  class="dataentrycheckbox" id="newkey" type="checkbox" name="newKey" value="1">
                </td>
            </tr>
            <tr>
                <td colspan="2">Administrator email password is required for a key change.</td>
            </tr>
            <tr>
                <td class="dataregisterlabels">
                    <label for="emailPassword">Administrator Email Password:</label>
                </td>
                <td>
                    <input id="emailPassword" type="password" name="emailPassword" value="">
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
</c:if>
</section>
<jsp:include page="/includes/footer.jsp" />
