
SET FOREIGN_KEY_CHECKS=0;
SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";



--
-- Dumping data for table `Clinic`
--

INSERT INTO `Clinic` (`clinic_id`, `name`, `address`, `phone_number`, `registration_key`, `salt`) VALUES
(1, 'Test Clinic', '123 Nice St. Beautiful City', '(123) 456-7890', 
'f6a90d11d7a58045920efc262a18ec73bf5e4272add58de2d0bf8959e78c97bc', 
'SSbNpXEql3aAp78QgU8V2dyIZzLWJ1+1qlO+M0VLZhY=');


--
-- Dumping data for table `EmailMessageSubject`
--

INSERT INTO `EmailMessageSubject` (`subject`) VALUES
('Clinic Visit Reminder'),
('Lab Work Reminder');


--
-- Dumping data for table `EyeExamDefinition`
--

INSERT INTO `EyeExamDefinition` (`eye_exam_code`, `definition`) VALUES
('0', 'No Retinopathy'),
('1', 'Diabetic Retinopathy'),
('2', 'Other'),
('3', 'Not Examined');


--
-- Dumping data for table `FootExamRiskDefinition`
--

INSERT INTO `FootExamRiskDefinition` (`risk_category`, `definition`) VALUES
('0', 'No loss of protective sensation.'),
('1', 'Loss of protective sensation.'),
('2', 'Loss of protective sensation with either high pressure (callus/deformity) or poor 		circulation. '),
('3', 'History of plantar ulceration, neuropathic fracture (Charcot foot), or amputation.'),
('N', 'Not Tested');


--
-- Dumping data for table `HealthyTarget`
--

INSERT INTO `HealthyTarget` (`measurement`, `lower_target`, `upper_target`) VALUES
('a1c', '0.00', '7.00'),
('alt', '0.00', '44.00'),
('ast', '0.00', '40.00'),
('bloodpressurediastole', '0.00', '90.00'),
('bloodpressuresystole', '0.00', '140.00'),
('bmi', '17.00', '26.00'),
('class', '1.00', '10000.00'),
('creatinine', '0.25', '1.27'),
('egfr', '60.00', '10000.00'),
('eye', '0.00', '100.00'),
('foot', '0.00', '100.00'),
('glucoseac', '79.00', '131.00'),
('glucosepc', '79.00', '180.00'),
('hdlfemale', '50.00', '10000.00'),
('hdlmale', '40.00', '10000.00'),
('hepatitisb', '0.00', '100.00'),
('hospitalization', '0.00', '0.00'),
('influenzavaccine', '0.00', '100.00'),
('ldl', '0.00', '100.00'),
('ldlpostmi', '0.00', '70.00'),
('pcv13', '0.00', '100.00'),
('physicalactivity', '149.00', '10000.00'),
('ppsv23', '0.00', '100.00'),
('psa', '0.00', '3.99'),
('psychologicalscreening', '0.00', '15.00'),
('smoking', '0.00', '0.00'),
('t4', '4.50', '12.00'),
('tdap', '0.00', '100.00'),
('telephonefollowup', '1.00', '1.00'),
('triglycerides', '0.00', '150.00'),
('tsh', '0.45', '4.50'),
('uacr', '0.00', '30.00'),
('waistfemale', '0.00', '35.00'),
('waistmale', '0.00', '40.00'),
('zoster', '0.00', '100.00');


--
-- Dumping data for table `Language`
--

INSERT INTO `Language` (`language`) VALUES
('English'),
('Spanish');


--
-- Dumping data for table `Medication`
--

