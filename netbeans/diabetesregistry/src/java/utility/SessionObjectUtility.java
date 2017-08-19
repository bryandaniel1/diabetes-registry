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
package utility;

import javax.servlet.http.HttpSession;

/**
 * This utility class holds keys for the session map and contains methods for
 * resetting session attributes to their default values.
 *
 * @author Bryan Daniel
 * @version 2, March 16, 2017
 */
public class SessionObjectUtility {

    /**
     * The user attribute key
     */
    public static final String USER = "user";

    /**
     * The password management attribute key
     */
    public static final String PASSWORD_MANAGEMENT_KEY = "passwordManagementKey";

    /**
     * The patients attribute key
     */
    public static final String PATIENTS = "patients";

    /**
     * The patient attribute key
     */
    public static final String PATIENT = "patient";

    /**
     * The progress note attribute key
     */
    public static final String PROGRESS_NOTE = "progressNote";

    /**
     * The progress dates attribute key
     */
    public static final String PROGRESS_DATES = "progressDates";

    /**
     * The call list patients attribute key
     */
    public static final String CALL_LIST_PATIENTS = "callListPatients";

    /**
     * The recent checklist items attribute key
     */
    public static final String RECENT_CHECKLIST_ITEMS = "recentChecklistItems";

    /**
     * The call list subject attribute key
     */
    public static final String CALL_LIST_SUBJECT = "callListSubject";

    /**
     * The measurement date-type-header attribute key
     */
    public static final String MEASUREMENT_DATE_TYPE_HEADER = "measurementDateTypeHeader";

    /**
     * The attribute key for A1C graph data
     */
    public static final String A1C_GRAPH_POINTS = "a1cGraphPoints";

    /**
     * The attribute key for PSA graph data
     */
    public static final String PSA_GRAPH_POINTS = "psaGraphPoints";

    /**
     * The attribute key for ALT graph data
     */
    public static final String ALT_GRAPH_POINTS = "altGraphPoints";

    /**
     * The attribute key for AST graph data
     */
    public static final String AST_GRAPH_POINTS = "astGraphPoints";

    /**
     * The attribute key for blood pressure graph data
     */
    public static final String BP_GRAPH_POINTS = "bpGraphPoints";

    /**
     * The attribute key for BMI graph data
     */
    public static final String BMI_GRAPH_POINTS = "bmiGraphPoints";

    /**
     * The attribute key for creatinine graph data
     */
    public static final String CREATININE_GRAPH_POINTS = "creatinineGraphPoints";

    /**
     * The attribute key for eGFR graph data
     */
    public static final String EGFR_GRAPH_POINTS = "egfrGraphPoints";

    /**
     * The attribute key for glucose graph data
     */
    public static final String GLUCOSE_GRAPH_POINTS = "glucoseGraphPoints";

    /**
     * The attribute key for HDL graph data
     */
    public static final String HDL_GRAPH_POINTS = "hdlGraphPoints";

    /**
     * The attribute key for LDL graph data
     */
    public static final String LDL_GRAPH_POINTS = "ldlGraphPoints";

    /**
     * The attribute key for patient compliance graph data
     */
    public static final String COMPLIANCE_GRAPH_POINTS = "complianceGraphPoints";

    /**
     * The attribute key for physical activity graph data
     */
    public static final String PHYSICAL_ACTIVITY_GRAPH_POINTS = "physicalActivityGraphPoints";

    /**
     * The attribute key for psychological screening graph data
     */
    public static final String PSYCHOLOGICAL_GRAPH_POINTS = "psychologicalGraphPoints";

    /**
     * The attribute key for T4 graph data
     */
    public static final String T4_GRAPH_POINTS = "t4GraphPoints";

    /**
     * The attribute key for triglycerides graph data
     */
    public static final String TRIGLYCERIDES_GRAPH_POINTS = "triglyceridesGraphPoints";

    /**
     * The attribute key for TSH graph data
     */
    public static final String TSH_GRAPH_POINTS = "tshGraphPoints";

    /**
     * The attribute key for UACR graph data
     */
    public static final String UACR_GRAPH_POINTS = "uacrGraphPoints";

