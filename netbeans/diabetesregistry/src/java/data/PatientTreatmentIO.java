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
import clinic.Medication;
import clinic.Therapy;
import clinic.TreatmentHistory;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.DatabaseUtil;

/**
 * Accesses the database to update and retrieve treatment information
 *
 * @author Bryan Daniel
 * @version 1, April 8, 2016
 */
public class PatientTreatmentIO {

    /**
     * Connects to the database to add a treatment for a patient
     *
     * @param patientId the patient id
     * @param rxClass the prescription class
     * @param medications the medications
     * @param thisDate the date
     * @param userName the user name
     * @param clinicId the clinic id
     * @return the boolean indicating the success of the operation
     */
    public static boolean addTreatment(int patientId, String rxClass,
            String[] medications, Date thisDate, String userName,
            int clinicId) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        CallableStatement cs = null;

        try {
            connection.setAutoCommit(false);
            cs = connection.prepareCall("{CALL addTreatment(?, ?, ?, ?, ?, ?)}");

            for (String s : medications) {
                cs.setInt(1, patientId);
                cs.setString(2, rxClass);
                cs.setString(3, s);
                cs.setDate(4, thisDate);
                cs.setString(5, userName);
                cs.setInt(6, clinicId);
                cs.addBatch();
            }

            /* no result set to return */
            cs.executeBatch();
            connection.commit();
            connection.setAutoCommit(true);

        } catch (SQLException ex) {
            try {
                connection.rollback();
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                Logger.getLogger(ReferencesIO.class.getName())
                        .log(Level.SEVERE, null, e);
            }
            Logger.getLogger(ReferencesIO.class.getName())
                    .log(Level.SEVERE, null, ex);
        } finally {
            DatabaseUtil.closeCallableStatement(cs);
            pool.freeConnection(connection);
        }
        return true;
    }

    /**
     * Connects to the database to get the treatment history of a patient
     *
     * @param patientId the patient id
     * @return the boolean indicating the success of the operation
     */
    public static TreatmentHistory getTreatments(Integer patientId) {
        TreatmentHistory treatments = new TreatmentHistory();
        ArrayList<Therapy> therapies = new ArrayList<>();
        ArrayList<Medication> medications = new ArrayList<>();
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        CallableStatement cs = null;
        ResultSet rs = null;

        try {
            cs = connection.prepareCall("{CALL getTreatment(?, ?)}");
            cs.setInt(1, patientId);
            cs.registerOutParameter(2, java.sql.Types.TINYINT);

            /* therapies */
            boolean success = cs.execute();
            if (!success) {
                return null;
            }

            rs = cs.getResultSet();
            while (rs.next()) {
                Therapy th = new Therapy();
                th.setPrescriptionClass(rs.getString("rx class"));
                th.setTherapyType(rs.getString("therapy type"));
                th.setDateReviewed(rs.getDate("date recorded"));
                therapies.add(th);
            }

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
                m.setDateReviewed(rs.getDate("date recorded"));
                medications.add(m);
            }

            if (therapies.isEmpty()) {
                treatments.setTherapies(null);
            } else {
                treatments.setTherapies(therapies);
            }
            if (medications.isEmpty()) {
                treatments.setMedications(null);
            } else {
                treatments.setMedications(medications);
            }

        } catch (SQLException ex) {
            Logger.getLogger(ReferencesIO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DatabaseUtil.closeResultSet(rs);
            DatabaseUtil.closeCallableStatement(cs);
            pool.freeConnection(connection);
        }
        return treatments;
    }
}
