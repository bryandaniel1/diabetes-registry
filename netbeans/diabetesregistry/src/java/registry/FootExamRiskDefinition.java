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
package registry;

import java.io.Serializable;

/**
 * This class represents a foot exam risk category and its definition.
 *
 * @author Bryan Daniel
 * @version 2, March 16, 2017
 */
public class FootExamRiskDefinition implements Serializable {
    
    /**
     * Serial version UID
     */
    private static final long serialVersionUID = -2531168387274864434L;

    /**
     * The foot exam risk category
     */
    private String riskCategory;

    /**
     * The category definition
     */
    private String definition;

    /**
     * Default constructor
     */
    public FootExamRiskDefinition() {
        riskCategory = null;
        definition = null;
    }

    /**
     * Parameterized constructor
     *
     * @param riskCategory the risk category
     * @param definition the risk definition
     */
    public FootExamRiskDefinition(String riskCategory, String definition) {
        this.riskCategory = riskCategory;
        this.definition = definition;
    }

    /**
     * Get the value of riskCategory
     *
     * @return the value of riskCategory
     */
    public String getRiskCategory() {
        return riskCategory;
    }

    /**
     * Set the value of riskCategory
     *
     * @param riskCategory new value of riskCategory
     */
    public void setRiskCategory(String riskCategory) {
        this.riskCategory = riskCategory;
    }

    /**
     * Get the value of definition
     *
     * @return the value of definition
     */
    public String getDefinition() {
        return definition;
    }

    /**
     * Set the value of definition
     *
     * @param definition new value of definition
     */
    public void setDefinition(String definition) {
        this.definition = definition;
    }

}
