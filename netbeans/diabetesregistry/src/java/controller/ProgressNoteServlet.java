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
import data.ProgressNoteDataAccess;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import registry.DataEntryContainer;
import registry.Patient;
import registry.ProgressNote;
import registry.ReferenceContainer;
import registry.User;
import utility.SessionObjectUtility;
import utility.StringUtility;

/**
 * This HttpServlet class coordinates the functions for the progress note page.
 *
 * @author Bryan Daniel
 * @version 2, March 16, 2017
 */
public class ProgressNoteServlet extends HttpServlet {

    /**
     * Serial version UID
     */
    private static final long serialVersionUID = 4286662503011015701L;

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
     * navigation of the progress note page and processes the requests for
     * creating, retrieving, and updating progress notes documented for each
     * patient visit.
     *
     * For creating a new note or updating an existing one, all form inputs are
     * validated before being passed on to the data-access class.
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
        String url = "/progress/index.jsp";
        int clinicId = ReferenceContainer.CLINIC_ID;
        boolean validData = true;
        User user = (User) session.getAttribute(SessionObjectUtility.USER);
        ArrayList<Patient> patients;
        Patient patient;
        ArrayList<Date> progressDates;
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
                patient = (Patient) session.getAttribute(SessionObjectUtility.PATIENT);
                progressDates
                        = (ArrayList<Date>) session.getAttribute(SessionObjectUtility.PROGRESS_DATES);
                if ((patient != null) && (progressDates == null)) {
                    progressDates = ProgressNoteDataAccess
                            .getProgressDates(patient.getPatientId());
                    session.setAttribute(SessionObjectUtility.PROGRESS_DATES, progressDates);
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
                    progressDates
                            = ProgressNoteDataAccess.getProgressDates(patientId);
                    session.setAttribute(SessionObjectUtility.PROGRESS_DATES, progressDates);
                } catch (NumberFormatException nfe) {
                    message = "patient id invalid";
                    request.setAttribute("errorMessage", message);
                }
                break;
            case "selectPreviousNote": {
                String previousNoteDate
                        = request.getParameter("previousNoteDate");
                Date dateCreated = null;
                if ((previousNoteDate != null) && (previousNoteDate.trim().length() != 0)) {
                    if (StringUtility.dateCheck(previousNoteDate)) {
                        dateCreated = Date.valueOf(previousNoteDate);
                    } else {
                        message = "The date entered does not conform to the pattern, "
                                + "YYYY-MM-DD.";
                        request.setAttribute("errorMessage", message);
                        validData = false;
                    }
                } else {
                    message = "The date of the note is required.";
                    request.setAttribute("errorMessage", message);
                    validData = false;
                }
                if (validData) {
                    patient = (Patient) session.getAttribute(SessionObjectUtility.PATIENT);
                    int patientId = patient.getPatientId();
                    ProgressNote progressNote
                            = ProgressNoteDataAccess.getProgressNote(patientId, dateCreated,
                                    session.getServletContext()
                                    .getAttribute("referenceCharacters"));
                    progressNote.setPatient(patient);
                    progressNote.setDateCreated(dateCreated);
                    session.setAttribute(SessionObjectUtility.PROGRESS_NOTE, progressNote);
                }
                break;
            }
            case "startNewNote": {
                patient = (Patient) session.getAttribute(SessionObjectUtility.PATIENT);
                String dateCreatedString = request.getParameter("noteCreatedDate");
                Date dateCreated = null;
                if ((dateCreatedString != null) && (dateCreatedString.trim().length() != 0)) {
                    if (StringUtility.dateCheck(dateCreatedString)) {
                        dateCreated = Date.valueOf(dateCreatedString);
                    } else {
                        message = "The date entered does not conform to the pattern, "
                                + "YYYY-MM-DD.";
                        request.setAttribute("errorMessage", message);
                        validData = false;
                    }
                } else {
                    message = "The date of the note is required.";
                    request.setAttribute("errorMessage", message);
                    validData = false;
                }
                if (validData) {
                    ProgressNote progressNote = ProgressNoteDataAccess
                            .getProgressNote(patient.getPatientId(), dateCreated,
                                    session.getServletContext()
                                    .getAttribute("referenceCharacters"));
                    progressNote.setPatient(patient);
                    progressNote.setDateCreated(dateCreated);
                    session.setAttribute(SessionObjectUtility.PROGRESS_NOTE, progressNote);
                }
                break;
            }
            case "updateNote": {
                MathContext mc = new MathContext(7);
                final BigDecimal maxDecimal = new BigDecimal("99999.99");
                final int scale = 2;
                DataEntryContainer dec = new DataEntryContainer();
                ProgressNote progressNote
                        = (ProgressNote) session.getAttribute(SessionObjectUtility.PROGRESS_NOTE);
                ProgressNote noteToSave = new ProgressNote();

                /* #1 patient ID */
                noteToSave.setPatient(progressNote.getPatient());

                /* #2 date created */
                noteToSave.setDateCreated(progressNote.getDateCreated());

                /* #3 medical insurance */
                String medicalInsuranceString
                        = request.getParameter("medicalInsurance");
                if (medicalInsuranceString.equals("yes")) {
                    noteToSave.setMedicalInsurance(true);
                } else {
                    noteToSave.setMedicalInsurance(false);
                }

                /* #4 shoe size */
                noteToSave.setShoeSize(request.getParameter("shoeSize"));
                if ((noteToSave.getShoeSize() != null) && (noteToSave.getShoeSize().trim().length() != 0)) {
                    if (StringUtility.tooLongForShortVarChar(noteToSave.getShoeSize())) {
                        message = "The shoe size must be 50 characters or less.";
                        request.setAttribute("errorMessage", message);
                        validData = false;
                    }
                }

                /* #5 allergic to medications */
                String allergicToMedicationsString
                        = request.getParameter("allergicToMedications");
                if (allergicToMedicationsString.equals("yes")) {
                    noteToSave.setAllergicToMedications(true);
                } else {
                    noteToSave.setAllergicToMedications(false);
                }

                /* #6 allergies */
                noteToSave.setAllergies(request.getParameter("allergies"));
                if ((noteToSave.getAllergies() != null) && (noteToSave.getAllergies().trim().length() != 0)) {
                    if (StringUtility.tooLongForShortVarChar(noteToSave.getAllergies())) {
                        message = "The allergies entry must be 50 characters or less.";
                        request.setAttribute("errorMessage", message);
                        validData = false;
                    }
                }

                /* #7 weight */
                String weightString = request.getParameter("weight");
                if ((weightString != null) && (weightString.trim().length() != 0)) {
                    try {
                        BigDecimal weight = new BigDecimal(weightString, mc);
                        weight.setScale(scale, BigDecimal.ROUND_HALF_UP);
                        if (weight.compareTo(maxDecimal) > 0) {
                            message = "Weight value is too large.";
                            request.setAttribute("errorMessage", message);
                            validData = false;
                        } else {
                            noteToSave.setWeight(weight);
                        }
                    } catch (NumberFormatException nfe) {
                        message = "Weight value must be a number.";
                        request.setAttribute("errorMessage", message);
                        validData = false;
                    }
                }

                /* #8 height feet */
                String heightFeetString = request.getParameter("heightFeet");
                if ((heightFeetString != null)
                        && (heightFeetString.trim().length() != 0)) {
                    try {
                        Integer heightFeet = Integer.parseInt(heightFeetString);
                        noteToSave.setHeightFeet(heightFeet);
                    } catch (NumberFormatException nfe) {
                        message = "Height feet value must be a number.";
                        request.setAttribute("errorMessage", message);
                        validData = false;
                    }
                }

                /* #9 height inches */
                String heightInchesString = request.getParameter("heightInches");
                if ((heightInchesString != null)
                        && (heightInchesString.trim().length() != 0)) {
                    try {
                        Integer heightInches = Integer.parseInt(heightInchesString);
                        noteToSave.setHeightInches(heightInches);
                    } catch (NumberFormatException nfe) {
                        message = "Height inches value must be a number.";
                        request.setAttribute("errorMessage", message);
                        validData = false;
                    }
                }

                /* #10 weight reduction goal */
                String weightReductionString
                        = request.getParameter("weightReduction");
                if ((weightReductionString != null)
                        && (weightReductionString.trim().length() != 0)) {
                    try {
                        BigDecimal weightReduction = new BigDecimal(weightReductionString, mc);
                        weightReduction.setScale(scale, BigDecimal.ROUND_HALF_UP);
                        if (weightReduction.compareTo(maxDecimal) > 0) {
                            message = "Weight reduction value is too large.";
                            request.setAttribute("errorMessage", message);
                            validData = false;
                        } else {
                            noteToSave.setWeightReductionGoal(weightReduction);
                        }
                    } catch (NumberFormatException nfe) {
                        message = "Weight reduction value must be a number.";
                        request.setAttribute("errorMessage", message);
                        validData = false;
                    }
                }

                /* #11 pulse */
                String pulseString = request.getParameter("pulse");
                if ((pulseString != null) && (pulseString.trim().length() != 0)) {
                    try {
                        Integer pulse = Integer.parseInt(pulseString);
                        noteToSave.setPulse(pulse);
                    } catch (NumberFormatException nfe) {
                        message = "Pulse value must be a number.";
                        request.setAttribute("errorMessage", message);
                        validData = false;
                    }
                }

                /* 12 respirations */
                String respirationsString = request.getParameter("respirations");
                if ((respirationsString != null)
                        && (respirationsString.trim().length() != 0)) {
                    try {
                        Integer respirations = Integer.parseInt(respirationsString);
                        noteToSave.setRespirations(respirations);
                    } catch (NumberFormatException nfe) {
                        message = "Respirations value must be a number.";
                        request.setAttribute("errorMessage", message);
                        validData = false;
                    }
                }

                /* #13 temperature */
                String temperatureString = request.getParameter("temperature");
                if ((temperatureString != null)
                        && (temperatureString.trim().length() != 0)) {
                    try {
                        BigDecimal temperature = new BigDecimal(temperatureString, mc);
                        temperature.setScale(scale, BigDecimal.ROUND_HALF_UP);
                        if (temperature.compareTo(maxDecimal) > 0) {
                            message = "Temperature value is too large.";
                            request.setAttribute("errorMessage", message);
                            validData = false;
                        } else {
                            noteToSave.setTemperature(temperature);
                        }
                    } catch (NumberFormatException nfe) {
                        message = "Temperature value must be a number.";
                        request.setAttribute("errorMessage", message);
                        validData = false;
                    }
                }

                /* #14 foot screening */
                String footScreeningString
                        = request.getParameter("footScreening");
                if (footScreeningString.equals("yes")) {
                    noteToSave.setFootScreening(true);
                } else {
                    noteToSave.setFootScreening(false);
                }

                /* #15 medications */
                noteToSave.setMedications(request.getParameter("progressMedications"));
                if ((noteToSave.getMedications() != null) && (noteToSave.getMedications().trim().length() != 0)) {
                    if (StringUtility.tooLongForEmailVarChar(noteToSave.getMedications())) {
                        message = "The medications entry must be 255 characters or less.";
                        request.setAttribute("errorMessage", message);
                        validData = false;
                    }
                }

                /* #16 A1C */
                String a1cString = request.getParameter("a1c");
                if ((a1cString != null) && (a1cString.trim().length() != 0)) {
                    try {
                        BigDecimal a1c = new BigDecimal(a1cString, mc);
                        a1c.setScale(scale, BigDecimal.ROUND_HALF_UP);
                        if (a1c.compareTo(maxDecimal) > 0) {
                            message = "A1C value is too large.";
                            request.setAttribute("errorMessage", message);
                            validData = false;
                        } else {
                            noteToSave.setA1c(a1c);
                        }
                    } catch (NumberFormatException nfe) {
                        message = "A1C value must be a number.";
                        request.setAttribute("errorMessage", message);
                        validData = false;
                    }
                }

                /* #17 glucose */
                String glucoseString = request.getParameter("glucose");
                if ((glucoseString != null) && (glucoseString.trim().length() != 0)) {
                    try {
                        BigDecimal glucose = new BigDecimal(glucoseString, mc);
                        glucose.setScale(scale, BigDecimal.ROUND_HALF_UP);
                        if (glucose.compareTo(maxDecimal) > 0) {
                            message = "Glucose value is too large.";
                            request.setAttribute("errorMessage", message);
                            validData = false;
                        } else {
                            noteToSave.setGlucose(glucose);
                        }
                    } catch (NumberFormatException nfe) {
                        message = "Glucose value must be a number.";
                        request.setAttribute("errorMessage", message);
                        validData = false;
                    }
                }

                /* #18 waist */
                String waistString = request.getParameter("waist");
                if ((waistString != null) && (waistString.trim().length() != 0)) {
                    try {
                        BigDecimal waist = new BigDecimal(waistString, mc);
                        waist.setScale(scale, BigDecimal.ROUND_HALF_UP);
                        if (waist.compareTo(maxDecimal) > 0) {
                            message = "Waist value is too large.";
                            request.setAttribute("errorMessage", message);
                            validData = false;
                        } else {
                            noteToSave.setWaist(waist);
                        }
                    } catch (NumberFormatException nfe) {
                        message = "Waist value must be a number.";
                        request.setAttribute("errorMessage", message);
                        validData = false;
                    }
                }

                /* Blood Pressure */
                String bpSystoleString = request.getParameter("bpSystole");
                String bpDiastoleString = request.getParameter("bpDiastole");
                if ((((bpSystoleString != null) && (bpSystoleString.trim().length() > 0))
                        && ((bpDiastoleString == null) || (bpDiastoleString.trim().length() == 0)))
                        || (((bpDiastoleString != null) && (bpDiastoleString.trim().length() > 0))
                        && ((bpSystoleString == null) || (bpSystoleString.trim().length() == 0)))) {
                    message = "Blood pressure must contain both systolic and diastolic values.";
                    request.setAttribute("errorMessage", message);
                    validData = false;
                }

                /* #19 Systolic Blood Pressure */
                if ((bpSystoleString != null) && (bpSystoleString.trim().length() != 0)) {
                    try {
                        Integer bpSystole = Integer.parseInt(bpSystoleString);
                        noteToSave.setBloodPressureSystole(bpSystole);
                    } catch (NumberFormatException nfe) {
                        message = "Blood pressure value must be a number.";
                        request.setAttribute("errorMessage", message);
                        validData = false;
                    }
                }

                /* #20 Diatolic Blood Pressure */
                if ((bpDiastoleString != null) && (bpDiastoleString.trim().length() != 0)) {
                    try {
                        Integer bpDiastole = Integer.parseInt(bpDiastoleString);
                        noteToSave.setBloodPressureDiastole(bpDiastole);
                    } catch (NumberFormatException nfe) {
                        message = "Blood pressure value must be a number.";
                        request.setAttribute("errorMessage", message);
                        validData = false;
                    }
                }

                /* #21 ACE or ARB */
                String aceOrArb = request.getParameter("aceOrArb");
                if ((aceOrArb != null) && (aceOrArb.trim().length() != 0)) {
                    noteToSave.setAceOrArb(true);
                } else {
                    noteToSave.setAceOrArb(false);
                }

                /* #22 BMI */
                String bmiString = request.getParameter("bmi");
                if ((bmiString != null) && (bmiString.trim().length() != 0)) {
                    try {
                        BigDecimal bmi = new BigDecimal(bmiString, mc);
                        bmi.setScale(scale, BigDecimal.ROUND_HALF_UP);
                        if (bmi.compareTo(maxDecimal) > 0) {
                            message = "BMI value is too large.";
                            request.setAttribute("errorMessage", message);
                            validData = false;
                        } else {
                            noteToSave.setBmi(bmi);
                        }
                    } catch (NumberFormatException nfe) {
                        message = "BMI value must be a number.";
                        request.setAttribute("errorMessage", message);
                        validData = false;
                    }
                }

                /* #23 Class date */
                String classDateString = request.getParameter("classDate");
                if ((classDateString != null) && (classDateString.trim().length() != 0)) {
                    if (StringUtility.dateCheck(classDateString)) {
                        Date classDate = Date.valueOf(classDateString);
                        noteToSave.setLastClassDate(classDate);
                    } else {
                        message = "The class date does not conform to the pattern, "
                                + "YYYY-MM-DD.";
                        request.setAttribute("errorMessage", message);
                        validData = false;
                    }
                }

                /* #24 Eye Screening */
                noteToSave.setEyeScreeningCategory(request.getParameter("eyeScreening"));
                if ((noteToSave.getEyeScreeningCategory() != null)
                        && (noteToSave.getEyeScreeningCategory().trim().length() != 0)) {
                    if (StringUtility.tooLongForShortVarChar(noteToSave.getEyeScreeningCategory())) {
                        message = "The eye screening value must be 50 characters or less.";
                        request.setAttribute("errorMessage", message);
                        validData = false;
                    }
                }

                /* #25 Foot Screening */
                noteToSave.setFootScreeningCategory(request.getParameter("footScreeningResult"));
                if ((noteToSave.getFootScreeningCategory() != null)
                        && (noteToSave.getFootScreeningCategory().trim().length() != 0)) {
                    if (StringUtility.tooLongForShortVarChar(noteToSave.getFootScreeningCategory())) {
                        message = "The foot screening value must be 50 characters or less.";
                        request.setAttribute("errorMessage", message);
                        validData = false;
                    }
                }

                /* #26 Psychological Screening */
                String psychologicalScreening = request.getParameter("psychologicalScreening");
                if ((psychologicalScreening != null) && (psychologicalScreening.trim().length() != 0)) {
                    try {
                        Integer psychologicalScreeningInteger = Integer.parseInt(psychologicalScreening);
                        noteToSave.setPsychologicalScreening(psychologicalScreeningInteger);
                    } catch (NumberFormatException nfe) {
                        message = "Psychological screening value must be a number.";
                        request.setAttribute("errorMessage", message);
                        validData = false;
                    }
                }

                /* #27 Physical Activity */
                String physicalActivity = request.getParameter("physicalActivity");
                if ((physicalActivity != null) && (physicalActivity.trim().length() != 0)) {
                    try {
                        Integer physicalActivityInteger = Integer.parseInt(physicalActivity);
                        noteToSave.setPhysicalActivity(physicalActivityInteger);
                    } catch (NumberFormatException nfe) {
                        message = "Physical activity value must be a number.";
                        request.setAttribute("errorMessage", message);
                        validData = false;
                    }
                }

                /* #28 Smoking status */
                String smoking = request.getParameter("smoking");
                if ((smoking != null) && (smoking.trim().length() != 0)) {
                    if (smoking.equals("yes")) {
                        noteToSave.setSmoking(true);
                    } else {
                        noteToSave.setSmoking(false);
                    }
                }

                /* #29 Compliance */
                String compliance = request.getParameter("compliance");
                if ((compliance != null) && (compliance.trim().length() != 0)) {
                    final BigDecimal lowEnd = new BigDecimal("0.00");
                    final BigDecimal highEnd = new BigDecimal("10.00");
                    try {
                        BigDecimal complianceValue = new BigDecimal(compliance);
                        complianceValue.setScale(scale, BigDecimal.ROUND_HALF_UP);
                        if ((complianceValue.compareTo(highEnd) > 0)
                                || (complianceValue.compareTo(lowEnd) < 0)) {
                            message = "The compliance value is out of range.";
                            request.setAttribute("errorMessage", message);
                            validData = false;
                        } else {
                            noteToSave.setCompliance(complianceValue);
                        }
                    } catch (NumberFormatException nfe) {
                        message = "Compliance value must be a number.";
                        request.setAttribute("errorMessage", message);
                        validData = false;
                    }
                }

                /* #30 Hospitalization date */
                String erDateString = request.getParameter("erDate");
                if ((erDateString != null) && (erDateString.trim().length() != 0)) {
                    if (StringUtility.dateCheck(erDateString)) {
                        Date erDate = Date.valueOf(erDateString);
                        noteToSave.setHospitalizationDate(erDate);
                    } else {
                        message = "The hospitalization date does not conform to the pattern, "
                                + "YYYY-MM-DD.";
                        request.setAttribute("errorMessage", message);
                        validData = false;
                    }
                }

                /* #31 nurse or dietitian note */
                noteToSave.setNurseOrDietitianNote(request.getParameter("nurseOrDietitianNote"));
                if ((noteToSave.getNurseOrDietitianNote() != null)
                        && (noteToSave.getNurseOrDietitianNote().trim().length() != 0)) {
                    if (StringUtility.tooLongForLongVarChar(noteToSave.getNurseOrDietitianNote())) {
                        message = "The nurse/dietitian note must be 1000 "
                                + "characters or less.";
                        request.setAttribute("errorMessage", message);
                        validData = false;
                    }
                }

                /* #32 subjective section */
                noteToSave.setSubjective(request.getParameter("subjective"));
                if ((noteToSave.getSubjective() != null)
                        && (noteToSave.getSubjective().trim().length() != 0)) {
                    if (StringUtility.tooLongForLongVarChar(noteToSave.getSubjective())) {
                        message = "The subjective section must be 1000 "
                                + "characters or less.";
                        request.setAttribute("errorMessage", message);
                        validData = false;
                    }
                }

                /* #33 objective */
                noteToSave.setObjective(request.getParameter("objective"));
                if ((noteToSave.getObjective() != null)
                        && (noteToSave.getObjective().trim().length() != 0)) {
                    if (StringUtility.tooLongForLongVarChar(noteToSave.getObjective())) {
                        message = "The objective section must be 1000 "
                                + "characters or less.";
                        request.setAttribute("errorMessage", message);
                        validData = false;
                    }
                }

                /* #34 assessment */
                noteToSave.setAssessment(request.getParameter("assessment"));
                if ((noteToSave.getAssessment() != null)
                        && (noteToSave.getAssessment().trim().length() != 0)) {
                    if (StringUtility.tooLongForLongVarChar(noteToSave.getAssessment())) {
                        message = "The assessment section must be 1000 "
                                + "characters or less.";
                        request.setAttribute("errorMessage", message);
                        validData = false;
                    }
                }

                /* #35 plan */
                noteToSave.setPlan(request.getParameter("plan"));
                if ((noteToSave.getPlan() != null)
                        && (noteToSave.getPlan().trim().length() != 0)) {
                    if (StringUtility.tooLongForLongVarChar(noteToSave.getPlan())) {
                        message = "The plan section must be 1000 "
                                + "characters or less.";
                        request.setAttribute("errorMessage", message);
                        validData = false;
                    }
                }

                /* #36 updated by */
                String userName = user.getUserName();

                /* #37 timestamp */
                Timestamp timestamp
                        = new Timestamp(System.currentTimeMillis());

                /* #39 clinic ID */
                dec.setClinicId(clinicId);

                if (validData) {
                    boolean successfulUpdate
                            = ProgressNoteDataAccess.saveProgressNote(noteToSave, userName,
                                    timestamp, clinicId, session.getServletContext()
                                    .getAttribute("referenceCharacters"));
                    if (successfulUpdate) {

                        /*
                         * pulling patient data from the database as patient status 
                         * may have changed
                         */
                        patients = PatientDataAccess.getPatients(clinicId,
                                session.getServletContext()
                                .getAttribute("referenceCharacters"));
                        session.setAttribute(SessionObjectUtility.PATIENTS, patients);
                        for (Patient p : patients) {
                            if (p.getPatientId() == noteToSave.getPatient().getPatientId()) {
                                session.setAttribute(SessionObjectUtility.PATIENT, p);
                                SessionObjectUtility.resetPatientObjects(session);
                                break;
                            }
                        }

                        /* keeping the latest progress note data on the page */
                        progressNote = ProgressNoteDataAccess
                                .getProgressNote(noteToSave.getPatient().getPatientId(),
                                        noteToSave.getDateCreated(), session.getServletContext()
                                        .getAttribute("referenceCharacters"));
                        progressNote.setPatient(noteToSave.getPatient());
                        progressNote.setDateCreated(noteToSave.getDateCreated());
                        session.setAttribute(SessionObjectUtility.PROGRESS_NOTE, progressNote);
                        progressDates
                                = ProgressNoteDataAccess.getProgressDates(noteToSave.getPatient().getPatientId());
                        session.setAttribute(SessionObjectUtility.PROGRESS_DATES, progressDates);
                        message = "Progress note successfully updated!";
                        request.setAttribute("message", message);
                    } else {
                        message = "The progress note could not be updated.";
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