INSERT INTO `Medication` (`med_id`, `med_name`, `med_class`) VALUES
('0', 'None', 'None'),
('AlphaG', 'Acarbose', 'alpha-Glucosidase inhibitors'),
('AMY1', 'Pramlintide (Sylmin)', 'Amylin mimetics'),
('BA1', 'Colesevelam', 'bile acid sequestrants'),
('D+M', 'Metformin Combo (Januvia, Jentadueto)', 'Biguaide + DPP4'),
('D1', 'Sitaglipton (Januvia)', 'DPP-4 inhibitors'),
('D2', 'Linaglipton', 'DPP-4 inhibitors'),
('D3', 'Saxaglipton', 'DPP-4 inhibitors'),
('D4', 'Vildaglipton', 'DPP-4 inhibitors'),
('D5', 'Aloglipton', 'DPP-4 inhibitors'),
('G1', 'Liraglutide (Victoza)', 'Glp-1 receptor agonists'),
('G2', 'Exenatide (Byetta)', 'Glp-1 receptor agonists'),
('G3', 'Exenatide extended-release (Bydueron)', 'Glp-1 receptor agonists'),
('G4', 'Dulaglutide', 'Glp-1 receptor agonists'),
('G5', 'Albiglutide (Eperzan and Tanzeum)', 'Glp-1 receptor agonists'),
('G6', 'Lixisenatide (Lyxumia)', 'Glp-1 receptor agonists'),
('IIA1', 'Human NPH (Humulin N)', 'Insulin Intermediate-acting'),
('IIA2', 'Human NPH (Novolin N)', 'Insulin Intermediate-acting'),
('ILA1', '(Detemir) Levemir', 'Insulin Basal analogs (long-acting)'),
('ILA2', '(Glargine) Lantus', 'Insulin Basal analogs (long-acting)'),
('IPM', 'Insulin Premixed', 'Novalog 70/30, Humanlog 75/25, several others'),
('IRA1', 'Aspart (Novolog)', 'Insulin Rapid-acting analogs'),
('IRA2', 'Lispro (Humalog)', 'Insulin Rapid-acting analogs'),
('IRA3', 'Glulisine (Apidra)', 'Insulin Rapid-acting analogs'),
('ISA1', 'Human Regular (Humulin)', 'Insulin Short-acting'),
('IULA1', 'U-300 Glargine (Trujeo)', 'Insulin Basal analogs (Ultra long-acting)'),
('IULA2', 'Degludec (Tresiba)', 'Insulin Basal analogs (Ultra long-acting)'),
('IULA3', 'Pegylated Lispro', 'Insulin Basal analogs (Ultra long-acting)'),
('M1', 'Metformin', 'Biguanides'),
('M2', 'Metformin XR', 'None'),
('MEG1', 'Repaglinide (prandin)', 'Meglitinides'),
('MEG2', 'Nateglinide (Starlix)', 'Meglitinides'),
('OCA', 'Other Combination agent', 'No classification'),
('OI', 'Other Insulin', 'No classification'),
('OOA', 'Other oral agent', 'No classification'),
('OPA', 'Other Parenteral agent', 'No classification'),
('S1', 'Glimepride', 'Sulfonyurea 2nd generation'),
('S2', 'Glyburide', 'Sulfonyurea 2nd generation'),
('S3', 'Glipizide', 'Sulfonyurea 2nd generation'),
('SG1', 'Canagliflozin (Invocana)', 'SGLT-2 inhibitors'),
('SG2', 'Dapagliflozin (Farxiga)', 'SGLT-2 inhibitors'),
('SG3', 'Empagliflozin (Jardiance)', 'SGLT-2 inhibitors'),
('Tz', 'Actos (Pioglitazone)', 'TZD');

--
-- Dumping data for table `NoteTopic`
--

INSERT INTO `NoteTopic` (`topic`) VALUES
('Hepatitis B Vaccine'),
('Hospitalization'),
('Influenza Vaccine'),
('LDL'),
('Other'),
('Patient'),
('PCV-13 Vaccine'),
('T4'),
('TDAP Vaccine'),
('Telephone Follow Up'),
('UACR');


--
-- Dumping data for table `PHQ9`
--

