<%-- 
    Document   : index
    Created on : Nov 10, 2015, 9:53:05 PM
    Author     : Bryan Daniel
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<jsp:include page="/includes/taskheader.jsp" />
<h2>Patient History</h2>
<c:if test="${errorMessage != null}">
    <p class="error"><c:out value="${errorMessage}"/></p>
</c:if>
<c:if test="${message != null}">
    <p class="success"><c:out value="${message}"/></p>
</c:if>
<section class="pagecontent">
    <form class="contentText" action="history" method="post">
        <input type="hidden" name="action" value="getClinic">
        <table class="patientselect">
            <tr>
                <td class="dataregisterlabels">
                    <label for="clinicselect">Clinic:</label>
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
    <form class="contentText" action="history" method="post">
        <input type="hidden" name="action" value="getPatient">
        <table class="patientselect">
            <tr>
                <td class="dataregisterlabels">
                    <label for="patientselect">Select Patient:</label>
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
        <form class="contentText" action="history" method="post">
            <input type="hidden" name="action" value="historyOption">
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
            <table id="historyselecttable">
                <tr>
                    <td class="dataregisterlabels">
                        <label for="historyselection">History Selection:</label>
                    </td>
                    <td>
                        <select id="historyselection" name="historySelect" onchange="this.form.submit()">
                            <option value="" selected disabled>Select an option</option>           
                            <option value="a1c">A1C</option>          
                            <option value="alt">ALT</option>
                            <option value="ast">AST</option>
                            <option value="bloodPressure">blood pressure</option>
                            <option value="BMI">BMI</option>
                            <option value="checklists">checklists</option>
                            <option value="class">class attendance</option>
                            <option value="creatinine">creatinine</option>           
                            <option value="eGFR">eGFR</option>
                            <option value="eye">eye screening</option>
                            <option value="foot">foot screening</option>
                            <option value="glucose">glucose</option>           
                            <option value="HDL">HDL</option>
                            <option value="hepb">hepatitis B vaccine</option>
                            <option value="hospitalization">hospitalization</option>
                            <option value="influenza">influenza vaccine</option>
                            <option value="LDL">LDL</option>
                            <option value="notes">notes</option>
                            <option value="dashboard">patient dashboard</option>
                            <option value="compliance">patient-reported compliance</option>
                            <option value="PCV-13">PCV-13 vaccine</option>
                            <option value="physicalActivity">physical activity</option>
                            <option value="PPSV-23">PPSV-23 vaccine</option>
                            <option value="psa">PSA</option>
                            <option value="psychological">psychological screening</option>
                            <option value="smoking">smoking</option>
                            <option value="T4">T4</option>
                            <option value="TDAP">TDAP vaccine</option>
                            <option value="telephone">telephone follow-up</option>
                            <option value="treatment">treatment</option>
                            <option value="triglycerides">triglycerides</option>
                            <option value="TSH">TSH</option>           
                            <option value="UACR">UACR</option>          
                            <option value="waist">waist</option>          
                            <option value="zoster">zoster vaccine</option>
                        </select>
                    </td>
                </tr>
            </table>
        </form>
        <table class="historytable">
            <c:choose>
                <c:when test="${a1cHistory != null}">
                    <tr>
                        <th colspan="2">
                            A1C
                        </th>
                    </tr>
                    <tr>
                        <td>
                            date
                        </td>
                        <td>
                            result<span class="historyunits">&nbsp;&#40;&#37;&#41;</span>
                        </td>
                    </tr>
                    <c:forEach var="result" items="${a1cHistory}">
                        <tr>
                            <td>
                                <c:out value="${result.date}"></c:out>
                                </td>                
                                <td>
                                <c:choose>
                                    <c:when test="${result.poc == true}">
                                        <c:out value="${result.value}"></c:out>&nbsp;
                                            POC
                                    </c:when>
                                    <c:otherwise>
                                        <c:out value="${result.value}"></c:out>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    </c:forEach>        
                </c:when>
                <c:when test="${psaHistory != null}">
                    <tr>
                        <th colspan="2">
                            PSA
                        </th>
                    </tr>
                    <tr>
                        <td>
                            date
                        </td>
                        <td>
                            result<span class="historyunits">&nbsp;&#40;ng/mL&#41;</span>
                        </td>
                    </tr>
                    <c:forEach var="result" items="${psaHistory}">
                        <tr>
                            <td>
                                <c:out value="${result.date}"></c:out>
                                </td>                
                                <td>
                                <c:out value="${result.value}"></c:out>
                                </td>
                            </tr>
                    </c:forEach>        
                </c:when>
                <c:when test="${altHistory != null}">
                    <tr>
                        <th colspan="2">
                            ALT
                        </th>
                    </tr>
                    <tr>
                        <td>
                            date
                        </td>
                        <td>
                            result<span class="historyunits">&nbsp;&#40;U/L&#41;</span>
                        </td>
                    </tr>
                    <c:forEach var="result" items="${altHistory}">
                        <tr>
                            <td>
                                <c:out value="${result.date}"></c:out>
                                </td>                
                                <td>
                                <c:out value="${result.value}"></c:out>
                                </td>
                            </tr>
                    </c:forEach>        
                </c:when>
                <c:when test="${astHistory != null}">
                    <tr>
                        <th colspan="2">
                            AST
                        </th>
                    </tr>
                    <tr>
                        <td>
                            date
                        </td>
                        <td>
                            result<span class="historyunits">&nbsp;&#40;U/L&#41;</span>
                        </td>
                    </tr>
                    <c:forEach var="result" items="${astHistory}">
                        <tr>
                            <td>
                                <c:out value="${result.date}"></c:out>
                                </td>                
                                <td>
                                <c:out value="${result.value}"></c:out>
                                </td>
                            </tr>
                    </c:forEach>        
                </c:when>
                <c:when test="${bpHistory != null}">
                    <tr>
                        <th colspan="2">
                            blood pressure
                        </th>
                    </tr>
                    <tr>
                        <td>
                            date
                        </td>
                        <td>
                            result<span class="historyunits">&nbsp;&#40;mmHg&#41;</span>
                        </td>
                    </tr>
                    <c:forEach var="result" items="${bpHistory}">
                        <tr>
                            <td>
                                <c:out value="${result.date}"></c:out>
                                </td> 
                            <c:choose>
                                <c:when test="${result.aceOrArb == true}">
                                    <td>
                                        <c:out value="${result.systolicValue}"></c:out>/
                                        <c:out value="${result.diastolicValue}"></c:out>&nbsp;
                                            Patient on ACE or ARB.
                                        </td>
                                </c:when>
                                <c:otherwise>
                                    <td>
                                        <c:out value="${result.systolicValue}"></c:out>/
                                        <c:out value="${result.diastolicValue}"></c:out>
                                        </td>
                                </c:otherwise>
                            </c:choose>
                        </tr>
                    </c:forEach>        
                </c:when>
                <c:when test="${bmiHistory != null}">
                    <tr>
                        <th colspan="2">
                            BMI
                        </th>
                    </tr>
                    <tr>
                        <td>
                            date
                        </td>
                        <td>
                            result
                        </td>
                    </tr>
                    <c:forEach var="result" items="${bmiHistory}">
                        <tr>
                            <td>
                                <c:out value="${result.date}"></c:out>
                                </td>                
                                <td>
                                <c:out value="${result.value}"></c:out>
                                </td>
                            </tr>
                    </c:forEach>
                </c:when>
                <c:when test="${checklistDates != null}">
                </table>    
                <form class="contentText" action="history" method="post">
                    <input type="hidden" name="action" value="qualityChecklist">
                    <table class="historytable">
                        <tr>
                            <td class="dataregisterlabels">
                                <label for="checklistdateselection">Quality Checklist Dates:</label>
                            </td>
                            <td>
                                <select id="checklistdateselection" name="checklistSelect" class="historyselect" onchange="this.form.submit()">
                                    <option value="" selected disabled>Select a date</option>
                                    <c:forEach var="checklistdate" items="${checklistDates}">
                                        <option value="${checklistdate}">
                                            <c:out value="${checklistdate}"></c:out>
                                            </option>
                                    </c:forEach>
                                </select>
                            </td>
                        </tr>
                    </table>
                </form>
                <table>
                </c:when>
                <c:when test="${checklistItems != null}">
                    <tr>
                        <th colspan="2">
                            quality checklist tasks accomplished on <c:out value="${checklistDate}"></c:out>
                            </th>
                        </tr>
                    <c:forEach var="result" items="${checklistItems}" varStatus="loop">
                        <c:choose>
                            <c:when test="${((loop.index != 0)&&(checklistItems[loop.index].role == checklistItems[loop.index-1].role))}">
                                <tr>
                                    <td>
                                        <c:out value="${result.responsibility}"></c:out>                               
                                        </td>
                                    </tr>
                            </c:when>
                            <c:otherwise>
                                <tr>
                                    <th><span>${result.role}</span></th>
                                </tr>
                                <tr>
                                    <td>
                                        <c:out value="${result.responsibility}"></c:out>                            
                                        </td>
                                    </tr>
                            </c:otherwise>
                        </c:choose>            
                    </c:forEach>
                </c:when>
                <c:when test="${classHistory != null}">
                    <tr>
                        <th colspan="2">
                            class attendance
                        </th>
                    </tr>
                    <tr>
                        <td colspan="2">
                            total classes attended:
                            <c:out value="${fn:length(classHistory)}"></c:out>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                date
                            </td><td></td>
                        </tr>
                    <c:forEach var="result" items="${classHistory}">
                        <tr>
                            <td>
                                <c:out value="${result}"></c:out>
                                </td><td></td>
                            </tr>
                    </c:forEach>        
                </c:when>
                <c:when test="${creatinineHistory != null}">
                    <tr>
                        <th colspan="2">
                            creatinine
                        </th>
                    </tr>
                    <tr>
                        <td>
                            date
                        </td>
                        <td>
                            result<span class="historyunits">&nbsp;&#40;mg/dl&#41;</span>
                        </td>
                    </tr>
                    <c:forEach var="result" items="${creatinineHistory}">
                        <tr>
                            <td>
                                <c:out value="${result.date}"></c:out>
                                </td>                
                                <td>
                                <c:out value="${result.value}"></c:out>
                                </td>
                            </tr>
                    </c:forEach>        
                </c:when>
                <c:when test="${egfrHistory != null}">
                    <tr>
                        <th colspan="2">
                            eGFR
                        </th>
                    </tr>
                    <tr>
                        <td>
                            date
                        </td>
                        <td>
                            result
                        </td>
                    </tr>
                    <c:forEach var="result" items="${egfrHistory}">
                        <tr>
                            <td>
                                <c:out value="${result.date}"></c:out>
                                </td>                
                                <td>
                                <c:out value="${result.value}"></c:out>
                                </td>
                            </tr>
                    </c:forEach>        
                </c:when>
                <c:when test="${eyeHistory != null}">
                    <tr>
                        <th colspan="3">
                            eye screening
                        </th>
                    </tr>
                    <tr>
                        <td>
                            date
                        </td>
                        <td>
                            category
                        </td>
                        <td>
                            definition
                        </td>
                    </tr>
                    <c:forEach var="result" items="${eyeHistory}">
                        <tr>
                            <td>
                                <c:out value="${result.dateRecorded}"></c:out>
                                </td>                
                                <td>
                                <c:out value="${result.category}"></c:out>
                                </td>
                                <td>
                                <c:out value="${result.definition}"></c:out>
                                </td>
                            </tr>
                    </c:forEach>        
                </c:when>
                <c:when test="${footHistory != null}">
                    <tr>
                        <th colspan="3">
                            foot screening
                        </th>
                    </tr>
                    <tr>
                        <td>
                            date
                        </td>
                        <td>
                            category
                        </td>
                        <td>
                            definition
                        </td>
                    </tr>
                    <c:forEach var="result" items="${footHistory}">
                        <tr>
                            <td>
                                <c:out value="${result.dateRecorded}"></c:out>
                                </td>                
                                <td>
                                <c:out value="${result.category}"></c:out>
                                </td>
                                <td>
                                <c:out value="${result.definition}"></c:out>
                                </td>
                            </tr>
                    </c:forEach>        
                </c:when>
                <c:when test="${glucoseHistory != null}">
                    <tr>
                        <th colspan="2">
                            glucose
                        </th>
                    </tr>
                    <tr>
                        <td>
                            date
                        </td>
                        <td>
                            result<span class="historyunits">&nbsp;&#40;mg/dl&#41;</span>
                        </td>
                    </tr>
                    <c:forEach var="result" items="${glucoseHistory}">
                        <tr>
                            <td>
                                <c:out value="${result.date}"></c:out>
                                </td>                
                                <td>                
                                <c:out value="${result.value}"></c:out>
                                </td>
                            </tr>
                    </c:forEach>        
                </c:when>
                <c:when test="${hdlHistory != null}">
                    <tr>
                        <th colspan="2">
                            HDL
                        </th>
                    </tr>
                    <tr>
                        <td>
                            date
                        </td>
                        <td>
                            result<span class="historyunits">&nbsp;&#40;mg/dl&#41;</span>
                        </td>
                    </tr>
                    <c:forEach var="result" items="${hdlHistory}">
                        <tr>
                            <td>
                                <c:out value="${result.date}"></c:out>
                                </td>                
                                <td>
                                <c:out value="${result.value}"></c:out>
                                </td>
                            </tr>
                    </c:forEach>        
                </c:when>
                <c:when test="${hepBHistory != null}">
                    <tr>
                        <th colspan="2">
                            hepatitis B vaccination
                        </th>
                    </tr>
                    <tr>
                        <td>
                            date
                        </td><td></td>
                    </tr>
                    <c:forEach var="result" items="${hepBHistory}">
                        <tr>
                            <td>
                                <c:out value="${result}"></c:out>
                                </td><td></td>
                            </tr>
                    </c:forEach>        
                </c:when>
                <c:when test="${hospitalizationHistory != null}">
                    <tr>
                        <th colspan="2">
                            hospitalization
                        </th>
                    </tr>
                    <tr>
                        <td>
                            date
                        </td><td></td>
                    </tr>
                    <c:forEach var="result" items="${hospitalizationHistory}">
                        <tr>
                            <td>
                                <c:out value="${result}"></c:out>
                                </td><td></td>
                            </tr>
                    </c:forEach>        
                </c:when>
                <c:when test="${influenzaHistory != null}">
                    <tr>
                        <th colspan="2">
                            influenza vaccination
                        </th>
                    </tr>
                    <tr>
                        <td>
                            date
                        </td><td></td>
                    </tr>
                    <c:forEach var="result" items="${influenzaHistory}">
                        <tr>
                            <td>
                                <c:out value="${result}"></c:out>
                                </td><td></td>
                            </tr>
                    </c:forEach>        
                </c:when>
                <c:when test="${ldlHistory != null}">
                    <tr>
                        <th colspan="2">
                            LDL
                        </th>
                    </tr>
                    <tr>
                        <td>
                            date
                        </td>
                        <td>
                            result<span class="historyunits">&nbsp;&#40;mg/dl&#41;</span>
                        </td>
                    </tr>
                    <c:forEach var="result" items="${ldlHistory}">
                        <tr>
                            <td>
                                <c:out value="${result.date}"></c:out>
                                </td>                
                                <td>
                                <c:out value="${result.value}"></c:out>&nbsp;
                                <c:if test="${result.postMi == true}">
                                    post MI&nbsp;
                                </c:if>
                                <c:if test="${result.onStatin == true}">
                                    on statin
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>        
                </c:when>
                <c:when test="${complianceHistory != null}">
                    <tr>
                        <th colspan="2">
                            patient-reported compliance
                        </th>
                    </tr>
                    <tr>
                        <td>
                            date
                        </td>
                        <td>
                            result
                        </td>
                    </tr>
                    <c:forEach var="result" items="${complianceHistory}">
                        <tr>
                            <td>
                                <c:out value="${result.date}"></c:out>
                                </td>                
                                <td>
                                <c:out value="${result.value}"></c:out>
                                </td>
                            </tr>
                    </c:forEach>        
                </c:when>
                <c:when test="${pcv13History != null}">
                    <tr>
                        <th colspan="2">
                            PCV-13 vaccine
                        </th>
                    </tr>
                    <tr>
                        <td>
                            date
                        </td><td></td>
                    </tr>
                    <c:forEach var="result" items="${pcv13History}">
                        <tr>
                            <td>
                                <c:out value="${result}"></c:out>
                                </td><td></td>
                            </tr>
                    </c:forEach>        
                </c:when>
                <c:when test="${physicalActivityHistory != null}">
                    <tr>
                        <th colspan="2">
                            minutes of weekly physical activity
                        </th>
                    </tr>
                    <tr>
                        <td>
                            date
                        </td>
                        <td>
                            result
                        </td>
                    </tr>
                    <c:forEach var="result" items="${physicalActivityHistory}">
                        <tr>
                            <td>
                                <c:out value="${result.date}"></c:out>
                                </td>                
                                <td>
                                <c:out value="${result.value}"></c:out>
                                </td>
                            </tr>
                    </c:forEach>        
                </c:when>
                <c:when test="${ppsv23History != null}">
                    <tr>
                        <th colspan="2">
                            PPSV-23 vaccine
                        </th>
                    </tr>
                    <tr>
                        <td>
                            date
                        </td><td></td>
                    </tr>
                    <c:forEach var="result" items="${ppsv23History}">
                        <tr>
                            <td>
                                <c:out value="${result}"></c:out>
                                </td><td></td>
                            </tr>
                    </c:forEach>        
                </c:when>
                <c:when test="${psychologicalHistory != null}">
                    <tr>
                        <th colspan="4">
                            psychological screening
                        </th>
                    </tr>
                    <tr>
                        <td>
                            date
                        </td>
                        <td>
                            score
                        </td>
                        <td>
                            severity
                        </td>
                        <td>
                            proposed actions
                        </td>
                    </tr>
                    <c:forEach var="result" items="${psychologicalHistory}">
                        <tr>
                            <td>
                                <c:out value="${result.date}"></c:out>
                                </td>                
                                <td>
                                <c:out value="${result.score}"></c:out>
                                </td>
                                <td>
                                <c:out value="${result.severity}"></c:out>
                                </td>
                                <td>
                                <c:out value="${result.proposedActions}"></c:out>
                                </td>
                            </tr>
                    </c:forEach>        
                </c:when>
                <c:when test="${smokingHistory != null}">
                    <tr>
                        <th colspan="2">
                            smoking status
                        </th>
                    </tr>
                    <tr>
                        <td>
                            date
                        </td>
                        <td>
                            result
                        </td>
                    </tr>
                    <c:forEach var="result" items="${smokingHistory}">
                        <tr>
                            <td>
                                <c:out value="${result.date}"></c:out>
                                </td>                
                                <td>
                                <c:choose>
                                    <c:when test="${result.value == true}">
                                        smoking
                                    </c:when>
                                    <c:otherwise>
                                        not smoking
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    </c:forEach>        
                </c:when>
                <c:when test="${t4History != null}">
                    <tr>
                        <th colspan="2">
                            T4
                        </th>
                    </tr>
                    <tr>
                        <td>
                            date
                        </td>
                        <td>
                            result
                        </td>
                    </tr>
                    <c:forEach var="result" items="${t4History}">
                        <tr>
                            <td>
                                <c:out value="${result.date}"></c:out>
                                </td>                
                                <td>
                                <c:out value="${result.value}"></c:out>
                                </td>
                            </tr>
                    </c:forEach>        
                </c:when>
                <c:when test="${tdapHistory != null}">
                    <tr>
                        <th colspan="2">
                            TDAP vaccine
                        </th>
                    </tr>
                    <tr>
                        <td>
                            date
                        </td><td></td>
                    </tr>
                    <c:forEach var="result" items="${tdapHistory}">
                        <tr>
                            <td>
                                <c:out value="${result}"></c:out>
                                </td><td></td>
                            </tr>
                    </c:forEach>        
                </c:when>
                <c:when test="${rxHistory != null}">
                    <tr>
                        <th colspan="4">
                            Therapies
                        </th>
                    </tr>
                    <tr>
                        <th>
                            RX class
                        </th>
                        <th>
                            therapy type
                        </th>
                        <th></th>
                        <th>
                            date reviewed
                        </th>
                    </tr>
                    <c:forEach var="tr" items="${rxHistory}">
                        <tr>
                            <td>
                                <c:out value="${tr.prescriptionClass}"></c:out>
                                </td>                
                                <td>
                                <c:out value="${tr.therapyType}"></c:out>
                                </td>
                                <td></td>
                                <td>
                                <c:out value="${tr.dateReviewed}"></c:out>
                                </td>
                            </tr>
                    </c:forEach>
                    <tr>
                        <th colspan="4">
                            Medications
                        </th>     
                    </tr>
                    <tr>           
                        <th>
                            med ID
                        </th>
                        <th>
                            med name
                        </th>
                        <th>
                            med class
                        </th>
                        <th>
                            date reviewed
                        </th>
                    </tr>
                    <c:forEach var="tr" items="${medHistory}">
                        <tr>
                            <td>
                                <c:out value="${tr.medicationId}"></c:out>
                                </td>                
                                <td>
                                <c:out value="${tr.medicationName}"></c:out>
                                </td>
                                <td>
                                <c:out value="${tr.medicationClass}"></c:out>
                                </td>
                                <td>
                                <c:out value="${tr.dateReviewed}"></c:out>
                                </td>
                            </tr>
                    </c:forEach>
                </c:when>
                <c:when test="${telephoneHistory != null}">
                    <tr>
                        <th colspan="3">
                            telephone follow-up
                        </th>
                    </tr>
                    <tr>
                        <td>
                            date
                        </td>
                        <td>
                            code
                        </td>
                        <td>
                            definition
                        </td>
                    </tr>
                    <c:forEach var="result" items="${telephoneHistory}">
                        <tr>
                            <td>
                                <c:out value="${result.dateRecorded}"></c:out>
                                </td>                
                                <td>
                                <c:out value="${result.category}"></c:out>
                                </td>
                                <td>
                                <c:out value="${result.definition}"></c:out>
                                </td>
                            </tr>
                    </c:forEach>        
                </c:when>
                <c:when test="${triglyceridesHistory != null}">
                    <tr>
                        <th colspan="2">
                            triglycerides
                        </th>
                    </tr>
                    <tr>
                        <td>
                            date
                        </td>
                        <td>
                            result<span class="historyunits">&nbsp;&#40;mg/dl&#41;</span>
                        </td>
                    </tr>
                    <c:forEach var="result" items="${triglyceridesHistory}">
                        <tr>
                            <td>
                                <c:out value="${result.date}"></c:out>
                                </td>                
                                <td>
                                <c:out value="${result.value}"></c:out>
                                </td>
                            </tr>
                    </c:forEach>        
                </c:when>
                <c:when test="${tshHistory != null}">
                    <tr>
                        <th colspan="2">
                            TSH
                        </th>
                    </tr>
                    <tr>
                        <td>
                            date
                        </td>
                        <td>
                            result
                        </td>
                    </tr>
                    <c:forEach var="result" items="${tshHistory}">
                        <tr>
                            <td>
                                <c:out value="${result.date}"></c:out>
                                </td>                
                                <td>
                                <c:out value="${result.value}"></c:out>&nbsp;
                                <c:if test="${result.onThyroidTreatment == true}">
                                    on thyroid treatment
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>        
                </c:when>
                <c:when test="${uacrHistory != null}">
                    <tr>
                        <th colspan="2">
                            UACR
                        </th>
                    </tr>
                    <tr>
                        <td>
                            date
                        </td>
                        <td>
                            result<span class="historyunits">&nbsp;&#40;mg/g&#41;</span>
                        </td>
                    </tr>
                    <c:forEach var="result" items="${uacrHistory}">
                        <tr>
                            <td>
                                <c:out value="${result.date}"></c:out>
                                </td>                
                                <td>
                                <c:out value="${result.value}"></c:out>
                                </td>
                            </tr>
                    </c:forEach>        
                </c:when>
                <c:when test="${waistHistory != null}">
                    <tr>
                        <th colspan="2">
                            waist
                        </th>
                    </tr>
                    <tr>
                        <td>
                            date
                        </td>
                        <td>
                            result<span class="historyunits">&nbsp;&#40;inches&#41;</span>
                        </td>
                    </tr>
                    <c:forEach var="result" items="${waistHistory}">
                        <tr>
                            <td>
                                <c:out value="${result.date}"></c:out>
                                </td>                
                                <td>
                                <c:out value="${result.value}"></c:out>
                                </td>
                            </tr>
                    </c:forEach>        
                </c:when>
                <c:when test="${zosterHistory != null}">
                    <tr>
                        <th colspan="2">
                            zoster vaccination
                        </th>
                    </tr>
                    <tr>
                        <td>
                            date
                        </td><td></td>
                    </tr>
                    <c:forEach var="result" items="${zosterHistory}">
                        <tr>
                            <td>
                                <c:out value="${result}"></c:out>
                                </td><td></td>
                            </tr>
                    </c:forEach>        
                </c:when>
            </c:choose>
        </table>
        <c:if test="${dashboard != null}">
            <h3>Patient Dashboard</h3>
            <span class="legend">out of target:<span class="outoftarget">__</span><br>
                missing:<span class="missing">__</span></span>
            <table class="minitable">
                <tr>
                    <th colspan="3">
                        glucose
                    </th>
                </tr>
                <tr>
                    <td colspan="2">
                        result<span class="historyunits">&nbsp;&#40;mg/dl&#41;</span>
                    </td>
                    <td>
                        date
                    </td>
                </tr>
                <c:choose>
                    <c:when test="${dashboard.glucose != null}">
                        <tr>
                            <c:choose>
                                <c:when test="${statuslist.lastGlucoseFasting == true}">
                                    <c:choose>
                                        <c:when test="${statuslist.glucoseacOutOfTarget == true}">
                                            <td colspan="2" class="outoftarget">
                                                <c:out value="${dashboard.glucose.value}"/> &nbsp;
                                                AC
                                            </td>
                                        </c:when>
                                        <c:otherwise>
                                            <td colspan="2">
                                                <c:out value="${dashboard.glucose.value}"/>
                                            </td>
                                        </c:otherwise>
                                    </c:choose>
                                </c:when>
                                <c:otherwise>
                                    <c:choose>
                                        <c:when test="${statuslist.glucosepcOutOfTarget == true}">
                                            <td colspan="2" class="outoftarget">
                                                <c:out value="${dashboard.glucose.value}"/> &nbsp;
                                                PC
                                            </td>
                                        </c:when>
                                        <c:otherwise>
                                            <td colspan="2">
                                                <c:out value="${dashboard.glucose.value}"/>
                                            </td>
                                        </c:otherwise>
                                    </c:choose>
                                </c:otherwise>
                            </c:choose>
                            <td>
                                <c:out value="${dashboard.glucose.date}"/>
                            </td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <tr class="missing"><td>-</td><td>-</td><td>-</td></tr>                    
                    </c:otherwise>
                </c:choose>
            </table>
            <table class="minitable">
                <tr>
                    <th colspan="2">
                        creatinine
                    </th>
                </tr>
                <tr>
                    <td>
                        result<span class="historyunits">&nbsp;&#40;mg/dl&#41;</span>
                    </td>
                    <td>
                        date
                    </td>
                </tr>
                <c:choose>
                    <c:when test="${dashboard.creatinine != null}">
                        <tr>
                            <c:choose>
                                <c:when test="${statuslist.creatinineOutOfTarget == true}">
                                    <td class="outoftarget">
                                        <c:out value="${dashboard.creatinine.value}"/>                                        
                                    </td>
                                </c:when>
                                <c:otherwise>
                                    <td>
                                        <c:out value="${dashboard.creatinine.value}"/>
                                    </td>
                                </c:otherwise>
                            </c:choose>
                            <td>
                                <c:out value="${dashboard.creatinine.date}"/>
                            </td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <tr class="missing"><td>-</td><td>-</td></tr>                    
                    </c:otherwise>            
                </c:choose>
            </table>
            <table class="minitable">
                <tr>
                    <th colspan="2">
                        eGFR
                    </th>
                </tr>
                <tr>
                    <td>
                        result
                    </td>
                    <td>
                        date
                    </td>
                </tr>
                <c:choose>
                    <c:when test="${dashboard.egfr != null}">
                        <tr>
                            <c:choose>
                                <c:when test="${statuslist.egfrOutOfTarget == true}">
                                    <td class="outoftarget">
                                        <c:out value="${dashboard.egfr.value}"/>                                        
                                    </td>
                                </c:when>
                                <c:otherwise>
                                    <td>
                                        <c:out value="${dashboard.egfr.value}"/>
                                    </td>
                                </c:otherwise>
                            </c:choose>
                            <td>
                                <c:out value="${dashboard.egfr.date}"/>
                            </td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <tr class="missing"><td>-</td><td>-</td></tr>                    
                    </c:otherwise>            
                </c:choose>
            </table>
            <table class="minitable">
                <tr>
                    <th colspan="2">
                        AST
                    </th>
                </tr>
                <tr>
                    <td>
                        result<span class="historyunits">&nbsp;&#40;U/L&#41;</span>
                    </td>
                    <td>
                        date
                    </td>
                </tr>
                <c:choose>
                    <c:when test="${dashboard.ast != null}">
                        <tr>
                            <c:choose>
                                <c:when test="${statuslist.astOutOfTarget == true}">
                                    <td class="outoftarget">
                                        <c:out value="${dashboard.ast.value}"/>                                        
                                    </td>
                                </c:when>
                                <c:otherwise>
                                    <td>
                                        <c:out value="${dashboard.ast.value}"/>
                                    </td>
                                </c:otherwise>
                            </c:choose>
                            <td>
                                <c:out value="${dashboard.ast.date}"/>
                            </td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <tr class="missing"><td>-</td><td>-</td></tr>                    
                    </c:otherwise>            
                </c:choose>
            </table>
            <table class="minitable">
                <tr>
                    <th colspan="2">
                        ALT
                    </th>
                </tr>
                <tr>
                    <td>
                        result<span class="historyunits">&nbsp;&#40;U/L&#41;</span>
                    </td>
                    <td>
                        date
                    </td>
                </tr>
                <c:choose>
                    <c:when test="${dashboard.alt != null}">
                        <tr>
                            <c:choose>
                                <c:when test="${statuslist.altOutOfTarget == true}">
                                    <td class="outoftarget">
                                        <c:out value="${dashboard.alt.value}"/>                                        
                                    </td>
                                </c:when>
                                <c:otherwise>
                                    <td>
                                        <c:out value="${dashboard.alt.value}"/>
                                    </td>
                                </c:otherwise>
                            </c:choose>
                            <td>
                                <c:out value="${dashboard.alt.date}"/>
                            </td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <tr class="missing"><td>-</td><td>-</td></tr>                    
                    </c:otherwise>            
                </c:choose>
            </table>
            <table class="minitable">
                <tr>
                    <th colspan="2">
                        triglycerides
                    </th>
                </tr>
                <tr>
                    <td>
                        result<span class="historyunits">&nbsp;&#40;mg/dl&#41;</span>
                    </td>
                    <td>
                        date
                    </td>
                </tr>
                <c:choose>
                    <c:when test="${dashboard.triglycerides != null}">
                        <tr>
                            <c:choose>
                                <c:when test="${statuslist.triglyceridesOutOfTarget == true}">
                                    <td class="outoftarget">
                                        <c:out value="${dashboard.triglycerides.value}"/>                                        
                                    </td>
                                </c:when>
                                <c:otherwise>
                                    <td>
                                        <c:out value="${dashboard.triglycerides.value}"/>
                                    </td>
                                </c:otherwise>
                            </c:choose>
                            <td>
                                <c:out value="${dashboard.triglycerides.date}"/>
                            </td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <tr class="missing"><td>-</td><td>-</td></tr>                    
                    </c:otherwise>            
                </c:choose>
            </table>
            <table class="minitable">
                <tr>
                    <th colspan="2">
                        HDL
                    </th>
                </tr>
                <tr>
                    <td>
                        result<span class="historyunits">&nbsp;&#40;mg/dl&#41;</span>
                    </td>
                    <td>
                        date
                    </td>
                </tr>
                <c:choose>
                    <c:when test="${dashboard.hdl != null}">
                        <tr>
                            <c:choose>
                                <c:when test="${((statuslist.hdlMaleOutOfTarget == true)
                                                ||(statuslist.hdlFemaleOutOfTarget == true))}">
                                        <td class="outoftarget">
                                            <c:out value="${dashboard.hdl.value}"/>                                        
                                        </td>
                                </c:when>
                                <c:otherwise>
                                    <td>
                                        <c:out value="${dashboard.hdl.value}"/>
                                    </td>
                                </c:otherwise>
                            </c:choose>
                            <td>
                                <c:out value="${dashboard.hdl.date}"/>
                            </td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <tr class="missing"><td>-</td><td>-</td></tr>                    
                    </c:otherwise>            
                </c:choose>
            </table>
            <table class="minitable">
                <tr>
                    <th colspan="2">
                        LDL
                    </th>
                </tr>
                <tr>
                    <td>
                        result<span class="historyunits">&nbsp;&#40;mg/dl&#41;</span>
                    </td>
                    <td>
                        date
                    </td>
                </tr>
                <c:choose>
                    <c:when test="${dashboard.ldl != null}">
                        <tr>
                            <c:choose>
                                <c:when test="${dashboard.ldl.postMi == true}">
                                    <c:choose>
                                        <c:when test="${statuslist.ldlPostMiOutOfTarget == true}">
                                            <td class="outoftarget">
                                                <c:out value="${dashboard.ldl.value}"/>&nbsp;
                                                post MI &nbsp;
                                                <c:if test="${dashboard.ldl.onStatin == true}">
                                                    on statin
                                                </c:if>
                                            </td>
                                        </c:when>
                                        <c:otherwise>
                                            <td>
                                                <c:out value="${dashboard.ldl.value}"/>&nbsp;
                                                post MI &nbsp;
                                                <c:if test="${dashboard.ldl.onStatin == true}">
                                                    on statin
                                                </c:if>
                                            </td>
                                        </c:otherwise> 
                                    </c:choose>
                                </c:when>
                                <c:otherwise>
                                    <c:choose>
                                        <c:when test="${statuslist.ldlOutOfTarget == true}">
                                            <td class="outoftarget">
                                                <c:out value="${dashboard.ldl.value}"/>&nbsp;
                                                <c:if test="${dashboard.ldl.onStatin == true}">
                                                    on statin
                                                </c:if>
                                            </td>
                                        </c:when>
                                        <c:otherwise>
                                            <td>
                                                <c:out value="${dashboard.ldl.value}"/>&nbsp;
                                                <c:if test="${dashboard.ldl.onStatin == true}">
                                                    on statin
                                                </c:if>
                                            </td>
                                        </c:otherwise>
                                    </c:choose>
                                </c:otherwise>
                            </c:choose>
                            <td>
                                <c:out value="${dashboard.ldl.date}"/>
                            </td>
                        </tr>
                    </c:when>
                    <c:otherwise>                    
                        <tr class="missing"><td>-</td><td>-</td></tr>
                    </c:otherwise>
                </c:choose>
            </table>
            <table class="minitable">
                <tr>
                    <th colspan="2">
                        UACR
                    </th>
                </tr>
                <tr>
                    <td>
                        result<span class="historyunits">&nbsp;&#40;mg/g&#41;</span>
                    </td>
                    <td>
                        date
                    </td>
                </tr>
                <c:choose>
                    <c:when test="${dashboard.uacr != null}">
                        <tr>
                            <c:choose>
                                <c:when test="${statuslist.uacrOutOfTarget == true}">
                                    <td class="outoftarget">
                                        <c:out value="${dashboard.uacr.value}"/>                                        
                                    </td>
                                </c:when>
                                <c:otherwise>
                                    <td>
                                        <c:out value="${dashboard.uacr.value}"/>
                                    </td>
                                </c:otherwise>
                            </c:choose>
                            <td>
                                <c:out value="${dashboard.uacr.date}"/>
                            </td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <tr class="missing"><td>-</td><td>-</td></tr>                    
                    </c:otherwise>            
                </c:choose>
            </table>
            <table class="minitable">
                <tr>
                    <th colspan="3">
                        A1C
                    </th>
                </tr>
                <tr>
                    <td colspan="2">
                        result<span class="historyunits">&nbsp;&#40;&#37;&#41;</span>
                    </td>
                    <td>
                        date
                    </td>
                </tr>
                <c:choose>
                    <c:when test="${dashboard.a1c != null}">                    
                        <tr>
                            <c:choose>
                                <c:when test="${statuslist.a1cOutOfTarget == true}">
                                    <td colspan="2" class="outoftarget">
                                        <c:out value="${dashboard.a1c.value}"/>&nbsp;
                                        <c:if test="${dashboard.a1c.poc == true}">
                                            POC
                                        </c:if>
                                    </td>
                                </c:when>
                                <c:otherwise>
                                    <td colspan="2">
                                        <c:out value="${dashboard.a1c.value}"/>&nbsp;
                                        <c:if test="${dashboard.a1c.poc == true}">
                                            POC
                                        </c:if>
                                    </td>
                                </c:otherwise>
                            </c:choose>
                            <td>
                                <c:out value="${dashboard.a1c.date}"/>
                            </td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <tr class="missing"><td>-</td><td>-</td><td>-</td></tr>                    
                    </c:otherwise>
                </c:choose>
            </table>                       
            <table class="minitable">
                <tr>
                    <th colspan="2">
                        TSH
                    </th>
                </tr>
                <tr>
                    <td>
                        result
                    </td>
                    <td>
                        date
                    </td>
                </tr>
                <c:choose>
                    <c:when test="${dashboard.tsh != null}">
                        <tr>
                            <c:choose>
                                <c:when test="${statuslist.tshOutOfTarget == true}">
                                    <td class="outoftarget">
                                        <c:out value="${dashboard.tsh.value}"/>&nbsp;
                                        <c:if test="${dashboard.tsh.onThyroidTreatment == true}">
                                            on thyroid treatment
                                        </c:if>
                                    </td>
                                </c:when>
                                <c:otherwise>
                                    <td>
                                        <c:out value="${dashboard.tsh.value}"/>&nbsp;
                                        <c:if test="${dashboard.tsh.onThyroidTreatment == true}">
                                            on thyroid treatment
                                        </c:if>
                                    </td>
                                </c:otherwise>
                            </c:choose>
                            <td>
                                <c:out value="${dashboard.tsh.date}"/>
                            </td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <tr class="missing"><td>-</td><td>-</td></tr>                    
                    </c:otherwise>            
                </c:choose>
            </table>
            <table class="minitable">
                <tr>
                    <th colspan="2">
                        T4
                    </th>
                </tr>
                <tr>
                    <td>
                        result
                    </td>
                    <td>
                        date
                    </td>
                </tr>
                <c:choose>
                    <c:when test="${dashboard.t4 != null}">
                        <tr>
                            <c:choose>
                                <c:when test="${statuslist.t4OutOfTarget == true}">
                                    <td class="outoftarget">
                                        <c:out value="${dashboard.t4.value}"/>                                        
                                    </td>
                                </c:when>
                                <c:otherwise>
                                    <td>
                                        <c:out value="${dashboard.t4.value}"/>
                                    </td>
                                </c:otherwise>
                            </c:choose>
                            <td>
                                <c:out value="${dashboard.t4.date}"/>
                            </td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <tr class="missing"><td>-</td><td>-</td></tr>                    
                    </c:otherwise>            
                </c:choose>
            </table>                                    
            <table class="minitable">
                <tr>
                    <th colspan="2">
                        BMI
                    </th>
                </tr>
                <tr>
                    <td>
                        result
                    </td>
                    <td>
                        date
                    </td>
                </tr>
                <c:choose>
                    <c:when test="${dashboard.bmi != null}">
                        <tr>
                            <c:choose>
                                <c:when test="${statuslist.bmiOutOfTarget == true}">
                                    <td class="outoftarget">
                                        <c:out value="${dashboard.bmi.value}"/>                                        
                                    </td>
                                </c:when>
                                <c:otherwise>
                                    <td>
                                        <c:out value="${dashboard.bmi.value}"/>
                                    </td>
                                </c:otherwise>
                            </c:choose>
                            <td>
                                <c:out value="${dashboard.bmi.date}"/>
                            </td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <tr class="missing"><td>-</td><td>-</td></tr>                    
                    </c:otherwise>            
                </c:choose>
            </table>
            <table class="minitable">
                <tr>
                    <th colspan="2">
                        waist
                    </th>
                </tr>
                <tr>
                    <td>
                        result<span class="historyunits">&nbsp;&#40;inches&#41;</span>
                    </td>
                    <td>
                        date
                    </td>
                </tr>
                <c:choose>
                    <c:when test="${dashboard.waist != null}">
                        <tr>
                            <c:choose>
                                <c:when test="${((statuslist.waistMaleOutOfTarget == true)
                                                ||(statuslist.waistFemaleOutOfTarget == true))}">
                                        <td class="outoftarget">
                                            <c:out value="${dashboard.waist.value}"/>                                        
                                        </td>
                                </c:when>
                                <c:otherwise>
                                    <td>
                                        <c:out value="${dashboard.waist.value}"/>
                                    </td>
                                </c:otherwise>
                            </c:choose>
                            <td>
                                <c:out value="${dashboard.waist.date}"/>
                            </td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <tr class="missing"><td>-</td><td>-</td></tr>                    
                    </c:otherwise>            
                </c:choose>
            </table>
            <table class="minitable">
                <tr>
                    <th colspan="2">
                        PSA
                    </th>
                </tr>
                <tr>
                    <td>
                        result<span class="historyunits">&nbsp;&#40;ng/mL&#41;</span>
                    </td>
                    <td>
                        date
                    </td>
                </tr>
                <c:choose>
                    <c:when test="${dashboard.psa != null}">
                        <tr>
                            <c:choose>
                                <c:when test="${statuslist.psaOutOfTarget == true}">
                                    <td class="outoftarget">
                                        <c:out value="${dashboard.psa.value}"/>                                        
                                    </td>
                                </c:when>
                                <c:otherwise>
                                    <td>
                                        <c:out value="${dashboard.psa.value}"/>
                                    </td>
                                </c:otherwise>
                            </c:choose>
                            <td>
                                <c:out value="${dashboard.psa.date}"/>
                            </td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <c:choose>
                            <c:when test="${patient.gender == 'Male'}">
                                <tr class="missing"><td>-</td><td>-</td></tr>
                            </c:when>
                            <c:otherwise>
                                <tr><td>-</td><td>-</td></tr>
                            </c:otherwise>
                        </c:choose>                                            
                    </c:otherwise>            
                </c:choose>
            </table>
            <table class="minitable">
                <tr>
                    <th colspan="3">
                        blood pressure
                    </th>
                </tr>
                <tr>
                    <td colspan="2">
                        result<span class="historyunits">&nbsp;&#40;mmHg&#41;</span>
                    </td>
                    <td>
                        date
                    </td>
                </tr>
                <c:choose>
                    <c:when test="${dashboard.bloodPressure != null}">
                        <tr>
                            <c:choose>
                                <c:when test="${((statuslist.systolicBloodPressureOutOfTarget == true)
                                                ||(statuslist.diastolicBloodPressureOutOfTarget == true))}">
                                        <td colspan="2" class="outoftarget">
                                            <c:out value="${dashboard.bloodPressure.systolicValue}"/>/
                                            <c:out value="${dashboard.bloodPressure.diastolicValue}"/>&nbsp;
                                            <c:if test="${dashboard.bloodPressure.aceOrArb == true}">
                                                Patient on ACE or ARB
                                            </c:if>
                                        </td>
                                </c:when>
                                <c:otherwise>
                                    <td colspan="2">
                                        <c:out value="${dashboard.bloodPressure.systolicValue}"/>/
                                        <c:out value="${dashboard.bloodPressure.diastolicValue}"/>&nbsp;
                                        <c:if test="${dashboard.bloodPressure.aceOrArb == true}">
                                            Patient on ACE or ARB
                                        </c:if>
                                    </td>
                                </c:otherwise>
                            </c:choose>
                            <td>
                                <c:out value="${dashboard.bloodPressure.date}"/>
                            </td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <tr class="missing"><td colspan="2">-</td><td>-</td></tr>                    
                    </c:otherwise>            
                </c:choose>
            </table>            
            <table class="minitable">
                <tr>
                    <th colspan="2">
                        eye screening
                    </th>
                </tr>
                <tr>
                    <td>
                        category
                    </td>
                    <td>
                        date
                    </td>
                </tr>
                <c:choose>
                    <c:when test="${dashboard.eyeScreening != null}">
                        <tr>
                            <c:choose>
                                <c:when test="${statuslist.eyeScreeningOutOfTarget == true}">
                                    <td class="outoftarget">
                                        <c:out value="${dashboard.eyeScreening.category}"/>                                        
                                    </td>
                                </c:when>
                                <c:otherwise>
                                    <td>
                                        <c:out value="${dashboard.eyeScreening.category}"/>
                                    </td>
                                </c:otherwise>
                            </c:choose>
                            <td>
                                <c:out value="${dashboard.eyeScreening.dateRecorded}"/>
                            </td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <tr><td class="missing">-</td><td>-</td></tr>                    
                    </c:otherwise>            
                </c:choose>
            </table>
            <table class="minitable">
                <tr>
                    <th colspan="2">
                        foot screening
                    </th>
                </tr>
                <tr>
                    <td>
                        category
                    </td>
                    <td>
                        date
                    </td>
                </tr>
                <c:choose>
                    <c:when test="${dashboard.footScreening != null}">
                        <tr>
                            <c:choose>
                                <c:when test="${statuslist.footScreeningOutOfTarget == true}">
                                    <td class="outoftarget">
                                        <c:out value="${dashboard.footScreening.category}"/>                                        
                                    </td>
                                </c:when>
                                <c:otherwise>
                                    <td>
                                        <c:out value="${dashboard.footScreening.category}"/>
                                    </td>
                                </c:otherwise>
                            </c:choose>
                            <td>
                                <c:out value="${dashboard.footScreening.dateRecorded}"/>
                            </td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <tr><td class="missing">-</td><td>-</td></tr>                    
                    </c:otherwise>            
                </c:choose>
            </table>
            <table class="minitable">
                <tr>
                    <th colspan="2">
                        psychological screening
                    </th>
                </tr>
                <tr>
                    <td>
                        score
                    </td>
                    <td>
                        date
                    </td>
                </tr>
                <c:choose>
                    <c:when test="${dashboard.psychologicalScreening != null}">
                        <tr>
                            <c:choose>
                                <c:when test="${statuslist.psychologicalScreeningOutOfTarget == true}">
                                    <td class="outoftarget">
                                        <c:out value="${dashboard.psychologicalScreening.score}"/>                                        
                                    </td>
                                </c:when>
                                <c:otherwise>
                                    <td>
                                        <c:out value="${dashboard.psychologicalScreening.score}"/>
                                    </td>
                                </c:otherwise>
                            </c:choose>
                            <td>
                                <c:out value="${dashboard.psychologicalScreening.date}"/>
                            </td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <tr><td class="missing">-</td><td>-</td></tr>                    
                    </c:otherwise>            
                </c:choose>
            </table>
            <table class="minitable">
                <tr>
                    <th colspan="2">
                        physical activity
                    </th>
                </tr>
                <tr>
                    <td>
                        min/week
                    </td>
                    <td>
                        date
                    </td>
                </tr>
                <c:choose>
                    <c:when test="${dashboard.physicalActivity != null}">
                        <tr>
                            <c:choose>
                                <c:when test="${statuslist.physicalActivityOutOfTarget == true}">
                                    <td class="outoftarget">
                                        <c:out value="${dashboard.physicalActivity.value}"/>                                        
                                    </td>
                                </c:when>
                                <c:otherwise>
                                    <td>
                                        <c:out value="${dashboard.physicalActivity.value}"/>
                                    </td>
                                </c:otherwise>
                            </c:choose>
                            <td>
                                <c:out value="${dashboard.physicalActivity.date}"/>
                            </td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <tr class="missing"><td>-</td><td>-</td></tr>                    
                    </c:otherwise>            
                </c:choose>
            </table>
            <table class="minitable">
                <tr>
                    <th colspan="2">
                        smoking status
                    </th>
                </tr>
                <tr>
                    <td>
                        status
                    </td>
                    <td>
                        date
                    </td>
                </tr>
                <c:choose>
                    <c:when test="${dashboard.smokingStatus != null}">
                        <tr>
                            <c:choose>
                                <c:when test="${statuslist.smokingStatusOutOfTarget == true}">
                                    <td class="outoftarget">
                                        smoking                                        
                                    </td>
                                </c:when>
                                <c:otherwise>
                                    <td>
                                        not smoking
                                    </td>
                                </c:otherwise>
                            </c:choose>
                            <td>
                                <c:out value="${dashboard.smokingStatus.date}"/>
                            </td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <tr class="missing"><td>-</td><td>-</td></tr>                    
                    </c:otherwise>            
                </c:choose>
            </table>
            <table class="minitable">
                <tr>
                    <th colspan="2">
                        telephone follow up
                    </th>
                </tr>
                <tr>
                    <td>
                        category
                    </td>
                    <td>
                        date
                    </td>
                </tr>
                <c:choose>
                    <c:when test="${dashboard.telephoneFollowUp != null}">
                        <tr>
                            <c:choose>
                                <c:when test="${statuslist.telephoneFollowUpOutOfTarget == true}">
                                    <td class="outoftarget">
                                        <c:out value="${dashboard.telephoneFollowUp.category}"/>                                        
                                    </td>
                                </c:when>
                                <c:otherwise>
                                    <td>
                                        <c:out value="${dashboard.telephoneFollowUp.category}"/>
                                    </td>
                                </c:otherwise>
                            </c:choose>
                            <td>
                                <c:out value="${dashboard.telephoneFollowUp.dateRecorded}"/>
                            </td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <tr class="missing"><td>-</td><td>-</td></tr>                    
                    </c:otherwise>            
                </c:choose>
            </table>            
            <table class="minitable">
                <tr>
                    <th colspan="1">
                        last class
                    </th>
                </tr>
                <tr>
                    <td>
                        date
                    </td>
                </tr>
                <c:choose>
                    <c:when test="${dashboard.lastClass != null}">
                        <tr>
                            <c:choose>
                                <c:when test="${statuslist.classAttendanceOutOfTarget == true}">
                                    <td class="outoftarget">
                                        <c:out value="${dashboard.lastClass}"/>                                        
                                    </td>
                                </c:when>
                                <c:otherwise>
                                    <td>
                                        <c:out value="${dashboard.lastClass}"/>
                                    </td>
                                </c:otherwise>
                            </c:choose>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <tr class="missing"><td>-</td></tr>                    
                    </c:otherwise>            
                </c:choose>
            </table>
            <table class="minitable">
                <tr>
                    <th colspan="1">
                        influenza vaccine
                    </th>
                </tr>
                <tr>
                    <td>
                        date
                    </td>
                </tr>
                <c:choose>
                    <c:when test="${dashboard.influenzaVaccine != null}">
                        <tr>
                            <c:choose>
                                <c:when test="${statuslist.influenzaVaccineOutOfTarget == true}">
                                    <td class="outoftarget">
                                        <c:out value="${dashboard.influenzaVaccine}"/>                                        
                                    </td>
                                </c:when>
                                <c:otherwise>
                                    <td>
                                        <c:out value="${dashboard.influenzaVaccine}"/>
                                    </td>
                                </c:otherwise>
                            </c:choose>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <tr class="missing"><td>-</td></tr>                    
                    </c:otherwise>            
                </c:choose>
            </table>
            <table class="minitable">
                <tr>
                    <th colspan="1">
                        PCV-13 vaccine
                    </th>
                </tr>
                <tr>
                    <td>
                        date
                    </td>
                </tr>
                <c:choose>
                    <c:when test="${dashboard.pcv13Vaccine != null}">
                        <tr>
                            <c:choose>
                                <c:when test="${statuslist.pcv13VaccineOutOfTarget == true}">
                                    <td class="outoftarget">
                                        <c:out value="${dashboard.pcv13Vaccine}"/>                                        
                                    </td>
                                </c:when>
                                <c:otherwise>
                                    <td>
                                        <c:out value="${dashboard.pcv13Vaccine}"/>
                                    </td>
                                </c:otherwise>
                            </c:choose>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <tr class="missing"><td>-</td></tr>                    
                    </c:otherwise>            
                </c:choose>
            </table>
            <table class="minitable">
                <tr>
                    <th colspan="1">
                        PPSV-23 vaccine
                    </th>
                </tr>
                <tr>
                    <td>
                        date
                    </td>
                </tr>
                <c:choose>
                    <c:when test="${dashboard.ppsv23Vaccine != null}">
                        <tr>
                            <c:choose>
                                <c:when test="${statuslist.ppsv23VaccineOutOfTarget == true}">
                                    <td class="outoftarget">
                                        <c:out value="${dashboard.ppsv23Vaccine}"/>                                        
                                    </td>
                                </c:when>
                                <c:otherwise>
                                    <td>
                                        <c:out value="${dashboard.ppsv23Vaccine}"/>
                                    </td>
                                </c:otherwise>
                            </c:choose>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <tr class="missing"><td>-</td></tr>                    
                    </c:otherwise>            
                </c:choose>
            </table>
            <table class="minitable">
                <tr>
                    <th colspan="1">
                        hepatitis B vaccine
                    </th>
                </tr>
                <tr>
                    <td>
                        date
                    </td>
                </tr>
                <c:choose>
                    <c:when test="${dashboard.hepatitisBVaccine != null}">
                        <tr>
                            <c:choose>
                                <c:when test="${statuslist.hepatitisBVaccineOutOfTarget == true}">
                                    <td class="outoftarget">
                                        <c:out value="${dashboard.hepatitisBVaccine}"/>                                        
                                    </td>
                                </c:when>
                                <c:otherwise>
                                    <td>
                                        <c:out value="${dashboard.hepatitisBVaccine}"/>
                                    </td>
                                </c:otherwise>
                            </c:choose>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <tr class="missing"><td>-</td></tr>                    
                    </c:otherwise>            
                </c:choose>
            </table>
            <table class="minitable">
                <tr>
                    <th colspan="1">
                        TDAP vaccine
                    </th>
                </tr>
                <tr>
                    <td>
                        date
                    </td>
                </tr>
                <c:choose>
                    <c:when test="${dashboard.tdapVaccine != null}">
                        <tr>
                            <c:choose>
                                <c:when test="${statuslist.tdapVaccineOutOfTarget == true}">
                                    <td class="outoftarget">
                                        <c:out value="${dashboard.tdapVaccine}"/>                                        
                                    </td>
                                </c:when>
                                <c:otherwise>
                                    <td>
                                        <c:out value="${dashboard.tdapVaccine}"/>
                                    </td>
                                </c:otherwise>
                            </c:choose>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <tr class="missing"><td>-</td></tr>                    
                    </c:otherwise>            
                </c:choose>
            </table>
            <table class="minitable">
                <tr>
                    <th colspan="1">
                        zoster vaccine
                    </th>
                </tr>
                <tr>
                    <td>
                        date
                    </td>
                </tr>
                <c:choose>
                    <c:when test="${dashboard.zosterVaccine != null}">
                        <tr>
                            <c:choose>
                                <c:when test="${statuslist.zosterVaccineOutOfTarget == true}">
                                    <td class="outoftarget">
                                        <c:out value="${dashboard.zosterVaccine}"/>                                        
                                    </td>
                                </c:when>
                                <c:otherwise>
                                    <td>
                                        <c:out value="${dashboard.zosterVaccine}"/>
                                    </td>
                                </c:otherwise>
                            </c:choose>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <c:choose>
                            <c:when test="${statuslist.zosterVaccineOutOfTarget == true}">
                                <tr><td class="outoftarget">-</td></tr>
                            </c:when>
                            <c:otherwise>
                                <tr><td>-</td></tr>
                            </c:otherwise>
                        </c:choose>                                            
                    </c:otherwise>            
                </c:choose>
            </table>
            <table class="minitable">
                <tr>
                    <th colspan="1">
                        Hospitalization
                    </th>
                </tr>
                <tr>
                    <td>
                        date
                    </td>
                </tr>
                <c:choose>
                    <c:when test="${dashboard.er != null}">
                        <tr>
                            <c:choose>
                                <c:when test="${statuslist.hospitalizationOutOfTarget == true}">
                                    <td class="outoftarget">
                                        <c:out value="${dashboard.er}"/>                                        
                                    </td>
                                </c:when>
                                <c:otherwise>
                                    <td>
                                        <c:out value="${dashboard.er}"/>
                                    </td>
                                </c:otherwise>
                            </c:choose>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <tr><td>-</td></tr>                    
                    </c:otherwise>            
                </c:choose>
            </table>
            <table class="minitable">
                <tr>
                    <th colspan="2">
                        therapy
                    </th>
                </tr>
                <tr>
                    <td>
                        Rx class
                    </td>
                    <td>
                        date reviewed
                    </td>
                </tr>
                <c:choose>
                    <c:when test="${dashboard.therapy != null}">
                        <tr>
                            <td>
                                <c:out value="${dashboard.therapy.prescriptionClass}"/>                                        
                            </td>
                            <td>
                                <c:out value="${dashboard.therapy.dateReviewed}"/>                                        
                            </td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <tr class="missing"><td>-</td><td>-</td></tr>                    
                    </c:otherwise>            
                </c:choose>
            </table>
            <table class="minitable">
                <tr>
                    <th colspan="2">
                        medications
                    </th>
                </tr>
                <tr>
                    <td>
                        med ID
                    </td>
                    <td>
                        date
                    </td>
                </tr>
                <c:choose>
                    <c:when test="${dashboard.medications != null}">
                        <c:forEach var="med" items="${dashboard.medications}">
                            <tr>
                                <td>
                                    <c:out value="${med.medicationId}"/>
                                </td>
                                <td>
                                    <c:out value="${med.dateReviewed}"/>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <tr class="missing"><td>-</td><td>-</td></tr>                    
                    </c:otherwise>            
                </c:choose>
            </table>
        </c:if>
        <c:if test="${treatment != null}">
            <table class="historytable">
                <tr>
                    <th>
                        date reviewed
                    </th>
                    <th>
                        Rx class
                    </th>
                    <th>
                        therapy type
                    </th>
                    <th></th>
                </tr>
                <tr>
                    <td>
                        <c:out value="${treatment.therapy.dateReviewed}"></c:out>
                        </td>
                        <td>
                        <c:out value="${treatment.therapy.prescriptionClass}"></c:out>
                        </td>
                        <td>
                        <c:out value="${treatment.therapy.therapyType}"></c:out>
                        </td>
                        <td></td>
                    </tr>
                    <tr>
                        <th>
                            date reviewed
                        </th>
                        <th>
                            medication ID
                        </th>
                        <th>
                            medication name
                        </th>
                        <th>
                            medication class
                        </th>
                    </tr>
                <c:forEach var="med" items="${treatment.medications}">
                    <tr>
                        <td>
                            <c:out value="${med.dateReviewed}"></c:out>
                            </td>
                            <td>
                            <c:out value="${med.medicationId}"></c:out>
                            </td>
                            <td>
                            <c:out value="${med.medicationName}"></c:out>
                            </td>
                            <td>
                            <c:out value="${med.medicationClass}"></c:out>
                            </td>
                        </tr>
                </c:forEach>
            </table>
        </c:if>
        <c:choose>
            <c:when test="${a1cGraphPoints != null}">
                <section class="imgbox">
                    <img src="drawline?action=a1c" alt="line chart">
                </section>
            </c:when>                
        </c:choose>
        <c:choose>
            <c:when test="${psaGraphPoints != null}">
                <section class="imgbox">
                    <img src="drawline?action=psa" alt="line chart">
                </section>
            </c:when>                
        </c:choose>
        <c:choose>
            <c:when test="${altGraphPoints != null}">
                <section class="imgbox">
                    <img src="drawline?action=alt" alt="line chart">
                </section>
            </c:when>                
        </c:choose>
        <c:choose>
            <c:when test="${astGraphPoints != null}">
                <section class="imgbox">
                    <img src="drawline?action=ast" alt="line chart">
                </section>
            </c:when>                
        </c:choose>
        <c:choose>
            <c:when test="${bpGraphPoints != null}">
                <section class="imgbox">
                    <img src="drawline?action=bp" alt="line chart">
                </section>
            </c:when>                
        </c:choose>
        <c:choose>
            <c:when test="${bmiGraphPoints != null}">
                <section class="imgbox">
                    <img src="drawline?action=bmi" alt="line chart">
                </section>
            </c:when>                
        </c:choose>
        <c:choose>
            <c:when test="${creatinineGraphPoints != null}">
                <section class="imgbox">
                    <img src="drawline?action=creatinine" alt="line chart">
                </section>
            </c:when>                
        </c:choose>
        <c:choose>
            <c:when test="${egfrGraphPoints != null}">
                <section class="imgbox">
                    <img src="drawline?action=egfr" alt="line chart">
                </section>
            </c:when>                
        </c:choose>
        <c:choose>
            <c:when test="${glucoseGraphPoints != null}">
                <section class="imgbox">
                    <img src="drawline?action=glucose" alt="line chart">
                </section>
            </c:when>                
        </c:choose>
        <c:choose>
            <c:when test="${hdlGraphPoints != null}">
                <section class="imgbox">
                    <img src="drawline?action=hdl" alt="line chart">
                </section>
            </c:when>                
        </c:choose>
        <c:choose>
            <c:when test="${ldlGraphPoints != null}">
                <section class="imgbox">
                    <img src="drawline?action=ldl" alt="line chart">
                </section>
            </c:when>                
        </c:choose>
        <c:choose>
            <c:when test="${complianceGraphPoints != null}">
                <section class="imgbox">
                    <img src="drawline?action=compliance" alt="line chart">
                </section>
            </c:when>                
        </c:choose>
        <c:choose>
            <c:when test="${physicalActivityGraphPoints != null}">
                <section class="imgbox">
                    <img src="drawline?action=physicalActivity" alt="line chart">
                </section>
            </c:when>                
        </c:choose>
        <c:choose>
            <c:when test="${psychologicalGraphPoints != null}">
                <section class="imgbox">
                    <img src="drawline?action=psychological" alt="line chart">
                </section>
            </c:when>                
        </c:choose>
        <c:choose>
            <c:when test="${t4GraphPoints != null}">
                <section class="imgbox">
                    <img src="drawline?action=t4" alt="line chart">
                </section>
            </c:when>                
        </c:choose>
        <c:choose>
            <c:when test="${triglyceridesGraphPoints != null}">
                <section class="imgbox">
                    <img src="drawline?action=triglycerides" alt="line chart">
                </section>
            </c:when>                
        </c:choose>
        <c:choose>
            <c:when test="${tshGraphPoints != null}">
                <section class="imgbox">
                    <img src="drawline?action=tsh" alt="line chart">
                </section>
            </c:when>                
        </c:choose>
        <c:choose>
            <c:when test="${uacrGraphPoints != null}">
                <section class="imgbox">
                    <img src="drawline?action=uacr" alt="line chart">
                </section>
            </c:when>                
        </c:choose>
        <c:choose>
            <c:when test="${waistGraphPoints != null}">
                <section class="imgbox">
                    <img src="drawline?action=waist" alt="line chart">
                </section>
            </c:when>                
        </c:choose>
        <c:if test="${notes != null}">
            <table class="historytable">
                <tr><th colspan="2">Notes</th></tr>
                <tr><td>date</td><td>note</td></tr>
                <c:forEach var="note" items="${notes}">        
                    <tr>
                        <td>
                            <c:out value="${note.dateRecorded}"/>
                        </td>
                        <td>
                            <c:out value="${note.definition}"/>
                        </td>
                    </tr>
                </c:forEach>        
            </table>
        </c:if>
        <c:if test="${allNotes != null}">
            <table class="historytable" id="notetable">
                <tr><th colspan="3">Notes</th></tr>
                <tr><td>date</td><td>note topic</td><td colspan="2">note</td></tr>
                <c:forEach var="note" items="${allNotes}">
                    <tr>            
                        <td>
                            <c:out value="${note.dateRecorded}"/>
                        </td>
                        <td>
                            <c:out value="${note.category}"/>
                        </td>
                        <td id="note" colspan="2">
                            <c:out value="${note.definition}"/>
                        </td>
                    </tr>
                </c:forEach>        
            </table>
        </c:if>
    </section>
</c:if>
<jsp:include page="/includes/footer.jsp" />
