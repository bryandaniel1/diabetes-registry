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
package registry;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * This class represents a diabetes registry user.
 *
 * @author Bryan Daniel
 * @version 2, March 16, 2017
 */
public class User implements Serializable {
    
    /**
     * Serial version UID
     */
    private static final long serialVersionUID = -8266674184210079232L;

    /**
     * The username
     */
    private String userName;

    /**
     * The user's first name
     */
    private String firstName;

    /**
     * The user's last name
     */
    private String lastName;

    /**
     * The job title
     */
    private String jobTitle;

    /**
     * Indication of whether the user is an administrator
     */
    private boolean administrator;

    /**
     * Indication of whether the user is active
     */
    private boolean active;

    /**
     * The date the user registered
     */
    private Timestamp dateJoined;

    /**
     * The last login of the user
     */
    private Timestamp lastLogin;
    
    /**
     * The user's email address
     */
    private String emailAddress;

    /**
     * Default constructor
     */
    public User() {
        userName = null;
        firstName = null;
        lastName = null;
        jobTitle = null;
        administrator = false;
        active = false;
        dateJoined = null;
        lastLogin = null;
        emailAddress = null;
    }

    /**
     * Parameterized constructor
     *
     * @param userName the user name
     * @param firstName the first name
     * @param lastName the last name
     * @param jobTitle the job title
     * @param administrator administration boolean
     * @param active the status of the user
     * @param dateJoined the date joined
     * @param lastLogin the last login
     * @param userEmail the user email address
     */
    public User(String userName, String firstName, String lastName, String jobTitle,
            boolean administrator, boolean active, Timestamp dateJoined, 
            Timestamp lastLogin, String userEmail) {
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.jobTitle = jobTitle;
        this.administrator = administrator;
        this.active = active;
        this.dateJoined = dateJoined;
        this.lastLogin = lastLogin;
        this.emailAddress = userEmail;
    }

    /**
     * Get the value of userName
     *
     * @return the value of userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Set the value of userName
     *
     * @param userName new value of userName
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Get the value of firstName
     *
     * @return the value of firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Set the value of firstName
     *
     * @param firstName new value of firstName
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Get the value of lastName
     *
     * @return the value of lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Set the value of lastName
     *
     * @param lastName new value of lastName
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Get the value of jobTitle
     *
     * @return the value of jobTitle
     */
    public String getJobTitle() {
        return jobTitle;
    }

    /**
     * Set the value of jobTitle
     *
     * @param jobTitle new value of jobTitle
     */
    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    /**
     * Get the value of administrator
     *
     * @return the value of administrator
     */
    public boolean getAdministrator() {
        return administrator;
    }

    /**
     * Set the value of administrator
     *
     * @param administrator new value of administrator
     */
    public void setAdministrator(boolean administrator) {
        this.administrator = administrator;
    }

    /**
     * Get the value of active
     *
     * @return the value of active
     */
    public boolean getActive() {
        return active;
    }

    /**
     * Set the value of active
     *
     * @param active new value of active
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Get the value of dateJoined
     *
     * @return the value of dateJoined
     */
    public Timestamp getDateJoined() {
        return dateJoined;
    }

    /**
     * Set the value of dateJoined
     *
     * @param dateJoined new value of dateJoined
     */
    public void setDateJoined(Timestamp dateJoined) {
        this.dateJoined = dateJoined;
    }

    /**
     * Get the value of lastLogin
     *
     * @return the value of lastLogin
     */
    public Timestamp getLastLogin() {
        return lastLogin;
    }

    /**
     * Set the value of lastLogin
     *
     * @param lastLogin new value of lastLogin
     */
    public void setLastLogin(Timestamp lastLogin) {
        this.lastLogin = lastLogin;
    }

    /**
     * Get the value of emailAddress
     *
     * @return the value of emailAddress
     */
    public String getEmailAddress() {
        return emailAddress;
    }

    /**
     * Set the value of emailAddress
     *
     * @param emailAddress new value of emailAddress
     */
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
}
