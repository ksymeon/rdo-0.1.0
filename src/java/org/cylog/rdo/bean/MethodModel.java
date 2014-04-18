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

import java.lang.reflect.Method;

/**
 * @author Kostas Symeonidis
 */
public class MethodModel {

    // ---- Member Variables --------------------------------------------------

    private final Method method;
    private final RdoDataType dataType;
    private final Class enumClass;
    private final boolean isPrimitive;

    // ---- Constructors ------------------------------------------------------

    public MethodModel(Method method, RdoDataType dataType, boolean isPrimitive) {
        this.method = method;
        this.dataType = dataType;
        this.enumClass = null;
        this.isPrimitive = isPrimitive;
    }

    public MethodModel(Method method, RdoDataType dataType, Class enumClass) {
        this.method = method;
        this.dataType = dataType;
        this.enumClass = enumClass;
        this.isPrimitive = false;
    }

    // ---- String representation ---------------------------------------------

    @Override
    public String toString() {
        return "MethodModel{'" + method.getName() + '\'' +
               ", dataType=" + dataType +
               (enumClass == null ?
                ", isPrimitive=" + isPrimitive :
                ", enumClass=" + enumClass.getCanonicalName())
               + "}";
    }

    // ---- Public methods ----------------------------------------------------

    public String getMethodName() {
        return this.method.getName();
    }

    // ---- Bean Properties ---------------------------------------------------

    public Method getMethod() {
        return this.method;
    }

    public RdoDataType getDataType() {
        return dataType;
    }

    public Class getEnumClass() {
        return enumClass;
    }

    public boolean isPrimitive() {
        return isPrimitive;
    }
}
