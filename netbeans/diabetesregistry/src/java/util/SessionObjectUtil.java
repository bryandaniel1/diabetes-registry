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

import javax.servlet.http.HttpSession;

/**
 * Resets session objects to their default values
 *
 * @author Bryan Daniel
 * @version 1, April 8, 2016
 */
public class SessionObjectUtil {

    /**
     * The patient string
     */
    private static final String PATIENT = "patient";

    /**
     * The progress note string
     */
    private static final String PROGRESS_NOTE = "progressNote";

    /**
     * The progress dates string
     */
    private static final String PROGRESS_DATES = "progressDates";

    /**
     * The call list patients string
     */
    private static final String CALL_LIST_PATIENTS = "callListPatients";

    /**
     * The recent checklist items string
     */
    private static final String RECENT_CHECKLIST_ITEMS = "recentChecklistItems";

    /**
     * Resets the objects associated with clinics to default values
     *
     * @param session the HttpSession object
     */
    public static void resetClinicObjects(HttpSession session) {

        session.setAttribute(PATIENT, null);
        session.setAttribute(PROGRESS_NOTE, null);
        session.setAttribute(PROGRESS_DATES, null);
        session.setAttribute(CALL_LIST_PATIENTS, null);
        session.setAttribute(RECENT_CHECKLIST_ITEMS, null);

    }

    /**
     * Resets the objects associated with patients to default values
     *
     * @param session the HttpSession object
     */
    public static void resetPatientObjects(HttpSession session) {

        session.setAttribute(PROGRESS_NOTE, null);
        session.setAttribute(PROGRESS_DATES, null);
        session.setAttribute(CALL_LIST_PATIENTS, null);
        session.setAttribute(RECENT_CHECKLIST_ITEMS, null);

    }
}
