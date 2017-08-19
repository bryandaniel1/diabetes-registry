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
 * This class holds the healthy target status values for a patient.
 *
 * @author Bryan Daniel
 * @version 2, March 16, 2017
 */
public class HealthyTargetStatus implements Serializable {
    
    /**
     * Serial version UID
     */
    private static final long serialVersionUID = -5429196189519606838L;

    /**
     * A1C out-of-target indication
     */
    private boolean a1cOutOfTarget;

    /**
     * Glucose AC out-of-target indication
     */
    private boolean glucoseacOutOfTarget;

    /**
     * Glucose PC out-of-target indication
     */
    private boolean glucosepcOutOfTarget;

    /**
     * Indicates if the last glucose measurement was AC
     */
    private boolean lastGlucoseFasting;

    /**
     * LDL out-of-target indication
     */
    private boolean ldlOutOfTarget;

    /**
     * LDL post MI out-of-target indication
     */
    private boolean ldlPostMiOutOfTarget;

    /**
     * Male HDL out-of-target indication
     */
    private boolean hdlMaleOutOfTarget;

    /**
     * Female HDL out-of-target indication
     */
    private boolean hdlFemaleOutOfTarget;

    /**
     * Triglycerides out-of-target indication
     */
    private boolean triglyceridesOutOfTarget;

    /**
     * TSH out-of-target indication
     */
    private boolean tshOutOfTarget;

    /**
     * T4 out-of-target indication
     */
    private boolean t4OutOfTarget;

    /**
     * UACR out-of-target indication
     */
    private boolean uacrOutOfTarget;

    /**
     * eGFR out-of-target indication
     */
    private boolean egfrOutOfTarget;

    /**
     * Creatinine out-of-target indication
     */
    private boolean creatinineOutOfTarget;

    /**
     * BMI out-of-target indication
     */
    private boolean bmiOutOfTarget;

    /**
     * Male waist out-of-target indication
     */
    private boolean waistMaleOutOfTarget;

    /**
     * Female waist out-of-target indication
     */
    private boolean waistFemaleOutOfTarget;

    /**
     * Systolic blood pressure out-of-target indication
     */
    private boolean systolicBloodPressureOutOfTarget;

    /**
     * Diastolic blood pressure out-of-target indication
     */
    private boolean diastolicBloodPressureOutOfTarget;

    /**
     * Class attendance out-of-target indication
     */
    private boolean classAttendanceOutOfTarget;

    /**
     * Eye screening score out-of-target indication
     */
    private boolean eyeScreeningOutOfTarget;

    /**
     * Foot screening score out-of-target indication
     */
    private boolean footScreeningOutOfTarget;

    /**
     * Psychological screening score out-of-target indication
     */
    private boolean psychologicalScreeningOutOfTarget;

    /**
     * Physical activity out-of-target indication
     */
    private boolean physicalActivityOutOfTarget;

    /**
     * Influenza vaccine out-of-target indication
     */
    private boolean influenzaVaccineOutOfTarget;

    /**
     * PCV13 vaccine out-of-target indication
     */
    private boolean pcv13VaccineOutOfTarget;

    /**
     * PPSV23 vaccine out-of-target indication
     */
    private boolean ppsv23VaccineOutOfTarget;

    /**
     * Hepatitis B vaccine out-of-target indication
     */
    private boolean hepatitisBVaccineOutOfTarget;

    /**
     * TDAP vaccine out-of-target indication
     */
    private boolean tdapVaccineOutOfTarget;

    /**
     * Zoster vaccine out-of-target indication
     */
    private boolean zosterVaccineOutOfTarget;

    /**
     * Smoking status out-of-target indication
     */
    private boolean smokingStatusOutOfTarget;

    /**
     * Telephone follow-up out-of-target indication
     */
    private boolean telephoneFollowUpOutOfTarget;

    /**
     * AST out-of-target indication
     */
    private boolean astOutOfTarget;

    /**
     * ALT out-of-target indication
     */
    private boolean altOutOfTarget;