INSERT INTO `PHQ9` (`phq_score`, `severity`, `proposed_actions`) VALUES
(0, 'None-minimal', 'None'),
(1, 'None-minimal', 'None'),
(2, 'None-minimal', 'None'),
(3, 'None-minimal', 'None'),
(4, 'None-minimal', 'None'),
(5, 'Mild', 'Watchful waiting; repeat PHQ-9 at follow-up'),
(6, 'Mild', 'Watchful waiting; repeat PHQ-9 at follow-up'),
(7, 'Mild', 'Watchful waiting; repeat PHQ-9 at follow-up'),
(8, 'Mild', 'Watchful waiting; repeat PHQ-9 at follow-up'),
(9, 'Mild', 'Watchful waiting; repeat PHQ-9 at follow-up'),
(10, 'Moderate', 'Treatment plan, considering counseling, follow-up and/or pharmacotherapy'),
(11, 'Moderate', 'Treatment plan, considering counseling, follow-up and/or pharmacotherapy'),
(12, 'Moderate', 'Treatment plan, considering counseling, follow-up and/or pharmacotherapy'),
(13, 'Moderate', 'Treatment plan, considering counseling, follow-up and/or pharmacotherapy'),
(14, 'Moderate', 'Treatment plan, considering counseling, follow-up and/or pharmacotherapy'),
(15, 'Moderately Severe', 'Active treatment with pharmacotherapy and/or psychotherapy'),
(16, 'Moderately Severe', 'Active treatment with pharmacotherapy and/or psychotherapy'),
(17, 'Moderately Severe', 'Active treatment with pharmacotherapy and/or psychotherapy'),
(18, 'Moderately Severe', 'Active treatment with pharmacotherapy and/or psychotherapy'),
(19, 'Moderately Severe', 'Active treatment with pharmacotherapy and/or psychotherapy'),
(20, 'Severe', 'Immediate initiation of pharmacotherapy and, if severe impairment or poor response to therapy, expedited referral to a mental health specialist for psychotherapy and/or collaborative management'),
(21, 'Severe', 'Immediate initiation of pharmacotherapy and, if severe impairment or poor response to therapy, expedited referral to a mental health specialist for psychotherapy and/or collaborative management'),
(22, 'Severe', 'Immediate initiation of pharmacotherapy and, if severe impairment or poor response to therapy, expedited referral to a mental health specialist for psychotherapy and/or collaborative management'),
(23, 'Severe', 'Immediate initiation of pharmacotherapy and, if severe impairment or poor response to therapy, expedited referral to a mental health specialist for psychotherapy and/or collaborative management'),
(24, 'Severe', 'Immediate initiation of pharmacotherapy and, if severe impairment or poor response to therapy, expedited referral to a mental health specialist for psychotherapy and/or collaborative management'),
(25, 'Severe', 'Immediate initiation of pharmacotherapy and, if severe impairment or poor response to therapy, expedited referral to a mental health specialist for psychotherapy and/or collaborative management'),
(26, 'Severe', 'Immediate initiation of pharmacotherapy and, if severe impairment or poor response to therapy, expedited referral to a mental health specialist for psychotherapy and/or collaborative management'),
(27, 'Severe', 'Immediate initiation of pharmacotherapy and, if severe impairment or poor response to therapy, expedited referral to a mental health specialist for psychotherapy and/or collaborative management');


--
-- Dumping data for table `Quality`
--

