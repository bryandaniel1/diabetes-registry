<%-- 
    Document   : adminnav
    Created on : Feb 6, 2016, 11:44:04 AM
    Author     : Bryan Daniel
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<section class="pagecontent">
    <ul id="admin-nav">
        <li><a href="<c:url value='/admin?action=addclinic'/>">Add new clinic</a></li>
        <li><a href="<c:url value='/admin?action=updateclinic'/>">Update existing clinic</a></li>
        <li><a href="<c:url value='/admin?action=removeuser'/>">Control user access</a></li>
    </ul>
