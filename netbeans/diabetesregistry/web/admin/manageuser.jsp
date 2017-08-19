<%-- 
    Document   : manageuser
    Created on : Feb 6, 2016, 11:52:23 AM
    Author     : Bryan Daniel
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/includes/taskheader.jsp" />
<h2>Administration Functions</h2>
<c:if test="${errorMessage != null}">
    <p class="error"><c:out value="${errorMessage}"/></p>
</c:if>
<c:if test="${message != null}">
    <p class="success"><c:out value="${message}"/></p>
</c:if>
<jsp:include page="/includes/adminnav.jsp" />
<h3>User Management</h3>
<form class="contentText" action="admin" method="post">
    <input type="hidden" name="action" value="getUserDetails">
    <table class="adminText">
        <tr>
            <td class="datalabels">
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
    <form id="editUserForm" style="display: none" action="admin" method="post">
        <input type="hidden" name="action" value="updateUser"> 
        <table>
            <tr>
                <td class="dataentry">
                    user name:&nbsp;<c:out value="${detailedUser.userName}"/>
                </td>            
            </tr>
            <tr>
                <td class="dataentry">
                    job title:&nbsp;<input id="jobTitle" type="text" name="jobTitle" value="<c:out value="${detailedUser.jobTitle}"/>" required>
                </td>            
            </tr>
            <tr>
                <td class="dataentry">
                    first name:&nbsp;<input id="firstName" type="text" name="firstName" value="<c:out value="${detailedUser.firstName}"/>" required>            
                </td>
            </tr>
            <tr>
                <td>
                    last name:&nbsp;<input id="lastName" type="text" name="lastName" value="<c:out value="${detailedUser.lastName}"/>" required>                                
                </td>
            </tr>
            <tr>
                <td class="dataentry">
                    email address:&nbsp;<input id="emailAddress" type="text" name="userEmail" value="<c:out value="${detailedUser.emailAddress}"/>">
                </td>
            </tr>
            <tr>
                <td><input class="button" type="submit" value="Save"></td>
            </tr>
            <tr>
                <td><button id="editusercancel" class="button">Cancel</button></td>
            </tr>
        </table>
    </form>
    <table id="userTable">
        <tr>
            <td class="dataentry">
                user name:&nbsp;<c:out value="${detailedUser.userName}"/>
            </td>            
        </tr>
        <tr>
            <td class="dataentry">
                job title:&nbsp;<c:out value="${detailedUser.jobTitle}"/>
            </td>            
        </tr>
        <tr>
            <td class="dataentry">
                name:&nbsp;<c:out value="${detailedUser.firstName} ${detailedUser.lastName}"/>                
            </td>
        </tr>                
        <tr>
            <td class="dataentry">
                email address&nbsp;<c:out value="${detailedUser.emailAddress}"/>                
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
        <tr>
            <td>
                <button id="edituserbutton" class="button">Edit User</button>
            </td>
        </tr>
    </table>
    <div id="userAccessControlTable">
        <c:if test="${detailedUser.active == true}">
            <div id="terminationConfirmation" style="display: none">
                Are you sure you want to terminate this user?
            </div>
            <div id="resetPasswordConfirmation" style="display: none">
                Are you sure you want to reset this user's password?
            </div>
            <div>                        
                <h4>User Access</h4>
                <form action="admin" method="post" id="terminateForm">
                    <input type="hidden" name="action" value="terminateUser">
                    <input type="hidden" name="userName" value="${detailedUser.userName}">
                    <table class="adminText">
                        <tr>
                            <td><input class="button" type="submit" id="terminate" value="Terminate User"></td>
                        </tr>
                    </table>
                </form>
            </div>
            <div>
                <form action="admin" method="post" id="resetPasswordForm">
                    <input type="hidden" name="action" value="resetUserPassword">
                    <input type="hidden" name="userName" value="${detailedUser.userName}">                            
                    <table class="adminText">
                        <tr>
                            <td><input class="button" type="submit" id="reset" value="Reset Password"></td>
                        </tr>
                        <tr>
                            <td>
                                <h4>Enter the administrator email password to reset the user's password.</h4>
                                <label id="adminemailpasswordlabel" class="adminemailpassword" for="emailPassword">Administrator Email Password:</label>
                                <input class="adminemailpassword" id="emailPassword" type="password" name="emailPassword" value="">                                        
                            </td>
                        </tr>
                    </table>                            
                </form>                               
            </div>
        </c:if>
        <div>
            <h4>User Audit</h4>
            <form action="admin" method="post">
                <input type="hidden" name="action" value="auditUser">
                <input type="hidden" name="userName" value="${detailedUser.userName}">
                <table class="adminText">
                    <tr>
                        <td colspan="2">Date Range:</td>
                    </tr>
                    <tr>
                        <td><label for="begindate" class="dataentrylabel">Begin Audit (YYYY-MM-DD):</label></td>
                        <td><input class="datepicker" id="from" type="text" name="beginDate" value="" required></td>
                    </tr>
                    <tr>
                        <td><label for="enddate" class="dataentrylabel">End Audit (YYYY-MM-DD):</label></td>
                        <td><input class="datepicker" id="to" type="text" name="endDate" value="" required></td>
                    </tr>
                    <tr>
                        <td colspan="2"><input class="button" type="submit" value="View Activity"></td>
                    </tr>
                </table>
            </form>
        </div>
    </div>
    <c:if test="${userActivities != null}">
        <div>
            <h4 id="userauditheader">Activities for <c:out value="${detailedUser.userName}"/></h4>
            <table class="useraudittable">
                <tr>
                    <th>Time</th><th>Activity</th>
                </tr>
                <c:forEach var="ua" items="${userActivities}">
                    <tr>
                        <td><c:out value="${ua[0]}"/></td>
                        <td><c:out value="${ua[1]}"/></td>
                    </tr>
                </c:forEach>
            </table>
        </div>
    </c:if>
</c:if>
</section>
<jsp:include page="/includes/footer.jsp" />
