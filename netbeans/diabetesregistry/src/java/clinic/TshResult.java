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

import java.math.BigDecimal;
import java.sql.Date;

/**
 * This class represents a patient's TSH result.
 *
 * @author Bryan Daniel
 * @version 1, April 8, 2016
 */
public class TshResult {

    /**
     * The date recorded
     */
    private Date date;

    /**
     * The value of the measurement
     */
    private BigDecimal value;

    /**
     * Indication of thyroid treatment
     */
    private boolean onThyroidTreatment;

    /**
     * Default constructor
     */
    public TshResult() {
        date = null;
        value = null;
    }

    /**
     * Parameterized constructor
     *
     * @param date the date
     * @param value the value
     * @param onThyroidTreatment TSH on thyroid treatment
     */
    public TshResult(Date date, BigDecimal value, boolean onThyroidTreatment) {
        this.date = date;
        this.value = value;
        this.onThyroidTreatment = onThyroidTreatment;
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
    public BigDecimal getValue() {
        return value;
    }

    /**
     * Set the value of value
     *
     * @param value new value of value
     */
    public void setValue(BigDecimal value) {
        this.value = value;
    }

    /**
     * Get the value of onThyroidTreatment
     *
     * @return the value of onThyroidTreatment
     */
    public boolean getOnThyroidTreatment() {
        return onThyroidTreatment;
    }

    /**
     * Set the value of onThyroidTreatment
     *
     * @param onThyroidTreatment new value of onThyroidTreatment
     */
    public void setOnThyroidTreatment(boolean onThyroidTreatment) {
        this.onThyroidTreatment = onThyroidTreatment;
    }
}