    /**
     * PSA out-of-target indication
     */
    private boolean psaOutOfTarget;

    /**
     * Hospitalization out-of-target indication
     */
    private boolean hospitalizationOutOfTarget;

    /**
     * Default constructor
     */
    public HealthyTargetStatus() {
    }

    /**
     * Parameterized constructor
     *
     * @param a1cOutOfTarget A1C out-of-target indication
     * @param glucoseacOutOfTarget glucose AC out-of-target indication
     * @param glucosepcOutOfTarget glucose PC out-of-target indication
     * @param lastGlucoseFasting indicates if the last glucose measurement was
     * AC
     * @param ldlOutOfTarget LDL out-of-target indication
     * @param ldlPostMiOutOfTarget LDL after myocardial infarction out-of-target
     * indication
     * @param hdlMaleOutOfTarget male HDL out-of-target indication
     * @param hdlFemaleOutOfTarget female HDL out-of-target indication
     * @param triglyceridesOutOfTarget triglycerides out-of-target indication
     * @param tshOutOfTarget TSH out-of-target indication
     * @param t4OutOfTarget T4 out-of-target indication
     * @param uacrOutOfTarget UACR out-of-target indication
     * @param egfrOutOfTarget eGFR out-of-target indication
     * @param creatinineOutOfTarget creatinine out-of-target indication
     * @param bmiOutOfTarget BMI out-of-target indication
     * @param waistMaleOutOfTarget male waist out-of-target indication
     * @param waistFemaleOutOfTarget female waist out-of-target indication
     * @param systolicBloodPressureOutOfTarget systolic BP out-of-target
     * indication
     * @param diastolicBloodPressureOutOfTarget diastolic BP out-of-target
     * indication
     * @param classAttendanceOutOfTarget class attendance out-of-target
     * indication
     * @param eyeScreeningOutOfTarget eye screening score out-of-target
     * indication
     * @param footScreeningOutOfTarget foot screening score out-of-target
     * indication
     * @param psychologicalScreeningOutOfTarget psychological screening score
     * out-of-target indication
     * @param physicalActivityOutOfTarget physical activity out-of-target
     * indication
     * @param influenzaVaccineOutOfTarget flu vaccine out-of-target indication
     * @param pcv13VaccineOutOfTarget PCV13 vaccine out-of-target indication
     * @param ppsv23VaccineOutOfTarget PPSV23 vaccine out-of-target indication
     * @param hepatitisBVaccineOutOfTarget hepatitis B vaccine out-of-target
     * indication
     * @param tdapVaccineOutOfTarget TDAP vaccine out-of-target indication
     * @param zosterVaccineOutOfTarget zoster vaccine out-of-target indication
     * @param smokingStatusOutOfTarget smoking status out-of-target indication
     * @param telephoneFollowUpOutOfTarget telephone follow-up out-of-target
     * indication
     * @param astOutOfTarget AST out-of-target indication
     * @param altOutOfTarget ALT out-of-target indication
     * @param psaOutOfTarget PSA out-of-target indication
     * @param hospitalizationOutOfTarget hospitalization out-of-target
     * indication
     */
    public HealthyTargetStatus(boolean a1cOutOfTarget, boolean glucoseacOutOfTarget,
            boolean glucosepcOutOfTarget, boolean lastGlucoseFasting, boolean ldlOutOfTarget,
            boolean ldlPostMiOutOfTarget, boolean hdlMaleOutOfTarget,
            boolean hdlFemaleOutOfTarget, boolean triglyceridesOutOfTarget,
            boolean tshOutOfTarget, boolean t4OutOfTarget, boolean uacrOutOfTarget,
            boolean egfrOutOfTarget, boolean creatinineOutOfTarget,
            boolean bmiOutOfTarget, boolean waistMaleOutOfTarget,
            boolean waistFemaleOutOfTarget, boolean systolicBloodPressureOutOfTarget,
            boolean diastolicBloodPressureOutOfTarget, boolean classAttendanceOutOfTarget,
            boolean eyeScreeningOutOfTarget, boolean footScreeningOutOfTarget,
            boolean psychologicalScreeningOutOfTarget,
            boolean physicalActivityOutOfTarget, boolean influenzaVaccineOutOfTarget,
            boolean pcv13VaccineOutOfTarget, boolean ppsv23VaccineOutOfTarget,
            boolean hepatitisBVaccineOutOfTarget, boolean tdapVaccineOutOfTarget,
            boolean zosterVaccineOutOfTarget, boolean smokingStatusOutOfTarget,
            boolean telephoneFollowUpOutOfTarget, boolean astOutOfTarget,
            boolean altOutOfTarget, boolean psaOutOfTarget,
            boolean hospitalizationOutOfTarget) {
        this.a1cOutOfTarget = a1cOutOfTarget;
        this.glucoseacOutOfTarget = glucoseacOutOfTarget;
        this.glucosepcOutOfTarget = glucosepcOutOfTarget;
        this.lastGlucoseFasting = lastGlucoseFasting;
        this.ldlOutOfTarget = ldlOutOfTarget;
        this.ldlPostMiOutOfTarget = ldlPostMiOutOfTarget;
        this.hdlMaleOutOfTarget = hdlMaleOutOfTarget;
        this.hdlFemaleOutOfTarget = hdlFemaleOutOfTarget;
        this.triglyceridesOutOfTarget = triglyceridesOutOfTarget;
        this.tshOutOfTarget = tshOutOfTarget;
        this.t4OutOfTarget = t4OutOfTarget;
        this.uacrOutOfTarget = uacrOutOfTarget;
        this.egfrOutOfTarget = egfrOutOfTarget;
        this.creatinineOutOfTarget = creatinineOutOfTarget;
        this.bmiOutOfTarget = bmiOutOfTarget;
        this.waistMaleOutOfTarget = waistMaleOutOfTarget;
        this.waistFemaleOutOfTarget = waistFemaleOutOfTarget;
        this.systolicBloodPressureOutOfTarget = systolicBloodPressureOutOfTarget;
        this.diastolicBloodPressureOutOfTarget = diastolicBloodPressureOutOfTarget;
        this.classAttendanceOutOfTarget = classAttendanceOutOfTarget;
        this.eyeScreeningOutOfTarget = eyeScreeningOutOfTarget;
        this.footScreeningOutOfTarget = footScreeningOutOfTarget;
        this.psychologicalScreeningOutOfTarget = psychologicalScreeningOutOfTarget;
        this.physicalActivityOutOfTarget = physicalActivityOutOfTarget;
        this.influenzaVaccineOutOfTarget = influenzaVaccineOutOfTarget;
        this.pcv13VaccineOutOfTarget = pcv13VaccineOutOfTarget;
        this.ppsv23VaccineOutOfTarget = ppsv23VaccineOutOfTarget;
        this.hepatitisBVaccineOutOfTarget = hepatitisBVaccineOutOfTarget;
        this.tdapVaccineOutOfTarget = tdapVaccineOutOfTarget;
        this.zosterVaccineOutOfTarget = zosterVaccineOutOfTarget;
        this.smokingStatusOutOfTarget = smokingStatusOutOfTarget;
        this.telephoneFollowUpOutOfTarget = telephoneFollowUpOutOfTarget;
        this.astOutOfTarget = astOutOfTarget;
        this.altOutOfTarget = altOutOfTarget;
        this.psaOutOfTarget = psaOutOfTarget;
        this.hospitalizationOutOfTarget = hospitalizationOutOfTarget;
    }

