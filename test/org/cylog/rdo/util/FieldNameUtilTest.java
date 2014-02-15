/*
 * Copyright (c) 2013 Kostas Symeonidis
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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;

/**
 * FieldNameUtil Tester.
 *
 * @author Kostas Symeonidis
 */
@RunWith(JUnit4.class)
public class FieldNameUtilTest {

    @Test
    public void getCamelCase() throws Exception {
        System.out.println("Testing testGetCamelCase...");
        assertEquals(null, FieldNameUtil.getCamelCase(null));
        assertEquals("", FieldNameUtil.getCamelCase(""));
        assertEquals("A", FieldNameUtil.getCamelCase("a"));
        assertEquals("Ab", FieldNameUtil.getCamelCase("ab"));
        assertEquals("Ab", FieldNameUtil.getCamelCase("AB"));
        assertEquals("ABC", FieldNameUtil.getCamelCase(" a b c"));
        assertEquals("AaaBbbCcc", FieldNameUtil.getCamelCase("aaa_bbb_ccc"));
        assertEquals("AaaBbbCcc", FieldNameUtil.getCamelCase("aaa bbb ccc"));
        assertEquals("IsDeleted", FieldNameUtil.getCamelCase("is_deleted"));

        assertEquals("Customerid", FieldNameUtil.getCamelCase("customerid"));

        assertEquals("CustomerId", FieldNameUtil.getCamelCase("CustomerId"));
        assertEquals("AQuickBrownFoxJumpsOverTheLazyDog",
                     FieldNameUtil.getCamelCase("aQuickBrownFox_Jumps_Over_the_lazy_dog"));

        assertEquals("AbcdDef", FieldNameUtil.getCamelCase("ABCD_DEF"));
        assertEquals("AbcdDeFgh", FieldNameUtil.getCamelCase("ABCD_DeFgh"));
    }

    @Test
    public void camelCaseOfCamelCase() throws Exception {
        System.out.println("Testing testCamelCaseOfCamelCase...");
        String s = "ABCD_DeFgh";
        assertEquals(FieldNameUtil.getCamelCase(s),
                     FieldNameUtil.getCamelCase(FieldNameUtil.getCamelCase(s)));
        s = "is_deleted";
        assertEquals(FieldNameUtil.getCamelCase(s),
                     FieldNameUtil.getCamelCase(FieldNameUtil.getCamelCase(s)));
    }

    @Test
    public void getPossibleSettersFromFieldName() throws Exception {
        System.out.println("Testing testGetPossibleSettersFromFieldName...");
        String[] s = FieldNameUtil.getPossibleSettersFromFieldName("is_deleted");
        assertEquals(2, s.length);
        assertEquals("setIsDeleted", s[0]);
        assertEquals("setDeleted", s[1]);

        s = FieldNameUtil.getPossibleSettersFromFieldName("game_product_id");
        assertEquals(1, s.length);
        assertEquals("setGameProductId", s[0]);

        s = FieldNameUtil.getPossibleSettersFromFieldName("customerId");
        assertEquals(1, s.length);
        assertEquals("setCustomerId", s[0]);
    }
}
