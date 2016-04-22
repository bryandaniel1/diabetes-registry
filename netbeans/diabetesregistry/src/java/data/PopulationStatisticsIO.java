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
import clinic.CategoricalValue;
import clinic.DemographicData;
import clinic.Stats;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.DatabaseUtil;

/**
 * Connects to the database to retrieve patient population statistics
 *
 * @author Bryan Daniel
 * @version 1, April 8, 2016
 */
public class PopulationStatisticsIO {

    /**
     * Returns demographic data from the database
     *
     * @param clinicId the clinic ID
     * @param referenceCharacters the character string
     * @return the demographic data
     */
    public static DemographicData getDemographicData(int clinicId,
            Object referenceCharacters) {
        DemographicData demographicData = new DemographicData();
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        CallableStatement cs = null;
        ResultSet rs = null;

        try {
            cs = connection.prepareCall("{CALL getDemographics(?, ?, ?)}");
            cs.setInt(1, clinicId);
            cs.setString(2, (String) referenceCharacters);
            cs.registerOutParameter(3, java.sql.Types.TINYINT);

            /* number of patients and demographic percentages */
            boolean success = cs.execute();
            if (!success) {
                return null;
            }

            rs = cs.getResultSet();
            while (rs.next()) {
                demographicData.setTotalPatients(rs.getInt("number of patients"));
                demographicData.setPercentMale(rs.getBigDecimal("percent male"));
                demographicData.setPercentFemale(rs.getBigDecimal("percent female"));
                demographicData.setPercentWhite(rs.getBigDecimal("percent white"));
                demographicData.setPercentAfricanAmerican(rs.getBigDecimal("percent african american"));
                demographicData.setPercentAsian(rs.getBigDecimal("percent asian"));
                demographicData.setPercentIndian(rs.getBigDecimal("percent indian"));
                demographicData.setPercentHispanic(rs.getBigDecimal("percent hispanic"));
                demographicData.setPercentMiddleEastern(rs.getBigDecimal("percent middle eastern"));
                demographicData.setPercentOther(rs.getBigDecimal("percent other"));
            }

            DatabaseUtil.closeResultSet(rs);

            /* ages */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }
            rs = cs.getResultSet();
            ArrayList<Integer> ages = new ArrayList<>();
            while (rs.next()) {
                ages.add(rs.getInt("age"));
            }
            demographicData.setAges(ages);

        } catch (SQLException ex) {
            Logger.getLogger(ReferencesIO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DatabaseUtil.closeResultSet(rs);
            DatabaseUtil.closeCallableStatement(cs);
            pool.freeConnection(connection);
        }

        return demographicData;
    }

    /**
     * Returns glycemic control data from the database
     *
     * @param clinicId the clinic ID
     * @return the demographic data
     */
    public static Stats[] getGlycemicControl(int clinicId) {
        Stats[] glycemicStats = new Stats[2];
        Stats glycemicStatsByClass = new Stats();
        Stats glycemicStatsByTreatment = new Stats();
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        CallableStatement cs = null;
        ResultSet rs = null;

        try {
            cs = connection.prepareCall("{CALL getGlycemicControl(?, ?)}");
            cs.setInt(1, clinicId);
            cs.registerOutParameter(2, java.sql.Types.TINYINT);

            /* average A1C */
            boolean success = cs.execute();
            if (!success) {
                return null;
            }
            rs = cs.getResultSet();
            while (rs.next()) {
                glycemicStatsByClass.setAverage(rs.getBigDecimal("average A1C"));
            }

            DatabaseUtil.closeResultSet(rs);

            /* percent at A1C target */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }
            rs = cs.getResultSet();
            while (rs.next()) {
                glycemicStatsByClass.setPercentage(rs.getBigDecimal("percent at A1C target"));
            }

            DatabaseUtil.closeResultSet(rs);

            /* last A1C by class attendance */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }
            ArrayList<CategoricalValue> groupOne = new ArrayList<>();
            ArrayList<CategoricalValue> groupTwo = new ArrayList<>();
            ArrayList<CategoricalValue> groupThree = new ArrayList<>();
            ArrayList<CategoricalValue> groupFour = new ArrayList<>();
            ArrayList<CategoricalValue> groupFive = new ArrayList<>();
            rs = cs.getResultSet();
            while (rs.next()) {
                BigDecimal lastA1c = rs.getBigDecimal("last A1C");
                int classesAttended = rs.getInt("classes attended");
                CategoricalValue cv = new CategoricalValue(lastA1c,
                        classesAttended);
                switch (classesAttended) {
                    case 1:
                        groupOne.add(cv);
                        break;
                    case 2:
                        groupTwo.add(cv);
                        break;
                    case 3:
                        groupThree.add(cv);
                        break;
                    case 4:
                        groupFour.add(cv);
                        break;
                    default:
                        groupFive.add(cv);
                        break;
                }
            }
            ArrayList<ArrayList<CategoricalValue>> groups = new ArrayList<>();
            if (!groupOne.isEmpty()) {
                groups.add(groupOne);
            }
            if (!groupTwo.isEmpty()) {
                groups.add(groupTwo);
            }
            if (!groupThree.isEmpty()) {
                groups.add(groupThree);
            }
            if (!groupFour.isEmpty()) {
                groups.add(groupFour);
            }
            if (!groupFive.isEmpty()) {
                groups.add(groupFive);
            }
            glycemicStatsByClass.setGroups(groups);

            DatabaseUtil.closeResultSet(rs);