    /**
     * Get the value of a1cOutOfTarget
     *
     * @return the value of a1cOutOfTarget
     */
    public boolean getA1cOutOfTarget() {
        return a1cOutOfTarget;
    }

    /**
     * Set the value of a1cOutOfTarget
     *
     * @param a1cOutOfTarget new value of a1cOutOfTarget
     */
    public void setA1cOutOfTarget(boolean a1cOutOfTarget) {
        this.a1cOutOfTarget = a1cOutOfTarget;
    }

    /**
     * Get the value of glucoseacOutOfTarget
     *
     * @return the value of glucoseacOutOfTarget
     */
    public boolean getGlucoseacOutOfTarget() {
        return glucoseacOutOfTarget;
    }

    /**
     * Set the value of glucoseacOutOfTarget
     *
     * @param glucoseacOutOfTarget new value of glucoseacOutOfTarget
     */
    public void setGlucoseacOutOfTarget(boolean glucoseacOutOfTarget) {
        this.glucoseacOutOfTarget = glucoseacOutOfTarget;
    }

    /**
     * Get the value of glucosepcOutOfTarget
     *
     * @return the value of glucosepcOutOfTarget
     */
    public boolean getGlucosepcOutOfTarget() {
        return glucosepcOutOfTarget;
    }

    /**
     * Set the value of glucosepcOutOfTarget
     *
     * @param glucosepcOutOfTarget new value of glucosepcOutOfTarget
     */
    public void setGlucosepcOutOfTarget(boolean glucosepcOutOfTarget) {
        this.glucosepcOutOfTarget = glucosepcOutOfTarget;
    }

