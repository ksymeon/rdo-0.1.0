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

import org.cylog.rdo.exception.ModelNotSupportedException;

/**
 * Represents a database column and encapsulates its properties like
 * name, data type, precision and nullability.
 *
 * The ColumnModel is constructed with the database parameters and an
 * RdoDataType is assigned to it based on a private method in this class
 * that determines the suitable RdoDataType for a given {@link java.sql.Types}.
 *
 * @author Kostas Symeonidis
 */
public class ColumnModel {

    // ---- Member Variables --------------------------------------------------

    private final int columnIndex;
    private final String columnName;
    private final int sqlType;         // from java.sql.Types
    private final int columnLength;
    private final int decimalDigits;
    private final boolean nullable;
    private final RdoDataType dataType;

    // ---- Constructors ------------------------------------------------------

    public ColumnModel(int columnIndex, String columnName, int sqlType,
                       int columnLength, int decimalDigits, boolean nullable) {
        this.columnIndex = columnIndex;
        this.columnName = columnName;
        this.sqlType = sqlType;
        this.columnLength = columnLength;
        this.decimalDigits = decimalDigits;
        this.nullable = nullable;
        this.dataType = getDataTypeFromSqlType();
    }

    // ---- String representation ---------------------------------------------

    @Override
    public String toString() {
        return "ColumnModel{" + columnIndex +
               ". '" + columnName + '\'' +
               ": sqlType=" + sqlType +
               ", dataType=" + dataType +
               ", columnLength=" + columnLength +
               ", decimalDigits=" + decimalDigits +
               ", " + (nullable ? "NOT NULL" : "NULL") +
               '}';
    }

    // ---- Private methods ---------------------------------------------------

    /**
     * Returns a suitable RdoDataType for the internal SQL type or throws
     * a ModelNotSupportedException if the data type cannot be handled.
     *
     * @return the most suitable RdoDataType for the internal SQL type
     */
    private RdoDataType getDataTypeFromSqlType() {

        switch (sqlType) {
            case java.sql.Types.BIT:
            case java.sql.Types.TINYINT:
            case java.sql.Types.SMALLINT:
            case java.sql.Types.INTEGER:
                return RdoDataType.INT;

            case java.sql.Types.BIGINT:
                return RdoDataType.LONG;

            case java.sql.Types.FLOAT:
            case java.sql.Types.REAL:
                return RdoDataType.FLOAT;

            case java.sql.Types.DOUBLE:
                return RdoDataType.DOUBLE;

            case java.sql.Types.NUMERIC:
            case java.sql.Types.DECIMAL:
                return (this.decimalDigits == 0) ? RdoDataType.LONG : RdoDataType.DOUBLE;

            case java.sql.Types.CHAR:
            case java.sql.Types.VARCHAR:
            case java.sql.Types.CLOB:
                return RdoDataType.STRING;

            case java.sql.Types.DATE:
            case java.sql.Types.TIME:
            case java.sql.Types.TIMESTAMP:
                return RdoDataType.DATE;

            /* TODO: support some of these
            case java.sql.Types.BINARY:
            case java.sql.Types.VARBINARY:
            case java.sql.Types.LONGVARBINARY:
            case java.sql.Types.NULL:
            case java.sql.Types.OTHER:
            case java.sql.Types.JAVA_OBJECT:
            case java.sql.Types.DISTINCT:
            case java.sql.Types.STRUCT:
            case java.sql.Types.ARRAY:
            case java.sql.Types.BLOB:
            case java.sql.Types.REF:
            case java.sql.Types.DATALINK:
            case java.sql.Types.BOOLEAN:
            case java.sql.Types.ROWID:
            case java.sql.Types.NCHAR:
            case java.sql.Types.NVARCHAR:
            case java.sql.Types.LONGNVARCHAR:
            case java.sql.Types.NCLOB:
            case java.sql.Types.SQLXML:
            */

            default:
                throw new ModelNotSupportedException(
                        "Framework does not support sqlType " + this.sqlType +
                        ", column " + this.columnName);
        }
    }

    // ---- Bean Properties ---------------------------------------------------

    public int getColumnIndex() {
        return columnIndex;
    }

    public String getColumnName() {
        return columnName;
    }

    public int getSqlType() {
        return sqlType;
    }

    public RdoDataType getDataType() {
        return dataType;
    }

    public int getColumnLength() {
        return columnLength;
    }

    public int getDecimalDigits() {
        return decimalDigits;
    }

    public boolean isNullable() {
        return nullable;
    }
}
