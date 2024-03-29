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

import java.util.HashMap;
import java.util.Map;

/**
 * @author Kostas Symeonidis
 */
public class ClassModel {

    // ---- Member Variables --------------------------------------------------

    Class internalClass;

    String className;

    Map<String, MethodModel> methods;

    // ---- Constructors ------------------------------------------------------

    public ClassModel(Class c) {
        this.internalClass = c;
        this.className = c.getCanonicalName();
        this.methods = new HashMap<String, MethodModel>();
    }

    // ---- String representation ---------------------------------------------

    @Override
    public String toString() {
        return "ClassModel{'" + className + '\'' +
               ", methods=" + methods +
               '}';
    }

    // ---- Public methods ----------------------------------------------------

    /**
     * Adds a JavaMethodModel in the internal List
     */
    public void addMethod(MethodModel method) {
        this.methods.put(method.getMethodName(), method);
    }

    /**
     * TODO doc
     */
    public MethodModel getMethodWithName(String methodName) {
        return this.methods.get(methodName);
    }

    public Object createNewInstance() throws InstantiationException, IllegalAccessException {
        return this.internalClass.newInstance();
    }

    // ---- Bean Properties ---------------------------------------------------

    public String getClassName() {
        return className;
    }
}
