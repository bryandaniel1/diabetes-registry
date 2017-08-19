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

import data.CallListDataAccess;
import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import registry.Clinic;
import registry.EmailMessage;
import registry.Patient;
import registry.ReferenceContainer;
import utility.CallListUtility;
import utility.EmailUtility;
import utility.SessionObjectUtility;

/**
 * This HttpServlet class coordinates the functions of the call lists page.
 *
 * @author Bryan Daniel
 * @version 2, March 16, 2017
 */
public class CallListServlet extends HttpServlet {

    /**
     * Serial version UID
     */
    private static final long serialVersionUID = -2942951295559564059L;

    /**
     * Handles the HTTP <code>GET</code> method. This method forwards the
     * request to the call list page if the value for callListPatients has not
     * been set. Otherwise, the doPost method is invoked.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String url = "/calllists/index.jsp";
        ArrayList<Patient> callListPatients
                = (ArrayList<Patient>) session.getAttribute(SessionObjectUtility.CALL_LIST_PATIENTS);
        if (callListPatients == null) {
            getServletContext().getRequestDispatcher(url).forward(request, response);
        } else {
            doPost(request, response);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method. This method processes the
     * requests for retrieving patient call lists, sorting the call lists by
     * last name, sorting the call lists by the most recent measurement date,
     * and sending reminder email messages to selected patients.
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
        String url = "/calllists/index.jsp";
        int clinicId = ReferenceContainer.CLINIC_ID;
        String message;
        String action = request.getParameter("action");

        if (action == null) {
            action = "";
        }

        switch (action) {
            case "getCallList": {

                SessionObjectUtility.resetClinicObjects(session);

                /* The measurement date type is determined in the data access class. */
                CallListDataAccess.LastMeasurementDateType[] measurementDateType
                        = new CallListDataAccess.LastMeasurementDateType[1];
                String measurementDateTypeHeader = null;

                /* the subject of the call list selected by the user */
                String subject = request.getParameter("listSubject");
                ArrayList<Patient> callListPatients
                        = CallListDataAccess.getCallList(clinicId, subject, measurementDateType,
                                session.getServletContext()
                                .getAttribute("referenceCharacters"));
                if (callListPatients == null) {
                    message = "No patients for the call list were found.";
                    request.setAttribute("message", message);
                } else {
                    if (measurementDateType[0] != null) {
                        switch (measurementDateType[0]) {
                            case BP:
                                measurementDateTypeHeader = "Date of Last BP";
                                break;
                            case A1C:
                                measurementDateTypeHeader = "Date of Last A1C";
                                break;
                            case NONE:
                                measurementDateTypeHeader = null;
                            default:
                                break;
                        }
                    }
                    session.setAttribute(SessionObjectUtility.CALL_LIST_PATIENTS, callListPatients);
                    session.setAttribute(SessionObjectUtility.CALL_LIST_SUBJECT, subject);
                    session.setAttribute(SessionObjectUtility.MEASUREMENT_DATE_TYPE_HEADER, measurementDateTypeHeader);
                    request.setAttribute("callListDateSort", 1);
                }
                break;
            }
            case "sortByLastName": {
                ArrayList<Patient> callListPatients
                        = (ArrayList<Patient>) session.getAttribute(SessionObjectUtility.CALL_LIST_PATIENTS);
                CallListUtility.sortPatients(callListPatients, CallListUtility.SortType.LAST_NAME);
                request.setAttribute("callListNameSort", 1);
                break;
            }
            case "sortByDate": {
                ArrayList<Patient> callListPatients
                        = (ArrayList<Patient>) session.getAttribute(SessionObjectUtility.CALL_LIST_PATIENTS);
                CallListUtility.sortPatients(callListPatients, CallListUtility.SortType.LAST_MEASUREMENT_DATE);
                request.setAttribute("callListDateSort", 1);
                break;
            }
            case "reverseSortByLastName": {
                ArrayList<Patient> callListPatients
                        = (ArrayList<Patient>) session.getAttribute(SessionObjectUtility.CALL_LIST_PATIENTS);
                CallListUtility.reverseSortPatients(callListPatients, CallListUtility.SortType.LAST_NAME);
                break;
            }
            case "reverseSortByDate": {
                ArrayList<Patient> callListPatients
                        = (ArrayList<Patient>) session.getAttribute(SessionObjectUtility.CALL_LIST_PATIENTS);
                CallListUtility.reverseSortPatients(callListPatients, CallListUtility.SortType.LAST_MEASUREMENT_DATE);
                break;
            }
            case "sendEmails": {

                /* clinic email */
                ReferenceContainer rc = (ReferenceContainer) session.getServletContext()
                        .getAttribute("references");
                Clinic clinic = rc.getClinic();
                String adminEmail = clinic.getEmailAddress();

                if (adminEmail == null) {
                    message = "The clinic must have an email address.  "
                            + "Please update the clinic email.";
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
                        = (ArrayList<Patient>) session.getAttribute(SessionObjectUtility.CALL_LIST_PATIENTS);
                String subject = (String) session.getAttribute(SessionObjectUtility.CALL_LIST_SUBJECT);
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
                            = CallListDataAccess.getEmailMessages(clinicId, subject);
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
                break;
            }
            default:
                break;
        }
        getServletContext().getRequestDispatcher(url)
                .forward(request, response);
    }

    /**
     * This method sends reminder email messages to a list of patients.
     *
     * @param emailPatients the list of patients to be sent messages
     * @param adminEmail the administrator email address
     * @param messages the list of messages to send
     * @param emailPassword the password
     * @param request the request object
     */
    private void sendBatchEmail(ArrayList<Patient> emailPatients, String adminEmail,
            ArrayList<EmailMessage> messages, String emailPassword,
            HttpServletRequest request) {
        String from = adminEmail;
        boolean isBodyHTML = false;
        boolean sent = utility.EmailUtility.sendBatchMail(emailPatients, from,
                messages, isBodyHTML, emailPassword, request);
        if (!sent) {
            EmailUtility.logEmailError(null, null, null, "CallListServlet", true);
            request.setAttribute("errorMessage", "The email function encountered "
                    + "a problem.  See the server logs for additional details.");
        } else {
            String message = "The patient reminders were sent successfully.";
            request.setAttribute("message", message);
        }
    }
}