    /**
     * Get the value of lastGlucoseFasting
     *
     * @return the value of lastGlucoseFasting
     */
    public boolean getLastGlucoseFasting() {
        return lastGlucoseFasting;
    }

    /**
     * Set the value of lastGlucoseFasting
     *
     * @param lastGlucoseFasting new value of lastGlucoseFasting
     */
    public void setLastGlucoseFasting(boolean lastGlucoseFasting) {
        this.lastGlucoseFasting = lastGlucoseFasting;
    }

    /**
     * Get the value of ldlOutOfTarget
     *
     * @return the value of ldlOutOfTarget
     */
    public boolean getLdlOutOfTarget() {
        return ldlOutOfTarget;
    }

    /**
     * Set the value of ldlOutOfTarget
     *
     * @param ldlOutOfTarget new value of ldlOutOfTarget
     */
    public void setLdlOutOfTarget(boolean ldlOutOfTarget) {
        this.ldlOutOfTarget = ldlOutOfTarget;
    }

    /**
     * Get the value of ldlPostMiOutOfTarget
     *
     * @return the value of ldlPostMiOutOfTarget
     */
    public boolean getLdlPostMiOutOfTarget() {
        return ldlPostMiOutOfTarget;
    }

    /**
     * Set the value of ldlPostMiOutOfTarget
     *
     * @param ldlPostMiOutOfTarget new value of ldlPostMiOutOfTarget
     */
    public void setLdlPostMiOutOfTarget(boolean ldlPostMiOutOfTarget) {
        this.ldlPostMiOutOfTarget = ldlPostMiOutOfTarget;
    }

    /**
     * Get the value of hdlOutOfTarget
     *
     * @return the value of hdlOutOfTarget
     */
    public boolean getHdlMaleOutOfTarget() {
        return hdlMaleOutOfTarget;
    }

    /**
     * Set the value of hdlOutOfTarget
     *
     * @param hdlOutOfTarget new value of hdlOutOfTarget
     */
    public void setHdlMaleOutOfTarget(boolean hdlOutOfTarget) {
        this.hdlMaleOutOfTarget = hdlOutOfTarget;
    }

    /**
     * Get the value of hdlFemaleOutOfTarget
     *
     * @return the value of hdlFemaleOutOfTarget
     */
    public boolean getHdlFemaleOutOfTarget() {
        return hdlFemaleOutOfTarget;
    }

    /**
     * Set the value of hdlFemaleOutOfTarget
     *
     * @param hdlFemaleOutOfTarget new value of hdlFemaleOutOfTarget
     */
    public void setHdlFemaleOutOfTarget(boolean hdlFemaleOutOfTarget) {
        this.hdlFemaleOutOfTarget = hdlFemaleOutOfTarget;
    }

    /**
     * Get the value of triglyceridesOutOfTarget
     *
     * @return the value of triglyceridesOutOfTarget
     */
    public boolean getTriglyceridesOutOfTarget() {
        return triglyceridesOutOfTarget;
    }

