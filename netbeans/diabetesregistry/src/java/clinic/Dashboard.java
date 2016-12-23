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

import java.sql.Date;
import java.util.ArrayList;

/**
 * This class holds the most recent information gathered for a patient.
 *
 * @author Bryan Daniel
 * @version 1, April 8, 2016
 */
public class Dashboard {

    /**
     * The A1C measurement
     */
    private A1cResult a1c;

    /**
     * The glucose measurement
     */
    private ContinuousResult glucose;

    /**
     * The LDL measurement
     */
    private LdlResult ldl;

    /**
     * The HDL measurement
     */
    private ContinuousResult hdl;

    /**
     * The triglycerides measurement
     */
    private ContinuousResult triglycerides;

    /**
     * The TSH measurement
     */
    private TshResult tsh;

    /**
     * The T4 measurement
     */
    private ContinuousResult t4;

    /**
     * The UACR measurement
     */
    private ContinuousResult uacr;

    /**
     * The eGFR measurement
     */
    private ContinuousResult egfr;

    /**
     * The creatinine measurement
     */
    private ContinuousResult creatinine;

    /**
     * The BMI measurement
     */
    private ContinuousResult bmi;

    /**
     * The waist measurement
     */
    private ContinuousResult waist;

    /**
     * The blood pressure measurement
     */
    private BloodPressureResult bloodPressure;

    /**
     * The date of the last class attended by the patient
     */
    private Date lastClass;

    /**
     * The eye screening result
     */
    private CategoricalResult eyeScreening;

    /**
     * The foot screening result
     */
    private CategoricalResult footScreening;

    /**
     * The psychological screening result
     */
    private PsychologicalScreeningResult psychologicalScreening;

    /**
     * The patient's minutes of physical activity for the week
     */
    private DiscreteResult physicalActivity;

    /**
     * The date of the influenza vaccine
     */
    private Date influenzaVaccine;

    /**
     * The date of the PCV13 vaccine
     */
    private Date pcv13Vaccine;

    /**
     * The date of the PPSV23 vaccine
     */
    private Date ppsv23Vaccine;

    /**
     * The date of the hepatitis vaccine
     */
    private Date hepatitisBVaccine;

    /**
     * The date of the TDAP vaccine
     */
    private Date tdapVaccine;

    /**
     * The date of the zoster vaccine
     */
    private Date zosterVaccine;

    /**
     * Indicates whether the patient is a smoker
     */
    private BooleanResult smokingStatus;

    /**
     * The telephone follow-up code
     */
    private CategoricalResult telephoneFollowUp;

    /**
     * The AST measurement
     */
    private ContinuousResult ast;

    /**
     * The ALT measurement
     */
    private ContinuousResult alt;

    /**
     * The PSA measurement
     */
    private ContinuousResult psa;

    /**
     * The date of hospitalization
     */
    private Date er;

    /**
     * The healthy-target status of the patient
     */
    private HealthyTargetStatus hts;

    /**
     * The therapy of the patient
     */
    private Therapy therapy;

    /**
     * The list of the patient's medications
     */
    private ArrayList<Medication> medications;

    /**
     * Default constructor
     */
    public Dashboard() {
        a1c = null;
        glucose = null;
        ldl = null;
        hdl = null;
        triglycerides = null;
        tsh = null;
        t4 = null;
        uacr = null;
        egfr = null;
        creatinine = null;
        bmi = null;
        waist = null;
        bloodPressure = null;
        lastClass = null;
        eyeScreening = null;
        footScreening = null;
        psychologicalScreening = null;
        physicalActivity = null;
        influenzaVaccine = null;
        pcv13Vaccine = null;
        ppsv23Vaccine = null;
        hepatitisBVaccine = null;
        tdapVaccine = null;
        zosterVaccine = null;
        smokingStatus = null;
        telephoneFollowUp = null;
        ast = null;
        alt = null;
        psa = null;
        er = null;
        hts = null;
        therapy = null;
        medications = null;
    }

