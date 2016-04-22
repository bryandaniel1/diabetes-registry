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

/**
 * Holds the references for healthy target ranges
 *
 * @author Bryan Daniel
 * @version 1, April 8, 2016
 */
public class HealthyTargetReference {

    /**
     * Instance variables
     */
    private HealthyTarget a1c;
    private HealthyTarget alt;
    private HealthyTarget ast;
    private HealthyTarget bloodPressureDiastole;
    private HealthyTarget bloodPressureSystole;
    private HealthyTarget bmi;
    private HealthyTarget creatinine;
    private HealthyTarget egfr;
    private HealthyTarget glucoseAc;
    private HealthyTarget hdlFemale;
    private HealthyTarget hdlMale;
    private HealthyTarget ldl;
    private HealthyTarget physicalActivity;
    private HealthyTarget psa;
    private HealthyTarget t4;
    private HealthyTarget triglycerides;
    private HealthyTarget tsh;
    private HealthyTarget uacr;
    private HealthyTarget waistFemale;
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
     * @param a1c
     * @param alt
     * @param ast
     * @param bloodPressureDiastole
     * @param bloodPressureSystole
     * @param bmi
     * @param creatinine
     * @param egfr
     * @param glucoseAc
     * @param hdlFemale
     * @param hdlMale
     * @param ldl
     * @param physicalActivity
     * @param psa
     * @param t4
     * @param triglycerides
     * @param tsh
     * @param uacr
     * @param waistFemale
     * @param waistMale
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
