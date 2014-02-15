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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * TODO description for class JdbcUtil
 *
 * @author Kostas Symeonidis
 */
public class JdbcUtil {
    // ---- Static ------------------------------------------------------------

    /**
     * Gets a java.util.Date from a ResultSet, returning java null if the column was a SQL null.
     *
     * @param rs         - the result set to operate on
     * @param columnName - the name of the column to get
     */
    public static Character getCharOrNull(ResultSet rs, String columnName)
            throws SQLException {
        String stringValue = rs.getString(columnName);
        Character charValue = null;
        if (stringValue != null) {
            if (stringValue.length() == 1) {
                charValue = stringValue.charAt(0);
            } else if (stringValue.length() > 1) {
                throw new SQLException(
                        "String value too long for getCharOrNull method " + stringValue);
            }
        }

        return charValue;
    }

    /**
     * Gets a java.util.Date from a ResultSet, returning java null if the column was a SQL null.
     *
     * @param rs         - the result set to operate on
     * @param columnName - the name of the column to get
     */
    public static java.util.Date getDateOrNull(ResultSet rs, String columnName)
            throws SQLException {
        Timestamp ts = rs.getTimestamp(columnName);
        return ts == null ? null : new java.util.Date(ts.getTime());
    }

    /**
     * Gets an integer from a ResultSet, returning java null if the column was a SQL null.
     *
     * @param rs         - the result set to operate on
     * @param columnName - the name of the column to get
     */
    public static Integer getIntOrNull(ResultSet rs, String columnName)
            throws SQLException {
        int i = rs.getInt(columnName);
        return rs.wasNull() ? null : i;
    }

    /**
     * Gets a long from a ResultSet, returning java null if the column was a SQL null.
     *
     * @param rs         - the result set to operate on
     * @param columnName - the name of the column to get
     */
    public static Long getLongOrNull(ResultSet rs, String columnName)
            throws SQLException {
        long l = rs.getLong(columnName);
        return rs.wasNull() ? null : l;
    }

    /**
     * Gets a float from a ResultSet, returning java null if the column was a SQL null.
     *
     * @param rs         - the result set to operate on
     * @param columnName - the name of the column to get
     */
    public static Float getFloatOrNull(ResultSet rs, String columnName)
            throws SQLException {
        float f = rs.getFloat(columnName);
        return rs.wasNull() ? null : f;
    }

    /**
     * Gets a double from a ResultSet, returning java null if the column was a SQL null.
     *
     * @param rs         - the result set to operate on
     * @param columnName - the name of the column to get
     */
    public static Double getDoubleOrNull(ResultSet rs, String columnName)
            throws SQLException {
        double d = rs.getDouble(columnName);
        return rs.wasNull() ? null : d;
    }

    /**
     * Gets a boolean from a ResultSet for a <emph>non-nullable</emph>
     * VARCHAR2(1) column which contains 'Y' for true or 'N' for false.
     *
     * @param rs         - the result set to operate on
     * @param columnName - the name of the column to get
     */
    public static boolean getYNString(ResultSet rs, String columnName)
            throws SQLException {
        String s = rs.getString(columnName);
        return "Y".equals(s);
    }

    /**
     * Gets an Boolean from a ResultSet, returning null if the column was null,
     * or the appropriate Boolean value where a 'Y' column value would return
     * true and anything else false
     *
     * @param rs         - the result set to operate on
     * @param columnName - the name of the column to get
     */
    public static Boolean getYNStringOrNull(ResultSet rs, String columnName)
            throws SQLException {
        String s = rs.getString(columnName);
        return rs.wasNull() ? null : "Y".equals(s);
    }
}
