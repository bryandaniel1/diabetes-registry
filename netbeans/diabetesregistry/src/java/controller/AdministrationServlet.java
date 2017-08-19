/* 
 * Copyright 2016 Bryan Daniel.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package controller;

import data.AdministrationDataAccess;
import data.ReferencesDataAccess;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import registry.EmailMessage;
import registry.ReferenceContainer;
import registry.User;
import registry.administration.EmailMessageConfiguration;
import registry.administration.EmailMessageConfigurationContainer;
import registry.administration.PasswordResetRequest;
import registry.administration.QualityReferenceConfiguration;
import utility.EmailUtility;
import utility.HashAndSaltUtility;
import utility.SessionObjectUtility;
import utility.StringUtility;

/**
 * This HttpServlet class coordinates functions of the administration pages.
 *
 * @author Bryan Daniel
 * @version 2, March 16, 2017
 */
public class AdministrationServlet extends HttpServlet {
    
    /**
     * Serial version UID
     */
    private static final long serialVersionUID = -7607056530076022826L;

    /**
     * Handles the HTTP <code>GET</code> method. This method invokes the doPost
     * method for all requests.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method. This method coordinates the
     * navigation of the administration pages and processes requests for the
     * following cases:
     *
     * 1. updating the diabetes registry clinic
     *
     * 2. retrieving user details
     *
     * 3. terminating a user
     *
     * 4. resetting a user's password
     *
     * 5. adding a new user to the registry
     *
     * 6. updating information for an existing user
     *
     * 7. auditing user activity
     *
     * 8. displaying password reset requests
     *
     * 9. marking password reset requests as read
     *
     * 10. setting the email reminder configurations in the session when
     * navigating to the email reminder management page
     *
     * 11. selecting an email reminder configuration to review
     *
     * 12. saving a new or updating an existing email reminder configuration
     *
     * 13. deleting an email reminder configuration
     *
     * 14. retrieving all quality checklist configurations when navigating to
     * the quality checklist management page
     *
     * 15. selecting quality checklist configurations by role for review
     *
     * 16. changing quality reference status
     *
     * 17. adding a new quality reference
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        SessionObjectUtility.resetClinicObjects(session);
        String url = "/index.jsp";
        int clinicId = ReferenceContainer.CLINIC_ID;
        boolean validInput = true;
        User user = (User) session.getAttribute(SessionObjectUtility.USER);
        String adminName = user.getUserName();
        String message;
        ArrayList<String> userNames;
        String action = request.getParameter("action");

        if (action == null) {
            action = "administrate";
        }

        switch (action) {
            case "administrate":
                url = "/admin/index.jsp";
                break;
            case "updateclinic":
                url = "/admin/updateclinic.jsp";
                break;
            case "manageuser":
                url = "/admin/manageuser.jsp";
                userNames = AdministrationDataAccess.getUserNames();
                session.setAttribute(SessionObjectUtility.USER_NAMES, userNames);
                break;
            case "adduser":
                url = "/admin/adduser.jsp";
                break;
            case "updateClinicInformation": {
                url = "/admin/updateclinic.jsp";

                /* clinic name *required* */
                String clinicName = request.getParameter("clinicName");
                if ((clinicName == null) || (clinicName.trim().length() == 0)) {
                    message = "A clinic name must be entered.";
                    request.setAttribute("errorMessage", message);
                    break;
                }
                if (StringUtility.tooLongForShortVarChar(clinicName)) {
                    message = "The first name must be 50 characters or less.";
                    request.setAttribute("errorMessage", message);
                    validInput = false;
                }

                /* clinic address *required* */
                String address = request.getParameter("address");
                if ((address == null) || (address.trim().length() == 0)) {
                    message = "An address must be entered.";
                    request.setAttribute("errorMessage", message);
                    break;
                }
                if (StringUtility.tooLongForLongVarChar(address)) {
                    message = "The address must be 1000 characters or less.";
                    request.setAttribute("errorMessage", message);
                    validInput = false;
                }

                /* clinic phone number *required* */
                String phoneNumber = request.getParameter("phoneNumber");
                if ((phoneNumber == null) || (phoneNumber.trim().length() == 0)) {
                    message = "A phone number must be entered.";
                    request.setAttribute("errorMessage", message);
                    break;
                }
                if (StringUtility.tooLongForShortVarChar(phoneNumber)) {
                    message = "The contact number must be 50 characters or less.";
                    request.setAttribute("errorMessage", message);
                    validInput = false;
                }

