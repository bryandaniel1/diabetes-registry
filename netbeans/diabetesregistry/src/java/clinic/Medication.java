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

import java.sql.Date;

/**
 * Represents a patient medication
 *
 * @author Bryan Daniel
 * @version 1, April 8, 2016
 */
public class Medication {

    /**
     * The med medicationId
     */
    private String medicationId;

    /**
     * The med medicationName
     */
    private String medicationName;

    /**
     * The med class
     */
    private String medicationClass;

    /**
     * The date reviewed
     */
    private Date dateReviewed;

    /**
     * Default constructor
     */
    public Medication() {
        medicationId = null;
        medicationName = null;
        medicationClass = null;
        dateReviewed = null;
    }

    /**
     * Parameterized constructor
     *
     * @param medicationId the med id
     * @param medicationName the med name
     * @param medicationClass the med class
     * @param dateReviewed the date reviewed
     */
    public Medication(String medicationId, String medicationName,
            String medicationClass, Date dateReviewed) {
        this.medicationId = medicationId;
        this.medicationName = medicationName;
        this.medicationClass = medicationClass;
        this.dateReviewed = dateReviewed;
    }

    /**
     * Get the value of medicationId
     *
     * @return the value of medicationId
     */
    public String getMedicationId() {
        return medicationId;
    }

    /**
     * Set the value of medicationId
     *
     * @param medicationId new value of medicationId
     */
    public void setMedicationId(String medicationId) {
        this.medicationId = medicationId;
    }

    /**
     * Get the value of medicationName
     *
     * @return the value of medicationName
     */
    public String getMedicationName() {
        return medicationName;
    }

    /**
     * Set the value of medicationName
     *
     * @param medicationName new value of medicationName
     */
    public void setMedicationName(String medicationName) {
        this.medicationName = medicationName;
    }

    /**
     * Get the value of medicationClass
     *
     * @return the value of medicationClass
     */
    public String getMedicationClass() {
        return medicationClass;
    }

    /**
     * Set the value of medicationClass
     *
     * @param medicationClass new value of medicationClass
     */
    public void setMedicationClass(String medicationClass) {
        this.medicationClass = medicationClass;
    }

    /**
     * Get the value of dateReviewed
     *
     * @return the value of dateReviewed
     */
    public Date getDateReviewed() {
        return dateReviewed;
    }

    /**
     * Set the value of dateReviewed
     *
     * @param dateReviewed new value of dateReviewed
     */
    public void setDateReviewed(Date dateReviewed) {
        this.dateReviewed = dateReviewed;
    }

}
