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

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import registry.A1cResult;
import registry.BloodPressureResult;
import registry.BooleanResult;
import registry.CategoricalResult;
import registry.ContinuousResult;
import registry.Dashboard;
import registry.DiscreteResult;
import registry.HealthyTargetStatus;
import registry.LdlResult;
import registry.Medication;
import registry.PsychologicalScreeningResult;
import registry.Therapy;
import registry.TshResult;
import utility.ConnectionPool;
import utility.DatabaseUtility;

/**
 * This class accesses the database to retrieve various histories of measurement
 * results.
 *
 * @author Bryan Daniel
 * @version 2, March 16, 2017
 */
public class PatientHistoryDataAccess {

    /**
     * This method returns the list of A1C results for a patient.
     *
     * @param patientId the patient ID
     * @return the list of A1C results or null if results are missing
     */
    public static ArrayList<A1cResult> getA1c(Integer patientId) {
        ArrayList<A1cResult> results
                = new ArrayList<>();
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        CallableStatement cs = null;
        ResultSet rs = null;

        try {
            cs = connection.prepareCall("{CALL getA1C(?, ?)}");
            cs.setInt(1, patientId);
            cs.registerOutParameter(2, java.sql.Types.TINYINT);

            /* reads true if result set exists */
            boolean success = cs.execute();
            if (!success) {
                return null;
            }

            rs = cs.getResultSet();
            while (rs.next()) {
                A1cResult r = new A1cResult();
                r.setDate(rs.getDate("date recorded"));
                r.setValue(rs.getBigDecimal("result"));
                r.setPoc(rs.getBoolean("poc"));
                results.add(r);
            }

        } catch (SQLException ex) {
            Logger.getLogger(PatientHistoryDataAccess.class.getName()).log(Level.SEVERE,
                    "An exception occurred during the getA1c method.", ex);
            return null;
        } finally {
            DatabaseUtility.closeResultSet(rs);
            DatabaseUtility.closeCallableStatement(cs);
            pool.freeConnection(connection);
        }

        if (results.isEmpty()) {
            return null;
        } else {
            return results;
        }
    }

    /**
     * This method returns the list of PSA results for a patient.
     *
     * @param patientId the patient ID
     * @return the list of PSA results or null if results are missing
     */
    public static ArrayList<ContinuousResult> getPsa(Integer patientId) {
        ArrayList<ContinuousResult> results
                = new ArrayList<>();
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        CallableStatement cs = null;
        ResultSet rs = null;

        try {
            cs = connection.prepareCall("{CALL getPsa(?, ?)}");
            cs.setInt(1, patientId);
            cs.registerOutParameter(2, java.sql.Types.TINYINT);

            /* reads true if result set exists */
            boolean success = cs.execute();
            if (!success) {
                return null;
            }

            rs = cs.getResultSet();
            while (rs.next()) {
                ContinuousResult r = new ContinuousResult();
                r.setDate(rs.getDate("date recorded"));
                r.setValue(rs.getBigDecimal("result"));
                results.add(r);
            }

        } catch (SQLException ex) {
            Logger.getLogger(PatientHistoryDataAccess.class.getName()).log(Level.SEVERE,
                    "An exception occurred during the getPsa method.", ex);
            return null;
        } finally {
            DatabaseUtility.closeResultSet(rs);
            DatabaseUtility.closeCallableStatement(cs);
            pool.freeConnection(connection);
        }

        if (results.isEmpty()) {
            return null;
        } else {
            return results;
        }
    }

    /**
     * This method returns the list of ALT results for a patient.
     *
     * @param patientId the patient ID
     * @return the list of ALT results or null if results are missing
     */
    public static ArrayList<ContinuousResult> getAlt(Integer patientId) {
        ArrayList<ContinuousResult> results
                = new ArrayList<>();
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        CallableStatement cs = null;
        ResultSet rs = null;

        try {
            cs = connection.prepareCall("{CALL getAlt(?, ?)}");
            cs.setInt(1, patientId);
            cs.registerOutParameter(2, java.sql.Types.TINYINT);

            /* reads true if result set exists */
            boolean success = cs.execute();
            if (!success) {
                return null;
            }

            rs = cs.getResultSet();
            while (rs.next()) {
                ContinuousResult r = new ContinuousResult();
                r.setDate(rs.getDate("date recorded"));
                r.setValue(rs.getBigDecimal("result"));
                results.add(r);
            }

        } catch (SQLException ex) {
            Logger.getLogger(PatientHistoryDataAccess.class.getName()).log(Level.SEVERE,
                    "An exception occurred during the getAlt method.", ex);
            return null;
        } finally {
            DatabaseUtility.closeResultSet(rs);
            DatabaseUtility.closeCallableStatement(cs);
            pool.freeConnection(connection);
        }

        if (results.isEmpty()) {
            return null;
        } else {
            return results;
        }
    }

    /**
     * This method returns the list of AST results for a patient.
     *
     * @param patientId the patient ID
     * @return the list of AST results or null if results are missing
     */
    public static ArrayList<ContinuousResult> getAst(Integer patientId) {
        ArrayList<ContinuousResult> results
                = new ArrayList<>();
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        CallableStatement cs = null;
        ResultSet rs = null;

        try {
            cs = connection.prepareCall("{CALL getAst(?, ?)}");
            cs.setInt(1, patientId);
            cs.registerOutParameter(2, java.sql.Types.TINYINT);

            /* reads true if result set exists */
            boolean success = cs.execute();
            if (!success) {
                return null;
            }

            rs = cs.getResultSet();
            while (rs.next()) {
                ContinuousResult r = new ContinuousResult();
                r.setDate(rs.getDate("date recorded"));
                r.setValue(rs.getBigDecimal("result"));
                results.add(r);
            }

        } catch (SQLException ex) {
            Logger.getLogger(PatientHistoryDataAccess.class.getName()).log(Level.SEVERE,
                    "An exception occurred during the getAst method.", ex);
            return null;
        } finally {
            DatabaseUtility.closeResultSet(rs);
            DatabaseUtility.closeCallableStatement(cs);
            pool.freeConnection(connection);
        }

        if (results.isEmpty()) {
            return null;
        } else {
            return results;
        }
    }

    /**
     * This method returns the list of blood pressure results for a patient.
     *
     * @param patientId the patient ID
     * @return the list of blood pressure results or null if results are missing
     */
    public static ArrayList<BloodPressureResult> getBP(Integer patientId) {
        ArrayList<BloodPressureResult> results
                = new ArrayList<>();
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        CallableStatement cs = null;
        ResultSet rs = null;

        try {
            cs = connection.prepareCall("{CALL getBP(?, ?)}");
            cs.setInt(1, patientId);
            cs.registerOutParameter(2, java.sql.Types.TINYINT);

            /* reads true if result set exists */
            boolean success = cs.execute();
            if (!success) {
                return null;
            }

            rs = cs.getResultSet();
            while (rs.next()) {
                BloodPressureResult r = new BloodPressureResult();
                r.setDate(rs.getDate("date recorded"));
                r.setSystolicValue(rs.getInt("systole"));
                r.setDiastolicValue(rs.getInt("diastole"));
                r.setAceOrArb(rs.getBoolean("ace or arb"));
                results.add(r);
            }

        } catch (SQLException ex) {
            Logger.getLogger(PatientHistoryDataAccess.class.getName()).log(Level.SEVERE,
                    "An exception occurred during the getBP method.", ex);
            return null;
        } finally {
            DatabaseUtility.closeResultSet(rs);
            DatabaseUtility.closeCallableStatement(cs);
            pool.freeConnection(connection);
        }

        if (results.isEmpty()) {
            return null;
        } else {
            return results;
        }
    }

