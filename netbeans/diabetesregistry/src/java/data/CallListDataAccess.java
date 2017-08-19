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
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import registry.EmailMessage;
import registry.Patient;
import utility.ConnectionPool;
import utility.DatabaseUtility;

/**
 * This data-access class retrieves patient call lists and email messages.
 *
 * @author Bryan Daniel
 * @version 2, March 16, 2017
 */
public class CallListDataAccess {

    /**
     * This enum indicates a type for a last measurement date collected in a
     * call list.
     */
    public enum LastMeasurementDateType {

        /**
         * The type indicating a blood pressure measurement
         */
        BP,
        
        /**
         * The type indicating an A1C measurement
         */
        A1C,
        
        /**
         * The type indicating no measurement
         */
        NONE
    }

    /**
     * The label for blood pressure measurement date
     */
    private static final String BP_DATE_LABEL = "last BP date";

    /**
     * The label for A1C measurement date
     */
    private static final String A1C_DATE_LABEL = "last A1C date";

    /**
     * This method uses the getCallList procedure in the database to find and
     * return the list of patients to be contacted.
     *
     * @param clinicId the clinic ID
     * @param subject the call list subject
     * @param dateType the last measurement date type determined from the result
     * set
     * @param referenceCharacters the character string
     * @return the list of patients or null if no list is found
     */
    public static ArrayList<Patient> getCallList(int clinicId, String subject,
            LastMeasurementDateType[] dateType, Object referenceCharacters) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        CallableStatement cs = null;
        ResultSet rs = null;
        ArrayList<Patient> callListPatients = new ArrayList<>();

        try {
            cs = connection.prepareCall("{CALL getCallList(?, ?, ?, ?)}");
            cs.setInt(1, clinicId);
            cs.setString(2, subject);
            cs.setString(3, (String) referenceCharacters);
            cs.registerOutParameter(4, java.sql.Types.INTEGER);

            /* reads true if result set exists */
            boolean success = cs.execute();
            if (!success) {
                return null;
            }

            rs = cs.getResultSet();

            /* retrieving the column labels to determine if last measurement date will be set */
            ResultSetMetaData metaData = rs.getMetaData();
            Set<String> columnLabels = new HashSet();
            String dateOfLastMeasurementLabel = "";
            for (int i = 1; i < metaData.getColumnCount() + 1; i++) {
                String columnLabel = metaData.getColumnLabel(i);
                if (!columnLabels.contains(columnLabel)) {
                    columnLabels.add(columnLabel);
                }
            }
            if (columnLabels.contains(BP_DATE_LABEL)) {
                dateOfLastMeasurementLabel = BP_DATE_LABEL;
                dateType[0] = CallListDataAccess.LastMeasurementDateType.BP;
            } else if (columnLabels.contains(A1C_DATE_LABEL)) {
                dateOfLastMeasurementLabel = A1C_DATE_LABEL;
                dateType[0] = CallListDataAccess.LastMeasurementDateType.A1C;
            } else {
                dateType[0] = CallListDataAccess.LastMeasurementDateType.NONE;
            }

            while (rs.next()) {
                Patient p = new Patient();
                p.setPatientId(rs.getInt("patient id"));
                p.setFirstName(rs.getString("first name"));
                p.setLastName(rs.getString("last name"));
                p.setBirthDate(rs.getDate("birth date"));
                p.setContactNumber(rs.getString("contact number"));
                p.setEmailAddress(rs.getString("email"));
                p.setLanguage(rs.getString("language"));

                switch (dateOfLastMeasurementLabel) {
                    case BP_DATE_LABEL:
                        p.setDateOfLastMeasurement(rs.getDate(BP_DATE_LABEL));
                        break;
                    case A1C_DATE_LABEL:
                        p.setDateOfLastMeasurement(rs.getDate(A1C_DATE_LABEL));
                        break;
                    default:
                        break;
                }
                callListPatients.add(p);
            }

        } catch (SQLException ex) {
            Logger.getLogger(CallListDataAccess.class.getName()).log(Level.SEVERE,
                    "An exception occurred during the getCallList method.", ex);
        } finally {
            DatabaseUtility.closeResultSet(rs);
            DatabaseUtility.closeCallableStatement(cs);
            pool.freeConnection(connection);
        }

        if (callListPatients.size() > 0) {
            return callListPatients;
        } else {
            return null;
        }
    }

    /**
     * This method uses the getEmailMessages procedure in the database to find
     * and return the list of email messages to be sent to selected patients in
     * the call list.
     *
     * @param clinicId the clinic ID
     * @param subject the call list subject
     * @return the list of email messages or null if no messages are found
     */
    public static ArrayList<EmailMessage> getEmailMessages(int clinicId, String subject) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        CallableStatement cs = null;
        ResultSet rs = null;
        ArrayList<EmailMessage> emailMessages = new ArrayList<>();

        try {
            cs = connection.prepareCall("{CALL getEmailMessages(?, ?, ?)}");
            cs.setInt(1, clinicId);
            cs.setString(2, subject);
            cs.registerOutParameter(3, java.sql.Types.INTEGER);

            /* reads true if result set exists */
            boolean success = cs.execute();
            if (!success) {
                return null;
            }

            rs = cs.getResultSet();
            while (rs.next()) {
                EmailMessage em = new EmailMessage();
                em.setSubject(subject);
                em.setLanguage(rs.getString("language"));
                em.setMessage(rs.getString("message"));
                emailMessages.add(em);
            }

        } catch (SQLException ex) {
            Logger.getLogger(CallListDataAccess.class.getName()).log(Level.SEVERE,
                    "An exception ocurred during the getEmailMessages method.", ex);
        } finally {
            DatabaseUtility.closeResultSet(rs);
            DatabaseUtility.closeCallableStatement(cs);
            pool.freeConnection(connection);
        }

        if (emailMessages.size() > 0) {
            return emailMessages;
        } else {
            return null;
        }
    }
}
