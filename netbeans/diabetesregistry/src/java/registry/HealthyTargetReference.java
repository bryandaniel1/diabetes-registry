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

/**
 * This class holds the references for healthy target ranges.
 *
 * @author Bryan Daniel
 * @version 2, March 16, 2017
 */
public class HealthyTargetReference implements Serializable {
    
    /**
     * Serial version UID
     */
    private static final long serialVersionUID = 8441831892864606485L;

    /**
     * The healthy target for A1C
     */
    private HealthyTarget a1c;

    /**
     * The healthy target for ALT
     */
    private HealthyTarget alt;

    /**
     * The healthy target for AST
     */
    private HealthyTarget ast;

    /**
     * The healthy target for diastolic blood pressure
     */
    private HealthyTarget bloodPressureDiastole;

    /**
     * The healthy target for systolic blood pressure
     */
    private HealthyTarget bloodPressureSystole;

    /**
     * The healthy target for BMI
     */
    private HealthyTarget bmi;

    /**
     * The healthy target for creatinine
     */
    private HealthyTarget creatinine;

    /**
     * The healthy target for eGFR
     */
    private HealthyTarget egfr;

    /**
     * The healthy target for fasting glucose
     */
    private HealthyTarget glucoseAc;

    /**
     * The healthy target for female HDL
     */
    private HealthyTarget hdlFemale;

    /**
     * The healthy target for male HDL
     */
    private HealthyTarget hdlMale;

    /**
     * The healthy target for LDL
     */
    private HealthyTarget ldl;

    /**
     * The healthy target for physical activity
     */
    private HealthyTarget physicalActivity;

    /**
     * The healthy target for PSA
     */
    private HealthyTarget psa;

    /**
     * The healthy target for T4
     */
    private HealthyTarget t4;

    /**
     * The healthy target for triglycerides
     */
    private HealthyTarget triglycerides;

    /**
     * The healthy target for TSH
     */
    private HealthyTarget tsh;

    /**
     * The healthy target for UACR
     */
    private HealthyTarget uacr;

    /**
     * The healthy target for female waist
     */
    private HealthyTarget waistFemale;

    /**
     * The healthy target for male waist
     */
    private HealthyTarget waistMale;

    /**
     * Default constructor
     */
    public HealthyTargetReference() {
        a1c = null;
        alt = null;
        ast = null;
        bloodPressureDiastole = null;
        bloodPressureSystole = null;
        bmi = null;
        creatinine = null;
        egfr = null;
        glucoseAc = null;
        hdlFemale = null;
        hdlMale = null;
        ldl = null;
        physicalActivity = null;
        psa = null;
        t4 = null;
        triglycerides = null;
        tsh = null;
        uacr = null;
        waistFemale = null;
        waistMale = null;
    }

    /**
     * Parameterized constructor
     *
     * @param a1c the target A1C
     * @param alt the target ALT
     * @param ast the target AST
     * @param bloodPressureDiastole the target diastolic blood pressure
     * @param bloodPressureSystole the target systolic blood pressure
     * @param bmi the target BMI
     * @param creatinine the target creatinine
     * @param egfr the target eGFR
     * @param glucoseAc the target glucose AC
     * @param hdlFemale the target HDL for females
     * @param hdlMale the target HDL for males
     * @param ldl the target LDL
     * @param physicalActivity the target physical activity in weekly minutes
     * @param psa the target PSA
     * @param t4 the target T4
     * @param triglycerides the target triglycerides
     * @param tsh the target TSH
     * @param uacr the target UACR
     * @param waistFemale the target waist measurement for females
     * @param waistMale the target waist measurement for males
     */
    public HealthyTargetReference(HealthyTarget a1c, HealthyTarget alt, HealthyTarget ast,
            HealthyTarget bloodPressureDiastole, HealthyTarget bloodPressureSystole,
            HealthyTarget bmi, HealthyTarget creatinine, HealthyTarget egfr,
            HealthyTarget glucoseAc, HealthyTarget hdlFemale, HealthyTarget hdlMale,
            HealthyTarget ldl, HealthyTarget physicalActivity, HealthyTarget psa,
            HealthyTarget t4, HealthyTarget triglycerides, HealthyTarget tsh,
            HealthyTarget uacr, HealthyTarget waistFemale, HealthyTarget waistMale) {
        this.a1c = a1c;
        this.alt = alt;
        this.ast = ast;
        this.bloodPressureDiastole = bloodPressureDiastole;
        this.bloodPressureSystole = bloodPressureSystole;
        this.bmi = bmi;
        this.creatinine = creatinine;
        this.egfr = egfr;
        this.glucoseAc = glucoseAc;
        this.hdlFemale = hdlFemale;
        this.hdlMale = hdlMale;
        this.ldl = ldl;
        this.physicalActivity = physicalActivity;
        this.psa = psa;
        this.t4 = t4;
        this.triglycerides = triglycerides;
        this.tsh = tsh;
        this.uacr = uacr;
        this.waistFemale = waistFemale;
        this.waistMale = waistMale;
    }

