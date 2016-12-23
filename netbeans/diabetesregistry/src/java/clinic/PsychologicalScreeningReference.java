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
 * This class describes a psychological screening value and reference
 * information.
 *
 * @author Bryan Daniel
 * @version 1, April 8, 2016
 */
public class PsychologicalScreeningReference {

    /**
     * The score
     */
    private int score;

    /**
     * The severity
     */
    private String severity;

    /**
     * The proposed actions
     */
    private String proposedActions;

    /**
     * Default constructor
     */
    public PsychologicalScreeningReference() {
        severity = null;
        proposedActions = null;
    }

    /**
     * Parameterized constructor
     *
     * @param score the score
     * @param severity the severity
     * @param proposedActions the proposed actions
     */
    public PsychologicalScreeningReference(int score, String severity, String proposedActions) {
        this.score = score;
        this.severity = severity;
        this.proposedActions = proposedActions;
    }

    /**
     * Get the value of proposedActions
     *
     * @return the value of proposedActions
     */
    public String getProposedActions() {
        return proposedActions;
    }

    /**
     * Set the value of proposedActions
     *
     * @param proposedActions new value of proposedActions
     */
    public void setProposedActions(String proposedActions) {
        this.proposedActions = proposedActions;
    }

    /**
     * Get the value of score
     *
     * @return the value of score
     */
    public int getScore() {
        return score;
    }

    /**
     * Set the value of score
     *
     * @param score the score
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * Get the value of severity
     *
     * @return the value of severity
     */
    public String getSeverity() {
        return severity;
    }

    /**
     * Set the value of severity
     *
     * @param severity new value of severity
     */
    public void setSeverity(String severity) {
        this.severity = severity;
    }
}
