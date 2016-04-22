<%-- 
    Document   : index
    Created on : Nov 4, 2015, 9:24:39 PM
    Author     : Bryan Daniel
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/includes/taskheader.jsp" />
<h2>Add New Patient</h2>
<c:if test="${errorMessage != null}">
    <p class="error"><c:out value="${errorMessage}"/></p>
</c:if>
<c:if test="${message != null}">
    <p class="success"><c:out value="${message}"/></p>
</c:if>
<section class="pagecontent">
    <form class="contentText" action="newpatient" method="post">
        <input type="hidden" name="action" value="add">
        <table class="patientform">
            <tr>
                <td class="dataregisterlabels">
                    <label for="firstName">First Name:</label>
                </td>
                <td>
                    <input id="firstName" type="text" name="firstName" value="" required>
                </td><td></td>
            </tr>
            <tr>
                <td class="dataregisterlabels">
                    <label for="lastName">Last Name:</label>
                </td>
                <td>
                    <input id="lastName" type="text" name="lastName" value="" required>
                </td><td></td>
            </tr>
            <tr>
                <td class="dataregisterlabels">
                    <label for="birthDate">Birth Date:</label>
                </td>
                <td>
                    <input class="datepicker" id="birthDate" type="text" name="birthDate" value="" required>
                </td><td></td>
            </tr>
            <tr>
                <td class="dataregisterlabels">
                    <label for="address">Address:</label>
                </td>
                <td>
                    <input id="address" type="text" name="address" value="">
                </td>
            </tr>
            <tr>
                <td class="dataregisterlabels">
                    <label for="phoneNumber">Phone Number:</label>
                </td>
                <td>
                    <input id="phoneNumber" type="text" name="phoneNumber" value="">
                </td><td></td>
            </tr>
            <tr>
                <td class="dataregisterlabels">
                    <label for="gender">Gender:</label>
                </td>
                <td>
                    <select id="gender" name="gender" required>
                        <option value="" selected disabled>Select a gender</option>
                        <option value="male">Male</option>
                        <option value="female">Female</option>
                    </select>
                </td><td></td>            
            </tr>
            <tr>
            <tr>
                <td class="dataregisterlabels">
                    <label for="ethnicity">Race:</label>
                </td>
                <td>
                    <select id="ethnicity" name="race" required>
                        <option value="" selected disabled>Select an ethnicity</option>
                        <option value="White">White</option>
                        <option value="African American">African American</option>
                        <option value="Asian/Pacific Islander">Asian/Pacific Islander</option>
                        <option value="American Indian/Alaska Native">American Indian/Alaska Native</option>
                        <option value="Hispanic">Hispanic</option>
                        <option value="Middle Eastern">Middle Eastern</option>
                        <option value="Other">Other</option>
                    </select>
                </td><td></td>
            </tr>
            <tr>
                <td class="dataregisterlabels">
                    <label for="firstName">Clinic:</label>
                </td>
                <td>
                    <select id="clinicid" name="clinicId">
                        <c:forEach var="clinic" items="${user.clinics}">
                            <option value="${clinic.clinicId}"><c:out value="${clinic.clinicName}"/></option>                        
                        </c:forEach>
                    </select>
                </td>
            </tr>
            <tr>
                <td class="dataregisterlabels">
                    <label for="email">Email:</label>
                </td>
                <td>
                    <input id="email" type="email" name="email" value="">
                </td>
            </tr>
            <tr>
                <td class="dataregisterlabels">
                    <label for="language">Language:</label>
                </td>
                <td>
                    <select id="language" name="language">
                        <option value="" selected disabled>Select a language</option>
                        <c:forEach var="language" items="${references.languages}">
                            <option value="${language}"><c:out value="${language}"/></option>
                        </c:forEach>
                    </select>
                </td>
            </tr>
            <tr>
                <td class="dataregisterlabels">
                    <label for="startDate">Start Date:</label>
                </td>
                <td>
                    <input class="datepicker" id="startDate" type="text" name="startDate" value="" required>
                </td><td></td>
            </tr>
            <tr>
                <td></td>
                <td colspan="2">
                    <input class="button" type="submit" value="Add Patient">
                </td>
            </tr>
        </table>            
    </form>
</section>
<jsp:include page="/includes/footer.jsp" />