INSERT INTO `Quality` (`role`, `responsibility`, `active`) VALUES
('during every visit', 'Answer patient questions', 0),
('during every visit', 'Aspirin therapy men and women >50 years', 0),
('during every visit', 'Aspirin therapy men>50 years and women > 50 years', 1),
('during every visit', 'Assess survival needs (literacy, housing, food, clothing, transportation)', 1),
('during every visit', 'Calculate diabetic medication compliance (consumed/prescribed doses)', 1),
('pre-appointment', 'Contact patient for needed laboratory work, provide prescription ', 0),
('pre-appointment', 'Contacts patient for needed laboratory work', 1),
('during every visit', 'Establish 7% weight loss goal over 16 wks if not previously established', 0),
('after visits', 'Establish content of interest for classroom instruction (diet, stress, exercise, glucose testing, complications, medications, insulin)', 0),
('after visit', 'Establish goal for next visit', 1),
('during every visit', 'Establish if  treatment plan was altered from previous visit', 0),
('pre-appointment', 'Establish patient goal for visit', 0),
('during every visit', 'Exam feet for injury', 0),
('during every visit', 'Identify family/friend support system', 1),
('after visit', 'Identify needed tests at visit (foot exam, eye exam)', 1),
('after visit', 'Initiate dental referral', 1),
('after visit', 'Initiate dentist referral', 0),
('during every visit', 'Initiate medication prescription refills', 1),
('after visit', 'Initiate mental health referral for PHQ-9 > 9 ', 1),
('after visits', 'Initiate referral for dentist', 0),
('after visit', 'Initiate Registered Dietitian referral (as needed)', 1),
('after visit', 'Initiate social service referral for barriers to goal attainment;  illiteracy, homelessness, unemployment, food insecuritity, inadequate transportation, obesity (BMI>35), lack of clothing, etc', 1),
('after visits', 'Initiate social worker referral for WeCare if BMI >35, unemployment, homeless, etc', 0),
('pre-appointment', 'Inquire about recent emergent care/hospitalizations with metabolic syndrome related events (hypo/hyperglycemia, hypertensive crisis, depression, infections, etc) and request record', 0),
('during every visit', 'Inquire as to smoking related diseases and alcohol consumption', 0),
('At least once a year', 'Nutritional assessment completed by Registered Dietitian', 1),
('during every visit', 'Order diabetes alert necklace', 1),
('at least once a year', 'Perform comprehensive foot exam', 1),
('at least once a year', 'Perform UACR laboratory testing', 1),
('pre-appointment', 'Print worksheet summary and attaches to chart for new patients without existing worksheet', 0),
('after visits', 'Provide bus passes if transportation a barrier to care', 0),
('at least once a year', 'Provide eye exam (every two years)', 1),
('after visits', 'Provide glucose testing strips', 0),
('after visit', 'Provide insulin syringes ', 1),
('after visit', 'Provide medication prescriptions', 1),
('after visits', 'Provide pedometer', 0),
('after visit', 'Provide prescribed laboratory request form', 1),
('after visits', 'Provide Rx for required lab work for next visit', 0),
('pre-appointment', 'Recent hospitalizations/emergent care, secure medical release and records', 1),
('during every visit', 'Recent vaccines, influenza, tetanus, diphtheria, pertussis, hepatitis B, pneumonia, shingles', 0),
('during every visit', 'Recent vaccines; influenza, tetanus, diphtheria, pertussis, hepatitis B, pneumonia, shingles', 1),
('at least once a year', 'Recommend influenza vaccine', 1),
('during every visit', 'Reconcile all medications', 1),
('during every visit', 'Record BP, weights, heights, BMI, waist circumference, medications, 7% weight loss goal', 0),
('during every visit', 'Record psychological screening results (PHQ-9)', 0),
('pre-appointment', 'Remind patient of needed tests at this visit (foot exam, eye exam)', 0),
('after visit', 'Remind patient to bring blood glucose log book, food & activity tracker and medications including  insulin and supplements', 1),
('during every visit', 'Review all medications, 1 week diabetic medication compliance (number of consumed doses/total number of prescribed doses) ', 0),
('during every visit', 'Review at home glucose log to adjust food intake, exercise or pharmacologic therapy to achieve specific targets', 0),
('during every visit', 'Review B/P and urine albumin to creatinine ratio (UACR) and initiate Angiotension-Converting Enzyme (ACE) or Angiotension-Receptor Blocker (ARB) if B/P>140/90, UACR>30mg/g', 1),
('during every visit', 'Review BP and urine albumin to creatinine ratio (UACR) and initiates Angiotension-Converting Enzyme (ACE) or Angiotension Receptor Blocker (ARB) if BP>140/90, UACR > 30mg/g', 0),
('during every visit', 'Review carbohydrate counting', 1),
('after visit', 'Review criteria for follow-up clinic visit', 1),
('after visit', 'Review drawing up and administration of insulin', 1),
('during every visit', 'Review exercise plan (record in minutes) and limitations to exercise', 0),
('during every visit', 'Review exercise plan and exercise limitations', 1),
('after visit', 'Review glucose testing (DVD) and urine ketone testing', 1),
('during every visit', 'Review hypo/hyperglycemia awareness', 1),
('during every visit', 'Review lab results (HgbA1C, blood glucose, LDL, HDL, triglycerides, AST, ALT, TSH, T4, eGFR, creatinine, and UACR), if results >1 yr initiate lab request', 0),
('during every visit', 'Review lab results (HgbA1C, blood glucose, LDL, HDL, triglycerides, AST, ALT, TSH, T4, eGFR, creatinine, and UACR), if results >3 months initiate lab request', 1),
('during every visit', 'Review laboratory test results (Hgb A1C, blood glucose, LDL, HDL, triglycerides, AST, ALT, TSH, T4, eGFR, creatinine and urine albumen to creatinine ratios), if results > 1 yr initiate lab request', 0),
('during every visit', 'Review need to have a food source available at all times in anticipation of hypoglycemia', 1),
('during every visit', 'Review patient glucose log to adjust food intake, exercise or pharmacologic therapy to achieve specific targets', 1),
('during every visit', 'Review sick day management', 1),
('after visit', 'Review guidelines for sharp disposal', 1),
('after visits', 'Review guidelines for syringe disposal', 0),
('during every visit', 'Review strategy for weight reduction', 1),
('during every visit', 'Review stress management strategies', 1),
('after visits', 'Schedule follow-up visit', 0);


