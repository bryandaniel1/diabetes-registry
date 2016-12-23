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
import java.sql.Date;

/**
 * This class represents a patient's categorical result. This type of object may
 * hold notes or other information regarding patient health.
 *
 * @author Bryan Daniel
 * @version 1, April 8, 2016
 */
public class CategoricalResult implements Serializable {

    /**
     * The category
     */
    private String category;

    /**
     * The definition
     */
    private String definition;

    /**
     * The date recorded
     */
    private Date dateRecorded;

    /**
     * Default constructor
     */
    public CategoricalResult() {
        category = null;
        definition = null;
        dateRecorded = null;
    }

    /**
     * Parameterized constructor
     *
     * @param category the category
     * @param definition the definition
     * @param dateRecorded the date recorded
     */
    public CategoricalResult(String category, String definition, Date dateRecorded) {
        this.category = category;
        this.definition = definition;
        this.dateRecorded = dateRecorded;
    }

    /**
     * Get the value of category
     *
     * @return the value of category
     */
    public String getCategory() {
        return category;
    }

    /**
     * Set the value of category
     *
     * @param category new value of category
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Get the value of definition
     *
     * @return the value of definition
     */
    public String getDefinition() {
        return definition;
    }

    /**
     * Set the value of definition
     *
     * @param definition new value of definition
     */
    public void setDefinition(String definition) {
        this.definition = definition;
    }

    /**
     * Get the value of dateRecorded
     *
     * @return the value of dateRecorded
     */
    public Date getDateRecorded() {
        return dateRecorded;
    }

    /**
     * Set the value of dateRecorded
     *
     * @param dateRecorded new value of dateRecorded
     */
    public void setDateRecorded(Date dateRecorded) {
        this.dateRecorded = dateRecorded;
    }

}
