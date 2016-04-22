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
package clinic;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * Represents a diabetes registry user
 *
 * @author Bryan Daniel
 * @version 1, April 8, 2016
 */
public class User implements Serializable {

    /**
     * Instance variables
     */
    private String userName;
    private String firstName;
    private String lastName;
    private String jobTitle;
    private ArrayList<Clinic> clinics;
    private boolean administrator;
    private boolean active;
    private Timestamp dateJoined;
    private Timestamp lastLogin;

    /**
     * Default constructor
     */
    public User() {
        userName = null;
        firstName = null;
        lastName = null;
        jobTitle = null;
        clinics = null;
        administrator = false;
        active = false;
        dateJoined = null;
        lastLogin = null;
    }

    /**
     * Parameterized constructor
     *
     * @param userName the user name
     * @param firstName the first name
     * @param lastName the last name
     * @param jobTitle the job title
     * @param clinics the clinic IDs
     * @param administrator administration boolean
     * @param active the status of the user
     * @param dateJoined the date joined
     * @param lastLogin the last login
     */
    public User(String userName, String firstName, String lastName, String jobTitle,
            ArrayList<Clinic> clinics, boolean administrator, boolean active,
            Timestamp dateJoined, Timestamp lastLogin) {
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.jobTitle = jobTitle;
        this.clinics = clinics;
        this.administrator = administrator;
        this.active = active;
        this.dateJoined = dateJoined;
        this.lastLogin = lastLogin;
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
     * Get the value of clinics
     *
     * @return the value of clinics
     */
    public ArrayList<Clinic> getClinics() {
        return clinics;
    }

    /**
     * Set the value of clinics
     *
     * @param clinics new value of clinics
     */
    public void setClinics(ArrayList<Clinic> clinics) {
        this.clinics = clinics;
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
}