--
-- Dumping data for table `ReasonForInactivity`
--

INSERT INTO `ReasonForInactivity` (`reason`) VALUES
('> 1 year since last clinic visit'),
('age >= 65'),
('deceased'),
('moved away'),
('obtained insurance');


--
-- Dumping data for table `TelephoneFollowUpDefinition`
--

INSERT INTO `TelephoneFollowUpDefinition` (`follow_up_code`, `definition`) VALUES
('Confirmed', 'Spoke with patient in clinic'),
('Contacted', 'Telephone conversation'),
('Contacted(L)', 'Telephone conversation re: Labs'),
('LM', 'Left message'),
('LM(L)', 'Left message re: Labs'),
('NS', 'Disconnected/no service'),
('NVM', 'No voice mail'),
('O', 'Other / Wrong number');

--
-- Dumping data for table `Therapy`
--

INSERT INTO `Therapy` (`rx_class`, `therapy_type`) VALUES
('0', 'No Meds'),
('I', 'Mono'),
('II', 'Dual'),
('III', 'Triple'),
('IV', 'Injectable'),
('U', 'Unknown'),
('V', 'Combination Injectable');


--
-- Dumping data for table `User`
--

INSERT INTO `User` (`user_name`, `first_name`, `last_name`, `job_title`, `date_joined`, `last_login`, `active`, `administrator`) VALUES
('joeUser', 'Joe', 'User', 'Nurse', '2016-02-17 08:01:38', '2016-04-06 10:43:24', 1, 1);

--
-- Dumping data for table `UserCredentials`
--

INSERT INTO `UserCredentials` (`user_name`, `password`, `salt`, `change_password`) VALUES
('joeUser', 'f58f9f51f8d4361e2e8cdebfb5c6f831e95ef5ba46fd1c913a32ce1b0a27cc65', '4QdeOcg+gG1QYm5/BETXDwqDbirx693tgzkqvUvjxFw=', 0);


--
-- Dumping data for table `UserRegistration`
--

INSERT INTO `UserRegistration` (`user_name`, `registration_key`) VALUES
('joeUser', 'f6a90d11d7a58045920efc262a18ec73bf5e4272add58de2d0bf8959e78c97bc');


SET FOREIGN_KEY_CHECKS=1;