    /**
     * Set the value of triglyceridesOutOfTarget
     *
     * @param triglyceridesOutOfTarget new value of triglyceridesOutOfTarget
     */
    public void setTriglyceridesOutOfTarget(boolean triglyceridesOutOfTarget) {
        this.triglyceridesOutOfTarget = triglyceridesOutOfTarget;
    }

    /**
     * Get the value of tshOutOfTarget
     *
     * @return the value of tshOutOfTarget
     */
    public boolean getTshOutOfTarget() {
        return tshOutOfTarget;
    }

    /**
     * Set the value of tshOutOfTarget
     *
     * @param tshOutOfTarget new value of tshOutOfTarget
     */
    public void setTshOutOfTarget(boolean tshOutOfTarget) {
        this.tshOutOfTarget = tshOutOfTarget;
    }

    /**
     * Get the value of t4OutOfTarget
     *
     * @return the value of t4OutOfTarget
     */
    public boolean getT4OutOfTarget() {
        return t4OutOfTarget;
    }

    /**
     * Set the value of t4OutOfTarget
     *
     * @param t4OutOfTarget new value of t4OutOfTarget
     */
    public void setT4OutOfTarget(boolean t4OutOfTarget) {
        this.t4OutOfTarget = t4OutOfTarget;
    }

    /**
     * Get the value of uacrOutOfTarget
     *
     * @return the value of uacrOutOfTarget
     */
    public boolean getUacrOutOfTarget() {
        return uacrOutOfTarget;
    }

    /**
     * Set the value of uacrOutOfTarget
     *
     * @param uacrOutOfTarget new value of uacrOutOfTarget
     */
    public void setUacrOutOfTarget(boolean uacrOutOfTarget) {
        this.uacrOutOfTarget = uacrOutOfTarget;
    }

    /**
     * Get the value of egfrOutOfTarget
     *
     * @return the value of egfrOutOfTarget
     */
    public boolean getEgfrOutOfTarget() {
        return egfrOutOfTarget;
    }

    /**
     * Set the value of egfrOutOfTarget
     *
     * @param egfrOutOfTarget new value of egfrOutOfTarget
     */
    public void setEgfrOutOfTarget(boolean egfrOutOfTarget) {
        this.egfrOutOfTarget = egfrOutOfTarget;
    }

    /**
     * Get the value of creatinineOutOfTarget
     *
     * @return the value of creatinineOutOfTarget
     */
    public boolean getCreatinineOutOfTarget() {
        return creatinineOutOfTarget;
    }

    /**
     * Set the value of creatinineOutOfTarget
     *
     * @param creatinineOutOfTarget new value of creatinineOutOfTarget
     */
    public void setCreatinineOutOfTarget(boolean creatinineOutOfTarget) {
        this.creatinineOutOfTarget = creatinineOutOfTarget;
    }

    /**
     * Get the value of bmiOutOfTarget
     *
     * @return the value of bmiOutOfTarget
     */
    public boolean getBmiOutOfTarget() {
        return bmiOutOfTarget;
    }

    /**
     * Set the value of bmiOutOfTarget
     *
     * @param bmiOutOfTarget new value of bmiOutOfTarget
     */
    public void setBmiOutOfTarget(boolean bmiOutOfTarget) {
        this.bmiOutOfTarget = bmiOutOfTarget;
    }

    /**
     * Get the value of waistMaleOutOfTarget
     *
     * @return the value of waistMaleOutOfTarget
     */
    public boolean getWaistMaleOutOfTarget() {
        return waistMaleOutOfTarget;
    }

    /**
     * Set the value of waistMaleOutOfTarget
     *
     * @param waistMaleOutOfTarget new value of waistmaleOutOfTarget
     */
    public void setWaistMaleOutOfTarget(boolean waistMaleOutOfTarget) {
        this.waistMaleOutOfTarget = waistMaleOutOfTarget;
    }

