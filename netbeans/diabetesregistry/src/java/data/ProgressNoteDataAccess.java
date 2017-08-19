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

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import registry.NoteAuthor;
import registry.ProgressNote;
import utility.ConnectionPool;
import utility.DatabaseUtility;

/**
 * This data-access class connects to the database to execute progress note
 * functions.
 *
 * @author Bryan Daniel
 * @version 2, March 16, 2017
 */
public class ProgressNoteDataAccess {

    /**
     * This method returns the progress note of a given patient for a given
     * date.
     *
     * @param patientId the patient ID
     * @param dateCreated the date the progress note was created
     * @param referenceCharacters the character string
     * @return the progress note or null if results are missing
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
                progressNote.setBloodPressureSystole(rs.getInt("bp systole"));
                if (rs.wasNull()) {
                    progressNote.setBloodPressureSystole(null);
                }
                progressNote.setBloodPressureDiastole(rs.getInt("bp diastole"));
                if (rs.wasNull()) {
                    progressNote.setBloodPressureDiastole(null);
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

            DatabaseUtility.closeResultSet(rs);

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

            DatabaseUtility.closeResultSet(rs);

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
            Logger.getLogger(ProgressNoteDataAccess.class.getName()).log(Level.SEVERE,
                    "An exception occurred during the getProgressNote method.", ex);
            return null;
        } finally {
            DatabaseUtility.closeResultSet(rs);
            DatabaseUtility.closeCallableStatement(cs);
            pool.freeConnection(connection);
        }
        return progressNote;
    }

    /**
     * This method receives the ProgressNote object as a parameter and accesses
     * the variables in the object to pass to the stored procedure,
     * saveProgressNote, saving the patient's progress note information to the
     * database.
     *
     * @param noteToSave the progress note to be saved
     * @param updatedBy the registry user
     * @param timestamp the timestamp of the note update
     * @param clinicId the clinic ID
     * @param referenceCharacters the character string
     * @return the boolean result of the save operation
     */
    public static boolean saveProgressNote(ProgressNote noteToSave, String updatedBy,
            Timestamp timestamp, int clinicId, Object referenceCharacters) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        CallableStatement cs = null;

