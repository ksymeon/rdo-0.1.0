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
package org.cylog.rdo.test.dto;

/**
 * Class NumbersDataType.
 *
 * @author Kostas Symeonidis
 */
public class NumberData {

    // ---- Member Variables --------------------------------------------------

    private int integerValue;
    private Integer integerValueNullable;
    private long longValue;
    private Long longValueNullable;
    private float floatValue;
    private Float floatValueNullable;
    private double doubleValue;
    private Double doubleValueNullable;

    // ---- Constructors ------------------------------------------------------

    public NumberData() {
        // empty
    }

    // ---- Public methods ----------------------------------------------------

    @Override
    public String toString() {
        return "NumberData{" +
               "integerValue=" + integerValue +
               ", integerValueNullable=" + integerValueNullable +
               ", longValue=" + longValue +
               ", longValueNullable=" + longValueNullable +
               ", floatValue=" + floatValue +
               ", floatValueNullable=" + floatValueNullable +
               ", doubleValue=" + doubleValue +
               ", doubleValueNullable=" + doubleValueNullable +
               '}';
    }

    // ---- Bean Properties ---------------------------------------------------

    public int getIntegerValue() {
        return integerValue;
    }

    public void setIntegerValue(int integerValue) {
        this.integerValue = integerValue;
    }

    public Integer getIntegerValueNullable() {
        return integerValueNullable;
    }

    public void setIntegerValueNullable(Integer integerValueNullable) {
        this.integerValueNullable = integerValueNullable;
    }

    public long getLongValue() {
        return longValue;
    }

    public void setLongValue(long longValue) {
        this.longValue = longValue;
    }

    public Long getLongValueNullable() {
        return longValueNullable;
    }

    public void setLongValueNullable(Long longValueNullable) {
        this.longValueNullable = longValueNullable;
    }

    public float getFloatValue() {
        return floatValue;
    }

    public void setFloatValue(float floatValue) {
        this.floatValue = floatValue;
    }

    public Float getFloatValueNullable() {
        return floatValueNullable;
    }

    public void setFloatValueNullable(Float floatValueNullable) {
        this.floatValueNullable = floatValueNullable;
    }

    public double getDoubleValue() {
        return doubleValue;
    }

    public void setDoubleValue(double doubleValue) {
        this.doubleValue = doubleValue;
    }

    public Double getDoubleValueNullable() {
        return doubleValueNullable;
    }

    public void setDoubleValueNullable(Double doubleValueNullable) {
        this.doubleValueNullable = doubleValueNullable;
    }
}