    /**
     * Get the value of waistFemaleOutOfTarget
     *
     * @return the value of waistFemaleOutOfTarget
     */
    public boolean getWaistFemaleOutOfTarget() {
        return waistFemaleOutOfTarget;
    }

    /**
     * Set the value of waistFemaleOutOfTarget
     *
     * @param waistFemaleOutOfTarget new value of waistFemaleOutOfTarget
     */
    public void setWaistFemaleOutOfTarget(boolean waistFemaleOutOfTarget) {
        this.waistFemaleOutOfTarget = waistFemaleOutOfTarget;
    }

    /**
     * Get the value of systolicBloodPressureOutOfTarget
     *
     * @return the value of systolicBloodPressureOutOfTarget
     */
    public boolean getSystolicBloodPressureOutOfTarget() {
        return systolicBloodPressureOutOfTarget;
    }

    /**
     * Set the value of systolicBloodPressureOutOfTarget
     *
     * @param systolicBloodPressureOutOfTarget new value of
     * systolicBloodPressureOutOfTarget
     */
    public void setSystolicBloodPressureOutOfTarget(boolean systolicBloodPressureOutOfTarget) {
        this.systolicBloodPressureOutOfTarget = systolicBloodPressureOutOfTarget;
    }

    /**
     * Get the value of diastolicBloodPressureOutOfTarget
     *
     * @return the value of diastolicBloodPressureOutOfTarget
     */
    public boolean getDiastolicBloodPressureOutOfTarget() {
        return diastolicBloodPressureOutOfTarget;
    }

    /**
     * Set the value of diastolicBloodPressureOutOfTarget
     *
     * @param diastolicBloodPressureOutOfTarget new value of
     * diastolicBloodPressureOutOfTarget
     */
    public void setDiastolicBloodPressureOutOfTarget(boolean diastolicBloodPressureOutOfTarget) {
        this.diastolicBloodPressureOutOfTarget = diastolicBloodPressureOutOfTarget;
    }

    /**
     * Get the value of classAttendanceOutOfTarget
     *
     * @return the value of classAttendanceOutOfTarget
     */
    public boolean getClassAttendanceOutOfTarget() {
        return classAttendanceOutOfTarget;
    }

    /**
     * Set the value of classAttendanceOutOfTarget
     *
     * @param classAttendanceOutOfTarget new value of classAttendanceOutOfTaget
     */
    public void setClassAttendanceOutOfTarget(boolean classAttendanceOutOfTarget) {
        this.classAttendanceOutOfTarget = classAttendanceOutOfTarget;
    }

    /**
     * Get the value of eyeScreeningOutOfTarget
     *
     * @return the value of eyeScreeningOutOfTarget
     */
    public boolean getEyeScreeningOutOfTarget() {
        return eyeScreeningOutOfTarget;
    }

    /**
     * Set the value of eyeScreeningOutOfTarget
     *
     * @param eyeScreeningOutOfTarget new value of eyeScreeningOutOfTarget
     */
    public void setEyeScreeningOutOfTarget(boolean eyeScreeningOutOfTarget) {
        this.eyeScreeningOutOfTarget = eyeScreeningOutOfTarget;
    }

    /**
     * Get the value of footScreeningOutOfTarget
     *
     * @return the value of footScreeningOutOfTarget
     */
    public boolean getFootScreeningOutOfTarget() {
        return footScreeningOutOfTarget;
    }

    /**
     * Set the value of footScreeningOutOfTarget
     *
     * @param footScreeningOutOfTarget new value of footScreeningOutOfTarget
     */
    public void setFootScreeningOutOfTarget(boolean footScreeningOutOfTarget) {
        this.footScreeningOutOfTarget = footScreeningOutOfTarget;
    }

    /**
     * Get the value of psychologicalScreeningOutOfTarget
     *
     * @return the value of psychologicalScreeningOutOfTarget
     */
    public boolean getPsychologicalScreeningOutOfTarget() {
        return psychologicalScreeningOutOfTarget;
    }

