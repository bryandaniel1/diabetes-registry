<%-- 
    Document   : removeuser
    Created on : Feb 6, 2016, 11:52:23 AM
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
<h3>Control User Access</h3>
<form class="contentText" action="admin" method="post">
    <input type="hidden" name="action" value="getUserDetails">
    <table class="adminText">
        <tr>
            <td class="dataregisterlabels">
                <label for="userselect">User:</label>
            </td>
            <td>
                <select name="userselect" onchange="this.form.submit()">
                    <option value="" selected disabled>Select a user</option>
                    <c:forEach var="userName" items="${userNames}">
                        <option value="${userName}"><c:out value="${userName}"/></option>
                    </c:forEach>
                </select>
            </td>
        </tr>
    </table>
</form>
<c:if test="${detailedUser != null}">
    <table id="userTable">
        <tr>
            <td class="dataentry">
                user name:&nbsp;<c:out value="${detailedUser.userName}"/>
            </td>            
        </tr>
        <tr>
            <td class="dataentry">
                <c:out value="${detailedUser.jobTitle}"/>
            </td>            
        </tr>
        <tr>
            <td class="dataentry">
                <c:out value="${detailedUser.firstName} ${detailedUser.lastName}"/>                
            </td>
        </tr>    
        <tr>
            <td class="dataentry">
                date joined:&nbsp;<c:out value="${detailedUser.dateJoined}"/>
            </td>
        </tr>
        <tr>
            <c:choose> 
                <c:when test="${detailedUser.active == true}">
                    <td class="dataentry">
                        active:&nbsp;yes                
                    </td>
                </c:when>
                <c:otherwise>
                    <td class="dataentry">
                        active:&nbsp;no                
                    </td>
                </c:otherwise>
            </c:choose>        
        </tr>
        <tr>
            <c:choose> 
                <c:when test="${detailedUser.administrator == true}">
                    <td class="dataentry">
                        administrator:&nbsp;yes                
                    </td>
                </c:when>
                <c:otherwise>
                    <td class="dataentry">
                        administrator:&nbsp;no                
                    </td>
                </c:otherwise>
            </c:choose>        
        </tr>
        <c:if test="${detailedUser.lastLogin != null}">
            <tr>
                <td class="dataentry">
                    last login:&nbsp;<c:out value="${detailedUser.lastLogin}"/>
                </td>            
            </tr>
        </c:if>    
    </table>
    <form action="admin" method="post">
        <input type="hidden" name="action" value="updateUserAccess">
        <input type="hidden" name="userName" value="${detailedUser.userName}">
        <table id="userAccessControlTable">    
            <c:choose>
                <c:when test="${((detailedUser.clinics != null)&&(fn:length(detailedUser.clinics)) > 0)}">
                    <tr>
                        <td colspan="2">
                            <select name="userclinicselect">
                                <option value="" selected disabled>Select a clinic</option>
                                <c:forEach var="clinic" items="${detailedUser.clinics}">
                                    <option value="${clinic.clinicId}"><c:out value="${clinic.clinicName}"/></option>
                                </c:forEach>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2">
                            <label id="removeuserlabel" for="removeuser" class="datacells">remove user registration for selected clinic:</label>
                            <input  class="dataentrycheckbox" id="removeuser" type="checkbox" name="removeuser" value="1">
                        </td>
                    </tr>
                </c:when>
                <c:otherwise>
                    <tr>
                        <td colspan="2">
                            No registered clinics
                        </td>
                    </tr>
                </c:otherwise>
            </c:choose>
            <c:if test="${detailedUser.active == true}">
                <tr>
                    <td colspan="2">
                        <label id="terminateuserlabel" for="terminateuser" class="datacells">terminate user:</label>
                        <input  class="dataentrycheckbox" id="terminateuser" type="checkbox" name="terminateuser" value="1">
                    </td>
                </tr>
                <tr>
                    <td colspan="2">Administrator email and email password are required for a user termination.</td>
                </tr>
                <tr>
                    <td class="dataregisterlabels">
                        <label class="progressText" for="clinic">Clinic of Administrator:</label>
                    </td>
                    <td>
                        <select class="datacells2" id="clinic" name="adminclinicselect">
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
                        <label class="progressText" for="emailPassword">Administrator Email Password:</label>
                    </td>
                    <td>
                        <input class="datacells2" id="emailPassword" type="password" name="emailPassword" value="">
                    </td>
                </tr>
            </c:if>
        </table>
        <input class="button" id="dataentrybutton" type="submit" value="Save">
    </form>
</c:if>
</section>
<jsp:include page="/includes/footer.jsp" />
