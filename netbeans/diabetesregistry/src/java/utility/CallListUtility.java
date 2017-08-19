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

import java.util.ArrayList;
import registry.Patient;

/**
 * This utility class contains methods for sorting the patient call lists in a
 * manner chosen by the user.
 *
 * @author Bryan Daniel
 * @version 2, March 16, 2017
 */
public class CallListUtility {

    /**
     * This enum type holds indicator values for the sort type.
     */
    public enum SortType {

        /**
         * The type indicating a sort by last name
         */
        LAST_NAME, 

        /**
         * The type indicating a sort by last measurement date
         */
        LAST_MEASUREMENT_DATE
    }

    /**
     * This method sorts the patients in the given list in ascending order.
     *
     * @param callList the list of patients in the call list
     * @param sortBy the indicator of sort type
     */
    public static void sortPatients(ArrayList<Patient> callList, SortType sortBy) {

        int numberOfPatients = callList.size();
        int index; 
        int indexOfNextEarliest;
        for (index = 0; index < numberOfPatients - 1; index++) {
            indexOfNextEarliest = indexOfEarliest(index,
                    callList, numberOfPatients, sortBy);
            swap(index, indexOfNextEarliest, callList);
        }
    }

    /**
     * This method returns the index of the earliest patient. Since the last
     * measurement date for a patient may be null, the comparison for that sort
     * type must handle the possibility of null dates.
     *
     * @param index the index
     * @param callList the list of patients
     * @param numberOfPatients the number of patients
     * @param sortBy the indicator of sort type
     * @return the index of the earliest patient
     */
    private static int indexOfEarliest(int index,
            ArrayList<Patient> callList, int numberOfPatients, SortType sortBy) {
        Patient earliestPatient = callList.get(index);
        int indexOfMin = index;
        int i;
        if (sortBy == SortType.LAST_NAME) {
            for (i = index + 1; i < numberOfPatients; i++) {
                if (callList.get(i).getLastName()
                        .compareTo(earliestPatient.getLastName()) < 0) {
                    earliestPatient = callList.get(i);
                    indexOfMin = i;
                }
            }
        } else if (sortBy == SortType.LAST_MEASUREMENT_DATE) {
            for (i = index + 1; i < numberOfPatients; i++) {
                if ((callList.get(i).getDateOfLastMeasurement() == null)
                        || ((earliestPatient.getDateOfLastMeasurement() != null)
                        && (callList.get(i).getDateOfLastMeasurement()
                        .compareTo(earliestPatient.getDateOfLastMeasurement()) < 0))) {
                    earliestPatient = callList.get(i);
                    indexOfMin = i;
                }
            }
        }
        return indexOfMin;
    }

    /**
     * This method sorts the patients in the given list in descending order.
     *
     * @param callList the list of patients in the call list
     * @param sortBy the indicator of sort type
     */
    public static void reverseSortPatients(ArrayList<Patient> callList, SortType sortBy) {

        int numberOfPatients = callList.size();
        int index;
        int indexOfNextLatest;
        for (index = 0; index < numberOfPatients - 1; index++) {
            indexOfNextLatest = indexOfLatest(index,
                    callList, numberOfPatients, sortBy);
            swap(index, indexOfNextLatest, callList);
        }
    }

    /**
     * This method returns the index of the latest patient. Since the last
     * measurement date for a patient may be null, the comparison for that sort
     * type must handle the possibility of null dates.
     *
     * @param index the index
     * @param callList the list of patients
     * @param numberOfPatients the number of patients
     * @param sortBy the indicator of sort type
     * @return the index of the earliest patient
     */
    private static int indexOfLatest(int index,
            ArrayList<Patient> callList, int numberOfPatients, SortType sortBy) {
        Patient latestPatient = callList.get(index);
        int indexOfMax = index;
        int i;
        if (sortBy == SortType.LAST_NAME) {
            for (i = index + 1; i < numberOfPatients; i++) {
                if (callList.get(i).getLastName()
                        .compareTo(latestPatient.getLastName()) > 0) {
                    latestPatient = callList.get(i);
                    indexOfMax = i;
                }
            }
        } else if (sortBy == SortType.LAST_MEASUREMENT_DATE) {
            for (i = index + 1; i < numberOfPatients; i++) {
                if ((latestPatient.getDateOfLastMeasurement() == null)
                        || ((callList.get(i).getDateOfLastMeasurement() != null)
                        && (callList.get(i).getDateOfLastMeasurement()
                        .compareTo(latestPatient.getDateOfLastMeasurement()) > 0))) {
                    latestPatient = callList.get(i);
                    indexOfMax = i;
                }
            }
        }
        return indexOfMax;
    }

    /**
     * This method swaps patients in the call list.
     *
     * @param index index of patient to be swapped
     * @param nextIndex the index of the next patient in order
     * @param callList the call list
     */
    private static void swap(int index, int nextIndex,
            ArrayList<Patient> callList) {
        Patient temp;
        temp = callList.get(index);
        callList.set(index, callList.get(nextIndex));
        callList.set(nextIndex, temp);
    }
}
