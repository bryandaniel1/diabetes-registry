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

import clinic.Clinic;
import clinic.ClinicRegistration;
import clinic.User;
import data.AdministrationIO;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import util.StringUtil;
import util.HashAndSaltUtil;

/**
 * Coordinates administration functions
 *
 * @author Bryan Daniel
 * @version 1, April 8, 2016
 */
public class AdministrationServlet extends HttpServlet {

    /**
     * Handles the HTTP <code>GET</code> method.
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
     * Handles the HTTP <code>POST</code> method.
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
        String url = "/index.jsp";
        String message;
        String adminEmail = null;
        String adminEmailPassword = null;
        Integer clinicId;
        String action = request.getParameter("action");

        if (action == null) {
            action = "administrate";
        }

        switch (action) {
            case "administrate":
                url = "/admin/index.jsp";
                break;
            case "addclinic":
                url = "/admin/newclinic.jsp";
                break;
            case "updateclinic":
                url = "/admin/updateclinic.jsp";
                break;
            case "removeuser":
                ArrayList<String> userNames = AdministrationIO.getUserNames();
                session.setAttribute("userNames", userNames);
                url = "/admin/removeuser.jsp";
                break;
            case "addclinicsubmit": {
                boolean validData = true;
                url = "/admin/newclinic.jsp";

                /* administrator clinic */
                String clinicIdString = request.getParameter("clinicselect");
                try {
                    clinicId = Integer.parseInt(clinicIdString);
                    Clinic adminClinic = AdministrationIO.getClinic(clinicId);
                    adminEmail = adminClinic.getEmailAddress();
                    if (adminEmail == null) {
                        message = "The administrator's clinic must have an email "
                                + "address.  Please update the administrator "
                                + "clinic email.";
                        request.setAttribute("errorMessage", message);
                        break;
                    }
                } catch (NumberFormatException nfe) {
                    message = "clinic id invalid";
                    request.setAttribute("errorMessage", message);
                    break;
                }

                /* admin email password */
                adminEmailPassword = request.getParameter("emailPassword");
                if ((adminEmailPassword == null)
                        || (adminEmailPassword.length() == 0)) {
                    message = "The email password for the administrator's clinic "
                            + "must be entered.";
                    request.setAttribute("errorMessage", message);
                    break;
                }

                /* new clinic name */
                String clinicName = request.getParameter("clinicName");
                if ((clinicName == null) || (clinicName.trim().length() == 0)) {
                    message = "A clinic name must be entered.";
                    request.setAttribute("errorMessage", message);
                    break;
                }
                if (StringUtil.tooLongForShortVarChar(clinicName)) {
                    message = "The first name must be 50 characters or less.";
                    request.setAttribute("errorMessage", message);
                    validData = false;
                }

                /* new clinic address */
                String address = request.getParameter("address");
                if ((address == null) || (address.trim().length() == 0)) {
                    message = "An address must be entered.";
                    request.setAttribute("errorMessage", message);
                    break;
                }
                if (StringUtil.tooLongForLongVarChar(address)) {
                    message = "The address must be 1000 characters or less.";
                    request.setAttribute("errorMessage", message);
                    validData = false;
                }

                /* new clinic phone number */
                String phoneNumber = request.getParameter("phoneNumber");
                if ((phoneNumber == null) || (phoneNumber.trim().length() == 0)) {
                    message = "A phone number must be entered.";
                    request.setAttribute("errorMessage", message);
                    break;
                }
                if (StringUtil.tooLongForShortVarChar(phoneNumber)) {
                    message = "The contact number must be 50 characters or less.";
                    request.setAttribute("errorMessage", message);
                    validData = false;
                }

