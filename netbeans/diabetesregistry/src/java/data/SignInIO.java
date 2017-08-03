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
 * Accesses the database during the sign in process
 *
 * @author Bryan Daniel
 * @version 1, April 8, 2016
 */
public class SignInIO {

    /**
     * This method retrieves a user's salt from the database.
     *
     * @param userName the user name
     * @return the salt
     */
    public static String retrieveSalt(String userName) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        CallableStatement cs = null;
        ResultSet rs = null;
        String result = null;

        try {
            cs = connection.prepareCall("{CALL retrieveSalt(?, ?)}");
            cs.setString(1, userName);

            boolean success = cs.execute();
            if (!success) {
                return null;
            }

            rs = cs.getResultSet();
            if (rs.next()) {
                result = rs.getString("salt");
            }
            return result;

        } catch (SQLException e) {
            Logger.getLogger(SignInIO.class.getName()).log(Level.SEVERE, 
                    "An exception occurred in the retrieveSalt method.", e);
            return result;
        } finally {
            DatabaseUtil.closeResultSet(rs);
            DatabaseUtil.closeCallableStatement(cs);
            pool.freeConnection(connection);
        }
    }

    /**
     * This method verifies that the sign in credentials match the information
     * in the database.
     *
     * @param userName the user name
     * @param password the password
     * @return a boolean value to determine if the user was added successfully
     */
    public static boolean validateUser(String userName, String password) {
        boolean result = false;
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        CallableStatement cs = null;
        ResultSet rs = null;

        try {
            cs = connection.prepareCall("{CALL authenticateUser(?, ?, ?, ?, ?)}");
            cs.setString(1, userName);
            cs.setString(2, password);
            cs.registerOutParameter(3, java.sql.Types.INTEGER);
            cs.registerOutParameter(4, java.sql.Types.INTEGER);
            cs.registerOutParameter(5, java.sql.Types.INTEGER);

            boolean success = cs.execute();
            if (!success) {
                return false;
            }

            rs = cs.getResultSet();
            int validUser = 0;
            if (rs.next()) {
                validUser = rs.getInt("authentic");
            }

            if (validUser == 1) {
                result = true;
            }
            return result;

        } catch (SQLException e) {
            Logger.getLogger(SignInIO.class.getName()).log(Level.SEVERE, 
                    "An exception occurred in the validateUser method.", e);
            return result;
        } finally {
            DatabaseUtil.closeResultSet(rs);
            DatabaseUtil.closeCallableStatement(cs);
            pool.freeConnection(connection);
        }
    }

    /**
     * This method returns a user associated with the given user name.
     *
     * @param userName the user name
     * @return the user object
     */
    public static User getUser(String userName) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        CallableStatement cs = null;
        ResultSet rs = null;

        try {
            cs = connection.prepareCall("{CALL getUser(?, ?)}");
            cs.setString(1, userName);
            cs.registerOutParameter(2, java.sql.Types.INTEGER);

            boolean success = cs.execute();
            if (!success) {
                return null;
            }

            rs = cs.getResultSet();
            User user = null;
            while (rs.next()) {
                user = new User();
                user.setUserName(userName);
                user.setFirstName(rs.getString("first name"));
                user.setLastName(rs.getString("last name"));
                user.setJobTitle(rs.getString("job title"));
                user.setAdministrator(rs.getBoolean("administrator"));
            }
            if (user != null) {

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
                    clinics.add(clinic);
                }
                user.setClinics(clinics);
            }
            return user;

        } catch (SQLException e) {
            Logger.getLogger(SignInIO.class.getName()).log(Level.SEVERE, 
                    "An exception occurred in the getUser method.", e);
            return null;
        } finally {
            DatabaseUtil.closeResultSet(rs);
            DatabaseUtil.closeCallableStatement(cs);
            pool.freeConnection(connection);
        }
    }
}
