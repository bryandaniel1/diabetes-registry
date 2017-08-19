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
import utility.SessionObjectUtility;
import utility.StringUtility;

/**
 * This HttpServlet class coordinates the add, update, and retrieval activities
 * for patient information.
 *
 * @author Bryan Daniel
 * @version 2, March 16, 2017
 */
public class PatientServlet extends HttpServlet {

    /**
     * Serial version UID
     */
    private static final long serialVersionUID = 8083513236024731617L;

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
     * navigation of the new-patient and update-patient pages and processes the
     * requests for adding new patients to the registry, retrieving individual
     * patient demographic information, and updating patient information.
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
        String url = "/updatepatient/index.jsp";
        int clinicId = ReferenceContainer.CLINIC_ID;
        String message;
        String action = request.getParameter("action");

        if (action == null) {
            action = "Go to page";
        }

        switch (action) {
            case "Go to page":
                String requestURI = request.getRequestURI();
                if (requestURI.contains("/newpatient")) {
                    url = "/newpatient/index.jsp";
                } else if (requestURI.contains("/updatepatient")) {
                    ArrayList<Patient> patients
                            = (ArrayList<Patient>) session.getAttribute(SessionObjectUtility.PATIENTS);
                    if (patients == null) {
                        patients = PatientDataAccess.getPatients(clinicId,
                                session.getServletContext()
                                .getAttribute("referenceCharacters"));
                        session.setAttribute(SessionObjectUtility.PATIENTS, patients);
                    }
                }
                break;
            case "add": {
                boolean validData = true;
                url = "/newpatient/index.jsp";

                /* new patient first name *required* */
                String firstName = request.getParameter("firstName");
                if ((firstName == null) || (firstName.trim().length() == 0)) {
                    message = "A first name must be entered.";
                    request.setAttribute("errorMessage", message);
                    break;
                }
                if (StringUtility.tooLongForShortVarChar(firstName)) {
                    message = "The first name must be 50 characters or less.";
                    request.setAttribute("errorMessage", message);
                    validData = false;
                }

                /* new patient last name *required* */
                String lastName = request.getParameter("lastName");
                if ((lastName == null) || (lastName.trim().length() == 0)) {
                    message = "A last name must be entered.";
                    request.setAttribute("errorMessage", message);
                    break;
                }
                if (StringUtility.tooLongForShortVarChar(lastName)) {
                    message = "The last name must be 50 characters or less.";
                    request.setAttribute("errorMessage", message);
                    validData = false;
                }

                /* new patient birth date *required* */
                String birthDateString = request.getParameter("birthDate");
                if ((birthDateString == null) || (birthDateString.trim().length() == 0)) {
                    message = "A birth date must be entered.";
                    request.setAttribute("errorMessage", message);
                    break;
                }
                Date birthDate = null;
                if (StringUtility.dateCheck(birthDateString)) {
                    birthDate = Date.valueOf(birthDateString);
                } else {
                    message = "The birth date does not conform to the pattern, "
                            + "YYYY-MM-DD.";
                    request.setAttribute("errorMessage", message);
                    validData = false;
                }

                /* new patient address */
                String address = request.getParameter("address");
                if ((address != null) && (address.trim().length() != 0)
                        && StringUtility.tooLongForEmailVarChar(address)) {
                    message = "The address must be 255 characters or less.";
                    request.setAttribute("errorMessage", message);
                    validData = false;
                }

                /* new patient phone number */
                String contactNumber = request.getParameter("phoneNumber");
                if ((contactNumber != null) && (contactNumber.trim().length() != 0)
                        && (StringUtility.tooLongForShortVarChar(contactNumber))) {
                    message = "The contact number must be 50 characters or less.";
                    request.setAttribute("errorMessage", message);
                    validData = false;
                }

                /* new patient gender *required* */
                String gender = request.getParameter("gender");
                if ((gender == null) || (gender.trim().length() == 0)) {
                    message = "A gender must be entered.";
                    request.setAttribute("errorMessage", message);
                    break;
                }
                if (StringUtility.tooLongForShortVarChar(gender)) {
                    message = "The gender must be 50 characters or less.";
                    request.setAttribute("errorMessage", message);
                    validData = false;
                }

                /* new patient race *required* */
                String race = request.getParameter("race");
                if ((race == null) || (race.trim().length() == 0)) {
                    message = "A race must be entered.";
                    request.setAttribute("errorMessage", message);
                    break;
                }
                if (StringUtility.tooLongForShortVarChar(race)) {
                    message = "The race must be 50 characters or less.";
                    request.setAttribute("errorMessage", message);
                    validData = false;
                }

                /* new patient email */
                String email = request.getParameter("email");
                if ((email != null) && (email.trim().length() != 0)
                        && StringUtility.tooLongForEmailVarChar(email)) {
                    message = "The email must be 255 characters or less.";
                    request.setAttribute("errorMessage", message);
                    validData = false;
                }

                /* new patient language */
                String language = request.getParameter("language");
                if ((language != null) && (language.trim().length() != 0)
                        && StringUtility.tooLongForShortVarChar(language)) {
                    message = "The language must be 50 characters or less.";
                    request.setAttribute("errorMessage", message);
                    validData = false;
                }

                /* new patient start date *requried* */
                String startDateString = request.getParameter("startDate");
                if ((startDateString == null) || (startDateString.trim().length() == 0)) {
                    message = "A start date must be entered.";
                    request.setAttribute("errorMessage", message);
                    break;
                }
                Date startDate = null;
                if (StringUtility.dateCheck(startDateString)) {
                    startDate = Date.valueOf(startDateString);
                } else {
                    message = "The start date does not conform to the pattern, "
                            + "YYYY-MM-DD.";
                    request.setAttribute("errorMessage", message);
                    validData = false;
                }

                if (validData) {
                    boolean successfulAdd = PatientDataAccess.addPatient(firstName,
                            lastName, birthDate, address, contactNumber,
                            gender, race, email, language, startDate, clinicId,
                            session.getServletContext()
                            .getAttribute("referenceCharacters"));

                    if (successfulAdd) {
                        message = "Patient was added successfully!";
                        ArrayList<Patient> patients
                                = PatientDataAccess.getPatients(clinicId,
                                        session.getServletContext()
                                        .getAttribute("referenceCharacters"));
                        session.setAttribute(SessionObjectUtility.PATIENTS, patients);
                        SessionObjectUtility.resetClinicObjects(session);

                        request.setAttribute("message", message);
                    } else {
                        message = "The patient could not be added.";
                        request.setAttribute("errorMessage", message);
                    }
                }
                break;
            }
            case "getPatient":
                String patientSelect = request.getParameter("patientselect");
                try {
                    int patientId = Integer.parseInt(patientSelect);
                    ArrayList<Patient> patients
                            = (ArrayList<Patient>) session.getAttribute(SessionObjectUtility.PATIENTS);
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
            case "updatePatient": {
                boolean validData = true;
                Patient patient = (Patient) session.getAttribute(SessionObjectUtility.PATIENT);
                int patientId = patient.getPatientId();

                /* patient first name *required* */
                String firstName = request.getParameter("firstName");
                if ((firstName == null) || (firstName.trim().length() == 0)) {
                    message = "A first name must be entered.";
                    request.setAttribute("errorMessage", message);
                    break;
                }
                if (StringUtility.tooLongForShortVarChar(firstName)) {
                    message = "The first name must be 50 characters or less.";
                    request.setAttribute("errorMessage", message);
                    validData = false;
                }

                /* patient last name *requried* */
                String lastName = request.getParameter("lastName");
                if ((lastName == null) || (lastName.trim().length() == 0)) {
                    message = "A last name must be entered.";
                    request.setAttribute("errorMessage", message);
                    break;
                }
                if (StringUtility.tooLongForShortVarChar(lastName)) {
                    message = "The last name must be 50 characters or less.";
                    request.setAttribute("errorMessage", message);
                    validData = false;
                }

                /* patient birth date *required* */
                String birthDateString = request.getParameter("birthDate");
                if ((birthDateString == null) || (birthDateString.trim().length() == 0)) {
                    message = "A birth date must be entered.";
                    request.setAttribute("errorMessage", message);
                    break;
                }
                Date birthDate = null;
                if (StringUtility.dateCheck(birthDateString)) {
                    birthDate = Date.valueOf(birthDateString);
                } else {
                    message = "The birth date does not conform to the pattern, "
                            + "YYYY-MM-DD.";
                    request.setAttribute("errorMessage", message);
                    validData = false;
                }

                /* patient address */
                String address = request.getParameter("address");
                if ((address != null) && (address.trim().length() != 0)
                        && StringUtility.tooLongForEmailVarChar(address)) {
                    message = "The address must be 255 characters or less.";
                    request.setAttribute("errorMessage", message);
                    validData = false;
                }

                /* patient contact number */
                String contactNumber = request.getParameter("phoneNumber");
                if ((contactNumber != null) && (contactNumber.trim().length() != 0)
                        && (StringUtility.tooLongForShortVarChar(contactNumber))) {
                    message = "The contact number must be 50 characters or less.";
                    request.setAttribute("errorMessage", message);
                    validData = false;
                }

                /* patient gender *required* */
                String gender = request.getParameter("gender");
                if ((gender == null) || (gender.trim().length() == 0)) {
                    message = "A gender must be entered.";
                    request.setAttribute("errorMessage", message);
                    break;
                }
                if (StringUtility.tooLongForShortVarChar(gender)) {
                    message = "The gender must be 50 characters or less.";
                    request.setAttribute("errorMessage", message);
                    validData = false;
                }

                /* patient race *requried* */
                String race = request.getParameter("race");
                if ((race == null) || (race.trim().length() == 0)) {
                    message = "A race must be entered.";
                    request.setAttribute("errorMessage", message);
                    break;
                }
                if (StringUtility.tooLongForShortVarChar(race)) {
                    message = "The race must be 50 characters or less.";
                    request.setAttribute("errorMessage", message);
                    validData = false;
                }

                /* patient email address */
                String email = request.getParameter("email");
                if ((email != null) && (email.trim().length() != 0)
                        && StringUtility.tooLongForEmailVarChar(email)) {
                    message = "The email must be 255 characters or less.";
                    request.setAttribute("errorMessage", message);
                    validData = false;
                }

                /* patient language */
                String language = request.getParameter("language");
                if ((language != null) && (language.trim().length() != 0)
                        && StringUtility.tooLongForShortVarChar(language)) {
                    message = "The language must be 50 characters or less.";
                    request.setAttribute("errorMessage", message);
                    validData = false;
                }

                /* patient reason for inactivity */
                String reason = request.getParameter("reason");
                if ((reason != null) && (reason.trim().length() != 0)
                        && StringUtility.tooLongForShortVarChar(reason)) {
                    message = "The reason for inactivity must be 50 characters or less.";
                    request.setAttribute("errorMessage", message);
                    validData = false;
                }

                /* patient start date *required* */
                String startDateString = request.getParameter("startDate");
                if ((startDateString == null) || (startDateString.trim().length() == 0)) {
                    message = "A start date must be entered.";
                    request.setAttribute("errorMessage", message);
                    break;
                }
                Date startDate = null;
                if (StringUtility.dateCheck(startDateString)) {
                    startDate = Date.valueOf(startDateString);
                } else {
                    message = "The start date does not conform to the pattern, "
                            + "YYYY-MM-DD.";
                    request.setAttribute("errorMessage", message);
                    validData = false;
                }

                if (validData) {
                    boolean successfulUpdate = PatientDataAccess.updatePatient(patientId,
                            firstName, lastName, birthDate, address, contactNumber,
                            gender, race, email, language, reason, startDate,
                            session.getServletContext()
                            .getAttribute("referenceCharacters"));

                    if (successfulUpdate) {
                        message = "Patient was updated successfully!";
                        ArrayList<Patient> patients
                                = PatientDataAccess.getPatients(clinicId,
                                        session.getServletContext()
                                        .getAttribute("referenceCharacters"));
                        session.setAttribute(SessionObjectUtility.PATIENTS, patients);
                        for (Patient p : patients) {
                            if (p.getPatientId() == patientId) {
                                session.setAttribute(SessionObjectUtility.PATIENT, p);
                                SessionObjectUtility.resetPatientObjects(session);
                                break;
                            }
                        }
                        request.setAttribute("message", message);
                    } else {
                        message = "The patient could not be updated.";
                        request.setAttribute("errorMessage", message);
                    }
                }
                break;
            }
            default:
                break;
        }
        getServletContext().getRequestDispatcher(url)
                .forward(request, response);
    }
}
