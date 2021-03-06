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
import java.sql.Date;

/**
 * This class contains all the variables necessary for patient data entry.
 *
 * @author Bryan Daniel
 * @version 2, March 16, 2017
 */
public class DataEntryContainer implements Serializable {
    
    /**
     * Serial version UID
     */
    private static final long serialVersionUID = 8166176472666621340L;

    /**
     * The patient ID
     */
    private Integer patientId;

    /**
     * The A1C measurement
     */
    private BigDecimal a1c;

    /**
     * The fasting glucose measurement
     */
    private BigDecimal glucoseAc;

    /**
     * The postprandial glucose measurement
     */
    private BigDecimal glucosePc;

    /**
     * The LDL measurement
     */
    private BigDecimal ldl;

    /**
     * The LDL measurement after myocardial infarction
     */
    private BigDecimal ldlPostMi;

    /**
     * Indication of statin treatment
     */
    private Boolean onStatin;

    /**
     * The HDL measurement
     */
    private BigDecimal hdl;

    /**
     * The triglycerides measurement
     */
    private BigDecimal triglycerides;

    /**
     * The TSH measurement
     */
    private BigDecimal tsh;

    /**
     * Indication of thyroid treatment
     */
    private Boolean onThyroidTreatment;

    /**
     * The T4 measurement
     */
    private BigDecimal t4;

    /**
     * The UACR measurement
     */
    private BigDecimal uacr;

    /**
     * The eGFR measurement
     */
    private BigDecimal egfr;

    /**
     * The creatinine measurement
     */
    private BigDecimal creatinine;

    /**
     * The date of the influenza vaccine
     */
    private Date influenzaVaccineDate;

    /**
     * The date of the PCV13 vaccine
     */
    private Date pcv13Date;

    /**
     * The date of the PPSV23 vaccine
     */
    private Date ppsv23Date;

    /**
     * The date of the hepatitis vaccine
     */
    private Date hepatitisBDate;

    /**
     * The date of the TDAP vaccine
     */
    private Date tdapDate;

    /**
     * The date of the zoster vaccine
     */
    private Date zosterDate;

    /**
     * The telephone follow-up code
     */
    private String telephoneFollowUp;

    /**
     * The AST measurement
     */
    private BigDecimal ast;

    /**
     * The ALT measurement
     */
    private BigDecimal alt;

    /**
     * The PSA measurement
     */
    private BigDecimal psa;

    /**
     * The note topic
     */
    private String noteTopic;

    /**
     * The note content
     */
    private String note;

    /**
     * The date of data entry
     */
    private Date dateEntered;

    /**
     * Indicates whether the measurement was taken at the point of care
     */
    private Boolean poc;

    /**
     * The username
     */
    private String userName;

    /**
     * The clinic ID
     */
    private Integer clinicId;

    /**
     * Default constructor
     */
    public DataEntryContainer() {
        patientId = null;
        a1c = null;
        glucoseAc = null;
        glucosePc = null;
        ldl = null;
        ldlPostMi = null;
        onStatin = null;
        hdl = null;
        triglycerides = null;
        tsh = null;
        onThyroidTreatment = null;
        t4 = null;
        uacr = null;
        egfr = null;
        creatinine = null;
        influenzaVaccineDate = null;
        pcv13Date = null;
        ppsv23Date = null;
        hepatitisBDate = null;
        tdapDate = null;
        zosterDate = null;
        telephoneFollowUp = null;
        ast = null;
        alt = null;
        psa = null;
        noteTopic = null;
        note = null;
        dateEntered = null;
        poc = null;
        userName = null;
        clinicId = null;
    }

