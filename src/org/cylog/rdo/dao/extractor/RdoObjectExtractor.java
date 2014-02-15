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
package org.cylog.rdo.dao.extractor;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;

import org.cylog.rdo.bean.RdoMapper;
import org.cylog.rdo.bean.RdoOperation;
import org.cylog.rdo.exception.ModelNotSupportedException;
import org.cylog.rdo.logger.ClassLogger;
import org.cylog.rdo.util.JdbcUtil;
import org.cylog.rdo.util.SqlResourceUtil;

/**
 * TODO description for class RdoResultSetExtractor
 *
 * TODO throw Framework exceptions instead of RuntimeExceptions
 *
 * @author Kostas Symeonidis
 */
public class RdoObjectExtractor {
    // ---- Static ------------------------------------------------------------

    public static Log log = new ClassLogger();

    // ---- Constants ---------------------------------------------------------

    /**
     * Required value of an Object array with a single null value, so that
     * it can be passed to setter invocations to denote an empty value
     */
    public static final Object[] NULL_VALUE = new Object[] {null};

    // ---- Member Variables --------------------------------------------------

    private DataSource dataSource;
    private RdoMapper rdoMapper;

    private boolean lowerCaseEnums = true;

    // ---- Constructors ------------------------------------------------------

    /**
     * Constructs a ResultSetExtractor for the given rdoMapper
     */
    public RdoObjectExtractor(DataSource dataSource, RdoMapper rdoMapper) {
        this.dataSource = dataSource;
        this.rdoMapper = rdoMapper;
    }

    // ---- Properties --------------------------------------------------------

    public boolean isLowerCaseEnums() {
        return lowerCaseEnums;
    }

    public void setLowerCaseEnums(boolean lowerCaseEnums) {
        this.lowerCaseEnums = lowerCaseEnums;
    }

    // ---- Public methods ----------------------------------------------------

    /**
     * @param keys
     * @return
     */
    public Object extractObject(Object... keys) {
        return extractOrMergeObject(null, rdoMapper.getSqlStatement(keys), keys);
    }

    /**
     * @param whereClause
     * @param args
     * @return
     */
    public Object extractObjectWhere(String whereClause, Object... args) {
        return extractOrMergeObject(null, rdoMapper.getSqlStatementWithWhereClause(whereClause),
                                    args);
    }

    /**
     * @param sql
     * @param args
     * @return
     */
    public Object extractObjectFromSql(String sql, Object... args) {
        return extractOrMergeObject(null, sql, args);
    }

    /**
     * @param keys
     * @return
     */
    public Object extractAndMergeObject(Object dto, Object... keys) {
        return extractOrMergeObject(dto, rdoMapper.getSqlStatement(keys), keys);
    }

    /**
     * @param dto
     * @param whereClause
     * @param args
     * @return
     */
    public Object extractAndMergeObjectWhere(Object dto, String whereClause, Object... args) {
        return extractOrMergeObject(dto, rdoMapper.getSqlStatementWithWhereClause(whereClause),
                                    args);
    }

    /**
     * @param dto
     * @param sql
     * @param args
     * @return
     */
    public Object extractAndMergeObjectFromSql(Object dto, String sql, Object... args) {
        return extractOrMergeObject(dto, sql, args);
    }

    /**
     * @param dto
     * @param sql
     * @param params
     * @return
     */
    private Object extractOrMergeObject(Object dto, String sql, Object[] params) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = SqlResourceUtil.getConnection(dataSource);
            pstmt = conn.prepareStatement(sql);
            setPreparedStatementParameters(pstmt, params);

            log.debug(" - Executing query SQL = " + sql + "], params=" + Arrays.asList(params));
            long startTime = System.currentTimeMillis();
            rs = pstmt.executeQuery();
            if (rs.next()) {
                dto = extractObjectFromResultSetRow(rs, dto);
            }
            log.info(" - Query executed read in " + (System.currentTimeMillis() - startTime) +
                     " milliseconds, result count = " + (dto == null ? 0 : 1));