                /* new clinic email address */
                String emailAddress = request.getParameter("email");
                if (StringUtil.tooLongForEmailVarChar(emailAddress)) {
                    message = "The email address must be 255 characters or less.";
                    request.setAttribute("errorMessage", message);
                    validData = false;
                }
                if (validData) {
                    String newRegistrationKey = StringUtil.generateKey();
                    String clinicSalt = HashAndSaltUtil.getSalt();
                    String clinicKey = null;
                    try {
                        clinicKey = HashAndSaltUtil
                                .hashBrownsWithSalt(newRegistrationKey, clinicSalt);
                    } catch (NoSuchAlgorithmException ex) {
                        Logger.getLogger(RegistrationServlet.class.getName())
                                .log(Level.SEVERE, null, ex);
                    }
                    boolean successfullyAdded
                            = AdministrationIO.addClinic(clinicName, address,
                                    phoneNumber, emailAddress, clinicKey,
                                    clinicSalt);
                    if (successfullyAdded) {
                        Clinic newClinic = new Clinic(0, clinicName, address,
                                phoneNumber, emailAddress);
                        sendNewClinicEmail(adminEmail, newRegistrationKey,
                                newClinic, adminEmailPassword, request);
                        message = "The new clinic has been added successfully.";
                        request.setAttribute("message", message);
                    } else {
                        message = "The clinic could not be added.";
                        request.setAttribute("errorMessage", message);
                    }
                }
                break;
            }
            case "getClinic": {
                url = "/admin/updateclinic.jsp";
                String clinicSelect = request.getParameter("clinicselect");

                try {
                    clinicId = Integer.parseInt(clinicSelect);
                    Clinic clinic = AdministrationIO.getClinic(clinicId);
                    request.setAttribute("clinic", clinic);
                } catch (NumberFormatException nfe) {
                    message = "clinic id invalid";
                    request.setAttribute("errorMessage", message);
                }
                break;
            }
            case "updateClinicInformation": {
                url = "/admin/updateclinic.jsp";
                boolean validData = true;
                int adminClinicId;

                /* clinic ID *required* */
                String clinicIdString = request.getParameter("clinicId");
                try {
                    clinicId = Integer.parseInt(clinicIdString);
                } catch (NumberFormatException nfe) {
                    message = "clinic id invalid";
                    request.setAttribute("errorMessage", message);
                    break;
                }

                /* option to change key */
                String newKey = request.getParameter("newKey");
                if ((newKey != null) && (newKey.trim().length() != 0)) {
                    newKey = StringUtil.generateKey();
                } else {
                    newKey = null;
                }

                /* administrator clinic */
                String adminClinicIdString = request.getParameter("adminclinicselect");
                Clinic adminClinic;
                try {
                    adminClinicId = Integer.parseInt(adminClinicIdString);
                    adminClinic = AdministrationIO.getClinic(adminClinicId);

                    /* admin email */
                    adminEmail = adminClinic.getEmailAddress();

                    /* email password */
                    adminEmailPassword = request.getParameter("emailPassword");

                    /**
                     * Administrator clinic must be sent an email to document a
                     * registration key change
                     */
                    if ((newKey != null) && (adminEmail == null)) {
                        message = "The administrator's clinic must have an email "
                                + "address to update a registration key.  "
                                + "Please update the administrator clinic email.";
                        request.setAttribute("errorMessage", message);
                        break;
                    } else if ((newKey != null) && ((adminEmailPassword == null)
                            || (adminEmailPassword.length() == 0))) {
                        message = "The email password for the administrator's clinic "
                                + "must be entered.";
                        request.setAttribute("errorMessage", message);
                        break;
                    }
                } catch (NumberFormatException nfe) {
                    message = "clinic id invalid";
                    request.setAttribute("errorMessage", message);
                    break;
                }

                /* clinic name *required* */
                String clinicName = request.getParameter("clinicName");
                if ((clinicName == null) || (clinicName.trim().length() == 0)) {
                    message = "A clinic name must be entered.";
                    request.setAttribute("errorMessage", message);
                    break;
                }
                if (StringUtil.tooLongForShortVarChar(clinicName)) {
                    message = "The first name must be 50 characters or less.";
                    request.setAttribute("errorMessage", message);
                    validData = false;
                }

                /* clinic address *required* */
                String address = request.getParameter("address");
                if ((address == null) || (address.trim().length() == 0)) {
                    message = "An address must be entered.";
                    request.setAttribute("errorMessage", message);
                    break;
                }
                if (StringUtil.tooLongForLongVarChar(address)) {
                    message = "The address must be 1000 characters or less.";
                    request.setAttribute("errorMessage", message);
                    validData = false;
                }

                /* clinic phone number *required* */
                String phoneNumber = request.getParameter("phoneNumber");
                if ((phoneNumber == null) || (phoneNumber.trim().length() == 0)) {
                    message = "A phone number must be entered.";
                    request.setAttribute("errorMessage", message);
                    break;
                }
                if (StringUtil.tooLongForShortVarChar(phoneNumber)) {
                    message = "The contact number must be 50 characters or less.";
                    request.setAttribute("errorMessage", message);
                    validData = false;
                }

                /* new clinic email address */
                String emailAddress = request.getParameter("email");
                if (StringUtil.tooLongForEmailVarChar(emailAddress)) {
                    message = "The email address must be 255 characters or less.";
                    request.setAttribute("errorMessage", message);
                    validData = false;
                }
                if (validData) {
                    String clinicKey = null;
                    String salt = null;
                    if (newKey != null) {
                        salt = HashAndSaltUtil.getSalt();
                        try {
                            clinicKey = HashAndSaltUtil
                                    .hashBrownsWithSalt(newKey, salt);
                        } catch (NoSuchAlgorithmException ex) {
                            Logger.getLogger(RegistrationServlet.class.getName())
                                    .log(Level.SEVERE, null, ex);
                        }
                    }
                    boolean successfullyUpdated
                            = AdministrationIO.updateClinic(clinicId, clinicName,
                                    address, phoneNumber, emailAddress,
                                    clinicKey, salt);
                    if (successfullyUpdated) {
                        Clinic updatedClinic = new Clinic(0, clinicName, address,
                                phoneNumber, emailAddress);

                        /* Only send email for key change */
                        if (newKey != null) {
                            sendUpdatedClinicEmail(adminEmail, newKey,
                                    updatedClinic, adminEmailPassword, request);
                        }
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
                url = "/admin/removeuser.jsp";
                String userName = request.getParameter("userselect");
                User detailedUser = AdministrationIO.getUserDetails(userName);
                session.setAttribute("detailedUser", detailedUser);
                break;
            }
            case "updateUserAccess": {
                url = "/admin/removeuser.jsp";
                String userName = request.getParameter("userName");
                String removeUser = request.getParameter("removeuser");
                boolean removeUserBoolean = false;
                if ((removeUser != null) && (removeUser.trim().length() != 0)) {
                    removeUserBoolean = true;
                }
                String clinicIdString = request.getParameter("userclinicselect");
                clinicId = null;
                if (removeUserBoolean) {
                    try {
                        clinicId = Integer.parseInt(clinicIdString);
                    } catch (NumberFormatException nfe) {
                        message = "A clinic must be selected if you elect to "
                                + "remove user registration.";
                        request.setAttribute("errorMessage", message);
                        break;
                    }
                }
                String terminateUser = request.getParameter("terminateuser");
                boolean terminateBoolean = false;
                if ((terminateUser != null) && (terminateUser.trim().length() != 0)) {
                    terminateBoolean = true;
                }
                if (terminateBoolean) {

                    /* administrator clinic */
                    String adminClinicIdString = request.getParameter("adminclinicselect");
                    Clinic adminClinic;
                    try {
                        int adminClinicId = Integer.parseInt(adminClinicIdString);
                        adminClinic = AdministrationIO.getClinic(adminClinicId);

                        /* admin email */
                        adminEmail = adminClinic.getEmailAddress();

                        /* email password */
                        adminEmailPassword = request.getParameter("emailPassword");

                        /**
                         * Administrator clinic must be sent an email to
                         * document a registration key change
                         */
                        if (adminEmail == null) {
                            message = "The administrator's clinic must have an email "
                                    + "address to update a registration key.  "
                                    + "Please update the administrator clinic email.";
                            request.setAttribute("errorMessage", message);
                            break;
                        } else if ((adminEmailPassword == null)
                                || (adminEmailPassword.length() == 0)) {
                            message = "The email password for the administrator's clinic "
                                    + "must be entered.";
                            request.setAttribute("errorMessage", message);
                            break;
                        }
                    } catch (NumberFormatException nfe) {
                        message = "clinic id invalid";
                        request.setAttribute("errorMessage", message);
                        break;
                    }
                }
                User detailedUser = (User) session.getAttribute("detailedUser");
                ArrayList<Clinic> userClinics = detailedUser.getClinics();
                ArrayList<ClinicRegistration> newRegistrations = new ArrayList<>();

                /* new registration keys generated if user is terminated */
                if (terminateBoolean) {
                    for (Clinic c : userClinics) {
                        String newRegistrationKey = StringUtil.generateKey();
                        String clinicSalt = HashAndSaltUtil.getSalt();
                        String hashedClinicKey;
                        try {
                            hashedClinicKey = HashAndSaltUtil
                                    .hashBrownsWithSalt(newRegistrationKey, clinicSalt);
                            newRegistrations
                                    .add(new ClinicRegistration(c, hashedClinicKey,
                                                    clinicSalt, newRegistrationKey));
                        } catch (NoSuchAlgorithmException ex) {
                            Logger.getLogger(RegistrationServlet.class.getName())
                                    .log(Level.SEVERE, null, ex);
                            message = "New registration keys could not be created.";
                            request.setAttribute("errorMessage", message);
                            break;
                        }
                    }
                }

                boolean userUpdatedSuccessfully
                        = AdministrationIO.updateUserAccess(userName,
                                removeUserBoolean, clinicId, terminateBoolean,
                                newRegistrations);
                if (userUpdatedSuccessfully) {
                    if (terminateBoolean) {
                        for (ClinicRegistration cr : newRegistrations) {
                            sendUpdatedClinicEmail(adminEmail, cr.getEmailKey(),
                                    cr.getClinic(), adminEmailPassword, request);
                        }
                    }
                    message = "User access has been updated.";
                    request.setAttribute("message", message);
                } else {
                    message = "User access could not be updated.";
                    request.setAttribute("errorMessage", message);
                }
                break;
            }
            default:
                break;
        }
        getServletContext().getRequestDispatcher(url)
                .forward(request, response);
    }

    /**
     * This method sends messages concerning a new clinic.
     *
     * @param adminEmail the administrator's email
     * @param newRegistrationKey the registration key
     * @param newClinic the new clinic
     */
    private void sendNewClinicEmail(String adminEmail, String newRegistrationKey,
            Clinic newClinic, String emailPassword, HttpServletRequest request) {
        String to = adminEmail;
        String from = adminEmail;
        String subject = "New Clinic";
        String body = new java.util.Date().toString()
                + "\n\nA new clinic has been added to the registry.\n"
                + "The details are listed below.\n\n"
                + "New Clinic:\n"
                + newClinic.toString() + "\n\n"
                + "Registration Key:\n"
                + newRegistrationKey;
        boolean isBodyHTML = false;
        boolean sent = util.EmailUtil.sendMail(to, from, subject, body, isBodyHTML,
                emailPassword, request);
        if (!sent) {
            this.log(
                    "Unable to send email. \n"
                    + "Here is the email you tried to send: \n"
                    + "=====================================\n"
                    + "TO: " + to + "\n"
                    + "FROM: " + from + "\n"
                    + "SUBJECT: " + subject + "\n"
                    + "\n"
                    + body + "\n\n");
            request.setAttribute("errorMessage", "The email function encountered "
                    + "a problem.  See the server logs for additional details.");
        }

    }

    /**
     * This method sends messages concerning an updated clinic.
     *
     * @param adminEmail the administrator's email
     * @param registrationKey the registration key
     * @param updatedClinic the updated clinic
     */
    private void sendUpdatedClinicEmail(String adminEmail, String registrationKey,
            Clinic updatedClinic, String emailPassword,
            HttpServletRequest request) {
        String to = adminEmail;
        String from = adminEmail;
        String subject = "Updated Clinic";
        String body;
        if (registrationKey != null) {
            body = new java.util.Date().toString()
                    + "\n\nA clinic has been updated in the registry.\n"
                    + "The details are listed below.\n\n"
                    + "Updated Clinic:\n"
                    + updatedClinic.toString() + "\n\n"
                    + "New Registration Key:\n"
                    + registrationKey;
        } else {
            body = "A clinic has been updated in the registry.\n"
                    + "The details are listed below.\n\n"
                    + "New Clinic:\n"
                    + updatedClinic.toString() + "\n\n";
        }
        boolean isBodyHTML = false;
        boolean sent = util.EmailUtil.sendMail(to, from, subject, body, isBodyHTML,
                emailPassword, request);
        if (!sent) {
            this.log(
                    "Unable to send email. \n"
                    + "Here is the email you tried to send: \n"
                    + "=====================================\n"
                    + "TO: " + to + "\n"
                    + "FROM: " + from + "\n"
                    + "SUBJECT: " + subject + "\n"
                    + "\n"
                    + body + "\n\n");
            request.setAttribute("errorMessage", "The email function encountered "
                    + "a problem.  See the server logs for additional details.");
        }
    }
}
