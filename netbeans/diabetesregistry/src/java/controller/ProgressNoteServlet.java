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

import clinic.DataEntryContainer;
import clinic.Patient;
import clinic.ProgressNote;
import clinic.User;
import data.PatientIO;
import data.ProgressNoteIO;
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
import util.SessionObjectUtil;
import util.StringUtil;

/**
 * Coordinates the progress note functions
 *
 * @author Bryan Daniel
 * @version 1, April 8, 2016
 */
public class ProgressNoteServlet extends HttpServlet {

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
        String url = "/progress/index.jsp";
        ArrayList<Patient> patients;
        Patient patient;
        ArrayList<Date> progressDates;
        String message;
        int clinicId;
        boolean validData = true;
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
                patient = (Patient) session.getAttribute("patient");
                progressDates
                        = (ArrayList<Date>) session.getAttribute("progressDates");
                if ((patient != null) && (progressDates == null)) {
                    progressDates = ProgressNoteIO
                            .getProgressDates(patient.getPatientId());
                    session.setAttribute("progressDates", progressDates);
                }
                url = "/progress/index.jsp";
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
                    url = "/progress/index.jsp";
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
                    progressDates
                            = ProgressNoteIO.getProgressDates(patientId);
                    session.setAttribute("progressDates", progressDates);
                    url = "/progress/index.jsp";
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
                    if (StringUtil.dateCheck(previousNoteDate)) {
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
                    patient = (Patient) session.getAttribute("patient");
                    int patientId = patient.getPatientId();
                    ProgressNote progressNote
                            = ProgressNoteIO.getProgressNote(patientId, dateCreated,
                                    session.getServletContext()
                                    .getAttribute("referenceCharacters"));
                    progressNote.setPatient(patient);
                    progressNote.setDateCreated(dateCreated);
                    session.setAttribute("progressNote", progressNote);
                }
            }
            break;
            case "startNewNote": {
                patient = (Patient) session.getAttribute("patient");
                String dateCreatedString = request.getParameter("noteCreatedDate");
                Date dateCreated = null;
                if ((dateCreatedString != null) && (dateCreatedString.trim().length() != 0)) {
                    if (StringUtil.dateCheck(dateCreatedString)) {
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
                    ProgressNote progressNote = ProgressNoteIO
                            .getProgressNote(patient.getPatientId(), dateCreated,
                                    session.getServletContext()
                                    .getAttribute("referenceCharacters"));
                    progressNote.setPatient(patient);
                    progressNote.setDateCreated(dateCreated);
                    session.setAttribute("progressNote", progressNote);
                }
            }
            break;
            case "updateNote": {
                MathContext mc = new MathContext(7);
                final BigDecimal maxDecimal = new BigDecimal("99999.99");
                final int scale = 2;
                DataEntryContainer dec = new DataEntryContainer();

                /* #1 patient ID */
                patient = (Patient) session.getAttribute("patient");
                int patientId = patient.getPatientId();

                /* #2 date created */
                ProgressNote progressNote
                        = (ProgressNote) session.getAttribute("progressNote");
                Date dateCreated = progressNote.getDateCreated();

                /* #3 medical insurance */
                String medicalInsuranceString
                        = request.getParameter("medicalInsurance");
                boolean medicalInsurance = false;
                if (medicalInsuranceString.equals("yes")) {
                    medicalInsurance = true;
                }

                /* #4 shoe size */
                String shoeSize = request.getParameter("shoeSize");
                if ((shoeSize != null) && (shoeSize.trim().length() != 0)) {
                    if (StringUtil.tooLongForShortVarChar(shoeSize)) {
                        message = "The shoe size must be 50 characters or less.";
                        request.setAttribute("errorMessage", message);
                        validData = false;
                    }
                }

                /* #5 allergic to medications */
                String allergicToMedicationsString
                        = request.getParameter("allergicToMedications");
                boolean allergicToMedications = false;
                if (allergicToMedicationsString.equals("yes")) {
                    allergicToMedications = true;
                }

                /* #6 allergies */
                String allergies = request.getParameter("allergies");
                if ((allergies != null) && (allergies.trim().length() != 0)) {
                    if (StringUtil.tooLongForShortVarChar(allergies)) {
                        message = "The allergies entry must be 50 characters or less.";
                        request.setAttribute("errorMessage", message);
                        validData = false;
                    }
                }

                /* #7 weight */
                String weightString = request.getParameter("weight");
                BigDecimal weight = null;
                if ((weightString != null) && (weightString.trim().length() != 0)) {
                    try {
                        weight = new BigDecimal(weightString, mc);
                        weight.setScale(scale, BigDecimal.ROUND_HALF_UP);
                        if (weight.compareTo(maxDecimal) > 0) {
                            message = "Weight value is too large.";
                            request.setAttribute("errorMessage", message);
                            validData = false;
                        }
                    } catch (NumberFormatException nfe) {
                        message = "Weight value must be a number.";
                        request.setAttribute("errorMessage", message);
                        validData = false;
                    }
                }

                /* #8 height feet */
                String heightFeetString = request.getParameter("heightFeet");
                Integer heightFeet = null;
                if ((heightFeetString != null)
                        && (heightFeetString.trim().length() != 0)) {
                    try {
                        heightFeet = Integer.parseInt(heightFeetString);
                    } catch (NumberFormatException nfe) {
                        message = "Height feet value must be a number.";
                        request.setAttribute("errorMessage", message);
                        validData = false;
                    }
                }

                /* #9 height inches */
                String heightInchesString = request.getParameter("heightInches");
                Integer heightInches = null;
                if ((heightInchesString != null)
                        && (heightInchesString.trim().length() != 0)) {
                    try {
                        heightInches = Integer.parseInt(heightInchesString);
                    } catch (NumberFormatException nfe) {
                        message = "Height inches value must be a number.";
                        request.setAttribute("errorMessage", message);
                        validData = false;
                    }
                }

                /* #10 weight reduction goal */
                String weightReductionString
                        = request.getParameter("weightReduction");
                BigDecimal weightReduction = null;
                if ((weightReductionString != null)
                        && (weightReductionString.trim().length() != 0)) {
                    try {
                        weightReduction = new BigDecimal(weightReductionString, mc);
                        weightReduction.setScale(scale, BigDecimal.ROUND_HALF_UP);
                        if (weightReduction.compareTo(maxDecimal) > 0) {
                            message = "Weight reduction value is too large.";
                            request.setAttribute("errorMessage", message);
                            validData = false;
                        }
                    } catch (NumberFormatException nfe) {
                        message = "Weight reduction value must be a number.";
                        request.setAttribute("errorMessage", message);
                        validData = false;
                    }
                }

                /* #11 pulse */
                String pulseString = request.getParameter("pulse");
                Integer pulse = null;
                if ((pulseString != null) && (pulseString.trim().length() != 0)) {
                    try {
                        pulse = Integer.parseInt(pulseString);
                    } catch (NumberFormatException nfe) {
                        message = "Pulse value must be a number.";
                        request.setAttribute("errorMessage", message);
                        validData = false;
                    }
                }

                /* 12 respirations */
                String respirationsString = request.getParameter("respirations");
                Integer respirations = null;
                if ((respirationsString != null)
                        && (respirationsString.trim().length() != 0)) {
                    try {
                        respirations = Integer.parseInt(respirationsString);
                    } catch (NumberFormatException nfe) {
                        message = "Respirations value must be a number.";
                        request.setAttribute("errorMessage", message);
                        validData = false;
                    }
                }

                /* #13 temperature */
                String temperatureString = request.getParameter("temperature");
                BigDecimal temperature = null;
                if ((temperatureString != null)
                        && (temperatureString.trim().length() != 0)) {
                    try {
                        temperature = new BigDecimal(temperatureString, mc);
                        temperature.setScale(scale, BigDecimal.ROUND_HALF_UP);
                        if (temperature.compareTo(maxDecimal) > 0) {
                            message = "Temperature value is too large.";
                            request.setAttribute("errorMessage", message);
                            validData = false;
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
                boolean footScreening = false;
                if (footScreeningString.equals("yes")) {
                    footScreening = true;
                }

                /* #15 medications */
                String medications = request.getParameter("progressMedications");
                if ((medications != null) && (medications.trim().length() != 0)) {
                    if (StringUtil.tooLongForShortVarChar(medications)) {
                        message = "The medications entry must be 50 characters or less.";
                        request.setAttribute("errorMessage", message);
                        validData = false;
                    }
                }

                /* #16 A1C */
                String a1cString = request.getParameter("a1c");
                BigDecimal a1c = null;
                if ((a1cString != null) && (a1cString.trim().length() != 0)) {
                    try {
                        a1c = new BigDecimal(a1cString, mc);
                        a1c.setScale(scale, BigDecimal.ROUND_HALF_UP);
                        if (a1c.compareTo(maxDecimal) > 0) {
                            message = "A1C value is too large.";
                            request.setAttribute("errorMessage", message);
                            validData = false;
                        }
                    } catch (NumberFormatException nfe) {
                        message = "A1C value must be a number.";
                        request.setAttribute("errorMessage", message);
                        validData = false;
                    }
                }

                /* #17 glucose */
                String glucoseString = request.getParameter("glucose");
                BigDecimal glucose = null;
                if ((glucoseString != null) && (glucoseString.trim().length() != 0)) {
                    try {
                        glucose = new BigDecimal(glucoseString, mc);
                        glucose.setScale(scale, BigDecimal.ROUND_HALF_UP);
                        if (glucose.compareTo(maxDecimal) > 0) {
                            message = "Glucose value is too large.";
                            request.setAttribute("errorMessage", message);
                            validData = false;
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
                            dec.setWaist(waist);
                        }
                    } catch (NumberFormatException nfe) {
                        message = "Waist value must be a number.";
                        request.setAttribute("errorMessage", message);
                        validData = false;
                    }
                } else {
                    dec.setWaist(null);
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
                        dec.setBloodPressureSystole(bpSystole);
                    } catch (NumberFormatException nfe) {
                        message = "Blood pressure value must be a number.";
                        request.setAttribute("errorMessage", message);
                        validData = false;
                    }
                } else {
                    dec.setBloodPressureSystole(null);
                }

                /* #20 Diatolic Blood Pressure */
                if ((bpDiastoleString != null) && (bpDiastoleString.trim().length() != 0)) {
                    try {
                        Integer bpDiastole = Integer.parseInt(bpDiastoleString);
                        dec.setBloodPressureDiastole(bpDiastole);
                    } catch (NumberFormatException nfe) {
                        message = "Blood pressure value must be a number.";
                        request.setAttribute("errorMessage", message);
                        validData = false;
                    }
                } else {
                    dec.setBloodPressureDiastole(null);
                }

                /* #21 ACE or ARB */
                String aceOrArb = request.getParameter("aceOrArb");
                if ((aceOrArb != null) && (aceOrArb.trim().length() != 0)) {
                    dec.setAceOrArb(true);
                } else {
                    dec.setAceOrArb(false);
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
                            dec.setBmi(bmi);
                        }
                    } catch (NumberFormatException nfe) {
                        message = "BMI value must be a number.";
                        request.setAttribute("errorMessage", message);
                        validData = false;
                    }
                } else {
                    dec.setBmi(null);
                }

                /* #23 Class date */
                String classDateString = request.getParameter("classDate");
                Date classDate;
                if ((classDateString != null) && (classDateString.trim().length() != 0)) {
                    if (StringUtil.dateCheck(classDateString)) {
                        classDate = Date.valueOf(classDateString);
                        dec.setClassDate(classDate);
                    } else {
                        message = "The class date does not conform to the pattern, "
                                + "YYYY-MM-DD.";
                        request.setAttribute("errorMessage", message);
                        validData = false;
                    }
                } else {
                    dec.setClassDate(null);
                }

                /* #24 Eye Screening */
                String eyeScreening = request.getParameter("eyeScreening");
                if ((eyeScreening != null) && (eyeScreening.trim().length() != 0)) {
                    if (StringUtil.tooLongForShortVarChar(eyeScreening)) {
                        message = "The eye screening value must be 50 characters or less.";
                        request.setAttribute("errorMessage", message);
                        validData = false;
                    } else {
                        dec.setEye(eyeScreening);
                    }
                } else {
                    dec.setEye(null);
                }

                /* #25 Foot Screening */
                String footScreeningCategory = request.getParameter("footScreeningResult");
                if ((footScreeningCategory != null) && (footScreeningCategory.trim().length() != 0)) {
                    if (StringUtil.tooLongForShortVarChar(footScreeningCategory)) {
                        message = "The foot screening value must be 50 characters or less.";
                        request.setAttribute("errorMessage", message);
                        validData = false;
                    } else {
                        dec.setFoot(footScreeningCategory);
                    }
                } else {
                    dec.setFoot(null);
                }

                /* #26 Psychological Screening */
                String psychologicalScreening = request.getParameter("psychologicalScreening");
                if ((psychologicalScreening != null) && (psychologicalScreening.trim().length() != 0)) {
                    try {
                        Integer psychologicalScreeningInteger = new Integer(psychologicalScreening);
                        dec.setPsychologicalScreening(psychologicalScreeningInteger);
                    } catch (NumberFormatException nfe) {
                        message = "Psychological screening value must be a number.";
                        request.setAttribute("errorMessage", message);
                        validData = false;
                    }
                } else {
                    dec.setPsychologicalScreening(null);
                }

                /* #27 Physical Activity */
                String physicalActivity = request.getParameter("physicalActivity");
                if ((physicalActivity != null) && (physicalActivity.trim().length() != 0)) {
                    try {
                        int physicalActivityInteger = Integer.parseInt(physicalActivity);
                        dec.setPhysicalActivity(physicalActivityInteger);
                    } catch (NumberFormatException nfe) {
                        message = "Physical activity value must be a number.";
                        request.setAttribute("errorMessage", message);
                        validData = false;
                    }
                } else {
                    dec.setPhysicalActivity(null);
                }

                /* #28 Smoking status */
                String smoking = request.getParameter("smoking");
                if ((smoking != null) && (smoking.trim().length() != 0)) {
                    Boolean smokingStatus;
                    if (smoking.equals("yes")) {
                        smokingStatus = true;
                    } else {
                        smokingStatus = false;
                    }
                    dec.setSmoking(smokingStatus);
                } else {
                    dec.setSmoking(null);
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
                            dec.setCompliance(complianceValue);
                        }
                    } catch (NumberFormatException nfe) {
                        message = "Compliance value must be a number.";
                        request.setAttribute("errorMessage", message);
                        validData = false;
                    }
                } else {
                    dec.setCompliance(null);
                }

                /* #30 Hospitalization date */
                String erDateString = request.getParameter("erDate");
                Date erDate;
                if ((erDateString != null) && (erDateString.trim().length() != 0)) {
                    if (StringUtil.dateCheck(erDateString)) {
                        erDate = Date.valueOf(erDateString);
                        dec.setHospitalizationDate(erDate);
                    } else {
                        message = "The hospitalization date does not conform to the pattern, "
                                + "YYYY-MM-DD.";
                        request.setAttribute("errorMessage", message);
                        validData = false;
                    }
                } else {
                    dec.setHospitalizationDate(null);
                }

                /* #31 nurse or dietitian note */
                String nurseOrDietitianNote
                        = request.getParameter("nurseOrDietitianNote");
                if ((nurseOrDietitianNote != null)
                        && (nurseOrDietitianNote.trim().length() != 0)) {
                    if (StringUtil.tooLongForLongVarChar(nurseOrDietitianNote)) {
                        message = "The nurse/dietitian note must be 1000 "
                                + "characters or less.";
                        request.setAttribute("errorMessage", message);
                        validData = false;
                    }
                }

                /* #32 subjective section */
                String subjective = request.getParameter("subjective");
                if ((subjective != null) && (subjective.trim().length() != 0)) {
                    if (StringUtil.tooLongForLongVarChar(subjective)) {
                        message = "The subjective section must be 1000 "
                                + "characters or less.";
                        request.setAttribute("errorMessage", message);
                        validData = false;
                    }
                }

                /* #33 objective */
                String objective = request.getParameter("objective");
                if ((objective != null) && (objective.trim().length() != 0)) {
                    if (StringUtil.tooLongForLongVarChar(objective)) {
                        message = "The objective section must be 1000 "
                                + "characters or less.";
                        request.setAttribute("errorMessage", message);
                        validData = false;
                    }
                }

                /* #34 assessment */
                String assessment = request.getParameter("assessment");
                if ((assessment != null) && (assessment.trim().length() != 0)) {
                    if (StringUtil.tooLongForLongVarChar(assessment)) {
                        message = "The assessment section must be 1000 "
                                + "characters or less.";
                        request.setAttribute("errorMessage", message);
                        validData = false;
                    }
                }

                /* #35 plan */
                String plan = request.getParameter("plan");
                if ((plan != null) && (plan.trim().length() != 0)) {
                    if (StringUtil.tooLongForLongVarChar(plan)) {
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
                if (clinicId != EMPTY_VALUE) {
                    dec.setClinicId(clinicId);
                } else {
                    message = "You must select a clinic.";
                    request.setAttribute("errorMessage", message);
                    validData = false;
                }

                if (validData) {
                    boolean successfulUpdate
                            = ProgressNoteIO.saveProgressNote(patientId,
                                    dateCreated, medicalInsurance, shoeSize,
                                    allergicToMedications, allergies, weight,
                                    heightFeet, heightInches, weightReduction,
                                    pulse, respirations, temperature,
                                    footScreening, medications, a1c, glucose,
                                    dec, nurseOrDietitianNote, subjective,
                                    objective, assessment, plan,
                                    userName, timestamp,
                                    session.getServletContext()
                                    .getAttribute("referenceCharacters"));
                    if (successfulUpdate) {
                        progressNote = ProgressNoteIO
                                .getProgressNote(patientId, dateCreated,
                                        session.getServletContext()
                                        .getAttribute("referenceCharacters"));
                        progressNote.setPatient(patient);
                        progressNote.setDateCreated(dateCreated);
                        session.setAttribute("progressNote", progressNote);
                        progressDates
                                = ProgressNoteIO.getProgressDates(patientId);
                        session.setAttribute("progressDates", progressDates);
                        message = "Progress note successfully updated!";
                        request.setAttribute("message", message);
                    } else {
                        message = "The progress note could not be updated.";
                        request.setAttribute("errorMessage", message);
                    }
                }
            }
            break;
            default:
                url = "/progress/index.jsp";
                break;
        }
        getServletContext().getRequestDispatcher(url)
                .forward(request, response);

    }
}
