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

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.logging.Log;

import org.cylog.rdo.bean.ColumnModel;
import org.cylog.rdo.bean.TableModel;
import org.cylog.rdo.exception.ModelRetrievalException;
import org.cylog.rdo.logger.ClassLogger;

/**
 * Static utility to retrieve a list of TableModels from a database connection.
 *
 * @author Kostas Symeonidis
 */
public class DatabaseMetadataUtil {

    // ---- Static ------------------------------------------------------------

    public static Log log = new ClassLogger();

    // ---- Static Public methods ---------------------------------------------

    /**
     * Retrieves the {@link TableModel} for a given table from the {@link DatabaseMetaData}. The
     * name of the table must match the name as it's stored in the database. For more info, see
     * {@link DatabaseMetaData#getColumns(String, String, String, String)}.
     *
     * @param metaData  the database metadata object containing all the table models
     * @param tableName the name of the table as stored in the database.
     *
     * @return the {@link TableModel} of the given table if retrieval was successful
     *
     * @throws ModelRetrievalException if an SQL exception occurs while retrieving the model or if
     *                                 the table contains no columns
     */
    public static TableModel retrieveTableModel(DatabaseMetaData metaData, String tableName) {
        log.info("Retrieving table model for table [" + tableName + "]");
        ResultSet rsCols = null;
        try {
            TableModel tableModel = new TableModel(tableName);

            rsCols = metaData.getColumns(null, null, tableName, "%");
            while (rsCols.next()) {
                ColumnModel col = new ColumnModel(
                        rsCols.getInt("ORDINAL_POSITION"),
                        rsCols.getString("COLUMN_NAME"),
                        rsCols.getInt("DATA_TYPE"),
                        rsCols.getInt("COLUMN_SIZE"),
                        rsCols.getInt("DECIMAL_DIGITS"),
                        rsCols.getInt("NULLABLE") == 1);

                log.debug("Adding " + col);
                tableModel.addColumn(col);
            }
            rsCols.close();

            if (tableModel.getColumns().size() == 0) {
                throw new ModelRetrievalException(
                        "No columns found when retrieving table model for " + tableName +
                        ". Check if table name has been specified " +
                        "correctly or maybe if your database is case-sensitive");
            }

            return tableModel;
        } catch (SQLException e) {
            throw new ModelRetrievalException("Error retrieving table model for " + tableName, e);
        } finally {
            SqlResourceUtil.closeResource(rsCols);
        }
    }
}
