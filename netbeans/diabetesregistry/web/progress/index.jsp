<%-- 
    Document   : index
    Created on : Feb 10, 2016, 9:11:42 PM
    Author     : Bryan Daniel
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/includes/taskheader.jsp" />
<h2>Progress Note</h2>
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
    <form class="contentText" action="progress" method="post">
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
        <table class="progressDemographicTable">        
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
        <form class="contentText" action="progress" method="post">
            <input type="hidden" name="action" value="startNewNote">
            <table class="progressTable">
                <tr>
                    <td colspan="2">
                        <label for="noteCreatedDate" class="dataentrylabel">Date of new note (YYYY-MM-DD):</label>
                        <input class="datepicker" id="noteCreatedDate" type="text" name="noteCreatedDate" value="">
                    </td>
                </tr>
            </table>
            <input class="button" id="newnotebutton" type="submit" value="Start New Note">
        </form>
        <form class="contentText" action="progress" method="post">
            <input type="hidden" name="action" value="selectPreviousNote">
            <table class="progressTable">
                <tr>
                    <td colspan="2">             
                        <label for="previousNoteDate" class="dataentrylabel">Select previous note (YYYY-MM-DD):</label>
                        <select id="previousNoteDate" name="previousNoteDate" onchange="this.form.submit()">
                            <option value="" selected disabled>Select a note</option>
                            <c:forEach var="p" items="${progressDates}">
                                <option value="${p}"><c:out value="${p}"/></option>
                            </c:forEach>
                        </select>
                    </td>
                </tr>
            </table>
        </form>
    </section>
    <c:if test="${progressNote != null}">
        <section class="pagecontent">
            <h3>Progress Note</h3>
            <h3><c:out value="${progressNote.dateCreated}"/></h3>
            <form class="contentText" action="progress" method="post">
                <input type="hidden" name="action" value="updateNote">
                <table class="dataTable">
                    <tr>
                        <td colspan="2">
                            <label class="progressCells" for="allergicToMedications">Allergic to medications:</label>
                            <c:choose>
                                <c:when test="${progressNote.allergicToMedications == true}">
                                    <select id="allergicToMedications" name="allergicToMedications">
                                        <option value="yes" selected>yes</option>
                                        <option value="no">no</option>
                                    </select>
                                </c:when>
                                <c:otherwise>
                                    <select id="allergicToMedications" name="allergicToMedications">
                                        <option value="yes">yes</option>
                                        <option value="no" selected>no</option>
                                    </select>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td colspan="4">
                            <label class="progressCells" for="allergies">Allergies:</label>
                            <input id="allergies" type="text" name="allergies" size="30" value="<c:out value="${progressNote.allergies}"/>">
                        </td>      
                    </tr>
                    <tr>
                        <td colspan="6">
                            <label class="progressCells" for="progressMedications">Current Medications:</label>
                            <input id="progressMedications" type="text" name="progressMedications" size="46" value="<c:out value="${progressNote.medications}"/>">            
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2">
                            <label class="progressCells" for="medicalInsurance">Medical Insurance:</label>
                            <c:choose>
                                <c:when test="${progressNote.medicalInsurance == true}">
                                    <select id="medicalInsurance" name="medicalInsurance">
                                        <option value="yes" selected>yes</option>
                                        <option value="no">no</option>
                                    </select>
                                </c:when>
                                <c:otherwise>
                                    <select id="medicalInsurance" name="medicalInsurance">
                                        <option value="yes">yes</option>
                                        <option value="no" selected>no</option>
                                    </select>
                                </c:otherwise>
                            </c:choose> 
                        </td>
                        <td colspan="2">
                            <label for="shoeSize" class="progressCells">Shoe Size:</label>
                            <input class="datacells" id="shoeSize" type="text" name="shoeSize" value="<c:out value="${progressNote.shoeSize}"/>">
                        </td>
                        <td colspan="2">
                            <label for="weight" class="progressCells">Weight(lbs):</label>
                            <input class="datacells" id="weight" type="text" name="weight" value="<c:out value="${progressNote.weight}"/>">
                        </td>
                    </tr>
                    <tr>            
                        <td colspan="2">
                            <label for="heightFeet" class="progressCells">Height(feet):</label>
                            <input class="datacells" id="heightFeet" type="text" name="heightFeet" value="<c:out value="${progressNote.heightFeet}"/>">
                        </td>
                        <td colspan="2">
                            <label for="heightInches" class="progressCells">Height(inches):</label>
                            <input class="datacells" id="heightInches" type="text" name="heightInches" value="<c:out value="${progressNote.heightInches}"/>">
                        </td>
                        <td colspan="2">
                        </td>
                    </tr>
                    <tr>
                        <td colspan="4">
                            <span class="progressText">Calculate BMI as &#40;lbs&divide;height&#40;inches&#41;&sup2;&#41;&times;703</span>                            
                        </td>
                        <td colspan="2">
                            <label for="bmi" class="progressCells">BMI:</label>
                            <c:choose>
                                <c:when test="${progressNote.bmi != null}">
                                    <input class="datacells" id="bmi" type="text" name="bmi" value="<c:out value="${progressNote.bmi}"/>" onfocus="calculatebmi();">
                                </c:when>
                                <c:otherwise>
                                    <input class="datacells" id="bmi" type="text" name="bmi" value="" onfocus="calculatebmi();">
                                </c:otherwise>
                            </c:choose>                
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2" id="weightreductionlabel">
                            <label for="weightreduction" class="progressCells">If BMI>25 then establish 7% weight reduction goal(lbs) over 16 weeks:</label>
                        </td>
                        <td id="weightreduction">
                            <input class="datacells2" id="weightreductioninput" type="text" name="weightReduction" value="<c:out value="${progressNote.weightReductionGoal}"/>">
                        </td>
                        <td colspan="2" id="waistprogresslabel">
                            <label for="waistprogress" class="progressCells">If BMI<35 then measure waist circumference (inches):</label>
                        </td>
                        <td id="waistprogress">
                            <c:choose>
                                <c:when test="${progressNote.waist != null}">
                                    <input class="datacells" type="text" name="waist" value="<c:out value="${progressNote.waist}"/>">
                                </c:when>
                                <c:otherwise>
                                    <input class="datacells" type="text" name="waist" value="">
                                </c:otherwise>
                            </c:choose>                
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2">
                            <table id="bptable">
                                <c:choose>
                                    <c:when test="${progressNote.bloodPressureSystole != null}">
                                        <tr>
                                            <td><label for="bpsystole" class="progressCells" id="bplabel">BP:</label></td>
                                            <td id="bpsystolecell">
                                                <input class="datacells" id="bpsystole" type="text" name="bpSystole" value="<c:out value="${progressNote.bloodPressureSystole}"/>">
                                            </td>
                                            <td class="bpunits">mmHg</td>
                                        </tr>
                                        <tr>
                                            <td></td>
                                            <td>
                                                <input class="datacells" id="bpdiastole" type="text" name="bpDiastole" value="<c:out value="${progressNote.bloodPressureDiastole}"/>">
                                            </td>
                                            <td class="bpunits">mmHg</td>
                                        </tr>
                                    </c:when>
                                    <c:otherwise>
                                        <tr>
                                            <td><label for="bpsystole" class="progressCells" id="bplabel">BP:</label></td>
                                            <td id="bpsystolecell">
                                                <input class="datacells" id="bpsystole" type="text" name="bpSystole" value="">
                                            </td>
                                            <td class="bpunits">mmHg</td>
                                        </tr>
                                        <tr>
                                            <td></td>
                                            <td>
                                                <input class="datacells" id="bpdiastole" type="text" name="bpDiastole" value="">
                                            </td>
                                            <td class="bpunits">mmHg</td>
                                        </tr>
                                    </c:otherwise>
                                </c:choose>
                                <tr>
                                    <td><label for="aceorarb" class="progressCells" id="aceorarblabel">ACE or ARB:</label></td>                                    
                                    <c:choose>
                                        <c:when test="${progressNote.aceOrArb == true}">
                                            <td><input  class="dataentrycheckbox" id="aceorarb" type="checkbox" name="aceOrArb" value="1" checked></td>
                                        </c:when>
                                        <c:otherwise>
                                            <td><input  class="dataentrycheckbox" id="aceorarb" type="checkbox" name="aceOrArb" value="1"></td>
                                        </c:otherwise>
                                    </c:choose>
                                    <td></td>
                                </tr>
                            </table>
                        </td>
                        <td colspan="2">
                            <label for="pulse" class="progressCells">Pulse:</label>
                            <input class="datacells" id="pulse" type="text" name="pulse" value="<c:out value="${progressNote.pulse}"/>">
                        </td>
                        <td colspan="2">
                            <label for="respirations" class="progressCells">Respirations:</label>
                            <input class="datacells" id="respirations" type="text" name="respirations" value="<c:out value="${progressNote.respirations}"/>">
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2">
                            <label for="temperature" class="progressCells">Temperature:</label>
                            <input class="datacells" id="temperature" type="text" name="temperature" value="<c:out value="${progressNote.temperature}"/>">
                        </td>
                        <td colspan="2">
                            <label class="progressCells" for="footScreening">Foot Screening:</label>
                            <c:choose>
                                <c:when test="${progressNote.footScreening == true}">
                                    <select id="footScreening" name="footScreening">
                                        <option value="yes" selected>yes</option>
                                        <option value="no">no</option>
                                    </select>
                                </c:when>
                                <c:otherwise>
                                    <select id="footScreening" name="footScreening">
                                        <option value="yes">yes</option>
                                        <option value="no" selected>no</option>
                                    </select>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td colspan="2"></td>
                    </tr>
                    <tr>
                        <td colspan="4">
                            <label for="glucose" class="datacells">Average Weekly Fasting Glucose:</label>
                            <input class="datacells" id="glucose" type="text" name="glucose" value="<c:out value="${progressNote.glucose}"/>"><span id="glucoseunits">mg/dl</span>                
                        </td>
                        <td colspan="2">
                            <label for="a1c" class="datacells">POC A1C:</label>
                            <input class="datacells" id="a1c" type="text" name="a1c" value="<c:out value="${progressNote.a1c}"/>"><span id="a1cunits">&#37;</span>                                
                        </td>
                    </tr>
                </table>
                <table class="datatable" id="screening">
                    <tr>
                        <c:choose>
                            <c:when test="${progressNote.eyeScreeningCategory != null}">
                                <td colspan="2">
                                    <label for="eye" class="datacells2">Eye Screening Result:</label>
                                    <select id="eye" name="eyeScreening">
                                        <option value="${progressNote.eyeScreeningCategory}" selected><c:out value="${progressNote.eyeScreeningCategory}"/></option>
                                        <c:forEach var="reference" items="${references.eyeExamDefinitions}">
                                            <option value="${reference.code}"><c:out value="${reference.code} - ${reference.definition}"/></option>
                                        </c:forEach>
                                    </select>
                                </td>
                            </c:when>
                            <c:otherwise>
                                <td colspan="2">
                                    <label for="eye" class="datacells2">Eye Screening Result:</label>
                                    <select id="eye" name="eyeScreening">
                                        <option value="" selected disabled>Select a score</option>
                                        <c:forEach var="reference" items="${references.eyeExamDefinitions}">
                                            <option value="${reference.code}"><c:out value="${reference.code} - ${reference.definition}"/></option>
                                        </c:forEach>
                                    </select>
                                </td>                    
                            </c:otherwise>
                        </c:choose>
                    </tr>
                    <tr>
                        <c:choose>
                            <c:when test="${progressNote.footScreeningCategory != null}">
                                <td colspan="2">
                                    <label for="footResult" class="datacells2">Foot Screening Result:</label>
                                    <select id="footResult" name="footScreeningResult">
                                        <option value="${progressNote.footScreeningCategory}" selected><c:out value="${progressNote.footScreeningCategory}"/></option>
                                        <c:forEach var="reference" items="${references.footExamRiskDefinitions}">
                                            <option value="${reference.riskCategory}"><c:out value="${reference.riskCategory}"/></option>
                                        </c:forEach>
                                    </select>
                                </td>
                            </c:when>
                            <c:otherwise>
                                <td colspan="2">
                                    <label for="footResult" class="datacells2">Foot Screening Result:</label>
                                    <select id="footResult" name="footScreeningResult">
                                        <option value="" selected disabled>Select a score</option>
                                        <c:forEach var="reference" items="${references.footExamRiskDefinitions}">
                                            <option value="${reference.riskCategory}"><c:out value="${reference.riskCategory}"/></option>
                                        </c:forEach>
                                    </select>
                                </td>
                            </c:otherwise>
                        </c:choose>            
                    </tr>
                    <tr>
                        <c:choose>
                            <c:when test="${progressNote.psychologicalScreening != null}">
                                <td colspan="2">
                                    <label for="psychological" class="datacells2">Psychological Screening:</label>
                                    <select id="psychological" name="psychologicalScreening">
                                        <option value="${progressNote.psychologicalScreening}" selected><c:out value="${progressNote.psychologicalScreening}"/></option>
                                        <c:forEach var="reference" items="${references.psychologicalScreeningReferences}">
                                            <option value="${reference.score}"><c:out value="${reference.score}"/></option>
                                        </c:forEach>
                                    </select>
                                </td>
                            </c:when>
                            <c:otherwise>
                                <td colspan="2">
                                    <label for="psychological" class="datacells2">Psychological Screening:</label>
                                    <select id="psychological" name="psychologicalScreening">
                                        <option value="" selected disabled>Select a score</option>
                                        <c:forEach var="reference" items="${references.psychologicalScreeningReferences}">
                                            <option value="${reference.score}"><c:out value="${reference.score}"/></option>
                                        </c:forEach>
                                    </select>
                                </td>
                            </c:otherwise>
                        </c:choose>            
                    </tr>
                </table>
                <table class="datatable">
                    <tr>
                        <td>
                            <c:if test="${progressNote.lastClassDate != null}">
                                <span class="highlighteddate" id="lastclassdate">Last class attended: <c:out value="${progressNote.lastClassDate}"/></span><br>
                            </c:if>
                            <label for="classdate" class="vaccineentrydate">Class Attended (YYYY-MM-DD)<br>(input only):</label>
                            <input class="datepicker" id="classdate" type="text" name="classDate" value="">
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label for="erdate" class="vaccineentrydate">Hospitalization (YYYY-MM-DD)<br>(input only):</label>
                            <input class="datepicker" id="erdate" type="text" name="erDate" value="">
                        </td>
                    </tr>
                </table>
                <table>
                    <tr>
                        <td>
                            <label for="smoking">Smoking:</label>
                            <c:choose>
                                <c:when test="${progressNote.smoking != null}">
                                    <c:choose>
                                        <c:when test="${progressNote.smoking == true}">
                                            <select id="smoking" name="smoking">
                                                <option value="yes" selected>yes</option>
                                                <option value="no">no</option>
                                            </select>
                                        </c:when>
                                        <c:otherwise>
                                            <select id="smoking" name="smoking">
                                                <option value="yes">yes</option>
                                                <option value="no" selected>no</option>
                                            </select>
                                        </c:otherwise>
                                    </c:choose>                
                                </c:when>
                                <c:otherwise>
                                    <select id="smoking" name="smoking">
                                        <option value="" selected disabled>Select a status</option>
                                        <option value="yes">yes</option>
                                        <option value="no">no</option>
                                    </select>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <label for="compliance" class="datacells">Compliance (0-10):</label>
                            <input class="datacells" id="compliance" type="text" name="compliance" value="<c:out value="${progressNote.compliance}"/>">                
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2">
                            <label for="physicalactivity" class="datacells">Physical Activity (min/week):</label>
                            <input class="datacells" id="physicalactivity" type="text" name="physicalActivity" value="<c:out value="${progressNote.physicalActivity}"/>">
                        </td>
                    </tr>
                </table>
                <table class="dataTable">
                    <tr>
                        <td>
                            <label for="nurseOrDietitianNote">Nurse/Dietitian Notes(1000 char max):</label>                
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <textarea cols="60" rows="4" id="nurseOrDietitianNote" name="nurseOrDietitianNote"><c:out value="${progressNote.nurseOrDietitianNote}"/></textarea>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label for="subjective">Subjective(1000 char max):</label>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <textarea cols="60" rows="4" id="subjective" name="subjective"><c:out value="${progressNote.subjective}"/></textarea>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label for="objective">Objective(1000 char max):</label>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <textarea cols="60" rows="4" id="objective" name="objective"><c:out value="${progressNote.objective}"/></textarea>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label for="assessment">Assessment(1000 char max):</label>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <textarea cols="60" rows="4" id="assessment" name="assessment"><c:out value="${progressNote.assessment}"/></textarea>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label for="plan">Plan(1000 char max):</label>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <textarea cols="60" rows="4" id="plan" name="plan"><c:out value="${progressNote.plan}"/></textarea>
                        </td>
                    </tr>
                    <c:forEach var="author" items="${progressNote.updatedBy}">
                        <tr>
                            <td>
                                <span>Updated by:&nbsp;<c:out value="${author.firstName}"/>
                                    &nbsp;<c:out value="${author.lastName}"/>&comma;
                                    &nbsp;<c:out value="${author.jobTitle}"/>&comma;
                                    &nbsp;<c:out value="${author.timeStamp}"/></span>
                            </td>
                        </tr>
                    </c:forEach>
                </table>
                <input class="button" id="dataentrybutton" type="submit" value="Save Note">        
            </form>
            <a class="button" id="viewpdfbutton" href="<c:url value='/viewpdf'/>">View PDF</a>
        </section>
    </c:if>
</c:if>
<jsp:include page="/includes/footer.jsp" />
