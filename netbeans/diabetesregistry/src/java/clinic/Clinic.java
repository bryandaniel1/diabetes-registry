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

/**
 * Represents a clinic
 *
 * @author Bryan Daniel
 * @version 1, April 8, 2016
 */
public class Clinic implements Serializable {

    /**
     * Instance variables
     */
    private int clinicId;
    private String clinicName;
    private String address;
    private String phoneNumber;
    private String emailAddress;

    /**
     * Default constructor
     */
    public Clinic() {
        clinicName = null;
        address = null;
        phoneNumber = null;
        emailAddress = null;
    }

    /**
     * Parameterized constructor
     *
     * @param clinicId the clinic ID
     * @param clinicName the clinic name
     * @param address the address
     * @param phoneNumber the phone number
     * @param emailAddress the email address
     */
    public Clinic(int clinicId, String clinicName, String address,
            String phoneNumber, String emailAddress) {
        this.clinicId = clinicId;
        this.clinicName = clinicName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
    }

    /**
     * Get the value of clinicId
     *
     * @return the value of clinicId
     */
    public int getClinicId() {
        return clinicId;
    }

    /**
     * Set the value of clinicId
     *
     * @param clinicId new value of clinicId
     */
    public void setClinicId(int clinicId) {
        this.clinicId = clinicId;
    }

    /**
     * Get the value of clinicName
     *
     * @return the value of clinicName
     */
    public String getClinicName() {
        return clinicName;
    }

    /**
     * Set the value of clinicName
     *
     * @param clinicName new value of clinicName
     */
    public void setClinicName(String clinicName) {
        this.clinicName = clinicName;
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
     * Get the value of phoneNumber
     *
     * @return the value of phoneNumber
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Set the value of phoneNumber
     *
     * @param phoneNumber new value of phoneNumber
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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
     * Returns a string listing clinic attributes
     *
     * @return
     */
    @Override
    public String toString() {
        if (emailAddress == null) {
            return "clinic name: " + clinicName + "\naddress: " + address
                    + "\nphone number: " + phoneNumber;
        } else {
            return "clinic name: " + clinicName + "\naddress: " + address
                    + "\nphone number: " + phoneNumber + "\nemail address: "
                    + emailAddress;
        }
    }

}
