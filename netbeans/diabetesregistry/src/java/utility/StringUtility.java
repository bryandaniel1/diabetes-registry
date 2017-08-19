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
package utility;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class performs pattern matching and various other functions on strings
 * required by the application.
 *
 * @author Bryan Daniel
 * @version 2, March 16, 2017
 */
public class StringUtility {

    /**
     * The password pattern contains at least one lower-case letter, one
     * upper-case letter, one number, one special character, no spaces, and is
     * between 10 and 20 characters inclusive.
     */
    private static final String PASSWORD_PATTERN
            = "((?=.*\\d)(?=.*[a-z])(?=\\S+$)(?=.*[A-Z])"
            + "(?=.*[!\"#$%&'()*+,-./:;<=>?@\\[\\]^_`{|}~]).{10,20})";

    /**
     * The SQL date pattern
     */
    private static final String DATE_PATTERN
            = "^(\\d{4})-((0[1-9])|(1[0-2]))-(0[1-9]|[12][0-9]|3[01])$";

    /**
     * The limit for short VARCHAR
     */
    private static final int SHORT_STRING_LIMIT = 50;

    /**
     * The limit for username VARCHAR
     */
    private static final int USERNAME_STRING_LIMIT = 100;

    /**
     * The limit for an email VARCHAR
     */
    private static final int EMAIL_STRING_LIMIT = 255;

    /**
     * The limit for long VARCHAR
     */
    private static final int LONG_STRING_LIMIT = 1000;

    /**
     * The secure RANDOM number generator
     */
    private static final SecureRandom RANDOM = new SecureRandom();

    /**
     * The bit length for RANDOM number generation
     */
    private static final int BIT_LENGTH = 80;

    /**
     * The RADIX for RANDOM number generation
     */
    private static final int RADIX = 36;

    /**
     * This method checks to see if the given password matches the password
     * pattern.
     *
     * @param password the password
     * @return the boolean indicating whether or not there is a match
     */
    public static boolean passwordCheck(String password) {
        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    /**
     * This method checks to see if the given date matches the date pattern for
     * the database.
     *
     * @param dateString the date string
     * @return the boolean indicating whether or not there is a match
     */
    public static boolean dateCheck(String dateString) {
        Pattern pattern = Pattern.compile(DATE_PATTERN);
        Matcher matcher = pattern.matcher(dateString);
        return matcher.matches();
    }

    /**
     * Checks the string length to determine if it is too long for a short
     * VARCHAR database attribute
     *
     * @param word the string
     * @return the boolean for excessive length
     */
    public static boolean tooLongForShortVarChar(String word) {
        return word.length() > SHORT_STRING_LIMIT;
    }

    /**
     * Checks the string length to determine if it is too long for a username
     * VARCHAR database attribute
     *
     * @param word the string
     * @return the boolean for excessive length
     */
    public static boolean tooLongForUsernameVarChar(String word) {
        return word.length() > USERNAME_STRING_LIMIT;
    }

    /**
     * Checks the string length to determine if it is too long for an
     * email-length database attribute
     *
     * @param word the string
     * @return the boolean for excessive length
     */
    public static boolean tooLongForEmailVarChar(String word) {
        return word.length() > EMAIL_STRING_LIMIT;
    }

    /**
     * Checks the string length to determine if it is too long for a long
     * VARCHAR string database attribute
     *
     * @param word the string
     * @return the boolean for excessive length
     */
    public static boolean tooLongForLongVarChar(String word) {
        return word.length() > LONG_STRING_LIMIT;
    }

    /**
     * This method sorts the strings in the given list.
     *
     * @param stringList the string list
     */
    public static void sortStrings(ArrayList<String> stringList) {

        int numberOfStrings = stringList.size();
        int index, indexOfNextEarliest;
        for (index = 0; index < numberOfStrings - 1; index++) {
            indexOfNextEarliest = indexOfEarliest(index,
                    stringList, numberOfStrings);
            swap(index, indexOfNextEarliest, stringList);
        }
    }

    /**
     * This method returns the index of the earliest string.
     *
     * @param index the index
     * @param stringList the list of strings
     * @param numberOfStrings the number of strings
     * @return the index of the earliest string
     */
    private static int indexOfEarliest(int index,
            ArrayList<String> stringList, int numberOfStrings) {
        String earliestString = stringList.get(index);
        int indexOfMin = index;
        int i;
        for (i = index + 1; i < numberOfStrings; i++) {
            if (stringList.get(i).compareTo(earliestString) < 0) {
                earliestString = stringList.get(i);
                indexOfMin = i;
            }
        }
        return indexOfMin;
    }

    /**
     * This method swaps strings in the string list.
     *
     * @param index index of string to be swapped
     * @param indexOfNextEarliest the index of the earlier string
     * @param stringList the string list
     */
    private static void swap(int index, int indexOfNextEarliest,
            ArrayList<String> stringList) {
        String temp;
        temp = stringList.get(index);
        stringList.set(index, stringList.get(indexOfNextEarliest));
        stringList.set(indexOfNextEarliest, temp);
    }

    /**
     * This method returns a temporary password containing randomly generated
     * characters for new users and password resets.
     *
     * @return the temporary password
     */
    public static String generateTemporaryPassword() {

        String tempPassword;
        String newString = new BigInteger(BIT_LENGTH, RANDOM).toString(RADIX);
        char[] charString = newString.toCharArray();
        StringBuilder keyString = new StringBuilder();

        for (int i = 0; i < charString.length; i++) {
            if (charString[i] > '9') {
                if (i % 2 == 0) {
                    Character c = Character.toUpperCase(charString[i]);
                    keyString.append(c);
                } else {
                    keyString.append(charString[i]);
                }
            } else {
                if (i % 3 == 0) {
                    String specialCharString = "!@#$%^&*()";
                    int index = RANDOM.nextInt(specialCharString.length());
                    keyString.append(specialCharString.charAt(index));
                } else {
                    keyString.append(charString[i]);
                }
            }
        }

        tempPassword = keyString.toString();

        if (!passwordCheck(tempPassword)) {
            tempPassword = generateTemporaryPassword();
        }

        return tempPassword;
    }

    /**
     * This method invokes the database procedure for creating a user name for a
     * new user.
     *
     * @param firstName the first name
     * @param lastName the last name
     * @return the new user name
     */
    public static String generateUserName(String firstName, String lastName) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        CallableStatement cs = null;
        String newUserName = null;

        try {
            cs = connection.prepareCall("{CALL generateUserName(?, ?, ?)}");
            cs.setString(1, firstName);
            cs.setString(2, lastName);
            cs.registerOutParameter(3, java.sql.Types.VARCHAR);

            /* no result set to return */
            cs.execute();

            /* grab new user name */
            newUserName = cs.getString(3);

        } catch (SQLException e) {
            Logger.getLogger(StringUtility.class.getName())
                    .log(Level.SEVERE, null, e);
            return null;
        } finally {
            DatabaseUtility.closeCallableStatement(cs);
            pool.freeConnection(connection);
        }
        return newUserName;
    }
}
