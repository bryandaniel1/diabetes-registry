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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang3.text.WordUtils;
import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import registry.NoteAuthor;
import registry.Patient;
import registry.ProgressNote;
import utility.SessionObjectUtility;

/**
 * This HttpServlet class shows a PDF document containing progress note
 * information.
 *
 * @author Bryan Daniel
 * @version 2, March 16, 2017
 */
public class PDFServlet extends HttpServlet {

    /**
     * The constant for line length
     */
    private static final int NOTE_LINE_LENGTH = 75;

    /**
     * The constant for address line length
     */
    private static final int ADDRESS_LINE_LENGTH = 65;

    /**
     * The constant for minimum vertical position on the page
     */
    private static final int MINIMUM_Y_POSITION = 21;

    /**
     * The constant for maximum vertical position on the page
     */
    private static final int MAXIMUM_Y_POSITION = 750;

    /**
     * The constant for line height
     */
    private static final int LINE_HEIGHT = 20;

    /**
     * The constant for title font size
     */
    private static final int TITLE_FONT_SIZE = 20;

    /**
     * The constant for date font size
     */
    private static final int DATE_FONT_SIZE = 16;

    /**
     * The constant for text font size
     */
    private static final int TEXT_FONT_SIZE = 12;

    /**
     * Serial version UID
     */
    private static final long serialVersionUID = 8336176213498776385L;

