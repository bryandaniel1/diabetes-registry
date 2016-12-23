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

/**
 * This class contains the new strings required for a registration key change
 *
 * @author Bryan Daniel
 * @version 1, April 8, 2016
 */
public class ClinicRegistration {

    /**
     * The clinic for which the registration is concerned
     */
    private Clinic clinic;
    
    /**
     * The hashed key string
     */
    private String hashedKey;
    
    /**
     * The salt string
     */
    private String salt;
    
    /**
     * The key sent to the administrator's email address
     */
    private String emailKey;

    /**
     * Default constructor
     */
    public ClinicRegistration() {
        hashedKey = null;
        salt = null;
        emailKey = null;
    }

    /**
     * Parameterized constructor
     *
     * @param clinic the clinic
     * @param hashedKey the key
     * @param salt the salt
     * @param emailKey the key sent to the administrator
     */
    public ClinicRegistration(Clinic clinic, String hashedKey, String salt,
            String emailKey) {
        this.clinic = clinic;
        this.hashedKey = hashedKey;
        this.salt = salt;
        this.emailKey = emailKey;
    }

    /**
     * Get the value of clinic
     *
     * @return the value of clinic
     */
    public Clinic getClinic() {
        return clinic;
    }

    /**
     * Set the value of clinic
     *
     * @param clinic new value of clinic
     */
    public void setClinic(Clinic clinic) {
        this.clinic = clinic;
    }

    /**
     * Get the value of hashedKey
     *
     * @return the value of hashedKey
     */
    public String getHashedKey() {
        return hashedKey;
    }

    /**
     * Set the value of hashedKey
     *
     * @param hashedKey new value of hashedKey
     */
    public void setHashedKey(String hashedKey) {
        this.hashedKey = hashedKey;
    }

    /**
     * Get the value of salt
     *
     * @return the value of salt
     */
    public String getSalt() {
        return salt;
    }

    /**
     * Set the value of salt
     *
     * @param salt new value of salt
     */
    public void setSalt(String salt) {
        this.salt = salt;
    }

    /**
     * Get the value of emailKey
     *
     * @return the value of emailKey
     */
    public String getEmailKey() {
        return emailKey;
    }

    /**
     * Set the value of emailKey
     *
     * @param emailKey new value of emailKey
     */
    public void setEmailKey(String emailKey) {
        this.emailKey = emailKey;
    }

}
