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

import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import registry.EmailMessage;
import registry.Patient;

/**
 * This utility class sends email messages for the registry.
 *
 * @author Bryan Daniel
 * @version 2, March 16, 2017
 */
public class EmailUtility {
    
    /**
     * The default language for email messages
     */
    private static final String DEFAULT_LANGUAGE = "English";

    /**
     * This method takes the given parameters and uses them to send an email
     * from a host email address to a recipient's address.
     *
     * @param to the recipient's address
     * @param from the mailer's address
     * @param subject the subject line
     * @param body the message body
     * @param bodyIsHTML a boolean indicating if the body is HTML
     * @param emailPassword the user-provided email password
     * @param request the HttpServletRequest object
     * @return the status of the operation
     */
    public static boolean sendMail(String to, String from,
            String subject, String body, boolean bodyIsHTML,
            String emailPassword, HttpServletRequest request) {

        String host = ConfigurationManager.getMailHost(request.getServletContext());
        if (host == null) {
            return false;
        }

        if (!isEmailAddressValid(to)) {
            return false;
        }

        /* This section gets a mail session. */
        Properties props = new Properties();
        props.put("mail.transport.protocol", "smtps");
        props.put("mail.smtps.host", host);
        props.put("mail.smtps.port", 465);
        props.put("mail.smtps.auth", "true");
        props.put("mail.smtps.quitwait", "false");
        Session session = Session.getDefaultInstance(props);
        session.setDebug(true);
        Transport transport = null;

        try {

            /* This section creates a message. */
            Message message = new MimeMessage(session);
            message.setSubject(subject);
            message.setSentDate(new Date());
            if (bodyIsHTML) {
                message.setContent(body, "text/html");
            } else {
                message.setText(body);
            }

            /* This section addresses the message. */
            Address fromAddress = new InternetAddress(from);
            Address toAddress = new InternetAddress(to);
            message.setFrom(fromAddress);
            message.setRecipient(Message.RecipientType.TO, toAddress);

            /* This section sends the message. */
            transport = session.getTransport();
            transport.connect(from, emailPassword);
            transport.sendMessage(message, message.getAllRecipients());

        } catch (MessagingException me) {
            Logger.getLogger(EmailUtility.class.getName()).log(Level.SEVERE,
                    "A messaging exception occurred in the sendMail method.", me);
            return false;
        } finally {
            try {
                if (transport != null) {
                    transport.close();
                }
            } catch (MessagingException e) {
                Logger.getLogger(EmailUtility.class.getName()).log(Level.SEVERE,
                    "Unable to close email transport connection.", e);
            }
        }
        return true;
    }

    /**
     * This method takes the given parameters and uses them to send a batch of
     * email reminders to multiple patients from a host email account.
     *
     * @param emailPatients the list of patients
     * @param from the mailer's address
     * @param messages the list of messages
     * @param bodyIsHTML a boolean indicating if the body is HTML
     * @param emailPassword the user-provided email password
     * @param request the HttpServletRequest object
     * @return the status of the operation
     */
    public static boolean sendBatchMail(ArrayList<Patient> emailPatients,
            String from, ArrayList<EmailMessage> messages, boolean bodyIsHTML,
            String emailPassword, HttpServletRequest request) {

        String host = ConfigurationManager.getMailHost(request.getServletContext());
        if (host == null) {
            return false;
        }

        /* This section gets a mail session. */
        Properties props = new Properties();
        props.put("mail.transport.protocol", "smtps");
        props.put("mail.smtps.host", host);
        props.put("mail.smtps.port", 465);
        props.put("mail.smtps.auth", "true");
        props.put("mail.smtps.quitwait", "false");
        Session session = Session.getDefaultInstance(props);
        session.setDebug(true);
        Transport transport = null;

        try {

            transport = session.getTransport();
            transport.connect(from, emailPassword);

            for (Patient p : emailPatients) {

                if (isEmailAddressValid(p.getEmailAddress())) {

                    String language = null;
                    String subject = "";
                    String body = "";

                    for (EmailMessage em : messages) {
                        if (em.getLanguage().equals(p.getLanguage())) {
                            language = em.getLanguage();
                            subject = em.getSubject();
                            body = em.getMessage();
                        }
                    }
                    if (language == null) {
                        language = DEFAULT_LANGUAGE;
                        for (EmailMessage em : messages) {
                            if (em.getLanguage().equals(language)) {
                                subject = em.getSubject();
                                body = em.getMessage();
                            }
                        }
                    }

                    /* This section creates a message. */
                    Message message = new MimeMessage(session);
                    message.setSubject(subject);
                    message.setSentDate(new Date());
                    if (bodyIsHTML) {
                        message.setContent(body, "text/html");
                    } else {
                        message.setText(body);
                    }

                    /* This section addresses the message. */
                    Address fromAddress = new InternetAddress(from);
                    Address toAddress = new InternetAddress(p.getEmailAddress());
                    message.setFrom(fromAddress);
                    message.setRecipient(Message.RecipientType.TO, toAddress);

                    /* This line sends the message. */
                    transport.sendMessage(message, message.getAllRecipients());
                }
            }

        } catch (MessagingException me) {
            Logger.getLogger(EmailUtility.class.getName()).log(Level.SEVERE,
                    "A messaging exception occurred in the sendBatchMail method.", me);
            return false;
        } finally {
            try {
                if (transport != null) {
                    transport.close();
                }
            } catch (MessagingException e) {
                Logger.getLogger(EmailUtility.class.getName()).log(Level.SEVERE,
                    "Unable to close email transport connection.", e);
            }
        }
        return true;
    }

    /**
     * This method does a basic check of an input string to validate that it is
     * an email address.
     *
     * @param emailAddressInput the input string
     * @return the result indicating a valid or invalid string
     */
    public static boolean isEmailAddressValid(String emailAddressInput) {

        try {
            InternetAddress emailAddress = new InternetAddress(emailAddressInput);
            emailAddress.validate();
        } catch (AddressException ex) {
            Logger.getLogger(EmailUtility.class.getName()).log(Level.INFO, null, ex);
            return false;
        }
        return true;
    }

    /**
     * If an error occurs in the sendNewUserEmail method, this method is called
     * to log the error and send a message back to the user interface.
     *
     * @param to the recipient
     * @param from the source
     * @param subject the subject
     * @param source
     * @param batch
     */
    public static void logEmailError(String to, String from, String subject, String source, 
            boolean batch) {

        StringBuilder sb = new StringBuilder();
        if (batch) {
            sb.append(source);
            sb.append(": Unable to send batch email. \n");
        } else {
            sb.append(source);
            sb.append(": Unable to send email. \n");
            sb.append("TO: ");
            sb.append(to);
            sb.append("\nFROM: ");
            sb.append(from);
            sb.append("\nSUBJECT: ");
            sb.append(subject);
            sb.append("\n\n");
        }
        Logger.getLogger(EmailUtility.class.getName()).log(Level.SEVERE, sb.toString());
    }
}
