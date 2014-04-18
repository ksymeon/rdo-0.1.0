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

import java.util.ArrayList;
import java.util.List;

/**
 * Convenience String utilities to convert field names to method names.
 *
 * @author Kostas Symeonidis
 */
public class FieldNameUtil {

    /**
     * Returns a string in CamelCase from the given string. All characters after
     * a space or an underscore are converted to UpperCase, those following an
     * existing UpperCase char are converted to LowerCase and the rest are left
     * as they are.
     *
     * This ensures that an existing "CamelCase" string remains as it is, i.e.
     * the following statement is ALWAYS TRUE:
     *
     * <code>camelCase(camelCase(x)) = camelCase(x)</code>
     *
     * This method will ensure that either "THIS_ID" and "this_id" are converted
     * to "ThisId".
     *
     * A NULL string returns NULL.
     */
    public static String camelCase(String s) {
        if (s == null) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        boolean afterSeparator = true;
        boolean afterUpperCase = false;
        for (char c : s.toCharArray()) {
            if (Character.isSpaceChar(c) || (c == '_')) {
                afterSeparator = true;
            } else { // not a separator char
                if (afterSeparator) {
                    sb.append(Character.toUpperCase(c));
                } else {
                    if (afterUpperCase) {
                        sb.append(Character.toLowerCase(c));
                    } else {
                        sb.append(c);
                    }
                }
                afterUpperCase = Character.isUpperCase(c);
                afterSeparator = false;
            }
        }

        return sb.toString();
    }

    /**
     * Convert the first character of a string to UPPER case.
     *
     * @param s the string to convert its first character
     *
     * @return a string with the first character uppercase
     */
    public static String firstUpper(String s) {
        if ((s == null) || (s.length() == 0)) {
            return s;
        }

        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }

    /**
     * Returns java method names based on a database field name. Database fields
     * should be written with underscores ("_") between words, where java fields
     * should be written in CamelCase.
     *
     * A database field that starts with "is_x" can be converted to a "setIsX" or
     * "setX" method.
     */
    public static String[] getPossibleSettersFromFieldName(String fieldName) {
        SingularStringList result = new SingularStringList();

        result.add("set" + firstUpper(camelCase(fieldName)));
        result.add("set" + camelCase(fieldName));

        // if this field starts with "is_XXX", check if there's method called "setXXX"
        if (fieldName.toLowerCase().startsWith("is_") && (fieldName.length() > 3)) {
            result.add("set" + camelCase(fieldName.substring(3)));
        } else if (fieldName.startsWith("is")
                   && (fieldName.length() > 2)
                   && Character.isUpperCase(fieldName.charAt(2))) {
            result.add("set" + camelCase(fieldName.substring(2)));
        }

        return result.toArray();
    }

    private static class SingularStringList {

        private final List<String> list;

        private SingularStringList() {
            this.list = new ArrayList<String>();
        }

        public void add(String s) {
            if (!list.contains(s)) {
                list.add(s);
            }
        }

        public String[] toArray() {
            return this.list.toArray(new String[1]);
        }
    }
}
