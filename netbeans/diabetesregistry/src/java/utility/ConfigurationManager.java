/*
 * Copyright 2017 Bryan Daniel.
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

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * This class handles configuration properties needed for application
 * functionality.
 *
 * @author Bryan Daniel
 * @version 2, March 16, 2017
 */
public class ConfigurationManager {

    /**
     * The key to the data source JNDI name value
     */
    public static final String DATASOURCE_JNDI_NAME_KEY = "dataSourceName";

    /**
     * The map holding the JNDI name for the data source
     */
    public static final HashMap<String, String> DATASOURCE_JNDI_NAME_MAP = new HashMap<>();

    /**
     * The location of the configuration file
     */
    private static final String CONFIGURATION_PROPERTIES_LOCATION = "/WEB-INF/configuration.xml";

    /**
     * The index for a single element
     */
    private static final int SINGLE_ELEMENT = 0;

    /**
     * This method retrieves the email host from the configuration file.
     *
     * @param sc the ServletContext object
     * @return the email host
     */
    public static String getMailHost(ServletContext sc) {

        String host = null;
        String baseElement = "host";
        String propertyName = "value";

        try {
            host = readConfigurationFile(sc, baseElement, propertyName);

        } catch (ParserConfigurationException | SAXException | IOException ex) {
            Logger.getLogger(ConfigurationManager.class.getName()).log(Level.SEVERE,
                    "An exception occurred in getMailHost method.", ex);
        }

        return host;
    }

    /**
     * This method retrieves the clinic number as a String object from the
     * configuration file.
     *
     * @param sc the ServletContext object
     * @return the clinic number as a string
     */
    public static String getClinicNumber(ServletContext sc) {

        String clinicNumber = null;
        String baseElement = "clinic";
        String propertyName = "number";

        try {
            clinicNumber = readConfigurationFile(sc, baseElement, propertyName);

        } catch (ParserConfigurationException | SAXException | IOException ex) {
            Logger.getLogger(ConfigurationManager.class.getName()).log(Level.SEVERE,
                    "An exception occurred in getClinicNumber method.", ex);
        }

        return clinicNumber;
    }

    /**
     * This method retrieves the JNDI name for the data source from the
     * configuration file.
     *
     * @param sc the ServletContext object
     * @return the name of the data source
     */
    public static String getDataSource(ServletContext sc) {

        String jndiName = null;
        String baseElement = "datasource";
        String propertyName = "jndi";

        try {
            jndiName = readConfigurationFile(sc, baseElement, propertyName);

        } catch (ParserConfigurationException | SAXException | IOException ex) {
            Logger.getLogger(ConfigurationManager.class.getName()).log(Level.SEVERE,
                    "An exception occurred in getDataSource method.", ex);
        }

        return jndiName;
    }

    /**
     * This method reads the configuration file to find the property value for
     * the element and property name given.
     *
     * @param sc the ServletContext object
     * @param baseElement the element containing the desired property value
     * @param propertyName the name of the desired property
     * @return the configuration property value
     * @throws javax.xml.parsers.ParserConfigurationException
     * @throws org.xml.sax.SAXException
     * @throws java.io.IOException
     */
    private static String readConfigurationFile(ServletContext sc, String baseElement,
            String propertyName) throws ParserConfigurationException, SAXException, IOException {

        String propertyValue = null;

        InputStream input = sc.getResourceAsStream(CONFIGURATION_PROPERTIES_LOCATION);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(input);

        /* Normalize the nodes */
        doc.getDocumentElement().normalize();

        /* Find the base element */
        NodeList nodeList = doc.getElementsByTagName(baseElement);

        /* There should only be one node in the element. */
        Node n = nodeList.item(SINGLE_ELEMENT);
        if (n.getNodeType() == Node.ELEMENT_NODE) {
            Element e = (Element) n;
            propertyValue = e.getElementsByTagName(propertyName).item(SINGLE_ELEMENT)
                    .getTextContent();
        }
        if (input != null) {
            try {
                input.close();
            } catch (IOException ex) {
                Logger.getLogger(ConfigurationManager.class.getName()).log(Level.SEVERE,
                        "An exception occurred in readConfigurationFile method.", ex);
            }
        }

        return propertyValue;
    }
}
