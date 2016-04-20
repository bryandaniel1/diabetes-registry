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

/*********************************************************************************************
* Create the database named diabetes_registry, its tables, its procedures, and a user
*********************************************************************************************/

DROP DATABASE IF EXISTS diabetes_registry;

CREATE DATABASE diabetes_registry;

USE diabetes_registry;

CREATE TABLE User (
	user_name VARCHAR(50) NOT NULL, first_name VARCHAR(50) NOT NULL,
	last_name VARCHAR(50) NOT NULL, job_title VARCHAR(50) NOT NULL,
	date_joined DATETIME NOT NULL, last_login DATETIME,
	active TINYINT(1) NOT NULL, 
	administrator TINYINT(1) NOT NULL, 
	CONSTRAINT user_name_pk PRIMARY KEY(user_name)
) ENGINE=InnoDB;

CREATE TABLE UserCredentials (
	user_name VARCHAR(50) NOT NULL,
	password VARCHAR(64) NOT NULL,
	salt VARCHAR(64) NOT NULL,
	change_password TINYINT(1) NOT NULL,
	KEY FK_UserCredentials_user_name (user_name),
	CONSTRAINT FK_UserCredentials_user_name FOREIGN KEY (user_name) 
	REFERENCES User (user_name)
) ENGINE=InnoDB;

CREATE TABLE Clinic (
	clinic_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	name VARCHAR(50) NOT NULL,
	address VARCHAR(1000) NOT NULL,
	phone_number VARCHAR(50) NOT NULL,
	registration_key VARCHAR(64) UNIQUE NOT NULL,
	salt VARCHAR(64) UNIQUE NOT NULL
) ENGINE=InnoDB;

CREATE TABLE ClinicEmailAddress (
	clinic_id INT NOT NULL, 
	email_address VARCHAR(255) NOT NULL, 
	PRIMARY KEY (clinic_id),
	FOREIGN KEY (clinic_id) REFERENCES Clinic (clinic_id)
) ENGINE=InnoDB;

