<%-- 
    Document   : index
    Created on : Nov 16, 2015, 11:09:12 PM
    Author     : Bryan Daniel
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="/includes/taskheader.jsp" />
<h2>Clinic Statistics</h2>
<c:if test="${errorMessage != null}">
    <p class="error"><c:out value="${errorMessage}"/></p>
</c:if>
<c:if test="${message != null}">
    <p class="success"><c:out value="${message}"/></p>
</c:if>
<section class="pagecontent">
    <form action="statistics" method="post">
        <section class="contentText">
            <input type="hidden" name="action" value="getStatistic">    
            <table class="patientselect">
                <tr>
                    <td class="dataregisterlabels">
                        <label for="clinicselect" class="registerlabels">Clinic:</label>
                    </td>
                    <td>
                        <select name="clinicselect">
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
        </section>
        <section class="contentText">
            <table class="patientselect" id="statisticselect">
                <tr>
                    <td class="dataregisterlabels">
                        <label for="statselect" class="registerlabels">Select Statistic:</label>
                    </td>
                    <td>
                        <select name="statselect" onchange="this.form.submit()">
                            <option value="" selected disabled>Select a statistic</option>
                            <option value="demographics">demographics</option>
                            <option value="glycemicControl">glycemic control</option>
                            <option value="bodyMass">body mass</option>
                            <option value="treatment">treatment</option>
                        </select>
                    </td>
                </tr>
            </table>
        </section>
    </form>
</section>
<c:if test="${demographics != null}">
    <section class="pagecontent">
        <h3>Demographics</h3>
        <table class="statstable">
            <tr>
                <td>
                    Total patients:
                </td>
                <td>
                    <c:out value="${demographics.totalPatients}"/>
                </td>
            </tr>
            <tr>
                <td>
                    Percent male:
                </td>
                <td>
                    <fmt:formatNumber pattern="0.00" value="${demographics.percentMale * 100}"/>
                </td>
            </tr>
            <tr>
                <td>
                    Percent female:
                </td>
                <td>
                    <fmt:formatNumber pattern="0.00" value="${demographics.percentFemale * 100}"/>
                </td>
            </tr>
            <tr>
                <td>
                    Percent white:
                </td>
                <td>
                    <fmt:formatNumber pattern="0.00" value="${demographics.percentWhite * 100}"/>
                </td>
            </tr>
            <tr>
                <td>
                    Percent African American:
                </td>
                <td>
                    <fmt:formatNumber pattern="0.00" value="${demographics.percentAfricanAmerican * 100}"/>
                </td>
            </tr>
            <tr>
                <td>
                    Percent Asian/Pacific Islander:
                </td>
                <td>
                    <fmt:formatNumber pattern="0.00" value="${demographics.percentAsian * 100}"/>
                </td>
            </tr>
            <tr>
                <td>
                    Percent American Indian/Alaska Native:
                </td>
                <td>
                    <fmt:formatNumber pattern="0.00" value="${demographics.percentIndian * 100}"/>
                </td>
            </tr>
            <tr>
                <td>
                    Percent Hispanic:
                </td>
                <td>
                    <fmt:formatNumber pattern="0.00" value="${demographics.percentHispanic * 100}"/>
                </td>
            </tr>
            <tr>
                <td>
                    Percent Middle Eastern:
                </td>
                <td>
                    <fmt:formatNumber pattern="0.00" value="${demographics.percentMiddleEastern * 100}"/>
                </td>
            </tr>
            <tr>
                <td>
                    Percent other:
                </td>
                <td>
                    <fmt:formatNumber pattern="0.00" value="${demographics.percentOther * 100}"/>
                </td>
            </tr>
        </table>
    </c:if>
    <c:if test="${demographics != null}">
        <img src="drawchart?action=genderdemographics" alt="pie chart"><br>
        <img src="drawchart?action=racedemographics" alt="pie chart"><br>
        <img src="drawchart?action=agedemographics" alt="distribution chart">
    </c:if>
</section>
<c:if test="${glycemicStats != null}">
    <section class="pagecontent">
        <h3>Glycemic Control</h3>
        <table class="statstable">
            <tr>
                <td>
                    Average A1C value:
                </td>
                <td>
                    <fmt:formatNumber pattern="0.00" value="${glycemicStats.average}"/>
                </td>
            </tr>
            <tr>
                <td>
                    Percent at A1C target:
                </td>
                <td>
                    <fmt:formatNumber pattern="0.00" value="${glycemicStats.percentage * 100}"/>
                </td>
            </tr>        
        </table>
    </c:if>
    <c:if test="${glycemicStats != null}">
        <img src="drawchart?action=lasta1c" alt="distribution chart"><br>
        <img src="drawchart?action=lasta1cbyclassattendance" alt="boxandwhisker">
        <img src="drawchart?action=lasta1cbytreatment" alt="boxandwhisker">
    </c:if>
