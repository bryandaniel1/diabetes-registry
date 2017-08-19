<%-- 
    Document   : passwordresetrequests
    Created on : March 6, 2017, 11:52:23 AM
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
<h3>Password Reset Requests</h3>
<c:choose>
    <c:when test="${passwordResetRequests != null}">
        <div>
            <h4 id="passwordresetrequestheader">Unread Password Reset Requests</h4>
            <form action="admin" method="post">
                <input type="hidden" name="action" value="markRequestsAsRead">
                <table class="passwordresetrequesttable">
                    <tr><td colspan="5"><input class="button" type="submit" id="savepasswordresetrequestlist" 
                                               value="Save"></td>                                
                    </tr>
                    <tr>
                        <th>Mark As Read</th><th>User Name</th><th>First Name</th><th>Last Name</th><th>Time Requested</th>
                    </tr>
                    <c:forEach var="prr" items="${passwordResetRequests}">
                        <tr>
                            <td>
                                <input type="checkbox" name="passwordResetRequestList" value="${prr.requestId}">
                            </td>
                            <td><c:out value="${prr.userName}"/></td>
                            <td><c:out value="${prr.firstName}"/></td>
                            <td><c:out value="${prr.lastName}"/></td>
                            <td><c:out value="${prr.timeRequested}"/></td>
                        </tr>
                    </c:forEach>
                </table>
            </form>
        </div>
    </c:when>
    <c:otherwise>
        <p>No unread password reset requests were found.</p>
    </c:otherwise>
</c:choose>
</section>
<jsp:include page="/includes/footer.jsp" />