            /* last A1C by treatment class */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }
            ArrayList<CategoricalValue> groupSix = new ArrayList<>();
            ArrayList<CategoricalValue> groupSeven = new ArrayList<>();
            ArrayList<CategoricalValue> groupEight = new ArrayList<>();
            ArrayList<CategoricalValue> groupNine = new ArrayList<>();
            ArrayList<CategoricalValue> groupTen = new ArrayList<>();
            ArrayList<CategoricalValue> groupEleven = new ArrayList<>();
            ArrayList<CategoricalValue> groupTwelve = new ArrayList<>();
            rs = cs.getResultSet();
            while (rs.next()) {
                BigDecimal lastA1c = rs.getBigDecimal("last A1C");
                String treatmentClass = rs.getString("treatment class");
                CategoricalValue cv = new CategoricalValue(treatmentClass, lastA1c);
                switch (treatmentClass) {
                    case "0":
                        groupSix.add(cv);
                        break;
                    case "I":
                        groupSeven.add(cv);
                        break;
                    case "II":
                        groupEight.add(cv);
                        break;
                    case "III":
                        groupNine.add(cv);
                        break;
                    case "IV":
                        groupTen.add(cv);
                        break;
                    case "V":
                        groupEleven.add(cv);
                        break;
                    default:
                        groupTwelve.add(cv);
                        break;
                }
            }
            ArrayList<ArrayList<CategoricalValue>> moreGroups = new ArrayList<>();
            if (!groupSix.isEmpty()) {
                moreGroups.add(groupSix);
            }
            if (!groupSeven.isEmpty()) {
                moreGroups.add(groupSeven);
            }
            if (!groupEight.isEmpty()) {
                moreGroups.add(groupEight);
            }
            if (!groupNine.isEmpty()) {
                moreGroups.add(groupNine);
            }
            if (!groupTen.isEmpty()) {
                moreGroups.add(groupTen);
            }
            if (!groupEleven.isEmpty()) {
                moreGroups.add(groupEleven);
            }
            if (!groupTwelve.isEmpty()) {
                moreGroups.add(groupTwelve);
            }
            glycemicStatsByTreatment.setGroups(moreGroups);

            glycemicStats[0] = glycemicStatsByClass;
            glycemicStats[1] = glycemicStatsByTreatment;

        } catch (SQLException ex) {
            Logger.getLogger(ReferencesIO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DatabaseUtil.closeResultSet(rs);
            DatabaseUtil.closeCallableStatement(cs);
            pool.freeConnection(connection);
        }

        return glycemicStats;
    }

    /**
     * Returns BMI data from the database
     *
     * @param clinicId the clinic ID
     * @param referenceCharacters the character string
     * @return the demographic data
     */
    public static Stats[] getBodyMassStatistics(int clinicId,
            Object referenceCharacters) {
        Stats[] bmiStats = new Stats[2];
        Stats maleBmi = new Stats();
        Stats femaleBmi = new Stats();
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        CallableStatement cs = null;
        ResultSet rs = null;

        try {
            cs = connection.prepareCall("{CALL getBodyMassStatistics(?, ?, ?)}");
            cs.setInt(1, clinicId);
            cs.setString(2, (String) referenceCharacters);
            cs.registerOutParameter(3, java.sql.Types.TINYINT);

            /* BMI averages */
            boolean success = cs.execute();
            if (!success) {
                return null;
            }

            rs = cs.getResultSet();
            while (rs.next()) {
                BigDecimal maleAvg = rs.getBigDecimal("avg male");
                BigDecimal femaleAvg = rs.getBigDecimal("avg female");
                if (maleAvg != null) {
                    maleBmi.setAverage(maleAvg);
                }
                if (femaleAvg != null) {
                    femaleBmi.setAverage(femaleAvg);
                }
            }

            DatabaseUtil.closeResultSet(rs);

            /* last BMI for males by class attendance */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }
            ArrayList<CategoricalValue> groupOne = new ArrayList<>();
            ArrayList<CategoricalValue> groupTwo = new ArrayList<>();
            ArrayList<CategoricalValue> groupThree = new ArrayList<>();
            ArrayList<CategoricalValue> groupFour = new ArrayList<>();
            ArrayList<CategoricalValue> groupFive = new ArrayList<>();
            rs = cs.getResultSet();
            while (rs.next()) {
                BigDecimal lastBmi = rs.getBigDecimal("last BMI");
                int classesAttended = rs.getInt("classes attended");
                CategoricalValue cv = new CategoricalValue(lastBmi,
                        classesAttended);
                switch (classesAttended) {
                    case 1:
                        groupOne.add(cv);
                        break;
                    case 2:
                        groupTwo.add(cv);
                        break;
                    case 3:
                        groupThree.add(cv);
                        break;
                    case 4:
                        groupFour.add(cv);
                        break;
                    default:
                        groupFive.add(cv);
                        break;
                }
            }
            ArrayList<ArrayList<CategoricalValue>> maleGroups = new ArrayList<>();
            if (!groupOne.isEmpty()) {
                maleGroups.add(groupOne);
            }
            if (!groupTwo.isEmpty()) {
                maleGroups.add(groupTwo);
            }
            if (!groupThree.isEmpty()) {
                maleGroups.add(groupThree);
            }
            if (!groupFour.isEmpty()) {
                maleGroups.add(groupFour);
            }
            if (!groupFive.isEmpty()) {
                maleGroups.add(groupFive);
            }
            maleBmi.setGroups(maleGroups);

            DatabaseUtil.closeResultSet(rs);

            /* last BMI for females by class attendance */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }
            ArrayList<CategoricalValue> groupOneFemales = new ArrayList<>();
            ArrayList<CategoricalValue> groupTwoFemales = new ArrayList<>();
            ArrayList<CategoricalValue> groupThreeFemales = new ArrayList<>();
            ArrayList<CategoricalValue> groupFourFemales = new ArrayList<>();
            ArrayList<CategoricalValue> groupFiveFemales = new ArrayList<>();
            rs = cs.getResultSet();
            while (rs.next()) {
                BigDecimal lastBmi = rs.getBigDecimal("last BMI");
                int classesAttended = rs.getInt("classes attended");
                CategoricalValue cv = new CategoricalValue(lastBmi,
                        classesAttended);
                switch (classesAttended) {
                    case 1:
                        groupOneFemales.add(cv);
                        break;
                    case 2:
                        groupTwoFemales.add(cv);
                        break;
                    case 3:
                        groupThreeFemales.add(cv);
                        break;
                    case 4:
                        groupFourFemales.add(cv);
                        break;
                    default:
                        groupFiveFemales.add(cv);
                        break;
                }
            }
            ArrayList<ArrayList<CategoricalValue>> femaleGroups = new ArrayList<>();
            if (!groupOneFemales.isEmpty()) {
                femaleGroups.add(groupOneFemales);
            }
            if (!groupTwoFemales.isEmpty()) {
                femaleGroups.add(groupTwoFemales);
            }
            if (!groupThreeFemales.isEmpty()) {
                femaleGroups.add(groupThreeFemales);
            }
            if (!groupFourFemales.isEmpty()) {
                femaleGroups.add(groupFourFemales);
            }
            if (!groupFiveFemales.isEmpty()) {
                femaleGroups.add(groupFiveFemales);
            }
            femaleBmi.setGroups(femaleGroups);

            bmiStats[0] = maleBmi;
            bmiStats[1] = femaleBmi;

        } catch (SQLException ex) {
            Logger.getLogger(ReferencesIO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DatabaseUtil.closeResultSet(rs);
            DatabaseUtil.closeCallableStatement(cs);
            pool.freeConnection(connection);
        }

        return bmiStats;
    }

    /**
     * Returns treatment data from the database
     *
     * @param clinicId the clinic ID
     * @param referenceCharacters the character string
     * @return the demographic data
     */
    public static Stats getTreatmentStatistics(int clinicId,
            Object referenceCharacters) {
        Stats treatmentStats = new Stats();
        ArrayList<ArrayList<CategoricalValue>> groups = new ArrayList<>();
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        CallableStatement cs = null;
        ResultSet rs = null;

        try {
            cs = connection.prepareCall("{CALL getTreatmentStatistics(?, ?, ?)}");
            cs.setInt(1, clinicId);
            cs.setString(2, (String) referenceCharacters);
            cs.registerOutParameter(3, java.sql.Types.TINYINT);

            /* average change in A1C by treatment class */
            ArrayList<CategoricalValue> a1cChanges = new ArrayList<>();

            /* total average change*/
            boolean success = cs.execute();
            if (!success) {
                return null;
            }

            rs = cs.getResultSet();
            while (rs.next()) {
                BigDecimal value = rs.getBigDecimal("total avg change");
                CategoricalValue cv = new CategoricalValue("total average change",
                        value);
                a1cChanges.add(cv);
            }

            DatabaseUtil.closeResultSet(rs);

            /* average change in treatment class zero */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }
            rs = cs.getResultSet();
            while (rs.next()) {
                BigDecimal value = rs.getBigDecimal("class zero avg change");
                CategoricalValue cv = new CategoricalValue("0",
                        value);
                a1cChanges.add(cv);
            }

            DatabaseUtil.closeResultSet(rs);

            /* average change in treatment class one */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }
            rs = cs.getResultSet();
            while (rs.next()) {
                BigDecimal value = rs.getBigDecimal("class one avg change");
                CategoricalValue cv = new CategoricalValue("I",
                        value);
                a1cChanges.add(cv);
            }

            DatabaseUtil.closeResultSet(rs);

            /* average change in treatment class two */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }
            rs = cs.getResultSet();
            while (rs.next()) {
                BigDecimal value = rs.getBigDecimal("class two avg change");
                CategoricalValue cv = new CategoricalValue("II",
                        value);
                a1cChanges.add(cv);
            }

            DatabaseUtil.closeResultSet(rs);

            /* average change in treatment class three */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }
            rs = cs.getResultSet();
            while (rs.next()) {
                BigDecimal value = rs.getBigDecimal("class three avg change");
                CategoricalValue cv = new CategoricalValue("III",
                        value);
                a1cChanges.add(cv);
            }

            DatabaseUtil.closeResultSet(rs);

            /* average change in treatment class four */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }
            rs = cs.getResultSet();
            while (rs.next()) {
                BigDecimal value = rs.getBigDecimal("class four avg change");
                CategoricalValue cv = new CategoricalValue("IV",
                        value);
                a1cChanges.add(cv);
            }

            DatabaseUtil.closeResultSet(rs);

            /* average change in treatment class five */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }
            rs = cs.getResultSet();
            while (rs.next()) {
                BigDecimal value = rs.getBigDecimal("class five avg change");
                CategoricalValue cv = new CategoricalValue("V",
                        value);
                a1cChanges.add(cv);
            }

            DatabaseUtil.closeResultSet(rs);

            /* average change in treatment class unknown */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }
            rs = cs.getResultSet();
            while (rs.next()) {
                BigDecimal value = rs.getBigDecimal("class unknown avg change");
                CategoricalValue cv = new CategoricalValue("U",
                        value);
                a1cChanges.add(cv);
            }
            groups.add(a1cChanges);

            DatabaseUtil.closeResultSet(rs);

            /* treatment class counts */
            ArrayList<CategoricalValue> classCounts = new ArrayList<>();

            /* count for treatment class zero */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }
            rs = cs.getResultSet();
            while (rs.next()) {
                BigDecimal value = rs.getBigDecimal("class zero count");
                CategoricalValue cv = new CategoricalValue("0",
                        value);
                classCounts.add(cv);
            }

            DatabaseUtil.closeResultSet(rs);

            /* count for treatment class one */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }
            rs = cs.getResultSet();
            while (rs.next()) {
                BigDecimal value = rs.getBigDecimal("class one count");
                CategoricalValue cv = new CategoricalValue("I",
                        value);
                classCounts.add(cv);
            }

            DatabaseUtil.closeResultSet(rs);

            /* count for treatment class two */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }
            rs = cs.getResultSet();
            while (rs.next()) {
                BigDecimal value = rs.getBigDecimal("class two count");
                CategoricalValue cv = new CategoricalValue("II",
                        value);
                classCounts.add(cv);
            }

            DatabaseUtil.closeResultSet(rs);

            /* count for treatment class three */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }
            rs = cs.getResultSet();
            while (rs.next()) {
                BigDecimal value = rs.getBigDecimal("class three count");
                CategoricalValue cv = new CategoricalValue("III",
                        value);
                classCounts.add(cv);
            }

            DatabaseUtil.closeResultSet(rs);

            /* count for treatment class four */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }
            rs = cs.getResultSet();
            while (rs.next()) {
                BigDecimal value = rs.getBigDecimal("class four count");
                CategoricalValue cv = new CategoricalValue("IV",
                        value);
                classCounts.add(cv);
            }

            DatabaseUtil.closeResultSet(rs);

            /* count for treatment class five */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }
            rs = cs.getResultSet();
            while (rs.next()) {
                BigDecimal value = rs.getBigDecimal("class five count");
                CategoricalValue cv = new CategoricalValue("V",
                        value);
                classCounts.add(cv);
            }

            DatabaseUtil.closeResultSet(rs);

            /* count for treatment class unknown */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }
            rs = cs.getResultSet();
            while (rs.next()) {
                BigDecimal value = rs.getBigDecimal("class unknown count");
                CategoricalValue cv = new CategoricalValue("U",
                        value);
                classCounts.add(cv);
            }
            groups.add(classCounts);

            DatabaseUtil.closeResultSet(rs);

            /* treatment class counts for males */
            ArrayList<CategoricalValue> maleClassCounts = new ArrayList<>();

            /* male count for treatment class zero */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }
            rs = cs.getResultSet();
            while (rs.next()) {
                BigDecimal value = rs.getBigDecimal("males class zero count");
                CategoricalValue cv = new CategoricalValue("0",
                        value);
                maleClassCounts.add(cv);
            }

            DatabaseUtil.closeResultSet(rs);

            /* male count for treatment class one */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }
            rs = cs.getResultSet();
            while (rs.next()) {
                BigDecimal value = rs.getBigDecimal("males class one count");
                CategoricalValue cv = new CategoricalValue("I",
                        value);
                maleClassCounts.add(cv);
            }

            DatabaseUtil.closeResultSet(rs);

            /* male count for treatment class two */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }
            rs = cs.getResultSet();
            while (rs.next()) {
                BigDecimal value = rs.getBigDecimal("males class two count");
                CategoricalValue cv = new CategoricalValue("II",
                        value);
                maleClassCounts.add(cv);
            }

            DatabaseUtil.closeResultSet(rs);

            /* male count for treatment class three */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }
            rs = cs.getResultSet();
            while (rs.next()) {
                BigDecimal value = rs.getBigDecimal("males class three count");
                CategoricalValue cv = new CategoricalValue("III",
                        value);
                maleClassCounts.add(cv);
            }

            DatabaseUtil.closeResultSet(rs);

            /* male count for treatment class four */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }
            rs = cs.getResultSet();
            while (rs.next()) {
                BigDecimal value = rs.getBigDecimal("males class four count");
                CategoricalValue cv = new CategoricalValue("IV",
                        value);
                maleClassCounts.add(cv);
            }

            DatabaseUtil.closeResultSet(rs);

            /* male count for treatment class five */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }
            rs = cs.getResultSet();
            while (rs.next()) {
                BigDecimal value = rs.getBigDecimal("males class five count");
                CategoricalValue cv = new CategoricalValue("V",
                        value);
                maleClassCounts.add(cv);
            }

            DatabaseUtil.closeResultSet(rs);

            /* male count for treatment class unknown */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }
            rs = cs.getResultSet();
            while (rs.next()) {
                BigDecimal value = rs.getBigDecimal("males class unknown count");
                CategoricalValue cv = new CategoricalValue("U",
                        value);
                maleClassCounts.add(cv);
            }
            groups.add(maleClassCounts);

            DatabaseUtil.closeResultSet(rs);

            /* treatment class counts for females */
            ArrayList<CategoricalValue> femaleClassCounts = new ArrayList<>();

            /* female count for treatment class zero */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }
            rs = cs.getResultSet();
            while (rs.next()) {
                BigDecimal value = rs.getBigDecimal("females class zero count");
                CategoricalValue cv = new CategoricalValue("0",
                        value);
                femaleClassCounts.add(cv);
            }

            DatabaseUtil.closeResultSet(rs);

            /* female count for treatment class one */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }
            rs = cs.getResultSet();
            while (rs.next()) {
                BigDecimal value = rs.getBigDecimal("females class one count");
                CategoricalValue cv = new CategoricalValue("I",
                        value);
                femaleClassCounts.add(cv);
            }

            DatabaseUtil.closeResultSet(rs);

            /* female count for treatment class two */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }
            rs = cs.getResultSet();
            while (rs.next()) {
                BigDecimal value = rs.getBigDecimal("females class two count");
                CategoricalValue cv = new CategoricalValue("II",
                        value);
                femaleClassCounts.add(cv);
            }

            DatabaseUtil.closeResultSet(rs);

            /* female count for treatment class three */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }
            rs = cs.getResultSet();
            while (rs.next()) {
                BigDecimal value = rs.getBigDecimal("females class three count");
                CategoricalValue cv = new CategoricalValue("III",
                        value);
                femaleClassCounts.add(cv);
            }

            DatabaseUtil.closeResultSet(rs);

            /* female count for treatment class four */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }
            rs = cs.getResultSet();
            while (rs.next()) {
                BigDecimal value = rs.getBigDecimal("females class four count");
                CategoricalValue cv = new CategoricalValue("IV",
                        value);
                femaleClassCounts.add(cv);
            }

            DatabaseUtil.closeResultSet(rs);

            /* female count for treatment class five */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }
            rs = cs.getResultSet();
            while (rs.next()) {
                BigDecimal value = rs.getBigDecimal("females class five count");
                CategoricalValue cv = new CategoricalValue("V",
                        value);
                femaleClassCounts.add(cv);
            }

            DatabaseUtil.closeResultSet(rs);

            /* female count for treatment class unknown */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }
            rs = cs.getResultSet();
            while (rs.next()) {
                BigDecimal value = rs.getBigDecimal("females class unknown count");
                CategoricalValue cv = new CategoricalValue("U",
                        value);
                femaleClassCounts.add(cv);
            }
            groups.add(femaleClassCounts);

            DatabaseUtil.closeResultSet(rs);

            /* treatment class counts for whites */
            ArrayList<CategoricalValue> whiteClassCounts = new ArrayList<>();

            /* white count for treatment class zero */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }
            rs = cs.getResultSet();
            while (rs.next()) {
                BigDecimal value = rs.getBigDecimal("whites class zero count");
                CategoricalValue cv = new CategoricalValue("0",
                        value);
                whiteClassCounts.add(cv);
            }

            DatabaseUtil.closeResultSet(rs);

            /* white count for treatment class one */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }
            rs = cs.getResultSet();
            while (rs.next()) {
                BigDecimal value = rs.getBigDecimal("whites class one count");
                CategoricalValue cv = new CategoricalValue("I",
                        value);
                whiteClassCounts.add(cv);
            }

            DatabaseUtil.closeResultSet(rs);

            /* white count for treatment class two */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }
            rs = cs.getResultSet();
            while (rs.next()) {
                BigDecimal value = rs.getBigDecimal("whites class two count");
                CategoricalValue cv = new CategoricalValue("II",
                        value);
                whiteClassCounts.add(cv);
            }

            DatabaseUtil.closeResultSet(rs);

            /* white count for treatment class three */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }
            rs = cs.getResultSet();
            while (rs.next()) {
                BigDecimal value = rs.getBigDecimal("whites class three count");
                CategoricalValue cv = new CategoricalValue("III",
                        value);
                whiteClassCounts.add(cv);
            }

            DatabaseUtil.closeResultSet(rs);

            /* white count for treatment class four */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }
            rs = cs.getResultSet();
            while (rs.next()) {
                BigDecimal value = rs.getBigDecimal("whites class four count");
                CategoricalValue cv = new CategoricalValue("IV",
                        value);
                whiteClassCounts.add(cv);
            }

            DatabaseUtil.closeResultSet(rs);

            /* white count for treatment class five */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }
            rs = cs.getResultSet();
            while (rs.next()) {
                BigDecimal value = rs.getBigDecimal("whites class five count");
                CategoricalValue cv = new CategoricalValue("V",
                        value);
                whiteClassCounts.add(cv);
            }

            DatabaseUtil.closeResultSet(rs);

            /* white count for treatment class unknown */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }
            rs = cs.getResultSet();
            while (rs.next()) {
                BigDecimal value = rs.getBigDecimal("whites class unknown count");
                CategoricalValue cv = new CategoricalValue("U",
                        value);
                whiteClassCounts.add(cv);
            }
            groups.add(whiteClassCounts);

            DatabaseUtil.closeResultSet(rs);

            /* treatment class counts for African Americans */
            ArrayList<CategoricalValue> africanAmericanClassCounts = new ArrayList<>();

            /* African American count for treatment class zero */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }
            rs = cs.getResultSet();
            while (rs.next()) {
                BigDecimal value = rs.getBigDecimal("african americans class zero count");
                CategoricalValue cv = new CategoricalValue("0",
                        value);
                africanAmericanClassCounts.add(cv);
            }

            DatabaseUtil.closeResultSet(rs);

            /* African American count for treatment class one */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }
            rs = cs.getResultSet();
            while (rs.next()) {
                BigDecimal value = rs.getBigDecimal("african americans class one count");
                CategoricalValue cv = new CategoricalValue("I",
                        value);
                africanAmericanClassCounts.add(cv);
            }

            DatabaseUtil.closeResultSet(rs);

            /* African American count for treatment class two */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }
            rs = cs.getResultSet();
            while (rs.next()) {
                BigDecimal value = rs.getBigDecimal("african americans class two count");
                CategoricalValue cv = new CategoricalValue("II",
                        value);
                africanAmericanClassCounts.add(cv);
            }

            DatabaseUtil.closeResultSet(rs);

            /* African American count for treatment class three */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }
            rs = cs.getResultSet();
            while (rs.next()) {
                BigDecimal value = rs.getBigDecimal("african americans class three count");
                CategoricalValue cv = new CategoricalValue("III",
                        value);
                africanAmericanClassCounts.add(cv);
            }

            DatabaseUtil.closeResultSet(rs);

            /* African American count for treatment class four */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }
            rs = cs.getResultSet();
            while (rs.next()) {
                BigDecimal value = rs.getBigDecimal("african americans class four count");
                CategoricalValue cv = new CategoricalValue("IV",
                        value);
                africanAmericanClassCounts.add(cv);
            }

            DatabaseUtil.closeResultSet(rs);

            /* African American count for treatment class five */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }
            rs = cs.getResultSet();
            while (rs.next()) {
                BigDecimal value = rs.getBigDecimal("african americans class five count");
                CategoricalValue cv = new CategoricalValue("V",
                        value);
                africanAmericanClassCounts.add(cv);
            }

            DatabaseUtil.closeResultSet(rs);

            /* African American count for treatment class unknown */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }
            rs = cs.getResultSet();
            while (rs.next()) {
                BigDecimal value = rs.getBigDecimal("african americans class unknown count");
                CategoricalValue cv = new CategoricalValue("U",
                        value);
                africanAmericanClassCounts.add(cv);
            }
            groups.add(africanAmericanClassCounts);

            DatabaseUtil.closeResultSet(rs);

            /* treatment class counts for Asians/Pacific Islanders */
            ArrayList<CategoricalValue> asianPacificIslanderClassCounts = new ArrayList<>();

            /* Asians/Pacific Islanders count for treatment class zero */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }
            rs = cs.getResultSet();
            while (rs.next()) {
                BigDecimal value = rs.getBigDecimal("asian/pacific islander class zero count");
                CategoricalValue cv = new CategoricalValue("0",
                        value);
                asianPacificIslanderClassCounts.add(cv);
            }

            DatabaseUtil.closeResultSet(rs);

            /* Asians/Pacific Islanders count for treatment class one */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }
            rs = cs.getResultSet();
            while (rs.next()) {
                BigDecimal value = rs.getBigDecimal("asian/pacific islander class one count");
                CategoricalValue cv = new CategoricalValue("I",
                        value);
                asianPacificIslanderClassCounts.add(cv);
            }

            DatabaseUtil.closeResultSet(rs);

            /* Asians/Pacific Islanders count for treatment class two */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }
            rs = cs.getResultSet();
            while (rs.next()) {
                BigDecimal value = rs.getBigDecimal("asian/pacific islander class two count");
                CategoricalValue cv = new CategoricalValue("II",
                        value);
                asianPacificIslanderClassCounts.add(cv);
            }

            DatabaseUtil.closeResultSet(rs);

            /* Asians/Pacific Islanders count for treatment class three */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }
            rs = cs.getResultSet();
            while (rs.next()) {
                BigDecimal value = rs.getBigDecimal("asian/pacific islander class three count");
                CategoricalValue cv = new CategoricalValue("III",
                        value);
                asianPacificIslanderClassCounts.add(cv);
            }

            DatabaseUtil.closeResultSet(rs);

            /* Asians/Pacific Islanders count for treatment class four */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }
            rs = cs.getResultSet();
            while (rs.next()) {
                BigDecimal value = rs.getBigDecimal("asian/pacific islander class four count");
                CategoricalValue cv = new CategoricalValue("IV",
                        value);
                asianPacificIslanderClassCounts.add(cv);
            }

            DatabaseUtil.closeResultSet(rs);

            /* Asians/Pacific Islanders count for treatment class five */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }
            rs = cs.getResultSet();
            while (rs.next()) {
                BigDecimal value = rs.getBigDecimal("asian/pacific islander class five count");
                CategoricalValue cv = new CategoricalValue("V",
                        value);
                asianPacificIslanderClassCounts.add(cv);
            }

            DatabaseUtil.closeResultSet(rs);

            /* Asians/Pacific Islanders count for treatment class unknown */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }
            rs = cs.getResultSet();
            while (rs.next()) {
                BigDecimal value = rs.getBigDecimal("asian/pacific islander class unknown count");
                CategoricalValue cv = new CategoricalValue("U",
                        value);
                asianPacificIslanderClassCounts.add(cv);
            }
            groups.add(asianPacificIslanderClassCounts);

            DatabaseUtil.closeResultSet(rs);

            /* treatment class counts for American Indians/Alaska Natives */
            ArrayList<CategoricalValue> americanIndianAlaskaNativeClassCounts = new ArrayList<>();

            /* American Indians/Alaska Natives count for treatment class zero */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }
            rs = cs.getResultSet();
            while (rs.next()) {
                BigDecimal value = rs.getBigDecimal("american indian/alaska native class zero count");
                CategoricalValue cv = new CategoricalValue("0",
                        value);
                americanIndianAlaskaNativeClassCounts.add(cv);
            }

            DatabaseUtil.closeResultSet(rs);

            /* American Indians/Alaska Natives count for treatment class one */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }
            rs = cs.getResultSet();
            while (rs.next()) {
                BigDecimal value = rs.getBigDecimal("american indian/alaska native class one count");
                CategoricalValue cv = new CategoricalValue("I",
                        value);
                americanIndianAlaskaNativeClassCounts.add(cv);
            }

            DatabaseUtil.closeResultSet(rs);

            /* American Indians/Alaska Natives count for treatment class two */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }
            rs = cs.getResultSet();
            while (rs.next()) {
                BigDecimal value = rs.getBigDecimal("american indian/alaska native class two count");
                CategoricalValue cv = new CategoricalValue("II",
                        value);
                americanIndianAlaskaNativeClassCounts.add(cv);
            }

            DatabaseUtil.closeResultSet(rs);

            /* American Indians/Alaska Natives count for treatment class three */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }
            rs = cs.getResultSet();
            while (rs.next()) {
                BigDecimal value = rs.getBigDecimal("american indian/alaska native class three count");
                CategoricalValue cv = new CategoricalValue("III",
                        value);
                americanIndianAlaskaNativeClassCounts.add(cv);
            }

            DatabaseUtil.closeResultSet(rs);

            /* American Indians/Alaska Natives count for treatment class four */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }
            rs = cs.getResultSet();
            while (rs.next()) {
                BigDecimal value = rs.getBigDecimal("american indian/alaska native class four count");
                CategoricalValue cv = new CategoricalValue("IV",
                        value);
                americanIndianAlaskaNativeClassCounts.add(cv);
            }

            DatabaseUtil.closeResultSet(rs);

            /* American Indians/Alaska Natives count for treatment class five */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }
            rs = cs.getResultSet();
            while (rs.next()) {
                BigDecimal value = rs.getBigDecimal("american indian/alaska native class five count");
                CategoricalValue cv = new CategoricalValue("V",
                        value);
                americanIndianAlaskaNativeClassCounts.add(cv);
            }

            DatabaseUtil.closeResultSet(rs);

            /* American Indians/Alaska Natives count for treatment class unknown */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }
            rs = cs.getResultSet();
            while (rs.next()) {
                BigDecimal value = rs.getBigDecimal("american indian/alaska native class unknown count");
                CategoricalValue cv = new CategoricalValue("U",
                        value);
                americanIndianAlaskaNativeClassCounts.add(cv);
            }
            groups.add(americanIndianAlaskaNativeClassCounts);

            DatabaseUtil.closeResultSet(rs);

            /* treatment class counts for Hispanics */
            ArrayList<CategoricalValue> hispanicClassCounts = new ArrayList<>();

            /* Hispanics count for treatment class zero */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }
            rs = cs.getResultSet();
            while (rs.next()) {
                BigDecimal value = rs.getBigDecimal("hispanics class zero count");
                CategoricalValue cv = new CategoricalValue("0",
                        value);
                hispanicClassCounts.add(cv);
            }

            DatabaseUtil.closeResultSet(rs);

            /* Hispanics count for treatment class one */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }
            rs = cs.getResultSet();
            while (rs.next()) {
                BigDecimal value = rs.getBigDecimal("hispanics class one count");
                CategoricalValue cv = new CategoricalValue("I",
                        value);
                hispanicClassCounts.add(cv);
            }

            DatabaseUtil.closeResultSet(rs);

            /* Hispanics count for treatment class two */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }
            rs = cs.getResultSet();
            while (rs.next()) {
                BigDecimal value = rs.getBigDecimal("hispanics class two count");
                CategoricalValue cv = new CategoricalValue("II",
                        value);
                hispanicClassCounts.add(cv);
            }

            DatabaseUtil.closeResultSet(rs);

            /* Hispanics count for treatment class three */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }
            rs = cs.getResultSet();
            while (rs.next()) {
                BigDecimal value = rs.getBigDecimal("hispanics class three count");
                CategoricalValue cv = new CategoricalValue("III",
                        value);
                hispanicClassCounts.add(cv);
            }

            DatabaseUtil.closeResultSet(rs);

            /* Hispanics count for treatment class four */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }
            rs = cs.getResultSet();
            while (rs.next()) {
                BigDecimal value = rs.getBigDecimal("hispanics class four count");
                CategoricalValue cv = new CategoricalValue("IV",
                        value);
                hispanicClassCounts.add(cv);
            }

            DatabaseUtil.closeResultSet(rs);

            /* Hispanics count for treatment class five */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }
            rs = cs.getResultSet();
            while (rs.next()) {
                BigDecimal value = rs.getBigDecimal("hispanics class five count");
                CategoricalValue cv = new CategoricalValue("V",
                        value);
                hispanicClassCounts.add(cv);
            }

            DatabaseUtil.closeResultSet(rs);

            /* Hispanics count for treatment class unknown */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }
            rs = cs.getResultSet();
            while (rs.next()) {
                BigDecimal value = rs.getBigDecimal("hispanics class unknown count");
                CategoricalValue cv = new CategoricalValue("U",
                        value);
                hispanicClassCounts.add(cv);
            }
            groups.add(hispanicClassCounts);

            DatabaseUtil.closeResultSet(rs);

            /* treatment class counts for Middle Easterners */
            ArrayList<CategoricalValue> middleEasternClassCounts = new ArrayList<>();

            /* Middle Easterners count for treatment class zero */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }
            rs = cs.getResultSet();
            while (rs.next()) {
                BigDecimal value = rs.getBigDecimal("middle eastern class zero count");
                CategoricalValue cv = new CategoricalValue("0",
                        value);
                middleEasternClassCounts.add(cv);
            }

            DatabaseUtil.closeResultSet(rs);

            /* Middle Easterners count for treatment class one */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }
            rs = cs.getResultSet();
            while (rs.next()) {
                BigDecimal value = rs.getBigDecimal("middle eastern class one count");
                CategoricalValue cv = new CategoricalValue("I",
                        value);
                middleEasternClassCounts.add(cv);
            }

            DatabaseUtil.closeResultSet(rs);

            /* Middle Easterners count for treatment class two */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }
            rs = cs.getResultSet();
            while (rs.next()) {
                BigDecimal value = rs.getBigDecimal("middle eastern class two count");
                CategoricalValue cv = new CategoricalValue("II",
                        value);
                middleEasternClassCounts.add(cv);
            }

            DatabaseUtil.closeResultSet(rs);

            /* Middle Easterners count for treatment class three */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }
            rs = cs.getResultSet();
            while (rs.next()) {
                BigDecimal value = rs.getBigDecimal("middle eastern class three count");
                CategoricalValue cv = new CategoricalValue("III",
                        value);
                middleEasternClassCounts.add(cv);
            }

            DatabaseUtil.closeResultSet(rs);

            /* Middle Easterners count for treatment class four */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }
            rs = cs.getResultSet();
            while (rs.next()) {
                BigDecimal value = rs.getBigDecimal("middle eastern class four count");
                CategoricalValue cv = new CategoricalValue("IV",
                        value);
                middleEasternClassCounts.add(cv);
            }

            DatabaseUtil.closeResultSet(rs);

            /* Middle Easterners count for treatment class five */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }
            rs = cs.getResultSet();
            while (rs.next()) {
                BigDecimal value = rs.getBigDecimal("middle eastern class five count");
                CategoricalValue cv = new CategoricalValue("V",
                        value);
                middleEasternClassCounts.add(cv);
            }

            DatabaseUtil.closeResultSet(rs);

            /* Middle Easterners count for treatment class unknown */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }
            rs = cs.getResultSet();
            while (rs.next()) {
                BigDecimal value = rs.getBigDecimal("middle eastern class unknown count");
                CategoricalValue cv = new CategoricalValue("U",
                        value);
                middleEasternClassCounts.add(cv);
            }
            groups.add(middleEasternClassCounts);

            DatabaseUtil.closeResultSet(rs);

            /* treatment class counts for other ethnicities */
            ArrayList<CategoricalValue> otherClassCounts = new ArrayList<>();

            /* other ethnicities count for treatment class zero */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }
            rs = cs.getResultSet();
            while (rs.next()) {
                BigDecimal value = rs.getBigDecimal("other class zero count");
                CategoricalValue cv = new CategoricalValue("0",
                        value);
                otherClassCounts.add(cv);
            }

            DatabaseUtil.closeResultSet(rs);

            /* other ethnicities count for treatment class one */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }
            rs = cs.getResultSet();
            while (rs.next()) {
                BigDecimal value = rs.getBigDecimal("other class one count");
                CategoricalValue cv = new CategoricalValue("I",
                        value);
                otherClassCounts.add(cv);
            }

            DatabaseUtil.closeResultSet(rs);

            /* other ethnicities count for treatment class two */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }
            rs = cs.getResultSet();
            while (rs.next()) {
                BigDecimal value = rs.getBigDecimal("other class two count");
                CategoricalValue cv = new CategoricalValue("II",
                        value);
                otherClassCounts.add(cv);
            }

            DatabaseUtil.closeResultSet(rs);

            /* other ethnicities count for treatment class three */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }
            rs = cs.getResultSet();
            while (rs.next()) {
                BigDecimal value = rs.getBigDecimal("other class three count");
                CategoricalValue cv = new CategoricalValue("III",
                        value);
                otherClassCounts.add(cv);
            }

            DatabaseUtil.closeResultSet(rs);

            /* other ethnicities count for treatment class four */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }
            rs = cs.getResultSet();
            while (rs.next()) {
                BigDecimal value = rs.getBigDecimal("other class four count");
                CategoricalValue cv = new CategoricalValue("IV",
                        value);
                otherClassCounts.add(cv);
            }

            DatabaseUtil.closeResultSet(rs);

            /* other ethnicities count for treatment class five */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }
            rs = cs.getResultSet();
            while (rs.next()) {
                BigDecimal value = rs.getBigDecimal("other class five count");
                CategoricalValue cv = new CategoricalValue("V",
                        value);
                otherClassCounts.add(cv);
            }

            DatabaseUtil.closeResultSet(rs);

            /* other ethnicities count for treatment class unknown */
            success = cs.getMoreResults();
            if (!success) {
                return null;
            }
            rs = cs.getResultSet();
            while (rs.next()) {
                BigDecimal value = rs.getBigDecimal("other class unknown count");
                CategoricalValue cv = new CategoricalValue("U",
                        value);
                otherClassCounts.add(cv);
            }
            groups.add(otherClassCounts);

            treatmentStats.setGroups(groups);

        } catch (SQLException ex) {
            Logger.getLogger(ReferencesIO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DatabaseUtil.closeResultSet(rs);
            DatabaseUtil.closeCallableStatement(cs);
            pool.freeConnection(connection);
        }

        return treatmentStats;
    }
}
