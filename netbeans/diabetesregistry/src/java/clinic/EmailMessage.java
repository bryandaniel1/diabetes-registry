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
 * This class contains the subject and content of an email message.
 *
 * @author Bryan Daniel
 * @version 1, April 8, 2016
 */
public class EmailMessage {

    /**
     * The subject
     */
    private String subject;

    /**
     * The language used for the message
     */
    private String language;

    /**
     * The message content
     */
    private String message;

    /**
     * Default constructor
     */
    public EmailMessage() {
        subject = null;
        language = null;
        message = null;
    }

    /**
     * Parameterized constructor
     *
     * @param subject the subject
     * @param language the language
     * @param message the message
     */
    public EmailMessage(String subject, String language, String message) {
        this.subject = subject;
        this.language = language;
        this.message = message;
    }

    /**
     * Get the value of subject
     *
     * @return the value of subject
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Set the value of subject
     *
     * @param subject new value of subject
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * Get the value of language
     *
     * @return the value of language
     */
    public String getLanguage() {
        return language;
    }

    /**
     * Set the value of language
     *
     * @param language new value of language
     */
    public void setLanguage(String language) {
        this.language = language;
    }

    /**
     * Get the value of message
     *
     * @return the value of message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Set the value of message
     *
     * @param message new value of message
     */
    public void setMessage(String message) {
        this.message = message;
    }

}
