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
import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * This class contains data for statistical representation of clinic population
 * health.
 *
 * @author Bryan Daniel
 * @version 2, March 16, 2017
 */
public class Stats implements Serializable {
    
    /**
     * Serial version UID
     */
    private static final long serialVersionUID = -5835230712396501852L;

    /**
     * The statistic as a mean value
     */
    private BigDecimal average;

    /**
     * The statistic as a percentage
     */
    private BigDecimal percentage;

    /**
     * Multiple lists of values for statistical representation
     */
    private ArrayList<ArrayList<CategoricalValue>> groups;

    /**
     * Default constructor
     */
    public Stats() {
        average = null;
        percentage = null;
        groups = null;
    }

    /**
     * Parameterized constructor
     *
     * @param average the average
     * @param percentage the percentage
     * @param groups the categorical groups
     */
    public Stats(BigDecimal average, BigDecimal percentage,
            ArrayList<ArrayList<CategoricalValue>> groups) {
        this.average = average;
        this.percentage = percentage;
        this.groups = groups;
    }

    /**
     * Get the value of average
     *
     * @return the value of average
     */
    public BigDecimal getAverage() {
        return average;
    }

    /**
     * Set the value of average
     *
     * @param average new value of average
     */
    public void setAverage(BigDecimal average) {
        this.average = average;
    }

    /**
     * Get the value of percentage
     *
     * @return the value of percentage
     */
    public BigDecimal getPercentage() {
        return percentage;
    }

    /**
     * Set the value of percentage
     *
     * @param percentage new value of percentage
     */
    public void setPercentage(BigDecimal percentage) {
        this.percentage = percentage;
    }

    /**
     * Get the value of groups
     *
     * @return the value of groups
     */
    public ArrayList<ArrayList<CategoricalValue>> getGroups() {
        return groups;
    }

    /**
     * Set the value of groups
     *
     * @param groups new value of groups
     */
    public void setGroups(ArrayList<ArrayList<CategoricalValue>> groups) {
        this.groups = groups;
    }

}