    /**
     * This method returns the list of BMI results for a patient.
     *
     * @param patientId the patient ID
     * @return the list of BMI results or null if results are missing
     */
    public static ArrayList<ContinuousResult> getBmi(Integer patientId) {
        ArrayList<ContinuousResult> results
                = new ArrayList<>();
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        CallableStatement cs = null;
        ResultSet rs = null;

        try {
            cs = connection.prepareCall("{CALL getBMI(?, ?)}");
            cs.setInt(1, patientId);
            cs.registerOutParameter(2, java.sql.Types.TINYINT);

            /* reads true if result set exists */
            boolean success = cs.execute();
            if (!success) {
                return null;
            }

            rs = cs.getResultSet();
            while (rs.next()) {
                ContinuousResult r = new ContinuousResult();
                r.setDate(rs.getDate("date recorded"));
                r.setValue(rs.getBigDecimal("result"));
                results.add(r);
            }

        } catch (SQLException ex) {
            Logger.getLogger(PatientHistoryDataAccess.class.getName()).log(Level.SEVERE,
                    "An exception occurred during the getBmi method.", ex);
            return null;
        } finally {
            DatabaseUtility.closeResultSet(rs);
            DatabaseUtility.closeCallableStatement(cs);
            pool.freeConnection(connection);
        }

        if (results.isEmpty()) {
            return null;
        } else {
            return results;
        }
    }

    /**
     * This method returns the list of class attendance dates for a patient.
     *
     * @param patientId the patient ID
     * @return the list of class attendance dates or null if results are missing
     */
    public static ArrayList<Date> getClass(Integer patientId) {
        ArrayList<Date> results
                = new ArrayList<>();
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        CallableStatement cs = null;
        ResultSet rs = null;

        try {
            cs = connection.prepareCall("{CALL getClass(?, ?)}");
            cs.setInt(1, patientId);
            cs.registerOutParameter(2, java.sql.Types.TINYINT);

            /* reads true if result set exists */
            boolean success = cs.execute();
            if (!success) {
                return null;
            }

            rs = cs.getResultSet();
            while (rs.next()) {
                Date d = rs.getDate("date attended");
                results.add(d);
            }

        } catch (SQLException ex) {
            Logger.getLogger(PatientHistoryDataAccess.class.getName()).log(Level.SEVERE,
                    "An exception occurred during the getClass method.", ex);
            return null;
        } finally {
            DatabaseUtility.closeResultSet(rs);
            DatabaseUtility.closeCallableStatement(cs);
            pool.freeConnection(connection);
        }

        if (results.isEmpty()) {
            return null;
        } else {
            return results;
        }
    }

    /**
     * This method returns the list of creatinine results for a patient.
     *
     * @param patientId the patient ID
     * @return the list of creatinine results or null if results are missing
     */
    public static ArrayList<ContinuousResult> getCreatinine(Integer patientId) {
        ArrayList<ContinuousResult> results
                = new ArrayList<>();
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        CallableStatement cs = null;
        ResultSet rs = null;

        try {
            cs = connection.prepareCall("{CALL getCreatinine(?, ?)}");
            cs.setInt(1, patientId);
            cs.registerOutParameter(2, java.sql.Types.TINYINT);

            /* reads true if result set exists */
            boolean success = cs.execute();
            if (!success) {
                return null;
            }

            rs = cs.getResultSet();
            while (rs.next()) {
                ContinuousResult r = new ContinuousResult();
                r.setDate(rs.getDate("date recorded"));
                r.setValue(rs.getBigDecimal("result"));
                results.add(r);
            }

        } catch (SQLException ex) {
            Logger.getLogger(PatientHistoryDataAccess.class.getName()).log(Level.SEVERE,
                    "An exception occurred during the getCreatinine method.", ex);
            return null;
        } finally {
            DatabaseUtility.closeResultSet(rs);
            DatabaseUtility.closeCallableStatement(cs);
            pool.freeConnection(connection);
        }

        if (results.isEmpty()) {
            return null;
        } else {
            return results;
        }
    }

    /**
     * This method returns the list of eGFR results for a patient.
     *
     * @param patientId the patient ID
     * @return the list of eGFR results or null if results are missing
     */
    public static ArrayList<ContinuousResult> getEgfr(Integer patientId) {
        ArrayList<ContinuousResult> results
                = new ArrayList<>();
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        CallableStatement cs = null;
        ResultSet rs = null;

        try {
            cs = connection.prepareCall("{CALL getEGFR(?, ?)}");
            cs.setInt(1, patientId);
            cs.registerOutParameter(2, java.sql.Types.TINYINT);

            /* reads true if result set exists */
            boolean success = cs.execute();
            if (!success) {
                return null;
            }

            rs = cs.getResultSet();
            while (rs.next()) {
                ContinuousResult r = new ContinuousResult();
                r.setDate(rs.getDate("date recorded"));
                r.setValue(rs.getBigDecimal("result"));
                results.add(r);
            }

        } catch (SQLException ex) {
            Logger.getLogger(PatientHistoryDataAccess.class.getName()).log(Level.SEVERE,
                    "An exception occurred during the getEgfr method.", ex);
            return null;
        } finally {
            DatabaseUtility.closeResultSet(rs);
            DatabaseUtility.closeCallableStatement(cs);
            pool.freeConnection(connection);
        }

        if (results.isEmpty()) {
            return null;
        } else {
            return results;
        }
    }

    /**
     * This method returns the list of eye screening results for a patient.
     *
     * @param patientId the patient ID
     * @return the list of eye screening results or null if results are missing
     */
    public static ArrayList<CategoricalResult> getEye(Integer patientId) {
        ArrayList<CategoricalResult> results
                = new ArrayList<>();
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        CallableStatement cs = null;
        ResultSet rs = null;

        try {
            cs = connection.prepareCall("{CALL getEye(?, ?)}");
            cs.setInt(1, patientId);
            cs.registerOutParameter(2, java.sql.Types.TINYINT);

            /* reads true if result set exists */
            boolean success = cs.execute();
            if (!success) {
                return null;
            }

            rs = cs.getResultSet();
            while (rs.next()) {
                CategoricalResult r = new CategoricalResult();
                r.setDateRecorded(rs.getDate("date recorded"));
                r.setCategory(rs.getString("eye exam code"));
                r.setDefinition(rs.getString("definition"));
                results.add(r);
            }

        } catch (SQLException ex) {
            Logger.getLogger(PatientHistoryDataAccess.class.getName()).log(Level.SEVERE,
                    "An exception occurred during the getEye method.", ex);
            return null;
        } finally {
            DatabaseUtility.closeResultSet(rs);
            DatabaseUtility.closeCallableStatement(cs);
            pool.freeConnection(connection);
        }

        if (results.isEmpty()) {
            return null;
        } else {
            return results;
        }
    }

    /**
     * This method returns the list of foot screening results for a patient.
     *
     * @param patientId the patient ID
     * @return the list of foot screening results or null if results are missing
     */
    public static ArrayList<CategoricalResult> getFoot(Integer patientId) {
        ArrayList<CategoricalResult> results
                = new ArrayList<>();
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        CallableStatement cs = null;
        ResultSet rs = null;

        try {
            cs = connection.prepareCall("{CALL getFoot(?, ?)}");
            cs.setInt(1, patientId);
            cs.registerOutParameter(2, java.sql.Types.TINYINT);

            /* reads true if result set exists */
            boolean success = cs.execute();
            if (!success) {
                return null;
            }

            rs = cs.getResultSet();
            while (rs.next()) {
                CategoricalResult r = new CategoricalResult();
                r.setDateRecorded(rs.getDate("date recorded"));
                r.setCategory(rs.getString("risk category"));
                r.setDefinition(rs.getString("definition"));
                results.add(r);
            }

        } catch (SQLException ex) {
            Logger.getLogger(PatientHistoryDataAccess.class.getName()).log(Level.SEVERE,
                    "An exception occurred during the getFoot method.", ex);
            return null;
        } finally {
            DatabaseUtility.closeResultSet(rs);
            DatabaseUtility.closeCallableStatement(cs);
            pool.freeConnection(connection);
        }

        if (results.isEmpty()) {
            return null;
        } else {
            return results;
        }
    }

