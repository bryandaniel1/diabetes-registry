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

import data.PatientDataAccess;
import data.PatientTreatmentDataAccess;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import registry.Patient;
import registry.ReferenceContainer;
import registry.User;
import utility.SessionObjectUtility;
import utility.StringUtility;

/**
 * This HttpServlet class coordinates the treatment entry activity on the
 * treatment page.
 *
 * @author Bryan Daniel
 * @version 2, March 16, 2017
 */
public class PatientTreatmentServlet extends HttpServlet {

    /**
     * Serial version UID
     */
    private static final long serialVersionUID = -5718844091568294087L;

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
     * navigation of the treatment page and processes the requests for
     * documenting a patient's prescribed medications.
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
        String url = "/treatment/index.jsp";
        int clinicId = ReferenceContainer.CLINIC_ID;
        User user = (User) session.getAttribute(SessionObjectUtility.USER);
        ArrayList<Patient> patients;
        String message;
        String action = request.getParameter("action");

        if (action == null) {
            action = "get list";
        }

        switch (action) {
            case "get list":
                patients = (ArrayList<Patient>) session.getAttribute(SessionObjectUtility.PATIENTS);
                if (patients == null) {
                    patients = PatientDataAccess.getPatients(clinicId,
                            session.getServletContext()
                            .getAttribute("referenceCharacters"));
                    session.setAttribute(SessionObjectUtility.PATIENTS, patients);
                }
                break;
            case "getPatient":
                String patientSelect = request.getParameter("patientselect");
                try {
                    int patientId = Integer.parseInt(patientSelect);
                    patients = (ArrayList<Patient>) session.getAttribute(SessionObjectUtility.PATIENTS);
                    for (Patient p : patients) {
                        if (p.getPatientId() == patientId) {
                            session.setAttribute(SessionObjectUtility.PATIENT, p);
                            SessionObjectUtility.resetPatientObjects(session);
                            break;
                        }
                    }
                } catch (NumberFormatException nfe) {
                    message = "patient id invalid";
                    request.setAttribute("errorMessage", message);
                }
                break;
            case "addTreatment":
                boolean validData = true;
                Patient patient = (Patient) session.getAttribute(SessionObjectUtility.PATIENT);
                int patientId = patient.getPatientId();
                String userName = user.getUserName();

                /* validating input for Rx class and medication */
                String rxClass = request.getParameter("rxClass");
                String[] medications = request.getParameterValues("medications");
                if ((((rxClass != null) && (rxClass.trim().length() > 0))
                        && ((medications == null) || (medications.length == 0)))
                        || (((medications != null) && (medications.length > 0))
                        && ((rxClass == null) || (rxClass.trim().length() == 0)))) {
                    message = "Treatment must include both prescription class and medication.";
                    request.setAttribute("errorMessage", message);
                    validData = false;
                } else if (((medications == null) || (medications.length == 0))
                        && ((rxClass == null) || (rxClass.trim().length() == 0))) {
                    message = "Prescription class and medication are required.";
                    request.setAttribute("errorMessage", message);
                    validData = false;
                } else {
                    for (String s : medications) {
                        if (StringUtility.tooLongForShortVarChar(s)) {
                            message = "The medication value must be 50 characters or less.";
                            request.setAttribute("errorMessage", message);
                            validData = false;
                        }
                    }
                    if (StringUtility.tooLongForShortVarChar(rxClass)) {
                        message = "The rx class value must be 50 characters or less.";
                        request.setAttribute("errorMessage", message);
                        validData = false;
                    }
                }

                /* validating date reviewed */
                String dateEnteredString = request.getParameter("treatmentReviewDate");
                Date treatmentReviewDate = null;
                if ((dateEnteredString != null) && (dateEnteredString.trim().length() != 0)) {
                    if (StringUtility.dateCheck(dateEnteredString)) {
                        treatmentReviewDate = Date.valueOf(dateEnteredString);
                    } else {
                        message = "The date of results does not conform to the pattern, "
                                + "YYYY-MM-DD.";
                        request.setAttribute("errorMessage", message);
                        validData = false;
                    }
                } else {
                    message = "The date of results is required.";
                    request.setAttribute("errorMessage", message);
                    validData = false;
                }

                if (validData) {
                    boolean treatmentSuccess
                            = PatientTreatmentDataAccess.addTreatment(patientId, rxClass,
                                    medications, treatmentReviewDate, userName,
                                    clinicId);

                    if (treatmentSuccess) {
                        message = "The treatment was updated successfully!";
                        request.setAttribute("message", message);
                    }
                }
                break;
            default:
                break;
        }
        getServletContext().getRequestDispatcher(url)
                .forward(request, response);
    }
}
