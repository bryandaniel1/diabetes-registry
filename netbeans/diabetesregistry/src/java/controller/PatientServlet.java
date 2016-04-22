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

import clinic.Patient;
import clinic.User;
import data.PatientIO;
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
 * Coordinates the add, update, and retrieval activities for patient information
 *
 * @author Bryan Daniel
 * @version 1, April 8, 2016
 */
public class PatientServlet extends HttpServlet {

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
        String url = "/updatepatient/index.jsp";
        final int EMPTY_VALUE = 0;
        int index = 0;
        String message;
        int clinicId;
        if (session.getAttribute("clinicId") == null) {
            clinicId = EMPTY_VALUE;
        } else {
            clinicId = (int) session.getAttribute("clinicId");
        }
        String action = request.getParameter("action");
        User user = (User) session.getAttribute("user");
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
                            = (ArrayList<Patient>) session.getAttribute("patients");
                    if ((patients == null) && (user.getClinics().size()
                            > EMPTY_VALUE)) {
                        clinicId = user.getClinics().get(index).getClinicId();
                        patients = PatientIO.getPatients(clinicId,
                                session.getServletContext()
                                .getAttribute("referenceCharacters"));
                        session.setAttribute("clinicId", clinicId);
                        session.setAttribute("patients", patients);
                    }
                    url = "/updatepatient/index.jsp";
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
                if (StringUtil.tooLongForShortVarChar(firstName)) {
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
                if (StringUtil.tooLongForShortVarChar(lastName)) {
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
                if (StringUtil.dateCheck(birthDateString)) {
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
                        && StringUtil.tooLongForEmailVarChar(address)) {
                    message = "The address must be 255 characters or less.";
                    request.setAttribute("errorMessage", message);
                    validData = false;
                }

                /* new patient phone number */
                String contactNumber = request.getParameter("phoneNumber");
                if ((contactNumber != null) && (contactNumber.trim().length() != 0)
                        && (StringUtil.tooLongForShortVarChar(contactNumber))) {
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
                if (StringUtil.tooLongForShortVarChar(gender)) {
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
                if (StringUtil.tooLongForShortVarChar(race)) {
                    message = "The race must be 50 characters or less.";
                    request.setAttribute("errorMessage", message);
                    validData = false;
                }

                /* clinic ID *required* */
                String clinicIdString = request.getParameter("clinicId");
                try {
                    clinicId = Integer.parseInt(clinicIdString);
                } catch (NumberFormatException nfe) {
                    message = "The clinic ID is invalid.";
                    request.setAttribute("errorMessage", message);
                    validData = false;
                }

                /* new patient email */
                String email = request.getParameter("email");
                if ((email != null) && (email.trim().length() != 0)
                        && StringUtil.tooLongForEmailVarChar(email)) {
                    message = "The email must be 255 characters or less.";
                    request.setAttribute("errorMessage", message);
                    validData = false;
                }

                /* new patient language */
                String language = request.getParameter("language");
                if ((language != null) && (language.trim().length() != 0)
                        && StringUtil.tooLongForShortVarChar(language)) {
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
                if (StringUtil.dateCheck(startDateString)) {
                    startDate = Date.valueOf(startDateString);
                } else {
                    message = "The start date does not conform to the pattern, "
                            + "YYYY-MM-DD.";
                    request.setAttribute("errorMessage", message);
                    validData = false;
                }

                /* clinic ID */
                if (clinicId == EMPTY_VALUE) {
                    message = "You must select a clinic.";
                    request.setAttribute("errorMessage", message);
                    validData = false;
                }

                if (validData) {
                    boolean successfulAdd = PatientIO.addPatient(firstName,
                            lastName, birthDate, address, contactNumber,
                            gender, race, email, language, startDate, clinicId,
                            session.getServletContext()
                            .getAttribute("referenceCharacters"));

                    if (successfulAdd) {
                        message = "Patient was added successfully!";
                        ArrayList<Patient> patients
                                = PatientIO.getPatients(clinicId,
                                        session.getServletContext()
                                        .getAttribute("referenceCharacters"));
                        session.setAttribute("clinicId", clinicId);
                        session.setAttribute("patients", patients);
                        SessionObjectUtil.resetClinicObjects(session);

                        request.setAttribute("message", message);
                    } else {
                        message = "The patient could not be added.";
                        request.setAttribute("errorMessage", message);
                    }
                }
                break;
            }
            case "getClinic":
                String clinicSelect = request.getParameter("clinicselect");
                try {
                    clinicId = Integer.parseInt(clinicSelect);
                    ArrayList<Patient> patients = PatientIO.getPatients(clinicId,
                            session.getServletContext()
                            .getAttribute("referenceCharacters"));
                    session.setAttribute("clinicId", clinicId);
                    session.setAttribute("patients", patients);
                    SessionObjectUtil.resetClinicObjects(session);
                    url = "/updatepatient/index.jsp";
                } catch (NumberFormatException nfe) {
                    message = "clinic id invalid";
                    request.setAttribute("errorMessage", message);
                }
                break;
            case "getPatient":
                String patientSelect = request.getParameter("patientselect");
                try {
                    int patientId = Integer.parseInt(patientSelect);
                    ArrayList<Patient> patients
                            = (ArrayList<Patient>) session.getAttribute("patients");
                    for (Patient p : patients) {
                        if (p.getPatientId() == patientId) {
                            session.setAttribute("patient", p);
                            SessionObjectUtil.resetPatientObjects(session);
                            break;
                        }
                    }
                    url = "/updatepatient/index.jsp";
                } catch (NumberFormatException nfe) {
                    message = "patient id invalid";
                    request.setAttribute("errorMessage", message);
                }
                break;
            case "updatePatient": {
                boolean validData = true;
                url = "/updatepatient/index.jsp";
                Patient patient = (Patient) session.getAttribute("patient");
                int patientId = patient.getPatientId();

                /* patient first name *required* */
                String firstName = request.getParameter("firstName");
                if ((firstName == null) || (firstName.trim().length() == 0)) {
                    message = "A first name must be entered.";
                    request.setAttribute("errorMessage", message);
                    break;
                }
                if (StringUtil.tooLongForShortVarChar(firstName)) {
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
                if (StringUtil.tooLongForShortVarChar(lastName)) {
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
                if (StringUtil.dateCheck(birthDateString)) {
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
                        && StringUtil.tooLongForEmailVarChar(address)) {
                    message = "The address must be 255 characters or less.";
                    request.setAttribute("errorMessage", message);
                    validData = false;
                }

                /* patient contact number */
                String contactNumber = request.getParameter("phoneNumber");
                if ((contactNumber != null) && (contactNumber.trim().length() != 0)
                        && (StringUtil.tooLongForShortVarChar(contactNumber))) {
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
                if (StringUtil.tooLongForShortVarChar(gender)) {
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
                if (StringUtil.tooLongForShortVarChar(race)) {
                    message = "The race must be 50 characters or less.";
                    request.setAttribute("errorMessage", message);
                    validData = false;
                }

                /* patient email address */
                String email = request.getParameter("email");
                if ((email != null) && (email.trim().length() != 0)
                        && StringUtil.tooLongForEmailVarChar(email)) {
                    message = "The email must be 255 characters or less.";
                    request.setAttribute("errorMessage", message);
                    validData = false;
                }

                /* patient language */
                String language = request.getParameter("language");
                if ((language != null) && (language.trim().length() != 0)
                        && StringUtil.tooLongForShortVarChar(language)) {
                    message = "The language must be 50 characters or less.";
                    request.setAttribute("errorMessage", message);
                    validData = false;
                }

                /* patient reason for inactivity */
                String reason = request.getParameter("reason");
                if ((reason != null) && (reason.trim().length() != 0)
                        && StringUtil.tooLongForShortVarChar(reason)) {
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
                if (StringUtil.dateCheck(startDateString)) {
                    startDate = Date.valueOf(startDateString);
                } else {
                    message = "The start date does not conform to the pattern, "
                            + "YYYY-MM-DD.";
                    request.setAttribute("errorMessage", message);
                    validData = false;
                }

                /* clinic ID */
                if (clinicId == EMPTY_VALUE) {
                    message = "You must select a clinic.";
                    request.setAttribute("errorMessage", message);
                    validData = false;
                }

                if (validData) {
                    boolean successfulUpdate = PatientIO.updatePatient(patientId,
                            firstName, lastName, birthDate, address, contactNumber,
                            gender, race, email, language, reason, startDate,
                            session.getServletContext()
                            .getAttribute("referenceCharacters"));

                    if (successfulUpdate) {
                        message = "Patient was updated successfully!";
                        ArrayList<Patient> patients
                                = PatientIO.getPatients(clinicId,
                                        session.getServletContext()
                                        .getAttribute("referenceCharacters"));
                        session.setAttribute("patients", patients);
                        for (Patient p : patients) {
                            if (p.getPatientId() == patientId) {
                                session.setAttribute("patient", p);
                                SessionObjectUtil.resetPatientObjects(session);
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
                url = "/updatepatient/index.jsp";
                break;
        }
        getServletContext().getRequestDispatcher(url)
                .forward(request, response);
    }
}
