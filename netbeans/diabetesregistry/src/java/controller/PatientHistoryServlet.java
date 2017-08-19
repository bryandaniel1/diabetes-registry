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
import data.PatientHistoryDataAccess;
import data.PatientTreatmentDataAccess;
import data.QualityDataAccess;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import registry.A1cResult;
import registry.BloodPressureResult;
import registry.BooleanResult;
import registry.CategoricalResult;
import registry.ContinuousResult;
import registry.Dashboard;
import registry.DiscreteResult;
import registry.LdlResult;
import registry.Patient;
import registry.PsychologicalScreeningResult;
import registry.QualityReference;
import registry.ReferenceContainer;
import registry.TreatmentHistory;
import registry.TshResult;
import utility.SessionObjectUtility;
import utility.StringUtility;

/**
 * This HttpServlet class coordinates the retrieval of patient information on
 * the patient history page.
 *
 * @author Bryan Daniel
 * @version 2, March 16, 2017
 */
public class PatientHistoryServlet extends HttpServlet {

    /**
     * Serial version UID
     */
    private static final long serialVersionUID = 1480103124302998726L;

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
     * navigation of the patient history page and processes the requests for
     * viewing a dashboard of recent measurements or a measurement-specific
     * history.
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
        String url = "/history/index.jsp";
        Patient patient = (Patient) session.getAttribute(SessionObjectUtility.PATIENT);
        int clinicId = ReferenceContainer.CLINIC_ID;
        Object referenceCharacters = session.getServletContext()
                .getAttribute("referenceCharacters");
        int patientId;
        String message;
        ArrayList<Patient> patients;
        String action = request.getParameter("action");

        if (action == null) {
            action = "get list";
        }

