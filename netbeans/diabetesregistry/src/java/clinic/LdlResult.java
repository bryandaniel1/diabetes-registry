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
 * Represents a patient's LDL result
 *
 * @author Bryan Daniel
 * @version 1, April 8, 2016
 */
public class LdlResult {

    /**
     * Instance variables
     */
    private Date date;
    private BigDecimal value;
    private boolean postMi;
    private boolean onStatin;

    /**
     * Default constructor
     */
    public LdlResult() {
        date = null;
        value = null;
    }

    /**
     * Parameterized constructor
     *
     * @param date the date
     * @param value the value
     * @param postMi the boolean for post MI
     * @param onStatin LDL on statin
     */
    public LdlResult(Date date, BigDecimal value, boolean postMi,
            boolean onStatin) {
        this.date = date;
        this.value = value;
        this.postMi = postMi;
        this.onStatin = onStatin;
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
     * Get the value of postMi
     *
     * @return the value of postMi
     */
    public boolean getPostMi() {
        return postMi;
    }

    /**
     * Set the value of postMi
     *
     * @param postMi the value of postMI
     */
    public void setPostMi(boolean postMi) {
        this.postMi = postMi;
    }

    /**
     * Get the value of onStatin
     *
     * @return the value of onStatin
     */
    public boolean getOnStatin() {
        return onStatin;
    }

    /**
     * Set the value of onStatin
     *
     * @param onStatin new value of onStatin
     */
    public void setOnStatin(boolean onStatin) {
        this.onStatin = onStatin;
    }
}
