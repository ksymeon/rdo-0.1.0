/*
 * Copyright (c) 2009,2014 Kostas Symeonidis
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.cylog.rdo.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;

import org.cylog.rdo.logger.ClassLogger;

/**
 * Convenience class for acquiring and releasing SQL resources.
 *
 * Keeps track of number of connections open and total count.
 *
 * @author Kostas Symeonidis
 */
public class SqlResourceUtil {
    // ---- Static ------------------------------------------------------------

    private static Log log = new ClassLogger();

    public static int connectionCount = 0;
    public static int connectionTotal = 0;

    // ---- Static Methods ----------------------------------------------------

    /**
     * Get a SQL connection from the given dataSource, wrapping SQL exceptions
     * as Runtime exceptions.
     */
    public static Connection getConnection(DataSource ds) {
        try {
            Connection conn = ds.getConnection();
            connectionCount++;
            connectionTotal++;
            // log.debug("Connection opened, current connections (" + connectionCount + "), total connections so far(" + connectionTotal + ")");
            return conn;
        } catch (SQLException sqle) {
            log.error("Cannot get SQL connection for dataSource : " + sqle.getMessage());
            throw new RuntimeException("Cannot get SQL connection for dataSource " + ds, sqle);
        }
    }

    /**
     * Safely closes the given SQL connection.
     */
    public static void closeResource(Connection conn) {
        if (conn != null) {
            try {
                //log.debug("Closing (1) connection, current connection count (" + connectionCount +
                //          "), total connections so far (" + connectionTotal + ")");
                conn.close();
                connectionCount--;
            } catch (SQLException sqle) {
                log.warn("SQL Exception " + sqle.getMessage() + " when trying to close Connection");
            }
        }
    }

    /**
     * Safely closes the given ResultSet
     */
    public static void closeResource(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException sqle) {
                log.warn("SQL Exception " + sqle.getMessage() + " when trying to close result set");
            }
        }
    }

    /**
     * Safely closes the given Prepared or Callable Statement
     */
    public static void closeResource(Statement pstmt) {
        if (pstmt != null) {
            try {
                pstmt.close();
            } catch (SQLException sqle) {
                log.warn(
                        "SQL Exception " + sqle.getMessage() + " when trying to close Prepared or Callable statement");
            }
        }
    }
}
