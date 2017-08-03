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
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.DatabaseUtil;

/**
 * Accesses the database during the registration process
 *
 * @author Bryan Daniel
 * @version 1, April 8, 2016
 */
public class RegistrationIO {

    /**
     * Constant to set any new user's administrator indicator to false
     */
    private static final Integer administrator = 0;

    /**
     * This method returns a clinic id associated with the given registration
     * key.
     *
     * @param registrationKey the registration key
     * @return the user object
     */
    public static Integer checkRegistrationKey(String registrationKey) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        CallableStatement cs = null;
        ResultSet rs = null;

        try {
            cs = connection.prepareCall("{CALL checkRegistrationKey(?, ?, ?)}");
            cs.setString(1, registrationKey);
            cs.registerOutParameter(2, java.sql.Types.INTEGER);
            cs.registerOutParameter(3, java.sql.Types.INTEGER);
            boolean success = cs.execute();
            if (!success) {
                return null;
            }

            rs = cs.getResultSet();
            Integer clinicId = null;
            while (rs.next()) {
                clinicId = rs.getInt("clinic id");
            }
            return clinicId;

        } catch (SQLException e) {
            Logger.getLogger(RegistrationIO.class.getName()).log(Level.SEVERE, 
                    "An exception occurred in the checkRegistrationKey method.", e);
            return null;
        } finally {
            DatabaseUtil.closeResultSet(rs);
            DatabaseUtil.closeCallableStatement(cs);
            pool.freeConnection(connection);
        }
    }

    /**
     * This method returns a salt associated with the given clinic id.
     *
     * @param clinicId the clinic id
     * @return the user object
     */
    public static String retrieveClinicSalt(Integer clinicId) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        CallableStatement cs = null;
        ResultSet rs = null;

        try {
            cs = connection.prepareCall("{CALL retrieveClinicSalt(?, ?)}");
            cs.setInt(1, clinicId);
            cs.registerOutParameter(2, java.sql.Types.INTEGER);
            boolean success = cs.execute();
            if (!success) {
                return null;
            }

            rs = cs.getResultSet();
            String clinicSalt = null;
            while (rs.next()) {
                clinicSalt = rs.getString("clinic salt");
            }
            return clinicSalt;

        } catch (SQLException e) {
            Logger.getLogger(RegistrationIO.class.getName()).log(Level.SEVERE, 
                    "An exception occurred in the retrieveClinicSalt method.", e);
            return null;
        } finally {
            DatabaseUtil.closeResultSet(rs);
            DatabaseUtil.closeCallableStatement(cs);
            pool.freeConnection(connection);
        }
    }

    /**
     * This method invokes the database procedure for registering a user.
     *
     * @param userName the user name
     * @param firstName the first name
     * @param lastName the last name
     * @param jobTitle the job title
     * @param userPassword the password
     * @param userSalt the salt
     * @param clinicKey the key
     * @return the success boolean
     */
    public static boolean registerUser(String userName, String firstName,
            String lastName, String jobTitle, String userPassword,
            String userSalt, String clinicKey) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        CallableStatement cs = null;
        ResultSet rs = null;

        try {
            cs = connection.prepareCall("{CALL registerUser(?, ?, ?, ?, ?, ?, ?, ?, ?)}");
            cs.setString(1, userName);
            cs.setString(2, firstName);
            cs.setString(3, lastName);
            cs.setString(4, jobTitle);
            cs.setInt(5, administrator);
            cs.setString(6, userPassword);
            cs.setString(7, userSalt);
            cs.setString(8, clinicKey);
            cs.registerOutParameter(9, java.sql.Types.INTEGER);

            boolean success = cs.execute();
            if (!success) {
                return false;
            }

            rs = cs.getResultSet();
            int successNumber = 0;
            while (rs.next()) {
                successNumber = rs.getInt("success");
            }
            if (successNumber == 1) {
                return true;
            } else {
                return false;
            }

        } catch (SQLException e) {
            Logger.getLogger(RegistrationIO.class.getName()).log(Level.SEVERE, 
                    "An exception occurred in the registerUser method.", e);
            return false;
        } finally {
            DatabaseUtil.closeResultSet(rs);
            DatabaseUtil.closeCallableStatement(cs);
            pool.freeConnection(connection);
        }
    }

    /**
     * This method invokes the database procedure for registering an existing
     * user.
     *
     * @param userName the user name
     * @param userPassword the password
     * @param clinicKey the key
     * @return the success boolean
     */
    public static boolean registerExistingUser(String userName, String userPassword,
            String clinicKey) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        CallableStatement cs = null;
        ResultSet rs = null;

        try {
            cs = connection.prepareCall("{CALL addNewRegistrationToExistingUser(?, ?, ?, ?)}");
            cs.setString(1, userName);
            cs.setString(2, userPassword);
            cs.setString(3, clinicKey);
            cs.registerOutParameter(4, java.sql.Types.INTEGER);

            boolean success = cs.execute();
            if (!success) {
                return false;
            }

            rs = cs.getResultSet();
            int successNumber = 0;
            while (rs.next()) {
                successNumber = rs.getInt("success");
            }
            if (successNumber == 1) {
                return true;
            } else {
                return false;
            }

        } catch (SQLException e) {
            Logger.getLogger(RegistrationIO.class.getName()).log(Level.SEVERE, 
                    "An exception occurred in the registerExistingUser method.", e);
            return false;
        } finally {
            DatabaseUtil.closeResultSet(rs);
            DatabaseUtil.closeCallableStatement(cs);
            pool.freeConnection(connection);
        }
    }
}
