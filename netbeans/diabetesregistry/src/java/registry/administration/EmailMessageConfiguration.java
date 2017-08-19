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
import registry.EmailMessage;

/**
 * This class contains the variables to support adding and updating a patient
 * email reminder.
 *
 * @author Bryan Daniel
 * @version 2, March 16, 2017
 */
public class EmailMessageConfiguration implements Serializable {
    
    /**
     * Serial version UID
     */
    private static final long serialVersionUID = -401410513942866294L;
    
    /**
     * The email message configuration ID
     */
    private int identifier;

    /**
     * The email reminder message
     */
    private EmailMessage emailMessage;
    
    /**
     * The filter for recipients of the message
     */
    private String callListFilter;
    
    /**
     * Get the value of identifier
     *
     * @return the value of identifier
     */
    public int getIdentifier() {
        return identifier;
    }

    /**
     * Set the value of identifier
     *
     * @param identifier new value of identifier
     */
    public void setIdentifier(int identifier) {
        this.identifier = identifier;
    }

    /**
     * Get the value of emailMessage
     *
     * @return the value of emailMessage
     */
    public EmailMessage getEmailMessage() {
        return emailMessage;
    }

    /**
     * Set the value of emailMessage
     *
     * @param emailMessage new value of emailMessage
     */
    public void setEmailMessage(EmailMessage emailMessage) {
        this.emailMessage = emailMessage;
    }

    /**
     * Get the value of callListFilter
     *
     * @return the value of callListFilter
     */
    public String getCallListFilter() {
        return callListFilter;
    }

    /**
     * Set the value of callListFilter
     *
     * @param callListFilter new value of callListFilter
     */
    public void setCallListFilter(String callListFilter) {
        this.callListFilter = callListFilter;
    }
}
