<%-- 
    Document   : error
    Created on : Oct 30, 2015, 11:02:41 PM
    Author     : Bryan Daniel
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/includes/header.jsp" />
<section id="errorpage">

    <p class="error">
        <c:if test="${errorMessage!=null}">
            <c:out value="${errorMessage}"></c:out>
        </c:if>
    <p>

</section>
<jsp:include page="/includes/footer.jsp" />
