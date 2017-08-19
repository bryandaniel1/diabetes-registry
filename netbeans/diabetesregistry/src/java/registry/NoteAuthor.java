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
 * This class holds a user and a time stamp to indicate when the user updated a
 * progress note.
 *
 * @author Bryan Daniel
 * @version 2, March 16, 2017
 */
public class NoteAuthor implements Serializable {
    
    /**
     * Serial version UID
     */
    private static final long serialVersionUID = 4766880128638157393L;

    /**
     * The user's first name
     */
    private String firstName;

    /**
     * The user's last name
     */
    private String lastName;

    /**
     * The user's job title
     */
    private String jobTitle;

    /**
     * The timestamp
     */
    private Timestamp timeStamp;

    /**
     * Default constructor
     */
    public NoteAuthor() {
        firstName = null;
        lastName = null;
        jobTitle = null;
        timeStamp = null;
    }

    /**
     * Parameterized constructor
     *
     * @param firstName the first name
     * @param lastName the last name
     * @param jobTitle the job title
     * @param timeStamp the update time
     */
    public NoteAuthor(String firstName, String lastName,
            String jobTitle, Timestamp timeStamp) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.jobTitle = jobTitle;
        this.timeStamp = timeStamp;
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
     * Get the value of timeStamp
     *
     * @return the value of timeStamp
     */
    public Timestamp getTimeStamp() {
        return timeStamp;
    }

    /**
     * Set the value of timeStamp
     *
     * @param timeStamp new value of timeStamp
     */
    public void setTimeStamp(Timestamp timeStamp) {
        this.timeStamp = timeStamp;
    }
}
