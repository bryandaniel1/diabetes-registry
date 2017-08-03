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

import clinic.EmailMessage;
import clinic.Patient;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.ConnectionPool;
import util.DatabaseUtil;

/**
 * Connects to the database to retrieve patient call lists
 *
 * @author Bryan Daniel
 * @version 1, April 8, 2016
 */
public class CallListIO {

    /**
     * Visit reminder string
     */
    private static final String VISIT_REMINDER = "Clinic Visit Reminder";

    /**
     * Lab work reminder string
     */
    private static final String LAB_REMINDER = "Lab Work Reminder";

    /**
     * Returns the list of patients to be contacted
     *
     * @param clinicId the clinic ID
     * @param subject the call list subject
     * @param referenceCharacters the character string
     * @return the list of patients or null if no results are found
     */
    public static ArrayList<Patient> getCallList(int clinicId, String subject,
            Object referenceCharacters) {
        ArrayList<Patient> callListPatients = new ArrayList<>();
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        CallableStatement cs = null;
        ResultSet rs = null;

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
            while (rs.next()) {
                Patient p = new Patient();
                p.setPatientId(rs.getInt("patient id"));
                p.setFirstName(rs.getString("first name"));
                p.setLastName(rs.getString("last name"));
                p.setBirthDate(rs.getDate("birth date"));
                p.setContactNumber(rs.getString("contact number"));
                p.setEmailAddress(rs.getString("email"));
                p.setLanguage(rs.getString("language"));
                switch (subject) {
                    case VISIT_REMINDER:

                        /* start date variable used for BP date*/
                        p.setStartDate(rs.getDate("last BP date"));
                        break;
                    case LAB_REMINDER:

                        /* start date variable used for A1C date*/
                        p.setStartDate(rs.getDate("last A1C date"));
                        break;
                }
                callListPatients.add(p);
            }

        } catch (SQLException ex) {
            Logger.getLogger(CallListIO.class.getName()).log(Level.SEVERE, 
                    "An exception occurred in the getCallList method.", ex);
        } finally {
            DatabaseUtil.closeResultSet(rs);
            DatabaseUtil.closeCallableStatement(cs);
            pool.freeConnection(connection);
        }

        if (callListPatients.size() > 0) {
            return callListPatients;
        } else {
            return null;
        }
    }

    /**
     * Returns the list of email messages to be sent
     *
     * @param clinicId the clinic ID
     * @param subject the call list subject
     * @return the list of patients or null if no results are found
     */
    public static ArrayList<EmailMessage> getEmailMessages(int clinicId, String subject) {
        ArrayList<EmailMessage> emailMessages = new ArrayList<>();
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        CallableStatement cs = null;
        ResultSet rs = null;

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
            Logger.getLogger(CallListIO.class.getName()).log(Level.SEVERE, 
                    "An exception occurred in the getEmailMessages method.", ex);
        } finally {
            DatabaseUtil.closeResultSet(rs);
            DatabaseUtil.closeCallableStatement(cs);
            pool.freeConnection(connection);
        }

        if (emailMessages.size() > 0) {
            return emailMessages;
        } else {
            return null;
        }
    }
}
