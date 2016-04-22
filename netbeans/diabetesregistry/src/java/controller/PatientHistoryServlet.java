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

import clinic.A1cResult;
import clinic.BloodPressureResult;
import clinic.BooleanResult;
import clinic.CategoricalResult;
import clinic.ContinuousResult;
import clinic.Dashboard;
import clinic.DiscreteResult;
import clinic.LdlResult;
import clinic.Patient;
import clinic.PsychologicalScreeningResult;
import clinic.QualityReference;
import clinic.TreatmentHistory;
import clinic.TshResult;
import clinic.User;
import data.PatientHistoryIO;
import data.PatientIO;
import data.PatientTreatmentIO;
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
 * Coordinates the retrieval of patient information
 *
 * @author Bryan Daniel
 * @version 1, April 8, 2016
 */
public class PatientHistoryServlet extends HttpServlet {

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
        Patient patient = (Patient) session.getAttribute("patient");
        String url = "/history/index.jsp";
        final int EMPTY_VALUE = 0;
        int index = 0;
        int patientId;
        int clinicId;
        if (session.getAttribute("clinicId") == null) {
            clinicId = EMPTY_VALUE;
        } else {
            clinicId = (int) session.getAttribute("clinicId");
        }
        String message;
        String action = request.getParameter("action");
        User user = (User) session.getAttribute("user");
        ArrayList<Patient> patients;

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
                if (patient != null) {
                    Dashboard dashboard
                            = PatientHistoryIO.getPatientDashboard(patient.getPatientId(),
                                    session.getServletContext()
                                    .getAttribute("referenceCharacters"));
                    request.setAttribute("dashboard", dashboard);
                    request.setAttribute("statuslist", dashboard.getHts());
                }
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
                } catch (NumberFormatException nfe) {
                    message = "clinic id invalid";
                    request.setAttribute("errorMessage", message);
                }
                break;
            case "getPatient":
                String patientSelect = request.getParameter("patientselect");
                try {
                    patientId = Integer.parseInt(patientSelect);
                    patients = (ArrayList<Patient>) session.getAttribute("patients");
                    for (Patient p : patients) {
                        if (p.getPatientId() == patientId) {
                            session.setAttribute("patient", p);
                            SessionObjectUtil.resetPatientObjects(session);
                            Dashboard dashboard
                                    = PatientHistoryIO.getPatientDashboard(patientId,
                                            session.getServletContext()
                                            .getAttribute("referenceCharacters"));
                            request.setAttribute("dashboard", dashboard);
                            request.setAttribute("statuslist", dashboard.getHts());
                            break;
                        }
                    }
                } catch (NumberFormatException nfe) {
                    message = "patient id invalid";
                    request.setAttribute("errorMessage", message);
                }
                break;
            case "historyOption":
                String a1cTopic = "A1C";
                String glucoseTopic = "Glucose";
                String ldlTopic = "LDL";
                String hdlTopic = "HDL";
                String trigTopic = "Triglycerides";
                String tshTopic = "TSH";
                String t4Topic = "T4";
                String uacrTopic = "UACR";
                String egfrTopic = "eGFR";
                String creatinineTopic = "Creatinine";
                String bmiTopic = "BMI";
                String waistTopic = "Waist";
                String bpTopic = "Blood Pressure";
                String classTopic = "Class";
                String eyeTopic = "Eye Screening";
                String footTopic = "Foot Screening";
                String psycTopic = "Psychological Screening";
                String physicalTopic = "Physical Activity";
                String fluTopic = "Influenza Vaccine";
                String pcv13Topic = "PCV-13 Vaccine";
                String ppsv23Topic = "PPSV-23 Vaccine";
                String hepbTopic = "Hepatitis B Vaccine";
                String tdapTopic = "TDAP Vaccine";
                String zosterTopic = "Zoster Vaccine";
                String smokingTopic = "Smoking";
                String telephoneTopic = "Telephone Follow Up";
                String astTopic = "AST";
                String altTopic = "ALT";
                String psaTopic = "PSA";
                String complianceTopic = "Compliance";
                String erTopic = "Hospitalization";
                String treatmentTopic = "Treatment";

                patient = (Patient) session.getAttribute("patient");
                patientId = patient.getPatientId();
                String historySelect = request.getParameter("historySelect");
                switch (historySelect) {
                    case "dashboard":
                        Dashboard dashboard
                                = PatientHistoryIO.getPatientDashboard(patientId,
                                        session.getServletContext()
                                        .getAttribute("referenceCharacters"));
                        request.setAttribute("dashboard", dashboard);
                        request.setAttribute("statuslist", dashboard.getHts());

                        break;
                    case "a1c": {
                        ArrayList<A1cResult> a1cHistory
                                = PatientHistoryIO.getA1c(patientId);
                        ArrayList<CategoricalResult> notes
                                = PatientHistoryIO.getNotes(patientId, a1cTopic);
                        request.setAttribute("notes", notes);
                        if (a1cHistory != null) {
                            request.setAttribute("a1cHistory", a1cHistory);

                            if (a1cHistory.size() > 1) {
                                session.setAttribute("a1cGraphPoints", a1cHistory);
                            }
                        }
                        if ((a1cHistory == null) && (notes == null)) {
                            message = "No results to display";
                            request.setAttribute("message", message);
                        }
                        break;
                    }
                    case "psa": {
                        ArrayList<ContinuousResult> psaHistory
                                = PatientHistoryIO.getPsa(patientId);
                        ArrayList<CategoricalResult> notes
                                = PatientHistoryIO.getNotes(patientId, psaTopic);
                        request.setAttribute("notes", notes);
                        if (psaHistory != null) {
                            request.setAttribute("psaHistory", psaHistory);

                            if (psaHistory.size() > 1) {
                                session.setAttribute("psaGraphPoints", psaHistory);
                            }
                        }
                        if ((psaHistory == null) && (notes == null)) {
                            message = "No results to display";
                            request.setAttribute("message", message);
                        }
                        break;
                    }
                    case "alt": {
                        ArrayList<ContinuousResult> altHistory
                                = PatientHistoryIO.getAlt(patientId);
                        ArrayList<CategoricalResult> notes
                                = PatientHistoryIO.getNotes(patientId, altTopic);
                        request.setAttribute("notes", notes);
                        if (altHistory != null) {
                            request.setAttribute("altHistory", altHistory);

                            if (altHistory.size() > 1) {
                                session.setAttribute("altGraphPoints", altHistory);
                            }
                        }
                        if ((altHistory == null) && (notes == null)) {
                            message = "No results to display";
                            request.setAttribute("message", message);
                        }
                        break;
                    }
                    case "ast": {
                        ArrayList<ContinuousResult> astHistory
                                = PatientHistoryIO.getAst(patientId);
                        ArrayList<CategoricalResult> notes
                                = PatientHistoryIO.getNotes(patientId, astTopic);
                        request.setAttribute("notes", notes);
                        if (astHistory != null) {
                            request.setAttribute("astHistory", astHistory);

                            if (astHistory.size() > 1) {
                                session.setAttribute("astGraphPoints", astHistory);
                            }
                        }
                        if ((astHistory == null) && (notes == null)) {
                            message = "No results to display";
                            request.setAttribute("message", message);
                        }
                        break;
                    }
                    case "bloodPressure": {
                        ArrayList<BloodPressureResult> bpHistory
                                = PatientHistoryIO.getBP(patientId);
                        ArrayList<CategoricalResult> notes
                                = PatientHistoryIO.getNotes(patientId, bpTopic);
                        request.setAttribute("notes", notes);
                        if (bpHistory != null) {
                            request.setAttribute("bpHistory", bpHistory);

                            if (bpHistory.size() > 1) {
                                session.setAttribute("bpGraphPoints", bpHistory);
                            }
                        }
                        if ((bpHistory == null) && (notes == null)) {
                            message = "No results to display";
                            request.setAttribute("message", message);
                        }
                        break;
                    }
                    case "BMI": {
                        ArrayList<ContinuousResult> bmiHistory
                                = PatientHistoryIO.getBmi(patientId);
                        ArrayList<CategoricalResult> notes
                                = PatientHistoryIO.getNotes(patientId, bmiTopic);
                        request.setAttribute("notes", notes);
                        if (bmiHistory != null) {
                            request.setAttribute("bmiHistory", bmiHistory);

                            if (bmiHistory.size() > 1) {
                                session.setAttribute("bmiGraphPoints", bmiHistory);
                            }
                        }
                        if ((bmiHistory == null) && (notes == null)) {
                            message = "No results to display";
                            request.setAttribute("message", message);
                        }
                        break;
                    }
                    case "checklists": {
                        ArrayList<Date> checklistDates
                                = QualityIO.getChecklistDates(patientId);
                        if (checklistDates != null) {
                            request.setAttribute("checklistDates", checklistDates);

                        } else {
                            message = "No results to display";
                            request.setAttribute("message", message);
                        }
                        break;
                    }
                    case "class": {
                        ArrayList<Date> classHistory
                                = PatientHistoryIO.getClass(patientId);
                        ArrayList<CategoricalResult> notes
                                = PatientHistoryIO.getNotes(patientId, classTopic);
                        request.setAttribute("notes", notes);
                        if (classHistory != null) {
                            request.setAttribute("classHistory", classHistory);

                        }
                        if ((classHistory == null) && (notes == null)) {
                            message = "No results to display";
                            request.setAttribute("message", message);
                        }
                        break;
                    }
                    case "creatinine": {
                        ArrayList<ContinuousResult> creatinineHistory
                                = PatientHistoryIO.getCreatinine(patientId);
                        ArrayList<CategoricalResult> notes
                                = PatientHistoryIO.getNotes(patientId, creatinineTopic);
                        request.setAttribute("notes", notes);
                        if (creatinineHistory != null) {
                            request.setAttribute("creatinineHistory", creatinineHistory);

                            if (creatinineHistory.size() > 1) {
                                session.setAttribute("creatinineGraphPoints",
                                        creatinineHistory);
                            }
                        }
                        if ((creatinineHistory == null) && (notes == null)) {
                            message = "No results to display";
                            request.setAttribute("message", message);
                        }
                        break;
                    }
                    case "eGFR": {
                        ArrayList<ContinuousResult> egfrHistory
                                = PatientHistoryIO.getEgfr(patientId);
                        ArrayList<CategoricalResult> notes
                                = PatientHistoryIO.getNotes(patientId, egfrTopic);
                        request.setAttribute("notes", notes);
                        if (egfrHistory != null) {
                            request.setAttribute("egfrHistory", egfrHistory);

                            if (egfrHistory.size() > 1) {
                                session.setAttribute("egfrGraphPoints", egfrHistory);
                            }
                        }
                        if ((egfrHistory == null) && (notes == null)) {
                            message = "No results to display";
                            request.setAttribute("message", message);
                        }
                        break;
                    }
                    case "eye": {
                        ArrayList<CategoricalResult> eyeHistory
                                = PatientHistoryIO.getEye(patientId);
                        ArrayList<CategoricalResult> notes
                                = PatientHistoryIO.getNotes(patientId, eyeTopic);
                        request.setAttribute("notes", notes);
                        if (eyeHistory != null) {
                            request.setAttribute("eyeHistory", eyeHistory);

                        }
                        if ((eyeHistory == null) && (notes == null)) {
                            message = "No results to display";
                            request.setAttribute("message", message);
                        }
                        break;
                    }
                    case "foot": {
                        ArrayList<CategoricalResult> footHistory
                                = PatientHistoryIO.getFoot(patientId);
                        ArrayList<CategoricalResult> notes
                                = PatientHistoryIO.getNotes(patientId, footTopic);
                        request.setAttribute("notes", notes);
                        if (footHistory != null) {
                            request.setAttribute("footHistory", footHistory);

                        }
                        if ((footHistory == null) && (notes == null)) {
                            message = "No results to display";
                            request.setAttribute("message", message);
                        }
                        break;
                    }
                    case "glucose": {
                        ArrayList<ContinuousResult> glucoseHistory
                                = PatientHistoryIO.getGlucose(patientId);
                        ArrayList<CategoricalResult> notes
                                = PatientHistoryIO.getNotes(patientId, glucoseTopic);
                        request.setAttribute("notes", notes);
                        if (glucoseHistory != null) {
                            request.setAttribute("glucoseHistory", glucoseHistory);

                            if (glucoseHistory.size() > 1) {
                                session.setAttribute("glucoseGraphPoints", glucoseHistory);
                            }
                        }
                        if ((glucoseHistory == null) && (notes == null)) {
                            message = "No results to display";
                            request.setAttribute("message", message);
                        }
                        break;
                    }
                    case "HDL": {
                        ArrayList<ContinuousResult> hdlHistory
                                = PatientHistoryIO.getHdl(patientId);
                        ArrayList<CategoricalResult> notes
                                = PatientHistoryIO.getNotes(patientId, hdlTopic);
                        request.setAttribute("notes", notes);
                        if (hdlHistory != null) {
                            request.setAttribute("hdlHistory", hdlHistory);

                            if (hdlHistory.size() > 1) {
                                session.setAttribute("hdlGraphPoints", hdlHistory);
                            }
                        }
                        if ((hdlHistory == null) && (notes == null)) {
                            message = "No results to display";
                            request.setAttribute("message", message);
                        }
                        break;
                    }
                    case "hepb": {
                        ArrayList<Date> hepBHistory
                                = PatientHistoryIO.getHepB(patientId);
                        ArrayList<CategoricalResult> notes
                                = PatientHistoryIO.getNotes(patientId, hepbTopic);
                        request.setAttribute("notes", notes);
                        if (hepBHistory != null) {
                            request.setAttribute("hepBHistory", hepBHistory);

                        }
                        if ((hepBHistory == null) && (notes == null)) {
                            message = "No results to display";
                            request.setAttribute("message", message);
                        }
                        break;
                    }
                    case "hospitalization": {
                        ArrayList<Date> hospitalizationHistory
                                = PatientHistoryIO.getER(patientId);
                        ArrayList<CategoricalResult> notes
                                = PatientHistoryIO.getNotes(patientId, erTopic);
                        request.setAttribute("notes", notes);
                        if (hospitalizationHistory != null) {
                            request.setAttribute("hospitalizationHistory", hospitalizationHistory);

                        }
                        if ((hospitalizationHistory == null) && (notes == null)) {
                            message = "No results to display";
                            request.setAttribute("message", message);
                        }
                        break;
                    }
                    case "influenza": {
                        ArrayList<Date> influenzaHistory
                                = PatientHistoryIO.getInfluenza(patientId);
                        ArrayList<CategoricalResult> notes
                                = PatientHistoryIO.getNotes(patientId, fluTopic);
                        request.setAttribute("notes", notes);
                        if (influenzaHistory != null) {
                            request.setAttribute("influenzaHistory", influenzaHistory);

                        }
                        if ((influenzaHistory == null) && (notes == null)) {
                            message = "No results to display";
                            request.setAttribute("message", message);
                        }
                        break;
                    }
                    case "LDL": {
                        ArrayList<LdlResult> ldlHistory
                                = PatientHistoryIO.getLdl(patientId);
                        ArrayList<CategoricalResult> notes
                                = PatientHistoryIO.getNotes(patientId, ldlTopic);
                        request.setAttribute("notes", notes);
                        if (ldlHistory != null) {
                            request.setAttribute("ldlHistory", ldlHistory);

                            if (ldlHistory.size() > 1) {
                                session.setAttribute("ldlGraphPoints", ldlHistory);
                            }
                        }
                        if ((ldlHistory == null) && (notes == null)) {
                            message = "No results to display";
                            request.setAttribute("message", message);
                        }
                        break;
                    }
                    case "notes": {
                        ArrayList<CategoricalResult> allNotes
                                = PatientHistoryIO.getAllNotes(patientId);
                        if (allNotes != null) {
                            request.setAttribute("allNotes", allNotes);
                        } else {
                            message = "No results to display";
                            request.setAttribute("message", message);
                        }
                        break;
                    }
                    case "compliance": {
                        ArrayList<ContinuousResult> complianceHistory
                                = PatientHistoryIO.getCompliance(patientId);
                        ArrayList<CategoricalResult> notes
                                = PatientHistoryIO.getNotes(patientId, complianceTopic);
                        request.setAttribute("notes", notes);
                        if (complianceHistory != null) {
                            request.setAttribute("complianceHistory", complianceHistory);

                            if (complianceHistory.size() > 1) {
                                session.setAttribute("comlianceGraphPoints", complianceHistory);
                            }
                        }
                        if ((complianceHistory == null) && (notes == null)) {
                            message = "No results to display";
                            request.setAttribute("message", message);
                        }
                        break;
                    }
                    case "PCV-13": {
                        ArrayList<Date> pcv13History
                                = PatientHistoryIO.getPcv13(patientId);
                        ArrayList<CategoricalResult> notes
                                = PatientHistoryIO.getNotes(patientId, pcv13Topic);
                        request.setAttribute("notes", notes);
                        if (pcv13History != null) {
                            request.setAttribute("pcv13History", pcv13History);

                        }
                        if ((pcv13History == null) && (notes == null)) {
                            message = "No results to display";
                            request.setAttribute("message", message);
                        }
                        break;
                    }
                    case "physicalActivity": {
                        ArrayList<DiscreteResult> physicalActivityHistory
                                = PatientHistoryIO.getPhysical(patientId);
                        ArrayList<CategoricalResult> notes
                                = PatientHistoryIO.getNotes(patientId, physicalTopic);
                        request.setAttribute("notes", notes);
                        if (physicalActivityHistory != null) {
                            request.setAttribute("physicalActivityHistory",
                                    physicalActivityHistory);

                            if (physicalActivityHistory.size() > 1) {
                                session.setAttribute("physicalActivityGraphPoints",
                                        physicalActivityHistory);
                            }
                        }
                        if ((physicalActivityHistory == null) && (notes == null)) {
                            message = "No results to display";
                            request.setAttribute("message", message);
                        }
                        break;
                    }
                    case "PPSV-23": {
                        ArrayList<Date> ppsv23History
                                = PatientHistoryIO.getPpsv23(patientId);
                        ArrayList<CategoricalResult> notes
                                = PatientHistoryIO.getNotes(patientId, ppsv23Topic);
                        request.setAttribute("notes", notes);
                        if (ppsv23History != null) {
                            request.setAttribute("ppsv23History", ppsv23History);

                        }
                        if ((ppsv23History == null) && (notes == null)) {
                            message = "No results to display";
                            request.setAttribute("message", message);
                        }
                        break;
                    }
                    case "psychological": {
                        ArrayList<PsychologicalScreeningResult> psychologicalHistory
                                = PatientHistoryIO.getPsychological(patientId);
                        ArrayList<CategoricalResult> notes
                                = PatientHistoryIO.getNotes(patientId, psycTopic);
                        request.setAttribute("notes", notes);
                        if (psychologicalHistory != null) {
                            request.setAttribute("psychologicalHistory", psychologicalHistory);

                            if (psychologicalHistory.size() > 1) {
                                session.setAttribute("psychologicalGraphPoints",
                                        psychologicalHistory);
                            }
                        }
                        if ((psychologicalHistory == null) && (notes == null)) {
                            message = "No results to display";
                            request.setAttribute("message", message);
                        }
                        break;
                    }
                    case "smoking": {
                        ArrayList<BooleanResult> smokingHistory
                                = PatientHistoryIO.getSmoking(patientId);
                        ArrayList<CategoricalResult> notes
                                = PatientHistoryIO.getNotes(patientId, smokingTopic);
                        request.setAttribute("notes", notes);
                        if (smokingHistory != null) {
                            request.setAttribute("smokingHistory", smokingHistory);

                        }
                        if ((smokingHistory == null) && (notes == null)) {
                            message = "No results to display";
                            request.setAttribute("message", message);
                        }
                        break;
                    }
                    case "T4": {
                        ArrayList<ContinuousResult> t4History
                                = PatientHistoryIO.getT4(patientId);
                        ArrayList<CategoricalResult> notes
                                = PatientHistoryIO.getNotes(patientId, t4Topic);
                        request.setAttribute("notes", notes);
                        if (t4History != null) {
                            request.setAttribute("t4History", t4History);

                            if (t4History.size() > 1) {
                                session.setAttribute("t4GraphPoints",
                                        t4History);
                            }
                        }
                        if ((t4History == null) && (notes == null)) {
                            message = "No results to display";
                            request.setAttribute("message", message);
                        }
                        break;
                    }
                    case "TDAP": {
                        ArrayList<Date> tdapHistory
                                = PatientHistoryIO.getTdap(patientId);
                        ArrayList<CategoricalResult> notes
                                = PatientHistoryIO.getNotes(patientId, tdapTopic);
                        request.setAttribute("notes", notes);
                        if (tdapHistory != null) {
                            request.setAttribute("tdapHistory", tdapHistory);

                        }
                        if ((tdapHistory == null) && (notes == null)) {
                            message = "No results to display";
                            request.setAttribute("message", message);
                        }
                        break;
                    }
                    case "telephone": {
                        ArrayList<CategoricalResult> telephoneHistory
                                = PatientHistoryIO.getTelephone(patientId);
                        ArrayList<CategoricalResult> notes
                                = PatientHistoryIO.getNotes(patientId, telephoneTopic);
                        request.setAttribute("notes", notes);
                        if (telephoneHistory != null) {
                            request.setAttribute("telephoneHistory", telephoneHistory);

                        }
                        if ((telephoneHistory == null) && (notes == null)) {
                            message = "No results to display";
                            request.setAttribute("message", message);
                        }
                        break;
                    }
                    case "treatment": {
                        TreatmentHistory treatmentHistory
                                = PatientTreatmentIO.getTreatments(patientId);
                        ArrayList<CategoricalResult> notes
                                = PatientHistoryIO.getNotes(patientId, treatmentTopic);
                        request.setAttribute("notes", notes);
                        if ((treatmentHistory != null)
                                && (treatmentHistory.getTherapies() == null)
                                && (treatmentHistory.getMedications() == null)
                                && (notes == null)) {
                            message = "No results to display";
                            request.setAttribute("message", message);
                        } else if (treatmentHistory != null) {
                            request.setAttribute("rxHistory", treatmentHistory.getTherapies());
                            request.setAttribute("medHistory", treatmentHistory.getMedications());
                        }
                        break;
                    }
                    case "triglycerides": {
                        ArrayList<ContinuousResult> triglyceridesHistory
                                = PatientHistoryIO.getTriglycerides(patientId);
                        ArrayList<CategoricalResult> notes
                                = PatientHistoryIO.getNotes(patientId, trigTopic);
                        request.setAttribute("notes", notes);
                        if (triglyceridesHistory != null) {
                            request.setAttribute("triglyceridesHistory",
                                    triglyceridesHistory);

                            if (triglyceridesHistory.size() > 1) {
                                session.setAttribute("triglyceridesGraphPoints",
                                        triglyceridesHistory);
                            }
                        }
                        if ((triglyceridesHistory == null) && (notes == null)) {
                            message = "No results to display";
                            request.setAttribute("message", message);
                        }
                        break;
                    }
                    case "TSH": {
                        ArrayList<TshResult> tshHistory
                                = PatientHistoryIO.getTsh(patientId);
                        ArrayList<CategoricalResult> notes
                                = PatientHistoryIO.getNotes(patientId, tshTopic);
                        request.setAttribute("notes", notes);
                        if (tshHistory != null) {
                            request.setAttribute("tshHistory", tshHistory);

                            if (tshHistory.size() > 1) {
                                session.setAttribute("tshGraphPoints",
                                        tshHistory);
                            }
                        }
                        if ((tshHistory == null) && (notes == null)) {
                            message = "No results to display";
                            request.setAttribute("message", message);
                        }
                        break;
                    }
                    case "UACR": {
                        ArrayList<ContinuousResult> uacrHistory
                                = PatientHistoryIO.getUacr(patientId);
                        ArrayList<CategoricalResult> notes
                                = PatientHistoryIO.getNotes(patientId, uacrTopic);
                        request.setAttribute("notes", notes);
                        if (uacrHistory != null) {
                            request.setAttribute("uacrHistory", uacrHistory);

                            if (uacrHistory.size() > 1) {
                                session.setAttribute("uacrGraphPoints", uacrHistory);
                            }
                        }
                        if ((uacrHistory == null) && (notes == null)) {
                            message = "No results to display";
                            request.setAttribute("message", message);
                        }
                        break;
                    }
                    case "waist": {
                        ArrayList<ContinuousResult> waistHistory
                                = PatientHistoryIO.getWaist(patientId);
                        ArrayList<CategoricalResult> notes
                                = PatientHistoryIO.getNotes(patientId, waistTopic);
                        request.setAttribute("notes", notes);
                        if (waistHistory != null) {
                            request.setAttribute("waistHistory", waistHistory);

                            if (waistHistory.size() > 1) {
                                session.setAttribute("waistGraphPoints",
                                        waistHistory);
                            }
                        }
                        if ((waistHistory == null) && (notes == null)) {
                            message = "No results to display";
                            request.setAttribute("message", message);
                        }
                        break;
                    }
                    case "zoster": {
                        ArrayList<Date> zosterHistory
                                = PatientHistoryIO.getZoster(patientId);
                        ArrayList<CategoricalResult> notes
                                = PatientHistoryIO.getNotes(patientId, zosterTopic);
                        request.setAttribute("notes", notes);
                        if (zosterHistory != null) {
                            request.setAttribute("zosterHistory", zosterHistory);

                        }
                        if ((zosterHistory == null) && (notes == null)) {
                            message = "No results to display";
                            request.setAttribute("message", message);
                        }
                        break;
                    }
                    default:
                        dashboard = PatientHistoryIO
                                .getPatientDashboard(patient.getPatientId(),
                                        session.getServletContext()
                                        .getAttribute("referenceCharacters"));
                        request.setAttribute("dashboard", dashboard);
                        request.setAttribute("statuslist", dashboard.getHts());
                        break;
                }
                break;
            case "qualityChecklist":
                boolean validData = true;
                if (clinicId == EMPTY_VALUE) {
                    message = "You must select a clinic.";
                    request.setAttribute("errorMessage", message);
                    validData = false;
                }
                String checklistSelect = request.getParameter("checklistSelect");
                Date checklistDate = null;
                if (StringUtil.dateCheck(checklistSelect)) {
                    checklistDate = Date.valueOf(checklistSelect);
                } else {
                    message = "The checklist date does not conform to the pattern, "
                            + "YYYY-MM-DD.";
                    request.setAttribute("errorMessage", message);
                    validData = false;
                }
                if (validData) {
                    patientId = patient.getPatientId();
                    ArrayList<QualityReference> checklistItems
                            = QualityIO.getChecklistItems(patientId,
                                    checklistDate, clinicId);
                    if (checklistItems != null) {
                        request.setAttribute("checklistDate", checklistSelect);
                        request.setAttribute("checklistItems", checklistItems);

                    } else {
                        message = "No results to display";
                        request.setAttribute("message", message);
                    }
                }
                break;
        }
        getServletContext().getRequestDispatcher(url)
                .forward(request, response);
    }
}
