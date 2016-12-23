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
import java.sql.Date;
import java.util.ArrayList;

/**
 * This class holds all the details for a patient's progress note.
 *
 * @author Bryan Daniel
 * @version 1, April 8, 2016s
 */
public class ProgressNote implements Serializable {

    /**
     * The patient
     */
    private Patient patient;

    /**
     * The date of the progress note
     */
    private Date dateCreated;

    /**
     * Indicates if the patient has medical insurance
     */
    private boolean medicalInsurance;

    /**
     * The patient's shoe size
     */
    private String shoeSize;

    /**
     * Indicates if the patient is allergic to medications
     */
    private boolean allergicToMedications;

    /**
     * The description of allergies
     */
    private String allergies;

    /**
     * The weight
     */
    private BigDecimal weight;

    /**
     * The height in feet
     */
    private Integer heightFeet;

    /**
     * The remainder of the patient's height in inches
     */
    private Integer heightInches;

    /**
     * The goal for weight reduction
     */
    private BigDecimal weightReductionGoal;

    /**
     * The pulse
     */
    private Integer pulse;

    /**
     * The number of respirations measured
     */
    private Integer respirations;

    /**
     * The patient's temperature
     */
    private BigDecimal temperature;

    /**
     * Indicates a foot screening was performed
     */
    private boolean footScreening;

    /**
     * The description of medications
     */
    private String medications;

    /**
     * The A1C measurement
     */
    private BigDecimal a1c;

    /**
     * The glucose measurement
     */
    private BigDecimal glucose;

    /**
     * The waist measurement
     */
    private BigDecimal waist;

    /**
     * The systolic blood pressure
     */
    private Integer bpSystole;

    /**
     * The diastolic blood pressure
     */
    private Integer bpDiastole;

    /**
     * Indication of an ACE or ARB treatment
     */
    private boolean aceOrArb;

    /**
     * The BMI measurement
     */
    private BigDecimal bmi;

    /**
     * The date of the last class attended
     */
    private Date lastClassDate;

    /**
     * The eye screening result
     */
    private String eyeScreeningCategory;

    /**
     * The foot screening result
     */
    private String footScreeningCategory;

    /**
     * The psychological screening result
     */
    private Integer psychologicalScreening;

    /**
     * The physical activity during the week measured in minutes
     */
    private Integer physicalActivity;

    /**
     * The smoking status
     */
    private Boolean smoking;

    /**
     * The patient's compliance with treatment
     */
    private BigDecimal compliance;

    /**
     * The note for the nurse or dietitian
     */
    private String nurseOrDietitianNote;

    /**
     * The subjective section of the note
     */
    private String subjective;

    /**
     * The objective section of the note
     */
    private String objective;

    /**
     * The assessment section of the note
     */
    private String assessment;

    /**
     * The plan section of the note
     */
    private String plan;

    /**
     * The list of note writers
     */
    private ArrayList<NoteAuthor> updatedBy;

    /**
     * Default constructor
     */
    public ProgressNote() {
        patient = null;
        dateCreated = null;
        shoeSize = null;
        allergies = null;
        weight = null;
        heightFeet = null;
        heightInches = null;
        weightReductionGoal = null;
        pulse = null;
        respirations = null;
        temperature = null;
        medications = null;
        a1c = null;
        glucose = null;
        waist = null;
        bpSystole = null;
        bpDiastole = null;
        bmi = null;
        nurseOrDietitianNote = null;
        subjective = null;
        objective = null;
        assessment = null;
        plan = null;
        updatedBy = null;
    }

