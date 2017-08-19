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
package registry.administration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class contains a list of all email patient reminder messages and the
 * list of filters for patient recipients.
 *
 * @author Bryan Daniel
 * @version 2, March 16, 2017
 */
public class EmailMessageConfigurationContainer implements Serializable {
    
    /**
     * Serial version UID
     */
    private static final long serialVersionUID = -7600144456323011753L;
    
    /**
     * The map of email message configurations
     */
    private HashMap<Integer, EmailMessageConfiguration> emailMessageConfigurations;
    
    /**
     * The list of all filters
     */
    private ArrayList<String> allFilters;    

    /**
     * Get the value of allFilters
     *
     * @return the value of allFilters
     */
    public ArrayList<String> getAllFilters() {
        return allFilters;
    }

    /**
     * Set the value of allFilters
     *
     * @param allFilters new value of allFilters
     */
    public void setAllFilters(ArrayList<String> allFilters) {
        this.allFilters = allFilters;
    }

    /**
     * Get the value of emailMessageConfigurations
     *
     * @return the value of emailMessageConfigurations
     */
    public HashMap<Integer, EmailMessageConfiguration> getEmailMessageConfigurations() {
        return emailMessageConfigurations;
    }

    /**
     * Set the value of emailMessageConfigurations
     *
     * @param emailMessageConfigurations new value of emailMessageConfigurations
     */
    public void setEmailMessageConfigurations(HashMap<Integer, EmailMessageConfiguration> emailMessageConfigurations) {
        this.emailMessageConfigurations = emailMessageConfigurations;
    }
}