    /**
     * Set the value of psychologicalScreeningOutOfTarget
     *
     * @param psychologicalScreeningOutOfTarget new value of
     * psychologicalScreeningOutOfTarget
     */
    public void setPsychologicalScreeningOutOfTarget(boolean psychologicalScreeningOutOfTarget) {
        this.psychologicalScreeningOutOfTarget = psychologicalScreeningOutOfTarget;
    }

    /**
     * Get the value of physicalActivityOutOfTarget
     *
     * @return the value of physicalActivityOutOfTarget
     */
    public boolean getPhysicalActivityOutOfTarget() {
        return physicalActivityOutOfTarget;
    }

    /**
     * Set the value of physicalActivityOutOfTarget
     *
     * @param physicalActivityOutOfTarget new value of
     * physicalActivityOutOfTarget
     */
    public void setPhysicalActivityOutOfTarget(boolean physicalActivityOutOfTarget) {
        this.physicalActivityOutOfTarget = physicalActivityOutOfTarget;
    }

    /**
     * Get the value of influenzaVaccineOutOfTarget
     *
     * @return the value of influenzaVaccineOutOfTarget
     */
    public boolean getInfluenzaVaccineOutOfTarget() {
        return influenzaVaccineOutOfTarget;
    }

    /**
     * Set the value of influenzaVaccineOutOfTarget
     *
     * @param influenzaVaccineOutOfTarget new value of
     * influenzaVaccineOutOfTarget
     */
    public void setInfluenzaVaccineOutOfTarget(boolean influenzaVaccineOutOfTarget) {
        this.influenzaVaccineOutOfTarget = influenzaVaccineOutOfTarget;
    }

    /**
     * Get the value of pcv13VaccineOutOfTarget
     *
     * @return the value of pcv13VaccineOutOfTarget
     */
    public boolean getPcv13VaccineOutOfTarget() {
        return pcv13VaccineOutOfTarget;
    }

    /**
     * Set the value of pcv13VaccineOutOfTarget
     *
     * @param pcv13VaccineOutOfTarget new value of pcv13VaccineOutOfTarget
     */
    public void setPcv13VaccineOutOfTarget(boolean pcv13VaccineOutOfTarget) {
        this.pcv13VaccineOutOfTarget = pcv13VaccineOutOfTarget;
    }

    /**
     * Get the value of ppsv23VaccineOutOfTarget
     *
     * @return the value of ppsv23VaccineOutOfTarget
     */
    public boolean getPpsv23VaccineOutOfTarget() {
        return ppsv23VaccineOutOfTarget;
    }

    /**
     * Set the value of ppsv23VaccineOutOfTarget
     *
     * @param ppsv23VaccineOutOfTarget new value of ppsv23VaccineOutOfTarget
     */
    public void setPpsv23VaccineOutOfTarget(boolean ppsv23VaccineOutOfTarget) {
        this.ppsv23VaccineOutOfTarget = ppsv23VaccineOutOfTarget;
    }

    /**
     * Get the value of hepatitisBVaccineOutOfTarget
     *
     * @return the value of hepatitisBVaccineOutOfTarget
     */
    public boolean getHepatitisBVaccineOutOfTarget() {
        return hepatitisBVaccineOutOfTarget;
    }

    /**
     * Set the value of hepatitisBVaccineOutOfTarget
     *
     * @param hepatitisBVaccineOutOfTarget new value of
     * hepatitisBVaccineOutOfTarget
     */
    public void setHepatitisBVaccineOutOfTarget(boolean hepatitisBVaccineOutOfTarget) {
        this.hepatitisBVaccineOutOfTarget = hepatitisBVaccineOutOfTarget;
    }

    /**
     * Get the value of tdapVaccineOutOfTarget
     *
     * @return the value of tdapVaccineOutOfTarget
     */
    public boolean getTdapVaccineOutOfTarget() {
        return tdapVaccineOutOfTarget;
    }

