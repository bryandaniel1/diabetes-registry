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

/**
 * This class contains a category and an associated value for statistical
 * representation.
 *
 * @author Bryan Daniel
 * @version 2, March 16, 2017
 */
public class CategoricalValue implements Serializable {
    
    /**
     * Serial version UID
     */
    private static final long serialVersionUID = 7129467910997034359L;

    /**
     * The descriptive category
     */
    private String category;

    /**
     * The decimal value
     */
    private BigDecimal value;

    /**
     * The numerical category
     */
    private int numericalCategory;

    /**
     * Default constructor
     */
    public CategoricalValue() {
        category = null;
        value = null;
    }

    /**
     * Parameterized constructor
     *
     * @param category the category
     * @param value the value
     */
    public CategoricalValue(String category, BigDecimal value) {
        this.category = category;
        this.value = value;
    }

    /**
     * Parameterized constructor
     *
     * @param value the value
     * @param numericalCategory the numerical category
     */
    public CategoricalValue(BigDecimal value, int numericalCategory) {
        this.value = value;
        this.numericalCategory = numericalCategory;
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
     * Get the value of numericalCategory
     *
     * @return the value of numericalCategory
     */
    public int getNumericalCategory() {
        return numericalCategory;
    }

    /**
     * Set the value of numericalCategory
     *
     * @param numericalCategory new value of numericalCategory
     */
    public void setNumericalCategory(int numericalCategory) {
        this.numericalCategory = numericalCategory;
    }

}