    /**
     * Parameterized constructor
     *
     * @param patient the patient
     * @param dateCreated the date note was created
     * @param medicalInsurance medical insurance status
     * @param shoeSize patient shoe size
     * @param allergicToMedications allergic to medications status
     * @param allergies allergies
     * @param weight weight
     * @param heightFeet the height in feet
     * @param heightInches the height in inches
     * @param weight_reduction_goal the goal for weight reduction
     * @param pulse pulse
     * @param respirations respirations
     * @param temperature temperature
     * @param footScreening footScreeningCategory screening status
     * @param nurseOrDietitianNote nurse or dietitian note
     * @param subjective subjective note
     * @param objective objective note
     * @param assessment assessment note
     * @param plan plan note
     * @param patientAddress the patient address
     * @param updatedBy the list of note authors
     * @param medications the patient's medications
     * @param a1c the A1C measurement
     * @param glucose the glucose measurement
     * @param waist the waist measurement
     * @param bpSystole the BP systole measurement
     * @param bpDiastole the BP diastole measurement
     * @param bmi the BMI
     */
    public ProgressNote(Patient patient, Date dateCreated,
            boolean medicalInsurance, String shoeSize,
            boolean allergicToMedications, String allergies,
            BigDecimal weight, Integer heightFeet, Integer heightInches,
            BigDecimal weight_reduction_goal, Integer pulse, Integer respirations,
            BigDecimal temperature, boolean footScreening,
            String nurseOrDietitianNote, String subjective, String objective,
            String assessment, String plan, String patientAddress,
            ArrayList<NoteAuthor> updatedBy, String medications, BigDecimal a1c,
            BigDecimal glucose, BigDecimal waist, Integer bpSystole, Integer bpDiastole,
            BigDecimal bmi) {
        this.patient = patient;
        this.dateCreated = dateCreated;
        this.medicalInsurance = medicalInsurance;
        this.shoeSize = shoeSize;
        this.allergicToMedications = allergicToMedications;
        this.allergies = allergies;
        this.weight = weight;
        this.heightFeet = heightFeet;
        this.heightInches = heightInches;
        this.weightReductionGoal = weight_reduction_goal;
        this.pulse = pulse;
        this.respirations = respirations;
        this.temperature = temperature;
        this.footScreening = footScreening;
        this.medications = medications;
        this.a1c = a1c;
        this.glucose = glucose;
        this.bmi = bmi;
        this.waist = bmi;
        this.bpSystole = bpSystole;
        this.bpDiastole = bpDiastole;
        this.nurseOrDietitianNote = nurseOrDietitianNote;
        this.subjective = subjective;
        this.objective = objective;
        this.assessment = assessment;
        this.plan = plan;
        this.updatedBy = updatedBy;
    }

    /**
     * Get the value of patient
     *
     * @return the value of patient
     */
    public Patient getPatient() {
        return patient;
    }

    /**
     * Set the value of patient
     *
     * @param patient new value of patient
     */
    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    /**
     * Get the value of dateCreated
     *
     * @return the value of dateCreated
     */
    public Date getDateCreated() {
        return dateCreated;
    }

    /**
     * Set the value of dateCreated
     *
     * @param dateCreated new value of dateCreated
     */
    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    /**
     * Get the value of medicalInsurance
     *
     * @return the value of medicalInsurance
     */
    public boolean getMedicalInsurance() {
        return medicalInsurance;
    }

    /**
     * Set the value of medicalInsurance
     *
     * @param medicalInsurance new value of medicalInsurance
     */
    public void setMedicalInsurance(boolean medicalInsurance) {
        this.medicalInsurance = medicalInsurance;
    }

    /**
     * Get the value of shoeSize
     *
     * @return the value of shoeSize
     */
    public String getShoeSize() {
        return shoeSize;
    }

    /**
     * Set the value of shoeSize
     *
     * @param shoeSize new value of shoeSize
     */
    public void setShoeSize(String shoeSize) {
        this.shoeSize = shoeSize;
    }

    /**
     * Get the value of allergicToMedications
     *
     * @return the value of allergicToMedications
     */
    public boolean getAllergicToMedications() {
        return allergicToMedications;
    }

    /**
     * Set the value of allergicToMedications
     *
     * @param allergicToMedications new value of allergicToMedications
     */
    public void setAllergicToMedications(boolean allergicToMedications) {
        this.allergicToMedications = allergicToMedications;
    }

    /**
     * Get the value of allergies
     *
     * @return the value of allergies
     */
    public String getAllergies() {
        return allergies;
    }

    /**
     * Set the value of allergies
     *
     * @param allergies new value of allergies
     */
    public void setAllergies(String allergies) {
        this.allergies = allergies;
    }

    /**
     * Get the value of weight
     *
     * @return the value of weight
     */
    public BigDecimal getWeight() {
        return weight;
    }

    /**
     * Set the value of weight
     *
     * @param weight new value of weight
     */
    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    /**
     * Get the value of heightFeet
     *
     * @return the value of heightFeet
     */
    public Integer getHeightFeet() {
        return heightFeet;
    }

    /**
     * Set the value of heightFeet
     *
     * @param heightFeet new value of heightFeet
     */
    public void setHeightFeet(Integer heightFeet) {
        this.heightFeet = heightFeet;
    }

    /**
     * Get the value of heightInches
     *
     * @return the value of heightInches
     */
    public Integer getHeightInches() {
        return heightInches;
    }

    /**
     * Set the value of heightInches
     *
     * @param heightInches new value of heightInches
     */
    public void setHeightInches(Integer heightInches) {
        this.heightInches = heightInches;
    }

