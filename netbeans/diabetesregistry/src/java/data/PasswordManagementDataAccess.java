/*
 * Copyright 2017 Bryan Daniel.
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
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import utility.ConnectionPool;
import utility.DatabaseUtility;

/**
 * This class accesses the database to manage user passwords.
 *
 * @author Bryan Daniel
 * @version 1, March 16, 2017
 */
public class PasswordManagementDataAccess {

    /**
     * This method invokes the database procedure for updating a user's
     * password.
     *
     * @param userName the user name
     * @param hash the new hash
     * @param userSalt the salt
     * @return the boolean indicating success or failure
     */
    public static boolean changeUserPassword(String userName, String hash,
            String userSalt) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        CallableStatement cs = null;

        try {
            cs = connection.prepareCall("{CALL changeUserPassword(?, ?, ?, ?)}");
            cs.setString(1, userName);
            cs.setString(2, hash);
            cs.setString(3, userSalt);
            cs.registerOutParameter(4, java.sql.Types.TINYINT);

            cs.execute();

            boolean success = cs.getBoolean(4);
            if (!success) {
                return false;
            }
        } catch (SQLException e) {
            Logger.getLogger(PasswordManagementDataAccess.class.getName()).log(Level.SEVERE,
                    "The call to stored procedure, changeUserPassword, was unsuccessful.", e);
            return false;
        } finally {
            DatabaseUtility.closeCallableStatement(cs);
            pool.freeConnection(connection);
        }
        return true;
    }

    /**
     * This method invokes the database procedure for storing a message for the
     * administrator requesting that the given user's password be reset.
     *
     * @param userName the user name
     * @param userEmail the user email address
     * @return the boolean indicating success or failure
     */
    public static boolean requestPasswordReset(String userName, String userEmail) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        CallableStatement cs = null;

        try {
            cs = connection.prepareCall("{CALL requestPasswordReset(?, ?, ?)}");
            cs.setString(1, userName);
            cs.setString(2, userEmail);
            cs.registerOutParameter(3, java.sql.Types.TINYINT);

            cs.execute();

            boolean success = cs.getBoolean(3);
            if (!success) {
                return false;
            }
        } catch (SQLException e) {
            Logger.getLogger(PasswordManagementDataAccess.class.getName()).log(Level.SEVERE,
                    "The call to stored procedure, requestPasswordReset, was unsuccessful.", e);
            return false;
        } finally {
            DatabaseUtility.closeCallableStatement(cs);
            pool.freeConnection(connection);
        }
        return true;
    }
}
