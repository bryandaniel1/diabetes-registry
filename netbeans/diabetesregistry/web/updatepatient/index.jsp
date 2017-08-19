<%-- 
    Document   : index
    Created on : Nov 4, 2015, 9:24:39 PM
    Author     : Bryan Daniel
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/includes/taskheader.jsp" />
<h2>Update Patient</h2>
<c:if test="${errorMessage != null}">
    <p class="error"><c:out value="${errorMessage}"/></p>
</c:if>
<c:if test="${message != null}">
    <p class="success"><c:out value="${message}"/></p>
</c:if>
<section class="pagecontent">
    <table class="patientselect">
        <tr>
            <td class="datalabels">
                <label for="clinicname" class="labels">Clinic:</label>
            </td>
            <td>
                <output name="clinicname"><c:out value="${applicationScope.references.clinic.clinicName}"/></output>
            </td>
        </tr>
    </table>
    <form class="contentText" action="updatepatient" method="post">
        <input type="hidden" name="action" value="getPatient">
        <table class="patientselect">
            <tr>
                <td class="datalabels">
                    <label for="patientselect" class="labels">Select Patient:</label>
                </td>
                <td>
                    <select name="patientselect" onchange="this.form.submit()">
                        <option value="" selected disabled>Select a patient</option>
                        <c:forEach var="p" items="${patients}">
                            <option value="${p.patientId}"><c:out value="${p.lastName}, ${p.firstName} (${p.birthDate})"/></option>
                        </c:forEach>
                    </select>
                </td>
            </tr>
        </table>
    </form>
</section>
<c:if test="${patient != null}">
    <section class="pagecontent">
        <form class="contentText" action="updatepatient" method="post">
            <input type="hidden" name="action" value="updatePatient">
            <table class="patientform">
                <tr>
                    <td class="datalabels">
                        <label for="firstName" class="labels">First Name:</label>
                    </td>
                    <td>
                        <input id="firstName" type="text" name="firstName" value="<c:out value="${patient.firstName}"/>" required>
                    </td><td></td>
                </tr>
                <tr>
                    <td class="datalabels">
                        <label for="lastName" class="labels">Last Name:</label>
                    </td>
                    <td>
                        <input id="lastName" type="text" name="lastName" value="<c:out value="${patient.lastName}"/>" required>
                    </td><td></td>
                </tr>
                <tr>
                    <td class="datalabels">
                        <label for="birthDate" class="labels">Birth Date:</label>
                    </td>
                    <td>
                        <input id="birthDate" type="text" name="birthDate" value="<c:out value="${patient.birthDate}"/>" required>
                    </td><td></td>
                </tr>
                <tr>
                    <td class="datalabels">
                        <label for="address">Address:</label>
                    </td>
                    <td>
                        <input id="address" type="text" name="address" value="<c:out value="${patient.address}"/>">
                    </td><td></td>
                </tr>
                <tr>
                    <td class="datalabels">
                        <label for="phoneNumber" class="labels">Contact Number:</label>
                    </td>
                    <td>
                        <input id="phoneNumber" type="text" name="phoneNumber" value="<c:out value="${patient.contactNumber}"/>">
                    </td><td></td>
                </tr>
                <tr>
                    <td class="datalabels">
                        <label for="gender" class="labels">Gender:</label>
                    </td>
                    <td>
                        <select id="gender" name="gender" required>
                            <option value="<c:out value="${patient.gender}"/>" selected><c:out value="${patient.gender}"/></option>
                            <option value="male">Male</option>
                            <option value="female">Female</option>
                        </select>
                    </td><td></td>            
                </tr>
                <tr>
                    <td class="datalabels">
                        <label for="race" class="labels">Race:</label>
                    </td>
                    <td>
                        <select id="race" name="race" required>
                            <option value="${patient.race}" selected><c:out value="${patient.race}"/></option>
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
                    <td class="datalabels">
                        <label for="email">Email:</label>
                    </td>
                    <td>
                        <input id="email" type="email" name="email" value="<c:out value="${patient.emailAddress}"/>">
                    </td><td></td>
                </tr>
                <tr>
                    <td class="datalabels">
                        <label for="language">Language:</label>
                    </td>
                    <td>
                        <select id="language" name="language">
                            <c:if test="${patient.language != null}">
                                <option value="${patient.language}" selected><c:out value="${patient.language}"/></option>
                            </c:if>
                            <c:forEach var="language" items="${references.languages}">
                                <option value="${language}"><c:out value="${language}"/></option>                        
                            </c:forEach>
                        </select>
                    </td><td></td>
                </tr>
                <tr>
                    <td class="datalabels">
                        <label for="startDate" class="labels">Start Date:</label>
                    </td>
                    <td>
                        <input id="startDate" type="text" name="startDate" value="<c:out value="${patient.startDate}"/>" required>
                    </td><td></td>
                </tr>
                <tr>
                    <td class="datalabels">
                        <label for="reason">Active:</label>
                    </td>
                    <td>
                        <select id="reason" name="reason">
                            <c:choose>
                                <c:when test="${patient.reasonForInactivity == null}">
                                    <option value="${null}" selected>yes</option>
                                    <c:forEach var="reason" items="${references.reasonsForInactivity}">
                                        <option value="${reason}">no: <c:out value="${reason}"/></option>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <option value="${patient.reasonForInactivity}" selected>no: <c:out value="${patient.reasonForInactivity}"/></option>
                                    <option value="${null}">yes</option>
                                    <c:forEach var="reason" items="${references.reasonsForInactivity}">
                                        <option value="${reason}">no: <c:out value="${reason}"/></option>
                                    </c:forEach>
                                </c:otherwise>                        
                            </c:choose>                    
                        </select>
                    </td><td></td>
                </tr>
                <tr>
                    <td></td>
                    <td colspan="2">
                        <input class="button" type="submit" value="Update Patient">
                    </td>
                </tr>
            </table>            
        </form>
    </section>
</c:if>
<jsp:include page="/includes/footer.jsp" />
