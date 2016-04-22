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
import clinic.DataEntryContainer;
import clinic.NoteAuthor;
import clinic.ProgressNote;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.DatabaseUtil;

/**
 * Accesses the database during the progress note functions
 *
 * @author Bryan Daniel
 * @version 1, April 8, 2016
 */
public class ProgressNoteIO {

    /**
     * Returns the progress note of a given patient for a given date
     *
     * @param patientId the patient ID
     * @param dateCreated the date the progress note was created
     * @param referenceCharacters the character string
     * @return the list of patients
     */
    public static ProgressNote getProgressNote(int patientId,
            Date dateCreated, Object referenceCharacters) {
        ProgressNote progressNote = new ProgressNote();
        ArrayList<NoteAuthor> noteAuthors = new ArrayList<>();
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        CallableStatement cs = null;
        ResultSet rs = null;

        try {
            cs = connection.prepareCall("{CALL getProgressNote(?, ?, ?, ?)}");
            cs.setInt(1, patientId);
            cs.setDate(2, dateCreated);
            cs.setString(3, (String) referenceCharacters);
            cs.registerOutParameter(4, java.sql.Types.INTEGER);

            /* reads true if result set exists */
            boolean success = cs.execute();
            if (!success) {
                return null;
            }

            /* progress note */
            rs = cs.getResultSet();

            while (rs.next()) {
                progressNote.setMedicalInsurance(rs.getBoolean("medical insurance"));
                progressNote.setShoeSize(rs.getString("shoe size"));
                progressNote.setAllergicToMedications(rs.getBoolean("allergic to medications"));
                progressNote.setAllergies(rs.getString("allergies"));
                progressNote.setWeight(rs.getBigDecimal("weight"));
                progressNote.setHeightFeet(rs.getInt("height feet"));
                if (rs.wasNull()) {
                    progressNote.setHeightFeet(null);
                }
                progressNote.setHeightInches(rs.getInt("height inches"));
                if (rs.wasNull()) {
                    progressNote.setHeightInches(null);
                }
                progressNote.setWeightReductionGoal(rs.getBigDecimal("weight reduction goal"));
                progressNote.setPulse(rs.getInt("pulse"));
                if (rs.wasNull()) {
                    progressNote.setPulse(null);
                }
                progressNote.setRespirations(rs.getInt("respirations"));
                if (rs.wasNull()) {
                    progressNote.setRespirations(null);
                }
                progressNote.setTemperature(rs.getBigDecimal("temperature"));
                progressNote.setFootScreening(rs.getBoolean("foot screening"));
                progressNote.setMedications(rs.getString("medications"));
                progressNote.setA1c(rs.getBigDecimal("a1c"));
                progressNote.setGlucose(rs.getBigDecimal("glucose"));
                progressNote.setWaist(rs.getBigDecimal("waist"));
                progressNote.setBpSystole(rs.getInt("bp systole"));
                if (rs.wasNull()) {
                    progressNote.setBpSystole(null);
                }
                progressNote.setBpDiastole(rs.getInt("bp diastole"));
                if (rs.wasNull()) {
                    progressNote.setBpDiastole(null);
                }
                progressNote.setAceOrArb(rs.getBoolean("ace or arb"));
                progressNote.setBmi(rs.getBigDecimal("bmi"));
                progressNote.setEyeScreeningCategory(rs.getString("eye exam code"));
                progressNote.setFootScreeningCategory(rs.getString("risk category"));
                progressNote.setPsychologicalScreening(rs.getInt("phq score"));
                if (rs.wasNull()) {
                    progressNote.setPsychologicalScreening(null);
                }
                progressNote.setSmoking(rs.getBoolean("smoker"));
                progressNote.setCompliance(rs.getBigDecimal("compliance"));
                progressNote.setPhysicalActivity(rs.getInt("physical activity"));
                if (rs.wasNull()) {
                    progressNote.setPhysicalActivity(null);
                }
                progressNote.setNurseOrDietitianNote(rs.getString("nurse or dietitian note"));
                progressNote.setSubjective(rs.getString("subjective"));
                progressNote.setObjective(rs.getString("objective"));
                progressNote.setAssessment(rs.getString("assessment"));
                progressNote.setPlan(rs.getString("plan"));
            }

            DatabaseUtil.closeResultSet(rs);

            /* last class date */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }

            rs = cs.getResultSet();
            while (rs.next()) {
                progressNote.setLastClassDate(rs.getDate("last class date"));
                if (rs.wasNull()) {
                    progressNote.setLastClassDate(null);
                }
            }

            DatabaseUtil.closeResultSet(rs);

            /* note authors */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }

