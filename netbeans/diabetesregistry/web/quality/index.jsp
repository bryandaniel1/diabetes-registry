<%-- 
    Document   : index
    Created on : Nov 1, 2015, 6:16:49 PM
    Author     : Bryan Daniel
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/includes/taskheader.jsp" />
<h2>Quality Checklist</h2>
<c:if test="${errorMessage != null}">
    <p class="error"><c:out value="${errorMessage}"/></p>
</c:if>
<c:if test="${message != null}">
    <p class="success"><c:out value="${message}"/></p>
</c:if>
<section class="pagecontent">
    <form class="contentText" action="quality" method="post">
        <input type="hidden" name="action" value="getClinic">
        <table class="patientselect">
            <tr>
                <td class="dataregisterlabels">
                    <label for="clinicselect" class="registerlabels">Clinic:</label>
                </td>
                <td>
                    <select name="clinicselect" onchange="this.form.submit()">
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
        </table>
    </form>
    <form class="contentText" action="quality" method="post">
        <input type="hidden" name="action" value="getPatient">
        <table class="patientselect">
            <tr>
                <td class="dataregisterlabels">
                    <label for="patientselect" class="registerlabels">Select Patient:</label>
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
        <form class="contentText" action="quality" method="post">
            <input type="hidden" name="action" value="saveChecklist">
            <input type="hidden" name="clinicId" value="${clinicId}">
            <table id="demographicTable">            
                <tr>
                    <td class="dataentry">
                        <c:out value="${patient.firstName} ${patient.lastName}"/>                
                    </td>
                </tr>
                <tr>
                    <td class="dataentry">
                        <c:out value="${patient.gender}"/>
                    </td>            
                </tr>
                <tr>
                    <td class="dataentry">
                        <c:out value="${patient.race}"/>
                    </td>
                </tr>
                <tr>
                    <td class="dataentry">
                        patient ID: <c:out value="${patient.patientId}"/>                
                    </td>
                </tr>
                <tr>
                    <td class="dataentry">
                        birth date: <c:out value="${patient.birthDate}"/>
                    </td>
                </tr>
                <c:if test="${patient.address != null}">
                    <tr>
                        <td class="dataentry">
                            address: <c:out value="${patient.address}"/>
                        </td>
                    </tr>
                </c:if>
                <tr>
                    <td class="dataentry">
                        contact number: <c:out value="${patient.contactNumber}"/>
                    </td>
                </tr>            
                <c:if test="${patient.emailAddress != null}">
                    <tr>
                        <td class="dataentry">
                            email: <c:out value="${patient.emailAddress}"/>
                        </td>
                    </tr>
                </c:if>
                <c:if test="${patient.language != null}">
                    <tr>
                        <td class="dataentry">
                            language: <c:out value="${patient.language}"/>
                        </td>
                    </tr>
                </c:if>
                <c:if test="${patient.reasonForInactivity != null}">
                    <tr>
                        <td class="dataentry">
                            inactive: <c:out value="${patient.reasonForInactivity}"/>
                        </td>
                    </tr>
                </c:if>
                <tr>
                    <td class="dataentry">
                        start date: <c:out value="${patient.startDate}"/>
                    </td>
                </tr>
            </table>
            <label for="datadate" class="dataentrylabel">Date of results (YYYY-MM-DD):</label>
            <input class="datepicker" id="datadate" type="text" name="checklistDate" value="" required>
            <input class="button" id="dataentrybutton" type="submit" value="Save Checklist">
            <table class="datatable">
                <c:forEach var="reference" items="${references.qualityReferences}" varStatus="loop">
                    <c:choose>
                        <c:when test="${((loop.index != 0)&&(references.qualityReferences[loop.index].role == references.qualityReferences[loop.index-1].role))}">
                            <tr>
                                <td>
                                    <input type="checkbox" name="list" value="${reference.responsibility}">
                                    <span>${reference.responsibility}</span><br>
                                    <c:if test="${recentChecklistItems != null}">
                                        <c:forEach var="item" items="${recentChecklistItems}">
                                            <c:if test="${item.category eq reference.responsibility}">
                                                <span class="highlighteddate">Last accomplished: <c:out value="${item.dateRecorded}"/></span>
                                            </c:if>
                                        </c:forEach>
                                    </c:if>
                                </td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <tr>
                                <th><span>${reference.role}</span></th>
                            </tr>
                            <tr>
                                <td>
                                    <input type="checkbox" name="list" value="${reference.responsibility}">
                                    <span>${reference.responsibility}</span><br>
                                    <c:if test="${recentChecklistItems != null}">
                                        <c:forEach var="item" items="${recentChecklistItems}">
                                            <c:if test="${item.category eq reference.responsibility}">
                                                <span class="highlighteddate">Last accomplished: <c:out value="${item.dateRecorded}"/></span>
                                            </c:if>
                                        </c:forEach>
                                    </c:if>
                                </td>
                            </tr>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
            </table>
        </form>
    </section>
</c:if>
<jsp:include page="/includes/footer.jsp" />
