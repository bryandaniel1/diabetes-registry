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

import java.sql.Connection;
import java.sql.SQLException;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * This class returns and frees connections to the database.
 *
 * @author Bryan Daniel
 * @version 1, April 8, 2016
 */
public class ConnectionPool {

    /**
     * The connection pool
     */
    private static ConnectionPool pool = null;

    /**
     * The data source
     */
    private static DataSource dataSource = null;

    /**
     * The private constructor ensures that only a single instance of
     * ConnectionPool is created.
     */
    private ConnectionPool() {
        try {
            InitialContext ic = new InitialContext();
            dataSource = (DataSource) ic.lookup("java:/comp/env/jdbc/diabetes_registry");
        } catch (NamingException e) {
            System.err.println(e);
        }
    }

    /**
     * This static method returns the ConnectionPool object.
     *
     * @return the ConnectionPool object
     */
    public synchronized static ConnectionPool getInstance() {
        if (pool == null) {
            pool = new ConnectionPool();
        }
        return pool;
    }

    /**
     * This method returns the connection to the database.
     *
     * @return the connection
     */
    public Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            System.err.println(e);
            return null;
        }
    }

    /**
     * This method frees the connection to the database.
     *
     * @param c the connection
     */
    public void freeConnection(Connection c) {
        try {
            c.close();
        } catch (SQLException e) {
            System.err.println(e);
        }
    }

}
