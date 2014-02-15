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

import java.lang.reflect.Method;

import org.apache.commons.logging.Log;

import org.cylog.rdo.bean.ClassModel;
import org.cylog.rdo.bean.MethodModel;
import org.cylog.rdo.bean.RdoDataType;
import org.cylog.rdo.exception.ModelNotSupportedException;
import org.cylog.rdo.logger.ClassLogger;

/**
 * TODO description for class ReflectionUtil
 *
 * @author Kostas Symeonidis
 */
public class ReflectionUtil {

    // ---- Static ------------------------------------------------------------

    public static Log log = new ClassLogger();

    // ---- Static Methods ----------------------------------------------------

    /**
     * @param c
     * @return
     */
    public static ClassModel createClassModel(Class c) {
        log.info("Retrieving class model for class [" + c.getCanonicalName() + "]");
        ClassModel classModel = new ClassModel(c);

        // using reflection iterate through the methods
        Method[] methods = c.getMethods();
        for (Method m : methods) {
            try {
                MethodModel mm = ReflectionUtil.validateAndReturnMethodModel(m);
                if (mm != null) {
                    log.debug(" adding method : " + mm);
                    classModel.addMethod(mm);
                }
            } catch (ModelNotSupportedException mnse) {
                log.warn(mnse.getMessage());
            }
        }

        return classModel;
    }

    /**
     * @param m
     * @return
     */
    private static MethodModel validateAndReturnMethodModel(Method m) {
        MethodModel methodModel = null;

        // if the method has a return type of VOID, name starts with set and
        // ONE single parameter, then it is good to check
        if ((Void.TYPE.equals(m.getReturnType())) &&
            m.getName().startsWith("set") &&
            m.getParameterTypes().length == 1) {
            Class param = m.getParameterTypes()[0];

            RdoDataType dt = null;
            boolean isPrimitive = false;

            // enum
            if (param.isEnum()) {
                dt = RdoDataType.ENUM;
            }
            // string
            else if (String.class.equals(param)) {
                dt = RdoDataType.STRING;
            }
            // dates
            else if (java.util.Date.class.equals(param)) {
                dt = RdoDataType.DATE;
            } else if (java.sql.Timestamp.class.equals(param)) {
                dt = RdoDataType.DATE;
            }
            // primitives
            else if (Boolean.class.equals(param)) // boolean
            {
                dt = RdoDataType.BOOLEAN;
            } else if (Boolean.TYPE.equals(param)) {
                dt = RdoDataType.BOOLEAN;
                isPrimitive = true;
            } else if (Character.class.equals(param)) // char
            {
                dt = RdoDataType.CHAR;
            } else if (Character.TYPE.equals(param)) {
                dt = RdoDataType.CHAR;
                isPrimitive = true;
            } else if (Integer.class.equals(param) || Short.class.equals(
                    param) || Byte.class.equals(param))  // int
            {
                dt = RdoDataType.INT;
            } else if (Integer.TYPE.equals(param) || Short.TYPE.equals(param) || Byte.TYPE.equals(
                    param)) {
                dt = RdoDataType.INT;
                isPrimitive = true;
            } else if (Long.class.equals(param))  // long
            {
                dt = RdoDataType.LONG;
            } else if (Long.TYPE.equals(param)) {
                dt = RdoDataType.LONG;
                isPrimitive = true;
            } else if (Float.class.equals(param))  // float
            {
                dt = RdoDataType.FLOAT;
            } else if (Float.TYPE.equals(param)) {
                dt = RdoDataType.FLOAT;
                isPrimitive = true;
            } else if (Double.class.equals(param))  // double
            {
                dt = RdoDataType.DOUBLE;
            } else if (Double.TYPE.equals(param)) {
                dt = RdoDataType.DOUBLE;
                isPrimitive = true;
            } else {
                throw new ModelNotSupportedException(
                        "RdoDataType " + param + " not supported for injector method " + m.getName());
            }

            // Return the enum class if this is an enum
            if (param.isEnum()) {
                methodModel = new MethodModel(m, RdoDataType.ENUM, param);
            } else {
                methodModel = new MethodModel(m, dt, isPrimitive);
            }
        }

        return methodModel;
    }
}