    /**
     * This method processes requests from the progress note page for both HTTP
     * <code>GET</code> and <code>POST</code> methods. The progress note data
     * retrieved from the request is written as a PDF document.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        ProgressNote progressNote
                = (ProgressNote) session.getAttribute(SessionObjectUtility.PROGRESS_NOTE);
        Patient patient = progressNote.getPatient();
        response.setContentType("text/html;charset=UTF-8");
        int yPosition = 705;
        int xPosition = 70;
        int lineCount;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        String yesOrNo;
        String nextYesOrNo;
        try (PDDocument doc = new PDDocument()) {

            PDPage page = new PDPage();
            doc.addPage(page);

            try {
                PDPageContentStream content = new PDPageContentStream(doc, page);

                content.beginText();
                content.setFont(PDType1Font.HELVETICA_BOLD, TITLE_FONT_SIZE);
                content.moveTextPositionByAmount(225, MAXIMUM_Y_POSITION);
                content.drawString("Progress Note");
                content.endText();

                content.beginText();
                content.setFont(PDType1Font.HELVETICA_BOLD, DATE_FONT_SIZE);
                content.moveTextPositionByAmount(245, 725);
                content.drawString(progressNote.getDateCreated().toString());
                content.endText();

                content.beginText();
                content.setFont(PDType1Font.HELVETICA_BOLD, TEXT_FONT_SIZE);
                content.moveTextPositionByAmount(xPosition, yPosition);
                content.drawString("Patient Name: ");
                content.setFont(PDType1Font.HELVETICA, TEXT_FONT_SIZE);
                content.drawString(patient.getFirstName() + " "
                        + patient.getLastName());
                content.endText();

                yPosition -= LINE_HEIGHT;
                if (yPosition < MINIMUM_Y_POSITION) {
                    content.close();
                    page = new PDPage();
                    doc.addPage(page);
                    content = new PDPageContentStream(doc, page);
                    yPosition = MAXIMUM_Y_POSITION;
                }
                content.beginText();
                content.setFont(PDType1Font.HELVETICA_BOLD, TEXT_FONT_SIZE);
                content.moveTextPositionByAmount(xPosition, yPosition);
                content.drawString("Telephone: ");
                content.setFont(PDType1Font.HELVETICA, TEXT_FONT_SIZE);
                content.drawString("" + nullCheck(patient.getContactNumber()));
                content.setFont(PDType1Font.HELVETICA_BOLD, TEXT_FONT_SIZE);
                content.drawString("   DOB: ");
                content.setFont(PDType1Font.HELVETICA, TEXT_FONT_SIZE);
                content.drawString("" + patient.getBirthDate());
                content.endText();

                String[] noteLines = getNoteLines((String) nullCheck(progressNote.getPatient().getAddress()),
                        ADDRESS_LINE_LENGTH);

                lineCount = 1;
                for (String s : noteLines) {
                    yPosition -= LINE_HEIGHT;
                    if (yPosition < MINIMUM_Y_POSITION) {
                        content.close();
                        page = new PDPage();
                        doc.addPage(page);
                        content = new PDPageContentStream(doc, page);
                        yPosition = MAXIMUM_Y_POSITION;
                    }
                    content.beginText();
                    content.moveTextPositionByAmount(xPosition, yPosition);
                    if (lineCount == 1) {
                        content.setFont(PDType1Font.HELVETICA_BOLD, TEXT_FONT_SIZE);
                        content.drawString("Address: ");
                    }
                    content.setFont(PDType1Font.HELVETICA, TEXT_FONT_SIZE);
                    content.drawString(s);
                    content.endText();
                    lineCount++;
                }

                noteLines = getNoteLines((String) nullCheck(patient.getEmailAddress()),
                        ADDRESS_LINE_LENGTH);

                lineCount = 1;
                for (String s : noteLines) {
                    yPosition -= LINE_HEIGHT;
                    if (yPosition < MINIMUM_Y_POSITION) {
                        content.close();
                        page = new PDPage();
                        doc.addPage(page);
                        content = new PDPageContentStream(doc, page);
                        yPosition = MAXIMUM_Y_POSITION;
                    }
                    content.beginText();
                    content.moveTextPositionByAmount(xPosition, yPosition);
                    if (lineCount == 1) {
                        content.setFont(PDType1Font.HELVETICA_BOLD, TEXT_FONT_SIZE);
                        content.drawString("Email Address: ");
                    }
                    content.setFont(PDType1Font.HELVETICA, TEXT_FONT_SIZE);
                    content.drawString(s);
                    content.endText();
                    lineCount++;
                }

                if (progressNote.getAllergicToMedications() == true) {
                    yesOrNo = "yes";
                } else {
                    yesOrNo = "no";
                }
                if (progressNote.getMedicalInsurance() == true) {
                    nextYesOrNo = "yes";
                } else {
                    nextYesOrNo = "no";
                }

                yPosition -= LINE_HEIGHT;
                if (yPosition < MINIMUM_Y_POSITION) {
                    content.close();
                    page = new PDPage();
                    doc.addPage(page);
                    content = new PDPageContentStream(doc, page);
                    yPosition = MAXIMUM_Y_POSITION;
                }
                content.beginText();
                content.setFont(PDType1Font.HELVETICA_BOLD, TEXT_FONT_SIZE);
                content.moveTextPositionByAmount(xPosition, yPosition);
                content.drawString("Allergic to medications: ");
                content.setFont(PDType1Font.HELVETICA, TEXT_FONT_SIZE);
                content.drawString(yesOrNo);
                content.setFont(PDType1Font.HELVETICA_BOLD, TEXT_FONT_SIZE);
                content.drawString("  Medical Insurance: ");
                content.setFont(PDType1Font.HELVETICA, TEXT_FONT_SIZE);
                content.drawString(nextYesOrNo);
                content.endText();

                yPosition -= LINE_HEIGHT;
                if (yPosition < MINIMUM_Y_POSITION) {
                    content.close();
                    page = new PDPage();
                    doc.addPage(page);
                    content = new PDPageContentStream(doc, page);
                    yPosition = MAXIMUM_Y_POSITION;
                }
                content.beginText();
                content.setFont(PDType1Font.HELVETICA_BOLD, TEXT_FONT_SIZE);
                content.moveTextPositionByAmount(xPosition, yPosition);
                content.drawString("Shoe Size: ");
                content.setFont(PDType1Font.HELVETICA, TEXT_FONT_SIZE);
                content.drawString("" + nullCheck(progressNote.getShoeSize()));
                content.endText();

                yPosition -= LINE_HEIGHT;
                if (yPosition < MINIMUM_Y_POSITION) {
                    content.close();
                    page = new PDPage();
                    doc.addPage(page);
                    content = new PDPageContentStream(doc, page);
                    yPosition = MAXIMUM_Y_POSITION;
                }
                content.beginText();
                content.setFont(PDType1Font.HELVETICA_BOLD, TEXT_FONT_SIZE);
                content.moveTextPositionByAmount(xPosition, yPosition);
                content.drawString("Allergies: ");
                content.setFont(PDType1Font.HELVETICA, TEXT_FONT_SIZE);
                content.drawString("" + nullCheck(progressNote.getAllergies()));
                content.endText();

                noteLines = getNoteLines((String) nullCheck(progressNote.getMedications()),
                        ADDRESS_LINE_LENGTH);

                lineCount = 1;
                for (String s : noteLines) {
                    yPosition -= 20;
                    if (yPosition < MINIMUM_Y_POSITION) {
                        content.close();
                        page = new PDPage();
                        doc.addPage(page);
                        content = new PDPageContentStream(doc, page);
                        yPosition = MAXIMUM_Y_POSITION;
                    }
                    content.beginText();
                    content.moveTextPositionByAmount(xPosition, yPosition);
                    if (lineCount == 1) {
                        content.setFont(PDType1Font.HELVETICA_BOLD, TEXT_FONT_SIZE);
                        content.drawString("Medications: ");
                    }
                    content.setFont(PDType1Font.HELVETICA, TEXT_FONT_SIZE);
                    content.drawString(s);
                    content.endText();
                    lineCount++;
                }

                yPosition -= LINE_HEIGHT;
                if (yPosition < MINIMUM_Y_POSITION) {
                    content.close();
                    page = new PDPage();
                    doc.addPage(page);
                    content = new PDPageContentStream(doc, page);
                    yPosition = MAXIMUM_Y_POSITION;
                }
                content.beginText();
                content.setFont(PDType1Font.HELVETICA_BOLD, TEXT_FONT_SIZE);
                content.moveTextPositionByAmount(xPosition, yPosition);
                content.drawString("Weight(lbs): ");
                content.setFont(PDType1Font.HELVETICA, TEXT_FONT_SIZE);
                content.drawString("" + nullCheck(progressNote.getWeight()));
                content.setFont(PDType1Font.HELVETICA_BOLD, TEXT_FONT_SIZE);
                content.drawString("   Height(in.): ");
                content.setFont(PDType1Font.HELVETICA, TEXT_FONT_SIZE);
                if ((progressNote.getHeightFeet() != null)
                        && (progressNote.getHeightInches() != null)) {
                    content.drawString("" + ((progressNote.getHeightFeet() * 12)
                            + (progressNote.getHeightInches())));
                }
                content.setFont(PDType1Font.HELVETICA_BOLD, TEXT_FONT_SIZE);
                content.drawString("   BMI: ");
                content.setFont(PDType1Font.HELVETICA, TEXT_FONT_SIZE);
                content.drawString("" + nullCheck(progressNote.getBmi()));
                content.endText();

                yPosition -= LINE_HEIGHT;
                if (yPosition < MINIMUM_Y_POSITION) {
                    content.close();
                    page = new PDPage();
                    doc.addPage(page);
                    content = new PDPageContentStream(doc, page);
                    yPosition = MAXIMUM_Y_POSITION;
                }
                content.beginText();
                content.setFont(PDType1Font.HELVETICA_BOLD, TEXT_FONT_SIZE);
                content.moveTextPositionByAmount(xPosition, yPosition);
                content.drawString("Waist(in.): ");
                content.setFont(PDType1Font.HELVETICA, TEXT_FONT_SIZE);
                content.drawString("" + nullCheck(progressNote.getWaist()));
                content.setFont(PDType1Font.HELVETICA_BOLD, TEXT_FONT_SIZE);
                content.drawString("   BP: ");
                content.setFont(PDType1Font.HELVETICA, TEXT_FONT_SIZE);
                if ((progressNote.getBloodPressureSystole() != null)
                        && (progressNote.getBloodPressureDiastole() != null)) {
                    content.drawString(progressNote.getBloodPressureSystole() + "/"
                            + progressNote.getBloodPressureDiastole());
                }
                content.setFont(PDType1Font.HELVETICA_BOLD, TEXT_FONT_SIZE);
                content.drawString("   Pulse: ");
                content.setFont(PDType1Font.HELVETICA, TEXT_FONT_SIZE);
                content.drawString("" + nullCheck(progressNote.getPulse()));
                content.setFont(PDType1Font.HELVETICA_BOLD, TEXT_FONT_SIZE);
                content.drawString("   Respirations: ");
                content.setFont(PDType1Font.HELVETICA, TEXT_FONT_SIZE);
                content.drawString("" + nullCheck(progressNote.getRespirations()));
                content.endText();

                if (progressNote.getFootScreening() == true) {
                    yesOrNo = "yes";
                } else {
                    yesOrNo = "no";
                }
                yPosition -= LINE_HEIGHT;
                if (yPosition < MINIMUM_Y_POSITION) {
                    content.close();
                    page = new PDPage();
                    doc.addPage(page);
                    content = new PDPageContentStream(doc, page);
                    yPosition = MAXIMUM_Y_POSITION;
                }
                content.beginText();
                content.setFont(PDType1Font.HELVETICA_BOLD, TEXT_FONT_SIZE);
                content.moveTextPositionByAmount(xPosition, yPosition);
                content.drawString("Temperature: ");
                content.setFont(PDType1Font.HELVETICA, TEXT_FONT_SIZE);
                content.drawString("" + nullCheck(progressNote.getTemperature()));
                content.setFont(PDType1Font.HELVETICA_BOLD, TEXT_FONT_SIZE);
                content.drawString("   Foot Screening: ");
                content.setFont(PDType1Font.HELVETICA, TEXT_FONT_SIZE);
                content.drawString(yesOrNo);
                content.setFont(PDType1Font.HELVETICA_BOLD, TEXT_FONT_SIZE);
                content.drawString("   Glucose: ");
                content.setFont(PDType1Font.HELVETICA, TEXT_FONT_SIZE);
                content.drawString("" + nullCheck(progressNote.getGlucose()));
                content.endText();

                yPosition -= LINE_HEIGHT;
                if (yPosition < MINIMUM_Y_POSITION) {
                    content.close();
                    page = new PDPage();
                    doc.addPage(page);
                    content = new PDPageContentStream(doc, page);
                    yPosition = MAXIMUM_Y_POSITION;
                }
                content.beginText();
                content.setFont(PDType1Font.HELVETICA_BOLD, TEXT_FONT_SIZE);
                content.moveTextPositionByAmount(xPosition, yPosition);
                content.drawString("A1C: ");
                content.setFont(PDType1Font.HELVETICA, TEXT_FONT_SIZE);
                content.drawString("" + nullCheck(progressNote.getA1c()));
                content.endText();

                /* nurse or dietitian note */
                yPosition -= LINE_HEIGHT;
                if (yPosition < MINIMUM_Y_POSITION) {
                    content.close();
                    page = new PDPage();
                    doc.addPage(page);
                    content = new PDPageContentStream(doc, page);
                    yPosition = MAXIMUM_Y_POSITION;
                }
                content.beginText();
                content.setFont(PDType1Font.HELVETICA_BOLD, TEXT_FONT_SIZE);
                content.moveTextPositionByAmount(xPosition, yPosition);
                content.drawString("Nurse/Dietitian Note: ");
                content.endText();

