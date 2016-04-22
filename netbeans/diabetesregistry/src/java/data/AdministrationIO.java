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
import clinic.ClinicRegistration;
import clinic.User;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.DatabaseUtil;

/**
 * Accesses the database for administration functions
 *
 * @author Bryan Daniel
 * @version 1, April 8, 2016
 */
public class AdministrationIO {

    /**
     * This method returns a clinic associated with the given clinic ID.
     *
     * @param clinicId the clinic ID
     * @return the clinic object
     */
    public static Clinic getClinic(int clinicId) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        CallableStatement cs = null;
        ResultSet rs = null;

        try {
            cs = connection.prepareCall("{CALL getClinic(?, ?)}");
            cs.setInt(1, clinicId);
            cs.registerOutParameter(2, java.sql.Types.INTEGER);
            boolean success = cs.execute();
            if (!success) {
                return null;
            }

            rs = cs.getResultSet();
            Clinic clinic = new Clinic();
            while (rs.next()) {
                clinic.setClinicId(clinicId);
                clinic.setClinicName(rs.getString("name"));
                clinic.setAddress(rs.getString("address"));
                clinic.setPhoneNumber(rs.getString("phone number"));
                clinic.setEmailAddress(rs.getString("email"));
            }
            return clinic;

        } catch (SQLException e) {
            System.err.println(e);
            return null;
        } finally {
            DatabaseUtil.closeResultSet(rs);
            DatabaseUtil.closeCallableStatement(cs);
            pool.freeConnection(connection);
        }
    }

    /**
     * This method invokes the database procedure for adding a clinic.
     *
     * @param clinicName the clinic name
     * @param address the clinic address
     * @param phoneNumber the clinic phone number
     * @param emailAddress the clinic email address
     * @param registrationKey the registration key
     * @param salt the salt
     * @return the success boolean
     */
    public static boolean addClinic(String clinicName, String address,
            String phoneNumber, String emailAddress, String registrationKey,
            String salt) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        CallableStatement cs = null;

        try {
            cs = connection.prepareCall("{CALL addClinic(?, ?, ?, ?, ?, ?, ?)}");
            cs.setString(1, clinicName);
            cs.setString(2, address);
            cs.setString(3, phoneNumber);
            if ((emailAddress != null) && (emailAddress.trim().length() != 0)) {
                cs.setString(4, emailAddress);
            } else {
                cs.setNull(4, java.sql.Types.VARCHAR);
            }
            cs.setString(5, registrationKey);
            cs.setString(6, salt);
            cs.registerOutParameter(7, java.sql.Types.TINYINT);

            /* no result set to return */
            cs.execute();

            /* grab out parameter */
            boolean success = cs.getBoolean(7);
            if (!success) {
                return false;
            }

        } catch (SQLException e) {
            System.err.println(e);
            return false;
        } finally {
            DatabaseUtil.closeCallableStatement(cs);
            pool.freeConnection(connection);
        }
        return true;
    }

    /**
     * Updates a clinic's information in the database
     *
     * @param clinicId the clinic ID
     * @param clinicName the clinic name
     * @param address the address
     * @param phoneNumber the phone number
     * @param emailAddress the email address
     * @param registrationKey the registration key
     * @param salt the salt
     * @return the boolean for update success
     */
    public static boolean updateClinic(int clinicId, String clinicName,
            String address, String phoneNumber, String emailAddress,
            String registrationKey, String salt) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        CallableStatement cs = null;

        try {
            connection.setAutoCommit(false);
            cs = connection.prepareCall("{CALL updateClinic(?, ?, ?, ?, ?, ?, "
                    + "?)}");
            cs.setInt(1, clinicId);
            cs.setString(2, clinicName);
            cs.setString(3, address);
            cs.setString(4, phoneNumber);
            if ((emailAddress != null) && (emailAddress.trim().length() != 0)) {
                cs.setString(5, emailAddress);
            } else {
                cs.setNull(5, java.sql.Types.VARCHAR);
            }
            if ((registrationKey != null) && (registrationKey.trim().length() != 0)) {
                cs.setString(6, registrationKey);
            } else {
                cs.setNull(6, java.sql.Types.VARCHAR);
            }
            if ((salt != null) && (salt.trim().length() != 0)) {
                cs.setString(7, salt);
            } else {
                cs.setNull(7, java.sql.Types.VARCHAR);
            }

            /* no result set to return */
            cs.execute();
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
     * This method returns the list of registry user names.
     *
     * @return the list of user names
     */
    public static ArrayList<String> getUserNames() {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        CallableStatement cs = null;
        ResultSet rs = null;
        ArrayList<String> userNames = new ArrayList<>();

        try {
            cs = connection.prepareCall("{CALL getAllUsers(?)}");
            cs.registerOutParameter(1, java.sql.Types.INTEGER);
            boolean success = cs.execute();
            if (!success) {
                return null;
            }

            rs = cs.getResultSet();
            while (rs.next()) {
                String userName = rs.getString("user name");
                userNames.add(userName);
            }

        } catch (SQLException e) {
            System.err.println(e);
            return null;
        } finally {
            DatabaseUtil.closeResultSet(rs);
            DatabaseUtil.closeCallableStatement(cs);
            pool.freeConnection(connection);
        }
        return userNames;
    }

    /**
     * This method returns the user details for the user name given.
     *
     * @param userName the user name
     * @return the user details
     */
    public static User getUserDetails(String userName) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        CallableStatement cs = null;
        ResultSet rs = null;
        User detailedUser = null;

        try {
            cs = connection.prepareCall("{CALL getUserDetails(?, ?)}");
            cs.setString(1, userName);
            cs.registerOutParameter(2, java.sql.Types.INTEGER);
            boolean success = cs.execute();
            if (!success) {
                return null;
            }

            rs = cs.getResultSet();
            while (rs.next()) {
                detailedUser = new User();
                detailedUser.setUserName(userName);
                detailedUser.setFirstName(rs.getString("first name"));
                detailedUser.setLastName(rs.getString("last name"));
                detailedUser.setJobTitle(rs.getString("job title"));
                detailedUser.setAdministrator(rs.getBoolean("administrator"));
                detailedUser.setActive(rs.getBoolean("active"));
                detailedUser.setDateJoined(rs.getTimestamp("date joined"));
                detailedUser.setLastLogin(rs.getTimestamp("last login"));
            }
            if (detailedUser != null) {

                DatabaseUtil.closeResultSet(rs);
                success = cs.getMoreResults();
                if (!success) {
                    return null;
                }

                rs = cs.getResultSet();
                ArrayList<Clinic> clinics = new ArrayList<>();
                while (rs.next()) {
                    Clinic clinic = new Clinic();
                    clinic.setClinicId(rs.getInt("clinic id"));
                    clinic.setClinicName(rs.getString("clinic name"));
                    clinic.setAddress(rs.getString("address"));
                    clinic.setPhoneNumber(rs.getString("phone number"));
                    clinics.add(clinic);
                }
                detailedUser.setClinics(clinics);
            }

        } catch (SQLException e) {
            System.err.println(e);
            return null;
        } finally {
            DatabaseUtil.closeResultSet(rs);
            DatabaseUtil.closeCallableStatement(cs);
            pool.freeConnection(connection);
        }
        return detailedUser;
    }

    /**
     * Updates a user's access permissions in the database
     *
     * @param userName the user name
     * @param removeUser boolean for user registration removal
     * @param clinicId the clinic ID
     * @param terminate the boolean for user termination
     * @param newRegistrations the new clinic registration keys
     * @return the boolean for update success
     */
    public static boolean updateUserAccess(String userName,
            boolean removeUser, Integer clinicId, boolean terminate,
            ArrayList<ClinicRegistration> newRegistrations) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        CallableStatement cs = null;

        try {
            connection.setAutoCommit(false);
            cs = connection.prepareCall("{CALL updateClinic(?, ?, ?, ?, ?, ?, "
                    + "?)}");
            connection.setAutoCommit(false);
            if (newRegistrations.size() > 0) {
                for (ClinicRegistration cr : newRegistrations) {
                    Clinic c = cr.getClinic();
                    cs.setInt(1, c.getClinicId());
                    cs.setString(2, c.getClinicName());
                    cs.setString(3, c.getAddress());
                    cs.setString(4, c.getPhoneNumber());
                    if ((c.getEmailAddress() != null)
                            && (c.getEmailAddress().trim().length() != 0)) {
                        cs.setString(5, c.getEmailAddress());
                    } else {
                        cs.setNull(5, java.sql.Types.VARCHAR);
                    }
                    if ((cr.getHashedKey() != null)
                            && (cr.getHashedKey().trim().length() != 0)) {
                        cs.setString(6, cr.getHashedKey());
                    } else {
                        cs.setNull(6, java.sql.Types.VARCHAR);
                    }
                    if ((cr.getSalt() != null)
                            && (cr.getSalt().trim().length() != 0)) {
                        cs.setString(7, cr.getSalt());
                    } else {
                        cs.setNull(7, java.sql.Types.VARCHAR);
                    }
                    cs.addBatch();
                }

                /* no result set to return */
                cs.executeBatch();
            }

            cs = connection.prepareCall("{CALL updateUserAccess(?, ?, ?, ?)}");
            cs.setString(1, userName);
            cs.setBoolean(2, removeUser);
            if (clinicId != null) {
                cs.setInt(3, clinicId);
            } else {
                cs.setNull(3, java.sql.Types.INTEGER);
            }
            cs.setBoolean(4, terminate);
            cs.addBatch();

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
}
