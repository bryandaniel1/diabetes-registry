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

import clinic.DemographicData;
import clinic.Patient;
import clinic.Stats;
import data.PatientIO;
import data.PopulationStatisticsIO;
import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import util.SessionObjectUtil;

/**
 * Coordinates the statistics data retrieval process
 *
 * @author Bryan Daniel
 * @version 1, April 8, 2016
 */
public class PopulationStatisticsServlet extends HttpServlet {

    /**
     * Handles the HTTP <code>GET</code> method.
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
     * Handles the HTTP <code>POST</code> method.
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
        final int EMPTY_VALUE = 0;
        boolean validData = true;
        String message;
        int clinicId;
        if (session.getAttribute("clinicId") == null) {
            clinicId = EMPTY_VALUE;
        } else {
            clinicId = (int) session.getAttribute("clinicId");
        }
        String action = request.getParameter("action");
        if (action == null) {
            action = "";
        }

        switch (action) {
            case "getStatistic": {
                String clinicIdString = request.getParameter("clinicselect");
                if ((clinicIdString != null) && (clinicIdString.trim().length()
                        != EMPTY_VALUE)) {
                    try {
                        clinicId = Integer.parseInt(clinicIdString);
                        ArrayList<Patient> patients
                                = PatientIO.getPatients(clinicId,
                                        session.getServletContext()
                                        .getAttribute("referenceCharacters"));
                        session.setAttribute("clinicId", clinicId);
                        session.setAttribute("patients", patients);
                        SessionObjectUtil.resetClinicObjects(session);
                    } catch (NumberFormatException nfe) {
                        message = "The clinic ID value is not valid.";
                        request.setAttribute("errorMessage", message);
                        validData = false;
                    }
                } else {
                    message = "A clinic must be selected.";
                    request.setAttribute("errorMessage", message);
                    validData = false;
                }
                if (validData) {
                    String selection = request.getParameter("statselect");
                    switch (selection) {
                        case "demographics": {
                            DemographicData demographicData
                                    = PopulationStatisticsIO
                                    .getDemographicData(clinicId,
                                            session.getServletContext()
                                            .getAttribute("referenceCharacters"));
                            request.setAttribute("demographics", demographicData);
                            session.setAttribute("genderDemographicsGraphData", demographicData);
                            session.setAttribute("raceDemographicsGraphData", demographicData);
                            session.setAttribute("ageDemographicsGraphData", demographicData);
                            break;
                        }
                        case "glycemicControl": {
                            int classesIndex = 0;
                            int treatmentIndex = 1;
                            Stats[] glycemicStats
                                    = PopulationStatisticsIO.getGlycemicControl(clinicId);
                            request.setAttribute("glycemicStats", glycemicStats[classesIndex]);
                            session.setAttribute("lastA1cData", glycemicStats[classesIndex]);
                            session.setAttribute("lastA1cByClassData", glycemicStats[classesIndex]);
                            session.setAttribute("lastA1cByTreatment", glycemicStats[treatmentIndex]);
                            break;
                        }
                        case "bodyMass": {
                            int maleIndex = 0;
                            int femaleIndex = 1;
                            Stats[] bmiStats
                                    = PopulationStatisticsIO
                                    .getBodyMassStatistics(clinicId,
                                            session.getServletContext()
                                            .getAttribute("referenceCharacters"));
                            request.setAttribute("bmiStats", bmiStats);
                            session.setAttribute("lastBmiMalesData", bmiStats[maleIndex]);
                            session.setAttribute("lastBmiFemalesData", bmiStats[femaleIndex]);
                            session.setAttribute("lastBmiMalesByClassData", bmiStats[maleIndex]);
                            session.setAttribute("lastBmiFemalesByClassData", bmiStats[femaleIndex]);
                            break;
                        }
                        case "treatment": {
                            int avgA1cChangeIndex = 0;

                            Stats treatmentStats
                                    = PopulationStatisticsIO
                                    .getTreatmentStatistics(clinicId,
                                            session.getServletContext()
                                            .getAttribute("referenceCharacters"));
                            request.setAttribute("treatmentStats",
                                    treatmentStats.getGroups().get(avgA1cChangeIndex));
                            session.setAttribute("classCountsTreatmentStats",
                                    treatmentStats);
                            session.setAttribute("genderClassCountsTreatmentStats",
                                    treatmentStats);
                            session.setAttribute("raceClassCountsTreatmentStats",
                                    treatmentStats);
                            break;
                        }
                        default:
                            break;
                    }
                }
            }
            default:
                break;

        }
        getServletContext().getRequestDispatcher(url)
                .forward(request, response);
    }

}