                /* clinic email address */
                String emailAddress = request.getParameter("email");
                if (StringUtility.tooLongForEmailVarChar(emailAddress)) {
                    message = "The email address must be 255 characters or less.";
                    request.setAttribute("errorMessage", message);
                    validInput = false;
                }
                if (validInput) {

                    boolean successfullyUpdated
                            = AdministrationDataAccess.updateClinic(clinicId, clinicName,
                                    address, phoneNumber, emailAddress, adminName);
                    if (successfullyUpdated) {

                        // refreshing the references in the application
                        refreshReferences(session);
                        message = "The clinic has been updated successfully.";
                        request.setAttribute("message", message);
                    } else {
                        message = "The clinic could not be updated.";
                        request.setAttribute("errorMessage", message);
                    }
                }
                break;
            }
            case "getUserDetails": {
                url = "/admin/manageuser.jsp";
                setDetailedUser(request.getParameter("userselect"), session);
                break;
            }
            case "terminateUser": {
                url = "/admin/manageuser.jsp";
                String userName = request.getParameter("userName");
                boolean userTerminatedSuccessfully
                        = AdministrationDataAccess.terminateUser(userName, adminName);
                if (userTerminatedSuccessfully) {

                    // setting the current user status
                    setDetailedUser(userName, session);
                    message = "The user, " + userName + ", has been terminated successfully.";
                    request.setAttribute("message", message);
                } else {
                    message = "User termination failed.";
                    request.setAttribute("errorMessage", message);
                }
                break;
            }
            case "resetUserPassword": {
                url = "/admin/manageuser.jsp";
                String userName = request.getParameter("userName");
                User detailedUser = (User) session.getAttribute(SessionObjectUtility.DETAILED_USER);

                /* admin email password */
                String adminEmailPassword = request.getParameter("emailPassword");
                if ((adminEmailPassword == null)
                        || (adminEmailPassword.length() == 0)) {
                    message = "The email password for the administrator's clinic "
                            + "must be entered to reset a user password.";
                    request.setAttribute("errorMessage", message);
                    break;
                }

                String userSalt = HashAndSaltUtility.getSalt();
                String temporaryPassword = StringUtility.generateTemporaryPassword();
                String hash = null;

                try {
                    hash = HashAndSaltUtility.hashWithSalt(temporaryPassword, userSalt);
                } catch (NoSuchAlgorithmException ex) {
                    Logger.getLogger(AdministrationServlet.class.getName())
                            .log(Level.SEVERE, null, ex);
                }

                boolean userReset = false;

                if (hash != null) {
                    userReset = AdministrationDataAccess.resetUserPassword(userName, adminName,
                            hash, userSalt);
                }

                if (userReset) {
                    message = "The password for " + userName + " has been reset.";

                    /* clinic email */
                    String adminEmail = ((ReferenceContainer) session.getServletContext()
                            .getAttribute("references")).getClinic().getEmailAddress();

                    sendResetPasswordEmail(adminEmail, adminEmailPassword, temporaryPassword,
                            userName, detailedUser.getFirstName(), detailedUser.getLastName(),
                            request, detailedUser.getEmailAddress());

                    request.setAttribute("message", message);
                } else {
                    message = "The user password could not be reset.";
                    request.setAttribute("errorMessage", message);
                }
                break;
            }
            case "saveNewUser": {
                url = "/admin/adduser.jsp";

                /* new user first name *required* */
                String firstName = request.getParameter("firstName");
                if ((firstName == null) || (firstName.trim().length() == 0)) {
                    message = "A first name must be entered.";
                    request.setAttribute("errorMessage", message);
                    break;
                }
                if (StringUtility.tooLongForShortVarChar(firstName)) {
                    message = "The first name must be 50 characters or less.";
                    request.setAttribute("errorMessage", message);
                    validInput = false;
                }

                /* new user last name *required* */
                String lastName = request.getParameter("lastName");
                if ((lastName == null) || (lastName.trim().length() == 0)) {
                    message = "A last name must be entered.";
                    request.setAttribute("errorMessage", message);
                    break;
                }
                if (StringUtility.tooLongForShortVarChar(lastName)) {
                    message = "The last name must be 50 characters or less.";
                    request.setAttribute("errorMessage", message);
                    validInput = false;
                }

                /* new user job title *required* */
                String jobTitle = request.getParameter("jobTitle");
                if ((jobTitle == null) || (jobTitle.trim().length() == 0)) {
                    message = "A job title must be entered.";
                    request.setAttribute("errorMessage", message);
                    break;
                }
                if (StringUtility.tooLongForShortVarChar(jobTitle)) {
                    message = "The job title must be 50 characters or less.";
                    request.setAttribute("errorMessage", message);
                    validInput = false;
                }

                /* new user email address */
                String userEmail = request.getParameter("userEmail");
                if ((userEmail != null) && (userEmail.trim().length() != 0)) {
                    if (StringUtility.tooLongForEmailVarChar(userEmail)) {
                        message = "The email address must be 255 characters or less.";
                        request.setAttribute("errorMessage", message);
                        validInput = false;
                    }
                }

                /* admin email password */
                String adminEmailPassword = request.getParameter("emailPassword");
                if ((adminEmailPassword == null)
                        || (adminEmailPassword.length() == 0)) {
                    message = "The email password for the administrator's clinic "
                            + "must be entered.";
                    request.setAttribute("errorMessage", message);
                    break;
                }

                if (validInput) {
                    String userName = StringUtility.generateUserName(firstName, lastName);
                    if (userName == null) {
                        message = "An error occurred when creating the username.";
                        request.setAttribute("errorMessage", message);
                        break;
                    }
                    boolean userAdded = false;

                    String userSalt = HashAndSaltUtility.getSalt();
                    String temporaryPassword = StringUtility.generateTemporaryPassword();
                    String hash = null;

                    try {
                        hash = HashAndSaltUtility.hashWithSalt(temporaryPassword, userSalt);
                    } catch (NoSuchAlgorithmException ex) {
                        Logger.getLogger(AdministrationServlet.class.getName())
                                .log(Level.SEVERE, null, ex);
                    }

                    if (hash != null) {
                        userAdded = AdministrationDataAccess.addUser(userName, firstName,
                                lastName, jobTitle, hash, userSalt, userEmail);
                    }

                    if (userAdded) {
                        message = "The new user has been successfully added to the registry.";

                        /* clinic email */
                        String adminEmail = ((ReferenceContainer) session.getServletContext()
                                .getAttribute("references")).getClinic().getEmailAddress();

                        sendNewUserEmail(adminEmail, adminEmailPassword, temporaryPassword, userName,
                                firstName, lastName, request, userEmail);
                        request.setAttribute("message", message);
                    } else {
                        message = "The new user could not be added.";
                        request.setAttribute("errorMessage", message);
                    }
                }
                break;
            }
            case "updateUser": {
                url = "/admin/manageuser.jsp";

                /* user first name *required* */
                String firstName = request.getParameter("firstName");
                if ((firstName == null) || (firstName.trim().length() == 0)) {
                    message = "A first name must be entered.";
                    request.setAttribute("errorMessage", message);
                    break;
                }
                if (StringUtility.tooLongForShortVarChar(firstName)) {
                    message = "The first name must be 50 characters or less.";
                    request.setAttribute("errorMessage", message);
                    validInput = false;
                }

                /* user last name *required* */
                String lastName = request.getParameter("lastName");
                if ((lastName == null) || (lastName.trim().length() == 0)) {
                    message = "A last name must be entered.";
                    request.setAttribute("errorMessage", message);
                    break;
                }
                if (StringUtility.tooLongForShortVarChar(lastName)) {
                    message = "The last name must be 50 characters or less.";
                    request.setAttribute("errorMessage", message);
                    validInput = false;
                }

                /* user job title *required* */
                String jobTitle = request.getParameter("jobTitle");
                if ((jobTitle == null) || (jobTitle.trim().length() == 0)) {
                    message = "A job title must be entered.";
                    request.setAttribute("errorMessage", message);
                    break;
                }
                if (StringUtility.tooLongForShortVarChar(jobTitle)) {
                    message = "The job title must be 50 characters or less.";
                    request.setAttribute("errorMessage", message);
                    validInput = false;
                }

                /* user email address */
                String userEmail = request.getParameter("userEmail");
                if ((userEmail != null) && (userEmail.trim().length() != 0)) {
                    if (StringUtility.tooLongForEmailVarChar(userEmail)) {
                        message = "The email address must be 255 characters or less.";
                        request.setAttribute("errorMessage", message);
                        validInput = false;
                    }
                }

                if (validInput) {
                    User detailedUser = (User) session.getAttribute(SessionObjectUtility.DETAILED_USER);
                    String userName = detailedUser.getUserName();
                    boolean userUpdated = AdministrationDataAccess.updateUser(userName, firstName,
                            lastName, jobTitle, userEmail);

                    if (userUpdated) {
                        setDetailedUser(userName, session);
                        message = "The user has been successfully updated.";
                        request.setAttribute("message", message);
                    } else {
                        message = "The user could not be updated.";
                        request.setAttribute("errorMessage", message);
                    }
                }
                break;
            }
            case "auditUser": {
                url = "/admin/manageuser.jsp";
                String userName = request.getParameter("userName");
                String beginDateString = request.getParameter("beginDate");
                String endDateString = request.getParameter("endDate");

                /* validating start date */
                Date beginDate = null;
                if ((beginDateString != null) && (beginDateString.trim().length() != 0)) {
                    if (StringUtility.dateCheck(beginDateString)) {
                        beginDate = Date.valueOf(beginDateString);
                    } else {
                        message = "The begin date does not conform to the pattern, "
                                + "YYYY-MM-DD.";
                        request.setAttribute("errorMessage", message);
                        validInput = false;
                    }
                } else {
                    message = "you must enter a beginning date for the audit.";
                    request.setAttribute("errorMessage", message);
                    validInput = false;
                }

                /* validating end date */
                Date endDate = null;
                if ((endDateString != null) && (endDateString.trim().length() != 0)) {
                    if (StringUtility.dateCheck(endDateString)) {
                        endDate = Date.valueOf(endDateString);
                    } else {
                        message = "The end date does not conform to the pattern, "
                                + "YYYY-MM-DD.";
                        request.setAttribute("errorMessage", message);
                        validInput = false;
                    }
                } else {
                    message = "You must enter an end date for the audit.";
                    request.setAttribute("errorMessage", message);
                    validInput = false;
                }
                if (beginDate != null && endDate != null && validInput) {
                    if (!beginDate.before(endDate)) {
                        message = "The beginning date must be before the end date.";
                        request.setAttribute("errorMessage", message);
                    } else {
                        ArrayList<String[]> userActivities
                                = AdministrationDataAccess.auditUser(userName, beginDate, endDate);
                        if (userActivities == null || userActivities.isEmpty()) {
                            message = "No activities recorded for this user.";
                            request.setAttribute("errorMessage", message);
                        } else {
                            request.setAttribute("userActivities", userActivities);
                        }
                    }
                }
                break;
            }
            case "showpasswordresetrequests": {
                url = "/admin/passwordresetrequests.jsp";
                setPasswordResetRequests(request);
                break;
            }
            case "markRequestsAsRead": {
                url = "/admin/passwordresetrequests.jsp";
                ArrayList<Integer> requestsToMarkAsRead = new ArrayList<>();
                String[] checkBoxes = request.getParameterValues("passwordResetRequestList");
                if (checkBoxes != null) {
                    for (String s : checkBoxes) {
                        requestsToMarkAsRead.add(Integer.parseInt(s));
                    }
                }
                boolean successfullyMarked
                        = AdministrationDataAccess.markPasswordResetRequestsAsRead(requestsToMarkAsRead);
                if (successfullyMarked) {
                    setPasswordResetRequests(request);
                    message = "The selected requests have been marked as read.";
                    request.setAttribute("message", message);
                } else {
                    message = "An error occurred while marking the selected requests.  "
                            + "Refer to server logs for more details.";
                    request.setAttribute("errorMessage", message);
                }
                break;
            }
            case "manageemailconfigurations":
                url = "/admin/manageemailreminders.jsp";
                setEmailMessageConfigurations(clinicId, session);
                break;
            case "selectEmailReminderConfiguration":
                url = "/admin/manageemailreminders.jsp";
                String identifierString = request.getParameter("emailmessageselect");

                /* show new reminder form or configuration of an existing reminder message */
                if (identifierString.equals("new")) {
                    request.setAttribute("newReminder", 1);
                    session.setAttribute(SessionObjectUtility.SELECTED_EMAIL_MESSAGE_CONFIGURATION, null);
                } else {
                    int identifer = Integer.parseInt(identifierString);
                    EmailMessageConfigurationContainer configurationContainer
                            = (EmailMessageConfigurationContainer) session
                            .getAttribute(SessionObjectUtility.EMAIL_MESSAGE_CONFIGURATION_CONTAINER);
                    EmailMessageConfiguration selectedEmailMessageConfiguration
                            = configurationContainer.getEmailMessageConfigurations().get(identifer);
                    session.setAttribute(SessionObjectUtility.SELECTED_EMAIL_MESSAGE_CONFIGURATION,
                            selectedEmailMessageConfiguration);
                }
                break;
            case "saveEmailMessageConfiguration":
                url = "/admin/manageemailreminders.jsp";
                boolean validData = true;
                EmailMessageConfiguration newConfiguration
                        = (EmailMessageConfiguration) session.getAttribute(SessionObjectUtility.SELECTED_EMAIL_MESSAGE_CONFIGURATION);
                EmailMessage newMessage;

                /* message input */
                String messageInput = request.getParameter("message");
                if ((messageInput == null)
                        || (messageInput.trim().length() == 0)) {
                    message = "A email message must be entered.";
                    request.setAttribute("errorMessage", message);
                    break;
                }
                if (StringUtility.tooLongForLongVarChar(messageInput)) {
                    message = "The email message must be 1000 characters or less.";
                    request.setAttribute("errorMessage", message);
                    validData = false;
                }

                /*
                 * All required variables are set for new configurations while 
                 * only the message is set for existing configurations.
                 */
                if (newConfiguration == null) {
                    newConfiguration = new EmailMessageConfiguration();
                    newMessage = new EmailMessage();

                    /* subject required */
                    newMessage.setSubject(request.getParameter("subject"));
                    if ((newMessage.getSubject() == null)
                            || (newMessage.getSubject().trim().length() == 0)) {
                        message = "A subject must be entered.";
                        request.setAttribute("errorMessage", message);
                        break;
                    }
                    if (StringUtility.tooLongForShortVarChar(newMessage.getSubject())) {
                        message = "The subject must be 50 characters or less.";
                        request.setAttribute("errorMessage", message);
                        validData = false;
                    }

                    /* filter required */
                    newConfiguration.setCallListFilter(request.getParameter("filter"));
                    if ((newConfiguration.getCallListFilter() == null)
                            || (newConfiguration.getCallListFilter().trim().length() == 0)) {
                        message = "A filter must be selected.";
                        request.setAttribute("errorMessage", message);
                        break;
                    }
                    if (StringUtility.tooLongForUsernameVarChar(newConfiguration.getCallListFilter())) {
                        message = "The filter must be 100 characters or less.";
                        request.setAttribute("errorMessage", message);
                        validData = false;
                    }

                    /* language required */
                    newMessage.setLanguage(request.getParameter("language"));
                    if ((newMessage.getLanguage() == null)
                            || (newMessage.getLanguage().trim().length() == 0)) {
                        message = "A language must be selected.";
                        request.setAttribute("errorMessage", message);
                        break;
                    }
                    if (StringUtility.tooLongForShortVarChar(newMessage.getLanguage())) {
                        message = "The language must be 50 characters or less.";
                        request.setAttribute("errorMessage", message);
                        validData = false;
                    }

                    /* message required */
                    newMessage.setMessage(messageInput);
                    newConfiguration.setEmailMessage(newMessage);

                    /* The new reminder is checked for subject/language uniqueness and filter continuity. */
                    EmailMessageConfigurationContainer configurations = (EmailMessageConfigurationContainer) session
                            .getAttribute("emailMessageConfigurationContainer");
                    if (checkForDuplicateEmailConfiguration(newConfiguration, configurations)) {
                        message = "The subject and language already exist in an "
                                + "email reminder configuration.";
                        request.setAttribute("errorMessage", message);
                        validData = false;
                    } else if (checkForInconsistentFilter(newConfiguration, configurations)) {
                        message = "The selected filter does not match the existing filter "
                                + "for this subject.";
                        request.setAttribute("errorMessage", message);
                        validData = false;
                    }

                } else {
                    newMessage = newConfiguration.getEmailMessage();
                    newMessage.setMessage(messageInput);
                }

                if (validData) {
                    boolean successfulUpdate = AdministrationDataAccess
                            .saveEmailMessageConfiguration(clinicId, newConfiguration);
                    if (successfulUpdate) {
                        setEmailMessageConfigurations(clinicId, session);

                        // refreshing the references in the application
                        refreshReferences(session);
                        message = "The email reminder has been saved!";
                        request.setAttribute("message", message);
                    } else {
                        message = "The email configuration could not be saved.  See server logs for details.";
                        request.setAttribute("errorMessage", message);
                        session.setAttribute(SessionObjectUtility.SELECTED_EMAIL_MESSAGE_CONFIGURATION, null);
                    }
                }

                break;
            case "deleteemailconfiguration":
                url = "/admin/manageemailreminders.jsp";
                EmailMessageConfiguration selectedEmailMessageConfiguration
                        = (EmailMessageConfiguration) session
                        .getAttribute(SessionObjectUtility.SELECTED_EMAIL_MESSAGE_CONFIGURATION);
                if (selectedEmailMessageConfiguration == null) {
                    message = "No email reminder was selected.";
                    request.setAttribute("errorMessage", message);
                } else {
                    boolean success = AdministrationDataAccess
                            .deleteEmailMessageConfiguration(clinicId, selectedEmailMessageConfiguration);
                    if (success) {
                        message = "The email reminder was successfully deleted.";
                        request.setAttribute("message", message);
                        setEmailMessageConfigurations(clinicId, session);

                        // refreshing the references in the application
                        refreshReferences(session);
                    } else {
                        message = "An error occurred while deleting the email reminder.  "
                                + "Check the server logs for more details.";
                        request.setAttribute("errorMessage", message);
                    }
                }
                break;
            case "managequalitychecklist":
                url = "/admin/managequalitychecklist.jsp";

                /* setting the quality reference configurations */
                ArrayList<QualityReferenceConfiguration> configurations
                        = (ArrayList<QualityReferenceConfiguration>) session
                        .getAttribute(SessionObjectUtility.QUALITY_CONFIGURATIONS);
                if (configurations == null) {
                    updateQualityReferences(session);
                    configurations = (ArrayList<QualityReferenceConfiguration>) session
                            .getAttribute(SessionObjectUtility.QUALITY_CONFIGURATIONS);
                }

                /* setting the unique roles for the configurations */
                ArrayList<String> roles = new ArrayList<>();
                for (QualityReferenceConfiguration q : configurations) {
                    if (!roles.contains(q.getQualityReference().getRole())) {
                        roles.add(q.getQualityReference().getRole());
                    }
                }
                session.setAttribute(SessionObjectUtility.QUALITY_CONFIGURATION_ROLES, roles);
                break;
            case "getQualityConfigurations":
                url = "/admin/managequalitychecklist.jsp";
                String allRoles = "all";
                String selectedRole = request.getParameter("roleselect");
                ArrayList<QualityReferenceConfiguration> allConfigurations
                        = (ArrayList<QualityReferenceConfiguration>) session
                        .getAttribute(SessionObjectUtility.QUALITY_CONFIGURATIONS);

                /* setting selected configurations by the role selected */
                if (selectedRole.equals(allRoles)) {
                    request.setAttribute("selectedQualityConfigurations", allConfigurations);
                } else {
                    ArrayList<QualityReferenceConfiguration> selectedConfigurations = new ArrayList<>();
                    for (QualityReferenceConfiguration q : allConfigurations) {
                        if (selectedRole.equals(q.getQualityReference().getRole())) {
                            selectedConfigurations.add(q);
                        }
                    }
                    request.setAttribute("selectedQualityConfigurations", selectedConfigurations);
                }
                break;
            case "changeQualityReferenceStatus":
                url = "/admin/managequalitychecklist.jsp";
                boolean success = AdministrationDataAccess
                        .changeQualityReferenceStatus(request.getParameter("responsibility"));

                /* retrieving all quality configurations after the successful update */
                if (success) {

                    // refreshing the references in the application
                    updateQualityReferences(session);
                    refreshReferences(session);
                    message = "The quality reference was successfully updated.";
                    request.setAttribute("message", message);
                } else {
                    message = "An error occurred while changing the quality reference status.  "
                            + "Check the server logs for more details.";
                    request.setAttribute("errorMessage", message);
                }
                break;
            case "addQualityReference":
                url = "/admin/managequalitychecklist.jsp";
                String roleForNewItem = request.getParameter("rolefornewitem");
                String newResponsibility = request.getParameter("newresponsibility");

                /* validating role input */
                if ((roleForNewItem == null) || (roleForNewItem.trim().length() == 0)) {
                    message = "A role must be selected.";
                    request.setAttribute("errorMessage", message);
                    break;
                }
                if (StringUtility.tooLongForShortVarChar(roleForNewItem)) {
                    message = "The role must be 50 characters or less.";
                    request.setAttribute("errorMessage", message);
                    validInput = false;
                }

                /* validating responsibility input */
                if ((newResponsibility == null) || (newResponsibility.trim().length() == 0)) {
                    message = "A responsibility must be entered.";
                    request.setAttribute("errorMessage", message);
                    break;
                }
                if (StringUtility.tooLongForEmailVarChar(newResponsibility)) {
                    message = "The responsibility must be 255 characters or less.";
                    request.setAttribute("errorMessage", message);
                    validInput = false;
                }

                /* validating that no duplicate responsibility is given */
                ArrayList<QualityReferenceConfiguration> currentConfigurations
                        = (ArrayList<QualityReferenceConfiguration>) session
                        .getAttribute(SessionObjectUtility.QUALITY_CONFIGURATIONS);
                for (QualityReferenceConfiguration q : currentConfigurations) {
                    if (newResponsibility.trim().equalsIgnoreCase(q.getQualityReference().getResponsibility())) {
                        message = "A matching responsibility already exists.  You must enter "
                                + "a unique responsibility to add a new checklist item.";
                        request.setAttribute("errorMessage", message);
                        validInput = false;
                        break;
                    }
                }

                if (validInput) {
                    boolean added = AdministrationDataAccess
                            .addQualityReference(roleForNewItem, newResponsibility);
                    if (added) {

                        // refreshing the references in the application
                        updateQualityReferences(session);
                        refreshReferences(session);
                        message = "The new quality checklist item was successfully added.";
                        request.setAttribute("message", message);
                    } else {
                        message = "An error occurred while saving the checklist item.  "
                                + "Check the server logs for more details.";
                        request.setAttribute("errorMessage", message);
                    }
                }
                break;
            default:
                break;
        }
        getServletContext().getRequestDispatcher(url)
                .forward(request, response);
    }

    /**
     * This method takes all the necessary parameters for an email message and
     * uses the EmailUtility class to send messages regarding the addition of a
     * new user to the database.
     *
     * @param adminEmail the administrator email
     * @param emailPassword the administrator email password
     * @param temporaryPassword the user's temporary password
     * @param userName the user name
     * @param firstName the user's first name
     * @param lastName the user's last name
     * @param request the request object
     * @param userEmail the user's email
     */
    private void sendNewUserEmail(String adminEmail, String emailPassword, String temporaryPassword,
            String userName, String firstName, String lastName, HttpServletRequest request,
            String userEmail) {
        String to = adminEmail;
        String from = adminEmail;
        String subject = "New User";
        String body = new java.util.Date().toString()
                + "\n\nA user has been added to the registry.\n"
                + "The details are listed below.\n\n"
                + "New User:\n"
                + firstName + " " + lastName + "\n\n"
                + "Username:\n"
                + userName + "\n\n"
                + "Temporary password:\n"
                + temporaryPassword;

        boolean isBodyHTML = false;
        boolean sent = EmailUtility.sendMail(to, from, subject, body, isBodyHTML,
                emailPassword, request);
        if (!sent) {
            EmailUtility.logEmailError(to, from, subject, "AdministrationServlet", false);
            request.setAttribute("errorMessage", "The email function encountered "
                    + "a problem.  See the server logs for additional details.");
        }

        if ((userEmail != null) && (userEmail.trim().length() != 0)) {
            to = userEmail;
            body = new java.util.Date().toString()
                    + "\n\nYou have been added to the diabetes registry.\n"
                    + "The user name and temporary password are listed below.\n"
                    + "Once you have signed in, you will be required to change your password.\n"
                    + "If you have any questions, please contact the administrator.\n\n"
                    + "Username:\n"
                    + userName + "\n\n"
                    + "Temporary password:\n"
                    + temporaryPassword;
            sent = EmailUtility.sendMail(to, from, subject, body, isBodyHTML,
                    emailPassword, request);
            if (!sent) {
                EmailUtility.logEmailError(to, from, subject, "AdministrationServlet", false);
                request.setAttribute("errorMessage", "The email function encountered "
                        + "a problem.  See the server logs for additional details.");
            }
        }
    }

    /**
     * This method takes all the necessary parameters for an email message and
     * uses the EmailUtility class to send messages concerning a user's reset
     * password.
     *
     * @param adminEmail the administrator email
     * @param emailPassword the administrator email password
     * @param temporaryPassword the user's temporary password
     * @param userName the user name
     * @param firstName the user's first name
     * @param lastName the user's last name
     * @param request the request object
     * @param userEmail the user's email
     */
    private void sendResetPasswordEmail(String adminEmail, String emailPassword, String temporaryPassword,
            String userName, String firstName, String lastName, HttpServletRequest request,
            String userEmail) {
        String to = adminEmail;
        String from = adminEmail;
        String subject = "User Password Reset";
        String body = new java.util.Date().toString()
                + "\n\nThe user listed below has a new temporary password.\n\n"
                + "User:\n"
                + firstName + " " + lastName + "\n\n"
                + "Username:\n"
                + userName + "\n\n"
                + "Temporary password:\n"
                + temporaryPassword;

        boolean isBodyHTML = false;
        boolean sent = EmailUtility.sendMail(to, from, subject, body, isBodyHTML,
                emailPassword, request);
        if (!sent) {
            EmailUtility.logEmailError(to, from, subject, "AdministrationServlet", false);
            request.setAttribute("errorMessage", "The email function encountered "
                    + "a problem.  See the server logs for additional details.");
        }

        if ((userEmail != null) && (userEmail.trim().length() != 0)) {
            to = userEmail;
            body = new java.util.Date().toString()
                    + "\n\nYour administrator has rest your password.\n"
                    + "The temporary password is listed below.\n"
                    + "Once you have signed in, you will be required to change your password.\n"
                    + "If you have any questions, please contact the administrator.\n\n"
                    + "Temporary password:\n"
                    + temporaryPassword;
            sent = EmailUtility.sendMail(to, from, subject, body, isBodyHTML,
                    emailPassword, request);
            if (!sent) {
                EmailUtility.logEmailError(to, from, subject, "AdministrationServlet", false);
                request.setAttribute("errorMessage", "The email function encountered "
                        + "a problem.  See the server logs for additional details.");
            }
        }
    }

    /**
     * This method sets the session attribute for user details.
     *
     * @param userName the user name
     * @param session the session object
     */
    private void setDetailedUser(String userName, HttpSession session) {
        User detailedUser = AdministrationDataAccess.getUserDetails(userName);
        session.setAttribute(SessionObjectUtility.DETAILED_USER, detailedUser);
    }

    /**
     * This method sets the request attribute for unread password reset
     * requests.
     *
     * @param request the request
     */
    private void setPasswordResetRequests(HttpServletRequest request) {
        ArrayList<PasswordResetRequest> passwordResetRequests
                = AdministrationDataAccess.readUnreadPasswordResetRequests();
        request.setAttribute("passwordResetRequests", passwordResetRequests);
    }

    /**
     * This method sets the session attribute for email message configurations.
     *
     * @param clinicId the clinic ID
     * @param session the session
     */
    private void setEmailMessageConfigurations(int clinicId, HttpSession session) {

        // setting the email configuration in the session
        session.setAttribute(SessionObjectUtility.SELECTED_EMAIL_MESSAGE_CONFIGURATION, null);
        EmailMessageConfigurationContainer container
                = AdministrationDataAccess.getEmailMessageConfigurations(clinicId);
        session.setAttribute(SessionObjectUtility.EMAIL_MESSAGE_CONFIGURATION_CONTAINER, container);
    }

    /**
     * This method evaluates new email reminder configuration input to indicate
     * if the entered subject and language match an existing configuration.
     *
     * @param newConfiguration the new configuration
     * @param existingConfigurations the map of existing configurations
     * @return true if the new configuration matches an existing one, false
     * otherwise
     */
    private boolean checkForDuplicateEmailConfiguration(EmailMessageConfiguration newConfiguration,
            EmailMessageConfigurationContainer existingConfigurations) {
        for (EmailMessageConfiguration configuration : existingConfigurations.getEmailMessageConfigurations().values()) {
            if ((newConfiguration.getEmailMessage().getSubject().trim()
                    .equalsIgnoreCase(configuration.getEmailMessage().getSubject().trim()))
                    && (newConfiguration.getEmailMessage().getLanguage().trim()
                    .equalsIgnoreCase(configuration.getEmailMessage().getLanguage().trim()))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Since an email subject is associated with only one filter, this method is
     * used to evaluate new email reminder configuration input to indicate if
     * the selected filter does not match an existing filter for the same
     * subject.
     *
     * @param newConfiguration the new configuration
     * @param existingConfigurations the map of existing configurations
     * @return true if the new configuration contains an inconsistent filter
     */
    private boolean checkForInconsistentFilter(EmailMessageConfiguration newConfiguration,
            EmailMessageConfigurationContainer existingConfigurations) {
        for (EmailMessageConfiguration configuration : existingConfigurations.getEmailMessageConfigurations().values()) {
            if ((newConfiguration.getEmailMessage().getSubject().trim()
                    .equalsIgnoreCase(configuration.getEmailMessage().getSubject().trim()))
                    && (!newConfiguration.getCallListFilter().trim()
                    .equalsIgnoreCase(configuration.getCallListFilter().trim()))) {
                return true;
            }
        }
        return false;
    }

    /**
     * This method retrieves all quality reference configurations from the
     * database and updates the session.
     *
     * @param session the HttpSession object
     */
    private void updateQualityReferences(HttpSession session) {

        // setting the new quality configurations in the session
        ArrayList<QualityReferenceConfiguration> newConfigurations
                = AdministrationDataAccess.retrieveQualityReferenceConfigurations();
        session.setAttribute(SessionObjectUtility.QUALITY_CONFIGURATIONS, newConfigurations);
    }

    /**
     * This method retrieves updated values for the references in the
     * application and sets the references attribute of the ServletContext. The
     * 'setAttribute' method of the ServletContext is thread-safe, so this is
     * the preferred method for updating this application-scoped object.
     *
     * @param session the HttpSession object
     */
    private void refreshReferences(HttpSession session) {
        ReferenceContainer rc = ReferencesDataAccess.getReferenceContainer();
        session.getServletContext().setAttribute("references", rc);
    }
}
