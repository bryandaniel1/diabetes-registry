<%-- 
    Document   : managequalitychecklist
    Created on : Mar 12, 2017, 9:58:42 PM
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
<h3>Quality Checklist Management</h3>
<form class="contentText" action="admin" method="post">
    <input type="hidden" name="action" value="getQualityConfigurations">
    <table class="adminText">
        <tr>
            <td class="datalabels">
                <label for="roleselect">Role:</label>
            </td>
            <td>
                <select id="roleselect" name="roleselect" onchange="this.form.submit()">
                    <option value="" selected disabled>Select a role</option>
                    <c:forEach var="role" items="${qualityConfigurationRoles}">
                        <option value="${role}"><c:out value="${role}"/></option>
                    </c:forEach>
                    <option value="all">all roles</option>
                </select>
            </td>
        </tr>
    </table>
</form>
<c:if test="${selectedQualityConfigurations != null}">
    <table class="qualityconfigurationtable">
        <thead>
            <tr>
                <th id="qualityconfigurationtablehead">Checklist Items</th>
            </tr>
            <tr>
                <th>
                    <span class="qualityconfigurationdata">Role</span>
                    <span class="qualityconfigurationresponsibility">Responsibility</span>
                    <span class="qualityconfigurationdata">Status</span>
                    <span class="qualityconfigurationdata">Update</span>                
                </th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="configuration" items="${selectedQualityConfigurations}">
                <tr>
                    <td>
                        <form action="admin" method="post">
                            <input type="hidden" name="action" value="changeQualityReferenceStatus">
                            <input type="hidden" name="responsibility" value="${configuration.qualityReference.responsibility}">
                            <span class="qualityconfigurationdata">
                                <c:out value="${configuration.qualityReference.role}"/>
                            </span>
                            <span class="qualityconfigurationresponsibility">
                                <c:out value="${configuration.qualityReference.responsibility}"/>
                            </span>
                            <c:choose>
                                <c:when test="${configuration.active == true}">
                                    <span class="qualityconfigurationstatus">
                                        Active
                                    </span>
                                    <span class="qualityconfigurationdata">
                                        <input type="submit" class="button" value="Deactivate"/>
                                    </span>
                                </c:when>
                                <c:otherwise>
                                    <span class="qualityconfigurationstatus">
                                        Inactive
                                    </span>
                                    <span class="qualityconfigurationdata">
                                        <input type="submit" class="button" value="Reactivate"/>
                                    </span>
                                </c:otherwise>
                            </c:choose>
                        </form>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
    <button id="newchecklistitembutton" class="button">New Checklist Item</button>
    <form id="newchecklistitemform" style="display: none" action="admin" method="post">
        <input type="hidden" name="action" value="addQualityReference">
        <table class="adminText">
            <tr><th colspan="2">New Checklist Item</th></tr>
            <tr>
                <td colspan="2">
                    <label for="rolefornewitem">Role:</label>
                    <select id="rolefornewitem" name="rolefornewitem" required>
                        <option value="" selected disabled>Select a role</option>
                        <c:forEach var="role" items="${qualityConfigurationRoles}">
                            <option value="${role}"><c:out value="${role}"/></option>
                        </c:forEach>
                    </select>
                </td>
            </tr>
            <tr>
                <td colspan="2">
                    <label for="newresponsibility">Responsibility:</label>
                    <input type="text" id="newresponsibility" name="newresponsibility" required>
                </td>
            </tr>
            <tr>
                <td>
                    <button id="cancelnewchecklistitem" class="button">Cancel</button>
                </td>
                <td>
                    <input type="submit" class="button" value="Save">
                </td>
            </tr>
        </table>
    </form>
</c:if>
</section>
<jsp:include page="/includes/footer.jsp" />