CREATE TABLE UserRegistration (
	user_name VARCHAR(50) NOT NULL,
	registration_key VARCHAR(64) NOT NULL,
	KEY FK_UserRegistration_user_name (user_name),
	CONSTRAINT FK_UserRegistration_user_name FOREIGN KEY (user_name) 
	REFERENCES User (user_name),
	KEY FK_UserRegistration_registration_key (registration_key),
	CONSTRAINT FK_UserRegistration_registration_key FOREIGN KEY (registration_key) 
	REFERENCES Clinic (registration_key) ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE Patient (
	patient_id INT NOT NULL AUTO_INCREMENT,
	first_name VARBINARY(100) NOT NULL,
	last_name VARBINARY(100) NOT NULL,
	birth_date VARBINARY(100) NOT NULL,
	contact_number VARBINARY(100),
	gender VARBINARY(100) NOT NULL,
	race VARBINARY(100) NOT NULL, 
	start_date VARBINARY(100) NOT NULL, 
	PRIMARY KEY (patient_id)
) ENGINE=InnoDB;

CREATE TABLE PatientClinic (
	patient_id INT NOT NULL, 
	clinic_id INT NOT NULL, 
	KEY FK_PatientClinic_patient_id (patient_id),
	CONSTRAINT FK_PatientClinic_patient_id FOREIGN KEY (patient_id) 
	REFERENCES Patient (patient_id) 
	ON DELETE CASCADE, 
	KEY FK_PatientClinic_clinic_id (clinic_id),
	CONSTRAINT FK_PatientClinic_clinic_id FOREIGN KEY (clinic_id) 
	REFERENCES Clinic (clinic_id)
) ENGINE=InnoDB;

CREATE TABLE PatientAddress (
	patient_id INT NOT NULL, 
	address VARBINARY(500) NOT NULL, 
	PRIMARY KEY (patient_id),
	FOREIGN KEY (patient_id) REFERENCES Patient (patient_id) 
	ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE PatientEmailAddress (
	patient_id INT NOT NULL, 
	email_address VARBINARY(500) NOT NULL, 
	PRIMARY KEY (patient_id),
	FOREIGN KEY (patient_id) REFERENCES Patient (patient_id) 
	ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE Language (
	language VARCHAR(50) NOT NULL, 
	PRIMARY KEY (language) 
) ENGINE=InnoDB;

CREATE TABLE EmailMessageSubject (
	subject VARCHAR(50) NOT NULL, 
	PRIMARY KEY (subject) 
) ENGINE=InnoDB;

CREATE TABLE PatientLanguage (
	patient_id INT NOT NULL, 
	language VARCHAR(50) NOT NULL, 
	PRIMARY KEY (patient_id),
	FOREIGN KEY (patient_id) REFERENCES Patient (patient_id) 
	ON DELETE CASCADE, 
	FOREIGN KEY (language) REFERENCES Language (language) 
) ENGINE=InnoDB;

CREATE TABLE EmailMessage (
	clinic_id INT NOT NULL, 
	language VARCHAR(50) NOT NULL, 
	subject VARCHAR(50) NOT NULL, 
	message VARCHAR(1000) NOT NULL, 
	FOREIGN KEY (clinic_id) REFERENCES Clinic (clinic_id), 
	FOREIGN KEY (language) REFERENCES Language (language), 
	FOREIGN KEY (subject) REFERENCES EmailMessageSubject (subject), 
	CONSTRAINT PK_EmailMessage_clinic_id_language_subject 
	PRIMARY KEY (clinic_id, language, subject)
) ENGINE=InnoDB;

CREATE TABLE ReasonForInactivity (
	reason VARCHAR(50) NOT NULL, 
	PRIMARY KEY (reason)
) ENGINE=InnoDB;

CREATE TABLE InactivePatient (
	patient_id INT NOT NULL, 
	reason VARCHAR(50) NOT NULL, 
	PRIMARY KEY (patient_id),
	FOREIGN KEY (patient_id) REFERENCES Patient (patient_id) 
	ON DELETE CASCADE, 
	FOREIGN KEY (reason) REFERENCES ReasonForInactivity (reason)
) ENGINE=InnoDB;

CREATE TABLE Therapy (
	rx_class VARCHAR(50) NOT NULL PRIMARY KEY,
	therapy_type VARCHAR(50) NOT NULL
) ENGINE=InnoDB;

CREATE TABLE Medication (
	med_id VARCHAR(50) NOT NULL PRIMARY KEY,
	med_name VARCHAR(50) NOT NULL,
	med_class VARCHAR(50) NOT NULL
) ENGINE=InnoDB;
 
CREATE TABLE PatientRx (
	patient_id INT NOT NULL,
	date_recorded DATE NOT NULL,
	rx_class VARCHAR(50) NOT NULL,
	updated_by VARCHAR(50),
	clinic_id INT,
	KEY FK_PatientRx_patient_id (patient_id),
	CONSTRAINT FK_PatientRx_patient_id FOREIGN KEY (patient_id) 
	REFERENCES Patient (patient_id) 
	ON DELETE CASCADE, 
	KEY FK_PatientRx_rx_class (rx_class),
	CONSTRAINT FK_PatientRx_rx_class FOREIGN KEY (rx_class)
	REFERENCES Therapy (rx_class)
) ENGINE=InnoDB;

CREATE TABLE PatientMed (
	patient_id INT NOT NULL,
	date_recorded DATE NOT NULL,
	med_id VARCHAR(50) NOT NULL,
	updated_by VARCHAR(50),
	clinic_id INT,
	KEY FK_PatientMed_patient_id (patient_id),
	CONSTRAINT FK_PatientMed_patient_id FOREIGN KEY (patient_id) 
	REFERENCES Patient (patient_id) 
	ON DELETE CASCADE, 
	KEY FK_PatientMed_med_id (med_id),
	CONSTRAINT FK_PatientMed_med_id FOREIGN KEY (med_id)
	REFERENCES Medication (med_id)
) ENGINE=InnoDB;

CREATE TABLE A1C (
	patient_id INT NOT NULL,
	date_recorded DATE NOT NULL,
	result DECIMAL(7,2) NOT NULL,
	poc TINYINT(1) NOT NULL,
	updated_by VARCHAR(50),
	clinic_id INT,
	FOREIGN KEY (patient_id) REFERENCES Patient (patient_id) 
	ON DELETE CASCADE, 
	FOREIGN KEY (updated_by) REFERENCES User (user_name),
	FOREIGN KEY (clinic_id) REFERENCES Clinic (clinic_id),
	CONSTRAINT PK_A1C_patient_id_date_recorded 
	PRIMARY KEY (patient_id, date_recorded)	
) ENGINE=InnoDB;

CREATE TABLE Glucose (
	patient_id INT NOT NULL,
	date_recorded DATE NOT NULL,
	result DECIMAL(7,2) NOT NULL,
	updated_by VARCHAR(50) NOT NULL,
	clinic_id INT NOT NULL,
	FOREIGN KEY (patient_id) REFERENCES Patient (patient_id) 
	ON DELETE CASCADE, 
	FOREIGN KEY (updated_by) REFERENCES User (user_name),
	FOREIGN KEY (clinic_id) REFERENCES Clinic (clinic_id),
	CONSTRAINT PK_Glucose_patient_id_date_recorded 
	PRIMARY KEY (patient_id, date_recorded)	
) ENGINE=InnoDB;

CREATE TABLE LastGlucoseFasting (
	patient_id INT NOT NULL, 
	ac TINYINT NOT NULL, 
	PRIMARY KEY (patient_id), 
	FOREIGN KEY (patient_id) REFERENCES Patient (patient_id) 
	ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE LDL (
	patient_id INT NOT NULL,
	date_recorded DATE NOT NULL,
	result DECIMAL(7,2) NOT NULL, 
	post_mi TINYINT(1) NOT NULL, 
	updated_by VARCHAR(50) NOT NULL,
	clinic_id INT NOT NULL,
	FOREIGN KEY (patient_id) REFERENCES Patient (patient_id) 
	ON DELETE CASCADE, 
	FOREIGN KEY (updated_by) REFERENCES User (user_name),
	FOREIGN KEY (clinic_id) REFERENCES Clinic (clinic_id),
	CONSTRAINT PK_LDL_patient_id_date_recorded 
	PRIMARY KEY (patient_id, date_recorded)	
) ENGINE=InnoDB;

CREATE TABLE LDLOnStatin (
	patient_id INT NOT NULL, 
	date_recorded DATE NOT NULL, 
	on_statin TINYINT NOT NULL, 
	PRIMARY KEY (patient_id, date_recorded), 
	FOREIGN KEY (patient_id, date_recorded) REFERENCES LDL (patient_id, date_recorded) 
	ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE HDL (
	patient_id INT NOT NULL,
	date_recorded DATE NOT NULL,
	result DECIMAL(7,2) NOT NULL,
	updated_by VARCHAR(50) NOT NULL,
	clinic_id INT NOT NULL,
	FOREIGN KEY (patient_id) REFERENCES Patient (patient_id) 
	ON DELETE CASCADE, 
	FOREIGN KEY (updated_by) REFERENCES User (user_name),
	FOREIGN KEY (clinic_id) REFERENCES Clinic (clinic_id),
	CONSTRAINT PK_HDL_patient_id_date_recorded 
	PRIMARY KEY (patient_id, date_recorded)	
) ENGINE=InnoDB;

CREATE TABLE Triglycerides (
	patient_id INT NOT NULL,
	date_recorded DATE NOT NULL,
	result DECIMAL(7,2) NOT NULL,
	updated_by VARCHAR(50) NOT NULL,
	clinic_id INT NOT NULL,
	FOREIGN KEY (patient_id) REFERENCES Patient (patient_id) 
	ON DELETE CASCADE, 
	FOREIGN KEY (updated_by) REFERENCES User (user_name),
	FOREIGN KEY (clinic_id) REFERENCES Clinic (clinic_id),
	CONSTRAINT PK_Triglycerides_patient_id_date_recorded 
	PRIMARY KEY (patient_id, date_recorded)	
) ENGINE=InnoDB;

CREATE TABLE TSH (
	patient_id INT NOT NULL,
	date_recorded DATE NOT NULL,
	result DECIMAL(7,2) NOT NULL,
	updated_by VARCHAR(50) NOT NULL,
	clinic_id INT NOT NULL,
	FOREIGN KEY (patient_id) REFERENCES Patient (patient_id) 
	ON DELETE CASCADE, 
	FOREIGN KEY (updated_by) REFERENCES User (user_name),
	FOREIGN KEY (clinic_id) REFERENCES Clinic (clinic_id),
	CONSTRAINT PK_TSH_patient_id_date_recorded 
	PRIMARY KEY (patient_id, date_recorded)	
) ENGINE=InnoDB;

CREATE TABLE TSHOnThyroidTreatment (
	patient_id INT NOT NULL, 
	date_recorded DATE NOT NULL,
	on_thyroid_treatment TINYINT NOT NULL, 
	PRIMARY KEY (patient_id, date_recorded), 
	FOREIGN KEY (patient_id, date_recorded) REFERENCES TSH (patient_id, date_recorded) 
	ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE T4 (
	patient_id INT NOT NULL,
	date_recorded DATE NOT NULL,
	result DECIMAL(7,2) NOT NULL,
	updated_by VARCHAR(50) NOT NULL,
	clinic_id INT NOT NULL,
	FOREIGN KEY (patient_id) REFERENCES Patient (patient_id) 
	ON DELETE CASCADE, 
	FOREIGN KEY (updated_by) REFERENCES User (user_name),
	FOREIGN KEY (clinic_id) REFERENCES Clinic (clinic_id),
	CONSTRAINT PK_T4_patient_id_date_recorded 
	PRIMARY KEY (patient_id, date_recorded)	
) ENGINE=InnoDB;

CREATE TABLE UACR (
	patient_id INT NOT NULL,
	date_recorded DATE NOT NULL,
	result DECIMAL(7,2) NOT NULL,
	updated_by VARCHAR(50) NOT NULL,
	clinic_id INT NOT NULL,
	FOREIGN KEY (patient_id) REFERENCES Patient (patient_id) 
	ON DELETE CASCADE, 
	FOREIGN KEY (updated_by) REFERENCES User (user_name),
	FOREIGN KEY (clinic_id) REFERENCES Clinic (clinic_id),
	CONSTRAINT PK_UACR_patient_id_date_recorded 
	PRIMARY KEY (patient_id, date_recorded)	
) ENGINE=InnoDB;

CREATE TABLE EGFR (
	patient_id INT NOT NULL,
	date_recorded DATE NOT NULL,
	result DECIMAL(7,2) NOT NULL,
	updated_by VARCHAR(50) NOT NULL,
	clinic_id INT NOT NULL,
	FOREIGN KEY (patient_id) REFERENCES Patient (patient_id) 
	ON DELETE CASCADE, 
	FOREIGN KEY (updated_by) REFERENCES User (user_name),
	FOREIGN KEY (clinic_id) REFERENCES Clinic (clinic_id),
	CONSTRAINT PK_EGFR_patient_id_date_recorded 
	PRIMARY KEY (patient_id, date_recorded)	
) ENGINE=InnoDB;

CREATE TABLE Creatinine (
	patient_id INT NOT NULL,
	date_recorded DATE NOT NULL,
	result DECIMAL(7,2) NOT NULL,
	updated_by VARCHAR(50) NOT NULL,
	clinic_id INT NOT NULL,
	FOREIGN KEY (patient_id) REFERENCES Patient (patient_id) 
	ON DELETE CASCADE, 
	FOREIGN KEY (updated_by) REFERENCES User (user_name),
	FOREIGN KEY (clinic_id) REFERENCES Clinic (clinic_id),
	CONSTRAINT PK_Creatinine_patient_id_date_recorded 
	PRIMARY KEY (patient_id, date_recorded)	
) ENGINE=InnoDB;

CREATE TABLE BMI (
	patient_id INT NOT NULL,
	date_recorded DATE NOT NULL,
	result DECIMAL(7,2) NOT NULL,
	updated_by VARCHAR(50) NOT NULL,
	clinic_id INT NOT NULL,
	FOREIGN KEY (patient_id) REFERENCES Patient (patient_id) 
	ON DELETE CASCADE, 
	FOREIGN KEY (updated_by) REFERENCES User (user_name),
	FOREIGN KEY (clinic_id) REFERENCES Clinic (clinic_id),
	CONSTRAINT PK_BMI_patient_id_date_recorded 
	PRIMARY KEY (patient_id, date_recorded)	
) ENGINE=InnoDB;

CREATE TABLE Waist (
	patient_id INT NOT NULL,
	date_recorded DATE NOT NULL,
	result DECIMAL(7,2) NOT NULL,
	updated_by VARCHAR(50) NOT NULL,
	clinic_id INT NOT NULL,
	FOREIGN KEY (patient_id) REFERENCES Patient (patient_id) 
	ON DELETE CASCADE, 
	FOREIGN KEY (updated_by) REFERENCES User (user_name),
	FOREIGN KEY (clinic_id) REFERENCES Clinic (clinic_id),
	CONSTRAINT PK_Waist_patient_id_date_recorded 
	PRIMARY KEY (patient_id, date_recorded)	
) ENGINE=InnoDB;

CREATE TABLE BloodPressure (
	patient_id INT NOT NULL,
	date_recorded DATE NOT NULL,
	result_s INT NOT NULL,
	result_d INT NOT NULL,
	ace_or_arb TINYINT(1) NOT NULL,
	updated_by VARCHAR(50) NOT NULL,
	clinic_id INT NOT NULL,
	FOREIGN KEY (patient_id) REFERENCES Patient (patient_id) 
	ON DELETE CASCADE, 
	FOREIGN KEY (updated_by) REFERENCES User (user_name),
	FOREIGN KEY (clinic_id) REFERENCES Clinic (clinic_id),
	CONSTRAINT PK_BloodPressure_patient_id_date_recorded 
	PRIMARY KEY (patient_id, date_recorded)	
) ENGINE=InnoDB;

CREATE TABLE LastClass (
	patient_id INT NOT NULL,
	date_recorded DATE NOT NULL,
	updated_by VARCHAR(50) NOT NULL,
	clinic_id INT NOT NULL,
	FOREIGN KEY (patient_id) REFERENCES Patient (patient_id) 
	ON DELETE CASCADE, 
	FOREIGN KEY (updated_by) REFERENCES User (user_name),
	FOREIGN KEY (clinic_id) REFERENCES Clinic (clinic_id),
	CONSTRAINT PK_LastClass_patient_id_date_recorded 
	PRIMARY KEY (patient_id, date_recorded)	
) ENGINE=InnoDB;

CREATE TABLE EyeExamDefinition (
	eye_exam_code VARCHAR(50) NOT NULL,
	definition VARCHAR(50) NOT NULL,
	CONSTRAINT PK_EyeExamDefinition_eye_exam_code 
	PRIMARY KEY (eye_exam_code)	
) ENGINE=InnoDB;

CREATE TABLE EyeScreening (
	patient_id INT NOT NULL,
	date_recorded DATE NOT NULL,
	eye_exam_code VARCHAR(50) NOT NULL,
	updated_by VARCHAR(50) NOT NULL,
	clinic_id INT NOT NULL,
	FOREIGN KEY (patient_id) REFERENCES Patient (patient_id) 
	ON DELETE CASCADE, 
	FOREIGN KEY (eye_exam_code) REFERENCES EyeExamDefinition (eye_exam_code),
	FOREIGN KEY (updated_by) REFERENCES User (user_name),
	FOREIGN KEY (clinic_id) REFERENCES Clinic (clinic_id),
	CONSTRAINT PK_EyeScreening_patient_id_date_recorded 
	PRIMARY KEY (patient_id, date_recorded)	
) ENGINE=InnoDB;

CREATE TABLE FootExamRiskDefinition (
	risk_category VARCHAR(50) NOT NULL,
	definition VARCHAR(100) NOT NULL,
	CONSTRAINT PK_FootExamRiskDefinition_risk_category 
	PRIMARY KEY (risk_category)	
) ENGINE=InnoDB;

CREATE TABLE FootScreening (
	patient_id INT NOT NULL,
	date_recorded DATE NOT NULL,
	risk_category VARCHAR(50) NOT NULL,
	updated_by VARCHAR(50) NOT NULL,
	clinic_id INT NOT NULL,
	FOREIGN KEY (patient_id) REFERENCES Patient (patient_id) 
	ON DELETE CASCADE, 
	FOREIGN KEY (risk_category) REFERENCES FootExamRiskDefinition (risk_category),
	FOREIGN KEY (updated_by) REFERENCES User (user_name),
	FOREIGN KEY (clinic_id) REFERENCES Clinic (clinic_id),
	CONSTRAINT PK_FootScreening_patient_id_date_recorded 
	PRIMARY KEY (patient_id, date_recorded)	
) ENGINE=InnoDB;

CREATE TABLE TelephoneFollowUpDefinition (
	follow_up_code VARCHAR(50) NOT NULL,
	definition VARCHAR(50) NOT NULL,
	CONSTRAINT PK_TelephoneFollowUpDefinition_follow_up_code 
	PRIMARY KEY (follow_up_code)	
) ENGINE=InnoDB;

CREATE TABLE TelephoneFollowUp (
	patient_id INT NOT NULL,
	date_recorded DATE NOT NULL,
	follow_up_code VARCHAR(50) NOT NULL,
	updated_by VARCHAR(50) NOT NULL,
	clinic_id INT NOT NULL,
	FOREIGN KEY (patient_id) REFERENCES Patient (patient_id) 
	ON DELETE CASCADE, 
	FOREIGN KEY (follow_up_code) REFERENCES TelephoneFollowUpDefinition (follow_up_code),
	FOREIGN KEY (updated_by) REFERENCES User (user_name),
	FOREIGN KEY (clinic_id) REFERENCES Clinic (clinic_id),
	CONSTRAINT PK_TelephoneFollowUp_patient_id_date_recorded 
	PRIMARY KEY (patient_id, date_recorded)	
) ENGINE=InnoDB;

CREATE TABLE PHQ9 (
	phq_score INT NOT NULL,
	severity VARCHAR(50) NOT NULL,
	proposed_actions VARCHAR(500) NOT NULL,
	CONSTRAINT PK_PHQ9_phq_score 
	PRIMARY KEY (phq_score)	
) ENGINE=InnoDB;

CREATE TABLE PsychologicalScreening (
	patient_id INT NOT NULL,
	date_recorded DATE NOT NULL,
	phq_score INT NOT NULL,
	updated_by VARCHAR(50) NOT NULL,
	clinic_id INT NOT NULL,
	FOREIGN KEY (patient_id) REFERENCES Patient (patient_id) 
	ON DELETE CASCADE, 
	FOREIGN KEY (phq_score) REFERENCES PHQ9 (phq_score),
	FOREIGN KEY (updated_by) REFERENCES User (user_name),
	FOREIGN KEY (clinic_id) REFERENCES Clinic (clinic_id),
	CONSTRAINT PK_PsychologicalScreening_patient_id_date_recorded 
	PRIMARY KEY (patient_id, date_recorded)	
) ENGINE=InnoDB;

CREATE TABLE PhysicalActivity (
	patient_id INT NOT NULL,
	date_recorded DATE NOT NULL,
	min_per_week INT NOT NULL,
	updated_by VARCHAR(50) NOT NULL,
	clinic_id INT NOT NULL,
	FOREIGN KEY (patient_id) REFERENCES Patient (patient_id) 
	ON DELETE CASCADE, 
	FOREIGN KEY (updated_by) REFERENCES User (user_name),
	FOREIGN KEY (clinic_id) REFERENCES Clinic (clinic_id),
	CONSTRAINT PK_PhysicalActivity_patient_id_date_recorded 
	PRIMARY KEY (patient_id, date_recorded)	
) ENGINE=InnoDB;

CREATE TABLE ER (
	patient_id INT NOT NULL,
	date_recorded DATE NOT NULL,
	updated_by VARCHAR(50) NOT NULL,
	clinic_id INT NOT NULL,
	FOREIGN KEY (patient_id) REFERENCES Patient (patient_id) 
	ON DELETE CASCADE, 
	FOREIGN KEY (updated_by) REFERENCES User (user_name),
	FOREIGN KEY (clinic_id) REFERENCES Clinic (clinic_id),
	CONSTRAINT PK_ER_patient_id_date_recorded 
	PRIMARY KEY (patient_id, date_recorded)	
) ENGINE=InnoDB;

CREATE TABLE SmokingCessation (
	patient_id INT NOT NULL,
	date_recorded DATE NOT NULL,
	smoker TINYINT(1) NOT NULL,
	updated_by VARCHAR(50) NOT NULL,
	clinic_id INT NOT NULL,
	FOREIGN KEY (patient_id) REFERENCES Patient (patient_id) 
	ON DELETE CASCADE, 
	FOREIGN KEY (updated_by) REFERENCES User (user_name),
	FOREIGN KEY (clinic_id) REFERENCES Clinic (clinic_id),
	CONSTRAINT PK_SmokingCessation_patient_id_date_recorded 
	PRIMARY KEY (patient_id, date_recorded)	
) ENGINE=InnoDB;

CREATE TABLE AST (
	patient_id INT NOT NULL,
	date_recorded DATE NOT NULL,
	result DECIMAL(7,2) NOT NULL,
	updated_by VARCHAR(50) NOT NULL,
	clinic_id INT NOT NULL,
	FOREIGN KEY (patient_id) REFERENCES Patient (patient_id) 
	ON DELETE CASCADE, 
	FOREIGN KEY (updated_by) REFERENCES User (user_name),
	FOREIGN KEY (clinic_id) REFERENCES Clinic (clinic_id),
	CONSTRAINT PK_AST_patient_id_date_recorded 
	PRIMARY KEY (patient_id, date_recorded)	
) ENGINE=InnoDB;

CREATE TABLE ALT (
	patient_id INT NOT NULL,
	date_recorded DATE NOT NULL,
	result DECIMAL(7,2) NOT NULL,
	updated_by VARCHAR(50) NOT NULL,
	clinic_id INT NOT NULL,
	FOREIGN KEY (patient_id) REFERENCES Patient (patient_id) 
	ON DELETE CASCADE, 
	FOREIGN KEY (updated_by) REFERENCES User (user_name),
	FOREIGN KEY (clinic_id) REFERENCES Clinic (clinic_id),
	CONSTRAINT PK_ALT_patient_id_date_recorded 
	PRIMARY KEY (patient_id, date_recorded)	
) ENGINE=InnoDB;

CREATE TABLE PSA (
	patient_id INT NOT NULL,
	date_recorded DATE NOT NULL,
	result DECIMAL(7,2) NOT NULL,
	updated_by VARCHAR(50) NOT NULL,
	clinic_id INT NOT NULL,
	FOREIGN KEY (patient_id) REFERENCES Patient (patient_id) 
	ON DELETE CASCADE, 
	FOREIGN KEY (updated_by) REFERENCES User (user_name),
	FOREIGN KEY (clinic_id) REFERENCES Clinic (clinic_id),
	CONSTRAINT PK_PSA_patient_id_date_recorded 
	PRIMARY KEY (patient_id, date_recorded)	
) ENGINE=InnoDB;

CREATE TABLE InfluenzaVaccine (
	patient_id INT NOT NULL,
	date_recorded DATE NOT NULL,
	updated_by VARCHAR(50) NOT NULL,
	clinic_id INT NOT NULL,
	FOREIGN KEY (patient_id) REFERENCES Patient (patient_id) 
	ON DELETE CASCADE, 
	FOREIGN KEY (updated_by) REFERENCES User (user_name),
	FOREIGN KEY (clinic_id) REFERENCES Clinic (clinic_id),
	CONSTRAINT PK_InfluenzaVaccine_patient_id_date_recorded 
	PRIMARY KEY (patient_id, date_recorded)	
) ENGINE=InnoDB;

CREATE TABLE PCV13 (
	patient_id INT NOT NULL,
	date_recorded DATE NOT NULL,
	updated_by VARCHAR(50) NOT NULL,
	clinic_id INT NOT NULL,
	FOREIGN KEY (patient_id) REFERENCES Patient (patient_id) 
	ON DELETE CASCADE, 
	FOREIGN KEY (updated_by) REFERENCES User (user_name),
	FOREIGN KEY (clinic_id) REFERENCES Clinic (clinic_id),
	CONSTRAINT PK_PCV13_patient_id_date_recorded 
	PRIMARY KEY (patient_id, date_recorded)	
) ENGINE=InnoDB;

CREATE TABLE PPSV23 (
	patient_id INT NOT NULL,
	date_recorded DATE NOT NULL,
	updated_by VARCHAR(50) NOT NULL,
	clinic_id INT NOT NULL,
	FOREIGN KEY (patient_id) REFERENCES Patient (patient_id) 
	ON DELETE CASCADE, 
	FOREIGN KEY (updated_by) REFERENCES User (user_name),
	FOREIGN KEY (clinic_id) REFERENCES Clinic (clinic_id),
	CONSTRAINT PK_PPSV23_patient_id_date_recorded 
	PRIMARY KEY (patient_id, date_recorded)	
) ENGINE=InnoDB;

CREATE TABLE TDAP (
	patient_id INT NOT NULL,
	date_recorded DATE NOT NULL,
	updated_by VARCHAR(50) NOT NULL,
	clinic_id INT NOT NULL,
	FOREIGN KEY (patient_id) REFERENCES Patient (patient_id) 
	ON DELETE CASCADE, 
	FOREIGN KEY (updated_by) REFERENCES User (user_name),
	FOREIGN KEY (clinic_id) REFERENCES Clinic (clinic_id),
	CONSTRAINT PK_DTAP_patient_id_date_recorded 
	PRIMARY KEY (patient_id, date_recorded)	
) ENGINE=InnoDB;

CREATE TABLE HepatitisB (
	patient_id INT NOT NULL,
	date_recorded DATE NOT NULL,
	updated_by VARCHAR(50) NOT NULL,
	clinic_id INT NOT NULL,
	FOREIGN KEY (patient_id) REFERENCES Patient (patient_id) 
	ON DELETE CASCADE, 
	FOREIGN KEY (updated_by) REFERENCES User (user_name),
	FOREIGN KEY (clinic_id) REFERENCES Clinic (clinic_id),
	CONSTRAINT PK_HepatitisB_patient_id_date_recorded 
	PRIMARY KEY (patient_id, date_recorded)	
) ENGINE=InnoDB;

CREATE TABLE Zoster (
	patient_id INT NOT NULL,
	date_recorded DATE NOT NULL,
	updated_by VARCHAR(50) NOT NULL,
	clinic_id INT NOT NULL,
	FOREIGN KEY (patient_id) REFERENCES Patient (patient_id) 
	ON DELETE CASCADE, 
	FOREIGN KEY (updated_by) REFERENCES User (user_name),
	FOREIGN KEY (clinic_id) REFERENCES Clinic (clinic_id),
	CONSTRAINT PK_Zoster_patient_id_date_recorded 
	PRIMARY KEY (patient_id, date_recorded)	
) ENGINE=InnoDB;

CREATE TABLE HealthyTarget (
	measurement VARCHAR(50) NOT NULL,
	lower_target DECIMAL(8,2) NOT NULL,
	upper_target DECIMAL(8,2) NOT NULL,
	CONSTRAINT PK_HealthyTarget_measurement 
	PRIMARY KEY (measurement)
) ENGINE=InnoDB;

CREATE TABLE HealthyTargetStatus (
	patient_id INT NOT NULL,
	measurement VARCHAR(50) NOT NULL,
	out_of_target TINYINT(1) NOT NULL,
	FOREIGN KEY (patient_id) REFERENCES Patient (patient_id) 
	ON DELETE CASCADE, 
	FOREIGN KEY (measurement) REFERENCES HealthyTarget (measurement),
	CONSTRAINT PK_HealthyTargetStatus_patient_id_measurement 
	PRIMARY KEY (patient_id, measurement)
) ENGINE=InnoDB;

CREATE TABLE NoteTopic (
	topic VARCHAR(50) NOT NULL, 
	PRIMARY KEY (topic) 
) ENGINE=InnoDB;

CREATE TABLE Note (
	patient_id INT NOT NULL,
	date_recorded DATE NOT NULL,
	topic VARCHAR(50) NOT NULL,
	note VARCHAR(1000) NOT NULL, 
	updated_by VARCHAR(50) NOT NULL,
	clinic_id INT NOT NULL,
	FOREIGN KEY (patient_id) REFERENCES Patient (patient_id) 
	ON DELETE CASCADE, 
	FOREIGN KEY (topic) REFERENCES NoteTopic (topic), 
	FOREIGN KEY (updated_by) REFERENCES User (user_name),
	FOREIGN KEY (clinic_id) REFERENCES Clinic (clinic_id)
) ENGINE=InnoDB;

CREATE TABLE ProgressNote (
	progress_note_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, 
	patient_id INT NOT NULL, 
	date_created DATE NOT NULL, 
	medical_insurance TINYINT(1), 
	shoe_size VARCHAR(50), 
	allergic_to_medications TINYINT(1), 
	allergies VARCHAR(50), 
	weight DECIMAL(7,2), 
	height_feet INT, 
	height_inches INT, 
	weight_reduction_goal DECIMAL(7,2), 
	pulse INT, 
	respirations INT, 
	temperature DECIMAL(7,2), 
	foot_screening TINYINT(1),
	medications VARCHAR(50), 
	a1c DECIMAL(7,2), 
	glucose DECIMAL(7,2), 
	nurse_or_dietitian_note VARBINARY(1500), 
	subjective VARBINARY(1500), 
	objective VARBINARY(1500), 
	assessment VARBINARY(1500), 
	plan VARBINARY(1500), 
	FOREIGN KEY (patient_id) REFERENCES Patient (patient_id) 
	ON DELETE CASCADE,  
	CONSTRAINT UK_ProgressNote_patient_id_date_created 
	UNIQUE KEY (patient_id, date_created)
) ENGINE=InnoDB;

CREATE TABLE ProgressNoteAuthor (
	progress_note_id INT NOT NULL, 
	datetime_recorded DATETIME NOT NULL, 
	updated_by VARCHAR(50) NOT NULL, 
	FOREIGN KEY (progress_note_id) REFERENCES ProgressNote (progress_note_id) 
	ON DELETE CASCADE,  
	FOREIGN KEY (updated_by) REFERENCES User (user_name),
	CONSTRAINT PK_ProgressNoteAuthor_note_id_datetime_recorded_updated_by 
	PRIMARY KEY (progress_note_id, datetime_recorded, updated_by)
) ENGINE=InnoDB;

CREATE TABLE Compliance (
	patient_id INT NOT NULL,
	date_recorded DATE NOT NULL,
	result DECIMAL(7,2) NOT NULL, 
	updated_by VARCHAR(50) NOT NULL,
	clinic_id INT NOT NULL,
	FOREIGN KEY (patient_id) REFERENCES Patient (patient_id) 
	ON DELETE CASCADE,  
	FOREIGN KEY (updated_by) REFERENCES User (user_name),
	FOREIGN KEY (clinic_id) REFERENCES Clinic (clinic_id), 
	CONSTRAINT PK_Compliance_patient_id_date_recorded 
	PRIMARY KEY (patient_id, date_recorded)
) ENGINE=InnoDB;

CREATE TABLE Quality(
	role VARCHAR(50) NOT NULL,
	responsibility VARCHAR(255) NOT NULL, 
	active TINYINT(1) NOT NULL, 
	CONSTRAINT PK_Quality_responsibility 
	PRIMARY KEY (responsibility)
) ENGINE=InnoDB;

CREATE TABLE QualityChecklist (
	patient_id INT NOT NULL, 
	date_recorded DATE NOT NULL, 
	responsibility VARCHAR(255) NOT NULL, 
	updated_by VARCHAR(50) NOT NULL,
	clinic_id INT NOT NULL,
	FOREIGN KEY (patient_id) REFERENCES Patient (patient_id) 
	ON DELETE CASCADE,  
	FOREIGN KEY (updated_by) REFERENCES User (user_name),
	FOREIGN KEY (clinic_id) REFERENCES Clinic (clinic_id), 
	FOREIGN KEY (responsibility) REFERENCES Quality (responsibility), 
	CONSTRAINT PK_QualityChecklist_patient_id_date_recorded_responsibility 
	PRIMARY KEY (patient_id, date_recorded, responsibility)
) ENGINE=InnoDB;

/**
* USAGE: To set the values for the Healthy Target Status table
*CALL setTargetStatus 
*	(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 
*	?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);
*1 = patient_id
*2 = a1c_result
*3 = glucoseac_result
*4 = glucosepc_result
*5 = ldl_result
*6 = ldlpostmi_result
*7 = hdl_result
*8 = triglycerides_result
*9 = tsh_result
*10 = t4_result
*11 = uacr_result
*12 = egfr_result
*13 = creatinine_result
*14 = bmi_result
*15 = waist_result
*16 = bloodpressuresystole_result
*17 = bloodpressurediastole_result
*18 = physicalactivity_result
*19 = smoking_result
*20 = telephonefollowup_result
*21 = ast_result
*22 = alt_result
*23 = psa_result
*24 = date
*25 = string
*26 = proc_success
*/

DELIMITER //
CREATE PROCEDURE setTargetStatus
	(IN patient_id_in INT, IN a1c_result_in DECIMAL(7,2), IN glucoseac_result_in DECIMAL(7,2),
	IN glucosepc_result_in DECIMAL(7,2), IN ldl_result_in DECIMAL(7,2), 
	IN ldlpostmi_result_in DECIMAL(7,2), IN hdl_result_in DECIMAL(7,2), 
	IN triglycerides_result_in DECIMAL(7,2), IN tsh_result_in DECIMAL(7,2), 
	IN t4_result_in DECIMAL(7,2), IN uacr_result_in DECIMAL(7,2), IN egfr_result_in DECIMAL(7,2), 
	IN creatinine_result_in DECIMAL(7,2), IN bmi_result_in DECIMAL(7,2), 
	IN waist_result_in DECIMAL(7,2), IN bloodpressuresystole_result_in INT, 
	IN bloodpressurediastole_result_in INT, 
	IN physicalactivity_result_in INT, IN smoking_result_in TINYINT(1), 
	IN telephonefollowup_result_in TINYINT(1),
	IN ast_result_in DECIMAL(7,2), IN alt_result_in DECIMAL(7,2),
	IN psa_result_in DECIMAL(7,2), IN date_in DATE, IN string_in VARCHAR(64), 
	OUT proc_success TINYINT(1))
BEGIN
DECLARE a1c_count INT DEFAULT 0;
DECLARE glucoseac_count INT DEFAULT 0;
DECLARE glucosepc_count INT DEFAULT 0;
DECLARE lastglucose_count INT DEFAULT 0;
DECLARE ldl_count INT DEFAULT 0;
DECLARE ldlpostmi_count INT DEFAULT 0;
DECLARE hdlmale_count INT DEFAULT 0;
DECLARE hdlfemale_count INT DEFAULT 0;
DECLARE triglycerides_count INT DEFAULT 0;
DECLARE tsh_count INT DEFAULT 0;
DECLARE t4_count INT DEFAULT 0;
DECLARE uacr_count INT DEFAULT 0;
DECLARE egfr_count INT DEFAULT 0;
DECLARE creatinine_count INT DEFAULT 0;
DECLARE bmi_count INT DEFAULT 0;
DECLARE waistmale_count INT DEFAULT 0;
DECLARE waistfemale_count INT DEFAULT 0;
DECLARE bloodpressuresystole_count INT DEFAULT 0;
DECLARE bloodpressurediastole_count INT DEFAULT 0;
DECLARE class_count INT DEFAULT 0;
DECLARE class_value INT DEFAULT 0;
DECLARE eye_count INT DEFAULT 0;
DECLARE eye_value INT DEFAULT 0;
DECLARE foot_count INT DEFAULT 0;
DECLARE foot_value INT DEFAULT 0;
DECLARE psychologicalscreening_count INT DEFAULT 0;
DECLARE psychologicalscreening_value INT DEFAULT 0;
DECLARE physicalactivity_count INT DEFAULT 0;
DECLARE influenzavaccine_count INT DEFAULT 0;
DECLARE influenzavaccine_value INT DEFAULT -1;
DECLARE pcv13_count INT DEFAULT 0;
DECLARE pcv13_value INT DEFAULT -1;
DECLARE ppsv23_count INT DEFAULT 0;
DECLARE ppsv23_value INT DEFAULT -1;
DECLARE hepatitisb_count INT DEFAULT 0;
DECLARE hepatitisb_value INT DEFAULT -1;
DECLARE tdap_count INT DEFAULT 0;
DECLARE tdap_value INT DEFAULT -1;
DECLARE zoster_count INT DEFAULT 0;
DECLARE zoster_value INT DEFAULT -1;
DECLARE smoking_count INT DEFAULT 0;
DECLARE telephonefollowup_count INT DEFAULT 0;
DECLARE ast_count INT DEFAULT 0;
DECLARE alt_count INT DEFAULT 0;
DECLARE psa_count INT DEFAULT 0;
DECLARE hospitalization_count INT DEFAULT 0;
DECLARE hospitalization_value INT DEFAULT 0;
DECLARE out_t INT DEFAULT 0;
DECLARE high DECIMAL(7, 2) DEFAULT 0;
DECLARE low DECIMAL(7, 2) DEFAULT 0;
DECLARE gender_in VARCHAR(50);
DECLARE birthday DATE;
DECLARE today DATE DEFAULT DATE(NOW());
DECLARE sixty DATE;
DECLARE low_date DATE;
DECLARE measurement_count INT DEFAULT 0;
DECLARE max_date DATE DEFAULT NULL;

DECLARE EXIT HANDLER FOR SQLEXCEPTION ROLLBACK;
DECLARE EXIT HANDLER FOR SQLWARNING ROLLBACK;

START TRANSACTION;

SET proc_success = 0;

SELECT today - INTERVAL 60 YEAR INTO sixty;

SELECT (CAST(AES_DECRYPT(p.birth_date, string_in) AS DATE)) INTO birthday FROM Patient p 
WHERE p.patient_id = patient_id_in;

SELECT (CAST(AES_DECRYPT(p.gender, string_in) AS CHAR(50))) INTO gender_in FROM Patient p
WHERE p.patient_id = patient_id_in;

/*Setting the value for A1C*/

IF a1c_result_in IS NOT NULL THEN 
	SELECT COUNT(*) INTO measurement_count 
	FROM A1C a 
	WHERE a.patient_id = patient_id_in;
	IF measurement_count > 0 THEN 
	SELECT MAX(a.date_recorded) INTO max_date 
	FROM A1C a 
	WHERE a.patient_id = patient_id_in;
		END IF;
/*assuring only the latest measurment is processed*/
	IF measurement_count = 0 OR 
	(max_date IS NOT NULL AND date_in >= max_date) THEN 
	SELECT COUNT(*) INTO a1c_count 
	FROM HealthyTargetStatus hts
	WHERE hts.patient_id = patient_id_in 
	AND hts.measurement = 'a1c';
	SELECT ht.upper_target INTO high 
	FROM HealthyTarget ht
	WHERE ht.measurement = 'a1c';
	SELECT ht.lower_target INTO low
	FROM HealthyTarget ht
	WHERE ht.measurement = 'a1c';
	IF a1c_result_in < low OR 
		a1c_result_in > high THEN
		SET out_t = 1;
		ELSE SET out_t = 0;
		END IF;
	IF a1c_count > 0 THEN		
		UPDATE HealthyTargetStatus hts SET 
		hts.out_of_target = out_t
		WHERE hts.patient_id = patient_id_in 
		AND hts.measurement = 'a1c';
		ELSE
		INSERT INTO HealthyTargetStatus 
		VALUES (patient_id_in, 'a1c', out_t);
		END IF;
/*returning measurment count and max date to defaults*/
	SET measurement_count = 0;
	SET max_date = NULL;
	END IF;
END IF;

/*Setting the value for glucose ac and setting fasting to true*/

IF glucoseac_result_in IS NOT NULL THEN 
	SELECT COUNT(*) INTO measurement_count 
	FROM Glucose g 
	WHERE g.patient_id = patient_id_in;
	IF measurement_count > 0 THEN 
	SELECT MAX(g.date_recorded) INTO max_date 
	FROM Glucose g 
	WHERE g.patient_id = patient_id_in;
		END IF;
/*assuring only the latest measurment is processed*/
	IF measurement_count = 0 OR 
	(max_date IS NOT NULL AND date_in >= max_date) THEN 
	SELECT COUNT(*) INTO glucoseac_count 
	FROM HealthyTargetStatus hts
	WHERE hts.patient_id = patient_id_in 
	AND hts.measurement = 'glucoseac';
	SELECT ht.upper_target INTO high 
	FROM HealthyTarget ht
	WHERE ht.measurement = 'glucoseac';
	SELECT ht.lower_target INTO low
	FROM HealthyTarget ht
	WHERE ht.measurement = 'glucoseac';
	IF glucoseac_result_in < low OR 
		glucoseac_result_in > high THEN
		SET out_t = 1;
		ELSE SET out_t = 0;
		END IF;
	IF glucoseac_count > 0 THEN		
		UPDATE HealthyTargetStatus hts SET 
		hts.out_of_target = out_t
		WHERE hts.patient_id = patient_id_in 
		AND hts.measurement = 'glucoseac';
		ELSE
		INSERT INTO HealthyTargetStatus 
		VALUES (patient_id_in, 'glucoseac', out_t);
		END IF;
	SELECT COUNT(*) INTO lastglucose_count 
	FROM LastGlucoseFasting lgf 
	WHERE lgf.patient_id = patient_id_in; 
	IF lastglucose_count > 0 THEN 
		UPDATE LastGlucoseFasting lgf SET 
		lgf.ac = 1 
		WHERE lgf.patient_id = patient_id_in; 
		ELSE 
		INSERT INTO LastGlucoseFasting 
		VALUES (patient_id_in, 1);
		END IF;
/*returning measurment count and max date to defaults*/
	SET measurement_count = 0;
	SET max_date = NULL;
	END IF;
END IF;

/*Setting the value for glucose pc and setting fasting to false*/

IF glucosepc_result_in IS NOT NULL THEN 
	SELECT COUNT(*) INTO measurement_count 
	FROM Glucose g 
	WHERE g.patient_id = patient_id_in;
	IF measurement_count > 0 THEN 
	SELECT MAX(g.date_recorded) INTO max_date 
	FROM Glucose g 
	WHERE g.patient_id = patient_id_in;
		END IF;
/*assuring only the latest measurment is processed*/
	IF measurement_count = 0 OR 
	(max_date IS NOT NULL AND date_in >= max_date) THEN 
	SELECT COUNT(*) INTO glucosepc_count 
	FROM HealthyTargetStatus hts
	WHERE hts.patient_id = patient_id_in 
	AND hts.measurement = 'glucosepc';
	SELECT ht.upper_target INTO high 
	FROM HealthyTarget ht
	WHERE ht.measurement = 'glucosepc';
	SELECT ht.lower_target INTO low
	FROM HealthyTarget ht
	WHERE ht.measurement = 'glucosepc';
	IF glucosepc_result_in < low OR 
		glucosepc_result_in > high THEN
		SET out_t = 1;
		ELSE SET out_t = 0;
		END IF;
	IF glucosepc_count > 0 THEN		
		UPDATE HealthyTargetStatus hts SET 
		hts.out_of_target = out_t
		WHERE hts.patient_id = patient_id_in 
		AND hts.measurement = 'glucosepc';
		ELSE
		INSERT INTO HealthyTargetStatus 
		VALUES (patient_id_in, 'glucosepc', out_t);
		END IF;
	SELECT COUNT(*) INTO lastglucose_count 
	FROM LastGlucoseFasting lgf 
	WHERE lgf.patient_id = patient_id_in; 
	IF lastglucose_count > 0 THEN 
		UPDATE LastGlucoseFasting lgf SET 
		lgf.ac = 0 
		WHERE lgf.patient_id = patient_id_in; 
		ELSE 
		INSERT INTO LastGlucoseFasting 
		VALUES (patient_id_in, 0);
		END IF;
/*returning measurment count and max date to defaults*/
	SET measurement_count = 0;
	SET max_date = NULL;
	END IF;
END IF;

/*Setting the value for LDL*/

IF ldl_result_in IS NOT NULL THEN 
	SELECT COUNT(*) INTO measurement_count 
	FROM LDL l 
	WHERE l.patient_id = patient_id_in;
	IF measurement_count > 0 THEN 
	SELECT MAX(l.date_recorded) INTO max_date 
	FROM LDL l 
	WHERE l.patient_id = patient_id_in;
		END IF;
/*assuring only the latest measurment is processed*/
	IF measurement_count = 0 OR 
		(max_date IS NOT NULL AND date_in >= max_date) THEN 
		SELECT COUNT(*) INTO ldl_count 
		FROM HealthyTargetStatus hts
		WHERE hts.patient_id = patient_id_in 
		AND hts.measurement = 'ldl';
		SELECT ht.upper_target INTO high 
		FROM HealthyTarget ht
		WHERE ht.measurement = 'ldl';
		SELECT ht.lower_target INTO low
		FROM HealthyTarget ht
		WHERE ht.measurement = 'ldl';
		IF ldl_result_in < low OR 
			ldl_result_in > high THEN
			SET out_t = 1;
			ELSE SET out_t = 0;
			END IF;
		IF ldl_count > 0 THEN		
			UPDATE HealthyTargetStatus hts SET 
			hts.out_of_target = out_t
			WHERE hts.patient_id = patient_id_in 
			AND hts.measurement = 'ldl';
		ELSE
			INSERT INTO HealthyTargetStatus 
			VALUES (patient_id_in, 'ldl', out_t);
			END IF;
/*returning measurment count and max date to defaults*/
		SET measurement_count = 0;
		SET max_date = NULL;
		END IF;
END IF;

/*Setting the value for LDL post MI*/

IF ldlpostmi_result_in IS NOT NULL THEN 
	SELECT COUNT(*) INTO measurement_count 
	FROM LDL l 
	WHERE l.patient_id = patient_id_in;
	IF measurement_count > 0 THEN 
	SELECT MAX(l.date_recorded) INTO max_date 
	FROM LDL l 
	WHERE l.patient_id = patient_id_in;
		END IF;
/*assuring only the latest measurment is processed*/
	IF measurement_count = 0 OR 
	(max_date IS NOT NULL AND date_in >= max_date) THEN 
	SELECT COUNT(*) INTO ldlpostmi_count 
	FROM HealthyTargetStatus hts
	WHERE hts.patient_id = patient_id_in 
	AND hts.measurement = 'ldlpostmi';
	SELECT ht.upper_target INTO high 
	FROM HealthyTarget ht
	WHERE ht.measurement = 'ldlpostmi';
	SELECT ht.lower_target INTO low
	FROM HealthyTarget ht
	WHERE ht.measurement = 'ldlpostmi';
	IF ldlpostmi_result_in < low OR 
		ldlpostmi_result_in > high THEN
		SET out_t = 1;
		ELSE SET out_t = 0;
		END IF;
	IF ldlpostmi_count > 0 THEN		
		UPDATE HealthyTargetStatus hts SET 
		hts.out_of_target = out_t
		WHERE hts.patient_id = patient_id_in 
		AND hts.measurement = 'ldlpostmi';
		ELSE
		INSERT INTO HealthyTargetStatus 
		VALUES (patient_id_in, 'ldlpostmi', out_t);
		END IF;
/*returning measurment count and max date to defaults*/
	SET measurement_count = 0;
	SET max_date = NULL;
	END IF;
END IF;

/*Setting the value for HDL*/

IF hdl_result_in IS NOT NULL THEN 
	SELECT COUNT(*) INTO measurement_count 
	FROM HDL h 
	WHERE h.patient_id = patient_id_in;
	IF measurement_count > 0 THEN 
	SELECT MAX(h.date_recorded) INTO max_date 
	FROM HDL h 
	WHERE h.patient_id = patient_id_in;
		END IF;
/*assuring only the latest measurment is processed*/
	IF measurement_count = 0 OR 
	(max_date IS NOT NULL AND date_in >= max_date) THEN 
	IF gender_in = 'Male' THEN 	
	SELECT COUNT(*) INTO hdlmale_count 
	FROM HealthyTargetStatus hts
	WHERE hts.patient_id = patient_id_in 
	AND hts.measurement = 'hdlmale';
	SELECT ht.upper_target INTO high 
	FROM HealthyTarget ht
	WHERE ht.measurement = 'hdlmale';
	SELECT ht.lower_target INTO low
	FROM HealthyTarget ht
	WHERE ht.measurement = 'hdlmale';
	IF hdl_result_in < low OR 
		hdl_result_in > high THEN
		SET out_t = 1;
		ELSE SET out_t = 0;
		END IF;
	IF hdlmale_count > 0 THEN		
		UPDATE HealthyTargetStatus hts SET 
		hts.out_of_target = out_t
		WHERE hts.patient_id = patient_id_in 
		AND hts.measurement = 'hdlmale';
		ELSE
		INSERT INTO HealthyTargetStatus 
		VALUES (patient_id_in, 'hdlmale', out_t);
		END IF;
	END IF;
	IF gender_in = 'Female' THEN 	
	SELECT COUNT(*) INTO hdlfemale_count 
	FROM HealthyTargetStatus hts
	WHERE hts.patient_id = patient_id_in 
	AND hts.measurement = 'hdlfemale';
	SELECT ht.upper_target INTO high 
	FROM HealthyTarget ht
	WHERE ht.measurement = 'hdlfemale';
	SELECT ht.lower_target INTO low
	FROM HealthyTarget ht
	WHERE ht.measurement = 'hdlfemale';
	IF hdl_result_in < low OR 
		hdl_result_in > high THEN
		SET out_t = 1;
		ELSE SET out_t = 0;
		END IF;
	IF hdlfemale_count > 0 THEN		
		UPDATE HealthyTargetStatus hts SET 
		hts.out_of_target = out_t
		WHERE hts.patient_id = patient_id_in 
		AND hts.measurement = 'hdlfemale';
		ELSE
		INSERT INTO HealthyTargetStatus 
		VALUES (patient_id_in, 'hdlfemale', out_t);
		END IF;
	END IF;
/*returning measurment count and max date to defaults*/
	SET measurement_count = 0;
	SET max_date = NULL;
	END IF;
END IF;

/*Setting the value for triglycerides*/

IF triglycerides_result_in IS NOT NULL THEN 
	SELECT COUNT(*) INTO measurement_count 
	FROM Triglycerides t 
	WHERE t.patient_id = patient_id_in;
	IF measurement_count > 0 THEN 
	SELECT MAX(t.date_recorded) INTO max_date 
	FROM Triglycerides t 
	WHERE t.patient_id = patient_id_in;
		END IF;
/*assuring only the latest measurment is processed*/
	IF measurement_count = 0 OR 
	(max_date IS NOT NULL AND date_in >= max_date) THEN 
	SELECT COUNT(*) INTO triglycerides_count 
	FROM HealthyTargetStatus hts
	WHERE hts.patient_id = patient_id_in 
	AND hts.measurement = 'triglycerides';
	SELECT ht.upper_target INTO high 
	FROM HealthyTarget ht
	WHERE ht.measurement = 'triglycerides';
	SELECT ht.lower_target INTO low
	FROM HealthyTarget ht
	WHERE ht.measurement = 'triglycerides';
	IF triglycerides_result_in < low OR 
		triglycerides_result_in > high THEN
		SET out_t = 1;
		ELSE SET out_t = 0;
		END IF;
	IF triglycerides_count > 0 THEN		
		UPDATE HealthyTargetStatus hts SET 
		hts.out_of_target = out_t
		WHERE hts.patient_id = patient_id_in 
		AND hts.measurement = 'triglycerides';
		ELSE
		INSERT INTO HealthyTargetStatus 
		VALUES (patient_id_in, 'triglycerides', out_t);
		END IF;
/*returning measurment count and max date to defaults*/
	SET measurement_count = 0;
	SET max_date = NULL;
	END IF;
END IF;

/*Setting the value for TSH*/

IF tsh_result_in IS NOT NULL THEN 
	SELECT COUNT(*) INTO measurement_count 
	FROM TSH t 
	WHERE t.patient_id = patient_id_in;
	IF measurement_count > 0 THEN 
	SELECT MAX(t.date_recorded) INTO max_date 
	FROM TSH t 
	WHERE t.patient_id = patient_id_in;
		END IF;
/*assuring only the latest measurment is processed*/
	IF measurement_count = 0 OR 
	(max_date IS NOT NULL AND date_in >= max_date) THEN 
	SELECT COUNT(*) INTO tsh_count 
	FROM HealthyTargetStatus hts
	WHERE hts.patient_id = patient_id_in 
	AND hts.measurement = 'tsh';
	SELECT ht.upper_target INTO high 
	FROM HealthyTarget ht
	WHERE ht.measurement = 'tsh';
	SELECT ht.lower_target INTO low
	FROM HealthyTarget ht
	WHERE ht.measurement = 'tsh';
	IF tsh_result_in < low OR 
		tsh_result_in > high THEN
		SET out_t = 1;
		ELSE SET out_t = 0;
		END IF;
	IF tsh_count > 0 THEN		
		UPDATE HealthyTargetStatus hts SET 
		hts.out_of_target = out_t
		WHERE hts.patient_id = patient_id_in 
		AND hts.measurement = 'tsh';
		ELSE
		INSERT INTO HealthyTargetStatus 
		VALUES (patient_id_in, 'tsh', out_t);
		END IF;
/*returning measurment count and max date to defaults*/
	SET measurement_count = 0;
	SET max_date = NULL;
	END IF;
END IF;

/*Setting the value for T4*/

IF t4_result_in IS NOT NULL THEN 
	SELECT COUNT(*) INTO measurement_count 
	FROM T4 t 
	WHERE t.patient_id = patient_id_in;
	IF measurement_count > 0 THEN 
	SELECT MAX(t.date_recorded) INTO max_date 
	FROM T4 t 
	WHERE t.patient_id = patient_id_in;
		END IF;
/*assuring only the latest measurment is processed*/
	IF measurement_count = 0 OR 
	(max_date IS NOT NULL AND date_in >= max_date) THEN 
	SELECT COUNT(*) INTO t4_count 
	FROM HealthyTargetStatus hts
	WHERE hts.patient_id = patient_id_in 
	AND hts.measurement = 't4';
	SELECT ht.upper_target INTO high 
	FROM HealthyTarget ht
	WHERE ht.measurement = 't4';
	SELECT ht.lower_target INTO low
	FROM HealthyTarget ht
	WHERE ht.measurement = 't4';
	IF t4_result_in < low OR 
		t4_result_in > high THEN
		SET out_t = 1;
		ELSE SET out_t = 0;
		END IF;
	IF t4_count > 0 THEN		
		UPDATE HealthyTargetStatus hts SET 
		hts.out_of_target = out_t
		WHERE hts.patient_id = patient_id_in 
		AND hts.measurement = 't4';
		ELSE
		INSERT INTO HealthyTargetStatus 
		VALUES (patient_id_in, 't4', out_t);
		END IF;
/*returning measurment count and max date to defaults*/
	SET measurement_count = 0;
	SET max_date = NULL;
	END IF;
END IF;

/*Setting the value for UACR*/

IF uacr_result_in IS NOT NULL THEN 
	SELECT COUNT(*) INTO measurement_count 
	FROM UACR u 
	WHERE u.patient_id = patient_id_in;
	IF measurement_count > 0 THEN 
	SELECT MAX(u.date_recorded) INTO max_date 
	FROM UACR u 
	WHERE u.patient_id = patient_id_in;
		END IF;
/*assuring only the latest measurment is processed*/
	IF measurement_count = 0 OR 
	(max_date IS NOT NULL AND date_in >= max_date) THEN 
	SELECT COUNT(*) INTO uacr_count 
	FROM HealthyTargetStatus hts
	WHERE hts.patient_id = patient_id_in 
	AND hts.measurement = 'uacr';
	SELECT ht.upper_target INTO high 
	FROM HealthyTarget ht
	WHERE ht.measurement = 'uacr';
	SELECT ht.lower_target INTO low
	FROM HealthyTarget ht
	WHERE ht.measurement = 'uacr';
	IF uacr_result_in < low OR 
		uacr_result_in > high THEN
		SET out_t = 1;
		ELSE SET out_t = 0;
		END IF;
	IF uacr_count > 0 THEN		
		UPDATE HealthyTargetStatus hts SET 
		hts.out_of_target = out_t
		WHERE hts.patient_id = patient_id_in 
		AND hts.measurement = 'uacr';
		ELSE
		INSERT INTO HealthyTargetStatus 
		VALUES (patient_id_in, 'uacr', out_t);
		END IF;
/*returning measurment count and max date to defaults*/
	SET measurement_count = 0;
	SET max_date = NULL;
	END IF;
END IF;

/*Setting the value for eGFR*/

IF egfr_result_in IS NOT NULL THEN 
	SELECT COUNT(*) INTO measurement_count 
	FROM EGFR e 
	WHERE e.patient_id = patient_id_in;
	IF measurement_count > 0 THEN 
	SELECT MAX(e.date_recorded) INTO max_date 
	FROM EGFR e 
	WHERE e.patient_id = patient_id_in;
		END IF;
/*assuring only the latest measurment is processed*/
	IF measurement_count = 0 OR 
	(max_date IS NOT NULL AND date_in >= max_date) THEN 
	SELECT COUNT(*) INTO egfr_count 
	FROM HealthyTargetStatus hts
	WHERE hts.patient_id = patient_id_in 
	AND hts.measurement = 'egfr';
	SELECT ht.upper_target INTO high 
	FROM HealthyTarget ht
	WHERE ht.measurement = 'egfr';
	SELECT ht.lower_target INTO low
	FROM HealthyTarget ht
	WHERE ht.measurement = 'egfr';
	IF egfr_result_in < low OR 
		egfr_result_in > high THEN
		SET out_t = 1;
		ELSE SET out_t = 0;
		END IF;
	IF egfr_count > 0 THEN		
		UPDATE HealthyTargetStatus hts SET 
		hts.out_of_target = out_t
		WHERE hts.patient_id = patient_id_in 
		AND hts.measurement = 'egfr';
		ELSE
		INSERT INTO HealthyTargetStatus 
		VALUES (patient_id_in, 'egfr', out_t);
		END IF;
/*returning measurment count and max date to defaults*/
	SET measurement_count = 0;
	SET max_date = NULL;
	END IF;
END IF;

/*Setting the value for creatinine*/

IF creatinine_result_in IS NOT NULL THEN 
	SELECT COUNT(*) INTO measurement_count 
	FROM Creatinine c 
	WHERE c.patient_id = patient_id_in;
	IF measurement_count > 0 THEN 
	SELECT MAX(c.date_recorded) INTO max_date 
	FROM Creatinine c 
	WHERE c.patient_id = patient_id_in;
		END IF;
/*assuring only the latest measurment is processed*/
	IF measurement_count = 0 OR 
	(max_date IS NOT NULL AND date_in >= max_date) THEN 
	SELECT COUNT(*) INTO creatinine_count 
	FROM HealthyTargetStatus hts
	WHERE hts.patient_id = patient_id_in 
	AND hts.measurement = 'creatinine';
	SELECT ht.upper_target INTO high 
	FROM HealthyTarget ht
	WHERE ht.measurement = 'creatinine';
	SELECT ht.lower_target INTO low
	FROM HealthyTarget ht
	WHERE ht.measurement = 'creatinine';
	IF creatinine_result_in < low OR 
		creatinine_result_in > high THEN
		SET out_t = 1;
		ELSE SET out_t = 0;
		END IF;
	IF creatinine_count > 0 THEN		
		UPDATE HealthyTargetStatus hts SET 
		hts.out_of_target = out_t
		WHERE hts.patient_id = patient_id_in 
		AND hts.measurement = 'creatinine';
		ELSE
		INSERT INTO HealthyTargetStatus 
		VALUES (patient_id_in, 'creatinine', out_t);
		END IF;
/*returning measurment count and max date to defaults*/
	SET measurement_count = 0;
	SET max_date = NULL;
	END IF;
END IF;

/*Setting the value for bmi*/

IF bmi_result_in IS NOT NULL THEN 
	SELECT COUNT(*) INTO measurement_count 
	FROM BMI b 
	WHERE b.patient_id = patient_id_in;
	IF measurement_count > 0 THEN 
	SELECT MAX(b.date_recorded) INTO max_date 
	FROM BMI b 
	WHERE b.patient_id = patient_id_in;
		END IF;
/*assuring only the latest measurment is processed*/
	IF measurement_count = 0 OR 
	(max_date IS NOT NULL AND date_in >= max_date) THEN 
	SELECT COUNT(*) INTO bmi_count 
	FROM HealthyTargetStatus hts
	WHERE hts.patient_id = patient_id_in 
	AND hts.measurement = 'bmi';
	SELECT ht.upper_target INTO high 
	FROM HealthyTarget ht
	WHERE ht.measurement = 'bmi';
	SELECT ht.lower_target INTO low
	FROM HealthyTarget ht
	WHERE ht.measurement = 'bmi';
	IF bmi_result_in < low OR 
		bmi_result_in > high THEN
		SET out_t = 1;
		ELSE SET out_t = 0;
		END IF;
	IF bmi_count > 0 THEN		
		UPDATE HealthyTargetStatus hts SET 
		hts.out_of_target = out_t
		WHERE hts.patient_id = patient_id_in 
		AND hts.measurement = 'bmi';
		ELSE
		INSERT INTO HealthyTargetStatus 
		VALUES (patient_id_in, 'bmi', out_t);
		END IF;
/*returning measurment count and max date to defaults*/
	SET measurement_count = 0;
	SET max_date = NULL;
	END IF;
END IF;

/*Setting the value for waist measurement*/

IF waist_result_in IS NOT NULL THEN 
	SELECT COUNT(*) INTO measurement_count 
	FROM Waist w 
	WHERE w.patient_id = patient_id_in;
	IF measurement_count > 0 THEN 
	SELECT MAX(w.date_recorded) INTO max_date 
	FROM Waist w 
	WHERE w.patient_id = patient_id_in;
		END IF;
/*assuring only the latest measurment is processed*/
	IF measurement_count = 0 OR 
	(max_date IS NOT NULL AND date_in >= max_date) THEN 
	IF gender_in = 'Male' THEN
	SELECT COUNT(*) INTO waistmale_count 
	FROM HealthyTargetStatus hts
	WHERE hts.patient_id = patient_id_in 
	AND hts.measurement = 'waistmale';
	SELECT ht.upper_target INTO high 
	FROM HealthyTarget ht
	WHERE ht.measurement = 'waistmale';
	SELECT ht.lower_target INTO low
	FROM HealthyTarget ht
	WHERE ht.measurement = 'waistmale';
	IF waist_result_in < low OR 
		waist_result_in > high THEN
		SET out_t = 1;
		ELSE SET out_t = 0;
		END IF;
	IF waistmale_count > 0 THEN		
		UPDATE HealthyTargetStatus hts SET 
		hts.out_of_target = out_t
		WHERE hts.patient_id = patient_id_in 
		AND hts.measurement = 'waistmale';
		ELSE
		INSERT INTO HealthyTargetStatus 
		VALUES (patient_id_in, 'waistmale', out_t);
		END IF;
	END IF;
	IF gender_in = 'Female' THEN
	SELECT COUNT(*) INTO waistfemale_count 
	FROM HealthyTargetStatus hts
	WHERE hts.patient_id = patient_id_in 
	AND hts.measurement = 'waistfemale';
	SELECT ht.upper_target INTO high 
	FROM HealthyTarget ht
	WHERE ht.measurement = 'waistfemale';
	SELECT ht.lower_target INTO low
	FROM HealthyTarget ht
	WHERE ht.measurement = 'waistfemale';
	IF waist_result_in < low OR 
		waist_result_in > high THEN
		SET out_t = 1;
		ELSE SET out_t = 0;
		END IF;
	IF waistfemale_count > 0 THEN		
		UPDATE HealthyTargetStatus hts SET 
		hts.out_of_target = out_t
		WHERE hts.patient_id = patient_id_in 
		AND hts.measurement = 'waistfemale';
		ELSE
		INSERT INTO HealthyTargetStatus 
		VALUES (patient_id_in, 'waistfemale', out_t);
		END IF;
	END IF;
/*returning measurment count and max date to defaults*/
	SET measurement_count = 0;
	SET max_date = NULL;
	END IF;
END IF;

/*Setting the value for blood pressure systole*/

IF bloodpressuresystole_result_in IS NOT NULL THEN 
	SELECT COUNT(*) INTO measurement_count 
	FROM BloodPressure bp 
	WHERE bp.patient_id = patient_id_in;
	IF measurement_count > 0 THEN 
	SELECT MAX(bp.date_recorded) INTO max_date 
	FROM BloodPressure bp 
	WHERE bp.patient_id = patient_id_in;
		END IF;
/*assuring only the latest measurment is processed*/
	IF measurement_count = 0 OR 
	(max_date IS NOT NULL AND date_in >= max_date) THEN 
	SELECT COUNT(*) INTO bloodpressuresystole_count 
	FROM HealthyTargetStatus hts
	WHERE hts.patient_id = patient_id_in 
	AND hts.measurement = 'bloodpressuresystole';
	SELECT ht.upper_target INTO high 
	FROM HealthyTarget ht
	WHERE ht.measurement = 'bloodpressuresystole';
	SELECT ht.lower_target INTO low
	FROM HealthyTarget ht
	WHERE ht.measurement = 'bloodpressuresystole';
	IF bloodpressuresystole_result_in < low OR 
		bloodpressuresystole_result_in > high THEN
		SET out_t = 1;
		ELSE SET out_t = 0;
		END IF;
	IF bloodpressuresystole_count > 0 THEN		
		UPDATE HealthyTargetStatus hts SET 
		hts.out_of_target = out_t
		WHERE hts.patient_id = patient_id_in 
		AND hts.measurement = 'bloodpressuresystole';
		ELSE
		INSERT INTO HealthyTargetStatus 
		VALUES (patient_id_in, 'bloodpressuresystole', out_t);
		END IF;
/*returning measurment count and max date to defaults*/
	SET measurement_count = 0;
	SET max_date = NULL;
	END IF;
END IF;

/*Setting the value for blood pressure diastole*/

IF bloodpressurediastole_result_in IS NOT NULL THEN 
	SELECT COUNT(*) INTO measurement_count 
	FROM BloodPressure bp 
	WHERE bp.patient_id = patient_id_in;
	IF measurement_count > 0 THEN 
	SELECT MAX(bp.date_recorded) INTO max_date 
	FROM BloodPressure bp 
	WHERE bp.patient_id = patient_id_in;
		END IF;
/*assuring only the latest measurment is processed*/
	IF measurement_count = 0 OR 
	(max_date IS NOT NULL AND date_in >= max_date) THEN 
	SELECT COUNT(*) INTO bloodpressurediastole_count 
	FROM HealthyTargetStatus hts
	WHERE hts.patient_id = patient_id_in 
	AND hts.measurement = 'bloodpressurediastole';
	SELECT ht.upper_target INTO high 
	FROM HealthyTarget ht
	WHERE ht.measurement = 'bloodpressurediastole';
	SELECT ht.lower_target INTO low
	FROM HealthyTarget ht
	WHERE ht.measurement = 'bloodpressurediastole';
	IF bloodpressurediastole_result_in < low OR 
		bloodpressurediastole_result_in > high THEN
		SET out_t = 1;
		ELSE SET out_t = 0;
		END IF;
	IF bloodpressurediastole_count > 0 THEN		
		UPDATE HealthyTargetStatus hts SET 
		hts.out_of_target = out_t
		WHERE hts.patient_id = patient_id_in 
		AND hts.measurement = 'bloodpressurediastole';
		ELSE
		INSERT INTO HealthyTargetStatus 
		VALUES (patient_id_in, 'bloodpressurediastole', out_t);
		END IF;
/*returning measurment count and max date to defaults*/
	SET measurement_count = 0;
	SET max_date = NULL;
	END IF;
END IF;

/*Setting the value for the class attendance*/

SELECT COUNT(*) INTO class_count 
	FROM HealthyTargetStatus hts
	WHERE hts.patient_id = patient_id_in 
	AND hts.measurement = 'class';
	
SELECT COUNT(*) INTO class_value 
	FROM LastClass lc
	WHERE lc.patient_id = patient_id_in; 
	
IF class_value < 1 THEN
	SET out_t = 1;
	ELSE SET out_t = 0;
	END IF;
IF class_count > 0 THEN		
	UPDATE HealthyTargetStatus hts SET 
	hts.out_of_target = out_t
	WHERE hts.patient_id = patient_id_in 
	AND hts.measurement = 'class';
	ELSE
	INSERT INTO HealthyTargetStatus 
	VALUES (patient_id_in, 'class', out_t);
	END IF;

/*Setting the value for the eye screening*/

SELECT COUNT(*) INTO eye_count 
	FROM HealthyTargetStatus hts
	WHERE hts.patient_id = patient_id_in 
	AND hts.measurement = 'eye';

SELECT today - INTERVAL 2 YEAR INTO low_date;

SELECT COUNT(*) INTO eye_value 
	FROM EyeScreening e
	WHERE e.patient_id = patient_id_in 
	AND e.date_recorded > low_date;

IF eye_value < 1 THEN
	SET out_t = 1;
	ELSE SET out_t = 0;
	END IF;
IF eye_count > 0 THEN		
	UPDATE HealthyTargetStatus hts SET 
	hts.out_of_target = out_t
	WHERE hts.patient_id = patient_id_in 
	AND hts.measurement = 'eye';
	ELSE
	INSERT INTO HealthyTargetStatus 
	VALUES (patient_id_in, 'eye', out_t);
	END IF;

/*Setting the value for the foot screening*/

SELECT COUNT(*) INTO foot_count 
	FROM HealthyTargetStatus hts
	WHERE hts.patient_id = patient_id_in 
	AND hts.measurement = 'foot';

SELECT today - INTERVAL 1 YEAR INTO low_date;

SELECT COUNT(*) INTO foot_value 
	FROM FootScreening f
	WHERE f.patient_id = patient_id_in 
	AND f.date_recorded > low_date;
	
IF foot_value < 1 THEN
	SET out_t = 1;
	ELSE SET out_t = 0;
	END IF;
IF foot_count > 0 THEN		
	UPDATE HealthyTargetStatus hts SET 
	hts.out_of_target = out_t
	WHERE hts.patient_id = patient_id_in 
	AND hts.measurement = 'foot';
	ELSE
	INSERT INTO HealthyTargetStatus 
	VALUES (patient_id_in, 'foot', out_t);
	END IF;

/*Setting the value for psychological screening*/

SELECT COUNT(*) INTO psychologicalscreening_count 
	FROM HealthyTargetStatus hts
	WHERE hts.patient_id = patient_id_in 
	AND hts.measurement = 'psychologicalscreening';

SELECT today - INTERVAL 1 YEAR INTO low_date;

SELECT COUNT(*) INTO psychologicalscreening_value 
	FROM PsychologicalScreening ps
	WHERE ps.patient_id = patient_id_in 
	AND ps.date_recorded > low_date;

IF psychologicalscreening_value < 1 THEN
	SET out_t = 1;
	ELSE SET out_t = 0;
	END IF;
IF psychologicalscreening_count > 0 THEN		
	UPDATE HealthyTargetStatus hts SET 
	hts.out_of_target = out_t
	WHERE hts.patient_id = patient_id_in 
	AND hts.measurement = 'psychologicalscreening';
	ELSE
	INSERT INTO HealthyTargetStatus 
	VALUES (patient_id_in, 'psychologicalscreening', out_t);
	END IF;

/*Setting the value for physical activity*/

IF physicalactivity_result_in IS NOT NULL THEN 
	SELECT COUNT(*) INTO measurement_count 
	FROM PhysicalActivity p 
	WHERE p.patient_id = patient_id_in;
	IF measurement_count > 0 THEN 
	SELECT MAX(p.date_recorded) INTO max_date 
	FROM PhysicalActivity p 
	WHERE p.patient_id = patient_id_in;
		END IF;
/*assuring only the latest measurment is processed*/
	IF measurement_count = 0 OR 
	(max_date IS NOT NULL AND date_in >= max_date) THEN 
	SELECT COUNT(*) INTO physicalactivity_count 
	FROM HealthyTargetStatus hts
	WHERE hts.patient_id = patient_id_in 
	AND hts.measurement = 'physicalactivity';
	SELECT ht.upper_target INTO high 
	FROM HealthyTarget ht
	WHERE ht.measurement = 'physicalactivity';
	SELECT ht.lower_target INTO low
	FROM HealthyTarget ht
	WHERE ht.measurement = 'physicalactivity';
	IF physicalactivity_result_in < low OR 
		physicalactivity_result_in > high THEN
		SET out_t = 1;
		ELSE SET out_t = 0;
		END IF;
	IF physicalactivity_count > 0 THEN		
		UPDATE HealthyTargetStatus hts SET 
		hts.out_of_target = out_t
		WHERE hts.patient_id = patient_id_in 
		AND hts.measurement = 'physicalactivity';
		ELSE
		INSERT INTO HealthyTargetStatus 
		VALUES (patient_id_in, 'physicalactivity', out_t);
		END IF;
/*returning measurment count and max date to defaults*/
	SET measurement_count = 0;
	SET max_date = NULL;
	END IF;
END IF;

/*Setting the value for the influenza vaccine*/

SELECT COUNT(*) INTO influenzavaccine_count 
	FROM HealthyTargetStatus hts
	WHERE hts.patient_id = patient_id_in 
	AND hts.measurement = 'influenzavaccine';

SELECT today - INTERVAL 1 YEAR INTO low_date;

SELECT COUNT(*) INTO influenzavaccine_value 
	FROM InfluenzaVaccine iv
	WHERE iv.patient_id = patient_id_in 
	AND iv.date_recorded > low_date;
IF influenzavaccine_value < 1 THEN 
	SET out_t = 1;
	ELSE SET out_t = 0;
	END IF;
IF influenzavaccine_count > 0 THEN		
	UPDATE HealthyTargetStatus hts SET 
	hts.out_of_target = out_t
	WHERE hts.patient_id = patient_id_in 
	AND hts.measurement = 'influenzavaccine';
	ELSE
	INSERT INTO HealthyTargetStatus 
	VALUES (patient_id_in, 'influenzavaccine', out_t);
	END IF;

/*Setting the value for the pcv-13 vaccine*/

SELECT COUNT(*) INTO pcv13_count 
	FROM HealthyTargetStatus hts
	WHERE hts.patient_id = patient_id_in 
	AND hts.measurement = 'pcv13';
	
IF birthday > sixty THEN 
	SELECT birthday + INTERVAL 18 YEAR INTO low_date;
	SELECT COUNT(*) INTO pcv13_value 
	FROM PCV13 pc
	WHERE pc.patient_id = patient_id_in 
	AND pc.date_recorded > low_date;
	IF pcv13_value < 1 THEN 
		SET out_t = 1;
		ELSE SET out_t = 0;
		END IF;
	IF pcv13_count > 0 THEN		
		UPDATE HealthyTargetStatus hts SET 
		hts.out_of_target = out_t
		WHERE hts.patient_id = patient_id_in 
		AND hts.measurement = 'pcv13';
		ELSE
		INSERT INTO HealthyTargetStatus 
		VALUES (patient_id_in, 'pcv13', out_t);
		END IF;
END IF;

/*Setting the value for the ppsv-23 vaccine*/

SELECT COUNT(*) INTO ppsv23_count 
	FROM HealthyTargetStatus hts
	WHERE hts.patient_id = patient_id_in 
	AND hts.measurement = 'ppsv23';

IF birthday > sixty THEN 
	SELECT birthday + INTERVAL 18 YEAR INTO low_date;
	SELECT COUNT(*) INTO ppsv23_value 
	FROM PPSV23 pp
	WHERE pp.patient_id = patient_id_in 
	AND pp.date_recorded > low_date;
	IF ppsv23_value < 2 THEN 
		SET out_t = 1;
		ELSE SET out_t = 0;
		END IF;
	IF ppsv23_count > 0 THEN	
		UPDATE HealthyTargetStatus hts SET 
		hts.out_of_target = out_t
		WHERE hts.patient_id = patient_id_in 
		AND hts.measurement = 'ppsv23';
		ELSE
		INSERT INTO HealthyTargetStatus 
		VALUES (patient_id_in, 'ppsv23', out_t);
		END IF;
END IF;

/*Setting the value for the Hepatitis B vaccine*/

SELECT COUNT(*) INTO hepatitisb_count 
	FROM HealthyTargetStatus hts
	WHERE hts.patient_id = patient_id_in 
	AND hts.measurement = 'hepatitisb';

IF birthday > sixty THEN 
	SELECT birthday + INTERVAL 18 YEAR INTO low_date;
	SELECT COUNT(*) INTO hepatitisb_value 
		FROM HepatitisB hb
		WHERE hb.patient_id = patient_id_in 
		AND hb.date_recorded > low_date;
	IF hepatitisb_value < 1 THEN 
		SET out_t = 1;
		ELSE SET out_t = 0;
		END IF;
	IF hepatitisb_count > 0 THEN		
		UPDATE HealthyTargetStatus hts SET 
		hts.out_of_target = out_t
		WHERE hts.patient_id = patient_id_in 
		AND hts.measurement = 'hepatitisb';
		ELSE
		INSERT INTO HealthyTargetStatus 
		VALUES (patient_id_in, 'hepatitisb', out_t);
		END IF;
END IF;

/*Setting the value for the TDAP vaccine*/

SELECT COUNT(*) INTO tdap_count 
	FROM HealthyTargetStatus hts
	WHERE hts.patient_id = patient_id_in 
	AND hts.measurement = 'tdap';
	
SELECT today - INTERVAL 10 YEAR INTO low_date;

SELECT COUNT(*) INTO tdap_value 
	FROM TDAP td
	WHERE td.patient_id = patient_id_in 
	AND td.date_recorded > low_date;
IF tdap_value < 1 THEN 
	SET out_t = 1;
	ELSE SET out_t = 0;
	END IF;
IF tdap_count > 0 THEN		
	UPDATE HealthyTargetStatus hts SET 
	hts.out_of_target = out_t
	WHERE hts.patient_id = patient_id_in 
	AND hts.measurement = 'tdap';
	ELSE
	INSERT INTO HealthyTargetStatus 
	VALUES (patient_id_in, 'tdap', out_t);
	END IF;

/*Setting the value for the Zoster vaccine*/

SELECT COUNT(*) INTO zoster_count 
	FROM HealthyTargetStatus hts
	WHERE hts.patient_id = patient_id_in 
	AND hts.measurement = 'zoster';
	
IF birthday < sixty THEN
	SELECT COUNT(*) INTO zoster_value 
	FROM Zoster z
	WHERE z.patient_id = patient_id_in;
	IF zoster_value < 1 THEN 
		SET out_t = 1;
		ELSE SET out_t = 0;
		END IF;
	IF zoster_count > 0 THEN		
		UPDATE HealthyTargetStatus hts SET 
		hts.out_of_target = out_t
		WHERE hts.patient_id = patient_id_in 
		AND hts.measurement = 'zoster';
		ELSE
		INSERT INTO HealthyTargetStatus 
		VALUES (patient_id_in, 'zoster', out_t);
		END IF;
END IF;

/*Setting the value for smoking*/

IF smoking_result_in IS NOT NULL THEN 
	SELECT COUNT(*) INTO measurement_count 
	FROM SmokingCessation s 
	WHERE s.patient_id = patient_id_in;
	IF measurement_count > 0 THEN 
	SELECT MAX(s.date_recorded) INTO max_date 
	FROM SmokingCessation s 
	WHERE s.patient_id = patient_id_in;
		END IF;
/*assuring only the latest measurment is processed*/
	IF measurement_count = 0 OR 
	(max_date IS NOT NULL AND date_in >= max_date) THEN 
	SELECT COUNT(*) INTO smoking_count 
	FROM HealthyTargetStatus hts
	WHERE hts.patient_id = patient_id_in 
	AND hts.measurement = 'smoking';
	SELECT ht.upper_target INTO high 
	FROM HealthyTarget ht
	WHERE ht.measurement = 'smoking';
	SELECT ht.lower_target INTO low
	FROM HealthyTarget ht
	WHERE ht.measurement = 'smoking';
	IF smoking_result_in < low OR 
		smoking_result_in > high THEN
		SET out_t = 1;
		ELSE SET out_t = 0;
		END IF;
	IF smoking_count > 0 THEN		
		UPDATE HealthyTargetStatus hts SET 
		hts.out_of_target = out_t
		WHERE hts.patient_id = patient_id_in 
		AND hts.measurement = 'smoking';
		ELSE
		INSERT INTO HealthyTargetStatus 
		VALUES (patient_id_in, 'smoking', out_t);
		END IF;
/*returning measurment count and max date to defaults*/
	SET measurement_count = 0;
	SET max_date = NULL;
	END IF;
END IF;

/*Setting the value for the telephone follow-up*/

IF telephonefollowup_result_in IS NOT NULL THEN 
	SELECT COUNT(*) INTO measurement_count 
	FROM TelephoneFollowUp t 
	WHERE t.patient_id = patient_id_in;
	IF measurement_count > 0 THEN 
	SELECT MAX(t.date_recorded) INTO max_date 
	FROM TelephoneFollowUp t 
	WHERE t.patient_id = patient_id_in;
		END IF;
/*assuring only the latest measurment is processed*/
	IF measurement_count = 0 OR 
	(max_date IS NOT NULL AND date_in >= max_date) THEN 
	SELECT COUNT(*) INTO telephonefollowup_count 
	FROM HealthyTargetStatus hts
	WHERE hts.patient_id = patient_id_in 
	AND hts.measurement = 'telephonefollowup';
	SELECT ht.upper_target INTO high 
	FROM HealthyTarget ht
	WHERE ht.measurement = 'telephonefollowup';
	SELECT ht.lower_target INTO low
	FROM HealthyTarget ht
	WHERE ht.measurement = 'telephonefollowup';
	IF telephonefollowup_result_in < low OR 
		telephonefollowup_result_in > high THEN
		SET out_t = 1;
		ELSE SET out_t = 0;
		END IF;
	IF telephonefollowup_count > 0 THEN		
		UPDATE HealthyTargetStatus hts SET 
		hts.out_of_target = out_t
		WHERE hts.patient_id = patient_id_in 
		AND hts.measurement = 'telephonefollowup';
		ELSE
		INSERT INTO HealthyTargetStatus 
		VALUES (patient_id_in, 'telephonefollowup', out_t);
		END IF;
/*returning measurment count and max date to defaults*/
	SET measurement_count = 0;
	SET max_date = NULL;
	END IF;
END IF;

/*Setting the value for AST*/

IF ast_result_in IS NOT NULL THEN 
	SELECT COUNT(*) INTO measurement_count 
	FROM AST a 
	WHERE a.patient_id = patient_id_in;
	IF measurement_count > 0 THEN 
	SELECT MAX(a.date_recorded) INTO max_date 
	FROM AST a 
	WHERE a.patient_id = patient_id_in;
		END IF;
/*assuring only the latest measurment is processed*/
	IF measurement_count = 0 OR 
	(max_date IS NOT NULL AND date_in >= max_date) THEN 
	SELECT COUNT(*) INTO ast_count 
	FROM HealthyTargetStatus hts
	WHERE hts.patient_id = patient_id_in 
	AND hts.measurement = 'ast';
	SELECT ht.upper_target INTO high 
	FROM HealthyTarget ht
	WHERE ht.measurement = 'ast';
	SELECT ht.lower_target INTO low
	FROM HealthyTarget ht
	WHERE ht.measurement = 'ast';
	IF ast_result_in < low OR 
		ast_result_in > high THEN
		SET out_t = 1;
		ELSE SET out_t = 0;
		END IF;
	IF ast_count > 0 THEN		
		UPDATE HealthyTargetStatus hts SET 
		hts.out_of_target = out_t
		WHERE hts.patient_id = patient_id_in 
		AND hts.measurement = 'ast';
		ELSE
		INSERT INTO HealthyTargetStatus 
		VALUES (patient_id_in, 'ast', out_t);
		END IF;
/*returning measurment count and max date to defaults*/
	SET measurement_count = 0;
	SET max_date = NULL;
	END IF;
END IF;

/*Setting the value for ALT*/

IF alt_result_in IS NOT NULL THEN 
	SELECT COUNT(*) INTO measurement_count 
	FROM ALT a 
	WHERE a.patient_id = patient_id_in;
	IF measurement_count > 0 THEN 
	SELECT MAX(a.date_recorded) INTO max_date 
	FROM ALT a 
	WHERE a.patient_id = patient_id_in;
		END IF;
/*assuring only the latest measurment is processed*/
	IF measurement_count = 0 OR 
	(max_date IS NOT NULL AND date_in >= max_date) THEN 
	SELECT COUNT(*) INTO alt_count 
	FROM HealthyTargetStatus hts
	WHERE hts.patient_id = patient_id_in 
	AND hts.measurement = 'alt';
	SELECT ht.upper_target INTO high 
	FROM HealthyTarget ht
	WHERE ht.measurement = 'alt';
	SELECT ht.lower_target INTO low
	FROM HealthyTarget ht
	WHERE ht.measurement = 'alt';
	IF alt_result_in < low OR 
		alt_result_in > high THEN
		SET out_t = 1;
		ELSE SET out_t = 0;
		END IF;
	IF alt_count > 0 THEN		
		UPDATE HealthyTargetStatus hts SET 
		hts.out_of_target = out_t
		WHERE hts.patient_id = patient_id_in 
		AND hts.measurement = 'alt';
		ELSE
		INSERT INTO HealthyTargetStatus 
		VALUES (patient_id_in, 'alt', out_t);
		END IF;
/*returning measurment count and max date to defaults*/
	SET measurement_count = 0;
	SET max_date = NULL;
	END IF;
END IF;

/*Setting the value for PSA*/

IF psa_result_in IS NOT NULL THEN 
	SELECT COUNT(*) INTO measurement_count 
	FROM PSA p 
	WHERE p.patient_id = patient_id_in;
	IF measurement_count > 0 THEN 
	SELECT MAX(p.date_recorded) INTO max_date 
	FROM PSA p 
	WHERE p.patient_id = patient_id_in;
		END IF;
/*assuring only the latest measurment is processed*/
	IF measurement_count = 0 OR 
	(max_date IS NOT NULL AND date_in >= max_date) THEN 
	SELECT COUNT(*) INTO psa_count 
	FROM HealthyTargetStatus hts
	WHERE hts.patient_id = patient_id_in 
	AND hts.measurement = 'psa';
	SELECT ht.upper_target INTO high 
	FROM HealthyTarget ht
	WHERE ht.measurement = 'psa';
	SELECT ht.lower_target INTO low
	FROM HealthyTarget ht
	WHERE ht.measurement = 'psa';
	IF psa_result_in < low OR 
		psa_result_in > high THEN
		SET out_t = 1;
		ELSE SET out_t = 0;
		END IF;
	IF psa_count > 0 THEN		
		UPDATE HealthyTargetStatus hts SET 
		hts.out_of_target = out_t
		WHERE hts.patient_id = patient_id_in 
		AND hts.measurement = 'psa';
		ELSE
		INSERT INTO HealthyTargetStatus 
		VALUES (patient_id_in, 'psa', out_t);
		END IF;
/*returning measurment count and max date to defaults*/
	SET measurement_count = 0;
	SET max_date = NULL;
	END IF;
END IF;

/*Setting the value for ER visits*/


SELECT COUNT(*) INTO hospitalization_count 
	FROM HealthyTargetStatus hts
	WHERE hts.patient_id = patient_id_in 
	AND hts.measurement = 'hospitalization';
	SELECT ht.upper_target INTO high 
	FROM HealthyTarget ht
	WHERE ht.measurement = 'hospitalization';
	SELECT ht.lower_target INTO low
	FROM HealthyTarget ht
	WHERE ht.measurement = 'hospitalization';
SELECT COUNT(*) INTO hospitalization_value 
	FROM ER e
	WHERE e.patient_id = patient_id_in;
IF hospitalization_value < low OR 
	hospitalization_value > high THEN
	SET out_t = 1;
	ELSE SET out_t = 0;
	END IF;
IF hospitalization_count > 0 THEN		
	UPDATE HealthyTargetStatus hts SET 
	hts.out_of_target = out_t
	WHERE hts.patient_id = patient_id_in 
	AND hts.measurement = 'hospitalization';
	ELSE
	INSERT INTO HealthyTargetStatus 
	VALUES (patient_id_in, 'hospitalization', out_t);
	END IF;

SET proc_success = 1;

COMMIT;

END ; //
DELIMITER ;		

/**
* USAGE: To set the values for all measurements
*CALL addResults 
*	(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 
*	?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,
*	?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);
*1 = patient_id
*2 = a1c_result
*3 = glucoseac_result
*4 = glucosepc_result
*5 = ldl_result
*6 = ldlpostmi_result
*7 = ldlonstatin
*8 = hdl_result
*9 = triglycerides_result
*10 = tsh_result
*11 = tshonthyroidtreatment
*12 = t4_result
*13 = uacr_result
*14 = egfr_result
*15 = creatinine_result
*16 = bmi_result
*17 = waist_result
*18 = bloodpressuresystole_result
*19 = bloodpressurediastole_result
*20 = class_date
*21 = eye_result
*22 = foot_result
*23 = psychologicalscreening_result
*24 = physicalactivity_result
*25 = influenzavaccine_date
*26 = pcv13_date
*27 = ppsv23_date
*28 = hepatitisb_date
*29 = tdap_date
*30 = zoster_date
*31 = smoking_result
*32 = telephonefollowup_result
*33 = ast_result
*34 = alt_result
*35 = psa_result
*36 = compliance
*37 = hospitalization_date
*38 = note_topic
*39 = note
*40 = date_entered
*41 = poc_in
*42 = aceorarb
*43 = user_in
*44 = clinic_in
*45 = string
*46 = proc_success
*/

DELIMITER //
CREATE PROCEDURE addResults
	(IN patient_id_in INT, IN a1c_result_in DECIMAL(7,2), IN glucoseac_result_in DECIMAL(7,2),
	IN glucosepc_result_in DECIMAL(7,2), IN ldl_result_in DECIMAL(7,2), 
	IN ldlpostmi_result_in DECIMAL(7,2), IN ldlonstatin_in TINYINT(1), 
	IN hdl_result_in DECIMAL(7,2), IN triglycerides_result_in DECIMAL(7,2), 
	IN tsh_result_in DECIMAL(7,2), IN tshonthyroidtreatment_in TINYINT(1), 
	IN t4_result_in DECIMAL(7,2), IN uacr_result_in DECIMAL(7,2), IN egfr_result_in DECIMAL(7,2), 
	IN creatinine_result_in DECIMAL(7,2), IN bmi_result_in DECIMAL(7,2), 
	IN waist_result_in DECIMAL(7,2), IN bloodpressuresystole_result_in INT, 
	IN bloodpressurediastole_result_in INT, IN class_date_in DATE, 
	IN eye_result_in VARCHAR(50), IN foot_result_in VARCHAR(50), 
	IN psychologicalscreening_result_in INT, IN physicalactivity_result_in INT, 
	IN influenzavaccine_date_in DATE, IN pcv13_date_in DATE, 
	IN ppsv23_date_in DATE, IN hepatitisb_date_in DATE, 
	IN tdap_date_in DATE, IN zoster_date_in DATE,
	IN smoking_result_in TINYINT(1), IN telephonefollowup_result_in VARCHAR(50),
	IN ast_result_in DECIMAL(7,2), IN alt_result_in DECIMAL(7,2),
	IN psa_result_in DECIMAL(7,2), IN compliance_in DECIMAL(7,2), 
	IN hospitalization_date_in DATE, 
	IN note_topic_in VARCHAR(50), IN note_in VARCHAR(1000), 
	IN date_entered DATE, IN poc_in TINYINT(1), IN aceorarb_in TINYINT(1), 
	IN user_in VARCHAR(50),	IN clinic_in INT, IN string_in VARCHAR(64), 
	OUT proc_success TINYINT(1))
BEGIN
DECLARE a1c_count INT DEFAULT NULL;
DECLARE glucoseac_count INT DEFAULT NULL;
DECLARE glucosepc_count INT DEFAULT NULL;
DECLARE ldl_count INT DEFAULT NULL;
DECLARE ldlpostmi_count INT DEFAULT NULL;
DECLARE ldlonstatin_count INT DEFAULT NULL;
DECLARE hdl_count INT DEFAULT NULL;
DECLARE triglycerides_count INT DEFAULT NULL;
DECLARE tsh_count INT DEFAULT NULL;
DECLARE tshonthyroidtreatment_count INT DEFAULT NULL;
DECLARE t4_count INT DEFAULT NULL;
DECLARE uacr_count INT DEFAULT NULL;
DECLARE egfr_count INT DEFAULT NULL;
DECLARE creatinine_count INT DEFAULT NULL;
DECLARE bmi_count INT DEFAULT NULL;
DECLARE waist_count INT DEFAULT NULL;
DECLARE bloodpressuresystole_count INT DEFAULT NULL;
DECLARE bloodpressurediastole_count INT DEFAULT NULL;
DECLARE class_count INT DEFAULT NULL;
DECLARE eye_count INT DEFAULT NULL;
DECLARE foot_count INT DEFAULT NULL;
DECLARE psychologicalscreening_count INT DEFAULT NULL;
DECLARE physicalactivity_count INT DEFAULT NULL;
DECLARE influenzavaccine_count INT DEFAULT NULL;
DECLARE pcv13_count INT DEFAULT NULL;
DECLARE ppsv23_count INT DEFAULT NULL;
DECLARE hepatitisb_count INT DEFAULT NULL;
DECLARE tdap_count INT DEFAULT NULL;
DECLARE zoster_count INT DEFAULT NULL;
DECLARE smoking_count INT DEFAULT NULL;
DECLARE telephonefollowup_count INT DEFAULT NULL;
DECLARE telephonefollowup_result_out TINYINT(1) DEFAULT NULL;
DECLARE ast_count INT DEFAULT NULL;
DECLARE alt_count INT DEFAULT NULL;
DECLARE psa_count INT DEFAULT NULL;
DECLARE compliance_count INT DEFAULT NULL;
DECLARE hospitalization_count INT DEFAULT NULL;
DECLARE note_count INT DEFAULT NULL;
DECLARE gender_in VARCHAR(50);
DECLARE target_function TINYINT(1) DEFAULT NULL;

DECLARE EXIT HANDLER FOR SQLEXCEPTION ROLLBACK;
DECLARE EXIT HANDLER FOR SQLWARNING ROLLBACK;

START TRANSACTION;

SET proc_success = 0;

SELECT (CAST(AES_DECRYPT(p.gender, string_in) AS CHAR(50))) INTO gender_in 
FROM Patient p
WHERE p.patient_id = patient_id_in;

IF a1c_result_in IS NOT NULL 
AND poc_in IS NOT NULL THEN
	SELECT COUNT(*) INTO a1c_count 
	FROM A1C a1
	WHERE a1.patient_id = patient_id_in 
	AND a1.date_recorded = date_entered;
	IF a1c_count > 0 THEN		
		UPDATE A1C a1 SET 
		a1.result = a1c_result_in, a1.poc = poc_in 
		WHERE a1.patient_id = patient_id_in 
		AND a1.date_recorded = date_entered;
		ELSE
		INSERT INTO A1C 
		VALUES (patient_id_in, date_entered, a1c_result_in, 
		poc_in, user_in, clinic_in);
		END IF;
END IF;
IF glucoseac_result_in IS NOT NULL THEN
	SELECT COUNT(*) INTO glucoseac_count 
	FROM Glucose g
	WHERE g.patient_id = patient_id_in 
	AND g.date_recorded = date_entered;
	IF glucoseac_count > 0 THEN		
		UPDATE Glucose g SET 
		g.result = glucoseac_result_in
		WHERE g.patient_id = patient_id_in 
		AND g.date_recorded = date_entered;
		ELSE
		INSERT INTO Glucose 
		VALUES (patient_id_in, date_entered, glucoseac_result_in, 
		user_in, clinic_in);
		END IF;
END IF;
IF glucosepc_result_in IS NOT NULL THEN
	SELECT COUNT(*) INTO glucosepc_count 
	FROM Glucose g
	WHERE g.patient_id = patient_id_in 
	AND g.date_recorded = date_entered;
	IF glucosepc_count > 0 THEN		
		UPDATE Glucose g SET 
		g.result = glucosepc_result_in
		WHERE g.patient_id = patient_id_in 
		AND g.date_recorded = date_entered;
		ELSE
		INSERT INTO Glucose 
		VALUES (patient_id_in, date_entered, glucosepc_result_in, 
		user_in, clinic_in);
		END IF;
END IF;
IF ldl_result_in IS NOT NULL THEN
	SELECT COUNT(*) INTO ldl_count 
	FROM LDL ld
	WHERE ld.patient_id = patient_id_in 
	AND ld.date_recorded = date_entered;
	IF ldl_count > 0 THEN		
		UPDATE LDL ld SET 
		ld.result = ldl_result_in, ld.post_mi = 0 
		WHERE ld.patient_id = patient_id_in 
		AND ld.date_recorded = date_entered;
		ELSE
		INSERT INTO LDL 
		VALUES (patient_id_in, date_entered, ldl_result_in, 0, 
		user_in, clinic_in);
		END IF;
	IF ldlonstatin_in IS NOT NULL THEN
		SELECT COUNT(*) INTO ldlonstatin_count 
		FROM LDLOnStatin los
		WHERE los.patient_id = patient_id_in 
		AND los.date_recorded = date_entered;
		IF ldlonstatin_count > 0 THEN		
			UPDATE LDLOnStatin los SET 
			los.on_statin = ldlonstatin_in 
			WHERE los.patient_id = patient_id_in 
			AND los.date_recorded = date_entered;
			ELSE
			INSERT INTO LDLOnStatin 
			VALUES (patient_id_in, date_entered, ldlonstatin_in);
			END IF;
	END IF;
END IF;
IF ldlpostmi_result_in IS NOT NULL THEN
	SELECT COUNT(*) INTO ldlpostmi_count 
	FROM LDL ld
	WHERE ld.patient_id = patient_id_in 
	AND ld.date_recorded = date_entered;
	IF ldlpostmi_count > 0 THEN		
		UPDATE LDL ld SET 
		ld.result = ldlpostmi_result_in, ld.post_mi = 1
		WHERE ld.patient_id = patient_id_in 
		AND ld.date_recorded = date_entered;
		ELSE
		INSERT INTO LDL 
		VALUES (patient_id_in, date_entered, ldlpostmi_result_in, 1, 
		user_in, clinic_in);
		END IF;
	IF ldlonstatin_in IS NOT NULL THEN
		SELECT COUNT(*) INTO ldlonstatin_count 
		FROM LDLOnStatin los
		WHERE los.patient_id = patient_id_in 
		AND los.date_recorded = date_entered;
		IF ldlonstatin_count > 0 THEN		
			UPDATE LDLOnStatin los SET 
			los.on_statin = ldlonstatin_in 
			WHERE los.patient_id = patient_id_in 
			AND los.date_recorded = date_entered;
			ELSE
			INSERT INTO LDLOnStatin 
			VALUES (patient_id_in, date_entered, ldlonstatin_in);
			END IF;
	END IF;
END IF;
IF hdl_result_in IS NOT NULL THEN
	SELECT COUNT(*) INTO hdl_count 
	FROM HDL hd
	WHERE hd.patient_id = patient_id_in 
	AND hd.date_recorded = date_entered;
	IF hdl_count > 0 THEN		
		UPDATE HDL hd SET 
		hd.result = hdl_result_in
		WHERE hd.patient_id = patient_id_in 
		AND hd.date_recorded = date_entered;
		ELSE
		INSERT INTO HDL 
		VALUES (patient_id_in, date_entered, hdl_result_in, 
		user_in, clinic_in);
		END IF;
END IF;
IF triglycerides_result_in IS NOT NULL THEN
	SELECT COUNT(*) INTO triglycerides_count 
	FROM Triglycerides tr
	WHERE tr.patient_id = patient_id_in 
	AND tr.date_recorded = date_entered;
	IF triglycerides_count > 0 THEN		
		UPDATE Triglycerides tr SET 
		tr.result = triglycerides_result_in
		WHERE tr.patient_id = patient_id_in 
		AND tr.date_recorded = date_entered;
		ELSE
		INSERT INTO Triglycerides 
		VALUES (patient_id_in, date_entered, triglycerides_result_in, 
		user_in, clinic_in);
		END IF;
END IF;
IF tsh_result_in IS NOT NULL THEN
	SELECT COUNT(*) INTO tsh_count 
	FROM TSH ts
	WHERE ts.patient_id = patient_id_in 
	AND ts.date_recorded = date_entered;
	IF tsh_count > 0 THEN		
		UPDATE TSH ts SET 
		ts.result = tsh_result_in
		WHERE ts.patient_id = patient_id_in 
		AND ts.date_recorded = date_entered;
		ELSE
		INSERT INTO TSH 
		VALUES (patient_id_in, date_entered, tsh_result_in, 
		user_in, clinic_in);
		END IF;
	IF tshonthyroidtreatment_in IS NOT NULL THEN
		SELECT COUNT(*) INTO tshonthyroidtreatment_count 
		FROM TSHOnThyroidTreatment tott
		WHERE tott.patient_id = patient_id_in 
		AND tott.date_recorded = date_entered;
		IF tshonthyroidtreatment_count > 0 THEN		
			UPDATE TSHOnThyroidTreatment tott SET 
			tott.on_thyroid_treatment = tshonthyroidtreatment_in 
			WHERE tott.patient_id = patient_id_in 
			AND tott.date_recorded = date_entered;
			ELSE
			INSERT INTO TSHOnThyroidTreatment 
			VALUES (patient_id_in, date_entered, tshonthyroidtreatment_in);
			END IF;
	END IF;
END IF;
IF t4_result_in IS NOT NULL THEN
	SELECT COUNT(*) INTO t4_count 
	FROM T4 t
	WHERE t.patient_id = patient_id_in 
	AND t.date_recorded = date_entered;
	IF t4_count > 0 THEN		
		UPDATE T4 t SET 
		t.result = t4_result_in
		WHERE t.patient_id = patient_id_in 
		AND t.date_recorded = date_entered;
		ELSE
		INSERT INTO T4 
		VALUES (patient_id_in, date_entered, t4_result_in, 
		user_in, clinic_in);
		END IF;
END IF;
IF uacr_result_in IS NOT NULL THEN
	SELECT COUNT(*) INTO uacr_count 
	FROM UACR ua
	WHERE ua.patient_id = patient_id_in 
	AND ua.date_recorded = date_entered;
	IF uacr_count > 0 THEN		
		UPDATE UACR ua SET 
		ua.result = uacr_result_in
		WHERE ua.patient_id = patient_id_in 
		AND ua.date_recorded = date_entered;
		ELSE
		INSERT INTO UACR 
		VALUES (patient_id_in, date_entered, uacr_result_in, 
		user_in, clinic_in);
		END IF;
END IF;
IF egfr_result_in IS NOT NULL THEN
	SELECT COUNT(*) INTO egfr_count 
	FROM EGFR eg
	WHERE eg.patient_id = patient_id_in 
	AND eg.date_recorded = date_entered;
	IF egfr_count > 0 THEN		
		UPDATE EGFR eg SET 
		eg.result = egfr_result_in
		WHERE eg.patient_id = patient_id_in 
		AND eg.date_recorded = date_entered;
		ELSE
		INSERT INTO EGFR 
		VALUES (patient_id_in, date_entered, egfr_result_in, 
		user_in, clinic_in);
		END IF;
END IF;
IF creatinine_result_in IS NOT NULL THEN
	SELECT COUNT(*) INTO creatinine_count 
	FROM Creatinine cr
	WHERE cr.patient_id = patient_id_in 
	AND cr.date_recorded = date_entered;
	IF creatinine_count > 0 THEN		
		UPDATE Creatinine cr SET 
		cr.result = creatinine_result_in
		WHERE cr.patient_id = patient_id_in 
		AND cr.date_recorded = date_entered;
		ELSE
		INSERT INTO Creatinine 
		VALUES (patient_id_in, date_entered, creatinine_result_in, 
		user_in, clinic_in);
		END IF;
END IF;
IF bmi_result_in IS NOT NULL THEN
	SELECT COUNT(*) INTO bmi_count 
	FROM BMI b
	WHERE b.patient_id = patient_id_in 
	AND b.date_recorded = date_entered;
	IF bmi_count > 0 THEN		
		UPDATE BMI b SET 
		b.result = bmi_result_in
		WHERE b.patient_id = patient_id_in 
		AND b.date_recorded = date_entered;
		ELSE
		INSERT INTO BMI 
		VALUES (patient_id_in, date_entered, bmi_result_in, 
		user_in, clinic_in);
		END IF;
END IF;
IF waist_result_in IS NOT NULL THEN
	SELECT COUNT(*) INTO waist_count 
	FROM Waist w
	WHERE w.patient_id = patient_id_in 
	AND w.date_recorded = date_entered;
	IF waist_count > 0 THEN		
		UPDATE Waist w SET 
		w.result = waist_result_in
		WHERE w.patient_id = patient_id_in 
		AND w.date_recorded = date_entered;
		ELSE
		INSERT INTO Waist 
		VALUES (patient_id_in, date_entered, waist_result_in, 
		user_in, clinic_in);
		END IF;
END IF;
IF bloodpressuresystole_result_in IS NOT NULL 
AND bloodpressurediastole_result_in IS NOT NULL 
AND aceorarb_in IS NOT NULL THEN
	SELECT COUNT(*) INTO bloodpressuresystole_count 
	FROM BloodPressure bp
	WHERE bp.patient_id = patient_id_in 
	AND bp.date_recorded = date_entered;
	IF bloodpressuresystole_count > 0 THEN		
		UPDATE BloodPressure bp SET 
		bp.result_s = bloodpressuresystole_result_in, 
		bp.result_d = bloodpressurediastole_result_in, 
		bp.ace_or_arb = aceorarb_in 
		WHERE bp.patient_id = patient_id_in 
		AND bp.date_recorded = date_entered;
		ELSE
		INSERT INTO BloodPressure 
		VALUES (patient_id_in, date_entered, bloodpressuresystole_result_in, 
		bloodpressurediastole_result_in, aceorarb_in, user_in, clinic_in);
		END IF;
END IF;
IF class_date_in IS NOT NULL THEN
	SELECT COUNT(*) INTO class_count 
	FROM LastClass lc
	WHERE lc.patient_id = patient_id_in 
	AND lc.date_recorded = class_date_in;
	IF class_count > 0 THEN
		UPDATE LastClass lc SET 
		lc.date_recorded = class_date_in
		WHERE lc.patient_id = patient_id_in 
		AND lc.date_recorded = class_date_in;
		ELSE
		INSERT INTO LastClass 
		VALUES (patient_id_in, class_date_in, user_in, clinic_in);
		END IF;
END IF;
IF eye_result_in IS NOT NULL THEN
	SELECT COUNT(*) INTO eye_count 
	FROM EyeScreening eye
	WHERE eye.patient_id = patient_id_in 
	AND eye.date_recorded = date_entered;
	IF eye_count > 0 THEN		
		UPDATE EyeScreening e SET 
		e.eye_exam_code = eye_result_in
		WHERE e.patient_id = patient_id_in 
		AND e.date_recorded = date_entered;
		ELSE
		INSERT INTO EyeScreening 
		VALUES (patient_id_in, date_entered, eye_result_in, 
		user_in, clinic_in);
		END IF;
END IF;
IF foot_result_in IS NOT NULL THEN
	SELECT COUNT(*) INTO foot_count 
	FROM FootScreening f
	WHERE f.patient_id = patient_id_in 
	AND f.date_recorded = date_entered;
	IF foot_count > 0 THEN		
		UPDATE FootScreening f SET 
		f.risk_category = foot_result_in
		WHERE f.patient_id = patient_id_in 
		AND f.date_recorded = date_entered;
		ELSE
		INSERT INTO FootScreening 
		VALUES (patient_id_in, date_entered, foot_result_in, 
		user_in, clinic_in);
		END IF;
END IF;
IF psychologicalscreening_result_in IS NOT NULL THEN
	SELECT COUNT(*) INTO psychologicalscreening_count 
	FROM PsychologicalScreening ps
	WHERE ps.patient_id = patient_id_in 
	AND ps.date_recorded = date_entered;
	IF psychologicalscreening_count > 0 THEN		
		UPDATE PsychologicalScreening ps SET 
		ps.phq_score = psychologicalscreening_result_in
		WHERE ps.patient_id = patient_id_in 
		AND ps.date_recorded = date_entered;
		ELSE
		INSERT INTO PsychologicalScreening 
		VALUES (patient_id_in, date_entered, psychologicalscreening_result_in, 
		user_in, clinic_in);
		END IF;
END IF;
IF physicalactivity_result_in IS NOT NULL THEN
	SELECT COUNT(*) INTO physicalactivity_count 
	FROM PhysicalActivity pe
	WHERE pe.patient_id = patient_id_in 
	AND pe.date_recorded = date_entered;
	IF physicalactivity_count > 0 THEN		
		UPDATE PhysicalActivity pe SET 
		pe.min_per_week = physicalactivity_result_in
		WHERE pe.patient_id = patient_id_in 
		AND pe.date_recorded = date_entered;
		ELSE
		INSERT INTO PhysicalActivity 
		VALUES (patient_id_in, date_entered, physicalactivity_result_in, 
		user_in, clinic_in);
		END IF;
END IF;
IF influenzavaccine_date_in IS NOT NULL THEN
	SELECT COUNT(*) INTO influenzavaccine_count 
	FROM InfluenzaVaccine iv
	WHERE iv.patient_id = patient_id_in 
	AND iv.date_recorded = influenzavaccine_date_in;
	IF influenzavaccine_count = 0 THEN		
		INSERT INTO InfluenzaVaccine  
		VALUES (patient_id_in, influenzavaccine_date_in, 
		user_in, clinic_in);
		END IF;
END IF;
IF pcv13_date_in IS NOT NULL THEN
	SELECT COUNT(*) INTO pcv13_count 
	FROM PCV13 pc
	WHERE pc.patient_id = patient_id_in 
	AND pc.date_recorded = pcv13_date_in;
	IF pcv13_count = 0 THEN
		INSERT INTO PCV13  
		VALUES (patient_id_in, pcv13_date_in, 
		user_in, clinic_in);
		END IF;
END IF;
IF ppsv23_date_in IS NOT NULL THEN
	SELECT COUNT(*) INTO ppsv23_count 
	FROM PPSV23 pp
	WHERE pp.patient_id = patient_id_in 
	AND pp.date_recorded = ppsv23_date_in;
	IF ppsv23_count = 0 THEN
		INSERT INTO PPSV23  
		VALUES (patient_id_in, ppsv23_date_in, 
		user_in, clinic_in);
		END IF;
END IF;
IF hepatitisb_date_in IS NOT NULL THEN
	SELECT COUNT(*) INTO hepatitisb_count 
	FROM HepatitisB hb
	WHERE hb.patient_id = patient_id_in 
	AND hb.date_recorded = hepatitisb_date_in;
	IF hepatitisb_count = 0 THEN
		INSERT INTO HepatitisB  
		VALUES (patient_id_in, hepatitisb_date_in, 
		user_in, clinic_in);
		END IF;
END IF;
IF tdap_date_in IS NOT NULL THEN
	SELECT COUNT(*) INTO tdap_count 
	FROM TDAP td
	WHERE td.patient_id = patient_id_in 
	AND td.date_recorded = tdap_date_in;
	IF tdap_count = 0 THEN
		INSERT INTO TDAP  
		VALUES (patient_id_in, tdap_date_in, 
		user_in, clinic_in);
		END IF;
END IF;
IF zoster_date_in IS NOT NULL THEN
	SELECT COUNT(*) INTO zoster_count 
	FROM Zoster z
	WHERE z.patient_id = patient_id_in 
	AND z.date_recorded = zoster_date_in;
	IF zoster_count = 0 THEN
		INSERT INTO Zoster  
		VALUES (patient_id_in, zoster_date_in, 
		user_in, clinic_in);
		END IF;
END IF;
IF smoking_result_in IS NOT NULL THEN
	SELECT COUNT(*) INTO smoking_count 
	FROM SmokingCessation sm
	WHERE sm.patient_id = patient_id_in 
	AND sm.date_recorded = date_entered;
	IF smoking_count > 0 THEN		
		UPDATE SmokingCessation sm SET 
		sm.smoker = smoking_result_in
		WHERE sm.patient_id = patient_id_in 
		AND sm.date_recorded = date_entered;
		ELSE
		INSERT INTO SmokingCessation 
		VALUES (patient_id_in, date_entered, smoking_result_in, 
		user_in, clinic_in);
		END IF;
END IF;
IF telephonefollowup_result_in IS NOT NULL THEN
	SELECT COUNT(*) INTO telephonefollowup_count 
	FROM TelephoneFollowUp t
	WHERE t.patient_id = patient_id_in 
	AND t.date_recorded = date_entered;
	IF telephonefollowup_count > 0 THEN		
		UPDATE TelephoneFollowUp t SET 
		t.follow_up_code = telephonefollowup_result_in
		WHERE t.patient_id = patient_id_in 
		AND t.date_recorded = date_entered;
		ELSE
		INSERT INTO TelephoneFollowUp 
		VALUES (patient_id_in, date_entered, telephonefollowup_result_in, 
		user_in, clinic_in);
		END IF;
	IF telephonefollowup_result_in = 'Confirmed' 
		OR telephonefollowup_result_in = 'Contacted' THEN 
		SET telephonefollowup_result_out := 1;
		END IF;
END IF;
IF ast_result_in IS NOT NULL THEN
	SELECT COUNT(*) INTO ast_count 
	FROM AST a
	WHERE a.patient_id = patient_id_in 
	AND a.date_recorded = date_entered;
	IF ast_count > 0 THEN		
		UPDATE AST a SET 
		a.result = ast_result_in
		WHERE a.patient_id = patient_id_in 
		AND a.date_recorded = date_entered;
		ELSE
		INSERT INTO AST 
		VALUES (patient_id_in, date_entered, ast_result_in, 
		user_in, clinic_in);
		END IF;
END IF;
IF alt_result_in IS NOT NULL THEN
	SELECT COUNT(*) INTO alt_count 
	FROM ALT al
	WHERE al.patient_id = patient_id_in 
	AND al.date_recorded = date_entered;
	IF alt_count > 0 THEN		
		UPDATE ALT al SET 
		al.result = alt_result_in
		WHERE al.patient_id = patient_id_in 
		AND al.date_recorded = date_entered;
		ELSE
		INSERT INTO ALT 
		VALUES (patient_id_in, date_entered, alt_result_in, 
		user_in, clinic_in);
		END IF;
END IF;
IF psa_result_in IS NOT NULL THEN
	IF gender_in = 'Male' THEN 
		SELECT COUNT(*) INTO psa_count 
		FROM PSA p
		WHERE p.patient_id = patient_id_in 
		AND p.date_recorded = date_entered;
		IF psa_count > 0 THEN		
			UPDATE PSA p SET 
			p.result = psa_result_in
			WHERE p.patient_id = patient_id_in 
			AND p.date_recorded = date_entered;
			ELSE
			INSERT INTO PSA 
			VALUES (patient_id_in, date_entered, psa_result_in, 
			user_in, clinic_in);
			END IF;
	END IF;
END IF;
IF compliance_in IS NOT NULL THEN
	SELECT COUNT(*) INTO compliance_count 
	FROM Compliance c
	WHERE c.patient_id = patient_id_in 
	AND c.date_recorded = date_entered;
	IF compliance_count > 0 THEN 
		UPDATE Compliance c SET 
		c.result = compliance_in
		WHERE c.patient_id = patient_id_in 
		AND c.date_recorded = date_entered;
		ELSE
		INSERT INTO Compliance 
		VALUES (patient_id_in, date_entered, compliance_in, 
		user_in, clinic_in);
		END IF;
END IF;
IF hospitalization_date_in IS NOT NULL THEN
	SELECT COUNT(*) INTO hospitalization_count 
	FROM ER e
	WHERE e.patient_id = patient_id_in 
	AND e.date_recorded = hospitalization_date_in;
	IF hospitalization_count = 0 THEN 
		INSERT INTO ER 
		VALUES (patient_id_in, hospitalization_date_in, 
		user_in, clinic_in);
		END IF;
END IF;
IF note_topic_in IS NOT NULL THEN 
	IF note_in IS NOT NULL THEN 
		SELECT COUNT(*) INTO note_count 
		FROM Note n
		WHERE n.patient_id = patient_id_in 
		AND n.topic = note_topic_in 
		AND n.date_recorded = date_entered;
		IF note_count > 0 THEN		
			UPDATE Note n SET 
			n.note = note_in 
			WHERE n.patient_id = patient_id_in 
			AND n.topic = note_topic_in
			AND n.date_recorded = date_entered;
			ELSE
			INSERT INTO Note 
			VALUES (patient_id_in, date_entered, note_topic_in, 
			note_in, user_in, clinic_in);
			END IF;
	END IF;	
END IF;
CALL setTargetStatus(patient_id_in, a1c_result_in, glucoseac_result_in,
	glucosepc_result_in, ldl_result_in, ldlpostmi_result_in,
	hdl_result_in, triglycerides_result_in, tsh_result_in, 
	t4_result_in, uacr_result_in, egfr_result_in, 
	creatinine_result_in, bmi_result_in, 
	waist_result_in, bloodpressuresystole_result_in, 
	bloodpressurediastole_result_in, physicalactivity_result_in, 
	smoking_result_in, telephonefollowup_result_out,
	ast_result_in, alt_result_in,
	psa_result_in, date_entered, string_in, @success);
	
SELECT @success INTO target_function;

IF target_function = 1 THEN
	SET proc_success = 1;
	COMMIT;
	ELSE
	SET proc_success = 0;
	ROLLBACK;
	END IF;

END ; //
DELIMITER ;

/**
* USAGE: To set the values for patient treatment
*CALL addTreatment 
*	(?, ?, ?, ?, ?, ?);
*1 = patient_id
*2 = rx_class
*3 = med_id
*4 = date_entered
*5 = user_in
*6 = clinic_in
*/

DELIMITER //
CREATE PROCEDURE addTreatment
	(IN patient_id_in INT, IN rx_class_in VARCHAR(50), IN med_id_in VARCHAR(50), 
	IN date_entered DATE, IN user_in VARCHAR(50), IN clinic_in INT)
BEGIN

DECLARE rx_count INT DEFAULT NULL;
DECLARE med_count INT DEFAULT NULL;

IF rx_class_in IS NOT NULL THEN 
	SELECT COUNT(*) INTO rx_count 
	FROM PatientRx pr
	WHERE pr.patient_id = patient_id_in 
	AND pr.date_recorded = date_entered;
	IF rx_count > 0 THEN 
		UPDATE PatientRx pr SET 
		pr.rx_class = rx_class_in 
		WHERE pr.patient_id = patient_id_in
		AND pr.date_recorded = date_entered;
		ELSE 	
		INSERT INTO PatientRx 
		VALUES (patient_id_in, date_entered, rx_class_in, 
		user_in, clinic_in);
		END IF;
END IF;
IF med_id_in IS NOT NULL THEN 
	SELECT COUNT(*) INTO med_count 
	FROM PatientMed pm
	WHERE pm.patient_id = patient_id_in 
	AND pm.med_id = med_id_in 
	AND pm.date_recorded = date_entered;
	IF med_count = 0 THEN		
		INSERT INTO PatientMed 
		VALUES (patient_id_in, date_entered, med_id_in, 
		user_in, clinic_in);
		END IF;	
END IF;

END ; //
DELIMITER ;

/**
* USAGE: To update the values for the Healthy Target Status table
*CALL updateTargetStatus (?, ?, ?);
*1 = patient_id
*2 = string
*3 = proc_success
*/

DELIMITER //
CREATE PROCEDURE updateTargetStatus
	(IN patient_id_in INT, IN string_in VARCHAR(64), 
	OUT proc_success TINYINT(1))

BEGIN

DECLARE class_count INT DEFAULT 0;
DECLARE class_value INT DEFAULT 0;
DECLARE eye_count INT DEFAULT 0;
DECLARE eye_value INT DEFAULT 0;
DECLARE foot_count INT DEFAULT 0;
DECLARE foot_value INT DEFAULT 0;
DECLARE psychologicalscreening_count INT DEFAULT 0;
DECLARE psychologicalscreening_value INT DEFAULT 0;
DECLARE influenzavaccine_count INT DEFAULT 0;
DECLARE influenzavaccine_value INT DEFAULT -1;
DECLARE pcv13_count INT DEFAULT 0;
DECLARE pcv13_value INT DEFAULT -1;
DECLARE ppsv23_count INT DEFAULT 0;
DECLARE ppsv23_value INT DEFAULT -1;
DECLARE hepatitisb_count INT DEFAULT 0;
DECLARE hepatitisb_value INT DEFAULT -1;
DECLARE tdap_count INT DEFAULT 0;
DECLARE tdap_value INT DEFAULT -1;
DECLARE zoster_count INT DEFAULT 0;
DECLARE zoster_value INT DEFAULT -1;
DECLARE out_t INT DEFAULT 0;
DECLARE low DATE;
DECLARE birthday DATE;
DECLARE today DATE DEFAULT DATE(NOW());
DECLARE sixty DATE;

DECLARE EXIT HANDLER FOR SQLEXCEPTION ROLLBACK;
DECLARE EXIT HANDLER FOR SQLWARNING ROLLBACK;

START TRANSACTION;

SET proc_success = 0;

SELECT today - INTERVAL 60 YEAR INTO sixty;

SELECT (CAST(AES_DECRYPT(p.birth_date, string_in) AS DATE)) INTO birthday FROM Patient p 
WHERE p.patient_id = patient_id_in;

/*Setting the value for the class attendance*/

SELECT COUNT(*) INTO class_count 
	FROM HealthyTargetStatus hts
	WHERE hts.patient_id = patient_id_in 
	AND hts.measurement = 'class';
	
SELECT COUNT(*) INTO class_value 
	FROM LastClass lc
	WHERE lc.patient_id = patient_id_in; 
	
IF class_value < 1 THEN
	SET out_t = 1;
	ELSE SET out_t = 0;
	END IF;
IF class_count > 0 THEN		
	UPDATE HealthyTargetStatus hts SET 
	hts.out_of_target = out_t
	WHERE hts.patient_id = patient_id_in 
	AND hts.measurement = 'class';
	ELSE
	INSERT INTO HealthyTargetStatus 
	VALUES (patient_id_in, 'class', out_t);
	END IF;

/*Setting the value for the eye screening*/

SELECT COUNT(*) INTO eye_count 
	FROM HealthyTargetStatus hts
	WHERE hts.patient_id = patient_id_in 
	AND hts.measurement = 'eye';

SELECT today - INTERVAL 2 YEAR INTO low;

SELECT COUNT(*) INTO eye_value 
	FROM EyeScreening e
	WHERE e.patient_id = patient_id_in 
	AND e.date_recorded > low;

IF eye_value < 1 THEN
	SET out_t = 1;
	ELSE SET out_t = 0;
	END IF;
IF eye_count > 0 THEN		
	UPDATE HealthyTargetStatus hts SET 
	hts.out_of_target = out_t
	WHERE hts.patient_id = patient_id_in 
	AND hts.measurement = 'eye';
	ELSE
	INSERT INTO HealthyTargetStatus 
	VALUES (patient_id_in, 'eye', out_t);
	END IF;

/*Setting the value for the foot screening*/

SELECT COUNT(*) INTO foot_count 
	FROM HealthyTargetStatus hts
	WHERE hts.patient_id = patient_id_in 
	AND hts.measurement = 'foot';

SELECT today - INTERVAL 1 YEAR INTO low;

SELECT COUNT(*) INTO foot_value 
	FROM FootScreening f
	WHERE f.patient_id = patient_id_in 
	AND f.date_recorded > low;
	
IF foot_value < 1 THEN
	SET out_t = 1;
	ELSE SET out_t = 0;
	END IF;
IF foot_count > 0 THEN		
	UPDATE HealthyTargetStatus hts SET 
	hts.out_of_target = out_t
	WHERE hts.patient_id = patient_id_in 
	AND hts.measurement = 'foot';
	ELSE
	INSERT INTO HealthyTargetStatus 
	VALUES (patient_id_in, 'foot', out_t);
	END IF;

/*Setting the value for psychological screening*/

SELECT COUNT(*) INTO psychologicalscreening_count 
	FROM HealthyTargetStatus hts
	WHERE hts.patient_id = patient_id_in 
	AND hts.measurement = 'psychologicalscreening';

SELECT today - INTERVAL 1 YEAR INTO low;

SELECT COUNT(*) INTO psychologicalscreening_value 
	FROM PsychologicalScreening ps
	WHERE ps.patient_id = patient_id_in 
	AND ps.date_recorded > low;

IF psychologicalscreening_value < 1 THEN
	SET out_t = 1;
	ELSE SET out_t = 0;
	END IF;
IF psychologicalscreening_count > 0 THEN		
	UPDATE HealthyTargetStatus hts SET 
	hts.out_of_target = out_t
	WHERE hts.patient_id = patient_id_in 
	AND hts.measurement = 'psychologicalscreening';
	ELSE
	INSERT INTO HealthyTargetStatus 
	VALUES (patient_id_in, 'psychologicalscreening', out_t);
	END IF;

/*Setting the value for the influenza vaccine*/

SELECT COUNT(*) INTO influenzavaccine_count 
	FROM HealthyTargetStatus hts
	WHERE hts.patient_id = patient_id_in 
	AND hts.measurement = 'influenzavaccine';

SELECT today - INTERVAL 1 YEAR INTO low;

SELECT COUNT(*) INTO influenzavaccine_value 
	FROM InfluenzaVaccine iv
	WHERE iv.patient_id = patient_id_in 
	AND iv.date_recorded > low;
IF influenzavaccine_value < 1 THEN 
	SET out_t = 1;
	ELSE SET out_t = 0;
	END IF;
IF influenzavaccine_count > 0 THEN		
	UPDATE HealthyTargetStatus hts SET 
	hts.out_of_target = out_t
	WHERE hts.patient_id = patient_id_in 
	AND hts.measurement = 'influenzavaccine';
	ELSE
	INSERT INTO HealthyTargetStatus 
	VALUES (patient_id_in, 'influenzavaccine', out_t);
	END IF;

/*Setting the value for the pcv-13 vaccine*/

SELECT COUNT(*) INTO pcv13_count 
	FROM HealthyTargetStatus hts
	WHERE hts.patient_id = patient_id_in 
	AND hts.measurement = 'pcv13';
	
IF birthday > sixty THEN 
	SELECT birthday + INTERVAL 18 YEAR INTO low;
	SELECT COUNT(*) INTO pcv13_value 
	FROM PCV13 pc
	WHERE pc.patient_id = patient_id_in 
	AND pc.date_recorded > low;
	IF pcv13_value < 1 THEN 
		SET out_t = 1;
		ELSE SET out_t = 0;
		END IF;
	IF pcv13_count > 0 THEN		
		UPDATE HealthyTargetStatus hts SET 
		hts.out_of_target = out_t
		WHERE hts.patient_id = patient_id_in 
		AND hts.measurement = 'pcv13';
		ELSE
		INSERT INTO HealthyTargetStatus 
		VALUES (patient_id_in, 'pcv13', out_t);
		END IF;
END IF;

/*Setting the value for the ppsv-23 vaccine*/

SELECT COUNT(*) INTO ppsv23_count 
	FROM HealthyTargetStatus hts
	WHERE hts.patient_id = patient_id_in 
	AND hts.measurement = 'ppsv23';

IF birthday > sixty THEN 
	SELECT birthday + INTERVAL 18 YEAR INTO low;
	SELECT COUNT(*) INTO ppsv23_value 
	FROM PPSV23 pp
	WHERE pp.patient_id = patient_id_in 
	AND pp.date_recorded > low;
	IF ppsv23_value < 2 THEN 
		SET out_t = 1;
		ELSE SET out_t = 0;
		END IF;
	IF ppsv23_count > 0 THEN	
		UPDATE HealthyTargetStatus hts SET 
		hts.out_of_target = out_t
		WHERE hts.patient_id = patient_id_in 
		AND hts.measurement = 'ppsv23';
		ELSE
		INSERT INTO HealthyTargetStatus 
		VALUES (patient_id_in, 'ppsv23', out_t);
		END IF;
END IF;

/*Setting the value for the Hepatitis B vaccine*/

SELECT COUNT(*) INTO hepatitisb_count 
	FROM HealthyTargetStatus hts
	WHERE hts.patient_id = patient_id_in 
	AND hts.measurement = 'hepatitisb';

IF birthday > sixty THEN 
	SELECT birthday + INTERVAL 18 YEAR INTO low;
	SELECT COUNT(*) INTO hepatitisb_value 
		FROM HepatitisB hb
		WHERE hb.patient_id = patient_id_in 
		AND hb.date_recorded > low;
	IF hepatitisb_value < 1 THEN 
		SET out_t = 1;
		ELSE SET out_t = 0;
		END IF;
	IF hepatitisb_count > 0 THEN		
		UPDATE HealthyTargetStatus hts SET 
		hts.out_of_target = out_t
		WHERE hts.patient_id = patient_id_in 
		AND hts.measurement = 'hepatitisb';
		ELSE
		INSERT INTO HealthyTargetStatus 
		VALUES (patient_id_in, 'hepatitisb', out_t);
		END IF;
END IF;

/*Setting the value for the TDAP vaccine*/

SELECT COUNT(*) INTO tdap_count 
	FROM HealthyTargetStatus hts
	WHERE hts.patient_id = patient_id_in 
	AND hts.measurement = 'tdap';
	
SELECT today - INTERVAL 10 YEAR INTO low;

SELECT COUNT(*) INTO tdap_value 
	FROM TDAP td
	WHERE td.patient_id = patient_id_in 
	AND td.date_recorded > low;
IF tdap_value < 1 THEN 
	SET out_t = 1;
	ELSE SET out_t = 0;
	END IF;
IF tdap_count > 0 THEN		
	UPDATE HealthyTargetStatus hts SET 
	hts.out_of_target = out_t
	WHERE hts.patient_id = patient_id_in 
	AND hts.measurement = 'tdap';
	ELSE
	INSERT INTO HealthyTargetStatus 
	VALUES (patient_id_in, 'tdap', out_t);
	END IF;

/*Setting the value for the Zoster vaccine*/

SELECT COUNT(*) INTO zoster_count 
	FROM HealthyTargetStatus hts
	WHERE hts.patient_id = patient_id_in 
	AND hts.measurement = 'zoster';
	
IF birthday < sixty THEN
	SELECT COUNT(*) INTO zoster_value 
	FROM Zoster z
	WHERE z.patient_id = patient_id_in;
	IF zoster_value < 1 THEN 
		SET out_t = 1;
		ELSE SET out_t = 0;
		END IF;
	IF zoster_count > 0 THEN		
		UPDATE HealthyTargetStatus hts SET 
		hts.out_of_target = out_t
		WHERE hts.patient_id = patient_id_in 
		AND hts.measurement = 'zoster';
		ELSE
		INSERT INTO HealthyTargetStatus 
		VALUES (patient_id_in, 'zoster', out_t);
		END IF;
END IF;

SET proc_success = 1;

COMMIT;

END ; //
DELIMITER ;

/**
 * USAGE: To retrieve the most recent data on a patient
 * CALL getPatientDashboard(?, ?, ?);
 * 1 = patient_id
 * 2 = string
 * 2 = proc_success
 */
DELIMITER //
CREATE PROCEDURE getPatientDashboard(IN patient_id_in INT, 
	IN string_in VARCHAR(64), OUT proc_success TINYINT(1))

BEGIN
	DECLARE max_date DATE DEFAULT NULL;
	
	DECLARE EXIT HANDLER FOR SQLEXCEPTION ROLLBACK;
	DECLARE EXIT HANDLER FOR SQLWARNING ROLLBACK;

	START TRANSACTION;

	SET proc_success = 0;

	/*Retrieving a1c result*/

	SELECT MAX(a.date_recorded) INTO max_date 
		FROM A1C a 
		WHERE a.patient_id = patient_id_in;

	SELECT a.result AS 'a1c', a.date_recorded AS 'date', 
	a.poc AS 'poc' 
	FROM A1C a  
	WHERE a.patient_id = patient_id_in 
	AND a.date_recorded = max_date;

	/*Retrieving glucose result*/

	SELECT MAX(g.date_recorded) INTO max_date 
		FROM Glucose g 
		WHERE g.patient_id = patient_id_in;

	SELECT g.result AS 'glucose', g.date_recorded AS 'date' 
	FROM Glucose g  
	WHERE g.patient_id = patient_id_in
	AND g.date_recorded = max_date;

	/*Retrieving LDL result*/

	SELECT MAX(l.date_recorded) INTO max_date 
		FROM LDL l 
		WHERE l.patient_id = patient_id_in;

	SELECT l.result AS 'ldl', l.post_mi AS 'post mi', 
	los.on_statin AS 'on statin', l.date_recorded AS 'date' 
	FROM LDL l 
	LEFT JOIN LDLOnStatin los
	ON l.patient_id = los.patient_id 
	AND l.date_recorded = los.date_recorded 
	WHERE l.patient_id = patient_id_in
	AND l.date_recorded = max_date;
	
	/*Retrieving HDL result*/

	SELECT MAX(h.date_recorded) INTO max_date 
		FROM HDL h 
		WHERE h.patient_id = patient_id_in;

	SELECT h.result AS 'hdl', h.date_recorded AS 'date' 
	FROM HDL h  
	WHERE h.patient_id = patient_id_in
	AND h.date_recorded = max_date;

	/*Retrieving triglycerides result*/

	SELECT MAX(tr.date_recorded) INTO max_date 
		FROM Triglycerides tr 
		WHERE tr.patient_id = patient_id_in;

	SELECT tr.result AS 'triglycerides', tr.date_recorded AS 'date' 
	FROM Triglycerides tr  
	WHERE tr.patient_id = patient_id_in
	AND tr.date_recorded = max_date;

	/*Retrieving TSH result*/

	SELECT MAX(ts.date_recorded) INTO max_date 
		FROM TSH ts 
		WHERE ts.patient_id = patient_id_in;

	SELECT ts.result AS 'tsh', tott.on_thyroid_treatment AS 'on thyroid treatment', 
	ts.date_recorded AS 'date' 
	FROM TSH ts 
	LEFT JOIN TSHOnThyroidTreatment tott 
	ON ts.patient_id = tott.patient_id 
	AND ts.date_recorded = tott.date_recorded 
	WHERE ts.patient_id = patient_id_in
	AND ts.date_recorded = max_date;

	/*Retrieving T4 result*/

	SELECT MAX(t.date_recorded) INTO max_date 
		FROM T4 t 
		WHERE t.patient_id = patient_id_in;

	SELECT t.result AS 't4', t.date_recorded AS 'date' 
	FROM T4 t  
	WHERE t.patient_id = patient_id_in
	AND t.date_recorded = max_date;

	/*Retrieving UACR result*/

	SELECT MAX(u.date_recorded) INTO max_date 
		FROM UACR u 
		WHERE u.patient_id = patient_id_in;

	SELECT u.result AS 'uacr', u.date_recorded AS 'date' 
	FROM UACR u  
	WHERE u.patient_id = patient_id_in
	AND u.date_recorded = max_date;

	/*Retrieving eGFR result*/

	SELECT MAX(e.date_recorded) INTO max_date 
		FROM EGFR e 
		WHERE e.patient_id = patient_id_in;

	SELECT e.result AS 'egfr', e.date_recorded AS 'date' 
	FROM EGFR e  
	WHERE e.patient_id = patient_id_in
	AND e.date_recorded = max_date;

	/*Retrieving creatinine result*/

	SELECT MAX(c.date_recorded) INTO max_date 
		FROM Creatinine c 
		WHERE c.patient_id = patient_id_in;

	SELECT c.result AS 'creatinine', c.date_recorded AS 'date' 
	FROM Creatinine c  
	WHERE c.patient_id = patient_id_in
	AND c.date_recorded = max_date;

	/*Retrieving BMI result*/

	SELECT MAX(b.date_recorded) INTO max_date 
		FROM BMI b 
		WHERE b.patient_id = patient_id_in;

	SELECT b.result AS 'bmi', b.date_recorded AS 'date' 
	FROM BMI b  
	WHERE b.patient_id = patient_id_in
	AND b.date_recorded = max_date;

	/*Retrieving waist result*/

	SELECT MAX(w.date_recorded) INTO max_date 
		FROM Waist w 
		WHERE w.patient_id = patient_id_in;

	SELECT w.result AS 'waist', w.date_recorded AS 'date' 
	FROM Waist w  
	WHERE w.patient_id = patient_id_in
	AND w.date_recorded = max_date;

	/*Retrieving blood pressure result*/

	SELECT MAX(bp.date_recorded) INTO max_date 
		FROM BloodPressure bp 
		WHERE bp.patient_id = patient_id_in;

	SELECT bp.result_s AS 'bp systole', bp.result_d AS 'bp diastole',
	bp.ace_or_arb AS 'ace or arb', bp.date_recorded AS 'date' 
	FROM BloodPressure bp  
	WHERE bp.patient_id = patient_id_in
	AND bp.date_recorded = max_date;

	/*Retrieving last class date*/

	SELECT MAX(lc.date_recorded) INTO max_date 
		FROM LastClass lc 
		WHERE lc.patient_id = patient_id_in;

	SELECT lc.date_recorded AS 'last class date' 
	FROM LastClass lc  
	WHERE lc.patient_id = patient_id_in
	AND lc.date_recorded = max_date;

	/*Retrieving eye screening result*/

	SELECT MAX(e.date_recorded) INTO max_date 
		FROM EyeScreening e 
		WHERE e.patient_id = patient_id_in;

	SELECT e.eye_exam_code AS 'eye exam', e.date_recorded AS 'date' 
	FROM EyeScreening e  
	WHERE e.patient_id = patient_id_in
	AND e.date_recorded = max_date;

	/*Retrieving foot screening result*/

	SELECT MAX(f.date_recorded) INTO max_date 
		FROM FootScreening f 
		WHERE f.patient_id = patient_id_in;

	SELECT f.risk_category AS 'foot screening', f.date_recorded AS 'date' 
	FROM FootScreening f 
	WHERE f.patient_id = patient_id_in
	AND f.date_recorded = max_date;

	/*Retrieving psychological screening result*/

	SELECT MAX(ps.date_recorded) INTO max_date 
		FROM PsychologicalScreening ps 
		WHERE ps.patient_id = patient_id_in;

	SELECT ps.phq_score AS 'psychological screening', ps.date_recorded AS 'date' 
	FROM PsychologicalScreening ps  
	WHERE ps.patient_id = patient_id_in
	AND ps.date_recorded = max_date;

	/*Retrieving physical activity*/

	SELECT MAX(pe.date_recorded) INTO max_date 
		FROM PhysicalActivity pe 
		WHERE pe.patient_id = patient_id_in;

	SELECT pe.min_per_week AS 'physical activity (min)', pe.date_recorded AS 'date' 
	FROM PhysicalActivity pe 
	WHERE pe.patient_id = patient_id_in
	AND pe.date_recorded = max_date;
	
	/*Retrieving influenza vaccine date*/

	SELECT MAX(iv.date_recorded) INTO max_date 
		FROM InfluenzaVaccine iv 
		WHERE iv.patient_id = patient_id_in;

	SELECT iv.date_recorded AS 'influenza vaccine date'
	FROM InfluenzaVaccine iv 
	WHERE iv.patient_id = patient_id_in
	AND iv.date_recorded = max_date;

	/*Retrieving pcv-13 vaccine date*/

	SELECT MAX(pc.date_recorded) INTO max_date 
		FROM PCV13 pc 
		WHERE pc.patient_id = patient_id_in;

	SELECT pc.date_recorded AS 'pcv-13 date'
	FROM PCV13 pc 
	WHERE pc.patient_id = patient_id_in
	AND pc.date_recorded = max_date;

	/*Retrieving ppsv-23 vaccine date*/

	SELECT MAX(pp.date_recorded) INTO max_date 
		FROM PPSV23 pp 
		WHERE pp.patient_id = patient_id_in;

	SELECT pp.date_recorded AS 'ppsv-23 date'
	FROM PPSV23 pp 
	WHERE pp.patient_id = patient_id_in
	AND pp.date_recorded = max_date;

	/*Retrieving Hepatitis B vaccine date*/

	SELECT MAX(hb.date_recorded) INTO max_date 
		FROM HepatitisB hb 
		WHERE hb.patient_id = patient_id_in;

	SELECT hb.date_recorded AS 'hep b date'
	FROM HepatitisB hb 
	WHERE hb.patient_id = patient_id_in
	AND hb.date_recorded = max_date;

	/*Retrieving TDAP vaccine date*/

	SELECT MAX(td.date_recorded) INTO max_date 
		FROM TDAP td 
		WHERE td.patient_id = patient_id_in;

	SELECT td.date_recorded AS 'tdap date'
	FROM TDAP td 
	WHERE td.patient_id = patient_id_in
	AND td.date_recorded = max_date;

	/*Retrieving zoster vaccine date*/

	SELECT MAX(z.date_recorded) INTO max_date 
		FROM Zoster z 
		WHERE z.patient_id = patient_id_in;

	SELECT z.date_recorded AS 'zoster date'
	FROM Zoster z 
	WHERE z.patient_id = patient_id_in
	AND z.date_recorded = max_date;

	/*Retrieving smoking result*/

	SELECT MAX(sm.date_recorded) INTO max_date 
		FROM SmokingCessation sm 
		WHERE sm.patient_id = patient_id_in;

	SELECT sm.smoker AS 'smoker', sm.date_recorded AS 'date'
	FROM SmokingCessation sm 
	WHERE sm.patient_id = patient_id_in
	AND sm.date_recorded = max_date;

	/*Retrieving telephone follow-up result*/

	SELECT MAX(t.date_recorded) INTO max_date 
		FROM TelephoneFollowUp t 
		WHERE t.patient_id = patient_id_in;

	SELECT t.follow_up_code AS 'follow-up', t.date_recorded AS 'date'
	FROM TelephoneFollowUp t 
	WHERE t.patient_id = patient_id_in
	AND t.date_recorded = max_date;

	/*Retrieving AST result*/

	SELECT MAX(a.date_recorded) INTO max_date 
		FROM AST a 
		WHERE a.patient_id = patient_id_in;

	SELECT a.result AS 'ast', a.date_recorded AS 'date'
	FROM AST a 
	WHERE a.patient_id = patient_id_in
	AND a.date_recorded = max_date;

	/*Retrieving ALT result*/

	SELECT MAX(al.date_recorded) INTO max_date 
		FROM ALT al 
		WHERE al.patient_id = patient_id_in;

	SELECT al.result AS 'alt', al.date_recorded AS 'date'
	FROM ALT al 
	WHERE al.patient_id = patient_id_in
	AND al.date_recorded = max_date;

	/*Retrieving PSA result*/

	SELECT MAX(p.date_recorded) INTO max_date 
		FROM PSA p 
		WHERE p.patient_id = patient_id_in;

	SELECT p.result AS 'psa', p.date_recorded AS 'date'
	FROM PSA p 
	WHERE p.patient_id = patient_id_in
	AND p.date_recorded = max_date;

	/*Retrieving ER visit date*/

	SELECT MAX(e.date_recorded) INTO max_date 
		FROM ER e 
		WHERE e.patient_id = patient_id_in;

	SELECT e.date_recorded AS 'ER visit date'
	FROM ER e 
	WHERE e.patient_id = patient_id_in
	AND e.date_recorded = max_date;

	/*Retrieving the patient healthy target status*/

	CALL updateTargetStatus(patient_id_in, string_in, @success);

	SELECT hts.measurement AS 'measurement', 
	hts.out_of_target AS 'out of target' 
	FROM HealthyTargetStatus hts 
	WHERE hts.patient_id = patient_id_in;

	SELECT lgf.ac AS 'ac' 
	FROM LastGlucoseFasting lgf 
	WHERE lgf.patient_id = patient_id_in;

	/*Retrieving the patient treatment*/

	SELECT MAX(t.date_recorded) INTO max_date 
		FROM PatientRx t
		WHERE t.patient_id = patient_id_in;

	SELECT t.rx_class AS 'rx class', 
		t.date_recorded AS 'date reviewed'
	FROM PatientRx t 
	WHERE t.patient_id = patient_id_in 
	AND t.date_recorded = max_date;	

	SELECT MAX(pm.date_recorded) INTO max_date 
		FROM PatientMed pm
		WHERE pm.patient_id = patient_id_in;

	SELECT pm.med_id AS 'med id', 
		pm.date_recorded AS 'date reviewed'
	FROM PatientMed pm 
	WHERE pm.patient_id = patient_id_in 
	AND pm.date_recorded = max_date;
	
	SET proc_success = 1;

	COMMIT;

END ; //
DELIMITER ; 	

/**
*USAGE: To retrieve all a1c results for a patient
* CALL getA1C(?, ?)
* 1 = patient_id
* 2 = procedure success
*/

DELIMITER //
CREATE PROCEDURE getA1C
	(IN patient_id_in INT, OUT proc_success TINYINT(1))
BEGIN

DECLARE EXIT HANDLER FOR SQLEXCEPTION ROLLBACK;
DECLARE EXIT HANDLER FOR SQLWARNING ROLLBACK;

START TRANSACTION;

SET proc_success = 0;

SELECT a.result AS 'result', a.date_recorded AS 'date recorded', 
a.poc AS 'poc'  
FROM A1C a 
WHERE a.patient_id = patient_id_in 
ORDER BY a.date_recorded DESC;

SET proc_success = 1;

COMMIT;
END ; //
DELIMITER ;

/**
*USAGE: To retrieve all PSA results for a patient
* CALL getPsa(?, ?)
* 1 = patient_id
* 2 = procedure success
*/

DELIMITER //
CREATE PROCEDURE getPsa
	(IN patient_id_in INT, OUT proc_success TINYINT(1))
BEGIN

DECLARE EXIT HANDLER FOR SQLEXCEPTION ROLLBACK;
DECLARE EXIT HANDLER FOR SQLWARNING ROLLBACK;

START TRANSACTION;

SET proc_success = 0;

SELECT p.result AS 'result', p.date_recorded AS 'date recorded' 
FROM PSA p 
WHERE p.patient_id = patient_id_in 
ORDER BY p.date_recorded DESC;

SET proc_success = 1;

COMMIT;
END ; //
DELIMITER ;

/**
*USAGE: To retrieve all ALT results for a patient
* CALL getAlt(?, ?)
* 1 = patient_id
* 2 = procedure success
*/

DELIMITER //
CREATE PROCEDURE getAlt
	(IN patient_id_in INT, OUT proc_success TINYINT(1))
BEGIN

DECLARE EXIT HANDLER FOR SQLEXCEPTION ROLLBACK;
DECLARE EXIT HANDLER FOR SQLWARNING ROLLBACK;

START TRANSACTION;

SET proc_success = 0;

SELECT al.result AS 'result', al.date_recorded AS 'date recorded' 
FROM ALT al 
WHERE al.patient_id = patient_id_in 
ORDER BY al.date_recorded DESC;

SET proc_success = 1;

COMMIT;
END ; //
DELIMITER ;

/**
*USAGE: To retrieve all AST results for a patient
* CALL getAst(?, ?)
* 1 = patient_id
* 2 = procedure success
*/

DELIMITER //
CREATE PROCEDURE getAst
	(IN patient_id_in INT, OUT proc_success TINYINT(1))
BEGIN

DECLARE EXIT HANDLER FOR SQLEXCEPTION ROLLBACK;
DECLARE EXIT HANDLER FOR SQLWARNING ROLLBACK;

START TRANSACTION;

SET proc_success = 0;

SELECT a.result AS 'result', a.date_recorded AS 'date recorded' 
FROM AST a 
WHERE a.patient_id = patient_id_in 
ORDER BY a.date_recorded DESC;

SET proc_success = 1;

COMMIT;
END ; //
DELIMITER ;

/**
*USAGE: To retrieve all glucose results for a patient
* CALL getGlucose(?, ?)
* 1 = patient_id
* 2 = procedure success
*/

DELIMITER //
CREATE PROCEDURE getGlucose
	(IN patient_id_in INT, OUT proc_success TINYINT(1))
BEGIN

DECLARE EXIT HANDLER FOR SQLEXCEPTION ROLLBACK;
DECLARE EXIT HANDLER FOR SQLWARNING ROLLBACK;

START TRANSACTION;

SET proc_success = 0;

SELECT g.result AS 'result', g.date_recorded AS 'date recorded' 
FROM Glucose g  
WHERE g.patient_id = patient_id_in 
ORDER BY g.date_recorded DESC;

SET proc_success = 1;

COMMIT;
END ; //
DELIMITER ;

/**
*USAGE: To retrieve all ldl results for a patient
* CALL getLDL(?, ?)
* 1 = patient_id
* 2 = procedure success
*/

DELIMITER //
CREATE PROCEDURE getLDL
	(IN patient_id_in INT, OUT proc_success TINYINT(1))
BEGIN

DECLARE EXIT HANDLER FOR SQLEXCEPTION ROLLBACK;
DECLARE EXIT HANDLER FOR SQLWARNING ROLLBACK;

START TRANSACTION;

SET proc_success = 0;

SELECT l.result AS 'result', l.post_mi AS 'post mi', 
los.on_statin AS 'on statin', l.date_recorded AS 'date recorded' 
FROM LDL l 
LEFT JOIN LDLOnStatin los 
ON l.patient_id = los.patient_id 
AND l.date_recorded = los.date_recorded 
WHERE l.patient_id = patient_id_in 
ORDER BY l.date_recorded DESC;

SET proc_success = 1;

COMMIT;
END ; //
DELIMITER ;

/**
*USAGE: To retrieve all hdl results for a patient
* CALL getHDL(?, ?)
* 1 = patient_id
* 2 = procedure success
*/

DELIMITER //
CREATE PROCEDURE getHDL
	(IN patient_id_in INT, OUT proc_success TINYINT(1))
BEGIN

DECLARE EXIT HANDLER FOR SQLEXCEPTION ROLLBACK;
DECLARE EXIT HANDLER FOR SQLWARNING ROLLBACK;

START TRANSACTION;

SET proc_success = 0;

SELECT h.result AS 'result', h.date_recorded AS 'date recorded' 
FROM HDL h 
WHERE h.patient_id = patient_id_in 
ORDER BY h.date_recorded DESC;

SET proc_success = 1;

COMMIT;
END ; //
DELIMITER ;

/**
*USAGE: To retrieve all triglyceride results for a patient
* CALL getTryglycerides(?, ?)
* 1 = patient_id
* 2 = procedure success
*/

DELIMITER //
CREATE PROCEDURE getTriglycerides
	(IN patient_id_in INT, OUT proc_success TINYINT(1))
BEGIN

DECLARE EXIT HANDLER FOR SQLEXCEPTION ROLLBACK;
DECLARE EXIT HANDLER FOR SQLWARNING ROLLBACK;

START TRANSACTION;

SET proc_success = 0;

SELECT t.result AS 'result', 
t.date_recorded AS 'date recorded' 
FROM Triglycerides t 
WHERE t.patient_id = patient_id_in 
ORDER BY t.date_recorded DESC;

SET proc_success = 1;

COMMIT;
END ; //
DELIMITER ;

/**
*USAGE: To retrieve all tsh results for a patient
* CALL getTSH(?, ?)
* 1 = patient_id
* 2 = procedure success
*/

DELIMITER //
CREATE PROCEDURE getTSH
	(IN patient_id_in INT, OUT proc_success TINYINT(1))
BEGIN

DECLARE EXIT HANDLER FOR SQLEXCEPTION ROLLBACK;
DECLARE EXIT HANDLER FOR SQLWARNING ROLLBACK;

START TRANSACTION;

SET proc_success = 0;

SELECT ts.result AS 'result', tott.on_thyroid_treatment AS 'on thyroid treatment', 
ts.date_recorded AS 'date recorded' 
FROM TSH ts 
LEFT JOIN TSHOnThyroidTreatment tott 
ON ts.patient_id = tott.patient_id 
AND ts.date_recorded = tott.date_recorded 
WHERE ts.patient_id = patient_id_in 
ORDER BY ts.date_recorded DESC;

SET proc_success = 1;

COMMIT;
END ; //
DELIMITER ;

/**
*USAGE: To retrieve all t4 results for a patient
* CALL getT4(?, ?)
* 1 = patient_id
* 2 = procedure success
*/

DELIMITER //
CREATE PROCEDURE getT4
	(IN patient_id_in INT, OUT proc_success TINYINT(1))
BEGIN

DECLARE EXIT HANDLER FOR SQLEXCEPTION ROLLBACK;
DECLARE EXIT HANDLER FOR SQLWARNING ROLLBACK;

START TRANSACTION;

SET proc_success = 0;

SELECT t.result AS 'result', t.date_recorded AS 'date recorded' 
FROM T4 t 
WHERE t.patient_id = patient_id_in 
ORDER BY t.date_recorded DESC;

SET proc_success = 1;

COMMIT;
END ; //
DELIMITER ;

/**
*USAGE: To retrieve all uacr results for a patient
* CALL getUACR(?, ?)
* 1 = patient_id
* 2 = procedure success
*/

DELIMITER //
CREATE PROCEDURE getUACR
	(IN patient_id_in INT, OUT proc_success TINYINT(1))
BEGIN

DECLARE EXIT HANDLER FOR SQLEXCEPTION ROLLBACK;
DECLARE EXIT HANDLER FOR SQLWARNING ROLLBACK;

START TRANSACTION;

SET proc_success = 0;

SELECT u.result AS 'result', u.date_recorded AS 'date recorded' 
FROM UACR u 
WHERE u.patient_id = patient_id_in 
ORDER BY u.date_recorded DESC;

SET proc_success = 1;

COMMIT;
END ; //
DELIMITER ;

/**
*USAGE: To retrieve all egfr results for a patient
* CALL getEGFR(?, ?)
* 1 = patient_id
* 2 = procedure success
*/

DELIMITER //
CREATE PROCEDURE getEGFR
	(IN patient_id_in INT, OUT proc_success TINYINT(1))
BEGIN

DECLARE EXIT HANDLER FOR SQLEXCEPTION ROLLBACK;
DECLARE EXIT HANDLER FOR SQLWARNING ROLLBACK;

START TRANSACTION;

SET proc_success = 0;

SELECT e.result AS 'result', e.date_recorded AS 'date recorded' 
FROM EGFR e 
WHERE e.patient_id = patient_id_in 
ORDER BY e.date_recorded DESC;

SET proc_success = 1;

COMMIT;
END ; //
DELIMITER ;

/**
*USAGE: To retrieve all creatinine results for a patient
* CALL getCreatinine(?, ?)
* 1 = patient_id
* 2 = procedure success
*/

DELIMITER //
CREATE PROCEDURE getCreatinine
	(IN patient_id_in INT, OUT proc_success TINYINT(1))
BEGIN

DECLARE EXIT HANDLER FOR SQLEXCEPTION ROLLBACK;
DECLARE EXIT HANDLER FOR SQLWARNING ROLLBACK;

START TRANSACTION;

SET proc_success = 0;

SELECT c.result AS 'result', c.date_recorded AS 'date recorded' 
FROM Creatinine c 
WHERE c.patient_id = patient_id_in 
ORDER BY c.date_recorded DESC;

SET proc_success = 1;

COMMIT;
END ; //
DELIMITER ;

/**
*USAGE: To retrieve all bmi results for a patient
* CALL getBMI(?, ?)
* 1 = patient_id
* 2 = procedure success
*/

DELIMITER //
CREATE PROCEDURE getBMI
	(IN patient_id_in INT, OUT proc_success TINYINT(1))
BEGIN

DECLARE EXIT HANDLER FOR SQLEXCEPTION ROLLBACK;
DECLARE EXIT HANDLER FOR SQLWARNING ROLLBACK;

START TRANSACTION;

SET proc_success = 0;

SELECT b.result AS 'result', b.date_recorded AS 'date recorded' 
FROM BMI b 
WHERE b.patient_id = patient_id_in 
ORDER BY b.date_recorded DESC;

SET proc_success = 1;

COMMIT;
END ; //
DELIMITER ;

/**
*USAGE: To retrieve all waist measurement results for a patient
* CALL getWaist(?, ?)
* 1 = patient_id
* 2 = procedure success
*/

DELIMITER //
CREATE PROCEDURE getWaist
	(IN patient_id_in INT, OUT proc_success TINYINT(1))
BEGIN

DECLARE EXIT HANDLER FOR SQLEXCEPTION ROLLBACK;
DECLARE EXIT HANDLER FOR SQLWARNING ROLLBACK;

START TRANSACTION;

SET proc_success = 0;

SELECT w.result AS 'result', w.date_recorded AS 'date recorded' 
FROM Waist w 
WHERE w.patient_id = patient_id_in 
ORDER BY w.date_recorded DESC;

SET proc_success = 1;

COMMIT;
END ; //
DELIMITER ;

/**
*USAGE: To retrieve all blood pressure results for a patient
* CALL getBP(?, ?)
* 1 = patient_id
* 2 = procedure success
*/

DELIMITER //
CREATE PROCEDURE getBP
	(IN patient_id_in INT, OUT proc_success TINYINT(1))
BEGIN

DECLARE EXIT HANDLER FOR SQLEXCEPTION ROLLBACK;
DECLARE EXIT HANDLER FOR SQLWARNING ROLLBACK;

START TRANSACTION;

SET proc_success = 0;

SELECT b.result_s AS 'systole', b.result_d AS 'diastole', 
b.ace_or_arb AS 'ace or arb', b.date_recorded AS 'date recorded' 
FROM BloodPressure b 
WHERE b.patient_id = patient_id_in 
ORDER BY b.date_recorded DESC;

SET proc_success = 1;

COMMIT;
END ; //
DELIMITER ;

/**
*USAGE: To retrieve all class attendance results for a patient
* CALL getClass(?, ?)
* 1 = patient_id
* 2 = procedure success
*/

DELIMITER //
CREATE PROCEDURE getClass
	(IN patient_id_in INT, OUT proc_success TINYINT(1))
BEGIN

DECLARE EXIT HANDLER FOR SQLEXCEPTION ROLLBACK;
DECLARE EXIT HANDLER FOR SQLWARNING ROLLBACK;

START TRANSACTION;

SET proc_success = 0;

SELECT lc.date_recorded AS 'date attended'
FROM LastClass lc 
WHERE lc.patient_id = patient_id_in 
ORDER BY lc.date_recorded DESC;

SET proc_success = 1;

COMMIT;
END ; //
DELIMITER ;

/**
*USAGE: To retrieve all eye screening results for a patient
* CALL getEye(?, ?)
* 1 = patient_id
* 2 = procedure success
*/

DELIMITER //
CREATE PROCEDURE getEye
	(IN patient_id_in INT, OUT proc_success TINYINT(1))
BEGIN

DECLARE EXIT HANDLER FOR SQLEXCEPTION ROLLBACK;
DECLARE EXIT HANDLER FOR SQLWARNING ROLLBACK;

START TRANSACTION;

SET proc_success = 0;

SELECT e.eye_exam_code AS 'eye exam code', 
eed.definition AS 'definition', 
e.date_recorded AS 'date recorded' 
FROM EyeScreening e, EyeExamDefinition eed 
WHERE e.patient_id = patient_id_in 
AND e.eye_exam_code = eed.eye_exam_code 
ORDER BY e.date_recorded DESC;

SET proc_success = 1;

COMMIT;
END ; //
DELIMITER ;

/**
*USAGE: To retrieve all foot screening results for a patient
* CALL getFoot(?, ?)
* 1 = patient_id
* 2 = procedure success
*/

DELIMITER //
CREATE PROCEDURE getFoot
	(IN patient_id_in INT, OUT proc_success TINYINT(1))
BEGIN

DECLARE EXIT HANDLER FOR SQLEXCEPTION ROLLBACK;
DECLARE EXIT HANDLER FOR SQLWARNING ROLLBACK;

START TRANSACTION;

SET proc_success = 0;

SELECT f.risk_category AS 'risk category', 
ferd.definition AS 'definition', 
f.date_recorded AS 'date recorded' 
FROM FootScreening f, FootExamRiskDefinition ferd 
WHERE f.patient_id = patient_id_in 
AND f.risk_category = ferd.risk_category 
ORDER BY f.date_recorded DESC;

SET proc_success = 1;

COMMIT;
END ; //
DELIMITER ;

/**
*USAGE: To retrieve all psychological screening results for a patient
* CALL getPsychologicalScreening(?, ?)
* 1 = patient_id
* 2 = procedure success
*/

DELIMITER //
CREATE PROCEDURE getPsychologicalScreening
	(IN patient_id_in INT, OUT proc_success TINYINT(1))
BEGIN

DECLARE EXIT HANDLER FOR SQLEXCEPTION ROLLBACK;
DECLARE EXIT HANDLER FOR SQLWARNING ROLLBACK;

START TRANSACTION;

SET proc_success = 0;

SELECT ps.phq_score AS 'phq score', 
phq.severity AS 'severity', 
phq.proposed_actions AS 'proposed actions', 
ps.date_recorded AS 'date recorded' 
FROM PsychologicalScreening ps, PHQ9 phq 
WHERE ps.patient_id = patient_id_in 
AND ps.phq_score = phq.phq_score 
ORDER BY ps.date_recorded DESC;

SET proc_success = 1;

COMMIT;
END ; //
DELIMITER ;

/**
*USAGE: To retrieve all recorded physical activity for a patient
* CALL getPhysicalActivity(?, ?)
* 1 = patient_id
* 2 = procedure success
*/

DELIMITER //
CREATE PROCEDURE getPhysicalActivity
	(IN patient_id_in INT, OUT proc_success TINYINT(1))
BEGIN

DECLARE EXIT HANDLER FOR SQLEXCEPTION ROLLBACK;
DECLARE EXIT HANDLER FOR SQLWARNING ROLLBACK;

START TRANSACTION;

SET proc_success = 0;

SELECT pe.min_per_week AS 'min per week', pe.date_recorded AS 'date recorded' 
FROM PhysicalActivity pe 
WHERE pe.patient_id = patient_id_in 
ORDER BY pe.date_recorded DESC;

SET proc_success = 1;

COMMIT;
END ; //
DELIMITER ;

/**
*USAGE: To retrieve all telephone follow-up results for a patient
* CALL getTelephoneFollowUp(?, ?)
* 1 = patient_id
* 2 = procedure success
*/

DELIMITER //
CREATE PROCEDURE getTelephoneFollowUp
	(IN patient_id_in INT, OUT proc_success TINYINT(1))
BEGIN

DECLARE EXIT HANDLER FOR SQLEXCEPTION ROLLBACK;
DECLARE EXIT HANDLER FOR SQLWARNING ROLLBACK;

START TRANSACTION;

SET proc_success = 0;

SELECT t.follow_up_code AS 'follow up code', 
td.definition AS 'definition', 
t.date_recorded AS 'date recorded' 
FROM TelephoneFollowUp t, TelephoneFollowUpDefinition td 
WHERE t.patient_id = patient_id_in 
AND t.follow_up_code = td.follow_up_code 
ORDER BY t.date_recorded DESC;

SET proc_success = 1;

COMMIT;
END ; //
DELIMITER ;

/**
*USAGE: To retrieve all patient-reported compliance for a patient
* CALL getCompliance(?, ?)
* 1 = patient_id
* 2 = procedure success
*/

DELIMITER //
CREATE PROCEDURE getCompliance
	(IN patient_id_in INT, OUT proc_success TINYINT(1))
BEGIN

DECLARE EXIT HANDLER FOR SQLEXCEPTION ROLLBACK;
DECLARE EXIT HANDLER FOR SQLWARNING ROLLBACK;

START TRANSACTION;

SET proc_success = 0;

SELECT c.result AS 'result', c.date_recorded AS 'date recorded' 
FROM Compliance c 
WHERE c.patient_id = patient_id_in 
ORDER BY c.date_recorded DESC;

SET proc_success = 1;

COMMIT;
END ; //
DELIMITER ;

/**
*USAGE: To retrieve all ER visit information for a patient
* CALL getER(?, ?)
* 1 = patient_id
* 2 = procedure success
*/

DELIMITER //
CREATE PROCEDURE getER
	(IN patient_id_in INT, OUT proc_success TINYINT(1))
BEGIN

DECLARE EXIT HANDLER FOR SQLEXCEPTION ROLLBACK;
DECLARE EXIT HANDLER FOR SQLWARNING ROLLBACK;

START TRANSACTION;

SET proc_success = 0;

SELECT e.date_recorded AS 'date recorded' 
FROM ER e 
WHERE e.patient_id = patient_id_in 
ORDER BY e.date_recorded DESC;

SET proc_success = 1;

COMMIT;
END ; //
DELIMITER ;

/**
*USAGE: To retrieve smoking status information for a patient
* CALL getSmoker(?, ?)
* 1 = patient_id
* 2 = procedure success
*/

DELIMITER //
CREATE PROCEDURE getSmoker
	(IN patient_id_in INT, OUT proc_success TINYINT(1))
BEGIN

DECLARE EXIT HANDLER FOR SQLEXCEPTION ROLLBACK;
DECLARE EXIT HANDLER FOR SQLWARNING ROLLBACK;

START TRANSACTION;

SET proc_success = 0;

SELECT s.smoker AS 'smoker', 
s.date_recorded AS 'date recorded' 
FROM SmokingCessation s 
WHERE s.patient_id = patient_id_in 
ORDER BY s.date_recorded DESC;

SET proc_success = 1;

COMMIT;
END ; //
DELIMITER ;

/**
*USAGE: To retrieve influenza vaccination status for a patient
* CALL getInfluenza(?, ?)
* 1 = patient_id
* 2 = procedure success
*/

DELIMITER //
CREATE PROCEDURE getInfluenza
	(IN patient_id_in INT, OUT proc_success TINYINT(1))
BEGIN

DECLARE EXIT HANDLER FOR SQLEXCEPTION ROLLBACK;
DECLARE EXIT HANDLER FOR SQLWARNING ROLLBACK;

START TRANSACTION;

SET proc_success = 0;

SELECT iv.date_recorded AS 'date recorded' 
FROM InfluenzaVaccine iv 
WHERE iv.patient_id = patient_id_in 
ORDER BY iv.date_recorded DESC;

SET proc_success = 1;

COMMIT;
END ; //
DELIMITER ;

/**
*USAGE: To retrieve pcv-13 vaccination status for a patient
* CALL getPCV13(?, ?)
* 1 = patient_id
* 2 = procedure success
*/

DELIMITER //
CREATE PROCEDURE getPCV13
	(IN patient_id_in INT, OUT proc_success TINYINT(1))
BEGIN

DECLARE EXIT HANDLER FOR SQLEXCEPTION ROLLBACK;
DECLARE EXIT HANDLER FOR SQLWARNING ROLLBACK;

START TRANSACTION;

SET proc_success = 0;

SELECT p.date_recorded AS 'date recorded' 
FROM PCV13 p 
WHERE p.patient_id = patient_id_in 
ORDER BY p.date_recorded DESC;

SET proc_success = 1;

COMMIT;
END ; //
DELIMITER ;

/**
*USAGE: To retrieve ppsv23 vaccination status for a patient
* CALL getPPSV23(?, ?)
* 1 = patient_id
* 2 = procedure success
*/

DELIMITER //
CREATE PROCEDURE getPPSV23
	(IN patient_id_in INT, OUT proc_success TINYINT(1))
BEGIN

DECLARE EXIT HANDLER FOR SQLEXCEPTION ROLLBACK;
DECLARE EXIT HANDLER FOR SQLWARNING ROLLBACK;

START TRANSACTION;

SET proc_success = 0;

SELECT p.date_recorded AS 'date recorded' 
FROM PPSV23 p 
WHERE p.patient_id = patient_id_in 
ORDER BY p.date_recorded DESC;

SET proc_success = 1;

COMMIT;
END ; //
DELIMITER ;

/**
*USAGE: To retrieve tdap vaccination status for a patient
* CALL getTDAP(?, ?)
* 1 = patient_id
* 2 = procedure success
*/

DELIMITER //
CREATE PROCEDURE getTDAP
	(IN patient_id_in INT, OUT proc_success TINYINT(1))
BEGIN

DECLARE EXIT HANDLER FOR SQLEXCEPTION ROLLBACK;
DECLARE EXIT HANDLER FOR SQLWARNING ROLLBACK;

START TRANSACTION;

SET proc_success = 0;

SELECT t.date_recorded AS 'date recorded' 
FROM TDAP t 
WHERE t.patient_id = patient_id_in 
ORDER BY t.date_recorded DESC;

SET proc_success = 1;

COMMIT;
END ; //
DELIMITER ;

/**
*USAGE: To retrieve hepatitis b vaccination status for a patient
* CALL getHepB(?, ?)
* 1 = patient_id
* 2 = procedure success
*/

DELIMITER //
CREATE PROCEDURE getHepB
	(IN patient_id_in INT, OUT proc_success TINYINT(1))
BEGIN

DECLARE EXIT HANDLER FOR SQLEXCEPTION ROLLBACK;
DECLARE EXIT HANDLER FOR SQLWARNING ROLLBACK;

START TRANSACTION;

SET proc_success = 0;

SELECT h.date_recorded AS 'date recorded' 
FROM HepatitisB h 
WHERE h.patient_id = patient_id_in 
ORDER BY h.date_recorded DESC;

SET proc_success = 1;

COMMIT;
END ; //
DELIMITER ;

/**
*USAGE: To retrieve zoster vaccination status for a patient
* CALL getZoster(?, ?)
* 1 = patient_id
* 2 = procedure success
*/

DELIMITER //
CREATE PROCEDURE getZoster
	(IN patient_id_in INT, OUT proc_success TINYINT(1))
BEGIN

DECLARE EXIT HANDLER FOR SQLEXCEPTION ROLLBACK;
DECLARE EXIT HANDLER FOR SQLWARNING ROLLBACK;

START TRANSACTION;

SET proc_success = 0;

SELECT z.date_recorded AS 'date recorded' 
FROM Zoster z 
WHERE z.patient_id = patient_id_in 
ORDER BY z.date_recorded DESC;

SET proc_success = 1;

COMMIT;
END ; //
DELIMITER ;

/**
*USAGE: To retrieve treatment information for a patient
* CALL getTreatment(?, ?)
* 1 = patient_id
* 2 = procedure success
*/

DELIMITER //
CREATE PROCEDURE getTreatment
	(IN patient_id_in INT, OUT proc_success TINYINT(1))
BEGIN

DECLARE EXIT HANDLER FOR SQLEXCEPTION ROLLBACK;
DECLARE EXIT HANDLER FOR SQLWARNING ROLLBACK;

START TRANSACTION;

SET proc_success = 0;

SELECT tr.rx_class AS 'rx class', th.therapy_type AS 'therapy type', 
tr.date_recorded AS 'date recorded' 
FROM PatientRx tr, Therapy th 
WHERE tr.patient_id = patient_id_in 
AND tr.rx_class = th.rx_class 
ORDER BY tr.date_recorded DESC;

SELECT tr.med_id AS 'med id', med.med_name AS 'med name', 
med.med_class AS 'med class', 
tr.date_recorded AS 'date recorded' 
FROM PatientMed tr, Medication med 
WHERE tr.patient_id = patient_id_in 
AND tr.med_id = med.med_id 
ORDER BY tr.date_recorded DESC;

SET proc_success = 1;

COMMIT;
END ; //
DELIMITER ;

/**
*USAGE: To retrieve topic-specific notes for a patient
* CALL getNotes(?, ?, ?)
* 1 = patient_id
* 2 = note topic
* 3 = procedure success
*/

DELIMITER //
CREATE PROCEDURE getNotes
	(IN patient_id_in INT, IN note_topic VARCHAR(50), OUT proc_success TINYINT(1))
BEGIN

DECLARE EXIT HANDLER FOR SQLEXCEPTION ROLLBACK;
DECLARE EXIT HANDLER FOR SQLWARNING ROLLBACK;

START TRANSACTION;

SET proc_success = 0;

SELECT n.date_recorded AS 'date recorded', 
n.note AS 'note' 
FROM Note n 
WHERE n.patient_id = patient_id_in 
AND n.topic = note_topic 
ORDER BY n.date_recorded DESC;

SET proc_success = 1;

COMMIT;
END ; //
DELIMITER ;

/**
*USAGE: To retrieve all notes for a patient
* CALL getAllNotes(?, ?)
* 1 = patient_id
* 2 = procedure success
*/

DELIMITER //
CREATE PROCEDURE getAllNotes
	(IN patient_id_in INT, OUT proc_success TINYINT(1))
BEGIN

DECLARE EXIT HANDLER FOR SQLEXCEPTION ROLLBACK;
DECLARE EXIT HANDLER FOR SQLWARNING ROLLBACK;

START TRANSACTION;

SET proc_success = 0;

SELECT n.date_recorded AS 'date recorded', 
n.topic AS 'topic', 
n.note AS 'note' 
FROM Note n 
WHERE n.patient_id = patient_id_in 
ORDER BY n.date_recorded DESC;

SET proc_success = 1;

COMMIT;
END ; //
DELIMITER ;

/**
*USAGE: To retrieve all note topics
* CALL getNoteTopics(?)
* 1 = procedure success
*/

DELIMITER //
CREATE PROCEDURE getNoteTopics
	(OUT proc_success TINYINT(1))
BEGIN

DECLARE EXIT HANDLER FOR SQLEXCEPTION ROLLBACK;
DECLARE EXIT HANDLER FOR SQLWARNING ROLLBACK;

START TRANSACTION;

SET proc_success = 0;

SELECT n.topic AS 'topic' 
FROM NoteTopic n 
ORDER BY n.topic;

SET proc_success = 1;

COMMIT;
END ; //
DELIMITER ;

/**
*USAGE: to add patient demographic information to the database
*CALL addPatient(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);
*1 = first_name
*2 = last_name
*3 = birth_date
*4 = address
*5 = contact_number
*6 = gender
*7 = race
*8 = email
*9 = language
*10 = start_date
*11 = clinic_id_in
*12 = string
*13 = proc_success
*/
DELIMITER //
CREATE PROCEDURE addPatient
	(IN first_name_in VARCHAR(50), IN last_name_in VARCHAR(50), 
	IN birth_date_in DATE, IN address_in VARCHAR(255), IN contact_number_in VARCHAR(50), 
	IN gender_in VARCHAR(50), IN race_in VARCHAR(50), IN email_in VARCHAR(255), 
	IN language_in VARCHAR(50), IN start_date_in DATE, IN clinic_id_in INT, 
	IN string_in VARCHAR(64), OUT proc_success TINYINT(1))
BEGIN
	
	DECLARE EXIT HANDLER FOR SQLEXCEPTION ROLLBACK;
	DECLARE EXIT HANDLER FOR SQLWARNING ROLLBACK;

	START TRANSACTION;

	SET proc_success = 0;

	/*Adding patient demographic information*/

	INSERT INTO Patient VALUES (NULL, AES_ENCRYPT(first_name_in, string_in), 
		AES_ENCRYPT(last_name_in, string_in), 
		AES_ENCRYPT(birth_date_in, string_in), 
		AES_ENCRYPT(contact_number_in, string_in), 
		AES_ENCRYPT(gender_in, string_in), 
		AES_ENCRYPT(race_in, string_in), 
		AES_ENCRYPT(start_date_in, string_in));
	INSERT INTO PatientClinic VALUES (LAST_INSERT_ID(), clinic_id_in);

	IF address_in IS NOT NULL THEN 
		INSERT INTO PatientAddress VALUES (LAST_INSERT_ID(), 
			AES_ENCRYPT(address_in, string_in));
	END IF;
	IF email_in IS NOT NULL THEN 
		INSERT INTO PatientEmailAddress VALUES (LAST_INSERT_ID(), 
			AES_ENCRYPT(email_in, string_in));
	END IF;
	IF language_in IS NOT NULL THEN 
		INSERT INTO PatientLanguage VALUES (LAST_INSERT_ID(), 
			language_in);
	END IF;

SET proc_success = 1;

COMMIT;
END ; //
DELIMITER ;

/**
*USAGE: to authenticate a user on login
*CALL authenticateUser(?, ?, ?, ?, ?);
*1 = user_name
*2 = password
*3 = authentic
*4 = change_status
*5 = proc_success
*/
DELIMITER //
CREATE PROCEDURE authenticateUser
	(IN user_name_in VARCHAR(50), IN password_in VARCHAR(64), 
	OUT authentic_out TINYINT(1), OUT change_status_out TINYINT(1), 
	OUT proc_success TINYINT(1))
BEGIN
	
	DECLARE EXIT HANDLER FOR SQLEXCEPTION ROLLBACK;
	DECLARE EXIT HANDLER FOR SQLWARNING ROLLBACK;

	START TRANSACTION;

	SET proc_success = 0;

	/*Authenticating user*/

	SELECT COUNT(*) INTO authentic_out 
		FROM UserCredentials uc 
		WHERE uc.user_name = user_name_in 
		AND uc.password = password_in;

	IF authentic_out = 1 THEN 
		UPDATE User u SET last_login = NOW() 
		WHERE u.user_name = user_name_in;
		END IF;

	/*Getting status on password change*/

	SELECT COUNT(*) INTO change_status_out 
		FROM UserCredentials uc 
		WHERE uc.user_name = user_name_in 
		AND uc.change_password = 1;

SELECT authentic_out AS 'authentic';
SELECT change_status_out AS 'change password';

SET proc_success = 1;

COMMIT;
END ; //
DELIMITER ;

/**
*USAGE: to match a given registration key with a key in the database
*CALL checkRegistrationKey(?, ?, ?);
*1 = registration_key
*2 = clinic_id
*3 = proc_success
*/
DELIMITER //
CREATE PROCEDURE checkRegistrationKey
	(IN registration_key_in VARCHAR(64), OUT clinic_id_out INTEGER, 
	OUT proc_success TINYINT(1))
BEGIN
	DECLARE clinic_count INT DEFAULT 0;
	
	DECLARE EXIT HANDLER FOR SQLEXCEPTION ROLLBACK;
	DECLARE EXIT HANDLER FOR SQLWARNING ROLLBACK;

	START TRANSACTION;

	SET proc_success = 0;

	/*Checking the key match*/

	SELECT COUNT(*) INTO clinic_count FROM Clinic c 
		WHERE c.registration_key = registration_key_in;
	IF clinic_count = 1 THEN 
		SELECT c.clinic_id INTO clinic_id_out 
		FROM Clinic c 
		WHERE c.registration_key = registration_key_in;
		SELECT clinic_id_out AS 'clinic id';
		END IF;

SET proc_success = 1;

COMMIT;
END ; //
DELIMITER ;

/**
*USAGE: to retrieve clinic population body mass statistics:
* avg bmi, avg bmi for males and females, avg change in bmi,
* avg change in bmi for males and females
*CALL getBodyMassStatistics(?, ?, ?);
*1 = clinic_id
*2 = string
*3 = proc_success
*/
DELIMITER //
CREATE PROCEDURE getBodyMassStatistics
	(IN clinic_id_in INT, IN string_in VARCHAR(64), OUT proc_success TINYINT(1))
BEGIN
	DECLARE avg_all DECIMAL(7, 2) DEFAULT 0;
	DECLARE avg_m DECIMAL(7, 2) DEFAULT 0;
	DECLARE avg_f DECIMAL(7, 2) DEFAULT 0;
	DECLARE avg_this_year DECIMAL(7, 2) DEFAULT 0;
	DECLARE avg_last_year DECIMAL(7, 2) DEFAULT 0;
	DECLARE avg_year_before_last DECIMAL(7, 2) DEFAULT 0;
	DECLARE avg_year_before_year_before DECIMAL(7, 2) DEFAULT 0;
	DECLARE avg_this_year_m DECIMAL(7, 2) DEFAULT 0;
	DECLARE avg_last_year_m DECIMAL(7, 2) DEFAULT 0;
	DECLARE avg_year_before_last_m DECIMAL(7, 2) DEFAULT 0;
	DECLARE avg_year_before_year_before_m DECIMAL(7, 2) DEFAULT 0;
	DECLARE avg_this_year_f DECIMAL(7, 2) DEFAULT 0;
	DECLARE avg_last_year_f DECIMAL(7, 2) DEFAULT 0;
	DECLARE avg_year_before_last_f DECIMAL(7, 2) DEFAULT 0;
	DECLARE avg_year_before_year_before_f DECIMAL(7, 2) DEFAULT 0;
	
	
	DECLARE EXIT HANDLER FOR SQLEXCEPTION ROLLBACK;
	DECLARE EXIT HANDLER FOR SQLWARNING ROLLBACK;

	START TRANSACTION;

	SET proc_success = 0;

	/*Retrieving body mass statistics*/

	/*SELECT TRUNCATE(AVG(b.result), 2) INTO avg_all 
	FROM BMI b;*/
	SELECT TRUNCATE(AVG(b.result), 2) INTO avg_m 
	FROM BMI b, Patient p 
	WHERE b.clinic_id = clinic_id_in 
	AND b.patient_id = p.patient_id 
	AND LOWER(CAST(AES_DECRYPT(p.gender, string_in) AS CHAR(50))) = 'male';
	SELECT TRUNCATE(AVG(b.result), 2) INTO avg_f 
	FROM BMI b, Patient p 
	WHERE b.clinic_id = clinic_id_in 
	AND b.patient_id = p.patient_id 
	AND LOWER(CAST(AES_DECRYPT(p.gender, string_in) AS CHAR(50))) = 'female';
	
	SELECT avg_m AS 'avg male', 
		avg_f AS 'avg female';

/*last A1C males and females by classes attended*/
	SELECT a.result AS 'last BMI', count(c.patient_id) AS 'classes attended' 
		FROM BMI a, LastClass c, Patient p 
		WHERE a.clinic_id = clinic_id_in 
		AND a.patient_id = c.patient_id 
		AND c.patient_id = p.patient_id 
		AND LOWER(CAST(AES_DECRYPT(p.gender, string_in) AS CHAR(50))) = 'male' 
		AND a.date_recorded = (
		SELECT max(b.date_recorded) 
		FROM BMI b 
		WHERE b.patient_id = a.patient_id) 
		GROUP BY c.patient_id;
	SELECT a.result AS 'last BMI', count(c.patient_id) AS 'classes attended' 
		FROM BMI a, LastClass c, Patient p 
		WHERE a.clinic_id = clinic_id_in 
		AND a.patient_id = c.patient_id 
		AND c.patient_id = p.patient_id 
		AND LOWER(CAST(AES_DECRYPT(p.gender, string_in) AS CHAR(50))) = 'female' 
		AND a.date_recorded = (
		SELECT max(b.date_recorded) 
		FROM BMI b 
		WHERE b.patient_id = a.patient_id) 
		GROUP BY c.patient_id;

SET proc_success = 1;

COMMIT;
END ; //
DELIMITER ;

/**
*USAGE: to retrieve clinic population demographic information:
* total number of patients, percent male and female, percent 
* by ethnicity, and ages 
*CALL getDemographics(?, ?);
*1 = clinic_id
*2 = string
*3 = proc_success
*/
DELIMITER //
CREATE PROCEDURE getDemographics
	(IN clinic_id_in INT, IN string_in VARCHAR(64), OUT proc_success TINYINT(1))
BEGIN
	DECLARE total_patients INT DEFAULT 0;
	DECLARE male_number INT DEFAULT 0;
	DECLARE female_number INT DEFAULT 0;
	DECLARE white_number INT DEFAULT 0;
	DECLARE african_american_number INT DEFAULT 0;
	DECLARE asian_number INT DEFAULT 0;
	DECLARE indian_number INT DEFAULT 0;
	DECLARE hispanic_number INT DEFAULT 0;
	DECLARE middle_eastern_number INT DEFAULT 0;
	DECLARE other_number INT DEFAULT 0;
	
	DECLARE EXIT HANDLER FOR SQLEXCEPTION ROLLBACK;
	DECLARE EXIT HANDLER FOR SQLWARNING ROLLBACK;

	START TRANSACTION;

	SET proc_success = 0;

	/*Retrieving patient demographic information*/

	SELECT COUNT(*) INTO total_patients   
		FROM Patient p, PatientClinic pc 
		WHERE pc.clinic_id = clinic_id_in 
		AND p.patient_id = pc.patient_id;

	SELECT COUNT(*) INTO male_number 
		FROM Patient p, PatientClinic pc 
		WHERE pc.clinic_id = clinic_id_in 
		AND p.patient_id = pc.patient_id 
		AND LOWER(CAST(AES_DECRYPT(p.gender, string_in) AS CHAR(50))) LIKE 'male';

	SELECT COUNT(*) INTO female_number 
		FROM Patient p, PatientClinic pc 
		WHERE pc.clinic_id = clinic_id_in 
		AND p.patient_id = pc.patient_id 
		AND LOWER(CAST(AES_DECRYPT(p.gender, string_in) AS CHAR(50))) LIKE 'female';

	SELECT COUNT(*) INTO white_number 
		FROM Patient p, PatientClinic pc 
		WHERE pc.clinic_id = clinic_id_in 
		AND p.patient_id = pc.patient_id 
		AND UPPER(CAST(AES_DECRYPT(p.race, string_in) AS CHAR(50))) LIKE 'WHITE';

	SELECT COUNT(*) INTO african_american_number 
		FROM Patient p, PatientClinic pc 
		WHERE pc.clinic_id = clinic_id_in 
		AND p.patient_id = pc.patient_id 
		AND UPPER(CAST(AES_DECRYPT(p.race, string_in) AS CHAR(50))) LIKE 'AFRICAN AMERICAN';

	SELECT COUNT(*) INTO asian_number 
		FROM Patient p, PatientClinic pc 
		WHERE pc.clinic_id = clinic_id_in 
		AND p.patient_id = pc.patient_id 
		AND UPPER(CAST(AES_DECRYPT(p.race, string_in) AS CHAR(50))) LIKE 'ASIAN/PACIFIC ISLANDER';

	SELECT COUNT(*) INTO indian_number 
		FROM Patient p, PatientClinic pc 
		WHERE pc.clinic_id = clinic_id_in 
		AND p.patient_id = pc.patient_id 
		AND UPPER(CAST(AES_DECRYPT(p.race, string_in) AS CHAR(50))) LIKE 'AMERICAN INDIAN/ALASKA NATIVE';

	SELECT COUNT(*) INTO hispanic_number 
		FROM Patient p, PatientClinic pc 
		WHERE pc.clinic_id = clinic_id_in 
		AND p.patient_id = pc.patient_id 
		AND UPPER(CAST(AES_DECRYPT(p.race, string_in) AS CHAR(50))) LIKE 'HISPANIC';

	SELECT COUNT(*) INTO middle_eastern_number 
		FROM Patient p, PatientClinic pc 
		WHERE pc.clinic_id = clinic_id_in 
		AND p.patient_id = pc.patient_id 
		AND UPPER(CAST(AES_DECRYPT(p.race, string_in) AS CHAR(50))) LIKE 'MIDDLE EASTERN';

	SELECT COUNT(*) INTO other_number 
		FROM Patient p, PatientClinic pc 
		WHERE pc.clinic_id = clinic_id_in 
		AND p.patient_id = pc.patient_id 
		AND UPPER(CAST(AES_DECRYPT(p.race, string_in) AS CHAR(50))) LIKE 'OTHER';	

	SELECT total_patients AS 'number of patients', 
		male_number/total_patients AS 'percent male', 
		female_number/total_patients AS 'percent female', 
		white_number/total_patients AS 'percent white', 
		african_american_number/total_patients AS 'percent african american', 
		asian_number/total_patients AS 'percent asian', 
		indian_number/total_patients AS 'percent indian', 
		hispanic_number/total_patients AS 'percent hispanic', 
		middle_eastern_number/total_patients AS 'percent middle eastern', 
		other_number/total_patients AS 'percent other'; 

	SELECT FLOOR(DATEDIFF(DATE(NOW()), (CAST(AES_DECRYPT(p.birth_date, string_in) AS DATE)))/365) AS 'age' 
		FROM Patient p, PatientClinic pc 
		WHERE pc.clinic_id = clinic_id_in 
		AND p.patient_id = pc.patient_id;

SET proc_success = 1;

COMMIT;
END ; //
DELIMITER ;

/**
*USAGE: to retrieve clinic population glycemic control information:
* avg a1c results, avg change in a1c, percent at target
*CALL getGlycemicControl(?, ?);
*1 = clinic_id
*2 = proc_success
*/
DELIMITER //
CREATE PROCEDURE getGlycemicControl
	(IN clinic_id_in INT, OUT proc_success TINYINT(1))
BEGIN
	DECLARE total_patients INT DEFAULT 0;
	DECLARE target_count INT DEFAULT 0;
	
	DECLARE EXIT HANDLER FOR SQLEXCEPTION ROLLBACK;
	DECLARE EXIT HANDLER FOR SQLWARNING ROLLBACK;

	START TRANSACTION;

	SET proc_success = 0;

	
	SELECT COUNT(*) INTO total_patients   
		FROM Patient p, A1C a 
		WHERE a.clinic_id = clinic_id_in 
		AND a.patient_id = p.patient_id;

	SELECT COUNT(*) INTO target_count 
	FROM HealthyTargetStatus hts, A1C a, Patient p 
	WHERE a.clinic_id = clinic_id_in 
	AND hts.patient_id = p.patient_id 
	AND a.patient_id = p.patient_id 
	AND hts.measurement = 'a1c' 
	AND hts.out_of_target = 0;

	SELECT AVG(a.result) AS 'average A1C' 
		FROM A1C a 
		WHERE a.clinic_id = clinic_id_in;
	
	SELECT target_count/total_patients AS 'percent at A1C target';


/*last A1C and number of classes attended for each patient*/
	SELECT a.result AS 'last A1C', count(c.patient_id) AS 'classes attended' 
	FROM A1C a, LastClass c 
	WHERE a.clinic_id = clinic_id_in 
	AND a.patient_id = c.patient_id 
	AND a.date_recorded = (
	SELECT max(b.date_recorded) 
	FROM A1C b 
	WHERE b.patient_id = a.patient_id) 
	GROUP BY c.patient_id;

/*last A1C and treatment class attended for each patient*/
	SELECT a.result AS 'last A1C', c.rx_class AS 'treatment class'
		FROM A1C a, PatientRx c 
		WHERE a.patient_id = c.patient_id 
		AND a.clinic_id = clinic_id_in 
		AND a.date_recorded = (
		SELECT max(b.date_recorded) 
		FROM A1C b 
		WHERE b.patient_id = a.patient_id) 
		AND c.date_recorded = (
		SELECT max(d.date_recorded) 
		FROM PatientRx d 
		WHERE d.patient_id = a.patient_id)
		GROUP BY c.patient_id;

SET proc_success = 1;

COMMIT;
END ; //
DELIMITER ;

/**
*USAGE: to change patient demographic information in the database
*CALL updatePatient(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);
*1 = patient_id
*2 = first_name
*3 = last_name
*4 = birth_date
*5 = address
*6 = contact_number
*7 = gender
*8 = race
*9 = email_address
*10 = language
*11 = reason_for_inactivity
*12 = start_date
*13 = string
*14 = proc_success
*/
DELIMITER //
CREATE PROCEDURE updatePatient
	(IN patient_id_in INT, IN first_name_in VARCHAR(50), 
	IN last_name_in VARCHAR(50), IN birth_date_in DATE, IN address_in VARCHAR(255), 
	IN contact_number_in VARCHAR(50), IN gender_in VARCHAR(50), 
	IN race_in VARCHAR(50), IN email_address_in VARCHAR(255), 
	IN language_in VARCHAR(50), IN reason_for_inactivity_in VARCHAR(50), 
	IN start_date_in DATE, IN string_in VARCHAR(64), 
	OUT proc_success TINYINT(1))
BEGIN
	DECLARE email_address_count INT DEFAULT 0;
	DECLARE address_count INT DEFAULT 0;
	DECLARE language_count INT DEFAULT 0;
	DECLARE reason_count INT DEFAULT 0;	

	DECLARE EXIT HANDLER FOR SQLEXCEPTION ROLLBACK;
	DECLARE EXIT HANDLER FOR SQLWARNING ROLLBACK;

	START TRANSACTION;

	SET proc_success = 0;

	/*Adding patient demographic information*/

	UPDATE Patient p SET p.first_name = AES_ENCRYPT(first_name_in, string_in),
		p.last_name = AES_ENCRYPT(last_name_in, string_in), 
		p.birth_date = AES_ENCRYPT(birth_date_in, string_in), 
		p.contact_number = AES_ENCRYPT(contact_number_in, string_in), 
		p.gender = AES_ENCRYPT(gender_in, string_in), 
		p.race = AES_ENCRYPT(race_in, string_in), 
		p.start_date = AES_ENCRYPT(start_date_in, string_in) 
		WHERE p.patient_id = patient_id_in;
	
	SELECT COUNT(*) INTO email_address_count 
		FROM PatientEmailAddress pe 
		WHERE pe.patient_id = patient_id_in;

		IF email_address_in IS NOT NULL THEN 
			IF email_address_count > 0 THEN 
				UPDATE PatientEmailAddress pe 
				SET pe.email_address = AES_ENCRYPT(email_address_in, string_in) 
				WHERE pe.patient_id = patient_id_in;
			ELSE 
				INSERT INTO PatientEmailAddress 
				VALUES (patient_id_in, 
				AES_ENCRYPT(email_address_in, string_in));
			END IF;
		ELSE 
			IF email_address_count > 0 THEN 
				DELETE FROM PatientEmailAddress 
				WHERE patient_id = patient_id_in;
			END IF;
		END IF;

	SELECT COUNT(*) INTO address_count 
		FROM PatientAddress pa 
		WHERE pa.patient_id = patient_id_in;

		IF address_in IS NOT NULL THEN 
			IF address_count > 0 THEN 
				UPDATE PatientAddress pa 
				SET pa.address = AES_ENCRYPT(address_in, string_in) 
				WHERE pa.patient_id = patient_id_in;
			ELSE 
				INSERT INTO PatientAddress 
				VALUES (patient_id_in, 
				AES_ENCRYPT(address_in, string_in));
			END IF;
		ELSE 
			IF address_count > 0 THEN 
				DELETE FROM PatientAddress 
				WHERE patient_id = patient_id_in;
			END IF;
		END IF;
	
	SELECT COUNT(*) INTO language_count 
		FROM PatientLanguage pl 
		WHERE pl.patient_id = patient_id_in;

		IF language_in IS NOT NULL THEN 
			IF language_count > 0 THEN 
				UPDATE PatientLanguage pl 
				SET pl.language = language_in 
				WHERE pl.patient_id = patient_id_in;
			ELSE 
				INSERT INTO PatientLanguage 
				VALUES (patient_id_in, language_in);
			END IF;
		ELSE 
			IF language_count > 0 THEN 
				DELETE FROM PatientLanguage 
				WHERE patient_id = patient_id_in;
			END IF;
		END IF;
	
	SELECT COUNT(*) INTO reason_count 
		FROM InactivePatient ip 
		WHERE ip.patient_id = patient_id_in;

		IF reason_for_inactivity_in IS NOT NULL THEN 
			IF reason_count > 0 THEN 
				UPDATE InactivePatient ip 
				SET ip.reason = reason_for_inactivity_in 
				WHERE ip.patient_id = patient_id_in;
			ELSE 
				INSERT INTO InactivePatient 
				VALUES (patient_id_in, reason_for_inactivity_in);
			END IF;
		ELSE
			IF reason_count > 0 THEN 
				DELETE FROM InactivePatient 
				WHERE patient_id = patient_id_in;
			END IF;
		END IF;
	
	SET proc_success = 1;

COMMIT;
END ; //
DELIMITER ;

/**
*USAGE: to retrieve demographic information for all patients
*CALL getAllPatients(?, ?, ?);
*1 = clinic_id
*2 = string
*3 = proc_success
*/
DELIMITER //
CREATE PROCEDURE getAllPatients
	(IN clinic_id_in INT, IN string_in VARCHAR(64), OUT proc_success TINYINT(1))
BEGIN
		
	DECLARE EXIT HANDLER FOR SQLEXCEPTION ROLLBACK;
	DECLARE EXIT HANDLER FOR SQLWARNING ROLLBACK;

	START TRANSACTION;

	SET proc_success = 0;

/*Retrieving information for all patients*/

	SELECT p.patient_id AS 'patient id', 
		CAST(AES_DECRYPT(p.first_name, string_in) AS CHAR(50)) AS 'first name', 
		CAST(AES_DECRYPT(p.last_name, string_in) AS CHAR(50)) AS 'last name', 
		CAST(AES_DECRYPT(p.birth_date, string_in) AS DATE) AS 'birth date', 
		CAST(AES_DECRYPT(pa.address, string_in) AS CHAR(255)) AS 'address', 
		CAST(AES_DECRYPT(p.contact_number, string_in) AS CHAR(50)) AS 'contact number', 
		CAST(AES_DECRYPT(p.gender, string_in) AS CHAR(50)) AS 'gender', 
		CAST(AES_DECRYPT(p.race, string_in) AS CHAR(50)) AS 'race', 
		CAST(AES_DECRYPT(pea.email_address, string_in) AS CHAR(255)) AS 'email', 
		pl.language AS 'language', 
		ip.reason AS 'reason', 
		CAST(AES_DECRYPT(p.start_date, string_in) AS DATE) AS 'start date' 
		FROM Patient p INNER JOIN PatientClinic pc 
		ON p.patient_id = pc.patient_id
		LEFT JOIN PatientAddress pa 
		ON p.patient_id = pa.patient_id   
		LEFT JOIN PatientEmailAddress pea 
		ON p.patient_id = pea.patient_id 
		LEFT JOIN PatientLanguage pl 
		ON p.patient_id = pl.patient_id 
		LEFT JOIN InactivePatient ip 
		ON p.patient_id = ip.patient_id 
		WHERE pc.clinic_id = clinic_id_in 
		ORDER BY CAST(AES_DECRYPT(p.last_name, string_in) AS CHAR(50));
	
SET proc_success = 1;

COMMIT;
END ; //
DELIMITER ;

/**
*USAGE: to set login time and retrieve the user identification 
* information and the clinics where the active user is registered 
* for user functions
*CALL getUser(?, ?);
*1 = user_name
*2 = proc_success
*/
DELIMITER //
CREATE PROCEDURE getUser
	(IN user_name_in VARCHAR(50), OUT proc_success TINYINT(1))
BEGIN
		
	DECLARE EXIT HANDLER FOR SQLEXCEPTION ROLLBACK;
	DECLARE EXIT HANDLER FOR SQLWARNING ROLLBACK;

	START TRANSACTION;

	SET proc_success = 0;

	/*Retrieving user information*/

	SELECT u.first_name AS 'first name', u.last_name AS 'last name', 
		u.job_title AS 'job title', u.administrator AS 'administrator' 
		FROM User u 
		WHERE u.user_name = user_name_in;

	UPDATE User u SET u.last_login = NOW() 
		WHERE u.user_name = user_name_in;

	SELECT c.clinic_id AS 'clinic id', c.name AS 'clinic name' 
		FROM Clinic c, User u, UserRegistration ur 
		WHERE c.registration_key = ur.registration_key 
		AND ur.user_name = u.user_name 
		AND u.user_name = user_name_in 
		AND u.active = 1;

SET proc_success = 1;

COMMIT;
END ; //
DELIMITER ;

/**
*USAGE: to retrieve the list of all user names
*CALL getAllUsers(?);
*1 = proc_success
*/
DELIMITER //
CREATE PROCEDURE getAllUsers
	(OUT proc_success TINYINT(1))
BEGIN
		
	DECLARE EXIT HANDLER FOR SQLEXCEPTION ROLLBACK;
	DECLARE EXIT HANDLER FOR SQLWARNING ROLLBACK;

	START TRANSACTION;

	SET proc_success = 0;

	/*Retrieving user names*/

	SELECT u.user_name AS 'user name' FROM User u;

SET proc_success = 1;

COMMIT;
END ; //
DELIMITER ;

/**
*USAGE: to retrieve the user details along with the 
*clinics where the user is registered for administration 
*purposes
*CALL getUserDetails(?, ?);
*1 = user_name
*2 = proc_success
*/
DELIMITER //
CREATE PROCEDURE getUserDetails
	(IN user_name_in VARCHAR(50), OUT proc_success TINYINT(1))
BEGIN
		
	DECLARE EXIT HANDLER FOR SQLEXCEPTION ROLLBACK;
	DECLARE EXIT HANDLER FOR SQLWARNING ROLLBACK;

	START TRANSACTION;

	SET proc_success = 0;

	/*Retrieving user information*/

	SELECT u.first_name AS 'first name', u.last_name AS 'last name', 
		u.job_title AS 'job title', u.administrator AS 'administrator', 
		u.active AS 'active', u.date_joined AS 'date joined', 
		u.last_login AS 'last login'
		FROM User u 
		WHERE u.user_name = user_name_in;

	SELECT c.clinic_id AS 'clinic id', c.name AS 'clinic name', 
		c.address AS 'address', c.phone_number AS 'phone number' 
		FROM Clinic c, User u, UserRegistration ur 
		WHERE c.registration_key = ur.registration_key 
		AND ur.user_name = u.user_name 
		AND u.user_name = user_name_in;

SET proc_success = 1;

COMMIT;
END ; //
DELIMITER ;

/**
*USAGE: to update user access and capability 
* in the registry 
*purposes
*CALL updateUserAccess(?, ?, ?, ?);
*1 = user_name
*2 = remove_registration
*3 = clinic_id
*4 = terminate
*/
DELIMITER //
CREATE PROCEDURE updateUserAccess
	(IN user_name_in VARCHAR(50), IN remove_registration_in TINYINT(1), 
	IN clinic_id_in INT, IN terminate_in TINYINT(1))
BEGIN
		
	IF remove_registration_in = 1 THEN 
		IF clinic_id_in IS NOT NULL THEN 
			DELETE FROM UserRegistration 
			WHERE user_name = user_name_in 
			AND registration_key IN 
			(SELECT c.registration_key 
			FROM Clinic c 
			WHERE c.clinic_id = clinic_id_in);
		END IF;
	END IF;
	IF terminate_in = 1 THEN 
		UPDATE User u SET u.active = 0 
		WHERE u.user_name = user_name_in;
		DELETE FROM UserRegistration 
		WHERE user_name = user_name_in;
		DELETE FROM UserCredentials 
		WHERE user_name = user_name_in;
	END IF;

END ; //
DELIMITER ;

/**
*USAGE: to register a user in the database
*CALL registerUser(?, ?, ?, ?, ?, ?, ?, ?, ?);
*1 = user_name
*2 = first_name
*3 = last_name
*4 = job_title
*5 = administrator
*6 = password
*7 = salt
*8 = registration_key
*9 = proc_success
*/
DELIMITER //
CREATE PROCEDURE registerUser
	(IN user_name_in VARCHAR(50), IN first_name_in VARCHAR(50), 
	IN last_name_in VARCHAR(50), IN job_title_in VARCHAR(50), 
	IN administrator_in TINYINT(1), IN password_in VARCHAR(64), 
	IN salt_in VARCHAR(64), IN registration_key_in VARCHAR(64), 
	OUT proc_success TINYINT(1))
BEGIN
	DECLARE user_exists TINYINT(1) DEFAULT 0;

	DECLARE EXIT HANDLER FOR SQLEXCEPTION ROLLBACK;
	DECLARE EXIT HANDLER FOR SQLWARNING ROLLBACK;

	START TRANSACTION;

	SET proc_success = 0;

	SELECT COUNT(*) INTO user_exists 
		FROM User u 
		WHERE u.user_name = user_name_in;

	/*Adding user if user does not exist in the database*/

	IF user_exists = 0 THEN 

		INSERT INTO User VALUES (user_name_in, first_name_in, last_name_in, 
		job_title_in, NOW(), NULL, 1, administrator_in);

		INSERT INTO UserCredentials VALUES (user_name_in, 
		password_in, salt_in, 0);

		INSERT INTO UserRegistration VALUES (user_name_in, 
		registration_key_in);
		SET proc_success = 1;
	END IF;	


SELECT proc_success AS 'success';

COMMIT;
END ; //
DELIMITER ;

/**
*USAGE: to register an existing user for a clinic
*CALL addNewRegistrationToExistingUser(?, ?, ?, ?);
*1 = user_name
*2 = password
*3 = registration_key
*4 = proc_success
*/
DELIMITER //
CREATE PROCEDURE addNewRegistrationToExistingUser
	(IN user_name_in VARCHAR(50), IN password_in VARCHAR(64), 
	IN registration_key_in VARCHAR(64), OUT proc_success TINYINT(1))
BEGIN
	DECLARE authentic TINYINT(1) DEFAULT 0;
	DECLARE registration_exists TINYINT(1) DEFAULT 0;

	DECLARE EXIT HANDLER FOR SQLEXCEPTION ROLLBACK;
	DECLARE EXIT HANDLER FOR SQLWARNING ROLLBACK;

	START TRANSACTION;

	SET proc_success = 0;

	SELECT COUNT(*) INTO authentic 
		FROM UserCredentials uc 
		WHERE uc.user_name = user_name_in 
		AND uc.password = password_in;

	IF authentic = 1 THEN 
		SELECT COUNT(*) INTO registration_exists 
		FROM UserRegistration ur 
		WHERE ur.user_name = user_name_in 
		AND ur.registration_key = registration_key_in;
		IF registration_exists = 0 THEN 
			INSERT INTO UserRegistration VALUES (user_name_in, 
			registration_key_in);
			SET proc_success = 1;
		END IF;	
	END IF;

SELECT proc_success AS 'success';

COMMIT;
END ; //
DELIMITER ;

/**
*USAGE: to retrieve a clinic's salt
*CALL retrieveClinicSalt(?, ?);
*1 = clinic_id
*2 = proc_success
*/
DELIMITER //
CREATE PROCEDURE retrieveClinicSalt
	(IN clinic_id_in INT, OUT proc_success TINYINT(1))
BEGIN
	
	DECLARE EXIT HANDLER FOR SQLEXCEPTION ROLLBACK;
	DECLARE EXIT HANDLER FOR SQLWARNING ROLLBACK;

	START TRANSACTION;

	SET proc_success = 0;

	/*Retrieving the salt*/

	SELECT c.salt AS 'clinic salt' 
		FROM Clinic c 
		WHERE c.clinic_id = clinic_id_in;
	
SET proc_success = 1;

COMMIT;
END ; //
DELIMITER ;

/**
*USAGE: to retrieve a user's salt
*CALL retrieveSalt(?, ?);
*1 = user_name
*2 = proc_success
*/
DELIMITER //
CREATE PROCEDURE retrieveSalt
	(IN user_name_in VARCHAR(50), OUT proc_success TINYINT(1))
BEGIN
	
	DECLARE EXIT HANDLER FOR SQLEXCEPTION ROLLBACK;
	DECLARE EXIT HANDLER FOR SQLWARNING ROLLBACK;

	START TRANSACTION;

	SET proc_success = 0;

	/*Retrieving the salt*/

	SELECT uc.salt 
		FROM UserCredentials uc 
		WHERE uc.user_name = user_name_in;
	
SET proc_success = 1;

COMMIT;
END ; //
DELIMITER ;

/**
*USAGE: To retrieve all psychological screening references
* CALL getPHQ9(?)
* 1 = procedure success
*/

DELIMITER //
CREATE PROCEDURE getPHQ9
	(OUT proc_success TINYINT(1))
BEGIN

DECLARE EXIT HANDLER FOR SQLEXCEPTION ROLLBACK;
DECLARE EXIT HANDLER FOR SQLWARNING ROLLBACK;

START TRANSACTION;

SET proc_success = 0;

SELECT phq.phq_score AS 'phq score', 
phq.severity AS 'severity', 
phq.proposed_actions AS 'proposed actions' 
FROM PHQ9 phq 
ORDER BY phq.phq_score;

SET proc_success = 1;

COMMIT;
END ; //
DELIMITER ;

/**
*USAGE: To retrieve all telephone follow-up definitions
* CALL getTelephoneFollowUpDefinitions(?)
* 1 = procedure success
*/

DELIMITER //
CREATE PROCEDURE getTelephoneFollowUpDefinitions
	(OUT proc_success TINYINT(1))
BEGIN

DECLARE EXIT HANDLER FOR SQLEXCEPTION ROLLBACK;
DECLARE EXIT HANDLER FOR SQLWARNING ROLLBACK;

START TRANSACTION;

SET proc_success = 0;

SELECT td.follow_up_code AS 'follow up code', 
td.definition AS 'definition' 
FROM TelephoneFollowUpDefinition td 
ORDER BY td.follow_up_code;

SET proc_success = 1;

COMMIT;
END ; //
DELIMITER ;

/**
*USAGE: To retrieve all eye screening codes and definitions
* CALL getEyeExamDefinitions(?)
* 1 = procedure success
*/

DELIMITER //
CREATE PROCEDURE getEyeExamDefinitions
	(OUT proc_success TINYINT(1))
BEGIN

DECLARE EXIT HANDLER FOR SQLEXCEPTION ROLLBACK;
DECLARE EXIT HANDLER FOR SQLWARNING ROLLBACK;

START TRANSACTION;

SET proc_success = 0;

SELECT eed.eye_exam_code AS 'eye exam code', 
eed.definition AS 'definition' 
FROM EyeExamDefinition eed  
ORDER BY eed.eye_exam_code;

SET proc_success = 1;

COMMIT;
END ; //
DELIMITER ;

/**
*USAGE: To retrieve all foot screening categories and definitions
* CALL getFootExamRiskDefinitions(?)
* 1 = procedure success
*/

DELIMITER //
CREATE PROCEDURE getFootExamRiskDefinitions
	(OUT proc_success TINYINT(1))
BEGIN

DECLARE EXIT HANDLER FOR SQLEXCEPTION ROLLBACK;
DECLARE EXIT HANDLER FOR SQLWARNING ROLLBACK;

START TRANSACTION;

SET proc_success = 0;

SELECT ferd.risk_category AS 'risk category', 
ferd.definition AS 'definition' 
FROM FootExamRiskDefinition ferd 
ORDER BY ferd.risk_category;

SET proc_success = 1;

COMMIT;
END ; //
DELIMITER ;

/**
*USAGE: To retrieve Rx class and therapy type for 
* all therapies
* CALL getTherapies(?)
* 1 = procedure success
*/

DELIMITER //
CREATE PROCEDURE getTherapies
	(OUT proc_success TINYINT(1))
BEGIN

DECLARE EXIT HANDLER FOR SQLEXCEPTION ROLLBACK;
DECLARE EXIT HANDLER FOR SQLWARNING ROLLBACK;

START TRANSACTION;

SET proc_success = 0;

SELECT th.rx_class AS 'rx class', th.therapy_type AS 'therapy type' 
FROM Therapy th 
ORDER BY th.rx_class;

SET proc_success = 1;

COMMIT;
END ; //
DELIMITER ;

/**
*USAGE: To retrieve IDs, names, and classes of all medications
* CALL getMedications(?)
* 1 = procedure success
*/

DELIMITER //
CREATE PROCEDURE getMedications
	(OUT proc_success TINYINT(1))
BEGIN

DECLARE EXIT HANDLER FOR SQLEXCEPTION ROLLBACK;
DECLARE EXIT HANDLER FOR SQLWARNING ROLLBACK;

START TRANSACTION;

SET proc_success = 0;

SELECT med.med_id AS 'med id', med.med_name AS 'med name', 
med.med_class AS 'med class' 
FROM Medication med 
ORDER BY med.med_id;

SET proc_success = 1;

COMMIT;
END ; //
DELIMITER ;
/**
*USAGE: To retrieve all quality roles and responsibilities
* CALL getQualityReferences(?)
* 1 = procedure success
*/

DELIMITER //
CREATE PROCEDURE getQualityReferences
	(OUT proc_success TINYINT(1))
BEGIN

DECLARE EXIT HANDLER FOR SQLEXCEPTION ROLLBACK;
DECLARE EXIT HANDLER FOR SQLWARNING ROLLBACK;

START TRANSACTION;

SET proc_success = 0;

SELECT q.role AS 'role', q.responsibility AS 'responsibility' 
FROM Quality q 
WHERE q.active = 1 
ORDER BY q.role DESC;

SET proc_success = 1;

COMMIT;
END ; //
DELIMITER ;

/**
*USAGE: to retrieve clinic IDs and clinic names
*CALL getClinics(?);
*1 = proc_success
*/
DELIMITER //
CREATE PROCEDURE getClinics
	(OUT proc_success TINYINT(1))
BEGIN
	
	DECLARE EXIT HANDLER FOR SQLEXCEPTION ROLLBACK;
	DECLARE EXIT HANDLER FOR SQLWARNING ROLLBACK;

	START TRANSACTION;

	SET proc_success = 0;

	/*Retrieving the IDs*/

	SELECT c.clinic_id AS 'clinic id', c.name AS 'clinic name' 
		FROM Clinic c;
	
SET proc_success = 1;

COMMIT;
END ; //
DELIMITER ;

/**
*USAGE: to retrieve the list of languages
*CALL getLanguages(?);
*1 = proc_success
*/
DELIMITER //
CREATE PROCEDURE getLanguages
	(OUT proc_success TINYINT(1))
BEGIN
	
	DECLARE EXIT HANDLER FOR SQLEXCEPTION ROLLBACK;
	DECLARE EXIT HANDLER FOR SQLWARNING ROLLBACK;

	START TRANSACTION;

	SET proc_success = 0;

	/*Retrieving the IDs*/

	SELECT l.language AS 'language' 
		FROM Language l;
	
SET proc_success = 1;

COMMIT;
END ; //
DELIMITER ;

/**
*USAGE: to retrieve the reasons for patient inactivity
*CALL getReasonsForInactivity(?);
*1 = proc_success
*/
DELIMITER //
CREATE PROCEDURE getReasonsForInactivity
	(OUT proc_success TINYINT(1))
BEGIN
	
	DECLARE EXIT HANDLER FOR SQLEXCEPTION ROLLBACK;
	DECLARE EXIT HANDLER FOR SQLWARNING ROLLBACK;

	START TRANSACTION;

	SET proc_success = 0;

	/*Retrieving the IDs*/

	SELECT rfi.reason AS 'reason' 
		FROM ReasonForInactivity rfi;
	
SET proc_success = 1;

COMMIT;
END ; //
DELIMITER ;

/**
*USAGE: to retrieve the subjects for automated email messages
*CALL getEmailMessageSubjects(?);
*1 = proc_success
*/
DELIMITER //
CREATE PROCEDURE getEmailMessageSubjects
	(OUT proc_success TINYINT(1))
BEGIN
	
	DECLARE EXIT HANDLER FOR SQLEXCEPTION ROLLBACK;
	DECLARE EXIT HANDLER FOR SQLWARNING ROLLBACK;

	START TRANSACTION;

	SET proc_success = 0;

	/*Retrieving the IDs*/

	SELECT ems.subject AS 'subject' 
		FROM EmailMessageSubject ems;
	
SET proc_success = 1;

COMMIT;
END ; //
DELIMITER ;

/**
*USAGE: to retrieve the patient healthy target ranges
*CALL getHealthyTargetRanges(?);
*1 = proc_success
*/
DELIMITER //
CREATE PROCEDURE getHealthyTargetRanges
	(OUT proc_success TINYINT(1))
BEGIN
	
	DECLARE EXIT HANDLER FOR SQLEXCEPTION ROLLBACK;
	DECLARE EXIT HANDLER FOR SQLWARNING ROLLBACK;

	START TRANSACTION;

	SET proc_success = 0;

	/*Retrieving the IDs*/

	SELECT ht.measurement AS 'measurement', ht.upper_target AS 'upper', 
		ht.lower_target AS 'lower' 
		FROM HealthyTarget ht;
	
SET proc_success = 1;

COMMIT;
END ; //
DELIMITER ;

/**
*USAGE: To retrieve all references
* CALL getReferences(?)
* 1 = procedure success
*/

DELIMITER //
CREATE PROCEDURE getReferences
	(OUT proc_success TINYINT(1))
BEGIN

DECLARE q_success TINYINT(1) DEFAULT NULL;
DECLARE th_success TINYINT(1) DEFAULT NULL;
DECLARE m_success TINYINT(1) DEFAULT NULL;
DECLARE phq_success TINYINT(1) DEFAULT NULL;
DECLARE t_success TINYINT(1) DEFAULT NULL;
DECLARE f_success TINYINT(1) DEFAULT NULL;
DECLARE e_success TINYINT(1) DEFAULT NULL;
DECLARE c_success TINYINT(1) DEFAULT NULL;
DECLARE nt_success TINYINT(1) DEFAULT NULL;
DECLARE l_success TINYINT(1) DEFAULT NULL;
DECLARE rfi_success TINYINT(1) DEFAULT NULL;
DECLARE em_success TINYINT(1) DEFAULT NULL;
DECLARE ht_success TINYINT(1) DEFAULT NULL;

DECLARE EXIT HANDLER FOR SQLEXCEPTION ROLLBACK;
DECLARE EXIT HANDLER FOR SQLWARNING ROLLBACK;

START TRANSACTION;

SET proc_success = 0;

CALL getQualityReferences(@out1);
CALL getTherapies(@out2);
CALL getMedications(@out3);
CALL getPHQ9(@out4);
CALL getTelephoneFollowUpDefinitions(@out5);
CALL getFootExamRiskDefinitions(@out6);
CALL getEyeExamDefinitions(@out7);
CALL getClinics(@out8);
CALL getNoteTopics(@out9);
CALL getLanguages(@out10);
CALL getReasonsForInactivity(@out11);
CALL getEmailMessageSubjects(@out12);
CALL getHealthyTargetRanges(@out13);

SELECT @out1 INTO q_success;
SELECT @out2 INTO th_success;
SELECT @out3 INTO m_success;
SELECT @out4 INTO phq_success;
SELECT @out5 INTO t_success;
SELECT @out6 INTO f_success;
SELECT @out7 INTO e_success;
SELECT @out8 INTO c_success;
SELECT @out9 INTO nt_success;
SELECT @out10 INTO l_success;
SELECT @out11 INTO rfi_success;
SELECT @out12 INTO em_success;
SELECT @out13 INTO ht_success;

IF q_success = 1 THEN
IF th_success = 1 THEN
IF m_success = 1 THEN 
IF phq_success = 1 THEN 
IF t_success = 1 THEN 
IF f_success = 1 THEN 
IF e_success = 1 THEN 
IF c_success = 1 THEN 
IF nt_success = 1 THEN 
IF l_success = 1 THEN 
IF rfi_success = 1 THEN
IF em_success = 1 THEN 
IF ht_success = 1 THEN
SET proc_success = 1; 
END IF;
END IF;
END IF;
END IF;
END IF; 
END IF;
END IF;
END IF;
END IF;
END IF;
END IF;
END IF;
END IF;

SELECT proc_success AS 'success';

COMMIT;
END ; //
DELIMITER ;

/**
*USAGE: to document completed items in the quality checklist for each 
*patient visit 
*CALL saveQualityChecklist(?, ?, ?, ?, ?);
*1 = patient_id
*2 = date_entered
*3 = responsibility
*4 = user_name
*5 = clinic
*/
DELIMITER //
CREATE PROCEDURE saveQualityChecklist
	(IN patient_id_in INT, IN date_entered_in DATE, 
	IN responsibility_in VARCHAR(1000), IN user_name_in VARCHAR(50), 
	IN clinic_in INT)
BEGIN
	DECLARE already_saved TINYINT DEFAULT 0;
	
	DECLARE EXIT HANDLER FOR SQLEXCEPTION ROLLBACK;
	DECLARE EXIT HANDLER FOR SQLWARNING ROLLBACK;

	START TRANSACTION;

	SELECT COUNT(*) INTO already_saved 
	FROM QualityChecklist qc 
	WHERE qc.patient_id = patient_id_in 
	AND qc.responsibility = responsibility_in 
	AND qc.date_recorded = date_entered_in 
	AND qc.clinic_id = clinic_in;

	/*Adding checklist information*/

	IF already_saved = 0 THEN 	

	INSERT INTO QualityChecklist VALUES (patient_id_in, 
		date_entered_in, responsibility_in, 
		user_name_in, clinic_in);
	END IF;

COMMIT;
END ; //
DELIMITER ;

/**
*USAGE: to retrieve the quality checklist items for a patient 
*on a given date
*CALL getChecklistItems(?, ?, ?, ?);
*1 = patient_id
*2 = date_entered 
*3 = clinic 
*4 = proc_success
*/
DELIMITER //
CREATE PROCEDURE getChecklistItems
	(IN patient_id_in INT, IN date_entered_in DATE, 
	IN clinic_in INT, OUT proc_success TINYINT(1))
BEGIN
	
	DECLARE EXIT HANDLER FOR SQLEXCEPTION ROLLBACK;
	DECLARE EXIT HANDLER FOR SQLWARNING ROLLBACK;

	START TRANSACTION;

	SET proc_success = 0;

/*CHAR(13) is the return carriage*/

	SELECT qc.responsibility AS 'responsibility', q.role AS 'role' 
	FROM QualityChecklist qc, Quality q 
	WHERE qc.patient_id = patient_id_in 
	AND qc.date_recorded = date_entered_in 
	AND qc.clinic_id = clinic_in 
	AND REPLACE(qc.responsibility, CHAR(13), '') = REPLACE(q.responsibility, CHAR(13), '')
	ORDER BY q.role;

	SET proc_success = 1;

COMMIT;
END ; //
DELIMITER ;

/**
*USAGE: to retrieve the quality checklist dates for a patient
*CALL getChecklistDates(?, ?);
*1 = patient_id
*2 = proc_success
*/
DELIMITER //
CREATE PROCEDURE getChecklistDates
	(IN patient_id_in INT, OUT proc_success TINYINT(1))
BEGIN
	
	DECLARE EXIT HANDLER FOR SQLEXCEPTION ROLLBACK;
	DECLARE EXIT HANDLER FOR SQLWARNING ROLLBACK;

	START TRANSACTION;

	SET proc_success = 0;

	SELECT DISTINCT qc.date_recorded AS 'date' 
	FROM QualityChecklist qc 
	WHERE qc.patient_id = patient_id_in;

	SET proc_success = 1;

COMMIT;
END ; //
DELIMITER ;

/**
*USAGE: to add a clinic to the database
*CALL addClinic(?, ?, ?, ?, ?, ?, ?);
*1 = name
*2 = address
*3 = phone_number
*4 = email
*5 = registration_key
*6 = salt
*7 = proc_success
*/
DELIMITER //
CREATE PROCEDURE addClinic
	(IN name_in VARCHAR(50), IN address_in VARCHAR(1000), 
	IN phone_number_in VARCHAR(50), IN email_in VARCHAR(255), 
	IN registration_key_in VARCHAR(64), IN salt_in VARCHAR(64), 
	OUT proc_success TINYINT(1))
BEGIN
	
	DECLARE EXIT HANDLER FOR SQLEXCEPTION ROLLBACK;
	DECLARE EXIT HANDLER FOR SQLWARNING ROLLBACK;

	START TRANSACTION;

	SET proc_success = 0;

	INSERT INTO Clinic VALUES (NULL, name_in, address_in, 
	phone_number_in, registration_key_in, salt_in);

	IF email_in IS NOT NULL THEN 
		INSERT INTO ClinicEmailAddress VALUES (LAST_INSERT_ID(), 
		email_in);
	END IF;

	SET proc_success = 1;

COMMIT;
END ; //
DELIMITER ;

/**
*USAGE: to retrieve information on a clinic
*CALL getClinic(?, ?);
*1 = clinic_id
*2 = proc_success
*/
DELIMITER //
CREATE PROCEDURE getClinic
	(IN clinic_id_in INT, OUT proc_success TINYINT(1))
BEGIN
		
	DECLARE EXIT HANDLER FOR SQLEXCEPTION ROLLBACK;
	DECLARE EXIT HANDLER FOR SQLWARNING ROLLBACK;

	START TRANSACTION;

	SET proc_success = 0;

	/*Retrieving clinic information*/

	SELECT c.name AS 'name', c.address AS 'address', 
		c.phone_number AS 'phone number', ce.email_address AS 'email' 
		FROM Clinic c 
		LEFT JOIN ClinicEmailAddress ce 
		ON c.clinic_id = ce.clinic_id 
		WHERE c.clinic_id = clinic_id_in;

SET proc_success = 1;

COMMIT;
END ; //
DELIMITER ;

/**
*USAGE: to change clinic information in the database
*CALL updateClinic(?, ?, ?, ?, ?, ?, ?);
*1 = clinic_id
*2 = name
*3 = address
*4 = phone_number
*5 = email
*6 = registration_key
*7 = salt
*/
DELIMITER //
CREATE PROCEDURE updateClinic
	(IN clinic_id_in INT, IN name_in VARCHAR(50), 
	IN address_in VARCHAR(1000), IN phone_number_in VARCHAR(50), 
	IN email_in VARCHAR(255), IN registration_key_in VARCHAR(64), 
	IN salt_in VARCHAR(64))
BEGIN
	DECLARE email_count INT DEFAULT 0;
	DECLARE registration_count INT DEFAULT 0;	

	/*Updating clinic information*/

	UPDATE Clinic c SET c.name = name_in, 
		c.address = address_in, c.phone_number = phone_number_in 
		WHERE c.clinic_id = clinic_id_in;	

	IF email_in IS NOT NULL THEN 
		SELECT COUNT(*) INTO email_count 
		FROM ClinicEmailAddress ce 
		WHERE ce.clinic_id = clinic_id_in;
		IF email_count > 0 THEN 
			UPDATE ClinicEmailAddress ce SET ce.email_address = email_in 
			WHERE ce.clinic_id = clinic_id_in; 
		ELSE 
			INSERT INTO ClinicEmailAddress VALUES 
			(clinic_id_in, email_in);
		END IF;
	END IF;

	IF registration_key_in IS NOT NULL THEN 
		UPDATE Clinic c SET c.registration_key = registration_key_in, 
		c.salt = salt_in 
		WHERE c.clinic_id = clinic_id_in;
	END IF;

END ; //
DELIMITER ;

/**
*USAGE: to remove a user's clinic registration 
*CALL terminateUserRegistration(?, ?, ?);
*1 = user_name
*2 = clinic_id
*3 = proc_success
*/
DELIMITER //
CREATE PROCEDURE terminateUserRegistration
	(IN user_name_in VARCHAR(50), IN clinic_id_in INT, 
	OUT proc_success TINYINT(1))
BEGIN
	
	DECLARE EXIT HANDLER FOR SQLEXCEPTION ROLLBACK;
	DECLARE EXIT HANDLER FOR SQLWARNING ROLLBACK;

	START TRANSACTION;

	SET proc_success = 0;

	DELETE FROM UserRegistration 
	WHERE user_name = user_name_in 
	AND registration_key IN 
	(SELECT c.registration_key 
	FROM Clinic c 
	WHERE c.clinic_id = clinic_id_in);

	SET proc_success = 1;

COMMIT;
END ; //
DELIMITER ;

/**
*USAGE: to retrieve a patient's progress note for 
* a given date 
*CALL getProgressNote(?, ?, ?, ?);
*1 = patient_id
*2 = date_created
*3 = string
*4 = proc_success
*/
DELIMITER //
CREATE PROCEDURE getProgressNote
	(IN patient_id_in INT, IN date_created_in DATE, 
	IN string_in VARCHAR(64), OUT proc_success TINYINT(1))
BEGIN
	
	START TRANSACTION;

	SET proc_success = 0;
	
	SELECT pn.medical_insurance AS 'medical insurance', 
		pn.shoe_size AS 'shoe size', 
		pn.allergic_to_medications AS 'allergic to medications', 
		pn.allergies AS 'allergies', pn.weight AS 'weight', 
		pn.height_feet AS 'height feet', pn.height_inches AS 'height inches', 
		pn.weight_reduction_goal AS 'weight reduction goal', 
		pn.pulse AS 'pulse', pn.respirations AS 'respirations', 
		pn.temperature AS 'temperature', pn.foot_screening AS 'foot screening', 
		pn.medications AS 'medications', pn.a1c AS 'a1c', pn.glucose AS 'glucose', 
		w.result AS 'waist', bp.result_s AS 'bp systole', 
		bp.result_d AS 'bp diastole', bp.ace_or_arb AS 'ace or arb', b.result AS 'bmi', 
		e.eye_exam_code AS 'eye exam code', f.risk_category AS 'risk category', 
		psy.phq_score AS 'phq score', sc.smoker AS 'smoker', 
		c.result AS 'compliance', phy.min_per_week AS 'physical activity', 
		CAST(AES_DECRYPT(pn.nurse_or_dietitian_note, string_in) AS CHAR(1000)) 
		AS 'nurse or dietitian note', 
		CAST(AES_DECRYPT(pn.subjective, string_in) AS CHAR(1000)) AS 'subjective', 
		CAST(AES_DECRYPT(pn.objective, string_in) AS CHAR(1000)) AS 'objective', 
		CAST(AES_DECRYPT(pn.assessment, string_in) AS CHAR(1000)) AS 'assessment', 
		CAST(AES_DECRYPT(pn.plan, string_in) AS CHAR(1000)) AS 'plan'
		FROM ProgressNote pn 
		LEFT JOIN Waist w 
		ON pn.patient_id = w.patient_id 
		AND pn.date_created = w.date_recorded 
		LEFT JOIN BloodPressure bp 
		ON pn.patient_id = bp.patient_id 
		AND pn.date_created = bp.date_recorded  
		LEFT JOIN BMI b 
		ON pn.patient_id = b.patient_id 
		AND pn.date_created = b.date_recorded  
		LEFT JOIN EyeScreening e 
		ON pn.patient_id = e.patient_id 
		AND pn.date_created = e.date_recorded   
		LEFT JOIN FootScreening f 
		ON pn.patient_id = f.patient_id 
		AND pn.date_created = f.date_recorded   
		LEFT JOIN PsychologicalScreening psy 
		ON pn.patient_id = psy.patient_id 
		AND pn.date_created = psy.date_recorded   
		LEFT JOIN PhysicalActivity phy 
		ON pn.patient_id = phy.patient_id 
		AND pn.date_created = phy.date_recorded    
		LEFT JOIN SmokingCessation sc 
		ON pn.patient_id = sc.patient_id 
		AND pn.date_created = sc.date_recorded  
		LEFT JOIN Compliance c 
		ON pn.patient_id = c.patient_id 
		AND pn.date_created = c.date_recorded 
		WHERE pn.patient_id = patient_id_in 
		AND pn.date_created = date_created_in;

	SELECT lc.date_recorded AS 'last class date' 
		FROM LastClass lc 
		WHERE lc.patient_id = patient_id_in 
		AND lc.date_recorded IN 
		(SELECT MAX(b.date_recorded) 
		FROM LastClass b 
		WHERE b.patient_id = patient_id_in);

	SELECT u.first_name AS 'first name', u.last_name AS 'last name', 
		u.job_title AS 'job title', pna.datetime_recorded AS 'datetime recorded' 
		FROM ProgressNoteAuthor pna, ProgressNote pn, User u  
		WHERE pna.progress_note_id = pn.progress_note_id 
		AND pna.updated_by = u.user_name 
		AND pn.patient_id = patient_id_in 
		AND pn.date_created = date_created_in;
	
	SET proc_success = 1;

COMMIT;
END ; //
DELIMITER ;

/**
*USAGE: to retrieve a the list of dates of a 
* patient's progress notes 
*CALL getProgressDates(?, ?);
*1 = patient_id
*2 = proc_success
*/
DELIMITER //
CREATE PROCEDURE getProgressDates
	(IN patient_id_in INT, OUT proc_success TINYINT(1))
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION ROLLBACK;
	DECLARE EXIT HANDLER FOR SQLWARNING ROLLBACK;

	START TRANSACTION;

	SET proc_success = 0;

	SELECT date_created AS 'date created' 
	FROM ProgressNote 
	WHERE patient_id = patient_id_in;

	SET proc_success = 1;

COMMIT;
END ; //
DELIMITER ;

/**
*USAGE: to save a patient's progress note 
*CALL saveProgressNote(?, ?, ?, ?, ?, ?, 
*?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 
*?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 
*?, ?, ?, ?, ?, ?, ?, ?, ?);
*1 = patient_id
*2 = date_created
*3 = medical_insurance
*4 = shoe_size
*5 = allergic_to_medications
*6 = allergies
*7 = weight
*8 = height_feet
*9 = height_inches
*10 = weight_reduction_goal
*11 = pulse
*12 = resprations
*13 = temperature
*14 = foot_screening
*15 = medications
*16 = a1c
*17 = glucose
*18 = waist
*19 = bp_systole
*20 = bp_diastole
*21 = ace_or_arb
*22 = bmi
*23 = class_attendance
*24 = eye_screening_result
*25 = foot_screening_result
*26 = psychological_screening_result
*27 = physical_activity
*28 = smoking_status
*29 = compliance
*30 = hospitalization_date
*31 = nurse_or_dietitian_note
*32 = subjective
*33 = objective
*34 = assessment
*35 = plan
*36 = updated_by
*37 = time_stamp
*38 = string
*39 = clinic_id
*40 = proc_success
*/
DELIMITER //
CREATE PROCEDURE saveProgressNote
	(IN patient_id_in INT, IN date_created_in DATE, 
	IN medical_insurance_in TINYINT(1), IN shoe_size_in VARCHAR(50), 
	IN allergic_to_medications_in TINYINT(1), IN allergies_in VARCHAR(50), 
	IN weight_in DECIMAL(7,2), IN height_feet_in INT, IN height_inches_in INT, 
	IN weight_reduction_goal_in DECIMAL(7,2), IN pulse_in INT, 
	IN respirations_in INT, IN temperature_in DECIMAL(7,2), IN foot_screening_in TINYINT(1), 
	IN medications_in VARCHAR(50), IN a1c_in DECIMAL(7,2), IN glucose_in DECIMAL(7,2), 
	IN waist_in DECIMAL(7,2), IN bp_systole_in INT, IN bp_diastole_in INT, 
	IN ace_or_arb_in TINYINT(1), IN bmi_in DECIMAL(7,2), IN class_attendance_in DATE, 
	IN eye_screening_result_in VARCHAR(50), IN foot_screening_result_in VARCHAR(50), 
	IN psychological_screening_result_in INT, IN physical_activity_in INT, 
	IN smoking_status_in TINYINT(1), IN compliance_in DECIMAL(7,2), 
	IN hospitalization_date_in DATE, IN nurse_or_dietitian_note_in VARCHAR(1000), 
	IN subjective_in VARCHAR(1000), IN objective_in VARCHAR(1000), 
	IN assessment_in VARCHAR(1000), IN plan_in VARCHAR(1000), 
	IN user_in VARCHAR(50), IN timestamp_in DATETIME, IN string_in VARCHAR(64),
	IN clinic_id_in INT, OUT proc_success TINYINT(1))
BEGIN
	DECLARE progress_note_exists INT DEFAULT 0;
	DECLARE note_author_exists INT DEFAULT 0;
	DECLARE this_note_id INT DEFAULT 0;

	DECLARE EXIT HANDLER FOR SQLEXCEPTION ROLLBACK;
	DECLARE EXIT HANDLER FOR SQLWARNING ROLLBACK;

	START TRANSACTION;

	SET proc_success = 0;	

	IF patient_id_in IS NOT NULL 
		AND date_created_in IS NOT NULL THEN 
		SELECT COUNT(*) INTO progress_note_exists 
		FROM ProgressNote pn 
		WHERE pn.patient_id = patient_id_in 
		AND pn.date_created = date_created_in;
	IF progress_note_exists > 0 THEN 
		UPDATE ProgressNote pn 
			SET pn.medical_insurance = medical_insurance_in, 
			pn.shoe_size = shoe_size_in, 
			pn.allergic_to_medications = allergic_to_medications_in, 
			pn.allergies = allergies_in, 
			pn.weight = weight_in, 
			pn.height_feet = height_feet_in, 
			pn.height_inches = height_inches_in, 
			pn.weight_reduction_goal = weight_reduction_goal_in, 
			pn.pulse = pulse_in, 
			pn.respirations = respirations_in, 
			pn.temperature = temperature_in, 
			pn.foot_screening = foot_screening_in, 
			pn.medications = medications_in, 
			pn.a1c = a1c_in, 
			pn.glucose = glucose_in, 
			pn.nurse_or_dietitian_note = AES_ENCRYPT(nurse_or_dietitian_note_in, string_in), 
			pn.subjective = AES_ENCRYPT(subjective_in, string_in), 
			pn.objective = AES_ENCRYPT(objective_in, string_in), 
			pn.assessment = AES_ENCRYPT(assessment_in, string_in), 
			pn.plan = AES_ENCRYPT(plan_in, string_in) 
			WHERE pn.patient_id = patient_id_in 
			AND pn.date_created = date_created_in;		

/*Getting the note ID*/
		SELECT pn.progress_note_id INTO this_note_id 
			FROM ProgressNote pn 
			WHERE pn.patient_id = patient_id_in 
			AND pn.date_created = date_created_in;
	ELSE 
		INSERT INTO ProgressNote VALUES(NULL, patient_id_in, date_created_in, 
			medical_insurance_in, shoe_size_in, allergic_to_medications_in, 
			allergies_in, weight_in, height_feet_in, height_inches_in, 
			weight_reduction_goal_in, pulse_in, respirations_in, 
			temperature_in, foot_screening_in, medications_in, 
			a1c_in, glucose_in, 
			AES_ENCRYPT(nurse_or_dietitian_note_in, string_in), 
			AES_ENCRYPT(subjective_in, string_in), 
			AES_ENCRYPT(objective_in, string_in), 
			AES_ENCRYPT(assessment_in, string_in), 
			AES_ENCRYPT(plan_in, string_in));
/*Getting the note ID*/
		SET this_note_id = LAST_INSERT_ID();
	END IF;
		SELECT COUNT(*) INTO note_author_exists 
			FROM ProgressNoteAuthor pna 
			WHERE pna.progress_note_id = this_note_id 
			AND pna.datetime_recorded = timestamp_in 
			AND pna.updated_by = user_in;
/*Using the note ID*/
		IF note_author_exists = 0 THEN 
			INSERT INTO ProgressNoteAuthor VALUES(this_note_id, 
				timestamp_in, user_in);
		END IF;	

	END IF;

	CALL addResults(patient_id_in, NULL, NULL, NULL, NULL, 
			NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 
			NULL, bmi_in, waist_in, bp_systole_in, bp_diastole_in, 
			class_attendance_in, eye_screening_result_in, 
			foot_screening_result_in, psychological_screening_result_in, 
			physical_activity_in, NULL, NULL, NULL, NULL, NULL, NULL,
			smoking_status_in, NULL, NULL, NULL, NULL, 
			compliance_in, hospitalization_date_in, NULL, NULL, 
			date_created_in, NULL, ace_or_arb_in, 
			user_in, clinic_id_in, string_in, 
			@success);

SELECT @success INTO proc_success;

COMMIT;
END ; //
DELIMITER ;

/**
*USAGE: to retrieve the messages for automated emails 
* for a given clinic and subject 
*CALL getEmailMessages(?, ?, ?);
*1 = clinic_id
*2 = subject
*3 = proc_success
*/
DELIMITER //
CREATE PROCEDURE getEmailMessages
	(IN clinic_id_in INT, IN subject_in VARCHAR(50), 
	OUT proc_success TINYINT(1))
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION ROLLBACK;
	DECLARE EXIT HANDLER FOR SQLWARNING ROLLBACK;

	START TRANSACTION;

	SET proc_success = 0;

	SELECT em.language AS 'language', em.message AS 'message' 
	FROM EmailMessage em
	WHERE em.clinic_id = clinic_id_in 
	AND em.subject = subject_in;

	SET proc_success = 1;

COMMIT;
END ; //
DELIMITER ;

/**
*USAGE: to retrieve the patient call list for a given 
* clinic and reminder subject
*CALL getCallList(?, ?, ?, ?);
*1 = clinic_id
*2 = subject
*3 = string
*4 = proc_success
*/
DELIMITER //
CREATE PROCEDURE getCallList
	(IN clinic_id_in INT, IN subject_in VARCHAR(50), 
	IN string_in VARCHAR(64), OUT proc_success TINYINT(1))
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION ROLLBACK;
	DECLARE EXIT HANDLER FOR SQLWARNING ROLLBACK;
	
	START TRANSACTION;

	SET proc_success = 0;

	IF subject_in = 'Clinic Visit Reminder' THEN 

	SELECT p.patient_id AS 'patient id', 
		CAST(AES_DECRYPT(p.first_name, string_in) AS CHAR(50)) AS 'first name', 
		CAST(AES_DECRYPT(p.last_name, string_in) AS CHAR(50)) AS 'last name', 
		CAST(AES_DECRYPT(p.birth_date, string_in) AS DATE) AS 'birth date', 
		CAST(AES_DECRYPT(p.contact_number, string_in) AS CHAR(50)) AS 'contact number', 
		CAST(AES_DECRYPT(pea.email_address, string_in) AS CHAR(255)) AS 'email', 
		pl.language AS 'language', 
		bp.date_recorded AS 'last BP date' 
		FROM Patient p INNER JOIN PatientClinic pc 
		ON p.patient_id = pc.patient_id  
		LEFT JOIN PatientEmailAddress pea 
		ON p.patient_id = pea.patient_id 
		LEFT JOIN PatientLanguage pl 
		ON p.patient_id = pl.patient_id 
		LEFT JOIN InactivePatient ip 
		ON p.patient_id = ip.patient_id 
		LEFT JOIN BloodPressure bp 
		ON p.patient_id = bp.patient_id 
		WHERE pc.clinic_id = clinic_id_in 
		AND ip.patient_id IS NULL 
		AND bp.date_recorded IN 
		(SELECT MAX(bpsub.date_recorded) 
			FROM BloodPressure bpsub 
			WHERE bpsub.patient_id = p.patient_id)
		ORDER BY bp.date_recorded;

	END IF;
	IF subject_in = 'Lab Work Reminder' THEN 
	
	SELECT p.patient_id AS 'patient id', 
		CAST(AES_DECRYPT(p.first_name, string_in) AS CHAR(50)) AS 'first name', 
		CAST(AES_DECRYPT(p.last_name, string_in) AS CHAR(50)) AS 'last name', 
		CAST(AES_DECRYPT(p.birth_date, string_in) AS DATE) AS 'birth date', 
		CAST(AES_DECRYPT(p.contact_number, string_in) AS CHAR(50)) AS 'contact number', 
		CAST(AES_DECRYPT(pea.email_address, string_in) AS CHAR(255)) AS 'email', 
		pl.language AS 'language', 
		a.date_recorded AS 'last A1C date' 
		FROM Patient p INNER JOIN PatientClinic pc 
		ON p.patient_id = pc.patient_id  
		LEFT JOIN PatientEmailAddress pea 
		ON p.patient_id = pea.patient_id 
		LEFT JOIN PatientLanguage pl 
		ON p.patient_id = pl.patient_id 
		LEFT JOIN InactivePatient ip 
		ON p.patient_id = ip.patient_id 
		LEFT JOIN A1C a 
		ON p.patient_id = a.patient_id 
		WHERE pc.clinic_id = clinic_id_in 
		AND ip.patient_id IS NULL 
		AND ((a.date_recorded IN 
		(SELECT MAX(asub.date_recorded) 
			FROM A1C asub 
			WHERE asub.patient_id = p.patient_id)
		AND a.date_recorded < (DATE(NOW()) - INTERVAL 3 MONTH))
		OR a.date_recorded IS NULL) 
		ORDER BY a.date_recorded;

	END IF;

	SET proc_success = 1;

COMMIT;
END ; //
DELIMITER ;

/**
*USAGE: to retrieve clinic population treatment class information:
* 
*CALL getTreatmentStatistics(?, ?);
*1 = clinic_id
*2 = string
*3 = proc_success
*/
DELIMITER //
CREATE PROCEDURE getTreatmentStatistics
	(IN clinic_id_in INT, IN string_in VARCHAR(64), 
	OUT proc_success TINYINT(1))
BEGIN

	DECLARE EXIT HANDLER FOR SQLEXCEPTION ROLLBACK;
	DECLARE EXIT HANDLER FOR SQLWARNING ROLLBACK;

	START TRANSACTION;

	SET proc_success = 0;

/*A1C averages by treatment class*/

	SELECT AVG(SecondTable.result - FirstTable.result) AS 'total avg change'
	FROM (SELECT a.patient_id, a.result 
		FROM A1C a 
		WHERE a.clinic_id = clinic_id_in 
		AND 1 < (SELECT COUNT(b.patient_id) 
		FROM A1C b 
		WHERE b.patient_id = a.patient_id) 
		AND a.date_recorded IN 
		(SELECT MIN(c.date_recorded) 
		FROM A1C c 
		WHERE c.patient_id = a.patient_id)) AS FirstTable, 
	(SELECT a.patient_id, a.result 
		FROM A1C a 
		WHERE a.clinic_id = clinic_id_in 
		AND 1 < (SELECT COUNT(b.patient_id) 
		FROM A1C b 
		WHERE b.patient_id = a.patient_id) 
		AND a.date_recorded IN 
		(SELECT MAX(c.date_recorded) 
		FROM A1C c 
		WHERE c.patient_id = a.patient_id)) AS SecondTable
	WHERE FirstTable.patient_id = SecondTable.patient_id;

	SELECT AVG(SecondTable.result - FirstTable.result) AS 'class zero avg change'
	FROM (SELECT a.patient_id, a.result 
		FROM A1C a, Patient p, PatientRx r
		WHERE a.clinic_id = clinic_id_in 
		AND a.patient_id = p.patient_id 
		AND p.patient_id = r.patient_id 
		AND r.rx_class = '0'
		AND 1 < (SELECT COUNT(b.patient_id) 
		FROM A1C b 
		WHERE b.patient_id = a.patient_id) 
		AND a.date_recorded IN 
		(SELECT MIN(c.date_recorded) 
		FROM A1C c 
		WHERE c.patient_id = a.patient_id)) AS FirstTable, 
	(SELECT a.patient_id, a.result 
		FROM A1C a, Patient p, PatientRx r 
		WHERE a.clinic_id = clinic_id_in 
		AND a.patient_id = p.patient_id 
		AND p.patient_id = r.patient_id 
		AND r.rx_class = '0'
		AND 1 < (SELECT COUNT(b.patient_id) 
		FROM A1C b 
		WHERE b.patient_id = a.patient_id) 
		AND a.date_recorded IN 
		(SELECT MAX(c.date_recorded) 
		FROM A1C c 
		WHERE c.patient_id = a.patient_id)) AS SecondTable
	WHERE FirstTable.patient_id = SecondTable.patient_id;

	SELECT AVG(SecondTable.result - FirstTable.result) AS 'class one avg change'
	FROM (SELECT a.patient_id, a.result 
		FROM A1C a, Patient p, PatientRx r
		WHERE a.clinic_id = clinic_id_in 
		AND a.patient_id = p.patient_id 
		AND p.patient_id = r.patient_id 
		AND r.rx_class = 'I'
		AND 1 < (SELECT COUNT(b.patient_id) 
		FROM A1C b 
		WHERE b.patient_id = a.patient_id) 
		AND a.date_recorded IN 
		(SELECT MIN(c.date_recorded) 
		FROM A1C c 
		WHERE c.patient_id = a.patient_id)) AS FirstTable, 
	(SELECT a.patient_id, a.result 
		FROM A1C a, Patient p, PatientRx r 
		WHERE a.clinic_id = clinic_id_in 
		AND a.patient_id = p.patient_id 
		AND p.patient_id = r.patient_id 
		AND r.rx_class = 'I'
		AND 1 < (SELECT COUNT(b.patient_id) 
		FROM A1C b 
		WHERE b.patient_id = a.patient_id) 
		AND a.date_recorded IN 
		(SELECT MAX(c.date_recorded) 
		FROM A1C c 
		WHERE c.patient_id = a.patient_id)) AS SecondTable
	WHERE FirstTable.patient_id = SecondTable.patient_id;

	SELECT AVG(SecondTable.result - FirstTable.result) AS 'class two avg change'
	FROM (SELECT a.patient_id, a.result 
		FROM A1C a, Patient p, PatientRx r
		WHERE a.clinic_id = clinic_id_in 
		AND a.patient_id = p.patient_id 
		AND p.patient_id = r.patient_id 
		AND r.rx_class = 'II'
		AND 1 < (SELECT COUNT(b.patient_id) 
		FROM A1C b 
		WHERE b.patient_id = a.patient_id) 
		AND a.date_recorded IN 
		(SELECT MIN(c.date_recorded) 
		FROM A1C c 
		WHERE c.patient_id = a.patient_id)) AS FirstTable, 
	(SELECT a.patient_id, a.result 
		FROM A1C a, Patient p, PatientRx r 
		WHERE a.clinic_id = clinic_id_in 
		AND a.patient_id = p.patient_id 
		AND p.patient_id = r.patient_id 
		AND r.rx_class = 'II'
		AND 1 < (SELECT COUNT(b.patient_id) 
		FROM A1C b 
		WHERE b.patient_id = a.patient_id) 
		AND a.date_recorded IN 
		(SELECT MAX(c.date_recorded) 
		FROM A1C c 
		WHERE c.patient_id = a.patient_id)) AS SecondTable
	WHERE FirstTable.patient_id = SecondTable.patient_id;

	SELECT AVG(SecondTable.result - FirstTable.result) AS 'class three avg change'
	FROM (SELECT a.patient_id, a.result 
		FROM A1C a, Patient p, PatientRx r
		WHERE a.clinic_id = clinic_id_in 
		AND a.patient_id = p.patient_id 
		AND p.patient_id = r.patient_id 
		AND r.rx_class = 'III'
		AND 1 < (SELECT COUNT(b.patient_id) 
		FROM A1C b 
		WHERE b.patient_id = a.patient_id) 
		AND a.date_recorded IN 
		(SELECT MIN(c.date_recorded) 
		FROM A1C c 
		WHERE c.patient_id = a.patient_id)) AS FirstTable, 
	(SELECT a.patient_id, a.result 
		FROM A1C a, Patient p, PatientRx r 
		WHERE a.clinic_id = clinic_id_in 
		AND a.patient_id = p.patient_id 
		AND p.patient_id = r.patient_id 
		AND r.rx_class = 'III'
		AND 1 < (SELECT COUNT(b.patient_id) 
		FROM A1C b 
		WHERE b.patient_id = a.patient_id) 
		AND a.date_recorded IN 
		(SELECT MAX(c.date_recorded) 
		FROM A1C c 
		WHERE c.patient_id = a.patient_id)) AS SecondTable
	WHERE FirstTable.patient_id = SecondTable.patient_id;

	SELECT AVG(SecondTable.result - FirstTable.result) AS 'class four avg change'
	FROM (SELECT a.patient_id, a.result 
		FROM A1C a, Patient p, PatientRx r
		WHERE a.clinic_id = clinic_id_in 
		AND a.patient_id = p.patient_id 
		AND p.patient_id = r.patient_id 
		AND r.rx_class = 'IV'
		AND 1 < (SELECT COUNT(b.patient_id) 
		FROM A1C b 
		WHERE b.patient_id = a.patient_id) 
		AND a.date_recorded IN 
		(SELECT MIN(c.date_recorded) 
		FROM A1C c 
		WHERE c.patient_id = a.patient_id)) AS FirstTable, 
	(SELECT a.patient_id, a.result 
		FROM A1C a, Patient p, PatientRx r 
		WHERE a.clinic_id = clinic_id_in 
		AND a.patient_id = p.patient_id 
		AND p.patient_id = r.patient_id 
		AND r.rx_class = 'IV'
		AND 1 < (SELECT COUNT(b.patient_id) 
		FROM A1C b 
		WHERE b.patient_id = a.patient_id) 
		AND a.date_recorded IN 
		(SELECT MAX(c.date_recorded) 
		FROM A1C c 
		WHERE c.patient_id = a.patient_id)) AS SecondTable
	WHERE FirstTable.patient_id = SecondTable.patient_id;

	SELECT AVG(SecondTable.result - FirstTable.result) AS 'class five avg change'
	FROM (SELECT a.patient_id, a.result 
		FROM A1C a, Patient p, PatientRx r
		WHERE a.clinic_id = clinic_id_in 
		AND a.patient_id = p.patient_id 
		AND p.patient_id = r.patient_id 
		AND r.rx_class = 'V'
		AND 1 < (SELECT COUNT(b.patient_id) 
		FROM A1C b 
		WHERE b.patient_id = a.patient_id) 
		AND a.date_recorded IN 
		(SELECT MIN(c.date_recorded) 
		FROM A1C c 
		WHERE c.patient_id = a.patient_id)) AS FirstTable, 
	(SELECT a.patient_id, a.result 
		FROM A1C a, Patient p, PatientRx r 
		WHERE a.clinic_id = clinic_id_in 
		AND a.patient_id = p.patient_id 
		AND p.patient_id = r.patient_id 
		AND r.rx_class = 'V'
		AND 1 < (SELECT COUNT(b.patient_id) 
		FROM A1C b 
		WHERE b.patient_id = a.patient_id) 
		AND a.date_recorded IN 
		(SELECT MAX(c.date_recorded) 
		FROM A1C c 
		WHERE c.patient_id = a.patient_id)) AS SecondTable
	WHERE FirstTable.patient_id = SecondTable.patient_id;

	SELECT AVG(SecondTable.result - FirstTable.result) AS 'class unknown avg change'
	FROM (SELECT a.patient_id, a.result 
		FROM A1C a, Patient p, PatientRx r
		WHERE a.clinic_id = clinic_id_in 
		AND a.patient_id = p.patient_id 
		AND p.patient_id = r.patient_id 
		AND r.rx_class = 'U'
		AND 1 < (SELECT COUNT(b.patient_id) 
		FROM A1C b 
		WHERE b.patient_id = a.patient_id) 
		AND a.date_recorded IN 
		(SELECT MIN(c.date_recorded) 
		FROM A1C c 
		WHERE c.patient_id = a.patient_id)) AS FirstTable, 
	(SELECT a.patient_id, a.result 
		FROM A1C a, Patient p, PatientRx r 
		WHERE a.clinic_id = clinic_id_in 
		AND a.patient_id = p.patient_id 
		AND p.patient_id = r.patient_id 
		AND r.rx_class = 'U'
		AND 1 < (SELECT COUNT(b.patient_id) 
		FROM A1C b 
		WHERE b.patient_id = a.patient_id) 
		AND a.date_recorded IN 
		(SELECT MAX(c.date_recorded) 
		FROM A1C c 
		WHERE c.patient_id = a.patient_id)) AS SecondTable
	WHERE FirstTable.patient_id = SecondTable.patient_id;

/*treatment class counts*/

	SELECT COUNT(DISTINCT pr.patient_id) AS 'class zero count' 
		FROM PatientRx pr
		WHERE pr.clinic_id = clinic_id_in 
		AND pr.rx_class = '0' 
		AND pr.date_recorded IN 
		(SELECT MAX(pra.date_recorded) 
		FROM PatientRx pra 
		WHERE pra.clinic_id = clinic_id_in 
		AND pra.patient_id = pr.patient_id);

	SELECT COUNT(DISTINCT pr.patient_id) AS 'class one count' 
		FROM PatientRx pr
		WHERE pr.clinic_id = clinic_id_in 
		AND pr.rx_class = 'I' 
		AND pr.date_recorded IN 
		(SELECT MAX(pra.date_recorded) 
		FROM PatientRx pra 
		WHERE pra.clinic_id = clinic_id_in 
		AND pra.patient_id = pr.patient_id);

	SELECT COUNT(DISTINCT pr.patient_id) AS 'class two count' 
		FROM PatientRx pr
		WHERE pr.clinic_id = clinic_id_in 
		AND pr.rx_class = 'II' 
		AND pr.date_recorded IN 
		(SELECT MAX(pra.date_recorded) 
		FROM PatientRx pra 
		WHERE pra.clinic_id = clinic_id_in 
		AND pra.patient_id = pr.patient_id);

	SELECT COUNT(DISTINCT pr.patient_id) AS 'class three count' 
		FROM PatientRx pr
		WHERE pr.clinic_id = clinic_id_in 
		AND pr.rx_class = 'III' 
		AND pr.date_recorded IN 
		(SELECT MAX(pra.date_recorded) 
		FROM PatientRx pra 
		WHERE pra.clinic_id = clinic_id_in 
		AND pra.patient_id = pr.patient_id);

	SELECT COUNT(DISTINCT pr.patient_id) AS 'class four count' 
		FROM PatientRx pr
		WHERE pr.clinic_id = clinic_id_in 
		AND pr.rx_class = 'IV' 
		AND pr.date_recorded IN 
		(SELECT MAX(pra.date_recorded) 
		FROM PatientRx pra 
		WHERE pra.clinic_id = clinic_id_in 
		AND pra.patient_id = pr.patient_id);

	SELECT COUNT(DISTINCT pr.patient_id) AS 'class five count' 
		FROM PatientRx pr
		WHERE pr.clinic_id = clinic_id_in 
		AND pr.rx_class = 'V' 
		AND pr.date_recorded IN 
		(SELECT MAX(pra.date_recorded) 
		FROM PatientRx pra 
		WHERE pra.clinic_id = clinic_id_in 
		AND pra.patient_id = pr.patient_id);

	SELECT COUNT(DISTINCT pr.patient_id) AS 'class unknown count' 
		FROM PatientRx pr
		WHERE pr.clinic_id = clinic_id_in 
		AND pr.rx_class = 'U' 
		AND pr.date_recorded IN 
		(SELECT MAX(pra.date_recorded) 
		FROM PatientRx pra 
		WHERE pra.clinic_id = clinic_id_in 
		AND pra.patient_id = pr.patient_id);

/*treatment class counts for males*/

	SELECT COUNT(DISTINCT pr.patient_id) AS 'males class zero count' 
		FROM PatientRx pr, Patient p
		WHERE pr.clinic_id = clinic_id_in 
		AND pr.rx_class = '0' 
		AND LOWER(CAST(AES_DECRYPT(p.gender, string_in) AS CHAR(50))) = 'male' 
		AND pr.patient_id = p.patient_id 
		AND pr.date_recorded IN 
		(SELECT MAX(pra.date_recorded) 
		FROM PatientRx pra 
		WHERE pra.clinic_id = clinic_id_in 
		AND pra.patient_id = pr.patient_id);

	SELECT COUNT(DISTINCT pr.patient_id) AS 'males class one count' 
		FROM PatientRx pr, Patient p
		WHERE pr.clinic_id = clinic_id_in 
		AND pr.rx_class = 'I' 
		AND LOWER(CAST(AES_DECRYPT(p.gender, string_in) AS CHAR(50))) = 'male' 
		AND pr.patient_id = p.patient_id 
		AND pr.date_recorded IN 
		(SELECT MAX(pra.date_recorded) 
		FROM PatientRx pra 
		WHERE pra.clinic_id = clinic_id_in 
		AND pra.patient_id = pr.patient_id);

	SELECT COUNT(DISTINCT pr.patient_id) AS 'males class two count' 
		FROM PatientRx pr, Patient p
		WHERE pr.clinic_id = clinic_id_in 
		AND pr.rx_class = 'II' 
		AND LOWER(CAST(AES_DECRYPT(p.gender, string_in) AS CHAR(50))) = 'male' 
		AND pr.patient_id = p.patient_id 
		AND pr.date_recorded IN 
		(SELECT MAX(pra.date_recorded) 
		FROM PatientRx pra 
		WHERE pra.clinic_id = clinic_id_in 
		AND pra.patient_id = pr.patient_id);

	SELECT COUNT(DISTINCT pr.patient_id) AS 'males class three count' 
		FROM PatientRx pr, Patient p
		WHERE pr.clinic_id = clinic_id_in 
		AND pr.rx_class = 'III' 
		AND LOWER(CAST(AES_DECRYPT(p.gender, string_in) AS CHAR(50))) = 'male' 
		AND pr.patient_id = p.patient_id 
		AND pr.date_recorded IN 
		(SELECT MAX(pra.date_recorded) 
		FROM PatientRx pra 
		WHERE pra.clinic_id = clinic_id_in 
		AND pra.patient_id = pr.patient_id);

	SELECT COUNT(DISTINCT pr.patient_id) AS 'males class four count' 
		FROM PatientRx pr, Patient p
		WHERE pr.clinic_id = clinic_id_in 
		AND pr.rx_class = 'IV' 
		AND LOWER(CAST(AES_DECRYPT(p.gender, string_in) AS CHAR(50))) = 'male' 
		AND pr.patient_id = p.patient_id 
		AND pr.date_recorded IN 
		(SELECT MAX(pra.date_recorded) 
		FROM PatientRx pra 
		WHERE pra.clinic_id = clinic_id_in 
		AND pra.patient_id = pr.patient_id);

	SELECT COUNT(DISTINCT pr.patient_id) AS 'males class five count' 
		FROM PatientRx pr, Patient p
		WHERE pr.clinic_id = clinic_id_in 
		AND pr.rx_class = 'V' 
		AND LOWER(CAST(AES_DECRYPT(p.gender, string_in) AS CHAR(50))) = 'male' 
		AND pr.patient_id = p.patient_id 
		AND pr.date_recorded IN 
		(SELECT MAX(pra.date_recorded) 
		FROM PatientRx pra 
		WHERE pra.clinic_id = clinic_id_in 
		AND pra.patient_id = pr.patient_id);

	SELECT COUNT(DISTINCT pr.patient_id) AS 'males class unknown count' 
		FROM PatientRx pr, Patient p
		WHERE pr.clinic_id = clinic_id_in 
		AND pr.rx_class = 'U' 
		AND LOWER(CAST(AES_DECRYPT(p.gender, string_in) AS CHAR(50))) = 'male' 
		AND pr.patient_id = p.patient_id 
		AND pr.date_recorded IN 
		(SELECT MAX(pra.date_recorded) 
		FROM PatientRx pra 
		WHERE pra.clinic_id = clinic_id_in 
		AND pra.patient_id = pr.patient_id);

/*treatment class counts for females*/

	SELECT COUNT(DISTINCT pr.patient_id) AS 'females class zero count' 
		FROM PatientRx pr, Patient p
		WHERE pr.clinic_id = clinic_id_in 
		AND pr.rx_class = '0' 
		AND LOWER(CAST(AES_DECRYPT(p.gender, string_in) AS CHAR(50))) = 'female' 
		AND pr.patient_id = p.patient_id 
		AND pr.date_recorded IN 
		(SELECT MAX(pra.date_recorded) 
		FROM PatientRx pra 
		WHERE pra.clinic_id = clinic_id_in 
		AND pra.patient_id = pr.patient_id);

	SELECT COUNT(DISTINCT pr.patient_id) AS 'females class one count' 
		FROM PatientRx pr, Patient p
		WHERE pr.clinic_id = clinic_id_in 
		AND pr.rx_class = 'I' 
		AND LOWER(CAST(AES_DECRYPT(p.gender, string_in) AS CHAR(50))) = 'female' 
		AND pr.patient_id = p.patient_id 
		AND pr.date_recorded IN 
		(SELECT MAX(pra.date_recorded) 
		FROM PatientRx pra 
		WHERE pra.clinic_id = clinic_id_in 
		AND pra.patient_id = pr.patient_id);

	SELECT COUNT(DISTINCT pr.patient_id) AS 'females class two count' 
		FROM PatientRx pr, Patient p
		WHERE pr.clinic_id = clinic_id_in 
		AND pr.rx_class = 'II' 
		AND LOWER(CAST(AES_DECRYPT(p.gender, string_in) AS CHAR(50))) = 'female' 
		AND pr.patient_id = p.patient_id 
		AND pr.date_recorded IN 
		(SELECT MAX(pra.date_recorded) 
		FROM PatientRx pra 
		WHERE pra.clinic_id = clinic_id_in 
		AND pra.patient_id = pr.patient_id);

	SELECT COUNT(DISTINCT pr.patient_id) AS 'females class three count' 
		FROM PatientRx pr, Patient p
		WHERE pr.clinic_id = clinic_id_in 
		AND pr.rx_class = 'III' 
		AND LOWER(CAST(AES_DECRYPT(p.gender, string_in) AS CHAR(50))) = 'female' 
		AND pr.patient_id = p.patient_id 
		AND pr.date_recorded IN 
		(SELECT MAX(pra.date_recorded) 
		FROM PatientRx pra 
		WHERE pra.clinic_id = clinic_id_in 
		AND pra.patient_id = pr.patient_id);

	SELECT COUNT(DISTINCT pr.patient_id) AS 'females class four count' 
		FROM PatientRx pr, Patient p
		WHERE pr.clinic_id = clinic_id_in 
		AND pr.rx_class = 'IV' 
		AND LOWER(CAST(AES_DECRYPT(p.gender, string_in) AS CHAR(50))) = 'female' 
		AND pr.patient_id = p.patient_id 
		AND pr.date_recorded IN 
		(SELECT MAX(pra.date_recorded) 
		FROM PatientRx pra 
		WHERE pra.clinic_id = clinic_id_in 
		AND pra.patient_id = pr.patient_id);

	SELECT COUNT(DISTINCT pr.patient_id) AS 'females class five count' 
		FROM PatientRx pr, Patient p
		WHERE pr.clinic_id = clinic_id_in 
		AND pr.rx_class = 'V' 
		AND LOWER(CAST(AES_DECRYPT(p.gender, string_in) AS CHAR(50))) = 'female' 
		AND pr.patient_id = p.patient_id 
		AND pr.date_recorded IN 
		(SELECT MAX(pra.date_recorded) 
		FROM PatientRx pra 
		WHERE pra.clinic_id = clinic_id_in 
		AND pra.patient_id = pr.patient_id);

	SELECT COUNT(DISTINCT pr.patient_id) AS 'females class unknown count' 
		FROM PatientRx pr, Patient p
		WHERE pr.clinic_id = clinic_id_in 
		AND pr.rx_class = 'U' 
		AND LOWER(CAST(AES_DECRYPT(p.gender, string_in) AS CHAR(50))) = 'female' 
		AND pr.patient_id = p.patient_id 
		AND pr.date_recorded IN 
		(SELECT MAX(pra.date_recorded) 
		FROM PatientRx pra 
		WHERE pra.clinic_id = clinic_id_in 
		AND pra.patient_id = pr.patient_id);

/*treatment class counts for whites*/

	SELECT COUNT(DISTINCT pr.patient_id) AS 'whites class zero count' 
		FROM PatientRx pr, Patient p
		WHERE pr.clinic_id = clinic_id_in 
		AND pr.rx_class = '0' 
		AND LOWER(CAST(AES_DECRYPT(p.race, string_in) AS CHAR(50))) = 'white' 
		AND pr.patient_id = p.patient_id 
		AND pr.date_recorded IN 
		(SELECT MAX(pra.date_recorded) 
		FROM PatientRx pra 
		WHERE pra.clinic_id = clinic_id_in 
		AND pra.patient_id = pr.patient_id);

	SELECT COUNT(DISTINCT pr.patient_id) AS 'whites class one count' 
		FROM PatientRx pr, Patient p
		WHERE pr.clinic_id = clinic_id_in 
		AND pr.rx_class = 'I' 
		AND LOWER(CAST(AES_DECRYPT(p.race, string_in) AS CHAR(50))) = 'white' 
		AND pr.patient_id = p.patient_id 
		AND pr.date_recorded IN 
		(SELECT MAX(pra.date_recorded) 
		FROM PatientRx pra 
		WHERE pra.clinic_id = clinic_id_in 
		AND pra.patient_id = pr.patient_id);

	SELECT COUNT(DISTINCT pr.patient_id) AS 'whites class two count' 
		FROM PatientRx pr, Patient p
		WHERE pr.clinic_id = clinic_id_in 
		AND pr.rx_class = 'II' 
		AND LOWER(CAST(AES_DECRYPT(p.race, string_in) AS CHAR(50))) = 'white' 
		AND pr.patient_id = p.patient_id 
		AND pr.date_recorded IN 
		(SELECT MAX(pra.date_recorded) 
		FROM PatientRx pra 
		WHERE pra.clinic_id = clinic_id_in 
		AND pra.patient_id = pr.patient_id);

	SELECT COUNT(DISTINCT pr.patient_id) AS 'whites class three count' 
		FROM PatientRx pr, Patient p
		WHERE pr.clinic_id = clinic_id_in 
		AND pr.rx_class = 'III' 
		AND LOWER(CAST(AES_DECRYPT(p.race, string_in) AS CHAR(50))) = 'white' 
		AND pr.patient_id = p.patient_id 
		AND pr.date_recorded IN 
		(SELECT MAX(pra.date_recorded) 
		FROM PatientRx pra 
		WHERE pra.clinic_id = clinic_id_in 
		AND pra.patient_id = pr.patient_id);

	SELECT COUNT(DISTINCT pr.patient_id) AS 'whites class four count' 
		FROM PatientRx pr, Patient p
		WHERE pr.clinic_id = clinic_id_in 
		AND pr.rx_class = 'IV' 
		AND LOWER(CAST(AES_DECRYPT(p.race, string_in) AS CHAR(50))) = 'white' 
		AND pr.patient_id = p.patient_id 
		AND pr.date_recorded IN 
		(SELECT MAX(pra.date_recorded) 
		FROM PatientRx pra 
		WHERE pra.clinic_id = clinic_id_in 
		AND pra.patient_id = pr.patient_id);

	SELECT COUNT(DISTINCT pr.patient_id) AS 'whites class five count' 
		FROM PatientRx pr, Patient p
		WHERE pr.clinic_id = clinic_id_in 
		AND pr.rx_class = 'V' 
		AND LOWER(CAST(AES_DECRYPT(p.race, string_in) AS CHAR(50))) = 'white' 
		AND pr.patient_id = p.patient_id 
		AND pr.date_recorded IN 
		(SELECT MAX(pra.date_recorded) 
		FROM PatientRx pra 
		WHERE pra.clinic_id = clinic_id_in 
		AND pra.patient_id = pr.patient_id);

	SELECT COUNT(DISTINCT pr.patient_id) AS 'whites class unknown count' 
		FROM PatientRx pr, Patient p
		WHERE pr.clinic_id = clinic_id_in 
		AND pr.rx_class = 'U' 
		AND LOWER(CAST(AES_DECRYPT(p.race, string_in) AS CHAR(50))) = 'white' 
		AND pr.patient_id = p.patient_id 
		AND pr.date_recorded IN 
		(SELECT MAX(pra.date_recorded) 
		FROM PatientRx pra 
		WHERE pra.clinic_id = clinic_id_in 
		AND pra.patient_id = pr.patient_id);

/*treatment class counts for African Americans*/

	SELECT COUNT(DISTINCT pr.patient_id) AS 'african americans class zero count' 
		FROM PatientRx pr, Patient p
		WHERE pr.clinic_id = clinic_id_in 
		AND pr.rx_class = '0' 
		AND LOWER(CAST(AES_DECRYPT(p.race, string_in) AS CHAR(50))) = 'african american' 
		AND pr.patient_id = p.patient_id 
		AND pr.date_recorded IN 
		(SELECT MAX(pra.date_recorded) 
		FROM PatientRx pra 
		WHERE pra.clinic_id = clinic_id_in 
		AND pra.patient_id = pr.patient_id);

	SELECT COUNT(DISTINCT pr.patient_id) AS 'african americans class one count' 
		FROM PatientRx pr, Patient p
		WHERE pr.clinic_id = clinic_id_in 
		AND pr.rx_class = 'I' 
		AND LOWER(CAST(AES_DECRYPT(p.race, string_in) AS CHAR(50))) = 'african american' 
		AND pr.patient_id = p.patient_id 
		AND pr.date_recorded IN 
		(SELECT MAX(pra.date_recorded) 
		FROM PatientRx pra 
		WHERE pra.clinic_id = clinic_id_in 
		AND pra.patient_id = pr.patient_id);

	SELECT COUNT(DISTINCT pr.patient_id) AS 'african americans class two count' 
		FROM PatientRx pr, Patient p
		WHERE pr.clinic_id = clinic_id_in 
		AND pr.rx_class = 'II' 
		AND LOWER(CAST(AES_DECRYPT(p.race, string_in) AS CHAR(50))) = 'african american' 
		AND pr.patient_id = p.patient_id 
		AND pr.date_recorded IN 
		(SELECT MAX(pra.date_recorded) 
		FROM PatientRx pra 
		WHERE pra.clinic_id = clinic_id_in 
		AND pra.patient_id = pr.patient_id);

	SELECT COUNT(DISTINCT pr.patient_id) AS 'african americans class three count' 
		FROM PatientRx pr, Patient p
		WHERE pr.clinic_id = clinic_id_in 
		AND pr.rx_class = 'III' 
		AND LOWER(CAST(AES_DECRYPT(p.race, string_in) AS CHAR(50))) = 'african american' 
		AND pr.patient_id = p.patient_id 
		AND pr.date_recorded IN 
		(SELECT MAX(pra.date_recorded) 
		FROM PatientRx pra 
		WHERE pra.clinic_id = clinic_id_in 
		AND pra.patient_id = pr.patient_id);

	SELECT COUNT(DISTINCT pr.patient_id) AS 'african americans class four count' 
		FROM PatientRx pr, Patient p
		WHERE pr.clinic_id = clinic_id_in 
		AND pr.rx_class = 'IV' 
		AND LOWER(CAST(AES_DECRYPT(p.race, string_in) AS CHAR(50))) = 'african american' 
		AND pr.patient_id = p.patient_id 
		AND pr.date_recorded IN 
		(SELECT MAX(pra.date_recorded) 
		FROM PatientRx pra 
		WHERE pra.clinic_id = clinic_id_in 
		AND pra.patient_id = pr.patient_id);

	SELECT COUNT(DISTINCT pr.patient_id) AS 'african americans class five count' 
		FROM PatientRx pr, Patient p
		WHERE pr.clinic_id = clinic_id_in 
		AND pr.rx_class = 'V' 
		AND LOWER(CAST(AES_DECRYPT(p.race, string_in) AS CHAR(50))) = 'african american' 
		AND pr.patient_id = p.patient_id 
		AND pr.date_recorded IN 
		(SELECT MAX(pra.date_recorded) 
		FROM PatientRx pra 
		WHERE pra.clinic_id = clinic_id_in 
		AND pra.patient_id = pr.patient_id);

	SELECT COUNT(DISTINCT pr.patient_id) AS 'african americans class unknown count' 
		FROM PatientRx pr, Patient p
		WHERE pr.clinic_id = clinic_id_in 
		AND pr.rx_class = 'U' 
		AND LOWER(CAST(AES_DECRYPT(p.race, string_in) AS CHAR(50))) = 'african american' 
		AND pr.patient_id = p.patient_id 
		AND pr.date_recorded IN 
		(SELECT MAX(pra.date_recorded) 
		FROM PatientRx pra 
		WHERE pra.clinic_id = clinic_id_in 
		AND pra.patient_id = pr.patient_id);

/*treatment class counts for Asians/Pacific Islanders*/

	SELECT COUNT(DISTINCT pr.patient_id) AS 'asian/pacific islander class zero count' 
		FROM PatientRx pr, Patient p
		WHERE pr.clinic_id = clinic_id_in 
		AND pr.rx_class = '0' 
		AND LOWER(CAST(AES_DECRYPT(p.race, string_in) AS CHAR(50))) = 'asian/pacific islander' 
		AND pr.patient_id = p.patient_id 
		AND pr.date_recorded IN 
		(SELECT MAX(pra.date_recorded) 
		FROM PatientRx pra 
		WHERE pra.clinic_id = clinic_id_in 
		AND pra.patient_id = pr.patient_id);

	SELECT COUNT(DISTINCT pr.patient_id) AS 'asian/pacific islander class one count' 
		FROM PatientRx pr, Patient p
		WHERE pr.clinic_id = clinic_id_in 
		AND pr.rx_class = 'I' 
		AND LOWER(CAST(AES_DECRYPT(p.race, string_in) AS CHAR(50))) = 'asian/pacific islander' 
		AND pr.patient_id = p.patient_id 
		AND pr.date_recorded IN 
		(SELECT MAX(pra.date_recorded) 
		FROM PatientRx pra 
		WHERE pra.clinic_id = clinic_id_in 
		AND pra.patient_id = pr.patient_id);

	SELECT COUNT(DISTINCT pr.patient_id) AS 'asian/pacific islander class two count' 
		FROM PatientRx pr, Patient p
		WHERE pr.clinic_id = clinic_id_in 
		AND pr.rx_class = 'II' 
		AND LOWER(CAST(AES_DECRYPT(p.race, string_in) AS CHAR(50))) = 'asian/pacific islander' 
		AND pr.patient_id = p.patient_id 
		AND pr.date_recorded IN 
		(SELECT MAX(pra.date_recorded) 
		FROM PatientRx pra 
		WHERE pra.clinic_id = clinic_id_in 
		AND pra.patient_id = pr.patient_id);

	SELECT COUNT(DISTINCT pr.patient_id) AS 'asian/pacific islander class three count' 
		FROM PatientRx pr, Patient p
		WHERE pr.clinic_id = clinic_id_in 
		AND pr.rx_class = 'III' 
		AND LOWER(CAST(AES_DECRYPT(p.race, string_in) AS CHAR(50))) = 'asian/pacific islander' 
		AND pr.patient_id = p.patient_id 
		AND pr.date_recorded IN 
		(SELECT MAX(pra.date_recorded) 
		FROM PatientRx pra 
		WHERE pra.clinic_id = clinic_id_in 
		AND pra.patient_id = pr.patient_id);

	SELECT COUNT(DISTINCT pr.patient_id) AS 'asian/pacific islander class four count' 
		FROM PatientRx pr, Patient p
		WHERE pr.clinic_id = clinic_id_in 
		AND pr.rx_class = 'IV' 
		AND LOWER(CAST(AES_DECRYPT(p.race, string_in) AS CHAR(50))) = 'asian/pacific islander' 
		AND pr.patient_id = p.patient_id 
		AND pr.date_recorded IN 
		(SELECT MAX(pra.date_recorded) 
		FROM PatientRx pra 
		WHERE pra.clinic_id = clinic_id_in 
		AND pra.patient_id = pr.patient_id);

	SELECT COUNT(DISTINCT pr.patient_id) AS 'asian/pacific islander class five count' 
		FROM PatientRx pr, Patient p
		WHERE pr.clinic_id = clinic_id_in 
		AND pr.rx_class = 'V' 
		AND LOWER(CAST(AES_DECRYPT(p.race, string_in) AS CHAR(50))) = 'asian/pacific islander' 
		AND pr.patient_id = p.patient_id 
		AND pr.date_recorded IN 
		(SELECT MAX(pra.date_recorded) 
		FROM PatientRx pra 
		WHERE pra.clinic_id = clinic_id_in 
		AND pra.patient_id = pr.patient_id);

	SELECT COUNT(DISTINCT pr.patient_id) AS 'asian/pacific islander class unknown count' 
		FROM PatientRx pr, Patient p
		WHERE pr.clinic_id = clinic_id_in 
		AND pr.rx_class = 'U' 
		AND LOWER(CAST(AES_DECRYPT(p.race, string_in) AS CHAR(50))) = 'asian/pacific islander' 
		AND pr.patient_id = p.patient_id 
		AND pr.date_recorded IN 
		(SELECT MAX(pra.date_recorded) 
		FROM PatientRx pra 
		WHERE pra.clinic_id = clinic_id_in 
		AND pra.patient_id = pr.patient_id);

/*treatment class counts for American Indians/Alaska Natives*/

	SELECT COUNT(DISTINCT pr.patient_id) AS 'american indian/alaska native class zero count' 
		FROM PatientRx pr, Patient p
		WHERE pr.clinic_id = clinic_id_in 
		AND pr.rx_class = '0' 
		AND LOWER(CAST(AES_DECRYPT(p.race, string_in) AS CHAR(50))) 
			= 'american indian/alaska native' 
		AND pr.patient_id = p.patient_id 
		AND pr.date_recorded IN 
		(SELECT MAX(pra.date_recorded) 
		FROM PatientRx pra 
		WHERE pra.clinic_id = clinic_id_in 
		AND pra.patient_id = pr.patient_id);

	SELECT COUNT(DISTINCT pr.patient_id) AS 'american indian/alaska native class one count' 
		FROM PatientRx pr, Patient p
		WHERE pr.clinic_id = clinic_id_in 
		AND pr.rx_class = 'I' 
		AND LOWER(CAST(AES_DECRYPT(p.race, string_in) AS CHAR(50))) 
			= 'american indian/alaska native' 
		AND pr.patient_id = p.patient_id 
		AND pr.date_recorded IN 
		(SELECT MAX(pra.date_recorded) 
		FROM PatientRx pra 
		WHERE pra.clinic_id = clinic_id_in 
		AND pra.patient_id = pr.patient_id);

	SELECT COUNT(DISTINCT pr.patient_id) AS 'american indian/alaska native class two count' 
		FROM PatientRx pr, Patient p
		WHERE pr.clinic_id = clinic_id_in 
		AND pr.rx_class = 'II' 
		AND LOWER(CAST(AES_DECRYPT(p.race, string_in) AS CHAR(50))) 
			= 'american indian/alaska native' 
		AND pr.patient_id = p.patient_id 
		AND pr.date_recorded IN 
		(SELECT MAX(pra.date_recorded) 
		FROM PatientRx pra 
		WHERE pra.clinic_id = clinic_id_in 
		AND pra.patient_id = pr.patient_id);

	SELECT COUNT(DISTINCT pr.patient_id) AS 'american indian/alaska native class three count' 
		FROM PatientRx pr, Patient p
		WHERE pr.clinic_id = clinic_id_in 
		AND pr.rx_class = 'III' 
		AND LOWER(CAST(AES_DECRYPT(p.race, string_in) AS CHAR(50))) 
			= 'american indian/alaska native' 
		AND pr.patient_id = p.patient_id 
		AND pr.date_recorded IN 
		(SELECT MAX(pra.date_recorded) 
		FROM PatientRx pra 
		WHERE pra.clinic_id = clinic_id_in 
		AND pra.patient_id = pr.patient_id);

	SELECT COUNT(DISTINCT pr.patient_id) AS 'american indian/alaska native class four count' 
		FROM PatientRx pr, Patient p
		WHERE pr.clinic_id = clinic_id_in 
		AND pr.rx_class = 'IV' 
		AND LOWER(CAST(AES_DECRYPT(p.race, string_in) AS CHAR(50))) 
			= 'american indian/alaska native' 
		AND pr.patient_id = p.patient_id 
		AND pr.date_recorded IN 
		(SELECT MAX(pra.date_recorded) 
		FROM PatientRx pra 
		WHERE pra.clinic_id = clinic_id_in 
		AND pra.patient_id = pr.patient_id);

	SELECT COUNT(DISTINCT pr.patient_id) AS 'american indian/alaska native class five count' 
		FROM PatientRx pr, Patient p
		WHERE pr.clinic_id = clinic_id_in 
		AND pr.rx_class = 'V' 
		AND LOWER(CAST(AES_DECRYPT(p.race, string_in) AS CHAR(50))) 
			= 'american indian/alaska native' 
		AND pr.patient_id = p.patient_id 
		AND pr.date_recorded IN 
		(SELECT MAX(pra.date_recorded) 
		FROM PatientRx pra 
		WHERE pra.clinic_id = clinic_id_in 
		AND pra.patient_id = pr.patient_id);

	SELECT COUNT(DISTINCT pr.patient_id) AS 'american indian/alaska native class unknown count' 
		FROM PatientRx pr, Patient p
		WHERE pr.clinic_id = clinic_id_in 
		AND pr.rx_class = 'U' 
		AND LOWER(CAST(AES_DECRYPT(p.race, string_in) AS CHAR(50))) 
			= 'american indian/alaska native' 
		AND pr.patient_id = p.patient_id 
		AND pr.date_recorded IN 
		(SELECT MAX(pra.date_recorded) 
		FROM PatientRx pra 
		WHERE pra.clinic_id = clinic_id_in 
		AND pra.patient_id = pr.patient_id);

/*treatment class counts for Hispanics*/

	SELECT COUNT(DISTINCT pr.patient_id) AS 'hispanics class zero count' 
		FROM PatientRx pr, Patient p
		WHERE pr.clinic_id = clinic_id_in 
		AND pr.rx_class = '0' 
		AND LOWER(CAST(AES_DECRYPT(p.race, string_in) AS CHAR(50))) = 'hispanic' 
		AND pr.patient_id = p.patient_id 
		AND pr.date_recorded IN 
		(SELECT MAX(pra.date_recorded) 
		FROM PatientRx pra 
		WHERE pra.clinic_id = clinic_id_in 
		AND pra.patient_id = pr.patient_id);

	SELECT COUNT(DISTINCT pr.patient_id) AS 'hispanics class one count' 
		FROM PatientRx pr, Patient p
		WHERE pr.clinic_id = clinic_id_in 
		AND pr.rx_class = 'I' 
		AND LOWER(CAST(AES_DECRYPT(p.race, string_in) AS CHAR(50))) = 'hispanic' 
		AND pr.patient_id = p.patient_id 
		AND pr.date_recorded IN 
		(SELECT MAX(pra.date_recorded) 
		FROM PatientRx pra 
		WHERE pra.clinic_id = clinic_id_in 
		AND pra.patient_id = pr.patient_id);

	SELECT COUNT(DISTINCT pr.patient_id) AS 'hispanics class two count' 
		FROM PatientRx pr, Patient p
		WHERE pr.clinic_id = clinic_id_in 
		AND pr.rx_class = 'II' 
		AND LOWER(CAST(AES_DECRYPT(p.race, string_in) AS CHAR(50))) = 'hispanic' 
		AND pr.patient_id = p.patient_id 
		AND pr.date_recorded IN 
		(SELECT MAX(pra.date_recorded) 
		FROM PatientRx pra 
		WHERE pra.clinic_id = clinic_id_in 
		AND pra.patient_id = pr.patient_id);

	SELECT COUNT(DISTINCT pr.patient_id) AS 'hispanics class three count' 
		FROM PatientRx pr, Patient p
		WHERE pr.clinic_id = clinic_id_in 
		AND pr.rx_class = 'III' 
		AND LOWER(CAST(AES_DECRYPT(p.race, string_in) AS CHAR(50))) = 'hispanic' 
		AND pr.patient_id = p.patient_id 
		AND pr.date_recorded IN 
		(SELECT MAX(pra.date_recorded) 
		FROM PatientRx pra 
		WHERE pra.clinic_id = clinic_id_in 
		AND pra.patient_id = pr.patient_id);

	SELECT COUNT(DISTINCT pr.patient_id) AS 'hispanics class four count' 
		FROM PatientRx pr, Patient p
		WHERE pr.clinic_id = clinic_id_in 
		AND pr.rx_class = 'IV' 
		AND LOWER(CAST(AES_DECRYPT(p.race, string_in) AS CHAR(50))) = 'hispanic' 
		AND pr.patient_id = p.patient_id 
		AND pr.date_recorded IN 
		(SELECT MAX(pra.date_recorded) 
		FROM PatientRx pra 
		WHERE pra.clinic_id = clinic_id_in 
		AND pra.patient_id = pr.patient_id);

	SELECT COUNT(DISTINCT pr.patient_id) AS 'hispanics class five count' 
		FROM PatientRx pr, Patient p
		WHERE pr.clinic_id = clinic_id_in 
		AND pr.rx_class = 'V' 
		AND LOWER(CAST(AES_DECRYPT(p.race, string_in) AS CHAR(50))) = 'hispanic' 
		AND pr.patient_id = p.patient_id 
		AND pr.date_recorded IN 
		(SELECT MAX(pra.date_recorded) 
		FROM PatientRx pra 
		WHERE pra.clinic_id = clinic_id_in 
		AND pra.patient_id = pr.patient_id);

	SELECT COUNT(DISTINCT pr.patient_id) AS 'hispanics class unknown count' 
		FROM PatientRx pr, Patient p
		WHERE pr.clinic_id = clinic_id_in 
		AND pr.rx_class = 'U' 
		AND LOWER(CAST(AES_DECRYPT(p.race, string_in) AS CHAR(50))) = 'hispanic' 
		AND pr.patient_id = p.patient_id 
		AND pr.date_recorded IN 
		(SELECT MAX(pra.date_recorded) 
		FROM PatientRx pra 
		WHERE pra.clinic_id = clinic_id_in 
		AND pra.patient_id = pr.patient_id);

/*treatment class counts for Middle Easterners*/

	SELECT COUNT(DISTINCT pr.patient_id) AS 'middle eastern class zero count' 
		FROM PatientRx pr, Patient p
		WHERE pr.clinic_id = clinic_id_in 
		AND pr.rx_class = '0' 
		AND LOWER(CAST(AES_DECRYPT(p.race, string_in) AS CHAR(50))) = 'middle eastern' 
		AND pr.patient_id = p.patient_id 
		AND pr.date_recorded IN 
		(SELECT MAX(pra.date_recorded) 
		FROM PatientRx pra 
		WHERE pra.clinic_id = clinic_id_in 
		AND pra.patient_id = pr.patient_id);

	SELECT COUNT(DISTINCT pr.patient_id) AS 'middle eastern class one count' 
		FROM PatientRx pr, Patient p
		WHERE pr.clinic_id = clinic_id_in 
		AND pr.rx_class = 'I' 
		AND LOWER(CAST(AES_DECRYPT(p.race, string_in) AS CHAR(50))) = 'middle eastern' 
		AND pr.patient_id = p.patient_id 
		AND pr.date_recorded IN 
		(SELECT MAX(pra.date_recorded) 
		FROM PatientRx pra 
		WHERE pra.clinic_id = clinic_id_in 
		AND pra.patient_id = pr.patient_id);

	SELECT COUNT(DISTINCT pr.patient_id) AS 'middle eastern class two count' 
		FROM PatientRx pr, Patient p
		WHERE pr.clinic_id = clinic_id_in 
		AND pr.rx_class = 'II' 
		AND LOWER(CAST(AES_DECRYPT(p.race, string_in) AS CHAR(50))) = 'middle eastern' 
		AND pr.patient_id = p.patient_id 
		AND pr.date_recorded IN 
		(SELECT MAX(pra.date_recorded) 
		FROM PatientRx pra 
		WHERE pra.clinic_id = clinic_id_in 
		AND pra.patient_id = pr.patient_id);

	SELECT COUNT(DISTINCT pr.patient_id) AS 'middle eastern class three count' 
		FROM PatientRx pr, Patient p
		WHERE pr.clinic_id = clinic_id_in 
		AND pr.rx_class = 'III' 
		AND LOWER(CAST(AES_DECRYPT(p.race, string_in) AS CHAR(50))) = 'middle eastern' 
		AND pr.patient_id = p.patient_id 
		AND pr.date_recorded IN 
		(SELECT MAX(pra.date_recorded) 
		FROM PatientRx pra 
		WHERE pra.clinic_id = clinic_id_in 
		AND pra.patient_id = pr.patient_id);

	SELECT COUNT(DISTINCT pr.patient_id) AS 'middle eastern class four count' 
		FROM PatientRx pr, Patient p
		WHERE pr.clinic_id = clinic_id_in 
		AND pr.rx_class = 'IV' 
		AND LOWER(CAST(AES_DECRYPT(p.race, string_in) AS CHAR(50))) = 'middle eastern' 
		AND pr.patient_id = p.patient_id 
		AND pr.date_recorded IN 
		(SELECT MAX(pra.date_recorded) 
		FROM PatientRx pra 
		WHERE pra.clinic_id = clinic_id_in 
		AND pra.patient_id = pr.patient_id);

	SELECT COUNT(DISTINCT pr.patient_id) AS 'middle eastern class five count' 
		FROM PatientRx pr, Patient p
		WHERE pr.clinic_id = clinic_id_in 
		AND pr.rx_class = 'V' 
		AND LOWER(CAST(AES_DECRYPT(p.race, string_in) AS CHAR(50))) = 'middle eastern' 
		AND pr.patient_id = p.patient_id 
		AND pr.date_recorded IN 
		(SELECT MAX(pra.date_recorded) 
		FROM PatientRx pra 
		WHERE pra.clinic_id = clinic_id_in 
		AND pra.patient_id = pr.patient_id);

	SELECT COUNT(DISTINCT pr.patient_id) AS 'middle eastern class unknown count' 
		FROM PatientRx pr, Patient p
		WHERE pr.clinic_id = clinic_id_in 
		AND pr.rx_class = 'U' 
		AND LOWER(CAST(AES_DECRYPT(p.race, string_in) AS CHAR(50))) = 'middle eastern' 
		AND pr.patient_id = p.patient_id 
		AND pr.date_recorded IN 
		(SELECT MAX(pra.date_recorded) 
		FROM PatientRx pra 
		WHERE pra.clinic_id = clinic_id_in 
		AND pra.patient_id = pr.patient_id);

/*treatment class counts for other ethnicities*/

	SELECT COUNT(DISTINCT pr.patient_id) AS 'other class zero count' 
		FROM PatientRx pr, Patient p
		WHERE pr.clinic_id = clinic_id_in 
		AND pr.rx_class = '0' 
		AND LOWER(CAST(AES_DECRYPT(p.race, string_in) AS CHAR(50))) = 'other' 
		AND pr.patient_id = p.patient_id 
		AND pr.date_recorded IN 
		(SELECT MAX(pra.date_recorded) 
		FROM PatientRx pra 
		WHERE pra.clinic_id = clinic_id_in 
		AND pra.patient_id = pr.patient_id);

	SELECT COUNT(DISTINCT pr.patient_id) AS 'other class one count' 
		FROM PatientRx pr, Patient p
		WHERE pr.clinic_id = clinic_id_in 
		AND pr.rx_class = 'I' 
		AND LOWER(CAST(AES_DECRYPT(p.race, string_in) AS CHAR(50))) = 'other' 
		AND pr.patient_id = p.patient_id 
		AND pr.date_recorded IN 
		(SELECT MAX(pra.date_recorded) 
		FROM PatientRx pra 
		WHERE pra.clinic_id = clinic_id_in 
		AND pra.patient_id = pr.patient_id);

	SELECT COUNT(DISTINCT pr.patient_id) AS 'other class two count' 
		FROM PatientRx pr, Patient p
		WHERE pr.clinic_id = clinic_id_in 
		AND pr.rx_class = 'II' 
		AND LOWER(CAST(AES_DECRYPT(p.race, string_in) AS CHAR(50))) = 'other' 
		AND pr.patient_id = p.patient_id 
		AND pr.date_recorded IN 
		(SELECT MAX(pra.date_recorded) 
		FROM PatientRx pra 
		WHERE pra.clinic_id = clinic_id_in 
		AND pra.patient_id = pr.patient_id);

	SELECT COUNT(DISTINCT pr.patient_id) AS 'other class three count' 
		FROM PatientRx pr, Patient p
		WHERE pr.clinic_id = clinic_id_in 
		AND pr.rx_class = 'III' 
		AND LOWER(CAST(AES_DECRYPT(p.race, string_in) AS CHAR(50))) = 'other' 
		AND pr.patient_id = p.patient_id 
		AND pr.date_recorded IN 
		(SELECT MAX(pra.date_recorded) 
		FROM PatientRx pra 
		WHERE pra.clinic_id = clinic_id_in 
		AND pra.patient_id = pr.patient_id);

	SELECT COUNT(DISTINCT pr.patient_id) AS 'other class four count' 
		FROM PatientRx pr, Patient p
		WHERE pr.clinic_id = clinic_id_in 
		AND pr.rx_class = 'IV' 
		AND LOWER(CAST(AES_DECRYPT(p.race, string_in) AS CHAR(50))) = 'other' 
		AND pr.patient_id = p.patient_id 
		AND pr.date_recorded IN 
		(SELECT MAX(pra.date_recorded) 
		FROM PatientRx pra 
		WHERE pra.clinic_id = clinic_id_in 
		AND pra.patient_id = pr.patient_id);

	SELECT COUNT(DISTINCT pr.patient_id) AS 'other class five count' 
		FROM PatientRx pr, Patient p
		WHERE pr.clinic_id = clinic_id_in 
		AND pr.rx_class = 'V' 
		AND LOWER(CAST(AES_DECRYPT(p.race, string_in) AS CHAR(50))) = 'other' 
		AND pr.patient_id = p.patient_id 
		AND pr.date_recorded IN 
		(SELECT MAX(pra.date_recorded) 
		FROM PatientRx pra 
		WHERE pra.clinic_id = clinic_id_in 
		AND pra.patient_id = pr.patient_id);

	SELECT COUNT(DISTINCT pr.patient_id) AS 'other class unknown count' 
		FROM PatientRx pr, Patient p
		WHERE pr.clinic_id = clinic_id_in 
		AND pr.rx_class = 'U' 
		AND LOWER(CAST(AES_DECRYPT(p.race, string_in) AS CHAR(50))) = 'other' 
		AND pr.patient_id = p.patient_id 
		AND pr.date_recorded IN 
		(SELECT MAX(pra.date_recorded) 
		FROM PatientRx pra 
		WHERE pra.clinic_id = clinic_id_in 
		AND pra.patient_id = pr.patient_id);

SET proc_success = 1;

COMMIT;
END ; //
DELIMITER ;

/**
*USAGE: to retrieve the dates and items of the most recent quality 
* checklist items for a patient 
*CALL getMostRecentChecklistItems(?, ?, ?);
*1 = patient_id
*2 = clinic_id 
*3 = proc_success
*/
DELIMITER //
CREATE PROCEDURE getMostRecentChecklistItems
	(IN patient_id_in INT, IN clinic_id_in INT, OUT proc_success TINYINT(1))
BEGIN
	
	DECLARE EXIT HANDLER FOR SQLEXCEPTION ROLLBACK;
	DECLARE EXIT HANDLER FOR SQLWARNING ROLLBACK;

	START TRANSACTION;

	SET proc_success = 0;

	DROP TEMPORARY TABLE IF EXISTS TempChecklist;

	CREATE TEMPORARY TABLE TempChecklist (
		patient_id INT NOT NULL, 
		date_recorded DATE NOT NULL, 
		responsibility VARCHAR(255) NOT NULL, 
		updated_by VARCHAR(50) NOT NULL,
		clinic_id INT NOT NULL,
		PRIMARY KEY (patient_id, date_recorded, responsibility)
	);

	INSERT INTO TempChecklist 
		SELECT * FROM QualityChecklist 
		WHERE patient_id = patient_id_in;

/*CHAR(13) is the return carriage*/

	SELECT DISTINCT qa.date_recorded AS 'date recorded', 
	qa.responsibility AS 'responsibility'
	FROM QualityChecklist qa, Quality q
	WHERE qa.patient_id = patient_id_in 
	AND qa.clinic_id = clinic_id_in 
	AND REPLACE(qa.responsibility, CHAR(13), '') = REPLACE(q.responsibility, CHAR(13), '')
	AND q.active = 1 
	AND qa.date_recorded IN 
	(SELECT MAX(tc.date_recorded) 
	FROM TempChecklist tc 
	WHERE REPLACE(tc.responsibility, CHAR(13), '') = REPLACE(qa.responsibility, CHAR(13), '')
	AND tc.patient_id = qa.patient_id);

	DROP TEMPORARY TABLE IF EXISTS TempChecklist;

	SET proc_success = 1;

COMMIT;
END ; //
DELIMITER ;

-- Create user named registry_user and grant privileges

DELIMITER //
CREATE PROCEDURE drop_user_if_exists()
BEGIN
    DECLARE userCount BIGINT DEFAULT 0 ;

    SELECT COUNT(*) INTO userCount FROM mysql.user
    WHERE User = 'registry_user' and  Host = 'localhost';

    IF userCount > 0 THEN
        DROP USER registry_user@localhost;
    END IF;
END ; //
DELIMITER ;

CALL drop_user_if_exists() ;

CREATE USER registry_user@localhost IDENTIFIED BY 'somePassword';

GRANT SELECT ON `mysql`.`proc` TO registry_user@localhost;

GRANT EXECUTE 
ON diabetes_registry.*
TO registry_user@localhost;

