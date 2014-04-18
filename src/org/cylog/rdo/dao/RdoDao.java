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

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;

import org.cylog.rdo.bean.ClassModel;
import org.cylog.rdo.bean.RdoMapper;
import org.cylog.rdo.bean.TableModel;
import org.cylog.rdo.dao.extractor.RdoObjectExtractor;
import org.cylog.rdo.exception.ModelNotFoundException;
import org.cylog.rdo.exception.ModelRetrievalException;
import org.cylog.rdo.logger.ClassLogger;
import org.cylog.rdo.util.DatabaseMetadataUtil;
import org.cylog.rdo.util.ReflectionUtil;
import org.cylog.rdo.util.SqlResourceUtil;

/**
 * An implementation of a RdoRetriever.
 *
 * @author Kostas Symeonidis
 */
public class RdoDao implements RdoRetriever {
    // ---- Static ------------------------------------------------------------

    public static Log log = new ClassLogger();

    // ---- Constants ---------------------------------------------------------

    // ---- Member Variables --------------------------------------------------

    /**
     * The internal dataSource for this DAO object
     */
    private DataSource dataSource;

    /**
     * The internal HashMap of tableModels for this DAO's dataSource.
     * Initialised on the first attempt to retrieve a TableModel
     */
    private final Map<String, TableModel> tableModels;

    /**
     * The internal HashMap of classModels, gradually populated with
     * ClassModels as they are used
     */
    private final Map<String, ClassModel> classModels;

    /**
     * The internal HashMap of
     */
    private final Map<String, RdoMapper> mappers;

    // ---- Constructors ------------------------------------------------------

    /**
     * The default no-arguments constructor for this class
     */
    public RdoDao() {
        log.info("Constructing RdoDao...");
        this.tableModels = new HashMap<String, TableModel>();
        this.classModels = new HashMap<String, ClassModel>();
        this.mappers = new HashMap<String, RdoMapper>();
    }

    /**
     * Constructs an instance of this class using the given dataSource
     */
    public RdoDao(DataSource dataSource) {
        this();
        log.info("Constructing RdoDao with dataSource : " + dataSource.toString() + "...");
        setDataSource(dataSource);
    }

    // ---- Bean Properties ---------------------------------------------------

    /**
     * Sets the dataSource for this Dao
     */
    public void setDataSource(DataSource dataSource) {
        log.info("Setting dataSource to RdoDao : " + dataSource);
        this.dataSource = dataSource;
    }

    // ---- Interface implementation ------------------------------------------

