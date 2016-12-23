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

import clinic.NoteAuthor;
import clinic.Patient;
import clinic.ProgressNote;
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

/**
 * This HttpServlet class shows a PDF document containing progress note
 * information.
 *
 * @author Bryan Daniel
 * @version 1, April 8, 2016
 */
public class PDFServlet extends HttpServlet {

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
        final int NOTE_LINE_LENGTH = 65;
        final int ADDRESS_LINE_LENGTH = 55;
        int yPosition = 705;
        int xPosition = 70;
        HttpSession session = request.getSession();
        ProgressNote progressNote
                = (ProgressNote) session.getAttribute("progressNote");
        Patient patient = progressNote.getPatient();
        response.setContentType("text/html;charset=UTF-8");
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        String yesOrNo;
        String nextYesOrNo;
        try (PDDocument doc = new PDDocument()) {

            PDPage page = new PDPage();
            doc.addPage(page);

            try {
                PDPageContentStream content = new PDPageContentStream(doc, page);

                content.beginText();
                content.setFont(PDType1Font.HELVETICA_BOLD, 26);
                content.moveTextPositionByAmount(225, 750);
                content.drawString("Progress Note");
                content.endText();

                content.beginText();
                content.setFont(PDType1Font.HELVETICA_BOLD, 20);
                content.moveTextPositionByAmount(245, 725);
                content.drawString(progressNote.getDateCreated().toString());
                content.endText();

                content.beginText();
                content.setFont(PDType1Font.HELVETICA_BOLD, 16);
                content.moveTextPositionByAmount(xPosition, yPosition);
                content.drawString("Patient Name: ");
                content.setFont(PDType1Font.HELVETICA, 16);
                content.drawString(patient.getFirstName() + " "
                        + patient.getLastName());
                content.endText();

                yPosition -= 20;
                if (yPosition < 0) {
                    content.close();
                    page = new PDPage();
                    doc.addPage(page);
                    content = new PDPageContentStream(doc, page);
                    yPosition = 750;
                }
                content.beginText();
                content.setFont(PDType1Font.HELVETICA_BOLD, 16);
                content.moveTextPositionByAmount(xPosition, yPosition);
                content.drawString("Telephone: ");
                content.setFont(PDType1Font.HELVETICA, 16);
                content.drawString("" + nullCheck(patient.getContactNumber()));
                content.setFont(PDType1Font.HELVETICA_BOLD, 16);
                content.drawString("   DOB: ");
                content.setFont(PDType1Font.HELVETICA, 16);
                content.drawString("" + patient.getBirthDate());
                content.endText();

                String note
                        = (String) nullCheck(progressNote.getPatient().getAddress());
                note = WordUtils.wrap(note, ADDRESS_LINE_LENGTH, "\n", true);
                String[] noteLines = note.split("\n");

                for (String s : noteLines) {
                    yPosition -= 20;
                    if (yPosition < 0) {
                        content.close();
                        page = new PDPage();
                        doc.addPage(page);
                        content = new PDPageContentStream(doc, page);
                        yPosition = 750;
                    }
                    content.beginText();
                    content.setFont(PDType1Font.HELVETICA_BOLD, 16);
                    content.moveTextPositionByAmount(xPosition, yPosition);
                    content.drawString("Address: ");
                    content.setFont(PDType1Font.HELVETICA, 16);
                    content.drawString(s);
                    content.endText();
                }

                note = (String) nullCheck(patient.getEmailAddress());
                note = WordUtils.wrap(note, ADDRESS_LINE_LENGTH, "\n", true);
                noteLines = note.split("\n");

                for (String s : noteLines) {
                    yPosition -= 20;
                    if (yPosition < 0) {
                        content.close();
                        page = new PDPage();
                        doc.addPage(page);
                        content = new PDPageContentStream(doc, page);
                        yPosition = 750;
                    }
                    content.beginText();
                    content.setFont(PDType1Font.HELVETICA_BOLD, 16);
                    content.moveTextPositionByAmount(xPosition, yPosition);
                    content.drawString("Email Address: ");
                    content.setFont(PDType1Font.HELVETICA, 16);
                    content.drawString(s);
                    content.endText();
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

                yPosition -= 20;
                if (yPosition < 0) {
                    content.close();
                    page = new PDPage();
                    doc.addPage(page);
                    content = new PDPageContentStream(doc, page);
                    yPosition = 750;
                }
                content.beginText();
                content.setFont(PDType1Font.HELVETICA_BOLD, 16);
                content.moveTextPositionByAmount(xPosition, yPosition);
                content.drawString("Allergic to medications: ");
                content.setFont(PDType1Font.HELVETICA, 16);
                content.drawString(yesOrNo);
                content.setFont(PDType1Font.HELVETICA_BOLD, 16);
                content.drawString("  Medical Insurance: ");
                content.setFont(PDType1Font.HELVETICA, 16);
                content.drawString(nextYesOrNo);
                content.endText();

                yPosition -= 20;
                if (yPosition < 0) {
                    content.close();
                    page = new PDPage();
                    doc.addPage(page);
                    content = new PDPageContentStream(doc, page);
                    yPosition = 750;
                }
                content.beginText();
                content.setFont(PDType1Font.HELVETICA_BOLD, 16);
                content.moveTextPositionByAmount(xPosition, yPosition);
                content.drawString("Shoe Size: ");
                content.setFont(PDType1Font.HELVETICA, 16);
                content.drawString("" + nullCheck(progressNote.getShoeSize()));
                content.endText();

                yPosition -= 20;
                if (yPosition < 0) {
                    content.close();
                    page = new PDPage();
                    doc.addPage(page);
                    content = new PDPageContentStream(doc, page);
                    yPosition = 750;
                }
                content.beginText();
                content.setFont(PDType1Font.HELVETICA_BOLD, 16);
                content.moveTextPositionByAmount(xPosition, yPosition);
                content.drawString("Allergies: ");
                content.setFont(PDType1Font.HELVETICA, 16);
                content.drawString("" + nullCheck(progressNote.getAllergies()));
                content.endText();

                note = (String) nullCheck(progressNote.getMedications());
                note = WordUtils.wrap(note, ADDRESS_LINE_LENGTH, "\n", true);
                noteLines = note.split("\n");

                for (String s : noteLines) {
                    yPosition -= 20;
                    if (yPosition < 0) {
                        content.close();
                        page = new PDPage();
                        doc.addPage(page);
                        content = new PDPageContentStream(doc, page);
                        yPosition = 750;
                    }
                    content.beginText();
                    content.setFont(PDType1Font.HELVETICA_BOLD, 16);
                    content.moveTextPositionByAmount(xPosition, yPosition);
                    content.drawString("Medications: ");
                    content.setFont(PDType1Font.HELVETICA, 16);
                    content.drawString(s);
                    content.endText();
                }

                yPosition -= 20;
                if (yPosition < 0) {
                    content.close();
                    page = new PDPage();
                    doc.addPage(page);
                    content = new PDPageContentStream(doc, page);
                    yPosition = 750;
                }
                content.beginText();
                content.setFont(PDType1Font.HELVETICA_BOLD, 16);
                content.moveTextPositionByAmount(xPosition, yPosition);
                content.drawString("Weight(lbs): ");
                content.setFont(PDType1Font.HELVETICA, 16);
                content.drawString("" + nullCheck(progressNote.getWeight()));
                content.setFont(PDType1Font.HELVETICA_BOLD, 16);
                content.drawString("   Height(in.): ");
                content.setFont(PDType1Font.HELVETICA, 16);
                if ((progressNote.getHeightFeet() != null)
                        && (progressNote.getHeightInches() != null)) {
                    content.drawString("" + ((progressNote.getHeightFeet() * 12)
                            + (progressNote.getHeightInches())));
                }
                content.setFont(PDType1Font.HELVETICA_BOLD, 16);
                content.drawString("   BMI: ");
                content.setFont(PDType1Font.HELVETICA, 16);
                content.drawString("" + nullCheck(progressNote.getBmi()));
                content.endText();

                yPosition -= 20;
                if (yPosition < 0) {
                    content.close();
                    page = new PDPage();
                    doc.addPage(page);
                    content = new PDPageContentStream(doc, page);
                    yPosition = 750;
                }
                content.beginText();
                content.setFont(PDType1Font.HELVETICA_BOLD, 16);
                content.moveTextPositionByAmount(xPosition, yPosition);
                content.drawString("Waist(in.): ");
                content.setFont(PDType1Font.HELVETICA, 16);
                content.drawString("" + nullCheck(progressNote.getWaist()));
                content.setFont(PDType1Font.HELVETICA_BOLD, 16);
                content.drawString("   BP: ");
                content.setFont(PDType1Font.HELVETICA, 16);
                if ((progressNote.getBpSystole() != null)
                        && (progressNote.getBpDiastole() != null)) {
                    content.drawString(progressNote.getBpSystole() + "/"
                            + progressNote.getBpDiastole());
                }
                content.setFont(PDType1Font.HELVETICA_BOLD, 16);
                content.drawString("   Pulse: ");
                content.setFont(PDType1Font.HELVETICA, 16);
                content.drawString("" + nullCheck(progressNote.getPulse()));
                content.setFont(PDType1Font.HELVETICA_BOLD, 16);
                content.drawString("   Respirations: ");
                content.setFont(PDType1Font.HELVETICA, 16);
                content.drawString("" + nullCheck(progressNote.getRespirations()));
                content.endText();

                if (progressNote.getFootScreening() == true) {
                    yesOrNo = "yes";
                } else {
                    yesOrNo = "no";
                }
                yPosition -= 20;
                if (yPosition < 0) {
                    content.close();
                    page = new PDPage();
                    doc.addPage(page);
                    content = new PDPageContentStream(doc, page);
                    yPosition = 750;
                }
                content.beginText();
                content.setFont(PDType1Font.HELVETICA_BOLD, 16);
                content.moveTextPositionByAmount(xPosition, yPosition);
                content.drawString("Temperature: ");
                content.setFont(PDType1Font.HELVETICA, 16);
                content.drawString("" + nullCheck(progressNote.getTemperature()));
                content.setFont(PDType1Font.HELVETICA_BOLD, 16);
                content.drawString("   Foot Screening: ");
                content.setFont(PDType1Font.HELVETICA, 16);
                content.drawString(yesOrNo);
                content.setFont(PDType1Font.HELVETICA_BOLD, 16);
                content.drawString("   Glucose: ");
                content.setFont(PDType1Font.HELVETICA, 16);
                content.drawString("" + nullCheck(progressNote.getGlucose()));
                content.endText();

                yPosition -= 20;
                if (yPosition < 0) {
                    content.close();
                    page = new PDPage();
                    doc.addPage(page);
                    content = new PDPageContentStream(doc, page);
                    yPosition = 750;
                }
                content.beginText();
                content.setFont(PDType1Font.HELVETICA_BOLD, 16);
                content.moveTextPositionByAmount(xPosition, yPosition);
                content.drawString("A1C: ");
                content.setFont(PDType1Font.HELVETICA, 16);
                content.drawString("" + nullCheck(progressNote.getA1c()));
                content.endText();

                /* nurse or dietitian note */
                yPosition -= 20;
                if (yPosition < 0) {
                    content.close();
                    page = new PDPage();
                    doc.addPage(page);
                    content = new PDPageContentStream(doc, page);
                    yPosition = 750;
                }
                content.beginText();
                content.setFont(PDType1Font.HELVETICA_BOLD, 16);
                content.moveTextPositionByAmount(xPosition, yPosition);
                content.drawString("Nurse/Dietitian Note: ");
                content.endText();

                note = (String) nullCheck(progressNote.getNurseOrDietitianNote());
                note = WordUtils.wrap(note, NOTE_LINE_LENGTH, "\n", true);
                noteLines = note.split("\n");

                for (String s : noteLines) {
                    yPosition -= 20;
                    if (yPosition < 0) {
                        content.close();
                        page = new PDPage();
                        doc.addPage(page);
                        content = new PDPageContentStream(doc, page);
                        yPosition = 750;
                    }
                    content.beginText();
                    content.setFont(PDType1Font.HELVETICA, 16);
                    content.moveTextPositionByAmount(xPosition, yPosition);
                    content.drawString(s);
                    content.endText();
                }

                /* subjective section */
                yPosition -= 20;
                if (yPosition < 0) {
                    content.close();
                    page = new PDPage();
                    doc.addPage(page);
                    content = new PDPageContentStream(doc, page);
                    yPosition = 750;
                }
                content.beginText();
                content.setFont(PDType1Font.HELVETICA_BOLD, 16);
                content.moveTextPositionByAmount(xPosition, yPosition);
                content.drawString("Subjective: ");
                content.endText();

                note = (String) nullCheck(progressNote.getSubjective());
                note = WordUtils.wrap(note, NOTE_LINE_LENGTH, "\n", true);
                noteLines = note.split("\n");

                for (String s : noteLines) {
                    yPosition -= 20;
                    if (yPosition < 0) {
                        content.close();
                        page = new PDPage();
                        doc.addPage(page);
                        content = new PDPageContentStream(doc, page);
                        yPosition = 750;
                    }
                    content.beginText();
                    content.setFont(PDType1Font.HELVETICA, 16);
                    content.moveTextPositionByAmount(xPosition, yPosition);
                    content.drawString(s);
                    content.endText();
                }

                /* objective section */
                yPosition -= 20;
                if (yPosition < 0) {
                    content.close();
                    page = new PDPage();
                    doc.addPage(page);
                    content = new PDPageContentStream(doc, page);
                    yPosition = 750;
                }
                content.beginText();
                content.setFont(PDType1Font.HELVETICA_BOLD, 16);
                content.moveTextPositionByAmount(xPosition, yPosition);
                content.drawString("Objective: ");
                content.endText();

                note = (String) nullCheck(progressNote.getObjective());
                note = WordUtils.wrap(note, NOTE_LINE_LENGTH, "\n", true);
                String[] noteLinesArray = note.split("\n");

                for (String s : noteLinesArray) {
                    yPosition -= 20;
                    if (yPosition < 0) {
                        content.close();
                        page = new PDPage();
                        doc.addPage(page);
                        content = new PDPageContentStream(doc, page);
                        yPosition = 750;
                    }
                    content.beginText();
                    content.setFont(PDType1Font.HELVETICA, 16);
                    content.moveTextPositionByAmount(xPosition, yPosition);
                    content.drawString(s);
                    content.endText();
                }

                /* assessment section */
                yPosition -= 20;
                if (yPosition < 0) {
                    content.close();
                    page = new PDPage();
                    doc.addPage(page);
                    content = new PDPageContentStream(doc, page);
                    yPosition = 750;
                }
                content.beginText();
                content.setFont(PDType1Font.HELVETICA_BOLD, 16);
                content.moveTextPositionByAmount(xPosition, yPosition);
                content.drawString("Assessment: ");
                content.endText();

                note = (String) nullCheck(progressNote.getAssessment());
                note = WordUtils.wrap(note, NOTE_LINE_LENGTH, "\n", true);
                noteLines = note.split("\n");

                for (String s : noteLines) {
                    yPosition -= 20;
                    if (yPosition < 0) {
                        content.close();
                        page = new PDPage();
                        doc.addPage(page);
                        content = new PDPageContentStream(doc, page);
                        yPosition = 750;
                    }
                    content.beginText();
                    content.setFont(PDType1Font.HELVETICA, 16);
                    content.moveTextPositionByAmount(xPosition, yPosition);
                    content.drawString(s);
                    content.endText();
                }

                /* plan section */
                yPosition -= 20;
                if (yPosition < 0) {
                    content.close();
                    page = new PDPage();
                    doc.addPage(page);
                    content = new PDPageContentStream(doc, page);
                    yPosition = 750;
                }
                content.beginText();
                content.setFont(PDType1Font.HELVETICA_BOLD, 16);
                content.moveTextPositionByAmount(xPosition, yPosition);
                content.drawString("Plan: ");
                content.endText();

                note = (String) nullCheck(progressNote.getPlan());
                note = WordUtils.wrap(note, NOTE_LINE_LENGTH, "\n", true);
                noteLines = note.split("\n");

                for (String s : noteLines) {
                    yPosition -= 20;
                    if (yPosition < 0) {
                        content.close();
                        page = new PDPage();
                        doc.addPage(page);
                        content = new PDPageContentStream(doc, page);
                        yPosition = 750;
                    }
                    content.beginText();
                    content.setFont(PDType1Font.HELVETICA, 16);
                    content.moveTextPositionByAmount(xPosition, yPosition);
                    content.drawString(s);
                    content.endText();
                }

                for (NoteAuthor na : progressNote.getUpdatedBy()) {
                    yPosition -= 20;
                    if (yPosition < 0) {
                        content.close();
                        page = new PDPage();
                        doc.addPage(page);
                        content = new PDPageContentStream(doc, page);
                        yPosition = 750;
                    }
                    content.beginText();
                    content.setFont(PDType1Font.HELVETICA_BOLD, 16);
                    content.moveTextPositionByAmount(xPosition, yPosition);
                    content.drawString("Updated by: ");
                    content.setFont(PDType1Font.HELVETICA, 16);
                    content.drawString(na.getFirstName() + " "
                            + na.getLastName() + ", " + na.getJobTitle() + ", "
                            + na.getTimeStamp());
                    content.endText();
                }
                content.close();
            } catch (IOException e) {

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

        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (COSVisitorException ex) {
            Logger.getLogger(PDFServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Checks objects for null pointer
     *
     * @param objectToCheck the object to check
     * @return the object to be used in the document
     */
    private Object nullCheck(Object objectToCheck) {
        if (objectToCheck != null) {
            return objectToCheck;
        } else {
            return "";
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
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
