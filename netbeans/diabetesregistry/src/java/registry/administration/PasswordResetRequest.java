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
package registry.administration;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * This class represents a password request reset logged by a user for an
 * administrator to read.
 *
 * @author Bryan Daniel
 * @version 2, March 16, 2017
 */
public class PasswordResetRequest implements Serializable {
    
    /**
     * Serial version UID
     */
    private static final long serialVersionUID = 4621688245801044401L;

    /**
     * The request ID
     */
    private int requestId;    

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
     * The time requested
     */
    private Timestamp timeRequested;

    /**
     * Get the value of requestId
     *
     * @return the value of requestId
     */
    public int getRequestId() {
        return requestId;
    }

    /**
     * Set the value of requestId
     *
     * @param requestId new value of requestId
     */
    public void setRequestId(int requestId) {
        this.requestId = requestId;
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
     * Get the value of timeRequested
     *
     * @return the value of timeRequested
     */
    public Timestamp getTimeRequested() {
        return timeRequested;
    }

    /**
     * Set the value of timeRequested
     *
     * @param timeRequested new value of timeRequested
     */
    public void setTimeRequested(Timestamp timeRequested) {
        this.timeRequested = timeRequested;
    }    
}
