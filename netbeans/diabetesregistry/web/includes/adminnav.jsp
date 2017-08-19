<%-- 
    Document   : adminnav
    Created on : Feb 6, 2016, 11:44:04 AM
    Author     : Bryan Daniel
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<section class="pagecontent">
    <ul id="admin-nav">
        <li><a href="<c:url value='/admin?action=updateclinic'/>">Update Clinic Details</a></li>
        <li><a href="<c:url value='/admin?action=manageuser'/>">User Management</a></li>
        <li><a href="<c:url value='/admin?action=adduser'/>">Add a New User</a></li>
        <li><a href="<c:url value='/admin?action=showpasswordresetrequests'/>">Password Reset Requests</a></li>
        <li><a href="<c:url value='/admin?action=manageemailconfigurations'/>">Email Reminder Management</a></li>
        <li><a href="<c:url value='/admin?action=managequalitychecklist'/>">Quality Checklist Management</a></li>
    </ul>
