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
import data.QualityDataAccess;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import registry.CategoricalResult;
import registry.Patient;
import registry.ReferenceContainer;
import registry.User;
import utility.SessionObjectUtility;
import utility.StringUtility;

/**
 * This HttpServlet class coordinates the activities of the quality checklist
 * page.
 *
 * @author Bryan Daniel
 * @version 2, March 16, 2017
 */
public class QualityServlet extends HttpServlet {

    /**
     * Serial version UID
     */
    private static final long serialVersionUID = 1407603459965007333L;

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
     * navigation of the quality checklist page and processes the requests for
     * documenting completed activities related to patient care.
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
        String url = "/quality/index.jsp";
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
            case "saveChecklist":
                boolean validData = true;
                Patient patient = (Patient) session.getAttribute(SessionObjectUtility.PATIENT);
                int patientId = patient.getPatientId();

                /* validating date entered */
                String dateEnteredString = request.getParameter("checklistDate");
                Date checklistDate = null;
                if ((dateEnteredString != null) && (dateEnteredString.trim().length() != 0)) {
                    if (StringUtility.dateCheck(dateEnteredString)) {
                        checklistDate = Date.valueOf(dateEnteredString);
                    } else {
                        message = "The date entered does not conform to the pattern, "
                                + "YYYY-MM-DD.";
                        request.setAttribute("errorMessage", message);
                        validData = false;
                    }
                } else {
                    message = "The date is required to save the checklist.";
                    request.setAttribute("errorMessage", message);
                    validData = false;
                }

                if (validData) {
                    String[] checklistItems = request.getParameterValues("list");
                    if ((checklistItems != null) && (checklistItems.length > 0)) {
                        boolean dataSaved = QualityDataAccess.saveChecklist(patientId,
                                checklistDate, checklistItems, user.getUserName(),
                                clinicId);
                        if (dataSaved) {
                            ArrayList<CategoricalResult> recentChecklistItems
                                    = QualityDataAccess.getMostRecentChecklistItems(patientId,
                                            clinicId);
                            session.setAttribute(SessionObjectUtility.RECENT_CHECKLIST_ITEMS, recentChecklistItems);
                            message = "Checklist saved successfully";
                            request.setAttribute("message", message);
                        }
                    } else {
                        message = "No items were selected.";
                        request.setAttribute("errorMessage", message);
                    }
                }
                break;
            default:
                break;
        }

        Patient p = (Patient) session.getAttribute(SessionObjectUtility.PATIENT);
        if (p != null) {
            ArrayList<CategoricalResult> recentChecklistItems
                    = (ArrayList<CategoricalResult>) session.getAttribute(SessionObjectUtility.RECENT_CHECKLIST_ITEMS);
            if (recentChecklistItems == null) {
                recentChecklistItems
                        = QualityDataAccess.getMostRecentChecklistItems(p.getPatientId(),
                                clinicId);
                session.setAttribute(SessionObjectUtility.RECENT_CHECKLIST_ITEMS, recentChecklistItems);
            }
        }

        getServletContext().getRequestDispatcher(url)
                .forward(request, response);
    }
}