    /**
     * This method returns the list of glucose results for a patient.
     *
     * @param patientId the patient ID
     * @return the list of glucose results or null if results are missing
     */
    public static ArrayList<ContinuousResult> getGlucose(Integer patientId) {
        ArrayList<ContinuousResult> results
                = new ArrayList<>();
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        CallableStatement cs = null;
        ResultSet rs = null;

        try {
            cs = connection.prepareCall("{CALL getGlucose(?, ?)}");
            cs.setInt(1, patientId);
            cs.registerOutParameter(2, java.sql.Types.TINYINT);

            /* reads true if result set exists */
            boolean success = cs.execute();
            if (!success) {
                return null;
            }

            rs = cs.getResultSet();
            while (rs.next()) {
                ContinuousResult r = new ContinuousResult();
                r.setDate(rs.getDate("date recorded"));
                r.setValue(rs.getBigDecimal("result"));
                results.add(r);
            }

        } catch (SQLException ex) {
            Logger.getLogger(PatientHistoryDataAccess.class.getName()).log(Level.SEVERE,
                    "An exception occurred during the getGlucose method.", ex);
            return null;
        } finally {
            DatabaseUtility.closeResultSet(rs);
            DatabaseUtility.closeCallableStatement(cs);
            pool.freeConnection(connection);
        }

        if (results.isEmpty()) {
            return null;
        } else {
            return results;
        }
    }

    /**
     * This method returns the list of HDL results for a patient.
     *
     * @param patientId the patient ID
     * @return the list of HDL results or null if results are missing
     */
    public static ArrayList<ContinuousResult> getHdl(Integer patientId) {
        ArrayList<ContinuousResult> results
                = new ArrayList<>();
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        CallableStatement cs = null;
        ResultSet rs = null;

        try {
            cs = connection.prepareCall("{CALL getHDL(?, ?)}");
            cs.setInt(1, patientId);
            cs.registerOutParameter(2, java.sql.Types.TINYINT);

            /* reads true if result set exists */
            boolean success = cs.execute();
            if (!success) {
                return null;
            }

            rs = cs.getResultSet();
            while (rs.next()) {
                ContinuousResult r = new ContinuousResult();
                r.setDate(rs.getDate("date recorded"));
                r.setValue(rs.getBigDecimal("result"));
                results.add(r);
            }

        } catch (SQLException ex) {
            Logger.getLogger(PatientHistoryDataAccess.class.getName()).log(Level.SEVERE,
                    "An exception occurred during the getHdl method.", ex);
            return null;
        } finally {
            DatabaseUtility.closeResultSet(rs);
            DatabaseUtility.closeCallableStatement(cs);
            pool.freeConnection(connection);
        }

        if (results.isEmpty()) {
            return null;
        } else {
            return results;
        }
    }

    /**
     * This method returns the list of hepatitis B vaccination dates for a
     * patient.
     *
     * @param patientId the patient ID
     * @return the list of hepatitis B vaccination dates or null if results are
     * missing
     */
    public static ArrayList<Date> getHepB(Integer patientId) {
        ArrayList<Date> results
                = new ArrayList<>();
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        CallableStatement cs = null;
        ResultSet rs = null;

        try {
            cs = connection.prepareCall("{CALL getHepB(?, ?)}");
            cs.setInt(1, patientId);
            cs.registerOutParameter(2, java.sql.Types.TINYINT);

            /* reads true if result set exists */
            boolean success = cs.execute();
            if (!success) {
                return null;
            }

            rs = cs.getResultSet();
            while (rs.next()) {
                Date d = rs.getDate("date recorded");
                results.add(d);
            }

        } catch (SQLException ex) {
            Logger.getLogger(PatientHistoryDataAccess.class.getName()).log(Level.SEVERE,
                    "An exception occurred during the getHepB method.", ex);
            return null;
        } finally {
            DatabaseUtility.closeResultSet(rs);
            DatabaseUtility.closeCallableStatement(cs);
            pool.freeConnection(connection);
        }

        if (results.isEmpty()) {
            return null;
        } else {
            return results;
        }
    }

    /**
     * This method returns the list of hospitalization dates for a patient.
     *
     * @param patientId the patient ID
     * @return the list of hospitalization dates or null if results are missing
     */
    public static ArrayList<Date> getER(Integer patientId) {
        ArrayList<Date> results
                = new ArrayList<>();
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        CallableStatement cs = null;
        ResultSet rs = null;

        try {
            cs = connection.prepareCall("{CALL getER(?, ?)}");
            cs.setInt(1, patientId);
            cs.registerOutParameter(2, java.sql.Types.TINYINT);

            /* reads true if result set exists */
            boolean success = cs.execute();
            if (!success) {
                return null;
            }

            rs = cs.getResultSet();
            while (rs.next()) {
                Date d = rs.getDate("date recorded");
                results.add(d);
            }

        } catch (SQLException ex) {
            Logger.getLogger(PatientHistoryDataAccess.class.getName()).log(Level.SEVERE,
                    "An exception occurred during the getER method.", ex);
            return null;
        } finally {
            DatabaseUtility.closeResultSet(rs);
            DatabaseUtility.closeCallableStatement(cs);
            pool.freeConnection(connection);
        }

        if (results.isEmpty()) {
            return null;
        } else {
            return results;
        }
    }

    /**
     * This method returns the list of influenza vaccination dates for a
     * patient.
     *
     * @param patientId the patient ID
     * @return the list of influenza vaccination dates or null if results are
     * missing
     */
    public static ArrayList<Date> getInfluenza(Integer patientId) {
        ArrayList<Date> results
                = new ArrayList<>();
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        CallableStatement cs = null;
        ResultSet rs = null;

        try {
            cs = connection.prepareCall("{CALL getInfluenza(?, ?)}");
            cs.setInt(1, patientId);
            cs.registerOutParameter(2, java.sql.Types.TINYINT);

            /* reads true if result set exists */
            boolean success = cs.execute();
            if (!success) {
                return null;
            }

            rs = cs.getResultSet();
            while (rs.next()) {
                Date d = rs.getDate("date recorded");
                results.add(d);
            }

        } catch (SQLException ex) {
            Logger.getLogger(PatientHistoryDataAccess.class.getName()).log(Level.SEVERE,
                    "An exception occurred during the getInfluenza method.", ex);
            return null;
        } finally {
            DatabaseUtility.closeResultSet(rs);
            DatabaseUtility.closeCallableStatement(cs);
            pool.freeConnection(connection);
        }

        if (results.isEmpty()) {
            return null;
        } else {
            return results;
        }
    }

    /**
     * This method returns the list of LDL results for a patient.
     *
     * @param patientId the patient ID
     * @return the list of LDL results or null if results are missing
     */
    public static ArrayList<LdlResult> getLdl(Integer patientId) {
        ArrayList<LdlResult> results
                = new ArrayList<>();
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        CallableStatement cs = null;
        ResultSet rs = null;

        try {
            cs = connection.prepareCall("{CALL getLDL(?, ?)}");
            cs.setInt(1, patientId);
            cs.registerOutParameter(2, java.sql.Types.TINYINT);

            /* reads true if result set exists */
            boolean success = cs.execute();
            if (!success) {
                return null;
            }

            rs = cs.getResultSet();
            while (rs.next()) {
                LdlResult r = new LdlResult();
                r.setDate(rs.getDate("date recorded"));
                r.setValue(rs.getBigDecimal("result"));
                r.setPostMi(rs.getBoolean("post mi"));
                r.setOnStatin(rs.getBoolean("on statin"));
                results.add(r);
            }

        } catch (SQLException ex) {
            Logger.getLogger(PatientHistoryDataAccess.class.getName()).log(Level.SEVERE,
                    "An exception occurred during the getLdl method.", ex);
            return null;
        } finally {
            DatabaseUtility.closeResultSet(rs);
            DatabaseUtility.closeCallableStatement(cs);
            pool.freeConnection(connection);
        }

        if (results.isEmpty()) {
            return null;
        } else {
            return results;
        }
    }

