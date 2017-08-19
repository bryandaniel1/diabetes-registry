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
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import registry.Clinic;
import registry.EmailMessage;
import registry.QualityReference;
import registry.User;
import registry.administration.EmailMessageConfiguration;
import registry.administration.EmailMessageConfigurationContainer;
import registry.administration.PasswordResetRequest;
import registry.administration.QualityReferenceConfiguration;
import utility.ConnectionPool;
import utility.DatabaseUtility;

/**
 * This data-access class contains methods required to fulfill administration
 * responsibilities.
 *
 * @author Bryan Daniel
 * @version 2, March 16, 2017
 */
public class AdministrationDataAccess {

    /**
     * Constant to set any new user's administrator indicator to false
     */
    private static final Integer userAdministratorStatus = 0;

    /**
     * This method finds and returns the details for the diabetes registry
     * clinic.
     *
     * @param clinicId the clinic ID
     * @return the clinic information or null is results are missing
     */
    public static Clinic getClinicDetails(int clinicId) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        CallableStatement cs = null;
        ResultSet rs = null;
        Clinic clinic = null;

        try {
            cs = connection.prepareCall("{CALL getClinic(?, ?)}");
            cs.setInt(1, clinicId);
            cs.registerOutParameter(2, java.sql.Types.TINYINT);

            cs.execute();

            /* procedure success */
            boolean success = cs.getBoolean(2);
            if (!success) {
                return null;
            }

            rs = cs.getResultSet();
            while (rs.next()) {
                clinic = new Clinic();
                clinic.setClinicId(clinicId);
                clinic.setClinicName(rs.getString("name"));
                clinic.setAddress(rs.getString("address"));
                clinic.setPhoneNumber(rs.getString("phone number"));
                clinic.setEmailAddress(rs.getString("email"));
            }
        } catch (SQLException e) {
            Logger.getLogger(AdministrationDataAccess.class.getName()).log(Level.SEVERE,
                    "An exception occurred during the getClinicDetails method.", e);
            return null;
        } finally {
            DatabaseUtility.closeResultSet(rs);
            DatabaseUtility.closeCallableStatement(cs);
            pool.freeConnection(connection);
        }
        return clinic;
    }

    /**
     * This method uses the given parameters to update a clinic's information in
     * the database.
     *
     * @param clinicId the clinic ID
     * @param clinicName the clinic name
     * @param address the address
     * @param phoneNumber the phone number
     * @param emailAddress the email address
     * @param adminName the administrator user name
     * @return the boolean for update success
     */
    public static boolean updateClinic(int clinicId, String clinicName,
            String address, String phoneNumber, String emailAddress,
            String adminName) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        CallableStatement cs = null;

        try {
            connection.setAutoCommit(false);
            cs = connection.prepareCall("{CALL updateClinic(?, ?, ?, ?, ?, ?)}");
            cs.setInt(1, clinicId);
            cs.setString(2, clinicName);
            cs.setString(3, address);
            cs.setString(4, phoneNumber);
            if ((emailAddress != null) && (emailAddress.trim().length() != 0)) {
                cs.setString(5, emailAddress);
            } else {
                cs.setNull(5, java.sql.Types.VARCHAR);
            }
            cs.setString(6, adminName);

            /* no result set to return */
            cs.execute();
            connection.commit();
            connection.setAutoCommit(true);

        } catch (SQLException ex) {
            try {
                connection.rollback();
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                Logger.getLogger(AdministrationDataAccess.class.getName())
                        .log(Level.SEVERE, "An exception occurred during the updateClinic method.", e);
                return false;
            }
            Logger.getLogger(AdministrationDataAccess.class.getName())
                    .log(Level.SEVERE, "An exception occurred during the updateClinic method.", ex);
            return false;
        } finally {
            DatabaseUtility.closeCallableStatement(cs);
            pool.freeConnection(connection);
        }
        return true;
    }

    /**
     * This method returns the list of diabetes registry user names.
     *
     * @return the list of user names or null if results are missing
     */
    public static ArrayList<String> getUserNames() {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        CallableStatement cs = null;
        ResultSet rs = null;
        ArrayList<String> userNames = new ArrayList<>();

        try {
            cs = connection.prepareCall("{CALL getAllUsers(?)}");
            cs.registerOutParameter(1, java.sql.Types.TINYINT);

            cs.execute();

            boolean success = cs.getBoolean(1);
            if (!success) {
                return null;
            }

            rs = cs.getResultSet();
            while (rs.next()) {
                String userName = rs.getString("user name");
                userNames.add(userName);
            }

        } catch (SQLException e) {
            Logger.getLogger(AdministrationDataAccess.class.getName()).log(Level.SEVERE,
                    "An exception occurred during the getUserNames method.", e);
            return null;
        } finally {
            DatabaseUtility.closeResultSet(rs);
            DatabaseUtility.closeCallableStatement(cs);
            pool.freeConnection(connection);
        }
        return userNames;
    }

    /**
     * This method returns the user details for the user name given.
     *
     * @param userName the user name
     * @return the user details or null if results are missing
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
            cs.registerOutParameter(2, java.sql.Types.TINYINT);

            cs.execute();

            boolean success = cs.getBoolean(2);
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
                detailedUser.setEmailAddress(rs.getString("email address"));
            }

        } catch (SQLException e) {
            Logger.getLogger(AdministrationDataAccess.class.getName()).log(Level.SEVERE,
                    "An exception occurred during the getUserDetails method.", e);
            return null;
        } finally {
            DatabaseUtility.closeResultSet(rs);
            DatabaseUtility.closeCallableStatement(cs);
            pool.freeConnection(connection);
        }
        return detailedUser;
    }

    /**
     * This method terminates a user's access to the registry.
     *
     * @param userName the user name
     * @param adminName the administrator user name
     * @return the boolean for termination success
     */
    public static boolean terminateUser(String userName, String adminName) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        CallableStatement cs = null;

        try {
            cs = connection.prepareCall("{CALL terminateUser(?, ?)}");
            cs.setString(1, userName);
            cs.setString(2, adminName);

            /* no result set to return */
            cs.execute();

        } catch (SQLException ex) {
            Logger.getLogger(AdministrationDataAccess.class.getName())
                    .log(Level.SEVERE, "A SQL exception occured in the terminateUser method.", ex);
            return false;
        } finally {
            DatabaseUtility.closeCallableStatement(cs);
            pool.freeConnection(connection);
        }
        return true;
    }

    /**
     * This method calls the stored procedures required to reset a user's
     * password.
     *
     * First, the resetUserPassword procedure is called to allow for the
     * password update. Then the password is replaced by the temporary password
     * in the changeUserPassword procedure. Finally, the resetUserPassword
     * procedure is called again to allow the user to change the password after
     * signing in with the temporary password.
     *
     * @param userName the user name
     * @param adminName the administrator user name
     * @param temporaryPassword the temporary password
     * @param userSalt the salt
     * @return the boolean for success or failure
     */
    public static boolean resetUserPassword(String userName, String adminName,
            String temporaryPassword, String userSalt) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        CallableStatement cs = null;

        try {
            cs = connection.prepareCall("{CALL resetUserPassword(?, ?, ?)}");
            cs.setString(1, userName);
            cs.setString(2, adminName);
            cs.registerOutParameter(3, java.sql.Types.TINYINT);

            /* no result set to return */
            cs.execute();

            /* grab out parameter */
            boolean success = cs.getBoolean(3);
            if (!success) {
                return false;
            }

            cs = connection.prepareCall("{CALL changeUserPassword(?, ?, ?, ?)}");
            cs.setString(1, userName);
            cs.setString(2, temporaryPassword);
            cs.setString(3, userSalt);
            cs.registerOutParameter(4, java.sql.Types.INTEGER);

            /* no result set to return */
            cs.execute();

            /* grab out parameter */
            success = cs.getBoolean(4);
            if (!success) {
                return false;
            }

            cs = connection.prepareCall("{CALL resetUserPassword(?, ?, ?)}");
            cs.setString(1, userName);
            cs.setString(2, adminName);
            cs.registerOutParameter(3, java.sql.Types.TINYINT);

            /* no result set to return */
            cs.execute();

            /* grab out parameter */
            success = cs.getBoolean(3);
            if (!success) {
                return false;
            }

        } catch (SQLException ex) {
            Logger.getLogger(AdministrationDataAccess.class.getName()).log(Level.SEVERE,
                    "An exception occurred during the resetUserPassword method.", ex);
            return false;
        } finally {
            DatabaseUtility.closeCallableStatement(cs);
            pool.freeConnection(connection);
        }
        return true;
    }

    /**
     * This method invokes the database procedure for adding a new user to the
     * registry.
     *
     * @param userName the user name
     * @param firstName the first name
     * @param lastName the last name
     * @param jobTitle the job title
     * @param userPassword the password
     * @param userSalt the salt
     * @param emailAddress the user's email address
     * @return the boolean indicating the success or failure of the operation
     */
    public static boolean addUser(String userName, String firstName,
            String lastName, String jobTitle, String userPassword,
            String userSalt, String emailAddress) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        CallableStatement cs = null;

        try {
            cs = connection.prepareCall("{CALL addUser(?, ?, ?, ?, ?, ?, ?, ?, ?)}");
            cs.setString(1, userName);
            cs.setString(2, firstName);
            cs.setString(3, lastName);
            cs.setString(4, jobTitle);
            cs.setInt(5, userAdministratorStatus);
            cs.setString(6, userPassword);
            cs.setString(7, userSalt);
            if ((emailAddress != null) && (emailAddress.trim().length() != 0)) {
                cs.setString(8, emailAddress);
            } else {
                cs.setNull(8, java.sql.Types.VARCHAR);
            }
            cs.registerOutParameter(9, java.sql.Types.TINYINT);

            /* no result set to return */
            cs.execute();

            /* grab out parameter */
            boolean success = cs.getBoolean(9);
            if (!success) {
                return false;
            }

        } catch (SQLException e) {
            Logger.getLogger(AdministrationDataAccess.class.getName()).log(Level.SEVERE,
                    "An exception occurred during the addUser method.", e);
            return false;
        } finally {
            DatabaseUtility.closeCallableStatement(cs);
            pool.freeConnection(connection);
        }
        return true;
    }

    /**
     * This method invokes the procedure for updating user information in the
     * database.
     *
     * @param userName the user name
     * @param firstName the first name
     * @param lastName the last name
     * @param jobTitle the job title
     * @param emailAddress the user's email address
     * @return the boolean indicating the success or failure of the operation
     */
    public static boolean updateUser(String userName, String firstName,
            String lastName, String jobTitle, String emailAddress) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        CallableStatement cs = null;

        try {
            cs = connection.prepareCall("{CALL updateUser(?, ?, ?, ?, ?, ?)}");
            cs.setString(1, userName);
            cs.setString(2, firstName);
            cs.setString(3, lastName);
            cs.setString(4, jobTitle);
            if ((emailAddress != null) && (emailAddress.trim().length() != 0)) {
                cs.setString(5, emailAddress);
            } else {
                cs.setNull(5, java.sql.Types.VARCHAR);
            }
            cs.registerOutParameter(6, java.sql.Types.TINYINT);

            /* no result set to return */
            cs.execute();

            /* grab out parameter */
            boolean success = cs.getBoolean(6);
            if (!success) {
                return false;
            }

        } catch (SQLException e) {
            Logger.getLogger(AdministrationDataAccess.class.getName()).log(Level.SEVERE,
                    "An exception occurred during the updateUser method.", e);
            return false;
        } finally {
            DatabaseUtility.closeCallableStatement(cs);
            pool.freeConnection(connection);
        }
        return true;
    }

    /**
     * For auditing, this method returns a list of user activities stored
     * between the 2 given dates.
     *
     * @param userName the username
     * @param beginDate the begin date
     * @param endDate the end date
     * @return the list of user activities or null if results are missing
     */
    public static ArrayList<String[]> auditUser(String userName, Date beginDate, Date endDate) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        CallableStatement cs = null;
        ResultSet rs = null;
        ArrayList<String[]> userActivites = new ArrayList<>();

        try {
            cs = connection.prepareCall("{CALL auditUser(?, ?, ?, ?)}");
            cs.setString(1, userName);
            cs.setDate(2, beginDate);
            cs.setDate(3, endDate);
            cs.registerOutParameter(4, java.sql.Types.TINYINT);

            cs.execute();

            boolean success = cs.getBoolean(4);
            if (!success) {
                return null;
            }

            rs = cs.getResultSet();
            while (rs.next()) {
                String[] activity = {"" + rs.getTimestamp("time occurred"),
                    rs.getString("activity")};
                userActivites.add(activity);
            }

        } catch (SQLException e) {
            Logger.getLogger(AdministrationDataAccess.class.getName()).log(Level.SEVERE,
                    "An exception occurred in the auditUser method.", e);
            return null;
        } finally {
            DatabaseUtility.closeResultSet(rs);
            DatabaseUtility.closeCallableStatement(cs);
            pool.freeConnection(connection);
        }
        return userActivites;
    }

    /**
     * This method finds and returns the unread password reset requests for an
     * administrator to see.
     *
     * @return the list of unread password reset requests or null if none exist
     */
    public static ArrayList<PasswordResetRequest> readUnreadPasswordResetRequests() {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        CallableStatement cs = null;
        ResultSet rs = null;
        ArrayList<PasswordResetRequest> passwordResetRequests = new ArrayList<>();

        try {
            cs = connection.prepareCall("{CALL readUnreadPasswordResetRequests()}");

            boolean success = cs.execute();
            if (!success) {
                return null;
            }

            rs = cs.getResultSet();
            while (rs.next()) {
                PasswordResetRequest passwordResetRequest = new PasswordResetRequest();
                passwordResetRequest.setRequestId(rs.getInt("request id"));
                passwordResetRequest.setUserName(rs.getString("user name"));
                passwordResetRequest.setFirstName(rs.getString("first name"));
                passwordResetRequest.setLastName(rs.getString("last name"));
                passwordResetRequest.setTimeRequested(rs.getTimestamp("time requested"));
                passwordResetRequests.add(passwordResetRequest);
            }

            if (passwordResetRequests.isEmpty()) {
                return null;
            }

        } catch (SQLException e) {
            Logger.getLogger(AdministrationDataAccess.class.getName()).log(Level.SEVERE,
                    "An exception occurred in the readUnreadPasswordResetRequests method.", e);
            return null;
        } finally {
            DatabaseUtility.closeResultSet(rs);
            DatabaseUtility.closeCallableStatement(cs);
            pool.freeConnection(connection);
        }
        return passwordResetRequests;
    }

    /**
     * This method connects to the database to mark password reset requests as
     * read.
     *
     * @param requestIds the request IDs
     * @return the boolean indicating the success or failure of the operation
     */
    public static boolean markPasswordResetRequestsAsRead(ArrayList<Integer> requestIds) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        CallableStatement cs = null;

        try {
            cs = connection.prepareCall("{CALL markPasswordResetRequestAsRead(?)}");
            connection.setAutoCommit(false);
            for (Integer i : requestIds) {
                cs.setInt(1, i);
                cs.addBatch();
            }

            /* no result set to return */
            cs.executeBatch();
            connection.commit();
            connection.setAutoCommit(true);

        } catch (SQLException ex) {
            Logger.getLogger(AdministrationDataAccess.class.getName()).log(Level.SEVERE,
                    "An exception occurred during the markPasswordResetRequestsAsRead method.", ex);
            return false;
        } finally {
            DatabaseUtility.closeCallableStatement(cs);
            pool.freeConnection(connection);
        }
        return true;
    }

    /**
     * This method calls the stored procedure to retrieve all email message
     * configurations.
     *
     * @param clinicId the clinic ID
     * @return the list of email message configurations or null if results are
     * missing
     */
    public static EmailMessageConfigurationContainer getEmailMessageConfigurations(int clinicId) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        CallableStatement cs = null;
        ResultSet rs = null;
        EmailMessageConfigurationContainer container = new EmailMessageConfigurationContainer();
        HashMap<Integer, EmailMessageConfiguration> emailMessageConfigurations = new HashMap<>();

        try {
            cs = connection.prepareCall("{CALL retrieveEmailMessageConfigurations(?)}");
            cs.setInt(1, clinicId);

            boolean success = cs.execute();
            if (!success) {
                return null;
            }

            rs = cs.getResultSet();
            int count = 0;
            while (rs.next()) {
                EmailMessageConfiguration emailMessageConfiguration = new EmailMessageConfiguration();
                EmailMessage emailMessage = new EmailMessage();
                emailMessage.setSubject(rs.getString("subject"));
                emailMessage.setLanguage(rs.getString("language"));
                emailMessage.setMessage(rs.getString("message"));
                emailMessageConfiguration.setEmailMessage(emailMessage);
                emailMessageConfiguration.setCallListFilter(rs.getString("filter"));
                emailMessageConfigurations.put(++count, emailMessageConfiguration);
            }

            container.setEmailMessageConfigurations(emailMessageConfigurations);

            DatabaseUtility.closeResultSet(rs);

            /* all patient filters */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }
            rs = cs.getResultSet();

            ArrayList<String> filters = new ArrayList<>();

            while (rs.next()) {
                filters.add(rs.getString("filter"));
            }
            container.setAllFilters(filters);

        } catch (SQLException e) {
            Logger.getLogger(AdministrationDataAccess.class.getName()).log(Level.SEVERE,
                    "An exception occurred in the getEmailMessageConfigurations method.", e);
            return null;
        } finally {
            DatabaseUtility.closeResultSet(rs);
            DatabaseUtility.closeCallableStatement(cs);
            pool.freeConnection(connection);
        }
        return container;
    }

    /**
     * This method connects to the database to save the given email reminder
     * configuration to the database.
     *
     * @param clinicId the clinic ID
     * @param emailMessageConfiguration the email reminder configuration to save
     * @return the boolean indicating the success or failure of the operation
     */
    public static boolean saveEmailMessageConfiguration(int clinicId, EmailMessageConfiguration emailMessageConfiguration) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        CallableStatement cs = null;

        try {
            cs = connection.prepareCall("{CALL saveEmailMessageConfiguration(?, ?, ?, ?, ?, ?)}");
            cs.setInt(1, clinicId);
            cs.setString(2, emailMessageConfiguration.getEmailMessage().getSubject());
            cs.setString(3, emailMessageConfiguration.getEmailMessage().getLanguage());
            cs.setString(4, emailMessageConfiguration.getCallListFilter());
            cs.setString(5, emailMessageConfiguration.getEmailMessage().getMessage());
            cs.registerOutParameter(6, java.sql.Types.TINYINT);

            cs.execute();

            boolean success = cs.getBoolean(6);

            if (!success) {
                return false;
            }

        } catch (SQLException ex) {
            Logger.getLogger(AdministrationDataAccess.class.getName()).log(Level.SEVERE,
                    "An exception occurred during the saveEmailMessageConfiguration method.", ex);
            return false;
        } finally {
            DatabaseUtility.closeCallableStatement(cs);
            pool.freeConnection(connection);
        }
        return true;
    }

    /**
     * This method connects to the database to delete the given email reminder
     * configuration.
     *
     * @param clinicId the clinic ID
     * @param emailMessageConfiguration the email reminder to delete
     * @return the boolean indicating the success or failure of the operation
     */
    public static boolean deleteEmailMessageConfiguration(int clinicId,
            EmailMessageConfiguration emailMessageConfiguration) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        CallableStatement cs = null;

        try {
            cs = connection.prepareCall("{CALL deleteEmailMessageConfiguration(?, ?, ?, ?)}");
            cs.setInt(1, clinicId);
            cs.setString(2, emailMessageConfiguration.getEmailMessage().getSubject());
            cs.setString(3, emailMessageConfiguration.getEmailMessage().getLanguage());
            cs.registerOutParameter(4, java.sql.Types.TINYINT);

            cs.execute();

            boolean success = cs.getBoolean(4);

            if (!success) {
                return false;
            }

        } catch (SQLException ex) {
            Logger.getLogger(AdministrationDataAccess.class.getName()).log(Level.SEVERE,
                    "An exception occurred during the deleteEmailMessageConfiguration method.", ex);
            return false;
        } finally {
            DatabaseUtility.closeCallableStatement(cs);
            pool.freeConnection(connection);
        }
        return true;
    }

    /**
     * This method connects to the database to find and return all quality
     * reference configurations.
     *
     * @return the list of quality reference configurations or null if results
     * are missing
     */
    public static ArrayList<QualityReferenceConfiguration> retrieveQualityReferenceConfigurations() {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        CallableStatement cs = null;
        ResultSet rs = null;
        ArrayList<QualityReferenceConfiguration> configurations = new ArrayList<>();

        try {
            cs = connection.prepareCall("{CALL retrieveQualityReferenceConfigurations(?)}");
            cs.registerOutParameter(1, java.sql.Types.TINYINT);

            cs.execute();

            boolean success = cs.getBoolean(1);
            if (!success) {
                return null;
            }

            rs = cs.getResultSet();
            while (rs.next()) {
                QualityReferenceConfiguration configuration = new QualityReferenceConfiguration();
                QualityReference reference = new QualityReference();
                reference.setRole(rs.getString("role"));
                reference.setResponsibility(rs.getString("responsibility"));
                configuration.setQualityReference(reference);
                configuration.setActive(rs.getBoolean("active"));
                configurations.add(configuration);
            }

            if (configurations.isEmpty()) {
                return null;
            }

        } catch (SQLException e) {
            Logger.getLogger(AdministrationDataAccess.class.getName()).log(Level.SEVERE,
                    "An exception occurred during the retrieveQualityReferenceConfigurations method.", e);
            return null;
        } finally {
            DatabaseUtility.closeResultSet(rs);
            DatabaseUtility.closeCallableStatement(cs);
            pool.freeConnection(connection);
        }
        return configurations;
    }

    /**
     * This method connects to the database to change the status of the quality
     * reference associated with the given responsibility.
     *
     * @param responsibility the responsibility
     * @return the boolean indicating the success or failure of the operation
     */
    public static boolean changeQualityReferenceStatus(String responsibility) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        CallableStatement cs = null;

        try {
            cs = connection.prepareCall("{CALL changeQualityReferenceStatus(?, ?)}");
            cs.setString(1, responsibility);
            cs.registerOutParameter(2, java.sql.Types.TINYINT);

            /* no result set to return */
            cs.execute();

            /* grab out parameter */
            boolean success = cs.getBoolean(2);
            if (!success) {
                return false;
            }

        } catch (SQLException ex) {
            Logger.getLogger(AdministrationDataAccess.class.getName()).log(Level.SEVERE,
                    "An exception occurred during the changeQualityReferenceStatus method.", ex);
            return false;
        } finally {
            DatabaseUtility.closeCallableStatement(cs);
            pool.freeConnection(connection);
        }
        return true;
    }

    /**
     * This method invokes the database procedure for adding a new quality
     * reference to the registry.
     *
     * @param role the role
     * @param responsibility the new responsibility
     * @return the boolean indicating the success or failure of the operation
     */
    public static boolean addQualityReference(String role, String responsibility) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        CallableStatement cs = null;

        try {
            cs = connection.prepareCall("{CALL addQualityReference(?, ?, ?)}");
            cs.setString(1, role);
            cs.setString(2, responsibility);
            cs.registerOutParameter(3, java.sql.Types.TINYINT);

            /* no result set to return */
            cs.execute();

            /* grab out parameter */
            boolean success = cs.getBoolean(3);
            if (!success) {
                return false;
            }

        } catch (SQLException e) {
            Logger.getLogger(AdministrationDataAccess.class.getName()).log(Level.SEVERE,
                    "An exception occurred during the addQualityReference method.", e);
            return false;
        } finally {
            DatabaseUtility.closeCallableStatement(cs);
            pool.freeConnection(connection);
        }
        return true;
    }
}