                noteLines = getNoteLines((String) nullCheck(progressNote.getNurseOrDietitianNote()),
                        NOTE_LINE_LENGTH);

                for (String s : noteLines) {
                    yPosition -= LINE_HEIGHT;
                    if (yPosition < MINIMUM_Y_POSITION) {
                        content.close();
                        page = new PDPage();
                        doc.addPage(page);
                        content = new PDPageContentStream(doc, page);
                        yPosition = MAXIMUM_Y_POSITION;
                    }
                    content.beginText();
                    content.setFont(PDType1Font.HELVETICA, TEXT_FONT_SIZE);
                    content.moveTextPositionByAmount(xPosition, yPosition);
                    content.drawString(s);
                    content.endText();
                }

                /* subjective section */
                yPosition -= LINE_HEIGHT;
                if (yPosition < MINIMUM_Y_POSITION) {
                    content.close();
                    page = new PDPage();
                    doc.addPage(page);
                    content = new PDPageContentStream(doc, page);
                    yPosition = MAXIMUM_Y_POSITION;
                }
                content.beginText();
                content.setFont(PDType1Font.HELVETICA_BOLD, TEXT_FONT_SIZE);
                content.moveTextPositionByAmount(xPosition, yPosition);
                content.drawString("Subjective: ");
                content.endText();