    /**
     * This method returns the list of notes for a patient.
     *
     * @param patientId the patient ID
     * @param referenceCharacters the character string
     * @return the list of notes or null if results are missing
     */
    public static ArrayList<CategoricalResult> getAllNotes(Integer patientId,
            Object referenceCharacters) {
        ArrayList<CategoricalResult> allNotes
                = new ArrayList<>();
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        CallableStatement cs = null;
        ResultSet rs = null;

        try {
            cs = connection.prepareCall("{CALL getAllNotes(?, ?, ?)}");
            cs.setInt(1, patientId);
            cs.setString(2, (String) referenceCharacters);
            cs.registerOutParameter(3, java.sql.Types.TINYINT);

            /* reads true if result set exists */
            boolean success = cs.execute();
            if (!success) {
                return null;
            }

            rs = cs.getResultSet();
            while (rs.next()) {
                CategoricalResult r = new CategoricalResult();
                r.setDateRecorded(rs.getDate("date recorded"));
                r.setCategory(rs.getString("topic"));
                r.setDefinition(rs.getString("note"));
                allNotes.add(r);
            }

        } catch (SQLException ex) {
            Logger.getLogger(PatientHistoryDataAccess.class.getName()).log(Level.SEVERE,
                    "An exception occurred during the getAllNotes method.", ex);
            return null;
        } finally {
            DatabaseUtility.closeResultSet(rs);
            DatabaseUtility.closeCallableStatement(cs);
            pool.freeConnection(connection);
        }

        if (allNotes.isEmpty()) {
            return null;
        } else {
            return allNotes;
        }
    }

    /**
     * This method returns the list of patient-reported compliance results for a
     * patient.
     *
     * @param patientId the patient ID
     * @return the list of patient-reported compliance results or null if
     * results are missing
     */
    public static ArrayList<ContinuousResult> getCompliance(Integer patientId) {
        ArrayList<ContinuousResult> results
                = new ArrayList<>();
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        CallableStatement cs = null;
        ResultSet rs = null;

        try {
            cs = connection.prepareCall("{CALL getCompliance(?, ?)}");
            cs.setInt(1, patientId);
            cs.registerOutParameter(2, java.sql.Types.TINYINT);

            /* reads true if result set exists */
            boolean success = cs.execute();
            if (!success) {
                return null;
            }

            rs = cs.getResultSet();
            while (rs.next()) {
                ContinuousResult r = new ContinuousResult();
                r.setDate(rs.getDate("date recorded"));
                r.setValue(rs.getBigDecimal("result"));
                results.add(r);
            }

        } catch (SQLException ex) {
            Logger.getLogger(PatientHistoryDataAccess.class.getName()).log(Level.SEVERE,
                    "An exception occurred during the getCompliance method.", ex);
            return null;
        } finally {
            DatabaseUtility.closeResultSet(rs);
            DatabaseUtility.closeCallableStatement(cs);
            pool.freeConnection(connection);
        }

        if (results.isEmpty()) {
            return null;
        } else {
            return results;
        }
    }

    /**
     * This method returns the list of PCV-13 vaccine dates for a patient.
     *
     * @param patientId the patient ID
     * @return the list of PCV-13 vaccine dates or null if results are missing
     */
    public static ArrayList<Date> getPcv13(Integer patientId) {
        ArrayList<Date> results
                = new ArrayList<>();
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        CallableStatement cs = null;
        ResultSet rs = null;

        try {
            cs = connection.prepareCall("{CALL getPCV13(?, ?)}");
            cs.setInt(1, patientId);
            cs.registerOutParameter(2, java.sql.Types.TINYINT);

            /* reads true if result set exists */
            boolean success = cs.execute();
            if (!success) {
                return null;
            }

            rs = cs.getResultSet();
            while (rs.next()) {
                Date d = rs.getDate("date recorded");
                results.add(d);
            }

        } catch (SQLException ex) {
            Logger.getLogger(PatientHistoryDataAccess.class.getName()).log(Level.SEVERE,
                    "An exception occurred during the getPcv13 method.", ex);
            return null;
        } finally {
            DatabaseUtility.closeResultSet(rs);
            DatabaseUtility.closeCallableStatement(cs);
            pool.freeConnection(connection);
        }

        if (results.isEmpty()) {
            return null;
        } else {
            return results;
        }
    }

    /**
     * This method returns the list of weekly minutes of physical activity for a
     * patient.
     *
     * @param patientId the patient ID
     * @return the list of weekly minutes of physical activity or null if
     * results are missing
     */
    public static ArrayList<DiscreteResult> getPhysical(Integer patientId) {
        ArrayList<DiscreteResult> results
                = new ArrayList<>();
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        CallableStatement cs = null;
        ResultSet rs = null;

        try {
            cs = connection.prepareCall("{CALL getPhysicalActivity(?, ?)}");
            cs.setInt(1, patientId);
            cs.registerOutParameter(2, java.sql.Types.TINYINT);

            /* reads true if result set exists */
            boolean success = cs.execute();
            if (!success) {
                return null;
            }

            rs = cs.getResultSet();
            while (rs.next()) {
                DiscreteResult r = new DiscreteResult();
                r.setDate(rs.getDate("date recorded"));
                r.setValue(rs.getInt("min per week"));
                results.add(r);
            }

        } catch (SQLException ex) {
            Logger.getLogger(PatientHistoryDataAccess.class.getName()).log(Level.SEVERE,
                    "An exception occurred during the getPhysical method.", ex);
            return null;
        } finally {
            DatabaseUtility.closeResultSet(rs);
            DatabaseUtility.closeCallableStatement(cs);
            pool.freeConnection(connection);
        }

        if (results.isEmpty()) {
            return null;
        } else {
            return results;
        }
    }

    /**
     * This method returns the list of PPSV-23 vaccine dates for a patient.
     *
     * @param patientId the patient ID
     * @return the list of PPSV-23 vaccine dates or null if results are missing
     */
    public static ArrayList<Date> getPpsv23(Integer patientId) {
        ArrayList<Date> results
                = new ArrayList<>();
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        CallableStatement cs = null;
        ResultSet rs = null;

        try {
            cs = connection.prepareCall("{CALL getPPSV23(?, ?)}");
            cs.setInt(1, patientId);
            cs.registerOutParameter(2, java.sql.Types.TINYINT);

            /* reads true if result set exists */
            boolean success = cs.execute();
            if (!success) {
                return null;
            }

            rs = cs.getResultSet();
            while (rs.next()) {
                Date d = rs.getDate("date recorded");
                results.add(d);
            }

        } catch (SQLException ex) {
            Logger.getLogger(PatientHistoryDataAccess.class.getName()).log(Level.SEVERE,
                    "An exception occurred during the getPpsv23 method.", ex);
            return null;
        } finally {
            DatabaseUtility.closeResultSet(rs);
            DatabaseUtility.closeCallableStatement(cs);
            pool.freeConnection(connection);
        }

        if (results.isEmpty()) {
            return null;
        } else {
            return results;
        }
    }

    /**
     * This method returns the list of psychological screening results for a
     * patient.
     *
     * @param patientId the patient ID
     * @return the list of psychological screening results or null if results
     * are missing
     */
    public static ArrayList<PsychologicalScreeningResult> getPsychological(Integer patientId) {
        ArrayList<PsychologicalScreeningResult> results
                = new ArrayList<>();
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        CallableStatement cs = null;
        ResultSet rs = null;

        try {
            cs = connection.prepareCall("{CALL getPsychologicalScreening(?, ?)}");
            cs.setInt(1, patientId);
            cs.registerOutParameter(2, java.sql.Types.TINYINT);

            /* reads true if result set exists */
            boolean success = cs.execute();
            if (!success) {
                return null;
            }

            rs = cs.getResultSet();
            while (rs.next()) {
                PsychologicalScreeningResult r
                        = new PsychologicalScreeningResult();
                r.setDate(rs.getDate("date recorded"));
                r.setScore(rs.getInt("phq score"));
                r.setSeverity(rs.getString("severity"));
                r.setProposedActions(rs.getString("proposed actions"));
                results.add(r);
            }

        } catch (SQLException ex) {
            Logger.getLogger(PatientHistoryDataAccess.class.getName()).log(Level.SEVERE,
                    "An exception occurred during the getPsychological method.", ex);
            return null;
        } finally {
            DatabaseUtility.closeResultSet(rs);
            DatabaseUtility.closeCallableStatement(cs);
            pool.freeConnection(connection);
        }

        if (results.isEmpty()) {
            return null;
        } else {
            return results;
        }
    }

