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
 * Represents a blood pressure measurement
 *
 * @author Bryan Daniel
 * @version 2, March 16, 2017
 */
public class BloodPressureResult implements Serializable {
    
    /**
     * Serial version UID
     */
    private static final long serialVersionUID = -5859016112908759783L;

    /**
     * The date the blood pressure was measured
     */
    private Date date;

    /**
     * The systolic value of the blood pressure measurement
     */
    private int systolicValue;

    /**
     * The diastolic value of the blood pressure measurement
     */
    private int diastolicValue;

    /**
     * Indicates if the patient is on an ACE or ARB treatment
     */
    private boolean aceOrArb;

    /**
     * Default constructor
     */
    public BloodPressureResult() {
        date = null;
    }

    /**
     * Parameterized constructor
     *
     * @param date the date
     * @param sValue the systolic value
     * @param dValue the diastolic value
     * @param aceOrArb boolean for ACE or ARB medication
     */
    public BloodPressureResult(Date date, int sValue, int dValue, boolean aceOrArb) {
        this.date = date;
        this.systolicValue = sValue;
        this.diastolicValue = dValue;
        this.aceOrArb = aceOrArb;
    }

    /**
     * Get the value of date
     *
     * @return the systolicValue of date
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
     * Get the value of systolicValue
     *
     * @return the value of systolicValue
     */
    public int getSystolicValue() {
        return systolicValue;
    }

    /**
     * Set the value of systolicValue
     *
     * @param sValue new value of systolicValue
     */
    public void setSystolicValue(int sValue) {
        this.systolicValue = sValue;
    }

    /**
     * Get the value of diastolicValue
     *
     * @return the value of diastolicValue
     */
    public int getDiastolicValue() {
        return diastolicValue;
    }

    /**
     * Set the value of diastolicValue
     *
     * @param dValue new value of diastolicValue
     */
    public void setDiastolicValue(int dValue) {
        this.diastolicValue = dValue;
    }

    /**
     * Get the value of aceOrArb
     *
     * @return the value of aceOrArb
     */
    public boolean getAceOrArb() {
        return aceOrArb;
    }

    /**
     * Set the value of aceOrArb
     *
     * @param aceOrArb new value of aceOrArb
     */
    public void setAceOrArb(boolean aceOrArb) {
        this.aceOrArb = aceOrArb;
    }

}