    /**
     * Get the value of weightReductionGoal
     *
     * @return the value of weightReductionGoal
     */
    public BigDecimal getWeightReductionGoal() {
        return weightReductionGoal;
    }

    /**
     * Set the value of weightReductionGoal
     *
     * @param weightReductionGoal new value of weightReductionGoal
     */
    public void setWeightReductionGoal(BigDecimal weightReductionGoal) {
        this.weightReductionGoal = weightReductionGoal;
    }

    /**
     * Get the value of pulse
     *
     * @return the value of pulse
     */
    public Integer getPulse() {
        return pulse;
    }

    /**
     * Set the value of pulse
     *
     * @param pulse new value of pulse
     */
    public void setPulse(Integer pulse) {
        this.pulse = pulse;
    }

    /**
     * Get the value of respirations
     *
     * @return the value of respirations
     */
    public Integer getRespirations() {
        return respirations;
    }

    /**
     * Set the value of respirations
     *
     * @param respirations new value of respirations
     */
    public void setRespirations(Integer respirations) {
        this.respirations = respirations;
    }

    /**
     * Get the value of temperature
     *
     * @return the value of temperature
     */
    public BigDecimal getTemperature() {
        return temperature;
    }

    /**
     * Set the value of temperature
     *
     * @param temperature new value of temperature
     */
    public void setTemperature(BigDecimal temperature) {
        this.temperature = temperature;
    }

    /**
     * Get the value of footScreening
     *
     * @return the value of footScreening
     */
    public boolean getFootScreening() {
        return footScreening;
    }

    /**
     * Set the value of footScreening
     *
     * @param footScreening new value of footScreening
     */
    public void setFootScreening(boolean footScreening) {
        this.footScreening = footScreening;
    }

    /**
     * Get the value of nurseOrDietitianNote
     *
     * @return the value of nurseOrDietitianNote
     */
    public String getNurseOrDietitianNote() {
        return nurseOrDietitianNote;
    }

    /**
     * Set the value of nurseOrDietitianNote
     *
     * @param nurseOrDietitianNote new value of nurseOrDietitianNote
     */
    public void setNurseOrDietitianNote(String nurseOrDietitianNote) {
        this.nurseOrDietitianNote = nurseOrDietitianNote;
    }

    /**
     * Get the value of subjective
     *
     * @return the value of subjective
     */
    public String getSubjective() {
        return subjective;
    }

    /**
     * Set the value of subjective
     *
     * @param subjective new value of subjective
     */
    public void setSubjective(String subjective) {
        this.subjective = subjective;
    }

    /**
     * Get the value of objective
     *
     * @return the value of objective
     */
    public String getObjective() {
        return objective;
    }

    /**
     * Set the value of objective
     *
     * @param objective new value of objective
     */
    public void setObjective(String objective) {
        this.objective = objective;
    }

    /**
     * Get the value of assessment
     *
     * @return the value of assessment
     */
    public String getAssessment() {
        return assessment;
    }

    /**
     * Set the value of assessment
     *
     * @param assessment new value of assessment
     */
    public void setAssessment(String assessment) {
        this.assessment = assessment;
    }

    /**
     * Get the value of plan
     *
     * @return the value of plan
     */
    public String getPlan() {
        return plan;
    }

    /**
     * Set the value of plan
     *
     * @param plan new value of plan
     */
    public void setPlan(String plan) {
        this.plan = plan;
    }

    /**
     * Get the value of updatedBy
     *
     * @return the value of updatedBy
     */
    public ArrayList<NoteAuthor> getUpdatedBy() {
        return updatedBy;
    }

    /**
     * Set the value of updatedBy
     *
     * @param updatedBy new value of updatedBy
     */
    public void setUpdatedBy(ArrayList<NoteAuthor> updatedBy) {
        this.updatedBy = updatedBy;
    }

    /**
     * Get the value of medications
     *
     * @return the value of medications
     */
    public String getMedications() {
        return medications;
    }

