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
import clinic.EmailMessage;
import clinic.Patient;
import clinic.User;
import data.AdministrationIO;
import data.CallListIO;
import data.PatientIO;
import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import util.SessionObjectUtil;

/**
 * Coordinates the functions of the call lists page
 *
 * @author Bryan Daniel
 * @version 1, April 8, 2016
 */
public class CallListServlet extends HttpServlet {

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
        final int EMPTY_VALUE = 0;
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        String url = "/calllists/index.jsp";
        boolean validData = true;
        String message;
        int clinicId;
        if (session.getAttribute("clinicId") == null) {
            clinicId = EMPTY_VALUE;
        } else {
            clinicId = (int) session.getAttribute("clinicId");
        }
        String action = request.getParameter("action");

        if (action == null) {
            action = "";
        }

        switch (action) {
            case "getCallList": {

                /* clinic ID */
                String clinicIdString = request.getParameter("clinicselect");
                if ((clinicIdString != null) && (clinicIdString.trim().length()
                        != EMPTY_VALUE)) {
                    try {
                        clinicId = Integer.parseInt(clinicIdString);
                        ArrayList<Patient> patients
                                = PatientIO.getPatients(clinicId,
                                        session.getServletContext()
                                        .getAttribute("referenceCharacters"));
                        session.setAttribute("clinicId", clinicId);
                        session.setAttribute("patients", patients);
                        SessionObjectUtil.resetClinicObjects(session);
                    } catch (NumberFormatException nfe) {
                        message = "The clinic ID value is not valid.";
                        request.setAttribute("errorMessage", message);
                        validData = false;
                    }
                } else {
                    message = "A clinic must be selected.";
                    request.setAttribute("errorMessage", message);
                    validData = false;
                }
                if (validData) {

                    /* the subject of the call list */
                    String subject = request.getParameter("listSubject");
                    ArrayList<Patient> callListPatients
                            = CallListIO.getCallList(clinicId, subject,
                                    session.getServletContext()
                                    .getAttribute("referenceCharacters"));
                    if (callListPatients == null) {
                        message = "No patients for the call list were found.";
                        request.setAttribute("message", message);
                    } else {
                        session.setAttribute("callListPatients", callListPatients);
                        session.setAttribute("callListSubject", subject);
                    }
                }
                break;
            }
            case "sendEmails": {

                /* administrator clinic */
                Clinic adminClinic = AdministrationIO.getClinic(clinicId);
                String adminEmail;

                if (adminClinic != null) {
                    adminEmail = adminClinic.getEmailAddress();
                } else {
                    message = "You must select a clinic.";
                    request.setAttribute("errorMessage", message);
                    break;
                }

                if (adminEmail == null) {
                    message = "The administrator's clinic must have an email "
                            + "address.  Please update the administrator "
                            + "clinic email.";
                    request.setAttribute("errorMessage", message);
                    break;
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

                /* gathering patients and messages */
                ArrayList<Patient> callListPatients
                        = (ArrayList<Patient>) session.getAttribute("callListPatients");
                String subject = (String) session.getAttribute("callListSubject");
                ArrayList<Patient> emailPatients = new ArrayList<>();
                String[] checkBoxes = request.getParameterValues("emailList");
                if (checkBoxes != null) {
                    for (String s : checkBoxes) {
                        int patientId = Integer.parseInt(s);
                        for (Patient p : callListPatients) {
                            if (p.getPatientId() == patientId) {
                                emailPatients.add(p);
                            }
                        }
                    }
                    ArrayList<EmailMessage> messages
                            = CallListIO.getEmailMessages(clinicId, subject);
                    if ((!emailPatients.isEmpty()) && (messages != null)) {
                        sendBatchEmail(emailPatients, adminEmail, messages, adminEmailPassword,
                                request);
                    } else {
                        message = "No email messages were sent.";
                        request.setAttribute("errorMessage", message);
                    }
                } else {
                    message = "No patients were selected.";
                    request.setAttribute("errorMessage", message);
                }
            }
            default:
                break;
        }
        getServletContext().getRequestDispatcher(url)
                .forward(request, response);
    }

    /**
     * This method sends messages concerning an updated clinic.
     *
     * @param adminEmail the administrator's email
     * @param registrationKey the registration key
     * @param updatedClinic the updated clinic
     */
    private void sendBatchEmail(ArrayList<Patient> emailPatients, String adminEmail,
            ArrayList<EmailMessage> messages, String emailPassword,
            HttpServletRequest request) {
        String from = adminEmail;
        boolean isBodyHTML = false;
        boolean sent = util.EmailUtil.sendBatchMail(emailPatients, from,
                messages, isBodyHTML, emailPassword, request);
        if (!sent) {
            this.log("Unable to send email. \n");
            request.setAttribute("errorMessage", "The email function encountered "
                    + "a problem.  See the server logs for additional details.");
        } else {
            String message = "The patient reminders were sent successfully.";
            request.setAttribute("message", message);
        }
    }
}
