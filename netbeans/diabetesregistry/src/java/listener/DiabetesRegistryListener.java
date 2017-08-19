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
package listener;

import data.ReferencesDataAccess;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import registry.ReferenceContainer;
import utility.ConfigurationManager;
import utility.HashAndSaltUtility;

/**
 * This web application life cycle listener is used to set values used
 * throughout the application.
 *
 * @author Bryan Daniel
 * @version 2, March 16, 2017
 */
public class DiabetesRegistryListener implements ServletContextListener {

    /**
     * This method executes when the context is initialized to set the values
     * for the ReferenceContainer object used throughout the application, set
     * the value for the string of reference characters used in the application,
     * and to set the value for the data source JNDI name used in the
     * application.
     *
     * @param sce the servlet context event
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext sc = sce.getServletContext();

        // setting the data source JNDI name
        ConfigurationManager.DATASOURCE_JNDI_NAME_MAP
                .put(ConfigurationManager.DATASOURCE_JNDI_NAME_KEY, ConfigurationManager.getDataSource(sc));

        // setting an attribute to hold the ReferenceContainer object
        ReferenceContainer rc = ReferencesDataAccess.getReferenceContainer();
        sc.setAttribute("references", rc);

        // setting an attribute to hold the clinic number
        String referenceCharacters = ConfigurationManager.getClinicNumber(sc);        
        sc.setAttribute("referenceCharacters", referenceCharacters);
    }

    /**
     * Executes when the context is destroyed
     *
     * @param sce the servlet context event
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        /* No cleanup */
    }
}