                noteLines = getNoteLines((String) nullCheck(progressNote.getSubjective()),
                        NOTE_LINE_LENGTH);

                for (String s : noteLines) {
                    yPosition -= LINE_HEIGHT;
                    if (yPosition < MINIMUM_Y_POSITION) {
                        content.close();
                        page = new PDPage();
                        doc.addPage(page);
                        content = new PDPageContentStream(doc, page);
                        yPosition = MAXIMUM_Y_POSITION;
                    }
                    content.beginText();
                    content.setFont(PDType1Font.HELVETICA, TEXT_FONT_SIZE);
                    content.moveTextPositionByAmount(xPosition, yPosition);
                    content.drawString(s);
                    content.endText();
                }

                /* objective section */
                yPosition -= LINE_HEIGHT;
                if (yPosition < MINIMUM_Y_POSITION) {
                    content.close();
                    page = new PDPage();
                    doc.addPage(page);
                    content = new PDPageContentStream(doc, page);
                    yPosition = MAXIMUM_Y_POSITION;
                }
                content.beginText();
                content.setFont(PDType1Font.HELVETICA_BOLD, TEXT_FONT_SIZE);
                content.moveTextPositionByAmount(xPosition, yPosition);
                content.drawString("Objective: ");
                content.endText();

                noteLines = getNoteLines((String) nullCheck(progressNote.getObjective()),
                        NOTE_LINE_LENGTH);

