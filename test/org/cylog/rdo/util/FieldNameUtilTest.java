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

import static junit.framework.TestCase.assertNull;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

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
        assertEquals(null, FieldNameUtil.camelCase(null));
        assertEquals("", FieldNameUtil.camelCase(""));
        assertEquals("A", FieldNameUtil.camelCase("a"));
        assertEquals("Ab", FieldNameUtil.camelCase("ab"));
        assertEquals("Ab", FieldNameUtil.camelCase("AB"));
        assertEquals("ABC", FieldNameUtil.camelCase(" a b c"));
        assertEquals("AaaBbbCcc", FieldNameUtil.camelCase("aaa_bbb_ccc"));
        assertEquals("AaaBbbCcc", FieldNameUtil.camelCase("aaa bbb ccc"));
        assertEquals("IsDeleted", FieldNameUtil.camelCase("is_deleted"));

        assertEquals("Customerid", FieldNameUtil.camelCase("customerid"));

        assertEquals("CustomerId", FieldNameUtil.camelCase("CustomerId"));
        assertEquals("AQuickBrownFoxJumpsOverTheLazyDog",
                     FieldNameUtil.camelCase("aQuickBrownFox_Jumps_Over_the_lazy_dog"));

        assertEquals("AbcdDef", FieldNameUtil.camelCase("ABCD_DEF"));
        assertEquals("AbcdDeFgh", FieldNameUtil.camelCase("ABCD_DeFgh"));
    }

    @Test
    public void camelCaseOfCamelCase() throws Exception {
        System.out.println("Testing testCamelCaseOfCamelCase...");
        String s = "ABCD_DeFgh";
        assertEquals(FieldNameUtil.camelCase(s),
                     FieldNameUtil.camelCase(FieldNameUtil.camelCase(s)));
        s = "is_deleted";
        assertEquals(FieldNameUtil.camelCase(s),
                     FieldNameUtil.camelCase(FieldNameUtil.camelCase(s)));

        s = "CustomerId";
        assertEquals(FieldNameUtil.camelCase(s),
                     FieldNameUtil.camelCase(FieldNameUtil.camelCase(s)));

        s = "productId";
        assertEquals(FieldNameUtil.camelCase(s),
                     FieldNameUtil.camelCase(FieldNameUtil.camelCase(s)));
    }

    @Test
    public void firstUpper() {
        assertNull(FieldNameUtil.firstUpper(null));
        assertThat(FieldNameUtil.firstUpper(""), is(""));
        assertThat(FieldNameUtil.firstUpper("a"), is("A"));
        assertThat(FieldNameUtil.firstUpper("ab"), is("Ab"));
        assertThat(FieldNameUtil.firstUpper("Ab"), is("Ab"));
        assertThat(FieldNameUtil.firstUpper("ABC"), is("ABC"));
        assertThat(FieldNameUtil.firstUpper("aBC"), is("ABC"));

        assertThat(FieldNameUtil.firstUpper("AB_C"), is("AB_C"));
    }

    @Test
    public void getPossibleSettersFromFieldName() throws Exception {
        System.out.println("Testing testGetPossibleSettersFromFieldName...");
        String[] s = FieldNameUtil.getPossibleSettersFromFieldName("is_deleted");
        assertEquals(2, s.length);
        assertEquals("setIsDeleted", s[0]);
        assertEquals("setDeleted", s[1]);

        s = FieldNameUtil.getPossibleSettersFromFieldName("isActive");
        assertEquals(2, s.length);
        assertEquals("setIsActive", s[0]);
        assertEquals("setActive", s[1]);

        s = FieldNameUtil.getPossibleSettersFromFieldName("game_product_id");
        assertEquals(1, s.length);
        assertEquals("setGameProductId", s[0]);

        s = FieldNameUtil.getPossibleSettersFromFieldName("customerId");
        assertEquals(1, s.length);
        assertEquals("setCustomerId", s[0]);
    }
}
