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
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import registry.User;
import utility.ConnectionPool;
import utility.DatabaseUtility;

/**
 * This class accesses the database during the sign in process.
 *
 * @author Bryan Daniel
 * @version 2, March 16, 2017
 */
public class SignInDataAccess {

    /**
     * This enum type holds indicator values for the sign-in result.
     */
    public enum SignInStatus {

        /**
         * The type indicating an authenticated user
         */
        AUTHENTICATED,
        
        /**
         * The type indicating a user password must change
         */
        PASSWORD_CHANGE,
        
        /**
         * the type indicating a failed sign-in attempt
         */
        FAILED
    }

    /**
     * This method retrieves a user's salt from the database.
     *
     * @param userName the user name
     * @return the salt or null if results are missing
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

        } catch (SQLException e) {
            Logger.getLogger(SignInDataAccess.class.getName()).log(Level.SEVERE,
                    "An exception occurred during the retrieveSalt method.", e);
            return null;
        } finally {
            DatabaseUtility.closeResultSet(rs);
            DatabaseUtility.closeCallableStatement(cs);
            pool.freeConnection(connection);
        }
        return result;
    }

    /**
     * This method verifies that the sign in credentials match the information
     * in the database.
     *
     * @param userName the user name
     * @param password the password
     * @return a SignInStatus type indicating the authentication status of the
     * user
     */
    public static SignInStatus authenticateUser(String userName, String password) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        CallableStatement cs = null;
        ResultSet rs = null;
        SignInStatus status = SignInStatus.FAILED;

        try {
            cs = connection.prepareCall("{CALL authenticateUser(?, ?, ?, ?, ?)}");
            cs.setString(1, userName);
            cs.setString(2, password);
            cs.registerOutParameter(3, java.sql.Types.TINYINT);
            cs.registerOutParameter(4, java.sql.Types.TINYINT);
            cs.registerOutParameter(5, java.sql.Types.TINYINT);

            /* no result set to return */
            cs.execute();

            boolean success = cs.getBoolean(5);
            if (!success) {
                return status;
            }

            boolean authenticatedUser = cs.getBoolean(3);
            boolean passwordNeedsToChange = cs.getBoolean(4);

            if (passwordNeedsToChange == true) {
                status = SignInStatus.PASSWORD_CHANGE;
            } else if (authenticatedUser == true) {
                status = SignInStatus.AUTHENTICATED;
            }
        } catch (SQLException e) {
            Logger.getLogger(SignInDataAccess.class.getName()).log(Level.SEVERE,
                    "An exception occurred during the authenticateUser method.", e);
            return status;
        } finally {
            DatabaseUtility.closeResultSet(rs);
            DatabaseUtility.closeCallableStatement(cs);
            pool.freeConnection(connection);
        }
        return status;
    }

    /**
     * This method returns a user associated with the given user name.
     *
     * @param userName the user name
     * @return the user object or null if results are missing
     */
    public static User getUser(String userName) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        CallableStatement cs = null;
        ResultSet rs = null;
        User user = null;

        try {
            cs = connection.prepareCall("{CALL getUser(?, ?)}");
            cs.setString(1, userName);
            cs.registerOutParameter(2, java.sql.Types.INTEGER);

            boolean success = cs.execute();
            if (!success) {
                return null;
            }

            rs = cs.getResultSet();
            while (rs.next()) {
                user = new User();
                user.setUserName(userName);
                user.setFirstName(rs.getString("first name"));
                user.setLastName(rs.getString("last name"));
                user.setJobTitle(rs.getString("job title"));
                user.setAdministrator(rs.getBoolean("administrator"));
            }

        } catch (SQLException e) {
            Logger.getLogger(SignInDataAccess.class.getName()).log(Level.SEVERE,
                    "An exception occurred duing the getUser method.", e);
            return null;
        } finally {
            DatabaseUtility.closeResultSet(rs);
            DatabaseUtility.closeCallableStatement(cs);
            pool.freeConnection(connection);
        }
        return user;
    }
}