    /**
     * Set the value of tdapVaccineOutOfTarget
     *
     * @param tdapVaccineOutOfTarget new value of tdapVaccineOutOfTarget
     */
    public void setTdapVaccineOutOfTarget(boolean tdapVaccineOutOfTarget) {
        this.tdapVaccineOutOfTarget = tdapVaccineOutOfTarget;
    }

    /**
     * Get the value of zosterVaccineOutOfTarget
     *
     * @return the value of zosterVaccineOutOfTarget
     */
    public boolean getZosterVaccineOutOfTarget() {
        return zosterVaccineOutOfTarget;
    }

    /**
     * Set the value of zosterVaccineOutOfTarget
     *
     * @param zosterVaccineOutOfTarget new value of zosterVaccineOutOfTarget
     */
    public void setZosterVaccineOutOfTarget(boolean zosterVaccineOutOfTarget) {
        this.zosterVaccineOutOfTarget = zosterVaccineOutOfTarget;
    }

    /**
     * Get the value of smokingStatusOutOfTarget
     *
     * @return the value of smokingStatusOutOfTarget
     */
    public boolean getSmokingStatusOutOfTarget() {
        return smokingStatusOutOfTarget;
    }

    /**
     * Set the value of smokingStatusOutOfTarget
     *
     * @param smokingStatusOutOfTarget new value of smokingStatusOutOfTarget
     */
    public void setSmokingStatusOutOfTarget(boolean smokingStatusOutOfTarget) {
        this.smokingStatusOutOfTarget = smokingStatusOutOfTarget;
    }

    /**
     * Get the value of telephoneFollowUpOutOfTarget
     *
     * @return the value of telephoneFollowUpOutOfTarget
     */
    public boolean getTelephoneFollowUpOutOfTarget() {
        return telephoneFollowUpOutOfTarget;
    }

    /**
     * Set the value of telephoneFollowUpOutOfTarget
     *
     * @param telephoneFollowUpOutOfTarget new value of
     * telephoneFollowUpOutOfTarget
     */
    public void setTelephoneFollowUpOutOfTarget(boolean telephoneFollowUpOutOfTarget) {
        this.telephoneFollowUpOutOfTarget = telephoneFollowUpOutOfTarget;
    }

    /**
     * Get the value of astOutOfTarget
     *
     * @return the value of astOutOfTarget
     */
    public boolean getAstOutOfTarget() {
        return astOutOfTarget;
    }

    /**
     * Set the value of astOutOfTarget
     *
     * @param astOutOfTarget new value of astOutOfTarget
     */
    public void setAstOutOfTarget(boolean astOutOfTarget) {
        this.astOutOfTarget = astOutOfTarget;
    }

    /**
     * Get the value of altOutOfTarget
     *
     * @return the value of altOutOfTarget
     */
    public boolean getAltOutOfTarget() {
        return altOutOfTarget;
    }

    /**
     * Set the value of altOutOfTarget
     *
     * @param altOutOfTarget new value of altOutOfTarget
     */
    public void setAltOutOfTarget(boolean altOutOfTarget) {
        this.altOutOfTarget = altOutOfTarget;
    }

    /**
     * Get the value of psaOutOfTarget
     *
     * @return the value of psaOutOfTarget
     */
    public boolean getPsaOutOfTarget() {
        return psaOutOfTarget;
    }

    /**
     * Set the value of psaOutOfTarget
     *
     * @param psaOutOfTarget new value of psaOutOfTarget
     */
    public void setPsaOutOfTarget(boolean psaOutOfTarget) {
        this.psaOutOfTarget = psaOutOfTarget;
    }

    /**
     * Get the value of hospitalizationOutOfTarget
     *
     * @return the value of hospitalizationOutOfTarget
     */
    public boolean getHospitalizationOutOfTarget() {
        return hospitalizationOutOfTarget;
    }

    /**
     * Set the value of hospitalizationOutOfTarget
     *
     * @param hospitalizationOutOfTarget new value of hospitalizationOutOfTarget
     */
    public void setHospitalizationOutOfTarget(boolean hospitalizationOutOfTarget) {
        this.hospitalizationOutOfTarget = hospitalizationOutOfTarget;
    }

}