    /**
     * Parameterized constructor
     *
     * @param patientId patient ID
     * @param a1c the A1C measurement
     * @param glucoseAc the glucose AC measurement
     * @param glucosePc the glucose PC measurement
     * @param ldl the LDL measurement
     * @param ldlPostMi the LDL post MI measurement
     * @param onStatin the LDL on statin indicator
     * @param hdl the HDL measurement
     * @param triglycerides the triglycerides measurement
     * @param tsh the TSH measurement
     * @param onThyroidTreatment the TSH on thyroid treatment indicator
     * @param t4 the T4 measurement
     * @param uacr the UACR measurement
     * @param egfr the eGFR measurement
     * @param creatinine the creatinine measurement
     * @param influenzaVaccineDate the date of flu vaccine
     * @param pcv13Date the date of PCV-13 vaccine
     * @param ppsv23Date the date of PPSV-23 vaccine
     * @param hepatitisBDate the date of Hepatitis B vaccine
     * @param tdapDate the date of TDAP vaccine
     * @param zosterDate the date of zoster vaccine
     * @param telephoneFollowUp telephone follow up indicator
     * @param ast the AST measurement
     * @param alt the ALT measurement
     * @param psa the PSA measurement
     * @param noteTopic the note topic
     * @param note the note content
     * @param dateEntered the date entered
     * @param poc the point of care indicator
     * @param userName the user name
     * @param clinicId the clinic ID
     */
    public DataEntryContainer(Integer patientId, BigDecimal a1c,
            BigDecimal glucoseAc, BigDecimal glucosePc, BigDecimal ldl,
            BigDecimal ldlPostMi, Boolean onStatin, BigDecimal hdl,
            BigDecimal triglycerides, BigDecimal tsh, Boolean onThyroidTreatment,
            BigDecimal t4, BigDecimal uacr, BigDecimal egfr,
            BigDecimal creatinine, Date influenzaVaccineDate, Date pcv13Date, 
            Date ppsv23Date, Date hepatitisBDate, Date tdapDate, Date zosterDate, 
            String telephoneFollowUp, BigDecimal ast, BigDecimal alt,
            BigDecimal psa, String noteTopic, String note, Date dateEntered, 
            Boolean poc, String userName, Integer clinicId) {
        this.patientId = patientId;
        this.a1c = a1c;
        this.glucoseAc = glucoseAc;
        this.glucosePc = glucosePc;
        this.ldl = ldl;
        this.ldlPostMi = ldlPostMi;
        this.onStatin = onStatin;
        this.hdl = hdl;
        this.triglycerides = triglycerides;
        this.tsh = tsh;
        this.onThyroidTreatment = onThyroidTreatment;
        this.t4 = t4;
        this.uacr = uacr;
        this.egfr = egfr;
        this.creatinine = creatinine;
        this.influenzaVaccineDate = influenzaVaccineDate;
        this.pcv13Date = pcv13Date;
        this.ppsv23Date = ppsv23Date;
        this.hepatitisBDate = hepatitisBDate;
        this.tdapDate = tdapDate;
        this.zosterDate = zosterDate;
        this.telephoneFollowUp = telephoneFollowUp;
        this.ast = ast;
        this.alt = alt;
        this.psa = psa;
        this.noteTopic = noteTopic;
        this.note = note;
        this.dateEntered = dateEntered;
        this.poc = poc;
        this.userName = userName;
        this.clinicId = clinicId;
    }

    /**
     * Get the value of patientId
     *
     * @return the value of patientId
     */
    public Integer getPatientId() {
        return patientId;
    }

    /**
     * Set the value of patientId
     *
     * @param patientId new value of patientId
     */
    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    /**
     * Get the value of a1c
     *
     * @return the value of a1c
     */
    public BigDecimal getA1c() {
        return a1c;
    }

    /**
     * Set the value of a1c
     *
     * @param a1c new value of a1c
     */
    public void setA1c(BigDecimal a1c) {
        this.a1c = a1c;
    }

    /**
     * Get the value of glucoseAc
     *
     * @return the value of glucoseAc
     */
    public BigDecimal getGlucoseAc() {
        return glucoseAc;
    }

    /**
     * Set the value of glucoseAc
     *
     * @param glucoseAc new value of glucoseAc
     */
    public void setGlucoseAc(BigDecimal glucoseAc) {
        this.glucoseAc = glucoseAc;
    }

    /**
     * Get the value of glucosePc
     *
     * @return the value of glucosePc
     */
    public BigDecimal getGlucosePc() {
        return glucosePc;
    }

    /**
     * Set the value of glucosePc
     *
     * @param glucosePc new value of glucosePc
     */
    public void setGlucosePc(BigDecimal glucosePc) {
        this.glucosePc = glucosePc;
    }

    /**
     * Get the value of ldl
     *
     * @return the value of ldl
     */
    public BigDecimal getLdl() {
        return ldl;
    }

    /**
     * Set the value of ldl
     *
     * @param ldl new value of ldl
     */
    public void setLdl(BigDecimal ldl) {
        this.ldl = ldl;
    }

    /**
     * Get the value of ldlPostMi
     *
     * @return the value of ldlPostMi
     */
    public BigDecimal getLdlPostMi() {
        return ldlPostMi;
    }

