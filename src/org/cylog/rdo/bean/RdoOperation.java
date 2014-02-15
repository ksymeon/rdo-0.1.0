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

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * Represents an Rdo assignment operation from a database column to a java
 * setter method. Encapsulates a MethodModel and a ColumnModel
 *
 * @author Kostas Symeonidis
 */
public class RdoOperation implements Serializable {
    // ---- Static ------------------------------------------------------------

    // ---- Constants ---------------------------------------------------------

    // ---- Member Variables --------------------------------------------------

    MethodModel methodModel;
    ColumnModel columnModel;

    // ---- Constructors ------------------------------------------------------

    public RdoOperation(MethodModel methodModel, ColumnModel columnModel) {
        this.methodModel = methodModel;
        this.columnModel = columnModel;
    }

    // ---- Public methods ----------------------------------------------------

    @Override
    public String toString() {
        return "RdoOperation{" +
               "method = " + methodModel.getMethodName() +
               ", column = " + columnModel.getColumnName() +
               '}';
    }

    // ---- Private methods ---------------------------------------------------

    // ---- Public Convenience Methods ----------------------------------------

    public String getMethodName() {
        return this.methodModel.getMethodName();
    }

    public Method getMethod() {
        return this.methodModel.getMethod();
    }

    public String getColumnName() {
        return this.columnModel.getColumnName();
    }

    // ---- Bean Properties (Read-Only) ---------------------------------------

    public MethodModel getMethodModel() {
        return methodModel;
    }

    public ColumnModel getColumnModel() {
        return columnModel;
    }
}