        try {
            cs = connection.prepareCall("{CALL saveProgressNote(?, ?, ?, ?, ?, ?, "
                    + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
                    + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");

            /* #1 patient ID */
            cs.setInt(1, noteToSave.getPatient().getPatientId());

            /* #2 date created */
            cs.setDate(2, noteToSave.getDateCreated());

            /* #3 medical insurance */
            cs.setBoolean(3, noteToSave.getMedicalInsurance());

            /* #4 shoe size */
            if ((noteToSave.getShoeSize() != null)
                    && (noteToSave.getShoeSize().trim().length() != 0)) {
                cs.setString(4, noteToSave.getShoeSize());
            } else {
                cs.setNull(4, java.sql.Types.VARCHAR);
            }

            /* #5 allergic to medications */
            cs.setBoolean(5, noteToSave.getAllergicToMedications());

            /* #6 allergies */
            if ((noteToSave.getAllergies() != null)
                    && (noteToSave.getAllergies().trim().length() != 0)) {
                cs.setString(6, noteToSave.getAllergies());
            } else {
                cs.setNull(6, java.sql.Types.VARCHAR);
            }

            /* #7 weight */
            if (noteToSave.getWeight() != null) {
                cs.setBigDecimal(7, noteToSave.getWeight());
            } else {
                cs.setNull(7, java.sql.Types.DECIMAL);
            }

            /* #8 height in feet */
            if (noteToSave.getHeightFeet() != null) {
                cs.setInt(8, noteToSave.getHeightFeet());
            } else {
                cs.setNull(8, java.sql.Types.INTEGER);
            }

            /* #9 the inches component of height */
            if (noteToSave.getHeightInches() != null) {
                cs.setInt(9, noteToSave.getHeightInches());
            } else {
                cs.setNull(9, java.sql.Types.INTEGER);
            }

            /* #10 weight reduction goal */
            if (noteToSave.getWeightReductionGoal() != null) {
                cs.setBigDecimal(10, noteToSave.getWeightReductionGoal());
            } else {
                cs.setNull(10, java.sql.Types.DECIMAL);
            }

            /* #11 pulse */
            if (noteToSave.getPulse() != null) {
                cs.setInt(11, noteToSave.getPulse());
            } else {
                cs.setNull(11, java.sql.Types.INTEGER);
            }

            /* #12 respirations */
            if (noteToSave.getRespirations() != null) {
                cs.setInt(12, noteToSave.getRespirations());
            } else {
                cs.setNull(12, java.sql.Types.INTEGER);
            }

            /* #13 temperature */
            if (noteToSave.getTemperature() != null) {
                cs.setBigDecimal(13, noteToSave.getTemperature());
            } else {
                cs.setNull(13, java.sql.Types.DECIMAL);
            }

            /* #14 foot screening */
            cs.setBoolean(14, noteToSave.getFootScreening());

            /* #15 medications */
            if ((noteToSave.getMedications() != null)
                    && (noteToSave.getMedications().trim().length() != 0)) {
                cs.setString(15, noteToSave.getMedications());
            } else {
                cs.setNull(15, java.sql.Types.VARCHAR);
            }

            /* #16 A1C */
            if (noteToSave.getA1c() != null) {
                cs.setBigDecimal(16, noteToSave.getA1c());
            } else {
                cs.setNull(16, java.sql.Types.DECIMAL);
            }

            /* #17 glucose */
            if (noteToSave.getGlucose() != null) {
                cs.setBigDecimal(17, noteToSave.getGlucose());
            } else {
                cs.setNull(17, java.sql.Types.DECIMAL);
            }

            /* #18 waist */
            if (noteToSave.getWaist() != null) {
                cs.setBigDecimal(18, noteToSave.getWaist());
            } else {
                cs.setNull(18, java.sql.Types.DECIMAL);
            }

            /* #19 systolic BP */
            if (noteToSave.getBloodPressureSystole() != null) {
                cs.setInt(19, noteToSave.getBloodPressureSystole());
            } else {
                cs.setNull(19, java.sql.Types.INTEGER);
            }

            /* #20 diastolic BP */
            if (noteToSave.getBloodPressureDiastole() != null) {
                cs.setInt(20, noteToSave.getBloodPressureDiastole());
            } else {
                cs.setNull(20, java.sql.Types.INTEGER);
            }

            /* #21 ACE or ARB */
            cs.setBoolean(21, noteToSave.getAceOrArb());

            /* #22 BMI */
            if (noteToSave.getBmi() != null) {
                cs.setBigDecimal(22, noteToSave.getBmi());
            } else {
                cs.setNull(22, java.sql.Types.DECIMAL);
            }

            /* #23 class date */
            if (noteToSave.getLastClassDate() != null) {
                cs.setDate(23, noteToSave.getLastClassDate());
            } else {
                cs.setNull(23, java.sql.Types.DATE);
            }

            /* #24 eye result */
            if (noteToSave.getEyeScreeningCategory() != null) {
                cs.setString(24, noteToSave.getEyeScreeningCategory());
            } else {
                cs.setNull(24, java.sql.Types.VARCHAR);
            }

            /* #25 foot result */
            if (noteToSave.getFootScreeningCategory() != null) {
                cs.setString(25, noteToSave.getFootScreeningCategory());
            } else {
                cs.setNull(25, java.sql.Types.VARCHAR);
            }

            /* #26 psychological screening */
            if (noteToSave.getPsychologicalScreening() != null) {
                cs.setInt(26, noteToSave.getPsychologicalScreening());
            } else {
                cs.setNull(26, java.sql.Types.INTEGER);
            }

            /* #27 physical activity */
            if (noteToSave.getPhysicalActivity() != null) {
                cs.setInt(27, noteToSave.getPhysicalActivity());
            } else {
                cs.setNull(27, java.sql.Types.INTEGER);
            }

            /* #28 smoking status */
            if (noteToSave.getSmoking() != null) {
                cs.setBoolean(28, noteToSave.getSmoking());
            } else {
                cs.setNull(28, java.sql.Types.TINYINT);
            }

            /* #29 compliance */
            if (noteToSave.getCompliance() != null) {
                cs.setBigDecimal(29, noteToSave.getCompliance());
            } else {
                cs.setNull(29, java.sql.Types.DECIMAL);
            }

            /* #30 ER date */
            if (noteToSave.getHospitalizationDate() != null) {
                cs.setDate(30, noteToSave.getHospitalizationDate());
            } else {
                cs.setNull(30, java.sql.Types.DATE);
            }

            /* #31 nurse/dietitian note */
            if ((noteToSave.getNurseOrDietitianNote() != null)
                    && (noteToSave.getNurseOrDietitianNote().trim().length() != 0)) {
                cs.setString(31, noteToSave.getNurseOrDietitianNote());
            } else {
                cs.setNull(31, java.sql.Types.VARCHAR);
            }

            /* #32 subjective section */
            if ((noteToSave.getSubjective() != null)
                    && (noteToSave.getSubjective().trim().length() != 0)) {
                cs.setString(32, noteToSave.getSubjective());
            } else {
                cs.setNull(32, java.sql.Types.VARCHAR);
            }

            /* #33 objective section */
            if ((noteToSave.getObjective() != null)
                    && (noteToSave.getObjective().trim().length() != 0)) {
                cs.setString(33, noteToSave.getObjective());
            } else {
                cs.setNull(33, java.sql.Types.VARCHAR);
            }

            /* #34 assessment section */
            if ((noteToSave.getAssessment() != null)
                    && (noteToSave.getAssessment().trim().length() != 0)) {
                cs.setString(34, noteToSave.getAssessment());
            } else {
                cs.setNull(34, java.sql.Types.VARCHAR);
            }

            /* #35 plan */
            if ((noteToSave.getPlan() != null)
                    && (noteToSave.getPlan().trim().length() != 0)) {
                cs.setString(35, noteToSave.getPlan());
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
            cs.setInt(39, clinicId);

            cs.registerOutParameter(40, java.sql.Types.TINYINT);

            /* no result set to return */
            cs.execute();

            /* grab out parameter */
            boolean success = cs.getBoolean(40);
            if (!success) {
                return false;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProgressNoteDataAccess.class.getName()).log(Level.SEVERE,
                    "An exception occurred during the saveProgressNote method.", ex);
            return false;
        } finally {
            DatabaseUtility.closeCallableStatement(cs);
            pool.freeConnection(connection);
        }
        return true;
    }

    /**
     * This method retrieves the list of dates for a patient's progress notes.
     *
     * @param patientId the patient ID
     * @return the list of progress note dates or null if results are missing
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
            Logger.getLogger(ProgressNoteDataAccess.class.getName()).log(Level.SEVERE,
                    "An exception occurred during the getProgressDates method.", ex);
            return null;
        } finally {
            DatabaseUtility.closeResultSet(rs);
            DatabaseUtility.closeCallableStatement(cs);
            pool.freeConnection(connection);
        }
        return progressDates;
    }
}
