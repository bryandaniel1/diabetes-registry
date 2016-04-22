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
import clinic.User;
import data.DataEntryIO;
import data.PatientIO;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
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
 * Coordinates the data entry activity
 *
 * @author Bryan Daniel
 * @version 1, April 8, 2016
 */
public class DataEntryServlet extends HttpServlet {

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
        String url = "/dataentry/index.jsp";
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
                url = "/dataentry/index.jsp";
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
                    url = "/dataentry/index.jsp";
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
                    url = "/dataentry/index.jsp";
                } catch (NumberFormatException nfe) {
                    message = "patient id invalid";
                    request.setAttribute("errorMessage", message);
                }
                break;
            case "addResults":
                boolean validData = true;
                MathContext mc = new MathContext(7);
                final BigDecimal maxDecimal = new BigDecimal("99999.99");
                final int scale = 2;
                DataEntryContainer dec = new DataEntryContainer();

                /* #1 patient ID */
                Patient patient = (Patient) session.getAttribute("patient");
                int patientId = patient.getPatientId();
                dec.setPatientId(patientId);

                /* #2 A1C */
                String a1c = request.getParameter("a1c");
                if ((a1c != null) && (a1c.trim().length() != 0)) {
                    try {
                        BigDecimal a1cDecimal = new BigDecimal(a1c, mc);
                        a1cDecimal.setScale(scale, BigDecimal.ROUND_HALF_UP);
                        if (a1cDecimal.compareTo(maxDecimal) > 0) {
                            message = "A1C value is too large.";
                            request.setAttribute("errorMessage", message);
                            validData = false;
                        } else {
                            dec.setA1c(a1cDecimal);
                        }
                    } catch (NumberFormatException nfe) {
                        message = "A1C value must be a number.";
                        request.setAttribute("errorMessage", message);
                        validData = false;
                    }
                } else {
                    dec.setA1c(null);
                }

                /* #3 glucose AC & #4 glucose PC */
                String glucose = request.getParameter("glucose");
                String acOrPc = request.getParameter("acorpc");
                if ((glucose != null) && (glucose.trim().length() != 0)) {
                    try {
                        BigDecimal glucoseDecimal = new BigDecimal(glucose, mc);
                        glucoseDecimal.setScale(scale, BigDecimal.ROUND_HALF_UP);
                        if (glucoseDecimal.compareTo(maxDecimal) > 0) {
                            message = "Glucose value is too large.";
                            request.setAttribute("errorMessage", message);
                            validData = false;
                        } else {
                            if (acOrPc.equals("ac")) {
                                dec.setGlucoseAc(glucoseDecimal);
                                dec.setGlucosePc(null);
                            } else {
                                dec.setGlucosePc(glucoseDecimal);
                                dec.setGlucoseAc(null);
                            }
                        }

                    } catch (NumberFormatException nfe) {
                        message = "Glucose value must be a number.";
                        request.setAttribute("errorMessage", message);
                        validData = false;
                    }
                } else {
                    dec.setGlucoseAc(null);
                    dec.setGlucosePc(null);
                }

                /* #5 LDL */
                String ldl = request.getParameter("ldl");
                if ((ldl != null) && (ldl.trim().length() != 0)) {
                    try {
                        BigDecimal ldlDecimal = new BigDecimal(ldl, mc);
                        ldlDecimal.setScale(scale, BigDecimal.ROUND_HALF_UP);
                        if (ldlDecimal.compareTo(maxDecimal) > 0) {
                            message = "LDL value is too large.";
                            request.setAttribute("errorMessage", message);
                            validData = false;
                        } else {
                            dec.setLdl(ldlDecimal);
                        }
                    } catch (NumberFormatException nfe) {
                        message = "LDL value must be a number.";
                        request.setAttribute("errorMessage", message);
                        validData = false;
                    }
                } else {
                    dec.setLdl(null);
                }

                /* #6 LDL Post MI */
                String ldlPostMi = request.getParameter("ldlPostMi");
                if ((ldlPostMi != null) && (ldlPostMi.trim().length() != 0)) {
                    try {
                        BigDecimal ldlPostMiDecimal = new BigDecimal(ldlPostMi, mc);
                        ldlPostMiDecimal.setScale(scale, BigDecimal.ROUND_HALF_UP);
                        if (ldlPostMiDecimal.compareTo(maxDecimal) > 0) {
                            message = "LDL value is too large.";
                            request.setAttribute("errorMessage", message);
                            validData = false;
                        } else {
                            dec.setLdlPostMi(ldlPostMiDecimal);
                        }
                    } catch (NumberFormatException nfe) {
                        message = "LDL PostMi value must be a number.";
                        request.setAttribute("errorMessage", message);
                        validData = false;
                    }
                } else {
                    dec.setLdlPostMi(null);
                }

                /* #7 LDL on statin */
                String onStatin = request.getParameter("onstatin");
                if ((onStatin != null) && (onStatin.trim().length() != 0)) {
                    dec.setOnStatin(true);
                } else {
                    dec.setOnStatin(false);
                }

                /* #8 HDL */
                String hdl = request.getParameter("hdl");
                if ((hdl != null) && (hdl.trim().length() != 0)) {
                    try {
                        BigDecimal hdlDecimal = new BigDecimal(hdl, mc);
                        hdlDecimal.setScale(scale, BigDecimal.ROUND_HALF_UP);
                        if (hdlDecimal.compareTo(maxDecimal) > 0) {
                            message = "HDL value is too large.";
                            request.setAttribute("errorMessage", message);
                            validData = false;
                        } else {
                            dec.setHdl(hdlDecimal);
                        }
                    } catch (NumberFormatException nfe) {
                        message = "HDL value must be a number.";
                        request.setAttribute("errorMessage", message);
                        validData = false;
                    }
                } else {
                    dec.setHdl(null);
                }

                /* #9 Triglycerides */
                String triglycerides = request.getParameter("triglycerides");
                if ((triglycerides != null) && (triglycerides.trim().length() != 0)) {
                    try {
                        BigDecimal triglyceridesDecimal = new BigDecimal(triglycerides, mc);
                        triglyceridesDecimal.setScale(scale, BigDecimal.ROUND_HALF_UP);
                        if (triglyceridesDecimal.compareTo(maxDecimal) > 0) {
                            message = "Triglycerides value is too large.";
                            request.setAttribute("errorMessage", message);
                            validData = false;
                        } else {
                            dec.setTriglycerides(triglyceridesDecimal);
                        }
                    } catch (NumberFormatException nfe) {
                        message = "Triglycerides value must be a number.";
                        request.setAttribute("errorMessage", message);
                        validData = false;
                    }
                } else {
                    dec.setTriglycerides(null);
                }

                /* #10 TSH */
                String tsh = request.getParameter("tsh");
                if ((tsh != null) && (tsh.trim().length() != 0)) {
                    try {
                        BigDecimal tshDecimal = new BigDecimal(tsh, mc);
                        tshDecimal.setScale(scale, BigDecimal.ROUND_HALF_UP);
                        if (tshDecimal.compareTo(maxDecimal) > 0) {
                            message = "TSH value is too large.";
                            request.setAttribute("errorMessage", message);
                            validData = false;
                        } else {
                            dec.setTsh(tshDecimal);
                        }
                    } catch (NumberFormatException nfe) {
                        message = "TSH value must be a number.";
                        request.setAttribute("errorMessage", message);
                        validData = false;
                    }
                } else {
                    dec.setTsh(null);
                }

                /* #11 TSH on thyroid treatment */
                String onThyroidTreatment = request.getParameter("onthyroidtreatment");
                if ((onThyroidTreatment != null)
                        && (onThyroidTreatment.trim().length() != 0)) {
                    dec.setOnThyroidTreatment(true);
                } else {
                    dec.setOnThyroidTreatment(false);
                }

                /* #12 T4 */
                String t4 = request.getParameter("t4");
                if ((t4 != null) && (t4.trim().length() != 0)) {
                    try {
                        BigDecimal t4Decimal = new BigDecimal(t4, mc);
                        t4Decimal.setScale(scale, BigDecimal.ROUND_HALF_UP);
                        if (t4Decimal.compareTo(maxDecimal) > 0) {
                            message = "T4 value is too large.";
                            request.setAttribute("errorMessage", message);
                            validData = false;
                        } else {
                            dec.setT4(t4Decimal);
                        }
                    } catch (NumberFormatException nfe) {
                        message = "T4 value must be a number.";
                        request.setAttribute("errorMessage", message);
                        validData = false;
                    }
                } else {
                    dec.setT4(null);
                }

                /* #13 UACR */
                String uacr = request.getParameter("uacr");
                if ((uacr != null) && (uacr.trim().length() != 0)) {
                    try {
                        BigDecimal uacrDecimal = new BigDecimal(uacr, mc);
                        uacrDecimal.setScale(scale, BigDecimal.ROUND_HALF_UP);
                        if (uacrDecimal.compareTo(maxDecimal) > 0) {
                            message = "UACR value is too large.";
                            request.setAttribute("errorMessage", message);
                            validData = false;
                        } else {
                            dec.setUacr(uacrDecimal);
                        }
                    } catch (NumberFormatException nfe) {
                        message = "UACR value must be a number.";
                        request.setAttribute("errorMessage", message);
                        validData = false;
                    }
                } else {
                    dec.setUacr(null);
                }

                /* #14 eGFR */
                String egfr = request.getParameter("egfr");
                if ((egfr != null) && (egfr.trim().length() != 0)) {
                    try {
                        BigDecimal egfrDecimal = new BigDecimal(egfr, mc);
                        egfrDecimal.setScale(scale, BigDecimal.ROUND_HALF_UP);
                        if (egfrDecimal.compareTo(maxDecimal) > 0) {
                            message = "eGFR value is too large.";
                            request.setAttribute("errorMessage", message);
                            validData = false;
                        } else {
                            dec.setEgfr(egfrDecimal);
                        }
                    } catch (NumberFormatException nfe) {
                        message = "eGFR value must be a number.";
                        request.setAttribute("errorMessage", message);
                        validData = false;
                    }
                } else {
                    dec.setEgfr(null);
                }

                /* #15 Creatinine */
                String creatinine = request.getParameter("creatinine");
                if ((creatinine != null) && (creatinine.trim().length() != 0)) {
                    try {
                        BigDecimal creatinineDecimal = new BigDecimal(creatinine, mc);
                        creatinineDecimal.setScale(scale, BigDecimal.ROUND_HALF_UP);
                        if (creatinineDecimal.compareTo(maxDecimal) > 0) {
                            message = "Creatinine value is too large.";
                            request.setAttribute("errorMessage", message);
                            validData = false;
                        } else {
                            dec.setCreatinine(creatinineDecimal);
                        }
                    } catch (NumberFormatException nfe) {
                        message = "Creatinine value must be a number.";
                        request.setAttribute("errorMessage", message);
                        validData = false;
                    }
                } else {
                    dec.setCreatinine(null);
                }

                /* #16 BMI recorded in progress note */
                dec.setBmi(null);

                /* #17 Waist recorded in progress note */
                dec.setWaist(null);

                /* #18 & 19 Blood Pressure recorded in progress note */
                dec.setBloodPressureSystole(null);
                dec.setBloodPressureDiastole(null);

                /* #20 Class date recorded in progress note */
                dec.setClassDate(null);

                /* #21 Eye Screening recorded in progress note */
                dec.setEye(null);

                /* #22 Foot Screening recorded in progress note */
                dec.setFoot(null);

                /* #23 Psychological Screening recorded in progress note */
                dec.setPsychologicalScreening(null);

                /* #24 Physical Activity recorded in progress note */
                dec.setPhysicalActivity(null);

                /* #25 Influenza vaccine date */
                String influenzaDateString = request.getParameter("influenzaDate");
                Date influenzaDate;
                if ((influenzaDateString != null) && (influenzaDateString.trim().length() != 0)) {
                    if (StringUtil.dateCheck(influenzaDateString)) {
                        influenzaDate = Date.valueOf(influenzaDateString);
                        dec.setInfluenzaVaccineDate(influenzaDate);
                    } else {
                        message = "The influenza vaccine date does not conform to the pattern, "
                                + "YYYY-MM-DD.";
                        request.setAttribute("errorMessage", message);
                        validData = false;
                    }
                } else {
                    dec.setInfluenzaVaccineDate(null);
                }

                /* #26 PCV-13 vaccine date */
                String pcv13DateString = request.getParameter("pcv13Date");
                Date pcv13Date;
                if ((pcv13DateString != null) && (pcv13DateString.trim().length() != 0)) {
                    if (StringUtil.dateCheck(pcv13DateString)) {
                        pcv13Date = Date.valueOf(pcv13DateString);
                        dec.setPcv13Date(pcv13Date);
                    } else {
                        message = "The PCV-13 vaccine date does not conform to the pattern, "
                                + "YYYY-MM-DD.";
                        request.setAttribute("errorMessage", message);
                        validData = false;
                    }
                } else {
                    dec.setPcv13Date(null);
                }

                /* #27 PPSV-23 vaccine date */
                String ppsv23DateString = request.getParameter("ppsv23Date");
                Date ppsv23Date;
                if ((ppsv23DateString != null) && (ppsv23DateString.trim().length() != 0)) {
                    if (StringUtil.dateCheck(ppsv23DateString)) {
                        ppsv23Date = Date.valueOf(ppsv23DateString);
                        dec.setPpsv23Date(ppsv23Date);
                    } else {
                        message = "The PPSV-23 vaccine date does not conform to the pattern, "
                                + "YYYY-MM-DD.";
                        request.setAttribute("errorMessage", message);
                        validData = false;
                    }
                } else {
                    dec.setPpsv23Date(null);
                }

                /* #28 Hepatitis B vaccine date */
                String hepbDateString = request.getParameter("hepbDate");
                Date hepbDate;
                if ((hepbDateString != null) && (hepbDateString.trim().length() != 0)) {
                    if (StringUtil.dateCheck(hepbDateString)) {
                        hepbDate = Date.valueOf(hepbDateString);
                        dec.setHepatitisBDate(hepbDate);
                    } else {
                        message = "The hepatitis B vaccine date does not conform to the pattern, "
                                + "YYYY-MM-DD.";
                        request.setAttribute("errorMessage", message);
                        validData = false;
                    }
                } else {
                    dec.setHepatitisBDate(null);
                }

                /* #29 TDAP vaccine date */
                String tdapDateString = request.getParameter("tdapDate");
                Date tdapDate;
                if ((tdapDateString != null) && (tdapDateString.trim().length() != 0)) {
                    if (StringUtil.dateCheck(tdapDateString)) {
                        tdapDate = Date.valueOf(tdapDateString);
                        dec.setTdapDate(tdapDate);
                    } else {
                        message = "The TDAP vaccine date does not conform to the pattern, "
                                + "YYYY-MM-DD.";
                        request.setAttribute("errorMessage", message);
                        validData = false;
                    }
                } else {
                    dec.setTdapDate(null);
                }

                /* #30 Zoster vaccine date */
                String zosterDateString = request.getParameter("zosterDate");
                Date zosterDate;
                if ((zosterDateString != null) && (zosterDateString.trim().length() != 0)) {
                    if (StringUtil.dateCheck(zosterDateString)) {
                        zosterDate = Date.valueOf(zosterDateString);
                        dec.setZosterDate(zosterDate);
                    } else {
                        message = "The zoster vaccine date does not conform to the pattern, "
                                + "YYYY-MM-DD.";
                        request.setAttribute("errorMessage", message);
                        validData = false;
                    }
                } else {
                    dec.setZosterDate(null);
                }

                /* #31 Smoking status recorded in progress note */
                dec.setSmoking(null);

                /* #32 Telephone follow up */
                String telephoneFollowUp = request.getParameter("telephoneFollowUp");
                if ((telephoneFollowUp != null) && (telephoneFollowUp.trim().length() != 0)) {
                    if (StringUtil.tooLongForShortVarChar(telephoneFollowUp)) {
                        message = "The telephone follow-up value must be 50 characters or less.";
                        request.setAttribute("errorMessage", message);
                        validData = false;
                    } else {
                        dec.setTelephoneFollowUp(telephoneFollowUp);
                    }
                } else {
                    dec.setTelephoneFollowUp(null);
                }

                /* #33 AST */
                String ast = request.getParameter("ast");
                if ((ast != null) && (ast.trim().length() != 0)) {
                    try {
                        BigDecimal astDecimal = new BigDecimal(ast, mc);
                        astDecimal.setScale(scale, BigDecimal.ROUND_HALF_UP);
                        if (astDecimal.compareTo(maxDecimal) > 0) {
                            message = "AST value is too large.";
                            request.setAttribute("errorMessage", message);
                            validData = false;
                        } else {
                            dec.setAst(astDecimal);
                        }
                    } catch (NumberFormatException nfe) {
                        message = "AST value must be a number.";
                        request.setAttribute("errorMessage", message);
                        validData = false;
                    }
                } else {
                    dec.setAst(null);
                }

                /* #34 ALT */
                String alt = request.getParameter("alt");
                if ((alt != null) && (alt.trim().length() != 0)) {
                    try {
                        BigDecimal altDecimal = new BigDecimal(alt, mc);
                        altDecimal.setScale(scale, BigDecimal.ROUND_HALF_UP);
                        if (altDecimal.compareTo(maxDecimal) > 0) {
                            message = "ALT value is too large.";
                            request.setAttribute("errorMessage", message);
                            validData = false;
                        } else {
                            dec.setAlt(altDecimal);
                        }
                    } catch (NumberFormatException nfe) {
                        message = "ALT value must be a number.";
                        request.setAttribute("errorMessage", message);
                        validData = false;
                    }
                } else {
                    dec.setAlt(null);
                }

                /* #35 PSA */
                String psa = request.getParameter("psa");
                if ((psa != null) && (psa.trim().length() != 0)) {
                    try {
                        BigDecimal psaDecimal = new BigDecimal(psa, mc);
                        psaDecimal.setScale(scale, BigDecimal.ROUND_HALF_UP);
                        if (psaDecimal.compareTo(maxDecimal) > 0) {
                            message = "PSA value is too large.";
                            request.setAttribute("errorMessage", message);
                            validData = false;
                        } else {
                            dec.setPsa(psaDecimal);
                        }
                    } catch (NumberFormatException nfe) {
                        message = "PSA value must be a number.";
                        request.setAttribute("errorMessage", message);
                        validData = false;
                    }
                } else {
                    dec.setPsa(null);
                }

                /* #36 Compliance recorded in progress note */
                dec.setCompliance(null);

                /* #37 Hospitalization date recorded in progress note */
                dec.setHospitalizationDate(null);

                /* Notes */
                String noteTopic = request.getParameter("noteTopic");
                String note = request.getParameter("note");
                if (((note != null) && (note.trim().length() != 0))
                        && ((noteTopic == null) || (noteTopic.trim().length() == 0))) {
                    message = "A note topic must be selected to enter a note.";
                    request.setAttribute("errorMessage", message);
                    validData = false;
                }

                /* #38 Note topic */
                if ((noteTopic != null) && (noteTopic.trim().length() != 0)) {
                    if (StringUtil.tooLongForShortVarChar(noteTopic)) {
                        message = "The note topic must be 50 characters or less.";
                        request.setAttribute("errorMessage", message);
                        validData = false;
                    } else {
                        dec.setNoteTopic(noteTopic);
                    }
                } else {
                    dec.setNoteTopic(null);
                }

                /* #39 Note */
                if ((note != null) && (note.trim().length() != 0)) {
                    if (StringUtil.tooLongForLongVarChar(note)) {
                        message = "The note must be 1000 characters or less.";
                        request.setAttribute("errorMessage", message);
                        validData = false;
                    } else {
                        dec.setNote(note);
                    }
                } else {
                    dec.setNote(null);
                }

                /* #40 Date entered */
                String dateEnteredString = request.getParameter("dataEntryDate");
                Date dataEntryDate;
                if ((dateEnteredString != null) && (dateEnteredString.trim().length() != 0)) {
                    if (StringUtil.dateCheck(dateEnteredString)) {
                        dataEntryDate = Date.valueOf(dateEnteredString);
                        dec.setDateEntered(dataEntryDate);
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

                /* #41 Point of care */
                String poc = request.getParameter("poc");
                if ((poc != null) && (poc.trim().length() != 0)) {
                    dec.setPoc(true);
                } else {
                    dec.setPoc(false);
                }

                /* #42 ACE or ARB recorded in progress note */
                dec.setAceOrArb(false);

                /* #43 User name */
                dec.setUserName(user.getUserName());

                /* #44 Clinic Id */
                if (clinicId != EMPTY_VALUE) {
                    dec.setClinicId(clinicId);
                } else {
                    message = "You must select a clinic.";
                    request.setAttribute("errorMessage", message);
                    validData = false;
                }
                if (validData) {
                    boolean successfulUpdate = DataEntryIO.addResults(dec,
                            session.getServletContext()
                            .getAttribute("referenceCharacters"));

                    if (successfulUpdate) {
                        message = "The data was entered successfully!";
                        request.setAttribute("message", message);
                    }
                }
                url = "/dataentry/index.jsp";
                break;
            default:
                url = "/dataentry/index.jsp";
                break;
        }
        getServletContext().getRequestDispatcher(url)
                .forward(request, response);
    }
}
