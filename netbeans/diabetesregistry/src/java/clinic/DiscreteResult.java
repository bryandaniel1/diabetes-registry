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
 * This class represents a discrete measurement for a patient. This type of
 * object may hold unit measurements for a patient.
 *
 * @author Bryan Daniel
 * @version 1, April 8, 2016
 */
public class DiscreteResult {

    /**
     * The date recorded
     */
    private Date date;
    
    /**
     * The value of the measurement
     */
    private int value;

    /**
     * Default constructor
     */
    public DiscreteResult() {
        date = null;
    }

    /**
     * Parameterized constructor
     *
     * @param date the date
     * @param value the value
     */
    public DiscreteResult(Date date, int value) {
        this.date = date;
        this.value = value;
    }

    /**
     * Get the value of date
     *
     * @return the value of date
     */
    public Date getDate() {
        return date;
    }

    /**
     * Set the value of date
     *
     * @param date new value of date
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Get the value of value
     *
     * @return the value of value
     */
    public int getValue() {
        return value;
    }

    /**
     * Set the value of value
     *
     * @param value new value of value
     */
    public void setValue(int value) {
        this.value = value;
    }

}
