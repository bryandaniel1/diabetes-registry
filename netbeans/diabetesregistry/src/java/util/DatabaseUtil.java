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

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class closes database objects
 *
 * @author Bryan Daniel
 * @version 1, April 8, 2016
 */
public class DatabaseUtil {

    /**
     * This static method closes callable statements
     *
     * @param cs the callable statement
     */
    public static void closeCallableStatement(CallableStatement cs) {
        try {
            if (cs != null) {
                cs.close();
            }
        } catch (SQLException e) {
            System.err.println(e);
        }
    }

    /**
     * This static method closes result sets
     *
     * @param rs the result set
     */
    public static void closeResultSet(ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            System.err.println(e);
        }
    }
}