    /**
     * Get the value of waistMale
     *
     * @return the value of waistMale
     */
    public HealthyTarget getWaistMale() {
        return waistMale;
    }

    /**
     * Set the value of waistMale
     *
     * @param waistMale new value of waistMale
     */
    public void setWaistMale(HealthyTarget waistMale) {
        this.waistMale = waistMale;
    }

    /**
     * Get the value of waistFemale
     *
     * @return the value of waistFemale
     */
    public HealthyTarget getWaistFemale() {
        return waistFemale;
    }

    /**
     * Set the value of waistFemale
     *
     * @param waistFemale new value of waistFemale
     */
    public void setWaistFemale(HealthyTarget waistFemale) {
        this.waistFemale = waistFemale;
    }

    /**
     * Get the value of uacr
     *
     * @return the value of uacr
     */
    public HealthyTarget getUacr() {
        return uacr;
    }

    /**
     * Set the value of uacr
     *
     * @param uacr new value of uacr
     */
    public void setUacr(HealthyTarget uacr) {
        this.uacr = uacr;
    }

    /**
     * Get the value of tsh
     *
     * @return the value of tsh
     */
    public HealthyTarget getTsh() {
        return tsh;
    }

    /**
     * Set the value of tsh
     *
     * @param tsh new value of tsh
     */
    public void setTsh(HealthyTarget tsh) {
        this.tsh = tsh;
    }

    /**
     * Get the value of triglycerides
     *
     * @return the value of triglycerides
     */
    public HealthyTarget getTriglycerides() {
        return triglycerides;
    }

    /**
     * Set the value of triglycerides
     *
     * @param triglycerides new value of triglycerides
     */
    public void setTriglycerides(HealthyTarget triglycerides) {
        this.triglycerides = triglycerides;
    }

    /**
     * Get the value of t4
     *
     * @return the value of t4
     */
    public HealthyTarget getT4() {
        return t4;
    }

    /**
     * Set the value of t4
     *
     * @param t4 new value of t4
     */
    public void setT4(HealthyTarget t4) {
        this.t4 = t4;
    }

    /**
     * Get the value of psa
     *
     * @return the value of psa
     */
    public HealthyTarget getPsa() {
        return psa;
    }

    /**
     * Set the value of psa
     *
     * @param psa new value of psa
     */
    public void setPsa(HealthyTarget psa) {
        this.psa = psa;
    }

    /**
     * Get the value of physicalActivity
     *
     * @return the value of physicalActivity
     */
    public HealthyTarget getPhysicalActivity() {
        return physicalActivity;
    }

    /**
     * Set the value of physicalActivity
     *
     * @param physicalActivity new value of physicalActivity
     */
    public void setPhysicalActivity(HealthyTarget physicalActivity) {
        this.physicalActivity = physicalActivity;
    }

    /**
     * Get the value of ldl
     *
     * @return the value of ldl
     */
    public HealthyTarget getLdl() {
        return ldl;
    }

    /**
     * Set the value of ldl
     *
     * @param ldl new value of ldl
     */
    public void setLdl(HealthyTarget ldl) {
        this.ldl = ldl;
    }