    /**
     * Set the value of ldlPostMi
     *
     * @param ldlPostMi new value of ldlPostMi
     */
    public void setLdlPostMi(BigDecimal ldlPostMi) {
        this.ldlPostMi = ldlPostMi;
    }

    /**
     * Get the value of onStatin
     *
     * @return the value of onStatin
     */
    public Boolean getOnStatin() {
        return onStatin;
    }

    /**
     * Set the value of onStatin
     *
     * @param onStatin new value of onStatin
     */
    public void setOnStatin(Boolean onStatin) {
        this.onStatin = onStatin;
    }

    /**
     * Get the value of hdl
     *
     * @return the value of hdl
     */
    public BigDecimal getHdl() {
        return hdl;
    }

    /**
     * Set the value of hdl
     *
     * @param hdl new value of hdl
     */
    public void setHdl(BigDecimal hdl) {
        this.hdl = hdl;
    }

    /**
     * Get the value of triglycerides
     *
     * @return the value of triglycerides
     */
    public BigDecimal getTriglycerides() {
        return triglycerides;
    }

    /**
     * Set the value of triglycerides
     *
     * @param triglycerides new value of triglycerides
     */
    public void setTriglycerides(BigDecimal triglycerides) {
        this.triglycerides = triglycerides;
    }

    /**
     * Get the value of tsh
     *
     * @return the value of tsh
     */
    public BigDecimal getTsh() {
        return tsh;
    }

    /**
     * Set the value of tsh
     *
     * @param tsh new value of tsh
     */
    public void setTsh(BigDecimal tsh) {
        this.tsh = tsh;
    }

    /**
     * Get the value of onThyroidTreatment
     *
     * @return the value of onThyroidTreatment
     */
    public Boolean getOnThyroidTreatment() {
        return onThyroidTreatment;
    }

    /**
     * Set the value of onThyroidTreatment
     *
     * @param onThyroidTreatment new value of onThyroidTreatment
     */
    public void setOnThyroidTreatment(Boolean onThyroidTreatment) {
        this.onThyroidTreatment = onThyroidTreatment;
    }

    /**
     * Get the value of t4
     *
     * @return the value of t4
     */
    public BigDecimal getT4() {
        return t4;
    }

    /**
     * Set the value of t4
     *
     * @param t4 new value of t4
     */
    public void setT4(BigDecimal t4) {
        this.t4 = t4;
    }

    /**
     * Get the value of uacr
     *
     * @return the value of uacr
     */
    public BigDecimal getUacr() {
        return uacr;
    }

    /**
     * Set the value of uacr
     *
     * @param uacr new value of uacr
     */
    public void setUacr(BigDecimal uacr) {
        this.uacr = uacr;
    }

    /**
     * Get the value of egfr
     *
     * @return the value of egfr
     */
    public BigDecimal getEgfr() {
        return egfr;
    }

    /**
     * Set the value of egfr
     *
     * @param egfr new value of egfr
     */
    public void setEgfr(BigDecimal egfr) {
        this.egfr = egfr;
    }

    /**
     * Get the value of creatinine
     *
     * @return the value of creatinine
     */
    public BigDecimal getCreatinine() {
        return creatinine;
    }

    /**
     * Set the value of creatinine
     *
     * @param creatinine new value of creatinine
     */
    public void setCreatinine(BigDecimal creatinine) {
        this.creatinine = creatinine;
    }

    /**
     * Get the value of influenzaVaccineDate
     *
     * @return the value of influenzaVaccineDate
     */
    public Date getInfluenzaVaccineDate() {
        return influenzaVaccineDate;
    }

    /**
     * Set the value of influenzaVaccineDate
     *
     * @param influenzaVaccineDate new value of influenzaVaccineDate
     */
    public void setInfluenzaVaccineDate(Date influenzaVaccineDate) {
        this.influenzaVaccineDate = influenzaVaccineDate;
    }

    /**
     * Get the value of pcv13Date
     *
     * @return the value of pcv13Date
     */
    public Date getPcv13Date() {
        return pcv13Date;
    }

    /**
     * Set the value of pcv13Date
     *
     * @param pcv13Date new value of pcv13Date
     */
    public void setPcv13Date(Date pcv13Date) {
        this.pcv13Date = pcv13Date;
    }

    /**
     * Get the value of ppsv23Date
     *
     * @return the value of ppsv23Date
     */
    public Date getPpsv23Date() {
        return ppsv23Date;
    }

