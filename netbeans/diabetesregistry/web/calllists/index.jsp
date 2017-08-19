<%-- 
    Document   : index
    Created on : Feb 10, 2016, 9:09:16 PM
    Author     : Bryan Daniel
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/includes/taskheader.jsp" />
<h2>Call Lists</h2>
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
    <form class="contentText" action="calllists" method="post">
        <input type="hidden" name="action" value="getCallList">
        <table class="patientselect" id="calllistselect">
            <tr>
                <td class="datalabels">
                    <label for="listSubject" class="labels">Select a Call List:</label>
                </td>
                <td>
                    <select name="listSubject" onchange="this.form.submit()">
                        <option value="" selected disabled>Select a list</option>
                        <c:forEach var="subject" items="${references.emailMessageSubjects}">
                            <option value="${subject}"><c:out value="${subject}"/></option>
                        </c:forEach>
                    </select>
                </td>
            </tr>
        </table>
    </form>
</section>
<c:if test="${callListPatients != null}">
    <section class="pagecontent">
        <h3><c:out value="${callListSubject}"/></h3>
        <form class="contentText" action="calllists" method="post">
            <input type="hidden" name="action" value="sendEmails">
            <input type="hidden" name="subject" value="${callListSubject}">
            <table class="calllisttable">
                <tr>
                    <th>
                        <label for="mastercheckbox">Select All</label>
                        <input id="mastercheckbox" type="checkbox" onClick="selectAll(this)" />
                    </th>
                    <th>
                        Name
                        <c:choose>
                            <c:when test="${callListNameSort == 1}">
                                &nbsp;<a id="nameuparrow" href="<c:url value='/calllists?action=reverseSortByLastName'/>">&#129093;</a>
                            </c:when>
                            <c:otherwise>
                                &nbsp;<a id="namedownarrow" href="<c:url value='/calllists?action=sortByLastName'/>">&#129095;</a>
                            </c:otherwise>
                        </c:choose>
                    </th>
                    <c:choose>
                        <c:when test="${measurementDateTypeHeader != null}">
                            <th>
                                <c:out value="${measurementDateTypeHeader}"/>
                                <c:choose>
                                    <c:when test="${callListDateSort == 1}">
                                        &nbsp;<a id="dateuparrow" href="<c:url value='/calllists?action=reverseSortByDate'/>">&#129093;</a>
                                    </c:when>
                                    <c:otherwise>
                                        &nbsp;<a id="datedownarrow" href="<c:url value='/calllists?action=sortByDate'/>">&#129095;</a>
                                    </c:otherwise>
                                </c:choose>
                            </th>
                        </c:when>
                        <c:otherwise>
                            <th></th>
                        </c:otherwise>
                    </c:choose>
                    <th>
                        Contact Number
                    </th>
                    <th>
                        Email Address
                    </th>
                    <th>
                        Language
                    </th>
                </tr>
                <c:forEach var="patient" items="${callListPatients}">
                    <tr>
                        <c:choose>
                            <c:when test="${patient.emailAddress == null}">
                                <td>
                                    <input type="checkbox" name="emailList" disabled="disabled" value="${patient.patientId}">
                                </td>
                            </c:when>
                            <c:otherwise>
                                <td>
                                    <input type="checkbox" name="emailList" value="${patient.patientId}">
                                </td>
                            </c:otherwise>
                        </c:choose>
                        <td>
                            <c:out value="${patient.lastName}, ${patient.firstName} "/>
                        </td>
                        <td id="callliststartdate">
                            <c:out value="${patient.dateOfLastMeasurement}"/>
                        </td>
                        <c:choose>
                            <c:when test="${patient.contactNumber != null}">
                                <td id="calllistcontactnumber">
                                    <c:out value="${patient.contactNumber}"/>
                                </td>
                            </c:when>
                            <c:otherwise>
                                <td></td>
                            </c:otherwise>
                        </c:choose>
                        <c:choose>
                            <c:when test="${patient.emailAddress != null}">
                                <td id="calllistemail">
                                    <c:out value="${patient.emailAddress}"/>
                                </td>
                            </c:when>
                            <c:otherwise>
                                <td></td>
                            </c:otherwise>
                        </c:choose>
                        <c:choose>
                            <c:when test="${patient.language != null}">
                                <td>
                                    <c:out value="${patient.language}"/>
                                </td>
                            </c:when>
                            <c:otherwise>
                                <td></td>
                            </c:otherwise>
                        </c:choose>
                    </tr>
                </c:forEach>
            </table>
            <h4>Send e-mail reminders to selected patients</h4>
            <label id="calllistpasswordlabel" class="calllistpassword" for="emailPassword">Administrator Email Password:</label>
            <input class="calllistpassword" id="emailPassword" type="password" name="emailPassword" value="">
            <input class="button" id="calllistbutton" type="submit" value="Send Emails">
        </form>
    </section>
</c:if>
<jsp:include page="/includes/footer.jsp" />