    /**
     * Get the value of hdlMale
     *
     * @return the value of hdlMale
     */
    public HealthyTarget getHdlMale() {
        return hdlMale;
    }

    /**
     * Set the value of hdlMale
     *
     * @param hdlMale new value of hdlMale
     */
    public void setHdlMale(HealthyTarget hdlMale) {
        this.hdlMale = hdlMale;
    }

    /**
     * Get the value of hdlFemale
     *
     * @return the value of hdlFemale
     */
    public HealthyTarget getHdlFemale() {
        return hdlFemale;
    }

    /**
     * Set the value of hdlFemale
     *
     * @param hdlFemale new value of hdlFemale
     */
    public void setHdlFemale(HealthyTarget hdlFemale) {
        this.hdlFemale = hdlFemale;
    }

    /**
     * Get the value of glucoseAc
     *
     * @return the value of glucoseAc
     */
    public HealthyTarget getGlucoseAc() {
        return glucoseAc;
    }

    /**
     * Set the value of glucoseAc
     *
     * @param glucoseAc new value of glucoseAc
     */
    public void setGlucoseAc(HealthyTarget glucoseAc) {
        this.glucoseAc = glucoseAc;
    }

    /**
     * Get the value of egfr
     *
     * @return the value of egfr
     */
    public HealthyTarget getEgfr() {
        return egfr;
    }

    /**
     * Set the value of egfr
     *
     * @param egfr new value of egfr
     */
    public void setEgfr(HealthyTarget egfr) {
        this.egfr = egfr;
    }

    /**
     * Get the value of creatinine
     *
     * @return the value of creatinine
     */
    public HealthyTarget getCreatinine() {
        return creatinine;
    }

    /**
     * Set the value of creatinine
     *
     * @param creatinine new value of creatinine
     */
    public void setCreatinine(HealthyTarget creatinine) {
        this.creatinine = creatinine;
    }

    /**
     * Get the value of bmi
     *
     * @return the value of bmi
     */
    public HealthyTarget getBmi() {
        return bmi;
    }

    /**
     * Set the value of bmi
     *
     * @param bmi new value of bmi
     */
    public void setBmi(HealthyTarget bmi) {
        this.bmi = bmi;
    }

    /**
     * Get the value of bloodPressureSystole
     *
     * @return the value of bloodPressureSystole
     */
    public HealthyTarget getBloodPressureSystole() {
        return bloodPressureSystole;
    }

    /**
     * Set the value of bloodPressureSystole
     *
     * @param bloodPressureSystole new value of bloodPressureSystole
     */
    public void setBloodPressureSystole(HealthyTarget bloodPressureSystole) {
        this.bloodPressureSystole = bloodPressureSystole;
    }

    /**
     * Get the value of bloodPressureDiastole
     *
     * @return the value of bloodPressureDiastole
     */
    public HealthyTarget getBloodPressureDiastole() {
        return bloodPressureDiastole;
    }

    /**
     * Set the value of bloodPressureDiastole
     *
     * @param bloodPressureDiastole new value of bloodPressureDiastole
     */
    public void setBloodPressureDiastole(HealthyTarget bloodPressureDiastole) {
        this.bloodPressureDiastole = bloodPressureDiastole;
    }

    /**
     * Get the value of ast
     *
     * @return the value of ast
     */
    public HealthyTarget getAst() {
        return ast;
    }

    /**
     * Set the value of ast
     *
     * @param ast new value of ast
     */
    public void setAst(HealthyTarget ast) {
        this.ast = ast;
    }

    /**
     * Get the value of alt
     *
     * @return the value of alt
     */
    public HealthyTarget getAlt() {
        return alt;
    }

    /**
     * Set the value of alt
     *
     * @param alt new value of alt
     */
    public void setAlt(HealthyTarget alt) {
        this.alt = alt;
    }

    /**
     * Get the value of a1c
     *
     * @return the value of a1c
     */
    public HealthyTarget getA1c() {
        return a1c;
    }

    /**
     * Set the value of a1c
     *
     * @param a1c new value of a1c
     */
    public void setA1c(HealthyTarget a1c) {
        this.a1c = a1c;
    }

}
