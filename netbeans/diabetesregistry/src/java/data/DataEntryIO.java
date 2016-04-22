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
package data;

import util.ConnectionPool;
import clinic.DataEntryContainer;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.DatabaseUtil;

/**
 * Accesses the database during the data entry process
 *
 * @author Bryan Daniel
 * @version 1, April 8, 2016
 */
public class DataEntryIO {

    /**
     * Connects to the database to add results for a patient
     *
     * @param dec the data entry container
     * @param referenceCharacters the character string
     * @return the boolean indicating success of the operation
     */
    public static boolean addResults(DataEntryContainer dec,
            Object referenceCharacters) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        CallableStatement cs = null;

        try {
            cs = connection.prepareCall("{CALL addResults(?, ?, ?, ?, ?, ?, ?, "
                    + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
                    + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
                    + "?, ?, ?, ?)}");

            /* #1 patient ID */
            cs.setInt(1, dec.getPatientId());

            /* #2 A1C */
            if (dec.getA1c() != null) {
                cs.setBigDecimal(2, dec.getA1c());
            } else {
                cs.setNull(2, java.sql.Types.DECIMAL);
            }

            /* #3 glucose AC */
            if (dec.getGlucoseAc() != null) {
                cs.setBigDecimal(3, dec.getGlucoseAc());
            } else {
                cs.setNull(3, java.sql.Types.DECIMAL);
            }

            /* #4 glucose PC */
            if (dec.getGlucosePc() != null) {
                cs.setBigDecimal(4, dec.getGlucosePc());
            } else {
                cs.setNull(4, java.sql.Types.DECIMAL);
            }

            /* #5 LDL */
            if (dec.getLdl() != null) {
                cs.setBigDecimal(5, dec.getLdl());
            } else {
                cs.setNull(5, java.sql.Types.DECIMAL);
            }

            /* #6 LDL Post MI */
            if (dec.getLdlPostMi() != null) {
                cs.setBigDecimal(6, dec.getLdlPostMi());
            } else {
                cs.setNull(6, java.sql.Types.DECIMAL);
            }

            /* #7 LDL on statin */
            if (dec.getOnStatin() != null) {
                cs.setBoolean(7, dec.getOnStatin());
            } else {
                cs.setNull(7, java.sql.Types.TINYINT);
            }

            /* #8 HDL */
            if (dec.getHdl() != null) {
                cs.setBigDecimal(8, dec.getHdl());
            } else {
                cs.setNull(8, java.sql.Types.DECIMAL);
            }

            /* #9 Triglycerides */
            if (dec.getTriglycerides() != null) {
                cs.setBigDecimal(9, dec.getTriglycerides());
            } else {
                cs.setNull(9, java.sql.Types.DECIMAL);
            }

            /* #10 TSH */
            if (dec.getTsh() != null) {
                cs.setBigDecimal(10, dec.getTsh());
            } else {
                cs.setNull(10, java.sql.Types.DECIMAL);
            }

            /* #11 TSH on thyroid treatment */
            if (dec.getOnThyroidTreatment() != null) {
                cs.setBoolean(11, dec.getOnThyroidTreatment());
            } else {
                cs.setNull(11, java.sql.Types.TINYINT);
            }

            /* #12 T4 */
            if (dec.getT4() != null) {
                cs.setBigDecimal(12, dec.getT4());
            } else {
                cs.setNull(12, java.sql.Types.DECIMAL);
            }

            /* #13 UACR */
            if (dec.getUacr() != null) {
                cs.setBigDecimal(13, dec.getUacr());
            } else {
                cs.setNull(13, java.sql.Types.DECIMAL);
            }

            /* #14 eGFR */
            if (dec.getEgfr() != null) {
                cs.setBigDecimal(14, dec.getEgfr());
            } else {
                cs.setNull(14, java.sql.Types.DECIMAL);
            }

            /* #15 Creatinine */
            if (dec.getCreatinine() != null) {
                cs.setBigDecimal(15, dec.getCreatinine());
            } else {
                cs.setNull(15, java.sql.Types.DECIMAL);
            }

            /* #16 BMI */
            if (dec.getBmi() != null) {
                cs.setBigDecimal(16, dec.getBmi());
            } else {
                cs.setNull(16, java.sql.Types.DECIMAL);
            }

            /* #17 Waist */
            if (dec.getWaist() != null) {
                cs.setBigDecimal(17, dec.getWaist());
            } else {
                cs.setNull(17, java.sql.Types.DECIMAL);
            }

            /* #18 BP systole */
            if (dec.getBloodPressureSystole() != null) {
                cs.setInt(18, dec.getBloodPressureSystole());
            } else {
                cs.setNull(18, java.sql.Types.INTEGER);
            }

            /* #19 BP diastole */
            if (dec.getBloodPressureDiastole() != null) {
                cs.setInt(19, dec.getBloodPressureDiastole());
            } else {
                cs.setNull(19, java.sql.Types.INTEGER);
            }

            /* #20 class date */
            if (dec.getClassDate() != null) {
                cs.setDate(20, dec.getClassDate());
            } else {
                cs.setNull(20, java.sql.Types.DATE);
            }

            /* #21 eye */
            if (dec.getEye() != null) {
                cs.setString(21, dec.getEye());
            } else {
                cs.setNull(21, java.sql.Types.VARCHAR);
            }

            /* #22 foot */
            if (dec.getFoot() != null) {
                cs.setString(22, dec.getFoot());
            } else {
                cs.setNull(22, java.sql.Types.VARCHAR);
            }

            /* #23 psychological screening */
            if (dec.getPsychologicalScreening() != null) {
                cs.setInt(23, dec.getPsychologicalScreening());
            } else {
                cs.setNull(23, java.sql.Types.INTEGER);
            }

            /* #24 physical activity */
            if (dec.getPhysicalActivity() != null) {
                cs.setInt(24, dec.getPhysicalActivity());
            } else {
                cs.setNull(24, java.sql.Types.INTEGER);
            }

            /* #25 influenza vaccine date */
            if (dec.getInfluenzaVaccineDate() != null) {
                cs.setDate(25, dec.getInfluenzaVaccineDate());
            } else {
                cs.setNull(25, java.sql.Types.DATE);
            }

            /* #26 PCV-13 vaccine date */
            if (dec.getPcv13Date() != null) {
                cs.setDate(26, dec.getPcv13Date());
            } else {
                cs.setNull(26, java.sql.Types.DATE);
            }

            /* #27 PPSV-23 date */
            if (dec.getPpsv23Date() != null) {
                cs.setDate(27, dec.getPpsv23Date());
            } else {
                cs.setNull(27, java.sql.Types.DATE);
            }

            /* #28 hepatitis B vaccine date */
            if (dec.getHepatitisBDate() != null) {
                cs.setDate(28, dec.getHepatitisBDate());
            } else {
                cs.setNull(28, java.sql.Types.DATE);
            }

            /* #29 TDAP vaccine date */
            if (dec.getTdapDate() != null) {
                cs.setDate(29, dec.getTdapDate());
            } else {
                cs.setNull(29, java.sql.Types.DATE);
            }

            /* #30 zoster vaccine date */
            if (dec.getZosterDate() != null) {
                cs.setDate(30, dec.getZosterDate());
            } else {
                cs.setNull(30, java.sql.Types.DATE);
            }

            /* #31 smoking status */
            if (dec.getSmoking() != null) {
                cs.setBoolean(31, dec.getSmoking());
            } else {
                cs.setNull(31, java.sql.Types.TINYINT);
            }

            /* #32 telephone follow-up */
            if (dec.getTelephoneFollowUp() != null) {
                cs.setString(32, dec.getTelephoneFollowUp());
            } else {
                cs.setNull(32, java.sql.Types.VARCHAR);
            }

            /* #33 AST */
            if (dec.getAst() != null) {
                cs.setBigDecimal(33, dec.getAst());
            } else {
                cs.setNull(33, java.sql.Types.DECIMAL);
            }

            /* #34 ALT */
            if (dec.getAlt() != null) {
                cs.setBigDecimal(34, dec.getAlt());
            } else {
                cs.setNull(34, java.sql.Types.DECIMAL);
            }

            /* #35 PSA */
            if (dec.getPsa() != null) {
                cs.setBigDecimal(35, dec.getPsa());
            } else {
                cs.setNull(35, java.sql.Types.DECIMAL);
            }

            /* #36 compliance */
            if (dec.getCompliance() != null) {
                cs.setBigDecimal(36, dec.getCompliance());
            } else {
                cs.setNull(36, java.sql.Types.DECIMAL);
            }

            /* #37 ER date */
            if (dec.getHospitalizationDate() != null) {
                cs.setDate(37, dec.getHospitalizationDate());
            } else {
                cs.setNull(37, java.sql.Types.DATE);
            }

            /* #38 note topic */
            if (dec.getNoteTopic() != null) {
                cs.setString(38, dec.getNoteTopic());
            } else {
                cs.setNull(38, java.sql.Types.VARCHAR);
            }

            /* #39 note */
            if (dec.getNote() != null) {
                cs.setString(39, dec.getNote());
            } else {
                cs.setNull(39, java.sql.Types.VARCHAR);
            }

            /* #40 date entered */
            if (dec.getDateEntered() != null) {
                cs.setDate(40, dec.getDateEntered());
            } else {
                cs.setNull(40, java.sql.Types.DATE);
            }

            /* #41 POC */
            if (dec.getPoc() != null) {
                cs.setBoolean(41, dec.getPoc());
            } else {
                cs.setNull(41, java.sql.Types.TINYINT);
            }

            /* #42 ACE or ARB */
            if (dec.getAceOrArb() != null) {
                cs.setBoolean(42, dec.getAceOrArb());
            } else {
                cs.setNull(42, java.sql.Types.TINYINT);
            }

            /* #43 user name */
            if (dec.getUserName() != null) {
                cs.setString(43, dec.getUserName());
            } else {
                cs.setNull(43, java.sql.Types.VARCHAR);
            }

            /* #44 clinic ID */
            if (dec.getClinicId() != null) {
                cs.setInt(44, dec.getClinicId());
            } else {
                cs.setNull(44, java.sql.Types.INTEGER);
            }
            cs.setString(45, (String) referenceCharacters);
            cs.registerOutParameter(46, java.sql.Types.TINYINT);

            /* no result set to return */
            cs.execute();

            /* grab out parameter */
            boolean success = cs.getBoolean(46);
            if (!success) {
                return false;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ReferencesIO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DatabaseUtil.closeCallableStatement(cs);
            pool.freeConnection(connection);
        }
        return true;
    }
}
