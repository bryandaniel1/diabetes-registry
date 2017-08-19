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
 * The class represents a telephone follow-up status and its definition
 *
 * @author Bryan Daniel
 * @version 2, March 16, 2017
 */
public class TelephoneFollowUpDefinition implements Serializable {
    
    /**
     * Serial version UID
     */
    private static final long serialVersionUID = -1731041164175230560L;

    /**
     * The code
     */
    private String code;

    /**
     * The code definition
     */
    private String definition;

    /**
     * Default constructor
     */
    public TelephoneFollowUpDefinition() {
        code = null;
        definition = null;
    }

    /**
     * Parameterized constructor
     *
     * @param code the code
     * @param definition the definition
     */
    public TelephoneFollowUpDefinition(String code, String definition) {
        this.code = code;
        this.definition = definition;
    }

    /**
     * Get the value of code
     *
     * @return the value of code
     */
    public String getCode() {
        return code;
    }

    /**
     * Set the value of code
     *
     * @param code new value of code
     */
    public void setCode(String code) {
        this.code = code;
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
