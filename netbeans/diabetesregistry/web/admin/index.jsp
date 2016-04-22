<%-- 
    Document   : index
    Created on : Feb 5, 2016, 5:16:36 PM
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
</section>
<jsp:include page="/includes/footer.jsp" />
