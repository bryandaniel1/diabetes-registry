<%-- 
    Document   : manageemailreminders
    Created on : Mar 11, 2017, 9:05:40 AM
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
<h3>Manage Patient Email Reminders</h3>
<form class="contentText" action="admin" method="post">
    <input type="hidden" name="action" value="selectEmailReminderConfiguration">
    <table class="emailmessageselect">
        <tr>
            <td class="datalabels">
                <label for="emailmessageselect" class="labels">Select Email Reminder:</label>
            </td>
            <td>
                <select name="emailmessageselect" onchange="this.form.submit()">
                    <option value="" selected disabled>Select</option>
                    <c:forEach var="e" items="${emailMessageConfigurationContainer.emailMessageConfigurations}">
                        <option value="${e.key}">
                            <c:out value="${e.value.emailMessage.subject}(${e.value.emailMessage.language})"/>
                        </option>
                    </c:forEach>
                    <option value="new">Add New Reminder</option>
                </select>
            </td>
        </tr>
    </table>
</form>
<form id="cancelForm" action="admin?action=manageemailconfigurations" method="post"></form>
<form id="deleteForm" action="admin?action=deleteemailconfiguration" method="post"></form>
<c:if test="${selectedEmailMessageConfiguration != null}">
    <div id="deleteEmailMessageConfirmation" style="display: none">
        Are you sure you want to delete this email message?
    </div>
    <form class="contentText" action="admin" method="post">
        <input type="hidden" name="action" value="saveEmailMessageConfiguration">
        <table class="patientform">
            <tr>
                <td class="datalabels">
                    <label for="subject">Email Reminder Subject:</label>
                </td>
                <td>
                    <c:out value="${selectedEmailMessageConfiguration.emailMessage.subject}"/>
                </td><td></td>
            </tr>
            <tr>
                <td class="datalabels">
                    <label for="gender">Patient Filter:</label>
                </td>
                <td>
                    <c:out value="${selectedEmailMessageConfiguration.callListFilter}"/>                     
                </td><td></td>
            </tr>
            <tr>
                <td class="datalabels">
                    <label for="language">Language:</label>
                </td>
                <td>
                    <c:out value="${selectedEmailMessageConfiguration.emailMessage.language}"/>
                </td><td></td>
            </tr>
            <tr>
                <td class="datalabels">
                    <label for="email">Email Message:</label>
                </td>
                <td>
                    <textarea cols="60" rows="30" id="message" name="message" required><c:out value="${selectedEmailMessageConfiguration.emailMessage.message}"/></textarea>
                </td><td></td>
            </tr>
            <tr>
                <td></td>
                <td>
                    <input class="button" type="submit" value="Cancel" form="cancelForm">
                    <input class="button" type="submit" value="Save">
                    <input class="button" type="submit" id="delete" value="Delete" form="deleteForm">
                </td>
                <td></td>
            </tr>
        </table>            
    </form>
</c:if>
<c:if test="${newReminder == 1}">
    <form class="contentText" action="admin" method="post">
        <input type="hidden" name="action" value="saveEmailMessageConfiguration">
        <table class="patientform">
            <tr>
                <td class="datalabels">
                    <label for="subject">Email Reminder Subject:</label>
                </td>
                <td>
                    <input id="subject" type="text" name="subject" required>
                </td><td></td>
            </tr>
            <tr>
                <td class="datalabels">
                    <label for="gender">Patient Filter:</label>
                </td>
                <td>
                    <select id="filter" name="filter">
                        <option value="" selected disabled>Select</option>
                        <c:forEach var="filter" items="${emailMessageConfigurationContainer.allFilters}">
                            <option value="${filter}"><c:out value="${filter}"/></option>                        
                        </c:forEach>
                    </select>
                </td><td></td>            
            </tr>
            <tr>
                <td class="datalabels">
                    <label for="language">Language:</label>
                </td>
                <td>
                    <select id="language" name="language">
                        <option value="" selected disabled>Select</option>
                        <c:forEach var="language" items="${references.languages}">
                            <option value="${language}"><c:out value="${language}"/></option>                        
                        </c:forEach>
                    </select>
                </td><td></td>
            </tr>
            <tr>
                <td class="datalabels">
                    <label for="email">Email Message:</label>
                </td>
                <td>
                    <textarea cols="60" rows="30" id="message" name="message" required></textarea>
                </td><td></td>
            </tr>
            <tr>
                <td></td>
                <td>
                    <input class="button" type="submit" value="Cancel" form="cancelForm">
                    <input class="button" type="submit" value="Save">
                </td>
                <td></td>
            </tr>
        </table>            
    </form>
</c:if>
</section>
<jsp:include page="/includes/footer.jsp" />

