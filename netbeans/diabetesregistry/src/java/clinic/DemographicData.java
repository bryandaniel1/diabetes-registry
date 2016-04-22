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
import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Holds demographic data for a patient population
 *
 * @author Bryan Daniel
 * @version 1, April 8, 2016
 */
public class DemographicData implements Serializable {

    private int totalPatients;
    private BigDecimal percentMale;
    private BigDecimal percentFemale;
    private BigDecimal percentWhite;
    private BigDecimal percentAfricanAmerican;
    private BigDecimal percentAsian;
    private BigDecimal percentIndian;
    private BigDecimal percentHispanic;
    private BigDecimal percentMiddleEastern;
    private BigDecimal percentOther;
    private ArrayList<Integer> ages;

    /**
     * Default constructor
     */
    public DemographicData() {
        percentMale = null;
        percentFemale = null;
        percentWhite = null;
        percentAfricanAmerican = null;
        percentAsian = null;
        percentIndian = null;
        percentHispanic = null;
        percentMiddleEastern = null;
        percentOther = null;
        ages = null;
    }

    /**
     * Parameterized constructor
     *
     * @param totalPatients total number of patients
     * @param percentMale percentage of male patients
     * @param percentFemale percentage of female patients
     * @param percentWhite percent white
     * @param percentAfricanAmerican percent African American
     * @param percentAsian percent Asian
     * @param percentIndian percent Native American
     * @param percentHispanic percent Hispanic
     * @param percentMiddleEastern percent Middle Eastern
     * @param percentOther percent other race
     * @param ages all patient ages
     */
    public DemographicData(int totalPatients, BigDecimal percentMale, BigDecimal percentFemale, BigDecimal percentWhite, BigDecimal percentAfricanAmerican, BigDecimal percentAsian, BigDecimal percentIndian, BigDecimal percentHispanic, BigDecimal percentMiddleEastern, BigDecimal percentOther, ArrayList<Integer> ages) {
        this.totalPatients = totalPatients;
        this.percentMale = percentMale;
        this.percentFemale = percentFemale;
        this.percentWhite = percentWhite;
        this.percentAfricanAmerican = percentAfricanAmerican;
        this.percentAsian = percentAsian;
        this.percentIndian = percentIndian;
        this.percentHispanic = percentHispanic;
        this.percentMiddleEastern = percentMiddleEastern;
        this.percentOther = percentOther;
        this.ages = ages;
    }

    /**
     * Get the value of totalPatients
     *
     * @return the value of totalPatients
     */
    public int getTotalPatients() {
        return totalPatients;
    }

    /**
     * Set the value of totalPatients
     *
     * @param totalPatients new value of totalPatients
     */
    public void setTotalPatients(int totalPatients) {
        this.totalPatients = totalPatients;
    }

    /**
     * Get the value of percentMale
     *
     * @return the value of percentMale
     */
    public BigDecimal getPercentMale() {
        return percentMale;
    }

    /**
     * Set the value of percentMale
     *
     * @param percentMale new value of percentMale
     */
    public void setPercentMale(BigDecimal percentMale) {
        this.percentMale = percentMale;
    }

    /**
     * Get the value of percentFemale
     *
     * @return the value of percentFemale
     */
    public BigDecimal getPercentFemale() {
        return percentFemale;
    }

    /**
     * Set the value of percentFemale
     *
     * @param percentFemale new value of percentFemale
     */
    public void setPercentFemale(BigDecimal percentFemale) {
        this.percentFemale = percentFemale;
    }

    /**
     * Get the value of percentWhite
     *
     * @return the value of percentWhite
     */
    public BigDecimal getPercentWhite() {
        return percentWhite;
    }

    /**
     * Set the value of percentWhite
     *
     * @param percentWhite new value of percentWhite
     */
    public void setPercentWhite(BigDecimal percentWhite) {
        this.percentWhite = percentWhite;
    }

    /**
     * Get the value of percentAfricanAmerican
     *
     * @return the value of percentAfricanAmerican
     */
    public BigDecimal getPercentAfricanAmerican() {
        return percentAfricanAmerican;
    }

    /**
     * Set the value of percentAfricanAmerican
     *
     * @param percentAfricanAmerican new value of percentAfricanAmerican
     */
    public void setPercentAfricanAmerican(BigDecimal percentAfricanAmerican) {
        this.percentAfricanAmerican = percentAfricanAmerican;
    }

    /**
     * Get the value of percentAsian
     *
     * @return the value of percentAsian
     */
    public BigDecimal getPercentAsian() {
        return percentAsian;
    }

    /**
     * Set the value of percentAsian
     *
     * @param percentAsian new value of percentAsian
     */
    public void setPercentAsian(BigDecimal percentAsian) {
        this.percentAsian = percentAsian;
    }

    /**
     * Get the value of percentIndian
     *
     * @return the value of percentIndian
     */
    public BigDecimal getPercentIndian() {
        return percentIndian;
    }

    /**
     * Set the value of percentIndian
     *
     * @param percentIndian new value of percentIndian
     */
    public void setPercentIndian(BigDecimal percentIndian) {
        this.percentIndian = percentIndian;
    }

    /**
     * Get the value of percentHispanic
     *
     * @return the value of percentHispanic
     */
    public BigDecimal getPercentHispanic() {
        return percentHispanic;
    }

    /**
     * Set the value of percentHispanic
     *
     * @param percentHispanic new value of percentHispanic
     */
    public void setPercentHispanic(BigDecimal percentHispanic) {
        this.percentHispanic = percentHispanic;
    }

    /**
     * Get the value of percentMiddleEastern
     *
     * @return the value of percentMiddleEastern
     */
    public BigDecimal getPercentMiddleEastern() {
        return percentMiddleEastern;
    }

    /**
     * Set the value of percentMiddleEastern
     *
     * @param percentMiddleEastern new value of percentMiddleEastern
     */
    public void setPercentMiddleEastern(BigDecimal percentMiddleEastern) {
        this.percentMiddleEastern = percentMiddleEastern;
    }

    /**
     * Get the value of percentOther
     *
     * @return the value of percentOther
     */
    public BigDecimal getPercentOther() {
        return percentOther;
    }

    /**
     * Set the value of percentOther
     *
     * @param percentOther new value of percentOther
     */
    public void setPercentOther(BigDecimal percentOther) {
        this.percentOther = percentOther;
    }

    /**
     * Get the value of ages
     *
     * @return the value of ages
     */
    public ArrayList<Integer> getAges() {
        return ages;
    }

    /**
     * Set the value of ages
     *
     * @param ages new value of ages
     */
    public void setAges(ArrayList<Integer> ages) {
        this.ages = ages;
    }

}
