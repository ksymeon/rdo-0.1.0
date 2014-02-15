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

import org.apache.commons.logging.Log;

import org.cylog.rdo.logger.ClassLogger;
import org.cylog.rdo.util.FieldNameUtil;

/**
 * This class is an implementation of an RDO mapper, a mapper between a
 * TableModel and a ClassModel.
 *
 * @author Kostas Symeonidis
 */
public class RdoMapper {
    // ---- Static ------------------------------------------------------------

    public static Log log = new ClassLogger();

    // ---- Constants ---------------------------------------------------------

    // ---- Member Variables --------------------------------------------------

    private ClassModel classModel;
    private TableModel tableModel;

    private List<RdoOperation> readOperations;

    // ---- Constructors ------------------------------------------------------

    /**
     * Constructs an RdoMapper using a ClassModel and TableModel. The instance
     * will initialise itself and construct an internal list of readOperations,
     * i.e. value assignments from the Table columns to the Class's fields.
     */
    public RdoMapper(ClassModel classModel, TableModel tableModel) {
        this.classModel = classModel;
        this.tableModel = tableModel;
        this.readOperations = new ArrayList<RdoOperation>();

        init();
    }

    // ---- Public methods ----------------------------------------------------

    public String getSqlStatement(Object[] keys) {
        return this.tableModel.getSqlStatement(keys);
    }

    public String getSqlStatementWithWhereClause(String whereClause) {
        return this.tableModel.getSqlStatementWithWhereClause(whereClause);
    }

    // ---- Private methods ---------------------------------------------------

    private void init() {
        log.info(
                " + Initialising RowMapper for class " + this.classModel + " and table " + this.tableModel);
        for (ColumnModel column : tableModel.getColumns()) {
            for (String setterName : FieldNameUtil.getPossibleSettersFromFieldName(
                    column.getColumnName())) {
                log.debug(" ? Check if setter with name " + setterName + " exists");

                MethodModel method = classModel.getMethodWithName(setterName);
                if (method != null) {
                    if (compatibleTypes(column.getDataType(), method.getDataType())) {
                        log.debug(
                                " + Setter with name " + setterName + " exists and types are compatible. Adding mapping operation");

                        RdoOperation ro = new RdoOperation(method, column);
                        this.readOperations.add(ro);

                        // move to the next column
                        break;
                    } else {
                        log.warn("Incompatible data types for column " + column.getColumnName() +
                                 ". Source is " + column.getDataType() + ", Target is " + method.getDataType());
                    }
                }
            }
        }
    }

    private boolean compatibleTypes(RdoDataType source, RdoDataType target) {
        if (source.equals(target)) {
            return true;
        }

        // if not identical then try to do the compatibility matrix
        boolean result = true; // TODO change this to false

        switch (target) {
            case BOOLEAN:

                break;
            case INT:
            case LONG:
            case FLOAT:
            case DOUBLE:
            case STRING:
            case CHAR:
            case DATE:

                break;
        }

        return result;
    }

    // ---- Bean Properties ---------------------------------------------------

    public ClassModel getClassModel() {
        return classModel;
    }

    public TableModel getTableModel() {
        return tableModel;
    }

    public List<RdoOperation> getReadOperations() {
        return readOperations;
    }
}