    /**
     * Set the value of ppsv23Date
     *
     * @param ppsv23Date new value of ppsv23Date
     */
    public void setPpsv23Date(Date ppsv23Date) {
        this.ppsv23Date = ppsv23Date;
    }

    /**
     * Get the value of hepatitisBDate
     *
     * @return the value of hepatitisBDate
     */
    public Date getHepatitisBDate() {
        return hepatitisBDate;
    }

    /**
     * Set the value of hepatitisBDate
     *
     * @param hepatitisBDate new value of hepatitisBDate
     */
    public void setHepatitisBDate(Date hepatitisBDate) {
        this.hepatitisBDate = hepatitisBDate;
    }

    /**
     * Get the value of tdapDate
     *
     * @return the value of tdapDate
     */
    public Date getTdapDate() {
        return tdapDate;
    }

    /**
     * Set the value of tdapDate
     *
     * @param tdapDate new value of tdapDate
     */
    public void setTdapDate(Date tdapDate) {
        this.tdapDate = tdapDate;
    }

    /**
     * Get the value of zosterDate
     *
     * @return the value of zosterDate
     */
    public Date getZosterDate() {
        return zosterDate;
    }

    /**
     * Set the value of zosterDate
     *
     * @param zosterDate new value of zosterDate
     */
    public void setZosterDate(Date zosterDate) {
        this.zosterDate = zosterDate;
    }

    /**
     * Get the value of telephoneFollowUp
     *
     * @return the value of telephoneFollowUp
     */
    public String getTelephoneFollowUp() {
        return telephoneFollowUp;
    }

    /**
     * Set the value of telephoneFollowUp
     *
     * @param telephoneFollowUp new value of telephoneFollowUp
     */
    public void setTelephoneFollowUp(String telephoneFollowUp) {
        this.telephoneFollowUp = telephoneFollowUp;
    }

    /**
     * Get the value of ast
     *
     * @return the value of ast
     */
    public BigDecimal getAst() {
        return ast;
    }

    /**
     * Set the value of ast
     *
     * @param ast new value of ast
     */
    public void setAst(BigDecimal ast) {
        this.ast = ast;
    }

    /**
     * Get the value of alt
     *
     * @return the value of alt
     */
    public BigDecimal getAlt() {
        return alt;
    }

    /**
     * Set the value of alt
     *
     * @param alt new value of alt
     */
    public void setAlt(BigDecimal alt) {
        this.alt = alt;
    }

    /**
     * Get the value of psa
     *
     * @return the value of psa
     */
    public BigDecimal getPsa() {
        return psa;
    }

    /**
     * Set the value of psa
     *
     * @param psa new value of psa
     */
    public void setPsa(BigDecimal psa) {
        this.psa = psa;
    }

    /**
     * Get the value of noteTopic
     *
     * @return the value of noteTopic
     */
    public String getNoteTopic() {
        return noteTopic;
    }

    /**
     * Set the value of noteTopic
     *
     * @param noteTopic new value of noteTopic
     */
    public void setNoteTopic(String noteTopic) {
        this.noteTopic = noteTopic;
    }

    /**
     * Get the value of note
     *
     * @return the value of note
     */
    public String getNote() {
        return note;
    }

    /**
     * Set the value of note
     *
     * @param note new value of note
     */
    public void setNote(String note) {
        this.note = note;
    }

    /**
     * Get the value of dateEntered
     *
     * @return the value of dateEntered
     */
    public Date getDateEntered() {
        return dateEntered;
    }

    /**
     * Set the value of dateEntered
     *
     * @param dateEntered new value of dateEntered
     */
    public void setDateEntered(Date dateEntered) {
        this.dateEntered = dateEntered;
    }

    /**
     * Get the value of poc
     *
     * @return the value of poc
     */
    public Boolean getPoc() {
        return poc;
    }

    /**
     * Set the value of poc
     *
     * @param poc new value of poc
     */
    public void setPoc(Boolean poc) {
        this.poc = poc;
    }

    /**
     * Get the value of userName
     *
     * @return the value of userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Set the value of userName
     *
     * @param userName new value of userName
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Get the value of clinicId
     *
     * @return the value of clinicId
     */
    public Integer getClinicId() {
        return clinicId;
    }

    /**
     * Set the value of clinicId
     *
     * @param clinicId new value of clinicId
     */
    public void setClinicId(Integer clinicId) {
        this.clinicId = clinicId;
    }
}