            rs = cs.getResultSet();
            while (rs.next()) {
                NoteAuthor na = new NoteAuthor();
                na.setFirstName(rs.getString("first name"));
                na.setLastName(rs.getString("last name"));
                na.setJobTitle(rs.getString("job title"));
                na.setTimeStamp(rs.getTimestamp("datetime recorded"));
                noteAuthors.add(na);
            }
            progressNote.setUpdatedBy(noteAuthors);

        } catch (SQLException ex) {
            Logger.getLogger(ReferencesIO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DatabaseUtil.closeResultSet(rs);
            DatabaseUtil.closeCallableStatement(cs);
            pool.freeConnection(connection);
        }

        return progressNote;
    }

    /**
     * This method saves the patient's progress note to the database.
     *
     * @param patientId the patient ID
     * @param dateCreated the date the note was created
     * @param medicalInsurance medical insurance status
     * @param shoeSize shoe size
     * @param allergicToMedications allergic to medications status
     * @param allergies allergies
     * @param weight the patient's weight
     * @param heightFeet the feet of the patient's height
     * @param heightInches the inches of the patient's height
     * @param weightReductionGoal the goal for weight reduction
     * @param pulse the pulse
     * @param respirations the respirations count
     * @param temperature the temperature
     * @param footScreening the foot screening status
     * @param medications the medications
     * @param a1c the A1C measurement
     * @param glucose the glucose measurement
     * @param dec the data entry container
     * @param nurseOrDietitianNote the note from the nurse or dietitian
     * @param subjective the subjective note
     * @param objective the objective note
     * @param assessment the assessment note
     * @param plan the plan note
     * @param updatedBy the registry user
     * @param timestamp the timestamp of the note update
     * @param referenceCharacters the character string
     * @return the status of the save operation
     */
    public static boolean saveProgressNote(int patientId, Date dateCreated,
            boolean medicalInsurance, String shoeSize,
            boolean allergicToMedications, String allergies, BigDecimal weight,
            Integer heightFeet, Integer heightInches, BigDecimal weightReductionGoal,
            Integer pulse, Integer respirations, BigDecimal temperature,
            boolean footScreening, String medications, BigDecimal a1c,
            BigDecimal glucose, DataEntryContainer dec, String nurseOrDietitianNote,
            String subjective, String objective, String assessment, String plan,
            String updatedBy, Timestamp timestamp,
            Object referenceCharacters) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        CallableStatement cs = null;

        try {
            cs = connection.prepareCall("{CALL saveProgressNote(?, ?, ?, ?, ?, ?, "
                    + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
                    + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");

            /* #1 patient ID */
            cs.setInt(1, patientId);

            /* #2 date created */
            cs.setDate(2, dateCreated);

            /* #3 medical insurance */
            cs.setBoolean(3, medicalInsurance);

            /* #4 shoe size */
            if ((shoeSize != null) && (shoeSize.trim().length() != 0)) {
                cs.setString(4, shoeSize);
            } else {
                cs.setNull(4, java.sql.Types.VARCHAR);
            }

            /* #5 allergic to medications */
            cs.setBoolean(5, allergicToMedications);

            /* #6 allergies */
            if ((allergies != null) && (allergies.trim().length() != 0)) {
                cs.setString(6, allergies);
            } else {
                cs.setNull(6, java.sql.Types.VARCHAR);
            }

            /* #7 weight */
            if (weight != null) {
                cs.setBigDecimal(7, weight);
            } else {
                cs.setNull(7, java.sql.Types.DECIMAL);
            }

            /* #8 height in feet */
            if (heightFeet != null) {
                cs.setInt(8, heightFeet);
            } else {
                cs.setNull(8, java.sql.Types.INTEGER);
            }

            /* #9 the inches component of height */
            if (heightInches != null) {
                cs.setInt(9, heightInches);
            } else {
                cs.setNull(9, java.sql.Types.INTEGER);
            }

            /* #10 weight reduction goal */
            if (weightReductionGoal != null) {
                cs.setBigDecimal(10, weightReductionGoal);
            } else {
                cs.setNull(10, java.sql.Types.DECIMAL);
            }

            /* #11 pulse */
            if (pulse != null) {
                cs.setInt(11, pulse);
            } else {
                cs.setNull(11, java.sql.Types.INTEGER);
            }

            /* #12 respirations */
            if (respirations != null) {
                cs.setInt(12, respirations);
            } else {
                cs.setNull(12, java.sql.Types.INTEGER);
            }

            /* #13 temperature */
            if (temperature != null) {
                cs.setBigDecimal(13, temperature);
            } else {
                cs.setNull(13, java.sql.Types.DECIMAL);
            }

            /* #14 foot screening */
            cs.setBoolean(14, footScreening);

            /* #15 medications */
            if ((medications != null) && (medications.trim().length() != 0)) {
                cs.setString(15, medications);
            } else {
                cs.setNull(15, java.sql.Types.VARCHAR);
            }

            /* #16 A1C */
            if (a1c != null) {
                cs.setBigDecimal(16, a1c);
            } else {
                cs.setNull(16, java.sql.Types.DECIMAL);
            }

            /* #17 glucose */
            if (glucose != null) {
                cs.setBigDecimal(17, glucose);
            } else {
                cs.setNull(17, java.sql.Types.DECIMAL);
            }

            /* #18 waist */
            if (dec.getWaist() != null) {
                cs.setBigDecimal(18, dec.getWaist());
            } else {
                cs.setNull(18, java.sql.Types.DECIMAL);
            }

            /* #19 systolic BP */
            if (dec.getBloodPressureSystole() != null) {
                cs.setInt(19, dec.getBloodPressureSystole());
            } else {
                cs.setNull(19, java.sql.Types.INTEGER);
            }

            /* #20 diastolic BP */
            if (dec.getBloodPressureDiastole() != null) {
                cs.setInt(20, dec.getBloodPressureDiastole());
            } else {
                cs.setNull(20, java.sql.Types.INTEGER);
            }

            /* #21 ACE or ARB */
            if (dec.getAceOrArb() != null) {
                cs.setBoolean(21, dec.getAceOrArb());
            } else {
                cs.setNull(21, java.sql.Types.TINYINT);
            }

            /* #22 BMI */
            if (dec.getBmi() != null) {
                cs.setBigDecimal(22, dec.getBmi());
            } else {
                cs.setNull(22, java.sql.Types.DECIMAL);
            }

            /* #23 class date */
            if (dec.getClassDate() != null) {
                cs.setDate(23, dec.getClassDate());
            } else {
                cs.setNull(23, java.sql.Types.DATE);
            }

            /* #24 eye result */
            if (dec.getEye() != null) {
                cs.setString(24, dec.getEye());
            } else {
                cs.setNull(24, java.sql.Types.VARCHAR);
            }

            /* #25 foot result */
            if (dec.getFoot() != null) {
                cs.setString(25, dec.getFoot());
            } else {
                cs.setNull(25, java.sql.Types.VARCHAR);
            }

            /* #26 psychological screening */
            if (dec.getPsychologicalScreening() != null) {
                cs.setInt(26, dec.getPsychologicalScreening());
            } else {
                cs.setNull(26, java.sql.Types.INTEGER);
            }

            /* #27 physical activity */
            if (dec.getPhysicalActivity() != null) {
                cs.setInt(27, dec.getPhysicalActivity());
            } else {
                cs.setNull(27, java.sql.Types.INTEGER);
            }

            /* #28 smoking status */
            if (dec.getSmoking() != null) {
                cs.setBoolean(28, dec.getSmoking());
            } else {
                cs.setNull(28, java.sql.Types.TINYINT);
            }

            /* #29 compliance */
            if (dec.getCompliance() != null) {
                cs.setBigDecimal(29, dec.getCompliance());
            } else {
                cs.setNull(29, java.sql.Types.DECIMAL);
            }

            /* #30 ER date */
            if (dec.getHospitalizationDate() != null) {
                cs.setDate(30, dec.getHospitalizationDate());
            } else {
                cs.setNull(30, java.sql.Types.DATE);
            }

            /* #31 nurse/dietitian note */
            if ((nurseOrDietitianNote != null) && (nurseOrDietitianNote.trim().length() != 0)) {
                cs.setString(31, nurseOrDietitianNote);
            } else {
                cs.setNull(31, java.sql.Types.VARCHAR);
            }

            /* #32 subjective section */
            if ((subjective != null) && (subjective.trim().length() != 0)) {
                cs.setString(32, subjective);
            } else {
                cs.setNull(32, java.sql.Types.VARCHAR);
            }

            /* #33 objective section */
            if ((objective != null) && (objective.trim().length() != 0)) {
                cs.setString(33, objective);
            } else {
                cs.setNull(33, java.sql.Types.VARCHAR);
            }

            /* #34 assessment section */
            if ((assessment != null) && (assessment.trim().length() != 0)) {
                cs.setString(34, assessment);
            } else {
                cs.setNull(34, java.sql.Types.VARCHAR);
            }

            /* #35 plan */
            if ((plan != null) && (plan.trim().length() != 0)) {
                cs.setString(35, plan);
            } else {
                cs.setNull(35, java.sql.Types.VARCHAR);
            }

            /* #36 user */
            cs.setString(36, updatedBy);

            /* #37 date and time */
            cs.setTimestamp(37, timestamp);

            /* #38 character string */
            cs.setString(38, (String) referenceCharacters);

            /* #39 clinic ID */
            cs.setInt(39, dec.getClinicId());

            cs.registerOutParameter(40, java.sql.Types.TINYINT);

            /* no result set to return */
            cs.execute();

            /* grab out parameter */
            boolean success = cs.getBoolean(40);
            if (!success) {
                return false;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ReferencesIO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DatabaseUtil.closeCallableStatement(cs);
            pool.freeConnection(connection);
        }
        return true;
    }

    /**
     * Retrieves the list of dates for a patient's progress notes
     *
     * @param patientId the patient ID
     * @return the boolean for update success
     */
    public static ArrayList<Date> getProgressDates(int patientId) {
        ArrayList<Date> progressDates = new ArrayList<>();
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        CallableStatement cs = null;
        ResultSet rs = null;

        try {
            cs = connection.prepareCall("{CALL getProgressDates(?, ?)}");
            cs.setInt(1, patientId);
            cs.registerOutParameter(2, java.sql.Types.TINYINT);

            /* reads true if result set exists */
            boolean success = cs.execute();
            if (!success) {
                return null;
            }

            rs = cs.getResultSet();
            while (rs.next()) {
                Date progressDate = rs.getDate("date created");
                progressDates.add(progressDate);
            }

        } catch (SQLException ex) {
            Logger.getLogger(ReferencesIO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DatabaseUtil.closeResultSet(rs);
            DatabaseUtil.closeCallableStatement(cs);
            pool.freeConnection(connection);
        }
        return progressDates;
    }
}