    /**
     * Set the value of medications
     *
     * @param medications new value of medications
     */
    public void setMedications(String medications) {
        this.medications = medications;
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
     * Get the value of glucose
     *
     * @return the value of glucose
     */
    public BigDecimal getGlucose() {
        return glucose;
    }

    /**
     * Set the value of glucose
     *
     * @param glucose new value of glucose
     */
    public void setGlucose(BigDecimal glucose) {
        this.glucose = glucose;
    }

    /**
     * Get the value of bpSystole
     *
     * @return the value of bpSystole
     */
    public Integer getBpSystole() {
        return bpSystole;
    }

    /**
     * Set the value of bpSystole
     *
     * @param bpSystole new value of bpSystole
     */
    public void setBpSystole(Integer bpSystole) {
        this.bpSystole = bpSystole;
    }

    /**
     * Get the value of bpDiastole
     *
     * @return the value of bpDiastole
     */
    public Integer getBpDiastole() {
        return bpDiastole;
    }

    /**
     * Set the value of bpDiastole
     *
     * @param bpDiastole new value of bpDiastole
     */
    public void setBpDiastole(Integer bpDiastole) {
        this.bpDiastole = bpDiastole;
    }

    /**
     * Get the value of aceOrArb
     *
     * @return the value of aceOrArb
     */
    public boolean getAceOrArb() {
        return aceOrArb;
    }

    /**
     * Set the value of aceOrArb
     *
     * @param aceOrArb new value of aceOrArb
     */
    public void setAceOrArb(boolean aceOrArb) {
        this.aceOrArb = aceOrArb;
    }

    /**
     * Get the value of BMI
     *
     * @return the value of BMI
     */
    public BigDecimal getBmi() {
        return bmi;
    }

    /**
     * Set the value of BMI
     *
     * @param bmi new value of BMI
     */
    public void setBmi(BigDecimal bmi) {
        this.bmi = bmi;
    }

    /**
     * Get the value of waist
     *
     * @return the value of waist
     */
    public BigDecimal getWaist() {
        return waist;
    }

    /**
     * Set the value of waist
     *
     * @param waist new value of waist
     */
    public void setWaist(BigDecimal waist) {
        this.waist = waist;
    }

    /**
     * Get the value of lastClassDate
     *
     * @return the value of lastClassDate
     */
    public Date getLastClassDate() {
        return lastClassDate;
    }

    /**
     * Set the value of lastClassDate
     *
     * @param lastClassDate the new value of lastClassDate
     */
    public void setLastClassDate(Date lastClassDate) {
        this.lastClassDate = lastClassDate;
    }

    /**
     * Get the value of eyeScreeningCategory
     *
     * @return the value of eyeScreeningCategory
     */
    public String getEyeScreeningCategory() {
        return eyeScreeningCategory;
    }

    /**
     * Set the value of eyeScreeningCategory
     *
     * @param eyeScreeningCategory the value of eyeScreeningCategory
     */
    public void setEyeScreeningCategory(String eyeScreeningCategory) {
        this.eyeScreeningCategory = eyeScreeningCategory;
    }

    /**
     * Get the value of footScreeningCategory
     *
     * @return the value of footScreeningCategory
     */
    public String getFootScreeningCategory() {
        return footScreeningCategory;
    }

    /**
     * Set the value of footScreeningCategory
     *
     * @param footScreeningCategory the value of footScreeningCategory
     */
    public void setFootScreeningCategory(String footScreeningCategory) {
        this.footScreeningCategory = footScreeningCategory;
    }

    /**
     * Get the value of psychologicalScreening
     *
     * @return the value of psychologicalScreening
     */
    public Integer getPsychologicalScreening() {
        return psychologicalScreening;
    }

    /**
     * Set the value of psychologicalScreening
     *
     * @param psychologicalScreening the value of psychologicalScreening
     */
    public void setPsychologicalScreening(Integer psychologicalScreening) {
        this.psychologicalScreening = psychologicalScreening;
    }

    /**
     * Get the value of physicalActivity
     *
     * @return the value of physicalActivity
     */
    public Integer getPhysicalActivity() {
        return physicalActivity;
    }

    /**
     * Set the value of physicalActivity
     *
     * @param physicalActivity the value of physicalActivity
     */
    public void setPhysicalActivity(Integer physicalActivity) {
        this.physicalActivity = physicalActivity;
    }

    /**
     * Get the smoking status of the patient
     *
     * @return the smoking status of the patient
     */
    public Boolean getSmoking() {
        return smoking;
    }

    /**
     * Set the smoking status of the patient
     *
     * @param smoking the smoking status of the patient
     */
    public void setSmoking(Boolean smoking) {
        this.smoking = smoking;
    }

    /**
     * Get the reported compliance of the patient
     *
     * @return the reported compliance of the patient
     */
    public BigDecimal getCompliance() {
        return compliance;
    }

    /**
     * Set the reported compliance of the patient
     *
     * @param compliance the reported compliance of the patient
     */
    public void setCompliance(BigDecimal compliance) {
        this.compliance = compliance;
    }
}
