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

/**
 * This class represents a healthy target range for a patient's measurement.
 *
 * @author Bryan Daniel
 * @version 1, April 8, 2016
 */
public class HealthyTarget {

    /**
     * The upper bound of the healthy range
     */
    private BigDecimal upperBound;

    /**
     * The lower bound of the healthy range
     */
    private BigDecimal lowerBound;

    /**
     * Default constructor
     */
    public HealthyTarget() {
        upperBound = null;
        lowerBound = null;
    }

    /**
     * Parameterized constructor
     *
     * @param upperBound the upper bound
     * @param lowerBound the lower bound
     */
    public HealthyTarget(BigDecimal upperBound, BigDecimal lowerBound) {
        this.upperBound = upperBound;
        this.lowerBound = lowerBound;
    }

    /**
     * Get the value of lowerBound
     *
     * @return the value of lowerBound
     */
    public BigDecimal getLowerBound() {
        return lowerBound;
    }

    /**
     * Set the value of lowerBound
     *
     * @param lowerBound new value of lowerBound
     */
    public void setLowerBound(BigDecimal lowerBound) {
        this.lowerBound = lowerBound;
    }

    /**
     * Get the value of upperBound
     *
     * @return the value of upperBound
     */
    public BigDecimal getUpperBound() {
        return upperBound;
    }

    /**
     * Set the value of upperBound
     *
     * @param upperBound new value of upperBound
     */
    public void setUpperBound(BigDecimal upperBound) {
        this.upperBound = upperBound;
    }

}
