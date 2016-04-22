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
package data;

import util.ConnectionPool;
import clinic.Clinic;
import clinic.EyeExamDefinition;
import clinic.FootExamRiskDefinition;
import clinic.HealthyTarget;
import clinic.HealthyTargetReference;
import clinic.Medication;
import clinic.PsychologicalScreeningReference;
import clinic.QualityReference;
import clinic.ReferenceContainer;
import clinic.TelephoneFollowUpDefinition;
import clinic.Therapy;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.DatabaseUtil;
import util.StringUtil;

/**
 * Accesses the database to retrieve the clinical reference values used
 * throughout the application
 *
 * @author Bryan Daniel
 * @version 1, April 8, 2016
 */
public class ReferencesIO {

    /**
     * Collects references from the database and returns them
     *
     * @return the reference container
     */
    public static ReferenceContainer getReferenceContainer() {
        ReferenceContainer rc = new ReferenceContainer();
        ArrayList<QualityReference> qualityReferences = new ArrayList<>();
        ArrayList<Therapy> therapies = new ArrayList<>();
        ArrayList<Medication> medications = new ArrayList<>();
        ArrayList<PsychologicalScreeningReference> psychologicalScreeningReferences
                = new ArrayList<>();
        ArrayList<TelephoneFollowUpDefinition> telephoneFollowUpDefinitions
                = new ArrayList<>();
        ArrayList<FootExamRiskDefinition> footExamRiskDefinitions
                = new ArrayList<>();
        ArrayList<EyeExamDefinition> eyeExamDefinitions = new ArrayList<>();
        ArrayList<Clinic> clinics = new ArrayList<>();
        ArrayList<String> noteTopics = new ArrayList<>();
        ArrayList<String> languages = new ArrayList<>();
        ArrayList<String> reasonsForInactivity = new ArrayList<>();
        ArrayList<String> emailMessageSubjects = new ArrayList<>();
        noteTopics.add("Patient");
        noteTopics.add("A1C");
        noteTopics.add("Glucose");
        noteTopics.add("LDL");
        noteTopics.add("HDL");
        noteTopics.add("Triglycerides");
        noteTopics.add("TSH");
        noteTopics.add("T4");
        noteTopics.add("UACR");
        noteTopics.add("eGFR");
        noteTopics.add("Creatinine");
        noteTopics.add("BMI");
        noteTopics.add("Waist");
        noteTopics.add("Blood Pressure");
        noteTopics.add("Class");
        noteTopics.add("Eye Screening");
        noteTopics.add("Foot Screening");
        noteTopics.add("Psychological Screening");
        noteTopics.add("Physical Activity");
        noteTopics.add("Influenza Vaccine");
        noteTopics.add("PCV-13 Vaccine");
        noteTopics.add("PPSV-23 Vaccine");
        noteTopics.add("Hepatitis B Vaccine");
        noteTopics.add("TDAP Vaccine");
        noteTopics.add("Zoster Vaccine");
        noteTopics.add("Smoking");
        noteTopics.add("Telephone Follow Up");
        noteTopics.add("AST");
        noteTopics.add("ALT");
        noteTopics.add("PSA");
        noteTopics.add("Compliance");
        noteTopics.add("Hospitalization");
        noteTopics.add("Treatment");
        noteTopics.add("Discharge Instructions");
        noteTopics.add("Other");
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        CallableStatement cs = null;
        ResultSet rs = null;

        try {
            cs = connection.prepareCall("{CALL getReferences(?)}");
            cs.registerOutParameter(1, java.sql.Types.INTEGER);

            /* quality */
            boolean success = cs.execute();
            if (!success) {
                return null;
            }

            rs = cs.getResultSet();
            while (rs.next()) {
                QualityReference qr = new QualityReference();
                qr.setRole(rs.getString("role"));
                qr.setResponsibility(rs.getString("responsibility"));
                qualityReferences.add(qr);
            }
            rc.setQualityReferences(qualityReferences);

            DatabaseUtil.closeResultSet(rs);

            /* therapies */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }

            rs = cs.getResultSet();
            while (rs.next()) {
                Therapy th = new Therapy();
                th.setPrescriptionClass(rs.getString("rx class"));
                th.setTherapyType(rs.getString("therapy type"));
                therapies.add(th);
            }
            rc.setTherapies(therapies);

            DatabaseUtil.closeResultSet(rs);

            /* medications */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }

            rs = cs.getResultSet();
            while (rs.next()) {
                Medication m = new Medication();
                m.setMedicationId(rs.getString("med id"));
                m.setMedicationName(rs.getString("med name"));
                m.setMedicationClass(rs.getString("med class"));
                medications.add(m);
            }
            rc.setMedications(medications);

