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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Random;

/**
 * This class performs functions for hashing and salting.
 *
 * @author Bryan Daniel
 * @version 2, March 16, 2017
 */
public class HashAndSaltUtility {

    /**
     * This method generates and returns a random salt.
     *
     * @return a random string of characters
     */
    public static String getSalt() {
        Random random = new SecureRandom();
        byte[] saltBytes = new byte[32];

        /*
         * The byte array is populated with random bytes by the 
         * nextBytes method.
         */
        random.nextBytes(saltBytes);

        /* The array of bytes is converted to a string and returned. */
        return Base64.getEncoder().encodeToString(saltBytes);
    }

    /**
     * This method combines the password and salt to hash and return as a
     * string.
     *
     * @param password the password
     * @param salt the salt
     * @return the hashed password and salt string
     * @throws NoSuchAlgorithmException
     */
    public static String hashWithSalt(String password, String salt)
            throws NoSuchAlgorithmException {
        String wholePassword = password + salt;
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");

        /* The update method specifies the array of bytes to hash. */
        messageDigest.update(wholePassword.getBytes());

        /* The digest method hashes the array. */
        byte[] messageDigestArray = messageDigest.digest();

        /*
         * The array of bytes is converted to a string.
         * Each byte is represented by 2 hexadecimal characters.
         */
        StringBuilder stringBuilder
                = new StringBuilder(messageDigestArray.length * 2);
        for (byte b : messageDigestArray) {

            /*
             * The bitwise AND operation discards all but the 
             * lowest 8 bits.
             */
            int i = b & 0xff;
            if (i < 16) {
                stringBuilder.append('0');
            }
            stringBuilder.append(Integer.toHexString(i));
        }
        return stringBuilder.toString();
    }
}
