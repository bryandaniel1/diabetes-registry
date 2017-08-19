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

import data.DataEntryDataAccess;
import data.PatientDataAccess;
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
import registry.DataEntryContainer;
import registry.Patient;
import registry.ReferenceContainer;
import registry.User;
import utility.SessionObjectUtility;
import utility.StringUtility;

/**
 * This HttpServlet class coordinates the input processing for the data entry
 * page.
 *
 * @author Bryan Daniel
 * @version 2, March 16, 2017
 */
public class DataEntryServlet extends HttpServlet {

    /**
     * Serial version UID
     */
    private static final long serialVersionUID = 6466449494279734366L;

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
     * navigation of the data entry page, retrieves selected patient
     * information, and processes the requests for saving patient measurements
     * and other data related to patient care.
     *
     * The patient measurements are saved by validating all form inputs and
     * storing these inputs in a DataEntryContainer object to pass to the
     * data-access class.
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
        String url = "/dataentry/index.jsp";
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
            case "addResults":
                boolean validData = true;
                MathContext mc = new MathContext(7);
                final BigDecimal maxDecimal = new BigDecimal("99999.99");
                final int scale = 2;
                DataEntryContainer dec = new DataEntryContainer();

                /* patient ID */
                Patient patient = (Patient) session.getAttribute(SessionObjectUtility.PATIENT);
                int patientId = patient.getPatientId();
                dec.setPatientId(patientId);

                /* A1C */
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

                /* glucose AC & glucose PC */
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

                /* LDL */
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

                /* LDL Post MI */
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

                /* LDL on statin */
                String onStatin = request.getParameter("onstatin");
                if ((onStatin != null) && (onStatin.trim().length() != 0)) {
                    dec.setOnStatin(true);
                } else {
                    dec.setOnStatin(false);
                }

                /* HDL */
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

                /* Triglycerides */
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

                /* TSH */
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

                /* TSH on thyroid treatment */
                String onThyroidTreatment = request.getParameter("onthyroidtreatment");
                if ((onThyroidTreatment != null)
                        && (onThyroidTreatment.trim().length() != 0)) {
                    dec.setOnThyroidTreatment(true);
                } else {
                    dec.setOnThyroidTreatment(false);
                }

                /* T4 */
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

                /* UACR */
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

                /* eGFR */
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

                /* Creatinine */
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

                /* Influenza vaccine date */
                String influenzaDateString = request.getParameter("influenzaDate");
                Date influenzaDate;
                if ((influenzaDateString != null) && (influenzaDateString.trim().length() != 0)) {
                    if (StringUtility.dateCheck(influenzaDateString)) {
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

                /* PCV-13 vaccine date */
                String pcv13DateString = request.getParameter("pcv13Date");
                Date pcv13Date;
                if ((pcv13DateString != null) && (pcv13DateString.trim().length() != 0)) {
                    if (StringUtility.dateCheck(pcv13DateString)) {
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

                /* PPSV-23 vaccine date */
                String ppsv23DateString = request.getParameter("ppsv23Date");
                Date ppsv23Date;
                if ((ppsv23DateString != null) && (ppsv23DateString.trim().length() != 0)) {
                    if (StringUtility.dateCheck(ppsv23DateString)) {
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

                /* Hepatitis B vaccine date */
                String hepbDateString = request.getParameter("hepbDate");
                Date hepbDate;
                if ((hepbDateString != null) && (hepbDateString.trim().length() != 0)) {
                    if (StringUtility.dateCheck(hepbDateString)) {
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

                /* TDAP vaccine date */
                String tdapDateString = request.getParameter("tdapDate");
                Date tdapDate;
                if ((tdapDateString != null) && (tdapDateString.trim().length() != 0)) {
                    if (StringUtility.dateCheck(tdapDateString)) {
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

                /* Zoster vaccine date */
                String zosterDateString = request.getParameter("zosterDate");
                Date zosterDate;
                if ((zosterDateString != null) && (zosterDateString.trim().length() != 0)) {
                    if (StringUtility.dateCheck(zosterDateString)) {
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

                /* Telephone follow up */
                String telephoneFollowUp = request.getParameter("telephoneFollowUp");
                if ((telephoneFollowUp != null) && (telephoneFollowUp.trim().length() != 0)) {
                    if (StringUtility.tooLongForShortVarChar(telephoneFollowUp)) {
                        message = "The telephone follow-up value must be 50 characters or less.";
                        request.setAttribute("errorMessage", message);
                        validData = false;
                    } else {
                        dec.setTelephoneFollowUp(telephoneFollowUp);
                    }
                } else {
                    dec.setTelephoneFollowUp(null);
                }

                /* AST */
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

                /* ALT */
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

                /* PSA */
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

                /* Notes */
                String noteTopic = request.getParameter("noteTopic");
                String note = request.getParameter("note");
                if (((note != null) && (note.trim().length() != 0))
                        && ((noteTopic == null) || (noteTopic.trim().length() == 0))) {
                    message = "A note topic must be selected to enter a note.";
                    request.setAttribute("errorMessage", message);
                    validData = false;
                }

                /* Note topic */
                if ((noteTopic != null) && (noteTopic.trim().length() != 0)) {
                    if (StringUtility.tooLongForShortVarChar(noteTopic)) {
                        message = "The note topic must be 50 characters or less.";
                        request.setAttribute("errorMessage", message);
                        validData = false;
                    } else {
                        dec.setNoteTopic(noteTopic);
                    }
                } else {
                    dec.setNoteTopic(null);
                }

                /* Note */
                if ((note != null) && (note.trim().length() != 0)) {
                    if (StringUtility.tooLongForLongVarChar(note)) {
                        message = "The note must be 1000 characters or less.";
                        request.setAttribute("errorMessage", message);
                        validData = false;
                    } else {
                        dec.setNote(note);
                    }
                } else {
                    dec.setNote(null);
                }

                /* Date entered */
                String dateEnteredString = request.getParameter("dataEntryDate");
                Date dataEntryDate;
                if ((dateEnteredString != null) && (dateEnteredString.trim().length() != 0)) {
                    if (StringUtility.dateCheck(dateEnteredString)) {
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

                /* Point of care */
                String poc = request.getParameter("poc");
                if ((poc != null) && (poc.trim().length() != 0)) {
                    dec.setPoc(true);
                } else {
                    dec.setPoc(false);
                }

                /* User name */
                dec.setUserName(user.getUserName());

                /* Clinic Id */
                dec.setClinicId(clinicId);

                if (validData) {
                    boolean successfulUpdate = DataEntryDataAccess.addResults(dec,
                            session.getServletContext()
                            .getAttribute("referenceCharacters"));

                    if (successfulUpdate) {
                        message = "The data was entered successfully!";
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
