/*
 * Copyright 2014-2016 Victor Osolovskiy, Sergey Navrotskiy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ru.vood.freemarker.ext.sql;

import org.springframework.util.Assert;

import java.lang.reflect.Field;
import java.sql.Types;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


/**
 * A helper class for working with SQL types.
 */
public class TypeHelper {
    // Map of SQL type names to their int values.
    private static final Map<String, Object> encoder = new HashMap<>();

    static {
        Field[] fields = Types.class.getFields();
        for (Field field : fields) {
            try {
                String name = field.getName();
                Object value = field.get(null);
                if (value instanceof Integer) {
                    encoder.put(name, value);
                }
            } catch (IllegalAccessException e) {
                throw new SqlFtlException("Unable to create map of SQL type names to int values", e);
            }
        }
    }

    private TypeHelper() {
    }

    public static Integer getIntValue(String typeName) {
        Integer ret = (Integer) encoder.get(typeName);
        if (ret == null) {
            ret = extractConstant(typeName);
            encoder.put(typeName, ret);
        }
        return ret;
    }

    private static Integer extractConstant(String typeName) {
        int lastDotPos = typeName.lastIndexOf('.');
        Assert.isTrue(
                lastDotPos >= 1,
                () ->
                        "Type constant is not member of " + Types.class.getName()
                                + " and its name is not fully specified: " + typeName
        );
        String className = typeName.substring(0, lastDotPos);
        String fieldName = typeName.substring(lastDotPos + 1);
        Integer val = reflectionOp(className, fieldName);
        if (val == null) throw new NullPointerException(typeName);
        return val;
    }

    private static Integer reflectionOp(String className, String fieldName) {
        try {
            Class cls = Class.forName(className);
            return (Integer) cls.getField(fieldName).get(null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static java.util.Date toSQLDate(java.util.Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        long millis = cal.getTimeInMillis();
        boolean hasTimePart =
                cal.get(Calendar.HOUR_OF_DAY) + cal.get(Calendar.MINUTE)
                        + cal.get(Calendar.SECOND) + cal.get(Calendar.MILLISECOND) != 0;
        return hasTimePart ? new java.sql.Timestamp(millis) : new java.sql.Date(millis);
    }
}