    /**
     * This method returns the list of smoking status values for a patient.
     *
     * @param patientId the patient ID
     * @return the list of smoking status values or null if results are missing
     */
    public static ArrayList<BooleanResult> getSmoking(Integer patientId) {
        ArrayList<BooleanResult> results
                = new ArrayList<>();
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        CallableStatement cs = null;
        ResultSet rs = null;

        try {
            cs = connection.prepareCall("{CALL getSmoker(?, ?)}");
            cs.setInt(1, patientId);
            cs.registerOutParameter(2, java.sql.Types.TINYINT);

            /* reads true if result set exists */
            boolean success = cs.execute();
            if (!success) {
                return null;
            }

            rs = cs.getResultSet();
            while (rs.next()) {
                BooleanResult r = new BooleanResult();
                r.setDate(rs.getDate("date recorded"));
                r.setValue(rs.getBoolean("smoker"));
                results.add(r);
            }

        } catch (SQLException ex) {
            Logger.getLogger(PatientHistoryDataAccess.class.getName()).log(Level.SEVERE,
                    "An exception occurred during the getSmoking method.", ex);
            return null;
        } finally {
            DatabaseUtility.closeResultSet(rs);
            DatabaseUtility.closeCallableStatement(cs);
            pool.freeConnection(connection);
        }

        if (results.isEmpty()) {
            return null;
        } else {
            return results;
        }
    }

    /**
     * This method returns the list of T4 results for a patient.
     *
     * @param patientId the patient ID
     * @return the list of T4 results or null if results are missing
     */
    public static ArrayList<ContinuousResult> getT4(Integer patientId) {
        ArrayList<ContinuousResult> results
                = new ArrayList<>();
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        CallableStatement cs = null;
        ResultSet rs = null;

        try {
            cs = connection.prepareCall("{CALL getT4(?, ?)}");
            cs.setInt(1, patientId);
            cs.registerOutParameter(2, java.sql.Types.TINYINT);

            /* reads true if result set exists */
            boolean success = cs.execute();
            if (!success) {
                return null;
            }

            rs = cs.getResultSet();
            while (rs.next()) {
                ContinuousResult r = new ContinuousResult();
                r.setDate(rs.getDate("date recorded"));
                r.setValue(rs.getBigDecimal("result"));
                results.add(r);
            }

        } catch (SQLException ex) {
            Logger.getLogger(PatientHistoryDataAccess.class.getName()).log(Level.SEVERE,
                    "An exception occurred during the getT4 method.", ex);
            return null;
        } finally {
            DatabaseUtility.closeResultSet(rs);
            DatabaseUtility.closeCallableStatement(cs);
            pool.freeConnection(connection);
        }

        if (results.isEmpty()) {
            return null;
        } else {
            return results;
        }
    }

    /**
     * This method returns the list of TDAP vaccine dates for a patient.
     *
     * @param patientId the patient ID
     * @return the list of TDAP vaccine dates or null if results are missing
     */
    public static ArrayList<Date> getTdap(Integer patientId) {
        ArrayList<Date> results
                = new ArrayList<>();
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        CallableStatement cs = null;
        ResultSet rs = null;

        try {
            cs = connection.prepareCall("{CALL getTDAP(?, ?)}");
            cs.setInt(1, patientId);
            cs.registerOutParameter(2, java.sql.Types.TINYINT);

            /* reads true if result set exists */
            boolean success = cs.execute();
            if (!success) {
                return null;
            }

            rs = cs.getResultSet();
            while (rs.next()) {
                Date d = rs.getDate("date recorded");
                results.add(d);
            }

        } catch (SQLException ex) {
            Logger.getLogger(PatientHistoryDataAccess.class.getName()).log(Level.SEVERE,
                    "An exception occurred during the getTdap method.", ex);
            return null;
        } finally {
            DatabaseUtility.closeResultSet(rs);
            DatabaseUtility.closeCallableStatement(cs);
            pool.freeConnection(connection);
        }

        if (results.isEmpty()) {
            return null;
        } else {
            return results;
        }
    }

    /**
     * This method returns the list of telephone follow-up results for a
     * patient.
     *
     * @param patientId the patient ID
     * @return the list of telephone follow-up results or null if results are
     * missing
     */
    public static ArrayList<CategoricalResult> getTelephone(Integer patientId) {
        ArrayList<CategoricalResult> results
                = new ArrayList<>();
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        CallableStatement cs = null;
        ResultSet rs = null;

        try {
            cs = connection.prepareCall("{CALL getTelephoneFollowUp(?, ?)}");
            cs.setInt(1, patientId);
            cs.registerOutParameter(2, java.sql.Types.TINYINT);

            /* reads true if result set exists */
            boolean success = cs.execute();
            if (!success) {
                return null;
            }

            rs = cs.getResultSet();
            while (rs.next()) {
                CategoricalResult r = new CategoricalResult();
                r.setDateRecorded(rs.getDate("date recorded"));
                r.setCategory(rs.getString("follow up code"));
                r.setDefinition(rs.getString("definition"));
                results.add(r);
            }

        } catch (SQLException ex) {
            Logger.getLogger(PatientHistoryDataAccess.class.getName()).log(Level.SEVERE,
                    "An exception occurred during the getTelephone method.", ex);
            return null;
        } finally {
            DatabaseUtility.closeResultSet(rs);
            DatabaseUtility.closeCallableStatement(cs);
            pool.freeConnection(connection);
        }

        if (results.isEmpty()) {
            return null;
        } else {
            return results;
        }
    }

    /**
     * This method returns the list of triglycerides results for a patient.
     *
     * @param patientId the patient ID
     * @return the list of triglycerides results or null if results are missing
     */
    public static ArrayList<ContinuousResult> getTriglycerides(Integer patientId) {
        ArrayList<ContinuousResult> results
                = new ArrayList<>();
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        CallableStatement cs = null;
        ResultSet rs = null;

        try {
            cs = connection.prepareCall("{CALL getTriglycerides(?, ?)}");
            cs.setInt(1, patientId);
            cs.registerOutParameter(2, java.sql.Types.TINYINT);

            /* reads true if result set exists */
            boolean success = cs.execute();
            if (!success) {
                return null;
            }

            rs = cs.getResultSet();
            while (rs.next()) {
                ContinuousResult r = new ContinuousResult();
                r.setDate(rs.getDate("date recorded"));
                r.setValue(rs.getBigDecimal("result"));
                results.add(r);
            }

        } catch (SQLException ex) {
            Logger.getLogger(PatientHistoryDataAccess.class.getName()).log(Level.SEVERE,
                    "An exception occurred during the getTriglycerides method.", ex);
            return null;
        } finally {
            DatabaseUtility.closeResultSet(rs);
            DatabaseUtility.closeCallableStatement(cs);
            pool.freeConnection(connection);
        }

        if (results.isEmpty()) {
            return null;
        } else {
            return results;
        }
    }

    /**
     * This method returns the list of TSH results for a patient.
     *
     * @param patientId the patient ID
     * @return the list of TSH results or null if results are missing
     */
    public static ArrayList<TshResult> getTsh(Integer patientId) {
        ArrayList<TshResult> results
                = new ArrayList<>();
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        CallableStatement cs = null;
        ResultSet rs = null;

        try {
            cs = connection.prepareCall("{CALL getTSH(?, ?)}");
            cs.setInt(1, patientId);
            cs.registerOutParameter(2, java.sql.Types.TINYINT);

            /* reads true if result set exists */
            boolean success = cs.execute();
            if (!success) {
                return null;
            }

            rs = cs.getResultSet();
            while (rs.next()) {
                TshResult r = new TshResult();
                r.setDate(rs.getDate("date recorded"));
                r.setValue(rs.getBigDecimal("result"));
                r.setOnThyroidTreatment(rs.getBoolean("on thyroid treatment"));
                results.add(r);
            }

        } catch (SQLException ex) {
            Logger.getLogger(PatientHistoryDataAccess.class.getName()).log(Level.SEVERE,
                    "An exception occurred during the getTsh method.", ex);
            return null;
        } finally {
            DatabaseUtility.closeResultSet(rs);
            DatabaseUtility.closeCallableStatement(cs);
            pool.freeConnection(connection);
        }

        if (results.isEmpty()) {
            return null;
        } else {
            return results;
        }
    }