    /**
     * @param c
     * @param tableName
     * @param keys
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> T getDto(Class<T> c, String tableName, Object... keys) {
        RdoMapper m = getRdoMapper(c, tableName);
        RdoObjectExtractor r = new RdoObjectExtractor(dataSource, m);

        return (T) r.extractObject(keys);
    }

    /**
     * @param c
     * @param tableName
     * @param whereClause
     * @param params
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> T getDtoWhere(Class<T> c, String tableName, String whereClause, Object... params) {
        RdoMapper m = getRdoMapper(c, tableName);
        RdoObjectExtractor r = new RdoObjectExtractor(dataSource, m);

        return (T) r.extractObjectWhere(whereClause, params);
    }

    /**
     * @param c
     * @param tableName
     * @param sql
     * @param params
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> T getDtoFromSql(Class<T> c, String tableName, String sql, Object... params) {
        RdoMapper m = getRdoMapper(c, tableName);
        RdoObjectExtractor r = new RdoObjectExtractor(dataSource, m);

        return (T) r.extractObjectFromSql(sql, params);
    }

    /**
     * @param dto
     * @param tableName
     * @param keys
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> T mergeDto(T dto, String tableName, Object... keys) {
        RdoMapper m = getRdoMapper(dto.getClass(), tableName);
        RdoObjectExtractor r = new RdoObjectExtractor(dataSource, m);

        return (T) r.extractAndMergeObject(dto, keys);
    }

    /**
     * @param dto
     * @param tableName
     * @param whereClause
     * @param params
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> T mergeDtoWhere(T dto, String tableName, String whereClause, Object... params) {
        RdoMapper m = getRdoMapper(dto.getClass(), tableName);
        RdoObjectExtractor r = new RdoObjectExtractor(dataSource, m);

        return (T) r.extractAndMergeObjectWhere(dto, whereClause, params);
    }

    /**
     * @param dto
     * @param tableName
     * @param sql
     * @param params
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> T mergeDtoFromSql(T dto, String tableName, String sql, Object... params) {
        RdoMapper m = getRdoMapper(dto.getClass(), tableName);
        RdoObjectExtractor r = new RdoObjectExtractor(dataSource, m);

        return (T) r.extractAndMergeObjectFromSql(dto, sql, params);
    }

    /**
     * @param c
     * @param tableName
     * @param whereClause
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> List<T> getDtoList(Class<T> c, String tableName, String whereClause,
                                  Object... params) {
        RdoMapper m = getRdoMapper(c, tableName);
        RdoObjectExtractor r = new RdoObjectExtractor(dataSource, m);

        return r.extractList(whereClause, params);
    }

    /**
     * @param c
     * @param tableName
     * @param sql
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> List<T> getDtoListFromSql(Class<T> c, String tableName, String sql,
                                         Object... params) {
        RdoMapper m = getRdoMapper(c, tableName);
        RdoObjectExtractor r = new RdoObjectExtractor(dataSource, m);

        return r.extractListFromSql(sql, params);
    }

    // ---- Private methods ---------------------------------------------------

    /**
     * @param c
     * @param tableName
     * @return
     */
    private RdoMapper getRdoMapper(Class c, String tableName) {
        String key = c.getCanonicalName() + ":" + tableName.toUpperCase();
        log.info("+ Acquiring RdoMapper from cache with key \"" + key + "\"...");

        RdoMapper m = this.mappers.get(key);

        if (m == null) {
            log.info("! RdoMapper not found in cache, constructing from Class and TableModel...");

            ClassModel classModel = getClassModel(c);

            if (classModel == null) {
                throw new ModelNotFoundException(
                        "Cannot find model for class " + c.getCanonicalName());
            }

            TableModel tableModel = getTableModel(tableName);
            if (tableModel == null) {
                throw new ModelNotFoundException("Cannot find model for table " + tableName);
            }

            // instantiate a new RdoMapper to get the mathing done
            m = new RdoMapper(classModel, tableModel);

            // adding RdoMapper in cache
            this.mappers.put(key, m);
        }

        return m;
    }

    private TableModel getTableModel(String tableName) {
        TableModel model = this.tableModels.get(tableName);
        if (model == null) {
            Connection conn = SqlResourceUtil.getConnection(dataSource);
            try {
                model = DatabaseMetadataUtil.retrieveTableModel(conn.getMetaData(), tableName);

                if (model != null) {
                    this.tableModels.put(tableName, model);
                }
            } catch (SQLException sqle) {
                throw new ModelRetrievalException("Cannot retrieve data model", sqle);
            } finally {
                SqlResourceUtil.closeResource(conn);
            }

            /*
            log.info("+ Initializing table models from dataSource...");
            List<TableModel> tabs = getAllTableModels();

            // add the table models in the cache now
            Map<String, TableModel> map = new HashMap<String, TableModel>();
            for (TableModel tm : tabs) {
                map.put(tm.getTableName(), tm);
            }

            tableModels = map;
            */
        }

        return model;
    }

    /**
     * @param c
     * @return
     */
    private ClassModel getClassModel(Class c) {
        ClassModel classModel = this.classModels.get(c.getCanonicalName());

        if (classModel == null) {
            log.info("+ Creating class model for class " + c.getCanonicalName() + "...");
            classModel = ReflectionUtil.createClassModel(c);

            // add the class model in the cache
            this.classModels.put(c.getCanonicalName(), classModel);
        }

        return classModel;
    }

    /*
     * @return
     *
    private List<TableModel> getAllTableModels() {
        Connection conn = SqlResourceUtil.getConnection(dataSource);
        try {
            return DatabaseMetadataUtil.retrieveAllTableModels(conn.getMetaData());
        } catch (SQLException sqle) {
            throw new ModelRetrievalException("Cannot retrieve data model", sqle);
        } finally {
            SqlResourceUtil.closeResource(conn);
        }
    }
    */
}