        switch (action) {
            case "get list":
                patients = (ArrayList<Patient>) session.getAttribute(SessionObjectUtility.PATIENTS);
                if (patients == null) {
                    patients = PatientDataAccess.getPatients(clinicId,
                            referenceCharacters);
                    session.setAttribute(SessionObjectUtility.PATIENTS, patients);
                }
                if (patient != null) {
                    Dashboard dashboard
                            = PatientHistoryDataAccess.getPatientDashboard(patient.getPatientId(),
                                    referenceCharacters);
                    request.setAttribute("dashboard", dashboard);
                    request.setAttribute("statuslist", dashboard.getHts());
                }
                break;
            case "getPatient":
                String patientSelect = request.getParameter("patientselect");
                try {
                    patientId = Integer.parseInt(patientSelect);
                    patients = (ArrayList<Patient>) session.getAttribute(SessionObjectUtility.PATIENTS);
                    for (Patient p : patients) {
                        if (p.getPatientId() == patientId) {
                            session.setAttribute(SessionObjectUtility.PATIENT, p);
                            SessionObjectUtility.resetPatientObjects(session);
                            Dashboard dashboard
                                    = PatientHistoryDataAccess.getPatientDashboard(patientId,
                                            referenceCharacters);
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

                patient = (Patient) session.getAttribute(SessionObjectUtility.PATIENT);
                patientId = patient.getPatientId();
                String historySelect = request.getParameter("historySelect");
                switch (historySelect) {
                    case "dashboard":
                        Dashboard dashboard
                                = PatientHistoryDataAccess.getPatientDashboard(patientId,
                                        referenceCharacters);
                        request.setAttribute("dashboard", dashboard);
                        request.setAttribute("statuslist", dashboard.getHts());

                        break;
                    case "a1c": {
                        ArrayList<A1cResult> a1cHistory
                                = PatientHistoryDataAccess.getA1c(patientId);
                        ArrayList<CategoricalResult> notes
                                = PatientHistoryDataAccess.getNotes(patientId, a1cTopic,
                                        referenceCharacters);
                        request.setAttribute("notes", notes);
                        if (a1cHistory != null) {
                            request.setAttribute("a1cHistory", a1cHistory);

                            if (a1cHistory.size() > 1) {
                                session.setAttribute(SessionObjectUtility.A1C_GRAPH_POINTS, a1cHistory);
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
                                = PatientHistoryDataAccess.getPsa(patientId);
                        ArrayList<CategoricalResult> notes
                                = PatientHistoryDataAccess.getNotes(patientId, psaTopic,
                                        referenceCharacters);
                        request.setAttribute("notes", notes);
                        if (psaHistory != null) {
                            request.setAttribute("psaHistory", psaHistory);

                            if (psaHistory.size() > 1) {
                                session.setAttribute(SessionObjectUtility.PSA_GRAPH_POINTS, psaHistory);
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
                                = PatientHistoryDataAccess.getAlt(patientId);
                        ArrayList<CategoricalResult> notes
                                = PatientHistoryDataAccess.getNotes(patientId, altTopic,
                                        referenceCharacters);
                        request.setAttribute("notes", notes);
                        if (altHistory != null) {
                            request.setAttribute("altHistory", altHistory);

                            if (altHistory.size() > 1) {
                                session.setAttribute(SessionObjectUtility.ALT_GRAPH_POINTS, altHistory);
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
                                = PatientHistoryDataAccess.getAst(patientId);
                        ArrayList<CategoricalResult> notes
                                = PatientHistoryDataAccess.getNotes(patientId, astTopic,
                                        referenceCharacters);
                        request.setAttribute("notes", notes);
                        if (astHistory != null) {
                            request.setAttribute("astHistory", astHistory);

                            if (astHistory.size() > 1) {
                                session.setAttribute(SessionObjectUtility.AST_GRAPH_POINTS, astHistory);
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
                                = PatientHistoryDataAccess.getBP(patientId);
                        ArrayList<CategoricalResult> notes
                                = PatientHistoryDataAccess.getNotes(patientId, bpTopic,
                                        referenceCharacters);
                        request.setAttribute("notes", notes);
                        if (bpHistory != null) {
                            request.setAttribute("bpHistory", bpHistory);

                            if (bpHistory.size() > 1) {
                                session.setAttribute(SessionObjectUtility.BP_GRAPH_POINTS, bpHistory);
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
                                = PatientHistoryDataAccess.getBmi(patientId);
                        ArrayList<CategoricalResult> notes
                                = PatientHistoryDataAccess.getNotes(patientId, bmiTopic,
                                        referenceCharacters);
                        request.setAttribute("notes", notes);
                        if (bmiHistory != null) {
                            request.setAttribute("bmiHistory", bmiHistory);

                            if (bmiHistory.size() > 1) {
                                session.setAttribute(SessionObjectUtility.BMI_GRAPH_POINTS, bmiHistory);
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
                                = QualityDataAccess.getChecklistDates(patientId);
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
                                = PatientHistoryDataAccess.getClass(patientId);
                        ArrayList<CategoricalResult> notes
                                = PatientHistoryDataAccess.getNotes(patientId, classTopic,
                                        referenceCharacters);
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
                                = PatientHistoryDataAccess.getCreatinine(patientId);
                        ArrayList<CategoricalResult> notes
                                = PatientHistoryDataAccess.getNotes(patientId, creatinineTopic,
                                        referenceCharacters);
                        request.setAttribute("notes", notes);
                        if (creatinineHistory != null) {
                            request.setAttribute("creatinineHistory", creatinineHistory);

                            if (creatinineHistory.size() > 1) {
                                session.setAttribute(SessionObjectUtility.CREATININE_GRAPH_POINTS,
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
                                = PatientHistoryDataAccess.getEgfr(patientId);
                        ArrayList<CategoricalResult> notes
                                = PatientHistoryDataAccess.getNotes(patientId, egfrTopic,
                                        referenceCharacters);
                        request.setAttribute("notes", notes);
                        if (egfrHistory != null) {
                            request.setAttribute("egfrHistory", egfrHistory);

                            if (egfrHistory.size() > 1) {
                                session.setAttribute(SessionObjectUtility.EGFR_GRAPH_POINTS, egfrHistory);
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
                                = PatientHistoryDataAccess.getEye(patientId);
                        ArrayList<CategoricalResult> notes
                                = PatientHistoryDataAccess.getNotes(patientId, eyeTopic,
                                        referenceCharacters);
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
                                = PatientHistoryDataAccess.getFoot(patientId);
                        ArrayList<CategoricalResult> notes
                                = PatientHistoryDataAccess.getNotes(patientId, footTopic,
                                        referenceCharacters);
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
                                = PatientHistoryDataAccess.getGlucose(patientId);
                        ArrayList<CategoricalResult> notes
                                = PatientHistoryDataAccess.getNotes(patientId, glucoseTopic,
                                        referenceCharacters);
                        request.setAttribute("notes", notes);
                        if (glucoseHistory != null) {
                            request.setAttribute("glucoseHistory", glucoseHistory);

                            if (glucoseHistory.size() > 1) {
                                session.setAttribute(SessionObjectUtility.GLUCOSE_GRAPH_POINTS, glucoseHistory);
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
                                = PatientHistoryDataAccess.getHdl(patientId);
                        ArrayList<CategoricalResult> notes
                                = PatientHistoryDataAccess.getNotes(patientId, hdlTopic,
                                        referenceCharacters);
                        request.setAttribute("notes", notes);
                        if (hdlHistory != null) {
                            request.setAttribute("hdlHistory", hdlHistory);

                            if (hdlHistory.size() > 1) {
                                session.setAttribute(SessionObjectUtility.HDL_GRAPH_POINTS, hdlHistory);
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
                                = PatientHistoryDataAccess.getHepB(patientId);
                        ArrayList<CategoricalResult> notes
                                = PatientHistoryDataAccess.getNotes(patientId, hepbTopic,
                                        referenceCharacters);
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
                                = PatientHistoryDataAccess.getER(patientId);
                        ArrayList<CategoricalResult> notes
                                = PatientHistoryDataAccess.getNotes(patientId, erTopic,
                                        referenceCharacters);
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
                                = PatientHistoryDataAccess.getInfluenza(patientId);
                        ArrayList<CategoricalResult> notes
                                = PatientHistoryDataAccess.getNotes(patientId, fluTopic,
                                        referenceCharacters);
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
                                = PatientHistoryDataAccess.getLdl(patientId);
                        ArrayList<CategoricalResult> notes
                                = PatientHistoryDataAccess.getNotes(patientId, ldlTopic,
                                        referenceCharacters);
                        request.setAttribute("notes", notes);
                        if (ldlHistory != null) {
                            request.setAttribute("ldlHistory", ldlHistory);

                            if (ldlHistory.size() > 1) {
                                session.setAttribute(SessionObjectUtility.LDL_GRAPH_POINTS, ldlHistory);
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
                                = PatientHistoryDataAccess.getAllNotes(patientId,
                                        referenceCharacters);
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
                                = PatientHistoryDataAccess.getCompliance(patientId);
                        ArrayList<CategoricalResult> notes
                                = PatientHistoryDataAccess.getNotes(patientId, complianceTopic,
                                        referenceCharacters);
                        request.setAttribute("notes", notes);
                        if (complianceHistory != null) {
                            request.setAttribute("complianceHistory", complianceHistory);

                            if (complianceHistory.size() > 1) {
                                session.setAttribute(SessionObjectUtility.COMPLIANCE_GRAPH_POINTS, complianceHistory);
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
                                = PatientHistoryDataAccess.getPcv13(patientId);
                        ArrayList<CategoricalResult> notes
                                = PatientHistoryDataAccess.getNotes(patientId, pcv13Topic,
                                        referenceCharacters);
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
                                = PatientHistoryDataAccess.getPhysical(patientId);
                        ArrayList<CategoricalResult> notes
                                = PatientHistoryDataAccess.getNotes(patientId, physicalTopic,
                                        referenceCharacters);
                        request.setAttribute("notes", notes);
                        if (physicalActivityHistory != null) {
                            request.setAttribute("physicalActivityHistory",
                                    physicalActivityHistory);

                            if (physicalActivityHistory.size() > 1) {
                                session.setAttribute(SessionObjectUtility.PHYSICAL_ACTIVITY_GRAPH_POINTS,
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
                                = PatientHistoryDataAccess.getPpsv23(patientId);
                        ArrayList<CategoricalResult> notes
                                = PatientHistoryDataAccess.getNotes(patientId, ppsv23Topic,
                                        referenceCharacters);
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
                                = PatientHistoryDataAccess.getPsychological(patientId);
                        ArrayList<CategoricalResult> notes
                                = PatientHistoryDataAccess.getNotes(patientId, psycTopic,
                                        referenceCharacters);
                        request.setAttribute("notes", notes);
                        if (psychologicalHistory != null) {
                            request.setAttribute("psychologicalHistory", psychologicalHistory);

                            if (psychologicalHistory.size() > 1) {
                                session.setAttribute(SessionObjectUtility.PSYCHOLOGICAL_GRAPH_POINTS,
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
                                = PatientHistoryDataAccess.getSmoking(patientId);
                        ArrayList<CategoricalResult> notes
                                = PatientHistoryDataAccess.getNotes(patientId, smokingTopic,
                                        referenceCharacters);
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
                                = PatientHistoryDataAccess.getT4(patientId);
                        ArrayList<CategoricalResult> notes
                                = PatientHistoryDataAccess.getNotes(patientId, t4Topic,
                                        referenceCharacters);
                        request.setAttribute("notes", notes);
                        if (t4History != null) {
                            request.setAttribute("t4History", t4History);

                            if (t4History.size() > 1) {
                                session.setAttribute(SessionObjectUtility.T4_GRAPH_POINTS,
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
                                = PatientHistoryDataAccess.getTdap(patientId);
                        ArrayList<CategoricalResult> notes
                                = PatientHistoryDataAccess.getNotes(patientId, tdapTopic,
                                        referenceCharacters);
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
                                = PatientHistoryDataAccess.getTelephone(patientId);
                        ArrayList<CategoricalResult> notes
                                = PatientHistoryDataAccess.getNotes(patientId, telephoneTopic,
                                        referenceCharacters);
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
                                = PatientTreatmentDataAccess.getTreatments(patientId);
                        ArrayList<CategoricalResult> notes
                                = PatientHistoryDataAccess.getNotes(patientId, treatmentTopic,
                                        referenceCharacters);
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
                                = PatientHistoryDataAccess.getTriglycerides(patientId);
                        ArrayList<CategoricalResult> notes
                                = PatientHistoryDataAccess.getNotes(patientId, trigTopic,
                                        referenceCharacters);
                        request.setAttribute("notes", notes);
                        if (triglyceridesHistory != null) {
                            request.setAttribute("triglyceridesHistory",
                                    triglyceridesHistory);

                            if (triglyceridesHistory.size() > 1) {
                                session.setAttribute(SessionObjectUtility.TRIGLYCERIDES_GRAPH_POINTS,
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
                                = PatientHistoryDataAccess.getTsh(patientId);
                        ArrayList<CategoricalResult> notes
                                = PatientHistoryDataAccess.getNotes(patientId, tshTopic,
                                        referenceCharacters);
                        request.setAttribute("notes", notes);
                        if (tshHistory != null) {
                            request.setAttribute("tshHistory", tshHistory);

                            if (tshHistory.size() > 1) {
                                session.setAttribute(SessionObjectUtility.TSH_GRAPH_POINTS,
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
                                = PatientHistoryDataAccess.getUacr(patientId);
                        ArrayList<CategoricalResult> notes
                                = PatientHistoryDataAccess.getNotes(patientId, uacrTopic,
                                        referenceCharacters);
                        request.setAttribute("notes", notes);
                        if (uacrHistory != null) {
                            request.setAttribute("uacrHistory", uacrHistory);

                            if (uacrHistory.size() > 1) {
                                session.setAttribute(SessionObjectUtility.UACR_GRAPH_POINTS, uacrHistory);
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
                                = PatientHistoryDataAccess.getWaist(patientId);
                        ArrayList<CategoricalResult> notes
                                = PatientHistoryDataAccess.getNotes(patientId, waistTopic,
                                        referenceCharacters);
                        request.setAttribute("notes", notes);
                        if (waistHistory != null) {
                            request.setAttribute("waistHistory", waistHistory);

                            if (waistHistory.size() > 1) {
                                session.setAttribute(SessionObjectUtility.WAIST_GRAPH_POINTS,
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
                                = PatientHistoryDataAccess.getZoster(patientId);
                        ArrayList<CategoricalResult> notes
                                = PatientHistoryDataAccess.getNotes(patientId, zosterTopic,
                                        referenceCharacters);
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
                        dashboard = PatientHistoryDataAccess
                                .getPatientDashboard(patient.getPatientId(),
                                        referenceCharacters);
                        request.setAttribute("dashboard", dashboard);
                        request.setAttribute("statuslist", dashboard.getHts());
                        break;
                }
                break;
            case "qualityChecklist":
                boolean validData = true;
                String checklistSelect = request.getParameter("checklistSelect");
                Date checklistDate = null;
                if (StringUtility.dateCheck(checklistSelect)) {
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
                            = QualityDataAccess.getChecklistItems(patientId,
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