    /**
     * This method returns the list of UACR results for a patient.
     *
     * @param patientId the patient ID
     * @return the list of UACR results or null if results are missing
     */
    public static ArrayList<ContinuousResult> getUacr(Integer patientId) {
        ArrayList<ContinuousResult> results
                = new ArrayList<>();
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        CallableStatement cs = null;
        ResultSet rs = null;

        try {
            cs = connection.prepareCall("{CALL getUACR(?, ?)}");
            cs.setInt(1, patientId);
            cs.registerOutParameter(2, java.sql.Types.TINYINT);

            /* reads true if result set exists */
            boolean success = cs.execute();
            if (!success) {
                return null;
            }

            rs = cs.getResultSet();
            while (rs.next()) {
                ContinuousResult r = new ContinuousResult();
                r.setDate(rs.getDate("date recorded"));
                r.setValue(rs.getBigDecimal("result"));
                results.add(r);
            }

        } catch (SQLException ex) {
            Logger.getLogger(PatientHistoryDataAccess.class.getName()).log(Level.SEVERE,
                    "An exception occurred during the getUacr method.", ex);
            return null;
        } finally {
            DatabaseUtility.closeResultSet(rs);
            DatabaseUtility.closeCallableStatement(cs);
            pool.freeConnection(connection);
        }

        if (results.isEmpty()) {
            return null;
        } else {
            return results;
        }
    }

    /**
     * This method returns the list of waist measurement results for a patient.
     *
     * @param patientId the patient ID
     * @return the list of waist results or null if results are missing
     */
    public static ArrayList<ContinuousResult> getWaist(Integer patientId) {
        ArrayList<ContinuousResult> results
                = new ArrayList<>();
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        CallableStatement cs = null;
        ResultSet rs = null;

        try {
            cs = connection.prepareCall("{CALL getWaist(?, ?)}");
            cs.setInt(1, patientId);
            cs.registerOutParameter(2, java.sql.Types.TINYINT);

            /* reads true if result set exists */
            boolean success = cs.execute();
            if (!success) {
                return null;
            }

            rs = cs.getResultSet();
            while (rs.next()) {
                ContinuousResult r = new ContinuousResult();
                r.setDate(rs.getDate("date recorded"));
                r.setValue(rs.getBigDecimal("result"));
                results.add(r);
            }

        } catch (SQLException ex) {
            Logger.getLogger(PatientHistoryDataAccess.class.getName()).log(Level.SEVERE,
                    "An exception occurred during the getWaist method.", ex);
            return null;
        } finally {
            DatabaseUtility.closeResultSet(rs);
            DatabaseUtility.closeCallableStatement(cs);
            pool.freeConnection(connection);
        }

        if (results.isEmpty()) {
            return null;
        } else {
            return results;
        }
    }

    /**
     * This method returns the list of zoster vaccine dates for a patient.
     *
     * @param patientId the patient ID
     * @return the list of zoster vaccine dates or null if results are missing
     */
    public static ArrayList<Date> getZoster(Integer patientId) {
        ArrayList<Date> results
                = new ArrayList<>();
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        CallableStatement cs = null;
        ResultSet rs = null;

        try {
            cs = connection.prepareCall("{CALL getZoster(?, ?)}");
            cs.setInt(1, patientId);
            cs.registerOutParameter(2, java.sql.Types.TINYINT);

            /* reads true if result set exists */
            boolean success = cs.execute();
            if (!success) {
                return null;
            }

            rs = cs.getResultSet();
            while (rs.next()) {
                Date d = rs.getDate("date recorded");
                results.add(d);
            }

        } catch (SQLException ex) {
            Logger.getLogger(PatientHistoryDataAccess.class.getName()).log(Level.SEVERE,
                    "An exception occurred during the getZoster method.", ex);
            return null;
        } finally {
            DatabaseUtility.closeResultSet(rs);
            DatabaseUtility.closeCallableStatement(cs);
            pool.freeConnection(connection);
        }

        if (results.isEmpty()) {
            return null;
        } else {
            return results;
        }
    }

    /**
     * This method returns the patient dashboard data for a patient.
     *
     * @param patientId the patient ID
     * @param referenceCharacters the character string
     * @return the patient's dashboard or null if results are missing
     */
    public static Dashboard getPatientDashboard(Integer patientId,
            Object referenceCharacters) {
        Dashboard dashboard
                = new Dashboard();
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        CallableStatement cs = null;
        ResultSet rs = null;

        try {
            cs = connection.prepareCall("{CALL getPatientDashboard(?, ?, ?)}");
            cs.setInt(1, patientId);
            cs.setString(2, (String) referenceCharacters);
            cs.registerOutParameter(3, java.sql.Types.TINYINT);

            /* reads true if result set exists */
            boolean success = cs.execute();
            if (!success) {
                return null;
            }

            /* A1C */
            rs = cs.getResultSet();

            while (rs.next()) {
                A1cResult r = new A1cResult();
                Date date = rs.getDate("date");
                BigDecimal value = rs.getBigDecimal("a1c");
                Boolean poc = rs.getBoolean("poc");
                r.setDate(date);
                r.setValue(value);
                r.setPoc(poc);
                dashboard.setA1c(r);
            }

            DatabaseUtility.closeResultSet(rs);

            /* glucose */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }
            rs = cs.getResultSet();

            while (rs.next()) {
                ContinuousResult cr = new ContinuousResult();
                Date date = rs.getDate("date");
                BigDecimal value = rs.getBigDecimal("glucose");
                cr.setDate(date);
                cr.setValue(value);
                dashboard.setGlucose(cr);
            }

            DatabaseUtility.closeResultSet(rs);

            /* LDL */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }
            rs = cs.getResultSet();

            while (rs.next()) {
                LdlResult lr = new LdlResult();
                Date date = rs.getDate("date");
                BigDecimal value = rs.getBigDecimal("ldl");
                Boolean postMi = rs.getBoolean("post mi");
                Boolean onStatin = rs.getBoolean("on statin");
                lr.setDate(date);
                lr.setValue(value);
                lr.setPostMi(postMi);
                lr.setOnStatin(onStatin);
                dashboard.setLdl(lr);
            }

            DatabaseUtility.closeResultSet(rs);