    /**
     * Parameterized constructor
     *
     * @param a1c the latest A1C value
     * @param glucose the latest glucose value
     * @param ldl the latest LDL value
     * @param hdl the latest HDL value
     * @param triglycerides the latest triglycerides value
     * @param tsh the latest TSH value
     * @param t4 the latest T4 value
     * @param uacr the latest UACR value
     * @param egfr the latest eGFR value
     * @param creatinine the latest creatinine value
     * @param bmi the latest BMI value
     * @param waist the latest waist value
     * @param bloodPressure the latest blood pressure value
     * @param lastClass the latest class attended
     * @param eyeScreening the latest eye screening value
     * @param footScreening the latest foot screening value
     * @param psychologicalScreening the latest psychological screening value
     * @param physicalActivity the latest weekly minutes of physical activity
     * @param influenzaVaccine the latest influenza vaccine
     * @param pcv13Vaccine the latest PCV-13 vaccine
     * @param ppsv23Vaccine the latest PPSV-23 vaccine
     * @param hepatitisBVaccine the latest hepatitis B vaccine
     * @param tdapVaccine the latest TDAP vaccine
     * @param zosterVaccine the latest zoster vaccine
     * @param smokingStatus the latest smoking status
     * @param telephoneFollowUp the latest telephone follow-up code
     * @param ast the latest AST value
     * @param alt the latest ALT value
     * @param psa the PSA value
     * @param er the latest ER date
     * @param hts the latest healthy target status
     * @param therapy the latest therapy
     * @param medications the latest medications
     */
    public Dashboard(A1cResult a1c, ContinuousResult glucose,
            LdlResult ldl, ContinuousResult hdl, ContinuousResult triglycerides,
            TshResult tsh, ContinuousResult t4, ContinuousResult uacr,
            ContinuousResult egfr, ContinuousResult creatinine, ContinuousResult bmi,
            ContinuousResult waist, BloodPressureResult bloodPressure, Date lastClass,
            CategoricalResult eyeScreening, CategoricalResult footScreening,
            PsychologicalScreeningResult psychologicalScreening,
            DiscreteResult physicalActivity, Date influenzaVaccine, Date pcv13Vaccine,
            Date ppsv23Vaccine, Date hepatitisBVaccine, Date tdapVaccine,
            Date zosterVaccine, BooleanResult smokingStatus,
            CategoricalResult telephoneFollowUp, ContinuousResult ast,
            ContinuousResult alt, ContinuousResult psa, Date er,
            HealthyTargetStatus hts, Therapy therapy, ArrayList<Medication> medications) {
        this.a1c = a1c;
        this.glucose = glucose;
        this.ldl = ldl;
        this.hdl = hdl;
        this.triglycerides = triglycerides;
        this.tsh = tsh;
        this.t4 = t4;
        this.uacr = uacr;
        this.egfr = egfr;
        this.creatinine = creatinine;
        this.bmi = bmi;
        this.waist = waist;
        this.bloodPressure = bloodPressure;
        this.lastClass = lastClass;
        this.eyeScreening = eyeScreening;
        this.footScreening = footScreening;
        this.psychologicalScreening = psychologicalScreening;
        this.physicalActivity = physicalActivity;
        this.influenzaVaccine = influenzaVaccine;
        this.pcv13Vaccine = pcv13Vaccine;
        this.ppsv23Vaccine = ppsv23Vaccine;
        this.hepatitisBVaccine = hepatitisBVaccine;
        this.tdapVaccine = tdapVaccine;
        this.zosterVaccine = zosterVaccine;
        this.smokingStatus = smokingStatus;
        this.telephoneFollowUp = telephoneFollowUp;
        this.ast = ast;
        this.alt = alt;
        this.psa = psa;
        this.er = er;
        this.hts = hts;
        this.therapy = therapy;
        this.medications = medications;
    }

    /**
     * Get the value of a1c
     *
     * @return the value of a1c
     */
    public A1cResult getA1c() {
        return a1c;
    }