            return dto;
        } catch (SQLException sqle) {
            throw new RuntimeException(
                    "SQL Exception while extracting object ... " + sqle.getMessage(), sqle);
        } finally {
            SqlResourceUtil.closeResource(rs);
            SqlResourceUtil.closeResource(pstmt);
            SqlResourceUtil.closeResource(conn);
        }
    }

    /**
     * @param whereClause
     * @param params
     * @return
     */
    public List extractList(String whereClause, Object... params) {
        String sql = rdoMapper.getSqlStatementWithWhereClause(whereClause);
        return extractListFromSql(sql, params);
    }

    /**
     * @param params
     * @return
     */
    @SuppressWarnings("unchecked")
    public List extractListFromSql(String sql, Object... params) {
        List list = new ArrayList();

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = SqlResourceUtil.getConnection(dataSource);
            pstmt = conn.prepareStatement(sql);
            if (params != null) {
                setPreparedStatementParameters(pstmt, params);
            }

            log.debug(" - Executing query SQL = [" + sql + "], params=" + Arrays.asList(params));
            long startTime = System.currentTimeMillis();
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Object dto = extractObjectFromResultSetRow(rs, null);
                list.add(dto);
            }
            log.info(" - Query executed read in " + (System.currentTimeMillis() - startTime) +
                     " milliseconds, result count = " + list.size());

            return list;
        } catch (SQLException sqle) {
            throw new RuntimeException(
                    "SQL Exception while extracting object ... " + sqle.getMessage() + " SQL:" + sql, sqle);
        } finally {
            SqlResourceUtil.closeResource(rs);
            SqlResourceUtil.closeResource(pstmt);
            SqlResourceUtil.closeResource(conn);
        }
    }

    // ---- Private methods ---------------------------------------------------

    /**
     * Sets parameters from an array to a result set. Enums are normally converted
     * to lower case before passed to the database, unless the isLowerCaseEnums property
     * is set to FALSE.
     */
    private void setPreparedStatementParameters(PreparedStatement pstmt, Object[] params) {
        if (params == null) {
            return;
        }

        //log.debug("Setting PreparedStatement parameters");
        try {
            int i = 1;
            for (Object param : params) {
                if (param == null)   // if NULL, set the NULL value as a varchar, Oracle is happy with it
                {
                    //log.debug("Setting parameter " + i + " to NULL");
                    pstmt.setNull(i, Types.VARCHAR);
                } else if (param.getClass().isEnum()) {
                    String enumValue = param.toString();
                    if ((enumValue != null) && isLowerCaseEnums()) {
                        enumValue = enumValue.toLowerCase();
                    }
                    // log.debug("Setting parameter " + i + " to enum value " + enumValue);
                    pstmt.setString(i, enumValue);
                } else {
                    //log.debug("Setting parameter " + i + " to " + param);
                    pstmt.setObject(i, param);
                }

                i++;  // increase param counter
            }
        } catch (SQLException sqle) {
            throw new RuntimeException(
                    "SQL exception when setting params for prepared statement " + sqle.getMessage(),
                    sqle);
        }
    }

    /**
     * Extracts an object from a ResultSet's row. The object returned is the same as the object
     * passed
     * plus the new fields from the result set. If the passed object is NULL a new instance of the
     * internal rdoMapper's model will be created.
     *
     * Reading from the result set respects NULL values and is invoking the methods of the
     * underlying
     * objects
     *
     * @param dto an instance of an existing objec to be populated, OR NULL for a new instance to be
     *            created
     */
    @SuppressWarnings("unchecked")
    private Object extractObjectFromResultSetRow(ResultSet rs, Object dto) {
        try {
            try {
                if (dto == null) {
                    dto = rdoMapper.getClassModel().createNewInstance();
                }

                log.debug("========== setting values to object =====================");
                for (RdoOperation ro : rdoMapper.getReadOperations()) {
                    Object o = rs.getObject(ro.getColumnName());
                    log.debug(
                            " : " + ro.getColumnName() + " : " + (o != null ? o.getClass() : "NULL")
                            + " = " + o);

                    switch (ro.getMethodModel().getDataType()) {
                        case INT:
                            Integer intValue = JdbcUtil.getIntOrNull(rs, ro.getColumnName());

                            log.debug(
                                    " +++ Will set int value to " + intValue + " using rdoop " + ro);
                            ro.getMethod().invoke(dto, intValue);
                            break;

                        case LONG:
                            Long longValue = JdbcUtil.getLongOrNull(rs, ro.getColumnName());
                            log.debug(
                                    " +++ Will set long value to " + longValue + " using rdoop " + ro);
                            ro.getMethod().invoke(dto, longValue);
                            break;

                        case FLOAT:
                            Float floatValue = JdbcUtil.getFloatOrNull(rs, ro.getColumnName());
                            log.debug(
                                    " +++ Will set float value to " + floatValue + " using rdoop " + ro);
                            ro.getMethod().invoke(dto, floatValue);
                            break;

                        case DOUBLE:
                            Double doubleValue = JdbcUtil.getDoubleOrNull(rs, ro.getColumnName());
                            log.debug(
                                    " +++ Will set double value to " + doubleValue + " using rdoop " + ro);
                            ro.getMethod().invoke(dto, doubleValue);
                            break;

                        case STRING:
                            String stringValue = rs.getString(ro.getColumnName());
                            log.debug(
                                    " +++ Will set string value to \"" + stringValue + "\" using rdoop " + ro);
                            ro.getMethod().invoke(dto, stringValue);
                            break;

                        case CHAR:
                            Character charValue = JdbcUtil.getCharOrNull(rs, ro.getColumnName());
                            log.debug(
                                    " +++ Will set date value to \"" + charValue + "\" using rdoop " + ro);
                            ro.getMethod().invoke(dto, charValue);
                            break;

                        case DATE:
                            java.util.Date dateValue = JdbcUtil.getDateOrNull(rs,
                                                                              ro.getColumnName());
                            log.debug(
                                    " +++ Will set date value to \"" + dateValue + "\" using rdoop " + ro);
                            ro.getMethod().invoke(dto, dateValue);
                            break;

                        case BOOLEAN:
                            Boolean booleanValue = JdbcUtil.getYNStringOrNull(rs,
                                                                              ro.getColumnName());
                            log.debug(
                                    " +++ Will set boolean (YN) value to \"" + booleanValue + "\" using rdoop " + ro);
                            ro.getMethod().invoke(dto, booleanValue);
                            break;

                        case ENUM:
                            String enumStringValue = rs.getString(ro.getColumnName());
                            log.debug(
                                    " +++ Will set ENUM value to \"" + enumStringValue + "\" using rdoop " + ro);

                            if (enumStringValue == null) {
                                log.debug(" +++ invoking enum with NULL_VALUE ");
                                ro.getMethod().invoke(dto, NULL_VALUE);
                            } else {
                                Object enumValue = Enum.valueOf(ro.getMethodModel().getEnumClass(),
                                                                enumStringValue.toUpperCase());
                                log.debug(" +++ invoking with enumValue = " + enumValue);
                                ro.getMethod().invoke(dto, enumValue);
                            }

                            break;

                        default:
                            throw new ModelNotSupportedException(
                                    "RdoDataType " + ro.getMethodModel().getDataType() +
                                    " is not supported. Have you added this and forgot to change this class?");
                    }
                }

                return dto;
            } catch (InvocationTargetException ite) {
                throw new RuntimeException(
                        "Method invocation exception while extracting object " + ite.getMessage(),
                        ite);
            } catch (SQLException sqle) {
                throw new RuntimeException(
                        "SQL Exception while extracting object ... " + sqle.getMessage(), sqle);
            }
        } catch (InstantiationException ie) {
            throw new RuntimeException(
                    "Instantiation exception while extracting object " + ie.getMessage(), ie);
        } catch (IllegalAccessException iae) {
            throw new RuntimeException(
                    "Instantiation exception while extracting object " + iae.getMessage(), iae);
        }
    }
}