            /* HDL */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }
            rs = cs.getResultSet();

            while (rs.next()) {
                ContinuousResult cr = new ContinuousResult();
                Date date = rs.getDate("date");
                BigDecimal value = rs.getBigDecimal("hdl");
                cr.setDate(date);
                cr.setValue(value);
                dashboard.setHdl(cr);
            }

            DatabaseUtility.closeResultSet(rs);

            /* triglycerides */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }
            rs = cs.getResultSet();

            while (rs.next()) {
                ContinuousResult cr = new ContinuousResult();
                Date date = rs.getDate("date");
                BigDecimal value = rs.getBigDecimal("triglycerides");
                cr.setDate(date);
                cr.setValue(value);
                dashboard.setTriglycerides(cr);
            }

            DatabaseUtility.closeResultSet(rs);

            /* TSH */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }
            rs = cs.getResultSet();

            while (rs.next()) {
                TshResult tr = new TshResult();
                Date date = rs.getDate("date");
                BigDecimal value = rs.getBigDecimal("tsh");
                Boolean onThyroidTreatment
                        = rs.getBoolean("on thyroid treatment");
                tr.setDate(date);
                tr.setValue(value);
                tr.setOnThyroidTreatment(onThyroidTreatment);
                dashboard.setTsh(tr);
            }

            DatabaseUtility.closeResultSet(rs);

            /* T4 */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }
            rs = cs.getResultSet();

            while (rs.next()) {
                ContinuousResult cr = new ContinuousResult();
                Date date = rs.getDate("date");
                BigDecimal value = rs.getBigDecimal("t4");
                cr.setDate(date);
                cr.setValue(value);
                dashboard.setT4(cr);
            }

            DatabaseUtility.closeResultSet(rs);

            /* UACR */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }
            rs = cs.getResultSet();

            while (rs.next()) {
                ContinuousResult cr = new ContinuousResult();
                Date date = rs.getDate("date");
                BigDecimal value = rs.getBigDecimal("uacr");
                cr.setDate(date);
                cr.setValue(value);
                dashboard.setUacr(cr);
            }

            DatabaseUtility.closeResultSet(rs);

            /* eGFR */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }
            rs = cs.getResultSet();

            while (rs.next()) {
                ContinuousResult cr = new ContinuousResult();
                Date date = rs.getDate("date");
                BigDecimal value = rs.getBigDecimal("egfr");
                cr.setDate(date);
                cr.setValue(value);
                dashboard.setEgfr(cr);
            }

            DatabaseUtility.closeResultSet(rs);

            /* creatinine */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }
            rs = cs.getResultSet();

            while (rs.next()) {
                ContinuousResult cr = new ContinuousResult();
                Date date = rs.getDate("date");
                BigDecimal value = rs.getBigDecimal("creatinine");
                cr.setDate(date);
                cr.setValue(value);
                dashboard.setCreatinine(cr);
            }

            DatabaseUtility.closeResultSet(rs);

            /* BMI */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }
            rs = cs.getResultSet();

            while (rs.next()) {
                ContinuousResult cr = new ContinuousResult();
                Date date = rs.getDate("date");
                BigDecimal value = rs.getBigDecimal("bmi");
                cr.setDate(date);
                cr.setValue(value);
                dashboard.setBmi(cr);
            }

            DatabaseUtility.closeResultSet(rs);

            /* waist */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }
            rs = cs.getResultSet();

            while (rs.next()) {
                ContinuousResult cr = new ContinuousResult();
                Date date = rs.getDate("date");
                BigDecimal value = rs.getBigDecimal("waist");
                cr.setDate(date);
                cr.setValue(value);
                dashboard.setWaist(cr);
            }

            DatabaseUtility.closeResultSet(rs);

            /* blood pressure */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }
            rs = cs.getResultSet();

            while (rs.next()) {
                BloodPressureResult bpr = new BloodPressureResult();
                Date date = rs.getDate("date");
                Integer systole = rs.getInt("bp systole");
                Integer diastole = rs.getInt("bp diastole");
                Boolean aceOrArb = rs.getBoolean("ace or arb");
                bpr.setDate(date);
                bpr.setSystolicValue(systole);
                bpr.setDiastolicValue(diastole);
                bpr.setAceOrArb(aceOrArb);
                dashboard.setBloodPressure(bpr);
            }

            DatabaseUtility.closeResultSet(rs);

            /* last class */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }
            rs = cs.getResultSet();

            while (rs.next()) {
                Date date = rs.getDate("last class date");
                dashboard.setLastClass(date);
            }

            DatabaseUtility.closeResultSet(rs);

            /* eye screening */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }
            rs = cs.getResultSet();

            while (rs.next()) {
                CategoricalResult cr = new CategoricalResult();
                Date date = rs.getDate("date");
                String code = rs.getString("eye exam");
                cr.setDateRecorded(date);
                cr.setCategory(code);
                dashboard.setEyeScreening(cr);
            }

            DatabaseUtility.closeResultSet(rs);

            /* foot screening */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }
            rs = cs.getResultSet();

            while (rs.next()) {
                CategoricalResult cr = new CategoricalResult();
                Date date = rs.getDate("date");
                String code = rs.getString("foot screening");
                cr.setDateRecorded(date);
                cr.setCategory(code);
                dashboard.setFootScreening(cr);
            }

            DatabaseUtility.closeResultSet(rs);

            /* psychological screening */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }
            rs = cs.getResultSet();

            while (rs.next()) {
                PsychologicalScreeningResult psr
                        = new PsychologicalScreeningResult();
                Date date = rs.getDate("date");
                Integer score = rs.getInt("psychological screening");
                psr.setDate(date);
                psr.setScore(score);
                dashboard.setPsychologicalScreening(psr);
            }

            DatabaseUtility.closeResultSet(rs);

            /* physical activity */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }
            rs = cs.getResultSet();

            while (rs.next()) {
                DiscreteResult dr = new DiscreteResult();
                Date date = rs.getDate("date");
                Integer min = rs.getInt("physical activity (min)");
                dr.setDate(date);
                dr.setValue(min);
                dashboard.setPhysicalActivity(dr);
            }

            DatabaseUtility.closeResultSet(rs);

            /* influenza vaccine */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }
            rs = cs.getResultSet();

            while (rs.next()) {
                Date date = rs.getDate("influenza vaccine date");
                dashboard.setInfluenzaVaccine(date);
            }

            DatabaseUtility.closeResultSet(rs);

            /* PCV-13 */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }
            rs = cs.getResultSet();

            while (rs.next()) {
                Date date = rs.getDate("pcv-13 date");
                dashboard.setPcv13Vaccine(date);
            }

            DatabaseUtility.closeResultSet(rs);

            /* PPSV-23 */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }
            rs = cs.getResultSet();

            while (rs.next()) {
                Date date = rs.getDate("ppsv-23 date");
                dashboard.setPpsv23Vaccine(date);
            }

            DatabaseUtility.closeResultSet(rs);

            /* hepatitis B */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }
            rs = cs.getResultSet();

            while (rs.next()) {
                Date date = rs.getDate("hep b date");
                dashboard.setHepatitisBVaccine(date);
            }

            DatabaseUtility.closeResultSet(rs);

            /* TDAP */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }
            rs = cs.getResultSet();

            while (rs.next()) {
                Date date = rs.getDate("tdap date");
                dashboard.setTdapVaccine(date);
            }

            DatabaseUtility.closeResultSet(rs);

            /* zoster vaccine */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }
            rs = cs.getResultSet();

            while (rs.next()) {
                Date date = rs.getDate("zoster date");
                dashboard.setZosterVaccine(date);
            }

            DatabaseUtility.closeResultSet(rs);

            /* smoking status */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }
            rs = cs.getResultSet();

            while (rs.next()) {
                BooleanResult br = new BooleanResult();
                Date date = rs.getDate("date");
                Boolean status = rs.getBoolean("smoker");
                br.setDate(date);
                br.setValue(status);
                dashboard.setSmokingStatus(br);
            }

            DatabaseUtility.closeResultSet(rs);

            /* telephone follow up */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }
            rs = cs.getResultSet();

            while (rs.next()) {
                CategoricalResult cr = new CategoricalResult();
                Date date = rs.getDate("date");
                String code = rs.getString("follow-up");
                cr.setDateRecorded(date);
                cr.setCategory(code);
                dashboard.setTelephoneFollowUp(cr);
            }

            DatabaseUtility.closeResultSet(rs);

            /* AST */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }
            rs = cs.getResultSet();

            while (rs.next()) {
                ContinuousResult cr = new ContinuousResult();
                Date date = rs.getDate("date");
                BigDecimal value = rs.getBigDecimal("ast");
                cr.setDate(date);
                cr.setValue(value);
                dashboard.setAst(cr);
            }

            DatabaseUtility.closeResultSet(rs);

            /* ALT */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }
            rs = cs.getResultSet();

            while (rs.next()) {
                ContinuousResult cr = new ContinuousResult();
                Date date = rs.getDate("date");
                BigDecimal value = rs.getBigDecimal("alt");
                cr.setDate(date);
                cr.setValue(value);
                dashboard.setAlt(cr);
            }

            DatabaseUtility.closeResultSet(rs);

            /* PSA */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }
            rs = cs.getResultSet();

            while (rs.next()) {
                ContinuousResult cr = new ContinuousResult();
                Date date = rs.getDate("date");
                BigDecimal value = rs.getBigDecimal("psa");
                cr.setDate(date);
                cr.setValue(value);
                dashboard.setPsa(cr);
            }

            DatabaseUtility.closeResultSet(rs);

            /* ER */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }
            rs = cs.getResultSet();

            while (rs.next()) {
                Date date = rs.getDate("ER visit date");
                dashboard.setEr(date);
            }

            DatabaseUtility.closeResultSet(rs);

            /* healthy status */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }
            rs = cs.getResultSet();

            HealthyTargetStatus hts = new HealthyTargetStatus();
            while (rs.next()) {
                String measurement = rs.getString("measurement");
                switch (measurement) {
                    case "a1c":
                        hts.setA1cOutOfTarget(rs.getBoolean("out of target"));
                        break;
                    case "glucoseac":
                        hts.setGlucoseacOutOfTarget(rs.getBoolean("out of target"));
                        break;
                    case "glucosepc":
                        hts.setGlucosepcOutOfTarget(rs.getBoolean("out of target"));
                        break;
                    case "ldl":
                        hts.setLdlOutOfTarget(rs.getBoolean("out of target"));
                        break;
                    case "ldlpostmi":
                        hts.setLdlPostMiOutOfTarget(rs.getBoolean("out of target"));
                        break;
                    case "hdlmale":
                        hts.setHdlMaleOutOfTarget(rs.getBoolean("out of target"));
                        break;
                    case "hdlfemale":
                        hts.setHdlFemaleOutOfTarget(rs.getBoolean("out of target"));
                        break;
                    case "triglycerides":
                        hts.setTriglyceridesOutOfTarget(rs.getBoolean("out of target"));
                        break;
                    case "tsh":
                        hts.setTshOutOfTarget(rs.getBoolean("out of target"));
                        break;
                    case "t4":
                        hts.setT4OutOfTarget(rs.getBoolean("out of target"));
                        break;
                    case "uacr":
                        hts.setUacrOutOfTarget(rs.getBoolean("out of target"));
                        break;
                    case "egfr":
                        hts.setEgfrOutOfTarget(rs.getBoolean("out of target"));
                        break;
                    case "creatinine":
                        hts.setCreatinineOutOfTarget(rs.getBoolean("out of target"));
                        break;
                    case "bmi":
                        hts.setBmiOutOfTarget(rs.getBoolean("out of target"));
                        break;
                    case "waistmale":
                        hts.setWaistMaleOutOfTarget(rs.getBoolean("out of target"));
                        break;
                    case "waistfemale":
                        hts.setWaistFemaleOutOfTarget(rs.getBoolean("out of target"));
                        break;
                    case "bloodpressuresystole":
                        hts.setSystolicBloodPressureOutOfTarget(rs.getBoolean("out of target"));
                        break;
                    case "bloodpressurediastole":
                        hts.setDiastolicBloodPressureOutOfTarget(rs.getBoolean("out of target"));
                        break;
                    case "class":
                        hts.setClassAttendanceOutOfTarget(rs.getBoolean("out of target"));
                        break;
                    case "eye":
                        hts.setEyeScreeningOutOfTarget(rs.getBoolean("out of target"));
                        break;
                    case "foot":
                        hts.setFootScreeningOutOfTarget(rs.getBoolean("out of target"));
                        break;
                    case "psychologicalscreening":
                        hts.setPsychologicalScreeningOutOfTarget(rs.getBoolean("out of target"));
                        break;
                    case "physicalactivity":
                        hts.setPhysicalActivityOutOfTarget(rs.getBoolean("out of target"));
                        break;
                    case "influenzavaccine":
                        hts.setInfluenzaVaccineOutOfTarget(rs.getBoolean("out of target"));
                        break;
                    case "pcv13":
                        hts.setPcv13VaccineOutOfTarget(rs.getBoolean("out of target"));
                        break;
                    case "ppsv23":
                        hts.setPpsv23VaccineOutOfTarget(rs.getBoolean("out of target"));
                        break;
                    case "hepatitisb":
                        hts.setHepatitisBVaccineOutOfTarget(rs.getBoolean("out of target"));
                        break;
                    case "tdap":
                        hts.setTdapVaccineOutOfTarget(rs.getBoolean("out of target"));
                        break;
                    case "zoster":
                        hts.setZosterVaccineOutOfTarget(rs.getBoolean("out of target"));
                        break;
                    case "smoking":
                        hts.setSmokingStatusOutOfTarget(rs.getBoolean("out of target"));
                        break;
                    case "telephonefollowup":
                        hts.setTelephoneFollowUpOutOfTarget(rs.getBoolean("out of target"));
                        break;
                    case "ast":
                        hts.setAstOutOfTarget(rs.getBoolean("out of target"));
                        break;
                    case "alt":
                        hts.setAltOutOfTarget(rs.getBoolean("out of target"));
                        break;
                    case "psa":
                        hts.setPsaOutOfTarget(rs.getBoolean("out of target"));
                        break;
                    case "hospitalization":
                        hts.setHospitalizationOutOfTarget(rs.getBoolean("out of target"));
                        break;
                    default:
                        break;
                }
            }

            DatabaseUtility.closeResultSet(rs);

            /* last glucose fasting */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }
            rs = cs.getResultSet();

            while (rs.next()) {
                hts.setLastGlucoseFasting(rs.getBoolean("ac"));
            }

            dashboard.setHts(hts);

            DatabaseUtility.closeResultSet(rs);

            /* therapy */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }
            rs = cs.getResultSet();

            while (rs.next()) {
                Therapy th = new Therapy();
                Date date = rs.getDate("date reviewed");
                String rxClass = rs.getString("rx class");
                th.setDateReviewed(date);
                th.setPrescriptionClass(rxClass);
                dashboard.setTherapy(th);
            }

            DatabaseUtility.closeResultSet(rs);

            /* medications */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }
            rs = cs.getResultSet();

            ArrayList<Medication> meds = new ArrayList<>();
            while (rs.next()) {
                Medication m = new Medication();
                Date date = rs.getDate("date reviewed");
                String medId = rs.getString("med id");
                m.setDateReviewed(date);
                m.setMedicationId(medId);
                meds.add(m);
            }
            if (meds.size() > 0) {
                dashboard.setMedication(meds);
            }

        } catch (SQLException ex) {
            Logger.getLogger(PatientHistoryDataAccess.class.getName()).log(Level.SEVERE,
                    "An exception occurred during the getPatientDashboard method.", ex);
            return null;
        } finally {
            DatabaseUtility.closeResultSet(rs);
            DatabaseUtility.closeCallableStatement(cs);
            pool.freeConnection(connection);
        }

        return dashboard;
    }

    /**
     * This method returns the list of topic-specific notes for a patient.
     *
     * @param patientId the patient ID
     * @param topic the topic
     * @param referenceCharacters the character string
     * @return the list of topic-specific notes or null if results are missing
     */
    public static ArrayList<CategoricalResult> getNotes(Integer patientId,
            String topic, Object referenceCharacters) {
        ArrayList<CategoricalResult> notes
                = new ArrayList<>();
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        CallableStatement cs = null;
        ResultSet rs = null;

        try {
            cs = connection.prepareCall("{CALL getNotes(?, ?, ?, ?)}");
            cs.setInt(1, patientId);
            cs.setString(2, topic);
            cs.setString(3, (String) referenceCharacters);
            cs.registerOutParameter(4, java.sql.Types.TINYINT);

            /* reads true if result set exists */
            boolean success = cs.execute();
            if (!success) {
                return null;
            }

            rs = cs.getResultSet();
            while (rs.next()) {
                CategoricalResult r = new CategoricalResult();
                r.setDateRecorded(rs.getDate("date recorded"));
                r.setDefinition(rs.getString("note"));
                notes.add(r);
            }

        } catch (SQLException ex) {
            Logger.getLogger(PatientHistoryDataAccess.class.getName()).log(Level.SEVERE,
                    "An exception occurred during the getNotes method.", ex);
            return null;
        } finally {
            DatabaseUtility.closeResultSet(rs);
            DatabaseUtility.closeCallableStatement(cs);
            pool.freeConnection(connection);
        }

        if (notes.isEmpty()) {
            return null;
        } else {
            return notes;
        }
    }
}