    /**
     * Set the value of a1c
     *
     * @param a1c new value of a1c
     */
    public void setA1c(A1cResult a1c) {
        this.a1c = a1c;
    }

    /**
     * Get the value of glucose
     *
     * @return the value of glucose
     */
    public ContinuousResult getGlucose() {
        return glucose;
    }

    /**
     * Set the value of glucose
     *
     * @param glucose new value of glucose
     */
    public void setGlucose(ContinuousResult glucose) {
        this.glucose = glucose;
    }

    /**
     * Get the value of ldl
     *
     * @return the value of ldl
     */
    public LdlResult getLdl() {
        return ldl;
    }

    /**
     * Set the value of ldl
     *
     * @param ldl new value of ldl
     */
    public void setLdl(LdlResult ldl) {
        this.ldl = ldl;
    }

    /**
     * Get the value of hdl
     *
     * @return the value of hdl
     */
    public ContinuousResult getHdl() {
        return hdl;
    }

    /**
     * Set the value of hdl
     *
     * @param hdl new value of hdl
     */
    public void setHdl(ContinuousResult hdl) {
        this.hdl = hdl;
    }

    /**
     * Get the value of triglycerides
     *
     * @return the value of triglycerides
     */
    public ContinuousResult getTriglycerides() {
        return triglycerides;
    }

    /**
     * Set the value of triglycerides
     *
     * @param triglycerides new value of triglycerides
     */
    public void setTriglycerides(ContinuousResult triglycerides) {
        this.triglycerides = triglycerides;
    }

    /**
     * Get the value of tsh
     *
     * @return the value of tsh
     */
    public TshResult getTsh() {
        return tsh;
    }

    /**
     * Set the value of tsh
     *
     * @param tsh new value of tsh
     */
    public void setTsh(TshResult tsh) {
        this.tsh = tsh;
    }

    /**
     * Get the value of t4
     *
     * @return the value of t4
     */
    public ContinuousResult getT4() {
        return t4;
    }

    /**
     * Set the value of t4
     *
     * @param t4 new value of t4
     */
    public void setT4(ContinuousResult t4) {
        this.t4 = t4;
    }

    /**
     * Get the value of uacr
     *
     * @return the value of uacr
     */
    public ContinuousResult getUacr() {
        return uacr;
    }

    /**
     * Set the value of uacr
     *
     * @param uacr new value of uacr
     */
    public void setUacr(ContinuousResult uacr) {
        this.uacr = uacr;
    }

    /**
     * Get the value of egfr
     *
     * @return the value of egfr
     */
    public ContinuousResult getEgfr() {
        return egfr;
    }

    /**
     * Set the value of egfr
     *
     * @param egfr new value of egfr
     */
    public void setEgfr(ContinuousResult egfr) {
        this.egfr = egfr;
    }

    /**
     * Get the value of creatinine
     *
     * @return the value of creatinine
     */
    public ContinuousResult getCreatinine() {
        return creatinine;
    }

    /**
     * Set the value of creatinine
     *
     * @param creatinine new value of creatinine
     */
    public void setCreatinine(ContinuousResult creatinine) {
        this.creatinine = creatinine;
    }

    /**
     * Get the value of bmi
     *
     * @return the value of bmi
     */
    public ContinuousResult getBmi() {
        return bmi;
    }

    /**
     * Set the value of bmi
     *
     * @param bmi new value of bmi
     */
    public void setBmi(ContinuousResult bmi) {
        this.bmi = bmi;
    }

    /**
     * Get the value of waist
     *
     * @return the value of waist
     */
    public ContinuousResult getWaist() {
        return waist;
    }

    /**
     * Set the value of waist
     *
     * @param waist new value of waist
     */
    public void setWaist(ContinuousResult waist) {
        this.waist = waist;
    }

    /**
     * Get the value of bloodPressure
     *
     * @return the value of bloodPressure
     */
    public BloodPressureResult getBloodPressure() {
        return bloodPressure;
    }