                for (String s : noteLines) {
                    yPosition -= LINE_HEIGHT;
                    if (yPosition < MINIMUM_Y_POSITION) {
                        content.close();
                        page = new PDPage();
                        doc.addPage(page);
                        content = new PDPageContentStream(doc, page);
                        yPosition = MAXIMUM_Y_POSITION;
                    }
                    content.beginText();
                    content.setFont(PDType1Font.HELVETICA, TEXT_FONT_SIZE);
                    content.moveTextPositionByAmount(xPosition, yPosition);
                    content.drawString(s);
                    content.endText();
                }

                /* assessment section */
                yPosition -= LINE_HEIGHT;
                if (yPosition < MINIMUM_Y_POSITION) {
                    content.close();
                    page = new PDPage();
                    doc.addPage(page);
                    content = new PDPageContentStream(doc, page);
                    yPosition = MAXIMUM_Y_POSITION;
                }
                content.beginText();
                content.setFont(PDType1Font.HELVETICA_BOLD, TEXT_FONT_SIZE);
                content.moveTextPositionByAmount(xPosition, yPosition);
                content.drawString("Assessment: ");
                content.endText();

                noteLines = getNoteLines((String) nullCheck(progressNote.getAssessment()),
                        NOTE_LINE_LENGTH);

                for (String s : noteLines) {
                    yPosition -= LINE_HEIGHT;
                    if (yPosition < MINIMUM_Y_POSITION) {
                        content.close();
                        page = new PDPage();
                        doc.addPage(page);
                        content = new PDPageContentStream(doc, page);
                        yPosition = MAXIMUM_Y_POSITION;
                    }
                    content.beginText();
                    content.setFont(PDType1Font.HELVETICA, TEXT_FONT_SIZE);
                    content.moveTextPositionByAmount(xPosition, yPosition);
                    content.drawString(s);
                    content.endText();
                }

