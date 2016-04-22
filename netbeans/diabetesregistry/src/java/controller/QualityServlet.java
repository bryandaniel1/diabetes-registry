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

import clinic.CategoricalResult;
import clinic.Patient;
import clinic.User;
import data.PatientIO;
import data.QualityIO;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import util.SessionObjectUtil;
import util.StringUtil;

/**
 * Coordinates the activities of the quality checklist
 *
 * @author Bryan Daniel
 * @version 1, April 8, 2016
 */
public class QualityServlet extends HttpServlet {

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
        int index = 0;
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        String url = "/quality/index.jsp";
        ArrayList<Patient> patients;
        String message;
        int clinicId;
        if (session.getAttribute("clinicId") == null) {
            clinicId = EMPTY_VALUE;
        } else {
            clinicId = (int) session.getAttribute("clinicId");
        }
        String action = request.getParameter("action");

        if (action == null) {
            action = "get list";
        }

        switch (action) {
            case "get list":
                if ((clinicId == EMPTY_VALUE)
                        && (user.getClinics().size() > EMPTY_VALUE)) {
                    clinicId = user.getClinics().get(index).getClinicId();
                    session.setAttribute("clinicId", clinicId);
                }
                patients = (ArrayList<Patient>) session.getAttribute("patients");
                if (patients == null) {
                    patients = PatientIO.getPatients(clinicId,
                            session.getServletContext()
                            .getAttribute("referenceCharacters"));
                    session.setAttribute("patients", patients);
                }
                url = "/quality/index.jsp";
                break;
            case "getClinic":
                String clinicSelect = request.getParameter("clinicselect");
                try {
                    clinicId = Integer.parseInt(clinicSelect);
                    patients = PatientIO.getPatients(clinicId,
                            session.getServletContext()
                            .getAttribute("referenceCharacters"));
                    session.setAttribute("clinicId", clinicId);
                    session.setAttribute("patients", patients);
                    SessionObjectUtil.resetClinicObjects(session);
                    url = "/quality/index.jsp";
                } catch (NumberFormatException nfe) {
                    message = "clinic id invalid";
                    request.setAttribute("errorMessage", message);
                }
                break;
            case "getPatient":
                String patientSelect = request.getParameter("patientselect");
                try {
                    int patientId = Integer.parseInt(patientSelect);
                    patients = (ArrayList<Patient>) session.getAttribute("patients");
                    for (Patient p : patients) {
                        if (p.getPatientId() == patientId) {
                            session.setAttribute("patient", p);
                            SessionObjectUtil.resetPatientObjects(session);
                            break;
                        }
                    }
                    url = "/quality/index.jsp";
                } catch (NumberFormatException nfe) {
                    message = "patient id invalid";
                    request.setAttribute("errorMessage", message);
                }
                break;
            case "saveChecklist":
                boolean validData = true;
                Patient patient = (Patient) session.getAttribute("patient");
                int patientId = patient.getPatientId();

                /* validating date entered */
                String dateEnteredString = request.getParameter("checklistDate");
                Date checklistDate = null;
                if ((dateEnteredString != null) && (dateEnteredString.trim().length() != 0)) {
                    if (StringUtil.dateCheck(dateEnteredString)) {
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

                /* verifying a clinic ID is selected*/
                if (clinicId == EMPTY_VALUE) {
                    message = "You must select a clinic.";
                    request.setAttribute("errorMessage", message);
                    validData = false;
                }
                if (validData) {
                    String[] checklistItems = request.getParameterValues("list");
                    if ((checklistItems != null) && (checklistItems.length > 0)) {
                        boolean dataSaved = QualityIO.saveChecklist(patientId,
                                checklistDate, checklistItems, user.getUserName(),
                                clinicId);
                        if (dataSaved) {
                            ArrayList<CategoricalResult> recentChecklistItems
                                    = QualityIO.getMostRecentChecklistItems(patientId,
                                            clinicId);
                            session.setAttribute("recentChecklistItems", recentChecklistItems);
                            message = "Checklist saved successfully";
                            request.setAttribute("message", message);
                        }
                    } else {
                        message = "No items were selected.";
                        request.setAttribute("errorMessage", message);
                    }
                }
                url = "/quality/index.jsp";
                break;
        }

        Patient p = (Patient) session.getAttribute("patient");
        if ((p != null) && (clinicId != EMPTY_VALUE)) {
            ArrayList<CategoricalResult> recentChecklistItems
                    = (ArrayList<CategoricalResult>) session.getAttribute("recentChecklistItems");
            if (recentChecklistItems == null) {
                recentChecklistItems
                        = QualityIO.getMostRecentChecklistItems(p.getPatientId(),
                                clinicId);
                session.setAttribute("recentChecklistItems", recentChecklistItems);
            }
        }

        getServletContext().getRequestDispatcher(url)
                .forward(request, response);
    }
}
