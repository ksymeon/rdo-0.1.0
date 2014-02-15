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
package org.cylog.rdo.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO description for class TableModel
 *
 * @author Kostas Symeonidis
 */
public class TableModel {
    // ---- Member Variables --------------------------------------------------

    String tableName;

    List<ColumnModel> columns;

    // ---- Constructors ------------------------------------------------------

    /**
     *
     */
    public TableModel(String tableName) {
        this.tableName = tableName;
        this.columns = new ArrayList<ColumnModel>();
    }

    // ---- String representation ---------------------------------------------

    @Override
    public String toString() {
        return "TableModel{'" + tableName + '\'' +
               ", columns=" + columns +
               '}';
    }

    // ---- Public methods ----------------------------------------------------

    public void addColumn(ColumnModel col) {
        this.columns.add(col);
    }

    /**
     * @return
     */
    public String getColumnsAsCommaList() {
        StringBuilder sb = new StringBuilder();

        int f = 0;
        for (ColumnModel c : columns) {
            if (f > 0) {
                sb.append(", ");
            }
            sb.append(c.getColumnName().toLowerCase());
            f++;
        }

        return sb.toString();
    }

    /**
     * Returns a SQL statement using the internal columns in their
     * physical order and adding a WHERE clause for the keys
     * that are passed in. The number of keys will determine
     */
    public String getSqlStatement(Object[] keys) {
        StringBuilder sb = new StringBuilder();

        sb.append("SELECT ");
        sb.append(getColumnsAsCommaList());
        sb.append(" FROM ");
        sb.append(this.tableName.toLowerCase());

        if ((keys != null) && (keys.length > 0)) {
            sb.append(" WHERE ");
            for (int i = 0; i < keys.length; i++) {
                if (i > 0) {
                    sb.append(" AND ");
                }
                sb.append(this.columns.get(i).getColumnName().toLowerCase());
                sb.append(" = ?");
            }
        }

        return sb.toString();
    }

    /**
     * @param whereClause
     * @return
     */
    public String getSqlStatementWithWhereClause(String whereClause) {
        StringBuilder sb = new StringBuilder();

        sb.append("SELECT ");
        sb.append(getColumnsAsCommaList());
        sb.append(" FROM ");
        sb.append(this.tableName.toLowerCase());

        if (whereClause != null) {
            sb.append(" WHERE ");
            sb.append(whereClause);
        }

        return sb.toString();
    }

    // ---- Private methods ---------------------------------------------------

    // ---- Bean Properties ---------------------------------------------------

    public String getTableName() {
        return tableName;
    }

    public List<ColumnModel> getColumns() {
        return columns;
    }
}
