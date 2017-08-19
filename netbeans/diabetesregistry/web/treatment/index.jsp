<%-- 
    Document   : index
    Created on : Nov 8, 2015, 10:54:19 AM
    Author     : Bryan Daniel
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/includes/taskheader.jsp" />
<h2>Treatment</h2>
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
    <form class="contentText" action="treatment" method="post">
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
        <form class="contentText" action="treatment" method="post">
            <input type="hidden" name="action" value="addTreatment">
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
            <label for="datadate" class="dataentrylabel">Date reviewed (YYYY-MM-DD):</label>
            <input class="datepicker" id="datadate" type="text" name="treatmentReviewDate" value="" required>

            <input class="button" id="treatmentbutton" type="submit" value="Save Treatment">
            <table class="datatable">
                <tr><th>Prescription Class</th></tr>
                        <c:forEach var="reference" items="${references.therapies}">
                    <tr>
                        <td>
                            <input type="radio" name="rxClass" value="${reference.prescriptionClass}"><c:out value="${reference.prescriptionClass} - ${reference.therapyType}"/>
                        </td>                
                    </tr>            
                </c:forEach>
            </table>
            <table class="datatable">
                <tr><th>Medications</th></tr>
                        <c:forEach var="reference" items="${references.medications}">
                    <tr>
                        <td>
                            <input type="checkbox" name="medications" value="${reference.medicationId}"><c:out value="${reference.medicationId} - ${reference.medicationName} - ${reference.medicationClass}"/>
                        </td>            
                    </tr>            
                </c:forEach>
            </table>    
        </form>
    </section>
</c:if>
<jsp:include page="/includes/footer.jsp" />