    /**
     * Set the value of bloodPressure
     *
     * @param bloodPressure new value of bloodPressure
     */
    public void setBloodPressure(BloodPressureResult bloodPressure) {
        this.bloodPressure = bloodPressure;
    }

    /**
     * Get the value of lastClass
     *
     * @return the value of lastClass
     */
    public Date getLastClass() {
        return lastClass;
    }

    /**
     * Set the value of lastClass
     *
     * @param lastClass new value of lastClass
     */
    public void setLastClass(Date lastClass) {
        this.lastClass = lastClass;
    }

    /**
     * Get the value of eyeScreening
     *
     * @return the value of eyeScreening
     */
    public CategoricalResult getEyeScreening() {
        return eyeScreening;
    }

    /**
     * Set the value of eyeScreening
     *
     * @param eyeScreening new value of eyeScreening
     */
    public void setEyeScreening(CategoricalResult eyeScreening) {
        this.eyeScreening = eyeScreening;
    }

    /**
     * Get the value of footScreening
     *
     * @return the value of footScreening
     */
    public CategoricalResult getFootScreening() {
        return footScreening;
    }

    /**
     * Set the value of footScreening
     *
     * @param footScreening new value of footScreening
     */
    public void setFootScreening(CategoricalResult footScreening) {
        this.footScreening = footScreening;
    }

    /**
     * Get the value of psychologicalScreening
     *
     * @return the value of psychologicalScreening
     */
    public PsychologicalScreeningResult getPsychologicalScreening() {
        return psychologicalScreening;
    }

    /**
     * Set the value of psychologicalScreening
     *
     * @param psychologicalScreening new value of psychologicalScreening
     */
    public void setPsychologicalScreening(PsychologicalScreeningResult psychologicalScreening) {
        this.psychologicalScreening = psychologicalScreening;
    }

    /**
     * Get the value of physicalActivity
     *
     * @return the value of physicalActivity
     */
    public DiscreteResult getPhysicalActivity() {
        return physicalActivity;
    }

    /**
     * Set the value of physicalActivity
     *
     * @param physicalActivity new value of physicalActivity
     */
    public void setPhysicalActivity(DiscreteResult physicalActivity) {
        this.physicalActivity = physicalActivity;
    }

    /**
     * Get the value of influenzaVaccine
     *
     * @return the value of influenzaVaccine
     */
    public Date getInfluenzaVaccine() {
        return influenzaVaccine;
    }

    /**
     * Set the value of influenzaVaccine
     *
     * @param influenzaVaccine new value of influenzaVaccine
     */
    public void setInfluenzaVaccine(Date influenzaVaccine) {
        this.influenzaVaccine = influenzaVaccine;
    }

    /**
     * Get the value of pcv13Vaccine
     *
     * @return the value of pcv13Vaccine
     */
    public Date getPcv13Vaccine() {
        return pcv13Vaccine;
    }

    /**
     * Set the value of pcv13Vaccine
     *
     * @param pcv13Vaccine new value of pcv13Vaccine
     */
    public void setPcv13Vaccine(Date pcv13Vaccine) {
        this.pcv13Vaccine = pcv13Vaccine;
    }

    /**
     * Get the value of ppsv23Vaccine
     *
     * @return the value of ppsv23Vaccine
     */
    public Date getPpsv23Vaccine() {
        return ppsv23Vaccine;
    }

    /**
     * Set the value of ppsv23Vaccine
     *
     * @param ppsv23Vaccine new value of ppsv23Vaccine
     */
    public void setPpsv23Vaccine(Date ppsv23Vaccine) {
        this.ppsv23Vaccine = ppsv23Vaccine;
    }

    /**
     * Get the value of hepatitisBVaccine
     *
     * @return the value of hepatitisBVaccine
     */
    public Date getHepatitisBVaccine() {
        return hepatitisBVaccine;
    }

