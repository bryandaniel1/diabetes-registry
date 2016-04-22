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

import clinic.A1cResult;
import clinic.BloodPressureResult;
import clinic.CategoricalValue;
import clinic.ContinuousResult;
import clinic.DemographicData;
import clinic.DiscreteResult;
import clinic.HealthyTargetReference;
import clinic.Stats;
import clinic.LdlResult;
import clinic.PsychologicalScreeningResult;
import clinic.ReferenceContainer;
import clinic.TshResult;
import java.awt.BasicStroke;
import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.BoxAndWhiskerRenderer;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.statistics.BoxAndWhiskerCalculator;
import org.jfree.data.statistics.DefaultBoxAndWhiskerCategoryDataset;
import org.jfree.data.statistics.HistogramDataset;

/**
 * Creates a line chart to display a longitudinal view of patient results
 *
 * @author Bryan Daniel
 * @version 1, April 8, 2016
 */
public class ChartAndGraphServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {

        try {
            getChart(request, response);
        } catch (IOException e) {
            System.out.println("exception");
        }
    }

    /**
     * Creates the appropriate chart for the patient history page
     *
     * @param request servlet request
     * @param response servlet response
     * @throws IOException
     */
    public void getChart(HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        response.setContentType("image/png");
        OutputStream outputStream = response.getOutputStream();
        HttpSession session = request.getSession();
        final int WIDTH_INCREASE_THRESHOLD = 18;
        final int INCREMENTAL_INCREASE_THRESHOLD = 22;
        final int INCREMENTAL_INCREASE_IN_PIXELS = 30;
        final int TREATMENT_CLASS_ZERO_INDEX = 0;
        final int TREATMENT_CLASS_ONE_INDEX = 1;
        final int TREATMENT_CLASS_TWO_INDEX = 2;
        final int TREATMENT_CLASS_THREE_INDEX = 3;
        final int TREATMENT_CLASS_FOUR_INDEX = 4;
        final int TREATMENT_CLASS_FIVE_INDEX = 5;
        final int TREATMENT_CLASS_UNKNOWN_INDEX = 6;
        int width = 640;
        int height = 450;
        int bigWidth = 780;
        ReferenceContainer rc
                = (ReferenceContainer) session.getServletContext().getAttribute("references");
        HealthyTargetReference htr = rc.getHealthyTargets();
        String action = request.getParameter("action");

        switch (action) {
            case "a1c": {
                ArrayList<A1cResult> a1cHistory
                        = (ArrayList<A1cResult>) session.getAttribute("a1cGraphPoints");
                DefaultCategoryDataset dataset = new DefaultCategoryDataset();

                /* add the data */
                for (int i = a1cHistory.size() - 1; i > -1; i--) {
                    dataset.addValue(a1cHistory.get(i).getValue(), "A1C",
                            a1cHistory.get(i).getDate());
                }
                /* remove reference */
                session.setAttribute("a1cGraphPoints", null);

                boolean legend = true;
                boolean tooltips = false;
                boolean urls = false;

                /* get the chart */
                JFreeChart chart = ChartFactory.createLineChart("A1C History",
                        "dates", "A1C", dataset, PlotOrientation.VERTICAL, legend,
                        tooltips, urls);

                /* angle the x-axis labels */
                CategoryPlot plot = chart.getCategoryPlot();
                CategoryAxis xAxis = (CategoryAxis) plot.getDomainAxis();
                xAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);

                chart.setBorderPaint(Color.GREEN);
                chart.setBorderStroke(new BasicStroke(5.0f));
                chart.setBorderVisible(true);

                /* show the healthy target boundaries */
                BigDecimal upper = htr.getA1c().getUpperBound();
                BigDecimal lower = htr.getA1c().getLowerBound();
                if (upper != null) {
                    ValueMarker marker = new ValueMarker(upper.doubleValue());
                    marker.setPaint(Color.YELLOW);
                    plot.addRangeMarker(marker);
                }
                if (lower != null) {
                    ValueMarker marker = new ValueMarker(lower.doubleValue());
                    marker.setPaint(Color.YELLOW);
                    plot.addRangeMarker(marker);
                }

                if (a1cHistory.size() > WIDTH_INCREASE_THRESHOLD) {
                    width = bigWidth;
                }
                if (a1cHistory.size() > INCREMENTAL_INCREASE_THRESHOLD) {
                    int increments = a1cHistory.size() % INCREMENTAL_INCREASE_THRESHOLD;
                    for (int i = 0; i < increments; i++) {
                        width += INCREMENTAL_INCREASE_IN_PIXELS;
                    }
                }
                ChartUtilities.writeChartAsPNG(outputStream, chart, width, height);
                break;
            }
            case "psa": {
                ArrayList<ContinuousResult> psaHistory
                        = (ArrayList<ContinuousResult>) session.getAttribute("psaGraphPoints");
                DefaultCategoryDataset dataset = new DefaultCategoryDataset();

                /* add the data */
                for (int i = psaHistory.size() - 1; i > -1; i--) {
                    dataset.addValue(psaHistory.get(i).getValue(), "PSA",
                            psaHistory.get(i).getDate());
                }

                /* remove reference */
                session.setAttribute("psaGraphPoints", null);

                boolean legend = true;
                boolean tooltips = false;
                boolean urls = false;

                /* get the chart */
                JFreeChart chart = ChartFactory.createLineChart("PSA History",
                        "dates", "PSA", dataset, PlotOrientation.VERTICAL, legend,
                        tooltips, urls);

                /* angle the x-axis labels */
                CategoryPlot plot = chart.getCategoryPlot();
                CategoryAxis xAxis = (CategoryAxis) plot.getDomainAxis();
                xAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);

                chart.setBorderPaint(Color.GREEN);
                chart.setBorderStroke(new BasicStroke(5.0f));
                chart.setBorderVisible(true);

                /* show the healthy target boundaries */
                BigDecimal upper = htr.getPsa().getUpperBound();
                BigDecimal lower = htr.getPsa().getLowerBound();
                if (upper != null) {
                    ValueMarker marker = new ValueMarker(upper.doubleValue());
                    marker.setPaint(Color.YELLOW);
                    plot.addRangeMarker(marker);
                }
                if (lower != null) {
                    ValueMarker marker = new ValueMarker(lower.doubleValue());
                    marker.setPaint(Color.YELLOW);
                    plot.addRangeMarker(marker);
                }

                if (psaHistory.size() > WIDTH_INCREASE_THRESHOLD) {
                    width = bigWidth;
                }
                if (psaHistory.size() > INCREMENTAL_INCREASE_THRESHOLD) {
                    int increments = psaHistory.size() % INCREMENTAL_INCREASE_THRESHOLD;
                    for (int i = 0; i < increments; i++) {
                        width += INCREMENTAL_INCREASE_IN_PIXELS;
                    }
                }
                ChartUtilities.writeChartAsPNG(outputStream, chart, width, height);
                break;
            }
            case "alt": {
                ArrayList<ContinuousResult> altHistory
                        = (ArrayList<ContinuousResult>) session.getAttribute("altGraphPoints");
                DefaultCategoryDataset dataset = new DefaultCategoryDataset();

                /* add the data */
                for (int i = altHistory.size() - 1; i > -1; i--) {
                    dataset.addValue(altHistory.get(i).getValue(), "ALT",
                            altHistory.get(i).getDate());
                }

                /* remove reference */
                session.setAttribute("altGraphPoints", null);

                boolean legend = true;
                boolean tooltips = false;
                boolean urls = false;

                /* get the chart */
                JFreeChart chart = ChartFactory.createLineChart("ALT History",
                        "dates", "ALT", dataset, PlotOrientation.VERTICAL, legend,
                        tooltips, urls);

                /* angle the x-axis labels */
                CategoryPlot plot = chart.getCategoryPlot();
                CategoryAxis xAxis = (CategoryAxis) plot.getDomainAxis();
                xAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);

                chart.setBorderPaint(Color.GREEN);
                chart.setBorderStroke(new BasicStroke(5.0f));
                chart.setBorderVisible(true);

                /* show the healthy target boundaries */
                BigDecimal upper = htr.getAlt().getUpperBound();
                BigDecimal lower = htr.getAlt().getLowerBound();
                if (upper != null) {
                    ValueMarker marker = new ValueMarker(upper.doubleValue());
                    marker.setPaint(Color.YELLOW);
                    plot.addRangeMarker(marker);
                }
                if (lower != null) {
                    ValueMarker marker = new ValueMarker(lower.doubleValue());
                    marker.setPaint(Color.YELLOW);
                    plot.addRangeMarker(marker);
                }

                if (altHistory.size() > WIDTH_INCREASE_THRESHOLD) {
                    width = bigWidth;
                }
                if (altHistory.size() > INCREMENTAL_INCREASE_THRESHOLD) {
                    int increments = altHistory.size() % INCREMENTAL_INCREASE_THRESHOLD;
                    for (int i = 0; i < increments; i++) {
                        width += INCREMENTAL_INCREASE_IN_PIXELS;
                    }
                }
                ChartUtilities.writeChartAsPNG(outputStream, chart, width, height);
                break;
            }
            case "ast": {
                ArrayList<ContinuousResult> astHistory
                        = (ArrayList<ContinuousResult>) session.getAttribute("astGraphPoints");
                DefaultCategoryDataset dataset = new DefaultCategoryDataset();

                /* add the data */
                for (int i = astHistory.size() - 1; i > -1; i--) {
                    dataset.addValue(astHistory.get(i).getValue(), "AST",
                            astHistory.get(i).getDate());
                }

                /* remove reference */
                session.setAttribute("astGraphPoints", null);

                boolean legend = true;
                boolean tooltips = false;
                boolean urls = false;

                /* get the chart */
                JFreeChart chart = ChartFactory.createLineChart("AST History",
                        "dates", "AST", dataset, PlotOrientation.VERTICAL, legend,
                        tooltips, urls);

                /* angle the x-axis labels */
                CategoryPlot plot = chart.getCategoryPlot();
                CategoryAxis xAxis = (CategoryAxis) plot.getDomainAxis();
                xAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);

                chart.setBorderPaint(Color.GREEN);
                chart.setBorderStroke(new BasicStroke(5.0f));
                chart.setBorderVisible(true);

                /* show the healthy target boundaries */
                BigDecimal upper = htr.getAst().getUpperBound();
                BigDecimal lower = htr.getAst().getLowerBound();
                if (upper != null) {
                    ValueMarker marker = new ValueMarker(upper.doubleValue());
                    marker.setPaint(Color.YELLOW);
                    plot.addRangeMarker(marker);
                }
                if (lower != null) {
                    ValueMarker marker = new ValueMarker(lower.doubleValue());
                    marker.setPaint(Color.YELLOW);
                    plot.addRangeMarker(marker);
                }

                if (astHistory.size() > WIDTH_INCREASE_THRESHOLD) {
                    width = bigWidth;
                }
                if (astHistory.size() > INCREMENTAL_INCREASE_THRESHOLD) {
                    int increments = astHistory.size() % INCREMENTAL_INCREASE_THRESHOLD;
                    for (int i = 0; i < increments; i++) {
                        width += INCREMENTAL_INCREASE_IN_PIXELS;
                    }
                }
                ChartUtilities.writeChartAsPNG(outputStream, chart, width, height);
                break;
            }
            case "bp": {
                ArrayList<BloodPressureResult> bpHistory
                        = (ArrayList<BloodPressureResult>) session.getAttribute("bpGraphPoints");
                DefaultCategoryDataset dataset = new DefaultCategoryDataset();

                /* add the data */
                for (int i = bpHistory.size() - 1; i > -1; i--) {
                    dataset.addValue(bpHistory.get(i).getSystolicValue(), "systolic",
                            bpHistory.get(i).getDate());
                    dataset.addValue(bpHistory.get(i).getDiastolicValue(), "diastolic",
                            bpHistory.get(i).getDate());
                }

                /* remove reference */
                session.setAttribute("bpGraphPoints", null);

                boolean legend = true;
                boolean tooltips = false;
                boolean urls = false;

                /* get the chart */
                JFreeChart chart = ChartFactory.createLineChart("Blood Pressure History",
                        "dates", "blood pressure", dataset, PlotOrientation.VERTICAL, legend,
                        tooltips, urls);

                /* angle the x-axis labels */
                CategoryPlot plot = chart.getCategoryPlot();
                CategoryAxis xAxis = (CategoryAxis) plot.getDomainAxis();
                xAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);

                chart.setBorderPaint(Color.GREEN);
                chart.setBorderStroke(new BasicStroke(5.0f));
                chart.setBorderVisible(true);

                /* show the healthy target boundaries */
                BigDecimal upperSystole = htr.getBloodPressureSystole().getUpperBound();
                BigDecimal upperDiastole = htr.getBloodPressureDiastole().getUpperBound();
                if (upperSystole != null) {
                    ValueMarker marker = new ValueMarker(upperSystole.doubleValue());
                    marker.setPaint(Color.MAGENTA);
                    plot.addRangeMarker(marker);
                }
                if (upperDiastole != null) {
                    ValueMarker marker = new ValueMarker(upperDiastole.doubleValue());
                    marker.setPaint(Color.BLUE);
                    plot.addRangeMarker(marker);
                }

                if (bpHistory.size() > WIDTH_INCREASE_THRESHOLD) {
                    width = bigWidth;
                }
                if (bpHistory.size() > INCREMENTAL_INCREASE_THRESHOLD) {
                    int increments = bpHistory.size() % INCREMENTAL_INCREASE_THRESHOLD;
                    for (int i = 0; i < increments; i++) {
                        width += INCREMENTAL_INCREASE_IN_PIXELS;
                    }
                }
                ChartUtilities.writeChartAsPNG(outputStream, chart, width, height);
                break;
            }
            case "bmi": {
                ArrayList<ContinuousResult> bmiHistory
                        = (ArrayList<ContinuousResult>) session.getAttribute("bmiGraphPoints");
                DefaultCategoryDataset dataset = new DefaultCategoryDataset();

                /* add the data */
                for (int i = bmiHistory.size() - 1; i > -1; i--) {
                    dataset.addValue(bmiHistory.get(i).getValue(), "BMI",
                            bmiHistory.get(i).getDate());
                }

                /* remove reference */
                session.setAttribute("bmiGraphPoints", null);

                boolean legend = true;
                boolean tooltips = false;
                boolean urls = false;

                /* get the chart */
                JFreeChart chart = ChartFactory.createLineChart("BMI History",
                        "dates", "BMI", dataset, PlotOrientation.VERTICAL, legend,
                        tooltips, urls);

                /* angle the x-axis labels */
                CategoryPlot plot = chart.getCategoryPlot();
                CategoryAxis xAxis = (CategoryAxis) plot.getDomainAxis();
                xAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);

                chart.setBorderPaint(Color.GREEN);
                chart.setBorderStroke(new BasicStroke(5.0f));
                chart.setBorderVisible(true);

                /* show the healthy target boundaries */
                BigDecimal upper = htr.getBmi().getUpperBound();
                BigDecimal lower = htr.getBmi().getLowerBound();
                if (upper != null) {
                    ValueMarker marker = new ValueMarker(upper.doubleValue());
                    marker.setPaint(Color.YELLOW);
                    plot.addRangeMarker(marker);
                }
                if (lower != null) {
                    ValueMarker marker = new ValueMarker(lower.doubleValue());
                    marker.setPaint(Color.YELLOW);
                    plot.addRangeMarker(marker);
                }

                if (bmiHistory.size() > WIDTH_INCREASE_THRESHOLD) {
                    width = bigWidth;
                }
                if (bmiHistory.size() > INCREMENTAL_INCREASE_THRESHOLD) {
                    int increments = bmiHistory.size() % INCREMENTAL_INCREASE_THRESHOLD;
                    for (int i = 0; i < increments; i++) {
                        width += INCREMENTAL_INCREASE_IN_PIXELS;
                    }
                }
                ChartUtilities.writeChartAsPNG(outputStream, chart, width, height);
                break;
            }
            case "creatinine": {
                ArrayList<ContinuousResult> creatinineHistory
                        = (ArrayList<ContinuousResult>) session.getAttribute("creatinineGraphPoints");
                DefaultCategoryDataset dataset = new DefaultCategoryDataset();

                /* add the data */
                for (int i = creatinineHistory.size() - 1; i > -1; i--) {
                    dataset.addValue(creatinineHistory.get(i).getValue(), "creatinine",
                            creatinineHistory.get(i).getDate());
                }

                /* remove reference */
                session.setAttribute("creatinineGraphPoints", null);

                boolean legend = true;
                boolean tooltips = false;
                boolean urls = false;

                /* get the chart */
                JFreeChart chart = ChartFactory.createLineChart("Creatinine History",
                        "dates", "creatinine", dataset, PlotOrientation.VERTICAL, legend,
                        tooltips, urls);

                /* angle the x-axis labels */
                CategoryPlot plot = chart.getCategoryPlot();
                CategoryAxis xAxis = (CategoryAxis) plot.getDomainAxis();
                xAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);

                chart.setBorderPaint(Color.GREEN);
                chart.setBorderStroke(new BasicStroke(5.0f));
                chart.setBorderVisible(true);

                /* show the healthy target boundaries */
                BigDecimal upper = htr.getCreatinine().getUpperBound();
                BigDecimal lower = htr.getCreatinine().getLowerBound();
                if (upper != null) {
                    ValueMarker marker = new ValueMarker(upper.doubleValue());
                    marker.setPaint(Color.YELLOW);
                    plot.addRangeMarker(marker);
                }
                if (lower != null) {
                    ValueMarker marker = new ValueMarker(lower.doubleValue());
                    marker.setPaint(Color.YELLOW);
                    plot.addRangeMarker(marker);
                }

                if (creatinineHistory.size() > WIDTH_INCREASE_THRESHOLD) {
                    width = bigWidth;
                }
                if (creatinineHistory.size() > INCREMENTAL_INCREASE_THRESHOLD) {
                    int increments = creatinineHistory.size() % INCREMENTAL_INCREASE_THRESHOLD;
                    for (int i = 0; i < increments; i++) {
                        width += INCREMENTAL_INCREASE_IN_PIXELS;
                    }
                }
                ChartUtilities.writeChartAsPNG(outputStream, chart, width, height);
                break;
            }
            case "egfr": {
                ArrayList<ContinuousResult> egfrHistory
                        = (ArrayList<ContinuousResult>) session.getAttribute("egfrGraphPoints");
                DefaultCategoryDataset dataset = new DefaultCategoryDataset();

                /* add the data */
                for (int i = egfrHistory.size() - 1; i > -1; i--) {
                    dataset.addValue(egfrHistory.get(i).getValue(), "eGFR",
                            egfrHistory.get(i).getDate());
                }

                /* remove reference */
                session.setAttribute("egfrGraphPoints", null);

                boolean legend = true;
                boolean tooltips = false;
                boolean urls = false;

                /* get the chart */
                JFreeChart chart = ChartFactory.createLineChart("eGFR History",
                        "dates", "eGFR", dataset, PlotOrientation.VERTICAL, legend,
                        tooltips, urls);

                /* angle the x-axis labels */
                CategoryPlot plot = chart.getCategoryPlot();
                CategoryAxis xAxis = (CategoryAxis) plot.getDomainAxis();
                xAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);

                chart.setBorderPaint(Color.GREEN);
                chart.setBorderStroke(new BasicStroke(5.0f));
                chart.setBorderVisible(true);

                /* show the healthy target boundaries */
                BigDecimal upper = htr.getEgfr().getUpperBound();
                BigDecimal lower = htr.getEgfr().getLowerBound();
                if (upper != null) {
                    ValueMarker marker = new ValueMarker(upper.doubleValue());
                    marker.setPaint(Color.YELLOW);
                    plot.addRangeMarker(marker);
                }
                if (lower != null) {
                    ValueMarker marker = new ValueMarker(lower.doubleValue());
                    marker.setPaint(Color.YELLOW);
                    plot.addRangeMarker(marker);
                }

                if (egfrHistory.size() > WIDTH_INCREASE_THRESHOLD) {
                    width = bigWidth;
                }
                if (egfrHistory.size() > INCREMENTAL_INCREASE_THRESHOLD) {
                    int increments = egfrHistory.size() % INCREMENTAL_INCREASE_THRESHOLD;
                    for (int i = 0; i < increments; i++) {
                        width += INCREMENTAL_INCREASE_IN_PIXELS;
                    }
                }
                ChartUtilities.writeChartAsPNG(outputStream, chart, width, height);
                break;
            }
            case "glucose": {
                ArrayList<ContinuousResult> glucoseHistory
                        = (ArrayList<ContinuousResult>) session.getAttribute("glucoseGraphPoints");
                DefaultCategoryDataset dataset = new DefaultCategoryDataset();

                /* add the data */
                for (int i = glucoseHistory.size() - 1; i > -1; i--) {
                    dataset.addValue(glucoseHistory.get(i).getValue(), "glucose",
                            glucoseHistory.get(i).getDate());
                }

                /* remove reference */
                session.setAttribute("glucoseGraphPoints", null);

                boolean legend = true;
                boolean tooltips = false;
                boolean urls = false;

                /* get the chart */
                JFreeChart chart = ChartFactory.createLineChart("Glucose History",
                        "dates", "glucose", dataset, PlotOrientation.VERTICAL, legend,
                        tooltips, urls);

                /* angle the x-axis labels */
                CategoryPlot plot = chart.getCategoryPlot();
                CategoryAxis xAxis = (CategoryAxis) plot.getDomainAxis();
                xAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);

                chart.setBorderPaint(Color.GREEN);
                chart.setBorderStroke(new BasicStroke(5.0f));
                chart.setBorderVisible(true);

                /* show the healthy target boundaries */
                BigDecimal upper = htr.getGlucoseAc().getUpperBound();
                BigDecimal lower = htr.getGlucoseAc().getLowerBound();
                if (upper != null) {
                    ValueMarker marker = new ValueMarker(upper.doubleValue());
                    marker.setPaint(Color.YELLOW);
                    plot.addRangeMarker(marker);
                }
                if (lower != null) {
                    ValueMarker marker = new ValueMarker(lower.doubleValue());
                    marker.setPaint(Color.YELLOW);
                    plot.addRangeMarker(marker);
                }

                if (glucoseHistory.size() > WIDTH_INCREASE_THRESHOLD) {
                    width = bigWidth;
                }
                if (glucoseHistory.size() > INCREMENTAL_INCREASE_THRESHOLD) {
                    int increments = glucoseHistory.size() % INCREMENTAL_INCREASE_THRESHOLD;
                    for (int i = 0; i < increments; i++) {
                        width += INCREMENTAL_INCREASE_IN_PIXELS;
                    }
                }
                ChartUtilities.writeChartAsPNG(outputStream, chart, width, height);
                break;
            }
            case "hdl": {
                ArrayList<ContinuousResult> hdlHistory
                        = (ArrayList<ContinuousResult>) session.getAttribute("hdlGraphPoints");
                DefaultCategoryDataset dataset = new DefaultCategoryDataset();

                /* add the data */
                for (int i = hdlHistory.size() - 1; i > -1; i--) {
                    dataset.addValue(hdlHistory.get(i).getValue(), "HDL",
                            hdlHistory.get(i).getDate());
                }

                /* remove reference */
                session.setAttribute("hdlGraphPoints", null);

                boolean legend = true;
                boolean tooltips = false;
                boolean urls = false;

                /*get the chart */
                JFreeChart chart = ChartFactory.createLineChart("HDL History",
                        "dates", "HDL", dataset, PlotOrientation.VERTICAL, legend,
                        tooltips, urls);

                /* angle the x-axis labels */
                CategoryPlot plot = chart.getCategoryPlot();
                CategoryAxis xAxis = (CategoryAxis) plot.getDomainAxis();
                xAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);

                chart.setBorderPaint(Color.GREEN);
                chart.setBorderStroke(new BasicStroke(5.0f));
                chart.setBorderVisible(true);

                /* show the healthy target boundaries */
                BigDecimal upperFemale = htr.getHdlFemale().getUpperBound();
                BigDecimal lowerFemale = htr.getHdlFemale().getLowerBound();
                BigDecimal upperMale = htr.getHdlMale().getUpperBound();
                BigDecimal lowerMale = htr.getHdlMale().getLowerBound();
                if (upperFemale != null) {
                    ValueMarker marker = new ValueMarker(upperFemale.doubleValue());
                    marker.setPaint(Color.MAGENTA);
                    plot.addRangeMarker(marker);
                }
                if (lowerFemale != null) {
                    ValueMarker marker = new ValueMarker(lowerFemale.doubleValue());
                    marker.setPaint(Color.MAGENTA);
                    plot.addRangeMarker(marker);
                }
                if (upperMale != null) {
                    ValueMarker marker = new ValueMarker(upperMale.doubleValue());
                    marker.setPaint(Color.BLUE);
                    plot.addRangeMarker(marker);
                }
                if (lowerMale != null) {
                    ValueMarker marker = new ValueMarker(lowerMale.doubleValue());
                    marker.setPaint(Color.BLUE);
                    plot.addRangeMarker(marker);
                }

                if (hdlHistory.size() > WIDTH_INCREASE_THRESHOLD) {
                    width = bigWidth;
                }
                if (hdlHistory.size() > INCREMENTAL_INCREASE_THRESHOLD) {
                    int increments = hdlHistory.size() % INCREMENTAL_INCREASE_THRESHOLD;
                    for (int i = 0; i < increments; i++) {
                        width += INCREMENTAL_INCREASE_IN_PIXELS;
                    }
                }
                ChartUtilities.writeChartAsPNG(outputStream, chart, width, height);
                break;
            }
            case "ldl": {
                ArrayList<LdlResult> ldlHistory
                        = (ArrayList<LdlResult>) session.getAttribute("ldlGraphPoints");
                DefaultCategoryDataset dataset = new DefaultCategoryDataset();

                /* add the data */
                for (int i = ldlHistory.size() - 1; i > -1; i--) {
                    dataset.addValue(ldlHistory.get(i).getValue(), "LDL",
                            ldlHistory.get(i).getDate());
                }

                /* remove reference */
                session.setAttribute("ldlGraphPoints", null);

                boolean legend = true;
                boolean tooltips = false;
                boolean urls = false;

                /* get the chart */
                JFreeChart chart = ChartFactory.createLineChart("LDL History",
                        "dates", "LDL", dataset, PlotOrientation.VERTICAL, legend,
                        tooltips, urls);

                /* angle the x-axis labels */
                CategoryPlot plot = chart.getCategoryPlot();
                CategoryAxis xAxis = (CategoryAxis) plot.getDomainAxis();
                xAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);

                chart.setBorderPaint(Color.GREEN);
                chart.setBorderStroke(new BasicStroke(5.0f));
                chart.setBorderVisible(true);

                /* show the healthy target boundaries */
                BigDecimal upper = htr.getLdl().getUpperBound();
                BigDecimal lower = htr.getLdl().getLowerBound();
                if (upper != null) {
                    ValueMarker marker = new ValueMarker(upper.doubleValue());
                    marker.setPaint(Color.YELLOW);
                    plot.addRangeMarker(marker);
                }
                if (lower != null) {
                    ValueMarker marker = new ValueMarker(lower.doubleValue());
                    marker.setPaint(Color.YELLOW);
                    plot.addRangeMarker(marker);
                }

                if (ldlHistory.size() > WIDTH_INCREASE_THRESHOLD) {
                    width = bigWidth;
                }
                if (ldlHistory.size() > INCREMENTAL_INCREASE_THRESHOLD) {
                    int increments = ldlHistory.size() % INCREMENTAL_INCREASE_THRESHOLD;
                    for (int i = 0; i < increments; i++) {
                        width += INCREMENTAL_INCREASE_IN_PIXELS;
                    }
                }
                ChartUtilities.writeChartAsPNG(outputStream, chart, width, height);
                break;
            }
            case "compliance": {
                ArrayList<ContinuousResult> complianceHistory
                        = (ArrayList<ContinuousResult>) session.getAttribute("complianceGraphPoints");
                DefaultCategoryDataset dataset = new DefaultCategoryDataset();

                /* add the data */
                for (int i = complianceHistory.size() - 1; i > -1; i--) {
                    dataset.addValue(complianceHistory.get(i).getValue(), "compliance",
                            complianceHistory.get(i).getDate());
                }

                /* remove reference */
                session.setAttribute("complianceGraphPoints", null);

                boolean legend = true;
                boolean tooltips = false;
                boolean urls = false;

                /* get the chart */
                JFreeChart chart = ChartFactory.createLineChart("Compliance History",
                        "dates", "compliance", dataset, PlotOrientation.VERTICAL, legend,
                        tooltips, urls);

                /* angle the x-axis labels */
                CategoryPlot plot = chart.getCategoryPlot();
                CategoryAxis xAxis = (CategoryAxis) plot.getDomainAxis();
                xAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);

                chart.setBorderPaint(Color.GREEN);
                chart.setBorderStroke(new BasicStroke(5.0f));
                chart.setBorderVisible(true);

                if (complianceHistory.size() > WIDTH_INCREASE_THRESHOLD) {
                    width = bigWidth;
                }
                if (complianceHistory.size() > INCREMENTAL_INCREASE_THRESHOLD) {
                    int increments = complianceHistory.size() % INCREMENTAL_INCREASE_THRESHOLD;
                    for (int i = 0; i < increments; i++) {
                        width += INCREMENTAL_INCREASE_IN_PIXELS;
                    }
                }
                ChartUtilities.writeChartAsPNG(outputStream, chart, width, height);
                break;
            }
            case "physicalActivity": {
                ArrayList<DiscreteResult> physicalActivityHistory
                        = (ArrayList<DiscreteResult>) session.getAttribute("physicalActivityGraphPoints");
                DefaultCategoryDataset dataset = new DefaultCategoryDataset();

                /* add the data */
                for (int i = physicalActivityHistory.size() - 1; i > -1; i--) {
                    dataset.addValue(physicalActivityHistory.get(i).getValue(), "physical activity",
                            physicalActivityHistory.get(i).getDate());
                }

                /* remove reference */
                session.setAttribute("physicalActivityGraphPoints", null);

                boolean legend = true;
                boolean tooltips = false;
                boolean urls = false;

                /* get the chart */
                JFreeChart chart = ChartFactory.createLineChart("Physical Activity History",
                        "dates", "min per wk", dataset, PlotOrientation.VERTICAL, legend,
                        tooltips, urls);

                /* angle the x-axis labels */
                CategoryPlot plot = chart.getCategoryPlot();
                CategoryAxis xAxis = (CategoryAxis) plot.getDomainAxis();
                xAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);

                chart.setBorderPaint(Color.GREEN);
                chart.setBorderStroke(new BasicStroke(5.0f));
                chart.setBorderVisible(true);

                /* show the healthy target boundaries */
                BigDecimal upper = htr.getPhysicalActivity().getUpperBound();
                BigDecimal lower = htr.getPhysicalActivity().getLowerBound();
                if (upper != null) {
                    ValueMarker marker = new ValueMarker(upper.doubleValue());
                    marker.setPaint(Color.YELLOW);
                    plot.addRangeMarker(marker);
                }
                if (lower != null) {
                    ValueMarker marker = new ValueMarker(lower.doubleValue());
                    marker.setPaint(Color.YELLOW);
                    plot.addRangeMarker(marker);
                }

                if (physicalActivityHistory.size() > WIDTH_INCREASE_THRESHOLD) {
                    width = bigWidth;
                }
                if (physicalActivityHistory.size() > INCREMENTAL_INCREASE_THRESHOLD) {
                    int increments = physicalActivityHistory.size() % INCREMENTAL_INCREASE_THRESHOLD;
                    for (int i = 0; i < increments; i++) {
                        width += INCREMENTAL_INCREASE_IN_PIXELS;
                    }
                }
                ChartUtilities.writeChartAsPNG(outputStream, chart, width, height);
                break;
            }
            case "psychological": {
                ArrayList<PsychologicalScreeningResult> psychologicalHistory
                        = (ArrayList<PsychologicalScreeningResult>) session.getAttribute("psychologicalGraphPoints");
                DefaultCategoryDataset dataset = new DefaultCategoryDataset();

                /* add the data */
                for (int i = psychologicalHistory.size() - 1; i > -1; i--) {
                    dataset.addValue(psychologicalHistory.get(i).getScore(), "PHQ9 score",
                            psychologicalHistory.get(i).getDate());
                }

                /* remove reference */
                session.setAttribute("psychologicalGraphPoints", null);

                boolean legend = true;
                boolean tooltips = false;
                boolean urls = false;

                /* get the chart */
                JFreeChart chart = ChartFactory.createLineChart("Psychological Screening History",
                        "dates", "score", dataset, PlotOrientation.VERTICAL, legend,
                        tooltips, urls);

                /* angle the x-axis labels */
                CategoryPlot plot = chart.getCategoryPlot();
                CategoryAxis xAxis = (CategoryAxis) plot.getDomainAxis();
                xAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);

                chart.setBorderPaint(Color.GREEN);
                chart.setBorderStroke(new BasicStroke(5.0f));
                chart.setBorderVisible(true);

                if (psychologicalHistory.size() > WIDTH_INCREASE_THRESHOLD) {
                    width = bigWidth;
                }
                if (psychologicalHistory.size() > INCREMENTAL_INCREASE_THRESHOLD) {
                    int increments = psychologicalHistory.size() % INCREMENTAL_INCREASE_THRESHOLD;
                    for (int i = 0; i < increments; i++) {
                        width += INCREMENTAL_INCREASE_IN_PIXELS;
                    }
                }
                ChartUtilities.writeChartAsPNG(outputStream, chart, width, height);
                break;
            }
            case "t4": {
                ArrayList<ContinuousResult> t4History
                        = (ArrayList<ContinuousResult>) session.getAttribute("t4GraphPoints");
                DefaultCategoryDataset dataset = new DefaultCategoryDataset();

                /* add the data */
                for (int i = t4History.size() - 1; i > -1; i--) {
                    dataset.addValue(t4History.get(i).getValue(), "T4",
                            t4History.get(i).getDate());
                }

                /* remove reference */
                session.setAttribute("t4GraphPoints", null);

                boolean legend = true;
                boolean tooltips = false;
                boolean urls = false;

                /* get the chart */
                JFreeChart chart = ChartFactory.createLineChart("T4 History",
                        "dates", "T4", dataset, PlotOrientation.VERTICAL, legend,
                        tooltips, urls);

                /* angle the x-axis labels */
                CategoryPlot plot = chart.getCategoryPlot();
                CategoryAxis xAxis = (CategoryAxis) plot.getDomainAxis();
                xAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);

                chart.setBorderPaint(Color.GREEN);
                chart.setBorderStroke(new BasicStroke(5.0f));
                chart.setBorderVisible(true);

                /* show the healthy target boundaries */
                BigDecimal upper = htr.getT4().getUpperBound();
                BigDecimal lower = htr.getT4().getLowerBound();
                if (upper != null) {
                    ValueMarker marker = new ValueMarker(upper.doubleValue());
                    marker.setPaint(Color.YELLOW);
                    plot.addRangeMarker(marker);
                }
                if (lower != null) {
                    ValueMarker marker = new ValueMarker(lower.doubleValue());
                    marker.setPaint(Color.YELLOW);
                    plot.addRangeMarker(marker);
                }

                if (t4History.size() > WIDTH_INCREASE_THRESHOLD) {
                    width = bigWidth;
                }
                if (t4History.size() > INCREMENTAL_INCREASE_THRESHOLD) {
                    int increments = t4History.size() % INCREMENTAL_INCREASE_THRESHOLD;
                    for (int i = 0; i < increments; i++) {
                        width += INCREMENTAL_INCREASE_IN_PIXELS;
                    }
                }
                ChartUtilities.writeChartAsPNG(outputStream, chart, width, height);
                break;
            }
            case "triglycerides": {
                ArrayList<ContinuousResult> triglyceridesHistory
                        = (ArrayList<ContinuousResult>) session.getAttribute("triglyceridesGraphPoints");
                DefaultCategoryDataset dataset = new DefaultCategoryDataset();

                /* add the data */
                for (int i = triglyceridesHistory.size() - 1; i > -1; i--) {
                    dataset.addValue(triglyceridesHistory.get(i).getValue(), "triglycerides",
                            triglyceridesHistory.get(i).getDate());
                }

                /* remove reference */
                session.setAttribute("triglyceridesGraphPoints", null);

                boolean legend = true;
                boolean tooltips = false;
                boolean urls = false;

                /* get the chart */
                JFreeChart chart = ChartFactory.createLineChart("Triglycerides History",
                        "dates", "triglycerides", dataset, PlotOrientation.VERTICAL, legend,
                        tooltips, urls);

                /* angle the x-axis labels */
                CategoryPlot plot = chart.getCategoryPlot();
                CategoryAxis xAxis = (CategoryAxis) plot.getDomainAxis();
                xAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);

                chart.setBorderPaint(Color.GREEN);
                chart.setBorderStroke(new BasicStroke(5.0f));
                chart.setBorderVisible(true);

                /* show the healthy target boundaries */
                BigDecimal upper = htr.getTriglycerides().getUpperBound();
                BigDecimal lower = htr.getTriglycerides().getLowerBound();
                if (upper != null) {
                    ValueMarker marker = new ValueMarker(upper.doubleValue());
                    marker.setPaint(Color.YELLOW);
                    plot.addRangeMarker(marker);
                }
                if (lower != null) {
                    ValueMarker marker = new ValueMarker(lower.doubleValue());
                    marker.setPaint(Color.YELLOW);
                    plot.addRangeMarker(marker);
                }

                if (triglyceridesHistory.size() > WIDTH_INCREASE_THRESHOLD) {
                    width = bigWidth;
                }
                if (triglyceridesHistory.size() > INCREMENTAL_INCREASE_THRESHOLD) {
                    int increments = triglyceridesHistory.size() % INCREMENTAL_INCREASE_THRESHOLD;
                    for (int i = 0; i < increments; i++) {
                        width += INCREMENTAL_INCREASE_IN_PIXELS;
                    }
                }
                ChartUtilities.writeChartAsPNG(outputStream, chart, width, height);
                break;
            }
            case "tsh": {
                ArrayList<TshResult> tshHistory
                        = (ArrayList<TshResult>) session.getAttribute("tshGraphPoints");
                DefaultCategoryDataset dataset = new DefaultCategoryDataset();

                /* add the data */
                for (int i = tshHistory.size() - 1; i > -1; i--) {
                    dataset.addValue(tshHistory.get(i).getValue(), "TSH",
                            tshHistory.get(i).getDate());
                }

                /* remove reference */
                session.setAttribute("tshGraphPoints", null);

                boolean legend = true;
                boolean tooltips = false;
                boolean urls = false;

                /* get the chart */
                JFreeChart chart = ChartFactory.createLineChart("TSH History",
                        "dates", "TSH", dataset, PlotOrientation.VERTICAL, legend,
                        tooltips, urls);

                /* angle the x-axis labels */
                CategoryPlot plot = chart.getCategoryPlot();
                CategoryAxis xAxis = (CategoryAxis) plot.getDomainAxis();
                xAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);

                chart.setBorderPaint(Color.GREEN);
                chart.setBorderStroke(new BasicStroke(5.0f));
                chart.setBorderVisible(true);

                /* show the healthy target boundaries */
                BigDecimal upper = htr.getTsh().getUpperBound();
                BigDecimal lower = htr.getTsh().getLowerBound();
                if (upper != null) {
                    ValueMarker marker = new ValueMarker(upper.doubleValue());
                    marker.setPaint(Color.YELLOW);
                    plot.addRangeMarker(marker);
                }
                if (lower != null) {
                    ValueMarker marker = new ValueMarker(lower.doubleValue());
                    marker.setPaint(Color.YELLOW);
                    plot.addRangeMarker(marker);
                }

                if (tshHistory.size() > WIDTH_INCREASE_THRESHOLD) {
                    width = bigWidth;
                }
                if (tshHistory.size() > INCREMENTAL_INCREASE_THRESHOLD) {
                    int increments = tshHistory.size() % INCREMENTAL_INCREASE_THRESHOLD;
                    for (int i = 0; i < increments; i++) {
                        width += INCREMENTAL_INCREASE_IN_PIXELS;
                    }
                }
                ChartUtilities.writeChartAsPNG(outputStream, chart, width, height);
                break;
            }
            case "uacr": {
                ArrayList<ContinuousResult> uacrHistory
                        = (ArrayList<ContinuousResult>) session.getAttribute("uacrGraphPoints");
                DefaultCategoryDataset dataset = new DefaultCategoryDataset();

                /* add the data */
                for (int i = uacrHistory.size() - 1; i > -1; i--) {
                    dataset.addValue(uacrHistory.get(i).getValue(), "UACR",
                            uacrHistory.get(i).getDate());
                }

                /* remove reference */
                session.setAttribute("uacrGraphPoints", null);

                boolean legend = true;
                boolean tooltips = false;
                boolean urls = false;

                /* get the chart */
                JFreeChart chart = ChartFactory.createLineChart("UACR History",
                        "dates", "UACR", dataset, PlotOrientation.VERTICAL, legend,
                        tooltips, urls);

                /* angle the x-axis labels */
                CategoryPlot plot = chart.getCategoryPlot();
                CategoryAxis xAxis = (CategoryAxis) plot.getDomainAxis();
                xAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);

                chart.setBorderPaint(Color.GREEN);
                chart.setBorderStroke(new BasicStroke(5.0f));
                chart.setBorderVisible(true);

                /* show the healthy target boundaries */
                BigDecimal upper = htr.getUacr().getUpperBound();
                BigDecimal lower = htr.getUacr().getLowerBound();
                if (upper != null) {
                    ValueMarker marker = new ValueMarker(upper.doubleValue());
                    marker.setPaint(Color.YELLOW);
                    plot.addRangeMarker(marker);
                }
                if (lower != null) {
                    ValueMarker marker = new ValueMarker(lower.doubleValue());
                    marker.setPaint(Color.YELLOW);
                    plot.addRangeMarker(marker);
                }

                if (uacrHistory.size() > WIDTH_INCREASE_THRESHOLD) {
                    width = bigWidth;
                }
                if (uacrHistory.size() > INCREMENTAL_INCREASE_THRESHOLD) {
                    int increments = uacrHistory.size() % INCREMENTAL_INCREASE_THRESHOLD;
                    for (int i = 0; i < increments; i++) {
                        width += INCREMENTAL_INCREASE_IN_PIXELS;
                    }
                }
                ChartUtilities.writeChartAsPNG(outputStream, chart, width, height);
                break;
            }
            case "waist": {
                ArrayList<ContinuousResult> waistHistory
                        = (ArrayList<ContinuousResult>) session.getAttribute("waistGraphPoints");
                DefaultCategoryDataset dataset = new DefaultCategoryDataset();

                /* add the data */
                for (int i = waistHistory.size() - 1; i > -1; i--) {
                    dataset.addValue(waistHistory.get(i).getValue(), "waist",
                            waistHistory.get(i).getDate());
                }

                /* remove reference */
                session.setAttribute("waistGraphPoints", null);

                boolean legend = true;
                boolean tooltips = false;
                boolean urls = false;

                /* get the chart */
                JFreeChart chart = ChartFactory.createLineChart("Waist History",
                        "dates", "waist", dataset, PlotOrientation.VERTICAL, legend,
                        tooltips, urls);

                /* angle the x-axis labels */
                CategoryPlot plot = chart.getCategoryPlot();
                CategoryAxis xAxis = (CategoryAxis) plot.getDomainAxis();
                xAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);

                chart.setBorderPaint(Color.GREEN);
                chart.setBorderStroke(new BasicStroke(5.0f));
                chart.setBorderVisible(true);

                /* show the healthy target boundaries */
                BigDecimal upperFemale = htr.getWaistFemale().getUpperBound();
                BigDecimal lowerFemale = htr.getWaistFemale().getLowerBound();
                BigDecimal upperMale = htr.getWaistMale().getUpperBound();
                BigDecimal lowerMale = htr.getWaistMale().getLowerBound();
                if (upperFemale != null) {
                    ValueMarker marker = new ValueMarker(upperFemale.doubleValue());
                    marker.setPaint(Color.MAGENTA);
                    plot.addRangeMarker(marker);
                }
                if (lowerFemale != null) {
                    ValueMarker marker = new ValueMarker(lowerFemale.doubleValue());
                    marker.setPaint(Color.MAGENTA);
                    plot.addRangeMarker(marker);
                }
                if (upperMale != null) {
                    ValueMarker marker = new ValueMarker(upperMale.doubleValue());
                    marker.setPaint(Color.BLUE);
                    plot.addRangeMarker(marker);
                }
                if (lowerMale != null) {
                    ValueMarker marker = new ValueMarker(lowerMale.doubleValue());
                    marker.setPaint(Color.BLUE);
                    plot.addRangeMarker(marker);
                }

                if (waistHistory.size() > WIDTH_INCREASE_THRESHOLD) {
                    width = bigWidth;
                }
                if (waistHistory.size() > INCREMENTAL_INCREASE_THRESHOLD) {
                    int increments = waistHistory.size() % INCREMENTAL_INCREASE_THRESHOLD;
                    for (int i = 0; i < increments; i++) {
                        width += INCREMENTAL_INCREASE_IN_PIXELS;
                    }
                }
                ChartUtilities.writeChartAsPNG(outputStream, chart, width, height);
                break;
            }
            case "agedemographics": {
                DemographicData demographicData
                        = (DemographicData) session.getAttribute("ageDemographicsGraphData");
                HistogramDataset dataset = new HistogramDataset();
                ArrayList<Integer> ages = demographicData.getAges();
                if (ages.size() > 0) {
                    double[] vector = new double[ages.size()];

                    for (int i = 0; i < vector.length; i++) {
                        vector[i] = ages.get(i);
                    }

                    /* add the data */
                    dataset.addSeries("number of patients", vector, 10);

                    /* remove reference */
                    session.setAttribute("ageDemographicsGraphData", null);

                    boolean legend = true;
                    boolean tooltips = false;
                    boolean urls = false;

                    /* get the chart */
                    JFreeChart chart = ChartFactory.createHistogram(
                            "Age Distribution", "age", "number of patients", dataset, PlotOrientation.VERTICAL,
                            legend, tooltips, urls);

                    chart.setBorderPaint(Color.GREEN);
                    chart.setBorderStroke(new BasicStroke(5.0f));
                    chart.setBorderVisible(true);

                    XYPlot plot = chart.getXYPlot();

                    final XYBarRenderer renderer = (XYBarRenderer) plot.getRenderer();

                    /* creating a shadow */
                    renderer.setShadowXOffset(4.0);
                    renderer.setShadowYOffset(1.5);
                    renderer.setShadowVisible(true);

                    ChartUtilities.writeChartAsPNG(outputStream, chart, width, height);
                }
                break;
            }
            case "genderdemographics": {
                DemographicData demographicData
                        = (DemographicData) session.getAttribute("genderDemographicsGraphData");
                DefaultPieDataset dataset = new DefaultPieDataset();

                /* add the data */
                dataset.setValue("female", demographicData.getPercentFemale());
                dataset.setValue("male", demographicData.getPercentMale());

                /* remove reference */
                session.setAttribute("genderDemographicsGraphData", null);

                boolean legend = true;
                boolean tooltips = false;
                boolean urls = false;

                /* get the chart */
                JFreeChart chart = ChartFactory.createPieChart3D("Gender", dataset,
                        legend, tooltips, urls);

                final PiePlot3D plot = (PiePlot3D) chart.getPlot();
                plot.setStartAngle(90);
                plot.setForegroundAlpha(0.60f);
                plot.setInteriorGap(0.02);

                PieSectionLabelGenerator labels = new StandardPieSectionLabelGenerator(
                        "{0}: ({2})", new DecimalFormat("0"), new DecimalFormat("0%"));
                plot.setLabelGenerator(labels);

                chart.setBorderPaint(Color.GREEN);
                chart.setBorderStroke(new BasicStroke(5.0f));
                chart.setBorderVisible(true);

                ChartUtilities.writeChartAsPNG(outputStream, chart, width, height);
                break;
            }
            case "racedemographics": {
                DemographicData demographicData
                        = (DemographicData) session.getAttribute("raceDemographicsGraphData");
                DefaultPieDataset dataset = new DefaultPieDataset();

                /* add the data */
                dataset.setValue("White", demographicData.getPercentWhite());
                dataset.setValue("African American", demographicData.getPercentAfricanAmerican());
                dataset.setValue("Asian/Pacific Islander", demographicData.getPercentAsian());
                dataset.setValue("American Indian/Alaska Native", demographicData.getPercentIndian());
                dataset.setValue("Hispanic", demographicData.getPercentHispanic());
                dataset.setValue("Middle Eastern", demographicData.getPercentMiddleEastern());
                dataset.setValue("Other", demographicData.getPercentOther());

                /* remove reference */
                session.setAttribute("raceDemographicsGraphData", null);

                boolean legend = true;
                boolean tooltips = false;
                boolean urls = false;

                /* get the chart */
                JFreeChart chart = ChartFactory.createPieChart3D("Race", dataset,
                        legend, tooltips, urls);

                final PiePlot3D plot = (PiePlot3D) chart.getPlot();
                plot.setStartAngle(90);
                plot.setForegroundAlpha(0.60f);
                plot.setInteriorGap(0.02);

                PieSectionLabelGenerator labels = new StandardPieSectionLabelGenerator(
                        "{0}: ({2})", new DecimalFormat("0"), new DecimalFormat("0%"));
                plot.setLabelGenerator(labels);

                chart.setBorderPaint(Color.GREEN);
                chart.setBorderStroke(new BasicStroke(5.0f));
                chart.setBorderVisible(true);

                ChartUtilities.writeChartAsPNG(outputStream, chart, width, height);
                break;
            }
            case "lasta1c": {
                Stats glycemicStats
                        = (Stats) session.getAttribute("lastA1cData");
                HistogramDataset dataset = new HistogramDataset();
                ArrayList<CategoricalValue> lastA1cValues = new ArrayList<>();

                if (glycemicStats.getGroups() != null) {
                    int i;
                    for (i = 0; i < glycemicStats.getGroups().size(); i++) {
                        if (glycemicStats.getGroups().get(i) != null) {
                            lastA1cValues.addAll(glycemicStats.getGroups().get(i));
                        }
                    }
                }
                if (lastA1cValues.size() > 0) {
                    double[] vector = new double[lastA1cValues.size()];

                    for (int i = 0; i < vector.length; i++) {
                        vector[i] = lastA1cValues.get(i).getValue().doubleValue();
                    }

                    /* add the data */
                    dataset.addSeries("number of patients", vector, 15);

                    /* remove reference */
                    session.setAttribute("lastA1cData", null);

                    boolean legend = true;
                    boolean tooltips = false;
                    boolean urls = false;

                    /* get the chart */
                    JFreeChart chart = ChartFactory.createHistogram(
                            "Most Recent A1C Values", "last A1C(%)",
                            "number of patients", dataset, PlotOrientation.VERTICAL,
                            legend, tooltips, urls);

                    chart.setBorderPaint(Color.GREEN);
                    chart.setBorderStroke(new BasicStroke(5.0f));
                    chart.setBorderVisible(true);

                    XYPlot plot = chart.getXYPlot();

                    final XYBarRenderer renderer = (XYBarRenderer) plot.getRenderer();

                    /* creating a shadow */
                    renderer.setShadowXOffset(4.0);
                    renderer.setShadowYOffset(1.5);
                    renderer.setShadowVisible(true);

                    ChartUtilities.writeChartAsPNG(outputStream, chart, width, height);
                }
                break;
            }
            case "lasta1cbyclassattendance": {
                final int TOP_GROUP_INDEX = 4;
                Stats glycemicStats
                        = (Stats) session.getAttribute("lastA1cByClassData");

                DefaultBoxAndWhiskerCategoryDataset dataset = new DefaultBoxAndWhiskerCategoryDataset();

                if (glycemicStats.getGroups() != null) {
                    for (int i = 0; i < glycemicStats.getGroups().size(); i++) {
                        if ((glycemicStats.getGroups().get(i) != null)
                                && (!glycemicStats.getGroups().get(i).isEmpty())) {
                            List values = new ArrayList();
                            for (CategoricalValue cv : glycemicStats.getGroups().get(i)) {
                                values.add(cv.getValue());
                            }
                            if (i == TOP_GROUP_INDEX) {
                                dataset.add(BoxAndWhiskerCalculator.calculateBoxAndWhiskerStatistics(values),
                                        "last A1C(%)", "5 or more");
                            } else {
                                dataset.add(BoxAndWhiskerCalculator.calculateBoxAndWhiskerStatistics(values),
                                        "last A1C(%)", i + 1);
                            }
                        }
                    }
                }

                /* remove reference */
                session.setAttribute("lastA1cByClassData", null);

                CategoryAxis domainAxis = new CategoryAxis("number of classes attended");
                NumberAxis rangeAxis = new NumberAxis("last A1C(%)");
                BoxAndWhiskerRenderer renderer = new BoxAndWhiskerRenderer();
                CategoryPlot plot = new CategoryPlot(
                        dataset, domainAxis, rangeAxis, renderer
                );
                JFreeChart chart = new JFreeChart("Most Recent A1C by Classes Attended", plot);
                renderer.setMeanVisible(false);

                chart.setBorderPaint(Color.GREEN);
                chart.setBorderStroke(new BasicStroke(5.0f));
                chart.setBorderVisible(true);

                ChartUtilities.writeChartAsPNG(outputStream, chart, width, height);
                break;
            }
            case "lastbmimales": {
                Stats bmiMalesStats
                        = (Stats) session.getAttribute("lastBmiMalesData");
                HistogramDataset dataset = new HistogramDataset();
                ArrayList<CategoricalValue> lastBmiMalesValues = new ArrayList<>();
                if ((bmiMalesStats.getGroups() != null)
                        && (!bmiMalesStats.getGroups().isEmpty())) {
                    for (int i = 0; i < bmiMalesStats.getGroups().size(); i++) {
                        if (bmiMalesStats.getGroups().get(i) != null) {
                            lastBmiMalesValues.addAll(bmiMalesStats.getGroups().get(i));
                        }
                    }
                }
                if (lastBmiMalesValues.size() > 0) {
                    double[] vector = new double[lastBmiMalesValues.size()];

                    for (int i = 0; i < vector.length; i++) {
                        vector[i] = lastBmiMalesValues.get(i).getValue().doubleValue();
                    }

                    /* add the data */
                    dataset.addSeries("number of patients", vector, 15);

                    /* remove reference */
                    session.setAttribute("lastBmiMalesData", null);

                    boolean legend = true;
                    boolean tooltips = false;
                    boolean urls = false;

                    /* get the chart */
                    JFreeChart chart = ChartFactory.createHistogram(
                            "Most Recent BMI Values for Males", "last BMI",
                            "number of patients", dataset, PlotOrientation.VERTICAL,
                            legend, tooltips, urls);

                    chart.setBorderPaint(Color.GREEN);
                    chart.setBorderStroke(new BasicStroke(5.0f));
                    chart.setBorderVisible(true);

                    XYPlot plot = chart.getXYPlot();

                    final XYBarRenderer renderer = (XYBarRenderer) plot.getRenderer();

                    /* creating a shadow */
                    renderer.setShadowXOffset(4.0);
                    renderer.setShadowYOffset(1.5);
                    renderer.setShadowVisible(true);

                    ChartUtilities.writeChartAsPNG(outputStream, chart, width, height);
                }
                break;
            }
            case "lastbmifemales": {
                Stats bmiFemalesStats
                        = (Stats) session.getAttribute("lastBmiFemalesData");
                HistogramDataset dataset = new HistogramDataset();
                ArrayList<CategoricalValue> lastBmiFemalesValues = new ArrayList<>();
                if ((bmiFemalesStats.getGroups() != null)
                        && (!bmiFemalesStats.getGroups().isEmpty())) {
                    for (int i = 0; i < bmiFemalesStats.getGroups().size(); i++) {
                        if (bmiFemalesStats.getGroups().get(i) != null) {
                            lastBmiFemalesValues.addAll(bmiFemalesStats.getGroups().get(i));
                        }
                    }
                }
                if (lastBmiFemalesValues.size() > 0) {
                    double[] vector = new double[lastBmiFemalesValues.size()];

                    for (int i = 0; i < vector.length; i++) {
                        vector[i] = lastBmiFemalesValues.get(i).getValue().doubleValue();
                    }

                    /* add the data */
                    dataset.addSeries("number of patients", vector, 15);

                    /* remove reference */
                    session.setAttribute("lastBmiFemalesData", null);

                    boolean legend = true;
                    boolean tooltips = false;
                    boolean urls = false;

                    /* get the chart */
                    JFreeChart chart = ChartFactory.createHistogram(
                            "Most Recent BMI Values for Females", "last BMI",
                            "number of patients", dataset, PlotOrientation.VERTICAL,
                            legend, tooltips, urls);

                    chart.setBorderPaint(Color.GREEN);
                    chart.setBorderStroke(new BasicStroke(5.0f));
                    chart.setBorderVisible(true);

                    XYPlot plot = chart.getXYPlot();

                    final XYBarRenderer renderer = (XYBarRenderer) plot.getRenderer();

                    /* creating a shadow */
                    renderer.setShadowXOffset(4.0);
                    renderer.setShadowYOffset(1.5);
                    renderer.setShadowVisible(true);

                    ChartUtilities.writeChartAsPNG(outputStream, chart, width, height);
                }
                break;
            }
            case "lastbmimalesbyclassattendance": {
                final int TOP_GROUP_INDEX = 4;
                Stats bmiMalesStats
                        = (Stats) session.getAttribute("lastBmiMalesByClassData");

                DefaultBoxAndWhiskerCategoryDataset dataset = new DefaultBoxAndWhiskerCategoryDataset();

                if (bmiMalesStats.getGroups() != null) {
                    for (int i = 0; i < bmiMalesStats.getGroups().size(); i++) {
                        if ((bmiMalesStats.getGroups().get(i) != null)
                                && (!bmiMalesStats.getGroups().get(i).isEmpty())) {
                            List values = new ArrayList();
                            for (CategoricalValue cv : bmiMalesStats.getGroups().get(i)) {
                                values.add(cv.getValue());
                            }
                            if (i == TOP_GROUP_INDEX) {
                                dataset.add(BoxAndWhiskerCalculator.calculateBoxAndWhiskerStatistics(values),
                                        "last BMI (males)", "5 or more");
                            } else {
                                dataset.add(BoxAndWhiskerCalculator.calculateBoxAndWhiskerStatistics(values),
                                        "last BMI (males)", i + 1);
                            }
                        }
                    }
                }

                /* remove reference */
                session.setAttribute("lastBmiMalesByClassData", null);

                CategoryAxis domainAxis = new CategoryAxis("number of classes attended");
                NumberAxis rangeAxis = new NumberAxis("last BMI (males)");
                BoxAndWhiskerRenderer renderer = new BoxAndWhiskerRenderer();
                CategoryPlot plot = new CategoryPlot(
                        dataset, domainAxis, rangeAxis, renderer
                );
                JFreeChart chart = new JFreeChart("Most Recent BMI for Males by Classes Attended", plot);
                renderer.setMeanVisible(false);

                chart.setBorderPaint(Color.GREEN);
                chart.setBorderStroke(new BasicStroke(5.0f));
                chart.setBorderVisible(true);

                ChartUtilities.writeChartAsPNG(outputStream, chart, width, height);
                break;
            }
            case "lastbmifemalesbyclassattendance": {
                final int TOP_GROUP_INDEX = 4;
                Stats bmiFemalesStats
                        = (Stats) session.getAttribute("lastBmiFemalesByClassData");

                DefaultBoxAndWhiskerCategoryDataset dataset = new DefaultBoxAndWhiskerCategoryDataset();

                if (bmiFemalesStats.getGroups() != null) {
                    for (int i = 0; i < bmiFemalesStats.getGroups().size(); i++) {
                        if ((bmiFemalesStats.getGroups().get(i) != null)
                                && (!bmiFemalesStats.getGroups().get(i).isEmpty())) {
                            List values = new ArrayList();
                            for (CategoricalValue cv : bmiFemalesStats.getGroups().get(i)) {
                                values.add(cv.getValue());
                            }
                            if (i == TOP_GROUP_INDEX) {
                                dataset.add(BoxAndWhiskerCalculator.calculateBoxAndWhiskerStatistics(values),
                                        "last BMI (females)", "5 or more");
                            } else {
                                dataset.add(BoxAndWhiskerCalculator.calculateBoxAndWhiskerStatistics(values),
                                        "last BMI (females)", i + 1);
                            }
                        }
                    }
                }

                /* remove reference */
                session.setAttribute("lastBmiFemalesByClassData", null);

                CategoryAxis domainAxis = new CategoryAxis("number of classes attended");
                NumberAxis rangeAxis = new NumberAxis("last BMI (females)");
                BoxAndWhiskerRenderer renderer = new BoxAndWhiskerRenderer();
                CategoryPlot plot = new CategoryPlot(
                        dataset, domainAxis, rangeAxis, renderer
                );
                JFreeChart chart = new JFreeChart("Most Recent BMI for Females by Classes Attended", plot);
                renderer.setMeanVisible(false);

                chart.setBorderPaint(Color.GREEN);
                chart.setBorderStroke(new BasicStroke(5.0f));
                chart.setBorderVisible(true);

                ChartUtilities.writeChartAsPNG(outputStream, chart, width, height);
                break;
            }
            case "lasta1cbytreatment": {
                final int FIRST_INDEX = 0;
                Stats glycemicStats
                        = (Stats) session.getAttribute("lastA1cByTreatment");

                DefaultBoxAndWhiskerCategoryDataset dataset = new DefaultBoxAndWhiskerCategoryDataset();

                if (glycemicStats.getGroups() != null) {
                    for (int i = 0; i < glycemicStats.getGroups().size(); i++) {
                        if ((glycemicStats.getGroups().get(i) != null)
                                && (!glycemicStats.getGroups().get(i).isEmpty())) {
                            String category = glycemicStats.getGroups().get(i)
                                    .get(FIRST_INDEX) != null ? glycemicStats.getGroups()
                                            .get(i).get(FIRST_INDEX).getCategory() : "";
                            List values = new ArrayList();
                            for (CategoricalValue cv : glycemicStats.getGroups().get(i)) {
                                values.add(cv.getValue());
                            }
                            dataset.add(BoxAndWhiskerCalculator.calculateBoxAndWhiskerStatistics(values),
                                    "last A1C(%)", category);
                        }
                    }
                }

                /* remove reference */
                session.setAttribute("lastA1cByTreatment", null);

                CategoryAxis domainAxis = new CategoryAxis("treatment class");
                NumberAxis rangeAxis = new NumberAxis("last A1C(%)");
                BoxAndWhiskerRenderer renderer = new BoxAndWhiskerRenderer();
                CategoryPlot plot = new CategoryPlot(
                        dataset, domainAxis, rangeAxis, renderer
                );
                JFreeChart chart = new JFreeChart("Most Recent A1C by Treatment Class", plot);
                renderer.setMeanVisible(false);

                chart.setBorderPaint(Color.GREEN);
                chart.setBorderStroke(new BasicStroke(5.0f));
                chart.setBorderVisible(true);

                ChartUtilities.writeChartAsPNG(outputStream, chart, width, height);
                break;
            }
            case "treatmentclasscounts": {
                int treatmentClassCountsIndex = 1;

                Stats treatmentData
                        = (Stats) session.getAttribute("classCountsTreatmentStats");
                DefaultPieDataset dataset = new DefaultPieDataset();

                /* add the data */
                dataset.setValue(treatmentData.getGroups().get(treatmentClassCountsIndex).get(TREATMENT_CLASS_ZERO_INDEX).getCategory(),
                        treatmentData.getGroups().get(treatmentClassCountsIndex).get(TREATMENT_CLASS_ZERO_INDEX).getValue());
                dataset.setValue(treatmentData.getGroups().get(treatmentClassCountsIndex).get(TREATMENT_CLASS_ONE_INDEX).getCategory(),
                        treatmentData.getGroups().get(treatmentClassCountsIndex).get(TREATMENT_CLASS_ONE_INDEX).getValue());
                dataset.setValue(treatmentData.getGroups().get(treatmentClassCountsIndex).get(TREATMENT_CLASS_TWO_INDEX).getCategory(),
                        treatmentData.getGroups().get(treatmentClassCountsIndex).get(TREATMENT_CLASS_TWO_INDEX).getValue());
                dataset.setValue(treatmentData.getGroups().get(treatmentClassCountsIndex).get(TREATMENT_CLASS_THREE_INDEX).getCategory(),
                        treatmentData.getGroups().get(treatmentClassCountsIndex).get(TREATMENT_CLASS_THREE_INDEX).getValue());
                dataset.setValue(treatmentData.getGroups().get(treatmentClassCountsIndex).get(TREATMENT_CLASS_FOUR_INDEX).getCategory(),
                        treatmentData.getGroups().get(treatmentClassCountsIndex).get(TREATMENT_CLASS_FOUR_INDEX).getValue());
                dataset.setValue(treatmentData.getGroups().get(treatmentClassCountsIndex).get(TREATMENT_CLASS_FIVE_INDEX).getCategory(),
                        treatmentData.getGroups().get(treatmentClassCountsIndex).get(TREATMENT_CLASS_FIVE_INDEX).getValue());
                dataset.setValue(treatmentData.getGroups().get(treatmentClassCountsIndex).get(TREATMENT_CLASS_UNKNOWN_INDEX).getCategory(),
                        treatmentData.getGroups().get(treatmentClassCountsIndex).get(TREATMENT_CLASS_UNKNOWN_INDEX).getValue());

                /* remove reference */
                session.setAttribute("classCountsTreatmentStats", null);

                boolean legend = true;
                boolean tooltips = false;
                boolean urls = false;

                /* get the chart */
                JFreeChart chart = ChartFactory.createPieChart3D("Treatment Class", dataset,
                        legend, tooltips, urls);

                final PiePlot3D plot = (PiePlot3D) chart.getPlot();
                plot.setStartAngle(90);
                plot.setForegroundAlpha(0.60f);
                plot.setInteriorGap(0.02);

                PieSectionLabelGenerator labels = new StandardPieSectionLabelGenerator(
                        "{0}: {1} ({2})", new DecimalFormat("0"), new DecimalFormat("0%"));
                plot.setLabelGenerator(labels);

                chart.setBorderPaint(Color.GREEN);
                chart.setBorderStroke(new BasicStroke(5.0f));
                chart.setBorderVisible(true);

                ChartUtilities.writeChartAsPNG(outputStream, chart, width, height);
                break;
            }
            case "treatmentgenderclasscounts": {
                int maleClassCountsIndex = 2;
                int femaleClassCountsIndex = 3;

                Stats treatmentData
                        = (Stats) session.getAttribute("genderClassCountsTreatmentStats");
                final DefaultCategoryDataset dataset
                        = new DefaultCategoryDataset();

                /* females data */
                dataset.addValue(treatmentData.getGroups().get(femaleClassCountsIndex).get(TREATMENT_CLASS_ZERO_INDEX).getValue(),
                        "Female", treatmentData.getGroups().get(femaleClassCountsIndex).get(TREATMENT_CLASS_ZERO_INDEX).getCategory());
                dataset.addValue(treatmentData.getGroups().get(femaleClassCountsIndex).get(TREATMENT_CLASS_ONE_INDEX).getValue(),
                        "Female", treatmentData.getGroups().get(femaleClassCountsIndex).get(TREATMENT_CLASS_ONE_INDEX).getCategory());
                dataset.addValue(treatmentData.getGroups().get(femaleClassCountsIndex).get(TREATMENT_CLASS_TWO_INDEX).getValue(),
                        "Female", treatmentData.getGroups().get(femaleClassCountsIndex).get(TREATMENT_CLASS_TWO_INDEX).getCategory());
                dataset.addValue(treatmentData.getGroups().get(femaleClassCountsIndex).get(TREATMENT_CLASS_THREE_INDEX).getValue(),
                        "Female", treatmentData.getGroups().get(femaleClassCountsIndex).get(TREATMENT_CLASS_THREE_INDEX).getCategory());
                dataset.addValue(treatmentData.getGroups().get(femaleClassCountsIndex).get(TREATMENT_CLASS_FOUR_INDEX).getValue(),
                        "Female", treatmentData.getGroups().get(femaleClassCountsIndex).get(TREATMENT_CLASS_FOUR_INDEX).getCategory());
                dataset.addValue(treatmentData.getGroups().get(femaleClassCountsIndex).get(TREATMENT_CLASS_FIVE_INDEX).getValue(),
                        "Female", treatmentData.getGroups().get(femaleClassCountsIndex).get(TREATMENT_CLASS_FIVE_INDEX).getCategory());
                dataset.addValue(treatmentData.getGroups().get(femaleClassCountsIndex).get(TREATMENT_CLASS_UNKNOWN_INDEX).getValue(),
                        "Female", treatmentData.getGroups().get(femaleClassCountsIndex).get(TREATMENT_CLASS_UNKNOWN_INDEX).getCategory());

                /* males data */
                dataset.addValue(treatmentData.getGroups().get(maleClassCountsIndex).get(TREATMENT_CLASS_ZERO_INDEX).getValue(),
                        "Male", treatmentData.getGroups().get(maleClassCountsIndex).get(TREATMENT_CLASS_ZERO_INDEX).getCategory());
                dataset.addValue(treatmentData.getGroups().get(maleClassCountsIndex).get(TREATMENT_CLASS_ONE_INDEX).getValue(),
                        "Male", treatmentData.getGroups().get(maleClassCountsIndex).get(TREATMENT_CLASS_ONE_INDEX).getCategory());
                dataset.addValue(treatmentData.getGroups().get(maleClassCountsIndex).get(TREATMENT_CLASS_TWO_INDEX).getValue(),
                        "Male", treatmentData.getGroups().get(maleClassCountsIndex).get(TREATMENT_CLASS_TWO_INDEX).getCategory());
                dataset.addValue(treatmentData.getGroups().get(maleClassCountsIndex).get(TREATMENT_CLASS_THREE_INDEX).getValue(),
                        "Male", treatmentData.getGroups().get(maleClassCountsIndex).get(TREATMENT_CLASS_THREE_INDEX).getCategory());
                dataset.addValue(treatmentData.getGroups().get(maleClassCountsIndex).get(TREATMENT_CLASS_FOUR_INDEX).getValue(),
                        "Male", treatmentData.getGroups().get(maleClassCountsIndex).get(TREATMENT_CLASS_FOUR_INDEX).getCategory());
                dataset.addValue(treatmentData.getGroups().get(maleClassCountsIndex).get(TREATMENT_CLASS_FIVE_INDEX).getValue(),
                        "Male", treatmentData.getGroups().get(maleClassCountsIndex).get(TREATMENT_CLASS_FIVE_INDEX).getCategory());
                dataset.addValue(treatmentData.getGroups().get(maleClassCountsIndex).get(TREATMENT_CLASS_UNKNOWN_INDEX).getValue(),
                        "Male", treatmentData.getGroups().get(maleClassCountsIndex).get(TREATMENT_CLASS_UNKNOWN_INDEX).getCategory());

                /* remove reference */
                session.setAttribute("genderClassCountsTreatmentStats", null);

                boolean legend = true;
                boolean tooltips = false;
                boolean urls = false;

                JFreeChart barChart = ChartFactory.createBarChart(
                        "Treatment Class by Gender",
                        "Treatment Class",
                        "Number of Patients",
                        dataset,
                        PlotOrientation.VERTICAL,
                        legend, tooltips, urls);

                barChart.setBorderPaint(Color.GREEN);
                barChart.setBorderStroke(new BasicStroke(5.0f));
                barChart.setBorderVisible(true);

                CategoryPlot plot = barChart.getCategoryPlot();

                final BarRenderer renderer = (BarRenderer) plot.getRenderer();

                /* creating a shadow */
                renderer.setShadowXOffset(4.0);
                renderer.setShadowYOffset(1.5);
                renderer.setShadowVisible(true);

                ChartUtilities.writeChartAsPNG(outputStream, barChart, width, height);

                break;
            }
            case "treatmentraceclasscounts": {
                int whiteClassCountsIndex = 4;
                int africanAmericanClassCountsIndex = 5;
                int asianPacificIslanderClassCountsIndex = 6;
                int americanIndianAlaskaNativeClassCountsIndex = 7;
                int hispanicClassCountsIndex = 8;
                int middleEasternClassCountsIndex = 9;
                int otherClassCountsIndex = 10;

                Stats treatmentData
                        = (Stats) session.getAttribute("raceClassCountsTreatmentStats");
                final DefaultCategoryDataset dataset
                        = new DefaultCategoryDataset();

                /* whites data */
                dataset.addValue(treatmentData.getGroups().get(whiteClassCountsIndex).get(TREATMENT_CLASS_ZERO_INDEX).getValue(),
                        "White", treatmentData.getGroups().get(whiteClassCountsIndex).get(TREATMENT_CLASS_ZERO_INDEX).getCategory());
                dataset.addValue(treatmentData.getGroups().get(whiteClassCountsIndex).get(TREATMENT_CLASS_ONE_INDEX).getValue(),
                        "White", treatmentData.getGroups().get(whiteClassCountsIndex).get(TREATMENT_CLASS_ONE_INDEX).getCategory());
                dataset.addValue(treatmentData.getGroups().get(whiteClassCountsIndex).get(TREATMENT_CLASS_TWO_INDEX).getValue(),
                        "White", treatmentData.getGroups().get(whiteClassCountsIndex).get(TREATMENT_CLASS_TWO_INDEX).getCategory());
                dataset.addValue(treatmentData.getGroups().get(whiteClassCountsIndex).get(TREATMENT_CLASS_THREE_INDEX).getValue(),
                        "White", treatmentData.getGroups().get(whiteClassCountsIndex).get(TREATMENT_CLASS_THREE_INDEX).getCategory());
                dataset.addValue(treatmentData.getGroups().get(whiteClassCountsIndex).get(TREATMENT_CLASS_FOUR_INDEX).getValue(),
                        "White", treatmentData.getGroups().get(whiteClassCountsIndex).get(TREATMENT_CLASS_FOUR_INDEX).getCategory());
                dataset.addValue(treatmentData.getGroups().get(whiteClassCountsIndex).get(TREATMENT_CLASS_FIVE_INDEX).getValue(),
                        "White", treatmentData.getGroups().get(whiteClassCountsIndex).get(TREATMENT_CLASS_FIVE_INDEX).getCategory());
                dataset.addValue(treatmentData.getGroups().get(whiteClassCountsIndex).get(TREATMENT_CLASS_UNKNOWN_INDEX).getValue(),
                        "White", treatmentData.getGroups().get(whiteClassCountsIndex).get(TREATMENT_CLASS_UNKNOWN_INDEX).getCategory());

                /* African Americans data */
                dataset.addValue(treatmentData.getGroups().get(africanAmericanClassCountsIndex).get(TREATMENT_CLASS_ZERO_INDEX).getValue(),
                        "African American", treatmentData.getGroups().get(africanAmericanClassCountsIndex).get(TREATMENT_CLASS_ZERO_INDEX).getCategory());
                dataset.addValue(treatmentData.getGroups().get(africanAmericanClassCountsIndex).get(TREATMENT_CLASS_ONE_INDEX).getValue(),
                        "African American", treatmentData.getGroups().get(africanAmericanClassCountsIndex).get(TREATMENT_CLASS_ONE_INDEX).getCategory());
                dataset.addValue(treatmentData.getGroups().get(africanAmericanClassCountsIndex).get(TREATMENT_CLASS_TWO_INDEX).getValue(),
                        "African American", treatmentData.getGroups().get(africanAmericanClassCountsIndex).get(TREATMENT_CLASS_TWO_INDEX).getCategory());
                dataset.addValue(treatmentData.getGroups().get(africanAmericanClassCountsIndex).get(TREATMENT_CLASS_THREE_INDEX).getValue(),
                        "African American", treatmentData.getGroups().get(africanAmericanClassCountsIndex).get(TREATMENT_CLASS_THREE_INDEX).getCategory());
                dataset.addValue(treatmentData.getGroups().get(africanAmericanClassCountsIndex).get(TREATMENT_CLASS_FOUR_INDEX).getValue(),
                        "African American", treatmentData.getGroups().get(africanAmericanClassCountsIndex).get(TREATMENT_CLASS_FOUR_INDEX).getCategory());
                dataset.addValue(treatmentData.getGroups().get(africanAmericanClassCountsIndex).get(TREATMENT_CLASS_FIVE_INDEX).getValue(),
                        "African American", treatmentData.getGroups().get(africanAmericanClassCountsIndex).get(TREATMENT_CLASS_FIVE_INDEX).getCategory());
                dataset.addValue(treatmentData.getGroups().get(africanAmericanClassCountsIndex).get(TREATMENT_CLASS_UNKNOWN_INDEX).getValue(),
                        "African American", treatmentData.getGroups().get(africanAmericanClassCountsIndex).get(TREATMENT_CLASS_UNKNOWN_INDEX).getCategory());

                /* Asians/Pacific Islanders data */
                dataset.addValue(treatmentData.getGroups().get(asianPacificIslanderClassCountsIndex).get(TREATMENT_CLASS_ZERO_INDEX).getValue(),
                        "Asians/Pacific Islander", treatmentData.getGroups().get(asianPacificIslanderClassCountsIndex).get(TREATMENT_CLASS_ZERO_INDEX).getCategory());
                dataset.addValue(treatmentData.getGroups().get(asianPacificIslanderClassCountsIndex).get(TREATMENT_CLASS_ONE_INDEX).getValue(),
                        "Asians/Pacific Islander", treatmentData.getGroups().get(asianPacificIslanderClassCountsIndex).get(TREATMENT_CLASS_ONE_INDEX).getCategory());
                dataset.addValue(treatmentData.getGroups().get(asianPacificIslanderClassCountsIndex).get(TREATMENT_CLASS_TWO_INDEX).getValue(),
                        "Asians/Pacific Islander", treatmentData.getGroups().get(asianPacificIslanderClassCountsIndex).get(TREATMENT_CLASS_TWO_INDEX).getCategory());
                dataset.addValue(treatmentData.getGroups().get(asianPacificIslanderClassCountsIndex).get(TREATMENT_CLASS_THREE_INDEX).getValue(),
                        "Asians/Pacific Islander", treatmentData.getGroups().get(asianPacificIslanderClassCountsIndex).get(TREATMENT_CLASS_THREE_INDEX).getCategory());
                dataset.addValue(treatmentData.getGroups().get(asianPacificIslanderClassCountsIndex).get(TREATMENT_CLASS_FOUR_INDEX).getValue(),
                        "Asians/Pacific Islander", treatmentData.getGroups().get(asianPacificIslanderClassCountsIndex).get(TREATMENT_CLASS_FOUR_INDEX).getCategory());
                dataset.addValue(treatmentData.getGroups().get(asianPacificIslanderClassCountsIndex).get(TREATMENT_CLASS_FIVE_INDEX).getValue(),
                        "Asians/Pacific Islander", treatmentData.getGroups().get(asianPacificIslanderClassCountsIndex).get(TREATMENT_CLASS_FIVE_INDEX).getCategory());
                dataset.addValue(treatmentData.getGroups().get(asianPacificIslanderClassCountsIndex).get(TREATMENT_CLASS_UNKNOWN_INDEX).getValue(),
                        "Asians/Pacific Islander", treatmentData.getGroups().get(asianPacificIslanderClassCountsIndex).get(TREATMENT_CLASS_UNKNOWN_INDEX).getCategory());

                /* American Indians/Alaska Natives data */
                dataset.addValue(treatmentData.getGroups().get(americanIndianAlaskaNativeClassCountsIndex).get(TREATMENT_CLASS_ZERO_INDEX).getValue(),
                        "American Indian/Alaska Native", treatmentData.getGroups().get(americanIndianAlaskaNativeClassCountsIndex).get(TREATMENT_CLASS_ZERO_INDEX).getCategory());
                dataset.addValue(treatmentData.getGroups().get(americanIndianAlaskaNativeClassCountsIndex).get(TREATMENT_CLASS_ONE_INDEX).getValue(),
                        "American Indian/Alaska Native", treatmentData.getGroups().get(americanIndianAlaskaNativeClassCountsIndex).get(TREATMENT_CLASS_ONE_INDEX).getCategory());
                dataset.addValue(treatmentData.getGroups().get(americanIndianAlaskaNativeClassCountsIndex).get(TREATMENT_CLASS_TWO_INDEX).getValue(),
                        "American Indian/Alaska Native", treatmentData.getGroups().get(americanIndianAlaskaNativeClassCountsIndex).get(TREATMENT_CLASS_TWO_INDEX).getCategory());
                dataset.addValue(treatmentData.getGroups().get(americanIndianAlaskaNativeClassCountsIndex).get(TREATMENT_CLASS_THREE_INDEX).getValue(),
                        "American Indian/Alaska Native", treatmentData.getGroups().get(americanIndianAlaskaNativeClassCountsIndex).get(TREATMENT_CLASS_THREE_INDEX).getCategory());
                dataset.addValue(treatmentData.getGroups().get(americanIndianAlaskaNativeClassCountsIndex).get(TREATMENT_CLASS_FOUR_INDEX).getValue(),
                        "American Indian/Alaska Native", treatmentData.getGroups().get(americanIndianAlaskaNativeClassCountsIndex).get(TREATMENT_CLASS_FOUR_INDEX).getCategory());
                dataset.addValue(treatmentData.getGroups().get(americanIndianAlaskaNativeClassCountsIndex).get(TREATMENT_CLASS_FIVE_INDEX).getValue(),
                        "American Indian/Alaska Native", treatmentData.getGroups().get(americanIndianAlaskaNativeClassCountsIndex).get(TREATMENT_CLASS_FIVE_INDEX).getCategory());
                dataset.addValue(treatmentData.getGroups().get(americanIndianAlaskaNativeClassCountsIndex).get(TREATMENT_CLASS_UNKNOWN_INDEX).getValue(),
                        "American Indian/Alaska Native", treatmentData.getGroups().get(americanIndianAlaskaNativeClassCountsIndex).get(TREATMENT_CLASS_UNKNOWN_INDEX).getCategory());

                /* Hispanics data */
                dataset.addValue(treatmentData.getGroups().get(hispanicClassCountsIndex).get(TREATMENT_CLASS_ZERO_INDEX).getValue(),
                        "Hispanic", treatmentData.getGroups().get(hispanicClassCountsIndex).get(TREATMENT_CLASS_ZERO_INDEX).getCategory());
                dataset.addValue(treatmentData.getGroups().get(hispanicClassCountsIndex).get(TREATMENT_CLASS_ONE_INDEX).getValue(),
                        "Hispanic", treatmentData.getGroups().get(hispanicClassCountsIndex).get(TREATMENT_CLASS_ONE_INDEX).getCategory());
                dataset.addValue(treatmentData.getGroups().get(hispanicClassCountsIndex).get(TREATMENT_CLASS_TWO_INDEX).getValue(),
                        "Hispanic", treatmentData.getGroups().get(hispanicClassCountsIndex).get(TREATMENT_CLASS_TWO_INDEX).getCategory());
                dataset.addValue(treatmentData.getGroups().get(hispanicClassCountsIndex).get(TREATMENT_CLASS_THREE_INDEX).getValue(),
                        "Hispanic", treatmentData.getGroups().get(hispanicClassCountsIndex).get(TREATMENT_CLASS_THREE_INDEX).getCategory());
                dataset.addValue(treatmentData.getGroups().get(hispanicClassCountsIndex).get(TREATMENT_CLASS_FOUR_INDEX).getValue(),
                        "Hispanic", treatmentData.getGroups().get(hispanicClassCountsIndex).get(TREATMENT_CLASS_FOUR_INDEX).getCategory());
                dataset.addValue(treatmentData.getGroups().get(hispanicClassCountsIndex).get(TREATMENT_CLASS_FIVE_INDEX).getValue(),
                        "Hispanic", treatmentData.getGroups().get(hispanicClassCountsIndex).get(TREATMENT_CLASS_FIVE_INDEX).getCategory());
                dataset.addValue(treatmentData.getGroups().get(hispanicClassCountsIndex).get(TREATMENT_CLASS_UNKNOWN_INDEX).getValue(),
                        "Hispanic", treatmentData.getGroups().get(hispanicClassCountsIndex).get(TREATMENT_CLASS_UNKNOWN_INDEX).getCategory());

                /* Middle Easterners data */
                dataset.addValue(treatmentData.getGroups().get(middleEasternClassCountsIndex).get(TREATMENT_CLASS_ZERO_INDEX).getValue(),
                        "Middle Eastern", treatmentData.getGroups().get(middleEasternClassCountsIndex).get(TREATMENT_CLASS_ZERO_INDEX).getCategory());
                dataset.addValue(treatmentData.getGroups().get(middleEasternClassCountsIndex).get(TREATMENT_CLASS_ONE_INDEX).getValue(),
                        "Middle Eastern", treatmentData.getGroups().get(middleEasternClassCountsIndex).get(TREATMENT_CLASS_ONE_INDEX).getCategory());
                dataset.addValue(treatmentData.getGroups().get(middleEasternClassCountsIndex).get(TREATMENT_CLASS_TWO_INDEX).getValue(),
                        "Middle Eastern", treatmentData.getGroups().get(middleEasternClassCountsIndex).get(TREATMENT_CLASS_TWO_INDEX).getCategory());
                dataset.addValue(treatmentData.getGroups().get(middleEasternClassCountsIndex).get(TREATMENT_CLASS_THREE_INDEX).getValue(),
                        "Middle Eastern", treatmentData.getGroups().get(middleEasternClassCountsIndex).get(TREATMENT_CLASS_THREE_INDEX).getCategory());
                dataset.addValue(treatmentData.getGroups().get(middleEasternClassCountsIndex).get(TREATMENT_CLASS_FOUR_INDEX).getValue(),
                        "Middle Eastern", treatmentData.getGroups().get(middleEasternClassCountsIndex).get(TREATMENT_CLASS_FOUR_INDEX).getCategory());
                dataset.addValue(treatmentData.getGroups().get(middleEasternClassCountsIndex).get(TREATMENT_CLASS_FIVE_INDEX).getValue(),
                        "Middle Eastern", treatmentData.getGroups().get(middleEasternClassCountsIndex).get(TREATMENT_CLASS_FIVE_INDEX).getCategory());
                dataset.addValue(treatmentData.getGroups().get(middleEasternClassCountsIndex).get(TREATMENT_CLASS_UNKNOWN_INDEX).getValue(),
                        "Middle Eastern", treatmentData.getGroups().get(middleEasternClassCountsIndex).get(TREATMENT_CLASS_UNKNOWN_INDEX).getCategory());

                /* other ethnicities data */
                dataset.addValue(treatmentData.getGroups().get(otherClassCountsIndex).get(TREATMENT_CLASS_ZERO_INDEX).getValue(),
                        "Other", treatmentData.getGroups().get(otherClassCountsIndex).get(TREATMENT_CLASS_ZERO_INDEX).getCategory());
                dataset.addValue(treatmentData.getGroups().get(otherClassCountsIndex).get(TREATMENT_CLASS_ONE_INDEX).getValue(),
                        "Other", treatmentData.getGroups().get(otherClassCountsIndex).get(TREATMENT_CLASS_ONE_INDEX).getCategory());
                dataset.addValue(treatmentData.getGroups().get(otherClassCountsIndex).get(TREATMENT_CLASS_TWO_INDEX).getValue(),
                        "Other", treatmentData.getGroups().get(otherClassCountsIndex).get(TREATMENT_CLASS_TWO_INDEX).getCategory());
                dataset.addValue(treatmentData.getGroups().get(otherClassCountsIndex).get(TREATMENT_CLASS_THREE_INDEX).getValue(),
                        "Other", treatmentData.getGroups().get(otherClassCountsIndex).get(TREATMENT_CLASS_THREE_INDEX).getCategory());
                dataset.addValue(treatmentData.getGroups().get(otherClassCountsIndex).get(TREATMENT_CLASS_FOUR_INDEX).getValue(),
                        "Other", treatmentData.getGroups().get(otherClassCountsIndex).get(TREATMENT_CLASS_FOUR_INDEX).getCategory());
                dataset.addValue(treatmentData.getGroups().get(otherClassCountsIndex).get(TREATMENT_CLASS_FIVE_INDEX).getValue(),
                        "Other", treatmentData.getGroups().get(otherClassCountsIndex).get(TREATMENT_CLASS_FIVE_INDEX).getCategory());
                dataset.addValue(treatmentData.getGroups().get(otherClassCountsIndex).get(TREATMENT_CLASS_UNKNOWN_INDEX).getValue(),
                        "Other", treatmentData.getGroups().get(otherClassCountsIndex).get(TREATMENT_CLASS_UNKNOWN_INDEX).getCategory());

                /* remove reference */
                session.setAttribute("raceClassCountsTreatmentStats", null);

                boolean legend = true;
                boolean tooltips = false;
                boolean urls = false;

                JFreeChart barChart = ChartFactory.createStackedBarChart(
                        "Treatment Class by Race",
                        "Treatment Class",
                        "Number of Patients",
                        dataset,
                        PlotOrientation.VERTICAL,
                        legend, tooltips, urls);

                barChart.setBorderPaint(Color.GREEN);
                barChart.setBorderStroke(new BasicStroke(5.0f));
                barChart.setBorderVisible(true);

                CategoryPlot plot = barChart.getCategoryPlot();

                final BarRenderer renderer = (BarRenderer) plot.getRenderer();

                /* creating a shadow */
                renderer.setShadowXOffset(4.0);
                renderer.setShadowYOffset(1.5);
                renderer.setShadowVisible(true);

                ChartUtilities.writeChartAsPNG(outputStream, barChart, width, height);

                break;
            }
            default:
                break;
        }
    }
}
