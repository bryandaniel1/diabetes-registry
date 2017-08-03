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
import clinic.Patient;
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
 * Accesses the database during the patient add and update processes
 *
 * @author Bryan Daniel
 * @version 1, April 8, 2016
 */
public class PatientIO {

    /**
     * Returns the list of patients
     *
     * @param clinicId the clinic ID
     * @param referenceCharacters the character string
     * @return the list of patients
     */
    public static ArrayList<Patient> getPatients(int clinicId,
            Object referenceCharacters) {
        ArrayList<Patient> patients = new ArrayList<>();
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        CallableStatement cs = null;
        ResultSet rs = null;

        try {
            cs = connection.prepareCall("{CALL getAllPatients(?, ?, ?)}");
            cs.setInt(1, clinicId);
            cs.setString(2, (String) referenceCharacters);
            cs.registerOutParameter(3, java.sql.Types.INTEGER);

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
                p.setAddress(rs.getString("address"));
                p.setContactNumber(rs.getString("contact number"));
                p.setGender(rs.getString("gender"));
                p.setRace(rs.getString("race"));
                p.setEmailAddress(rs.getString("email"));
                p.setLanguage(rs.getString("language"));
                p.setReasonForInactivity(rs.getString("reason"));
                p.setStartDate(rs.getDate("start date"));
                patients.add(p);
            }

        } catch (SQLException ex) {
            Logger.getLogger(PatientIO.class.getName()).log(Level.SEVERE, 
                    "An exception occurred in the getPatients method.", ex);
        } finally {
            DatabaseUtil.closeResultSet(rs);
            DatabaseUtil.closeCallableStatement(cs);
            pool.freeConnection(connection);
        }

        return patients;
    }

    /**
     * Updates a patient's information in the database
     *
     * @param patientId the patient id
     * @param firstName the first name
     * @param lastName the last name
     * @param birthDate the birth date
     * @param contactNumber the contact number
     * @param address the address
     * @param gender the gender
     * @param race the race
     * @param email the email address
     * @param language the language
     * @param reasonForInactivity the reason for inactivity
     * @param startDate the start date
     * @param referenceCharacters the character string
     * @return the boolean for update success
     */
    public static boolean updatePatient(int patientId, String firstName,
            String lastName, Date birthDate, String address, String contactNumber,
            String gender, String race, String email, String language,
            String reasonForInactivity, Date startDate, Object referenceCharacters) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        CallableStatement cs = null;

        try {
            cs = connection.prepareCall("{CALL updatePatient(?, ?, ?, ?, ?, ?, "
                    + "?, ?, ?, ?, ?, ?, ?, ?)}");
            cs.setInt(1, patientId);
            cs.setString(2, firstName);
            cs.setString(3, lastName);
            cs.setDate(4, birthDate);
            if ((address != null) && (address.trim().length() != 0)) {
                cs.setString(5, address);
            } else {
                cs.setNull(5, java.sql.Types.VARCHAR);
            }
            if ((contactNumber != null) && (contactNumber.trim().length() != 0)) {
                cs.setString(6, contactNumber);
            } else {
                cs.setNull(6, java.sql.Types.VARCHAR);
            }
            cs.setString(7, gender);
            cs.setString(8, race);
            if ((email != null) && (email.trim().length() != 0)) {
                cs.setString(9, email);
            } else {
                cs.setNull(9, java.sql.Types.VARCHAR);
            }
            if ((language != null) && (language.trim().length() != 0)) {
                cs.setString(10, language);
            } else {
                cs.setNull(10, java.sql.Types.VARCHAR);
            }
            if ((reasonForInactivity != null)
                    && (reasonForInactivity.trim().length() != 0)) {
                cs.setString(11, reasonForInactivity);
            } else {
                cs.setNull(11, java.sql.Types.VARCHAR);
            }
            cs.setDate(12, startDate);
            cs.setString(13, (String) referenceCharacters);
            cs.registerOutParameter(14, java.sql.Types.TINYINT);

            /* no result set to return */
            cs.execute();

            /* grab out parameter */
            boolean success = cs.getBoolean(14);
            if (!success) {
                return false;
            }

        } catch (SQLException ex) {
            Logger.getLogger(PatientIO.class.getName()).log(Level.SEVERE, 
                    "An exception occurred in the updatePatient method.", ex);
        } finally {
            DatabaseUtil.closeCallableStatement(cs);
            pool.freeConnection(connection);
        }
        return true;
    }

    /**
     * Adds a new patient's information to the database
     *
     * @param firstName the first name
     * @param lastName the last name
     * @param birthDate the birth date
     * @param address the address
     * @param contactNumber the contact number
     * @param gender the gender
     * @param race the race
     * @param email the email
     * @param language the language
     * @param startDate the start date
     * @param clinicId the clinic ID
     * @param referenceCharacters the character string
     * @return the boolean for update success
     */
    public static boolean addPatient(String firstName,
            String lastName, Date birthDate, String address, String contactNumber,
            String gender, String race, String email, String language, Date startDate,
            int clinicId, Object referenceCharacters) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        CallableStatement cs = null;

        try {
            cs = connection.prepareCall("{CALL addPatient(?, ?, ?, ?, ?, ?, "
                    + "?, ?, ?, ?, ?, ?, ?)}");
            cs.setString(1, firstName);
            cs.setString(2, lastName);
            cs.setDate(3, birthDate);
            if ((address != null) && (address.trim().length() != 0)) {
                cs.setString(4, address);
            } else {
                cs.setNull(4, java.sql.Types.VARCHAR);
            }
            if ((contactNumber != null) && (contactNumber.trim().length() != 0)) {
                cs.setString(5, contactNumber);
            } else {
                cs.setNull(5, java.sql.Types.VARCHAR);
            }
            cs.setString(6, gender);
            cs.setString(7, race);
            if ((email != null) && (email.trim().length() != 0)) {
                cs.setString(8, email);
            } else {
                cs.setNull(8, java.sql.Types.VARCHAR);
            }
            if ((language != null) && (language.trim().length() != 0)) {
                cs.setString(9, language);
            } else {
                cs.setNull(9, java.sql.Types.VARCHAR);
            }
            cs.setDate(10, startDate);
            cs.setInt(11, clinicId);
            cs.setString(12, (String) referenceCharacters);
            cs.registerOutParameter(13, java.sql.Types.TINYINT);

            /* no result set to return */
            cs.execute();

            /* grab out parameter */
            boolean success = cs.getBoolean(13);
            if (!success) {
                return false;
            }

        } catch (SQLException ex) {
            Logger.getLogger(PatientIO.class.getName()).log(Level.SEVERE, 
                    "An exception occurred in the addPatient method.", ex);
        } finally {
            DatabaseUtil.closeCallableStatement(cs);
            pool.freeConnection(connection);
        }
        return true;
    }
}