    /**
     * Set the value of hepatitisBVaccine
     *
     * @param hepatitisBVaccine new value of hepatitisBVaccine
     */
    public void setHepatitisBVaccine(Date hepatitisBVaccine) {
        this.hepatitisBVaccine = hepatitisBVaccine;
    }

    /**
     * Get the value of tdapVaccine
     *
     * @return the value of tdapVaccine
     */
    public Date getTdapVaccine() {
        return tdapVaccine;
    }

    /**
     * Set the value of tdapVaccine
     *
     * @param tdapVaccine new value of tdapVaccine
     */
    public void setTdapVaccine(Date tdapVaccine) {
        this.tdapVaccine = tdapVaccine;
    }

    /**
     * Get the value of zosterVaccine
     *
     * @return the value of zosterVaccine
     */
    public Date getZosterVaccine() {
        return zosterVaccine;
    }

    /**
     * Set the value of zosterVaccine
     *
     * @param zosterVaccine new value of zosterVaccine
     */
    public void setZosterVaccine(Date zosterVaccine) {
        this.zosterVaccine = zosterVaccine;
    }

    /**
     * Get the value of smokingStatus
     *
     * @return the value of smokingStatus
     */
    public BooleanResult getSmokingStatus() {
        return smokingStatus;
    }

    /**
     * Set the value of smokingStatus
     *
     * @param smokingStatus new value of smokingStatus
     */
    public void setSmokingStatus(BooleanResult smokingStatus) {
        this.smokingStatus = smokingStatus;
    }

    /**
     * Get the value of telephoneFollowUp
     *
     * @return the value of telephoneFollowUp
     */
    public CategoricalResult getTelephoneFollowUp() {
        return telephoneFollowUp;
    }

    /**
     * Set the value of telephoneFollowUp
     *
     * @param telephoneFollowUp new value of telephoneFollowUp
     */
    public void setTelephoneFollowUp(CategoricalResult telephoneFollowUp) {
        this.telephoneFollowUp = telephoneFollowUp;
    }

    /**
     * Get the value of ast
     *
     * @return the value of ast
     */
    public ContinuousResult getAst() {
        return ast;
    }

    /**
     * Set the value of ast
     *
     * @param ast new value of ast
     */
    public void setAst(ContinuousResult ast) {
        this.ast = ast;
    }

    /**
     * Get the value of alt
     *
     * @return the value of alt
     */
    public ContinuousResult getAlt() {
        return alt;
    }

    /**
     * Set the value of alt
     *
     * @param alt new value of alt
     */
    public void setAlt(ContinuousResult alt) {
        this.alt = alt;
    }

    /**
     * Get the value of psa
     *
     * @return the value of psa
     */
    public ContinuousResult getPsa() {
        return psa;
    }

    /**
     * Set the value of psa
     *
     * @param psa the new value of psa
     */
    public void setPsa(ContinuousResult psa) {
        this.psa = psa;
    }

    /**
     * Get the value of er
     *
     * @return the value of er
     */
    public Date getEr() {
        return er;
    }

    /**
     * Set the value of er
     *
     * @param er new value of er
     */
    public void setEr(Date er) {
        this.er = er;
    }

    /**
     * Get the value of hts
     *
     * @return the value of hts
     */
    public HealthyTargetStatus getHts() {
        return hts;
    }

    /**
     * Set the value of hts
     *
     * @param hts new value of hts
     */
    public void setHts(HealthyTargetStatus hts) {
        this.hts = hts;
    }

    /**
     * Get the value of therapy
     *
     * @return the value of therapy
     */
    public Therapy getTherapy() {
        return therapy;
    }

    /**
     * Set the value of therapy
     *
     * @param therapy new value of therapy
     */
    public void setTherapy(Therapy therapy) {
        this.therapy = therapy;
    }

    /**
     * Get the value of medications
     *
     * @return the value of medications
     */
    public ArrayList<Medication> getMedications() {
        return medications;
    }

    /**
     * Set the value of medication
     *
     * @param medications new value of medication
     */
    public void setMedication(ArrayList<Medication> medications) {
        this.medications = medications;
    }

}
