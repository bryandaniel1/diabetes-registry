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
package clinic;

/**
 * Represents a quality role and responsibility
 *
 * @author Bryan Daniel
 * @version 1, April 8, 2016
 */
public class QualityReference {

    /**
     * Role
     */
    private String role;

    /**
     * Responsibility
     */
    private String responsibility;

    /**
     * Default constructor
     */
    public QualityReference() {
        role = null;
        responsibility = null;
    }

    /**
     * Parameterized constructor
     *
     * @param initRole the role
     * @param initResponsibility the responsibility
     */
    public QualityReference(String initRole,
            String initResponsibility) {
        role = initRole;
        responsibility = initResponsibility;
    }

    /**
     * Returns the role
     *
     * @return the role
     */
    public String getRole() {
        return role;
    }

    /**
     * Returns the responsibility
     *
     * @return the responsibility
     */
    public String getResponsibility() {
        return responsibility;
    }

    /**
     * Sets the role
     *
     * @param role the role
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * Sets the responsibility
     *
     * @param responsibility the responsibility
     */
    public void setResponsibility(String responsibility) {
        this.responsibility = responsibility;
    }

}