            DatabaseUtil.closeResultSet(rs);

            /* psychological screening references */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }

            rs = cs.getResultSet();
            while (rs.next()) {
                PsychologicalScreeningReference psr = new PsychologicalScreeningReference();
                psr.setScore(rs.getInt("phq score"));
                psr.setSeverity(rs.getString("severity"));
                psr.setProposedActions(rs.getString("proposed actions"));
                psychologicalScreeningReferences.add(psr);
            }
            rc.setPsychologicalScreeningReferences(psychologicalScreeningReferences);

            DatabaseUtil.closeResultSet(rs);

            /* telephone follow-up definitions */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }

            rs = cs.getResultSet();
            while (rs.next()) {
                TelephoneFollowUpDefinition t = new TelephoneFollowUpDefinition();
                t.setCode(rs.getString("follow up code"));
                t.setDefinition(rs.getString("definition"));
                telephoneFollowUpDefinitions.add(t);
            }
            rc.setTelephoneFollowUpDefinitions(telephoneFollowUpDefinitions);

            DatabaseUtil.closeResultSet(rs);

            /* foot exam risk definitions */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }

            rs = cs.getResultSet();
            while (rs.next()) {
                FootExamRiskDefinition th = new FootExamRiskDefinition();
                th.setRiskCategory(rs.getString("risk category"));
                th.setDefinition(rs.getString("definition"));
                footExamRiskDefinitions.add(th);
            }
            rc.setFootExamRiskDefinitions(footExamRiskDefinitions);

            DatabaseUtil.closeResultSet(rs);

            /* eye exam definitions */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }

            rs = cs.getResultSet();
            while (rs.next()) {
                EyeExamDefinition e = new EyeExamDefinition();
                e.setCode(rs.getString("eye exam code"));
                e.setDefinition(rs.getString("definition"));
                eyeExamDefinitions.add(e);
            }
            rc.setEyeExamDefinitions(eyeExamDefinitions);

            DatabaseUtil.closeResultSet(rs);

            /* clinic IDs */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }

            rs = cs.getResultSet();
            while (rs.next()) {
                Clinic c = new Clinic();
                c.setClinicId(rs.getInt("clinic id"));
                c.setClinicName(rs.getString("clinic name"));
                clinics.add(c);
            }
            rc.setClinics(clinics);

            DatabaseUtil.closeResultSet(rs);

            /* note topics */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }

            rs = cs.getResultSet();
            while (rs.next()) {
                String s = rs.getString("topic");
                if (!noteTopics.contains(s)) {
                    noteTopics.add(s);
                }
            }
            StringUtil.sortStrings(noteTopics);
            rc.setNoteTopics(noteTopics);

            DatabaseUtil.closeResultSet(rs);

            /* languages */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }

            rs = cs.getResultSet();
            while (rs.next()) {
                String s = rs.getString("language");
                if (!languages.contains(s)) {
                    languages.add(s);
                }
            }
            StringUtil.sortStrings(languages);
            rc.setLanguages(languages);

            DatabaseUtil.closeResultSet(rs);

            /* reasons for inactivity */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }

            rs = cs.getResultSet();
            while (rs.next()) {
                String s = rs.getString("reason");
                if (!reasonsForInactivity.contains(s)) {
                    reasonsForInactivity.add(s);
                }
            }
            StringUtil.sortStrings(reasonsForInactivity);
            rc.setReasonsForInactivity(reasonsForInactivity);

            DatabaseUtil.closeResultSet(rs);

            /* email message subjects */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }

            rs = cs.getResultSet();
            while (rs.next()) {
                String s = rs.getString("subject");
                if (!emailMessageSubjects.contains(s)) {
                    emailMessageSubjects.add(s);
                }
            }
            StringUtil.sortStrings(emailMessageSubjects);
            rc.setEmailMessageSubjects(emailMessageSubjects);

            DatabaseUtil.closeResultSet(rs);

            /* healthy targets */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }

            HealthyTargetReference healthyTargets = new HealthyTargetReference();
            BigDecimal outOfRange = new BigDecimal(10000);
            rs = cs.getResultSet();
            while (rs.next()) {
                String s = rs.getString("measurement");
                switch (s) {
                    case "a1c": {
                        HealthyTarget ht = new HealthyTarget();
                        if (rs.getBigDecimal("upper").compareTo(outOfRange) != -1) {
                            ht.setUpperBound(null);
                        } else {
                            ht.setUpperBound(rs.getBigDecimal("upper"));
                        }
                        ht.setLowerBound(rs.getBigDecimal("lower"));
                        healthyTargets.setA1c(ht);
                        break;
                    }
                    case "alt": {
                        HealthyTarget ht = new HealthyTarget();
                        if (rs.getBigDecimal("upper").compareTo(outOfRange) != -1) {
                            ht.setUpperBound(null);
                        } else {
                            ht.setUpperBound(rs.getBigDecimal("upper"));
                        }
                        ht.setLowerBound(rs.getBigDecimal("lower"));
                        healthyTargets.setAlt(ht);
                        break;
                    }
                    case "ast": {
                        HealthyTarget ht = new HealthyTarget();
                        if (rs.getBigDecimal("upper").compareTo(outOfRange) != -1) {
                            ht.setUpperBound(null);
                        } else {
                            ht.setUpperBound(rs.getBigDecimal("upper"));
                        }
                        ht.setLowerBound(rs.getBigDecimal("lower"));
                        healthyTargets.setAst(ht);
                        break;
                    }
                    case "bloodpressurediastole": {
                        HealthyTarget ht = new HealthyTarget();
                        if (rs.getBigDecimal("upper").compareTo(outOfRange) != -1) {
                            ht.setUpperBound(null);
                        } else {
                            ht.setUpperBound(rs.getBigDecimal("upper"));
                        }
                        ht.setLowerBound(rs.getBigDecimal("lower"));
                        healthyTargets.setBloodPressureDiastole(ht);
                        break;
                    }
                    case "bloodpressuresystole": {
                        HealthyTarget ht = new HealthyTarget();
                        if (rs.getBigDecimal("upper").compareTo(outOfRange) != -1) {
                            ht.setUpperBound(null);
                        } else {
                            ht.setUpperBound(rs.getBigDecimal("upper"));
                        }
                        ht.setLowerBound(rs.getBigDecimal("lower"));
                        healthyTargets.setBloodPressureSystole(ht);
                        break;
                    }
                    case "bmi": {
                        HealthyTarget ht = new HealthyTarget();
                        if (rs.getBigDecimal("upper").compareTo(outOfRange) != -1) {
                            ht.setUpperBound(null);
                        } else {
                            ht.setUpperBound(rs.getBigDecimal("upper"));
                        }
                        ht.setLowerBound(rs.getBigDecimal("lower"));
                        healthyTargets.setBmi(ht);
                        break;
                    }
                    case "creatinine": {
                        HealthyTarget ht = new HealthyTarget();
                        if (rs.getBigDecimal("upper").compareTo(outOfRange) != -1) {
                            ht.setUpperBound(null);
                        } else {
                            ht.setUpperBound(rs.getBigDecimal("upper"));
                        }
                        ht.setLowerBound(rs.getBigDecimal("lower"));
                        healthyTargets.setCreatinine(ht);
                        break;
                    }
                    case "egfr": {
                        HealthyTarget ht = new HealthyTarget();
                        if (rs.getBigDecimal("upper").compareTo(outOfRange) != -1) {
                            ht.setUpperBound(null);
                        } else {
                            ht.setUpperBound(rs.getBigDecimal("upper"));
                        }
                        ht.setLowerBound(rs.getBigDecimal("lower"));
                        healthyTargets.setEgfr(ht);
                        break;
                    }
                    case "glucoseac": {
                        HealthyTarget ht = new HealthyTarget();
                        if (rs.getBigDecimal("upper").compareTo(outOfRange) != -1) {
                            ht.setUpperBound(null);
                        } else {
                            ht.setUpperBound(rs.getBigDecimal("upper"));
                        }
                        ht.setLowerBound(rs.getBigDecimal("lower"));
                        healthyTargets.setGlucoseAc(ht);
                        break;
                    }
                    case "hdlfemale": {
                        HealthyTarget ht = new HealthyTarget();
                        if (rs.getBigDecimal("upper").compareTo(outOfRange) != -1) {
                            ht.setUpperBound(null);
                        } else {
                            ht.setUpperBound(rs.getBigDecimal("upper"));
                        }
                        ht.setLowerBound(rs.getBigDecimal("lower"));
                        healthyTargets.setHdlFemale(ht);
                        break;
                    }
                    case "hdlmale": {
                        HealthyTarget ht = new HealthyTarget();
                        if (rs.getBigDecimal("upper").compareTo(outOfRange) != -1) {
                            ht.setUpperBound(null);
                        } else {
                            ht.setUpperBound(rs.getBigDecimal("upper"));
                        }
                        ht.setLowerBound(rs.getBigDecimal("lower"));
                        healthyTargets.setHdlMale(ht);
                        break;
                    }
                    case "ldl": {
                        HealthyTarget ht = new HealthyTarget();
                        if (rs.getBigDecimal("upper").compareTo(outOfRange) != -1) {
                            ht.setUpperBound(null);
                        } else {
                            ht.setUpperBound(rs.getBigDecimal("upper"));
                        }
                        ht.setLowerBound(rs.getBigDecimal("lower"));
                        healthyTargets.setLdl(ht);
                        break;
                    }
                    case "physicalactivity": {
                        HealthyTarget ht = new HealthyTarget();
                        if (rs.getBigDecimal("upper").compareTo(outOfRange) != -1) {
                            ht.setUpperBound(null);
                        } else {
                            ht.setUpperBound(rs.getBigDecimal("upper"));
                        }
                        ht.setLowerBound(rs.getBigDecimal("lower"));
                        healthyTargets.setPhysicalActivity(ht);
                        break;
                    }
                    case "psa": {
                        HealthyTarget ht = new HealthyTarget();
                        if (rs.getBigDecimal("upper").compareTo(outOfRange) != -1) {
                            ht.setUpperBound(null);
                        } else {
                            ht.setUpperBound(rs.getBigDecimal("upper"));
                        }
                        ht.setLowerBound(rs.getBigDecimal("lower"));
                        healthyTargets.setPsa(ht);
                        break;
                    }
                    case "t4": {
                        HealthyTarget ht = new HealthyTarget();
                        if (rs.getBigDecimal("upper").compareTo(outOfRange) != -1) {
                            ht.setUpperBound(null);
                        } else {
                            ht.setUpperBound(rs.getBigDecimal("upper"));
                        }
                        ht.setLowerBound(rs.getBigDecimal("lower"));
                        healthyTargets.setT4(ht);
                        break;
                    }
                    case "triglycerides": {
                        HealthyTarget ht = new HealthyTarget();
                        if (rs.getBigDecimal("upper").compareTo(outOfRange) != -1) {
                            ht.setUpperBound(null);
                        } else {
                            ht.setUpperBound(rs.getBigDecimal("upper"));
                        }
                        ht.setLowerBound(rs.getBigDecimal("lower"));
                        healthyTargets.setTriglycerides(ht);
                        break;
                    }
                    case "tsh": {
                        HealthyTarget ht = new HealthyTarget();
                        if (rs.getBigDecimal("upper").compareTo(outOfRange) != -1) {
                            ht.setUpperBound(null);
                        } else {
                            ht.setUpperBound(rs.getBigDecimal("upper"));
                        }
                        ht.setLowerBound(rs.getBigDecimal("lower"));
                        healthyTargets.setTsh(ht);
                        break;
                    }
                    case "uacr": {
                        HealthyTarget ht = new HealthyTarget();
                        if (rs.getBigDecimal("upper").compareTo(outOfRange) != -1) {
                            ht.setUpperBound(null);
                        } else {
                            ht.setUpperBound(rs.getBigDecimal("upper"));
                        }
                        ht.setLowerBound(rs.getBigDecimal("lower"));
                        healthyTargets.setUacr(ht);
                        break;
                    }
                    case "waistfemale": {
                        HealthyTarget ht = new HealthyTarget();
                        if (rs.getBigDecimal("upper").compareTo(outOfRange) != -1) {
                            ht.setUpperBound(null);
                        } else {
                            ht.setUpperBound(rs.getBigDecimal("upper"));
                        }
                        ht.setLowerBound(rs.getBigDecimal("lower"));
                        healthyTargets.setWaistFemale(ht);
                        break;
                    }
                    case "waistmale": {
                        HealthyTarget ht = new HealthyTarget();
                        if (rs.getBigDecimal("upper").compareTo(outOfRange) != -1) {
                            ht.setUpperBound(null);
                        } else {
                            ht.setUpperBound(rs.getBigDecimal("upper"));
                        }
                        ht.setLowerBound(rs.getBigDecimal("lower"));
                        healthyTargets.setWaistMale(ht);
                        break;
                    }
                    default:
                        break;
                }
            }
            rc.setHealthyTargets(healthyTargets);

        } catch (SQLException ex) {
            Logger.getLogger(ReferencesIO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DatabaseUtil.closeResultSet(rs);
            DatabaseUtil.closeCallableStatement(cs);
            pool.freeConnection(connection);
        }

        return rc;
    }
}
