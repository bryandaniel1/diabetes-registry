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
package controller;

import data.PopulationStatisticsDataAccess;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import registry.DemographicData;
import registry.ReferenceContainer;
import registry.Stats;
import utility.SessionObjectUtility;

/**
 * This HttpServlet class coordinates the statistics data retrieval process for
 * the statistics page.
 *
 * @author Bryan Daniel
 * @version 2, March 16, 2017
 */
public class PopulationStatisticsServlet extends HttpServlet {

    /**
     * Serial version UID
     */
    private static final long serialVersionUID = -3975951474624563462L;

    /**
     * Handles the HTTP <code>GET</code> method. This method invokes the doPost
     * method for all requests.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method. This method coordinates the
     * navigation of the statistics page and processes the requests for
     * retrieving selected statistical information on the clinic population.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String url = "/statistics/index.jsp";
        int clinicId = ReferenceContainer.CLINIC_ID;
        String action = request.getParameter("action");

        if (action == null) {
            action = "";
        }

        switch (action) {
            case "getStatistic": {

                SessionObjectUtility.resetClinicObjects(session);
                String selection = request.getParameter("statselect");

                switch (selection) {
                    case "demographics": {
                        DemographicData demographicData
                                = PopulationStatisticsDataAccess
                                .getDemographicData(clinicId,
                                        session.getServletContext()
                                        .getAttribute("referenceCharacters"));
                        request.setAttribute("demographics", demographicData);
                        session.setAttribute(SessionObjectUtility.GENDER_DEMOGRAPHICS_GRAPH_DATA, demographicData);
                        session.setAttribute(SessionObjectUtility.RACE_DEMOGRAPHICS_GRAPH_DATA, demographicData);
                        session.setAttribute(SessionObjectUtility.AGE_DEMOGRAPHICS_GRAPH_DATA, demographicData);
                        break;
                    }
                    case "glycemicControl": {
                        int classesIndex = 0;
                        int treatmentIndex = 1;
                        Stats[] glycemicStats
                                = PopulationStatisticsDataAccess.getGlycemicControl(clinicId);
                        if (glycemicStats != null) {
                            request.setAttribute("glycemicStats", glycemicStats[classesIndex]);
                            session.setAttribute(SessionObjectUtility.LAST_A1C_DATA, glycemicStats[classesIndex]);
                            session.setAttribute(SessionObjectUtility.LAST_A1C_BY_CLASS_DATA, glycemicStats[classesIndex]);
                            session.setAttribute(SessionObjectUtility.LAST_A1C_BY_TREATMENT, glycemicStats[treatmentIndex]);
                        }
                        break;
                    }
                    case "bodyMass": {
                        int maleIndex = 0;
                        int femaleIndex = 1;
                        Stats[] bmiStats
                                = PopulationStatisticsDataAccess
                                .getBodyMassStatistics(clinicId,
                                        session.getServletContext()
                                        .getAttribute("referenceCharacters"));
                        request.setAttribute("bmiStats", bmiStats);
                        if (bmiStats != null) {
                            session.setAttribute(SessionObjectUtility.LAST_BMI_MALES_DATA, bmiStats[maleIndex]);
                            session.setAttribute(SessionObjectUtility.LAST_BMI_FEMALES_DATA, bmiStats[femaleIndex]);
                            session.setAttribute(SessionObjectUtility.LAST_BMI_MALES_BY_CLASS_DATA, bmiStats[maleIndex]);
                            session.setAttribute(SessionObjectUtility.LAST_BMI_FEMALES_BY_CLASS_DATA, bmiStats[femaleIndex]);
                        }
                        break;
                    }
                    case "treatment": {
                        int avgA1cChangeIndex = 0;

                        Stats treatmentStats
                                = PopulationStatisticsDataAccess
                                .getTreatmentStatistics(clinicId,
                                        session.getServletContext()
                                        .getAttribute("referenceCharacters"));
                        if (treatmentStats != null) {
                            request.setAttribute("treatmentStats",
                                    treatmentStats.getGroups().get(avgA1cChangeIndex));
                        }
                        session.setAttribute(SessionObjectUtility.CLASS_COUNTS_TREATMENT_STATS, treatmentStats);
                        session.setAttribute(SessionObjectUtility.GENDER_CLASS_COUNTS_TREATMENT_STATS, treatmentStats);
                        session.setAttribute(SessionObjectUtility.RACE_CLASS_COUNTS_TREATMENT_STATS, treatmentStats);
                        break;
                    }
                    default:
                        break;
                }
                break;
            }
            default:
                break;

        }
        getServletContext().getRequestDispatcher(url)
                .forward(request, response);
    }

}
