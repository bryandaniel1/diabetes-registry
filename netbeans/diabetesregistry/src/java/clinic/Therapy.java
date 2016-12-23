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
 * This class represents a therapy.
 *
 * @author Bryan Daniel
 * @version 1, April 8, 2016
 */
public class Therapy {

    /**
     * The prescription class
     */
    private String prescriptionClass;

    /**
     * The therapy type
     */
    private String therapyType;

    /**
     * The date reviewed
     */
    private Date dateReviewed;

    /**
     * Default constructor
     */
    public Therapy() {
        prescriptionClass = null;
        therapyType = null;
        dateReviewed = null;
    }

    /**
     * Parameterized constructor
     *
     * @param prescriptionClass the prescription class
     * @param therapyType the therapy type
     * @param dateReviewed the date reviewed
     */
    public Therapy(String prescriptionClass, String therapyType,
            Date dateReviewed) {
        this.prescriptionClass = prescriptionClass;
        this.therapyType = therapyType;
        this.dateReviewed = dateReviewed;
    }

    /**
     * Returns the prescription class
     *
     * @return the prescription class
     */
    public String getPrescriptionClass() {
        return prescriptionClass;
    }

    /**
     * Returns the therapy type
     *
     * @return the therapy type
     */
    public String getTherapyType() {
        return therapyType;
    }

    /**
     * Sets the prescription class
     *
     * @param prescriptionClass the prescription class
     */
    public void setPrescriptionClass(String prescriptionClass) {
        this.prescriptionClass = prescriptionClass;
    }

    /**
     * Sets the therapy type
     *
     * @param therapyType the therapy type
     */
    public void setTherapyType(String therapyType) {
        this.therapyType = therapyType;
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