</section>
<c:if test="${bmiStats != null}">
    <section class="pagecontent">
        <h3>Body Mass</h3>
        <table class="statstable">
            <tr>
                <td>
                    Average male BMI value:
                </td>
                <td>
                    <c:choose>
                        <c:when test="${bmiStats[0].average != null}">
                            <fmt:formatNumber pattern="0.00" value="${bmiStats[0].average}"/>
                        </c:when>                    
                        <c:otherwise>
                            N/A
                        </c:otherwise>
                    </c:choose>
                </td>
            </tr>
            <tr>
                <td>
                    Average female BMI value:
                </td>
                <td>
                    <c:choose>
                        <c:when test="${bmiStats[1].average != null}">
                            <fmt:formatNumber pattern="0.00" value="${bmiStats[1].average}"/>
                        </c:when>
                        <c:otherwise>
                            N/A
                        </c:otherwise>
                    </c:choose>                
                </td>
            </tr>
        </table>
    </c:if>
    <c:if test="${bmiStats != null}">
        <img src="drawchart?action=lastbmimales" alt="distribution chart"><br>
        <img src="drawchart?action=lastbmifemales" alt="distribution chart"><br>
        <img src="drawchart?action=lastbmimalesbyclassattendance" alt="boxandwhisker">
        <img src="drawchart?action=lastbmifemalesbyclassattendance" alt="boxandwhisker">
    </c:if>
</section>
<c:if test="${treatmentStats != null}">
    <section class="pagecontent">
        <h3>Treatment Categories</h3>
        <table class="statstable">
            <tr>
                <th colspan="2">
                    Average Change in A1C
                </th>
            </tr>
            <tr>
                <td class="treatmentclass">
                    <c:out value="${treatmentStats[0].category}"/>&colon;
                </td>
                <td>
                    <fmt:formatNumber pattern="0.0000" value="${treatmentStats[0].value}"/>                    
                </td>
            </tr>
            <tr>
                <td class="treatmentclass">
                    class&nbsp;<c:out value="${treatmentStats[1].category}"/>&colon;
                </td>
                <td>
                    <fmt:formatNumber pattern="0.0000" value="${treatmentStats[1].value}"/>                    
                </td>
            </tr>
            <tr>
                <td class="treatmentclass">
                    class&nbsp;<c:out value="${treatmentStats[2].category}"/>&colon;
                </td>
                <td>
                    <fmt:formatNumber pattern="0.0000" value="${treatmentStats[2].value}"/>                    
                </td>
            </tr>
            <tr>
                <td class="treatmentclass">
                    class&nbsp;<c:out value="${treatmentStats[3].category}"/>&colon;
                </td>
                <td>
                    <fmt:formatNumber pattern="0.0000" value="${treatmentStats[3].value}"/>                    
                </td>
            </tr>
            <tr>
                <td class="treatmentclass">
                    class&nbsp;<c:out value="${treatmentStats[4].category}"/>&colon;
                </td>
                <td>
                    <fmt:formatNumber pattern="0.0000" value="${treatmentStats[4].value}"/>                    
                </td>
            </tr>
            <tr>
                <td class="treatmentclass">
                    class&nbsp;<c:out value="${treatmentStats[5].category}"/>&colon;
                </td>
                <td>
                    <fmt:formatNumber pattern="0.0000" value="${treatmentStats[5].value}"/>                    
                </td>
            </tr>
            <tr>
                <td class="treatmentclass">
                    class&nbsp;<c:out value="${treatmentStats[6].category}"/>&colon;
                </td>
                <td>
                    <fmt:formatNumber pattern="0.0000" value="${treatmentStats[6].value}"/>                    
                </td>
            </tr>
            <tr>
                <td class="treatmentclass">
                    class&nbsp;<c:out value="${treatmentStats[7].category}"/>&colon;
                </td>
                <td>
                    <fmt:formatNumber pattern="0.0000" value="${treatmentStats[7].value}"/>                    
                </td>
            </tr>
        </table>
    </c:if>
    <c:if test="${treatmentStats != null}">
        <img src="drawchart?action=treatmentclasscounts" alt="pie chart"><br>
        <img src="drawchart?action=treatmentgenderclasscounts" alt="bar chart"><br>
        <img src="drawchart?action=treatmentraceclasscounts" alt="bar chart">        
    </c:if>
</section>
<jsp:include page="/includes/footer.jsp" />
