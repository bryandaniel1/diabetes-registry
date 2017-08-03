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
package util;

import clinic.EmailMessage;
import clinic.Patient;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.*;
import javax.mail.internet.*;
import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Sends email messages for the registry
 *
 * @author Bryan Daniel
 * @version 1, April 8, 2016
 */
public class EmailUtil {

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

        String host = getMailHost(request);
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
            Logger.getLogger(EmailUtil.class.getName()).log(Level.SEVERE,
                    "An exception occurred in the sendMail method.", me);
            return false;
        } finally {
            try {
                if (transport != null) {
                    transport.close();
                }
            } catch (MessagingException e) {
                Logger.getLogger(EmailUtil.class.getName()).log(Level.SEVERE,
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

        final String DEFAULT_LANGUAGE = "English";
        String host = getMailHost(request);
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

        } catch (MessagingException me) {
            Logger.getLogger(EmailUtil.class.getName()).log(Level.SEVERE,
                    "An exception occurred in the sendBatchMail method.", me);
            return false;
        } finally {
            try {
                if (transport != null) {
                    transport.close();
                }
            } catch (MessagingException e) {
                Logger.getLogger(EmailUtil.class.getName()).log(Level.SEVERE,
                        "Unable to close email transport connection.", e);
            }
        }
        return true;
    }

    /**
     * Retrieves the email host from the XML file
     *
     * @param request the HttpServletRequest object
     * @return the email host
     */
    private static String getMailHost(HttpServletRequest request) {

        final int SINGLE_ELEMENT = 0;
        String host = null;
        InputStream input = null;

        try {
            input = request.getServletContext().getResourceAsStream("/WEB-INF/mail.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(input);

            /* Normalize the nodes */
            doc.getDocumentElement().normalize();

            /* Find the host element */
            NodeList nodeList = doc.getElementsByTagName("host");

            /* There should only be one. */
            Node n = nodeList.item(SINGLE_ELEMENT);
            if (n.getNodeType() == Node.ELEMENT_NODE) {
                Element e = (Element) n;
                host = e.getElementsByTagName("value").item(SINGLE_ELEMENT).getTextContent();
            }

        } catch (ParserConfigurationException | SAXException | IOException ex) {
            Logger.getLogger(EmailUtil.class.getName()).log(Level.SEVERE,
                    "An exception occurred in the getMailHost method.", ex);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException ex) {
                    Logger.getLogger(EmailUtil.class.getName()).log(Level.SEVERE,
                            "An exception occurred in the getMailHost method.", ex);
                }
            }
        }

        return host;
    }
}