                /* plan section */
                yPosition -= LINE_HEIGHT;
                if (yPosition < MINIMUM_Y_POSITION) {
                    content.close();
                    page = new PDPage();
                    doc.addPage(page);
                    content = new PDPageContentStream(doc, page);
                    yPosition = MAXIMUM_Y_POSITION;
                }
                content.beginText();
                content.setFont(PDType1Font.HELVETICA_BOLD, TEXT_FONT_SIZE);
                content.moveTextPositionByAmount(xPosition, yPosition);
                content.drawString("Plan: ");
                content.endText();

                noteLines = getNoteLines((String) nullCheck(progressNote.getPlan()),
                        NOTE_LINE_LENGTH);

                for (String s : noteLines) {
                    yPosition -= LINE_HEIGHT;
                    if (yPosition < MINIMUM_Y_POSITION) {
                        content.close();
                        page = new PDPage();
                        doc.addPage(page);
                        content = new PDPageContentStream(doc, page);
                        yPosition = MAXIMUM_Y_POSITION;
                    }
                    content.beginText();
                    content.setFont(PDType1Font.HELVETICA, TEXT_FONT_SIZE);
                    content.moveTextPositionByAmount(xPosition, yPosition);
                    content.drawString(s);
                    content.endText();
                }

                for (NoteAuthor na : progressNote.getUpdatedBy()) {
                    yPosition -= LINE_HEIGHT;
                    if (yPosition < MINIMUM_Y_POSITION) {
                        content.close();
                        page = new PDPage();
                        doc.addPage(page);
                        content = new PDPageContentStream(doc, page);
                        yPosition = MAXIMUM_Y_POSITION;
                    }
                    content.beginText();
                    content.setFont(PDType1Font.HELVETICA_BOLD, TEXT_FONT_SIZE);
                    content.moveTextPositionByAmount(xPosition, yPosition);
                    content.drawString("Updated by: ");
                    content.setFont(PDType1Font.HELVETICA, TEXT_FONT_SIZE);
                    content.drawString(na.getFirstName() + " "
                            + na.getLastName() + ", " + na.getJobTitle() + ", "
                            + na.getTimeStamp());
                    content.endText();
                }
                content.close();
            } catch (IOException e) {
                Logger.getLogger(PDFServlet.class.getName()).log(Level.SEVERE,
                        "An IOException occurred when creating the PDF content.", e);
            }
            doc.addPage(page);
            doc.save(output);
            doc.close();
            String pdfFileName = "pdf-test.pdf";
            response.setContentType("application/pdf");
            response.addHeader("Content-Type", "application/force-download");
            response.addHeader("Content-Disposition", "attachment; filename="
                    + pdfFileName);

            response.getOutputStream().write(output.toByteArray());

        } catch (IOException | COSVisitorException e) {
            Logger.getLogger(PDFServlet.class.getName()).log(Level.SEVERE,
                    "An exception occurred when creating the PDF document.", e);
        }
    }

    /**
     * This method returns the given object if the given object is not null,
     * otherwise an empty string is returned.
     *
     * @param objectToCheck the object to check
     * @return the object to be used in the document
     */
    private Object nullCheck(Object objectToCheck) {
        return (objectToCheck != null) ? objectToCheck : "";
    }

    /**
     * This method takes note input as a string, splits it into an array of
     * strings so that each string element is no greater than the maximum
     * length, and returns the array.
     *
     * @param noteInput the note input
     * @param maxLength the maximum length
     * @return the array of strings
     */
    private String[] getNoteLines(String noteInput, int maxLength) {

        String[] noteInputStrings = noteInput.split("\n");
        StringBuilder sb = new StringBuilder();
        for (String s : noteInputStrings) {
            s = WordUtils.wrap(s, maxLength, "\n", true);
            sb.append(s);
            sb.append("\n");
        }
        return sb.toString().split("\n");
    }

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
        processRequest(request, response);
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
        processRequest(request, response);
    }
}
