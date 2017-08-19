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
import java.sql.Date;

/**
 * This class represents a patient.
 *
 * @author Bryan Daniel
 * @version 2, March 16, 2017
 */
public class Patient implements Serializable {
    
    /**
     * Serial version UID
     */
    private static final long serialVersionUID = 7300687260564859513L;

    /**
     * The patient ID
     */
    private int patientId;

    /**
     * The patient's first name
     */
    private String firstName;

    /**
     * The patient's last name
     */
    private String lastName;

    /**
     * The birth date
     */
    private Date birthDate;

    /**
     * The home address
     */
    private String address;

    /**
     * The telephone number for the patient
     */
    private String contactNumber;

    /**
     * The gender
     */
    private String gender;

    /**
     * The race
     */
    private String race;

    /**
     * The email address
     */
    private String emailAddress;

    /**
     * The patient's language
     */
    private String language;

    /**
     * The reason for inactivity if the patient is inactive
     */
    private String reasonForInactivity;

    /**
     * The patient's start date
     */
    private Date startDate;
    
    /**
     * The date of the patient's last measurement used for call lists
     */
    private Date dateOfLastMeasurement;

    /**
     * Default constructor
     */
    public Patient() {
        firstName = null;
        lastName = null;
        birthDate = null;
        contactNumber = null;
        gender = null;
        race = null;
        emailAddress = null;
        language = null;
        reasonForInactivity = null;
        startDate = null;
        dateOfLastMeasurement = null;
    }

    /**
     * Parameterized constructor
     *
     * @param patientId the patient ID
     * @param firstName the first name
     * @param lastName the last name
     * @param birthDate the birth date
     * @param contactNumber the contact number
     * @param gender the gender
     * @param race the race
     * @param emailAddress the email address
     * @param language the language
     * @param reasonForInactivity the reason for inactivity
     * @param startDate the start date
     * @param dateOfLastMeasurement the date of the patient's last measurement
     */
    public Patient(int patientId, String firstName, String lastName,
            Date birthDate, String contactNumber, String gender, String race,
            String emailAddress, String language, String reasonForInactivity,
            Date startDate, Date dateOfLastMeasurement) {
        this.patientId = patientId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.contactNumber = contactNumber;
        this.gender = gender;
        this.race = race;
        this.emailAddress = emailAddress;
        this.language = language;
        this.reasonForInactivity = reasonForInactivity;
        this.startDate = startDate;
        this.dateOfLastMeasurement = dateOfLastMeasurement;
    }

    /**
     * Get the value of patientId
     *
     * @return the value of patientId
     */
    public int getPatientId() {
        return patientId;
    }

    /**
     * Set the value of patientId
     *
     * @param patientId new value of patientId
     */
    public void setPatientId(int patientId) {
        this.patientId = patientId;
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
     * Get the value of birthDate
     *
     * @return the value of birthDate
     */
    public Date getBirthDate() {
        return birthDate;
    }

    /**
     * Set the value of birthDate
     *
     * @param birthDate new value of birthDate
     */
    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    /**
     * Get the value of address
     *
     * @return the value of address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Set the value of address
     *
     * @param address new value of address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Get the value of contactNumber
     *
     * @return the value of contactNumber
     */
    public String getContactNumber() {
        return contactNumber;
    }

    /**
     * Set the value of contactNumber
     *
     * @param contactNumber new value of contactNumber
     */
    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    /**
     * Get the value of gender
     *
     * @return the value of gender
     */
    public String getGender() {
        return gender;
    }

    /**
     * Set the value of gender
     *
     * @param gender new value of gender
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * Get the value of race
     *
     * @return the value of race
     */
    public String getRace() {
        return race;
    }

    /**
     * Set the value of race
     *
     * @param race new value of race
     */
    public void setRace(String race) {
        this.race = race;
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

    /**
     * Get the value of language
     *
     * @return the value of language
     */
    public String getLanguage() {
        return language;
    }

    /**
     * Set the value of language
     *
     * @param language new value of language
     */
    public void setLanguage(String language) {
        this.language = language;
    }

    /**
     * Get the value of reasonForInactivity
     *
     * @return the value of reasonForInactivity
     */
    public String getReasonForInactivity() {
        return reasonForInactivity;
    }

    /**
     * Set the value of reasonForInactivity
     *
     * @param reasonForInactivity new value of reasonForInactivity
     */
    public void setReasonForInactivity(String reasonForInactivity) {
        this.reasonForInactivity = reasonForInactivity;
    }

    /**
     * Get the value of startDate
     *
     * @return the value of startDate
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * Set the value of startDate
     *
     * @param startDate new value of startDate
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * Get the value of dateOfLastMeasurement
     *
     * @return the value of dateOfLastMeasurement
     */
    public Date getDateOfLastMeasurement() {
        return dateOfLastMeasurement;
    }

    /**
     * Set the value of dateOfLastMeasurement
     *
     * @param dateOfLastMeasurement new value of dateOfLastMeasurement
     */
    public void setDateOfLastMeasurement(Date dateOfLastMeasurement) {
        this.dateOfLastMeasurement = dateOfLastMeasurement;
    }
}
