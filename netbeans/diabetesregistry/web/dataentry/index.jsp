<%-- 
    Document   : index
    Created on : Nov 6, 2015, 12:47:49 PM
    Author     : Bryan Daniel
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/includes/taskheader.jsp" />
<h2>Data Entry</h2>
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
    <form class="contentText" action="dataentry" method="post">
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
        <form class="contentText" action="dataentry" method="post">
            <input type="hidden" name="action" value="addResults">
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
            <input class="datepicker" id="datadate" type="text" name="dataEntryDate" value="" required>    
            <input class="button" id="dataentrybutton" type="submit" value="Save Data">
            <table class="datatable">
                <tr>
                    <td>
                        <label for="glucose" class="datacells">Glucose:</label>
                        <input class="datacells" id="glucose" type="text" name="glucose" value=""><span id="glucoseunits">mg/dl</span>
                        <label for="acorpc" class="datacells">AC/PC:</label>
                        <select id="acorpc" name="acorpc">
                            <option value="ac" selected>AC</option>
                            <option value="pc">PC</option>
                        </select>
                    </td>
                    <td>
                        <label for="creatinine" class="datacells">Creat.:</label>
                        <input class="datacells" id="creatinine" type="text" name="creatinine" value=""><span class="units">mg/dl</span>
                    </td>
                    <td>
                        <label for="egfr" class="datacells">eGFR:</label>
                        <input class="datacells" id="egfr" type="text" name="egfr" value="">
                    </td>            
                </tr>
                <tr>
                    <td>
                        <label for="ast" class="datacells">AST:</label>
                        <input class="datacells" id="ast" type="text" name="ast" value=""><span class="units">U/L</span>
                    </td>
                    <td>
                        <label for="alt" class="datacells">ALT:</label>
                        <input class="datacells" id="alt" type="text" name="alt" value=""><span class="units">U/L</span>
                    </td>            
                    <td>
                        <label for="triglycerides" class="datacells">Trigl.:</label>
                        <input class="datacells" id="triglycerides" type="text" name="triglycerides" value=""><span class="units">mg/dl</span>
                    </td>
                </tr>
                <tr>
                    <td>
                        <label for="hdl" class="datacells">HDL:</label>
                        <input class="datacells" id="hdl" type="text" name="hdl" value=""><span class="units">mg/dl</span>
                    </td>
                    <td>
                        <label for="ldl" class="datacells">LDL:</label>
                        <input class="datacells" id="ldl" type="text" name="ldl" value=""><span class="units">mg/dl</span>
                        <label for="onstatin" class="datacells">on statin:</label>
                        <input  class="dataentrycheckbox" id="onstatin" type="checkbox" name="onstatin" value="1">
                    </td>
                    <td>
                        <label for="ldlpostmi" class="datacells">LDL Post MI:</label>
                        <input class="datacells" id="ldlpostmi" type="text" name="ldlPostMi" value=""><span class="units">mg/dl</span>
                    </td>                                    
                </tr>
                <tr>
                    <td>
                        <label for="uacr" class="datacells">UACR:</label>
                        <input class="datacells" id="uacr" type="text" name="uacr" value=""><span class="units">mg/g</span>
                    </td>
                    <td>
                        <label for="a1c" class="datacells">A1C:</label>
                        <input class="datacells" id="a1c" type="text" name="a1c" value=""><span id="a1cunits">&#37;</span>
                        <label for="poc" class="datacells">POC:</label>
                        <input  class="dataentrycheckbox" id="poc" type="checkbox" name="poc" value="1">                
                    </td>
                    <td>
                        <label for="tsh" class="datacells">TSH:</label>
                        <input class="datacells" id="tsh" type="text" name="tsh" value="">
                        <label for="onthyroidtreatment" id="tshcheckboxcell">on thyroid treatment:</label>
                        <input  class="dataentrycheckbox" id="onthyroidtreatment" type="checkbox" name="onthyroidtreatment" value="1">
                    </td>           
                </tr>
                <tr>
                    <td>
                        <label for="t4" class="datacells">T4:</label>
                        <input class="datacells" id="t4" type="text" name="t4" value="">
                    </td>
                    <td>
                        <label for="psa" class="datacells">PSA:</label>
                        <input class="datacells" id="psa" type="text" name="psa" value=""><span class="units">ng/mL</span>
                    </td><td></td>          
                </tr>
            </table>
            <table class="datatable" id="screening">
                <tr>
                    <td>
                        <label for="telephone" class="datacells2">Telephone Follow-up:</label>
                        <select id="telephone" name="telephoneFollowUp">
                            <option value="" selected disabled>Select a code</option>
                            <c:forEach var="reference" items="${references.telephoneFollowUpDefinitions}">
                                <option value="${reference.code}"><c:out value="${reference.code}"/></option>
                            </c:forEach>
                        </select>
                    </td>
                </tr>
            </table>
            <table class="datatable">
                <tr>
                    <td>
                        <label for="influenzadate" class="vaccineentrydate">Influenza Vaccine (YYYY-MM-DD):</label>
                        <input class="datepicker" id="influenzadate" type="text" name="influenzaDate" value="">
                    </td>
                </tr>
                <tr>
                    <td>
                        <label for="pcv13date" class="vaccineentrydate">PCV-13 Vaccine (YYYY-MM-DD):</label>
                        <input class="datepicker" id="pcv13date" type="text" name="pcv13Date" value="">
                    </td>
                </tr>
                <tr>
                    <td>
                        <label for="ppsv23date" class="vaccineentrydate">PPSV-23 Vaccine (YYYY-MM-DD):</label>
                        <input class="datepicker" id="ppsv23date" type="text" name="ppsv23Date" value="">
                    </td>
                </tr>
                <tr>
                    <td>
                        <label for="hepbdate" class="vaccineentrydate">Hepatitis B Vaccine (YYYY-MM-DD):</label>
                        <input class="datepicker" id="hepbdate" type="text" name="hepbDate" value="">
                    </td>
                </tr>
                <tr>
                    <td>
                        <label for="tdapdate" class="vaccineentrydate">TDAP Vaccine (YYYY-MM-DD):</label>
                        <input class="datepicker" id="tdapdate" type="text" name="tdapDate" value="">
                    </td>
                </tr>
                <tr>
                    <td>
                        <label for="zosterdate" class="vaccineentrydate">Zoster Vaccine (YYYY-MM-DD):</label>
                        <input class="datepicker" id="zosterdate" type="text" name="zosterDate" value="">
                    </td>
                </tr>
            </table>
            <table>
                <tr>
                    <td>
                        <label for="notetopic">Note Topic:</label>
                        <select id="notetopic" name="noteTopic">
                            <option value="" selected disabled>Select a note topic</option>
                            <c:forEach var="reference" items="${references.noteTopics}" varStatus="loop">
                                <option value="${references.noteTopics[loop.index]}"><c:out value="${references.noteTopics[loop.index]}"/></option>
                            </c:forEach>
                        </select>
                    </td>
                    <td>
                        <label for="note">Note:</label>
                        <textarea id="note" cols="30" rows="4" name="note"></textarea>
                    </td>
                </tr>
            </table>
        </form>
    </section>
</c:if>
<jsp:include page="/includes/footer.jsp" />
