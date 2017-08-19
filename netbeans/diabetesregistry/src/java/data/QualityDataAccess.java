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
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import registry.CategoricalResult;
import registry.QualityReference;
import utility.ConnectionPool;
import utility.DatabaseUtility;

/**
 * This class accesses the database to add and retrieve quality checklist items.
 *
 * @author Bryan Daniel
 * @version 2, March 16, 2017
 */
public class QualityDataAccess {

    /**
     * This method connects to the database to save accomplished checklist items
     * for a patient.
     *
     * @param patientId the patient ID
     * @param checklistItems the checklist items
     * @param thisDate the date
     * @param userName the user name
     * @param clinicId the clinic ID
     * @return the boolean indicating the success or failure of the operation
     */
    public static boolean saveChecklist(int patientId, Date thisDate,
            String[] checklistItems, String userName, int clinicId) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        CallableStatement cs = null;

        try {
            cs = connection.prepareCall("{CALL saveQualityChecklistItem(?, ?, ?, ?, ?)}");
            connection.setAutoCommit(false);
            for (String s : checklistItems) {
                cs.setInt(1, patientId);
                cs.setDate(2, thisDate);
                cs.setString(3, s);
                cs.setString(4, userName);
                cs.setInt(5, clinicId);
                cs.addBatch();
            }

            /* no result set to return */
            cs.executeBatch();
            connection.commit();
            connection.setAutoCommit(true);

        } catch (SQLException ex) {
            Logger.getLogger(QualityDataAccess.class.getName()).log(Level.SEVERE,
                    "An exception occurred during the saveChecklist method.", ex);
            return false;
        } finally {
            DatabaseUtility.closeCallableStatement(cs);
            pool.freeConnection(connection);
        }
        return true;
    }

    /**
     * This method connects to the database to retrieve checklist dates for a
     * patient.
     *
     * @param patientId the patient ID
     * @return the list of checklist dates or null if results are missing
     */
    public static ArrayList<Date> getChecklistDates(int patientId) {
        ArrayList<Date> checklistDates = new ArrayList<>();
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        CallableStatement cs = null;
        ResultSet rs = null;

        try {
            cs = connection.prepareCall("{CALL getChecklistDates(?, ?)}");
            cs.setInt(1, patientId);
            cs.registerOutParameter(2, java.sql.Types.INTEGER);

            /* reads true if result set exists */
            boolean success = cs.execute();
            if (!success) {
                return null;
            }

            rs = cs.getResultSet();
            while (rs.next()) {
                Date d = rs.getDate("date");
                checklistDates.add(d);
            }
            if (checklistDates.isEmpty()) {
                return null;
            }

        } catch (SQLException ex) {
            Logger.getLogger(QualityDataAccess.class.getName()).log(Level.SEVERE,
                    "An exception occurred during the getChecklistDates method.", ex);
            return null;
        } finally {
            DatabaseUtility.closeResultSet(rs);
            DatabaseUtility.closeCallableStatement(cs);
            pool.freeConnection(connection);
        }
        return checklistDates;
    }

    /**
     * This method connects to the database to retrieve accomplished checklist
     * items for a patient.
     *
     * @param patientId the patient ID
     * @param date the date
     * @param clinicId the clinic ID
     * @return the patient's list of checklist items or null if results are
     * missing
     */
    public static ArrayList<QualityReference> getChecklistItems(int patientId,
            Date date, int clinicId) {
        ArrayList<QualityReference> checklistItems = new ArrayList<>();
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        CallableStatement cs = null;
        ResultSet rs = null;

        try {
            cs = connection.prepareCall("{CALL getChecklistItems(?, ?, ?, ?)}");
            cs.setInt(1, patientId);
            cs.setDate(2, date);
            cs.setInt(3, clinicId);
            cs.registerOutParameter(4, java.sql.Types.INTEGER);

            /* reads true if result set exists */
            boolean success = cs.execute();
            if (!success) {
                return null;
            }

            rs = cs.getResultSet();
            while (rs.next()) {
                QualityReference qr = new QualityReference();
                String role = rs.getString("role");
                String responsibility = rs.getString("responsibility");
                qr.setRole(role);
                qr.setResponsibility(responsibility);
                checklistItems.add(qr);
            }
            if (checklistItems.isEmpty()) {
                return null;
            }

        } catch (SQLException ex) {
            Logger.getLogger(QualityDataAccess.class.getName()).log(Level.SEVERE,
                    "An exception occurred during the getChecklistItems method.", ex);
            return null;
        } finally {
            DatabaseUtility.closeResultSet(rs);
            DatabaseUtility.closeCallableStatement(cs);
            pool.freeConnection(connection);
        }
        return checklistItems;
    }

    /**
     * This method connects to the database to retrieve the most recent dates
     * and items for accomplished checklist items for a patient.
     *
     * @param patientId the patient ID
     * @param clinicId the clinic ID
     * @return the list of most recent checklist items for a patient or null if
     * results are missing
     */
    public static ArrayList<CategoricalResult> getMostRecentChecklistItems(int patientId,
            int clinicId) {
        ArrayList<CategoricalResult> checklistItems = new ArrayList<>();
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        CallableStatement cs = null;
        ResultSet rs = null;

        try {
            cs = connection.prepareCall("{CALL getMostRecentChecklistItems(?, ?, ?)}");
            cs.setInt(1, patientId);
            cs.setInt(2, clinicId);
            cs.registerOutParameter(3, java.sql.Types.INTEGER);

            /* reads true if result set exists */
            boolean success = cs.execute();
            if (!success) {
                return null;
            }

            rs = cs.getResultSet();
            while (rs.next()) {
                CategoricalResult cr = new CategoricalResult();
                Date date = rs.getDate("date recorded");
                String responsibility = rs.getString("responsibility");
                cr.setDateRecorded(date);
                cr.setCategory(responsibility);
                if (!checklistItems.contains(cr)) {
                    checklistItems.add(cr);
                }
            }
            if (checklistItems.isEmpty()) {
                return null;
            }

        } catch (SQLException ex) {
            Logger.getLogger(QualityDataAccess.class.getName()).log(Level.SEVERE,
                    "An exception occurred during the getMostRecentChecklistItems method.", ex);
            return null;
        } finally {
            DatabaseUtility.closeResultSet(rs);
            DatabaseUtility.closeCallableStatement(cs);
            pool.freeConnection(connection);
        }
        return checklistItems;
    }
}