    /**
     * The attribute key for waist graph data
     */
    public static final String WAIST_GRAPH_POINTS = "waistGraphPoints";

    /**
     * The attribute key for age demographics graph data
     */
    public static final String AGE_DEMOGRAPHICS_GRAPH_DATA = "ageDemographicsGraphData";

    /**
     * The attribute key for gender demographics graph data
     */
    public static final String GENDER_DEMOGRAPHICS_GRAPH_DATA = "genderDemographicsGraphData";

    /**
     * The attribute key for race demographics graph data
     */
    public static final String RACE_DEMOGRAPHICS_GRAPH_DATA = "raceDemographicsGraphData";

    /**
     * The attribute key for the most recent A1C data
     */
    public static final String LAST_A1C_DATA = "lastA1cData";

    /**
     * The attribute key for the most recent A1C data by class attendance
     */
    public static final String LAST_A1C_BY_CLASS_DATA = "lastA1cByClassData";

    /**
     * The attribute key for the most recent BMI data for males
     */
    public static final String LAST_BMI_MALES_DATA = "lastBmiMalesData";

    /**
     * The attribute key for the most recent BMI data for females
     */
    public static final String LAST_BMI_FEMALES_DATA = "lastBmiFemalesData";

    /**
     * The attribute key for the most recent BMI data for males by class
     * attendance
     */
    public static final String LAST_BMI_MALES_BY_CLASS_DATA = "lastBmiMalesByClassData";

    /**
     * The attribute key for the most recent BMI data for females by class
     * attendance
     */
    public static final String LAST_BMI_FEMALES_BY_CLASS_DATA = "lastBmiFemalesByClassData";

    /**
     * The attribute key for the most recent A1C data by treatment class
     */
    public static final String LAST_A1C_BY_TREATMENT = "lastA1cByTreatment";

    /**
     * The attribute key for treatment class statistics
     */
    public static final String CLASS_COUNTS_TREATMENT_STATS = "classCountsTreatmentStats";

    /**
     * The attribute key for treatment class statistics by gender
     */
    public static final String GENDER_CLASS_COUNTS_TREATMENT_STATS = "genderClassCountsTreatmentStats";

    /**
     * The attribute key for treatment class statistics by race
     */
    public static final String RACE_CLASS_COUNTS_TREATMENT_STATS = "raceClassCountsTreatmentStats";

    /**
     * The user names attribute key
     */
    public static final String USER_NAMES = "userNames";

    /**
     * The attribute key for user details
     */
    public static final String DETAILED_USER = "detailedUser";

    /**
     * The attribute key for selected email message configuration
     */
    public static final String SELECTED_EMAIL_MESSAGE_CONFIGURATION = "selectedEmailMessageConfiguration";

    /**
     * The attribute key for email message configuration container
     */
    public static final String EMAIL_MESSAGE_CONFIGURATION_CONTAINER = "emailMessageConfigurationContainer";

    /**
     * The attribute key for quality configurations
     */
    public static final String QUALITY_CONFIGURATIONS = "qualityConfigurations";

    /**
     * The attribute key for quality configuration roles
     */
    public static final String QUALITY_CONFIGURATION_ROLES = "qualityConfigurationRoles";

    /**
     * This method resets the clinic objects, including the patient attribute,
     * to default values.
     *
     * @param session the HttpSession object
     */
    public static void resetClinicObjects(HttpSession session) {

        session.setAttribute(PATIENT, null);
        session.setAttribute(PROGRESS_NOTE, null);
        session.setAttribute(PROGRESS_DATES, null);
        session.setAttribute(CALL_LIST_PATIENTS, null);
        session.setAttribute(RECENT_CHECKLIST_ITEMS, null);
    }

    /**
     * This method resets the objects associated with selected patients to
     * default values.
     *
     * @param session the HttpSession object
     */
    public static void resetPatientObjects(HttpSession session) {

        session.setAttribute(PROGRESS_NOTE, null);
        session.setAttribute(PROGRESS_DATES, null);
        session.setAttribute(CALL_LIST_PATIENTS, null);
        session.setAttribute(RECENT_CHECKLIST_ITEMS, null);
    }
}
