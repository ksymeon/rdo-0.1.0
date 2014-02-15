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
package org.cylog.rdo.dao;

import java.util.List;

import javax.sql.DataSource;

/**
 * Interface of RapidDataObject retriever classes. In the framework only the RdoDao
 * class implements this interface. Your code may implement this interface and
 * proxy the methods of the RdoDao by using an internal RdoDao object.
 *
 * @author Kostas Symeonidis
 */
public interface RdoRetriever {

    // ---- Resources ---------------------------------------------------------      

    /**
     * Sets the internal dataSource for this RdoRetriever
     */
    public void setDataSource(DataSource dataSource);

    // ---- Retrieval Operations ----------------------------------------------

    /**
     * Returns a newly instantiated object of class "c", taken and mapped from
     * table "tableName" using the "keys" arguments as primary keys.
     *
     * Internally this method will create a SQL statement like this:
     *
     * "SELECT field1, field2, ... FROM tableName WHERE field1 = ?, ..."
     *
     * The selected fields will always be ALL fields from the tableName as
     * retrieved from the Database schema.
     *
     * The WHERE clause fields will be the first "n", where "n" is the length
     * of the passed Object... array.
     *
     * @return A fully populated object from the table, NULL if the select
     *         does not return any rows
     */
    public <T> T getDto(Class<T> c, String tableName, Object... keys);

    public <T> T getDtoWhere(Class<T> c, String tableName, String whereClause, Object... params);

    public <T> T getDtoFromSql(Class<T> c, String tableName, String sql, Object... params);

    /**
     * Populates and returns the given object, after reading and mapping data from
     * the table "tableName" using the "keys" arguments as primary keys.
     *
     * Internally this method will create a SQL statement like this:
     *
     * "SELECT field1, field2, ... FROM tableName WHERE field1 = ?, ..."
     *
     * The selected fields will always be ALL fields from the tableName as
     * retrieved from the Database schema.
     *
     * The WHERE clause fields will be the first "n", where "n" is the length
     * of the passed Object... array.
     *
     * @return the given object populated with additional data from the table or
     *         the object intact if the select did not retrieve any rows
     */
    public <T> T mergeDto(T dto, String tableName, Object... keys);

    public <T> T mergeDtoWhere(T dto, String tableName, String whereClause, Object... params);

    public <T> T mergeDtoFromSql(T dto, String tableName, String sql, Object... params);

    /**
     * Returns a list of Class-"c" objects taken from table "tableName" and mapped
     * on to the new Class instances. This method will create a SQL statement like:
     *
     * "SELECT field1, field2, ... FROM tableName WHERE whereClause"
     *
     * If you have any SQL argument placeholders (?) in the whereClause you will need
     * to pass as many parameters in the params Object array.
     */
    public <T> List<T> getDtoList(Class<T> c, String tableName, String whereClause,
                                  Object... params);

    public <T> List<T> getDtoListFromSql(Class<T> c, String tableName, String sql,
                                         Object... params);
}
